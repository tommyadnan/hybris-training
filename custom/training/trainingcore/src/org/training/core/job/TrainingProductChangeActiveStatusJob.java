package org.training.core.job;

import de.hybris.platform.tx.Transaction;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.model.ModelService;
import org.training.core.enums.ActiveStatus;
import org.training.core.model.TrainingVariantProductModel;
import org.training.core.service.TrainingProductService;

import javax.annotation.Resource;
import java.util.List;
import org.apache.log4j.Logger;

public class TrainingProductChangeActiveStatusJob extends AbstractJobPerformable<CronJobModel> {
    private static final Logger LOG = Logger.getLogger(TrainingProductChangeActiveStatusJob.class);
    private TrainingProductService trainingProductService;
    private ModelService modelService;

    public TrainingProductService getTrainingProductService() {
        return trainingProductService;
    }

    public void setTrainingProductService(TrainingProductService trainingProductService) {
        this.trainingProductService = trainingProductService;
    }

    public ModelService getModelService() {
        return modelService;
    }

    @Override
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    @Resource
    private ProductService productService;

    @Resource
    private CatalogVersionService catalogVersionService;

    @Override
    public PerformResult perform(CronJobModel cronJobModel) {
        LOG.info("Cronjob change active status is starting...");
        List<ProductModel> productModels = trainingProductService.getAllProductModels();

        for (ProductModel productModel : productModels) {
            if (productModel instanceof TrainingVariantProductModel) {
                final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("trainingProductCatalog", "Staged");
                ProductModel product = productService.getProductForCode(catalogVersion, productModel.getCode());
                if (null != product) {
                    Transaction tx = null;
                    try {
                        tx = Transaction.current();
                        tx.begin();
                        ((TrainingVariantProductModel) product).setActiveStatus(ActiveStatus.INWAREHOUSE);
                        getModelService().save(product);
                        tx.commit();
                    } catch (final ModelRemovalException e) {
                        if (null != tx) {
                            tx.rollback();
                        }
                        LOG.error("Could not update the product -->" + e);
                    }
                }

            }
        }
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }
}


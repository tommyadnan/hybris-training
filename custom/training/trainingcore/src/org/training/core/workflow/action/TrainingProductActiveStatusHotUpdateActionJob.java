package org.training.core.workflow.action;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.solrfacetsearch.daos.SolrFacetSearchConfigDao;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;
import de.hybris.platform.solrfacetsearch.indexer.cron.SolrIndexerHotUpdateJob;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.indexer.cron.SolrIndexerHotUpdateCronJobModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.training.core.service.TrainingProductService;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TrainingProductActiveStatusHotUpdateActionJob extends AbstractTrainingProductActiveStatusApprovalActionJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingProductActiveStatusHotUpdateActionJob.class);
    @Resource(name = "catalogVersionService")
    private CatalogVersionService catalogVersionService;
    @Resource(name = "solrIndexerHotUpdateJob")
    private SolrIndexerHotUpdateJob solrIndexerHotUpdateJob;
    @Resource(name = "baseStoreService")
    private BaseStoreService baseStoreService;
    @Resource(name = "solrFacetSearchConfigDao")
    private SolrFacetSearchConfigDao solrFacetSearchConfigDao;
    @Resource(name = "trainingProductService")
    private TrainingProductService trainingProductService;
    @Resource(name = "searchRestrictionService")
    private SearchRestrictionService searchRestrictionService;

    @Override
    public WorkflowDecisionModel perform(final WorkflowActionModel action) {
        LOGGER.debug("Entered into Training Product Active Status Hot Update Action Job");
        final ProductModel productModel = getProductFromAttachment(action);
        if (callSyncAction(productModel)) {
            LOGGER.info("Sync Succeeded!");
        }
        if (!callIndexingAction(productModel)) {
            return getDecisionForActionName(action, RETRY);
        }
        return getDecisionForActionName(action, FINISH);
    }

    private boolean callIndexingAction(final ProductModel productModel) {
        try {
            String catalogName = "trainingProductCatalog";
            final CatalogVersionModel onlineCatalogVersion = catalogVersionService
                    .getCatalogVersion(catalogName, "Online");
            final List<ProductModel> productForCatalogVersionOnline = trainingProductService
                    .getProductForCatalogVersion(productModel.getCode(), onlineCatalogVersion);
            solrIndexerHotUpdateJob.performIndexingJob(hotUpdateforProduct(
                    productForCatalogVersionOnline.get(0), IndexerOperationValues.UPDATE, "Product"));
        } catch (Exception e) {
            LOGGER.debug("Entered into Training Product Active Status Hot Update Action Job" + e);
            return false;
        }
        return true;
    }

    public SolrIndexerHotUpdateCronJobModel hotUpdateforProduct(final ProductModel product,
                                                                final IndexerOperationValues actionValue, final String itemTypeName) {
        final SolrIndexerHotUpdateCronJobModel indexerCronJob = new SolrIndexerHotUpdateCronJobModel();
        final List<ItemModel> productCollection = Collections.singletonList(product);
        indexerCronJob.setItems(productCollection);
        indexerCronJob.setIndexerOperation(actionValue);
        final SolrFacetSearchConfigModel solrFacetSearchConfigModel = solrFacetSearchConfigDao
                .findFacetSearchConfigByName("trainingIndex");
        indexerCronJob.setFacetSearchConfig(solrFacetSearchConfigModel);
        //itemTypeName , example: Product
        indexerCronJob.setIndexTypeName(itemTypeName);
        final Set<LanguageModel> languageModelSet = getLanguagesFromBaseStores();
        indexerCronJob.setSessionLanguage(languageModelSet.iterator().next());
        return indexerCronJob;
    }

    private Set<LanguageModel> getLanguagesFromBaseStores() {
        final Set<LanguageModel> languageModelSet = new HashSet<>();
        baseStoreService.getAllBaseStores().forEach(baseStoreModel -> languageModelSet.addAll(baseStoreModel.getLanguages()));
        return languageModelSet;
    }


    private boolean callSyncAction(final ProductModel productModel) {
        try {
            trainingProductService.syncAction(productModel);
        } catch (Exception e) {
            LOGGER.debug("Entered into C2C workflow Sync action job" + e);
            return false;
        } finally {
            searchRestrictionService.enableSearchRestrictions();
        }
        return true;
    }
}

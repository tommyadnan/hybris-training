package org.training.core.service.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.catalog.synchronization.CatalogSynchronizationService;
import de.hybris.platform.catalog.synchronization.SyncConfig;
import de.hybris.platform.catalog.synchronization.SyncResult;
import de.hybris.platform.catalog.synchronization.SynchronizationStatusService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.product.impl.DefaultProductService;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.training.core.dao.TrainingProductDao;
import org.training.core.service.TrainingProductService;

import javax.annotation.Resource;
import java.util.*;

public class DefaultTrainingProductService extends DefaultProductService implements TrainingProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTrainingProductService.class);

    @Resource(name = "defaultTrainingProductDao")
    private TrainingProductDao trainingProductDao;

    @Resource(name = "trainingGenericProductDao")
    private GenericDao<ProductModel> trainingGenericProductDao;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "catalogVersionService")
    private CatalogVersionService catalogVersionService;

    @Resource(name = "searchRestrictionService")
    private SearchRestrictionService searchRestrictionService;

    @Resource
    private CatalogSynchronizationService catalogSynchronizationService;

    @Resource
    private SynchronizationStatusService synchronizationStatusService;

    @Override
    public List<ProductModel> getProductForCatalogVersion(final String productCode, CatalogVersionModel catalogVersion) {
        return sessionService.executeInLocalView(new SessionExecutionBody() {
            @Override
            public Object execute() {
                final Map<String, Object> queryParams = new HashMap<>();
                queryParams.put(ProductModel.CODE, productCode);
                queryParams.put(ProductModel.CATALOGVERSION, catalogVersion);
                return trainingGenericProductDao.find(queryParams);
            }
        }, userService.getAdminUser());
    }

    @Override
    public List<ProductModel> getAllProductModels() {
        return trainingProductDao.getAllProductModels();
    }

    @Override
    public boolean synchronizeProduct(final ProductModel productModel, final CatalogVersionModel target) {
        // This method assumes that the synchronization services are available in the class
        // Namely: SynchronizationStatusService and CatalogSynchronizationService.
        final String sourceCatalog = productModel.getCatalogVersion().getCatalog().getId() + ":"
                + productModel.getCatalogVersion().getVersion();
        final String targetCatalog = target.getCatalog().getId() + ":" + target.getVersion();
        LOGGER.info("Synchronizing " + productModel.getCode() + " from catalog " + sourceCatalog + " to " + targetCatalog);
        // We need to get the base job model for this product's catalog.
        final List<SyncItemJobModel> jobs = synchronizationStatusService.getOutboundSynchronizations(productModel);
        if (jobs.isEmpty()) {
            // None available. This happens only if the sync has never been setup.
            LOGGER.warn("No SyncItemJobModel available for the provided products catalog version " + sourceCatalog
                    + ". Please setup sync first.");
            return Boolean.FALSE;
        }
        // Now go through the jobs to check which one to use.
        SyncItemJobModel job = null;
        for (final SyncItemJobModel jobModel : jobs) {
            if (jobModel.getTargetVersion().equals(target)) {
                job = jobModel;
                break;
            }
        }
        if (job == null) {
            // source <==> target sync does not exist.
            LOGGER.warn("No SyncItemJobModel defined from catalog version " + sourceCatalog + " to catalog version " + targetCatalog);
            return Boolean.FALSE;
        }
        // Now setup the SyncConfig. The sync config is used to setup the actual CatalogVersionSyncJobModel
        // which is the base job model to be used to run the actual sync job.
        // The following configurations are required to create that model. As desired other configuration
        // items can be added or the below modified.
        final SyncConfig config = new SyncConfig();
        config.setKeepCronJob(Boolean.FALSE);
        config.setSynchronous(Boolean.TRUE);
        config.setCreateSavedValues(Boolean.TRUE);
        config.setForceUpdate(Boolean.FALSE);
        config.setLogLevelDatabase(JobLogLevel.ERROR);
        config.setLogLevelFile(JobLogLevel.INFO);
        config.setLogToDatabase(Boolean.FALSE);
        config.setLogToFile(Boolean.TRUE);
        // Now perform the synchronization.
        final SyncResult result = catalogSynchronizationService.performSynchronization(Collections.singletonList(productModel), job,
                config);
        return result.isSuccessful();
        // Now we can use the SyncResult to check if the sync was successful and completed and notify the user accordingly.
    }

    @Override
    public void syncAction(ProductModel productModel) {
        String catalogName = "trainingProductCatalog";
        final CatalogVersionModel stagedCatalogVersion = catalogVersionService.getCatalogVersion(catalogName, "Staged");
        final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(catalogName, "Online");
        List<CatalogVersionModel> catalogVersionList = new ArrayList<>();
        catalogVersionList.add(stagedCatalogVersion);
        catalogVersionList.add(onlineCatalogVersion);
        searchRestrictionService.disableSearchRestrictions();
        catalogVersionService.setSessionCatalogVersions(catalogVersionList);
        if (synchronizeProduct(productModel, onlineCatalogVersion)) {
            searchRestrictionService.enableSearchRestrictions();
        }
    }
}

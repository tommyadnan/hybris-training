package org.training.core.listeners;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.tx.AfterSaveEvent;
import de.hybris.platform.tx.AfterSaveListener;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.training.core.enums.ActiveStatus;
import org.training.core.event.TestEvent;
import org.training.core.model.TrainingVariantProductModel;

import java.util.Collection;
import java.util.List;

public class TrainingProductAfterSaveListener implements AfterSaveListener {
    private ModelService modelService;
    private EventService eventService;
    private BaseSiteService baseSiteService;
    private CommonI18NService commonI18NService;
    private UserService userService;
    private FlexibleSearchService flexibleSearchService;
    private WorkflowService workflowService;
    private WorkflowTemplateService workflowTemplateService;
    private WorkflowProcessingService workflowProcessingService;

    private static final String TRAINING_PRODUCT_WORKFLOW = "Training_PA_ProductActivityStatus";

    private static final String QUERY_TO_FETCH_WORKFLOW_FOR_PRODUCT = "SELECT {wf:" + WorkflowModel.PK
            + "} FROM {WorkflowItemAttachment as wa JOIN Workflow as wf ON {wa:" + WorkflowItemAttachmentModel.WORKFLOW + "} = {wf:"
            + WorkflowModel.PK + "}} WHERE {wa:" + WorkflowItemAttachmentModel.ITEM + "} = ?item AND {wf:" + WorkflowModel.STATUS
            + "} != ?status";

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingProductAfterSaveListener.class);

    @Override
    public void afterSave(Collection<AfterSaveEvent> events) {
        events.forEach(item -> {
            final PK pk = item.getPk();

            if (pk.getTypeCode() == 1) {
                ProductModel productModel = getProductForPk(pk);
                if (productModel instanceof TrainingVariantProductModel) {
                    TrainingVariantProductModel product = (TrainingVariantProductModel) productModel;
                    if (product.getActiveStatus().equals((ActiveStatus.INWAREHOUSE))) {
                        triggerTrainingProductWorkflow(product);
                    }
                    if (product.getActiveStatus().equals(ActiveStatus.SOLDOUT)) {
                        getEventService().publishEvent(initializeTestEvent(product, new TestEvent()));
                    }
                }
            }
        });
    }

    private ProductModel getProductForPk(final PK pk) {
        return getModelService().get(pk);
    }

    private TestEvent initializeTestEvent(TrainingVariantProductModel productModel, TestEvent event) {
        event.setSite(getBaseSiteService().getAllBaseSites().iterator().next());
        event.setLanguage(getCommonI18NService().getCurrentLanguage());
        event.setProductModel(productModel);
        return event;
    }

    private void triggerTrainingProductWorkflow(final ProductModel productModel) {
        //trigger the workflow here
        if (!checkIfWorkflowAlreadyRunningForItem(productModel)) {
            final WorkflowTemplateModel workflowTemplate = workflowTemplateService
                    .getWorkflowTemplateForCode(TRAINING_PRODUCT_WORKFLOW);
            final WorkflowModel workflow = workflowService.createWorkflow(workflowTemplate, productModel, userService.getAdminUser());
            modelService.save(workflow);
            LOGGER.info("Started product workflow with code " + workflow.getCode());
            for (final WorkflowActionModel action : workflow.getActions()) {
                modelService.save(action);
            }
            workflowProcessingService.startWorkflow(workflow);
            modelService.save(productModel);
        }
    }

    private boolean checkIfWorkflowAlreadyRunningForItem(final ItemModel item) {
        boolean isWorkflowAvailableForProduct = false;
        final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(QUERY_TO_FETCH_WORKFLOW_FOR_PRODUCT);
        searchQuery.addQueryParameter("item", item);
        searchQuery.addQueryParameter("status", CronJobStatus.FINISHED);
        final SearchResult<WorkflowModel> searchResult = flexibleSearchService.search(searchQuery);
        final List<WorkflowModel> workFlows = searchResult.getResult();
        if (CollectionUtils.isNotEmpty(workFlows)) {
            isWorkflowAvailableForProduct = true;
            LOGGER.info("Workflow for the product with pk " + item.getPk() + " is already there " + workFlows.get(0).getCode());
        }
        return isWorkflowAvailableForProduct;
    }



    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    public EventService getEventService() {
        return eventService;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    public BaseSiteService getBaseSiteService() {
        return baseSiteService;
    }

    public void setBaseSiteService(BaseSiteService baseSiteService) {
        this.baseSiteService = baseSiteService;
    }

    public CommonI18NService getCommonI18NService() {
        return commonI18NService;
    }

    public void setCommonI18NService(CommonI18NService commonI18NService) {
        this.commonI18NService = commonI18NService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public FlexibleSearchService getFlexibleSearchService() {
        return flexibleSearchService;
    }

    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }

    public WorkflowService getWorkflowService() {
        return workflowService;
    }

    public void setWorkflowService(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    public WorkflowTemplateService getWorkflowTemplateService() {
        return workflowTemplateService;
    }

    public void setWorkflowTemplateService(WorkflowTemplateService workflowTemplateService) {
        this.workflowTemplateService = workflowTemplateService;
    }

    public WorkflowProcessingService getWorkflowProcessingService() {
        return workflowProcessingService;
    }

    public void setWorkflowProcessingService(WorkflowProcessingService workflowProcessingService) {
        this.workflowProcessingService = workflowProcessingService;
    }
}

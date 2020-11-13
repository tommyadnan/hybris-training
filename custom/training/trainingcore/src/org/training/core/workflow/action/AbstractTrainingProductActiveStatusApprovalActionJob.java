package org.training.core.workflow.action;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.training.core.enums.ActiveStatus;
import org.training.core.model.TrainingVariantProductModel;

import javax.annotation.Resource;
import java.util.List;

public abstract class AbstractTrainingProductActiveStatusApprovalActionJob implements AutomatedWorkflowTemplateJob {
    protected static final String RETRY = "Retry!";
    protected static final String FINISH = "FINISHED!";

    @Resource(name = "modelService")
    private ModelService modelService;

    protected WorkflowDecisionModel approveProductAndFetchDecision(final WorkflowActionModel action) {
    final TrainingVariantProductModel product = getProductFromAttachment(action);
    if (null != product) {
        product.setActiveStatus(ActiveStatus.ACTIVE);
        modelService.save(product);
        return action.getDecisions().iterator().next();
    }
    return null;
    }

    protected WorkflowDecisionModel rejectProductAndFetchDecision(final WorkflowActionModel action) {
        final TrainingVariantProductModel product = getProductFromAttachment(action);
        if (null != product) {
            product.setActiveStatus(ActiveStatus.INWAREHOUSE);
            modelService.save(product);
            return action.getDecisions().iterator().next();
        }
        return null;
    }

    protected TrainingVariantProductModel getProductFromAttachment(final WorkflowActionModel action) {
        final List<ItemModel> attachments = action.getAttachmentItems();
        if (CollectionUtils.isNotEmpty(attachments)) {
            for (final ItemModel item : attachments) {
                if (item instanceof TrainingVariantProductModel) {
                    return (TrainingVariantProductModel) item;
                }
            }
        }
        return null;
    }

    public WorkflowDecisionModel getDecisionForActionName(final WorkflowActionModel action, final String option) {
        List<WorkflowDecisionModel> workflowDecisionModels = (List<WorkflowDecisionModel>) action.getDecisions();
        for (WorkflowDecisionModel optionDecisionModel : workflowDecisionModels) {
            if (StringUtils.containsIgnoreCase(option, optionDecisionModel.getName())) {
                return optionDecisionModel;
            }
        }
        return null;
    }

    @Override
    public WorkflowDecisionModel perform(WorkflowActionModel workflowActionModel) {
        return null;
    }
}

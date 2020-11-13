package org.training.core.workflow.action;

import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrainingProductActiveStatusRejectionActionJob extends AbstractTrainingProductActiveStatusApprovalActionJob{

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingProductActiveStatusApprovalActionJob.class);

    @Override
    public WorkflowDecisionModel perform(final WorkflowActionModel action) {
        LOGGER.info("Enter into Training Product Active Status Rejection Action Job");
        return approveProductAndFetchDecision(action);
    }
}

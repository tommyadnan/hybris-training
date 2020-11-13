package org.training.core.actions.training;

import de.hybris.platform.commerceservices.model.process.TestActionProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.task.RetryLaterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestAction2 extends AbstractSimpleDecisionAction<TestActionProcessModel>{
    private static final Logger LOGGER = LoggerFactory.getLogger(TestAction2.class);

    @Override
    public Transition executeAction(TestActionProcessModel testActionProcessModel) throws RetryLaterException, Exception {
        LOGGER.info("Action 2 Engage...");
        if (true) {
            return Transition.OK;
        }
        return Transition.NOK;
    }
}

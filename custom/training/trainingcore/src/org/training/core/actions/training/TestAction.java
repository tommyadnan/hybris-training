package org.training.core.actions.training;

import de.hybris.platform.commerceservices.model.process.TestActionProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestAction extends AbstractSimpleDecisionAction<TestActionProcessModel> {
    private static final Logger  LOGGER= LoggerFactory.getLogger(TestAction.class);

    @Override
    public Transition executeAction(TestActionProcessModel testActionProcessModel) {
        //do something
        LOGGER.info("Action 1 Engage...");
        if (true){
            return Transition.OK;
        }
        return Transition.NOK;
    }
}

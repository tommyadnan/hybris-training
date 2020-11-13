package org.training.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class TrainingPageController extends AbstractPageController {

    private static final String TRAINING_PAGE = "training-page";

    @RequestMapping(value = TRAINING_PAGE, method = RequestMethod.GET)
    public String getPriceEngine(final Model model ) throws CMSItemNotFoundException {
        final ContentPageModel contentPageModel =  getContentPageForLabelOrId(TRAINING_PAGE);
        storeCmsPageInModel(model, contentPageModel);
        setUpMetaDataForContentPage(model, contentPageModel);
        return getViewForPage(model);
    }
}

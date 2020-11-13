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
public class TermsConPageController extends AbstractPageController {

    private static final String TERMSCON_PAGE = "terms&con-page";

    @RequestMapping(value = TERMSCON_PAGE, method = RequestMethod.GET)
    public String getPriceEngine(final Model model ) throws CMSItemNotFoundException {
        final ContentPageModel contentPageModel =  getContentPageForLabelOrId(TERMSCON_PAGE);
        storeCmsPageInModel(model, contentPageModel);
        setUpMetaDataForContentPage(model, contentPageModel);
        return getViewForPage(model);
    }
}

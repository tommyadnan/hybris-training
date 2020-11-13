package org.training.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.training.facades.recommendation.impl.DefaultRecommendationFacade;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/")
public class RecommendationPageController extends AbstractPageController {

    private static final String RECOMMENDATION_PAGE = "recommendation-page";

    @Resource(name = "recommendationFacade")
    private DefaultRecommendationFacade defaultRecommendationFacade;

    @RequestMapping(value = RECOMMENDATION_PAGE, method = RequestMethod.GET)
    public String getPriceEngine(final Model model ) throws CMSItemNotFoundException {
        List<ProductData> productDataList = defaultRecommendationFacade.getListProductData();
        model.addAttribute("productList",productDataList);
        final ContentPageModel contentPageModel =  getContentPageForLabelOrId(RECOMMENDATION_PAGE);
        storeCmsPageInModel(model, contentPageModel);
        setUpMetaDataForContentPage(model, contentPageModel);
        return getViewForPage(model);
    }
}

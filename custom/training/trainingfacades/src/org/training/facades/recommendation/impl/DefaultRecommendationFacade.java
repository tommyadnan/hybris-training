package org.training.facades.recommendation.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.commercefacades.product.data.ProductData;
import org.apache.commons.collections.CollectionUtils;

import org.training.core.service.TrainingProductService;
import org.training.facades.recommendation.RecommendationFacade;

import javax.annotation.Resource;
import java.util.List;

public class DefaultRecommendationFacade implements RecommendationFacade {
    @Resource(name = "trainingProductService")
    private TrainingProductService trainingProductService;

    @Resource(name = "recommendationConverter")
    private Converter<ProductModel, ProductData> recommendationConverter;


    @Override
    public List<ProductData> getListProductData() {
        List<ProductData> productDataList = recommendationConverter.convertAll(trainingProductService.getAllProductModels());
        if (CollectionUtils.isEmpty(productDataList)) {
            return null;
        }
        return productDataList;

    }
}

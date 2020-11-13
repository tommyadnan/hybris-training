package org.training.facades.populators;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.training.core.model.TrainingVariantProductModel;

public class ListRecommendationPopulator implements Populator<ProductModel, ProductData> {
    @Override
    public void populate(ProductModel source, ProductData target) throws ConversionException {
        TrainingVariantProductModel trainingVariantProductModel = (TrainingVariantProductModel) source;
        target.setSku(trainingVariantProductModel.getSku());
        target.setMaterial(trainingVariantProductModel.getMaterial());
    }
}

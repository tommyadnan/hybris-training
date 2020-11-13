package org.training.facades.populators;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import org.apache.commons.beanutils.ConversionException;
import org.training.core.model.TrainingVariantProductModel;

public class RecommendationPopulator implements Populator<ProductModel, ProductData> {
    @Override
    public void populate(ProductModel source, ProductData target) throws ConversionException {
        if (source instanceof TrainingVariantProductModel) {
            TrainingVariantProductModel trainingVariantProductModel = (TrainingVariantProductModel) source;
//            target.getImages(source.getGalleryImages());
            target.setName(trainingVariantProductModel.getName());
            target.setMaterial(trainingVariantProductModel.getMaterial());
            target.setSku(trainingVariantProductModel.getSku());
        }
    }
}

package org.training.facades.populators;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.converters.populator.SearchResultProductPopulator;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import org.apache.commons.beanutils.ConversionException;

public class TrainingSearchResultProductPopulator extends SearchResultProductPopulator {

    @Override
     public void populate(final SearchResultValueData source, final ProductData target) throws ConversionException {
        target.setSku(this.<String>getValue(source,"sku"));
        target.setMaterial(this.<String>getValue(source,"material"));
    }
}

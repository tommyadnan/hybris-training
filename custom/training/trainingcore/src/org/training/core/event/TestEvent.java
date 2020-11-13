package org.training.core.event;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.model.product.ProductModel;

public class TestEvent extends AbstractCommerceUserEvent<BaseSiteModel> {
    private ProductModel productModel;

    public ProductModel getProductModel() {
        return productModel;
    }

    public void setProductModel(ProductModel productModel){
        this.productModel = productModel;
    }
}

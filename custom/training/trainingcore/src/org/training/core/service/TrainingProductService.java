package org.training.core.service;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;

public interface TrainingProductService {
    List<ProductModel> getAllProductModels();

    List<ProductModel> getProductForCatalogVersion(String productcode, CatalogVersionModel CatalogVersion);

    boolean synchronizeProduct(ProductModel productModel, CatalogVersionModel catalogVersionModel);

    void syncAction(ProductModel productModel);
}

package org.training.core.dao.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.impl.DefaultProductDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.apache.commons.collections.CollectionUtils;
import org.training.core.dao.TrainingProductDao;

import java.util.Collections;
import java.util.List;

public class DefaultTrainingProductDao extends DefaultProductDao implements TrainingProductDao {

    public DefaultTrainingProductDao(String typecode) {
        super(typecode);
    }

    private FlexibleSearchService flexibleSearchService;

    @Override
    public FlexibleSearchService getFlexibleSearchService() {
        return flexibleSearchService;
    }

    @Override
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }

    @Override
    public List<ProductModel> getAllProductModels() {
        final String query = "SELECT {p.pk} FROM {TrainingVariantProduct as p " +
                "JOIN CatalogVersion as cv ON {p.catalogversion}={cv.pk}}" +
                "WHERE {cv.version}=?catalogVersion";
        final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query);
            searchQuery.addQueryParameter("catalogversion", "Online");
        final SearchResult<ProductModel> searchResult = flexibleSearchService.search(searchQuery);
        final List<ProductModel> productModels = searchResult.getResult();

        if (CollectionUtils.isNotEmpty(productModels)) {
            return productModels;
        }

        return Collections.emptyList();
    }
}






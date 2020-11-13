package org.training.core.tasks;

import de.hybris.platform.acceleratorservices.dataimport.batch.BatchHeader;
import de.hybris.platform.acceleratorservices.dataimport.batch.task.HeaderSetupTask;
import org.apache.commons.lang.StringUtils;

import java.io.File;

public class TrainingHeaderTask extends HeaderSetupTask {
    private static final String PRODUCT_CATALOG = "ProductCatalog";

    private static final String CATALOG_NAME_SEPERATOR = "-";

    @Override
    public BatchHeader execute(final File file){
        final String fileName = file.getName();
        final String[] catalogName = fileName.split(CATALOG_NAME_SEPERATOR);
        if (!StringUtils.isBlank(catalogName[0])) {
            setCatalog(catalogName[0] + PRODUCT_CATALOG);
        }
        return super.execute(file);
    }
}

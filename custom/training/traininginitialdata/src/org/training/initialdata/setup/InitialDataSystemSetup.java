/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package org.training.initialdata.setup;

import de.hybris.platform.commerceservices.dataimport.impl.CoreDataImportService;
import de.hybris.platform.commerceservices.dataimport.impl.SampleDataImportService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.setup.events.CoreDataImportedEvent;
import de.hybris.platform.commerceservices.setup.events.SampleDataImportedEvent;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.util.Config;
import org.training.initialdata.constants.TrainingInitialDataConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * This class provides hooks into the system's initialization and update processes.
 */
@SystemSetup(extension = TrainingInitialDataConstants.EXTENSIONNAME)
public class InitialDataSystemSetup extends AbstractSystemSetup
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(InitialDataSystemSetup.class);

	private static final String TRAINING = "training";

	private static final String TRAINING_ESSENTIAL_DATA_IMPEXES = "traininginitialdata.essentialdata.impexes";

	private static final String TRAINING_SAMPLE_DATA_IMPEXES = "traininginitialdata.sampledata.impexes";

	private static final String IMPORT_CORE_DATA = "importCoreData";
	private static final String IMPORT_SAMPLE_DATA = "importSampleData";
	private static final String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";

	private CoreDataImportService coreDataImportService;
	private SampleDataImportService sampleDataImportService;

	/**
	 * Generates the Dropdown and Multi-select boxes for the project data import
	 */
	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();

		params.add(createBooleanSystemSetupParameter(IMPORT_CORE_DATA, "Import Core Data", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_SAMPLE_DATA, "Import Sample Data", true));
		params.add(createBooleanSystemSetupParameter(ACTIVATE_SOLR_CRON_JOBS, "Activate Solr Cron Jobs", true));
		// Add more Parameters here as you require

		return params;
	}

	/**
	 * Implement this method to create initial objects. This method will be called by system creator during
	 * initialization and system update. Be sure that this method can be called repeatedly.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.ESSENTIAL, process = Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{
		// Add Essential Data here as you require
	}

	/**
	 * Implement this method to create data that is used in your project. This method will be called during the system
	 * initialization. <br>
	 * Add import data for each site you have configured
	 *
	 * <pre>
	 * final List<ImportData> importData = new ArrayList<ImportData>();
	 *
	 * final ImportData sampleImportData = new ImportData();
	 * sampleImportData.setProductCatalogName(SAMPLE_PRODUCT_CATALOG_NAME);
	 * sampleImportData.setContentCatalogNames(Arrays.asList(SAMPLE_CONTENT_CATALOG_NAME));
	 * sampleImportData.setStoreNames(Arrays.asList(SAMPLE_STORE_NAME));
	 * importData.add(sampleImportData);
	 *
	 * getCoreDataImportService().execute(this, context, importData);
	 * getEventService().publishEvent(new CoreDataImportedEvent(context, importData));
	 *
	 * getSampleDataImportService().execute(this, context, importData);
	 * getEventService().publishEvent(new SampleDataImportedEvent(context, importData));
	 * </pre>
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		/*
		 * Add import data for each site you have configured
		 */
		LOG.info("::: Training Project Data Update. :::");
		final List<ImportData> importData = new ArrayList<>();

		final ImportData sampleImportData = new ImportData();
		sampleImportData.setProductCatalogName(TRAINING);
		sampleImportData.setContentCatalogNames(Arrays.asList(TRAINING));
		sampleImportData.setStoreNames(Arrays.asList(TRAINING));
		importData.add(sampleImportData);

		// Core data
		final boolean importCoreData = getBooleanSystemSetupParameter(context, IMPORT_CORE_DATA);
		if (importCoreData) {
			getCoreDataImportService().execute(this, context, importData);

			importTrainingCoreData(context);

			getEventService().publishEvent(new CoreDataImportedEvent(context, importData));
			getCoreDataImportService().synchronizeContentCatalog(this, context, TRAINING, true);
			getCoreDataImportService().synchronizeProductCatalog(this, context, TRAINING, true);
		}

		// Sample data
		final boolean importSampleData = getBooleanSystemSetupParameter(context, IMPORT_SAMPLE_DATA);
		if (importSampleData) {
			getSampleDataImportService().execute(this, context, importData);

			importTrainingSampleData(context);

			getEventService().publishEvent(new SampleDataImportedEvent(context, importData));
			getSampleDataImportService().synchronizeContentCatalog(this, context, TRAINING, true);
			getSampleDataImportService().synchronizeProductCatalog(this, context, TRAINING, true);
		}
	}

	/**
	 * Custom method to import the training core data.
	 *
	 * @param context context
	 */
	private void importTrainingCoreData(final SystemSetupContext context) {
		final String extensionName = context.getExtensionName();
		final String impexFiles = Config.getParameter(TRAINING_ESSENTIAL_DATA_IMPEXES);
		if (impexFiles != null) {
			final String[] impexFileArray = impexFiles.split(",");
			for (final String impexFile : impexFileArray) {
				if (impexFile != null) {
					getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/contentCatalogs/%sContentCatalog/%s",
							extensionName, TRAINING, impexFile), false);
					getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/productCatalogs/%sProductCatalog/%s",
							extensionName, TRAINING, impexFile), false);
					getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/common/%s", extensionName, impexFile),
							false);
					getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/stores/training/%s", extensionName, impexFile),
							false);
				}
			}
		}
	}

	/**
	 * Custom method to import the training sample data.
	 *
	 * @param context the context
	 */
	private void importTrainingSampleData(final SystemSetupContext context) {
		final String extensionName = context.getExtensionName();
		final String impexFiles = Config.getParameter(TRAINING_SAMPLE_DATA_IMPEXES);
		if (impexFiles != null) {
			final String[] impexFileArray = impexFiles.split(",");
			for (final String impexFile : impexFileArray) {
				if (impexFile != null) {
					getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/contentCatalogs/%sContentCatalog/%s",
							extensionName, TRAINING, impexFile), false);
					getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/%s",
							extensionName, TRAINING, impexFile), false);
					getSetupImpexService().importImpexFile(
							String.format("/%s/import/sampledata/stores/training/%s", extensionName, impexFile), false);
				}
			}
		}
	}

	public CoreDataImportService getCoreDataImportService()
	{
		return coreDataImportService;
	}

	@Required
	public void setCoreDataImportService(final CoreDataImportService coreDataImportService)
	{
		this.coreDataImportService = coreDataImportService;
	}

	public SampleDataImportService getSampleDataImportService()
	{
		return sampleDataImportService;
	}

	@Required
	public void setSampleDataImportService(final SampleDataImportService sampleDataImportService)
	{
		this.sampleDataImportService = sampleDataImportService;
	}
}

package org.training.core.tasks;


import de.hybris.platform.acceleratorservices.dataimport.batch.task.AbstractImpexRunnerTask;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import org.apache.poi.util.IOUtils;
import org.jboss.logging.Logger;
import org.training.core.util.TrainingUtils;

import java.io.FileInputStream;

public abstract class TrainingAbstractImpexRunnerTask extends AbstractImpexRunnerTask {

    private static final Logger LOG = Logger.getLogger(TrainingAbstractImpexRunnerTask.class);

    @Override
    protected void processFile(final java.io.File file, final String encoding) throws java.io.FileNotFoundException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            final ImportConfig config = getImportConfig();
            if (config == null) {
                LOG.error(String.format("Import config not found. The file %s wont be imported.",
                        file == null ? null : file.getName()));
                return;
            }
            final ImpExResource resource = new StreamBasedImpExResource(fis, encoding);
            config.setScript(resource);
            final ImportResult importResult = getImportService().importData(config);
            if (importResult.isError() && importResult.hasUnresolvedLines()) {
                final String unresolvedLines = importResult.getUnresolvedLines().getPreview();
                sendEmail(file, unresolvedLines);
                LOG.error(unresolvedLines);
            }
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }

    private void sendEmail(final java.io.File file, final String unresolvedLines) {
        final String subject = TrainingUtils.getTrainingHotFolderErrorSubject();
        final String filename = file.getName();
        String message = TrainingUtils.getTrainingHotFolderErrorMessage() + "\n" + filename;
        message = message + '\n' + unresolvedLines;
        final String toEmail = TrainingUtils.getTrainingHotFolderErrorEmail();
        final String[] toEmails = toEmail.split(TrainingUtils.EMAIL_ID_SEPARATOR);
        TrainingUtils.sendNotificationEmail(subject, message, toEmails);
    }

}

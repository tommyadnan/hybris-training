package org.training.core.tasks;

import de.hybris.platform.acceleratorservices.dataimport.batch.BatchHeader;
import de.hybris.platform.acceleratorservices.dataimport.batch.converter.ImpexConverter;
import de.hybris.platform.acceleratorservices.dataimport.batch.task.ImpexTransformerTask;
import de.hybris.platform.acceleratorservices.dataimport.batch.util.BatchDirectoryUtils;
import org.jboss.logging.Logger;
import org.training.core.util.TrainingUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TrainingImpexTransformerTask extends ImpexTransformerTask {
    private static final Logger LOG = Logger.getLogger(TrainingImpexTransformerTask.class);

    private static final String ERROR_FILE_PREFIX = "error_";

    @Override
    protected boolean convertFile(final BatchHeader header, final File file, final File impexFile, final ImpexConverter converter) throws UnsupportedEncodingException, FileNotFoundException {
        final boolean result = super.convertFile(header, file, impexFile, converter);
        final String errorFileName = BatchDirectoryUtils.getRelativeErrorDirectory(file) + File.separator + ERROR_FILE_PREFIX + file.getName();
        if (!result || Paths.get(errorFileName).toFile().exists()) {
            sendEmail(file, errorFileName);
        }
        return result;
    }

    private void sendEmail(final File file, final String errorFileName) {
        final String subject = TrainingUtils.getTrainingHotFolderErrorSubject();
        final String fileName = file.getName();
        String message = TrainingUtils.getTrainingHotFolderErrorMessage() + "\n" + fileName;
        message = message + "\n" + getFileContent(errorFileName);
        final String toEmail = TrainingUtils.getTrainingHotFolderErrorEmail();
        final String[] toEmails = toEmail.split(TrainingUtils.EMAIL_ID_SEPARATOR);
        TrainingUtils.sendNotificationEmail(subject, message, toEmails);
    }

    private String getFileContent(final String errorFileName) {
        try (Stream<String> stream = Files.lines(Paths.get(errorFileName))) {
            return stream.collect(Collectors.joining("\n"));
        } catch (final IOException e) {
            LOG.error("Exception Occured", e);
        }
        return null;
    }
}

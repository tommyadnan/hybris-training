package org.training.core.util;

import de.hybris.platform.util.Config;
import de.hybris.platform.util.mail.MailUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.jboss.logging.Logger;

public class TrainingUtils {
    private static final Logger LOG = Logger.getLogger(TrainingUtils.class);

    /** The constant EMAIL_ID_SEPERATOR */
    public static final String EMAIL_ID_SEPARATOR = ",";
    /**
     * The Constant HOTFOLDER_ERROR_SUBJECT
     */
    private static final String HOTFOLDER_ERROR_SUBJECT = "hotfolder.error.subject";

    /**
     * The Constant HOTFOLDER_ERROR_MESSAGE
     */
    private static final String HOTFOLDER_ERROR_MESSAGE = "hotfolder.error.msg";

    /**
     * The Constant HOTFOLDER_ERROR_MESSAGE
     */
    private static final String HOTFOLDER_ERROR_EMAIL = "hotfolder.error.notification.toaddress";

    /**
     * Gets the training hot folder error subject
     *
     * @return the training hot folder error subject
     */
    public static String getTrainingHotFolderErrorSubject() {
        return Config.getString(HOTFOLDER_ERROR_SUBJECT, "Hotfolder Import Failed");
    }

    /**
     * Gets the training hot folder error message
     *
     * @return the training hot folder error message
     */
    public static String getTrainingHotFolderErrorMessage() {
        return Config.getString(HOTFOLDER_ERROR_MESSAGE, "Import is Failed for");
    }

    /**
     * Gets the training hot folder error email
     *
     * @return the training hot folder error email
     */
    public static String getTrainingHotFolderErrorEmail() {
        return Config.getParameter(HOTFOLDER_ERROR_EMAIL);
    }

    public static void sendNotificationEmail(final String subject, final String message, final String[] toEmails) {
        try {
            final Email email = MailUtils.getPreConfiguredEmail();
            email.addTo(toEmails);
            email.setSubject(subject);
            email.setMsg(message);
            email.send();
        } catch (final EmailException e) {
            LOG.error("Make sure your mail properties are correctly ser", e);
        }

    }


}

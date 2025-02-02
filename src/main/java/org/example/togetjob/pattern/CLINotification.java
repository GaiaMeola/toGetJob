package org.example.togetjob.pattern;

import org.example.togetjob.model.entity.JobApplication;
import java.util.logging.Logger;

public class CLINotification implements Notification {

    private static final Logger logger = Logger.getLogger(CLINotification.class.getName());

    @Override
    public void showNotification(JobApplication jobApplication) {
        logger.info("New Job Application from: " + jobApplication.getStudent().getName());
        logger.info("You have a new application. Please review it at your convenience.");
    }
}

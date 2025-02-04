package org.example.togetjob.pattern;

import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.printer.Printer;

public class CLINotification implements Notification{
    @Override
    public void showNotification(JobApplication jobApplication) {
        Printer.print("New Job Application from: " + jobApplication.getStudent().getName());
        Printer.print("You have a new application. Please review it at your convenience.");
    }
}

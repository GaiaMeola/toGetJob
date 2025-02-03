package org.example.togetjob.pattern_observer;

import org.example.togetjob.model.entity.JobApplication;

public class CLINotification implements Notification{
    @Override
    public void showNotification(JobApplication jobApplication) {
        System.out.println("New Job Application from: " + jobApplication.getStudent().getName());
        System.out.println("You have a new application. Please review it at your convenience.");
    }
}

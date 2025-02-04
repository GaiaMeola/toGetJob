package org.example.togetjob.pattern;

import org.example.togetjob.model.entity.JobApplication;

public interface Notification {
    void showNotification(JobApplication jobApplication);
}

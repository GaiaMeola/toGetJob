package org.example.togetjob.pattern_observer;

import org.example.togetjob.model.entity.JobApplication;

public interface Notification {
    void showNotification(JobApplication jobApplication);
}

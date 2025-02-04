package org.example.togetjob.pattern.observer;

import org.example.togetjob.model.entity.JobApplication;

public interface NotificationObserver {
    void update(JobApplication jobApplication) ;
}

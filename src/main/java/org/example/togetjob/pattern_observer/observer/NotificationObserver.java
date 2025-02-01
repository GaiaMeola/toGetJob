package org.example.togetjob.pattern_observer.observer;

import org.example.togetjob.model.entity.JobApplication;

public interface NotificationObserver {
    void update(JobApplication jobApplication) ;
}

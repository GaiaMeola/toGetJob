package org.example.togetjob.pattern.subject;

import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.pattern.observer.NotificationObserver;

public interface Subject {

    void attach(NotificationObserver observer);
    void detach(NotificationObserver observer);
    void notify(JobApplication jobApplication);

}

package org.example.togetjob.pattern_observer.subject;

import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.pattern_observer.observer.NotificationObserver;

public interface Subject {

    void attach(NotificationObserver observer);
    void detach(NotificationObserver observer);
    void notify(JobApplication jobApplication);

}

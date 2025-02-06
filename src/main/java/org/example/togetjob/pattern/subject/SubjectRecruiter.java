package org.example.togetjob.pattern.subject;

import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.pattern.observer.RecruiterNotificationObserver;

public interface SubjectRecruiter {

    void attach(RecruiterNotificationObserver observer);
    void detach(RecruiterNotificationObserver observer);
    void notify(JobApplication jobApplication);

}

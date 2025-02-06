package org.example.togetjob.pattern.observer;

import org.example.togetjob.model.entity.InterviewScheduling;

public interface StudentNotificationObserver {
    void update(InterviewScheduling interviewScheduling) ;
}

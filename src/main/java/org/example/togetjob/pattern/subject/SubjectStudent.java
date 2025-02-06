package org.example.togetjob.pattern.subject;

import org.example.togetjob.model.entity.InterviewScheduling;
import org.example.togetjob.pattern.observer.StudentObserverStudent;

public interface SubjectStudent {

    void attach(StudentObserverStudent observer);
    void detach(StudentObserverStudent observer);
    void notify(InterviewScheduling interviewScheduling);

}

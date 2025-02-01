package org.example.togetjob.pattern_observer.observer;

import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.model.entity.Student;
import org.example.togetjob.pattern_observer.subject.JobApplicationSubject;

public class JobApplicationObserver implements Observer{

    private Student student ;
    private JobApplicationSubject jobApplicationSubject ;

    @Override
    public void update(JobApplication jobApplication){



    }

}

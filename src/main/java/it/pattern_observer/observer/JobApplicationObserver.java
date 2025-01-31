package it.pattern_observer.observer;

import it.model.entity.JobApplication;
import it.model.entity.Student;
import it.pattern_observer.subject.JobApplicationSubject;

public class JobApplicationObserver implements Observer{

    private Student student ;
    private JobApplicationSubject jobApplicationSubject ;

    @Override
    public void update(JobApplication jobApplication){



    }

}

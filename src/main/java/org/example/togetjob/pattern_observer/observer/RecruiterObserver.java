package org.example.togetjob.pattern_observer.observer;

import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.model.entity.Recruiter;

public class RecruiterObserver implements NotificationObserver{

    private Recruiter recruiter; //who published the job announcement or someone who collaborates

    public RecruiterObserver(Recruiter recruiter){
        this.recruiter = recruiter;
    }

    @Override
    public void update(JobApplication jobApplication){

    }
    
    public Recruiter getRecruiter(){
        return  this.recruiter; 
    }
    
}

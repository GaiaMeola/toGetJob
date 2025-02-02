package org.example.togetjob.pattern.observer;

import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.model.entity.Recruiter;
import org.example.togetjob.pattern.Notification;

public class RecruiterObserver implements NotificationObserver{

    private final Recruiter recruiter; //who published the job announcement or someone who collaborates
    private Notification notification;

    public RecruiterObserver(Recruiter recruiter){
        this.recruiter = recruiter;
    }

    public RecruiterObserver(Recruiter recruiter, Notification notification) {
        this.recruiter = recruiter;
        this.notification = notification;
    }

    @Override
    public void update(JobApplication jobApplication){
        notification.showNotification(jobApplication);
    }
    
    public Recruiter getRecruiter(){
        return  this.recruiter; 
    }
    
}

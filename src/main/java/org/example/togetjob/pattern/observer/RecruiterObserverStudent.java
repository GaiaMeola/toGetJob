package org.example.togetjob.pattern.observer;

import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.model.entity.Recruiter;
import org.example.togetjob.pattern.Notification;

public class RecruiterObserverStudent implements RecruiterNotificationObserver {

    private final Recruiter recruiter; //who published the job announcement or someone who collaborates
    private Notification notification;

    public RecruiterObserverStudent(Recruiter recruiter){
        this.recruiter = recruiter;
    }

    public RecruiterObserverStudent(Recruiter recruiter, Notification notification) {
        this.recruiter = recruiter;
        this.notification = notification;
    }

    @Override
    public void update(JobApplication jobApplication){
        notification.showJobApplicationNotification(jobApplication);
    }

    public Recruiter getRecruiter(){
        return  this.recruiter; 
    }
    
}

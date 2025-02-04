package org.example.togetjob.pattern.subject;

import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.pattern.observer.NotificationObserver;

import java.util.ArrayList;
import java.util.List;

public class JobApplicationCollectionSubject implements Subject {

    private final  List<NotificationObserver> observers = new ArrayList<>();
    private final  List<JobApplication> jobApplications = new ArrayList<>();


    @Override
    public void attach(NotificationObserver observer) {
        observers.add(observer);
    }

    @Override
    public void detach(NotificationObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notify(JobApplication jobApplication) {
        for (NotificationObserver observer: observers){
            observer.update(jobApplication);
        }
    }

    public void addJobApplication(JobApplication jobApplication){
        jobApplications.add(jobApplication);
        notify(jobApplication);
    }

    public List<JobApplication> getJobApplications(){
        return jobApplications;
    }

    public void removeJobApplication(JobApplication jobApplication) {
        jobApplications.remove(jobApplication);
    }
}


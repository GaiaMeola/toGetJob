package org.example.togetjob.pattern_observer.subject;

import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.pattern_observer.observer.NotificationObserver;

import java.util.ArrayList;
import java.util.List;

public class JobApplicationCollectionSubject implements Subject {

    private List<NotificationObserver> observers = new ArrayList<>();
    private List<JobApplication> jobApplications = new ArrayList<>();


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

    public boolean removeJobApplication(JobApplication jobApplication) {
        return jobApplications.remove(jobApplication);
    }
}


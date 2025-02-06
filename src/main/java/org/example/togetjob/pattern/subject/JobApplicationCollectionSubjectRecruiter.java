package org.example.togetjob.pattern.subject;

import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.pattern.observer.RecruiterNotificationObserver;
import org.example.togetjob.pattern.observer.RecruiterObserverStudent;

import java.util.ArrayList;
import java.util.List;

public class JobApplicationCollectionSubjectRecruiter implements SubjectRecruiter {

    private final  List<RecruiterNotificationObserver> observers = new ArrayList<>();
    private final  List<JobApplication> jobApplications = new ArrayList<>();

    @Override
    public void attach(RecruiterNotificationObserver observer) {
        observers.add(observer);
    }

    @Override
    public void detach(RecruiterNotificationObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notify(JobApplication jobApplication) {
        for (RecruiterNotificationObserver observer: observers){
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


package org.example.togetjob.pattern.subject;

import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.pattern.observer.Observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobApplicationCollection extends Subject {

    private final List<Observer> observers = new ArrayList<>();
    private final Map<JobAnnouncement, List<JobApplication>> jobApplicationsMap;
    private boolean newApplicationAdded;

    public JobApplicationCollection() {
        super(new ArrayList<>(), true);
        this.jobApplicationsMap = new HashMap<>();
        this.newApplicationAdded = false;
    }

    public void addJobApplication(JobApplication jobApplication) {
        JobAnnouncement jobAnnouncement = jobApplication.getJobAnnouncement();

        jobApplicationsMap.putIfAbsent(jobAnnouncement, new ArrayList<>());
        jobApplicationsMap.get(jobAnnouncement).add(jobApplication);

        this.newApplicationAdded = true;
        this.notifyObservers();
        this.newApplicationAdded = false;
    }

    public void removeJobApplication(JobApplication jobApplication) {
        JobAnnouncement jobAnnouncement = jobApplication.getJobAnnouncement();

        if (jobApplicationsMap.containsKey(jobAnnouncement)) {
            jobApplicationsMap.get(jobAnnouncement).remove(jobApplication);
        }

        this.notifyObservers();
    }

    public List<JobApplication> getJobApplicationsForAnnouncement(JobAnnouncement jobAnnouncement) {
        return jobApplicationsMap.getOrDefault(jobAnnouncement, new ArrayList<>());
    }

    public int getPendingApplicationsCount(JobAnnouncement jobAnnouncement) {
        return jobApplicationsMap.getOrDefault(jobAnnouncement, new ArrayList<>()).size();
    }

    public List<JobApplication> getAllApplications() {
        List<JobApplication> allApplications = new ArrayList<>();
        for (List<JobApplication> applications : jobApplicationsMap.values()) {
            allApplications.addAll(applications);
        }
        return allApplications;
    }

    @Override
    protected boolean isThereAnythingToNotify() {
        return newApplicationAdded;
    }

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }
}

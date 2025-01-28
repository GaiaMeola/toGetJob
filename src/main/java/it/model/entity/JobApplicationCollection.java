package it.model.entity;

import java.util.List;

public class JobApplicationCollection {

    private List<JobApplication> jobApplications;

    public JobApplicationCollection(List<JobApplication> jobApplications) {
        this.jobApplications = jobApplications;
    }

    public List<JobApplication> getJobApplications() {
        return jobApplications;
    }

    public void addJobApplication(JobApplication jobApplication){
        this.jobApplications.add(jobApplication);
    }

    public void removeJobApplication(JobApplication jobApplication){
        this.jobApplications.remove(jobApplication);
    }

}

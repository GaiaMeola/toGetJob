package org.example.togetjob.model.entity;

import org.example.togetjob.pattern.subject.JobApplicationCollectionSubject;

import java.util.ArrayList;
import java.util.List;

public class JobAnnouncement {

    private final String jobTitle;
    private final String jobType;
    private final String role;
    private final String location;
    private final int workingHours;
    private final String companyName;
    private final double salary;
    private final String description;
    private Boolean isActive;
    private final Recruiter recruiter;
    private final List <Recruiter> collaborators;
    private JobApplicationCollectionSubject jobApplicationCollection;

    public JobAnnouncement(String jobTitle, String jobType, String role, String location, int workingHours, String companyName, double salary, String description, Boolean isActive, Recruiter recruiter) {
        this.jobTitle = jobTitle;
        this.jobType = jobType;
        this.role = role;
        this.location = location;
        this.workingHours = workingHours;
        this.companyName = companyName;
        this.salary = salary;
        this.description = description;
        this.isActive = isActive;
        this.recruiter = recruiter;
        this.collaborators = new ArrayList<>(); //no collaborators when the job announcement is created
        this.jobApplicationCollection = new JobApplicationCollectionSubject();
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getJobType() {
        return jobType;
    }

    public String getRole() {
        return role;
    }

    public String getLocation() {
        return location;
    }

    public int getWorkingHours() {
        return workingHours;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getSalary() {
        return salary;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getActive() {
        return isActive;
    }

    public Recruiter getRecruiter() {
        return recruiter;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public JobApplicationCollectionSubject getJobApplicationCollection() {
        return jobApplicationCollection;
    }

    public void setJobApplicationCollection(JobApplicationCollectionSubject jobApplicationCollection) {
        this.jobApplicationCollection = jobApplicationCollection;
    }

    public void addCollaborator(Recruiter collaborator){
        this.collaborators.add(collaborator);
    }

    public void removeCollaborator(Recruiter collaborator){
        this.collaborators.remove(collaborator);
    }

    public List<Recruiter> getCollaborators(){
        return this.collaborators;
    }

}

package org.example.togetjob.model.factory;

import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.JobApplicationCollection;
import org.example.togetjob.model.entity.Recruiter;
import org.example.togetjob.pattern_observer.subject.JobApplicationCollectionSubject;

public class JobAnnouncementFactory {

    public static JobAnnouncement createJobAnnouncement(String jobTitle, String jobType, String role,
                                                        String location, int workingHours, String companyName,
                                                        double salary, String description, Recruiter recruiter){


        return new JobAnnouncement(jobTitle, jobType, role, location, workingHours, companyName, salary, description, true, recruiter);
    }

}

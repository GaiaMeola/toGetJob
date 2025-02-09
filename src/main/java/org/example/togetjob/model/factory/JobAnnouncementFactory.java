package org.example.togetjob.model.factory;

import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.Recruiter;

public class JobAnnouncementFactory {

    private JobAnnouncementFactory() {
    }

    public static JobAnnouncement createJobAnnouncement(String jobTitle, String jobType, String role,
                                                        String location, int workingHours, String companyName,
                                                        double salary) {
        // base
        return new JobAnnouncement(jobTitle, jobType, role, location, workingHours, companyName, salary);
    }

    // finish
    public static void completeJobAnnouncement(JobAnnouncement jobAnnouncement, String description, Recruiter recruiter, Boolean isActive) {
        jobAnnouncement.setDescription(description);
        jobAnnouncement.setRecruiter(recruiter);
        jobAnnouncement.setActive(isActive);
    }
}

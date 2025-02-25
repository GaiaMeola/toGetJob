package org.example.togetjob.dao.abstractobjects;

import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.Recruiter;

import java.util.List;
import java.util.Optional;

public interface JobAnnouncementDao {

    boolean saveJobAnnouncement(JobAnnouncement jobAnnouncement);
    Optional<JobAnnouncement> getJobAnnouncement(String jobTitle, Recruiter recruiter);
    boolean updateJobAnnouncement(JobAnnouncement jobAnnouncement);
    boolean deleteJobAnnouncement(JobAnnouncement jobAnnouncement);
    List<JobAnnouncement> getAllJobAnnouncements(Recruiter recruiter);
    List<JobAnnouncement> getAllJobAnnouncements();
}

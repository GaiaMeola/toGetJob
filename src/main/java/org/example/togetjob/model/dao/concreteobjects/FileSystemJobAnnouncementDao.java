package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.model.dao.abstractobjects.JobAnnouncementDao;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.Recruiter;

import java.util.List;
import java.util.Optional;

public class FileSystemJobAnnouncementDao implements JobAnnouncementDao {

    @Override
    public boolean saveJobAnnouncement(JobAnnouncement jobAnnouncement) {
        return false;
    }

    @Override
    public Optional<JobAnnouncement> getJobAnnouncement(String jobTitle, Recruiter recruiter) {
        return Optional.empty();
    }

    @Override
    public boolean updateJobAnnouncement(JobAnnouncement jobAnnouncement) {
        return false;
    }

    @Override
    public boolean deleteJobAnnouncement(JobAnnouncement jobAnnouncement) {
        return false;
    }

    @Override
    public boolean jobAnnouncementExists(String jobTitle, Recruiter recruiter) {
        return false;
    }

    @Override
    public List<JobAnnouncement> getAllJobAnnouncements(Recruiter recruiter) {
        return List.of();
    }

    @Override
    public List<JobAnnouncement> getAllJobAnnouncements() {
        return List.of();
    }

    @Override
    public Optional<Integer> getJobAnnouncementId(String jobTitle, String recruiterName) {
        return Optional.empty();
    }

    @Override
    public Optional<JobAnnouncement> getJobAnnouncementById(int jobAnnouncementId) {
        return Optional.empty();
    }
}

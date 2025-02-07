package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.model.dao.abstractobjects.JobApplicationDao;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.model.entity.Student;

import java.util.List;
import java.util.Optional;

public class FileSystemJobApplicationDao implements JobApplicationDao {
    @Override
    public void saveJobApplication(JobApplication jobApplication) {
        // TODO document why this method is empty
    }

    @Override
    public Optional<JobApplication> getJobApplication(Student student, JobAnnouncement jobAnnouncement) {
        return Optional.empty();
    }

    @Override
    public boolean updateJobApplication(JobApplication jobApplication) {
        return false;
    }

    @Override
    public void deleteJobApplication(JobApplication jobApplication) {
        // TODO document why this method is empty
    }

    @Override
    public boolean jobApplicationExists(Student student, JobAnnouncement jobAnnouncement) {
        return false;
    }

    @Override
    public List<JobApplication> getAllJobApplications(Student student) {
        return List.of();
    }

    @Override
    public List<JobApplication> getJobApplicationsByAnnouncement(JobAnnouncement jobAnnouncement) {
        return List.of();
    }
}

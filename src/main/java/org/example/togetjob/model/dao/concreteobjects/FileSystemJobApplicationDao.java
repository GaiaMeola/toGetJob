package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.model.dao.abstractobjects.JobApplicationDao;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.model.entity.Student;

import java.util.List;
import java.util.Optional;

public class FileSystemJobApplicationDao implements JobApplicationDao {
    @Override
    public boolean saveJobApplication(JobApplication jobApplication) {
        return false;
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
    public boolean deleteJobApplication(JobApplication jobApplication) {
        return false;
    }

    @Override
    public boolean jobApplicationExists(Student student, JobAnnouncement jobAnnouncement) {
        return false;
    }

    @Override
    public List<JobApplication> getAllJobApplications(Student student) {
        return List.of();
    }
}

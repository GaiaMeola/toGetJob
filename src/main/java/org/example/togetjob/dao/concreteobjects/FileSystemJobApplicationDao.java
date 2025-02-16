package org.example.togetjob.dao.concreteobjects;

import org.example.togetjob.dao.abstractobjects.JobApplicationDao;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.model.entity.Student;

import java.util.List;
import java.util.Optional;

public class FileSystemJobApplicationDao implements JobApplicationDao {
    @Override
    public void saveJobApplication(JobApplication jobApplication) {
        /* not implemented  */
    }

    @Override
    public Optional<JobApplication> getJobApplication(Student student, JobAnnouncement jobAnnouncement) {
        return Optional.empty();
    }

    @Override
    public void updateJobApplication(JobApplication jobApplication) {
        /* not implemented  */
    }

    @Override
    public void deleteJobApplication(JobApplication jobApplication) {
        /* not implemented  */
    }

    @Override
    public List<JobApplication> getAllJobApplications(Student student) {
        return List.of();
    }

    @Override
    public List<JobApplication> getJobApplicationsByJobAnnouncement(JobAnnouncement jobAnnouncement) {
        return List.of();
    }
}

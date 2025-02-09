package org.example.togetjob.model.dao.abstractobjects;

import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.model.entity.Student;

import java.util.List;
import java.util.Optional;

public interface JobApplicationDao {

    void saveJobApplication (JobApplication jobApplication);
    Optional<JobApplication> getJobApplication(Student student, JobAnnouncement jobAnnouncement);
    void updateJobApplication(JobApplication jobApplication);
    void deleteJobApplication(JobApplication jobApplication);
    boolean jobApplicationExists(Student student, JobAnnouncement jobAnnouncement);
    List<JobApplication> getAllJobApplications(Student student);
    List<JobApplication> getJobApplicationsByAnnouncement(JobAnnouncement jobAnnouncement);
}

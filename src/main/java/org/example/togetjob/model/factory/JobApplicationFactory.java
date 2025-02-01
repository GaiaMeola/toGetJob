package org.example.togetjob.model.factory;

import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.model.entity.Student;

public class JobApplicationFactory {

    public static JobApplication createJobApplication(Student student, String coverLetter, JobAnnouncement jobAnnouncement){

                return new JobApplication(student, coverLetter, jobAnnouncement);
    }

}

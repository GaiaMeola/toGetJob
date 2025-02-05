package org.example.togetjob.model.factory;

import org.example.togetjob.model.entity.InterviewScheduling;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.Student;

import java.time.LocalDateTime;

public class InterviewSchedulingFactory {

    private InterviewSchedulingFactory(){
    }

    public static InterviewScheduling createInterviewScheduling(
                                            LocalDateTime interviewDateTime,
                                            String location,
                                            Student candidate,
                                            JobAnnouncement jobAnnouncement){

        if (candidate == null || jobAnnouncement == null || jobAnnouncement.getJobTitle() == null || jobAnnouncement.getCompanyName() == null ||
                interviewDateTime == null || location == null) {
            throw new IllegalArgumentException("All fields must be provided.");
        }

        return new InterviewScheduling(
                candidate,
                jobAnnouncement,
                jobAnnouncement.getJobTitle(),
                jobAnnouncement.getCompanyName(),
                interviewDateTime,
                location
        );


    }

}

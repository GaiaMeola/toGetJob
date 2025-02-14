package org.example.togetjob.dao.abstractobjects;

import org.example.togetjob.model.entity.InterviewScheduling;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.Student;

import java.util.List;
import java.util.Optional;

public interface InterviewSchedulingDao {

    void saveInterviewScheduling(InterviewScheduling interviewScheduling);
    Optional<InterviewScheduling> getInterviewScheduling(Student student, JobAnnouncement jobAnnouncement);
    List<InterviewScheduling> getAllInterviewScheduling(JobAnnouncement jobAnnouncement);
    boolean interviewSchedulingExists(Student student, JobAnnouncement jobAnnouncement);
    List<InterviewScheduling> getAllInterviewScheduling(Student student);
    void deleteInterviewScheduling(InterviewScheduling interviewScheduling);
    void updateInterviewScheduling(InterviewScheduling interviewScheduling);
}

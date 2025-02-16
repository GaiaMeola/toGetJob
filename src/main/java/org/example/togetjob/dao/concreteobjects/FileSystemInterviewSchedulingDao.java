package org.example.togetjob.dao.concreteobjects;

import org.example.togetjob.dao.abstractobjects.InterviewSchedulingDao;
import org.example.togetjob.model.entity.InterviewScheduling;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.Student;

import java.util.List;
import java.util.Optional;

public class FileSystemInterviewSchedulingDao implements InterviewSchedulingDao {

    @Override
    public void saveInterviewScheduling(InterviewScheduling interviewScheduling) {
        /* not implemented  */
    }

    @Override
    public Optional<InterviewScheduling> getInterviewScheduling(Student student, JobAnnouncement jobAnnouncement) {
        return Optional.empty();
    }

    @Override
    public List<InterviewScheduling> getAllInterviewScheduling(JobAnnouncement jobAnnouncement) {
        return List.of();
    }

    @Override
    public List<InterviewScheduling> getAllInterviewScheduling(Student student) {
        return List.of();
    }

    @Override
    public void deleteInterviewScheduling(InterviewScheduling interviewScheduling) {
        /* not implemented */
    }

    @Override
    public void updateInterviewScheduling(InterviewScheduling interviewScheduling) {
        /* not implemented */
    }
}

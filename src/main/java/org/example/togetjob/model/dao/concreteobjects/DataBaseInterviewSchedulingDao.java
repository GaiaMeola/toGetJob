package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.model.dao.abstractobjects.InterviewSchedulingDao;
import org.example.togetjob.model.entity.InterviewScheduling;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.Student;

import java.util.List;
import java.util.Optional;

public class DataBaseInterviewSchedulingDao implements InterviewSchedulingDao {

    @Override
    public void saveInterviewScheduling(InterviewScheduling interviewScheduling) {

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
    public boolean interviewSchedulingExists(Student student, JobAnnouncement jobAnnouncement) {
        return false;
    }
}

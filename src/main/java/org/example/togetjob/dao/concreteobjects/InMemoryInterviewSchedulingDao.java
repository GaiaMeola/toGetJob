package org.example.togetjob.dao.concreteobjects;

import org.example.togetjob.dao.abstractobjects.InterviewSchedulingDao;
import org.example.togetjob.model.entity.InterviewScheduling;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.Student;

import java.util.*;

public class InMemoryInterviewSchedulingDao implements InterviewSchedulingDao {

    private static final Map<String, Map<JobAnnouncement, InterviewScheduling>> interviewSchedulingMap = new HashMap<>();

    @Override
    public void saveInterviewScheduling(InterviewScheduling interviewScheduling) {

        String key = generateKey(interviewScheduling.getCandidate());

        Map<JobAnnouncement, InterviewScheduling> jobAnnouncementMap = interviewSchedulingMap.getOrDefault(key, new HashMap<>());

        if (jobAnnouncementMap.containsKey(interviewScheduling.getJobAnnouncement())) {
            throw new IllegalArgumentException("Interview already scheduled for this candidate and job announcement.");
        } //Interview Scheduling exists

        // Save
        jobAnnouncementMap.put(interviewScheduling.getJobAnnouncement(), interviewScheduling);
        interviewSchedulingMap.put(key, jobAnnouncementMap);

    }

    @Override
    public Optional<InterviewScheduling> getInterviewScheduling(Student student, JobAnnouncement jobAnnouncement) {

        String key = generateKey(student);

        Map<JobAnnouncement, InterviewScheduling> jobAnnouncementMap = interviewSchedulingMap.get(key);
        if (jobAnnouncementMap != null) {
            return Optional.ofNullable(jobAnnouncementMap.get(jobAnnouncement));
        }
        return Optional.empty();

    }

    @Override
    public List<InterviewScheduling> getAllInterviewScheduling(JobAnnouncement jobAnnouncement) {
        List<InterviewScheduling> result = new ArrayList<>();

        // all the scheduling interview sent
        for (Map<JobAnnouncement, InterviewScheduling> jobAnnouncementMap : interviewSchedulingMap.values()) {
            jobAnnouncementMap.values().stream()
                    .filter(interviewScheduling -> interviewScheduling.getJobAnnouncement().equals(jobAnnouncement))
                    .forEach(result::add);
        }
        return result;
    }

    @Override
    public boolean interviewSchedulingExists(Student student, JobAnnouncement jobAnnouncement) {
        String key = generateKey(student);

        Map<JobAnnouncement, InterviewScheduling> jobAnnouncementMap = interviewSchedulingMap.get(key);
        return jobAnnouncementMap != null && jobAnnouncementMap.containsKey(jobAnnouncement);
    }

    @Override
    public List<InterviewScheduling> getAllInterviewScheduling(Student student) {
        String key = generateKey(student);

        Map<JobAnnouncement, InterviewScheduling> jobAnnouncementMap = interviewSchedulingMap.get(key);

        if (jobAnnouncementMap != null) {
            return new ArrayList<>(jobAnnouncementMap.values());
        }
        return new ArrayList<>();
    }

    @Override
    public void deleteInterviewScheduling(InterviewScheduling interviewScheduling) {
        String key = generateKey(interviewScheduling.getCandidate());

        Map<JobAnnouncement, InterviewScheduling> jobAnnouncementMap = interviewSchedulingMap.get(key);
        if (jobAnnouncementMap != null) {
            jobAnnouncementMap.remove(interviewScheduling.getJobAnnouncement());
            if (jobAnnouncementMap.isEmpty()) {
                interviewSchedulingMap.remove(key);
            }
        }
    }

    @Override
    public void updateInterviewScheduling(InterviewScheduling interviewScheduling) {
        String key = generateKey(interviewScheduling.getCandidate());

        Map<JobAnnouncement, InterviewScheduling> jobAnnouncementMap = interviewSchedulingMap.get(key);
        if (jobAnnouncementMap != null && jobAnnouncementMap.containsKey(interviewScheduling.getJobAnnouncement())) {
            // Update the interview scheduling in the map
            jobAnnouncementMap.put(interviewScheduling.getJobAnnouncement(), interviewScheduling);
        } else {
            throw new IllegalArgumentException("Interview scheduling not found for this candidate and job announcement.");
        }
    }

    private String generateKey(Student student) {
        return student.obtainUsername();
    }

}

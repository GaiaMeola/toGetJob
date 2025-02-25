package org.example.togetjob.dao.concreteobjects;

import org.example.togetjob.dao.abstractobjects.JobAnnouncementDao;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.Recruiter;

import java.util.*;

public class InMemoryJobAnnouncementDao implements JobAnnouncementDao {

    private static final Map<String, Map<Recruiter, JobAnnouncement>> jobAnnouncementsMap = new HashMap<>();

    @Override
    public boolean saveJobAnnouncement(JobAnnouncement jobAnnouncement) {
        String key = generateKey(jobAnnouncement);
        Map<Recruiter, JobAnnouncement> recruiterMap = jobAnnouncementsMap.getOrDefault(key, new HashMap<>());

        recruiterMap.put(jobAnnouncement.getRecruiter(), jobAnnouncement);
        jobAnnouncementsMap.put(key, recruiterMap);

        return true;
    }

    @Override
    public Optional<JobAnnouncement> getJobAnnouncement(String jobTitle, Recruiter recruiter) {
        String key = generateKey(jobTitle, recruiter);
        Map<Recruiter, JobAnnouncement> recruiterMap = jobAnnouncementsMap.get(key);

        return recruiterMap != null ? Optional.ofNullable(recruiterMap.get(recruiter)) : Optional.empty();
    }

    @Override
    public boolean updateJobAnnouncement(JobAnnouncement jobAnnouncement) {
        String key = generateKey(jobAnnouncement);
        Map<Recruiter, JobAnnouncement> recruiterMap = jobAnnouncementsMap.get(key);

        if (recruiterMap != null && recruiterMap.containsKey(jobAnnouncement.getRecruiter())) {
            recruiterMap.put(jobAnnouncement.getRecruiter(), jobAnnouncement);
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteJobAnnouncement(JobAnnouncement jobAnnouncement) {
        String key = generateKey(jobAnnouncement);
        Map<Recruiter, JobAnnouncement> recruiterMap = jobAnnouncementsMap.get(key);

        if (recruiterMap != null && recruiterMap.containsKey(jobAnnouncement.getRecruiter())) {
            recruiterMap.remove(jobAnnouncement.getRecruiter());
            return true;
        }

        return false;
    }

    @Override
    public List<JobAnnouncement> getAllJobAnnouncements(Recruiter recruiter) {
        return jobAnnouncementsMap.values().stream()
                .map(map -> map.get(recruiter))
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public List<JobAnnouncement> getAllJobAnnouncements() {
        return jobAnnouncementsMap.values().stream()
                .flatMap(map -> map.values().stream())
                .toList();
    }

    private String generateKey(JobAnnouncement jobAnnouncement) {
        return jobAnnouncement.obtainJobTitle() + "-" + jobAnnouncement.getRecruiter().obtainUsername();
    }

    private String generateKey(String jobTitle, Recruiter recruiter) {
        return jobTitle + "-" + recruiter.obtainUsername();
    }

}

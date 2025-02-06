package org.example.togetjob.controller.recruiter;

import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.model.dao.abstractobjects.JobAnnouncementDao;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.Recruiter;
import org.example.togetjob.session.SessionManager;

import java.util.Collections;
import java.util.List;

public class JobAnnouncementService {

    private final JobAnnouncementDao jobAnnouncementDao;
    private final SessionManager sessionManager;

    public JobAnnouncementService(JobAnnouncementDao jobAnnouncementDao) {
        this.jobAnnouncementDao = jobAnnouncementDao;
        this.sessionManager = SessionManager.getInstance();
    }

    public List<JobAnnouncementBean> getJobAnnouncementsForCurrentRecruiter() {
        Recruiter recruiter = sessionManager.getRecruiterFromSession();

        if (recruiter == null) {

            return Collections.emptyList();
        }

        List<JobAnnouncement> jobAnnouncements = jobAnnouncementDao.getAllJobAnnouncements(recruiter);

        return jobAnnouncements.stream()
                .map(job -> new JobAnnouncementBean(
                        job.getJobTitle(),
                        job.getJobType(),
                        job.getRole(),
                        job.getLocation(),
                        String.valueOf(job.getWorkingHours()),  // String
                        job.getCompanyName(),
                        String.valueOf(job.getSalary()),        // String
                        job.getDescription(),
                        job.getActive(),
                        recruiter.getUsername()
                ))
                .toList();

    }

}

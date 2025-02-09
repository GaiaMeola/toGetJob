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
                .map(job -> {
                    JobAnnouncementBean jobBean = new JobAnnouncementBean(); // empty

                    jobBean.setJobTitle(job.getJobTitle());
                    jobBean.setJobType(job.getJobType());
                    jobBean.setRole(job.getRole());
                    jobBean.setLocation(job.getLocation());
                    jobBean.setWorkingHours(String.valueOf(job.getWorkingHours()));  // String
                    jobBean.setCompanyName(job.getCompanyName());
                    jobBean.setSalary(String.valueOf(job.getSalary()));        // String
                    jobBean.setDescription(job.getDescription());
                    jobBean.setActive(job.getActive());
                    jobBean.setRecruiterUsername(recruiter.getUsername());

                    return jobBean;
                })
                .toList();
    }


}

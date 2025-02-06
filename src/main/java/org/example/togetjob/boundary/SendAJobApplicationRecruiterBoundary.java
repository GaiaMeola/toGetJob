package org.example.togetjob.boundary;

import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.JobApplicationBean;
import org.example.togetjob.controller.student.SendAJobApplication;
import org.example.togetjob.model.entity.Status;

import java.util.List;

public class SendAJobApplicationRecruiterBoundary {

    private final SendAJobApplication controller;

    public SendAJobApplicationRecruiterBoundary() {
        this.controller = new SendAJobApplication();
    }

    public boolean acceptJobApplication(JobApplicationBean jobApplicationBean) {
        return controller.updateJobApplicationStatus(jobApplicationBean, Status.ACCEPTED);
    }

    public boolean rejectJobApplication(JobApplicationBean jobApplicationBean) {
        return controller.updateJobApplicationStatus(jobApplicationBean, Status.REJECTED);
    }

    public List<JobApplicationBean> getAllJobApplications(JobAnnouncementBean jobAnnouncementBean){
        return controller.getJobApplicationsForRecruiter(jobAnnouncementBean);
    }

}



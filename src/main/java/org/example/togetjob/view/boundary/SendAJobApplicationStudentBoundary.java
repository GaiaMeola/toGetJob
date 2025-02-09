package org.example.togetjob.view.boundary;

import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.JobAnnouncementSearchBean;
import org.example.togetjob.bean.JobApplicationBean;
import org.example.togetjob.controller.student.SendAJobApplication;

import java.util.List;

public class SendAJobApplicationStudentBoundary {

    private final SendAJobApplication controller;

    public SendAJobApplicationStudentBoundary(){
        this.controller = new SendAJobApplication();
    }

    public List<JobAnnouncementBean> getJobAnnouncements(JobAnnouncementSearchBean jobAnnouncementSearchBean){
        return controller.showFilteredJobAnnouncements(jobAnnouncementSearchBean);
    }

    public JobAnnouncementBean getJobAnnouncementDetail(JobAnnouncementBean jobAnnouncementBean){
        return controller.showJobAnnouncementDetails(jobAnnouncementBean);
    }

    public JobApplicationBean fillJobApplicationForm(JobAnnouncementBean jobAnnouncementBean){
        return controller.showJobApplicationForm(jobAnnouncementBean);
    }

    public boolean sendAJobApplication(JobApplicationBean jobApplicationBean){
        return controller.sendAJobApplication(jobApplicationBean);
    }

    public List<JobApplicationBean> getJobApplicationsByStudent() {
        return controller.getAllJobApplication();
    }

    public boolean modifyAJobApplication(JobApplicationBean jobApplicationBean){
        return controller.modifyJobApplication(jobApplicationBean);
    }

    public boolean deleteAJobApplication(JobApplicationBean jobApplicationBean){
        return controller.deleteJobApplication(jobApplicationBean);
    }
}

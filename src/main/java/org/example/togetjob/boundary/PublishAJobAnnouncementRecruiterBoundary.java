package org.example.togetjob.boundary;

import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.controller.recruiter.PublishAJobAnnouncementController;

import java.util.List;

public class PublishAJobAnnouncementRecruiterBoundary {

    private final PublishAJobAnnouncementController controller;

    public PublishAJobAnnouncementRecruiterBoundary() {
        this.controller = new PublishAJobAnnouncementController();
    }

    //Method to publish a job announcement
    public boolean publishJobAnnouncement(JobAnnouncementBean jobAnnouncementBean){
        return controller.publishJobAnnouncement(jobAnnouncementBean);
    }

    public boolean deactivateJobAnnouncement(JobAnnouncementBean jobAnnouncementBean) {
        return controller.changeJobAnnouncementStatus(jobAnnouncementBean, false);
    }

    public boolean activateJobAnnouncement(JobAnnouncementBean jobAnnouncementBean) {
        return controller.changeJobAnnouncementStatus(jobAnnouncementBean, true);
    }

    public boolean deleteJobAnnouncement(JobAnnouncementBean jobAnnouncementBean){
        return controller.deleteJobAnnouncement(jobAnnouncementBean);
    }

    public List<JobAnnouncementBean> getJobAnnouncements(){
        return controller.getJobAnnouncement();
    }

}

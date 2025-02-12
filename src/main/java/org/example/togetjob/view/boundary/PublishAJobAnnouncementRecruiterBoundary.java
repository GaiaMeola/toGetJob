package org.example.togetjob.view.boundary;

import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.controller.recruiter.PublishAJobAnnouncementController;
import org.example.togetjob.exceptions.InvalidSalaryException;
import org.example.togetjob.exceptions.InvalidWorkingHourException;
import org.example.togetjob.exceptions.JobAnnouncementAlreadyExists;
import org.example.togetjob.exceptions.UserNotLoggedException;

import java.util.List;

public class PublishAJobAnnouncementRecruiterBoundary {

    private final PublishAJobAnnouncementController controller;

    public PublishAJobAnnouncementRecruiterBoundary() {
        this.controller = new PublishAJobAnnouncementController();
    }

    //Method to publish a job announcement
    public boolean publishJobAnnouncement(JobAnnouncementBean jobAnnouncementBean) throws JobAnnouncementAlreadyExists, InvalidWorkingHourException, InvalidSalaryException, UserNotLoggedException {
        return controller.publishJobAnnouncement(jobAnnouncementBean);
    }

    public boolean deactivateJobAnnouncement(JobAnnouncementBean jobAnnouncementBean) throws UserNotLoggedException{
        return controller.changeJobAnnouncementStatus(jobAnnouncementBean, false);
    }

    public boolean activateJobAnnouncement(JobAnnouncementBean jobAnnouncementBean) throws UserNotLoggedException {
        return controller.changeJobAnnouncementStatus(jobAnnouncementBean, true);
    }

    public boolean deleteJobAnnouncement(JobAnnouncementBean jobAnnouncementBean) throws UserNotLoggedException {
        return controller.deleteJobAnnouncement(jobAnnouncementBean);
    }

    public List<JobAnnouncementBean> getJobAnnouncements() throws UserNotLoggedException{
        return controller.getJobAnnouncement();
    }

}

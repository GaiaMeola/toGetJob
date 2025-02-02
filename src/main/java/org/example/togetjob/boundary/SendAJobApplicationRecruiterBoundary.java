package org.example.togetjob.boundary;

import org.example.togetjob.bean.JobApplicationBean;
import org.example.togetjob.controller.student.SendAJobApplication;

public class SendAJobApplicationRecruiterBoundary {

    private final SendAJobApplication controller;

    public SendAJobApplicationRecruiterBoundary(){
        this.controller = new SendAJobApplication();
    }

    public boolean acceptJobApplication(JobApplicationBean jobApplicationBean) {
        return controller.acceptJobApplication(jobApplicationBean);
    }

    public boolean rejectJobApplication(JobApplicationBean jobApplicationBean) {
        return controller.rejectJobApplication(jobApplicationBean);
    }



}

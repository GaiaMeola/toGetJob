package org.example.togetjob.pattern;

import org.example.togetjob.model.entity.InterviewScheduling;
import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.printer.Printer;

public class GUINotification implements Notification{

    private String message;

    @Override
    public void showJobApplicationNotification(JobApplication jobApplication) {
        Printer.print("GUI Notification: You have successfully applied for the job.");
    }

    @Override
    public void showInterviewNotification(InterviewScheduling interviewScheduling) {
        Printer.print("GUI Notification: Your interview has been scheduled on \" + interviewScheduling.getInterviewDateTime() + \" at \" + interviewScheduling.getLocation()");
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}

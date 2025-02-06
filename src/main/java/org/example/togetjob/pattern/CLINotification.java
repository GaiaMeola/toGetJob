package org.example.togetjob.pattern;

import org.example.togetjob.model.entity.InterviewScheduling;
import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.printer.Printer;

public class CLINotification implements Notification{

   private String message;

    @Override
    public void showJobApplicationNotification(JobApplication jobApplication) {
        Printer.print("New Job Application from: " + jobApplication.getStudent().getName());
        Printer.print("You have a new application. Please review it at your convenience.");
    }

    @Override
    public void showInterviewNotification(InterviewScheduling interviewScheduling) {
        Printer.print("CLI: Interview scheduled for student: " + interviewScheduling.getCandidate().getName());
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

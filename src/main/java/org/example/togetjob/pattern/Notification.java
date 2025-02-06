package org.example.togetjob.pattern;

import org.example.togetjob.model.entity.InterviewScheduling;
import org.example.togetjob.model.entity.JobApplication;

public interface Notification {
    void showJobApplicationNotification(JobApplication jobApplication);
    void showInterviewNotification(InterviewScheduling interviewScheduling);
    void setMessage(String message);
}

package org.example.togetjob.pattern.adapter;

// Class Target in Adapter

import org.example.togetjob.bean.InterviewSchedulingBean;
import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.StudentInfoBean;
import org.example.togetjob.bean.StudentInfoSearchBean;
import org.example.togetjob.exceptions.NotificationException;
import org.example.togetjob.model.entity.User;

import java.util.List;

public interface ContactAJobCandidateController {

    List<StudentInfoBean> showFiltersCandidate(StudentInfoSearchBean studentInfoSearchBean, JobAnnouncementBean jobAnnouncementBean);

    InterviewSchedulingBean showInterviewSchedulingForm(StudentInfoBean studentInfoBean, JobAnnouncementBean jobAnnouncementBean);

    boolean sendInterviewInvitation(InterviewSchedulingBean interviewSchedulingBean);

    List<JobAnnouncementBean> getJobAnnouncementsByRecruiter();

}

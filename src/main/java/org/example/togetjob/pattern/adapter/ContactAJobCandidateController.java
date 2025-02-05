package org.example.togetjob.pattern.adapter;

// Class Target in Adapter

import org.example.togetjob.bean.InterviewSchedulingBean;
import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.StudentInfoBean;
import org.example.togetjob.bean.StudentInfoSearchBean;

import java.util.List;

public interface ContactAJobCandidateController {

    List<StudentInfoBean> showFiltersCandidate(StudentInfoSearchBean studentInfoSearchBean, JobAnnouncementBean jobAnnouncementBean);
    StudentInfoBean showCandidateDetails(StudentInfoBean studentInfoBean, JobAnnouncementBean jobAnnouncementBean);
    InterviewSchedulingBean showInterviewSchedulingForm(StudentInfoBean studentInfoBean, JobAnnouncementBean jobAnnouncementBean);
    boolean sendInterviewInvitation(InterviewSchedulingBean interviewSchedulingBean);
}

package org.example.togetjob.boundary;

import org.example.togetjob.bean.InterviewSchedulingBean;
import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.StudentInfoBean;
import org.example.togetjob.bean.StudentInfoSearchBean;
import org.example.togetjob.controller.student.SendAJobApplication;
import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.pattern.adapter.ContactAJobCandidateAdapter;
import org.example.togetjob.pattern.adapter.ContactAJobCandidateController;
import org.example.togetjob.pattern.subject.SchedulingInterviewCollectionSubjectRecruiter;

import java.util.List;

public class ContactAJobCandidateRecruiterBoundary {

    private final ContactAJobCandidateController contactAJobCandidateController;

    public ContactAJobCandidateRecruiterBoundary() {
        this.contactAJobCandidateController = new ContactAJobCandidateAdapter(
                new SendAJobApplication(),
                AbstractFactoryDaoSingleton.getFactoryDao().createStudentDao(),
                AbstractFactoryDaoSingleton.getFactoryDao().createJobAnnouncementDao(),
                AbstractFactoryDaoSingleton.getFactoryDao().createJobApplicationDao(),
                AbstractFactoryDaoSingleton.getFactoryDao().createInterviewSchedulingDao(),
                new SchedulingInterviewCollectionSubjectRecruiter()
        );
    }

    public List<StudentInfoBean> getFilteredCandidates(StudentInfoSearchBean searchCriteria, JobAnnouncementBean jobAnnouncement) {
        return contactAJobCandidateController.showFiltersCandidate(searchCriteria, jobAnnouncement);
    }

    public InterviewSchedulingBean getInterviewSchedulingForm(StudentInfoBean candidate, JobAnnouncementBean jobAnnouncement) {
        return contactAJobCandidateController.showInterviewSchedulingForm(candidate, jobAnnouncement);
    }

    public boolean inviteCandidateToInterview(InterviewSchedulingBean interviewDetails) {
        return contactAJobCandidateController.sendInterviewInvitation(interviewDetails);
    }

    public List<JobAnnouncementBean> getJobAnnouncementsByRecruiter() {
        return contactAJobCandidateController.getJobAnnouncementsByRecruiter();
    }
}

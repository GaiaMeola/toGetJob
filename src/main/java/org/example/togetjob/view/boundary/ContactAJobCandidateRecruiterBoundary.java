package org.example.togetjob.view.boundary;

import org.example.togetjob.bean.InterviewSchedulingBean;
import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.StudentInfoBean;
import org.example.togetjob.bean.StudentInfoSearchBean;
import org.example.togetjob.controller.student.SendAJobApplication;
import org.example.togetjob.exceptions.*;
import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.controller.recruiter.ContactAJobCandidateController;
import org.example.togetjob.controller.recruiter.ContactAJobCandidate;

import java.util.List;

public class ContactAJobCandidateRecruiterBoundary {

    private final ContactAJobCandidate contactAJobCandidateController;

    public ContactAJobCandidateRecruiterBoundary() {
        this.contactAJobCandidateController = new ContactAJobCandidateController(
                new SendAJobApplication(),
                AbstractFactoryDaoSingleton.getFactoryDao().createStudentDao(),
                AbstractFactoryDaoSingleton.getFactoryDao().createJobAnnouncementDao(),
                AbstractFactoryDaoSingleton.getFactoryDao().createJobApplicationDao(),
                AbstractFactoryDaoSingleton.getFactoryDao().createInterviewSchedulingDao()
        );
    }

    public List<JobAnnouncementBean> getJobAnnouncementsByRecruiter() throws DatabaseException {
        return contactAJobCandidateController.getJobAnnouncementsByRecruiter();
    }

    public List<StudentInfoBean> getFilteredCandidates(StudentInfoSearchBean searchCriteria, JobAnnouncementBean jobAnnouncement) throws DatabaseException{
        return contactAJobCandidateController.showFilteredCandidates(searchCriteria, jobAnnouncement);
    }

    public InterviewSchedulingBean getInterviewSchedulingForm(StudentInfoBean candidate, JobAnnouncementBean jobAnnouncement) {
        return contactAJobCandidateController.showInterviewSchedulingForm(candidate, jobAnnouncement);
    }

    public boolean inviteCandidateToInterview(InterviewSchedulingBean interviewDetails) throws DateNotValidException, StudentNotFoundException , JobAnnouncementNotFoundException , JobApplicationNotFoundException , InterviewSchedulingAlreadyExistsException , NotificationException , DatabaseException {
        return contactAJobCandidateController.sendInterviewInvitation(interviewDetails);
    }

    public List<InterviewSchedulingBean> getInterviewSchedules(JobAnnouncementBean jobAnnouncementBean) throws JobAnnouncementNotFoundException{
        return contactAJobCandidateController.getInterviewSchedules(jobAnnouncementBean);
    }

    public boolean modifyInterview(InterviewSchedulingBean interviewScheduling) throws DateNotValidException , StudentNotFoundException , JobAnnouncementNotFoundException , InterviewSchedulingNotFoundException {
        return contactAJobCandidateController.modifyInterview(interviewScheduling);
    }

    public boolean deleteInterview(InterviewSchedulingBean interviewScheduling) throws StudentNotFoundException , JobAnnouncementNotFoundException , InterviewSchedulingNotFoundException{
        return contactAJobCandidateController.deleteInterview(interviewScheduling);
    }
}

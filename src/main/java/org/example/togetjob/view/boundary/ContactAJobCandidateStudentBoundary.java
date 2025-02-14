package org.example.togetjob.view.boundary;


import org.example.togetjob.bean.InterviewSchedulingStudentInfoBean;
import org.example.togetjob.controller.student.SendAJobApplication;
import org.example.togetjob.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.controller.recruiter.ContactAJobCandidateController;
import org.example.togetjob.controller.recruiter.ContactAJobCandidate;

import java.util.List;

public class ContactAJobCandidateStudentBoundary {

    private final ContactAJobCandidate contactAJobCandidateController;

    public ContactAJobCandidateStudentBoundary() {
        this.contactAJobCandidateController = new ContactAJobCandidateController(
                new SendAJobApplication(),
                AbstractFactoryDaoSingleton.getFactoryDao().createStudentDao(),
                AbstractFactoryDaoSingleton.getFactoryDao().createJobAnnouncementDao(),
                AbstractFactoryDaoSingleton.getFactoryDao().createJobApplicationDao(),
                AbstractFactoryDaoSingleton.getFactoryDao().createInterviewSchedulingDao()
        );
    }

    public List<InterviewSchedulingStudentInfoBean> getAllInterviewSchedulingForStudent() throws IllegalStateException {
        return contactAJobCandidateController.getAllInterviewSchedulingsForStudent();
    }

}

package org.example.togetjob.view.boundary;


import org.example.togetjob.bean.InterviewSchedulingStudentInfoBean;
import org.example.togetjob.controller.student.SendAJobApplication;
import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.pattern.adapter.ContactAJobCandidateAdapter;
import org.example.togetjob.pattern.adapter.ContactAJobCandidateController;
import org.example.togetjob.pattern.subject.SchedulingInterviewCollectionSubjectRecruiter;

import java.util.List;

public class ContactAJobCandidateStudentBoundary {

    private final ContactAJobCandidateController contactAJobCandidateController;

    public ContactAJobCandidateStudentBoundary() {
        this.contactAJobCandidateController = new ContactAJobCandidateAdapter(
                new SendAJobApplication(),
                AbstractFactoryDaoSingleton.getFactoryDao().createStudentDao(),
                AbstractFactoryDaoSingleton.getFactoryDao().createJobAnnouncementDao(),
                AbstractFactoryDaoSingleton.getFactoryDao().createJobApplicationDao(),
                AbstractFactoryDaoSingleton.getFactoryDao().createInterviewSchedulingDao(),
                new SchedulingInterviewCollectionSubjectRecruiter()
        );
    }

    public List<InterviewSchedulingStudentInfoBean> getAllInterviewSchedulingForStudent() {
        return contactAJobCandidateController.getAllInterviewSchedulingForStudent();
    }

}

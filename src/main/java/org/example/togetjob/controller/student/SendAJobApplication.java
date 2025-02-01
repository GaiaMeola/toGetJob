package org.example.togetjob.controller.student;

import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.JobAnnouncementSearchBean;
import org.example.togetjob.bean.JobApplicationBean;
import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.model.dao.abstractobjects.JobAnnouncementDao;
import org.example.togetjob.model.dao.abstractobjects.JobApplicationDao;
import org.example.togetjob.model.dao.abstractobjects.RecruiterDao;
import org.example.togetjob.model.entity.*;
import org.example.togetjob.model.factory.JobApplicationFactory;
import org.example.togetjob.session.SessionManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class SendAJobApplication {

    private final JobAnnouncementDao jobAnnouncementDao;
    private final JobApplicationDao jobApplicationDao ;
    private final RecruiterDao recruiterDao ;

    public SendAJobApplication(){
        this.jobAnnouncementDao = AbstractFactoryDaoSingleton.getFactoryDao().createJobAnnouncementDao();
        this.jobApplicationDao = AbstractFactoryDaoSingleton.getFactoryDao().createJobApplicationDao();
        this.recruiterDao = AbstractFactoryDaoSingleton.getFactoryDao().createRecruiterDao() ;
    }

    // DA FINIRE
    public List<JobAnnouncementSearchBean> getFilteredJobAnnouncement (JobAnnouncementSearchBean jobAnnouncementSearchBean){

        return List.of() ;

    }

    private Optional<JobApplication> getJobApplication(JobApplicationBean jobApplicationBean){


        Student student = getStudentFromSession();

        Optional<Recruiter> recruiterOpt = recruiterDao.getRecruiter(jobApplicationBean.getRecruiterUsername());
        if (recruiterOpt.isEmpty()) {
            throw new IllegalArgumentException("Errore: Recruiter non trovato.");
        }
        Recruiter recruiter = recruiterOpt.get(); // Ora c'è un recruiter valido

        // Recupero lo job announcement sfruttando il getter di jobApplicationBean
        Optional<JobAnnouncement> jobAnnouncementOpt = jobAnnouncementDao.getJobAnnouncement(jobApplicationBean.getJobTitle(), recruiter);
        if (jobAnnouncementOpt.isEmpty()) {

            throw new IllegalArgumentException("Errore: Recruiter non trovato.");

        }
        JobAnnouncement jobAnnouncement = jobAnnouncementOpt.get(); // Ora c'è un job announcement valido

        return jobApplicationDao.getJobApplication(student, jobAnnouncement);

    }

    public boolean sendAJobApplication (JobApplicationBean jobApplicationBean){

        // Recupero lo studente dalla sessione attuale
        Student student = getStudentFromSession();

        Optional<Recruiter> recruiterOpt = recruiterDao.getRecruiter(jobApplicationBean.getRecruiterUsername());
        if (recruiterOpt.isEmpty()) {
            throw new IllegalArgumentException("Errore: Recruiter non trovato.");
        }
        Recruiter recruiter = recruiterOpt.get(); // Ora c'è un recruiter valido

        // Recupero lo job announcement sfruttando il getter di jobApplicationBean
        Optional<JobAnnouncement> jobAnnouncementOpt = jobAnnouncementDao.getJobAnnouncement(jobApplicationBean.getJobTitle(), recruiter);
        if (jobAnnouncementOpt.isEmpty()) {

            throw new IllegalArgumentException("Errore: Recruiter non trovato.");

        }
        JobAnnouncement jobAnnouncement = jobAnnouncementOpt.get(); // Ora c'è un job announcement valido

        Optional<JobApplication> jobApplicationOpt = jobApplicationDao.getJobApplication(student, jobAnnouncement);

        if (jobApplicationOpt.isEmpty()){

            JobApplication jobApplication = new JobApplication(student, jobApplicationBean.getCoverLetter(), jobAnnouncement);
            jobApplicationDao.saveJobApplication(jobApplication) ;

            // job application inviata
            return true ;

        }

        // ritorna false nel caso in cui esista già una job application inviata dallo studente per quel job announcement
        return false ;

    }








    public Status getStatusJobApplication (JobApplicationBean jobApplicationBean){

        Optional<JobApplication> jobApplicationOpt = getJobApplication(jobApplicationBean) ;
        if (jobApplicationOpt.isEmpty()){

            throw new IllegalArgumentException("Errore: Job Application non trovata.");

        }

        JobApplication jobApplication = jobApplicationOpt.get() ;
        return jobApplication.getStatus() ;

    }


    public boolean modifyJobApplication(JobApplicationBean jobApplicationBean){

        Status status = getStatusJobApplication(jobApplicationBean) ;

        // se lo stato è pending approval allora la candidatura è ancora modificabile
        if (status.equals(Status.PENDING)){

            Optional<JobApplication> jobApplicationOPT = getJobApplication(jobApplicationBean) ;
            JobApplication oldJobApplication = jobApplicationOPT.get() ;

            JobApplication newJobApplication = new JobApplication(oldJobApplication.getStudent(),jobApplicationBean.getCoverLetter(),oldJobApplication.getJobAnnouncement()) ;
            jobApplicationDao.saveJobApplication(newJobApplication) ;

            // la job application è stata modificata
            return true ;


        }

        // la job application è immodificabile
        return false ;

    }

    public boolean deleteJobApplication(JobApplicationBean jobApplicationBean){
        Status status = getStatusJobApplication(jobApplicationBean) ;

        // se lo stato è pending approval allora la candidatura è ancora modificabile
        if (status.equals(Status.PENDING)){

            Optional<JobApplication> jobApplicationOPT = getJobApplication(jobApplicationBean) ;
            JobApplication oldJobApplication = jobApplicationOPT.get() ;

            JobApplication newJobApplication = new JobApplication(oldJobApplication.getStudent(),jobApplicationBean.getCoverLetter(),oldJobApplication.getJobAnnouncement()) ;
            jobApplicationDao.deleteJobApplication(newJobApplication) ;

            // la job application è stata eliminata
            return true ;
        }

        // la job application è ineliminabile
        return false ;

    }

    public boolean manageJobApplication(JobApplicationBean jobApplicationBean){


        Optional<JobApplication> jobApplicationOPT = getJobApplication(jobApplicationBean) ;
        if (jobApplicationOPT.isEmpty()){

            throw new IllegalArgumentException("Errore: Job Application non trovata.");

        }

        JobApplication jobApplication = jobApplicationOPT.get() ;

        Status status = jobApplicationBean.getStatus() ;
        if (status.equals(Status.PENDING)){

            return false ;

        }

        jobApplication.setStatus(status);
        jobApplicationDao.saveJobApplication(jobApplication) ;

        // lo stato della job application è stato modificato
        return true ;

    }

    private Student getStudentFromSession() {
        // Student from session
        return (Student) SessionManager.getInstance().getCurrentUser();
    }

}

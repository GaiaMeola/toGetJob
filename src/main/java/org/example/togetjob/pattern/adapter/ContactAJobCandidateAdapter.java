package org.example.togetjob.pattern.adapter;

import org.example.togetjob.bean.*;
import org.example.togetjob.controller.recruiter.JobAnnouncementService;
import org.example.togetjob.controller.student.SendAJobApplication;
import org.example.togetjob.exceptions.ConfigException;
import org.example.togetjob.exceptions.NotificationException;
import org.example.togetjob.model.dao.abstractobjects.InterviewSchedulingDao;
import org.example.togetjob.model.dao.abstractobjects.JobAnnouncementDao;
import org.example.togetjob.model.dao.abstractobjects.JobApplicationDao;
import org.example.togetjob.model.dao.abstractobjects.StudentDao;
import org.example.togetjob.model.entity.*;
import org.example.togetjob.model.factory.InterviewSchedulingFactory;
import org.example.togetjob.model.factory.NotificationFactory;
import org.example.togetjob.pattern.observer.StudentObserverStudent;
import org.example.togetjob.pattern.subject.SchedulingInterviewCollectionSubjectRecruiter;
import org.example.togetjob.session.SessionManager;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ContactAJobCandidateAdapter implements ContactAJobCandidateController{

    private final SendAJobApplication adapt;
    private final StudentDao studentDao;
    private final JobAnnouncementDao jobAnnouncementDao;
    private final JobApplicationDao jobApplicationDao;
    private final InterviewSchedulingDao interviewSchedulingDao;
    private final SchedulingInterviewCollectionSubjectRecruiter schedulingInterviewCollectionSubjectRecruiter;

    public ContactAJobCandidateAdapter(SendAJobApplication adapt, StudentDao studentDao, JobAnnouncementDao jobAnnouncementDao, JobApplicationDao jobApplicationDao, InterviewSchedulingDao interviewSchedulingDao, SchedulingInterviewCollectionSubjectRecruiter schedulingInterviewCollectionSubjectRecruiter) {
        this.adapt = adapt;
        this.studentDao = studentDao;
        this.jobAnnouncementDao = jobAnnouncementDao;
        this.jobApplicationDao = jobApplicationDao;
        this.interviewSchedulingDao = interviewSchedulingDao;
        this.schedulingInterviewCollectionSubjectRecruiter = schedulingInterviewCollectionSubjectRecruiter;
    }


    @Override
    public List<StudentInfoBean> showFiltersCandidate(StudentInfoSearchBean studentInfoSearchBean, JobAnnouncementBean jobAnnouncementBean) {

        //all job applications
        List<JobApplicationBean> jobApplications = adapt.getJobApplicationsForRecruiter(jobAnnouncementBean);

       // job application with status "accepted"
        List<JobApplicationBean> acceptedApplications = jobApplications.stream()
                .filter(application -> Status.ACCEPTED.equals(application.getStatus()))
                .toList();

        //students
        Set<String> acceptedStudentUsernames = acceptedApplications.stream()
                .map(JobApplicationBean::getStudentUsername)
                .collect(Collectors.toSet());

        // filter
        List<Student> filteredStudents = studentDao.getAllStudents()
                .stream()
                .filter(student -> acceptedStudentUsernames.contains(student.getUsername())) // Studenti che hanno una candidatura accettata
                .filter(student -> filterByDegrees(student, studentInfoSearchBean.getDegrees()))
                .filter(student -> filterByCourses(student, studentInfoSearchBean.getCoursesAttended()))
                .filter(student -> filterByCertifications(student, studentInfoSearchBean.getCertifications()))
                .filter(student -> filterByWorkExperiences(student, studentInfoSearchBean.getWorkExperiences()))
                .filter(student -> filterBySkills(student, studentInfoSearchBean.getSkills()))
                .filter(student -> filterByAvailability(student, studentInfoSearchBean.getAvailability()))
                .toList();


        return filteredStudents.stream()
                .map(this::convertToStudentInfoBean)
                .toList();
    }


    @Override
    public InterviewSchedulingBean showInterviewSchedulingForm(StudentInfoBean studentInfoBean, JobAnnouncementBean jobAnnouncementBean) {

        InterviewSchedulingBean form = new InterviewSchedulingBean();

        form.setStudentUsername(studentInfoBean.getUsername());
        form.setJobTitle(jobAnnouncementBean.getJobTitle());
        form.setCompanyName(jobAnnouncementBean.getCompanyName());

        form.setInterviewDateTime("");
        form.setLocation("");

        return form;
    }


    @Override
    public boolean sendInterviewInvitation(InterviewSchedulingBean interviewSchedulingBean) {

        Recruiter recruiter = SessionManager.getInstance().getRecruiterFromSession();

        //student
        Student student = studentDao.getStudent(interviewSchedulingBean.getStudentUsername())
                .orElseThrow(() -> new IllegalArgumentException("Error: Student not found."));

        //job announcement
        JobAnnouncement jobAnnouncement = jobAnnouncementDao.getJobAnnouncement(interviewSchedulingBean.getJobTitle(), recruiter)
                .orElseThrow(() -> new IllegalArgumentException("Error: Job Announcement not found."));

        //verify that the student has submitted the job application
        if (jobApplicationDao.getJobApplication(student, jobAnnouncement).isEmpty()) {
            throw new IllegalArgumentException("Error: Student has not applied for this job.");
        }

        //verify that there's not another interview scheduling
        if (interviewSchedulingDao.getInterviewScheduling(student, jobAnnouncement).isPresent()) {
            throw new IllegalArgumentException("Error: An interview for this student has already been scheduled.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime interviewDateTime = LocalDateTime.parse(interviewSchedulingBean.getInterviewDateTime(), formatter);

        //create interview scheduling from bean
        InterviewScheduling interviewScheduling = InterviewSchedulingFactory.createInterviewScheduling(
                interviewDateTime,
                interviewSchedulingBean.getLocation(), student,
                jobAnnouncement
        );

        interviewSchedulingDao.saveInterviewScheduling(interviewScheduling);
        //save it

        try { //student as observer
            schedulingInterviewCollectionSubjectRecruiter.attach(new StudentObserverStudent(student, NotificationFactory.createNotification("You have a new interview scheduled!")));
        } catch (ConfigException e) {
            throw new RuntimeException(e);
        }

        try {
            sendNotification(interviewScheduling);

        } catch (NotificationException e) {
            return false;
        }
        return true;
    }

    private void sendNotification(InterviewScheduling interviewScheduling) throws NotificationException {

        try{
            schedulingInterviewCollectionSubjectRecruiter.addInterviewScheduling(interviewScheduling);

        } catch (NotificationException e) {
            throw new NotificationException("Error during the configuration", e);
        }

    }

    @Override
    public List<JobAnnouncementBean> getJobAnnouncementsByRecruiter() {
        JobAnnouncementService jobAnnouncementService = new JobAnnouncementService(jobAnnouncementDao);

        List<JobAnnouncementBean> jobAnnouncements = jobAnnouncementService.getJobAnnouncementsForCurrentRecruiter();

        return Objects.requireNonNullElse(jobAnnouncements, Collections.emptyList());
    }


    private StudentInfoBean convertToStudentInfoBean(Student student) {

        StudentInfoBean studentInfoBean = new StudentInfoBean();

        studentInfoBean.setUsername(student.getUsername());
        studentInfoBean.setDateOfBirth(student.getDateOfBirth() != null ? student.getDateOfBirth() : LocalDate.of(2000, 1, 1));
        studentInfoBean.setPhoneNumber(student.getPhoneNumber() != null ? student.getPhoneNumber() : "No Phone Provided");
        studentInfoBean.setDegrees(student.getDegrees() != null ? new ArrayList<>(student.getDegrees()) : new ArrayList<>());
        studentInfoBean.setCourseAttended(student.getCourseAttended() != null ? new ArrayList<>(student.getCourseAttended()) : new ArrayList<>());
        studentInfoBean.setCertifications(student.getCertifications() != null ? new ArrayList<>(student.getCertifications()) : new ArrayList<>());
        studentInfoBean.setWorkExperiences(student.getWorkExperiences() != null ? new ArrayList<>(student.getWorkExperiences()) : new ArrayList<>());
        studentInfoBean.setSkills(student.getSkills() != null ? new ArrayList<>(student.getSkills()) : new ArrayList<>());
        studentInfoBean.setAvailability(student.getAvailability() != null ? student.getAvailability() : "Not Specified");
        return studentInfoBean;

    }


    private boolean filterByDegrees(Student student, List<String> requiredDegrees) {
        return requiredDegrees == null || requiredDegrees.isEmpty() ||
                student.getDegrees().stream()
                        .anyMatch(degree -> requiredDegrees.stream()
                                .anyMatch(reqDegree -> degree.toLowerCase().contains(reqDegree.toLowerCase())));
    }

    private boolean filterByCourses(Student student, List<String> requiredCourses) {
        return requiredCourses == null || requiredCourses.isEmpty() ||
                student.getCourseAttended().stream()
                        .anyMatch(course -> requiredCourses.stream()
                                .anyMatch(reqCourse -> course.toLowerCase().contains(reqCourse.toLowerCase())));
    }

    private boolean filterByCertifications(Student student, List<String> requiredCertifications) {
        return requiredCertifications == null || requiredCertifications.isEmpty() ||
                student.getCertifications().stream()
                        .anyMatch(cert -> requiredCertifications.stream()
                                .anyMatch(reqCert -> cert.toLowerCase().contains(reqCert.toLowerCase())));
    }

    private boolean filterByWorkExperiences(Student student, List<String> requiredWorkExperiences) {
        return requiredWorkExperiences == null || requiredWorkExperiences.isEmpty() ||
                student.getWorkExperiences().stream()
                        .anyMatch(workExp -> requiredWorkExperiences.stream()
                                .anyMatch(reqWorkExp -> workExp.toLowerCase().contains(reqWorkExp.toLowerCase())));
    }

    private boolean filterBySkills(Student student, List<String> requiredSkills) {
        return requiredSkills == null || requiredSkills.isEmpty() ||
                student.getSkills().stream()
                        .anyMatch(skill -> requiredSkills.stream()
                                .anyMatch(reqSkill -> skill.toLowerCase().contains(reqSkill.toLowerCase())));
    }

    private boolean filterByAvailability(Student student, String requiredAvailability) {
        return requiredAvailability == null || requiredAvailability.isEmpty() ||
                student.getAvailability().toLowerCase().contains(requiredAvailability.toLowerCase());
    }

}

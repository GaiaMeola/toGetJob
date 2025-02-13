package org.example.togetjob.pattern.adapter;

import org.example.togetjob.bean.*;
import org.example.togetjob.controller.recruiter.JobAnnouncementService;
import org.example.togetjob.controller.student.SendAJobApplication;
import org.example.togetjob.exceptions.*;
import org.example.togetjob.model.dao.abstractobjects.InterviewSchedulingDao;
import org.example.togetjob.model.dao.abstractobjects.JobAnnouncementDao;
import org.example.togetjob.model.dao.abstractobjects.JobApplicationDao;
import org.example.togetjob.model.dao.abstractobjects.StudentDao;
import org.example.togetjob.model.entity.*;
import org.example.togetjob.model.factory.InterviewSchedulingFactory;
import org.example.togetjob.session.SessionManager;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ContactAJobCandidateAdapter implements ContactAJobCandidateController{

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm";
    public static final String JOB_ANNOUNCEMENT_NOT_FOUND_ERROR = "Error:Job Announcement not found.";
    public static final String STUDENT_NOT_FOUND_ERROR = "Error:Student not found.";

    private final SendAJobApplication adapt;
    private final StudentDao studentDao;
    private final JobAnnouncementDao jobAnnouncementDao;
    private final JobApplicationDao jobApplicationDao;
    private final InterviewSchedulingDao interviewSchedulingDao;

    public ContactAJobCandidateAdapter(SendAJobApplication adapt, StudentDao studentDao, JobAnnouncementDao jobAnnouncementDao, JobApplicationDao jobApplicationDao, InterviewSchedulingDao interviewSchedulingDao) {
        this.adapt = adapt;
        this.studentDao = studentDao;
        this.jobAnnouncementDao = jobAnnouncementDao;
        this.jobApplicationDao = jobApplicationDao;
        this.interviewSchedulingDao = interviewSchedulingDao;
    }

    public List<StudentInfoBean> showFilteredCandidates(StudentInfoSearchBean studentInfoSearchBean, JobAnnouncementBean jobAnnouncementBean) throws DatabaseException{
        try {
            // Tutte le candidature
            List<JobApplicationBean> jobApplications = adapt.getJobApplicationsForRecruiter(jobAnnouncementBean);

            List<JobApplicationBean> acceptedApplications = jobApplications.stream()
                    .filter(application -> Status.ACCEPTED.equals(application.getStatus()))
                    .toList();

            Set<String> acceptedStudentUsernames = acceptedApplications.stream()
                    .map(JobApplicationBean::getStudentUsername)
                    .collect(Collectors.toSet());

            List<Student> filteredStudents = studentDao.getAllStudents()
                    .stream()
                    .filter(student -> acceptedStudentUsernames.contains(student.obtainUsername()))
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
        }catch (DatabaseException e){
            throw new DatabaseException(e.getMessage()) ;
        }
    }

    private boolean filterByDegrees(Student student, List<String> requiredDegrees) {
        if (requiredDegrees == null || requiredDegrees.isEmpty()) {
            return true;
        }
        if (student.obtainDegrees() == null || student.obtainDegrees().isEmpty()) {
            return true;
        }
        return student.obtainDegrees().stream()
                .anyMatch(degree -> requiredDegrees.stream()
                        .anyMatch(reqDegree -> degree.toLowerCase().contains(reqDegree.toLowerCase())));
    }

    private boolean filterByCourses(Student student, List<String> requiredCourses) {
        if (requiredCourses == null || requiredCourses.isEmpty()) {
            return true;
        }
        if (student.obtainCoursesAttended() == null || student.obtainCoursesAttended().isEmpty()) {
            return true;
        }
        return student.obtainCoursesAttended().stream()
                .anyMatch(course -> requiredCourses.stream()
                        .anyMatch(reqCourse -> course.toLowerCase().contains(reqCourse.toLowerCase())));
    }

    private boolean filterByCertifications(Student student, List<String> requiredCertifications) {
        if (requiredCertifications == null || requiredCertifications.isEmpty()) {
            return true;
        }
        if (student.obtainCertifications() == null || student.obtainCertifications().isEmpty()) {
            return true;
        }
        return student.obtainCertifications().stream()
                .anyMatch(cert -> requiredCertifications.stream()
                        .anyMatch(reqCert -> cert.toLowerCase().contains(reqCert.toLowerCase())));
    }

    private boolean filterByWorkExperiences(Student student, List<String> requiredWorkExperiences) {
        if (requiredWorkExperiences == null || requiredWorkExperiences.isEmpty()) {
            return true;
        }
        if (student.obtainWorkExperiences() == null || student.obtainWorkExperiences().isEmpty()) {
            return true;
        }
        return student.obtainWorkExperiences().stream()
                .anyMatch(workExp -> requiredWorkExperiences.stream()
                        .anyMatch(reqWorkExp -> workExp.toLowerCase().contains(reqWorkExp.toLowerCase())));
    }

    private boolean filterBySkills(Student student, List<String> requiredSkills) {
        if (requiredSkills == null || requiredSkills.isEmpty()) {
            return true;
        }
        if (student.obtainSkills() == null || student.obtainSkills().isEmpty()) {
            return true;
        }
        return student.obtainSkills().stream()
                .anyMatch(skill -> requiredSkills.stream()
                        .anyMatch(reqSkill -> skill.toLowerCase().contains(reqSkill.toLowerCase())));
    }

    private boolean filterByAvailability(Student student, String requiredAvailability) {
        if (requiredAvailability == null || requiredAvailability.isEmpty()) {
            return true;
        }
        if (student.obtainAvailability() == null || student.obtainAvailability().isEmpty()) {
            return true;
        }
        return student.obtainAvailability().toLowerCase().contains(requiredAvailability.toLowerCase());
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
    public boolean sendInterviewInvitation(InterviewSchedulingBean interviewSchedulingBean) throws DateNotValidException , StudentNotFoundException , JobAnnouncementNotFoundException , JobApplicationNotFoundException , InterviewSchedulingAlreadyExistsException , NotificationException , DatabaseException {
        if (isDateFuture(interviewSchedulingBean.getInterviewDateTime())) {
            throw new DateNotValidException("The date must be in the future.");
        }

        Recruiter recruiter = SessionManager.getInstance().getRecruiterFromSession();
        Student student = studentDao.getStudent(interviewSchedulingBean.getStudentUsername())
                .orElseThrow(() -> new StudentNotFoundException(STUDENT_NOT_FOUND_ERROR));
        JobAnnouncement jobAnnouncement = jobAnnouncementDao.getJobAnnouncement(interviewSchedulingBean.getJobTitle(), recruiter)
                .orElseThrow(() -> new JobAnnouncementNotFoundException(JOB_ANNOUNCEMENT_NOT_FOUND_ERROR));
        if (jobApplicationDao.getJobApplication(student, jobAnnouncement).isEmpty()) {
            throw new JobApplicationNotFoundException("Error: Job application not found.");
        }
        if (interviewSchedulingDao.getInterviewScheduling(student, jobAnnouncement).isPresent()) {
            throw new InterviewSchedulingAlreadyExistsException("Error: An Interview Scheduling already exists.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDateTime interviewDateTime = LocalDateTime.parse(interviewSchedulingBean.getInterviewDateTime(), formatter);
        InterviewScheduling interviewScheduling = InterviewSchedulingFactory.createInterviewScheduling(
                interviewDateTime,
                interviewSchedulingBean.getLocation(), student,
                jobAnnouncement
        );
        interviewSchedulingDao.saveInterviewScheduling(interviewScheduling);
        return true;
    }

    @Override
    public List<JobAnnouncementBean> getJobAnnouncementsByRecruiter() throws DatabaseException {
        JobAnnouncementService jobAnnouncementService = new JobAnnouncementService(jobAnnouncementDao);

        List<JobAnnouncementBean> jobAnnouncements = jobAnnouncementService.getJobAnnouncementsForCurrentRecruiter();

        return Objects.requireNonNullElse(jobAnnouncements, Collections.emptyList());
    }

    @Override
    public List<InterviewSchedulingStudentInfoBean> getAllInterviewSchedulingsForStudent() throws IllegalStateException {

        Student student = SessionManager.getInstance().getStudentFromSession();

        if (student == null) {
            throw new IllegalStateException("Student not found in session.");
        }

        List<InterviewScheduling> interviewSchedulings = interviewSchedulingDao.getAllInterviewScheduling(student);

        return interviewSchedulings.stream()
                .map(interviewScheduling -> {
                    InterviewSchedulingStudentInfoBean bean = new InterviewSchedulingStudentInfoBean();
                    bean.setSubject(interviewScheduling.obtainSubject());
                    bean.setGreeting(interviewScheduling.obtainGreeting());
                    bean.setIntroduction(interviewScheduling.obtainIntroduction());
                    bean.setJobTitle(interviewScheduling.getJobAnnouncement().obtainJobTitle());
                    bean.setCompanyName(interviewScheduling.getJobAnnouncement().obtainCompanyName());
                    bean.setInterviewDateTime(interviewScheduling.obtainInterviewDateTime().toString());
                    bean.setLocation(interviewScheduling.obtainLocation());
                    bean.setStudentUsername(interviewScheduling.getCandidate().obtainUsername());
                    return bean;
                })
                .toList();
    }

    @Override
    public List<InterviewSchedulingBean> getInterviewSchedules(JobAnnouncementBean jobAnnouncementBean) throws JobAnnouncementNotFoundException {

        Recruiter recruiter = SessionManager.getInstance().getRecruiterFromSession();
        JobAnnouncement jobAnnouncement = jobAnnouncementDao.getJobAnnouncement(jobAnnouncementBean.getJobTitle(), recruiter)
                .orElseThrow(() -> new JobAnnouncementNotFoundException(JOB_ANNOUNCEMENT_NOT_FOUND_ERROR));

        List<InterviewScheduling> interviewSchedulings = interviewSchedulingDao.getAllInterviewScheduling(jobAnnouncement);

        return interviewSchedulings.stream()
                .map(interviewScheduling -> {
                    InterviewSchedulingBean bean = new InterviewSchedulingBean();
                    bean.setStudentUsername(interviewScheduling.getCandidate().obtainUsername());
                    bean.setJobTitle(interviewScheduling.getJobAnnouncement().obtainJobTitle());
                    bean.setCompanyName(interviewScheduling.getJobAnnouncement().obtainCompanyName());
                    bean.setInterviewDateTime(interviewScheduling.obtainInterviewDateTime().toString());
                    bean.setLocation(interviewScheduling.obtainLocation());
                    return bean;
                })
                .toList();
    }

    @Override
    public boolean modifyInterview(InterviewSchedulingBean interviewSchedulingBean) throws DateNotValidException , StudentNotFoundException , JobAnnouncementNotFoundException , InterviewSchedulingNotFoundException{

        if (isDateFuture(interviewSchedulingBean.getInterviewDateTime())) {
            throw new DateNotValidException("The date must be in the future.");
        }

        Recruiter recruiter = SessionManager.getInstance().getRecruiterFromSession();

        Student student = studentDao.getStudent(interviewSchedulingBean.getStudentUsername())
                .orElseThrow(() -> new StudentNotFoundException(STUDENT_NOT_FOUND_ERROR));
        JobAnnouncement jobAnnouncement = jobAnnouncementDao.getJobAnnouncement(interviewSchedulingBean.getJobTitle(), recruiter)
                .orElseThrow(() -> new JobAnnouncementNotFoundException(JOB_ANNOUNCEMENT_NOT_FOUND_ERROR));

        InterviewScheduling interviewScheduling = interviewSchedulingDao.getInterviewScheduling(student, jobAnnouncement)
                .orElseThrow(() -> new InterviewSchedulingNotFoundException("Interview not found."));


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDateTime interviewDateTime = LocalDateTime.parse(interviewSchedulingBean.getInterviewDateTime(), formatter);
        interviewScheduling.setInterviewDateTime(interviewDateTime);
        interviewScheduling.setLocation(interviewSchedulingBean.getLocation());

        interviewSchedulingDao.updateInterviewScheduling(interviewScheduling);

        return true;
    }

    @Override
    public boolean deleteInterview(InterviewSchedulingBean interviewSchedulingBean) throws StudentNotFoundException , JobAnnouncementNotFoundException , InterviewSchedulingNotFoundException {

        Recruiter recruiter = SessionManager.getInstance().getRecruiterFromSession();
        Student student = studentDao.getStudent(interviewSchedulingBean.getStudentUsername())
                .orElseThrow(() -> new StudentNotFoundException(STUDENT_NOT_FOUND_ERROR));
        JobAnnouncement jobAnnouncement = jobAnnouncementDao.getJobAnnouncement(interviewSchedulingBean.getJobTitle(), recruiter)
                .orElseThrow(() -> new JobAnnouncementNotFoundException(JOB_ANNOUNCEMENT_NOT_FOUND_ERROR));


        InterviewScheduling interviewScheduling = interviewSchedulingDao.getInterviewScheduling(student, jobAnnouncement)
                .orElseThrow(() -> new InterviewSchedulingNotFoundException("Error: Interview not found."));

        interviewSchedulingDao.deleteInterviewScheduling(interviewScheduling);

        return true;
    }

    private StudentInfoBean convertToStudentInfoBean(Student student) {

        StudentInfoBean studentInfoBean = new StudentInfoBean();

        studentInfoBean.setUsername(student.obtainUsername());
        studentInfoBean.setDateOfBirth(student.obtainDateOfBirth() != null ? student.obtainDateOfBirth() : LocalDate.of(2000, 1, 1));
        studentInfoBean.setPhoneNumber(student.obtainPhoneNumber() != null ? student.obtainPhoneNumber() : "No Phone Provided");
        studentInfoBean.setDegrees(student.obtainDegrees() != null ? new ArrayList<>(student.obtainDegrees()) : new ArrayList<>());
        studentInfoBean.setCoursesAttended(student.obtainCoursesAttended() != null ? new ArrayList<>(student.obtainCoursesAttended()) : new ArrayList<>());
        studentInfoBean.setCertifications(student.obtainCertifications() != null ? new ArrayList<>(student.obtainCertifications()) : new ArrayList<>());
        studentInfoBean.setWorkExperiences(student.obtainWorkExperiences() != null ? new ArrayList<>(student.obtainWorkExperiences()) : new ArrayList<>());
        studentInfoBean.setSkills(student.obtainSkills() != null ? new ArrayList<>(student.obtainSkills()) : new ArrayList<>());
        studentInfoBean.setAvailability(student.obtainAvailability() != null ? student.obtainAvailability() : "Not Specified");
        return studentInfoBean;

    }

    private boolean isDateFuture(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDateTime interviewDateTime = LocalDateTime.parse(dateStr, formatter);
        return interviewDateTime.isAfter(LocalDateTime.now());
    }

}

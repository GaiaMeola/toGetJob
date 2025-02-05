package org.example.togetjob.pattern.adapter;

import org.example.togetjob.bean.*;
import org.example.togetjob.controller.student.SendAJobApplication;
import org.example.togetjob.exceptions.StudentNotFoundException;
import org.example.togetjob.model.dao.abstractobjects.JobAnnouncementDao;
import org.example.togetjob.model.dao.abstractobjects.JobApplicationDao;
import org.example.togetjob.model.dao.abstractobjects.StudentDao;
import org.example.togetjob.model.entity.InterviewScheduling;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.Recruiter;
import org.example.togetjob.model.entity.Student;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ContactAJobCandidateAdapter implements ContactAJobCandidateController{

    private final SendAJobApplication adaptee;
    private final StudentDao studentDao;
    private final JobAnnouncementDao jobAnnouncementDao;
    private final JobApplicationDao jobApplicationDao;

    public ContactAJobCandidateAdapter(SendAJobApplication adaptee, StudentDao studentDao, JobAnnouncementDao jobAnnouncementDao, JobApplicationDao jobApplicationDao) {
        this.adaptee = adaptee;
        this.studentDao = studentDao;
        this.jobAnnouncementDao = jobAnnouncementDao;
        this.jobApplicationDao = jobApplicationDao;
    }


    @Override
    public List<StudentInfoBean> showFiltersCandidate(StudentInfoSearchBean studentInfoSearchBean, JobAnnouncementBean jobAnnouncementBean) {

        List<JobApplicationBean> jobApplications = adaptee.getJobApplicationsForRecruiter(jobAnnouncementBean);

        Set<String> studentUsernames = jobApplications.stream()
                .map(JobApplicationBean::getStudentUsername)
                .collect(Collectors.toSet());

        List<Student> filteredStudents = studentDao.getAllStudents()
                .stream()
                .filter(student -> studentUsernames.contains(student.getUsername())) // Studenti che hanno applicato
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
    public StudentInfoBean showCandidateDetails(StudentInfoBean studentInfoBean, JobAnnouncementBean jobAnnouncementBean) throws StudentNotFoundException {

        String candidateUsername = studentInfoBean.getUsername();
        List<JobApplicationBean> jobApplications = adaptee.getJobApplicationsForRecruiter(jobAnnouncementBean);

        boolean hasApplied = jobApplications.stream()
                .anyMatch(app -> app.getStudentUsername().equals(candidateUsername));

        if (!hasApplied) {
            throw new StudentNotFoundException("No job application found for candidate: " + candidateUsername);
        }

        Optional<Student> studentOpt = studentDao.getStudent(candidateUsername);

        if (studentOpt.isEmpty()) {
            throw new StudentNotFoundException("Student not found for username: " + candidateUsername);
        }

        return convertToStudentInfoBean(studentOpt.get());
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

        /*Recruiter recruiter = adaptee.getLoggedRecruiter();

        Student student = studentDao.getStudent(interviewSchedulingBean.getStudentUsername())
                .orElseThrow(() -> new IllegalArgumentException("Error: Student not found."));

        JobAnnouncement jobAnnouncement = jobAnnouncementDao.getJobAnnouncement(interviewSchedulingBean.getJobTitle(), recruiter)
                .orElseThrow(() -> new IllegalArgumentException("Error: Job Announcement not found."));

        if (jobApplicationDao.getJobApplication(student, jobAnnouncement).isEmpty()) {
            throw new IllegalArgumentException("Student not exists.");
        }

        boolean interviewAlreadyScheduled = checkIfInterviewScheduled(interviewSchedulingBean.getStudentUsername());

        if (interviewAlreadyScheduled) {
            throw new IllegalArgumentException("Il colloquio per questo studente è già stato programmato.");
        }

        InterviewScheduling interviewScheduling = InterviewSchedulingFactory.createInterviewScheduling(
                recruiter, student, interviewSchedulingBean.getInterviewDateTime(), interviewSchedulingBean.getLocation(), jobAnnouncement
        );

        interviewSchedulingDao.saveInterviewScheduling(interviewScheduling);
         */
        return true;
    }

    /*private boolean checkIfInterviewScheduled(String studentUsername) {

        List<InterviewSchedulingBean> scheduledInterviews = adaptee.getScheduledInterviewsForStudent(studentUsername);
        return scheduledInterviews.stream().anyMatch(interview -> interview.getStudentUsername().equals(studentUsername));

    }*/


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
                student.getDegrees().stream().anyMatch(requiredDegrees::contains);
    }

    private boolean filterByCourses(Student student, List<String> requiredCourses) {
        return requiredCourses == null || requiredCourses.isEmpty() ||
                student.getCourseAttended().stream().anyMatch(requiredCourses::contains);
    }

    private boolean filterByCertifications(Student student, List<String> requiredCertifications) {
        return requiredCertifications == null || requiredCertifications.isEmpty() ||
                student.getCertifications().stream().anyMatch(requiredCertifications::contains);
    }

    private boolean filterByWorkExperiences(Student student, List<String> requiredWorkExperiences) {
        return requiredWorkExperiences == null || requiredWorkExperiences.isEmpty() ||
                student.getWorkExperiences().stream().anyMatch(requiredWorkExperiences::contains);
    }

    private boolean filterBySkills(Student student, List<String> requiredSkills) {
        return requiredSkills == null || requiredSkills.isEmpty() ||
                student.getSkills().stream().anyMatch(requiredSkills::contains);
    }

    private boolean filterByAvailability(Student student, String requiredAvailability) {
        return requiredAvailability == null || requiredAvailability.isEmpty() ||
                student.getAvailability().equalsIgnoreCase(requiredAvailability);
    }
}

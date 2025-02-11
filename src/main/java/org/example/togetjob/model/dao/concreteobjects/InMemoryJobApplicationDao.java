package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.model.dao.abstractobjects.JobApplicationDao;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.model.entity.Student;

import java.util.*;

public class InMemoryJobApplicationDao implements JobApplicationDao {

    private static final Map<String, Map<Student, JobApplication>> jobApplicationsMap = new HashMap<>();

    @Override
    public void saveJobApplication(JobApplication jobApplication) {
        String key = generateKey(jobApplication);
        Map<Student, JobApplication> studentMap = jobApplicationsMap.getOrDefault(key, new HashMap<>());

        studentMap.put(jobApplication.getStudent(), jobApplication);
        jobApplicationsMap.put(key, studentMap);

    }

    @Override
    public Optional<JobApplication> getJobApplication(Student student , JobAnnouncement jobAnnouncement ) {
        String key = generateKey(student, jobAnnouncement);

        Map<Student, JobApplication> studentMap = jobApplicationsMap.get(key);
        return studentMap != null ? Optional.ofNullable(studentMap.get(student)) : Optional.empty();
    }

    @Override
    public void updateJobApplication(JobApplication jobApplication) {
        String key = generateKey(jobApplication);
        Map<Student, JobApplication> studentMap = jobApplicationsMap.get(key);

        if (studentMap != null && studentMap.containsKey(jobApplication.getStudent())) {
            studentMap.put(jobApplication.getStudent(), jobApplication);
        }

    }

    @Override
    public void deleteJobApplication(JobApplication jobApplication) {
        String key = generateKey(jobApplication);
        Map<Student, JobApplication> studentMap = jobApplicationsMap.get(key);

        if (studentMap != null) {
            studentMap.remove(jobApplication.getStudent());
        }

    }

    @Override
    public boolean jobApplicationExists(Student student , JobAnnouncement jobAnnouncement ) {
        String key = generateKey(student, jobAnnouncement);
        Map<Student, JobApplication> studentMap = jobApplicationsMap.get(key);
        return studentMap != null && studentMap.containsKey(student);
    }


    @Override
    public List<JobApplication> getAllJobApplications(Student student) {
        return jobApplicationsMap.values().stream()
                .map(map -> map.get(student)) //
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public List<JobApplication> getJobApplicationsByAnnouncement(JobAnnouncement jobAnnouncement) {
        return jobApplicationsMap.values().stream() //
                .flatMap(studentMap -> studentMap.values().stream())
                .filter(application -> application.getJobAnnouncement().equals(jobAnnouncement))
                .toList();
    }


    private String generateKey(Student student , JobAnnouncement jobAnnouncement){

        return student.obtainUsername() + "-" + jobAnnouncement.obtainJobTitle() + "-" + jobAnnouncement.getRecruiter().obtainUsername() ;
    }

    private String generateKey(JobApplication jobApplication) {
        return jobApplication.getStudent().obtainUsername() + "-" + jobApplication.getJobAnnouncement().obtainJobTitle() + "-" + jobApplication.getJobAnnouncement().getRecruiter().obtainUsername() ;
    }

}


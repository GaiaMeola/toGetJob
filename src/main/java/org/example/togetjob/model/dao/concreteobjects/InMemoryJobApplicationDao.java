package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.model.dao.abstractobjects.JobApplicationDao;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.model.entity.Student;

import java.util.*;

public class InMemoryJobApplicationDao implements JobApplicationDao {

    private static final Map<String, Map<Student, JobApplication>> jobApplications = new HashMap<>();

    @Override
    public void saveJobApplication(JobApplication jobApplication) {
        String key = generateKey(jobApplication);
        Map<Student, JobApplication> studentMap = jobApplications.getOrDefault(key, new HashMap<>());

        studentMap.put(jobApplication.getStudent(), jobApplication);
        jobApplications.put(key, studentMap);

    }

    @Override
    public Optional<JobApplication> getJobApplication(Student student , JobAnnouncement jobAnnouncement ) {
        String key = generateKey(student, jobAnnouncement);

        Map<Student, JobApplication> studentMap = jobApplications.get(key);
        return studentMap != null ? Optional.ofNullable(studentMap.get(student)) : Optional.empty();
    }

    @Override
    public boolean updateJobApplication(JobApplication jobApplication) {
        String key = generateKey(jobApplication);
        Map<Student, JobApplication> studentMap = jobApplications.get(key);

        if (studentMap != null && studentMap.containsKey(jobApplication.getStudent())) {
            studentMap.put(jobApplication.getStudent(), jobApplication);
        }

        return true;

    }

    @Override
    public boolean deleteJobApplication(JobApplication jobApplication) {
        String key = generateKey(jobApplication);
        Map<Student, JobApplication> studentMap = jobApplications.get(key);

        if (studentMap != null) {
            studentMap.remove(jobApplication.getStudent());
        }

        return true;

    }

    @Override
    public boolean jobApplicationExists(Student student , JobAnnouncement jobAnnouncement ) {
        String key = generateKey(student, jobAnnouncement);
        Map<Student, JobApplication> studentMap = jobApplications.get(key);
        return studentMap != null && studentMap.containsKey(student);
    }


    @Override
    public List<JobApplication> getAllJobApplications(Student student) {
        return jobApplications.values().stream()
                .map(map -> map.get(student)) //
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public List<JobApplication> getJobApplicationsByJobAnnouncement(JobAnnouncement jobAnnouncement) {
        return jobApplications.values().stream() //
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


package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.model.dao.abstractobjects.JobApplicationDao;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.model.entity.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryJobApplicationDao implements JobApplicationDao {

    private static final Map<String, Map<Student, JobApplication>> jobApplicationsMap = new HashMap<>();

    @Override
    public boolean saveJobApplication(JobApplication jobApplication) {
        String key = generateKey(jobApplication);
        Map<Student, JobApplication> studentMap = jobApplicationsMap.getOrDefault(key, new HashMap<>());

        studentMap.put(jobApplication.getStudent(), jobApplication);
        jobApplicationsMap.put(key, studentMap);

        return true;
    }

    @Override
    public Optional<JobApplication> getJobApplication(Student student , JobAnnouncement jobAnnouncement ) {
        String key = generateKey(student, jobAnnouncement);

        Map<Student, JobApplication> studentMap = jobApplicationsMap.get(key);
        return studentMap != null ? Optional.ofNullable(studentMap.get(student)) : Optional.empty();
    }

    @Override
    public boolean updateJobApplication(JobApplication jobApplication) {
        String key = generateKey(jobApplication);
        Map<Student, JobApplication> studentMap = jobApplicationsMap.get(key);

        if (studentMap != null && studentMap.containsKey(jobApplication.getStudent())) {
            studentMap.put(jobApplication.getStudent(), jobApplication);
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteJobApplication(JobApplication jobApplication) {
        String key = generateKey(jobApplication);
        Map<Student, JobApplication> studentMap = jobApplicationsMap.get(key);

        if (studentMap != null && studentMap.containsKey(jobApplication.getStudent())) {
            studentMap.remove(jobApplication.getStudent());
            return true;
        }

        return false;
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
                .map(map -> map.get(student))
                .filter(application -> application != null)
                .collect(Collectors.toList());
    }


    private String generateKey(Student student , JobAnnouncement jobAnnouncement){

        return student.getUsername() + "-" + jobAnnouncement.getJobTitle() + "-" + jobAnnouncement.getRecruiter().getUsername() ;
    }

    // la chiave è costituita da: username studente + titolo job announcement + username recruiter
    private String generateKey(JobApplication jobApplication) {
        return jobApplication.getStudent().getUsername() + "-" + jobApplication.getJobAnnouncement().getJobTitle() + "-" + jobApplication.getJobAnnouncement().getRecruiter().getUsername() ;
    }

}


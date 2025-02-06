package org.example.togetjob.pattern.subject;

import org.example.togetjob.model.entity.InterviewScheduling;
import org.example.togetjob.model.entity.Student;
import org.example.togetjob.pattern.observer.StudentNotificationObserver;
import org.example.togetjob.pattern.observer.StudentObserverStudent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchedulingInterviewCollectionSubjectRecruiter implements SubjectStudent {

    private final Map<Student, StudentObserverStudent> studentObservers = new HashMap<>();
    private final Map<Student, InterviewScheduling> studentInterviews = new HashMap<>();


    @Override
    public void attach(StudentObserverStudent observer) {
        studentObservers.put(observer.getStudent(), observer);
    }

    @Override
    public void detach(StudentObserverStudent observer) {
        studentObservers.remove(observer.getStudent());
    }

    @Override
    public void notify(InterviewScheduling interviewScheduling) {
        Student student = interviewScheduling.getCandidate();
        StudentNotificationObserver observer = studentObservers.get(student);
        if (observer != null) {
            observer.update(interviewScheduling);
        }
    }

    public void addInterviewScheduling(InterviewScheduling interviewScheduling) {
        Student student = interviewScheduling.getCandidate();
        studentInterviews.put(student, interviewScheduling);
        notify(interviewScheduling);
    }

    public InterviewScheduling getInterviewScheduling(Student student) {
        return studentInterviews.get(student);
    }

    public void removeInterviewScheduling(InterviewScheduling interviewScheduling) {
        Student student = interviewScheduling.getCandidate();
        studentInterviews.remove(student);
    }
}

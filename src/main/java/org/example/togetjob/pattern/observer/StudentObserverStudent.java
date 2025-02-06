package org.example.togetjob.pattern.observer;

import org.example.togetjob.model.entity.InterviewScheduling;
import org.example.togetjob.model.entity.Student;
import org.example.togetjob.pattern.Notification;

public class StudentObserverStudent implements StudentNotificationObserver {

    private final Student student;
    private final Notification notification;

    public StudentObserverStudent(Student student, Notification notification) {
        this.student = student;
        this.notification = notification;
    }

    @Override
    public void update(InterviewScheduling interviewScheduling) {

        notification.showInterviewNotification(interviewScheduling);

    }

    public Student getStudent() {
        return student;
    }
}

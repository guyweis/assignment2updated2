package bgu.spl.mics.application.messages;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

public class PublishResultsEvent {
    private Student studentName;

    public PublishResultsEvent (Student student) {
        this.studentName = student;
    }

    public Student getStudent() {
        return studentName;
    }
}
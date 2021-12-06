package bgu.spl.mics.application.objects;

import jdk.net.SocketFlow;

import java.util.LinkedList;

/**
 * Passive object representing a Deep Learning model.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Model
{


    public enum Status {PreTrained, Training, Trained, Tested};
    public enum Result {None, Good, Bad};
    String name;
    Data data;
    Student student;
    Status status;
    Result result;


    public Model(String name1, Data data1, Student student1)
    {
        status = Status.PreTrained;
        result = Result.None;
        name = name1;
        data = data1;
        student =student1;
    }

    public Status getStatus()
    {
        return status;
    }

    public Data getData()
    {
        return data;
    }

    public Result getResult()
    {
        return result;
    }

    public Student getStudent() {
        return student;
    }


    public void setStatus(Status status) {
        this.status = status;
    }
}

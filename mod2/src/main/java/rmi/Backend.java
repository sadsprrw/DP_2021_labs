package rmi;

import models.StudentsGroup;
import models.Student;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Backend extends Remote {
    public StudentsGroup studentsGroupFindById(Integer id) throws RemoteException;
    public StudentsGroup studentsGroupFindByName(String name) throws RemoteException;
    public boolean studentsGroupUpdate(StudentsGroup group) throws RemoteException;
    public boolean studentsGroupInsert(StudentsGroup group) throws RemoteException;
    public boolean studentsGroupDelete(StudentsGroup group) throws RemoteException;
    public List<StudentsGroup> studentsGroupFindAll() throws RemoteException;
    public Student studentFindById(Integer id) throws RemoteException;
    public boolean studentUpdate(Student student) throws RemoteException;
    public boolean studentInsert(Student student) throws RemoteException;
    public boolean studentDelete(Student student) throws RemoteException;
    public List<Student> studentFindAll() throws RemoteException;
    public List<Student> studentFindByStudentsGroupId(Integer id) throws RemoteException;
}
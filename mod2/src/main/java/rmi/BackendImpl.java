package rmi;

import dao.StudentsGroupDAO;
import dao.StudentDAO;
import models.StudentsGroup;
import models.Student;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class BackendImpl  extends UnicastRemoteObject implements Backend  {

    protected BackendImpl() throws RemoteException{
        super();
    }

    @Override
    public StudentsGroup studentsGroupFindById(Integer id) throws RemoteException {
        return StudentsGroupDAO.findById(id);
    }

    @Override
    public StudentsGroup studentsGroupFindByName(String name) throws RemoteException {
        return StudentsGroupDAO.findByName(name);
    }

    @Override
    public boolean studentsGroupUpdate(StudentsGroup studentsGroup) throws RemoteException {
        return StudentsGroupDAO.update(studentsGroup);
    }

    @Override
    public boolean studentsGroupInsert(StudentsGroup studentsGroup) throws RemoteException {
        return StudentsGroupDAO.insert(studentsGroup);
    }

    @Override
    public boolean studentsGroupDelete(StudentsGroup studentsGroup) throws RemoteException {
        return StudentsGroupDAO.delete(studentsGroup);
    }

    @Override
    public List<StudentsGroup> studentsGroupFindAll() throws RemoteException {
        return StudentsGroupDAO.findAll();
    }

    @Override
    public Student studentFindById(Integer id) throws RemoteException {
        return StudentDAO.findById(id);
    }

    @Override
    public boolean studentUpdate(Student student) throws RemoteException {
        return StudentDAO.update(student);
    }

    @Override
    public boolean studentInsert(Student student) throws RemoteException {
        return StudentDAO.insert(student);
    }

    @Override
    public boolean studentDelete(Student student) throws RemoteException {
        return StudentDAO.delete(student);
    }

    @Override
    public List<Student> studentFindAll() throws RemoteException {
        return StudentDAO.findAll();
    }

    @Override
    public List<Student> studentFindByStudentsGroupId(Integer id) throws RemoteException {
        return StudentDAO.findByStudentsGroupId(id);
    }
}

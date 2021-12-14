package models;

import java.util.ArrayList;
import java.util.List;

public class StudiesDepartment {
    private final List<StudentsGroup> groups;

    public StudiesDepartment() {
        this.groups = new ArrayList<>();
    }
    public void addStudentsGroup(StudentsGroup group){
        groups.add(group);
    }
    public void addStudent(Student student){
        StudentsGroup c = new StudentsGroup();
        c.setId(student.getGroupId());
        groups.get(groups.indexOf(c)).addStudent(student);
    }
    public void deleteStudent(Student student, StudentsGroup group){
        groups.get(groups.indexOf(group)).removeStudent(student);
    }
    public List<StudentsGroup> getGroups() {
        return groups;
    }
}

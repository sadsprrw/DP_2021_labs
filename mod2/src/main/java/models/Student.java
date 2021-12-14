package models;

public class Student {

    private Integer id;
    private Integer groupId;
    private String name;
    private short age;

    public Student(){

    }

    public Student(Integer id, Integer groupId, String name, short age){
        this.id = id;
        this.groupId = groupId;
        this.name = name;
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getAge() {
        return age;
    }

    public void setAge(short age) {
        this.age = age;
    }

}

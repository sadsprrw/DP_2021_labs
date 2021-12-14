package tcp_ip;

import models.StudentsGroup;
import models.Student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;
    private static final String separator = "#";

    public Client(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public StudentsGroup studentsGroupFindById(Integer id) {
        String query = "StudentsGroupFindById" + separator + id.toString();
        out.println(query);
        String response;
        try {
            response = in.readLine();
            return new StudentsGroup(id, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public StudentsGroup studentsGroupFindByName(String name) {
        String query = "StudentsGroupFindByName" + separator + name;
        out.println(query);
        try {
            Integer response = Integer.parseInt(in.readLine());
            return new StudentsGroup(response, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean studentUpdate(Student student) {
        String query = "StudentUpdate" + separator + student.getId() + separator + student.getGroupId() + separator
                + student.getName() + separator + student.getAge();
        out.println(query);
        try {
            String response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean studentsGroupUpdate(StudentsGroup studentsGroup) {
        String query = "StudentsGroupUpdate" + separator + studentsGroup.getId() +
                separator + studentsGroup.getName();
        out.println(query);
        try {
            String response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean studentInsert(Student student) {
        String query = "StudentInsert" +
                separator +  student.getId() + separator + student.getGroupId() + separator
                + student.getName() + separator + student.getAge();
        out.println(query);
        try {
            String response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean studentsGroupInsert(StudentsGroup studentsGroup) {
        String query = "StudentsGroupInsert" +
                separator + studentsGroup.getName();
        out.println(query);
        try {
            String response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean studentsGroupDelete(StudentsGroup studentsGroup) {
        String query = "StudentsGroupDelete" + separator + studentsGroup.getId();
        out.println(query);
        try {
            String response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean studentDelete(Student student) {
        String query = "StudentDelete" + separator + student.getId();
        out.println(query);
        try {
            String response = in.readLine();
            return "true".equals(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<StudentsGroup> studentsGroupAll() {
        String query = "StudentsGroupAll";
        out.println(query);
        List<StudentsGroup> list = new ArrayList<>();
        try {
            String response = in.readLine();
            String[] fields = response.split(separator);
            for (int i = 0; i < fields.length; i += 2) {
                Integer id = Integer.parseInt(fields[i]);
                String name = fields[i + 1];
                list.add(new StudentsGroup(id, name));
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Student> studentAll() {
        String query = "StudentAll";
        return getStudents(query);
    }

    public List<Student> studentFindByStudentsGroupId(Integer studentsGroupId) {
        String query = "StudentFindByAirCompanyId" + separator + studentsGroupId.toString();
        return getStudents(query);
    }

    private List<Student> getStudents(String query) {
        out.println(query);
        List<Student> list = new ArrayList<>();
        try {
            String response = in.readLine();
            String[] fields = response.split(separator);
            for (int i = 0; i < fields.length; i += 5) {
                Integer id = Integer.parseInt(fields[i]);
                Integer groupId = Integer.parseInt(fields[i + 1]);
                String name = fields[i + 2];
                short age = Short.parseShort(fields[i + 3]);
                list.add(new Student(id,  groupId, name, age));
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
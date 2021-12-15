package tcp_ip;

import dao.StudentDAO;
import dao.StudentsGroupDAO;
import models.Student;
import models.StudentsGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.apache.activemq.ActiveMQConnectionFactory;
public class Server {
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private MessageConsumer consumer;
    private static final String separator = "#";

    public void start() throws IOException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        try {
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination queueTo = session.createQueue("toClient");
            Destination queueFrom = session.createQueue("fromClient");

            producer = session.createProducer(queueTo);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            consumer = session.createConsumer(queueFrom);

            while (processQuery()) {

            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private boolean processQuery() {
        String response;
        String query = "";
        try {
            Message request = consumer.receive(500);
            if (query == null) {
                return false;
            }

            if (request instanceof TextMessage) {
                TextMessage message = (TextMessage) request;
                query = message.getText();
            } else { return true; }


            String [] fields = query.split(separator);
            if (fields.length == 0) {
                return true;
            } else {
                String action = fields[0];
                StudentsGroup studentsGroup;
                Student student;

                switch (action) {
                    case "StudentsGroupFindById":
                        Integer id = Integer.parseInt(fields[1]);
                        studentsGroup = StudentsGroupDAO.findById(id);
                        response = studentsGroup.getName();
                        TextMessage message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "StudentFindByStudentsGroupId":
                        id = Integer.parseInt(fields[1]);
                        List<Student> list = StudentDAO.findByStudentsGroupId(id);
                        StringBuilder str = new StringBuilder();
                        assert list != null;
                        studentsToString(str, list);
                        response = str.toString();
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "StudentsGroupFindByName":
                        String name = fields[1];
                        studentsGroup = StudentsGroupDAO.findByName(name);
                        assert studentsGroup != null;
                        response = studentsGroup.getId() + "";
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "StudentUpdate":
                        id = Integer.parseInt(fields[1]);
                        Integer groupId = Integer.parseInt(fields[2]);
                        name = fields[3];
                        short age =Short.parseShort(fields[4]);

                        student = new Student(id, groupId, name, age);
                        if (StudentDAO.update(student))
                            response = "true";
                        else
                            response = "false";
                        System.out.println(response);
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "StudentsGroupUpdate":
                        id = Integer.parseInt(fields[1]);
                        name = fields[2];
                        studentsGroup = new StudentsGroup(id, name);
                        if (StudentsGroupDAO.update(studentsGroup)) {
                            response = "true";
                        } else {
                            response = "false";
                        }
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "StudentInsert":
                        groupId = Integer.parseInt(fields[2]);
                        name = fields[3];
                        age = Short.parseShort(fields[4]);
                        student = new Student(0,  groupId, name, age);
                        if (StudentDAO.insert(student)) {
                            response = "true";
                        } else {
                            response = "false";
                        }
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "StudentsGroupInsert":
                        name = fields[1];
                        studentsGroup = new StudentsGroup();
                        studentsGroup.setName(name);
                        if (StudentsGroupDAO.insert(studentsGroup)) {
                            response = "true";
                        } else {
                            response = "false";
                        }
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "StudentDelete":
                        id = Integer.parseInt(fields[1]);
                        student = new Student();
                        student.setId(id);
                        if (StudentDAO.delete(student)) {
                            response = "true";
                        } else {
                            response = "false";
                        }
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "StudentsGroupDelete":
                        id = Integer.parseInt(fields[1]);
                        studentsGroup = new StudentsGroup();
                        studentsGroup.setId(id);
                        if (StudentsGroupDAO.delete(studentsGroup)) {
                            response = "true";
                        } else {
                            response = "false";
                        }
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "StudentAll":
                        List<Student> studentList = StudentDAO.findAll();
                        str = new StringBuilder();
                        assert studentList != null;
                        studentsToString(str, studentList);
                        response = str.toString();
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                    case "StudentsGroupAll":
                        List<StudentsGroup> studentsGroupList = StudentsGroupDAO.findAll();
                        str = new StringBuilder();
                        for (StudentsGroup group : studentsGroupList) {
                            str.append(group.getId());
                            str.append(separator);
                            str.append(group.getName());
                            str.append(separator);
                        }
                        response = str.toString();
                        message = session.createTextMessage(response);
                        producer.send(message);
                        break;
                }
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    private void studentsToString(StringBuilder str, List<Student> list) {
        for (Student student : list) {
            str.append(student.getId());
            str.append(separator);
            str.append(student.getGroupId());
            str.append(separator);
            str.append(student.getName());
            str.append(separator);
            str.append(student.getAge());
            str.append(separator);
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start(5433);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
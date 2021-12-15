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

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private MessageConsumer consumer;
    private static final String separator = "#";

    public Client(){
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        try {
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination queueOut = session.createQueue("fromClient");
            Destination queueIn = session.createQueue("toClient");

            producer = session.createProducer(queueOut);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            consumer = session.createConsumer(queueIn);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private String handleMessage(String query, int timeout) throws JMSException {
        TextMessage message = session.createTextMessage(query);
        producer.send(message);
        Message mes = consumer.receive(timeout);
        if (mes == null) {
            return null;
        }

        if (mes instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) mes;
            return textMessage.getText();
        }

        return "";
    }

    public StudentsGroup studentsGroupFindById(Integer id) {
        String query = "StudentsGroupFindById" + separator + id.toString();
        try {
            String response = handleMessage(query, 15000);
            return new StudentsGroup(id, response);
        } catch (JMSException  e) {
            e.printStackTrace();
        }
        return null;
    }

    public StudentsGroup studentsGroupFindByName(String name) {
        String query = "StudentsGroupFindByName" + separator + name;

        try {
            String response = handleMessage(query, 15000);
            Integer responseId = Integer.parseInt(response);
            return new StudentsGroup(responseId, name);
        } catch (JMSException  e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean studentUpdate(Student student) {
        String query = "StudentUpdate" + separator + student.getId() + separator + student.getGroupId() + separator
                + student.getName() + separator + student.getAge();

        try {
            String response = handleMessage(query, 15000);
            return "true".equals(response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean studentsGroupUpdate(StudentsGroup studentsGroup) {
        String query = "StudentsGroupUpdate" + separator + studentsGroup.getId() +
                separator + studentsGroup.getName();
        try {
            String response = handleMessage(query, 15000);
            return "true".equals(response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean studentInsert(Student student) {
        String query = "StudentInsert" +
                separator +  student.getId() + separator + student.getGroupId() + separator
                + student.getName() + separator + student.getAge();
        try {
            String response = handleMessage(query, 15000);
            return "true".equals(response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean studentsGroupInsert(StudentsGroup studentsGroup) {
        String query = "StudentsGroupInsert" +
                separator + studentsGroup.getName();
        try {
            String response = handleMessage(query, 15000);
            return "true".equals(response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean studentsGroupDelete(StudentsGroup studentsGroup) {
        String query = "StudentsGroupDelete" + separator + studentsGroup.getId();
        try {
            String response = handleMessage(query, 15000);
            return "true".equals(response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean studentDelete(Student student) {
        String query = "StudentDelete" + separator + student.getId();
        try {
            String response = handleMessage(query, 15000);;
            return "true".equals(response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<StudentsGroup> studentsGroupAll() {
        String query = "StudentsGroupAll";
        List<StudentsGroup> list = new ArrayList<>();
        try {
            String response = handleMessage(query, 15000);
            String[] fields = response.split(separator);
            for (int i = 0; i < fields.length; i += 2) {
                Integer id = Integer.parseInt(fields[i]);
                String name = fields[i + 1];
                list.add(new StudentsGroup(id, name));
            }
            return list;
        } catch (JMSException  e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Student> studentAll() {
        String query = "StudentAll";
        return getStudents(query);
    }

    public List<Student> studentFindByStudentsGroupId(Integer studentsGroupId) {
        String query = "StudentFindByStudentsGroupId" + separator + studentsGroupId.toString();
        return getStudents(query);
    }

    private List<Student> getStudents(String query) {
        List<Student> list = new ArrayList<>();
        try {
            String response = handleMessage(query, 15000);
            String[] fields = response.split(separator);
            for (int i = 0; i < fields.length; i += 5) {
                Integer id = Integer.parseInt(fields[i]);
                Integer groupId = Integer.parseInt(fields[i + 1]);
                String name = fields[i + 2];
                short age = Short.parseShort(fields[i + 3]);
                list.add(new Student(id,  groupId, name, age));
            }
            return list;
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void disconnect() {
        try {
            session.close();
            connection.close();
        } catch (JMSException  e) {
            e.printStackTrace();
        }
    }
}
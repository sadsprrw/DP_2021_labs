package xml;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import models.StudiesDepartment;
import models.StudentsGroup;
import models.Student;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class DOMParser {
    public static class SimpleErrorHandler implements ErrorHandler {
        public void warning(SAXParseException e) throws SAXException {
            System.out.println("Line " +e.getLineNumber() + ":");
            System.out.println(e.getMessage());
        }
        public void error(SAXParseException e) throws SAXException {
            System.out.println("Line " +e.getLineNumber() + ":");
            System.out.println(e.getMessage());
        }
        public void fatalError(SAXParseException e) throws SAXException {
            System.out.println("Line " +e.getLineNumber() + ":");
            System.out.println(e.getMessage());
        }
    }

    public static StudiesDepartment parse(String path) throws ParserConfigurationException, SAXException, IOException {
        SchemaFactory sf =
                SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        Schema s = sf.newSchema(new File("src/main/java/xml/input.xsd"));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setSchema(s);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new SimpleErrorHandler());
        Document doc = builder.parse(new File(path));
        doc.getDocumentElement().normalize();

        StudiesDepartment studiesDepartment = new StudiesDepartment();
        NodeList nodes = doc.getElementsByTagName("StudentsGroup");
        for(int i = 0; i < nodes.getLength(); ++i) {
            Element n = (Element)nodes.item(i);
            StudentsGroup group = new StudentsGroup();
            group.setId(Integer.parseInt(n.getAttribute("id")));
            group.setName(n.getAttribute("name"));
            studiesDepartment.addStudentsGroup(group);

        }

        nodes = doc.getElementsByTagName("Student");
        for(int j =0; j < nodes.getLength(); ++j) {
            Element e = (Element) nodes.item(j);
            Student student = new Student();
            student.setId(Integer.parseInt(e.getAttribute("id")));
            student.setGroupId(Integer.parseInt(e.getAttribute("groupId")));
            student.setName(e.getAttribute("name"));
            student.setAge(Short.parseShort(e.getAttribute("age")));
            studiesDepartment.addStudent(student);
        }

        return studiesDepartment;
    }

    public static void write(StudiesDepartment studiesDepartment, String path) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElement("StudiesDepartment");
        doc.appendChild(root);

        List<StudentsGroup> groups = studiesDepartment.getGroups();
        for(StudentsGroup group :  groups) {
            Element grp = doc.createElement("StudentsGroup");
            grp.setAttribute("id", Long.toString(group.getId()));
            grp.setAttribute("name", group.getName());
            root.appendChild(grp);

            for(Student student: group.getStudents()) {
                Element s = doc.createElement("Student");
                s.setAttribute("id", Integer.toString(student.getId()));
                s.setAttribute("groupId", Integer.toString(student.getGroupId()));
                s.setAttribute("name", student.getName());
                s.setAttribute("age", Short.toString(student.getAge()));
                grp.appendChild(s);
            }
        }
        Source domSource = new DOMSource(doc);
        Result fileResult = new StreamResult(new File(path));
        TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer transformer = tfactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(domSource, fileResult);
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        StudiesDepartment studiesDepartment = parse("src/main/java/xml/input.xml");
        write(studiesDepartment,"src/main/java/xml/output.xml");
    }
}
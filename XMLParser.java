import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {
    // File name to store students data
    private static final String fileName = "University.xml";

    // Load students data from an XML file
    public List<Student> loadStudents() throws Exception {
        // Create a DocumentBuilder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Declare the Document object to be used to create the XML file
        Document document;

        // Declare the root element <University>
        Element university;

        // File object to check for the existence of the file
        File inputFile = new File(fileName);

        // Create a new xml file or parse an existing one
        if (inputFile.exists()) {
            document = builder.parse(inputFile);
            university = document.getDocumentElement();
        } else {
            return null;
        }

        // Get the list of all <Student> elements
        NodeList students = university.getElementsByTagName("Student");

        // Initialize the students list to return
        List<Student> studentList = new ArrayList<>();

        // Iterate over all elements inside the <University> element
        for (int i = 0; i < students.getLength(); i++) {
            Node student = students.item(i);
            Element element = (Element) student;
            studentList.add(new Student(
                student.getAttributes().getNamedItem("ID").getNodeValue(),
                element.getElementsByTagName("FirstName").item(0).getTextContent(),
                element.getElementsByTagName("LastName").item(0).getTextContent(),
                element.getElementsByTagName("Gender").item(0).getTextContent(),
                Double.parseDouble(element.getElementsByTagName("GPA").item(0).getTextContent()),
                Integer.parseInt(element.getElementsByTagName("Level").item(0).getTextContent()),
                element.getElementsByTagName("Address").item(0).getTextContent()));
        }
        if(studentList.size() > 0) {
            return studentList;
        }
        return null;
    }
}

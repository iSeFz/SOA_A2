import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLWriter {
    // File name to store students data
    private static final String fileName = "University.xml";

    // Store the added students data to an XML file
    public void storeStudentToXML(Student student) throws Exception {
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
            document = builder.newDocument();
            university = document.createElement("University");
            document.appendChild(university);
        }

        // Create student element and set attributes
        Element newStudent = document.createElement("Student");
        document.createAttribute("ID");
        newStudent.setAttribute("ID", student.getID());

        // Create child elements (student details)
        Element firstName = document.createElement("FirstName");
        firstName.appendChild(document.createTextNode(student.getFirstName()));
        newStudent.appendChild(firstName);

        Element lastName = document.createElement("LastName");
        lastName.appendChild(document.createTextNode(student.getLastName()));
        newStudent.appendChild(lastName);

        Element gender = document.createElement("Gender");
        gender.appendChild(document.createTextNode(student.getGender()));
        newStudent.appendChild(gender);

        Element gpa = document.createElement("GPA");
        gpa.appendChild(document.createTextNode(student.getGPA().toString()));
        newStudent.appendChild(gpa);

        Element level = document.createElement("Level");
        level.appendChild(document.createTextNode(student.getLevel().toString()));
        newStudent.appendChild(level);

        Element address = document.createElement("Address");
        address.appendChild(document.createTextNode(student.getAddress()));
        newStudent.appendChild(address);

        // Append the newStudent element to the root element
        university.appendChild(newStudent);

        // Write to XML file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(document);

        // Specify your local file path
        StreamResult result = new StreamResult(fileName);
        transformer.transform(source, result);
    }

}

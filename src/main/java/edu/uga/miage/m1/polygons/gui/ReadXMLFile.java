package edu.uga.miage.m1.polygons.gui;

import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static edu.uga.miage.m1.polygons.gui.JDrawingFrame.logger;

public class ReadXMLFile {

    File selectedFile;
    private transient ArrayList<Circle> circleShape = new ArrayList<>();
    private transient ArrayList<Triangle> triangleShape = new ArrayList<>();
    private transient ArrayList<Square> squareShape = new ArrayList<>();

    public ReadXMLFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    public void getShapes() throws ParserConfigurationException {
        //creating a constructor of file class and parsing an XML file
        File file = new File(selectedFile.toURI());
        //an instance of factory that gives a document builder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //REDHAT
        //https://www.blackhat.com/docs/us-15/materials/us-15-Wang-FileCry-The-New-Age-Of-XXE-java-wp.pdf
        factory.setAttribute(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        //OWASP
        //https://cheatsheetseries.owasp.org/cheatsheets/XML_External_Entity_Prevention_Cheat_Sheet.html
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        // Disable external DTDs as well
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        // and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);

        //an instance of builder to parse the specified xml file
        DocumentBuilder db = null;
        try {
            db = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            logger.log(Level.SEVERE, e, () -> "An error occurred when newDocumentBuilder at getShapes(). " + e);
        }
        Document doc = null;
        try {
            doc = db.parse(file);
        } catch (SAXException | IOException e) {
            logger.log(Level.SEVERE, e, () -> "An error occurred when parse at getShapes(). " + e);
        }
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("shape");
        // nodeList is not iterable, so we are using for loop
        for (int itr = 0; itr < nodeList.getLength(); itr++) {
            Node node = nodeList.item(itr);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                switch (eElement.getElementsByTagName("type").item(0).getTextContent()) {
                    case "circle":
                        circleShape.add(new Circle(Integer.parseInt(eElement.getElementsByTagName("x").item(0).getTextContent()), Integer.parseInt(eElement.getElementsByTagName("y").item(0).getTextContent())));
                        break;
                    case "triangle":
                        triangleShape.add(new Triangle(Integer.parseInt(eElement.getElementsByTagName("x").item(0).getTextContent()), Integer.parseInt(eElement.getElementsByTagName("y").item(0).getTextContent())));
                        break;
                    case "square":
                        squareShape.add(new Square(Integer.parseInt(eElement.getElementsByTagName("x").item(0).getTextContent()), Integer.parseInt(eElement.getElementsByTagName("y").item(0).getTextContent())));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public List<Circle> getCircleShape() {
        return circleShape;
    }

    public List<Square> getSquareShape() {
        return squareShape;
    }

    public List<Triangle> getTriangleShape() {
        return triangleShape;
    }
}
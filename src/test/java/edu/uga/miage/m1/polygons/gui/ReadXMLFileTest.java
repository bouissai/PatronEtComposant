package edu.uga.miage.m1.polygons.gui;

import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;
import org.junit.jupiter.api.Test;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReadXMLFileTest {

    @Test
    void test_getShapes() throws ParserConfigurationException {
        Circle expectedCircle = new Circle(69, 90);
        Square expectedSquare = new Square(187, 77);
        Triangle expectedTriangle = new Triangle(324, 162);

        List expectedListCircle = List.of(expectedCircle);
        List expectedListSquare = List.of(expectedSquare);
        List expectedListTriangle = List.of(expectedTriangle);

        String path_testFile = "src/test/resources/shapesTest.xml";
        ReadXMLFile r = new ReadXMLFile(new File(path_testFile));
        r.getShapes();

        r.getCircleShape().equals(expectedListCircle);
        r.getTriangleShape().equals(expectedListTriangle);
        r.getSquareShape().equals( expectedListSquare);

    }
}

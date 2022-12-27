package edu.uga.miage.m1.polygons.gui;

import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.List;

class ReadXMLFileTest {

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

        Assertions.assertEquals(((Circle)expectedListCircle.get(0)).getX(),r.getCircleShape().get(0).getX());
        Assertions.assertEquals(((Circle)expectedListCircle.get(0)).getY(),r.getCircleShape().get(0).getY());

        Assertions.assertEquals(((Square)expectedListSquare.get(0)).getX(),r.getSquareShape().get(0).getX());
        Assertions.assertEquals(((Square)expectedListSquare.get(0)).getY(),r.getSquareShape().get(0).getY());

        Assertions.assertEquals(((Triangle)expectedListTriangle.get(0)).getX(),r.getTriangleShape().get(0).getX());
        Assertions.assertEquals(((Triangle)expectedListTriangle.get(0)).getY(),r.getTriangleShape().get(0).getY());
    }


}

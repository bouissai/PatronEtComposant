package edu.uga.miage.m1.polygons.gui.shapes;

import edu.uga.miage.m1.polygons.gui.ReadXMLFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShapeFactoryTest {
    ShapeFactory shapeFactory =  new ShapeFactory();
    @Test
    void test_getShapes_circle() {
        SimpleShape circleFromFactory = shapeFactory.getShape("Circle",2,3);
        Assertions.assertEquals(2,circleFromFactory.getX());
        Assertions.assertEquals(3,circleFromFactory.getY());
    }

    @Test
    void test_getShapes_triangle() {
        SimpleShape triangleFromFactory = shapeFactory.getShape("Triangle",2,3);
        Assertions.assertEquals(2,triangleFromFactory.getX());
        Assertions.assertEquals(3,triangleFromFactory.getY());
    }

    @Test
    void test_getShapes_square() {
        SimpleShape squareFromFactory = shapeFactory.getShape("Square",2,3);
        Assertions.assertEquals(2,squareFromFactory.getX());
        Assertions.assertEquals(3,squareFromFactory.getY());
    }
    @Test
    void test_getShapes_group() {
        SimpleShape groupFromFactory = shapeFactory.getShape("Group",2,3);
        Assertions.assertEquals(2,groupFromFactory.getX());
        Assertions.assertEquals(3,groupFromFactory.getY());
    }

    @Test
    void test_getShapes_error() {
        SimpleShape groupFromFactory = shapeFactory.getShape("Losange",2,3);
        Assertions.assertEquals(null,groupFromFactory);
    }

}

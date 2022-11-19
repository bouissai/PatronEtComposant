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
        Assertions.assertEquals(circleFromFactory.getX(),2);
        Assertions.assertEquals(circleFromFactory.getY(),3);
    }

    @Test
    void test_getShapes_triangle() {
        SimpleShape circleFromFactory = shapeFactory.getShape("Triangle",2,3);
        Assertions.assertEquals(circleFromFactory.getX(),2);
        Assertions.assertEquals(circleFromFactory.getY(),3);
    }

    @Test
    void test_getShapes_square() {
        SimpleShape circleFromFactory = shapeFactory.getShape("Square",2,3);
        Assertions.assertEquals(circleFromFactory.getX(),2);
        Assertions.assertEquals(circleFromFactory.getY(),3);
    }
}

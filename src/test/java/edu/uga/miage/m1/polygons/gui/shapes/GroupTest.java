package edu.uga.miage.m1.polygons.gui.shapes;

import edu.uga.miage.m1.polygons.gui.persistence.Visitor;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GroupTest {

    @Test
    void test_getters() {
        Group g = new Group(0, 0);
        assertEquals(0, g.getX());
        assertEquals(0, g.getY());
    }
    @Test
    void test_draw() {
        Group g = new Group(0, 0);
        Graphics2D gMocks = mock(Graphics2D.class);
        g.addShape(new Square(0,0));
        g.addShape(new Circle(0,0));
        g.draw(gMocks);
        verify(gMocks,times(2)).draw(any());
    }

    @Test
    void test_accept() {
        Group g = new Group(0, 0);
        Visitor gMocks = mock(Visitor.class);
        g.accept(gMocks);
        verify(gMocks,times(1))
                .visit(g);
    }

    @Test
    void test_getter_list() {
        Triangle t = new Triangle(0,0);
        Group g = new Group(0, 0);
        ArrayList<SimpleShape> list = new ArrayList<>();
        list.add(t);
        g.addShape(t);
        assertEquals(list, g.getListGroup());
    }

    @Test
    void test_getter_type() {
        Group g = new Group(0, 0);
        assertEquals("group", g.getType());
    }

    @Test
    void test_getter_x1() {
        Group g = new Group(0, 0);
        assertEquals(0, g.getX1());
    }

    @Test
    void test_getter_x2() {
        Group g = new Group(0, 0);
        assertEquals(0, g.getX2());
    }

    @Test
    void test_getter_y1() {
        Group g = new Group(0, 0);
        assertEquals(0, g.getY1());
    }
    @Test
    void test_getter_y2() {
        Group g = new Group(0, 0);
        assertEquals(0, g.getY2());
    }

    @Test
    void test_setter_x1() {
        Group g = new Group(0, 0);
        g.setX1(2);
        assertEquals(2, g.getX1());
    }

    @Test
    void test_setter_x2() {
        Group g = new Group(0, 0);
        g.setX2(2);
        assertEquals(2, g.getX2());
    }

    @Test
    void test_setter_y1() {
        Group g = new Group(0, 0);
        g.setY1(2);
        assertEquals(2, g.getY1());
    }
    @Test
    void test_setter_y2() {
        Group g = new Group(0, 0);
        g.setY2(2);
        assertEquals(2, g.getY2());
    }
    @Test
    void test_updateCoordMinMax() {
        // Create a group with two shapes
        Group group = new Group(0,0);
        Triangle shape1 = new Triangle(0, 0);
        Triangle shape2 = new Triangle(10, 10);
        group.addShape(shape1);
        group.addShape(shape2);

        // Call the updateCoordMinMax method
        group.updateCoordMinMax();

        // Verify that the minimum and maximum coordinates are correct
        assertEquals(-25, group.getX1());
        assertEquals(-25, group.getY1());
        assertEquals(35, group.getX2());
        assertEquals(35, group.getY2());
    }

    @Test
    void test_updateCoordMinMax_with_nested_group() {
        // Create a group with a nested group and two shapes
        Group group = new Group(0,0);
        Group nestedGroup = new Group(0,0);
        Triangle shape1 = new Triangle(0, 0);
        Triangle shape2 = new Triangle(10, 10);
        nestedGroup.addShape(shape1);
        nestedGroup.addShape(shape2);
        group.addShape(nestedGroup);

        // Call the updateCoordMinMax method
        group.updateCoordMinMax();

        // Verify that the minimum and maximum coordinates are correct
        assertEquals(-25, group.getX1());
        assertEquals(-25, group.getY1());
        assertEquals(35, group.getX2());
        assertEquals(35, group.getY2());
    }

    @Test
    void test_moveGroup() {
        // Create a group with two shapes
        Group group = new Group(0,0);
        Triangle shape1 = new Triangle(0, 0);
        Triangle shape2 = new Triangle(10, 10);
        group.addShape(shape1);
        group.addShape(shape2);

        // Call the moveGroup method
        group.moveGroup(5, 5);

        // Verify that the shapes have been moved by the correct amount
        assertEquals(5, shape1.getX());
        assertEquals(5, shape1.getY());
        assertEquals(15, shape2.getX());
        assertEquals(15, shape2.getY());
    }

    @Test
    void test_moveGroup_with_nested_group() {
        // Create a group with a nested group and two shapes
        Group group = new Group(0,0);
        Group nestedGroup = new Group(0,0);
        Triangle shape1 = new Triangle(0, 0);
        Triangle shape2 = new Triangle(10, 10);
        nestedGroup.addShape(shape1);
        nestedGroup.addShape(shape2);
        group.addShape(nestedGroup);

        // Call the moveGroup method
        group.moveGroup(5, 5);

        // Verify that the shapes and nested group have been moved by the correct amount
        assertEquals(5, shape1.getX());
        assertEquals(5, shape1.getY());
        assertEquals(15, shape2.getX());
        assertEquals(15, shape2.getY());
        assertEquals(-20, nestedGroup.getX1());
        assertEquals(-20, nestedGroup.getY1());
        assertEquals(40, nestedGroup.getX2());
        assertEquals(40, nestedGroup.getY2());
    }
}

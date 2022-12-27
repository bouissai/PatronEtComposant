package edu.uga.miage.m1.polygons.gui.shapes;

import org.junit.jupiter.api.Test;

import java.awt.*;

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
}

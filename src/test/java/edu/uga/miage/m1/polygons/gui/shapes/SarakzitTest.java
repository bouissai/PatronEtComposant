package edu.uga.miage.m1.polygons.gui.shapes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import edu.uga.miage.m1.polygons.gui.persistence.Visitor;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

class SarakzitTest {
    @Test
    void test_getters() {
        Sarakzit s = new Sarakzit(0, 0);
        assertEquals(0, s.getX());
        assertEquals(0, s.getY());
    }
    @Test
    void test_draw() {
        Sarakzit sarakzit = new Sarakzit(0, 0);
        Graphics2D gMocks = mock(Graphics2D.class);
        sarakzit.draw(gMocks);
        verify(gMocks,times(1))
                .drawImage((Image) any(), anyInt(), anyInt(),(ImageObserver)any());
    }

    @Test
    void test_getter_type() {
        Sarakzit s = new Sarakzit(0, 0);
        assertEquals("sarakzit", s.getType());
    }
    @Test
    void test_accept() {
        Sarakzit s = new Sarakzit(0, 0);
        Visitor gMocks = mock(Visitor.class);
        s.accept(gMocks);
        verify(gMocks,times(1))
                .visit(s);
    }

}

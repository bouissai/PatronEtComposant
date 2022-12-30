package edu.uga.miage.m1.polygons.gui.shapes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import java.awt.*;

class SarakzitTest {
    @Test
    void test_getters() {
        Sarakzit s = new Sarakzit(0, 0);
        assertEquals(-25, s.getX());
        assertEquals(-25, s.getY());
    }



}

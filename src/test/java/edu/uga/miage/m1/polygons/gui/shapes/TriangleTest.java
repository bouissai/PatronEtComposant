package edu.uga.miage.m1.polygons.gui.shapes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import java.awt.*;

class TriangleTest {

	@Test
	void test_getters() {
		Triangle t = new Triangle(0, 0);
		assertEquals(0, t.getX());
		assertEquals(0, t.getY());
	}

	@Test
	void test_draw() {
		Triangle t = new Triangle(0, 0);
		Graphics2D gMocks = mock(Graphics2D.class);
		t.draw(gMocks);
		verify(gMocks,times(1)).draw(any());
	}
}

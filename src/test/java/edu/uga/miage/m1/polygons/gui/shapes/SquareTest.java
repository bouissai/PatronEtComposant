package edu.uga.miage.m1.polygons.gui.shapes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import java.awt.*;

class SquareTest {
	@Test
	void test_getters() {
		Square s = new Square(0, 0);
		assertEquals(0, s.getX());
		assertEquals(0, s.getY());
	}
	@Test
	void test_draw() {
		Square s = new Square(0, 0);
		Graphics2D gMocks = mock(Graphics2D.class);
		s.draw(gMocks);
		verify(gMocks,times(1)).draw(any());
	}



}

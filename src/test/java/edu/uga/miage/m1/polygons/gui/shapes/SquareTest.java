package edu.uga.miage.m1.polygons.gui.shapes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SquareTest {

	@Test
	void test_getters() {
		Square s = new Square(0, 0);
		assertEquals(0, s.getX());
		assertEquals(0, s.getY());
	}

}

package edu.uga.miage.m1.polygons.gui.shapes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.uga.miage.m1.polygons.gui.persistence.Visitor;

import java.awt.*;

@ExtendWith(MockitoExtension.class)
class CircleTest {
	
	private int counterVisitorCircle;

	@Test
	void test_getters() {
		Circle c = new Circle(0, 0);
		assertEquals(0, c.getX());
		assertEquals(0, c.getY());
	}
	
	@Test
	void test_visit_with_mock(@Mock Visitor v) {
		Circle c = new Circle(0, 0);		
		c.accept(v);
		verify(v, times(1)).visit(c);
	}

	@Test
	void test_visit_without_mockito() {
		Circle c = new Circle(0, 0);
		Visitor v = new Visitor() {

			@Override
			public void visit(SimpleShape circle) {
				counterVisitorCircle++;
			}
		};
		
		c.accept(v);
		assertEquals(1, counterVisitorCircle);
	}
	@Test
	void test_draw() {
		Circle c = new Circle(0, 0);
		Graphics2D gMocks = mock(Graphics2D.class);
		c.draw(gMocks);
		verify(gMocks,times(1)).draw(any());
	}
}

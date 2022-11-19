package edu.uga.miage.m1.polygons.gui.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.junit.jupiter.api.Test;

import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;

/**
 *  @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 *
 */
class JSonVisitorTest {

	@Test
	void test_circle_visitor() {
		var c = new Circle(0, 0);
		String representation = 
				String.format("{\"type\": \"%s\", \"x\": %d,\"y\": %d}", "circle", c.getX(), c.getY());
		String expectedRepresentation = jsonFromString(representation).toString();
		
		JSonVisitor visitor = new JSonVisitor();
		c.accept(visitor);
		representation = visitor.getRepresentation();
		if (representation == null) {
			fail("The visitor sequence must be implemented for the circle");
		}
		JsonObject jObject = jsonFromString(representation);
		assertEquals(expectedRepresentation, jObject.toString());
	}

	@Test
	void test_triangle_visitor() {
		var t = new Triangle(0, 0);
		String representation = 
				String.format("{\"type\": \"%s\",\"x\": %d,\"y\": %d}", "triangle", t.getX(), t.getY());
		String expectedRepresentation = jsonFromString(representation).toString();
		
		JSonVisitor visitor = new JSonVisitor();
		t.accept(visitor);
		representation = visitor.getRepresentation();
		if (representation == null) {
			fail("The visitor sequence must be implemented for the triangle");
		}
		JsonObject jObject = jsonFromString(representation);
		assertEquals(expectedRepresentation, jObject.toString());
	}

	@Test
	void test_square_visitor() {
		var s = new Square(0, 0);
		String representation = 
				String.format("{\"type\": \"%s\", \"x\": %d,\"y\": %d}", "square", s.getX(), s.getY());
		String expectedRepresentation = jsonFromString(representation).toString();
		
		JSonVisitor visitor = new JSonVisitor();
		s.accept(visitor);
		representation = visitor.getRepresentation();
		if (representation == null) {
			fail("The visitor sequence must be implemented for the square");
		}
		JsonObject jObject = jsonFromString(representation);
		assertEquals(expectedRepresentation, jObject.toString());
	}

	private static JsonObject jsonFromString(String jsonObjectStr) {

	    JsonReader jsonReader = Json.createReader(new StringReader(jsonObjectStr));
	    JsonObject object = jsonReader.readObject();
	    jsonReader.close();

	    return object;
	}
}

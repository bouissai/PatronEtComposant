package edu.uga.miage.m1.polygons.gui.shapes;

public class ShapeFactory {
    //use getShape method to get object of type shape
    public SimpleShape getShape(String shapeType, int x, int y) {
        if (shapeType == null) {
            return null;
        }
        if (shapeType.equalsIgnoreCase("CIRCLE")) {
            return new Circle(x, y);

        } else if (shapeType.equalsIgnoreCase("TRIANGLE")) {
            return new Triangle(x, y);

        } else if (shapeType.equalsIgnoreCase("SQUARE")) {
            return new Square(x, y);
        } else if (shapeType.equalsIgnoreCase("GROUP")) {
            return new Group(x, y);
        }
        return null;
    }
}

package edu.uga.miage.m1.polygons.gui.shapes;

public class ShapeFactory {
    //use getShape method to get object of type shape
    public SimpleShape getShape(String shapeType, int x, int y) {
        switch (shapeType.toUpperCase()) {
            case "CIRCLE":
                return new Circle(x, y);
            case "TRIANGLE":
                return new Triangle(x, y);
            case "SQUARE":
                return new Square(x, y);
            case "GROUP":
                return new Group(x, y);
            case "SARAKZIT":
                return new Sarakzit(x, y);
            default:
                return null;
        }
    }
}

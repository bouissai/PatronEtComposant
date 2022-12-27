package edu.uga.miage.m1.polygons.gui.shapes;

import edu.uga.miage.m1.polygons.gui.persistence.Visitable;
import edu.uga.miage.m1.polygons.gui.persistence.Visitor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Group extends SimpleShape implements Visitable {
    public static final String TYPE_GROUP = "group";
    private ArrayList<SimpleShape> myGroup = new ArrayList<>();

    private int x1;
    private int y1;
    private int x2;
    private int y2;

    public Group(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics2D g2) {
        myGroup.forEach(shape -> shape.draw(g2));
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getType() {
        return TYPE_GROUP;
    }


    public ArrayList<SimpleShape> getListGroup() {
        return myGroup;
    }

    public void addShape(SimpleShape shape) {
        myGroup.add(shape);
    }

    public void removeShape(SimpleShape shape) {
        myGroup.remove(shape);
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public void setCoordMinMax(int x1, int y1, int x2, int y2) {
        setX1(x1);
        setY1(y1);
        setX2(x2);
        setY2(y2);
        int coordCenterGroupX = (getX1() + getX2()) / 2;
        int coordCenterGroupY = (getY1() + getY2()) / 2;
        setXY(coordCenterGroupX, coordCenterGroupY);
    }

    public void updateCoordMinMax() {
        if (!getListGroup().isEmpty()) {
            int minX = getX1();
            int minY = getY1();
            int maxX = getX2();
            int maxY = getY2();
            for (SimpleShape simpleShape : getListGroup()) {
                if (Objects.equals(simpleShape.getType(), TYPE_GROUP)) {
                    Group groupShape = ((Group) simpleShape);
                    groupShape.updateCoordMinMax();
                    minX = max(minX, groupShape.getX2());
                    minY = max(minY, groupShape.getY2());
                    maxX = min(maxX, groupShape.getX1());
                    maxY = min(maxY, groupShape.getY1());
                    setCoordMinMax(maxX, maxY, minX, minY);
                } else {
                    minX = max(minX, simpleShape.getX());
                    minY = max(minY, simpleShape.getY());
                    maxX = min(maxX, simpleShape.getX());
                    maxY = min(maxY, simpleShape.getY());
                    setCoordMinMax(maxX - 25, maxY - 25, minX + 25, minY + 25);
                }
            }
        }
    }

    public void moveGroup(int translationX, int translationY) {
        myGroup.forEach(shape -> {
            if (shape.getType().equals(TYPE_GROUP)) {
                ((Group) shape).moveGroup(translationX, translationY);
            } else {
                shape.setXY(shape.getX() + translationX, shape.getY() + translationY);
            }
        });
        updateCoordMinMax();
    }
}

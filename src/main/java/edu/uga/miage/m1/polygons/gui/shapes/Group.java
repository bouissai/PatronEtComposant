package edu.uga.miage.m1.polygons.gui.shapes;

import edu.uga.miage.m1.polygons.gui.persistence.Visitable;
import edu.uga.miage.m1.polygons.gui.persistence.Visitor;

import java.awt.*;
import java.util.ArrayList;

public class Group extends SimpleShape implements Visitable {

    private ArrayList<SimpleShape> myGroup = new ArrayList<>();

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
        return "group";
    }

    @Override
    public int getX() {
        return super.getX();
    }

    @Override
    public int getY() {
        return super.getY();
    }

    @Override
    public void setXY(int x, int y) {
        super.setXY(x, y);
    }

    public void setListGroup(ArrayList<SimpleShape> group) {
        this.myGroup = group;
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

    public void updateCoordMinMax() {

    }
}

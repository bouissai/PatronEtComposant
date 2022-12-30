package edu.uga.miage.m1.polygons.gui.shapes;

import edu.uga.miage.m1.polygons.gui.persistence.Visitable;
import edu.uga.miage.m1.polygons.gui.persistence.Visitor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class Sarakzit extends SimpleShape implements Visitable {

    public Sarakzit(int x, int y) {
        super(x, y);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void draw(Graphics2D g2) {
        try {
            File input = new File("src/main/resources/edu/uga/miage/m1/polygons/gui/images/sarakzit.png");
            BufferedImage image = ImageIO.read(input);
            g2.drawImage( image, getX()-25, getY()-25, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getType() {
        return "sarakzit";
    }
}

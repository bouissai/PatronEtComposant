package edu.uga.miage.m1.polygons.gui.persistence;

import edu.uga.miage.m1.polygons.gui.shapes.Group;
import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;

/**
 * @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 */
public class JSonVisitor implements Visitor {

    private String representation = null;
    private static final String SEP_POS_XY = ",\"y\":";

    @Override
    public void visit(SimpleShape simpleShape) {
        representation = getString(simpleShape);
    }

    private String getString(SimpleShape simpleShape) {
        String s = "{\"type\":\"" + simpleShape.getType() + "\",";

        if (simpleShape.getType().equals("group")) {
            s += "\"list\":";
            for (SimpleShape shape : ((Group) simpleShape).getListGroup()) {
                s += this.getString(shape) + ",";
            }
            s += "}";
        } else {
            s += "\"x\":" + simpleShape.getX() + SEP_POS_XY + simpleShape.getY() + "}";
        }
        return s;
    }

    //@Override
    //public void visit(Circle circle) {
    //    representation =circle\ + circle.getX() + SEP_POS_XY + circle.getY() + "}";
    //}

    //@Override
    //public void visit(Square square) {
    //    representation = "{\"type\":\"square\",\"x\":" + square.getX() + SEP_POS_XY + square.getY() + "}";
    //}

    //@Override
    //public void visit(Triangle triangle) {
    //    representation = "{\"type\":\"triangle\",\"x\":" + triangle.getX() + SEP_POS_XY + triangle.getY() + "}";
    //}

    /**
     * @return the representation in JSon example for a Circle
     *
     * <pre>
     * {@code
     *  {
     *     "shape": {
     *     	  "type": "circle",
     *        "x": -25,
     *        "y": -25
     *     }
     *  }
     * }
     *         </pre>
     */
    public String getRepresentation() {
        return representation;
    }
}

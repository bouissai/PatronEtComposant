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
        StringBuilder s = new StringBuilder("{\"type\":\"" + simpleShape.getType() + "\",");

        if (simpleShape.getType().equals("group")) {
            s.append("\"list\":");
            for (SimpleShape shape : ((Group) simpleShape).getListGroup()) {
                s.append(this.getString(shape)).append(",");
            }
            s.append("}");
        } else {
            s.append("\"x\":").append(simpleShape.getX()).append(SEP_POS_XY).append(simpleShape.getY()).append("}");
        }
        return s.toString();
    }

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

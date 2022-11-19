package edu.uga.miage.m1.polygons.gui.persistence;

import edu.uga.miage.m1.polygons.gui.shapes.Group;
import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;

/**
 * @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 */
public class XMLVisitor implements Visitor {

    private String representation = null;
    private static final String SEP_POS_XY = "</x><y>";
    private static final String END_SHAPE = "</y></shape>";


    @Override
    public void visit(SimpleShape simpleShape) {
        representation = getString(simpleShape);
    }

    private String getString(SimpleShape simpleShape) {
        String s = " <shape><type>" + simpleShape.getType() + "</type>";
        if (simpleShape.getType().equals("group")) {
            s += "<list>";
            for (SimpleShape shape : ((Group) simpleShape).getListGroup()) {
                s += this.getString(shape);
            }
            s += "</list></shape>";
        } else {
            s += "<x>" + simpleShape.getX() + SEP_POS_XY + simpleShape.getY() + END_SHAPE;
        }
        return s;
    }

    /**
     * @return the representation in JSon example for a Triangle:
     *
     * <pre>
     * {@code
     *  <shape>
     *    <type>triangle</type>
     *    <x>-25</x>
     *    <y>-25</y>
     *  </shape>
     * }
     * </pre>
     */
    public String getRepresentation() {
        return representation;
    }
}
package edu.uga.miage.m1.polygons.gui;

import edu.uga.miage.m1.polygons.gui.persistence.JSonVisitor;
import edu.uga.miage.m1.polygons.gui.persistence.XMLVisitor;
import edu.uga.miage.m1.polygons.gui.shapes.Group;
import edu.uga.miage.m1.polygons.gui.shapes.ShapeFactory;
import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Boolean.FALSE;
import static java.lang.Math.max;
import static java.lang.Math.min;


/**
 * This class represents the main application class, which is a JFrame subclass
 * that manages a toolbar of shapes and a drawing canvas.
 *
 * @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 */
public class JDrawingFrame extends JFrame implements MouseListener, MouseMotionListener, Serializable {

    private enum Shapes {
        SQUARE, TRIANGLE, CIRCLE
    }

    JCheckBox checkbox = new JCheckBox("Selection mode", FALSE);
    private transient ShapeFactory shapeFactory = new ShapeFactory();
    @Serial
    private static final long serialVersionUID = 1L;

    private JToolBar mtoolbar;

    private Shapes mselected;

    private JPanel mpanel;

    private JLabel mlabel;

    private transient ActionListener mreusableActionListener = new ShapeActionListener();

    private ArrayList<String> shapeListJsonFormat = new ArrayList<>();
    private ArrayList<String> shapeListXMLFormat = new ArrayList<>();
    private transient ArrayList<SimpleShape> shapes = new ArrayList<>();


    /**
     * Tracks buttons to manage the background.
     */
    private Map<Shapes, JButton> mbuttons = new EnumMap<>(Shapes.class);

    static Logger logger = Logger.getLogger(JDrawingFrame.class.getName());

    private transient XMLVisitor xmlShape = new XMLVisitor();
    private transient JSonVisitor jsonShape = new JSonVisitor();


    private transient SimpleShape lastShapeSelected;

    private Boolean nothingSelected = true;
    private Boolean oneShapeSelected = false;
    private Boolean manyShapesSelected = false;

    private int xPressed;
    private int yPressed;

    private Rectangle2D selectionBox;


    /**
     * Default constructor that populates the main window.
     *
     * @param frameName
     */
    public JDrawingFrame(String frameName) {
        super(frameName);
        // Instantiates components
        mtoolbar = new JToolBar("Toolbar");
        mpanel = new JPanel();
        mpanel.setBackground(Color.WHITE);
        mpanel.setLayout(null);
        mpanel.setMinimumSize(new Dimension(400, 400));
        mpanel.addMouseListener(this);
        mpanel.addMouseMotionListener(this);
        mlabel = new JLabel(" ", javax.swing.SwingConstants.LEFT);
        // Fills the panel
        setLayout(new BorderLayout());
        add(mtoolbar, BorderLayout.NORTH);
        add(mpanel, BorderLayout.CENTER);
        add(mlabel, BorderLayout.SOUTH);
        // Add shapes in the menu
        addShape(Shapes.SQUARE, new ImageIcon(getClass().getResource("images/square.png")));
        addShape(Shapes.TRIANGLE, new ImageIcon(getClass().getResource("images/triangle.png")));
        addShape(Shapes.CIRCLE, new ImageIcon(getClass().getResource("images/circle.png")));
        setPreferredSize(new Dimension(800, 800));
        mtoolbar.add(checkbox);

        JButton buttonJSON = new JButton("Export to JSON");
        buttonJSON.addActionListener(e -> {
            String json = "{\"shapes\": [" + String.join(",", shapeListJsonFormat) + "]}";
            try {
                writeInAFile("shapes.json", json);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, ex, () -> "An error occurred when writeInAFile for Json. " + ex);
            }
        });

        JButton buttonXML = new JButton("Export to XML");
        buttonXML.addActionListener(e -> {
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root><shapes>" + String.join("", shapeListXMLFormat) + "</shapes></root>";
            try {
                writeInAFile("shapes.xml", xml);
            } catch (IOException ex) {
                logger.log(Level.INFO, ex, () -> "Result " + frameName);
            }
        });
        JButton buttonReset = new JButton("Reset");
        buttonReset.addActionListener(e -> {
            shapeListJsonFormat = new ArrayList<>();
            shapeListXMLFormat = new ArrayList<>();
            shapes = new ArrayList<>();
            selectionBox = null;
            repaintGraph();
        });

        mtoolbar.add(buttonReset);
        mtoolbar.add(buttonJSON);
        mtoolbar.add(buttonXML);


        JButton buttonImportXML = new JButton("Import XML");
        buttonImportXML.addActionListener(e -> {
            JFileChooser choose = new JFileChooser();
            choose.setDialogTitle("Choisissez un FICHIER: ");
            choose.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int res = choose.showOpenDialog(null);
            if (res == JFileChooser.APPROVE_OPTION && choose.getSelectedFile().exists()) {
                logger.log(Level.INFO, "Vous avez selectionne le repertoire: {0}", choose.getSelectedFile());
            }
            ReadXMLFile xmldata = new ReadXMLFile(choose.getSelectedFile());
            try {
                xmldata.getShapes();
            } catch (ParserConfigurationException ex) {
                throw new RuntimeException(ex);
            }
            xmldata.getCircleShape().stream().forEach(circle -> {
                Graphics2D g2 = (Graphics2D) mpanel.getGraphics();
                circle.draw(g2);
                circle.accept(xmlShape);
                shapeListXMLFormat.add(xmlShape.getRepresentation());
                shapes.add(circle);
            });
            xmldata.getSquareShape().stream().forEach(square -> {
                Graphics2D g2 = (Graphics2D) mpanel.getGraphics();
                square.draw(g2);
                square.accept(xmlShape);
                shapeListXMLFormat.add(xmlShape.getRepresentation());
                shapes.add(square);
            });
            xmldata.getTriangleShape().stream().forEach(triangle -> {
                Graphics2D g2 = (Graphics2D) mpanel.getGraphics();
                triangle.draw(g2);
                triangle.accept(xmlShape);
                shapeListXMLFormat.add(xmlShape.getRepresentation());
                shapes.add(triangle);
            });

        });
        mtoolbar.add(buttonImportXML);
    }

    /**
     * Write data in a file
     *
     * @param path Path of the file where we will write.
     * @param data Text who will be written in the file.
     */
    public static void writeInAFile(String path, String data) throws IOException {
        createFile(path);
        try (FileOutputStream out = new FileOutputStream(path)) {
            out.write(data.getBytes());
        } catch (Exception e) {
            logger.log(Level.INFO, e, () -> "Result " + path);
        }
    }

    /**
     * Create a new file
     *
     * @param filename name of my new file.
     */

    public static void createFile(String filename) {
        try {
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                logger.log(Level.INFO, "File created: {0}.", myObj.getName());
            } else {
                logger.log(Level.INFO, "File already exists.");
            }
        } catch (IOException e) {
            logger.log(Level.INFO, e, () -> "An error occurred. " + e);
        }
    }

    /**
     * Injects an available <tt>SimpleShape</tt> into the drawing frame.
     *
     * @param name The name of the injected <tt>SimpleShape</tt>.
     * @param icon The icon associated with the injected <tt>SimpleShape</tt>.
     */
    private void addShape(Shapes shape, ImageIcon icon) {
        JButton button = new JButton(icon);
        button.setBorderPainted(false);
        mbuttons.put(shape, button);
        button.setActionCommand(shape.toString());
        button.addActionListener(mreusableActionListener);
        if (mselected == null) {
            button.doClick();
        }
        mtoolbar.add(button);
        mtoolbar.validate();
        repaint();
    }

    /**
     * Implements method for the <tt>MouseListener</tt> interface to
     * draw the selected shape into the drawing canvas.
     *
     * @param evt The associated mouse event.
     */
    public void mouseClicked(MouseEvent evt) {
        logger.info("je clique!!!!!!!!!!!!!!!!!!!!!!");
        if (!checkbox.isSelected()) {
            if (mpanel.contains(evt.getX(), evt.getY())) {
                Graphics2D g2 = (Graphics2D) mpanel.getGraphics();
                switch (mselected) {
                    case CIRCLE -> {
                        SimpleShape c = shapeFactory.getShape("CIRCLE", evt.getX(), evt.getY());
                        c.draw(g2);
                        c.accept(jsonShape);
                        c.accept(xmlShape);
                        shapes.add(c);
                        shapeListJsonFormat.add(jsonShape.getRepresentation());
                        shapeListXMLFormat.add(xmlShape.getRepresentation());
                    }
                    case TRIANGLE -> {
                        SimpleShape t = shapeFactory.getShape("TRIANGLE", evt.getX(), evt.getY());
                        t.draw(g2);
                        t.accept(jsonShape);
                        t.accept(xmlShape);
                        shapes.add(t);
                        shapeListJsonFormat.add(jsonShape.getRepresentation());
                        shapeListXMLFormat.add(xmlShape.getRepresentation());
                    }
                    case SQUARE -> {
                        SimpleShape s = shapeFactory.getShape("SQUARE", evt.getX(), evt.getY());
                        s.draw(g2);
                        s.accept(jsonShape);
                        s.accept(xmlShape);
                        shapes.add(s);
                        shapeListJsonFormat.add(jsonShape.getRepresentation());
                        shapeListXMLFormat.add(xmlShape.getRepresentation());
                    }
                    default -> logger.log(Level.INFO, "No shape named {0} ", mselected);
                }
            }

        }
    }


    /**
     * Implements an empty method for the <tt>MouseListener</tt> interface.
     *
     * @param evt The associated mouse event.
     */
    public void mouseEntered(MouseEvent evt) {

    }

    /**
     * Implements an empty method for the <tt>MouseListener</tt> interface.
     *
     * @param evt The associated mouse event.
     */
    public void mouseExited(MouseEvent evt) {
        mlabel.setText(" ");
        mlabel.repaint();
    }

    /**
     * Implements method for the <tt>MouseListener</tt> interface to initiate
     * shape dragging.
     *
     * @param evt The associated mouse event.
     */
    public void mousePressed(MouseEvent evt) {
        logger.info("je presse ");
        int mouseClickedX = evt.getX();
        int mouseClickedY = evt.getY();
        shapes.forEach(shape -> {
            boolean isClickedOnShape = (mouseClickedX <= shape.getX() + 25) && (mouseClickedX >= shape.getX() - 25) && (mouseClickedY <= shape.getY() + 25) && (mouseClickedY >= shape.getY() - 25);
            if (isClickedOnShape) {
                lastShapeSelected = shape;
                lastShapeSelected.setXY(mouseClickedX, mouseClickedY);
            }
        });

        //j'ai selectionnée aucune figure
        if (lastShapeSelected == null) {
            xPressed = mouseClickedX;
            yPressed = mouseClickedY;
        }

    }

    /**
     * Implements method for the <tt>MouseListener</tt> interface to complete
     * shape dragging.
     *
     * @param evt The associated mouse event.
     */


    public void mouseReleased(MouseEvent evt) {
        int xReleased = evt.getX();
        int yReleased = evt.getY();

        if (lastShapeSelected != null) {
            this.repaintGraph();
            lastShapeSelected.draw((Graphics2D) mpanel.getGraphics());
        } else {
            this.repaintGraph();

            if (checkbox.isSelected()) {

                //on créé une liste de formes contenu dans les coordonnées de la selection
                ArrayList<SimpleShape> shapesSelected = new ArrayList<>();


                //on initialise des coordonnées pour pouvoir redefinir la zone de selection
                int minX = min(xPressed, xReleased);
                int minY = min(yPressed, yReleased);
                int maxX = max(xPressed, xReleased);
                int maxY = max(yPressed, yReleased);

                //on verrie si une forme est contenue dans la zone de selection
                for (SimpleShape shape : shapes) {
                    boolean isClickedOnShape = (shape.getX() <= max(xPressed, xReleased) &&
                            shape.getX() >= min(xPressed, xReleased) &&
                            shape.getY() <= max(yPressed, yReleased) &&
                            shape.getY() >= min(yPressed, yReleased));

                    //si une forme est contenue on fait
                    if (isClickedOnShape) {
                        logger.info("JAI UNE FORME :::::::::");
                        //on redefini la selection
                        minX = max(minX, shape.getX());
                        minY = max(minY, shape.getY());
                        maxX = min(maxX, shape.getX());
                        maxY = min(maxY, shape.getY());

                        shapesSelected.add(shape);

                    }
                }
                ;
                logger.info("j'ai selectionné " + shapesSelected.size() + " formes ");
                if (!shapesSelected.isEmpty()) {

                    //on retire les shapes séléctionné de la liste principale
                    // pour les mettre dans la liste du groupe
                    for (SimpleShape simpleShape : shapesSelected) {
                        shapes.remove(simpleShape);
                    }

                    drawPerfectRect(this.getGraphics(), maxX - 25, maxY - 25, minX + 25, minY + 25);
                    SimpleShape groupSelected = shapeFactory.getShape("group", (maxX + minX) / 2, (maxY + minY) / 2);
                    ((Group) groupSelected).setListGroup(shapesSelected);
                    shapes.add(groupSelected);
                    this.repaintGraph();
                    groupSelected.accept(jsonShape);
                    groupSelected.accept(xmlShape);
                    shapeListJsonFormat.add(jsonShape.getRepresentation());
                    shapeListXMLFormat.add(xmlShape.getRepresentation());
                } else {
                    selectionBox = null;
                    this.repaintGraph();

                }
            }
        }
        lastShapeSelected = null;

    }

    /**
     * Implements method for the <tt>MouseMotionListener</tt> interface to
     * move a dragged shape.
     *
     * @param evt The associated mouse event.
     */
    public void drawPerfectRect(Graphics g, int x, int y, int x2, int y2) {
        int px = min(x, x2);
        int py = min(y, y2);
        int pw = Math.abs(x - x2);
        int ph = Math.abs(y - y2);
        if (selectionBox != null)
            selectionBox.setRect(px, py, pw, ph);
        else {
            selectionBox = new Rectangle2D.Double(px, py, pw, ph);
        }
        //g.drawRect(px, py, pw, ph);
    }

    public void mouseDragged(MouseEvent evt) {
        logger.info("j'attrape ");
        if (checkbox.isSelected()) {
            logger.info("(xP = " + xPressed + ", yP=" + yPressed + ")");
            drawPerfectRect(getGraphics(), evt.getX(), evt.getY(), xPressed, yPressed);
            Graphics2D g2 = (Graphics2D) mpanel.getGraphics();
            if (checkbox.isSelected()) {
                g2.setColor(new Color(100, 100, 100, 100));
                g2.fill(selectionBox);
            }
        }
        //one shape is selected
        if (lastShapeSelected != null) {
            Graphics2D g2 = (Graphics2D) mpanel.getGraphics();
            //make select follow the mouse and repaint
            this.repaintGraph();
            lastShapeSelected.draw((Graphics2D) mpanel.getGraphics());
            lastShapeSelected.setXY(evt.getX(), evt.getY());
            lastShapeSelected.draw(g2);
        } else {
            logger.info("j'ai rien selectionné");
        }
    }

    /**
     * Implements an empty method for the <tt>MouseMotionListener</tt>
     * interface.
     *
     * @param evt The associated mouse event.
     */
    public void mouseMoved(MouseEvent evt) {
        modifyLabel(evt);
    }

    private void modifyLabel(MouseEvent evt) {
        mlabel.setText("(" + evt.getX() + "," + evt.getY() + ")");
    }

    /**
     * Simple action listener for shape tool bar buttons that sets
     * the drawing frame's currently selected shape when receiving
     * an action event.
     */
    private class ShapeActionListener implements ActionListener, Serializable {

        public void actionPerformed(ActionEvent evt) {
            // ItÃ¨re sur tous les boutons
            for (Map.Entry<Shapes, JButton> entry : mbuttons.entrySet()) {
                JButton btn = mbuttons.get(entry.getKey());
                if (evt.getActionCommand().equals(entry.getKey().toString())) {
                    btn.setBorderPainted(true);
                    mselected = entry.getKey();
                } else {
                    btn.setBorderPainted(false);
                }
                btn.repaint();
            }
        }
    }

    public void repaintGraph() {
        Graphics2D g2 = (Graphics2D) mpanel.getGraphics();
        paint(this.getGraphics());
        shapes.forEach(shape -> shape.draw(g2));
        if (checkbox.isSelected()) {
            g2.setColor(new Color(100, 100, 100, 100));
            g2.fill(selectionBox);
        }
    }

}



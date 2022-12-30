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

import static edu.uga.miage.m1.polygons.gui.shapes.Group.TYPE_GROUP;
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
        SQUARE, TRIANGLE, CIRCLE, SARAKZIT
    }

    @Serial
    private static final long serialVersionUID = 1L;
    JCheckBox checkbox = new JCheckBox("Selection mode", false);
    private transient ShapeFactory shapeFactory = new ShapeFactory();

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
    public static final Logger logger = Logger.getLogger(JDrawingFrame.class.getName());
    private transient XMLVisitor xmlShape = new XMLVisitor();
    private transient JSonVisitor jsonShape = new JSonVisitor();
    private transient SimpleShape lastShapeSelected;
    private int xPressed;
    private int yPressed;
    private transient Rectangle2D selectionBox;

    private int cursorUndoRedoList = 0;
    

    /**
     * Default constructor that populates the main window.
     *
     * @param frameName
     */
    public JDrawingFrame(String frameName) {
        super(frameName);
        initateComponents();
        fillsPanel();
        // Add shapes in the menu
        toolbarButtonShapes(Shapes.SQUARE, "images/square.png");
        toolbarButtonShapes(Shapes.TRIANGLE, "images/triangle.png");
        toolbarButtonShapes(Shapes.CIRCLE, "images/circle.png");
        toolbarButtonShapes(Shapes.SARAKZIT, "images/sarakzit.png");

        setPreferredSize(new Dimension(800, 800));

        JButton buttonJSON = new JButton("Export to JSON");
        exportToJsonFile(buttonJSON);

        JButton buttonXML = new JButton("Export to XML");
        exportToXmlFile(frameName, buttonXML);

        JButton buttonReset = new JButton("Reset");
        resetMyBoard(buttonReset);

        JButton buttonImportXML = new JButton("Import XML");
        importFromXmlFile(buttonImportXML);

        JButton buttonUndo = new JButton("Undo <");
        undoShape(buttonUndo);

        JButton buttonRedo = new JButton("> Redo");
        redoShape(buttonRedo);

        mtoolbar.add(buttonUndo);
        mtoolbar.add(buttonRedo);

        mtoolbar.add(buttonReset);
        mtoolbar.add(buttonJSON);
        mtoolbar.add(buttonXML);
        mtoolbar.add(buttonImportXML);
        mtoolbar.add(checkbox);
    }

    // TODO UNDO = BACK = CTRL+Z
    private void undoShape(JButton buttonUndo) {
        buttonUndo.addActionListener(e -> {
            if(cursorUndoRedoList >0){
                cursorUndoRedoList--;
                logger.log(Level.INFO,"Je repaint dans le undo");
                repaintGraph();
            }
        });
    }

    // TODO EN AVANT = REDO = CTRL+Y
    private void redoShape(JButton buttonRedo) {
        buttonRedo.addActionListener(e -> {
            if(cursorUndoRedoList < shapes.size()){
                cursorUndoRedoList++;
                logger.log(Level.INFO,"Je repaint dans le redo");
                repaintGraph();
            }
        });
    }

    private void fillsPanel() {
        setLayout(new BorderLayout());
        add(mtoolbar, BorderLayout.NORTH);
        add(mpanel, BorderLayout.CENTER);
        add(mlabel, BorderLayout.SOUTH);
    }

    private void initateComponents() {
        mtoolbar = new JToolBar("Toolbar");
        mpanel = new JPanel();
        mpanel.setBackground(Color.WHITE);
        mpanel.setLayout(null);
        mpanel.setMinimumSize(new Dimension(400, 400));
        mpanel.addMouseListener(this);
        mpanel.addMouseMotionListener(this);
        mlabel = new JLabel(" ", SwingConstants.LEFT);
    }

    private void toolbarButtonShapes(Shapes triangle, String pathImage) {
        JButton button1 = new JButton(new ImageIcon(getClass().getResource(pathImage)));
        button1.setBorderPainted(false);
        mbuttons.put(triangle, button1);
        button1.setActionCommand(triangle.toString());
        button1.addActionListener(mreusableActionListener);
        if (mselected == null) {
            button1.doClick();
        }
        mtoolbar.add(button1);
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
        if ((!checkbox.isSelected()) && (mpanel.contains(evt.getX(), evt.getY()))) {
            Graphics2D g2 = getGraphics2D();
            switch (mselected) {
                case CIRCLE -> setNewShape(evt, g2, "CIRCLE");
                case TRIANGLE -> setNewShape(evt, g2, "TRIANGLE");
                case SQUARE -> setNewShape(evt, g2, "SQUARE");
                case SARAKZIT -> setNewShape(evt, g2, "SARAKZIT");
                default -> logger.log(Level.INFO, "No shape named {0} ", mselected);
            }
        }
    }

    private Graphics2D getGraphics2D() {
        return (Graphics2D) mpanel.getGraphics();
    }

    private void setNewShape(MouseEvent evt, Graphics2D g2, String typeShape) {
        SimpleShape s = shapeFactory.getShape(typeShape, evt.getX(), evt.getY());
        if((cursorUndoRedoList+1)<shapes.size()){
            String str ="{";
            for (SimpleShape shape : shapes) {
                str+=shape.getType()+",";
            }
            logger.info(str+"}");
            logger.info(""+shapes);
            int nbShapeToDelete = shapes.size();
            for (int i = cursorUndoRedoList+1; i < shapes.size(); i++) {
                shapes.remove(shapes.get(i));
            }
        }
        shapes.add(s);
        cursorUndoRedoList++;
        repaintGraph();
        s.draw(g2);
        s.accept(jsonShape);
        s.accept(xmlShape);
        shapeListJsonFormat.add(jsonShape.getRepresentation());
        shapeListXMLFormat.add(xmlShape.getRepresentation());
    }


    /**
     * Implements an empty method for the <tt>MouseListener</tt> interface.
     *
     * @param evt The associated mouse event.
     */
    public void mouseEntered(MouseEvent evt) {
        // Do nothing.
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
        int mouseClickedX = evt.getX();
        int mouseClickedY = evt.getY();
        for (SimpleShape shape : shapes) {
            boolean isClickedOnShape;
            if (shape.getType().equals(TYPE_GROUP)) {
                Group groupSelected = (Group) shape;
                isClickedOnShape = isClickedOnGroup(mouseClickedX, mouseClickedY, groupSelected);
            } else {
                isClickedOnShape = isClickedOnShapeExceptGroup(mouseClickedX, mouseClickedY, shape);
            }

            if (isClickedOnShape) {
                lastShapeSelected = shape;
                lastShapeSelected.setXY(mouseClickedX, mouseClickedY);
            }
        }

        if (lastShapeSelected == null) {
            xPressed = mouseClickedX;
            yPressed = mouseClickedY;
        }
    }

    private static boolean isClickedOnShapeExceptGroup(int mouseClickedX, int mouseClickedY, SimpleShape shape) {
        return (mouseClickedX <= shape.getX() + 25) && (mouseClickedX >= shape.getX() - 25) && (mouseClickedY <= shape.getY() + 25) && (mouseClickedY >= shape.getY() - 25);
    }

    private static boolean isClickedOnGroup(int mouseClickedX, int mouseClickedY, Group groupSelected) {
        return (mouseClickedX <= groupSelected.getX2()) && (mouseClickedX >= groupSelected.getX1()) && (mouseClickedY <= groupSelected.getY2()) && (mouseClickedY >= groupSelected.getY1());
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
        this.repaintGraph();

        if (lastShapeSelected != null) {
            lastShapeSelected.draw(getGraphics2D());
        }

        if (checkbox.isSelected()) {
            int minX = min(xPressed, xReleased);
            int minY = min(yPressed, yReleased);
            int maxX = max(xPressed, xReleased);
            int maxY = max(yPressed, yReleased);

            SimpleShape groupCreated = shapeFactory.getShape(TYPE_GROUP, 0, 0);
            Group groupSelected = ((Group) groupCreated);
            groupSelected.setCoordMinMax(minX, minY, maxX, maxY);

            addShapeToGroupIfShapeSelected(xReleased, yReleased, groupSelected);

            if (!groupSelected.getListGroup().isEmpty()) {
                drawGroup(groupSelected);
            } else {
                selectionBox = null;
            }
            this.repaintGraph();
        }
        lastShapeSelected = null;
        checkbox.setSelected(false);
    }

    private void drawGroup(Group groupSelected) {
        groupSelected.updateCoordMinMax();
        if (groupSelected.getListGroup().size() > 1) {
            for (SimpleShape simpleShape : groupSelected.getListGroup()) {
                shapes.remove(simpleShape);
            }
            shapes.add(groupSelected);
            groupSelected.accept(jsonShape);
            groupSelected.accept(xmlShape);
            shapeListJsonFormat.add(jsonShape.getRepresentation());
            shapeListXMLFormat.add(xmlShape.getRepresentation());
        }
        drawPerfectRect(groupSelected.getX1(), groupSelected.getY1(), groupSelected.getX2(), groupSelected.getY2());
    }

    private void addShapeToGroupIfShapeSelected(int xReleased, int yReleased, Group groupSelected) {
        for (SimpleShape shape : shapes) {
            boolean isClickedOnShape;

            if (shape.getType().equals(TYPE_GROUP)) {
                Group shapeSelected = ((Group) shape);
                isClickedOnShape = (shapeSelected.getX1() <= max(xPressed, xReleased) &&
                        shapeSelected.getX2() >= min(xPressed, xReleased) &&
                        shapeSelected.getY1() <= max(yPressed, yReleased) &&
                        shapeSelected.getY2() >= min(yPressed, yReleased));
                shape = shapeSelected;
            } else {
                isClickedOnShape = (shape.getX() <= max(xPressed, xReleased) &&
                        shape.getX() >= min(xPressed, xReleased) &&
                        shape.getY() <= max(yPressed, yReleased) &&
                        shape.getY() >= min(yPressed, yReleased));
            }

            if (isClickedOnShape) {
                groupSelected.addShape(shape);
            }
        }
    }

    /**
     * Implements method for the <tt>MouseMotionListener</tt> interface to
     * move a dragged shape.
     *
     * @param evt The associated mouse event.
     */

    public void mouseDragged(MouseEvent evt) {
        Graphics2D g2 = getGraphics2D();
        if (checkbox.isSelected()) {
            drawPerfectRect(evt.getX(), evt.getY(), xPressed, yPressed);
            g2.setColor(new Color(100, 100, 100, 100));
            g2.fill(selectionBox);
        } else {
            if (lastShapeSelected != null) {
                this.repaintGraph();
                lastShapeSelected.draw(getGraphics2D());
                if (lastShapeSelected.getType().equals(TYPE_GROUP)) {
                    int translationX = evt.getX() - lastShapeSelected.getX();
                    int translationY = evt.getY() - lastShapeSelected.getY();
                    ((Group) lastShapeSelected).moveGroup(translationX, translationY);
                    selectionBox = null;
                    repaintGraph();
                } else {
                    lastShapeSelected.setXY(evt.getX(), evt.getY());
                }
                lastShapeSelected.draw(g2);
            }
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
        Graphics2D g2 = getGraphics2D();
        paint(this.getGraphics());

        for (int i = 0; i < cursorUndoRedoList; i++) {
            shapes.get(i).draw(g2);
        }

        //shapes.forEach(shape -> shape.draw(g2));
        if ((checkbox.isSelected()) && (selectionBox != null)) {
            g2.setColor(new Color(100, 100, 100, 100));
            g2.fill(selectionBox);
        }
    }

    private void drawPerfectRect(int x, int y, int x2, int y2) {
        int px = min(x, x2);
        int py = min(y, y2);
        int pw = Math.abs(x - x2);
        int ph = Math.abs(y - y2);
        if (selectionBox != null)
            selectionBox.setRect(px, py, pw, ph);
        else {
            selectionBox = new Rectangle2D.Double(px, py, pw, ph);
        }
    }

    /**
     * Create a new file
     *
     * @param filename name of my new file.
     */
    private static void createFile(String filename) {
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
     * Write data in a file
     *
     * @param path Path of the file where we will write.
     * @param data Text who will be written in the file.
     */
    private static void writeInAFile(String path, String data) throws IOException {
        createFile(path);
        try (FileOutputStream out = new FileOutputStream(path)) {
            out.write(data.getBytes());
        } catch (Exception e) {
            logger.log(Level.INFO, e, () -> "Result " + path);
        }
    }

    private void importFromXmlFile(JButton buttonImportXML) {
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
                logger.log(Level.INFO, ex, () -> "An error occurred. " + ex);
            }
            xmldata.getCircleShape().stream().forEach(circle -> {
                Graphics2D g2 = getGraphics2D();
                circle.draw(g2);
                circle.accept(xmlShape);
                shapeListXMLFormat.add(xmlShape.getRepresentation());
                shapes.add(circle);
            });
            xmldata.getSquareShape().stream().forEach(square -> {
                Graphics2D g2 = getGraphics2D();
                square.draw(g2);
                square.accept(xmlShape);
                shapeListXMLFormat.add(xmlShape.getRepresentation());
                shapes.add(square);
            });
            xmldata.getTriangleShape().stream().forEach(triangle -> {
                Graphics2D g2 = getGraphics2D();
                triangle.draw(g2);
                triangle.accept(xmlShape);
                shapeListXMLFormat.add(xmlShape.getRepresentation());
                shapes.add(triangle);
            });

        });
    }

    private void exportToJsonFile(JButton buttonJSON) {
        buttonJSON.addActionListener(e -> {
            File pathForExport = getDirectoryPathForExport();
            String json = "{\"shapes\": [" + String.join(",", shapeListJsonFormat) + "]}";
            try {
                writeInAFile(pathForExport+"/shapes.json", json);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, ex, () -> "An error occurred when writeInAFile for Json. " + ex);
            }
        });
    }

    private void exportToXmlFile(String frameName, JButton buttonXML) {
        buttonXML.addActionListener(e -> {
            File pathForExport = getDirectoryPathForExport();
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root><shapes>" + String.join("", shapeListXMLFormat) + "</shapes></root>";
            try {
                writeInAFile(pathForExport+"shapes.xml", xml);
            } catch (IOException ex) {
                logger.log(Level.INFO, ex, () -> "Result " + frameName);
            }
        });
    }

    private static File getDirectoryPathForExport() {
        JFileChooser choose = new JFileChooser();
        choose.setDialogTitle("Choisissez un DOSSIER où Exporter: ");
        choose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int res = choose.showOpenDialog(null);
        if (res == JFileChooser.APPROVE_OPTION && choose.getSelectedFile().exists()) {
            logger.log(Level.INFO, "Vous avez selectionne le repertoire: {0}", choose.getSelectedFile());
        }
        return choose.getSelectedFile();
    }

    private void resetMyBoard(JButton buttonReset) {
        buttonReset.addActionListener(e -> {
            cursorUndoRedoList = 0;
            shapeListJsonFormat = new ArrayList<>();
            shapeListXMLFormat = new ArrayList<>();
            shapes = new ArrayList<>();
            selectionBox = null;
            repaintGraph();
        });
    }

}



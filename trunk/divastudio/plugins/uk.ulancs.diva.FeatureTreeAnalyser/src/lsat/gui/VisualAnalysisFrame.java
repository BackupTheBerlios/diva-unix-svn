/*
 * VisualAnalysisFrame.java
 *
 * Created on 16 May 2006, 13:05
 */

package lsat.gui;


import com.jgraph.algebra.cost.JGraphConstantCostFunction;
import com.jgraph.layout.JGraphFacade;
import com.jgraph.layout.JGraphLayout;
import com.jgraph.layout.graph.JGraphSimpleLayout;
import com.jgraph.layout.organic.JGraphFastOrganicLayout;
import com.jgraph.layout.organic.JGraphOrganicLayout;
import com.jgraph.layout.organic.JGraphSelfOrganizingOrganicLayout;
import com.jgraph.layout.tree.JGraphCompactTreeLayout;
import com.jgraph.layout.tree.JGraphRadialTreeLayout;
import com.jgraph.layout.tree.JGraphTreeLayout;
import lsat.lsa.*;
import lsat.structures.*;
import lsat.log.*;
import org.jgraph.JGraph;
import org.jgraph.graph.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.geom.*;
import java.awt.*;
import java.util.*;


/**
 *
 * @author  stone
 */
public class VisualAnalysisFrame extends javax.swing.JInternalFrame
{
    
    /** Creates new form VisualAnalysisFrame */
    public VisualAnalysisFrame(LSAResults theResults, Project theProject)
    {
        initComponents();
        
        this.theResults = theResults;
        this.theProject = theProject;
        
        SpinnerNumberModel numModel = new SpinnerNumberModel(0, -1, 1, 0.05);
        thresholdSpinner.setModel(numModel);
        
        // construct the graph pane
        createNewGraph();
        
        //populate the combo boxes
        DefaultComboBoxModel classComboBoxModelSource = new DefaultComboBoxModel();
        DefaultComboBoxModel classComboBoxModelEquivalence = new DefaultComboBoxModel();
        
        for(DocumentClass dc : theProject.getDocumentClassCollection())
        {
            classComboBoxModelSource.addElement(dc.getDescription());
            classComboBoxModelEquivalence.addElement(dc.getDescription());
        }
        
        firstSourceBox.setModel(classComboBoxModelSource);
        secondSourceBox.setModel(classComboBoxModelEquivalence);
        
        // set appropriate border titles
        String title = theProject.getDocumenClass(1).getDescription();
        firstChunkPanel.setBorder(BorderFactory.createTitledBorder(title));
        secondChunkPanel.setBorder(BorderFactory.createTitledBorder(title));
     
        highlightBorder = BorderFactory.createLineBorder(Color.darkGray, 2);
        
        log = LogFactory.getLog();
    }
    
    public void draw(boolean drawConnections)
    {
        createNewGraph();
        
        cells = new TreeMap <Integer, ChunkGraphCell> ();
        ports = new TreeMap <ChunkGraphCell, ComparablePort> ();
        edgeSet = new ConnectionSet();
        
        // get the selected indices of the combo boxes
        // possible fence-post bug
        int leftIndex = firstSourceBox.getSelectedIndex();
        int rightIndex = secondSourceBox.getSelectedIndex();
        
        // get the list of chunks that belong to the selected indices
        ArrayList<ChunkAnalysisStruct> leftList = theResults.getChunksBelongingToClass(leftIndex+1);
        ArrayList<ChunkAnalysisStruct> rightList = theResults.getChunksBelongingToClass(rightIndex+1);

        // set up horizontal and vertical gap
        final int VERTICALGAP = 50;
        final int HORIZONTALGAP = (int) (0.75 * graphScrollPane.getWidth());
        
        
        // set up some variables used later
        int xOffset = 20, yOffset = 20;
        float leftIncrement = VERTICALGAP, rightIncrement = VERTICALGAP;
        
        
        // code to determine the y increment that should be used
        // this helps to keep the lists roughly the same length
        // the shorter list is padded out with extra white space
        if(leftList.size() <= rightList.size())
        {
            leftIncrement = (50 * rightList.size()) / leftList.size();
            if(leftIncrement == 0)
                leftIncrement++;
        }
        else
        {
            rightIncrement = (50 * leftList.size()) / rightList.size();
            if(rightIncrement== 0)
                rightIncrement++;
        }
        
        // add a load of chunkGraphCells that will appear down the left hand side
        for(int i=0;i<leftList.size();i++)
        {
            ChunkGraphCell c = new ChunkGraphCell(leftList.get(i), true);
            GraphConstants.setBounds(c.getAttributes(), new Rectangle2D.Double(xOffset,yOffset,60,20));
            ComparablePort rhsPort = new ComparablePort();
            //GraphConstants.setOffset(rhsPort.getAttributes(), new Point(GraphConstants.PERMILLE, GraphConstants.PERMILLE/2));            
            c.add(rhsPort);
            
            cells.put(c.getChunkNo(), c);
            ports.put(c, rhsPort);
            
            yOffset+=leftIncrement;
        }
        
        xOffset += HORIZONTALGAP;
        yOffset = 20;
        
        for(int i=0;i<rightList.size();i++)
        {
            ChunkGraphCell c = new ChunkGraphCell(rightList.get(i), false);
            GraphConstants.setBounds(c.getAttributes(), new Rectangle2D.Double(xOffset,yOffset,60,20));
            ComparablePort lhsPort = new ComparablePort();
            //GraphConstants.setOffset(lhsPort.getAttributes(), new Point(0, GraphConstants.PERMILLE/2));
            c.add(lhsPort);
            
            cells.put(c.getChunkNo(), c);
            ports.put(c, lhsPort);
            
            yOffset+=rightIncrement;
        }
        
        if(drawConnections)
        {
            double threshold = 0;
            try
            {
                threshold = Double.valueOf(thresholdSpinner.getModel().getValue().toString());
            }
            catch(NumberFormatException e)
            {
                // TODO : Add a log line here
                thresholdSpinner.getModel().setValue(0);
            }

            // get the target document class
            int docClass = secondSourceBox.getSelectedIndex();
            if(docClass != -1)//edge.setSource()
                docClass++;

            for(ChunkAnalysisStruct n : leftList)
            {
                ArrayList<ChunkAnalysisStruct> related = theResults.getContributors(n.getChunkNo(), threshold, docClass, false);
                

                for(ChunkAnalysisStruct cas : related)
                {
                    ChunkGraphCell sourceCell = cells.get(n.getChunkNo());
                    ChunkGraphCell targetCell = cells.get(cas.getChunkNo());
                    
                    ComparablePort sourcePort = ports.get(sourceCell);
                    ComparablePort targetPort = ports.get(targetCell);
                    
                    DefaultEdge edge = new DefaultEdge();
                    edge.setSource(sourcePort);
                    edge.setTarget(targetPort);
                    
                    //GraphConstants.setLineStyle(edge.getAttributes(), GraphConstants.STYLE);
                    //GraphConstants.setRouting(edge.getAttributes(), GraphConstants.ROUTING_SIMPLE);

                    edgeSet.connect(edge, sourcePort, targetPort);
                    }
                }
            }

        // combine the cells and edges into a single array for faster insert()
        // performance
        Vector insertionVector = new Vector(cells.values());
        insertionVector.addAll(edgeSet.getEdges());
        
        graph.getGraphLayoutCache().insert(insertionVector.toArray());
    }

    private void highLightCells(ArrayList<ChunkAnalysisStruct> toHighlight)
    {
        Vector<ChunkGraphCell> cells = getAllChunkGraphCells();

        for(ChunkGraphCell c : cells)
        {
            for(ChunkAnalysisStruct struct : toHighlight)
            {
                if(c.hasChunkNo(struct.getChunkNo()))
                {
                    GraphConstants.setBorder(c.getAttributes(), highlightBorder);
                }
            }
        }
        
        graph.getGraphLayoutCache().edit(cells.toArray(), new AttributeMap());
    }
    
    private void unHighlightCells()
    {
        Vector<ChunkGraphCell> cells = getAllChunkGraphCells();
        
        AttributeMap removeBorderMap = new AttributeMap();
        GraphConstants.setRemoveAttributes(removeBorderMap, new Object[]{GraphConstants.BORDER});
        
        graph.getGraphLayoutCache().edit(cells.toArray(), removeBorderMap);
    }
    

    private void fitGraphToPage() 
    {
        Rectangle2D p = graph.getCellBounds(graph.getRoots());
        if (p != null) 
        {
            if (graph.getParent() instanceof JViewport) 
            {
                JViewport viewPort = (JViewport) graph.getParent();
                Dimension s = viewPort.getExtentSize();
                
                double vScale = (double) s.getWidth() / (p.getX() + p.getWidth());
                double hScale = (double) s.getHeight() / (p.getY() + p.getHeight());

                double scale = Math.min(vScale, hScale);
                graph.setScale(scale);
            }
        }     
    }
    
    private void createNewGraph()
    {
        // an empty model, view and graph
        // we only keep the reference to the graph object
        GraphModel model = new DefaultGraphModel();
        GraphLayoutCache view = new GraphLayoutCache(model, 
                                        new DefaultCellViewFactory());
        this.graph = new JGraph(model, view);        
        
        // use our custome cellUI renderer in order to get the callbacks working
        this.graph.setUI(new ChunkGraphUI(firstChunkTextArea, secondChunkTextArea));
        
        // set some other options
        graph.setDoubleBuffered(false); //makes scrolling faster
        graph.setAntiAliased(false);    // makes scrolling faster
        graph.setGridEnabled(true);     // easier to see
        graph.setGridMode(JGraph.DOT_GRID_MODE);
        graph.setGridVisible(true);     // grid for lining up
        graph.setGridSize(10);          // size of grid
        graph.setEditable(false);       // i don't want people editing my graph
        
        // attach graph to the graphScrollPane (which is of type JScrollPane)
        graphScrollPane.setViewportView(graph);
        
        PortView.allowPortMagic = true;
    }
    
    private void applyLayout(String layoutName)
    {
        JGraphFacade theFacade = new JGraphFacade(graph);
        theFacade.setDirected(false);
        theFacade.setIgnoresHiddenCells(true);
        theFacade.setIgnoresUnconnectedCells(true);
        
        JGraphLayout layout = null;

        if("organic".equals(layoutName))
        {
            layout = new JGraphOrganicLayout();
        }
        if("fastOrganic".equals(layoutName))
        {
            layout = new JGraphFastOrganicLayout();
        }
        else if("tree".equals(layoutName))
        {
            layout = new JGraphTreeLayout();
        }
        else if("compactTree".equals(layoutName))
        {
            layout = new JGraphCompactTreeLayout();
        }
        else if("radialTree".equals(layoutName))
        {
            layout = new JGraphRadialTreeLayout();
        }
        else if("self".equals(layoutName))
        {
            layout = new JGraphSelfOrganizingOrganicLayout();
        }
        else if("circle".equals(layoutName))
        {
            layout = new JGraphSimpleLayout(JGraphSimpleLayout.TYPE_CIRCLE);
        }
        else
        {
            // TODO : insert log line here
        }

        // TODO : make this run on a thread
        if(layout != null)
        {
            layout.run(theFacade);
            Map nested = theFacade.createNestedMap(true, true);
            graph.getGraphLayoutCache().edit(nested);
        }
    }
    
    private void randomMoveVertices()
    {
        Vector<ChunkGraphCell> cells = getAllChunkGraphCells();
                        
        Math.random();
        
        for(ChunkGraphCell c : cells)
        {
                int x = (int)(Math.random() * 2000);
                int y = (int)(Math.random() * 2000);
                GraphConstants.setBounds(c.getAttributes(), new Rectangle2D.Double(x,y,60,20));
        }
        
        graph.getGraphLayoutCache().editCell(cells.toArray(), new AttributeMap());        
    }
    
    
    // TODO : write a method to return all vertices in the graph, then update all the other 
    // functions to use this one, such as unhighlight, highlight etc...
    private Vector<ChunkGraphCell> getAllChunkGraphCells()
    {
        Vector<ChunkGraphCell> returnVector = new Vector<ChunkGraphCell>();
        Object[] elements = graph.getRoots();
        
        for(Object o : elements)
        {
            ChunkGraphCell cell = null;
            
            try
            {
                cell = (ChunkGraphCell)o;
            }
            catch(java.lang.ClassCastException e)
            {
                // TODO : do something here, log line might not be necessary
            }
            
            if(cell != null)
            {
                returnVector.add((ChunkGraphCell)o);
            }
            
        }
        
        return returnVector;
    }
            
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        topPanel = new javax.swing.JPanel();
        classesPanel = new javax.swing.JPanel();
        spinnerPanel = new javax.swing.JPanel();
        jLabelSpinner = new javax.swing.JLabel();
        thresholdSpinner = new javax.swing.JSpinner();
        classPanel = new javax.swing.JPanel();
        firstSourceLabel = new javax.swing.JLabel();
        firstSourceBox = new javax.swing.JComboBox();
        secondSourceLabel = new javax.swing.JLabel();
        secondSourceBox = new javax.swing.JComboBox();
        redrawButton = new javax.swing.JButton();
        showLinksbutton = new javax.swing.JButton();
        jSplitPane = new javax.swing.JSplitPane();
        graphPanel = new javax.swing.JPanel();
        graphScrollPane = new javax.swing.JScrollPane();
        southPanel = new javax.swing.JPanel();
        progressBar = new javax.swing.JProgressBar();
        jPanel1 = new javax.swing.JPanel();
        layoutPanel = new javax.swing.JPanel();
        organicLayoutsPanel = new javax.swing.JPanel();
        selfOrganizingLayoutButton = new javax.swing.JButton();
        organicLayoutButton = new javax.swing.JButton();
        fastOrganicButton = new javax.swing.JButton();
        utilityLayoutsPanel = new javax.swing.JPanel();
        randomButton = new javax.swing.JButton();
        circleLayoutButton = new javax.swing.JButton();
        treeLayoutsPanel = new javax.swing.JPanel();
        treeLayoutButton = new javax.swing.JButton();
        compactTreeButton = new javax.swing.JButton();
        radialTreeButton = new javax.swing.JButton();
        zoomPanel = new javax.swing.JPanel();
        zoomSlider = new javax.swing.JSlider();
        fitToPageButton = new javax.swing.JButton();
        fitSelectionButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        displayPanel = new javax.swing.JPanel();
        relatedPanel = new javax.swing.JPanel();
        hideUnconnectedButton = new javax.swing.JToggleButton();
        relatedChunksLabel = new javax.swing.JLabel();
        relatedChunksField = new javax.swing.JTextField();
        unsourcedToggleButton = new javax.swing.JToggleButton();
        textPanel = new javax.swing.JPanel();
        firstChunkPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        firstChunkTextArea = new javax.swing.JTextArea();
        secondChunkPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        secondChunkTextArea = new javax.swing.JTextArea();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Visual Analysis");
        setPreferredSize(new java.awt.Dimension(1024, 768));
        try
        {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e1)
        {
            e1.printStackTrace();
        }
        setVisible(true);
        topPanel.setLayout(new java.awt.BorderLayout());

        topPanel.add(classesPanel, java.awt.BorderLayout.WEST);

        jLabelSpinner.setText("Threshold");
        spinnerPanel.add(jLabelSpinner);

        thresholdSpinner.setMinimumSize(new java.awt.Dimension(32, 24));
        thresholdSpinner.setPreferredSize(new java.awt.Dimension(50, 24));
        spinnerPanel.add(thresholdSpinner);

        topPanel.add(spinnerPanel, java.awt.BorderLayout.EAST);

        classPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        firstSourceLabel.setText("First Class");
        classPanel.add(firstSourceLabel);

        firstSourceBox.setPreferredSize(new java.awt.Dimension(100, 24));
        firstSourceBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                firstSourceBoxActionPerformed(evt);
            }
        });

        classPanel.add(firstSourceBox);

        secondSourceLabel.setText("Second Class");
        classPanel.add(secondSourceLabel);

        secondSourceBox.setPreferredSize(new java.awt.Dimension(100, 24));
        secondSourceBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                secondSourceBoxActionPerformed(evt);
            }
        });

        classPanel.add(secondSourceBox);

        redrawButton.setText("Draw Graph");
        redrawButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                redrawButtonActionPerformed(evt);
            }
        });

        classPanel.add(redrawButton);

        showLinksbutton.setText("Show Links");
        showLinksbutton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                showLinksbuttonActionPerformed(evt);
            }
        });

        classPanel.add(showLinksbutton);

        topPanel.add(classPanel, java.awt.BorderLayout.WEST);

        getContentPane().add(topPanel, java.awt.BorderLayout.NORTH);

        jSplitPane.setDividerLocation(800);
        jSplitPane.setMinimumSize(new java.awt.Dimension(800, 64));
        graphPanel.setLayout(new java.awt.BorderLayout());

        graphPanel.setMinimumSize(new java.awt.Dimension(600, 57));
        graphScrollPane.setMinimumSize(new java.awt.Dimension(300, 22));
        graphScrollPane.setPreferredSize(new java.awt.Dimension(800, 3));
        graphPanel.add(graphScrollPane, java.awt.BorderLayout.CENTER);

        southPanel.setLayout(new java.awt.BorderLayout());

        southPanel.add(progressBar, java.awt.BorderLayout.CENTER);

        graphPanel.add(southPanel, java.awt.BorderLayout.SOUTH);

        jPanel1.setLayout(new java.awt.GridLayout(1, 2));

        layoutPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        layoutPanel.setPreferredSize(new java.awt.Dimension(150, 60));
        organicLayoutsPanel.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        organicLayoutsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Organic Layouts"));
        organicLayoutsPanel.setMinimumSize(new java.awt.Dimension(130, 20));
        organicLayoutsPanel.setPreferredSize(new java.awt.Dimension(140, 150));
        selfOrganizingLayoutButton.setText("Self Organizing");
        selfOrganizingLayoutButton.setMaximumSize(new java.awt.Dimension(250, 150));
        selfOrganizingLayoutButton.setMinimumSize(new java.awt.Dimension(65, 0));
        selfOrganizingLayoutButton.setPreferredSize(new java.awt.Dimension(65, 15));
        selfOrganizingLayoutButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                selfOrganizingLayoutButtonActionPerformed(evt);
            }
        });

        organicLayoutsPanel.add(selfOrganizingLayoutButton);

        organicLayoutButton.setText("Organic Layout");
        organicLayoutButton.setMaximumSize(new java.awt.Dimension(250, 150));
        organicLayoutButton.setMinimumSize(new java.awt.Dimension(65, 0));
        organicLayoutButton.setPreferredSize(new java.awt.Dimension(65, 15));
        organicLayoutButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                organicLayoutButtonActionPerformed(evt);
            }
        });

        organicLayoutsPanel.add(organicLayoutButton);

        fastOrganicButton.setText("Fast Organic");
        fastOrganicButton.setMaximumSize(new java.awt.Dimension(250, 150));
        fastOrganicButton.setMinimumSize(new java.awt.Dimension(65, 0));
        fastOrganicButton.setPreferredSize(new java.awt.Dimension(65, 15));
        fastOrganicButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                fastOrganicButtonActionPerformed(evt);
            }
        });

        organicLayoutsPanel.add(fastOrganicButton);

        layoutPanel.add(organicLayoutsPanel);

        utilityLayoutsPanel.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        utilityLayoutsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Utility Layouts"));
        utilityLayoutsPanel.setMinimumSize(new java.awt.Dimension(130, 20));
        utilityLayoutsPanel.setPreferredSize(new java.awt.Dimension(140, 100));
        randomButton.setText("Random");
        randomButton.setMaximumSize(new java.awt.Dimension(250, 150));
        randomButton.setMinimumSize(new java.awt.Dimension(65, 0));
        randomButton.setPreferredSize(new java.awt.Dimension(65, 15));
        randomButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                randomButtonActionPerformed(evt);
            }
        });

        utilityLayoutsPanel.add(randomButton);

        circleLayoutButton.setText("Circle");
        circleLayoutButton.setMinimumSize(new java.awt.Dimension(65, 0));
        circleLayoutButton.setPreferredSize(new java.awt.Dimension(65, 15));
        circleLayoutButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                circleLayoutButtonActionPerformed(evt);
            }
        });

        utilityLayoutsPanel.add(circleLayoutButton);

        layoutPanel.add(utilityLayoutsPanel);

        treeLayoutsPanel.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        treeLayoutsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Tree Layouts"));
        treeLayoutsPanel.setMinimumSize(new java.awt.Dimension(130, 20));
        treeLayoutsPanel.setPreferredSize(new java.awt.Dimension(140, 150));
        treeLayoutButton.setText("Tree");
        treeLayoutButton.setMaximumSize(new java.awt.Dimension(250, 150));
        treeLayoutButton.setMinimumSize(new java.awt.Dimension(65, 0));
        treeLayoutButton.setPreferredSize(new java.awt.Dimension(65, 15));
        treeLayoutButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                treeLayoutButtonActionPerformed(evt);
            }
        });

        treeLayoutsPanel.add(treeLayoutButton);

        compactTreeButton.setText("Compact Tree");
        compactTreeButton.setMaximumSize(new java.awt.Dimension(250, 150));
        compactTreeButton.setMinimumSize(new java.awt.Dimension(65, 0));
        compactTreeButton.setPreferredSize(new java.awt.Dimension(65, 15));
        compactTreeButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                compactTreeButtonActionPerformed(evt);
            }
        });

        treeLayoutsPanel.add(compactTreeButton);

        radialTreeButton.setText("Radial Tree");
        radialTreeButton.setMaximumSize(new java.awt.Dimension(250, 150));
        radialTreeButton.setMinimumSize(new java.awt.Dimension(65, 0));
        radialTreeButton.setPreferredSize(new java.awt.Dimension(65, 15));
        radialTreeButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                radialTreeButtonActionPerformed(evt);
            }
        });

        treeLayoutsPanel.add(radialTreeButton);

        layoutPanel.add(treeLayoutsPanel);

        zoomPanel.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        zoomPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Zoom"));
        zoomSlider.setMajorTickSpacing(5);
        zoomSlider.setMaximum(10);
        zoomSlider.setMinimum(-10);
        zoomSlider.setPaintLabels(true);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setValue(0);
        zoomSlider.setMaximumSize(new java.awt.Dimension(150, 27));
        zoomSlider.setPreferredSize(new java.awt.Dimension(130, 42));
        zoomSlider.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                zoomSliderStateChanged(evt);
            }
        });

        zoomPanel.add(zoomSlider);

        fitToPageButton.setText("Fit to Page");
        fitToPageButton.setMinimumSize(new java.awt.Dimension(65, 0));
        fitToPageButton.setPreferredSize(new java.awt.Dimension(65, 15));
        fitToPageButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                fitToPageButtonActionPerformed(evt);
            }
        });

        zoomPanel.add(fitToPageButton);

        fitSelectionButton.setText("Fit Selection");
        fitSelectionButton.setMinimumSize(new java.awt.Dimension(65, 0));
        fitSelectionButton.setPreferredSize(new java.awt.Dimension(65, 15));
        fitSelectionButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                fitSelectionButtonActionPerformed(evt);
            }
        });

        zoomPanel.add(fitSelectionButton);

        layoutPanel.add(zoomPanel);

        jButton1.setText("produce stats");
        jButton1.setMinimumSize(new java.awt.Dimension(65, 0));
        jButton1.setPreferredSize(new java.awt.Dimension(65, 15));
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });

        layoutPanel.add(jButton1);

        jPanel1.add(layoutPanel);

        displayPanel.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        displayPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Display"));
        displayPanel.setPreferredSize(new java.awt.Dimension(140, 180));
        relatedPanel.setPreferredSize(new java.awt.Dimension(130, 50));
        hideUnconnectedButton.setText("Unconnected");
        hideUnconnectedButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        hideUnconnectedButton.setMinimumSize(new java.awt.Dimension(65, 0));
        hideUnconnectedButton.setPreferredSize(new java.awt.Dimension(130, 25));
        hideUnconnectedButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                hideUnconnectedButtonActionPerformed(evt);
            }
        });

        relatedPanel.add(hideUnconnectedButton);

        relatedChunksLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        relatedChunksLabel.setText("# Related");
        relatedChunksLabel.setPreferredSize(new java.awt.Dimension(100, 35));
        relatedPanel.add(relatedChunksLabel);

        relatedChunksField.setText("0");
        relatedChunksField.setMinimumSize(new java.awt.Dimension(50, 19));
        relatedChunksField.setPreferredSize(new java.awt.Dimension(100, 35));
        relatedPanel.add(relatedChunksField);

        unsourcedToggleButton.setText("Unsourced");
        unsourcedToggleButton.setMinimumSize(new java.awt.Dimension(65, 0));
        unsourcedToggleButton.setPreferredSize(new java.awt.Dimension(130, 25));
        unsourcedToggleButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                unsourcedToggleButtonActionPerformed(evt);
            }
        });

        relatedPanel.add(unsourcedToggleButton);

        displayPanel.add(relatedPanel);

        jPanel1.add(displayPanel);

        graphPanel.add(jPanel1, java.awt.BorderLayout.WEST);

        jSplitPane.setLeftComponent(graphPanel);

        textPanel.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        textPanel.setPreferredSize(new java.awt.Dimension(200, 234));
        firstChunkPanel.setLayout(new java.awt.BorderLayout());

        firstChunkPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Tempoary Title"));
        firstChunkPanel.setPreferredSize(new java.awt.Dimension(200, 117));
        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane4.setPreferredSize(new java.awt.Dimension(400, 93));
        firstChunkTextArea.setColumns(20);
        firstChunkTextArea.setEditable(false);
        firstChunkTextArea.setLineWrap(true);
        firstChunkTextArea.setRows(5);
        firstChunkTextArea.setWrapStyleWord(true);
        firstChunkTextArea.setDoubleBuffered(true);
        firstChunkTextArea.setDragEnabled(true);
        firstChunkTextArea.setMinimumSize(new java.awt.Dimension(250, 15));
        firstChunkTextArea.setPreferredSize(new java.awt.Dimension(200, 0));
        jScrollPane4.setViewportView(firstChunkTextArea);

        firstChunkPanel.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        textPanel.add(firstChunkPanel);

        secondChunkPanel.setLayout(new java.awt.BorderLayout());

        secondChunkPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Tempoary Title"));
        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        secondChunkTextArea.setColumns(20);
        secondChunkTextArea.setEditable(false);
        secondChunkTextArea.setLineWrap(true);
        secondChunkTextArea.setRows(5);
        secondChunkTextArea.setWrapStyleWord(true);
        secondChunkTextArea.setDoubleBuffered(true);
        secondChunkTextArea.setDragEnabled(true);
        secondChunkTextArea.setPreferredSize(new java.awt.Dimension(200, 0));
        jScrollPane3.setViewportView(secondChunkTextArea);

        secondChunkPanel.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        textPanel.add(secondChunkPanel);

        jSplitPane.setRightComponent(textPanel);

        getContentPane().add(jSplitPane, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed
        produceStats();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void circleLayoutButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_circleLayoutButtonActionPerformed
    {//GEN-HEADEREND:event_circleLayoutButtonActionPerformed
        applyLayout("circle");
    }//GEN-LAST:event_circleLayoutButtonActionPerformed

    private void hideUnconnectedButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_hideUnconnectedButtonActionPerformed
    {//GEN-HEADEREND:event_hideUnconnectedButtonActionPerformed
        if (hideUnconnectedButton.isSelected())
        {
            removedUnconnectedCells = new Vector<Object>();            
            for(ChunkGraphCell cell : cells.values())
            {
                ComparablePort p = ports.get(cell);
                if(p.getEdges().size() < 1)
                {
                    removedUnconnectedCells.add(cell);
                }
            }
            
            graph.getGraphLayoutCache().remove(removedUnconnectedCells.toArray(), true, true);
        }
        else
        {
            if(removedUnconnectedCells != null && removedUnconnectedCells.size() > 0)
            {
                graph.getGraphLayoutCache().insert(removedUnconnectedCells.toArray());
            }
        }
    }//GEN-LAST:event_hideUnconnectedButtonActionPerformed

    private void fitSelectionButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_fitSelectionButtonActionPerformed
    {//GEN-HEADEREND:event_fitSelectionButtonActionPerformed
        Object[] selected = graph.getSelectionCells();
        if(selected.length < 1)
        {
            return;
        }

        Vector<Double> xValues = new Vector<Double>();
        Vector<Double> yValues = new Vector<Double>(); 
        
        for(Object o : selected)
        {
            DefaultGraphCell c = null;
            
            try
            {
                c = (DefaultGraphCell)o;
            }
            catch(Exception e)
            {
                // TODO : Do we need to log here?
            }
            
            if(c!=null)
            {
                Rectangle2D bounds = GraphConstants.getBounds(c.getAttributes());
                
                if(bounds != null)
                {
                    xValues.add(bounds.getMinX());
                    xValues.add(bounds.getMaxX());
                    yValues.add(bounds.getMinY());
                    yValues.add(bounds.getMaxY());
                }
            }
        }
        
        Collections.sort(xValues);
        Collections.sort(yValues);
        
        int x1 = xValues.firstElement().intValue();
        int y1 = yValues.firstElement().intValue();
        int x2 = xValues.lastElement().intValue();
        int y2 = yValues.lastElement().intValue();
        
        Rectangle enclosingRect = new Rectangle(x1,y1,x2-x1,y2-y1);

        Dimension panelBounds = graphScrollPane.getViewport().getExtentSize();
        if (panelBounds == null)
           return;
        double x_h = panelBounds.getHeight() / enclosingRect.getHeight();  
        double x_w = panelBounds.getWidth() / enclosingRect.getWidth();
        double scale = (x_w < x_h) ? x_w : x_h;
        scale*=0.75;
        graph.setScale(scale);
        System.out.println("scale = " + scale);
        
        graph.scrollRectToVisible(new Rectangle((int) (enclosingRect.getX() * scale),
                                    (int) (enclosingRect.getY() * scale),
                                    (int) (enclosingRect.getWidth() * scale),
                                    (int) (enclosingRect.getHeight() * scale)));        
    }//GEN-LAST:event_fitSelectionButtonActionPerformed

    private void fastOrganicButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_fastOrganicButtonActionPerformed
    {//GEN-HEADEREND:event_fastOrganicButtonActionPerformed
        applyLayout("fastOrganic");
    }//GEN-LAST:event_fastOrganicButtonActionPerformed

    private void randomButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_randomButtonActionPerformed
    {//GEN-HEADEREND:event_randomButtonActionPerformed
        randomMoveVertices();
    }//GEN-LAST:event_randomButtonActionPerformed

    private void selfOrganizingLayoutButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_selfOrganizingLayoutButtonActionPerformed
    {//GEN-HEADEREND:event_selfOrganizingLayoutButtonActionPerformed
        applyLayout("self");
    }//GEN-LAST:event_selfOrganizingLayoutButtonActionPerformed

    private void fitToPageButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_fitToPageButtonActionPerformed
    {//GEN-HEADEREND:event_fitToPageButtonActionPerformed
        fitGraphToPage();
    }//GEN-LAST:event_fitToPageButtonActionPerformed

    private void radialTreeButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_radialTreeButtonActionPerformed
    {//GEN-HEADEREND:event_radialTreeButtonActionPerformed
        applyLayout("radialTree");
    }//GEN-LAST:event_radialTreeButtonActionPerformed

    private void compactTreeButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_compactTreeButtonActionPerformed
    {//GEN-HEADEREND:event_compactTreeButtonActionPerformed
        applyLayout("compactTree");
    }//GEN-LAST:event_compactTreeButtonActionPerformed

    private void treeLayoutButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_treeLayoutButtonActionPerformed
    {//GEN-HEADEREND:event_treeLayoutButtonActionPerformed
        applyLayout("tree");
    }//GEN-LAST:event_treeLayoutButtonActionPerformed

    private void unsourcedToggleButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_unsourcedToggleButtonActionPerformed
    {//GEN-HEADEREND:event_unsourcedToggleButtonActionPerformed
        if(unsourcedToggleButton.isSelected())
        {
            // action performed here for finding the badly sourced material
            double threshold = 0;
            int noRelatedChunks = 0;

            try
            {
                threshold = Double.valueOf(thresholdSpinner.getModel().getValue().toString());
                noRelatedChunks = Integer.valueOf(relatedChunksField.getText());
            }
            catch(NumberFormatException e)
            {
                // TODO : Add a log line here
                thresholdSpinner.getModel().setValue(0);
            }

            int selectedIndex = secondSourceBox.getSelectedIndex();

            // ok, so now get the chunks that are related
            ArrayList<ChunkAnalysisStruct> results = theResults.getPoorlySourced(selectedIndex + 1, theResults.WRTALLCHUNKS, threshold, noRelatedChunks);
            
            log.log(Log.INFO, results.size() + " number of chunks identified as badly sourced");
            Vector<Integer> chunkNumbers = new Vector<Integer>();
            for(ChunkAnalysisStruct cas : results)
            {
                chunkNumbers.add(cas.getChunkNo());
            }
            log.log(Log.INFO, "chunks are : " + chunkNumbers.toString());

            //time to highlight the badly sourced ones
            highLightCells(results);
        }
        else
        {
            unHighlightCells();
        }
    }//GEN-LAST:event_unsourcedToggleButtonActionPerformed

    private void zoomSliderStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_zoomSliderStateChanged
    {//GEN-HEADEREND:event_zoomSliderStateChanged
        //graph.setScale(zoomSlider.getValue());
        double requestedScale = zoomSlider.getValue();
        if(requestedScale < 0)
        {
            requestedScale = Math.abs(1 / requestedScale);
        }
        
        Rectangle2D bounds = graph.getBounds();
        Point centrePoint = new Point((int)(bounds.getWidth()/2), (int)(bounds.getHeight()/2));
        
        graph.setScale(requestedScale, centrePoint);
    }//GEN-LAST:event_zoomSliderStateChanged

    private void organicLayoutButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_organicLayoutButtonActionPerformed
    {//GEN-HEADEREND:event_organicLayoutButtonActionPerformed
        this.applyLayout("organic");
    }//GEN-LAST:event_organicLayoutButtonActionPerformed

    private void showLinksbuttonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_showLinksbuttonActionPerformed
    {//GEN-HEADEREND:event_showLinksbuttonActionPerformed
        unsourcedToggleButton.setSelected(false);
        hideUnconnectedButton.setSelected(false);

        draw(true);
    }//GEN-LAST:event_showLinksbuttonActionPerformed

    private void redrawButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_redrawButtonActionPerformed
    {//GEN-HEADEREND:event_redrawButtonActionPerformed
        unsourcedToggleButton.setSelected(false);
        hideUnconnectedButton.setSelected(false);

        draw(false);
    }//GEN-LAST:event_redrawButtonActionPerformed

    private void secondSourceBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_secondSourceBoxActionPerformed
    {//GEN-HEADEREND:event_secondSourceBoxActionPerformed
        // set title for border
        int index = secondSourceBox.getSelectedIndex();
        if(index != -1)
            index++;
        String selectedContent = null;
        if(index >= 1)
        {
            selectedContent = theProject.getDocumenClass(index).getDescription();            
        }

        
        secondChunkPanel.setBorder(BorderFactory.createTitledBorder(selectedContent));
    }//GEN-LAST:event_secondSourceBoxActionPerformed

    private void firstSourceBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_firstSourceBoxActionPerformed
    {//GEN-HEADEREND:event_firstSourceBoxActionPerformed
        // set title for border
        int index = firstSourceBox.getSelectedIndex();
        if(index != -1)
            index++;
        String selectedContent = null;
        if(index >= 1)
        {
            selectedContent = theProject.getDocumenClass(index).getDescription();            
        }

        
        firstChunkPanel.setBorder(BorderFactory.createTitledBorder(selectedContent));        
    }//GEN-LAST:event_firstSourceBoxActionPerformed

    // method to produce stats for constructing a number of chunks defined as unrelated vs threshold and unrelated chunk limit graph
    private void produceStats()
    {
        int selectedIndex = secondSourceBox.getSelectedIndex();
        
        // t is threshold
        for(double t=-1;t<=1.1;t+=0.05)
        {
            //max represents the number of related chunks
            for(int max = 0;max < 10; max++)
            {
                ArrayList<ChunkAnalysisStruct> results = theResults.getPoorlySourced(selectedIndex + 1, theResults.WRTALLCHUNKS, t, max);
                System.out.println(t + "\t" + max + "\t" + results.size());
            }
            System.out.println("");
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton circleLayoutButton;
    private javax.swing.JPanel classPanel;
    private javax.swing.JPanel classesPanel;
    private javax.swing.JButton compactTreeButton;
    private javax.swing.JPanel displayPanel;
    private javax.swing.JButton fastOrganicButton;
    private javax.swing.JPanel firstChunkPanel;
    private javax.swing.JTextArea firstChunkTextArea;
    private javax.swing.JComboBox firstSourceBox;
    private javax.swing.JLabel firstSourceLabel;
    private javax.swing.JButton fitSelectionButton;
    private javax.swing.JButton fitToPageButton;
    private javax.swing.JPanel graphPanel;
    private javax.swing.JScrollPane graphScrollPane;
    private javax.swing.JToggleButton hideUnconnectedButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabelSpinner;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSplitPane jSplitPane;
    private javax.swing.JPanel layoutPanel;
    private javax.swing.JButton organicLayoutButton;
    private javax.swing.JPanel organicLayoutsPanel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton radialTreeButton;
    private javax.swing.JButton randomButton;
    private javax.swing.JButton redrawButton;
    private javax.swing.JTextField relatedChunksField;
    private javax.swing.JLabel relatedChunksLabel;
    private javax.swing.JPanel relatedPanel;
    private javax.swing.JPanel secondChunkPanel;
    private javax.swing.JTextArea secondChunkTextArea;
    private javax.swing.JComboBox secondSourceBox;
    private javax.swing.JLabel secondSourceLabel;
    private javax.swing.JButton selfOrganizingLayoutButton;
    private javax.swing.JButton showLinksbutton;
    private javax.swing.JPanel southPanel;
    private javax.swing.JPanel spinnerPanel;
    private javax.swing.JPanel textPanel;
    private javax.swing.JSpinner thresholdSpinner;
    private javax.swing.JPanel topPanel;
    private javax.swing.JButton treeLayoutButton;
    private javax.swing.JPanel treeLayoutsPanel;
    private javax.swing.JToggleButton unsourcedToggleButton;
    private javax.swing.JPanel utilityLayoutsPanel;
    private javax.swing.JPanel zoomPanel;
    private javax.swing.JSlider zoomSlider;
    // End of variables declaration//GEN-END:variables
    
    // User variables declaration
    private LSAResults theResults;
    private Project theProject;
    private JGraph graph;
    private Border highlightBorder;
    private TreeMap <Integer, ChunkGraphCell> cells;
    private TreeMap <ChunkGraphCell, ComparablePort> ports;
    private ConnectionSet edgeSet;
    private Vector<Object> removedUnconnectedCells;
    private Vector<Object> removedConnectedCells;
    private Log log;
    // End of user variables declaration
}

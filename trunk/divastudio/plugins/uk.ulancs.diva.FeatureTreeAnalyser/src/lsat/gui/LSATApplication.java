/*
 * LSATApplication.java
 *
 * Created on 16 December 2005, 16:02
 */

package lsat.gui;

import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import lsat.log.*;
import lsat.lsa.*;
import lsat.structures.*;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author  stone
 */
public class LSATApplication extends javax.swing.JFrame 
{
    //private user variables declaration
    private Project theProject;
    private JDesktopPane theDesktop;
    private DocumentViewerFrame documentViewerTable;
    private LSAResults currentResults;
    private DebugFrame debugFrame;
    private Log log = LogFactory.getLog();
    private LSATPreferences thePrefs = LSATPreferencesFactory.getPrefs(false);
    private DocumentFrameTableModel docFrameTableModel;
    private File lastPath;
    //end private user variables declaration
    
    /**
     * Creates new form LSATApplication 
     */
    public LSATApplication() 
    {
        initComponents();
        initUserComponents();
        setWindowSizeAndTitle();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemNewProject = new javax.swing.JMenuItem();
        jMenuItemLoadProject = new javax.swing.JMenuItem();
        jMenuItemSaveProject = new javax.swing.JMenuItem();
        jMenuItemCloseProject = new javax.swing.JMenuItem();
        jSeparator = new javax.swing.JSeparator();
        jMenuItemImport = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItemQuit = new javax.swing.JMenuItem();
        jMenuEdit = new javax.swing.JMenu();
        jMenuItemCut = new javax.swing.JMenuItem();
        jMenuItemCopy = new javax.swing.JMenuItem();
        jMenuItemPaste = new javax.swing.JMenuItem();
        jMenuView = new javax.swing.JMenu();
        jMenuItemViewDocuments = new javax.swing.JMenuItem();
        jMenuItemViewDebug = new javax.swing.JMenuItem();
        jMenuLSA = new javax.swing.JMenu();
        jMenuItemPerformLSA = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jMenuItemViewLSAResults = new javax.swing.JMenuItem();
        jMenuItemViewVisualResults = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        jMenuItemLoadLSAResults = new javax.swing.JMenuItem();
        jMenuItemSaveLSAResults = new javax.swing.JMenuItem();
        jMenuItemDumpChunks = new javax.swing.JMenuItem();
        jMenuItemLogGraphingStats = new javax.swing.JMenuItem();
        jMenuItemLogRSGraphingStats = new javax.swing.JMenuItem();
        jMenuPreferences = new javax.swing.JMenu();
        jMenuItemShowPreferences = new javax.swing.JMenuItem();

        getContentPane().setLayout(null);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jMenuFile.setText("File");
        jMenuItemNewProject.setText("New Project");
        jMenuItemNewProject.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemNewProjectActionPerformed(evt);
            }
        });

        jMenuFile.add(jMenuItemNewProject);

        jMenuItemLoadProject.setText("Load Project");
        jMenuItemLoadProject.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemLoadProjectActionPerformed(evt);
            }
        });

        jMenuFile.add(jMenuItemLoadProject);

        jMenuItemSaveProject.setText("Save Project");
        jMenuItemSaveProject.setEnabled(false);
        jMenuItemSaveProject.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemSaveProjectActionPerformed(evt);
            }
        });

        jMenuFile.add(jMenuItemSaveProject);

        jMenuItemCloseProject.setText("Close Project");
        jMenuItemCloseProject.setEnabled(false);
        jMenuFile.add(jMenuItemCloseProject);

        jMenuFile.add(jSeparator);

        jMenuItemImport.setText("Import");
        jMenuItemImport.setEnabled(false);
        jMenuItemImport.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemImportActionPerformed(evt);
            }
        });

        jMenuFile.add(jMenuItemImport);

        jMenuFile.add(jSeparator1);

        jMenuItemQuit.setText("Quit");
        jMenuItemQuit.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemQuitActionPerformed(evt);
            }
        });

        jMenuFile.add(jMenuItemQuit);

        jMenuBar.add(jMenuFile);

        jMenuEdit.setText("Edit");
        jMenuItemCut.setText("Cut");
        jMenuItemCut.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemCutActionPerformed(evt);
            }
        });

        jMenuEdit.add(jMenuItemCut);

        jMenuItemCopy.setText("Copy");
        jMenuEdit.add(jMenuItemCopy);

        jMenuItemPaste.setText("Paste");
        jMenuEdit.add(jMenuItemPaste);

        jMenuBar.add(jMenuEdit);

        jMenuView.setText("View");
        jMenuView.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuViewActionPerformed(evt);
            }
        });

        jMenuItemViewDocuments.setText("Documents");
        jMenuItemViewDocuments.setEnabled(false);
        jMenuItemViewDocuments.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemViewDocumentsActionPerformed(evt);
            }
        });

        jMenuView.add(jMenuItemViewDocuments);

        jMenuItemViewDebug.setText("View Debug");
        jMenuItemViewDebug.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemViewDebugActionPerformed(evt);
            }
        });

        jMenuView.add(jMenuItemViewDebug);

        jMenuBar.add(jMenuView);

        jMenuLSA.setText("LSA");
        jMenuItemPerformLSA.setText("Perform LSA");
        jMenuItemPerformLSA.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemPerformLSAActionPerformed(evt);
            }
        });

        jMenuLSA.add(jMenuItemPerformLSA);

        jMenuLSA.add(jSeparator2);

        jMenuItemViewLSAResults.setText("View LSA Results");
        jMenuItemViewLSAResults.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemViewLSAResultsActionPerformed(evt);
            }
        });

        jMenuLSA.add(jMenuItemViewLSAResults);

        jMenuItemViewVisualResults.setText("View results graph");
        jMenuItemViewVisualResults.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemViewVisualResultsActionPerformed(evt);
            }
        });

        jMenuLSA.add(jMenuItemViewVisualResults);

        jMenuLSA.add(jSeparator3);

        jMenuItemLoadLSAResults.setText("Load LSA Results");
        jMenuItemLoadLSAResults.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemLoadLSAResultsActionPerformed(evt);
            }
        });

        jMenuLSA.add(jMenuItemLoadLSAResults);

        jMenuItemSaveLSAResults.setText("Save LSA Results");
        jMenuItemSaveLSAResults.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemSaveLSAResultsActionPerformed(evt);
            }
        });

        jMenuLSA.add(jMenuItemSaveLSAResults);

        jMenuItemDumpChunks.setText("Dump Chunks Belonging to Class");
        jMenuItemDumpChunks.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemDumpChunksActionPerformed(evt);
            }
        });

        jMenuLSA.add(jMenuItemDumpChunks);

        jMenuItemLogGraphingStats.setText("Log Graphing Stats");
        jMenuItemLogGraphingStats.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemLogGraphingStatsActionPerformed(evt);
            }
        });
        jMenuItemLogGraphingStats.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                jMenuItemLogGraphingStatsMouseClicked(evt);
            }
        });

        jMenuLSA.add(jMenuItemLogGraphingStats);

        jMenuItemLogRSGraphingStats.setText("Log RS Graphing Stats");
        jMenuItemLogRSGraphingStats.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemLogRSGraphingStatsActionPerformed(evt);
            }
        });
        jMenuItemLogRSGraphingStats.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                jMenuItemLogRSGraphingStatsMouseClicked(evt);
            }
        });

        jMenuLSA.add(jMenuItemLogRSGraphingStats);

        jMenuBar.add(jMenuLSA);

        jMenuPreferences.setText("Preferences");
        jMenuItemShowPreferences.setText("Show preferences dialog");
        jMenuItemShowPreferences.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItemShowPreferencesActionPerformed(evt);
            }
        });

        jMenuPreferences.add(jMenuItemShowPreferences);

        jMenuBar.add(jMenuPreferences);

        setJMenuBar(jMenuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemLogRSGraphingStatsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jMenuItemLogRSGraphingStatsMouseClicked
    {//GEN-HEADEREND:event_jMenuItemLogRSGraphingStatsMouseClicked
// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemLogRSGraphingStatsMouseClicked

    private void jMenuItemLogRSGraphingStatsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemLogRSGraphingStatsActionPerformed
    {//GEN-HEADEREND:event_jMenuItemLogRSGraphingStatsActionPerformed
    if(currentResults != null)
    {
        this.currentResults.printGraphingStatsFromReqSimilieData();
    }        
    }//GEN-LAST:event_jMenuItemLogRSGraphingStatsActionPerformed

    private void jMenuItemViewVisualResultsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemViewVisualResultsActionPerformed
    {//GEN-HEADEREND:event_jMenuItemViewVisualResultsActionPerformed
        if(this.currentResults != null)
        {
            VisualAnalysisFrame f = new VisualAnalysisFrame(this.currentResults,
                                                            this.theProject);
            theDesktop.add(f);
        }
    }//GEN-LAST:event_jMenuItemViewVisualResultsActionPerformed

    private void jMenuItemLoadProjectActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemLoadProjectActionPerformed
    {//GEN-HEADEREND:event_jMenuItemLoadProjectActionPerformed
            JFileChooser jfc = new JFileChooser();
            if(lastPath != null)
            {
                jfc.setCurrentDirectory(lastPath);
            }
            int fileDialogReturnVal = jfc.showOpenDialog(this);
            
            if(fileDialogReturnVal == JFileChooser.APPROVE_OPTION)
            {
                try
                {
                    File inputFile = jfc.getSelectedFile();
                    FileInputStream fis = new FileInputStream(inputFile);
                    ObjectInputStream ois = new ObjectInputStream(fis);

                    this.theProject = (Project)ois.readObject();
                    this.currentResults = (LSAResults)ois.readObject();
                    
                    lastPath = new File(jfc.getSelectedFile().getPath());
                }
                catch(IOException e)
                {
                    if(this.theProject == null)
                    {
                        log.log(Log.ERROR, "Failed to load project");
                    }
                    if(this.currentResults == null)
                    {
                        log.log(Log.WARNING, "Failed to load results");
                    }
                    
                    log.log(Log.WARNING, e.getMessage());
                }
                catch(ClassNotFoundException e)
                {
                    log.log(Log.ERROR, "Class not found error, version mismatch");
                }
            }
            if(this.theProject != null)
            {
                jMenuItemViewDocuments.setEnabled(true);
                jMenuItemSaveProject.setEnabled(true);
                this.setTitle(theProject.getProjectName());
                log.log(Log.INFO, "Project Loaded");
            }
            if(this.currentResults != null)
            {
                log.log(Log.INFO, "Results loaded");
            }
            
    }//GEN-LAST:event_jMenuItemLoadProjectActionPerformed

    private void jMenuItemSaveProjectActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemSaveProjectActionPerformed
    {//GEN-HEADEREND:event_jMenuItemSaveProjectActionPerformed
        if(this.theProject != null)
        {
            JFileChooser jfc = new JFileChooser();
            int fileDialogReturnVal = jfc.showSaveDialog(this);
            
            if(fileDialogReturnVal == JFileChooser.APPROVE_OPTION)
            {
                try
                {
                    File outputFile = jfc.getSelectedFile();
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);

                    oos.writeObject(this.theProject);
                    if(this.currentResults != null)
                    {
                        oos.writeObject(this.currentResults);
                    }
                }
                catch(IOException e)
                {
                    log.log(Log.ERROR, "Failed to save file\n" + e.getMessage());
                }
            }
                
        }        
    }//GEN-LAST:event_jMenuItemSaveProjectActionPerformed

    private void jMenuItemCutActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemCutActionPerformed
    {//GEN-HEADEREND:event_jMenuItemCutActionPerformed
        Toolkit t = java.awt.Toolkit.getDefaultToolkit();
        Clipboard c = t.getSystemClipboard();
        
        JInternalFrame currentFrame = theDesktop.getSelectedFrame();
        /*StringSelection contents = new StringSelection(srcData);
        clipboard.setContents(contents, this);*/
        
    }//GEN-LAST:event_jMenuItemCutActionPerformed

    private void jMenuItemShowPreferencesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItemShowPreferencesActionPerformed
    {//GEN-HEADEREND:event_jMenuItemShowPreferencesActionPerformed
        PreferencesDialog.showPreferencesDialog(this, this.thePrefs);
        
    }//GEN-LAST:event_jMenuItemShowPreferencesActionPerformed

    private void jMenuItemLogGraphingStatsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLogGraphingStatsActionPerformed
    if(currentResults != null)
    {
        //this.currentResults.printGraphingStats();
    }
    }//GEN-LAST:event_jMenuItemLogGraphingStatsActionPerformed

    private void jMenuItemLogGraphingStatsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItemLogGraphingStatsMouseClicked

            
    }//GEN-LAST:event_jMenuItemLogGraphingStatsMouseClicked

    private void jMenuItemDumpChunksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDumpChunksActionPerformed
        if(currentResults != null)
        {
            DocumentClassDialog d = DocumentClassDialog.showClassDialog(this, this.theProject);
            DocumentClass selectedClass = d.getSelectedDocumentClass();
            
            currentResults.dumpChunksBelongingToDocumentsWithClass(selectedClass.getId());
        }
        
    }//GEN-LAST:event_jMenuItemDumpChunksActionPerformed

    private void jMenuItemViewDebugActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemViewDebugActionPerformed
        this.debugFrame.setVisible(true);
    }//GEN-LAST:event_jMenuItemViewDebugActionPerformed

    private void jMenuItemViewLSAResultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemViewLSAResultsActionPerformed
        if(this.currentResults != null)
        {
            LSAResultsFrame f = new LSAResultsFrame(this.currentResults, 
                                                        this.theProject);
            theDesktop.add(f);
        }
    }//GEN-LAST:event_jMenuItemViewLSAResultsActionPerformed

    private void jMenuItemLoadLSAResultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLoadLSAResultsActionPerformed
            JFileChooser jfc = new JFileChooser();
            int fileDialogReturnVal = jfc.showOpenDialog(this);
            
            if(fileDialogReturnVal == JFileChooser.APPROVE_OPTION)
            {
                try
                {
                    File inputFile = jfc.getSelectedFile();
                    FileInputStream fis = new FileInputStream(inputFile);
                    ObjectInputStream ois = new ObjectInputStream(fis);

                    this.currentResults = (LSAResults)ois.readObject();
                }
                catch(IOException e)
                {
                    log.log(Log.ERROR, "Failed to load LSA results\n" + e.getMessage());
                }
                catch(ClassNotFoundException e)
                {
                    log.log(Log.ERROR, "Class not found : Error loading LSA results due to version mismatch");
                }
                
                System.out.println(currentResults == null);
            }
                

    }//GEN-LAST:event_jMenuItemLoadLSAResultsActionPerformed

    private void jMenuItemSaveLSAResultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveLSAResultsActionPerformed
        if(this.currentResults != null)
        {
            JFileChooser jfc = new JFileChooser();
            int fileDialogReturnVal = jfc.showSaveDialog(this);
            
            if(fileDialogReturnVal == JFileChooser.APPROVE_OPTION)
            {
                try
                {
                    File outputFile = jfc.getSelectedFile();
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);

                    oos.writeObject(this.currentResults);
                }
                catch(IOException e)
                {
                    System.out.println("IOexception");
                    System.out.println(e.getMessage());
                }
            }
                
        }
    }//GEN-LAST:event_jMenuItemSaveLSAResultsActionPerformed

    private void jMenuItemPerformLSAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPerformLSAActionPerformed
        Thread t = new LSAThread(theProject.getDocumentCollection(), 
                                theProject.getDocumentClassCollection(), 
                                thePrefs.get("lsa-regex"), this);
        t.start();
        
    }//GEN-LAST:event_jMenuItemPerformLSAActionPerformed

    private void jMenuItemImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemImportActionPerformed
        JFileChooser jfc = new JFileChooser();
        jfc.setMultiSelectionEnabled(true);
        if(lastPath != null)
        {
            jfc.setCurrentDirectory(lastPath);
        }
        int fileDialogReturnVal = jfc.showOpenDialog(this);

        // now select the file
        if(fileDialogReturnVal == JFileChooser.APPROVE_OPTION)
        {
            // add code here to allow selection of a document class
            
            DocumentClassDialog d = DocumentClassDialog.showClassDialog(this, this.theProject);
            
            DocumentClass selectedClass = d.getSelectedDocumentClass();

            File[] selected = jfc.getSelectedFiles();
            for(int i=0;i<selected.length;i++)
            {
                theProject.addNewDocument(selected[i], 1.0f, 
                                          selected[i].toString(), selectedClass);
            }
            
            docFrameTableModel.fireTableDataChanged();
            
            lastPath = new File(jfc.getSelectedFile().getPath());
        }
    }//GEN-LAST:event_jMenuItemImportActionPerformed

    private void jMenuItemViewDocumentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemViewDocumentsActionPerformed
        if(this.docFrameTableModel == null)
        {
            this.docFrameTableModel = new DocumentFrameTableModel(theProject.getDocumentCollection());
        }
        DocumentFrame d = new DocumentFrame(this.docFrameTableModel, theDesktop, theProject.getDocumentCollection());
        d.setVisible(true);
        theDesktop.add(d);
    }//GEN-LAST:event_jMenuItemViewDocumentsActionPerformed

    private void jMenuViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuViewActionPerformed

    }//GEN-LAST:event_jMenuViewActionPerformed

    private void jMenuItemQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemQuitActionPerformed
        this.shutDown();
        System.exit(0);
    }//GEN-LAST:event_jMenuItemQuitActionPerformed

    private void jMenuItemNewProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNewProjectActionPerformed
        String projectName= JOptionPane.showInputDialog(null, "Please enter a project name");
        
        if(projectName != null)
        {
            theProject = new Project(projectName);

            // now we've got a project we need to enalbe the save and close buttons
            jMenuItemSaveProject.setEnabled(true);
            jMenuItemCloseProject.setEnabled(true);
            jMenuItemViewDocuments.setEnabled(true);
            jMenuItemImport.setEnabled(true);

            // set title
            setTitle("LSAT - " + projectName);

            jMenuItemViewDocumentsActionPerformed(null);
        }
    }//GEN-LAST:event_jMenuItemNewProjectActionPerformed
    
    private void setWindowSizeAndTitle()
    {
        Dimension screenSize = this.getToolkit().getScreenSize();
        int newHeight =(int) ((float)screenSize.height * 0.7);
        int newWidth = (int) ((float)screenSize.width * 0.7);
        this.setSize(newWidth, newHeight);
        newHeight=(int)((0.3*(float)screenSize.height)/2);
        newWidth=(int)((0.3*(float)screenSize.width)/2);
        this.setLocation(newWidth, newHeight);
        this.setTitle("LSAT");        
    }
    
    private void initUserComponents()
    {
        // required to attach windows to in the MDI world
        theDesktop = new JDesktopPane();
        setContentPane(theDesktop);

        // create and add the debug frame
        debugFrame = new DebugFrame();
        debugFrame.setVisible(false);
        theDesktop.add(debugFrame);
    }
    
    private void shutDown()
    {
        //nothing in here yet
    }
    
    public void showLSAResults(LSAResults results)
    {
        this.currentResults = results;
        jMenuItemViewLSAResultsActionPerformed(null);
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu jMenuEdit;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenuItem jMenuItemCloseProject;
    private javax.swing.JMenuItem jMenuItemCopy;
    private javax.swing.JMenuItem jMenuItemCut;
    private javax.swing.JMenuItem jMenuItemDumpChunks;
    private javax.swing.JMenuItem jMenuItemImport;
    private javax.swing.JMenuItem jMenuItemLoadLSAResults;
    private javax.swing.JMenuItem jMenuItemLoadProject;
    private javax.swing.JMenuItem jMenuItemLogGraphingStats;
    private javax.swing.JMenuItem jMenuItemLogRSGraphingStats;
    private javax.swing.JMenuItem jMenuItemNewProject;
    private javax.swing.JMenuItem jMenuItemPaste;
    private javax.swing.JMenuItem jMenuItemPerformLSA;
    private javax.swing.JMenuItem jMenuItemQuit;
    private javax.swing.JMenuItem jMenuItemSaveLSAResults;
    private javax.swing.JMenuItem jMenuItemSaveProject;
    private javax.swing.JMenuItem jMenuItemShowPreferences;
    private javax.swing.JMenuItem jMenuItemViewDebug;
    private javax.swing.JMenuItem jMenuItemViewDocuments;
    private javax.swing.JMenuItem jMenuItemViewLSAResults;
    private javax.swing.JMenuItem jMenuItemViewVisualResults;
    private javax.swing.JMenu jMenuLSA;
    private javax.swing.JMenu jMenuPreferences;
    private javax.swing.JMenu jMenuView;
    private javax.swing.JSeparator jSeparator;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    // End of variables declaration//GEN-END:variables

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) 
    {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LSATApplication().setVisible(true);
            }
        });
    }
}

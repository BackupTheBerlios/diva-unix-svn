/*
 * DocumentClassDialog.java
 *
 * Created on 22 December 2005, 18:50
 */

package lsat.gui;

import javax.swing.*;
import lsat.structures.*;

/**
 *
 * @author  Administrator
 */
public class DocumentClassDialog extends javax.swing.JDialog 
{
    private Project p;
    private int selectedDocumentClass;
    
    public static final int NOCLASS = -1;
    private static DocumentClassDialog d;
    
    /** Creates new form DocumentClassDialog */
    public DocumentClassDialog(JFrame owner, Project p)
    {
        super(owner, "Select document class...", true);
        initComponents();
        
        this.p = p;
        selectedDocumentClass = -1;
        
        this.populateJList();
        
        setLocationRelativeTo(owner);
        this.jButtonOK.setEnabled(false);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jScrollPane = new javax.swing.JScrollPane();
        jList = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButtonOK = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jButtonAddNew = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        jPanel1.setLayout(new java.awt.BorderLayout());

        jList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListMouseClicked(evt);
            }
        });

        jScrollPane.setViewportView(jList);

        jPanel1.add(jScrollPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jButtonOK.setText("OK");
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });

        jPanel3.add(jButtonOK);

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jPanel3.add(jButtonCancel);

        jPanel2.add(jPanel3, java.awt.BorderLayout.EAST);

        jButtonAddNew.setText("Add new document class...");
        jButtonAddNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddNewActionPerformed(evt);
            }
        });

        jPanel4.add(jButtonAddNew);

        jPanel2.add(jPanel4, java.awt.BorderLayout.WEST);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListMouseClicked
        if(this.jList.getSelectedIndex() == -1)
        {
            jButtonOK.setEnabled(false);            
        }
        else
        {
            jButtonOK.setEnabled(true);            
        }
    }//GEN-LAST:event_jListMouseClicked

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        this.selectedDocumentClass = -1;
        this.hideWindow();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOKActionPerformed
        this.selectedDocumentClass = jList.getSelectedIndex();
        this.hideWindow();
    }//GEN-LAST:event_jButtonOKActionPerformed

    private void jButtonAddNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddNewActionPerformed
        String name = JOptionPane.showInputDialog(this, "Please enter a name for the new document class");
        p.addDocumentClass(name);
        jList.setSelectedIndex(jList.getModel().getSize());
        this.populateJList();
    }//GEN-LAST:event_jButtonAddNewActionPerformed
    
    private void populateJList()
    {
        DefaultListModel m = new DefaultListModel();
        for(int i=0;i<p.documentClassCollectionSize();i++)
        {
            m.addElement(p.getDocumenClass(i+1).getDescription());
        }
        
        jList.setModel(m);
    }
    
    public DocumentClass getSelectedDocumentClass()
    {
        DocumentClass returnValue = null;
        
        if(selectedDocumentClass != DocumentClassDialog.NOCLASS)
        {
            // rememeber that documentClasses are 1 based
            returnValue = p.getDocumenClass(selectedDocumentClass + 1);
        }
        
        return returnValue;
    }
    
    public void hideWindow()
    {
        this.setVisible(false);
    }
    
    public static DocumentClassDialog showClassDialog(JFrame owner, Project p)
    {
        if(d == null)
        {
            d = new DocumentClassDialog(owner, p);
        }
        
        d.setVisible(true);
        return d;
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddNew;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JList jList;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane;
    // End of variables declaration//GEN-END:variables
    
}
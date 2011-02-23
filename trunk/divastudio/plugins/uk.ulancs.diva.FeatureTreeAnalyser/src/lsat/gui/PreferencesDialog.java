/*
 * PreferencesDialog.java
 *
 * Created on 28 February 2006, 13:48
 */

package lsat.gui;

import lsat.lsa.LSAModule;
import lsat.structures.*;
import javax.swing.*;
import java.awt.event.*;
/**
 *
 * @author  stone
 */
public class PreferencesDialog extends javax.swing.JDialog
{
    private LSATPreferences thePrefs;
    private static PreferencesDialog d;

    /** Creates new form PreferencesDialog */
    public PreferencesDialog(JFrame owner, LSATPreferences thePrefs)
    {
        super(owner, "Preferences", false);
        initComponents();
        
        this.thePrefs = thePrefs;
        
        //d.setLocationRelativeTo(owner);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        buttonGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jButtonCancel = new javax.swing.JButton();
        jButtonOK = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        sentencesPerChunkField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        dimensionsField = new javax.swing.JTextField();
        customRegexField = new javax.swing.JTextField();
        linewiseButton = new javax.swing.JRadioButton();
        paragraphwiseButton = new javax.swing.JRadioButton();
        customregexButton = new javax.swing.JRadioButton();

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButtonCancelActionPerformed(evt);
            }
        });

        jButtonOK.setText("OK");
        jButtonOK.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButtonOKActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(617, Short.MAX_VALUE)
                .add(jButtonOK)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonCancel)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButtonCancel)
                    .add(jButtonOK))
                .addContainerGap())
        );
        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Preferences"));
        jLabel1.setText("Sentences per chunk");

        sentencesPerChunkField.setText("0");

        jLabel2.setText("Dimensions of Ak");

        dimensionsField.setText("0");

        buttonGroup.add(linewiseButton);
        linewiseButton.setText("Chunk source files line-wise");
        linewiseButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        linewiseButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        linewiseButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                linewiseButtonActionPerformed(evt);
            }
        });

        buttonGroup.add(paragraphwiseButton);
        paragraphwiseButton.setText("Chunk source files paragraph-wise");
        paragraphwiseButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        paragraphwiseButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

        buttonGroup.add(customregexButton);
        customregexButton.setText("Custom regex");
        customregexButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        customregexButton.setMargin(new java.awt.Insets(0, 0, 0, 0));

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(linewiseButton)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(customregexButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(customRegexField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE))
                            .add(paragraphwiseButton)
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabel1)
                                    .add(jLabel2))
                                .add(4, 4, 4)
                                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(dimensionsField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                                    .add(sentencesPerChunkField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE))))
                        .add(355, 355, 355)))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(linewiseButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(paragraphwiseButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(customregexButton)
                    .add(customRegexField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(sentencesPerChunkField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(dimensionsField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(286, Short.MAX_VALUE))
        );
        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents

    private void linewiseButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_linewiseButtonActionPerformed
    {//GEN-HEADEREND:event_linewiseButtonActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_linewiseButtonActionPerformed

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonOKActionPerformed
    {//GEN-HEADEREND:event_jButtonOKActionPerformed
        this.setPreferences();
        this.setVisible(false);
    }//GEN-LAST:event_jButtonOKActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonCancelActionPerformed
    {//GEN-HEADEREND:event_jButtonCancelActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButtonCancelActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JTextField customRegexField;
    private javax.swing.JRadioButton customregexButton;
    private javax.swing.JTextField dimensionsField;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton linewiseButton;
    private javax.swing.JRadioButton paragraphwiseButton;
    private javax.swing.JTextField sentencesPerChunkField;
    // End of variables declaration//GEN-END:variables
    
    public void getPreferences()
    {
        String sentencesPerChunk = thePrefs.get("lsa-boundarysize");
        
        if(Integer.parseInt(sentencesPerChunk) == LSAModule.LINEWISE)
        {
            linewiseButton.setSelected(true);
        }
        else if(Integer.parseInt(sentencesPerChunk) == LSAModule.PARAWISE)
        {
            paragraphwiseButton.setSelected(true);
        }
        else if(Integer.parseInt(sentencesPerChunk) == LSAModule.CUSTOMREGEX)
        {
            customregexButton.setSelected(true);
        }
        
        sentencesPerChunkField.setText(sentencesPerChunk);
        
        dimensionsField.setText(thePrefs.get("lsa-reducedrows"));
        customRegexField.setText(thePrefs.get("lsa-regex"));
    }
    
    private void setPreferences()
    {
        if(linewiseButton.isSelected())
        {
            thePrefs.put("lsa-boundarysize", String.valueOf(LSAModule.LINEWISE));
        }
        else if(paragraphwiseButton.isSelected())
        {
            thePrefs.put("lsa-boundarysize", String.valueOf(LSAModule.PARAWISE));
        }
        else if(customregexButton.isSelected())
        {
            thePrefs.put("lsa-boundarysize", String.valueOf(LSAModule.CUSTOMREGEX));;
        }
        else
        {
            thePrefs.put("lsa-boundarysize", sentencesPerChunkField.getText());
        }
        thePrefs.put("lsa-regex", customRegexField.getText());
        thePrefs.put("lsa-reducedrows", dimensionsField.getText());
    }
    
    public static void showPreferencesDialog(JFrame owner, LSATPreferences thePrefs)
    {
        if(d == null)
        {
            d = new PreferencesDialog(owner, thePrefs);
        }
        
        d.getPreferences();
       
        d.setSize(300,500);
        d.setLocationRelativeTo(owner);
        d.setVisible(true);
    }
}
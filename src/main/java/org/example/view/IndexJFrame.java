/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package org.example.view;

import java.awt.*;
import javax.swing.JOptionPane;

import org.example.dto.UserDTO;
import org.example.service.LoginService;

/**
 *
 * @author maxi
 */
public class IndexJFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(IndexJFrame.class.getName());
    private UserDTO user;
   

    /**
     * Creates new form IndexJFrame
     */
    
    public IndexJFrame(UserDTO user) {
        initComponents();
        this.user = user;
        NewExpenseJPanel expense = new NewExpenseJPanel();
        jLabelBienvenidoUsuario.setText("Bienvenido "+user.getName());

        jPanelMain.setLayout(new CardLayout());
        // Se inicializan los paneles que se van a mostrar en el jPanelMain
        BalancesJPanel balances = new BalancesJPanel(user);
        UserJPanel userPanel = new UserJPanel(user);
        EditPaymentJPanel editPaymentPanel = new EditPaymentJPanel(user, this);
        EditExpenseJPanel editExpensePanel = new EditExpenseJPanel(user, this);

        jPanelMain.add(balances, "Balances");
        jPanelMain.add(userPanel, "UserPanel");
        jPanelMain.add(editPaymentPanel, "EditPaymentPanel");
        jPanelMain.add(editExpensePanel, "EditExpensePanel");
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane = new javax.swing.JScrollPane();
        jTextPane = new javax.swing.JTextPane();
        jLabelBienvenidoUsuario = new javax.swing.JLabel();
        jButtonEditUser = new javax.swing.JButton();
        jButtonNewPayment = new javax.swing.JButton();
        jButtonBalances = new javax.swing.JButton();
        JButtonNewExpense = new javax.swing.JButton();
        jPanelMain = new javax.swing.JPanel();
        jButtonOtherUsers = new javax.swing.JButton();
        jButtonEditPayment = new javax.swing.JButton();
        jButtonEditExpense = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        jScrollPane.setViewportView(jTextPane);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));
        setMinimumSize(new java.awt.Dimension(1000, 600));
        setPreferredSize(new java.awt.Dimension(900, 600));

        jLabelBienvenidoUsuario.setText("Mensaje de bienvenida");

        jButtonEditUser.setFont(new java.awt.Font("Helvetica Neue", 1, 12)); // NOI18N
        jButtonEditUser.setText("Editar Mi Usuario");
        jButtonEditUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditUserActionPerformed(evt);
            }
        });

        jButtonNewPayment.setFont(new java.awt.Font("Helvetica Neue", 1, 12)); // NOI18N
        jButtonNewPayment.setText("Nuevo Pago");
        jButtonNewPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewPaymentActionPerformed(evt);
            }
        });

        jButtonBalances.setFont(new java.awt.Font("Helvetica Neue", 1, 12)); // NOI18N
        jButtonBalances.setText("Mi Estado");
        jButtonBalances.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBalancesActionPerformed(evt);
            }
        });

        JButtonNewExpense.setFont(new java.awt.Font("Helvetica Neue", 1, 12)); // NOI18N
        JButtonNewExpense.setText("Nuevo Gasto");
        JButtonNewExpense.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JButtonNewExpenseActionPerformed(evt);
            }
        });

        jPanelMain.setMinimumSize(new java.awt.Dimension(1100, 600));

        javax.swing.GroupLayout jPanelMainLayout = new javax.swing.GroupLayout(jPanelMain);
        jPanelMain.setLayout(jPanelMainLayout);
        jPanelMainLayout.setHorizontalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelMainLayout.setVerticalGroup(
            jPanelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 601, Short.MAX_VALUE)
        );

        jButtonOtherUsers.setFont(new java.awt.Font("Helvetica Neue", 1, 12)); // NOI18N
        jButtonOtherUsers.setText("Amigos");
        jButtonOtherUsers.setToolTipText("");
        jButtonOtherUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOtherUsersActionPerformed(evt);
            }
        });

        jButtonEditPayment.setFont(new java.awt.Font("Helvetica Neue", 1, 12)); // NOI18N
        jButtonEditPayment.setText("Editar Pagos");
        jButtonEditPayment.setToolTipText("");
        jButtonEditPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditPaymentActionPerformed(evt);
            }
        });

        jButtonEditExpense.setFont(new java.awt.Font("Helvetica Neue", 1, 12)); // NOI18N
        jButtonEditExpense.setText("Editar Gastos");
        jButtonEditExpense.setToolTipText("");
        jButtonEditExpense.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditExpenseActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 51, 51));
        jButton1.setText("Cerrar Sesión");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanelMain, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 860, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(JButtonNewExpense)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonEditExpense, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonNewPayment)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonEditPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonOtherUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabelBienvenidoUsuario)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButtonEditUser, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonBalances, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelBienvenidoUsuario)
                    .addComponent(jButtonEditUser)
                    .addComponent(jButton1))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonBalances, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonOtherUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonEditPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonNewPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JButtonNewExpense, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonEditExpense, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1)
                .addComponent(jPanelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 1, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonEditUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditUserActionPerformed

        if (user != null) {
            EditUserJFrame editUser = new EditUserJFrame(user); // Paso el usuario porque lo voy a usar
            editUser.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "No estas logueado, cierra el programa y vuelve a loguearte", "Edición fallida", JOptionPane.ERROR_MESSAGE);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonEditUserActionPerformed

    private void JButtonNewExpenseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JButtonNewExpenseActionPerformed
        if (user != null) {
            NewExpenseJFrame newExpense = new NewExpenseJFrame(user); // Paso el usuario porque lo voy a usar. No tiene constructor vacío
            newExpense.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "No estas logueado, cierra el programa y vuelve a loguearte", "Edición fallida", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_JButtonNewExpenseActionPerformed

    private void jButtonNewPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewPaymentActionPerformed
        if (user != null) {
            NewPaymentJFrame newPayment = new NewPaymentJFrame(user); // Paso el usuario porque lo voy a usar. No tiene constructor vacío
            newPayment.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "No estas logueado, cierra el programa y vuelve a loguearte", "Edición fallida", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButtonNewPaymentActionPerformed

    private void jButtonBalancesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBalancesActionPerformed
        // TODO add your handling code here:

        CardLayout layout = (CardLayout) jPanelMain.getLayout();
        layout.show(jPanelMain, "Balances");

    }//GEN-LAST:event_jButtonBalancesActionPerformed

    private void jButtonOtherUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOtherUsersActionPerformed

        // TODO add your handling code here:
        CardLayout layout = (CardLayout) jPanelMain.getLayout();
        layout.show(jPanelMain,"UserPanel");
    }//GEN-LAST:event_jButtonOtherUsersActionPerformed

    private void jButtonEditPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditPaymentActionPerformed
        CardLayout layout = (CardLayout) jPanelMain.getLayout();
        layout.show(jPanelMain,"EditPaymentPanel");
    }//GEN-LAST:event_jButtonEditPaymentActionPerformed

    private void jButtonEditExpenseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditExpenseActionPerformed
        CardLayout layout = (CardLayout) jPanelMain.getLayout();
        layout.show(jPanelMain,"EditExpensePanel");
    }//GEN-LAST:event_jButtonEditExpenseActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        LoginService loginService = new LoginService();
        loginService.logout(user);
        LoginJFrame login = new LoginJFrame();
        login.setVisible(true);
        this.dispose();
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButtonNewExpense;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonBalances;
    private javax.swing.JButton jButtonEditExpense;
    private javax.swing.JButton jButtonEditPayment;
    private javax.swing.JButton jButtonEditUser;
    private javax.swing.JButton jButtonNewPayment;
    private javax.swing.JButton jButtonOtherUsers;
    private javax.swing.JLabel jLabelBienvenidoUsuario;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JTextPane jTextPane;
    // End of variables declaration//GEN-END:variables
}

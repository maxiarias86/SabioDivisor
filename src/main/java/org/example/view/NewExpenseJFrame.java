/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package org.example.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import org.example.dto.ExpenseDTO;
import org.example.dto.UserDTO;

/**
 *
 * @author maxi
 */
public class NewExpenseJFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(NewExpenseJFrame.class.getName());
    private UserDTO user;

    /**
     * Creates new form NewExpenseJFrame
     */
    public NewExpenseJFrame(UserDTO user) {
        initComponents();
        this.user = user;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldDescription = new javax.swing.JTextField();
        jFormattedTextFieldAmount = new javax.swing.JFormattedTextField();
        jFormattedTextFieldDate = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jFormattedTextFieldInstallments = new javax.swing.JFormattedTextField();
        jButtonAddExpense = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Nuevo Gasto");

        jLabel2.setText("Motivo");

        jTextFieldDescription.setColumns(6);

        jFormattedTextFieldAmount.setColumns(6);
        jFormattedTextFieldAmount.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        jFormattedTextFieldDate.setColumns(6);
        jFormattedTextFieldDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("d/M/yy"))));

        jLabel3.setText("Día");

        jLabel4.setText("Monto");

        jLabel5.setText("Cuotas");

        jFormattedTextFieldInstallments.setColumns(6);
        jFormattedTextFieldInstallments.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jFormattedTextFieldInstallments.setText("1");

        jButtonAddExpense.setText("Cargar Pagadores y Deudores");
        jButtonAddExpense.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddExpenseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jTextFieldDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel5))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jFormattedTextFieldDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jFormattedTextFieldAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jFormattedTextFieldInstallments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jButtonAddExpense)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addComponent(jLabel1)))
                .addContainerGap(73, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jFormattedTextFieldDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jFormattedTextFieldAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jFormattedTextFieldInstallments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonAddExpense)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAddExpenseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddExpenseActionPerformed
        // TODO add your handling code here:
        ExpenseDTO expenseDTO = new ExpenseDTO(); //Creo un nuevo DTO para el gasto
        String description = jTextFieldDescription.getText().trim();//Obtengo la descripción del gasto
        expenseDTO.setDescription(description);//Se la asigno
        try {
            String dateInput = jFormattedTextFieldDate.getText().trim();//Obtengo la fecha del campo de texto
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yy");//Defino el formato de fecha esperado
            LocalDate date = LocalDate.parse(dateInput, formatter);//Parseo la fecha
            System.out.println("Fecha válida: " + date);
            expenseDTO.setDate(date);//Asigno la fecha al DTO
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fecha inválida. Usá el formato dd/mm/aa", "Error de fecha", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Object amountValue = jFormattedTextFieldAmount.getValue();//Obtengo el valor del campo de texto del monto
            if (amountValue == null) {
                JOptionPane.showMessageDialog(this,"Ingrese un monto correcto", "Error",       JOptionPane.ERROR_MESSAGE);
            }
            double amount = ((Number) amountValue).doubleValue();//Convierto el valor a double
            expenseDTO.setAmount(amount);//Asigno el monto al DTO

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try{
            Object value = jFormattedTextFieldInstallments.getValue();//Obtengo el valor del campo de texto de cuotas
            if (value == null) {
                JOptionPane.showMessageDialog(this, "Ingrese un número de cuotas válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            int installments = ((Number) value).intValue();//Convierto el valor a int
            expenseDTO.setInstallments(installments);//Asigno las cuotas al DTO
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        expenseDTO.setPayers(null);//Inicializo la lista de pagadores como nula
        expenseDTO.setDebtors(null);//Inicializo la lista de deudores como nula
        
        if (user != null && expenseDTO != null) {//Verifico que el usuario esté logueado y que el DTO no sea nulo
            PayersDebtorsJFrame newPayers = new PayersDebtorsJFrame(expenseDTO,this,user);//Creo un nuevo JFrame para agregar pagadores y deudores
            newPayers.setVisible(true);//Lo hago visible
            this.setVisible(false);//Oculto el JFrame actual. Lo elimino cuando se cierre el de pagadores y deudores
        } else {
            JOptionPane.showMessageDialog(this, "No estas logueado, cierra el programa y vuelve a loguearte", "Edición fallida", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_jButtonAddExpenseActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddExpense;
    private javax.swing.JFormattedTextField jFormattedTextFieldAmount;
    private javax.swing.JFormattedTextField jFormattedTextFieldDate;
    private javax.swing.JFormattedTextField jFormattedTextFieldInstallments;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField jTextFieldDescription;
    // End of variables declaration//GEN-END:variables
}

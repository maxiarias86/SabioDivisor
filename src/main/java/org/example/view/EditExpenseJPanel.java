/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package org.example.view;

import org.example.cache.ExpenseCache;
import org.example.dto.UserDTO;
import org.example.model.Debt;
import org.example.model.Expense;
import org.example.model.Response;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author maxi
 */
public class EditExpenseJPanel extends javax.swing.JPanel {
    private UserDTO user;
    private JFrame parentFrame;

    /**
     * Creates new form EditPaymentJPanel
     */
    public EditExpenseJPanel(UserDTO user, JFrame parentFrame) {
        initComponents();
        this.user = user;
        this.parentFrame = parentFrame;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");

        String expensesText = "Tus gastos: \n\n";// Inicializa el texto de gastos
        for (Expense expense : ExpenseCache.getInstance(user).getExpenses()) {// Recorre los gastos del usuario
            if (expense.getDescription() != null && !expense.getDescription().isBlank()) {// Verifica que la descripción no sea nula o vacía
                expensesText += "ID: " + expense.getId() + "\n";// Agrega el ID del gasto
                String descripcion = expense.getDescription();// Obtiene la descripción del gasto
                if (descripcion.length() > 30) {// Si la descripción es mayor a 30 caracteres, la acorta
                    descripcion = descripcion.substring(0, 30) + "...";// Acorta la descripción a 30 caracteres y agrega "..." al final
                }
                expensesText += "Descripción: " + descripcion + "\n";// Agrega la descripción del gasto acortada
                expensesText += "Monto: $" + String.format("%.2f",expense.getAmount()) + "\n";// Agrega el monto del gasto
                expensesText += "Fecha: " + expense.getDate().format(formatter) + "\n";// Agrega la fecha del gasto
                expensesText += "Cuotas: " + expense.getInstallments() + "\n";// Agrega la cantidad de cuotas del gasto
                expensesText += "Deudas: \n";// Agrega el encabezado de deudas
                for (Debt debt : expense.getDebts()) {// Recorre las deudas asociadas al gasto
                    expensesText += "- " + debt.getDebtor().getName() +// Agrega el nombre del deudor
                            " debe a " + debt.getCreditor().getName() +// Agrega el nombre del acreedor
                            " $" + String.format("%.2f",debt.getAmount()) + // Agrega el monto de la deuda
                            " (cuota " + debt.getInstallmentNumber() + ")" +// Agrega el número de cuota de la deuda
                            "\n";
                }
                expensesText += "\n";
            }
        }
        jTextAreaExpenses.setText(expensesText);
        jTextAreaExpenses.setEditable(false);

        jTextFieldError.setText("");
        jTextFieldError.setEditable(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelEditExpenseId = new javax.swing.JLabel();
        jTextFieldExpenseId = new javax.swing.JTextField();
        jButtonEdit = new javax.swing.JButton();
        jTextFieldError = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaExpenses = new javax.swing.JTextArea();

        jLabelEditExpenseId.setText("Editar gasto ID: ");

        jTextFieldExpenseId.setColumns(3);

        jButtonEdit.setText("Editar");
        jButtonEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditActionPerformed(evt);
            }
        });

        jTextFieldError.setColumns(30);
        jTextFieldError.setText("jTextFieldError");

        jScrollPane1.setMinimumSize(new java.awt.Dimension(900, 400));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(900, 400));

        jTextAreaExpenses.setColumns(40);
        jTextAreaExpenses.setRows(100);
        jTextAreaExpenses.setMinimumSize(new java.awt.Dimension(430, 430));
        jScrollPane1.setViewportView(jTextAreaExpenses);

        jScrollPane2.setViewportView(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabelEditExpenseId)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldExpenseId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldError, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelEditExpenseId)
                    .addComponent(jTextFieldExpenseId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonEdit)
                        .addComponent(jTextFieldError, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonEditActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButtonEditActionPerformed
        // TODO add your handling code here:
        //Selecciona el PAyment con ese ID y abre el EditPaymentJFrame correspondiente. Se debe bloquear que trate de editar un pago en el que no participa.
        int expenseId;// Variable para almacenar el ID del gasto
        try {
            expenseId = Integer.parseInt(jTextFieldExpenseId.getText());// Intenta convertir el texto del TextField en un entero
            Response expenseResponse = ExpenseCache.getInstance(user).getExpenseById(expenseId);//
            if (!expenseResponse.isSuccess()) {
                jTextFieldError.setEditable(true);
                jTextFieldError.setText("El pago no se relaciona al usuario logueado.");
                jTextFieldError.setEditable(false);

                return; // Si no se encuentra el gasto, no hace nada
            }
            Expense expense = (Expense) expenseResponse.getObj();
            //Revisar todas las debts, y si el usuario no es ni el pagador ni el receptor, no lo deja editar.
            boolean isParticipant = false;
            for (Debt debt : expense.getDebts()) {
                if (debt.getCreditor().getId() == user.getId() || debt.getDebtor().getId() == user.getId()) {
                    isParticipant = true;
                    break;
                }
            }
            if (!isParticipant) {
                jTextFieldError.setEditable(true);
                jTextFieldError.setText("No puedes editar un pago en el que no participas.");
                jTextFieldError.setEditable(false);
                return; // Si el usuario no es ni el pagador ni el receptor, no hacemos nada
            }
            EditExpenseJFrame editExpenseFrame = new EditExpenseJFrame(user,expense);
            editExpenseFrame.setVisible(true);
            parentFrame.dispose();

        } catch (NumberFormatException e) {// Si ocurre un error al convertir el texto a entero
            jTextFieldError.setEditable(true);
            jTextFieldError.setText("El ID del gasto debe ser un número válido.");
            jTextFieldError.setEditable(false);

        }
    }//GEN-LAST:event_jButtonEditActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonEdit;
    private javax.swing.JLabel jLabelEditExpenseId;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextAreaExpenses;
    private javax.swing.JTextField jTextFieldError;
    private javax.swing.JTextField jTextFieldExpenseId;
    // End of variables declaration//GEN-END:variables
}

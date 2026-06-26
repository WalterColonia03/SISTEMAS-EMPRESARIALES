package Vista;

import Clases.Cliente;
import Modelo.ClienteDAO;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author SEBASTIAN GR
 */
public class NewClient extends javax.swing.JInternalFrame {

    public NewClient() {
        initComponents();
        this.setSize(new Dimension(500, 400));
        this.setTitle("Nuevo Cliente");

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt_nombre = new javax.swing.JTextField();
        txt_apellido = new javax.swing.JTextField();
        txt_DNI = new javax.swing.JTextField();
        txt_telefono = new javax.swing.JTextField();
        jButton_Guardar = new javax.swing.JButton();
        txt_direccion = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("Nuevo Cliente");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, -1, -1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Telefono:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 190, 90, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Nombre:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 90, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Apellido:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 110, 90, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("DNI:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 150, 90, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Direccion:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 230, 90, -1));

        txt_nombre.addActionListener(this::txt_nombreActionPerformed);
        txt_nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_nombreKeyTyped(evt);
            }
        });
        getContentPane().add(txt_nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 70, 200, -1));

        txt_apellido.addActionListener(this::txt_apellidoActionPerformed);
        txt_apellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_apellidoKeyTyped(evt);
            }
        });
        getContentPane().add(txt_apellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 110, 200, -1));

        txt_DNI.addActionListener(this::txt_DNIActionPerformed);
        txt_DNI.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_DNIKeyTyped(evt);
            }
        });
        getContentPane().add(txt_DNI, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 150, 200, -1));

        txt_telefono.addActionListener(this::txt_telefonoActionPerformed);
        txt_telefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_telefonoKeyTyped(evt);
            }
        });
        getContentPane().add(txt_telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 190, 200, -1));

        jButton_Guardar.setBackground(new java.awt.Color(102, 102, 102));
        jButton_Guardar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton_Guardar.setForeground(new java.awt.Color(255, 255, 255));
        jButton_Guardar.setText("Guardar");
        jButton_Guardar.addActionListener(this::jButton_GuardarActionPerformed);
        getContentPane().add(jButton_Guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 300, 90, 30));

        txt_direccion.addActionListener(this::txt_direccionActionPerformed);
        getContentPane().add(txt_direccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 230, 200, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nombreActionPerformed

    private void txt_apellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_apellidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_apellidoActionPerformed

    private void jButton_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_GuardarActionPerformed
        ClienteDAO dao = new ClienteDAO();
        List<Cliente> lista = dao.listarTodos();

        String nombre = txt_nombre.getText().trim();
        String apellido = txt_apellido.getText().trim();
        String dni = txt_DNI.getText().trim();
        String telefono = txt_telefono.getText().trim();
        String direccion = txt_direccion.getText().trim();

        // Validasilos campos estan llenos si no muestra un mensaje
        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios");
            return;
        }
        if (!dni.matches("\\d{8}")) {
            JOptionPane.showMessageDialog(this, "El DNI debe tener 8 dígitos");
            return;
        }

        if (!telefono.matches("\\d{9}")) {
            JOptionPane.showMessageDialog(this, "El teléfono debe tener 9 dígitos");
            return;
        }

        for (Cliente c : lista) {

            if (c.getDni().equals(dni)) {
                JOptionPane.showMessageDialog(this, "El DNI ya está registrado");
                return;
            }
        }

        int nuevoId = dao.generarId();

        // Se crea el objeto cliente
        Cliente nuevo = new Cliente(nuevoId, nombre, apellido, dni, telefono, direccion, 1);

        // Aqui lo guarda
        dao.guardar(nuevo);

        JOptionPane.showMessageDialog(this, "Cliente guardado correctamente");

        // Y a la hora de guardar los datos esto limpia los cuadros de texto
        txt_nombre.setText("");
        txt_apellido.setText("");
        txt_DNI.setText("");
        txt_telefono.setText("");
        txt_direccion.setText("");
    }//GEN-LAST:event_jButton_GuardarActionPerformed

    private void txt_direccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_direccionActionPerformed

    }//GEN-LAST:event_txt_direccionActionPerformed

    private void txt_DNIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_DNIActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_DNIActionPerformed

    private void txt_telefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_telefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_telefonoActionPerformed

    private void txt_nombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nombreKeyTyped
        char c = evt.getKeyChar();

        if (!Character.isLetter(c) && c != ' ' && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
            evt.consume();
            JOptionPane.showMessageDialog(this, "Solo se permiten letras");
        }
    }//GEN-LAST:event_txt_nombreKeyTyped

    private void txt_apellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_apellidoKeyTyped
        char c = evt.getKeyChar();

        if (!Character.isLetter(c) && c != ' ' && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
            evt.consume();
            JOptionPane.showMessageDialog(this, "Solo se permiten letras");
        }// TODO add your handling code here:
    }//GEN-LAST:event_txt_apellidoKeyTyped

    private void txt_DNIKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_DNIKeyTyped
        char c = evt.getKeyChar();

        // Solo números
        if (!Character.isDigit(c)&& c != java.awt.event.KeyEvent.VK_BACK_SPACE) {

            evt.consume();
            JOptionPane.showMessageDialog(this, "Solo se permiten números");
        }

        // Máximo 8 dígitos
        if (txt_DNI.getText().length() >= 8) {
            evt.consume();
        }

        // No permitir espacios
        if (c == ' ') {
            evt.consume();
    }//GEN-LAST:event_txt_DNIKeyTyped
    }
    private void txt_telefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_telefonoKeyTyped
        char c = evt.getKeyChar();

        // Solo números y permitir borrar
        if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
            evt.consume();
            JOptionPane.showMessageDialog(this, "Solo se permiten números");
        }
        // Máximo 9 dígitos
        if (txt_telefono.getText().length() >= 9) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_telefonoKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Guardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField txt_DNI;
    private javax.swing.JTextField txt_apellido;
    private javax.swing.JTextField txt_direccion;
    private javax.swing.JTextField txt_nombre;
    private javax.swing.JTextField txt_telefono;
    // End of variables declaration//GEN-END:variables

}

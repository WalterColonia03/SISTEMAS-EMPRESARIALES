package Vista;

import Clases.Usuario;
import Modelo.UsuarioDAO;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author SEBASTIAN GR
 */
public class NewUser extends javax.swing.JInternalFrame {

    public NewUser() {
        initComponents();
        this.setSize(new Dimension(500, 400));
        this.setTitle("Nuevo Usuario");

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
        txt_usuario = new javax.swing.JTextField();
        jButton_Guardar = new javax.swing.JButton();
        txt_telefono = new javax.swing.JTextField();
        jPassword = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        jComboBox_rol = new javax.swing.JComboBox<>();

        setClosable(true);
        setIconifiable(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("Nuevo Usuario");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, -1, -1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Password:");
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
        jLabel5.setText("Usuario:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 150, 90, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Telefono:");
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

        txt_usuario.addActionListener(this::txt_usuarioActionPerformed);
        getContentPane().add(txt_usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 150, 200, -1));

        jButton_Guardar.setBackground(new java.awt.Color(153, 153, 153));
        jButton_Guardar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton_Guardar.setForeground(new java.awt.Color(255, 255, 255));
        jButton_Guardar.setText("Guardar");
        jButton_Guardar.addActionListener(this::jButton_GuardarActionPerformed);
        getContentPane().add(jButton_Guardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 310, 90, 30));

        txt_telefono.addActionListener(this::txt_telefonoActionPerformed);
        txt_telefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_telefonoKeyTyped(evt);
            }
        });
        getContentPane().add(txt_telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 230, 200, -1));
        getContentPane().add(jPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 190, 200, -1));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Rol:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 270, 110, -1));

        jComboBox_rol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione Rol:", "Administrador", "Vendedor" }));
        jComboBox_rol.addActionListener(this::jComboBox_rolActionPerformed);
        getContentPane().add(jComboBox_rol, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 270, 200, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nombreActionPerformed

    }//GEN-LAST:event_txt_nombreActionPerformed

    private void txt_apellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_apellidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_apellidoActionPerformed

    private void jButton_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_GuardarActionPerformed

        UsuarioDAO dao = new UsuarioDAO();
        List<Usuario> lista = dao.listarTodos();

        String nombre = txt_nombre.getText().trim();
        String apellido = txt_apellido.getText().trim();
        String usuario = txt_usuario.getText().trim();
        String password = new String(jPassword.getPassword());
        String telefono = txt_telefono.getText().trim();
        String rol = jComboBox_rol.getSelectedItem().toString();

        // Validar cada dato
        if (nombre.isEmpty() || apellido.isEmpty() || usuario.isEmpty() || password.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos");
            return;
        }

        if (jComboBox_rol.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un rol");
            return;
        }

        if (!telefono.matches("\\d{9}")) {
            JOptionPane.showMessageDialog(this, "El teléfono debe tener 9 dígitos");
            return;
        }

        for (Usuario u : lista) {
            if (u.getUsuario().equalsIgnoreCase(usuario)) {
                JOptionPane.showMessageDialog(this, "El usuario ya existe");
                return;
            }
        }

        int nuevoId = dao.generarId();

        // Agrega eso en la lista
        Usuario nuevo = new Usuario(nuevoId, nombre, apellido, usuario, password, telefono, rol, 1);

        dao.guardar(nuevo);

        JOptionPane.showMessageDialog(this, "Usuario guardado correctamente");

        // limpiar
        txt_nombre.setText("");
        txt_apellido.setText("");
        txt_usuario.setText("");
        txt_telefono.setText("");
        jPassword.setText("");
        jComboBox_rol.setSelectedIndex(0);
    }//GEN-LAST:event_jButton_GuardarActionPerformed

    private void txt_telefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_telefonoActionPerformed

    }//GEN-LAST:event_txt_telefonoActionPerformed

    private void txt_usuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_usuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_usuarioActionPerformed

    private void jComboBox_rolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_rolActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox_rolActionPerformed

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
        }
    }//GEN-LAST:event_txt_apellidoKeyTyped

    private void txt_telefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_telefonoKeyTyped
        char c = evt.getKeyChar();

        // Solo números y permitir borrar
        if (!Character.isDigit(c)&& c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
            evt.consume();
            JOptionPane.showMessageDialog(this,"Solo se permiten números");
        }
        // Máximo 9 dígitos
        if (txt_telefono.getText().length() >= 9) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_telefonoKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Guardar;
    private javax.swing.JComboBox<String> jComboBox_rol;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPasswordField jPassword;
    private javax.swing.JTextField txt_apellido;
    private javax.swing.JTextField txt_nombre;
    private javax.swing.JTextField txt_telefono;
    private javax.swing.JTextField txt_usuario;
    // End of variables declaration//GEN-END:variables
}

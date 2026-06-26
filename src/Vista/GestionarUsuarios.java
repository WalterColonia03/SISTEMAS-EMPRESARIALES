package Vista;

import Clases.Usuario;
import Modelo.UsuarioDAO;
import Utils.PasswordUtils;          // CORRECCIÓN OWASP A02: para hash de contraseñas
import java.awt.Dimension;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author SEBASTIAN GR
 */
public class GestionarUsuarios extends javax.swing.JInternalFrame {

    /**
     * Creates new form GestionarCategoria
     */
    public GestionarUsuarios() {
        initComponents();
        this.setSize(new Dimension(900, 500));
        this.setTitle("Gestion de Usuarios");

        cargarTabla();

        jTable_usuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int fila = jTable_usuarios.getSelectedRow();

                if (fila != -1) {
                    txt_nombre.setText(jTable_usuarios.getValueAt(fila, 1).toString()); //nombre
                    txt_apellido.setText(jTable_usuarios.getValueAt(fila, 2).toString()); //apellido
                    txt_usuario.setText(jTable_usuarios.getValueAt(fila, 3).toString()); //usuario
                    txt_password.setText(jTable_usuarios.getValueAt(fila, 4).toString()); //password
                    txt_telefono.setText(jTable_usuarios.getValueAt(fila, 5).toString()); //telefono
                    String rol = jTable_usuarios.getValueAt(fila, 6).toString(); //rol
                    jComboBox1.setSelectedItem(rol);
                }
            }
        });

//        ImageIcon wallpaper = new ImageIcon(getClass().getResource("/Imagenes/fondo3.jpg"));
//        Icon icono = new ImageIcon(wallpaper.getImage().getScaledInstance(900, 500, WIDTH));
//        jLabel_wallpaper.setIcon(icono);
//        this.repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_usuarios = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButton_Actulizar = new javax.swing.JButton();
        jButton_Eliminar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_password = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_usuario = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_nombre = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_apellido = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txt_telefono = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel_wallpaper = new javax.swing.JLabel();

        setClosable(true);
        setMaximizable(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Administrar Usuarios");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, 190, -1));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable_usuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable_usuarios);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 710, 250));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 730, 270));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton_Actulizar.setBackground(new java.awt.Color(0, 153, 0));
        jButton_Actulizar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton_Actulizar.setForeground(new java.awt.Color(255, 255, 255));
        jButton_Actulizar.setText("Actualizar");
        jButton_Actulizar.addActionListener(this::jButton_ActulizarActionPerformed);
        jPanel2.add(jButton_Actulizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, 30));

        jButton_Eliminar.setBackground(new java.awt.Color(153, 0, 0));
        jButton_Eliminar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton_Eliminar.setForeground(new java.awt.Color(255, 255, 255));
        jButton_Eliminar.setText("Eliminar");
        jButton_Eliminar.addActionListener(this::jButton_EliminarActionPerformed);
        jPanel2.add(jButton_Eliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 42, 90, 30));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 50, 130, 270));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Password:");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, 90, -1));

        txt_password.addActionListener(this::txt_passwordActionPerformed);
        jPanel3.add(txt_password, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 10, 170, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Usuario:");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, 90, -1));

        txt_usuario.addActionListener(this::txt_usuarioActionPerformed);
        jPanel3.add(txt_usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, 170, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Nombre:");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 90, -1));

        txt_nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_nombreKeyTyped(evt);
            }
        });
        jPanel3.add(txt_nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 170, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Apellido:");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 90, -1));

        txt_apellido.addActionListener(this::txt_apellidoActionPerformed);
        txt_apellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_apellidoKeyTyped(evt);
            }
        });
        jPanel3.add(txt_apellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, 170, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Telefono:");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 60, 90, -1));

        txt_telefono.addActionListener(this::txt_telefonoActionPerformed);
        txt_telefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_telefonoKeyTyped(evt);
            }
        });
        jPanel3.add(txt_telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 60, 170, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Rol:");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 50, 70, 30));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar Rol:", "Administrador", "Vendedor" }));
        jPanel3.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 50, 170, 30));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 870, 100));
        getContentPane().add(jLabel_wallpaper, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 890, 470));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_ActulizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ActulizarActionPerformed

        int fila = jTable_usuarios.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario");
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        List<Usuario> lista = dao.listarTodos();

        String nombre = txt_nombre.getText().trim();
        String apellido = txt_apellido.getText().trim();
        String usuario = txt_usuario.getText().trim();
        String password = txt_password.getText().trim();
        String telefono = txt_telefono.getText().trim();
        String rol = jComboBox1.getSelectedItem().toString();

        // VALIDACIONES
        if (nombre.isEmpty() || apellido.isEmpty()
                || usuario.isEmpty() || password.isEmpty()
                || telefono.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Complete todos los campos");
            return;
        }

        if (jComboBox1.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un rol");
            return;
        }

        if (!telefono.matches("\\d{9}")) {
            JOptionPane.showMessageDialog(this,
                    "El teléfono debe tener 9 dígitos");
            return;
        }

        int id = Integer.parseInt(
                jTable_usuarios.getValueAt(fila, 0).toString());

        for (Usuario u : lista) {

            if (u.getIdUsuario() == id) {

                u.setNombre(nombre);
                u.setApellido(apellido);
                u.setUsuario(usuario);
                // CORRECCIÓN OWASP A02: hashear contraseña con SHA-256 antes de persistir
                // (2026-06-26 — Auditoría ERP)
                u.setPassword(PasswordUtils.hashPassword(password));
                u.setTelefono(telefono);
                u.setRol(rol);
                dao.actualizar(u);

                break;
            }
        }

        JOptionPane.showMessageDialog(this,
                "Usuario actualizado");

        cargarTabla();
    }//GEN-LAST:event_jButton_ActulizarActionPerformed

    private void jButton_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EliminarActionPerformed

        int fila = jTable_usuarios.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario");
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        int id = Integer.parseInt(jTable_usuarios.getValueAt(fila, 0).toString());

        dao.eliminar(id);

        JOptionPane.showMessageDialog(this, "Usuario eliminado");

        cargarTabla();
    }//GEN-LAST:event_jButton_EliminarActionPerformed

    private void txt_passwordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_passwordActionPerformed

    }//GEN-LAST:event_txt_passwordActionPerformed

    private void txt_usuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_usuarioActionPerformed

    }//GEN-LAST:event_txt_usuarioActionPerformed

    private void txt_apellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_apellidoActionPerformed

    }//GEN-LAST:event_txt_apellidoActionPerformed

    private void txt_telefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_telefonoActionPerformed

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
        }
    }//GEN-LAST:event_txt_apellidoKeyTyped

    private void txt_telefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_telefonoKeyTyped
        char c = evt.getKeyChar();

        // Solo permite numeros
        if (!Character.isDigit(c)
                && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {

            evt.consume();

            JOptionPane.showMessageDialog(this,
                    "Solo se permiten números");
        }

        // Máximo 9 dígitos
        if (txt_telefono.getText().length() >= 9) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_telefonoKeyTyped

    private void cargarTabla() {
        
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{
                    "ID",
                    "Nombre",
                    "Apellido",
                    "Usuario",
                    "Password",
                    "Telefono",
                    "Rol",
                    "Estado"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        UsuarioDAO dao = new UsuarioDAO();
        List<Usuario> lista = dao.listarTodos();

        Object[] fila = new Object[8];

        for (Usuario u : lista) {
            fila[0] = u.getIdUsuario();
            fila[1] = u.getNombre();
            fila[2] = u.getApellido();
            fila[3] = u.getUsuario();
            fila[4] = u.getPassword();
            fila[5] = u.getTelefono();
            fila[6] = u.getRol();
            fila[7] = u.getEstado();

            modelo.addRow(fila);
        }

        jTable_usuarios.setModel(modelo);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Actulizar;
    private javax.swing.JButton jButton_Eliminar;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel_wallpaper;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_usuarios;
    private javax.swing.JTextField txt_apellido;
    private javax.swing.JTextField txt_nombre;
    private javax.swing.JTextField txt_password;
    private javax.swing.JTextField txt_telefono;
    private javax.swing.JTextField txt_usuario;
    // End of variables declaration//GEN-END:variables
}

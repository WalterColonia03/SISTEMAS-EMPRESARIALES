package Vista;

import Clases.Cliente;
import ArchivosTXT.ArchivoClienteTXT;
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
public class GestionarClientes extends javax.swing.JInternalFrame {

    /**
     * Creates new form GestionarCategoria
     */
    public GestionarClientes() {
        initComponents();
        this.setSize(new Dimension(900, 500));
        this.setTitle("Gestion de Clientes");

        cargarTabla();

        jTable_clientes.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int fila = jTable_clientes.getSelectedRow();

                if (fila != -1) {
                    txt_nombre.setText(jTable_clientes.getValueAt(fila, 1).toString()); //nombre
                    txt_apellido.setText(jTable_clientes.getValueAt(fila, 2).toString()); // apellido
                    txt_DNI.setText(jTable_clientes.getValueAt(fila, 3).toString()); // dni
                    txt_telefono.setText(jTable_clientes.getValueAt(fila, 4).toString()); // telefono
                    txt_direccion.setText(jTable_clientes.getValueAt(fila, 5).toString()); // direccion
                }
            }
        });

//        ImageIcon wallpaper = new ImageIcon(getClass().getResource(" /"Directorio"/"archivo" "));
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
        jTable_clientes = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButton_Actulizar = new javax.swing.JButton();
        jButton_Eliminar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_direccion = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_DNI = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_nombre = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_apellido = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txt_telefono = new javax.swing.JTextField();
        jLabel_wallpaper = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Administrar Clientes");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, 190, -1));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable_clientes.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTable_clientes);

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
        jLabel2.setForeground(new java.awt.Color(255, 0, 51));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Direccion:");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, 90, -1));

        txt_direccion.addActionListener(this::txt_direccionActionPerformed);
        jPanel3.add(txt_direccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 10, 170, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("DNI:");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, 90, -1));

        txt_DNI.addActionListener(this::txt_DNIActionPerformed);
        txt_DNI.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_DNIKeyTyped(evt);
            }
        });
        jPanel3.add(txt_DNI, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, 170, -1));

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

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 870, 100));
        getContentPane().add(jLabel_wallpaper, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 890, 470));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_ActulizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ActulizarActionPerformed
        int fila = jTable_clientes.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente");
            return;
        }

        String nombre = txt_nombre.getText().trim();
        String apellido = txt_apellido.getText().trim();
        String dni = txt_DNI.getText().trim();
        String telefono = txt_telefono.getText().trim();
        String direccion = txt_direccion.getText().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete los campos obligatorios");
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

        ArchivoClienteTXT archivo = new ArchivoClienteTXT();
        List<Cliente> lista = archivo.leer();

        int id = Integer.parseInt(jTable_clientes.getValueAt(fila, 0).toString());

        for (Cliente c : lista) {

            if (c.getDni().equals(dni) && c.getIdCliente() != id) {

                JOptionPane.showMessageDialog(this, "El DNI ya está registrado");
                return;
            }
        }

        for (Cliente c : lista) {
            if (c.getIdCliente() == id) {
                c.setNombre(nombre);
                c.setApellido(apellido);
                c.setDni(dni);
                c.setTelefono(telefono);
                c.setDireccion(direccion);
                break;
            }
        }

        archivo.guardar(lista);

        JOptionPane.showMessageDialog(this, "Cliente actualizado");

        cargarTabla();
        //Limpia los casilleros
        txt_nombre.setText("");
        txt_apellido.setText("");
        txt_DNI.setText("");
        txt_telefono.setText("");
        txt_direccion.setText("");
    }//GEN-LAST:event_jButton_ActulizarActionPerformed

    private void jButton_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EliminarActionPerformed
        int fila = jTable_clientes.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar este cliente?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        ArchivoClienteTXT archivo = new ArchivoClienteTXT();
        List<Cliente> lista = archivo.leer();

        int id = Integer.parseInt(jTable_clientes.getValueAt(fila, 0).toString());

        lista.removeIf(c -> c.getIdCliente() == id);

        archivo.guardar(lista);

        JOptionPane.showMessageDialog(this, "Cliente eliminado");

        cargarTabla();
        //Limpia los casilleros
        txt_nombre.setText("");
        txt_apellido.setText("");
        txt_DNI.setText("");
        txt_telefono.setText("");
        txt_direccion.setText("");
    }//GEN-LAST:event_jButton_EliminarActionPerformed

    private void txt_direccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_direccionActionPerformed

    }//GEN-LAST:event_txt_direccionActionPerformed

    private void txt_DNIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_DNIActionPerformed

    }//GEN-LAST:event_txt_DNIActionPerformed

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

    private void txt_DNIKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_DNIKeyTyped
        char c = evt.getKeyChar();

        // Solo números
        if (!Character.isDigit(c)
                && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {

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
        }
    }//GEN-LAST:event_txt_DNIKeyTyped

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

    public void cargarTabla() {

        ArchivoClienteTXT archivo = new ArchivoClienteTXT();
        List<Cliente> lista = archivo.leer();

        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("DNI");
        modelo.addColumn("Telefono");
        modelo.addColumn("Direccion");
        modelo.addColumn("Estado");

        for (Cliente c : lista) {
            Object[] fila = {
                c.getIdCliente(),
                c.getNombre(),
                c.getApellido(),
                c.getDni(),
                c.getTelefono(),
                c.getDireccion(),
                c.getEstado()
            };
            modelo.addRow(fila);
        }

        jTable_clientes.setModel(modelo);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Actulizar;
    private javax.swing.JButton jButton_Eliminar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel_wallpaper;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_clientes;
    private javax.swing.JTextField txt_DNI;
    private javax.swing.JTextField txt_apellido;
    private javax.swing.JTextField txt_direccion;
    private javax.swing.JTextField txt_nombre;
    private javax.swing.JTextField txt_telefono;
    // End of variables declaration//GEN-END:variables
}

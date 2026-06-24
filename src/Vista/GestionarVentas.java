package Vista;

import Clases.Venta;
import ArchivosTXT.ArchivoVentaTXT;
import java.awt.Dimension;
import java.util.List;
//import javax.swing.Icon;
//import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author SEBASTIAN GR
 */
public class GestionarVentas extends javax.swing.JInternalFrame {

    /**
     * Creates new form GestionarCategoria
     */
    public GestionarVentas() {
        initComponents();
        this.setSize(new Dimension(900, 500));
        this.setTitle("Gestion de Usuarios");

        cargarTabla();
        cargarClientes();

        jTable_ventas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int fila = jTable_ventas.getSelectedRow();

                if (fila != -1) {
                    txt_TotalPagar.setText(
                            jTable_ventas.getValueAt(fila, 2).toString());

                    txt_fecha.setText(
                            jTable_ventas.getValueAt(fila, 3).toString());

                    jComboBox_cliente.setSelectedItem(
                            jTable_ventas.getValueAt(fila, 1).toString());
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
        jTable_ventas = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButton_Actulizar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_TotalPagar = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_fecha = new javax.swing.JTextField();
        jComboBox_cliente = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel_wallpaper = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Administrar Ventas");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, 180, -1));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable_ventas.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTable_ventas);

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

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 50, 130, 270));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Cliente:");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, 90, 30));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Total Pagar:");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 90, -1));

        txt_TotalPagar.setEnabled(false);
        txt_TotalPagar.addActionListener(this::txt_TotalPagarActionPerformed);
        txt_TotalPagar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_TotalPagarKeyTyped(evt);
            }
        });
        jPanel3.add(txt_TotalPagar, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 170, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Fecha:");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 90, -1));

        txt_fecha.setEnabled(false);
        txt_fecha.addActionListener(this::txt_fechaActionPerformed);
        txt_fecha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_fechaKeyTyped(evt);
            }
        });
        jPanel3.add(txt_fecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, 170, -1));

        jComboBox_cliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar Cliente:" }));
        jComboBox_cliente.addActionListener(this::jComboBox_clienteActionPerformed);
        jPanel3.add(jComboBox_cliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, 170, 30));

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("Registro");
        jButton1.addActionListener(this::jButton1ActionPerformed);
        jPanel3.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 20, 140, 60));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 870, 100));
        getContentPane().add(jLabel_wallpaper, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 890, 470));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_ActulizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ActulizarActionPerformed

        int fila = jTable_ventas.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(this,
                    "Seleccione una venta");

            return;
        }

        ArchivoVentaTXT archivo = new ArchivoVentaTXT();

        List<Venta> lista = archivo.leer();

        int idVenta = Integer.parseInt(
                jTable_ventas.getValueAt(fila, 0).toString());

        String nuevoCliente
                = jComboBox_cliente.getSelectedItem().toString();

        for (Venta v : lista) {

            if (v.getIdVenta() == idVenta) {

                v.setCliente(nuevoCliente);

                break;
            }
        }

        archivo.guardar(lista);

        JOptionPane.showMessageDialog(this,
                "Cliente actualizado correctamente");

        cargarTabla();
    }//GEN-LAST:event_jButton_ActulizarActionPerformed

    private void txt_fechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_fechaActionPerformed

    }//GEN-LAST:event_txt_fechaActionPerformed

    private void txt_TotalPagarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_TotalPagarKeyTyped
        char c = evt.getKeyChar();

        if (!Character.isLetter(c) && c != ' ' && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
            evt.consume();
            JOptionPane.showMessageDialog(this, "Solo se permiten letras");
        }
    }//GEN-LAST:event_txt_TotalPagarKeyTyped

    private void txt_fechaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_fechaKeyTyped
        char c = evt.getKeyChar();

        if (!Character.isLetter(c) && c != ' ' && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
            evt.consume();
            JOptionPane.showMessageDialog(this, "Solo se permiten letras");
        }
    }//GEN-LAST:event_txt_fechaKeyTyped

    private void jComboBox_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_clienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox_clienteActionPerformed

    private void txt_TotalPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_TotalPagarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_TotalPagarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Registro_Ventas rv = new Registro_Ventas();

        javax.swing.JDesktopPane desktopPane = this.getDesktopPane();

        desktopPane.add(rv);

        rv.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cargarTabla() {

        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{
                    "ID Venta",
                    "Cliente",
                    "Total",
                    "Fecha"
                }, 0
        ) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ArchivoVentaTXT archivo = new ArchivoVentaTXT();

        List<Venta> lista = archivo.leer();

        Object[] fila = new Object[4];

        for (Venta v : lista) {

            fila[0] = v.getIdVenta();
            fila[1] = v.getCliente();
            fila[2] = v.getTotal();
            fila[3] = v.getFecha();

            modelo.addRow(fila);
        }

        jTable_ventas.setModel(modelo);
    }

    public void cargarClientes() {

        ArchivoVentaTXT archivo = new ArchivoVentaTXT();

        List<Venta> lista = archivo.leer();

        jComboBox_cliente.removeAllItems();

        jComboBox_cliente.addItem("Seleccionar Cliente:");

        for (Venta v : lista) {

            jComboBox_cliente.addItem(v.getCliente());
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton_Actulizar;
    private javax.swing.JComboBox<String> jComboBox_cliente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel_wallpaper;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_ventas;
    private javax.swing.JTextField txt_TotalPagar;
    private javax.swing.JTextField txt_fecha;
    // End of variables declaration//GEN-END:variables
}

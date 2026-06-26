package Vista;

import Clases.Producto;
import Modelo.ProductoDAO;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author SEBASTIAN GR
 */
public class ActulizarStock extends javax.swing.JInternalFrame {

    public ActulizarStock() {
        initComponents();
        this.setSize(new Dimension(500, 400));
        this.setTitle("Actualziar Stock");

        cargarProductos();

        jTextField_stockActual.setEditable(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField_stockActual = new javax.swing.JTextField();
        jTextField_StockNuevo = new javax.swing.JTextField();
        jComboBox_productos = new javax.swing.JComboBox<>();
        jButton_actulizar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("Actualizar Stock de Productos");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, -1, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Producto:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 110, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Stock Actual:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 130, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Stock Nuevo:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 220, 140, -1));

        jTextField_stockActual.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextField_stockActual.setEnabled(false);
        jTextField_stockActual.addActionListener(this::jTextField_stockActualActionPerformed);
        getContentPane().add(jTextField_stockActual, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 160, 260, 30));

        jTextField_StockNuevo.addActionListener(this::jTextField_StockNuevoActionPerformed);
        jTextField_StockNuevo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField_StockNuevoKeyTyped(evt);
            }
        });
        getContentPane().add(jTextField_StockNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 220, 260, 30));

        jComboBox_productos.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBox_productos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione Producto:", "Item 2", "Item 3", "Item 4" }));
        jComboBox_productos.addActionListener(this::jComboBox_productosActionPerformed);
        getContentPane().add(jComboBox_productos, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 300, 30));

        jButton_actulizar.setBackground(new java.awt.Color(153, 153, 153));
        jButton_actulizar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton_actulizar.setForeground(new java.awt.Color(255, 255, 255));
        jButton_actulizar.setText("Actulizar");
        jButton_actulizar.addActionListener(this::jButton_actulizarActionPerformed);
        getContentPane().add(jButton_actulizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 290, 160, 30));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField_stockActualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_stockActualActionPerformed

    }//GEN-LAST:event_jTextField_stockActualActionPerformed

    private void jComboBox_productosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_productosActionPerformed

        if (jComboBox_productos.getSelectedItem() == null) {
            return;
        }

        if (jComboBox_productos.getSelectedIndex() == 0) {
            jTextField_stockActual.setText("");
            return;
        }

        ProductoDAO dao = new ProductoDAO();
        List<Producto> lista = dao.listarTodos();

        String seleccion = jComboBox_productos.getSelectedItem().toString();
        int id = Integer.parseInt(seleccion.split("-")[0]);

        for (Producto p : lista) {
            if (p.getIdProducto() == id) {
                jTextField_stockActual.setText(String.valueOf(p.getCantidad()));
                break;
            }
        }

    }//GEN-LAST:event_jComboBox_productosActionPerformed

    private void jTextField_StockNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_StockNuevoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_StockNuevoActionPerformed

    private void jButton_actulizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_actulizarActionPerformed

        if (jComboBox_productos.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto");
            return;
        }

        String nuevoStockTxt = jTextField_StockNuevo.getText().trim();

        if (nuevoStockTxt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nuevo stock");
            return;
        }

        int nuevoStock;

        try {
            nuevoStock = Integer.parseInt(nuevoStockTxt);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido");
            return;
        }

        ProductoDAO dao = new ProductoDAO();
        List<Producto> lista = dao.listarTodos();

        String seleccion = jComboBox_productos.getSelectedItem().toString();
        int id = Integer.parseInt(seleccion.split("-")[0]);

        for (Producto p : lista) {
            if (p.getIdProducto() == id) {
                p.setCantidad(nuevoStock);
                dao.actualizar(p);
                break;
            }
        }

        JOptionPane.showMessageDialog(this, "Stock actualizado correctamente");

        jTextField_stockActual.setText("");
        jTextField_StockNuevo.setText("");
        jComboBox_productos.setSelectedIndex(0);
    }//GEN-LAST:event_jButton_actulizarActionPerformed

    private void jTextField_StockNuevoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_StockNuevoKeyTyped
        char c = evt.getKeyChar();

        // Solo números y borrar
        if (!Character.isDigit(c)
                && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {

            evt.consume();
            JOptionPane.showMessageDialog(this, "Stock invalido");
        }

        // No permitir espacios
        if (c == ' ') {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField_StockNuevoKeyTyped

    public void cargarProductos() {
        try {
            ProductoDAO dao = new ProductoDAO();
            List<Producto> lista = dao.listarTodos();

            jComboBox_productos.removeAllItems();
            jComboBox_productos.addItem("Seleccione Producto");

            for (Producto p : lista) {
                jComboBox_productos.addItem(p.getIdProducto() + "-" + p.getNombre() + " (" + p.getDescripcion() + ")");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando productos");
            e.printStackTrace();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_actulizar;
    private javax.swing.JComboBox<String> jComboBox_productos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField jTextField_StockNuevo;
    private javax.swing.JTextField jTextField_stockActual;
    // End of variables declaration//GEN-END:variables
}

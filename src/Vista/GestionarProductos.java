package Vista;

import Clases.Categoria;
import Clases.Producto;
import ArchivosTXT.ArchivoCategoriaTXT;
import ArchivosTXT.ArchivoProductoTXT;
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
public class GestionarProductos extends javax.swing.JInternalFrame {

    /**
     * Creates new form GestionarCategoria
     */
    public GestionarProductos() {
        initComponents();
        this.setSize(new Dimension(1100, 700));
        this.setTitle("Gestion de categorias");

        cargarTabla();
        cargarCategorias();

        jTable_productos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int fila = jTable_productos.getSelectedRow();

                if (fila != -1) {

                    txt_nombre.setText(jTable_productos.getValueAt(fila, 1).toString());
                    txt_cantidad.setText(jTable_productos.getValueAt(fila, 2).toString());
                    txt_precio.setText(jTable_productos.getValueAt(fila, 3).toString());
                    txt_descipcion.setText(jTable_productos.getValueAt(fila, 4).toString());

                    String categoria = jTable_productos.getValueAt(fila, 5).toString();

                    for (int i = 0; i < jComboBox_categoria.getItemCount(); i++) {

                        String item = jComboBox_categoria.getItemAt(i);

                        if (item.contains(categoria)) {
                            jComboBox_categoria.setSelectedIndex(i);
                            break;
                        }
                    }
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
        jTable_productos = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButton_Actulizar = new javax.swing.JButton();
        jButton_Eliminar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_cantidad = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_precio = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_nombre = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txt_descipcion = new javax.swing.JTextField();
        jComboBox_categoria = new javax.swing.JComboBox<>();
        jLabel_wallpaper = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Administrar Productos");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, -1, -1));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable_productos.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTable_productos);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 910, 460));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 930, 480));

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

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 50, 130, 90));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Cantidad:");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 10, 90, -1));

        txt_cantidad.setEnabled(false);
        txt_cantidad.addActionListener(this::txt_cantidadActionPerformed);
        jPanel3.add(txt_cantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 10, 170, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Precio:");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, 90, -1));

        txt_precio.addActionListener(this::txt_precioActionPerformed);
        jPanel3.add(txt_precio, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, 180, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Nombre:");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 90, -1));
        jPanel3.add(txt_nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 190, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Categoria:");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 90, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Descripcion:");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 60, 90, -1));

        txt_descipcion.addActionListener(this::txt_descipcionActionPerformed);
        jPanel3.add(txt_descipcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 60, 180, -1));

        jComboBox_categoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione categoria:", "Item 2", "Item 3", "Item 4" }));
        jComboBox_categoria.addActionListener(this::jComboBox_categoriaActionPerformed);
        jPanel3.add(jComboBox_categoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, 190, -1));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 550, 970, 100));
        getContentPane().add(jLabel_wallpaper, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1090, 670));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_ActulizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ActulizarActionPerformed
        int fila = jTable_productos.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto");
            return;
        }

        ArchivoProductoTXT archivo = new ArchivoProductoTXT();
        List<Producto> lista = archivo.leer();

        String nombre = txt_nombre.getText().trim();
        String cantidadTexto = txt_cantidad.getText().trim();
        String precioTexto = txt_precio.getText().trim();
        String descripcion = txt_descipcion.getText().trim();

        // Validaciones
        if (nombre.isEmpty() || cantidadTexto.isEmpty() || precioTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos");
            return;
        }

        int cantidad = Integer.parseInt(cantidadTexto);
        double precio = Double.parseDouble(precioTexto);

        //Valida que se haya elegido una categoria
        if (jComboBox_categoria.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoria");
            return;
        }

        // Obtener categoria seleccionada
        String seleccion = jComboBox_categoria.getSelectedItem().toString();
        int idCategoria = Integer.parseInt(seleccion.split("-")[0]);

        // Obtener ID real del producto
        int idProducto = lista.get(fila).getIdProducto();

        // Verificar productos duplicados
        for (Producto otro : lista) {

            // Ignorar el mismo producto que estamos editando
            if (otro.getIdProducto() != idProducto) {

                if (otro.getNombre().equalsIgnoreCase(nombre)
                        && otro.getDescripcion().equalsIgnoreCase(descripcion)
                        && otro.getIdCategoria() == idCategoria) {

                    JOptionPane.showMessageDialog(this,
                            "El producto ya existe");

                    return;
                }
            }
        }

        for (Producto p : lista) {

            if (p.getIdProducto() == idProducto) {

                p.setNombre(nombre);
                p.setCantidad(cantidad);
                p.setPrecio(precio);
                p.setDescripcion(descripcion);
                p.setIdCategoria(idCategoria);

                break;
            }
        }

        archivo.guardar(lista);

        JOptionPane.showMessageDialog(this, "Producto actualizado");

        cargarTabla();
        txt_nombre.setText("");
        txt_cantidad.setText("");
        txt_precio.setText("");
        txt_descipcion.setText("");
        jComboBox_categoria.setSelectedIndex(0);
    }//GEN-LAST:event_jButton_ActulizarActionPerformed

    private void jButton_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EliminarActionPerformed
        int fila = jTable_productos.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar producto?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        ArchivoProductoTXT archivo = new ArchivoProductoTXT();
        List<Producto> lista = archivo.leer();

        // Obtener ID real del producto
        int idProducto = lista.get(fila).getIdProducto();

        // Eliminar por ID
        for (Producto p : lista) {

            if (p.getIdProducto() == idProducto) {
                lista.remove(p);
                break;
            }
        }

        archivo.guardar(lista);

        JOptionPane.showMessageDialog(this, "Producto eliminado");

        cargarTabla();
        txt_nombre.setText("");
        txt_cantidad.setText("");
        txt_precio.setText("");
        txt_descipcion.setText("");
        jComboBox_categoria.setSelectedIndex(0);
    }//GEN-LAST:event_jButton_EliminarActionPerformed

    private void txt_cantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cantidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_cantidadActionPerformed

    private void txt_precioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_precioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_precioActionPerformed

    private void txt_descipcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_descipcionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_descipcionActionPerformed

    private void jComboBox_categoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_categoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox_categoriaActionPerformed

    public void cargarTabla() {

        ArchivoProductoTXT archivoP = new ArchivoProductoTXT();
        ArchivoCategoriaTXT archivoC = new ArchivoCategoriaTXT();

        List<Producto> listaP = archivoP.leer();
        List<Categoria> listaC = archivoC.leer();

        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelo.addColumn("N°");
        modelo.addColumn("Nombre");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Precio(c/u)");
        modelo.addColumn("Descripcion");
        modelo.addColumn("Categoria");
        modelo.addColumn("Estado");

        int contador = 1;

        for (Producto p : listaP) {

            String nombreCategoria = "";

            for (Categoria c : listaC) {
                if (c.getIdCategoria() == p.getIdCategoria()) {
                    nombreCategoria = c.getDescripcion();
                    break;
                }
            }

            Object[] fila = {
                contador++,
                p.getNombre(),
                p.getCantidad(),
                p.getPrecio(),
                p.getDescripcion(),
                nombreCategoria,
                p.getEstado()
            };

            modelo.addRow(fila);
        }

        jTable_productos.setModel(modelo);

        // Evita que las columnas cambien de tamaño automáticamente
        jTable_productos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);

        // Tamaño fijo de columnas
        jTable_productos.getColumnModel().getColumn(0).setPreferredWidth(40);   // N°
        jTable_productos.getColumnModel().getColumn(1).setPreferredWidth(200);  // Nombre
        jTable_productos.getColumnModel().getColumn(2).setPreferredWidth(80);   // Cantidad
        jTable_productos.getColumnModel().getColumn(3).setPreferredWidth(90);   // Precio
        jTable_productos.getColumnModel().getColumn(4).setPreferredWidth(250);  // Descripcion
        jTable_productos.getColumnModel().getColumn(5).setPreferredWidth(150);  // Categoria
        jTable_productos.getColumnModel().getColumn(6).setPreferredWidth(70);   // Estado
    }

    public void cargarCategorias() {

        ArchivoCategoriaTXT archivo = new ArchivoCategoriaTXT();
        List<Categoria> lista = archivo.leer();

        jComboBox_categoria.removeAllItems();
        jComboBox_categoria.addItem("Seleccione categoria");

        for (Categoria c : lista) {
            jComboBox_categoria.addItem(c.getIdCategoria() + "-" + c.getDescripcion());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Actulizar;
    private javax.swing.JButton jButton_Eliminar;
    private javax.swing.JComboBox<String> jComboBox_categoria;
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
    private javax.swing.JTable jTable_productos;
    private javax.swing.JTextField txt_cantidad;
    private javax.swing.JTextField txt_descipcion;
    private javax.swing.JTextField txt_nombre;
    private javax.swing.JTextField txt_precio;
    // End of variables declaration//GEN-END:variables
}

package Vista;

import java.awt.Dimension;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

/**
 *
 * @author SEBASTIAN GR
 */
public class Menu extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Menu.class.getName());

    /**
     * Creates new form Menu
     */
    public static JDesktopPane jDesktopPane_menu;

    public Menu() {
        initComponents();
        this.setSize(new Dimension(1200, 700));
        this.setExtendedState(Menu.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setTitle("Sistema de ventas");

        this.setLayout(null);
        jDesktopPane_menu = new JDesktopPane();

        int ancho = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        int alto = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        this.jDesktopPane_menu.setBounds(0, 0, ancho, (alto - 110));
        this.add(jDesktopPane_menu);

        aplicarRol();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu_USUARIO = new javax.swing.JMenu();
        jMenuItem_nuevo_usuario = new javax.swing.JMenuItem();
        jMenuItem_gestionar_usuario = new javax.swing.JMenuItem();
        jMenu_PRODUCTOS = new javax.swing.JMenu();
        jMenuItem_nuevo_producto = new javax.swing.JMenuItem();
        jMenuItem_gestionar_producto = new javax.swing.JMenuItem();
        jMenuItem_actulizar_stock = new javax.swing.JMenuItem();
        jMenu_CATEGORIAS = new javax.swing.JMenu();
        jMenuItem_nueva_categoria = new javax.swing.JMenuItem();
        jMenuItem_gestionar_categoria = new javax.swing.JMenuItem();
        jMenu_CLIENTES = new javax.swing.JMenu();
        jMenuItem_nuevo_cliente = new javax.swing.JMenuItem();
        jMenuItem_gestionar_cliente = new javax.swing.JMenuItem();
        jMenu_nueva_venta = new javax.swing.JMenu();
        jMenuItem_nueva_venta = new javax.swing.JMenuItem();
        jMenuItem_gestionar_venta = new javax.swing.JMenuItem();
        jMenu_REPORTES = new javax.swing.JMenu();
        jMenuItem_reporte_clientes = new javax.swing.JMenuItem();
        jMenuItem_reporte_categoria = new javax.swing.JMenuItem();
        jMenuItem_reporte_producto = new javax.swing.JMenuItem();
        jMenuItem_reporte_venta = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem_cerrar_sesion = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jMenu_USUARIO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/usuario.png"))); // NOI18N
        jMenu_USUARIO.setText("Usuario");
        jMenu_USUARIO.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jMenu_USUARIO.setPreferredSize(new java.awt.Dimension(150, 50));

        jMenuItem_nuevo_usuario.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMenuItem_nuevo_usuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/nuevo-cliente.png"))); // NOI18N
        jMenuItem_nuevo_usuario.setText("Nuevo Usuario");
        jMenuItem_nuevo_usuario.setPreferredSize(new java.awt.Dimension(180, 30));
        jMenuItem_nuevo_usuario.addActionListener(this::jMenuItem_nuevo_usuarioActionPerformed);
        jMenu_USUARIO.add(jMenuItem_nuevo_usuario);

        jMenuItem_gestionar_usuario.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMenuItem_gestionar_usuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/configuraciones.png"))); // NOI18N
        jMenuItem_gestionar_usuario.setText("Gestionar Usuarios");
        jMenuItem_gestionar_usuario.setPreferredSize(new java.awt.Dimension(180, 30));
        jMenuItem_gestionar_usuario.addActionListener(this::jMenuItem_gestionar_usuarioActionPerformed);
        jMenu_USUARIO.add(jMenuItem_gestionar_usuario);

        jMenuBar1.add(jMenu_USUARIO);

        jMenu_PRODUCTOS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/producto.png"))); // NOI18N
        jMenu_PRODUCTOS.setText("Producto");
        jMenu_PRODUCTOS.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jMenu_PRODUCTOS.setPreferredSize(new java.awt.Dimension(150, 50));

        jMenuItem_nuevo_producto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMenuItem_nuevo_producto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/nuevo-producto.png"))); // NOI18N
        jMenuItem_nuevo_producto.setText("Nuevo Producto");
        jMenuItem_nuevo_producto.setPreferredSize(new java.awt.Dimension(200, 30));
        jMenuItem_nuevo_producto.addActionListener(this::jMenuItem_nuevo_productoActionPerformed);
        jMenu_PRODUCTOS.add(jMenuItem_nuevo_producto);

        jMenuItem_gestionar_producto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMenuItem_gestionar_producto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/producto.png"))); // NOI18N
        jMenuItem_gestionar_producto.setText("Gestionar Productos");
        jMenuItem_gestionar_producto.setPreferredSize(new java.awt.Dimension(200, 30));
        jMenuItem_gestionar_producto.addActionListener(this::jMenuItem_gestionar_productoActionPerformed);
        jMenu_PRODUCTOS.add(jMenuItem_gestionar_producto);

        jMenuItem_actulizar_stock.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMenuItem_actulizar_stock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/nuevo.png"))); // NOI18N
        jMenuItem_actulizar_stock.setText("Actualizar Stock");
        jMenuItem_actulizar_stock.setPreferredSize(new java.awt.Dimension(200, 30));
        jMenuItem_actulizar_stock.addActionListener(this::jMenuItem_actulizar_stockActionPerformed);
        jMenu_PRODUCTOS.add(jMenuItem_actulizar_stock);

        jMenuBar1.add(jMenu_PRODUCTOS);

        jMenu_CATEGORIAS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/categorias.png"))); // NOI18N
        jMenu_CATEGORIAS.setText("Categoria");
        jMenu_CATEGORIAS.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jMenu_CATEGORIAS.setPreferredSize(new java.awt.Dimension(150, 50));

        jMenuItem_nueva_categoria.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMenuItem_nueva_categoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/nuevo.png"))); // NOI18N
        jMenuItem_nueva_categoria.setText("Nueva Categoria");
        jMenuItem_nueva_categoria.setPreferredSize(new java.awt.Dimension(200, 30));
        jMenuItem_nueva_categoria.addActionListener(this::jMenuItem_nueva_categoriaActionPerformed);
        jMenu_CATEGORIAS.add(jMenuItem_nueva_categoria);

        jMenuItem_gestionar_categoria.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMenuItem_gestionar_categoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/categorias.png"))); // NOI18N
        jMenuItem_gestionar_categoria.setText("Gestionar Categorias");
        jMenuItem_gestionar_categoria.addActionListener(this::jMenuItem_gestionar_categoriaActionPerformed);
        jMenu_CATEGORIAS.add(jMenuItem_gestionar_categoria);

        jMenuBar1.add(jMenu_CATEGORIAS);

        jMenu_CLIENTES.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cliente.png"))); // NOI18N
        jMenu_CLIENTES.setText("Cliente");
        jMenu_CLIENTES.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jMenu_CLIENTES.setPreferredSize(new java.awt.Dimension(150, 50));

        jMenuItem_nuevo_cliente.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMenuItem_nuevo_cliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/nuevo-cliente.png"))); // NOI18N
        jMenuItem_nuevo_cliente.setText("Nuevo Cliente");
        jMenuItem_nuevo_cliente.setPreferredSize(new java.awt.Dimension(180, 30));
        jMenuItem_nuevo_cliente.addActionListener(this::jMenuItem_nuevo_clienteActionPerformed);
        jMenu_CLIENTES.add(jMenuItem_nuevo_cliente);

        jMenuItem_gestionar_cliente.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMenuItem_gestionar_cliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cliente.png"))); // NOI18N
        jMenuItem_gestionar_cliente.setText("Gestionar Clientes");
        jMenuItem_gestionar_cliente.addActionListener(this::jMenuItem_gestionar_clienteActionPerformed);
        jMenu_CLIENTES.add(jMenuItem_gestionar_cliente);

        jMenuBar1.add(jMenu_CLIENTES);

        jMenu_nueva_venta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/carrito.png"))); // NOI18N
        jMenu_nueva_venta.setText("Facturar");
        jMenu_nueva_venta.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jMenu_nueva_venta.setPreferredSize(new java.awt.Dimension(150, 50));

        jMenuItem_nueva_venta.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMenuItem_nueva_venta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/anadir.png"))); // NOI18N
        jMenuItem_nueva_venta.setText("Nueva Venta");
        jMenuItem_nueva_venta.setPreferredSize(new java.awt.Dimension(200, 30));
        jMenuItem_nueva_venta.addActionListener(this::jMenuItem_nueva_ventaActionPerformed);
        jMenu_nueva_venta.add(jMenuItem_nueva_venta);

        jMenuItem_gestionar_venta.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMenuItem_gestionar_venta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/configuraciones.png"))); // NOI18N
        jMenuItem_gestionar_venta.setText("Gestionar Ventas");
        jMenuItem_gestionar_venta.setPreferredSize(new java.awt.Dimension(200, 30));
        jMenuItem_gestionar_venta.addActionListener(this::jMenuItem_gestionar_ventaActionPerformed);
        jMenu_nueva_venta.add(jMenuItem_gestionar_venta);

        jMenuBar1.add(jMenu_nueva_venta);

        jMenu_REPORTES.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/reportes.png"))); // NOI18N
        jMenu_REPORTES.setText("Reportes");
        jMenu_REPORTES.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jMenu_REPORTES.setPreferredSize(new java.awt.Dimension(150, 50));

        jMenuItem_reporte_clientes.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMenuItem_reporte_clientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/reporte1.png"))); // NOI18N
        jMenuItem_reporte_clientes.setText("Reportes Clientes");
        jMenuItem_reporte_clientes.setPreferredSize(new java.awt.Dimension(200, 30));
        jMenuItem_reporte_clientes.addActionListener(this::jMenuItem_reporte_clientesActionPerformed);
        jMenu_REPORTES.add(jMenuItem_reporte_clientes);

        jMenuItem_reporte_categoria.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMenuItem_reporte_categoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/reporte1.png"))); // NOI18N
        jMenuItem_reporte_categoria.setText("Reportes Categorias");
        jMenuItem_reporte_categoria.setPreferredSize(new java.awt.Dimension(200, 30));
        jMenuItem_reporte_categoria.addActionListener(this::jMenuItem_reporte_categoriaActionPerformed);
        jMenu_REPORTES.add(jMenuItem_reporte_categoria);

        jMenuItem_reporte_producto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMenuItem_reporte_producto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/reporte1.png"))); // NOI18N
        jMenuItem_reporte_producto.setText("Reportes Productos");
        jMenuItem_reporte_producto.setPreferredSize(new java.awt.Dimension(200, 30));
        jMenuItem_reporte_producto.addActionListener(this::jMenuItem_reporte_productoActionPerformed);
        jMenu_REPORTES.add(jMenuItem_reporte_producto);

        jMenuItem_reporte_venta.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMenuItem_reporte_venta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/reporte1.png"))); // NOI18N
        jMenuItem_reporte_venta.setText("Reportes Ventas");
        jMenuItem_reporte_venta.setPreferredSize(new java.awt.Dimension(200, 30));
        jMenuItem_reporte_venta.addActionListener(this::jMenuItem_reporte_ventaActionPerformed);
        jMenu_REPORTES.add(jMenuItem_reporte_venta);

        jMenuBar1.add(jMenu_REPORTES);

        jMenu8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion_1.png"))); // NOI18N
        jMenu8.setText("Cerrar Sesión");
        jMenu8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jMenu8.setMinimumSize(new java.awt.Dimension(200, 50));
        jMenu8.setPreferredSize(new java.awt.Dimension(200, 50));

        jMenuItem_cerrar_sesion.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jMenuItem_cerrar_sesion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrar-sesion_1.png"))); // NOI18N
        jMenuItem_cerrar_sesion.setText("Cerrar sesion");
        jMenuItem_cerrar_sesion.setPreferredSize(new java.awt.Dimension(150, 30));
        jMenuItem_cerrar_sesion.addActionListener(this::jMenuItem_cerrar_sesionActionPerformed);
        jMenu8.add(jMenuItem_cerrar_sesion);

        jMenuBar1.add(jMenu8);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem_nuevo_usuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_nuevo_usuarioActionPerformed
        NewUser newUser = new NewUser();
        jDesktopPane_menu.add(newUser);
        newUser.setVisible(true);
    }//GEN-LAST:event_jMenuItem_nuevo_usuarioActionPerformed

    private void jMenuItem_gestionar_usuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_gestionar_usuarioActionPerformed
        GestionarUsuarios gestionarUsuarios = new GestionarUsuarios();
        jDesktopPane_menu.add(gestionarUsuarios);
        gestionarUsuarios.setVisible(true);
    }//GEN-LAST:event_jMenuItem_gestionar_usuarioActionPerformed

    private void jMenuItem_nuevo_productoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_nuevo_productoActionPerformed
        NewProduct newProducto = new NewProduct();
        jDesktopPane_menu.add(newProducto);
        newProducto.setVisible(true);
    }//GEN-LAST:event_jMenuItem_nuevo_productoActionPerformed

    private void jMenuItem_gestionar_productoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_gestionar_productoActionPerformed
        GestionarProductos gestionarProducto = new GestionarProductos();
        jDesktopPane_menu.add(gestionarProducto);
        gestionarProducto.setVisible(true);
    }//GEN-LAST:event_jMenuItem_gestionar_productoActionPerformed

    private void jMenuItem_nueva_ventaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_nueva_ventaActionPerformed
        NewVent newVenta = new NewVent();
        jDesktopPane_menu.add(newVenta);
        newVenta.setVisible(true);
    }//GEN-LAST:event_jMenuItem_nueva_ventaActionPerformed

    private void jMenuItem_reporte_ventaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_reporte_ventaActionPerformed
        ReporteVentas reportVentas = new ReporteVentas();
        jDesktopPane_menu.add(reportVentas);
        reportVentas.setVisible(true);
    }//GEN-LAST:event_jMenuItem_reporte_ventaActionPerformed

    private void jMenuItem_nuevo_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_nuevo_clienteActionPerformed
        NewClient newCliente = new NewClient();
        jDesktopPane_menu.add(newCliente);
        newCliente.setVisible(true);
    }//GEN-LAST:event_jMenuItem_nuevo_clienteActionPerformed

    private void jMenuItem_reporte_clientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_reporte_clientesActionPerformed
        ReporteClientes reportClientes = new ReporteClientes();
        jDesktopPane_menu.add(reportClientes);
        reportClientes.setVisible(true);
    }//GEN-LAST:event_jMenuItem_reporte_clientesActionPerformed

    private void jMenuItem_reporte_categoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_reporte_categoriaActionPerformed
        ReporteCategoria reportCategorias = new ReporteCategoria();
        jDesktopPane_menu.add(reportCategorias);
        reportCategorias.setVisible(true);
    }//GEN-LAST:event_jMenuItem_reporte_categoriaActionPerformed

    private void jMenuItem_nueva_categoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_nueva_categoriaActionPerformed
        NewCategory newCategoria = new NewCategory();
        jDesktopPane_menu.add(newCategoria);
        newCategoria.setVisible(true);
    }//GEN-LAST:event_jMenuItem_nueva_categoriaActionPerformed

    private void jMenuItem_gestionar_categoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_gestionar_categoriaActionPerformed
        GestionarCategoria gestionarCategoria = new GestionarCategoria();
        jDesktopPane_menu.add(gestionarCategoria);
        gestionarCategoria.setVisible(true);
    }//GEN-LAST:event_jMenuItem_gestionar_categoriaActionPerformed

    private void jMenuItem_gestionar_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_gestionar_clienteActionPerformed
        GestionarClientes gestionarClientes = new GestionarClientes();
        jDesktopPane_menu.add(gestionarClientes);
        gestionarClientes.setVisible(true);
    }//GEN-LAST:event_jMenuItem_gestionar_clienteActionPerformed

    private void jMenuItem_actulizar_stockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_actulizar_stockActionPerformed
        ActulizarStock actStock = new ActulizarStock();
        jDesktopPane_menu.add(actStock);
        actStock.setVisible(true);
    }//GEN-LAST:event_jMenuItem_actulizar_stockActionPerformed

    private void jMenuItem_cerrar_sesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_cerrar_sesionActionPerformed
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea cerrar sesión?",
                "Cerrar Sesión",
                JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {

            // Cerrar ventana actual
            this.dispose();

            // Abrir Login nuevamente
            FrmLogin login = new FrmLogin();

            login.setVisible(true);
            login.setLocationRelativeTo(null);
        }
    }//GEN-LAST:event_jMenuItem_cerrar_sesionActionPerformed

    private void jMenuItem_reporte_productoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_reporte_productoActionPerformed
        ReporteProductos reportProductos = new ReporteProductos();
        jDesktopPane_menu.add(reportProductos);
        reportProductos.setVisible(true);
    }//GEN-LAST:event_jMenuItem_reporte_productoActionPerformed

    private void jMenuItem_gestionar_ventaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_gestionar_ventaActionPerformed
        IFrmReporteVentas gestVentas = new IFrmReporteVentas();
        jDesktopPane_menu.add(gestVentas);
        gestVentas.setVisible(true);
    }//GEN-LAST:event_jMenuItem_gestionar_ventaActionPerformed

    public void aplicarRol() {
        
        String rol = Clases.Sesion.getRol();
        if (rol.equalsIgnoreCase("Vendedor")) {
            jMenu_USUARIO.setVisible(false);
        }
        
        if (rol.equalsIgnoreCase("Vendedor")) {
            jMenu_REPORTES.setVisible(false);
        }
        if (rol.equalsIgnoreCase("Vendedor")) {
            jMenu_CATEGORIAS.setVisible(false);
        }
        if (rol.equalsIgnoreCase("Vendedor")) {
            jMenu_PRODUCTOS.setVisible(false);
        }
        
    }

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
        java.awt.EventQueue.invokeLater(() -> new Menu().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem_actulizar_stock;
    private javax.swing.JMenuItem jMenuItem_cerrar_sesion;
    private javax.swing.JMenuItem jMenuItem_gestionar_categoria;
    private javax.swing.JMenuItem jMenuItem_gestionar_cliente;
    private javax.swing.JMenuItem jMenuItem_gestionar_producto;
    private javax.swing.JMenuItem jMenuItem_gestionar_usuario;
    private javax.swing.JMenuItem jMenuItem_gestionar_venta;
    private javax.swing.JMenuItem jMenuItem_nueva_categoria;
    private javax.swing.JMenuItem jMenuItem_nueva_venta;
    private javax.swing.JMenuItem jMenuItem_nuevo_cliente;
    private javax.swing.JMenuItem jMenuItem_nuevo_producto;
    private javax.swing.JMenuItem jMenuItem_nuevo_usuario;
    private javax.swing.JMenuItem jMenuItem_reporte_categoria;
    private javax.swing.JMenuItem jMenuItem_reporte_clientes;
    private javax.swing.JMenuItem jMenuItem_reporte_producto;
    private javax.swing.JMenuItem jMenuItem_reporte_venta;
    private javax.swing.JMenu jMenu_CATEGORIAS;
    private javax.swing.JMenu jMenu_CLIENTES;
    private javax.swing.JMenu jMenu_PRODUCTOS;
    private javax.swing.JMenu jMenu_REPORTES;
    private javax.swing.JMenu jMenu_USUARIO;
    private javax.swing.JMenu jMenu_nueva_venta;
    // End of variables declaration//GEN-END:variables
}

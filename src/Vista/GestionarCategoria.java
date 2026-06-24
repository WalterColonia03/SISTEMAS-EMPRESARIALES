package Vista;

import Clases.Categoria;
import ArchivosTXT.ArchivoCategoriaTXT;
import ArchivosTXT.ArchivoProductoTXT;
import Clases.Producto;
import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Gestión de Categorías — rediseñada sobre el sistema de diseño UIKit.
 * Lógica de negocio idéntica a la versión original generada por NetBeans;
 * solo cambia la construcción visual (initComponents).
 */
public class GestionarCategoria extends javax.swing.JInternalFrame {

    public GestionarCategoria() {
        initComponents();
        this.setSize(new Dimension(760, 480));
        this.setTitle("Gestión de categorías");

        cargarTabla();

        jTable_categoria.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = jTable_categoria.getSelectedRow();
                if (fila != -1) {
                    String descripcion = jTable_categoria.getValueAt(fila, 1).toString();
                    txt_descripcion.setText(descripcion);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        // ===== Contenedor raíz =====
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(
                UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        // ===== Encabezado =====
        getContentPane().add(
                UIKit.screenHeader("Categorías", "Inventario  ›  Categorías"),
                BorderLayout.NORTH);

        // ===== Cuerpo: tabla + panel de detalle =====
        JPanel cuerpo = new JPanel(new BorderLayout(UIKit.SPACE_LG, 0));
        cuerpo.setOpaque(false);

        // --- Tarjeta de listado ---
        JPanel tarjetaTabla = UIKit.card();
        tarjetaTabla.setLayout(new BorderLayout(0, UIKit.SPACE_SM));

        JButton btnNuevo = UIKit.primaryButton("+ Nueva categoría");
        // TODO (fuera de alcance visual): conectar a un flujo de alta de categoría si no existe aún.
        tarjetaTabla.add(UIKit.sectionHeader("Categorías registradas", btnNuevo), BorderLayout.NORTH);

        jTable_categoria = UIKit.styledTable(new DefaultTableModel(
                new Object[][]{}, new String[]{"ID Categoria", "Descripcion", "Estado"}));
        JScrollPane scroll = new JScrollPane(jTable_categoria);
        scroll.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        tarjetaTabla.add(scroll, BorderLayout.CENTER);

        cuerpo.add(tarjetaTabla, BorderLayout.CENTER);

        // --- Tarjeta de detalle ---
        JPanel tarjetaDetalle = UIKit.card();
        tarjetaDetalle.setPreferredSize(new Dimension(260, 0));
        tarjetaDetalle.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        tarjetaDetalle.add(UIKit.sectionHeader("Editar categoría", null), gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        tarjetaDetalle.add(UIKit.fieldLabel("Descripción"), gbc);

        txt_descripcion = UIKit.textField();
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_LG, 0);
        tarjetaDetalle.add(txt_descripcion, gbc);

        jButton_Actulizar = UIKit.primaryButton("Guardar cambios");
        jButton_Actulizar.addActionListener(this::jButton_ActulizarActionPerformed);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_SM, 0);
        tarjetaDetalle.add(jButton_Actulizar, gbc);

        jButton_Eliminar = UIKit.dangerOutlineButton("Eliminar categoría");
        jButton_Eliminar.addActionListener(this::jButton_EliminarActionPerformed);
        gbc.gridy = 4;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        tarjetaDetalle.add(jButton_Eliminar, gbc);

        cuerpo.add(tarjetaDetalle, BorderLayout.EAST);

        getContentPane().add(cuerpo, BorderLayout.CENTER);
    }

    // ===================================================================
    // A partir de aquí, CERO cambios respecto al archivo original.
    // ===================================================================

    private void jButton_ActulizarActionPerformed(java.awt.event.ActionEvent evt) {
        int fila = jTable_categoria.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría");
            return;
        }

        String nuevaDescripcion = txt_descripcion.getText().trim();

        if (nuevaDescripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una descripción");
            return;
        }

        ArchivoCategoriaTXT archivo = new ArchivoCategoriaTXT();
        List<Categoria> lista = archivo.leer();

        int id = Integer.parseInt(jTable_categoria.getValueAt(fila, 0).toString());

        for (Categoria c : lista) {
            if (c.getDescripcion().equalsIgnoreCase(nuevaDescripcion) && c.getIdCategoria() != id) {
                JOptionPane.showMessageDialog(this, "La categoría ya existe");
                return;
            }
        }

        for (Categoria c : lista) {
            if (c.getIdCategoria() == id) {
                c.setDescripcion(nuevaDescripcion);
                break;
            }
        }

        archivo.guardar(lista);

        JOptionPane.showMessageDialog(this, "Categoría actualizada");

        cargarTabla();
        txt_descripcion.setText("");
    }

    private void jButton_EliminarActionPerformed(java.awt.event.ActionEvent evt) {
        int fila = jTable_categoria.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría");
            return;
        }

        int id = Integer.parseInt(jTable_categoria.getValueAt(fila, 0).toString());

        ArchivoProductoTXT archivoProducto = new ArchivoProductoTXT();
        List<Producto> listaProductos = archivoProducto.leer();
        for (Producto p : listaProductos) {
            if (p.getIdCategoria() == id) {
                JOptionPane.showMessageDialog(this,
                        "No se puede eliminar la categoría porque tiene productos asociados");
                return;
            }
        }

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar esta categoría?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        ArchivoCategoriaTXT archivo = new ArchivoCategoriaTXT();
        List<Categoria> lista = archivo.leer();

        lista.removeIf(c -> c.getIdCategoria() == id);

        archivo.guardar(lista);

        JOptionPane.showMessageDialog(this, "Categoría eliminada");

        cargarTabla();
        txt_descripcion.setText("");
    }

    public void cargarTabla() {

        ArchivoCategoriaTXT archivo = new ArchivoCategoriaTXT();
        List<Categoria> lista = archivo.leer();

        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelo.addColumn("ID Categoria");
        modelo.addColumn("Descripcion");
        modelo.addColumn("Estado");

        for (Categoria c : lista) {
            Object[] fila = {c.getIdCategoria(), c.getDescripcion(), c.getEstado()};
            modelo.addRow(fila);
        }

        jTable_categoria.setModel(modelo);
    }

    // Variables declaration
    private javax.swing.JButton jButton_Actulizar;
    private javax.swing.JButton jButton_Eliminar;
    private javax.swing.JTable jTable_categoria;
    private javax.swing.JTextField txt_descripcion;
}

package Vista;

import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class IFrmGestionProductos extends JInternalFrame {

    private JTable tblProductos;
    private DefaultTableModel modelProductos;
    private JTextField txtBuscar;
    private JComboBox<String> cbFiltroCategoria;

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtCantidad;
    private JTextField txtPrecio;
    private JComboBox<String> cbCategoria;
    private JTextField txtLote;
    private JTextField txtVencimiento;

    private JButton btnBuscar;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    public IFrmGestionProductos() {
        super("Gestión de Productos", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        setSize(1000, 620);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    private void initComponents() {
        txtBuscar = UIKit.textField();
        txtBuscar.setPreferredSize(new Dimension(200, 36));
        
        cbFiltroCategoria = new JComboBox<>(new String[]{"Todas las Categorías", "Lácteos", "Abarrotes", "Bebidas", "Limpieza"});
        cbFiltroCategoria.setFont(UIKit.BODY);
        cbFiltroCategoria.setPreferredSize(new Dimension(160, 36));

        btnBuscar = UIKit.secondaryButton("Buscar");

        String[] columns = {"ID", "Nombre", "Cant.", "Precio", "Categoría", "Lote", "Vencimiento"};
        modelProductos = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblProductos = UIKit.styledTable(modelProductos);

        txtId = UIKit.readOnlyField();
        txtNombre = UIKit.textField();
        txtDescripcion = UIKit.textField();
        
        txtCantidad = UIKit.textField();
        txtCantidad.setHorizontalAlignment(JTextField.RIGHT);
        
        txtPrecio = UIKit.textField();
        txtPrecio.setHorizontalAlignment(JTextField.RIGHT);
        
        cbCategoria = new JComboBox<>(new String[]{"Lácteos", "Abarrotes", "Bebidas", "Limpieza", "Otros"});
        cbCategoria.setFont(UIKit.BODY);
        cbCategoria.setPreferredSize(new Dimension(0, 36));
        
        txtLote = UIKit.textField();
        txtVencimiento = UIKit.textField();

        btnGuardar = UIKit.primaryButton("Guardar / Actualizar");
        btnLimpiar = UIKit.secondaryButton("Limpiar / Nuevo");
        btnEliminar = UIKit.dangerOutlineButton("Eliminar");
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(
                UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        // ===== Encabezado =====
        getContentPane().add(
                UIKit.screenHeader("Gestión de Productos", "Inventario  ›  Productos"),
                BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(UIKit.SPACE_LG, 0));
        cuerpo.setOpaque(false);

        // ── Tarjeta Izquierda: Tabla ──
        JPanel pnlTabla = UIKit.card();
        pnlTabla.setLayout(new BorderLayout(0, UIKit.SPACE_SM));

        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_SM, 0));
        pnlBusqueda.setOpaque(false);
        pnlBusqueda.add(txtBuscar);
        pnlBusqueda.add(cbFiltroCategoria);
        pnlBusqueda.add(btnBuscar);
        
        pnlTabla.add(UIKit.sectionHeader("Listado de Productos", pnlBusqueda), BorderLayout.NORTH);
        
        JScrollPane scroll = new JScrollPane(tblProductos);
        scroll.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        pnlTabla.add(scroll, BorderLayout.CENTER);

        cuerpo.add(pnlTabla, BorderLayout.CENTER);

        // ── Tarjeta Derecha: Formulario ──
        JPanel pnlForm = UIKit.card();
        pnlForm.setPreferredSize(new Dimension(380, 0));
        pnlForm.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);

        gbc.gridx = 0; gbc.gridy = 0;
        pnlForm.add(UIKit.sectionHeader("Detalle del Producto", null), gbc);

        // ID
        gbc.gridwidth = 2;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("ID Producto"), gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(txtId, gbc);

        // Nombre (full width)
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Nombre"), gbc);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(txtNombre, gbc);

        // Descripción (full width)
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Descripción"), gbc);
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(txtDescripcion, gbc);

        // Categoría (col 1) y Precio (col 2)
        gbc.gridwidth = 1;
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, UIKit.SPACE_SM);
        pnlForm.add(UIKit.fieldLabel("Categoría"), gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Precio (S/)"), gbc);

        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, UIKit.SPACE_SM);
        pnlForm.add(cbCategoria, gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(txtPrecio, gbc);

        // Cantidad (col 1) y Lote (col 2)
        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, UIKit.SPACE_SM);
        pnlForm.add(UIKit.fieldLabel("Cantidad"), gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Lote"), gbc);

        gbc.gridy = 10;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, UIKit.SPACE_SM);
        pnlForm.add(txtCantidad, gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(txtLote, gbc);

        // Vencimiento (full width)
        gbc.gridwidth = 2;
        gbc.gridy = 11;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Vencimiento (AAAA-MM-DD)"), gbc);
        
        gbc.gridy = 12;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_LG, 0);
        pnlForm.add(txtVencimiento, gbc);

        // Botones
        JPanel pnlBotones = new JPanel(new GridLayout(2, 2, UIKit.SPACE_SM, UIKit.SPACE_SM));
        pnlBotones.setOpaque(false);
        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnLimpiar);
        pnlBotones.add(btnEliminar);

        gbc.gridy = 13;
        gbc.weighty = 1.0; 
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        pnlForm.add(pnlBotones, gbc);

        cuerpo.add(pnlForm, BorderLayout.EAST);
        
        getContentPane().add(cuerpo, BorderLayout.CENTER);
    }

    private void attachEvents() {
        btnBuscar.addActionListener(e -> {
            // TODO: lógica TXT para buscar productos por nombre o filtrar por categoría
        });

        btnGuardar.addActionListener(e -> {
            // TODO: lógica TXT para crear o actualizar un producto (incluye lote y vencimiento)
        });

        btnEliminar.addActionListener(e -> {
            // TODO: lógica TXT para eliminar producto por ID (estado = 0)
        });

        btnLimpiar.addActionListener(e -> {
            txtId.setText("");
            txtNombre.setText("");
            txtDescripcion.setText("");
            txtCantidad.setText("");
            txtPrecio.setText("");
            cbCategoria.setSelectedIndex(0);
            txtLote.setText("");
            txtVencimiento.setText("");
        });

        tblProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblProductos.getSelectedRow() != -1) {
                int row = tblProductos.getSelectedRow();
                txtId.setText(modelProductos.getValueAt(row, 0).toString());
                txtNombre.setText(modelProductos.getValueAt(row, 1).toString());
                txtCantidad.setText(modelProductos.getValueAt(row, 2).toString());
                txtPrecio.setText(modelProductos.getValueAt(row, 3).toString());
                String cat = modelProductos.getValueAt(row, 4).toString();
                for (int i = 0; i < cbCategoria.getItemCount(); i++) {
                    if (cbCategoria.getItemAt(i).equals(cat)) {
                        cbCategoria.setSelectedIndex(i);
                        break;
                    }
                }
                txtLote.setText(modelProductos.getValueAt(row, 5).toString());
                txtVencimiento.setText(modelProductos.getValueAt(row, 6).toString());
            }
        });
    }
}

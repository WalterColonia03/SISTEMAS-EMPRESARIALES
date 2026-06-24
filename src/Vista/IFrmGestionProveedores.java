package Vista;

import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class IFrmGestionProveedores extends JInternalFrame {

    private JTable tblProveedores;
    private DefaultTableModel modelProveedores;
    private JTextField txtBuscarRuc;

    private JTextField txtId;
    private JTextField txtRuc;
    private JTextField txtRazonSocial;
    private JTextField txtContacto;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtDireccion;
    private JComboBox<String> cbEstado;

    private JButton btnBuscar;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    public IFrmGestionProveedores() {
        super("Gestión de Proveedores", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        setSize(960, 600);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    private void initComponents() {
        txtBuscarRuc = UIKit.textField();
        txtBuscarRuc.setPreferredSize(new Dimension(200, 36));
        
        btnBuscar = UIKit.secondaryButton("Buscar por RUC");

        String[] columns = {"ID", "RUC", "Razón Social", "Contacto", "Teléfono", "Correo", "Dirección", "Estado"};
        modelProveedores = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblProveedores = UIKit.styledTable(modelProveedores);

        txtId = UIKit.readOnlyField();
        txtRuc = UIKit.textField();
        txtRazonSocial = UIKit.textField();
        txtContacto = UIKit.textField();
        txtTelefono = UIKit.textField();
        txtCorreo = UIKit.textField();
        txtDireccion = UIKit.textField();
        
        cbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});
        cbEstado.setFont(UIKit.BODY);

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
                UIKit.screenHeader("Gestión de Proveedores", "Clientes y Proveedores  ›  Proveedores"),
                BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(UIKit.SPACE_LG, 0));
        cuerpo.setOpaque(false);

        // ── Tarjeta Izquierda: Tabla ──
        JPanel pnlTabla = UIKit.card();
        pnlTabla.setLayout(new BorderLayout(0, UIKit.SPACE_SM));

        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_SM, 0));
        pnlBusqueda.setOpaque(false);
        pnlBusqueda.add(txtBuscarRuc);
        pnlBusqueda.add(btnBuscar);
        
        pnlTabla.add(UIKit.sectionHeader("Listado de Proveedores", pnlBusqueda), BorderLayout.NORTH);
        
        JScrollPane scroll = new JScrollPane(tblProveedores);
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

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(UIKit.sectionHeader("Detalle del Proveedor", null), gbc);

        // ID y RUC
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, UIKit.SPACE_SM);
        pnlForm.add(UIKit.fieldLabel("ID Proveedor"), gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("RUC"), gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, UIKit.SPACE_SM);
        pnlForm.add(txtId, gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(txtRuc, gbc);

        // Razón Social (full width)
        gbc.gridwidth = 2;
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Razón Social"), gbc);
        
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(txtRazonSocial, gbc);

        // Contacto (col 1) y Teléfono (col 2)
        gbc.gridwidth = 1;
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, UIKit.SPACE_SM);
        pnlForm.add(UIKit.fieldLabel("Contacto"), gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Teléfono"), gbc);

        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, UIKit.SPACE_SM);
        pnlForm.add(txtContacto, gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(txtTelefono, gbc);

        // Correo (full width)
        gbc.gridwidth = 2;
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Correo Electrónico"), gbc);
        
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(txtCorreo, gbc);

        // Dirección (full width)
        gbc.gridy = 9;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Dirección"), gbc);
        
        gbc.gridy = 10;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(txtDireccion, gbc);

        // Estado
        gbc.gridy = 11;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Estado"), gbc);
        
        gbc.gridy = 12;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_LG, 0);
        pnlForm.add(cbEstado, gbc);

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
            // TODO: lógica TXT
        });

        btnGuardar.addActionListener(e -> {
            // TODO: lógica TXT
        });

        btnEliminar.addActionListener(e -> {
            // TODO: lógica TXT
        });

        btnLimpiar.addActionListener(e -> {
            txtId.setText("");
            txtRuc.setText("");
            txtRazonSocial.setText("");
            txtContacto.setText("");
            txtTelefono.setText("");
            txtCorreo.setText("");
            txtDireccion.setText("");
            cbEstado.setSelectedIndex(0);
        });

        tblProveedores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblProveedores.getSelectedRow() != -1) {
                int row = tblProveedores.getSelectedRow();
                txtId.setText(modelProveedores.getValueAt(row, 0).toString());
                txtRuc.setText(modelProveedores.getValueAt(row, 1).toString());
                txtRazonSocial.setText(modelProveedores.getValueAt(row, 2).toString());
                txtContacto.setText(modelProveedores.getValueAt(row, 3).toString());
                txtTelefono.setText(modelProveedores.getValueAt(row, 4).toString());
                txtCorreo.setText(modelProveedores.getValueAt(row, 5).toString());
                txtDireccion.setText(modelProveedores.getValueAt(row, 6).toString());
                cbEstado.setSelectedItem(modelProveedores.getValueAt(row, 7).toString());
            }
        });
    }
}

package Vista;

import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * IFrmGestionClientes - Fusión de creación, edición y administración de clientes.
 * Rediseñado con UIKit (Patrón A2).
 */
public class IFrmGestionClientes extends JInternalFrame {

    private JTable tblClientes;
    private DefaultTableModel modelClientes;
    private JTextField txtBuscar;
    
    // Campos del Formulario
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtDni;
    private JTextField txtTelefono;
    private JTextField txtCorreo;

    private JButton btnBuscar;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnHistorial;

    public IFrmGestionClientes() {
        super("Gestión de Clientes", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        setSize(960, 600);
        
        // Estilizar title bar nativo Swing a UIKit.PRIMARY (via UIManager or putClientProperty)
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    private void initComponents() {
        txtBuscar = UIKit.textField();
        txtBuscar.setPreferredSize(new Dimension(200, 36));
        
        btnBuscar = UIKit.secondaryButton("Buscar");

        // Tabla de Clientes
        String[] columns = {"ID", "Nombre", "Apellido", "DNI/RUC", "Teléfono", "Correo"};
        modelClientes = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblClientes = UIKit.styledTable(modelClientes);

        // Campos formulario
        txtId = UIKit.readOnlyField();
        txtNombre = UIKit.textField();
        txtApellido = UIKit.textField();
        txtDni = UIKit.textField();
        txtTelefono = UIKit.textField();
        txtCorreo = UIKit.textField();

        // Botones de acción
        btnGuardar = UIKit.primaryButton("Guardar / Actualizar");
        btnLimpiar = UIKit.secondaryButton("Limpiar / Nuevo");
        btnEliminar = UIKit.dangerOutlineButton("Eliminar");
        btnHistorial = UIKit.secondaryButton("Historial Compras");
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(
                UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        // ===== Encabezado =====
        getContentPane().add(
                UIKit.screenHeader("Gestión de Clientes", "Clientes y Proveedores  ›  Clientes"),
                BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(UIKit.SPACE_LG, 0));
        cuerpo.setOpaque(false);

        // ── Tarjeta Izquierda: Tabla de Clientes ──
        JPanel pnlTabla = UIKit.card();
        pnlTabla.setLayout(new BorderLayout(0, UIKit.SPACE_SM));

        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_SM, 0));
        pnlBusqueda.setOpaque(false);
        pnlBusqueda.add(txtBuscar);
        pnlBusqueda.add(btnBuscar);
        
        pnlTabla.add(UIKit.sectionHeader("Listado de Clientes", pnlBusqueda), BorderLayout.NORTH);
        
        JScrollPane scroll = new JScrollPane(tblClientes);
        scroll.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        pnlTabla.add(scroll, BorderLayout.CENTER);

        cuerpo.add(pnlTabla, BorderLayout.CENTER);

        // ── Tarjeta Derecha: Formulario de Registro/Edición ──
        JPanel pnlForm = UIKit.card();
        pnlForm.setPreferredSize(new Dimension(380, 0));
        pnlForm.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2; // Default full width for header/buttons
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);

        gbc.gridx = 0; gbc.gridy = 0;
        pnlForm.add(UIKit.sectionHeader("Detalle del Cliente", null), gbc);

        // ID Cliente (Read-only)
        gbc.gridwidth = 2;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("ID Cliente"), gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(txtId, gbc);

        // Nombre (col 1) y Apellido (col 2)
        gbc.gridwidth = 1;
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, UIKit.SPACE_SM);
        pnlForm.add(UIKit.fieldLabel("Nombre"), gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Apellido"), gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, UIKit.SPACE_SM);
        pnlForm.add(txtNombre, gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(txtApellido, gbc);

        // DNI (col 1) y Teléfono (col 2)
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, UIKit.SPACE_SM);
        pnlForm.add(UIKit.fieldLabel("DNI / RUC"), gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Teléfono"), gbc);

        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, UIKit.SPACE_SM);
        pnlForm.add(txtDni, gbc);
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
        gbc.insets = new Insets(0, 0, UIKit.SPACE_LG, 0);
        pnlForm.add(txtCorreo, gbc);

        // Botones (2x2 grid)
        JPanel pnlBotones = new JPanel(new GridLayout(2, 2, UIKit.SPACE_SM, UIKit.SPACE_SM));
        pnlBotones.setOpaque(false);
        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnLimpiar);
        pnlBotones.add(btnEliminar);
        pnlBotones.add(btnHistorial);

        gbc.gridy = 9;
        gbc.weighty = 1.0; // Push everything up
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        pnlForm.add(pnlBotones, gbc);

        cuerpo.add(pnlForm, BorderLayout.EAST);
        
        getContentPane().add(cuerpo, BorderLayout.CENTER);
    }

    private void attachEvents() {
        btnBuscar.addActionListener(e -> {
            // TODO: lógica TXT para filtrar clientes en la tabla
        });

        btnGuardar.addActionListener(e -> {
            // TODO: lógica TXT para crear o actualizar un cliente
        });

        btnEliminar.addActionListener(e -> {
            // TODO: lógica TXT para eliminar un cliente por ID
        });

        btnLimpiar.addActionListener(e -> {
            txtId.setText("");
            txtNombre.setText("");
            txtApellido.setText("");
            txtDni.setText("");
            txtTelefono.setText("");
            txtCorreo.setText("");
        });

        btnHistorial.addActionListener(e -> {
            // TODO: lógica TXT para buscar y mostrar el historial de compras del cliente seleccionado
            String clienteSel = txtNombre.getText() + " " + txtApellido.getText();
            JOptionPane.showMessageDialog(this, "Historial de compras para: " + clienteSel + "\n(Cargado de ventas.txt)");
        });

        tblClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblClientes.getSelectedRow() != -1) {
                int row = tblClientes.getSelectedRow();
                txtId.setText(modelClientes.getValueAt(row, 0).toString());
                txtNombre.setText(modelClientes.getValueAt(row, 1).toString());
                txtApellido.setText(modelClientes.getValueAt(row, 2).toString());
                txtDni.setText(modelClientes.getValueAt(row, 3).toString());
                txtTelefono.setText(modelClientes.getValueAt(row, 4).toString());
                txtCorreo.setText(modelClientes.getValueAt(row, 5).toString());
            }
        });
    }
}

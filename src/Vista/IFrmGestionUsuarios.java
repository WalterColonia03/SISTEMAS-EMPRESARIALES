package Vista;

import Vista.Estilos.UIKit;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * IFrmGestionUsuarios - Fusión de creación y gestión de usuarios y roles.
 * Rediseñado con UIKit (Patrón A2).
 */
public class IFrmGestionUsuarios extends JInternalFrame {

    private JTable tblUsuarios;
    private DefaultTableModel modelUsuarios;
    private JTextField txtBuscar;

    // Campos formulario
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JTextField txtTelefono;
    private JComboBox<String> cbRol;
    private JComboBox<String> cbEstado;

    private JButton btnBuscar;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    public IFrmGestionUsuarios() {
        super("Gestión de Usuarios y Roles", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        setSize(960, 600);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    private void initComponents() {
        txtBuscar = UIKit.textField();
        txtBuscar.setPreferredSize(new Dimension(200, 36));
        
        btnBuscar = UIKit.secondaryButton("Buscar");

        // Tabla de Usuarios
        String[] columns = {"ID", "Nombre", "Apellido", "Usuario", "Teléfono", "Rol", "Estado"};
        modelUsuarios = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblUsuarios = UIKit.styledTable(modelUsuarios);

        // Campos formulario
        txtId = UIKit.readOnlyField();
        txtNombre = UIKit.textField();
        txtApellido = UIKit.textField();
        txtUsuario = UIKit.textField();
        
        txtPassword = new JPasswordField();
        txtPassword.setFont(UIKit.BODY);
        txtPassword.putClientProperty(FlatClientProperties.STYLE,
                "arc: 8; borderColor: " + String.format("#%02x%02x%02x", UIKit.BORDER.getRed(), UIKit.BORDER.getGreen(), UIKit.BORDER.getBlue()) + 
                "; focusedBorderColor: " + String.format("#%02x%02x%02x", UIKit.ACCENT.getRed(), UIKit.ACCENT.getGreen(), UIKit.ACCENT.getBlue()) + ";");
        txtPassword.setPreferredSize(new Dimension(0, 36));
        
        txtTelefono = UIKit.textField();
        
        cbRol = new JComboBox<>(new String[]{"Administrador", "Vendedor", "Supervisor"});
        cbRol.setFont(UIKit.BODY);
        cbRol.setPreferredSize(new Dimension(0, 36));
        
        cbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});
        cbEstado.setFont(UIKit.BODY);
        cbEstado.setPreferredSize(new Dimension(0, 36));

        // Botones
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
                UIKit.screenHeader("Gestión de Usuarios", "Administración  ›  Usuarios y Roles"),
                BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(UIKit.SPACE_LG, 0));
        cuerpo.setOpaque(false);

        // ── Tarjeta Izquierda: Tabla de Usuarios ──
        JPanel pnlTabla = UIKit.card();
        pnlTabla.setLayout(new BorderLayout(0, UIKit.SPACE_SM));

        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_SM, 0));
        pnlBusqueda.setOpaque(false);
        pnlBusqueda.add(txtBuscar);
        pnlBusqueda.add(btnBuscar);
        
        pnlTabla.add(UIKit.sectionHeader("Usuarios del Sistema", pnlBusqueda), BorderLayout.NORTH);
        
        JScrollPane scroll = new JScrollPane(tblUsuarios);
        scroll.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        pnlTabla.add(scroll, BorderLayout.CENTER);

        cuerpo.add(pnlTabla, BorderLayout.CENTER);

        // ── Tarjeta Derecha: Formulario de Registro ──
        JPanel pnlForm = UIKit.card();
        pnlForm.setPreferredSize(new Dimension(380, 0));
        pnlForm.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);

        gbc.gridx = 0; gbc.gridy = 0;
        pnlForm.add(UIKit.sectionHeader("Detalle del Usuario", null), gbc);

        // ID Usuario (Read-only)
        gbc.gridwidth = 2;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("ID Usuario"), gbc);
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

        // Usuario (col 1) y Contraseña (col 2)
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, UIKit.SPACE_SM);
        pnlForm.add(UIKit.fieldLabel("Usuario"), gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Contraseña"), gbc);

        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, UIKit.SPACE_SM);
        pnlForm.add(txtUsuario, gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(txtPassword, gbc);

        // Teléfono (col 1) y Rol (col 2)
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, UIKit.SPACE_SM);
        pnlForm.add(UIKit.fieldLabel("Teléfono"), gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Rol"), gbc);

        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, UIKit.SPACE_SM);
        pnlForm.add(txtTelefono, gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(cbRol, gbc);

        // Estado (full width)
        gbc.gridwidth = 2;
        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Estado"), gbc);
        
        gbc.gridy = 10;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_LG, 0);
        pnlForm.add(cbEstado, gbc);

        // Botones
        JPanel pnlBotones = new JPanel(new GridLayout(2, 2, UIKit.SPACE_SM, UIKit.SPACE_SM));
        pnlBotones.setOpaque(false);
        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnLimpiar);
        pnlBotones.add(btnEliminar);

        gbc.gridy = 11;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        pnlForm.add(pnlBotones, gbc);

        cuerpo.add(pnlForm, BorderLayout.EAST);
        
        getContentPane().add(cuerpo, BorderLayout.CENTER);
    }

    private void attachEvents() {
        btnBuscar.addActionListener(e -> {
            // TODO: lógica TXT para buscar usuarios en la lista de usuarios.txt
        });

        btnGuardar.addActionListener(e -> {
            // TODO: lógica TXT para guardar un nuevo usuario o actualizar datos del existente en usuarios.txt
        });

        btnEliminar.addActionListener(e -> {
            // TODO: lógica TXT para desactivar o eliminar un usuario de usuarios.txt
        });

        btnLimpiar.addActionListener(e -> {
            txtId.setText("");
            txtNombre.setText("");
            txtApellido.setText("");
            txtUsuario.setText("");
            txtPassword.setText("");
            txtTelefono.setText("");
            cbRol.setSelectedIndex(0);
            cbEstado.setSelectedIndex(0);
        });

        tblUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblUsuarios.getSelectedRow() != -1) {
                int row = tblUsuarios.getSelectedRow();
                txtId.setText(modelUsuarios.getValueAt(row, 0).toString());
                txtNombre.setText(modelUsuarios.getValueAt(row, 1).toString());
                txtApellido.setText(modelUsuarios.getValueAt(row, 2).toString());
                txtUsuario.setText(modelUsuarios.getValueAt(row, 3).toString());
                txtTelefono.setText(modelUsuarios.getValueAt(row, 4).toString());
                cbRol.setSelectedItem(modelUsuarios.getValueAt(row, 5).toString());
                cbEstado.setSelectedItem(modelUsuarios.getValueAt(row, 6).toString());
            }
        });
    }
}

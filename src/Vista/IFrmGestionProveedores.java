package Vista;

import Clases.Proveedor;
import Modelo.ApisPeruService;
import Modelo.ProveedorDAO;
import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
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
        
        // --- Validaciones en Vivo ---
        java.awt.event.KeyAdapter numericoLimitado = new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                JTextField field = (JTextField) e.getSource();
                int maxLength = (field == txtTelefono) ? 15 : 11; // Teléfono hasta 15, RUC exactamente 11
                
                if (!Character.isDigit(c)) {
                    e.consume(); // Bloquea si no es número
                    Toolkit.getDefaultToolkit().beep();
                } else if (field.getText().length() >= maxLength) {
                    e.consume(); // Bloquea si excede el límite
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        };
        
        txtRuc.addKeyListener(numericoLimitado);
        txtBuscarRuc.addKeyListener(numericoLimitado);
        txtTelefono.addKeyListener(numericoLimitado);

        txtCorreo.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                if (e.getKeyChar() == ' ') {
                    e.consume(); // Los correos no llevan espacios
                    Toolkit.getDefaultToolkit().beep();
                } else if (txtCorreo.getText().length() >= 80) {
                    e.consume();
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
        
        java.awt.event.KeyAdapter limite100 = new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                JTextField field = (JTextField) e.getSource();
                if (field.getText().length() >= 100) {
                    e.consume();
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        };
        txtRazonSocial.addKeyListener(limite100);
        txtContacto.addKeyListener(limite100);
        txtDireccion.addKeyListener(limite100);
        
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
                UIKit.screenHeader("Gestión de Proveedores", "Clientes y Proveedores  >  Proveedores"),
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

    private void cargarTabla() {
        ProveedorDAO dao = new ProveedorDAO();
        List<Proveedor> lista = dao.listarTodos();
        modelProveedores.setRowCount(0);
        for (Proveedor p : lista) {
            modelProveedores.addRow(new Object[]{
                p.getIdProveedor(),
                p.getRuc(),
                p.getRazonSocial(),
                p.getContacto(),
                p.getTelefono(),
                p.getCorreo(),
                p.getDireccion(),
                p.getEstado() == 1 ? "Activo" : "Inactivo"
            });
        }
    }

    private void attachEvents() {
        cargarTabla();

        // Integración API SUNAT: Autocompletar al salir del campo RUC
        // Creado/Modificado: 2026-06-25T23:25:00-05:00
        txtRuc.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                String ruc = txtRuc.getText().trim();
                if (ruc.length() == 11 && txtRazonSocial.getText().trim().isEmpty()) {
                    txtRazonSocial.setText("Consultando SUNAT...");
                    SwingUtilities.invokeLater(() -> {
                        String[] datos = ApisPeruService.consultarRUC(ruc);
                        if (datos != null) {
                            txtRazonSocial.setText(datos[0]); // razonSocial
                            txtDireccion.setText(datos[1]); // direccion
                            if ("ACTIVO".equalsIgnoreCase(datos[2])) {
                                cbEstado.setSelectedIndex(0);
                            } else {
                                cbEstado.setSelectedIndex(1);
                            }
                        } else {
                            txtRazonSocial.setText("");
                            JOptionPane.showMessageDialog(IFrmGestionProveedores.this, "RUC no encontrado en SUNAT o error de conexión.");
                        }
                    });
                }
            }
        });

        btnBuscar.addActionListener(e -> {
            String ruc = txtBuscarRuc.getText().trim();
            if (ruc.isEmpty()) {
                cargarTabla();
                return;
            }
            ProveedorDAO dao = new ProveedorDAO();
            Proveedor p = dao.buscarPorRuc(ruc);
            modelProveedores.setRowCount(0);
            if (p != null) {
                modelProveedores.addRow(new Object[]{
                    p.getIdProveedor(), p.getRuc(), p.getRazonSocial(), p.getContacto(),
                    p.getTelefono(), p.getCorreo(), p.getDireccion(), p.getEstado() == 1 ? "Activo" : "Inactivo"
                });
            } else {
                JOptionPane.showMessageDialog(this, "Proveedor no encontrado");
            }
        });

        btnGuardar.addActionListener(e -> {
            String ruc = txtRuc.getText().trim();
            String razon = txtRazonSocial.getText().trim();
            if (ruc.isEmpty() || razon.isEmpty()) {
                JOptionPane.showMessageDialog(this, "RUC y Razón Social son obligatorios");
                return;
            }
            if (!ruc.matches("^(10|20)\\d{9}$")) {
                JOptionPane.showMessageDialog(this, "RUC inválido (Debe tener exactamente 11 dígitos numéricos y empezar con 10 o 20)");
                return;
            }
            
            String telefono = txtTelefono.getText().trim();
            if (!telefono.isEmpty() && !telefono.matches("^[0-9]{7,15}$")) {
                JOptionPane.showMessageDialog(this, "Teléfono inválido. Solo se permiten números (entre 7 y 15 dígitos).");
                return;
            }
            
            Proveedor p = new Proveedor();
            p.setRuc(ruc);
            p.setRazonSocial(razon);
            p.setContacto(txtContacto.getText().trim());
            p.setTelefono(txtTelefono.getText().trim());
            p.setCorreo(txtCorreo.getText().trim());
            p.setDireccion(txtDireccion.getText().trim());
            p.setEstado(cbEstado.getSelectedIndex() == 0 ? 1 : 0);

            ProveedorDAO dao = new ProveedorDAO();
            if (txtId.getText().isEmpty()) {
                if (dao.guardar(p)) {
                    JOptionPane.showMessageDialog(this, "Proveedor registrado exitosamente");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al registrar (El RUC podría estar duplicado)");
                }
            } else {
                p.setIdProveedor(Integer.parseInt(txtId.getText()));
                if (dao.actualizar(p)) {
                    JOptionPane.showMessageDialog(this, "Proveedor actualizado exitosamente");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar");
                }
            }
            btnLimpiar.doClick();
            cargarTabla();
        });

        btnEliminar.addActionListener(e -> {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione un proveedor para eliminar");
                return;
            }
            int opt = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este proveedor?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                ProveedorDAO dao = new ProveedorDAO();
                if (dao.eliminar(Integer.parseInt(txtId.getText()))) {
                    JOptionPane.showMessageDialog(this, "Proveedor eliminado");
                    btnLimpiar.doClick();
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar");
                }
            }
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

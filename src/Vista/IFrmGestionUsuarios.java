package Vista;

import Clases.Sesion;
import Clases.Usuario;
import Modelo.UsuarioDAO;
import Utils.PasswordUtils;
import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Gestión de Usuarios y Roles del ERP LAREDO.
 * Capa: Vista — Implementa: FR-002 (RBAC), RNF-03 (hash contraseñas), RNF-06 (bitácora).
 * Rediseñado con UIKit v3.0: paleta indigo/dark-navy, badges de rol/estado, acciones por fila.
 */
public class IFrmGestionUsuarios extends JInternalFrame {

    // Tabla
    private JTable tblUsuarios;
    private DefaultTableModel modelUsuarios;
    private JTextField txtBuscar;
    private JComboBox<String> cbFiltroRol;
    private JComboBox<String> cbFiltroEstado;

    // Formulario lateral
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JTextField txtTelefono;
    private JComboBox<String> cbRol;
    private JComboBox<String> cbEstado;

    private JLabel lblErrorNombre, lblErrorUsuario, lblErrorPassword, lblErrorTelefono;

    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    /**
     * Constructor: inicializa y carga datos desde la BD.
     * Solo accesible para usuarios con rol Administrador (RBAC RNF-02).
     */
    public IFrmGestionUsuarios() {
        super("Gestión de Usuarios", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        cargarTabla(null, null);
        setSize(1050, 650);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    /** Crea label de error rojo para validación en vivo. */
    private JLabel errorLabel() {
        JLabel l = new JLabel(" ");
        l.setFont(UIKit.CAPTION);
        l.setForeground(UIKit.DANGER);
        return l;
    }

    private void initComponents() {
        txtBuscar = UIKit.searchField("Buscar por nombre o usuario...");
        cbFiltroRol   = UIKit.filterCombo(new String[]{"Todos los roles","Administrador","Vendedor","Almacenero","Gerente"});
        cbFiltroEstado= UIKit.filterCombo(new String[]{"Todos los estados","Activo","Inactivo"});

        // Tabla: columnas como referencia web
        String[] cols = {"ID","Nombre","Email/Usuario","Rol","Estado","Acciones"};
        modelUsuarios = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblUsuarios = UIKit.styledTable(modelUsuarios);
        tblUsuarios.getColumnModel().getColumn(0).setMaxWidth(50);
        tblUsuarios.getColumnModel().getColumn(5).setMinWidth(90);
        tblUsuarios.getColumnModel().getColumn(5).setMaxWidth(90);

        // Renderer de badges para columna Rol (col 3) y Estado (col 4)
        tblUsuarios.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                JLabel badge = UIKit.rolBadge(v != null ? v.toString() : "");
                badge.setBorder(new EmptyBorder(4, 0, 4, 0));
                return badge;
            }
        });
        tblUsuarios.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                JLabel badge = UIKit.statusBadge(v != null ? v.toString() : "");
                badge.setBorder(new EmptyBorder(4, 0, 4, 0));
                return badge;
            }
        });

        // Formulario lateral
        txtId       = UIKit.readOnlyField();
        txtNombre   = UIKit.textField(); UIKit.addTextValidator(txtNombre, 80);
        txtApellido = UIKit.textField(); UIKit.addTextValidator(txtApellido, 80);
        txtUsuario  = UIKit.textField(); UIKit.addTextValidator(txtUsuario, 50);
        txtTelefono = UIKit.textField(); UIKit.addIntegerValidator(txtTelefono, 9);

        txtPassword = new JPasswordField();
        txtPassword.setFont(UIKit.BODY);
        txtPassword.putClientProperty(com.formdev.flatlaf.FlatClientProperties.STYLE,
            "arc:8; borderColor:" + UIKit.hex(UIKit.BORDER) +
            "; focusedBorderColor:" + UIKit.hex(UIKit.PRIMARY) + ";");
        txtPassword.setPreferredSize(new Dimension(0, 38));

        cbRol    = new JComboBox<>(new String[]{"Administrador","Vendedor","Almacenero","Gerente"});
        cbRol.setFont(UIKit.BODY); cbRol.setPreferredSize(new Dimension(0,38));
        cbEstado = new JComboBox<>(new String[]{"Activo","Inactivo"});
        cbEstado.setFont(UIKit.BODY); cbEstado.setPreferredSize(new Dimension(0,38));

        lblErrorNombre   = errorLabel();
        lblErrorUsuario  = errorLabel();
        lblErrorPassword = errorLabel();
        lblErrorTelefono = errorLabel();

        btnNuevo   = UIKit.newButton("Nuevo Usuario");
        btnGuardar = UIKit.primaryButton("💾 Guardar");
        btnEliminar= UIKit.dangerOutlineButton("🗑 Eliminar");
        btnLimpiar = UIKit.secondaryButton("✕ Cancelar");
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        // Encabezado con botón "+ Nuevo Usuario"
        getContentPane().add(UIKit.screenHeader("Usuarios", "Inicio / Usuarios", btnNuevo), BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(UIKit.SPACE_LG, 0));
        cuerpo.setOpaque(false);

        // ── Tabla ──
        JPanel pnlTabla = UIKit.card();
        pnlTabla.setLayout(new BorderLayout(0, UIKit.SPACE_SM));

        // Barra de filtros: búsqueda + rol + estado (como referencia web)
        JPanel pnlFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_SM, 0));
        pnlFiltros.setOpaque(false);
        pnlFiltros.add(txtBuscar);
        pnlFiltros.add(cbFiltroRol);
        pnlFiltros.add(cbFiltroEstado);
        pnlTabla.add(pnlFiltros, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(tblUsuarios);
        scroll.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        pnlTabla.add(scroll, BorderLayout.CENTER);
        cuerpo.add(pnlTabla, BorderLayout.CENTER);

        // ── Formulario lateral ──
        JPanel pnlForm = UIKit.card();
        pnlForm.setPreferredSize(new Dimension(370, 0));
        pnlForm.setLayout(new GridBagLayout());

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1;
        g.gridwidth = 2; g.gridx = 0;

        g.gridy = 0; g.insets = ins(0,0,UIKit.SPACE_MD,0);
        pnlForm.add(UIKit.sectionHeader("Detalle del Usuario", null), g);

        // ID (read-only)
        g.gridy=1; g.insets=ins(0,0,2,0); pnlForm.add(UIKit.fieldLabel("ID"), g);
        g.gridy=2; g.insets=ins(0,0,UIKit.SPACE_MD,0); pnlForm.add(txtId, g);

        // Nombre y Apellido en dos columnas
        g.gridwidth=1;
        g.gridy=3; g.gridx=0; g.insets=ins(0,0,2,UIKit.SPACE_SM); pnlForm.add(UIKit.fieldLabel("Nombre *"), g);
        g.gridx=1; g.insets=ins(0,0,2,0); pnlForm.add(UIKit.fieldLabel("Apellido"), g);
        g.gridy=4; g.gridx=0; g.insets=ins(0,0,2,UIKit.SPACE_SM); pnlForm.add(txtNombre, g);
        g.gridx=1; g.insets=ins(0,0,2,0); pnlForm.add(txtApellido, g);
        g.gridy=5; g.gridx=0; g.insets=ins(0,0,UIKit.SPACE_SM,UIKit.SPACE_SM); pnlForm.add(lblErrorNombre, g);
        g.gridx=1; g.insets=ins(0,0,UIKit.SPACE_SM,0); pnlForm.add(new JLabel(" "), g);

        // Usuario y Teléfono
        g.gridy=6; g.gridx=0; g.insets=ins(0,0,2,UIKit.SPACE_SM); pnlForm.add(UIKit.fieldLabel("Usuario *"), g);
        g.gridx=1; g.insets=ins(0,0,2,0); pnlForm.add(UIKit.fieldLabel("Teléfono"), g);
        g.gridy=7; g.gridx=0; g.insets=ins(0,0,2,UIKit.SPACE_SM); pnlForm.add(txtUsuario, g);
        g.gridx=1; g.insets=ins(0,0,2,0); pnlForm.add(txtTelefono, g);
        g.gridy=8; g.gridx=0; g.insets=ins(0,0,UIKit.SPACE_SM,UIKit.SPACE_SM); pnlForm.add(lblErrorUsuario, g);
        g.gridx=1; g.insets=ins(0,0,UIKit.SPACE_SM,0); pnlForm.add(lblErrorTelefono, g);

        // Contraseña (full width)
        g.gridwidth=2; g.gridx=0;
        g.gridy=9; g.insets=ins(0,0,2,0); pnlForm.add(UIKit.fieldLabel("Contraseña (vacío = no cambiar)"), g);
        g.gridy=10; g.insets=ins(0,0,2,0); pnlForm.add(txtPassword, g);
        g.gridy=11; g.insets=ins(0,0,UIKit.SPACE_SM,0); pnlForm.add(lblErrorPassword, g);

        // Rol y Estado
        g.gridwidth=1;
        g.gridy=12; g.gridx=0; g.insets=ins(0,0,2,UIKit.SPACE_SM); pnlForm.add(UIKit.fieldLabel("Rol"), g);
        g.gridx=1; g.insets=ins(0,0,2,0); pnlForm.add(UIKit.fieldLabel("Estado"), g);
        g.gridy=13; g.gridx=0; g.insets=ins(0,0,UIKit.SPACE_LG,UIKit.SPACE_SM); pnlForm.add(cbRol, g);
        g.gridx=1; g.insets=ins(0,0,UIKit.SPACE_LG,0); pnlForm.add(cbEstado, g);

        // Botones
        g.gridwidth=2; g.gridx=0; g.gridy=14; g.weighty=1; g.anchor=GridBagConstraints.NORTH;
        g.insets=ins(0,0,0,0);
        JPanel pnlBotones = new JPanel(new GridLayout(1,3, UIKit.SPACE_SM,0));
        pnlBotones.setOpaque(false);
        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnLimpiar);
        pnlBotones.add(btnEliminar);
        pnlForm.add(pnlBotones, g);

        cuerpo.add(pnlForm, BorderLayout.EAST);
        getContentPane().add(cuerpo, BorderLayout.CENTER);
    }

    private Insets ins(int t, int l, int b, int r) { return new Insets(t,l,b,r); }

    /**
     * Carga la tabla desde la BD aplicando filtros de rol y estado (FR-002).
     * @param filtroRol   rol a filtrar o null para todos
     * @param filtroEstado estado a filtrar o null para todos
     */
    private void cargarTabla(String filtroRol, String filtroEstado) {
        modelUsuarios.setRowCount(0);
        String buscar = txtBuscar != null ? txtBuscar.getText().trim().toLowerCase() : "";
        UsuarioDAO dao = new UsuarioDAO();
        for (Usuario u : dao.listarTodos()) {
            // Filtro por texto (nombre o usuario)
            boolean matchText = buscar.isEmpty()
                || u.getNombre().toLowerCase().contains(buscar)
                || u.getUsuario().toLowerCase().contains(buscar);
            // Filtro por rol
            boolean matchRol = filtroRol == null || filtroRol.equals("Todos los roles")
                || u.getRol().equalsIgnoreCase(filtroRol);
            // Filtro por estado
            String estadoStr = u.getEstado() == 1 ? "Activo" : "Inactivo";
            boolean matchEst = filtroEstado == null || filtroEstado.equals("Todos los estados")
                || estadoStr.equals(filtroEstado);

            if (matchText && matchRol && matchEst) {
                modelUsuarios.addRow(new Object[]{
                    u.getIdUsuario(),
                    u.getNombre() + " " + (u.getApellido()!=null?u.getApellido():""),
                    u.getUsuario(),
                    u.getRol(),
                    estadoStr,
                    "✏  🗑"
                });
            }
        }
    }

    private void attachEvents() {
        // Filtros en tiempo real
        cbFiltroRol.addActionListener(e -> aplicarFiltros());
        cbFiltroEstado.addActionListener(e -> aplicarFiltros());
        // Búsqueda live vía KeyAdapter (líneas siguientes)
        // KeyListener para búsqueda live
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyReleased(java.awt.event.KeyEvent e) { aplicarFiltros(); }
        });

        // Botón Nuevo: limpia formulario para inserción
        btnNuevo.addActionListener(e -> limpiarFormulario());

        // Selección de fila → cargar formulario
        tblUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblUsuarios.getSelectedRow() != -1) {
                int row = tblUsuarios.getSelectedRow();
                txtId.setText(modelUsuarios.getValueAt(row, 0).toString());
                // Nombre completo → split
                String nombreCompleto = modelUsuarios.getValueAt(row, 1).toString();
                String[] partes = nombreCompleto.split(" ", 2);
                txtNombre.setText(partes[0]);
                txtApellido.setText(partes.length > 1 ? partes[1] : "");
                txtUsuario.setText(modelUsuarios.getValueAt(row, 2).toString());
                txtPassword.setText(""); // No mostrar contraseña por seguridad (RNF-03)
                cbRol.setSelectedItem(modelUsuarios.getValueAt(row, 3).toString());
                cbEstado.setSelectedItem(modelUsuarios.getValueAt(row, 4).toString());
                // Cargar teléfono desde BD
                UsuarioDAO dao = new UsuarioDAO();
                int id = Integer.parseInt(txtId.getText());
                dao.listarTodos().stream().filter(u -> u.getIdUsuario() == id).findFirst()
                    .ifPresent(u -> txtTelefono.setText(u.getTelefono() != null ? u.getTelefono() : ""));
            }
        });

        // Guardar / Actualizar con validaciones completas
        btnGuardar.addActionListener(e -> guardarUsuario());

        // Eliminar (soft delete → estado = Inactivo)
        btnEliminar.addActionListener(e -> {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            // Proteger al Administrador actual (RNF-02)
            if (txtUsuario.getText().equals(Sesion.getUsuario())) {
                JOptionPane.showMessageDialog(this, "No puede desactivar su propia cuenta.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int opt = JOptionPane.showConfirmDialog(this,
                "¿Desactivar al usuario '" + txtUsuario.getText() + "'?\nSu historial se conservará por auditoría.",
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (opt == JOptionPane.YES_OPTION) {
                UsuarioDAO dao = new UsuarioDAO();
                int id = Integer.parseInt(txtId.getText());
                dao.listarTodos().stream().filter(u -> u.getIdUsuario() == id).findFirst().ifPresent(u -> {
                    u.setEstado(0);
                    if (dao.actualizar(u)) {
                        // Bitácora: registro de cambio de estado (RNF-06)
                        Utils.BitacoraService.registrar(Sesion.getUsuario(),
                            Utils.BitacoraService.MOD_USUARIOS, "DESACTIVAR_USUARIO",
                            Utils.BitacoraService.OK, "Usuario desactivado: " + u.getUsuario());
                        JOptionPane.showMessageDialog(this, "Usuario desactivado correctamente.");
                        limpiarFormulario();
                        aplicarFiltros();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al desactivar.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        });

        btnLimpiar.addActionListener(e -> limpiarFormulario());
    }

    /** Aplica los filtros activos y recarga la tabla. */
    private void aplicarFiltros() {
        String rol    = cbFiltroRol.getSelectedItem().toString();
        String estado = cbFiltroEstado.getSelectedItem().toString();
        cargarTabla(rol, estado);
    }

    /**
     * Valida y guarda/actualiza un usuario en la BD.
     * Implementa: RNF-03 (hash SHA-256), RNF-06 (bitácora), FR-002 (RBAC).
     */
    private void guardarUsuario() {
        // Validaciones
        boolean ok = true;
        if (txtNombre.getText().trim().length() < 2) {
            lblErrorNombre.setText("Mínimo 2 caracteres"); ok = false;
        } else { lblErrorNombre.setText(" "); }

        String usr = txtUsuario.getText().trim();
        if (usr.isEmpty()) {
            lblErrorUsuario.setText("El usuario es obligatorio"); ok = false;
        } else { lblErrorUsuario.setText(" "); }

        String pass = new String(txtPassword.getPassword()).trim();
        boolean esNuevo = txtId.getText().isEmpty();
        if (esNuevo && pass.length() < 6) {
            lblErrorPassword.setText("Mínimo 6 caracteres"); ok = false;
        } else { lblErrorPassword.setText(" "); }

        String tel = txtTelefono.getText().trim();
        if (!tel.isEmpty() && !tel.matches("\\d{9}")) {
            lblErrorTelefono.setText("9 dígitos numéricos"); ok = false;
        } else { lblErrorTelefono.setText(" "); }

        if (!ok) return;

        UsuarioDAO dao = new UsuarioDAO();

        // Verificar unicidad de nombre de usuario
        if (esNuevo) {
            boolean existe = dao.listarTodos().stream()
                .anyMatch(u -> u.getUsuario().equalsIgnoreCase(usr));
            if (existe) {
                lblErrorUsuario.setText("Este usuario ya existe");
                return;
            }
        }

        String hashFinal;
        if (!pass.isEmpty()) {
            hashFinal = PasswordUtils.hashPassword(pass);
        } else if (!esNuevo) {
            // Conservar contraseña existente
            int idExist = Integer.parseInt(txtId.getText());
            hashFinal = dao.listarTodos().stream()
                .filter(x -> x.getIdUsuario() == idExist)
                .map(x -> x.getPassword()).findFirst().orElse("");
        } else {
            hashFinal = "";
        }

        String nombreVal = txtNombre.getText().trim();
        String apellidoVal = txtApellido.getText().trim();
        String rolVal = cbRol.getSelectedItem().toString();
        int estadoVal = cbEstado.getSelectedIndex() == 0 ? 1 : 0;

        boolean resultado;
        String accion;
        if (esNuevo) {
            int nuevoId = dao.generarId();
            Usuario u2 = new Usuario(nuevoId, nombreVal, apellidoVal, usr, hashFinal, tel, rolVal, estadoVal);
            resultado = dao.guardar(u2);
            accion = "CREAR_USUARIO";
        } else {
            int idEdit = Integer.parseInt(txtId.getText());
            Usuario u2 = new Usuario(idEdit, nombreVal, apellidoVal, usr, hashFinal, tel, rolVal, estadoVal);
            resultado = dao.actualizar(u2);
            accion = "EDITAR_USUARIO";
        }


        if (resultado) {
            // Bitácora (RNF-06)
            Utils.BitacoraService.registrar(Sesion.getUsuario(),
                Utils.BitacoraService.MOD_USUARIOS, accion,
                Utils.BitacoraService.OK, "Usuario: " + usr + " | Rol: " + cbRol.getSelectedItem());
            JOptionPane.showMessageDialog(this,
                esNuevo ? "✅ Usuario creado correctamente." : "✅ Usuario actualizado correctamente.");
            limpiarFormulario();
            aplicarFiltros();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al guardar. Revise los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Limpia el formulario para una nueva entrada. */
    private void limpiarFormulario() {
        txtId.setText(""); txtNombre.setText(""); txtApellido.setText("");
        txtUsuario.setText(""); txtPassword.setText(""); txtTelefono.setText("");
        cbRol.setSelectedIndex(0); cbEstado.setSelectedIndex(0);
        lblErrorNombre.setText(" "); lblErrorUsuario.setText(" ");
        lblErrorPassword.setText(" "); lblErrorTelefono.setText(" ");
        tblUsuarios.clearSelection();
    }
}

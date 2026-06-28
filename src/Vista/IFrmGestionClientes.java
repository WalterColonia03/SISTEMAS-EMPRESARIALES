package Vista;

import Clases.Cliente;
import Modelo.ClienteDAO;
import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * IFrmGestionClientes â€” CRUD completo de clientes con historial de compras (FR-016),
 * productos más comprados (FR-021) y ranking VIP (FR-032).
 *
 * Historias de Usuario implementadas:
 *   - 1000007 / 1000012  Registrar y gestionar clientes
 *   - FR-016             Historial de compras por cliente
 *   - FR-021             Top 5 productos más comprados por cliente
 *   - FR-032             Ranking VIP de clientes
 *
 * Rediseñado con UIKit (INSTRUCCIONES_IA Â§2.B).
 * Creado: 2026-06-26 | Actualizado: 2026-06-26
 */
public class IFrmGestionClientes extends JInternalFrame {

    private final ClienteDAO dao = new ClienteDAO();

    private JTable            tblClientes;
    private DefaultTableModel modelClientes;
    private JTextField        txtBuscar;

    // Campos del Formulario
    private JTextField txtId, txtNombre, txtApellido, txtDni, txtTelefono, txtCorreo;

    private JButton btnBuscar, btnGuardar, btnEliminar, btnLimpiar;
    private JButton btnHistorial, btnTopProductos, btnRankingVip;

    public IFrmGestionClientes() {
        super("Gestión de Clientes", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        cargarTabla(dao.listarTodos());
        setSize(1000, 620);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    // â”€â”€ Init â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private void initComponents() {
        txtBuscar = UIKit.textField();
        txtBuscar.setPreferredSize(new Dimension(220, 36));
        txtBuscar.putClientProperty("JTextField.placeholderText", "Buscar por nombre, apellido o DNI...");

        String[] columns = {"ID", "Nombre", "Apellido", "DNI", "Teléfono", "Correo/Dir.", "Puntos"};
        modelClientes = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblClientes = UIKit.styledTable(modelClientes);

        txtId       = UIKit.readOnlyField();
        txtNombre   = UIKit.textField();
        txtApellido = UIKit.textField(); UIKit.addTextValidator(txtApellido, 100);
        txtDni      = UIKit.textField();
        txtTelefono = UIKit.textField(); UIKit.addIntegerValidator(txtTelefono, 15);
        txtCorreo   = UIKit.textField();

        btnBuscar       = UIKit.secondaryButton("Buscar");
        btnGuardar      = UIKit.primaryButton("Guardar / Actualizar");
        btnLimpiar      = UIKit.secondaryButton("Limpiar / Nuevo");
        btnEliminar     = UIKit.dangerOutlineButton("Eliminar");
        btnHistorial    = UIKit.secondaryButton("Historial Compras");
        btnTopProductos = UIKit.secondaryButton("Top Productos");
        btnRankingVip   = UIKit.secondaryButton("Ranking VIP");
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(
                new EmptyBorder(UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        getContentPane().add(
                UIKit.screenHeader("Gestión de Clientes", "Clientes y Proveedores  >  Clientes"),
                BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(UIKit.SPACE_LG, 0));
        cuerpo.setOpaque(false);

        // â”€â”€ Tabla â”€â”€
        JPanel pnlTabla = UIKit.card();
        pnlTabla.setLayout(new BorderLayout(0, UIKit.SPACE_SM));

        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_SM, 0));
        pnlBusqueda.setOpaque(false);
        pnlBusqueda.add(txtBuscar);
        pnlBusqueda.add(btnBuscar);
        pnlBusqueda.add(btnRankingVip);

        pnlTabla.add(UIKit.sectionHeader("Listado de Clientes", pnlBusqueda), BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(tblClientes);
        scroll.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        pnlTabla.add(scroll, BorderLayout.CENTER);
        cuerpo.add(pnlTabla, BorderLayout.CENTER);

        // â”€â”€ Formulario â”€â”€
        JPanel pnlForm = UIKit.card();
        pnlForm.setPreferredSize(new Dimension(360, 0));
        pnlForm.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);

        gbc.gridx = 0; gbc.gridy = 0;
        pnlForm.add(UIKit.sectionHeader("Detalle del Cliente", null), gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("ID Cliente"), gbc);
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(txtId, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 3; gbc.gridx = 0; gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, UIKit.SPACE_SM);
        pnlForm.add(UIKit.fieldLabel("Nombre"), gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Apellido"), gbc);

        gbc.gridy = 4; gbc.gridx = 0; gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, UIKit.SPACE_SM);
        pnlForm.add(txtNombre, gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(txtApellido, gbc);

        gbc.gridy = 5; gbc.gridx = 0; gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, UIKit.SPACE_SM);
        pnlForm.add(UIKit.fieldLabel("DNI"), gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Teléfono"), gbc);

        gbc.gridy = 6; gbc.gridx = 0; gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, UIKit.SPACE_SM);
        pnlForm.add(txtDni, gbc);
        gbc.gridx = 1; gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(txtTelefono, gbc);

        gbc.gridwidth = 2; gbc.gridy = 7; gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Correo / Dirección"), gbc);
        gbc.gridy = 8; gbc.insets = new Insets(0, 0, UIKit.SPACE_LG, 0);
        pnlForm.add(txtCorreo, gbc);

        JPanel pnlBotones = new JPanel(new GridLayout(3, 2, UIKit.SPACE_SM, UIKit.SPACE_SM));
        pnlBotones.setOpaque(false);
        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnLimpiar);
        pnlBotones.add(btnEliminar);
        pnlBotones.add(btnHistorial);
        pnlBotones.add(btnTopProductos);
        pnlBotones.add(new JLabel()); // spacer

        gbc.gridy = 9; gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        pnlForm.add(pnlBotones, gbc);

        cuerpo.add(pnlForm, BorderLayout.EAST);
        getContentPane().add(cuerpo, BorderLayout.CENTER);
    }

    // â”€â”€ Events â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private void attachEvents() {
        // Búsqueda dinámica (FR-016 / 1000012)
        btnBuscar.addActionListener(e -> {
            String t = txtBuscar.getText().trim();
            if (t.isEmpty()) { cargarTabla(dao.listarTodos()); return; }
            cargarTabla(dao.buscarPorNombre(t));
        });

        // Guardar / Actualizar (1000007 / 1000012)
        btnGuardar.addActionListener(e -> guardarCliente());

        // Eliminar (1000012)
        btnEliminar.addActionListener(e -> eliminarCliente());

        // Limpiar
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        // FR-016: historial de compras del cliente seleccionado
        btnHistorial.addActionListener(e -> mostrarHistorialCompras());

        // FR-021: top 5 productos más comprados
        btnTopProductos.addActionListener(e -> mostrarTopProductos());

        // FR-032: ranking VIP top 10
        btnRankingVip.addActionListener(e -> mostrarRankingVip());

        // Selección en tabla â†’ cargar en formulario
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

    // â”€â”€ Lógica de negocio â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private void guardarCliente() {
        String nombre   = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String dni      = txtDni.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo   = txtCorreo.getText().trim();

        // Validaciones (1000007 CA-2)
        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "âš  Nombre, Apellido y DNI son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (dni.length() != 8 && dni.length() != 11) {
            JOptionPane.showMessageDialog(this, "âš  El DNI debe tener 8 dígitos (persona natural) o RUC 11 dígitos.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean esNuevo = txtId.getText().isEmpty();
        int id = esNuevo ? dao.generarId() : Integer.parseInt(txtId.getText());

        // CA-3: verificar duplicado de DNI
        if (dao.existeDni(dni, esNuevo ? -1 : id)) {
            JOptionPane.showMessageDialog(this, "âš  Ya existe un cliente con DNI: " + dni, "DNI Duplicado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente c = new Cliente(id, nombre, apellido, dni, telefono, correo, 1);
        boolean ok = esNuevo ? dao.guardar(c) : dao.actualizar(c);

        if (ok) {
            JOptionPane.showMessageDialog(this, "âœ… Cliente " + (esNuevo ? "registrado" : "actualizado") + " correctamente.");
            limpiarFormulario();
            cargarTabla(dao.listarTodos());
            Utils.BitacoraService.registrar(Clases.Sesion.getUsuario(), Utils.BitacoraService.MOD_CLIENTES,
                esNuevo ? "REGISTRAR_CLIENTE" : "EDITAR_CLIENTE", Utils.BitacoraService.OK, "DNI=" + dni);
        } else {
            JOptionPane.showMessageDialog(this, "âŒ No se pudo guardar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCliente() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "âš  Seleccione un cliente de la tabla para eliminar.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = Integer.parseInt(txtId.getText());
        int confirmar = JOptionPane.showConfirmDialog(this,
            "Â¿Eliminar al cliente con ID " + id + "?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            if (dao.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "âœ… Cliente eliminado correctamente.");
                limpiarFormulario();
                cargarTabla(dao.listarTodos());
                Utils.BitacoraService.registrar(Clases.Sesion.getUsuario(), Utils.BitacoraService.MOD_CLIENTES,
                    "ELIMINAR_CLIENTE", Utils.BitacoraService.OK, "ID=" + id);
            } else {
                JOptionPane.showMessageDialog(this, "âŒ No se pudo eliminar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /** FR-016 â€” Historial de compras del cliente seleccionado */
    private void mostrarHistorialCompras() {
        String dni = txtDni.getText().trim();
        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "âš  Seleccione un cliente de la tabla primero.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<Object[]> ventas = new Modelo.VentaDAO().ventasPorCliente(dni);
        if (ventas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Este cliente no tiene compras registradas.", "Historial", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String[] cols = {"#Venta", "Total (S/)", "Fecha"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        for (Object[] v : ventas) {
            m.addRow(new Object[]{v[0], "S/ " + ((BigDecimal)v[2]).toPlainString(), v[3]});
        }
        JTable tbl = UIKit.styledTable(m);
        JOptionPane.showMessageDialog(this, new JScrollPane(tbl),
            "Historial de Compras â€” " + txtNombre.getText() + " " + txtApellido.getText(),
            JOptionPane.PLAIN_MESSAGE);
    }

    /** FR-021 â€” Top 5 productos más comprados por el cliente seleccionado */
    private void mostrarTopProductos() {
        String dni = txtDni.getText().trim();
        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "âš  Seleccione un cliente de la tabla primero.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<Object[]> top = dao.topProductosPorCliente(dni, 5);
        if (top.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Este cliente no tiene productos comprados registrados.", "Top Productos", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String[] cols = {"Producto", "Unidades", "Total Gastado (S/)"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        for (Object[] f : top) {
            m.addRow(new Object[]{f[0], f[1], "S/ " + ((BigDecimal)f[2]).toPlainString()});
        }
        JTable tbl = UIKit.styledTable(m);
        JOptionPane.showMessageDialog(this, new JScrollPane(tbl),
            "Top 5 Productos â€” " + txtNombre.getText() + " " + txtApellido.getText(),
            JOptionPane.PLAIN_MESSAGE);
    }

    /** FR-032 â€” Ranking VIP de los top 10 clientes por volumen de compras */
    private void mostrarRankingVip() {
        List<Object[]> ranking = dao.rankingVip(10);
        if (ranking.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Sin datos de ventas para calcular el ranking VIP.", "Ranking VIP", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String[] cols = {"#", "Nombre", "Apellido", "DNI", "Total Compras (S/)", "Puntos"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        int pos = 1;
        for (Object[] f : ranking) {
            m.addRow(new Object[]{pos++, f[0], f[1], f[2], "S/ " + ((BigDecimal)f[3]).toPlainString(), f[4]});
        }
        JTable tbl = UIKit.styledTable(m);
        tbl.setPreferredScrollableViewportSize(new Dimension(700, 250));
        JOptionPane.showMessageDialog(this, new JScrollPane(tbl), "ðŸ† Ranking VIP â€” Top 10 Clientes", JOptionPane.PLAIN_MESSAGE);
    }

    // â”€â”€ Helpers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private void cargarTabla(List<Cliente> lista) {
        modelClientes.setRowCount(0);
        for (Cliente c : lista) {
            modelClientes.addRow(new Object[]{
                c.getIdCliente(), c.getNombre(), c.getApellido(),
                c.getDni(), c.getTelefono(), c.getDireccion(), c.getPuntos()
            });
        }
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtDni.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
    }
}

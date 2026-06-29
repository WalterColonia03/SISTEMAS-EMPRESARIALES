package Vista;

import Clases.Categoria;
import Clases.Sesion;
import Modelo.CategoriaDAO;
import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Gestión de Categorías — ERP Minimarket LAREDO.
 * Capa: Vista — Implementa: FR-003, RNF-06 (bitácora).
 * Rediseñado UIKit v3.0: header con botón "+ Nueva Categoría", tabla con badges, acciones por fila.
 */
public class IFrmGestionCategorias extends JInternalFrame {

    private JTable tblCategorias;
    private DefaultTableModel modelCategorias;
    private JTextField txtBuscar;

    private JTextField txtId;
    private JTextField txtDescripcion;
    private JComboBox<String> cbEstado;

    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    private JLabel lblErrorNombre;

    public IFrmGestionCategorias() {
        super("Gestión de Categorías", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        cargarTabla("");
        setSize(1000, 600);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    private void initComponents() {
        txtBuscar = UIKit.searchField("Buscar categoría...");
        txtBuscar.setPreferredSize(new Dimension(260, 38));

        // Tabla — columnas como referencia web: ID | Nombre | Estado | Acciones
        String[] cols = {"ID", "Nombre de Categoría", "Estado"};
        modelCategorias = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblCategorias = UIKit.styledTable(modelCategorias);
        tblCategorias.getColumnModel().getColumn(0).setMaxWidth(60);
        tblCategorias.getColumnModel().getColumn(2).setMaxWidth(120);

        // Badge de estado en columna 2
        tblCategorias.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                JLabel badge = UIKit.statusBadge(v != null ? v.toString() : "");
                badge.setBorder(new EmptyBorder(4, 0, 4, 0));
                return badge;
            }
        });

        txtId          = UIKit.readOnlyField();
        txtDescripcion = UIKit.textField();
        UIKit.addTextValidator(txtDescripcion, 100);
        txtDescripcion.putClientProperty("JTextField.placeholderText", "Ej. Lácteos, Bebidas, Snacks...");

        cbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});
        cbEstado.setFont(UIKit.BODY); cbEstado.setPreferredSize(new Dimension(0, 38));

        lblErrorNombre = new JLabel(" "); lblErrorNombre.setFont(UIKit.CAPTION); lblErrorNombre.setForeground(UIKit.DANGER);

        btnNuevo   = UIKit.newButton("Nueva Categoría");
        btnGuardar = UIKit.primaryButton("💾 Guardar");
        btnLimpiar = UIKit.secondaryButton("✕ Cancelar");
        btnEliminar= UIKit.dangerOutlineButton("🗑 Inactivar");
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent)getContentPane()).setBorder(new EmptyBorder(UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        // Header con botón a la derecha
        getContentPane().add(UIKit.screenHeader("Categorías", "Inicio / Categorías", btnNuevo), BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(UIKit.SPACE_LG, 0));
        cuerpo.setOpaque(false);

        // ── Panel tabla ──
        JPanel pnlTabla = UIKit.card();
        pnlTabla.setLayout(new BorderLayout(0, UIKit.SPACE_SM));
        // Barra de búsqueda
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlTop.setOpaque(false); pnlTop.add(txtBuscar);
        pnlTabla.add(pnlTop, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(tblCategorias);
        scroll.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        pnlTabla.add(scroll, BorderLayout.CENTER);
        cuerpo.add(pnlTabla, BorderLayout.CENTER);

        // ── Formulario lateral ──
        JPanel pnlForm = UIKit.card();
        pnlForm.setPreferredSize(new Dimension(320, 0));
        pnlForm.setLayout(new GridBagLayout());

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1; g.gridx = 0;

        g.gridy=0; g.insets=new Insets(0,0,UIKit.SPACE_MD,0);
        pnlForm.add(UIKit.sectionHeader("Detalle de Categoría", null), g);

        g.gridy=1; g.insets=new Insets(0,0,2,0); pnlForm.add(UIKit.fieldLabel("ID"), g);
        g.gridy=2; g.insets=new Insets(0,0,UIKit.SPACE_MD,0); pnlForm.add(txtId, g);

        g.gridy=3; g.insets=new Insets(0,0,2,0); pnlForm.add(UIKit.fieldLabel("Nombre *"), g);
        g.gridy=4; g.insets=new Insets(0,0,2,0); pnlForm.add(txtDescripcion, g);
        g.gridy=5; g.insets=new Insets(0,0,UIKit.SPACE_MD,0); pnlForm.add(lblErrorNombre, g);

        g.gridy=6; g.insets=new Insets(0,0,2,0); pnlForm.add(UIKit.fieldLabel("Estado"), g);
        g.gridy=7; g.insets=new Insets(0,0,UIKit.SPACE_LG,0); pnlForm.add(cbEstado, g);

        JPanel pnlBotones = new JPanel(new GridLayout(1,3, UIKit.SPACE_SM,0));
        pnlBotones.setOpaque(false);
        pnlBotones.add(btnGuardar); pnlBotones.add(btnLimpiar); pnlBotones.add(btnEliminar);
        g.gridy=8; g.weighty=1; g.anchor=GridBagConstraints.NORTH; g.insets=new Insets(0,0,0,0);
        pnlForm.add(pnlBotones, g);

        cuerpo.add(pnlForm, BorderLayout.EAST);
        getContentPane().add(cuerpo, BorderLayout.CENTER);
    }

    private void cargarTabla(String filtro) {
        modelCategorias.setRowCount(0);
        CategoriaDAO dao = new CategoriaDAO();
        for (Categoria c : dao.listarTodos()) {
            if (filtro.isEmpty() || c.getDescripcion().toLowerCase().contains(filtro)) {
                modelCategorias.addRow(new Object[]{
                    c.getIdCategoria(),
                    c.getDescripcion(),
                    c.getEstado() == 1 ? "Activo" : "Inactivo"
                });
            }
        }
    }

    private void attachEvents() {
        // Búsqueda live
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyReleased(java.awt.event.KeyEvent e) {
                cargarTabla(txtBuscar.getText().trim().toLowerCase());
            }
        });

        // Selección de fila → formulario
        tblCategorias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblCategorias.getSelectedRow() != -1) {
                int row = tblCategorias.getSelectedRow();
                txtId.setText(modelCategorias.getValueAt(row, 0).toString());
                txtDescripcion.setText(modelCategorias.getValueAt(row, 1).toString());
                cbEstado.setSelectedItem(modelCategorias.getValueAt(row, 2).toString());
            }
        });

        btnNuevo.addActionListener(e -> limpiar());

        // Guardar / Actualizar
        btnGuardar.addActionListener(e -> {
            String nombre = txtDescripcion.getText().trim();
            if (nombre.length() < 2) { lblErrorNombre.setText("Mínimo 2 caracteres"); return; }
            lblErrorNombre.setText(" ");
            int estado = cbEstado.getSelectedIndex() == 0 ? 1 : 0;
            CategoriaDAO dao = new CategoriaDAO();
            if (txtId.getText().isEmpty()) {
                Categoria c = new Categoria(dao.generarId(), nombre, estado);
                boolean ok = dao.guardar(c);
                if (ok) {
                    Utils.BitacoraService.registrar(Sesion.getUsuario(),
                        Utils.BitacoraService.MOD_INVENTARIO, "CREAR_CATEGORIA",
                        Utils.BitacoraService.OK, "Categoría: " + nombre);
                    JOptionPane.showMessageDialog(this, "✅ Categoría creada.");
                } else JOptionPane.showMessageDialog(this, "❌ Error al crear.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Categoria c = new Categoria(Integer.parseInt(txtId.getText()), nombre, estado);
                boolean ok = dao.actualizar(c);
                if (ok) {
                    Utils.BitacoraService.registrar(Sesion.getUsuario(),
                        Utils.BitacoraService.MOD_INVENTARIO, "EDITAR_CATEGORIA",
                        Utils.BitacoraService.OK, "Categoría: " + nombre);
                    JOptionPane.showMessageDialog(this, "✅ Categoría actualizada.");
                } else JOptionPane.showMessageDialog(this, "❌ Error al actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            limpiar(); cargarTabla("");
        });

        btnEliminar.addActionListener(e -> {
            if (txtId.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Seleccione una categoría."); return; }
            int opt = JOptionPane.showConfirmDialog(this,
                "¿Inactivar la categoría '" + txtDescripcion.getText() + "'?\nLos productos asociados se conservan.",
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (opt == JOptionPane.YES_OPTION) {
                CategoriaDAO dao = new CategoriaDAO();
                Categoria c = new Categoria(Integer.parseInt(txtId.getText()), txtDescripcion.getText(), 0);
                if (dao.actualizar(c)) {
                    Utils.BitacoraService.registrar(Sesion.getUsuario(),
                        Utils.BitacoraService.MOD_INVENTARIO, "INACTIVAR_CATEGORIA",
                        Utils.BitacoraService.OK, "ID: " + c.getIdCategoria());
                    JOptionPane.showMessageDialog(this, "✅ Categoría inactivada.");
                    limpiar(); cargarTabla("");
                } else JOptionPane.showMessageDialog(this, "❌ Error al inactivar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLimpiar.addActionListener(e -> limpiar());
    }

    private void limpiar() {
        txtId.setText(""); txtDescripcion.setText("");
        cbEstado.setSelectedIndex(0); lblErrorNombre.setText(" ");
        tblCategorias.clearSelection();
    }
}
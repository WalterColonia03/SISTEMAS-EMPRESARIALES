package Vista;

import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class IFrmGestionCategorias extends JInternalFrame {

    private JTable tblCategorias;
    private DefaultTableModel modelCategorias;
    private JTextField txtBuscar;

    private JTextField txtId;
    private JTextField txtDescripcion;
    private JComboBox<String> cbEstado;

    private JButton btnBuscar;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    public IFrmGestionCategorias() {
        super("Gestión de Categorías", true, true, true, true);
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

        String[] columns = {"ID", "Descripción", "Estado"};
        modelCategorias = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblCategorias = UIKit.styledTable(modelCategorias);

        txtId = UIKit.readOnlyField();
        txtDescripcion = UIKit.textField();
        
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
                UIKit.screenHeader("Gestión de Categorías", "Inventario  ›  Categorías"),
                BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(UIKit.SPACE_LG, 0));
        cuerpo.setOpaque(false);

        // ── Tarjeta Izquierda: Tabla ──
        JPanel pnlTabla = UIKit.card();
        pnlTabla.setLayout(new BorderLayout(0, UIKit.SPACE_SM));

        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_SM, 0));
        pnlBusqueda.setOpaque(false);
        pnlBusqueda.add(txtBuscar);
        pnlBusqueda.add(btnBuscar);
        
        pnlTabla.add(UIKit.sectionHeader("Listado de Categorías", pnlBusqueda), BorderLayout.NORTH);
        
        JScrollPane scroll = new JScrollPane(tblCategorias);
        scroll.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        pnlTabla.add(scroll, BorderLayout.CENTER);

        cuerpo.add(pnlTabla, BorderLayout.CENTER);

        // ── Tarjeta Derecha: Formulario ──
        JPanel pnlForm = UIKit.card();
        pnlForm.setPreferredSize(new Dimension(320, 0));
        pnlForm.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(UIKit.sectionHeader("Detalle de Categoría", null), gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("ID Categoría"), gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(txtId, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Descripción"), gbc);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(txtDescripcion, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Estado"), gbc);
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_LG, 0);
        pnlForm.add(cbEstado, gbc);

        JPanel pnlBotones = new JPanel(new GridLayout(3, 1, 0, UIKit.SPACE_SM));
        pnlBotones.setOpaque(false);
        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnLimpiar);
        pnlBotones.add(btnEliminar);

        gbc.gridy = 7;
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
            txtDescripcion.setText("");
            cbEstado.setSelectedIndex(0);
        });

        tblCategorias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblCategorias.getSelectedRow() != -1) {
                int row = tblCategorias.getSelectedRow();
                txtId.setText(modelCategorias.getValueAt(row, 0).toString());
                txtDescripcion.setText(modelCategorias.getValueAt(row, 1).toString());
                String estado = modelCategorias.getValueAt(row, 2).toString();
                cbEstado.setSelectedItem(estado);
            }
        });
    }
}

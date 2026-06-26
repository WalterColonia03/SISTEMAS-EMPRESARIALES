package Vista;

import Clases.Categoria;
import Modelo.CategoriaDAO;
import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class IFrmGestionCategorias extends JInternalFrame {

    private JTable tblCategorias;
    private DefaultTableModel modelCategorias;
    private JTextField txtBuscar;

    private JTextField txtId;
    private JTextField txtDescripcion; // Este es el "Nombre" de la categorÃ­a
    private JComboBox<String> cbEstado;

    private JButton btnBuscar;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    public IFrmGestionCategorias() {
        super("GestiÃ³n de CategorÃ­as", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        cargarTabla();
        setSize(960, 600);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    private void initComponents() {
        txtBuscar = UIKit.textField();
        txtBuscar.setPreferredSize(new Dimension(200, 36));
        
        btnBuscar = UIKit.secondaryButton("Buscar");

        String[] columns = {"ID", "Nombre de CategorÃ­a", "Estado"};
        modelCategorias = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblCategorias = UIKit.styledTable(modelCategorias);

        txtId = UIKit.readOnlyField();
        txtDescripcion = UIKit.textField(); UIKit.addTextValidator(txtDescripcion, 100);
        txtDescripcion.putClientProperty("JTextField.placeholderText", "Ej. Gaseosas, LÃ¡cteos...");
        
        cbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});
        cbEstado.setFont(UIKit.BODY);

        btnGuardar = UIKit.primaryButton("Guardar / Actualizar");
        btnLimpiar = UIKit.secondaryButton("Limpiar / Nuevo");
        btnEliminar = UIKit.dangerOutlineButton("Eliminar (Ocultar)");
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(
                UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        // ===== Encabezado =====
        getContentPane().add(
                UIKit.screenHeader("GestiÃ³n de CategorÃ­as", "Inventario  â€º  CategorÃ­as"),
                BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(UIKit.SPACE_LG, 0));
        cuerpo.setOpaque(false);

        // â”€â”€ Tarjeta Izquierda: Tabla â”€â”€
        JPanel pnlTabla = UIKit.card();
        pnlTabla.setLayout(new BorderLayout(0, UIKit.SPACE_SM));

        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_SM, 0));
        pnlBusqueda.setOpaque(false);
        pnlBusqueda.add(txtBuscar);
        pnlBusqueda.add(btnBuscar);
        
        pnlTabla.add(UIKit.sectionHeader("Listado de CategorÃ­as", pnlBusqueda), BorderLayout.NORTH);
        
        JScrollPane scroll = new JScrollPane(tblCategorias);
        scroll.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        pnlTabla.add(scroll, BorderLayout.CENTER);

        cuerpo.add(pnlTabla, BorderLayout.CENTER);

        // â”€â”€ Tarjeta Derecha: Formulario â”€â”€
        JPanel pnlForm = UIKit.card();
        pnlForm.setPreferredSize(new Dimension(320, 0));
        pnlForm.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(UIKit.sectionHeader("Detalle de CategorÃ­a", null), gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("ID CategorÃ­a"), gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(txtId, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Nombre de CategorÃ­a"), gbc);
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
    
    private void cargarTabla() {
        CategoriaDAO dao = new CategoriaDAO();
        List<Categoria> lista = dao.listarTodos();
        modelCategorias.setRowCount(0);
        for (Categoria c : lista) {
            modelCategorias.addRow(new Object[]{
                c.getIdCategoria(),
                c.getDescripcion(),
                c.getEstado() == 1 ? "Activo" : "Inactivo"
            });
        }
    }

    private void attachEvents() {
        btnBuscar.addActionListener(e -> {
            String term = txtBuscar.getText().trim().toLowerCase();
            if (term.isEmpty()) {
                cargarTabla();
                return;
            }
            CategoriaDAO dao = new CategoriaDAO();
            List<Categoria> lista = dao.listarTodos();
            modelCategorias.setRowCount(0);
            for (Categoria c : lista) {
                if (c.getDescripcion().toLowerCase().contains(term)) {
                    modelCategorias.addRow(new Object[]{
                        c.getIdCategoria(),
                        c.getDescripcion(),
                        c.getEstado() == 1 ? "Activo" : "Inactivo"
                    });
                }
            }
        });

        btnGuardar.addActionListener(e -> {
            String nombre = txtDescripcion.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre de la categorÃ­a es obligatorio.");
                return;
            }
            
            int estado = cbEstado.getSelectedIndex() == 0 ? 1 : 0;
            Categoria c = new Categoria(0, nombre, estado);
            
            CategoriaDAO dao = new CategoriaDAO();
            
            if (txtId.getText().isEmpty()) {
                c.setIdCategoria(dao.generarId());
                if (dao.guardar(c)) {
                    JOptionPane.showMessageDialog(this, "CategorÃ­a guardada con Ã©xito.");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al guardar.");
                }
            } else {
                c.setIdCategoria(Integer.parseInt(txtId.getText()));
                if (dao.actualizar(c)) {
                    JOptionPane.showMessageDialog(this, "CategorÃ­a actualizada con Ã©xito.");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar.");
                }
            }
            btnLimpiar.doClick();
            cargarTabla();
        });

        btnEliminar.addActionListener(e -> {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione una categorÃ­a para eliminar/ocultar.");
                return;
            }
            int opt = JOptionPane.showConfirmDialog(this, 
                "Â¿EstÃ¡ seguro de ocultar/inactivar esta categorÃ­a?\n\nNota: Los productos y el historial de ventas vinculados a esta categorÃ­a NO se eliminarÃ¡n por motivos de auditorÃ­a.", 
                "Confirmar AcciÃ³n", JOptionPane.YES_NO_OPTION);
                
            if (opt == JOptionPane.YES_OPTION) {
                CategoriaDAO dao = new CategoriaDAO();
                Categoria c = new Categoria(Integer.parseInt(txtId.getText()), txtDescripcion.getText(), 0);
                if (dao.actualizar(c)) {
                    JOptionPane.showMessageDialog(this, "CategorÃ­a ocultada/inactivada exitosamente.");
                    btnLimpiar.doClick();
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al inactivar la categorÃ­a.");
                }
            }
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
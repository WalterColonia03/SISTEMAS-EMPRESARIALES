package Vista;

import Clases.Producto;
import Modelo.ProductoDAO;
import Modelo.CategoriaDAO;
import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

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
    private JComboBox<String> cbEstado;

    private JButton btnBuscar;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    public IFrmGestionProductos() {
        super("GestiÃ³n de Productos", true, true, true, true);
        initComponents();
        buildLayout();
        cargarCategorias();
        attachEvents();
        cargarTabla();
        setSize(1000, 620);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    private void initComponents() {
        txtBuscar = UIKit.textField();
        txtBuscar.setPreferredSize(new Dimension(200, 36));
        txtBuscar.putClientProperty("JTextField.placeholderText", "Buscar por nombre...");
        
        cbFiltroCategoria = new JComboBox<>();
        cbFiltroCategoria.setFont(UIKit.BODY);
        cbFiltroCategoria.setPreferredSize(new Dimension(160, 36));

        btnBuscar = UIKit.secondaryButton("Buscar / Filtrar");

        String[] columns = {"ID", "Nombre", "Cant.", "Precio", "Lote", "Vencimiento", "Estado"};
        modelProductos = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblProductos = UIKit.styledTable(modelProductos);
        
        // Pinta de rojo la columna Cant. si es menor a 15
        tblProductos.getColumnModel().getColumn(2).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                try {
                    int cant = Integer.parseInt(value.toString());
                    if (cant < 15) {
                        c.setForeground(Color.WHITE);
                        c.setBackground(new Color(198, 40, 40));
                    } else {
                        c.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                        c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                    }
                } catch(Exception e) {}
                return c;
            }
        });

        txtId = UIKit.readOnlyField();
        txtNombre = UIKit.textField(); UIKit.addTextValidator(txtNombre, 100);
        txtDescripcion = UIKit.textField(); UIKit.addTextValidator(txtDescripcion, 150);
        
        txtCantidad = UIKit.textField(); UIKit.addIntegerValidator(txtCantidad, 8);
        txtCantidad.setHorizontalAlignment(JTextField.RIGHT);
        
        txtPrecio = UIKit.textField(); UIKit.addNumericValidator(txtPrecio, 10);
        txtPrecio.setHorizontalAlignment(JTextField.RIGHT);
        
        cbCategoria = new JComboBox<>();
        cbCategoria.setFont(UIKit.BODY);
        cbCategoria.setPreferredSize(new Dimension(0, 36));
        
        txtLote = UIKit.textField();
        txtVencimiento = UIKit.textField();
        
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
                UIKit.screenHeader("GestiÃ³n de Productos", "Inventario  â€º  Productos"),
                BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(UIKit.SPACE_LG, 0));
        cuerpo.setOpaque(false);

        // â”€â”€ Tarjeta Izquierda: Tabla â”€â”€
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

        // â”€â”€ Tarjeta Derecha: Formulario â”€â”€
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

        // DescripciÃ³n (full width)
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("DescripciÃ³n"), gbc);
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(txtDescripcion, gbc);

        // CategorÃ­a (col 1) y Precio (col 2)
        gbc.gridwidth = 1;
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, UIKit.SPACE_SM);
        pnlForm.add(UIKit.fieldLabel("CategorÃ­a"), gbc);
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

        // Vencimiento (col 1) y Estado (col 2)
        gbc.gridwidth = 1;
        gbc.gridy = 11;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, UIKit.SPACE_SM);
        pnlForm.add(UIKit.fieldLabel("Vencimiento (AAAA-MM-DD)"), gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Estado"), gbc);
        
        gbc.gridy = 12;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_LG, UIKit.SPACE_SM);
        pnlForm.add(txtVencimiento, gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_LG, 0);
        pnlForm.add(cbEstado, gbc);

        // Botones
        JPanel pnlBotones = new JPanel(new GridLayout(2, 2, UIKit.SPACE_SM, UIKit.SPACE_SM));
        pnlBotones.setOpaque(false);
        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnLimpiar);
        pnlBotones.add(btnEliminar);

        gbc.gridy = 13;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0; 
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        pnlForm.add(pnlBotones, gbc);

        cuerpo.add(pnlForm, BorderLayout.EAST);
        
        getContentPane().add(cuerpo, BorderLayout.CENTER);
    }
    
    private void cargarCategorias() {
        cbCategoria.removeAllItems();
        cbFiltroCategoria.removeAllItems();
        cbFiltroCategoria.addItem("Todas las CategorÃ­as");
        
        CategoriaDAO dao = new CategoriaDAO();
        for (Clases.Categoria c : dao.listarTodos()) {
            String item = c.getIdCategoria() + " - " + c.getDescripcion();
            cbCategoria.addItem(item);
            cbFiltroCategoria.addItem(item);
        }
    }

    private void cargarTabla() {
        ProductoDAO dao = new ProductoDAO();
        List<Producto> lista = dao.listarTodos();
        modelProductos.setRowCount(0);
        for (Producto p : lista) {
            modelProductos.addRow(new Object[]{
                p.getIdProducto(),
                p.getNombre(),
                p.getCantidad(),
                "S/ " + p.getPrecio().toPlainString(),
                "-",
                "-",
                p.getEstado() == 1 ? "Activo" : "Inactivo"
            });
        }
    }

    private void attachEvents() {
        btnBuscar.addActionListener(e -> {
            String term = txtBuscar.getText().trim().toLowerCase();
            String cat = cbFiltroCategoria.getSelectedItem().toString();
            ProductoDAO dao = new ProductoDAO();
            List<Producto> lista = dao.listarTodos();
            
            modelProductos.setRowCount(0);
            for (Producto p : lista) {
                boolean matchTerm = term.isEmpty() || p.getNombre().toLowerCase().contains(term) || p.getDescripcion().toLowerCase().contains(term);
                boolean matchCat = cat.equals("Todas las CategorÃ­as");
                
                if (!matchCat) {
                    try {
                        int idCatFiltro = Integer.parseInt(cat.split(" - ")[0]);
                        if (p.getIdCategoria() == idCatFiltro) matchCat = true;
                    } catch (Exception ex) {}
                }
                
                if (matchTerm && matchCat) {
                    modelProductos.addRow(new Object[]{
                        p.getIdProducto(),
                        p.getNombre(),
                        p.getCantidad(),
                        "S/ " + p.getPrecio().toPlainString(),
                        "-",
                        "-",
                        p.getEstado() == 1 ? "Activo" : "Inactivo"
                    });
                }
            }
        });

        btnGuardar.addActionListener(e -> {
            if (txtNombre.getText().trim().isEmpty() || txtPrecio.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y Precio son obligatorios.");
                return;
            }
            if (cbCategoria.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una categorÃ­a (CrÃ©ela en el mÃ³dulo respectivo primero).");
                return;
            }
            try {
                Producto p = new Producto();
                p.setNombre(txtNombre.getText().trim());
                p.setDescripcion(txtDescripcion.getText().trim());
                p.setCantidad(txtCantidad.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtCantidad.getText().trim()));
                p.setPrecio(new BigDecimal(txtPrecio.getText().trim().replace(",", ".")));
                
                String catStr = cbCategoria.getSelectedItem().toString();
                p.setIdCategoria(Integer.parseInt(catStr.split(" - ")[0]));
                p.setEstado(cbEstado.getSelectedIndex() == 0 ? 1 : 0);

                ProductoDAO dao = new ProductoDAO();
                if (txtId.getText().isEmpty()) {
                    p.setIdProducto(dao.generarId());
                    if (dao.guardar(p)) {
                        JOptionPane.showMessageDialog(this, "Producto guardado.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al guardar el producto.");
                    }
                } else {
                    p.setIdProducto(Integer.parseInt(txtId.getText()));
                    if (dao.actualizar(p)) {
                        JOptionPane.showMessageDialog(this, "Producto actualizado.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al actualizar el producto.");
                    }
                }
                btnLimpiar.doClick();
                cargarTabla();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cantidad y Precio deben ser nÃºmeros vÃ¡lidos.");
            }
        });

        btnEliminar.addActionListener(e -> {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla.");
                return;
            }
            int opt = JOptionPane.showConfirmDialog(this, 
                "Â¿EstÃ¡ seguro de ocultar/inactivar este producto?\n\nAl igual que con las categorÃ­as, el historial de ventas no se borrarÃ¡, solo se desactivarÃ¡ para futuras ventas.", 
                "Confirmar AcciÃ³n", JOptionPane.YES_NO_OPTION);
            
            if (opt == JOptionPane.YES_OPTION) {
                ProductoDAO dao = new ProductoDAO();
                int id = Integer.parseInt(txtId.getText());
                Producto p = dao.listarTodos().stream().filter(prod -> prod.getIdProducto() == id).findFirst().orElse(null);
                if (p != null) {
                    p.setEstado(0); // Soft delete
                    if (dao.actualizar(p)) {
                        JOptionPane.showMessageDialog(this, "Producto inactivado.");
                        btnLimpiar.doClick();
                        cargarTabla();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al inactivar.");
                    }
                }
            }
        });

        btnLimpiar.addActionListener(e -> {
            txtId.setText("");
            txtNombre.setText("");
            txtDescripcion.setText("");
            txtCantidad.setText("");
            txtPrecio.setText("");
            if (cbCategoria.getItemCount() > 0) cbCategoria.setSelectedIndex(0);
            txtLote.setText("");
            txtVencimiento.setText("");
            cbEstado.setSelectedIndex(0);
            cargarCategorias(); // Refrescar por si se creÃ³ alguna nueva
        });

        tblProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblProductos.getSelectedRow() != -1) {
                int row = tblProductos.getSelectedRow();
                txtId.setText(modelProductos.getValueAt(row, 0).toString());
                txtNombre.setText(modelProductos.getValueAt(row, 1).toString());
                txtCantidad.setText(modelProductos.getValueAt(row, 2).toString());
                txtPrecio.setText(modelProductos.getValueAt(row, 3).toString().replace("S/ ", "").replace(",", "."));
                
                txtLote.setText(modelProductos.getValueAt(row, 4).toString());
                txtVencimiento.setText(modelProductos.getValueAt(row, 5).toString());
                cbEstado.setSelectedItem(modelProductos.getValueAt(row, 6).toString());
                
                // Setear categorÃ­a en el combobox requiere buscar por ID o buscar coincidencia
                ProductoDAO dao = new ProductoDAO();
                int id = Integer.parseInt(txtId.getText());
                Producto prodReal = dao.listarTodos().stream().filter(p -> p.getIdProducto() == id).findFirst().orElse(null);
                if (prodReal != null) {
                    for (int i = 0; i < cbCategoria.getItemCount(); i++) {
                        if (cbCategoria.getItemAt(i).startsWith(prodReal.getIdCategoria() + " -")) {
                            cbCategoria.setSelectedIndex(i);
                            break;
                        }
                    }
                    txtDescripcion.setText(prodReal.getDescripcion());
                }
            }
        });
    }
}
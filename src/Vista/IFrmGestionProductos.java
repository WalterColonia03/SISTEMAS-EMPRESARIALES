package Vista;

import Clases.Categoria;
import Clases.Producto;
import Modelo.CategoriaDAO;
import Modelo.ProductoDAO;
import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Formulario de Gestión de Productos.
 * Capa: Vista
 * Implementa HU: FR-003, FR-004
 */
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
    
    // Labels para validación (mensajitos rojos)
    private JLabel lblErrorNombre;
    private JLabel lblErrorDescripcion;
    private JLabel lblErrorCantidad;
    private JLabel lblErrorPrecio;
    private JLabel lblErrorLote;
    private JLabel lblErrorVencimiento;

    private JButton btnBuscar;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    private List<Categoria> categoriasCacheadas;

    public IFrmGestionProductos() {
        super("Gestión de Productos", true, true, true, true);
        initComponents();
        buildLayout();
        cargarCategorias();
        attachEvents();
        cargarTabla();
        setSize(1100, 720);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    private JLabel crearLabelError() {
        JLabel lbl = new JLabel(" ");
        lbl.setFont(UIKit.CAPTION);
        lbl.setForeground(UIKit.DANGER);
        return lbl;
    }

    private void initComponents() {
        txtBuscar = UIKit.searchField("Buscar por nombre o descripción...");
        txtBuscar.setPreferredSize(new Dimension(260, 38));
        
        cbFiltroCategoria = UIKit.filterCombo(new String[]{"Todas las Categorías"});
        cbFiltroCategoria.setPreferredSize(new Dimension(180, 38));

        btnBuscar = UIKit.secondaryButton("Filtrar");

        String[] columns = {"ID", "Nombre", "Categoría", "Cant.", "Precio", "Lote", "Vencimiento", "Estado"};
        modelProductos = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblProductos = UIKit.styledTable(modelProductos);
        
        // Renderizador para la columna Cantidad (Color rojo/ámbar según stock y categoría)
        tblProductos.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                try {
                    int cant = Integer.parseInt(value.toString());
                    String categoriaNombre = (String) table.getValueAt(row, 2);
                    
                    int umbralRojo = 15;
                    int umbralAmbar = 30;
                    
                    // Criterios según el tipo de producto
                    if (categoriaNombre != null) {
                        String catLower = categoriaNombre.toLowerCase();
                        if (catLower.contains("frutas") || catLower.contains("verduras") || catLower.contains("carnes")) {
                            umbralRojo = 10;
                            umbralAmbar = 20;
                        } else if (catLower.contains("lácteos") || catLower.contains("lacteos")) {
                            umbralRojo = 15;
                            umbralAmbar = 30;
                        } else if (catLower.contains("bebidas")) {
                            umbralRojo = 24;
                            umbralAmbar = 50;
                        } else if (catLower.contains("abarrotes")) {
                            umbralRojo = 20;
                            umbralAmbar = 40;
                        }
                    }

                    if (cant <= umbralRojo) {
                        c.setForeground(Color.WHITE);
                        c.setBackground(UIKit.DANGER);
                    } else if (cant <= umbralAmbar) {
                        c.setForeground(Color.BLACK);
                        c.setBackground(UIKit.WARNING);
                    } else {
                        c.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                        c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                    }
                } catch(Exception e) {
                    c.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                    c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                }
                setHorizontalAlignment(SwingConstants.RIGHT);
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

        lblErrorNombre = crearLabelError();
        lblErrorDescripcion = crearLabelError();
        lblErrorCantidad = crearLabelError();
        lblErrorPrecio = crearLabelError();
        lblErrorLote = crearLabelError();
        lblErrorVencimiento = crearLabelError();

        btnGuardar = UIKit.primaryButton("Guardar / Actualizar");
        btnLimpiar = UIKit.secondaryButton("Limpiar / Nuevo");
        btnEliminar = UIKit.dangerOutlineButton("Eliminar (Ocultar)");
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(
                UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        getContentPane().add(
                UIKit.screenHeader("Productos", "Inicio / Productos", btnLimpiar),
                BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(UIKit.SPACE_LG, 0));
        cuerpo.setOpaque(false);

        // -- Tarjeta Izquierda: Tabla --
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

        // -- Tarjeta Derecha: Formulario --
        JPanel pnlForm = UIKit.card();
        pnlForm.setPreferredSize(new Dimension(420, 0));
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
        gbc.insets = new Insets(0, 0, 2, 0);
        pnlForm.add(UIKit.fieldLabel("ID Producto"), gbc);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(txtId, gbc);

        // Nombre
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 2, 0);
        pnlForm.add(UIKit.fieldLabel("Nombre"), gbc);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 2, 0);
        pnlForm.add(txtNombre, gbc);
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_SM, 0);
        pnlForm.add(lblErrorNombre, gbc);

        // Descripción
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 2, 0);
        pnlForm.add(UIKit.fieldLabel("Descripción"), gbc);
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 2, 0);
        pnlForm.add(txtDescripcion, gbc);
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_SM, 0);
        pnlForm.add(lblErrorDescripcion, gbc);

        // Categoría (col 1) y Precio (col 2)
        gbc.gridwidth = 1;
        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 2, UIKit.SPACE_SM);
        pnlForm.add(UIKit.fieldLabel("Categoría"), gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 2, 0);
        pnlForm.add(UIKit.fieldLabel("Precio (S/)"), gbc);

        gbc.gridy = 10;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 2, UIKit.SPACE_SM);
        pnlForm.add(cbCategoria, gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 2, 0);
        pnlForm.add(txtPrecio, gbc);

        gbc.gridy = 11;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_SM, UIKit.SPACE_SM);
        pnlForm.add(new JLabel(" "), gbc); // Placeholder para mantener alineación
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_SM, 0);
        pnlForm.add(lblErrorPrecio, gbc);

        // Cantidad (col 1) y Lote (col 2)
        gbc.gridy = 12;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 2, UIKit.SPACE_SM);
        pnlForm.add(UIKit.fieldLabel("Cantidad"), gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 2, 0);
        pnlForm.add(UIKit.fieldLabel("Lote"), gbc);

        gbc.gridy = 13;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 2, UIKit.SPACE_SM);
        pnlForm.add(txtCantidad, gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 2, 0);
        pnlForm.add(txtLote, gbc);

        gbc.gridy = 14;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_SM, UIKit.SPACE_SM);
        pnlForm.add(lblErrorCantidad, gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_SM, 0);
        pnlForm.add(lblErrorLote, gbc);

        // Vencimiento (col 1) y Estado (col 2)
        gbc.gridy = 15;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 2, UIKit.SPACE_SM);
        pnlForm.add(UIKit.fieldLabel("Vencimiento (AAAA-MM-DD)"), gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 2, 0);
        pnlForm.add(UIKit.fieldLabel("Estado"), gbc);
        
        gbc.gridy = 16;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 2, UIKit.SPACE_SM);
        pnlForm.add(txtVencimiento, gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 2, 0);
        pnlForm.add(cbEstado, gbc);

        gbc.gridy = 17;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_LG, UIKit.SPACE_SM);
        pnlForm.add(lblErrorVencimiento, gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_LG, 0);
        pnlForm.add(new JLabel(" "), gbc);

        // Botones
        JPanel pnlBotones = new JPanel(new GridLayout(2, 2, UIKit.SPACE_SM, UIKit.SPACE_SM));
        pnlBotones.setOpaque(false);
        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnLimpiar);
        pnlBotones.add(btnEliminar);

        gbc.gridy = 18;
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
        cbFiltroCategoria.addItem("Todas las Categorías");
        
        CategoriaDAO dao = new CategoriaDAO();
        categoriasCacheadas = dao.listarTodos();
        for (Categoria c : categoriasCacheadas) {
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
            String catDesc = "-";
            if (categoriasCacheadas != null) {
                Optional<Categoria> catOpt = categoriasCacheadas.stream().filter(c -> c.getIdCategoria() == p.getIdCategoria()).findFirst();
                if (catOpt.isPresent()) {
                    catDesc = catOpt.get().getDescripcion();
                }
            }
            modelProductos.addRow(new Object[]{
                p.getIdProducto(),
                p.getNombre(),
                catDesc,
                p.getCantidad(),
                "S/ " + p.getPrecio().toPlainString(),
                "-", // Placeholder Lote
                "-", // Placeholder Vencimiento
                p.getEstado() == 1 ? "Activo" : "Inactivo"
            });
        }
    }

    private void validarNombre() {
        String t = txtNombre.getText().trim();
        if (t.isEmpty()) {
            lblErrorNombre.setText("El nombre es obligatorio");
        } else if (t.length() < 3) {
            lblErrorNombre.setText("Debe tener al menos 3 caracteres");
        } else {
            lblErrorNombre.setText(" ");
        }
    }

    private void validarPrecio() {
        String t = txtPrecio.getText().trim().replace(",", ".");
        if (t.isEmpty()) {
            lblErrorPrecio.setText("El precio es obligatorio");
        } else {
            try {
                BigDecimal val = new BigDecimal(t);
                if (val.compareTo(BigDecimal.ZERO) <= 0) {
                    lblErrorPrecio.setText("El precio debe ser mayor a 0");
                } else {
                    lblErrorPrecio.setText(" ");
                }
            } catch (Exception e) {
                lblErrorPrecio.setText("Formato numérico inválido");
            }
        }
    }
    
    private void validarCantidad() {
        String t = txtCantidad.getText().trim();
        if (t.isEmpty()) {
            lblErrorCantidad.setText("La cantidad es obligatoria");
        } else {
            try {
                int val = Integer.parseInt(t);
                if (val < 0) {
                    lblErrorCantidad.setText("No puede ser negativo");
                } else {
                    lblErrorCantidad.setText(" ");
                }
            } catch (Exception e) {
                lblErrorCantidad.setText("Debe ser un entero");
            }
        }
    }
    
    private void validarVencimiento() {
        String t = txtVencimiento.getText().trim();
        if (!t.isEmpty() && !t.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            lblErrorVencimiento.setText("Formato debe ser AAAA-MM-DD");
        } else {
            lblErrorVencimiento.setText(" ");
        }
    }

    private boolean validarTodo() {
        validarNombre();
        validarPrecio();
        validarCantidad();
        validarVencimiento();
        return lblErrorNombre.getText().trim().isEmpty() && 
               lblErrorPrecio.getText().trim().isEmpty() && 
               lblErrorCantidad.getText().trim().isEmpty() &&
               lblErrorVencimiento.getText().trim().isEmpty();
    }

    private void attachEvents() {
        // Validacion en vivo
        DocumentListener validatorListener = new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { validarCampo(e); }
            public void removeUpdate(DocumentEvent e) { validarCampo(e); }
            public void insertUpdate(DocumentEvent e) { validarCampo(e); }
            
            private void validarCampo(DocumentEvent e) {
                if (e.getDocument() == txtNombre.getDocument()) validarNombre();
                else if (e.getDocument() == txtPrecio.getDocument()) validarPrecio();
                else if (e.getDocument() == txtCantidad.getDocument()) validarCantidad();
                else if (e.getDocument() == txtVencimiento.getDocument()) validarVencimiento();
            }
        };
        txtNombre.getDocument().addDocumentListener(validatorListener);
        txtPrecio.getDocument().addDocumentListener(validatorListener);
        txtCantidad.getDocument().addDocumentListener(validatorListener);
        txtVencimiento.getDocument().addDocumentListener(validatorListener);

        btnBuscar.addActionListener(e -> {
            String term = txtBuscar.getText().trim().toLowerCase();
            String cat = cbFiltroCategoria.getSelectedItem().toString();
            ProductoDAO dao = new ProductoDAO();
            List<Producto> lista = dao.listarTodos();
            
            modelProductos.setRowCount(0);
            for (Producto p : lista) {
                boolean matchTerm = term.isEmpty() || p.getNombre().toLowerCase().contains(term) || p.getDescripcion().toLowerCase().contains(term);
                boolean matchCat = cat.equals("Todas las Categorías");
                
                String catDesc = "-";
                if (categoriasCacheadas != null) {
                    Optional<Categoria> catOpt = categoriasCacheadas.stream().filter(c -> c.getIdCategoria() == p.getIdCategoria()).findFirst();
                    if (catOpt.isPresent()) {
                        catDesc = catOpt.get().getDescripcion();
                        if (!matchCat && cat.startsWith(p.getIdCategoria() + " -")) {
                            matchCat = true;
                        }
                    }
                }
                
                if (matchTerm && matchCat) {
                    modelProductos.addRow(new Object[]{
                        p.getIdProducto(),
                        p.getNombre(),
                        catDesc,
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
            if (!validarTodo()) {
                JOptionPane.showMessageDialog(this, "Por favor corrija los errores en los campos antes de guardar.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (cbCategoria.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una categoría.");
                return;
            }
            try {
                Producto p = new Producto();
                p.setNombre(txtNombre.getText().trim());
                p.setDescripcion(txtDescripcion.getText().trim());
                p.setCantidad(Integer.parseInt(txtCantidad.getText().trim()));
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
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error inesperado al guardar.");
            }
        });

        btnEliminar.addActionListener(e -> {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla.");
                return;
            }
            int opt = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de ocultar/inactivar este producto?\n\nAl igual que con las categorías, el historial de ventas no se borrará, solo se desactivará para futuras ventas.", 
                "Confirmar Acción", JOptionPane.YES_NO_OPTION);
            
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
            
            lblErrorNombre.setText(" ");
            lblErrorDescripcion.setText(" ");
            lblErrorPrecio.setText(" ");
            lblErrorCantidad.setText(" ");
            lblErrorLote.setText(" ");
            lblErrorVencimiento.setText(" ");
            
            cargarCategorias(); 
        });

        tblProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblProductos.getSelectedRow() != -1) {
                int row = tblProductos.getSelectedRow();
                txtId.setText(modelProductos.getValueAt(row, 0).toString());
                txtNombre.setText(modelProductos.getValueAt(row, 1).toString());
                txtCantidad.setText(modelProductos.getValueAt(row, 3).toString());
                txtPrecio.setText(modelProductos.getValueAt(row, 4).toString().replace("S/ ", "").replace(",", "."));
                
                txtLote.setText(modelProductos.getValueAt(row, 5).toString());
                txtVencimiento.setText(modelProductos.getValueAt(row, 6).toString());
                cbEstado.setSelectedItem(modelProductos.getValueAt(row, 7).toString());
                
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
                validarTodo();
            }
        });
    }
}

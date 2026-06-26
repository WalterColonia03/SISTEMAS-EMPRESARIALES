package Vista;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.util.Date;

import Clases.Compra;
import Clases.DetalleCompra;
import Clases.Producto;
import Clases.Proveedor;
import Modelo.CompraDAO;
import Modelo.ProductoDAO;
import Modelo.ProveedorDAO;
import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
// CORRECCIÃƒÆ’Ã¢â‚¬Å“N: import java.awt.* duplicado eliminado (2026-06-26 ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â AuditorÃƒÆ’Ã‚Â­a ERP)

public class IFrmRegistroCompras extends JInternalFrame {

    private JComboBox<String> cbProveedor;
    private JTextField txtDocumento;
    private JDateChooser txtFecha;

    private JTextField txtCodProducto;
    private JTextField txtProductoNombre;
    private JTextField txtCantidad;
    private JTextField txtPrecioUnitario;
    private JTextField txtLote;
    private JTextField txtVencimiento;

    private JTable tblDetalle;
    private DefaultTableModel modelDetalle;
    private JLabel lblSubtotal;
    private JLabel lblIgv;
    private JLabel lblTotal;

    private JButton btnAgregarProducto;
    private JButton btnQuitarProducto;
    private JButton btnRegistrarCompra;
    private JButton btnLimpiar;

    public IFrmRegistroCompras() {
        super("Registro de Compras", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        setSize(1000, 650);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    private void initComponents() {
        cbProveedor = new JComboBox<>();
        cbProveedor.addItem("Seleccione proveedor");
        ProveedorDAO pdao = new ProveedorDAO();
        for (Proveedor p : pdao.listarTodos()) {
            if (p.getEstado() == 1) {
                cbProveedor.addItem(p.getIdProveedor() + " - " + p.getRazonSocial());
            }
        }
        cbProveedor.setFont(UIKit.BODY);
        
        txtDocumento = UIKit.textField();
        txtFecha = new JDateChooser(); txtFecha.setDateFormatString("dd/MM/yyyy"); txtFecha.setDate(new java.util.Date());

        txtCodProducto = UIKit.textField();
        
        txtProductoNombre = UIKit.readOnlyField();
        
        txtCantidad = UIKit.textField();
        txtCantidad.setText("1");
        txtCantidad.setHorizontalAlignment(JTextField.RIGHT);
        
        txtPrecioUnitario = UIKit.textField();
        txtPrecioUnitario.setHorizontalAlignment(JTextField.RIGHT);
        
        txtLote = UIKit.textField();
        txtVencimiento = UIKit.textField();
        txtVencimiento.putClientProperty("JTextField.placeholderText", "AAAA-MM-DD");

        String[] columns = {"CÃƒÆ’Ã‚Â³digo", "Producto", "Cant", "P. Unit", "Subtotal", "Lote", "Vencimiento"};
        modelDetalle = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblDetalle = UIKit.styledTable(modelDetalle);

        lblSubtotal = new JLabel("S/ 0.00");
        lblSubtotal.setFont(UIKit.BODY_BOLD);
        
        lblIgv = new JLabel("S/ 0.00");
        lblIgv.setFont(UIKit.BODY_BOLD);
        
        lblTotal = new JLabel("S/ 0.00");
        lblTotal.setFont(UIKit.H1);
        lblTotal.setForeground(UIKit.ACCENT);

        btnAgregarProducto = UIKit.secondaryButton("Agregar Producto");
        btnQuitarProducto = UIKit.dangerOutlineButton("Quitar");

        btnRegistrarCompra = UIKit.primaryButton("Registrar Compra");
        btnRegistrarCompra.setPreferredSize(new Dimension(0, 44));

        btnLimpiar = UIKit.secondaryButton("Limpiar");
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(
                UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        // ===== Encabezado =====
        getContentPane().add(
                UIKit.screenHeader("Registro de Compras", "Compras  ÃƒÂ¢Ã¢â€šÂ¬Ã‚Âº  Ingreso de MercaderÃƒÆ’Ã‚Â­a"),
                BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(UIKit.SPACE_LG, 0));
        cuerpo.setOpaque(false);

        // ÃƒÂ¢Ã¢â‚¬ÂÃ¢â€šÂ¬ÃƒÂ¢Ã¢â‚¬ÂÃ¢â€šÂ¬ Izquierda (Datos y Detalle) ÃƒÂ¢Ã¢â‚¬ÂÃ¢â€šÂ¬ÃƒÂ¢Ã¢â‚¬ÂÃ¢â€šÂ¬
        JPanel pnlIzquierda = new JPanel(new BorderLayout(0, UIKit.SPACE_MD));
        pnlIzquierda.setOpaque(false);

        // Cabecera de Compra
        JPanel pnlCabecera = UIKit.card();
        pnlCabecera.setLayout(new GridBagLayout());
        GridBagConstraints gbcC = new GridBagConstraints();
        gbcC.fill = GridBagConstraints.HORIZONTAL;
        gbcC.weightx = 1.0;
        
        gbcC.gridx = 0; gbcC.gridy = 0;
        gbcC.gridwidth = 3;
        gbcC.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlCabecera.add(UIKit.sectionHeader("Datos del Comprobante", null), gbcC);

        gbcC.gridy = 1;
        gbcC.gridwidth = 1;
        gbcC.insets = new Insets(0, 0, UIKit.SPACE_XS, UIKit.SPACE_SM);
        pnlCabecera.add(UIKit.fieldLabel("Proveedor"), gbcC);
        
        gbcC.gridx = 1;
        pnlCabecera.add(UIKit.fieldLabel("Documento"), gbcC);
        
        gbcC.gridx = 2;
        gbcC.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlCabecera.add(UIKit.fieldLabel("Fecha"), gbcC);
        
        gbcC.gridy = 2;
        gbcC.gridx = 0;
        gbcC.insets = new Insets(0, 0, 0, UIKit.SPACE_SM);
        pnlCabecera.add(cbProveedor, gbcC);
        
        gbcC.gridx = 1;
        pnlCabecera.add(txtDocumento, gbcC);
        
        gbcC.gridx = 2;
        gbcC.insets = new Insets(0, 0, 0, 0);
        pnlCabecera.add(txtFecha, gbcC);

        pnlIzquierda.add(pnlCabecera, BorderLayout.NORTH);

        // Agregar Productos
        JPanel pnlDetalle = UIKit.card();
        pnlDetalle.setLayout(new BorderLayout(0, UIKit.SPACE_SM));
        pnlDetalle.add(UIKit.sectionHeader("Detalle de MercaderÃƒÆ’Ã‚Â­a", btnQuitarProducto), BorderLayout.NORTH);
        
        JPanel pnlFormProd = new JPanel(new GridBagLayout());
        pnlFormProd.setOpaque(false);
        GridBagConstraints gbcP = new GridBagConstraints();
        gbcP.fill = GridBagConstraints.HORIZONTAL;
        gbcP.insets = new Insets(0, 0, UIKit.SPACE_SM, UIKit.SPACE_SM);
        
        // Fila 1
        gbcP.gridy = 0;
        gbcP.gridx = 0; gbcP.weightx = 0.2; pnlFormProd.add(UIKit.fieldLabel("CÃƒÆ’Ã‚Â³digo"), gbcP);
        gbcP.gridx = 1; gbcP.weightx = 0.4; pnlFormProd.add(UIKit.fieldLabel("Producto"), gbcP);
        gbcP.gridx = 2; gbcP.weightx = 0.1; pnlFormProd.add(UIKit.fieldLabel("Cant"), gbcP);
        gbcP.gridx = 3; gbcP.weightx = 0.1; gbcP.insets = new Insets(0, 0, UIKit.SPACE_SM, 0); pnlFormProd.add(UIKit.fieldLabel("P. Unit"), gbcP);
        
        gbcP.gridy = 1;
        gbcP.insets = new Insets(0, 0, UIKit.SPACE_MD, UIKit.SPACE_SM);
        gbcP.gridx = 0; pnlFormProd.add(txtCodProducto, gbcP);
        gbcP.gridx = 1; pnlFormProd.add(txtProductoNombre, gbcP);
        gbcP.gridx = 2; pnlFormProd.add(txtCantidad, gbcP);
        gbcP.gridx = 3; gbcP.insets = new Insets(0, 0, UIKit.SPACE_MD, 0); pnlFormProd.add(txtPrecioUnitario, gbcP);

        // Fila 2
        gbcP.gridy = 2;
        gbcP.insets = new Insets(0, 0, UIKit.SPACE_XS, UIKit.SPACE_SM);
        gbcP.gridx = 0; pnlFormProd.add(UIKit.fieldLabel("Lote"), gbcP);
        gbcP.gridx = 1; pnlFormProd.add(UIKit.fieldLabel("Vencimiento"), gbcP);
        
        gbcP.gridy = 3;
        gbcP.insets = new Insets(0, 0, UIKit.SPACE_SM, UIKit.SPACE_SM);
        gbcP.gridx = 0; pnlFormProd.add(txtLote, gbcP);
        gbcP.gridx = 1; pnlFormProd.add(txtVencimiento, gbcP);
        
        gbcP.gridx = 2;
        gbcP.gridwidth = 2;
        gbcP.insets = new Insets(0, 0, UIKit.SPACE_SM, 0);
        pnlFormProd.add(btnAgregarProducto, gbcP);
        
        JPanel pnlBody = new JPanel(new BorderLayout(0, UIKit.SPACE_SM));
        pnlBody.setOpaque(false);
        pnlBody.add(pnlFormProd, BorderLayout.NORTH);
        
        JScrollPane scroll = new JScrollPane(tblDetalle);
        scroll.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        pnlBody.add(scroll, BorderLayout.CENTER);
        
        pnlDetalle.add(pnlBody, BorderLayout.CENTER);

        pnlIzquierda.add(pnlDetalle, BorderLayout.CENTER);

        cuerpo.add(pnlIzquierda, BorderLayout.CENTER);

        // ÃƒÂ¢Ã¢â‚¬ÂÃ¢â€šÂ¬ÃƒÂ¢Ã¢â‚¬ÂÃ¢â€šÂ¬ Derecha (Resumen) ÃƒÂ¢Ã¢â‚¬ÂÃ¢â€šÂ¬ÃƒÂ¢Ã¢â‚¬ÂÃ¢â€šÂ¬
        JPanel pnlResumen = UIKit.card();
        pnlResumen.setPreferredSize(new Dimension(300, 0));
        pnlResumen.setLayout(new GridBagLayout());
        
        GridBagConstraints gbcR = new GridBagConstraints();
        gbcR.fill = GridBagConstraints.HORIZONTAL;
        gbcR.weightx = 1.0;
        
        gbcR.gridx = 0; gbcR.gridy = 0;
        gbcR.insets = new Insets(0, 0, UIKit.SPACE_LG, 0);
        pnlResumen.add(UIKit.sectionHeader("Resumen", null), gbcR);
        
        gbcR.gridy = 1;
        gbcR.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlResumen.add(UIKit.fieldLabel("Subtotal"), gbcR);
        gbcR.gridy = 2;
        gbcR.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlResumen.add(lblSubtotal, gbcR);
        
        gbcR.gridy = 3;
        gbcR.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlResumen.add(UIKit.fieldLabel("IGV (18%)"), gbcR);
        gbcR.gridy = 4;
        gbcR.insets = new Insets(0, 0, UIKit.SPACE_LG, 0);
        pnlResumen.add(lblIgv, gbcR);
        
        gbcR.gridy = 5;
        gbcR.insets = new Insets(UIKit.SPACE_MD, 0, UIKit.SPACE_XS, 0);
        JLabel lblTotalTitle = new JLabel("TOTAL A PAGAR");
        lblTotalTitle.setFont(UIKit.BODY_BOLD);
        lblTotalTitle.setForeground(UIKit.TEXT_SECONDARY);
        pnlResumen.add(lblTotalTitle, gbcR);
        
        gbcR.gridy = 6;
        gbcR.insets = new Insets(0, 0, UIKit.SPACE_LG, 0);
        pnlResumen.add(lblTotal, gbcR);
        
        gbcR.gridy = 7;
        gbcR.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlResumen.add(btnRegistrarCompra, gbcR);
        
        gbcR.gridy = 8;
        gbcR.weighty = 1.0;
        gbcR.anchor = GridBagConstraints.NORTH;
        gbcR.insets = new Insets(0, 0, 0, 0);
        pnlResumen.add(btnLimpiar, gbcR);

        cuerpo.add(pnlResumen, BorderLayout.EAST);

        getContentPane().add(cuerpo, BorderLayout.CENTER);
    }

    private void attachEvents() {
        // Buscar producto por ID
        txtCodProducto.addActionListener(e -> {
            String cod = txtCodProducto.getText().trim();
            if (cod.isEmpty()) return;
            try {
                int id = Integer.parseInt(cod);
                ProductoDAO dao = new ProductoDAO();
                Producto p = null;
                for (Producto prod : dao.listarTodos()) {
                    if (prod.getIdProducto() == id) {
                        p = prod;
                        break;
                    }
                }
                if (p != null) {
                    txtProductoNombre.setText(p.getNombre());
                    txtPrecioUnitario.setText(String.valueOf(p.getPrecio()));
                    txtCantidad.requestFocus();
                } else {
                    JOptionPane.showMessageDialog(this, "Producto no encontrado");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "CÃƒÆ’Ã‚Â³digo invÃƒÆ’Ã‚Â¡lido");
            }
        });

        btnAgregarProducto.addActionListener(e -> {
            if (txtCodProducto.getText().isEmpty() || txtProductoNombre.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe buscar un producto primero");
                return;
            }
            try {
                int cant = Integer.parseInt(txtCantidad.getText().trim());
                // CORRECCIÃƒÆ’Ã¢â‚¬Å“N: BigDecimal para evitar errores de coma flotante en precios de compra
                BigDecimal precio    = new BigDecimal(txtPrecioUnitario.getText().trim().replace(",", "."));
                BigDecimal subtotal  = precio.multiply(new BigDecimal(cant)).setScale(2, RoundingMode.HALF_UP);
                
                modelDetalle.addRow(new Object[]{
                    txtCodProducto.getText(),
                    txtProductoNombre.getText(),
                    cant,
                    "S/ " + precio.toPlainString(),
                    "S/ " + subtotal.toPlainString(),
                    txtLote.getText(),
                    txtVencimiento.getText()
                });
                
                txtCodProducto.setText("");
                txtProductoNombre.setText("");
                txtCantidad.setText("1");
                txtPrecioUnitario.setText("");
                txtLote.setText("");
                txtVencimiento.setText("");
                txtCodProducto.requestFocus();
                
                recalcularTotales();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ingrese valores numÃƒÆ’Ã‚Â©ricos vÃƒÆ’Ã‚Â¡lidos en Cantidad y Precio");
            }
        });

        btnQuitarProducto.addActionListener(e -> {
            int row = tblDetalle.getSelectedRow();
            if (row != -1) {
                modelDetalle.removeRow(row);
                recalcularTotales();
            }
        });

        btnRegistrarCompra.addActionListener(e -> {
            if (cbProveedor.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un proveedor");
                return;
            }
            if (modelDetalle.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Debe agregar al menos un producto al detalle");
                return;
            }
            
            try {
                Compra compra = new Compra();
                String provStr = cbProveedor.getSelectedItem().toString();
                compra.setIdProveedor(Integer.parseInt(provStr.split(" - ")[0]));
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); String fecha = txtFecha.getDate() != null ? sdf.format(txtFecha.getDate()) : "";
                if (fecha.isEmpty()) {
                    fecha = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                }
                compra.setFecha(fecha);
                
                // CORRECCIÃƒÆ’Ã¢â‚¬Å“N: total como BigDecimal
                BigDecimal total = new BigDecimal(lblTotal.getText().replace("S/ ", "").replace(",", "."));
                compra.setTotal(total);
                
                for (int i = 0; i < modelDetalle.getRowCount(); i++) {
                    DetalleCompra d = new DetalleCompra();
                    d.setIdProducto(Integer.parseInt(modelDetalle.getValueAt(i, 0).toString()));
                    d.setCantidad(Integer.parseInt(modelDetalle.getValueAt(i, 2).toString()));
                    String precioStr = modelDetalle.getValueAt(i, 3).toString().replace("S/ ", "").replace(",", ".");
                    // CORRECCIÃƒÆ’Ã¢â‚¬Å“N: precio unitario como BigDecimal
                    d.setPrecioUnitario(new BigDecimal(precioStr));
                    compra.getDetalles().add(d);
                }
                
                CompraDAO dao = new CompraDAO();
                if (dao.registrarCompra(compra)) {
                    JOptionPane.showMessageDialog(this, "Compra registrada y stock actualizado con ÃƒÆ’Ã‚Â©xito.");
                    btnLimpiar.doClick();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al registrar la compra en la base de datos.");
                }
                
            } catch (Exception ex) {
                Utils.LoggerGlobal.error("IFrmRegistroCompras.registrar() fallÃƒÆ’Ã‚Â³", ex); // CORRECCIÃƒÆ’Ã¢â‚¬Å“N
                JOptionPane.showMessageDialog(this, "Error al procesar los datos de la compra: " + ex.getMessage());
            }
        });

        btnLimpiar.addActionListener(e -> {
            cbProveedor.setSelectedIndex(0);
            txtDocumento.setText("");
            txtFecha.setDate(new Date());
            txtCodProducto.setText("");
            txtProductoNombre.setText("");
            txtCantidad.setText("1");
            txtPrecioUnitario.setText("");
            txtLote.setText("");
            txtVencimiento.setText("");
            modelDetalle.setRowCount(0);
            recalcularTotales();
        });
        
        txtFecha.setDate(new Date());
    }

    /**
     * Recalcula subtotal, IGV (18%) y total usando BigDecimal para precisiÃƒÆ’Ã‚Â³n exacta.
     * CORRECCIÃƒÆ’Ã¢â‚¬Å“N: Migrado de double a BigDecimal (2026-06-26 ÃƒÂ¢Ã¢â€šÂ¬Ã¢â‚¬Â AuditorÃƒÆ’Ã‚Â­a ERP)
     */
    private void recalcularTotales() {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (int i = 0; i < modelDetalle.getRowCount(); i++) {
            String val = modelDetalle.getValueAt(i, 4).toString().replace("S/ ", "").replace(",", ".");
            subtotal = subtotal.add(new BigDecimal(val));
        }
        BigDecimal igv   = subtotal.multiply(new BigDecimal("0.18")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(igv).setScale(2, RoundingMode.HALF_UP);
        subtotal         = subtotal.setScale(2, RoundingMode.HALF_UP);
        lblSubtotal.setText("S/ " + subtotal.toPlainString());
        lblIgv.setText("S/ " + igv.toPlainString());
        lblTotal.setText("S/ " + total.toPlainString());
    }
}

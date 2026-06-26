package Vista;

import Clases.Cliente;
import Clases.DetalleVenta;
import Clases.Producto;
import Clases.Venta;
import Modelo.ApisPeruService;
import Modelo.ClienteDAO;
import Modelo.ProductoDAO;
import Modelo.VentaDAO;
import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class IFrmPuntoVenta extends JInternalFrame {

    private JTextField txtCodProducto;
    private JTable tblCarrito;
    private DefaultTableModel modelCarrito;

    private JTextField txtBuscarProd;
    private JPanel pnlGridProductos;

    private JTextField txtDni;
    private JLabel lblNombreCliente;
    private JLabel lblPuntos;
    private JButton btnBuscarCliente;
    private JButton btnCanjearPuntos;

    private JComboBox<String> cbComprobante;
    private JComboBox<String> cbMetodoPago;
    private JTextField txtMontoRecibido;
    private JLabel lblSubtotal;
    private JLabel lblTotal;
    private JLabel lblVuelto;

    private JButton btnRegistrar;
    private JButton btnCancelar;

    private double descuentoPuntos = 0.0;
    private int puntosActualesCliente = 0;
    private int puntosACanjear = 0;

    public IFrmPuntoVenta() {
        super("Punto de Venta", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        cargarProductos(null);
        setSize(1150, 750);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    private void initComponents() {
        txtCodProducto = UIKit.textField();
        txtCodProducto.setPreferredSize(new Dimension(0, 44));
        txtCodProducto.putClientProperty("JTextField.placeholderText", "Ingresa el cÃ³digo de barras y presiona Enter...");

        String[] columns = { "ID", "Producto", "P. Unit.", "Cant.", "Subtotal" };
        modelCarrito = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return col == 3; } // Solo cantidad editable
        };
        tblCarrito = UIKit.styledTable(modelCarrito);

        txtBuscarProd = UIKit.textField();
        txtBuscarProd.setPreferredSize(new Dimension(300, 36));
        txtBuscarProd.putClientProperty("JTextField.placeholderText", "Buscar producto por nombre...");

        pnlGridProductos = new JPanel(new GridLayout(0, 3, 10, 10)); // 3 columnas
        pnlGridProductos.setBackground(UIKit.BG_APP);

        txtDni = UIKit.textField();
        btnBuscarCliente = UIKit.secondaryButton("ðŸ”");
        lblNombreCliente = new JLabel("Consumidor Final");
        lblNombreCliente.setFont(UIKit.BODY_BOLD);
        lblNombreCliente.setForeground(UIKit.PRIMARY);

        lblPuntos = new JLabel("Pts: 0");
        lblPuntos.setForeground(UIKit.WARNING);
        btnCanjearPuntos = UIKit.secondaryButton("Canjear");
        btnCanjearPuntos.setEnabled(false);

        cbComprobante = new JComboBox<>(new String[]{"Boleta Simple", "Factura"});
        cbComprobante.setPreferredSize(new Dimension(0, 36));

        cbMetodoPago = new JComboBox<>(new String[]{"Efectivo", "Mercado Pago (QR)", "Tarjeta DÃ©bito", "Tarjeta CrÃ©dito", "Yape", "Plin"});
        cbMetodoPago.setPreferredSize(new Dimension(0, 36));

        txtMontoRecibido = UIKit.textField();
        txtMontoRecibido.setPreferredSize(new Dimension(0, 36));
        txtMontoRecibido.putClientProperty("JTextField.placeholderText", "Monto recibido (S/)");

        lblSubtotal = new JLabel("S/ 0.00", SwingConstants.RIGHT);
        lblSubtotal.setFont(UIKit.BODY);
        
        lblTotal = new JLabel("S/ 0.00", SwingConstants.RIGHT);
        lblTotal.setFont(UIKit.H2);
        lblTotal.setForeground(UIKit.ACCENT);

        lblVuelto = new JLabel("S/ 0.00", SwingConstants.RIGHT);
        lblVuelto.setFont(UIKit.BODY_BOLD);
        lblVuelto.setForeground(UIKit.SUCCESS);

        btnRegistrar = UIKit.primaryButton("Realizar Venta");
        btnRegistrar.setPreferredSize(new Dimension(0, 44));

        btnCancelar = UIKit.dangerOutlineButton("Vaciar carrito");
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

        // ==== LADO IZQUIERDO (Carrito + CatÃ¡logo) ====
        JPanel pnlCentro = new JPanel(new BorderLayout(0, 15));
        pnlCentro.setOpaque(false);

        // Arriba: Carrito y cÃ³digo de barras
        JPanel pnlCart = UIKit.card();
        pnlCart.setLayout(new BorderLayout(0, 10));
        pnlCart.add(txtCodProducto, BorderLayout.NORTH);
        
        JScrollPane scrollCart = new JScrollPane(tblCarrito);
        scrollCart.setPreferredSize(new Dimension(0, 200));
        pnlCart.add(scrollCart, BorderLayout.CENTER);
        
        JLabel lblHelp = new JLabel("Doble clic en un producto del carrito para quitarlo");
        lblHelp.setFont(UIKit.CAPTION);
        lblHelp.setForeground(UIKit.TEXT_SECONDARY);
        pnlCart.add(lblHelp, BorderLayout.SOUTH);

        // Abajo: CatÃ¡logo
        JPanel pnlCatalogo = new JPanel(new BorderLayout(0, 10));
        pnlCatalogo.setOpaque(false);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlSearch.setOpaque(false);
        pnlSearch.add(txtBuscarProd);

        pnlCatalogo.add(pnlSearch, BorderLayout.NORTH);

        JScrollPane scrollGrid = new JScrollPane(pnlGridProductos);
        scrollGrid.setBorder(null);
        scrollGrid.getVerticalScrollBar().setUnitIncrement(16);
        pnlCatalogo.add(scrollGrid, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlCart, pnlCatalogo);
        splitPane.setDividerLocation(250);
        splitPane.setDividerSize(10);
        splitPane.setBorder(null);
        splitPane.setOpaque(false);
        pnlCentro.add(splitPane, BorderLayout.CENTER);

        getContentPane().add(pnlCentro, BorderLayout.CENTER);

        // ==== LADO DERECHO (Resumen de Venta) ====
        JPanel pnlDerecha = UIKit.card();
        pnlDerecha.setPreferredSize(new Dimension(340, 0));
        pnlDerecha.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Cliente
        gbc.insets = new Insets(0, 0, 5, 0);
        pnlDerecha.add(UIKit.sectionHeader("Cliente", null), gbc);
        gbc.gridy++;
        
        JPanel pnlDni = new JPanel(new BorderLayout(5, 0));
        pnlDni.setOpaque(false);
        pnlDni.add(txtDni, BorderLayout.CENTER);
        pnlDni.add(btnBuscarCliente, BorderLayout.EAST);
        pnlDerecha.add(pnlDni, gbc);
        gbc.gridy++;
        
        gbc.insets = new Insets(5, 0, 5, 0);
        pnlDerecha.add(lblNombreCliente, gbc);
        gbc.gridy++;
        
        JPanel pnlPts = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlPts.setOpaque(false);
        pnlPts.add(lblPuntos);
        pnlPts.add(btnCanjearPuntos);
        pnlDerecha.add(pnlPts, gbc);
        gbc.gridy++;

        gbc.insets = new Insets(15, 0, 10, 0);
        pnlDerecha.add(new JSeparator(), gbc);
        gbc.gridy++;

        // Resumen
        gbc.insets = new Insets(0, 0, 15, 0);
        pnlDerecha.add(UIKit.sectionHeader("Resumen de Venta", null), gbc);
        gbc.gridy++;

        JPanel pnlTotales = new JPanel(new GridLayout(2, 2, 5, 5));
        pnlTotales.setOpaque(false);
        pnlTotales.add(new JLabel("Subtotal"));
        pnlTotales.add(lblSubtotal);
        
        JLabel lblT = new JLabel("Total");
        lblT.setFont(UIKit.BODY_BOLD);
        pnlTotales.add(lblT);
        pnlTotales.add(lblTotal);
        pnlDerecha.add(pnlTotales, gbc);
        gbc.gridy++;

        gbc.insets = new Insets(15, 0, 5, 0);
        pnlDerecha.add(UIKit.fieldLabel("Tipo de comprobante"), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 10, 0);
        pnlDerecha.add(cbComprobante, gbc);
        gbc.gridy++;

        gbc.insets = new Insets(0, 0, 10, 0);
        pnlDerecha.add(cbMetodoPago, gbc);
        gbc.gridy++;

        gbc.insets = new Insets(0, 0, 10, 0);
        pnlDerecha.add(txtMontoRecibido, gbc);
        gbc.gridy++;

        JPanel pnlVuelto = new JPanel(new BorderLayout());
        pnlVuelto.setOpaque(false);
        pnlVuelto.add(new JLabel("Vuelto"), BorderLayout.WEST);
        pnlVuelto.add(lblVuelto, BorderLayout.EAST);
        pnlDerecha.add(pnlVuelto, gbc);
        gbc.gridy++;

        gbc.insets = new Insets(20, 0, 10, 0);
        pnlDerecha.add(btnRegistrar, gbc);
        gbc.gridy++;

        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        pnlDerecha.add(btnCancelar, gbc);

        getContentPane().add(pnlDerecha, BorderLayout.EAST);
    }

    private int getCantidadEnCarrito(int idProducto) {
        if (modelCarrito == null) return 0;
        for (int i = 0; i < modelCarrito.getRowCount(); i++) {
            if (Integer.parseInt(modelCarrito.getValueAt(i, 0).toString()) == idProducto) {
                try {
                    return Integer.parseInt(modelCarrito.getValueAt(i, 3).toString());
                } catch (Exception ex) { return 0; }
            }
        }
        return 0;
    }

    private void cargarProductos(String filtro) {
        pnlGridProductos.removeAll();
        ProductoDAO pdao = new ProductoDAO();
        List<Producto> lista = (filtro == null || filtro.isEmpty()) ? pdao.listarTodos() : pdao.buscarPorTermino(filtro);
        
        for (Producto p : lista) {
            JPanel card = new JPanel(new BorderLayout(5, 5));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(10, 10, 10, 10)
            ));
            card.setPreferredSize(new Dimension(180, 80));

            JLabel lblNom = new JLabel("<html><b>" + p.getNombre() + "</b></html>");
            lblNom.setFont(UIKit.BODY);
            card.add(lblNom, BorderLayout.NORTH);

            JLabel lblPrecio = new JLabel("S/ " + p.getPrecio().toPlainString());
            lblPrecio.setForeground(UIKit.PRIMARY);
            card.add(lblPrecio, BorderLayout.WEST);

            int stockVisual = p.getCantidad() - getCantidadEnCarrito(p.getIdProducto());

            JLabel lblStock = new JLabel(stockVisual + " ud.");
            lblStock.setFont(UIKit.CAPTION);
            if(stockVisual <= 0) {
                lblStock.setText("Sin stock");
                lblStock.setForeground(UIKit.DANGER);
            } else {
                lblStock.setForeground(UIKit.TEXT_SECONDARY);
            }
            card.add(lblStock, BorderLayout.EAST);

            if(stockVisual > 0) {
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
                card.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        agregarProductoAlCarrito(p);
                    }
                    @Override
                    public void mouseEntered(MouseEvent e) { card.setBackground(new Color(245, 245, 255)); }
                    @Override
                    public void mouseExited(MouseEvent e) { card.setBackground(Color.WHITE); }
                });
            } else {
                card.setBackground(new Color(250, 250, 250));
            }

            pnlGridProductos.add(card);
        }
        
        // Rellenar espacios vacÃ­os si hay pocos productos para no deformar el grid
        int faltantes = 3 - (lista.size() % 3);
        if(faltantes != 3) {
            for(int i=0; i<faltantes; i++) {
                JPanel empty = new JPanel();
                empty.setOpaque(false);
                pnlGridProductos.add(empty);
            }
        }

        pnlGridProductos.revalidate();
        pnlGridProductos.repaint();
    }

    private void agregarProductoAlCarrito(Producto prod) {
        // Verificar si ya existe
        for(int i=0; i<modelCarrito.getRowCount(); i++) {
            if(Integer.parseInt(modelCarrito.getValueAt(i, 0).toString()) == prod.getIdProducto()) {
                int cantActual = Integer.parseInt(modelCarrito.getValueAt(i, 3).toString());
                modelCarrito.setValueAt(cantActual + 1, i, 3);
                return;
            }
        }
        
        // Agregar nuevo
        int enCarrito = getCantidadEnCarrito(prod.getIdProducto());
        if(prod.getCantidad() - enCarrito < 1) {
            JOptionPane.showMessageDialog(this, "Stock agotado");
            return;
        }
        BigDecimal precio = prod.getPrecio();
        modelCarrito.addRow(new Object[]{
            prod.getIdProducto(),
            prod.getNombre(),
            precio.toPlainString(),
            1,
            precio.toPlainString()
        });
        recalcularTotal();
        cargarProductos(txtBuscarProd.getText());
    }

    private void attachEvents() {
        // Buscador catÃ¡logo
        txtBuscarProd.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { cargarProductos(txtBuscarProd.getText()); }
            public void removeUpdate(DocumentEvent e) { cargarProductos(txtBuscarProd.getText()); }
            public void changedUpdate(DocumentEvent e) { cargarProductos(txtBuscarProd.getText()); }
        });

        // Evento de ediciÃ³n de cantidad en el carrito
        modelCarrito.addTableModelListener(e -> {
            if (e.getColumn() == 3 && e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                if (row < 0 || row >= modelCarrito.getRowCount()) return;
                try {
                    int idProd = Integer.parseInt(modelCarrito.getValueAt(row, 0).toString());
                    int nuevaCant = Integer.parseInt(modelCarrito.getValueAt(row, 3).toString());
                    
                    if (nuevaCant <= 0) {
                        JOptionPane.showMessageDialog(IFrmPuntoVenta.this, "La cantidad debe ser mayor a 0. Para eliminar, doble clic en la fila.");
                        SwingUtilities.invokeLater(() -> modelCarrito.setValueAt(1, row, 3));
                        return;
                    }
                    
                    ProductoDAO dao = new ProductoDAO();
                    Producto prodReal = null;
                    for (Producto p : dao.listarTodos()) {
                        if (p.getIdProducto() == idProd) { prodReal = p; break; }
                    }
                    
                    if (prodReal != null && nuevaCant > prodReal.getCantidad()) {
                        int maxDisponible = prodReal.getCantidad();
                        JOptionPane.showMessageDialog(IFrmPuntoVenta.this, "Stock insuficiente. MÃ¡ximo disponible: " + maxDisponible);
                        SwingUtilities.invokeLater(() -> modelCarrito.setValueAt(maxDisponible, row, 3));
                        return;
                    }
                    
                    BigDecimal precio = new BigDecimal(modelCarrito.getValueAt(row, 2).toString().replace(",", "."));
                    BigDecimal sub = precio.multiply(new BigDecimal(nuevaCant));
                    
                    SwingUtilities.invokeLater(() -> {
                        modelCarrito.setValueAt(sub.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString(), row, 4);
                        recalcularTotal();
                        cargarProductos(txtBuscarProd.getText());
                    });
                } catch(NumberFormatException ex) {
                    JOptionPane.showMessageDialog(IFrmPuntoVenta.this, "Ingrese una cantidad numÃ©rica vÃ¡lida.");
                    SwingUtilities.invokeLater(() -> modelCarrito.setValueAt(1, row, 3));
                }
            }
        });

        // Ingreso por cÃ³digo barras
        txtCodProducto.addActionListener(e -> {
            String cod = txtCodProducto.getText().trim();
            if(cod.isEmpty()) return;
            try {
                int id = Integer.parseInt(cod);
                ProductoDAO dao = new ProductoDAO();
                Producto prod = null;
                for(Producto p : dao.listarTodos()) {
                    if(p.getIdProducto() == id) { prod = p; break; }
                }
                if(prod != null) {
                    agregarProductoAlCarrito(prod);
                    txtCodProducto.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Producto no encontrado");
                }
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "CÃ³digo invÃ¡lido");
            }
        });

        // Eliminar fila
        tblCarrito.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tblCarrito.getSelectedRow();
                    if(row != -1) {
                        modelCarrito.removeRow(row);
                        recalcularTotal();
                        cargarProductos(txtBuscarProd.getText());
                    }
                }
            }
        });

        // Buscar cliente
        btnBuscarCliente.addActionListener(e -> {
            String dni = txtDni.getText().trim();
            descuentoPuntos = 0.0;
            puntosACanjear = 0;
            recalcularTotal();
            if (dni.isEmpty()) {
                lblNombreCliente.setText("Consumidor Final");
                lblPuntos.setText("Pts: 0");
                btnCanjearPuntos.setEnabled(false);
                puntosActualesCliente = 0;
                return;
            }
            ClienteDAO cdao = new ClienteDAO();
            boolean encontrado = false;
            for (Cliente c : cdao.listarTodos()) {
                if (c.getDni() != null && c.getDni().equals(dni)) {
                    lblNombreCliente.setText(c.getNombre() + " " + (c.getApellido() != null ? c.getApellido() : ""));
                    puntosActualesCliente = c.getPuntos();
                    lblPuntos.setText("Pts: " + puntosActualesCliente);
                    btnCanjearPuntos.setEnabled(puntosActualesCliente >= 100);
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                lblNombreCliente.setText("Buscando en API...");
                SwingUtilities.invokeLater(() -> {
                    if (dni.length() == 8) {
                        String[] datos = ApisPeruService.consultarDNI(dni);
                        if (datos != null) {
                            lblNombreCliente.setText(datos[0] + " " + datos[1] + " " + datos[2]);
                            Cliente nC = new Cliente(cdao.generarId(), datos[0], datos[1] + " " + datos[2], dni, "", "", 1);
                            cdao.guardar(nC);
                        } else {
                            int resp = JOptionPane.showConfirmDialog(this, "DNI no encontrado en la base de datos pÃºblica.\nÂ¿Desea registrarlo manualmente?", "No encontrado", JOptionPane.YES_NO_OPTION);
                            if (resp == JOptionPane.YES_OPTION) {
                                String nombres = JOptionPane.showInputDialog(this, "Nombres y apellidos:");
                                if (nombres != null && !nombres.trim().isEmpty()) {
                                    lblNombreCliente.setText(nombres.trim());
                                    Cliente nC = new Cliente(cdao.generarId(), nombres.trim(), "", dni, "", "", 1);
                                    cdao.guardar(nC);
                                } else { lblNombreCliente.setText("Consumidor Final"); }
                            } else { lblNombreCliente.setText("Consumidor Final"); lblPuntos.setText("Pts: 0"); }
                        }
                    } else if (dni.length() == 11) {
                        String[] datos = ApisPeruService.consultarRUC(dni);
                        if (datos != null) {
                            lblNombreCliente.setText(datos[0]);
                            Cliente nC = new Cliente(cdao.generarId(), datos[0], "", dni, "", "", 1);
                            cdao.guardar(nC);
                        } else {
                            lblNombreCliente.setText("Consumidor Final");
                            JOptionPane.showMessageDialog(this, "RUC no encontrado");
                        }
                    } else {
                        lblNombreCliente.setText("Consumidor Final");
                        JOptionPane.showMessageDialog(this, "DNI/RUC invÃ¡lido");
                    }
                });
            }
        });

        // Canjear puntos
        btnCanjearPuntos.addActionListener(e -> {
            if (puntosActualesCliente >= 100) {
                int puntosUsables = (puntosActualesCliente / 100) * 100;
                descuentoPuntos = puntosUsables / 100.0;
                puntosACanjear = puntosUsables;
                JOptionPane.showMessageDialog(this, "Descuento de S/ " + String.format("%.2f", descuentoPuntos) + " aplicado.");
                recalcularTotal();
                btnCanjearPuntos.setEnabled(false);
            }
        });

        // Calcular vuelto y bloquear input si es pago digital
        txtMontoRecibido.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { calcularVuelto(); }
            public void removeUpdate(DocumentEvent e) { calcularVuelto(); }
            public void changedUpdate(DocumentEvent e) { calcularVuelto(); }
        });

        cbMetodoPago.addActionListener(e -> {
            String metodo = cbMetodoPago.getSelectedItem().toString();
            if (!metodo.equals("Efectivo")) {
                txtMontoRecibido.setText(lblTotal.getText().replace("S/ ", ""));
                txtMontoRecibido.setEditable(false);
                txtMontoRecibido.setBackground(new Color(240, 240, 240));
            } else {
                txtMontoRecibido.setEditable(true);
                txtMontoRecibido.setBackground(Color.WHITE);
                txtMontoRecibido.setText("");
            }
        });

        // Registrar
        btnRegistrar.addActionListener(e -> {
            if (modelCarrito.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "El carrito estÃ¡ vacÃ­o");
                return;
            }
            try {
                String totalStr = lblTotal.getText().replace("S/ ", "").replace(",", ".");
                BigDecimal total = new BigDecimal(totalStr);

                // ValidaciÃ³n de pago en Efectivo
                if (cbMetodoPago.getSelectedItem().toString().equals("Efectivo")) {
                    String montoStr = txtMontoRecibido.getText().trim().replace(",", ".");
                    if (montoStr.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Debe ingresar el monto recibido del cliente.", "Monto vacÃ­o", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    try {
                        BigDecimal montoRecibido = new BigDecimal(montoStr);
                        if (montoRecibido.compareTo(total) < 0) {
                            BigDecimal faltante = total.subtract(montoRecibido).setScale(2, java.math.RoundingMode.HALF_UP);
                            JOptionPane.showMessageDialog(this, 
                                "âš  El monto ingresado (S/ " + montoRecibido.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString() + 
                                ") es insuficiente para cubrir el total (S/ " + total.toPlainString() + ").\n\nFaltan S/ " + 
                                faltante.toPlainString() + " para completar el pago.", 
                                "Pago Insuficiente", 
                                JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Monto recibido invÃ¡lido. Ingrese solo nÃºmeros.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    // ValidaciÃ³n para pagos digitales (Yape, Tarjetas, etc)
                    String montoStr = txtMontoRecibido.getText().trim().replace(",", ".");
                    BigDecimal montoRecibido = new BigDecimal(montoStr);
                    if (montoRecibido.compareTo(total) > 0) {
                        JOptionPane.showMessageDialog(this, 
                            "âš  Los pagos digitales (Yape, Plin, Tarjeta) deben ser por el monto EXACTO de la venta.\n\n" +
                            "Si el cliente transfiriÃ³ S/ " + montoRecibido.toPlainString() + " por error (S/ " + 
                            montoRecibido.subtract(total).setScale(2, java.math.RoundingMode.HALF_UP).toPlainString() + " de mÃ¡s),\n" +
                            "la devoluciÃ³n debe realizarse manualmente desde su aplicaciÃ³n bancaria\npara evitar descuadres en la contabilidad del sistema.", 
                            "Sobrepago Digital Detectado", 
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                Venta v = new Venta();
                v.setCliente(lblNombreCliente.getText());
                v.setFecha(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
                v.setTotal(total);

                for (int i = 0; i < modelCarrito.getRowCount(); i++) {
                    DetalleVenta d = new DetalleVenta();
                    d.setIdProducto(Integer.parseInt(modelCarrito.getValueAt(i, 0).toString()));
                    d.setPrecioUnitario(new BigDecimal(modelCarrito.getValueAt(i, 2).toString().replace(",", ".")));
                    d.setCantidad(Integer.parseInt(modelCarrito.getValueAt(i, 3).toString()));
                    v.getDetalles().add(d);
                }

                VentaDAO dao = new VentaDAO();
                if (dao.registrarVentaCompleta(v)) {
                    Utils.BitacoraService.registrar(Clases.Sesion.getUsuario(), Utils.BitacoraService.MOD_POS, "REGISTRO_VENTA", Utils.BitacoraService.OK, "Total: S/ " + total.toPlainString());
                    String dniStr = txtDni.getText().trim();
                    if (!dniStr.isEmpty()) {
                        ClienteDAO cdao = new ClienteDAO();
                        int puntosGanados = total.intValue();
                        cdao.actualizarPuntosPorDni(dniStr, puntosGanados - puntosACanjear);
                    }
                    JOptionPane.showMessageDialog(this, "âœ… Venta registrada con Ã©xito.\nVuelto: " + lblVuelto.getText());
                    btnCancelar.doClick();
                    cargarProductos(txtBuscarProd.getText()); // recargar stock
                } else {
                    JOptionPane.showMessageDialog(this, "âŒ Error al guardar en base de datos.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error procesando la venta: " + ex.getMessage());
            }
        });

        // Cancelar
        btnCancelar.addActionListener(e -> {
            modelCarrito.setRowCount(0);
            txtDni.setText("");
            txtCodProducto.setText("");
            txtBuscarProd.setText("");
            lblNombreCliente.setText("Consumidor Final");
            lblPuntos.setText("Pts: 0");
            descuentoPuntos = 0.0;
            puntosACanjear = 0;
            puntosActualesCliente = 0;
            btnCanjearPuntos.setEnabled(false);
            txtMontoRecibido.setText("");
            recalcularTotal();
            cargarProductos(txtBuscarProd.getText());
        });
    }

    private void recalcularTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < modelCarrito.getRowCount(); i++) {
            String subtotalStr = modelCarrito.getValueAt(i, 4).toString().replace(",", ".");
            total = total.add(new BigDecimal(subtotalStr));
        }
        
        lblSubtotal.setText("S/ " + total.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString());

        BigDecimal descuento = new BigDecimal(descuentoPuntos);
        total = total.subtract(descuento);
        if (total.compareTo(BigDecimal.ZERO) < 0) total = BigDecimal.ZERO;
        
        lblTotal.setText("S/ " + total.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString());
        
        if (cbMetodoPago != null && cbMetodoPago.getSelectedItem() != null && !cbMetodoPago.getSelectedItem().toString().equals("Efectivo")) {
            txtMontoRecibido.setText(total.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString());
        }
        
        calcularVuelto();
    }

    private void calcularVuelto() {
        try {
            String montoStr = txtMontoRecibido.getText().trim();
            if(montoStr.isEmpty()) {
                lblVuelto.setText("S/ 0.00");
                return;
            }
            BigDecimal monto = new BigDecimal(montoStr.replace(",", "."));
            String totalStr = lblTotal.getText().replace("S/ ", "").replace(",", ".");
            BigDecimal total = new BigDecimal(totalStr);
            
            BigDecimal vuelto = monto.subtract(total);
            if(vuelto.compareTo(BigDecimal.ZERO) < 0) vuelto = BigDecimal.ZERO;
            
            lblVuelto.setText("S/ " + vuelto.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString());
        } catch(Exception e) {
            lblVuelto.setText("S/ 0.00");
        }
    }
}

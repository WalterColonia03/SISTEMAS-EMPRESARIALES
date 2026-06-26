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
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * IFrmPuntoVenta - Módulo de Facturación / POS.
 * Rediseñado con UIKit (Patrón H).
 */
public class IFrmPuntoVenta extends JInternalFrame {

    private JTextField txtDni;
    private JLabel lblNombreCliente;
    private JTextField txtCodProducto;
    private JTextField txtCantidad;
    private JTable tblCarrito;
    private DefaultTableModel modelCarrito;

    private JComboBox<String> cbMetodoPago;
    private JLabel lblTotal;
    private JButton btnAgregar;
    private JButton btnBuscarCliente;
    private JButton btnRegistrar;
    private JButton btnCancelar;
    
    private JLabel lblPuntos;
    private JButton btnCanjearPuntos;
    private double descuentoPuntos = 0.0;
    private int puntosActualesCliente = 0;
    private int puntosACanjear = 0;

    public IFrmPuntoVenta() {
        super("Punto de Venta (POS)", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        setSize(1000, 650);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    private void initComponents() {
        txtDni = UIKit.textField();
        txtDni.setPreferredSize(new Dimension(150, 36));

        lblNombreCliente = new JLabel("Consumidor Final");
        lblNombreCliente.setFont(UIKit.BODY_BOLD);
        lblNombreCliente.setForeground(UIKit.PRIMARY);

        txtCodProducto = UIKit.textField();
        txtCodProducto.setPreferredSize(new Dimension(180, 36));
        txtCodProducto.putClientProperty("JTextField.placeholderText", "Código de barras...");
        
        lblPuntos = new JLabel("Pts: 0");
        lblPuntos.setFont(UIKit.BODY);
        lblPuntos.setForeground(UIKit.WARNING);
        
        btnCanjearPuntos = UIKit.secondaryButton("Canjear Pts");
        btnCanjearPuntos.setEnabled(false);

        txtCantidad = UIKit.textField();
        txtCantidad.setText("1");
        txtCantidad.setHorizontalAlignment(JTextField.CENTER);
        txtCantidad.setPreferredSize(new Dimension(80, 36));

        // Tabla del carrito
        String[] columns = { "Código", "Descripción", "P. Unitario", "Cant.", "Subtotal" };
        modelCarrito = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tblCarrito = UIKit.styledTable(modelCarrito);

        cbMetodoPago = new JComboBox<>(
                new String[] { "Efectivo", "Mercado Pago (QR)", "Tarjeta de Débito", "Tarjeta de Crédito" });
        cbMetodoPago.setFont(UIKit.BODY);
        cbMetodoPago.setPreferredSize(new Dimension(0, 36));

        lblTotal = new JLabel("S/ 0.00");
        lblTotal.setFont(UIKit.H1);
        lblTotal.setForeground(UIKit.ACCENT);

        btnAgregar = UIKit.secondaryButton("Agregar al Carrito");
        btnBuscarCliente = UIKit.secondaryButton("Buscar");

        btnRegistrar = UIKit.primaryButton("Cobrar y Emitir Ticket");
        btnRegistrar.setPreferredSize(new Dimension(0, 44));

        btnCancelar = UIKit.dangerOutlineButton("Cancelar Venta");
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(
                UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        // ===== Encabezado =====
        getContentPane().add(
                UIKit.screenHeader("Punto de Venta (POS)", "Ventas  ›  Punto de Venta"),
                BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(UIKit.SPACE_LG, 0));
        cuerpo.setOpaque(false);

        // ── Lado Izquierdo (Cliente y Carrito) ──
        JPanel pnlIzquierda = new JPanel(new BorderLayout(0, UIKit.SPACE_MD));
        pnlIzquierda.setOpaque(false);

        // Cliente
        JPanel pnlCliente = UIKit.card();
        pnlCliente.setLayout(new BorderLayout(0, UIKit.SPACE_SM));
        pnlCliente.add(UIKit.sectionHeader("Información del Cliente", null), BorderLayout.NORTH);

        JPanel pnlBusquedaC = new JPanel(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_SM, 0));
        pnlBusquedaC.setOpaque(false);
        pnlBusquedaC.add(UIKit.fieldLabel("DNI / RUC:"));
        pnlBusquedaC.add(txtDni);
        pnlBusquedaC.add(btnBuscarCliente);

        JPanel pnlNombreC = new JPanel(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_SM, 0));
        pnlNombreC.setOpaque(false);
        pnlNombreC.add(UIKit.fieldLabel("Cliente Activo:"));
        pnlNombreC.add(lblNombreCliente);
        pnlNombreC.add(lblPuntos);        // BUG FIX: eliminada la adición duplicada de estos componentes
        pnlNombreC.add(btnCanjearPuntos); // (2026-06-26 — Auditoría de código)

        JPanel pnlClienteBody = new JPanel(new GridLayout(2, 1, 0, UIKit.SPACE_XS));
        pnlClienteBody.setOpaque(false);
        pnlClienteBody.add(pnlBusquedaC);
        pnlClienteBody.add(pnlNombreC);

        pnlCliente.add(pnlClienteBody, BorderLayout.CENTER);

        pnlIzquierda.add(pnlCliente, BorderLayout.NORTH);

        // Carrito
        JPanel pnlCarrito = UIKit.card();
        pnlCarrito.setLayout(new BorderLayout(0, UIKit.SPACE_SM));
        pnlCarrito.add(UIKit.sectionHeader("Detalle de Venta", null), BorderLayout.NORTH);

        JPanel pnlAgregarProd = new JPanel(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_SM, 0));
        pnlAgregarProd.setOpaque(false);
        pnlAgregarProd.add(UIKit.fieldLabel("Producto:"));
        pnlAgregarProd.add(txtCodProducto);
        pnlAgregarProd.add(UIKit.fieldLabel("Cant:"));
        pnlAgregarProd.add(txtCantidad);
        pnlAgregarProd.add(btnAgregar);

        JPanel pnlCarritoBody = new JPanel(new BorderLayout(0, UIKit.SPACE_SM));
        pnlCarritoBody.setOpaque(false);
        pnlCarritoBody.add(pnlAgregarProd, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(tblCarrito);
        scroll.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        pnlCarritoBody.add(scroll, BorderLayout.CENTER);

        pnlCarrito.add(pnlCarritoBody, BorderLayout.CENTER);

        pnlIzquierda.add(pnlCarrito, BorderLayout.CENTER);

        cuerpo.add(pnlIzquierda, BorderLayout.CENTER);

        // ── Lado Derecho (Resumen y Pago) ──
        JPanel pnlDerecha = UIKit.card();
        pnlDerecha.setPreferredSize(new Dimension(320, 0));
        pnlDerecha.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlDerecha.add(UIKit.sectionHeader("Resumen de Pago", null), gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlDerecha.add(UIKit.fieldLabel("Método de Pago"), gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_LG, 0);
        pnlDerecha.add(cbMetodoPago, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(UIKit.SPACE_MD, 0, UIKit.SPACE_XS, 0);
        JLabel lblTotalTitle = new JLabel("TOTAL A PAGAR");
        lblTotalTitle.setFont(UIKit.BODY_BOLD);
        lblTotalTitle.setForeground(UIKit.TEXT_SECONDARY);
        pnlDerecha.add(lblTotalTitle, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_LG, 0);
        pnlDerecha.add(lblTotal, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlDerecha.add(btnRegistrar, gbc);

        gbc.gridy = 6;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        pnlDerecha.add(btnCancelar, gbc);

        cuerpo.add(pnlDerecha, BorderLayout.EAST);

        getContentPane().add(cuerpo, BorderLayout.CENTER);
    }

    private void attachEvents() {
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
                lblPuntos.setText("Pts: 0");
                btnCanjearPuntos.setEnabled(false);
                puntosActualesCliente = 0;

                SwingUtilities.invokeLater(() -> {
                    if (dni.length() == 8) {
                        String[] datos = ApisPeruService.consultarDNI(dni);
                        if (datos != null) {
                            lblNombreCliente.setText(datos[0] + " " + datos[1] + " " + datos[2]);
                            Cliente nC = new Cliente(cdao.generarId(), datos[0],
                                    datos[1] + " " + datos[2], dni, "", "", 1);
                            cdao.guardar(nC);
                        } else {
                            lblNombreCliente.setText("Consumidor Final");
                            lblPuntos.setText("Pts: 0");
                            JOptionPane.showMessageDialog(this, "DNI no encontrado en RENIEC");
                        }
                    } else if (dni.length() == 11) {
                        String[] datos = ApisPeruService.consultarRUC(dni);
                        if (datos != null) {
                            lblNombreCliente.setText(datos[0]);
                            Cliente nC = new Cliente(cdao.generarId(), datos[0], "", dni, "", "", 1);
                            cdao.guardar(nC);
                        } else {
                            lblNombreCliente.setText("Consumidor Final");
                            lblPuntos.setText("Pts: 0");
                            JOptionPane.showMessageDialog(this, "RUC no encontrado en SUNAT");
                        }
                    } else {
                        lblNombreCliente.setText("Consumidor Final");
                        lblPuntos.setText("Pts: 0");
                        JOptionPane.showMessageDialog(this, "Debe ser un DNI (8 dígitos) o RUC (11 dígitos)");
                    }
                });
            }
        });

        btnCanjearPuntos.addActionListener(e -> {
            if (puntosActualesCliente >= 100) {
                // Cada 100 puntos = S/ 1.00 de descuento
                int puntosUsables = (puntosActualesCliente / 100) * 100;
                descuentoPuntos = puntosUsables / 100.0; // double solo para almacenaje del descuento UI
                puntosACanjear = puntosUsables;
                JOptionPane.showMessageDialog(this,
                        "Se aplicará un descuento de S/ " + String.format("%.2f", descuentoPuntos) +
                        "\n(Usando " + puntosUsables + " puntos)");
                recalcularTotal();
                btnCanjearPuntos.setEnabled(false);
            }
        });

        btnAgregar.addActionListener(e -> {
            String cod = txtCodProducto.getText().trim();
            if (cod.isEmpty()) return;
            try {
                int id = Integer.parseInt(cod);
                ProductoDAO pdao = new ProductoDAO();
                Producto prod = null;
                for (Producto p : pdao.listarTodos()) {
                    if (p.getIdProducto() == id) { prod = p; break; }
                }
                if (prod != null) {
                    int cant = Integer.parseInt(txtCantidad.getText().trim());
                    if (cant > prod.getCantidad()) {
                        JOptionPane.showMessageDialog(this,
                                "Stock insuficiente. Stock actual: " + prod.getCantidad());
                        return;
                    }
                    // CORRECCIÓN: uso de BigDecimal para precio * cantidad (precisión financiera)
                    java.math.BigDecimal precio   = prod.getPrecio();
                    java.math.BigDecimal subtotal = precio.multiply(new java.math.BigDecimal(cant));

                    modelCarrito.addRow(new Object[]{
                        prod.getIdProducto(),
                        prod.getNombre(),
                        precio.toPlainString(),
                        cant,
                        subtotal.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString()
                    });
                    txtCodProducto.setText("");
                    txtCantidad.setText("1");
                    recalcularTotal();
                    txtCodProducto.requestFocus();
                } else {
                    JOptionPane.showMessageDialog(this, "Producto no encontrado");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Código o cantidad inválida");
            }
        });

        btnRegistrar.addActionListener(e -> {
            if (modelCarrito.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "El carrito está vacío");
                return;
            }
            try {
                // CORRECCIÓN: total leído y almacenado como BigDecimal
                String totalStr = lblTotal.getText().replace("S/ ", "").replace(",", ".");
                java.math.BigDecimal total = new java.math.BigDecimal(totalStr);

                Venta v = new Venta();
                v.setCliente(lblNombreCliente.getText());
                v.setFecha(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
                v.setTotal(total);

                for (int i = 0; i < modelCarrito.getRowCount(); i++) {
                    DetalleVenta d = new DetalleVenta();
                    d.setIdProducto(Integer.parseInt(modelCarrito.getValueAt(i, 0).toString()));
                    // CORRECCIÓN: precio unitario como BigDecimal
                    d.setPrecioUnitario(new java.math.BigDecimal(
                            modelCarrito.getValueAt(i, 2).toString().replace(",", ".")));
                    d.setCantidad(Integer.parseInt(modelCarrito.getValueAt(i, 3).toString()));
                    v.getDetalles().add(d);
                }

                VentaDAO dao = new VentaDAO();
                if (dao.registrarVentaCompleta(v)) {
                    // FR-020-v2 CA-1: registrar venta exitosa en bitácora
                    Utils.BitacoraService.registrar(
                        Clases.Sesion.getUsuario(),
                        Utils.BitacoraService.MOD_POS,
                        "REGISTRO_VENTA",
                        Utils.BitacoraService.OK,
                        "Total: S/ " + total.toPlainString() + " | Cliente: " + v.getCliente()
                    );
                    String dniStr = txtDni.getText().trim();
                    if (!dniStr.isEmpty()) {
                        ClienteDAO cdao = new ClienteDAO();
                        int puntosGanados = total.intValue();
                        int deltaPuntos = puntosGanados - puntosACanjear;
                        cdao.actualizarPuntosPorDni(dniStr, deltaPuntos);
                        JOptionPane.showMessageDialog(this,
                                "✅ Venta registrada con éxito.\nPuntos ganados: +" + puntosGanados);
                    } else {
                        JOptionPane.showMessageDialog(this, "✅ Venta registrada con éxito.");
                    }
                    btnCancelar.doClick();
                } else {
                    Utils.BitacoraService.registrar(
                        Clases.Sesion.getUsuario(),
                        Utils.BitacoraService.MOD_POS,
                        "REGISTRO_VENTA",
                        Utils.BitacoraService.FALLO,
                        "Error al guardar en BD"
                    );
                    JOptionPane.showMessageDialog(this, "❌ Error al guardar en base de datos.");
                }
            } catch (Exception ex) {
                Utils.LoggerGlobal.error("IFrmPuntoVenta.registrar() falló", ex);
                JOptionPane.showMessageDialog(this, "Error procesando la venta: " + ex.getMessage());
            }
        });

        btnCancelar.addActionListener(e -> {
            modelCarrito.setRowCount(0);
            txtDni.setText("");
            txtCodProducto.setText("");
            txtCantidad.setText("1");
            lblNombreCliente.setText("Consumidor Final");
            lblPuntos.setText("Pts: 0");
            descuentoPuntos = 0.0;
            puntosACanjear = 0;
            puntosActualesCliente = 0;
            btnCanjearPuntos.setEnabled(false);
            lblTotal.setText("S/ 0.00");
        });
    }

    /**
     * Recalcula el total del carrito usando BigDecimal para evitar errores de coma flotante.
     * Aplica el descuento por canje de puntos si corresponde.
     * Creado: 2026-06-25 | Refactorizado: 2026-06-26 (Auditoría ERP)
     */
    private void recalcularTotal() {
        // CORRECCIÓN: acumulación con BigDecimal, no con double
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        for (int i = 0; i < modelCarrito.getRowCount(); i++) {
            String subtotalStr = modelCarrito.getValueAt(i, 4).toString().replace(",", ".");
            total = total.add(new java.math.BigDecimal(subtotalStr));
        }
        // Descuento por canje de puntos
        java.math.BigDecimal descuento = new java.math.BigDecimal(descuentoPuntos);
        total = total.subtract(descuento);
        if (total.compareTo(java.math.BigDecimal.ZERO) < 0) {
            total = java.math.BigDecimal.ZERO;
        }
        lblTotal.setText("S/ " + total.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString());
    }
}


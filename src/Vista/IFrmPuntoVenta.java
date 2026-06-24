package Vista;

import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

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
            // TODO: lógica TXT para buscar cliente por DNI
        });

        btnAgregar.addActionListener(e -> {
            // TODO: lógica TXT para buscar producto y agregar a la tabla del carrito
        });

        btnRegistrar.addActionListener(e -> {
            // TODO: lógica TXT para guardar la venta e imprimir PDF / mostrar QR Mercado
            // Pago si corresponde
        });

        btnCancelar.addActionListener(e -> {
            modelCarrito.setRowCount(0);
            txtDni.setText("");
            txtCodProducto.setText("");
            txtCantidad.setText("1");
            lblNombreCliente.setText("Consumidor Final");
            lblTotal.setText("S/ 0.00");
        });
    }
}

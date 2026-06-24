package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class IFrmLibroMayor extends JInternalFrame {

    private JComboBox<String> cbCuentaContable;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JButton btnFiltrar;
    private JButton btnRefrescar;

    private JTable tblAsientos;
    private DefaultTableModel modelAsientos;

    private JLabel lblSaldoAnterior;
    private JLabel lblDebeTotal;
    private JLabel lblHaberTotal;
    private JLabel lblSaldoActual;

    private static final Color COLOR_PRIMARY = new Color(25, 118, 210);
    private static final Color COLOR_ACCENT = new Color(46, 125, 50);

    public IFrmLibroMayor() {
        super("Libro Mayor - Visualizaci\u00f3n de Asientos Contables", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        setSize(1000, 620);
    }

    private void initComponents() {
        cbCuentaContable = new JComboBox<>(new String[]{
            "Todas las Cuentas",
            "Efectivo y Equivalentes (101)",
            "Mercader\u00edas (201)",
            "Cuentas por Cobrar (121)",
            "Cuentas por Pagar (421)",
            "Capital (501)",
            "Ventas (701)",
            "Compras (601)",
            "Gastos Administrativos (941)",
            "IGV por Pagar (4011)"
        });
        txtFechaInicio = new JTextField(10);
        txtFechaFin = new JTextField(10);
        btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setBackground(COLOR_PRIMARY);
        btnFiltrar.setForeground(Color.WHITE);
        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setBackground(new Color(96, 125, 139));
        btnRefrescar.setForeground(Color.WHITE);

        String[] columns = {"Fecha", "Glosa", "Cuenta Debe", "Cuenta Haber", "Debe (S/)", "Haber (S/)", "Nro Asiento"};
        modelAsientos = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblAsientos = new JTable(modelAsientos);

        lblSaldoAnterior = new JLabel("S/ 0.00");
        lblSaldoAnterior.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDebeTotal = new JLabel("S/ 0.00");
        lblDebeTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDebeTotal.setForeground(COLOR_ACCENT);
        lblHaberTotal = new JLabel("S/ 0.00");
        lblHaberTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHaberTotal.setForeground(new Color(198, 40, 40));
        lblSaldoActual = new JLabel("S/ 0.00");
        lblSaldoActual.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblSaldoActual.setForeground(COLOR_PRIMARY);

        // Datos de ejemplo
        modelAsientos.addRow(new Object[]{"2025-06-01", "Venta al contado", "101 Efectivo", "701 Ventas", "1,200.00", "0.00", "ASC-001"});
        modelAsientos.addRow(new Object[]{"2025-06-01", "Venta al contado", "701 Ventas", "101 Efectivo", "0.00", "1,200.00", "ASC-001"});
        modelAsientos.addRow(new Object[]{"2025-06-02", "Compra mercader\u00eda", "601 Compras", "421 Ctas Pagar", "850.00", "0.00", "ASC-002"});
        modelAsientos.addRow(new Object[]{"2025-06-02", "Compra mercader\u00eda", "421 Ctas Pagar", "601 Compras", "0.00", "850.00", "ASC-002"});
        modelAsientos.addRow(new Object[]{"2025-06-03", "Pago a proveedor", "421 Ctas Pagar", "101 Efectivo", "850.00", "0.00", "ASC-003"});
        modelAsientos.addRow(new Object[]{"2025-06-03", "Pago a proveedor", "101 Efectivo", "421 Ctas Pagar", "0.00", "850.00", "ASC-003"});

        lblDebeTotal.setText("S/ 2,900.00");
        lblHaberTotal.setText("S/ 2,900.00");
        lblSaldoActual.setText("S/ 0.00");
    }

    private void buildLayout() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel pnlFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlFiltros.setBorder(BorderFactory.createTitledBorder("Filtros Contables"));
        pnlFiltros.add(new JLabel("Cuenta Contable:"));
        pnlFiltros.add(cbCuentaContable);
        pnlFiltros.add(new JLabel("Fecha Inicio:"));
        pnlFiltros.add(txtFechaInicio);
        pnlFiltros.add(new JLabel("Fecha Fin:"));
        pnlFiltros.add(txtFechaFin);
        pnlFiltros.add(btnFiltrar);
        pnlFiltros.add(btnRefrescar);
        add(pnlFiltros, BorderLayout.NORTH);

        JPanel pnlCentral = new JPanel(new BorderLayout(10, 10));

        JPanel pnlTabla = new JPanel(new BorderLayout(5, 5));
        pnlTabla.setBorder(BorderFactory.createTitledBorder("Asientos Contables"));

        JPanel pnlAyuda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblAyuda = new JLabel("Seleccione una cuenta para ver sus movimientos individuales o \"Todas\" para ver el libro completo.");
        lblAyuda.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblAyuda.setForeground(new Color(117, 117, 117));
        pnlAyuda.add(lblAyuda);
        pnlTabla.add(pnlAyuda, BorderLayout.NORTH);
        pnlTabla.add(new JScrollPane(tblAsientos), BorderLayout.CENTER);
        pnlCentral.add(pnlTabla, BorderLayout.CENTER);

        JPanel pnlResumen = new JPanel(new GridBagLayout());
        pnlResumen.setBorder(BorderFactory.createTitledBorder("Resumen de Saldos"));
        pnlResumen.setPreferredSize(new Dimension(280, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        pnlResumen.add(new JLabel("Saldo Anterior:"), gbc);
        gbc.gridy = 1;
        pnlResumen.add(lblSaldoAnterior, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(15, 10, 5, 10);
        pnlResumen.add(new JLabel("Total Debe (Entradas):"), gbc);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 10, 5, 10);
        pnlResumen.add(lblDebeTotal, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(5, 10, 5, 10);
        pnlResumen.add(new JLabel("Total Haber (Salidas):"), gbc);
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 10, 5, 10);
        pnlResumen.add(lblHaberTotal, gbc);

        JSeparator sep = new JSeparator();
        sep.setPreferredSize(new Dimension(0, 10));
        gbc.gridy = 6;
        gbc.insets = new Insets(10, 10, 5, 10);
        pnlResumen.add(sep, gbc);

        gbc.gridy = 7;
        gbc.insets = new Insets(5, 10, 10, 10);
        pnlResumen.add(new JLabel("Saldo Actual:"), gbc);
        gbc.gridy = 8;
        pnlResumen.add(lblSaldoActual, gbc);

        pnlCentral.add(pnlResumen, BorderLayout.EAST);
        add(pnlCentral, BorderLayout.CENTER);
    }

    private void attachEvents() {
        btnFiltrar.addActionListener(e -> {
            // TODO: l\u00f3gica TXT para filtrar asientos contables por cuenta y rango de fechas desde libro_mayor.txt
            // Actualizar totales Debe, Haber y saldos
        });

        btnRefrescar.addActionListener(e -> {
            // TODO: l\u00f3gica TXT para recargar todos los asientos desde libro_mayor.txt
            JOptionPane.showMessageDialog(this, "Libro mayor actualizado desde archivo TXT.");
        });
    }
}

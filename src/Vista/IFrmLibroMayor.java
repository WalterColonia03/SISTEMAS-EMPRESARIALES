package Vista;

import Clases.LibroMayor;
import Modelo.LibroMayorDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

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

        // Datos de ejemplo removidos, se cargarán desde la BD en attachEvents()
        lblDebeTotal.setText("S/ 0.00");
        lblHaberTotal.setText("S/ 0.00");
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
        // Carga inicial
        cargarDatos();

        btnFiltrar.addActionListener(e -> {
            cargarDatos();
        });

        btnRefrescar.addActionListener(e -> {
            txtFechaInicio.setText("");
            txtFechaFin.setText("");
            cbCuentaContable.setSelectedIndex(0);
            cargarDatos();
            JOptionPane.showMessageDialog(this, "Libro mayor actualizado desde la base de datos.");
        });
    }

    /**
     * Carga y filtra los datos del Libro Mayor usando LibroMayorDAO
     * Modificado: 2026-06-25T23:15:00-05:00
     */
    private void cargarDatos() {
        LibroMayorDAO dao = new LibroMayorDAO();
        List<LibroMayor> lista = dao.listarTodos();

        String cuentaSel = cbCuentaContable.getSelectedItem().toString();
        String fechaIni = txtFechaInicio.getText().trim();
        String fechaFin = txtFechaFin.getText().trim();

        // Limpiar tabla
        modelAsientos.setRowCount(0);
        // CORRECCIÓN: acumuladores BigDecimal (2026-06-26 — Auditoría ERP)
        java.math.BigDecimal totalDebe  = java.math.BigDecimal.ZERO;
        java.math.BigDecimal totalHaber = java.math.BigDecimal.ZERO;

        for (LibroMayor lm : lista) {
            boolean pasaFiltro = true;

            // Filtro por cuenta contable
            if (cbCuentaContable.getSelectedIndex() > 0) {
                // Si seleccionó una cuenta específica, extraemos el código (ej. "Efectivo (101)" -> "101")
                // Por simplicidad, chequearemos si cuentaDebe o cuentaHaber contiene la selección
                if (!lm.getCuentaDebe().contains(cuentaSel) && !lm.getCuentaHaber().contains(cuentaSel)) {
                    pasaFiltro = false;
                }
            }

            // Filtro por fecha (simple coincidencia por ahora)
            if (!fechaIni.isEmpty() && lm.getFecha().compareTo(fechaIni) < 0) pasaFiltro = false;
            if (!fechaFin.isEmpty() && lm.getFecha().compareTo(fechaFin) > 0) pasaFiltro = false;

            if (pasaFiltro) {
                java.math.BigDecimal debe  = lm.getMontoDebe()  != null ? lm.getMontoDebe()  : java.math.BigDecimal.ZERO;
                java.math.BigDecimal haber = lm.getMontoHaber() != null ? lm.getMontoHaber() : java.math.BigDecimal.ZERO;
                modelAsientos.addRow(new Object[]{
                    lm.getFecha(),
                    lm.getGlosa(),
                    lm.getCuentaDebe(),
                    lm.getCuentaHaber(),
                    // CORRECCIÓN: toPlainString() para BigDecimal
                    debe.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString(),
                    haber.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString(),
                    lm.getNroAsiento()
                });
                totalDebe  = totalDebe.add(debe);
                totalHaber = totalHaber.add(haber);
            }
        }

        java.math.BigDecimal diferencia = totalDebe.subtract(totalHaber).abs();
        lblDebeTotal.setText("S/ " + totalDebe.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString());
        lblHaberTotal.setText("S/ " + totalHaber.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString());
        lblSaldoActual.setText("S/ " + diferencia.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString());
    }
}

package Vista;

import Clases.LibroMayor;
import Modelo.LibroMayorDAO;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class IFrmLibroMayor extends JInternalFrame {

    private JComboBox<String> cbCuentaContable;
    private JDateChooser txtFechaInicio;
    private JDateChooser txtFechaFin;
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
        super("Libro Mayor - Visualización de Asientos Contables", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        setSize(1000, 620);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    private void initComponents() {
        cbCuentaContable = new JComboBox<>(new String[]{
            "Todas las Cuentas",
            "Efectivo y Equivalentes (101)",
            "Mercaderías (201)",
            "Cuentas por Cobrar (121)",
            "Cuentas por Pagar (421)",
            "Capital (501)",
            "Ventas (701)",
            "Compras (601)",
            "Gastos Administrativos (941)",
            "IGV por Pagar (4011)"
        });
        txtFechaInicio = new JDateChooser();
        txtFechaInicio.setDateFormatString("dd/MM/yyyy");
        txtFechaInicio.setPreferredSize(new Dimension(130, 26));

        txtFechaFin = new JDateChooser();
        txtFechaFin.setDateFormatString("dd/MM/yyyy");
        txtFechaFin.setPreferredSize(new Dimension(130, 26));

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
        pnlResumen.add(new JLabel("Total Debe (Entradas):"), gbc);
        gbc.gridy = 3;
        pnlResumen.add(lblDebeTotal, gbc);

        gbc.gridy = 4;
        pnlResumen.add(new JLabel("Total Haber (Salidas):"), gbc);
        gbc.gridy = 5;
        pnlResumen.add(lblHaberTotal, gbc);

        JSeparator sep = new JSeparator();
        sep.setPreferredSize(new Dimension(0, 10));
        gbc.gridy = 6;
        pnlResumen.add(sep, gbc);

        gbc.gridy = 7;
        pnlResumen.add(new JLabel("Saldo Actual:"), gbc);
        gbc.gridy = 8;
        pnlResumen.add(lblSaldoActual, gbc);

        pnlCentral.add(pnlResumen, BorderLayout.EAST);
        add(pnlCentral, BorderLayout.CENTER);
    }

    private void attachEvents() {
        cargarDatos();
        btnFiltrar.addActionListener(e -> cargarDatos());
        btnRefrescar.addActionListener(e -> {
            txtFechaInicio.setDate(null);
            txtFechaFin.setDate(null);
            cbCuentaContable.setSelectedIndex(0);
            cargarDatos();
            JOptionPane.showMessageDialog(this, "Libro mayor actualizado.");
        });
        this.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
            @Override public void internalFrameActivated(javax.swing.event.InternalFrameEvent e) { cargarDatos(); }
        });
    }

    private void cargarDatos() {
        LibroMayorDAO dao = new LibroMayorDAO();
        List<LibroMayor> lista = dao.listarTodos();

        String cuentaSel = cbCuentaContable.getSelectedItem().toString();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fIni = txtFechaInicio.getDate() != null ? sdf.format(txtFechaInicio.getDate()) : "";
        String fFin = txtFechaFin.getDate() != null ? sdf.format(txtFechaFin.getDate()) : "";

        modelAsientos.setRowCount(0);
        java.math.BigDecimal totalDebe  = java.math.BigDecimal.ZERO;
        java.math.BigDecimal totalHaber = java.math.BigDecimal.ZERO;

        for (LibroMayor lm : lista) {
            boolean pasaFiltro = true;

            if (cbCuentaContable.getSelectedIndex() > 0) {
                if (!lm.getCuentaDebe().contains(cuentaSel) && !lm.getCuentaHaber().contains(cuentaSel)) {
                    pasaFiltro = false;
                }
            }

            try {
                if (!fIni.isEmpty() && !fFin.isEmpty()) {
                    String fv = lm.getFecha();
                    Date dIni = sdf.parse(fIni);
                    Date dFin = sdf.parse(fFin);
                    Date dVenta = sdf.parse(fv.length() >= 10 ? fv.substring(0, 10) : fv);
                    if (dVenta.before(dIni) || dVenta.after(dFin)) {
                        pasaFiltro = false;
                    }
                }
            } catch(Exception e) {}

            if (pasaFiltro) {
                java.math.BigDecimal debe  = lm.getMontoDebe()  != null ? lm.getMontoDebe()  : java.math.BigDecimal.ZERO;
                java.math.BigDecimal haber = lm.getMontoHaber() != null ? lm.getMontoHaber() : java.math.BigDecimal.ZERO;
                modelAsientos.addRow(new Object[]{
                    lm.getFecha(), lm.getGlosa(), lm.getCuentaDebe(), lm.getCuentaHaber(),
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
package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * IFrmReporteVentas - Visualización de ventas, filtros temporales y cálculo de ganancias.
 */
public class IFrmReporteVentas extends JInternalFrame {

    private JTable tblVentas;
    private DefaultTableModel modelVentas;

    // Filtros
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JComboBox<String> cbMes;
    private JButton btnBuscar;

    // Resumen
    private JLabel lblTotalVendido;
    private JLabel lblGananciaTotal;
    private JButton btnExportarPDF;

    private static final Color COLOR_PRIMARY = new Color(25, 118, 210);
    private static final Color COLOR_ACCENT = new Color(46, 125, 50);

    public IFrmReporteVentas() {
        super("Reporte de Ventas y Ganancias", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        setSize(850, 520);
    }

    private void initComponents() {
        // Filtros
        txtFechaInicio = new JTextField("DD/MM/AAAA", 10);
        txtFechaFin = new JTextField("DD/MM/AAAA", 10);
        
        cbMes = new JComboBox<>(new String[]{
            "Seleccionar Mes", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", 
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        });

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(COLOR_PRIMARY);
        btnBuscar.setForeground(Color.WHITE);

        // Tabla de Ventas
        String[] columns = {"ID Venta", "Cliente", "Total Venta", "Fecha", "Ganancia"};
        modelVentas = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblVentas = new JTable(modelVentas);

        // Resumen
        lblTotalVendido = new JLabel("S/ 0.00");
        lblTotalVendido.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotalVendido.setForeground(COLOR_PRIMARY);

        lblGananciaTotal = new JLabel("S/ 0.00");
        lblGananciaTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblGananciaTotal.setForeground(COLOR_ACCENT);

        btnExportarPDF = new JButton("📄 Exportar Reporte (PDF)");
        btnExportarPDF.setBackground(new Color(198, 40, 40));
        btnExportarPDF.setForeground(Color.WHITE);
        btnExportarPDF.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    private void buildLayout() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

        // ── Panel Superior: Filtros de Fecha ──
        JPanel pnlFiltros = new JPanel(new GridBagLayout());
        pnlFiltros.setBorder(BorderFactory.createTitledBorder("Filtros Temporales"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        pnlFiltros.add(new JLabel("Fecha Inicio:"), gbc);
        gbc.gridx = 1;
        pnlFiltros.add(txtFechaInicio, gbc);

        gbc.gridx = 2;
        pnlFiltros.add(new JLabel("Fecha Fin:"), gbc);
        gbc.gridx = 3;
        pnlFiltros.add(txtFechaFin, gbc);

        gbc.gridx = 4;
        pnlFiltros.add(new JLabel("Mes:"), gbc);
        gbc.gridx = 5;
        pnlFiltros.add(cbMes, gbc);

        gbc.gridx = 6;
        pnlFiltros.add(btnBuscar, gbc);

        // ── Panel Central: Tabla ──
        JPanel pnlCentral = new JPanel(new BorderLayout(10, 10));
        pnlCentral.add(new JScrollPane(tblVentas), BorderLayout.CENTER);

        // ── Panel Inferior: Totales y Ganancias ──
        JPanel pnlInferior = new JPanel(new GridBagLayout());
        pnlInferior.setBorder(BorderFactory.createTitledBorder("Resultados del Periodo"));
        GridBagConstraints gbcInf = new GridBagConstraints();
        gbcInf.insets = new Insets(10, 20, 10, 20);
        gbcInf.fill = GridBagConstraints.HORIZONTAL;

        gbcInf.gridx = 0; gbcInf.gridy = 0;
        pnlInferior.add(new JLabel("TOTAL FACTURADO:"), gbcInf);
        gbcInf.gridx = 1;
        pnlInferior.add(lblTotalVendido, gbcInf);

        gbcInf.gridx = 2;
        pnlInferior.add(new JLabel("NETO GANANCIAS:"), gbcInf);
        gbcInf.gridx = 3;
        pnlInferior.add(lblGananciaTotal, gbcInf);

        gbcInf.gridx = 4; gbcInf.weightx = 1.0;
        gbcInf.fill = GridBagConstraints.NONE;
        gbcInf.anchor = GridBagConstraints.EAST;
        pnlInferior.add(btnExportarPDF, gbcInf);

        add(pnlFiltros, BorderLayout.NORTH);
        add(pnlCentral, BorderLayout.CENTER);
        add(pnlInferior, BorderLayout.SOUTH);
    }

    private void attachEvents() {
        btnBuscar.addActionListener(e -> {
            // TODO: lógica TXT para leer ventas.txt, filtrar por fechas o mes y calcular sumatorias
        });

        btnExportarPDF.addActionListener(e -> {
            // TODO: lógica TXT / iTextPDF para exportar reporte formal a PDF
        });
    }
}

package Vista;

import Modelo.ReporteProductosDAO;
import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

/**
 * Vista de Reportes Avanzados — Ranking de Productos y Ganancias Reales.
 *
 * Historias de Usuario implementadas:
 *   - FR-006  Ranking de productos más vendidos (CA-1 a CA-5)
 *   - FR-018  Productos con stock bajo (CA-1 a CA-4)
 *   - FR-019-v2 Ganancias reales por período (CA-1 a CA-4)
 *
 * Restricción de rol (CA-5 FR-006): acceso solo para Gerente/Administrador.
 * Esta vista no se agrega al menú del Cajero — ver FrmDashboard.buildMenu().
 *
 * Creado: 2026-06-26T02:17:00-05:00
 */
public class IFrmReportesAvanzados extends JInternalFrame {

    // ── Filtros ──────────────────────────────────────────────────────────────
    private JComboBox<String>  cbMes;
    private JComboBox<Integer> cbAnio;
    private JButton            btnBuscar;

    // ── Tabs ─────────────────────────────────────────────────────────────────
    private JTabbedPane tabs;

    // ── Tab 1: Ranking más vendidos  ─────────────────────────────────
    private DefaultTableModel  modelRanking;
    private JLabel             lblTotalUnidades;
    private JLabel             lblTotalIngreso;

    // ── Tab 2: Stock Bajo  ───────────────────────────────────────────
    private DefaultTableModel  modelStockBajo;
    private JLabel             lblTotalBajo;

    // ── Tab 3: Ganancias (FR-019-v2) ─────────────────────────────────────────
    private DefaultTableModel  modelGanancias;
    private JLabel             lblTotIngreso;
    private JLabel             lblTotCosto;
    private JLabel             lblTotGanancia;

    private final ReporteProductosDAO dao = new ReporteProductosDAO();

    public IFrmReportesAvanzados() {
        super("Reportes Avanzados — Inteligencia de Negocio", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        setSize(1100, 680);
    }

    // ── initComponents ───────────────────────────────────────────────────────

    private void initComponents() {
        // Filtros de período
        String[] meses = {"Todos los meses", "Enero", "Febrero", "Marzo", "Abril",
                          "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre",
                          "Noviembre", "Diciembre"};
        cbMes  = new JComboBox<>(meses);
        cbMes.setFont(UIKit.BODY);

        int anioActual = Calendar.getInstance().get(Calendar.YEAR);
        cbAnio = new JComboBox<>(new Integer[]{0, anioActual - 1, anioActual, anioActual + 1});
        cbAnio.setSelectedItem(anioActual);
        cbAnio.setFont(UIKit.BODY);

        btnBuscar = UIKit.primaryButton("Generar Reportes");

        // Tab 1 — Ranking
        modelRanking = new DefaultTableModel(
            new String[]{"#", "Producto", "Unidades Vendidas", "Ingreso Total (S/)"},
            0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        lblTotalUnidades = kpiLabel("— uds");
        lblTotalIngreso  = kpiLabel("S/ —");

        // Tab 2 — Stock Bajo
        modelStockBajo = new DefaultTableModel(
            new String[]{"ID", "Producto", "Stock Actual", "Stock Mínimo", "Faltante"},
            0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        lblTotalBajo = kpiLabel("0 productos");

        // Tab 3 — Ganancias
        modelGanancias = new DefaultTableModel(
            new String[]{"Producto", "Cant. Vendida", "Ingreso (S/)", "Costo (S/)",
                         "Ganancia (S/)", "Margen (%)", "⚠"},
            0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        lblTotIngreso  = kpiLabel("S/ —");
        lblTotCosto    = kpiLabel("S/ —");
        lblTotGanancia = kpiLabel("S/ —");
    }

    // ── buildLayout ──────────────────────────────────────────────────────────

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(
                new EmptyBorder(UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        // Header
        getContentPane().add(
                UIKit.screenHeader("Reportes Avanzados", "Reportes  >  Inteligencia de Negocio"),
                BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(0, UIKit.SPACE_MD));
        cuerpo.setOpaque(false);

        // Barra de filtros
        JPanel pnlFiltros = UIKit.card();
        pnlFiltros.setLayout(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_SM, UIKit.SPACE_SM));
        pnlFiltros.add(new JLabel("Mes:"));
        pnlFiltros.add(cbMes);
        pnlFiltros.add(new JLabel("Año:"));
        pnlFiltros.add(cbAnio);
        pnlFiltros.add(btnBuscar);
        cuerpo.add(pnlFiltros, BorderLayout.NORTH);

        // Tabs con los 3 reportes
        tabs = new JTabbedPane();
        tabs.setFont(UIKit.BODY_BOLD);
        tabs.setBackground(UIKit.BG_CARD);
        tabs.addTab("📦 Más Vendidos ", buildTabRanking());
        tabs.addTab("⚠ Stock Bajo ",    buildTabStockBajo());
        tabs.addTab("💰 Ganancias ",     buildTabGanancias());
        cuerpo.add(tabs, BorderLayout.CENTER);

        getContentPane().add(cuerpo, BorderLayout.CENTER);
    }

    private JPanel buildTabRanking() {
        JPanel p = new JPanel(new BorderLayout(0, UIKit.SPACE_SM));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(UIKit.SPACE_SM, 0, 0, 0));

        // KPI row
        JPanel kpiRow = new JPanel(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_MD, 0));
        kpiRow.setOpaque(false);
        kpiRow.add(kpiCard("Total Unidades", lblTotalUnidades));
        kpiRow.add(kpiCard("Ingreso Generado", lblTotalIngreso));
        p.add(kpiRow, BorderLayout.NORTH);

        JPanel pnlTabla = UIKit.card();
        pnlTabla.setLayout(new BorderLayout());
        JScrollPane scroll = new JScrollPane(UIKit.styledTable(modelRanking));
        scroll.setBorder(BorderFactory.createEmptyBorder());
        pnlTabla.add(scroll, BorderLayout.CENTER);
        p.add(pnlTabla, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildTabStockBajo() {
        JPanel p = new JPanel(new BorderLayout(0, UIKit.SPACE_SM));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(UIKit.SPACE_SM, 0, 0, 0));

        JPanel kpiRow = new JPanel(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_MD, 0));
        kpiRow.setOpaque(false);
        kpiRow.add(kpiCard("Productos bajo mínimo", lblTotalBajo));
        p.add(kpiRow, BorderLayout.NORTH);

        JPanel pnlTabla = UIKit.card();
        pnlTabla.setLayout(new BorderLayout());
        JScrollPane scroll = new JScrollPane(UIKit.styledTable(modelStockBajo));
        scroll.setBorder(BorderFactory.createEmptyBorder());
        pnlTabla.add(scroll, BorderLayout.CENTER);
        p.add(pnlTabla, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildTabGanancias() {
        JPanel p = new JPanel(new BorderLayout(0, UIKit.SPACE_SM));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(UIKit.SPACE_SM, 0, 0, 0));

        // KPI row con los 3 totales
        JPanel kpiRow = new JPanel(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_MD, 0));
        kpiRow.setOpaque(false);
        kpiRow.add(kpiCard("Ingreso Bruto", lblTotIngreso));
        kpiRow.add(kpiCard("Costo Real", lblTotCosto));
        kpiRow.add(kpiCard("Ganancia Bruta", lblTotGanancia));
        p.add(kpiRow, BorderLayout.NORTH);

        JPanel pnlTabla = UIKit.card();
        pnlTabla.setLayout(new BorderLayout());
        JScrollPane scroll = new JScrollPane(UIKit.styledTable(modelGanancias));
        scroll.setBorder(BorderFactory.createEmptyBorder());
        pnlTabla.add(scroll, BorderLayout.CENTER);
        p.add(pnlTabla, BorderLayout.CENTER);
        return p;
    }

    // ── attachEvents ─────────────────────────────────────────────────────────

    private void attachEvents() {
        btnBuscar.addActionListener(e -> cargarTodosLosReportes());
        cargarTodosLosReportes(); // carga inicial
    }

    private void cargarTodosLosReportes() {
        int mes  = cbMes.getSelectedIndex();  // 0 = todos
        Object anioObj = cbAnio.getSelectedItem();
        int anio = (anioObj instanceof Integer && (Integer) anioObj != 0) ? (Integer) anioObj : 0;

        cargarRanking(mes, anio);
        cargarStockBajo();
        cargarGanancias(mes, anio);

        // FR-020-v2: registrar acceso a reportes en bitácora
        Utils.BitacoraService.registrar(
            Clases.Sesion.getUsuario(),
            Utils.BitacoraService.MOD_REPORTES,
            "VER_REPORTE_AVANZADO",
            Utils.BitacoraService.OK,
            "Mes=" + mes + " Año=" + anio
        );
    }

    // ── FR-006: Ranking ───────────────────────────────────────────────────────

    private void cargarRanking(int mesNum, int anio) {
        List<Object[]> filas = dao.rankingProductosMasVendidos(10, mesNum, anio);
        modelRanking.setRowCount(0);

        if (filas.isEmpty()) {
            modelRanking.addRow(new Object[]{"—", "Sin ventas registradas para el período", "", ""});
            lblTotalUnidades.setText("0 uds");
            lblTotalIngreso.setText("S/ 0.00");
            return;
        }

        int totalUds = 0;
        BigDecimal totalIng = BigDecimal.ZERO;
        int rank = 1;
        for (Object[] f : filas) {
            int uds = (Integer) f[2];
            BigDecimal ing = (BigDecimal) f[3];
            modelRanking.addRow(new Object[]{rank++, f[1], uds, "S/ " + ing.toPlainString()});
            totalUds += uds;
            totalIng = totalIng.add(ing);
        }
        lblTotalUnidades.setText(totalUds + " uds");
        lblTotalIngreso.setText("S/ " + totalIng.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString());
    }

    // ── FR-018: Stock Bajo ────────────────────────────────────────────────────

    private void cargarStockBajo() {
        List<Object[]> filas = dao.productosConStockBajo();
        modelStockBajo.setRowCount(0);

        if (filas.isEmpty()) {
            // CA-2: todos los productos tienen stock suficiente
            modelStockBajo.addRow(new Object[]{"—", "✅ Todos los productos tienen stock suficiente", "", "", ""});
            lblTotalBajo.setText("0 productos");
            return;
        }

        for (Object[] f : filas) {
            modelStockBajo.addRow(new Object[]{f[0], f[1], f[2], f[3], f[4]});
        }
        lblTotalBajo.setText(filas.size() + " productos");
    }

    // ── FR-019-v2: Ganancias ──────────────────────────────────────────────────

    private void cargarGanancias(int mesNum, int anio) {
        List<Object[]> filas = dao.gananciasPorProducto(mesNum, anio);
        modelGanancias.setRowCount(0);

        if (filas.isEmpty()) {
            modelGanancias.addRow(new Object[]{"Sin movimientos para el período seleccionado",
                                               "", "", "", "", "", ""});
            lblTotIngreso.setText("S/ 0.00");
            lblTotCosto.setText("S/ 0.00");
            lblTotGanancia.setText("S/ 0.00");
            return;
        }

        for (Object[] f : filas) {
            boolean sinCosto = (Boolean) f[6];
            modelGanancias.addRow(new Object[]{
                f[0],                                       // nombre
                f[1],                                       // cantVendida
                "S/ " + ((BigDecimal)f[2]).toPlainString(), // ingreso
                "S/ " + ((BigDecimal)f[3]).toPlainString(), // costo
                "S/ " + ((BigDecimal)f[4]).toPlainString(), // ganancia
                ((BigDecimal)f[5]).toPlainString() + " %",  // margen
                sinCosto ? "⚠ Sin compra" : "✅"            // advertencia I-1
            });
        }

        // Totales
        BigDecimal[] totales = dao.totalesGanancias(filas);
        lblTotIngreso.setText("S/ " + totales[0].setScale(2, java.math.RoundingMode.HALF_UP).toPlainString());
        lblTotCosto.setText("S/ " + totales[1].setScale(2, java.math.RoundingMode.HALF_UP).toPlainString());
        lblTotGanancia.setText("S/ " + totales[2].setScale(2, java.math.RoundingMode.HALF_UP).toPlainString());
    }

    // ── Helpers de UI ────────────────────────────────────────────────────────

    private JLabel kpiLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(UIKit.KPI_VALUE);
        lbl.setForeground(UIKit.PRIMARY);
        return lbl;
    }

    private JPanel kpiCard(String titulo, JLabel valorLabel) {
        JPanel card = UIKit.card();
        card.setLayout(new BorderLayout(0, UIKit.SPACE_XS));
        card.setPreferredSize(new Dimension(220, 72));
        card.setBorder(new EmptyBorder(UIKit.SPACE_SM, UIKit.SPACE_MD, UIKit.SPACE_SM, UIKit.SPACE_MD));

        JLabel lblTitulo = new JLabel(titulo.toUpperCase());
        lblTitulo.setFont(UIKit.LABEL);
        lblTitulo.setForeground(UIKit.TEXT_SECONDARY);
        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(valorLabel, BorderLayout.CENTER);
        return card;
    }
}

package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * IFrmReporteInventario - Reporte de Inventario que muestra alertas de stock bajo y productos más vendidos.
 */
public class IFrmReporteInventario extends JInternalFrame {

    private JTable tblStockBajo;
    private DefaultTableModel modelStockBajo;

    private JTable tblMasVendidos;
    private DefaultTableModel modelMasVendidos;

    private JButton btnActualizar;
    private JButton btnExportarPDF;

    private static final Color COLOR_PRIMARY = new Color(25, 118, 210);
    private static final Color COLOR_ROJO = new Color(198, 40, 40);

    public IFrmReporteInventario() {
        super("Reporte de Inventario y Stock", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        setSize(880, 520);
    }

    private void initComponents() {
        // Tabla de Stock Bajo
        String[] columnsStock = {"ID Producto", "Descripción", "Categoría", "Stock Actual", "Mínimo Requerido"};
        modelStockBajo = new DefaultTableModel(columnsStock, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblStockBajo = new JTable(modelStockBajo);

        // Tabla de Más Vendidos
        String[] columnsVentas = {"Posición", "Producto", "Cantidad Vendida", "Total Ingresos"};
        modelMasVendidos = new DefaultTableModel(columnsVentas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblMasVendidos = new JTable(modelMasVendidos);

        // Botones
        btnActualizar = new JButton("🔄 Actualizar Datos");
        btnActualizar.setBackground(COLOR_PRIMARY);
        btnActualizar.setForeground(Color.WHITE);

        btnExportarPDF = new JButton("📂 Exportar Inventario (PDF)");
        btnExportarPDF.setBackground(new Color(46, 125, 50));
        btnExportarPDF.setForeground(Color.WHITE);
    }

    private void buildLayout() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

        // ── Panel Central: Dos tablas lado a lado ──
        JPanel pnlTablas = new JPanel(new GridLayout(1, 2, 15, 0));

        // Panel de Stock Bajo
        JPanel pnlStockBajo = new JPanel(new BorderLayout(10, 10));
        pnlStockBajo.setBorder(BorderFactory.createTitledBorder("⚠️ Alertas de Reposición (Stock Bajo)"));
        pnlStockBajo.add(new JScrollPane(tblStockBajo), BorderLayout.CENTER);

        // Panel de Más Vendidos
        JPanel pnlMasVendidos = new JPanel(new BorderLayout(10, 10));
        pnlMasVendidos.setBorder(BorderFactory.createTitledBorder("🏆 Productos Más Vendidos"));
        pnlMasVendidos.add(new JScrollPane(tblMasVendidos), BorderLayout.CENTER);

        pnlTablas.add(pnlStockBajo);
        pnlTablas.add(pnlMasVendidos);

        // ── Panel Inferior: Botones de Acción ──
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        pnlBotones.add(btnActualizar);
        pnlBotones.add(btnExportarPDF);

        add(pnlTablas, BorderLayout.CENTER);
        add(pnlBotones, BorderLayout.SOUTH);
    }

    private void attachEvents() {
        btnActualizar.addActionListener(e -> {
            // TODO: lógica TXT para leer productos.txt y ventas.txt para procesar el stock crítico y ventas totales
        });

        btnExportarPDF.addActionListener(e -> {
            // TODO: lógica TXT / iTextPDF para exportar reporte de inventario a PDF
        });
    }
}

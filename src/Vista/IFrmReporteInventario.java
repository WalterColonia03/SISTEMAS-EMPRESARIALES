package Vista;

import Clases.Producto;
import Modelo.ConexionDB;
import Modelo.ProductoDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

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
        cargarDatos();
        setSize(880, 520);
    }

    private void initComponents() {
        String[] columnsStock = {"ID Producto", "Descripción", "Stock Actual"};
        modelStockBajo = new DefaultTableModel(columnsStock, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblStockBajo = new JTable(modelStockBajo);

        String[] columnsVentas = {"Posición", "Producto", "Cantidad Vendida"};
        modelMasVendidos = new DefaultTableModel(columnsVentas, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblMasVendidos = new JTable(modelMasVendidos);

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

        JPanel pnlTablas = new JPanel(new GridLayout(1, 2, 15, 0));

        JPanel pnlStockBajo = new JPanel(new BorderLayout(10, 10));
        pnlStockBajo.setBorder(BorderFactory.createTitledBorder("⚠️ Alertas de Reposición (Stock Menor a 15)"));
        pnlStockBajo.add(new JScrollPane(tblStockBajo), BorderLayout.CENTER);

        JPanel pnlMasVendidos = new JPanel(new BorderLayout(10, 10));
        pnlMasVendidos.setBorder(BorderFactory.createTitledBorder("🏆 Productos Más Vendidos Histórico"));
        pnlMasVendidos.add(new JScrollPane(tblMasVendidos), BorderLayout.CENTER);

        pnlTablas.add(pnlStockBajo);
        pnlTablas.add(pnlMasVendidos);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        pnlBotones.add(btnActualizar);
        pnlBotones.add(btnExportarPDF);

        add(pnlTablas, BorderLayout.CENTER);
        add(pnlBotones, BorderLayout.SOUTH);
    }
    
    private void cargarDatos() {
        // Stock Bajo
        modelStockBajo.setRowCount(0);
        ProductoDAO pdao = new ProductoDAO();
        for (Producto p : pdao.listarTodos()) {
            if (p.getCantidad() < 15) {
                modelStockBajo.addRow(new Object[]{ p.getIdProducto(), p.getNombre(), p.getCantidad() });
            }
        }
        
        // Más vendidos
        modelMasVendidos.setRowCount(0);
        String sql = "SELECT idProducto, SUM(cantidad) as total_vendido FROM tb_detalle_venta GROUP BY idProducto ORDER BY total_vendido DESC LIMIT 10";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            int pos = 1;
            while (rs.next()) {
                int idProd = rs.getInt("idProducto");
                int qty = rs.getInt("total_vendido");
                Producto p = null;
                for (Producto prod : pdao.listarTodos()) {
                    if (prod.getIdProducto() == idProd) { p = prod; break; }
                }
                String nombre = (p != null) ? p.getNombre() : "Producto Eliminado";
                modelMasVendidos.addRow(new Object[]{ pos++, nombre, qty });
            }
        } catch (Exception e) {}
    }

    private void attachEvents() {
        btnActualizar.addActionListener(e -> cargarDatos());

        btnExportarPDF.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "El módulo de exportación PDF requiere la librería iTextPDF instalada.");
        });
    }
}
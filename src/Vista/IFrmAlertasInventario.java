package Vista;

import Clases.Producto;
import Modelo.ProductoDAO;
import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.awt.*;

public class IFrmAlertasInventario extends JInternalFrame {

    private JTable tblPorVencer;
    private DefaultTableModel modelPorVencer;
    private JTable tblSinRotacion;
    private DefaultTableModel modelSinRotacion;
    private JTable tblStockBajo;
    private DefaultTableModel modelStockBajo;

    private JButton btnRefrescar;

    public IFrmAlertasInventario() {
        super("Alertas de Inventario", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        setSize(950, 600);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    private void initComponents() {
        btnRefrescar = UIKit.secondaryButton("Refrescar alertas");

        String[] colsVencer = {"Producto", "Lote", "Vencimiento", "Días Rest.", "Stock"};
        modelPorVencer = new DefaultTableModel(colsVencer, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblPorVencer = UIKit.styledTable(modelPorVencer);

        // Renderizado condicional para Días Restantes
        tblPorVencer.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    try {
                        int dias = Integer.parseInt(value.toString());
                        if (dias < 10) {
                            c.setBackground(UIKit.DANGER);
                            c.setForeground(Color.WHITE);
                        } else if (dias < 30) {
                            c.setBackground(UIKit.WARNING);
                            c.setForeground(Color.WHITE);
                        } else {
                            c.setBackground(row % 2 == 0 ? UIKit.BG_CARD : getBackground());
                            c.setForeground(UIKit.TEXT_SECONDARY);
                        }
                    } catch (NumberFormatException ignored) {}
                }
                return c;
            }
        });

        String[] colsSinRot = {"Producto", "Stock Actual", "Ingresos (30d)", "Salidas (30d)", "Días Inactivo"};
        modelSinRotacion = new DefaultTableModel(colsSinRot, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblSinRotacion = UIKit.styledTable(modelSinRotacion);

        String[] colsStock = {"Producto", "Stock Actual", "Stock Mínimo", "Déficit"};
        modelStockBajo = new DefaultTableModel(colsStock, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblStockBajo = UIKit.styledTable(modelStockBajo);

        // Renderizado condicional para Déficit
        tblStockBajo.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value != null && value.toString().startsWith("-")) {
                    JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                    pnl.setOpaque(true);
                    pnl.setBackground(isSelected ? table.getSelectionBackground() : (row % 2 == 0 ? UIKit.BG_CARD : table.getBackground()));
                    JLabel badge = UIKit.statusBadge(value.toString(), UIKit.DANGER);
                    pnl.add(badge);
                    return pnl;
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });

        cargarAlertas();
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(
                UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        // ===== Encabezado =====
        getContentPane().add(
                UIKit.screenHeader("Alertas de Inventario", "Inventario  >  Alertas de Inventario"),
                BorderLayout.NORTH);

        JPanel cuerpo = UIKit.card();
        cuerpo.setLayout(new BorderLayout(0, UIKit.SPACE_MD));

        cuerpo.add(UIKit.sectionHeader("Monitor de Alertas", btnRefrescar), BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UIKit.BODY);

        JPanel pnlVencer = new JPanel(new BorderLayout(0, 0));
        pnlVencer.setOpaque(false);
        JScrollPane scrollVencer = new JScrollPane(tblPorVencer);
        scrollVencer.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        pnlVencer.add(scrollVencer, BorderLayout.CENTER);
        tabbedPane.addTab("Próximos a Vencer", pnlVencer);

        JPanel pnlSinRot = new JPanel(new BorderLayout(0, 0));
        pnlSinRot.setOpaque(false);
        JScrollPane scrollSinRot = new JScrollPane(tblSinRotacion);
        scrollSinRot.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        pnlSinRot.add(scrollSinRot, BorderLayout.CENTER);
        tabbedPane.addTab("Sin Rotación", pnlSinRot);

        JPanel pnlStockBajo = new JPanel(new BorderLayout(0, 0));
        pnlStockBajo.setOpaque(false);
        JScrollPane scrollStockBajo = new JScrollPane(tblStockBajo);
        scrollStockBajo.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        pnlStockBajo.add(scrollStockBajo, BorderLayout.CENTER);
        tabbedPane.addTab("Stock Bajo", pnlStockBajo);

        cuerpo.add(tabbedPane, BorderLayout.CENTER);

        getContentPane().add(cuerpo, BorderLayout.CENTER);
    }

    private void attachEvents() {
        btnRefrescar.addActionListener(e -> {
            cargarAlertas();
            JOptionPane.showMessageDialog(this, "Alertas actualizadas desde la base de datos.");
        });
    }

    private void cargarAlertas() {
        ProductoDAO pdao = new ProductoDAO();
        List<Producto> lista = pdao.listarTodos();
        
        modelStockBajo.setRowCount(0);
        modelSinRotacion.setRowCount(0);
        modelPorVencer.setRowCount(0);
        
        // 1. Alertas de Stock Bajo
        int stockMinimo = 10; // Valor fijo de stock mínimo por ahora
        for (Producto p : lista) {
            if (p.getCantidad() < stockMinimo) {
                int deficit = p.getCantidad() - stockMinimo;
                modelStockBajo.addRow(new Object[]{
                    p.getNombre(),
                    p.getCantidad(),
                    stockMinimo,
                    String.valueOf(deficit)
                });
            }
        }
        
        // 2. Alertas Sin Rotación (FR-052)
        Modelo.ReporteProductosDAO reportesDAO = new Modelo.ReporteProductosDAO();
        List<Object[]> sinRotacion = reportesDAO.productosSinRotacion();
        for(Object[] row : sinRotacion) {
            modelSinRotacion.addRow(row);
        }
        
        // 3. Alertas Próximos a Vencer (FR-052)
        List<Object[]> porVencer = reportesDAO.productosProximosAVencer();
        for(Object[] row : porVencer) {
            modelPorVencer.addRow(row);
        }
    }
}

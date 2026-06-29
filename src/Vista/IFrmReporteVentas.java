package Vista;

import Clases.Venta;
import Clases.Sesion;
import Modelo.VentaDAO;
import Utils.LoggerGlobal;
import Vista.Estilos.UIKit;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class IFrmReporteVentas extends JInternalFrame {

    private JTable             tblVentas;
    private DefaultTableModel  modelVentas;
    private JDateChooser       txtFechaInicio;
    private JDateChooser       txtFechaFin;
    private JComboBox<String>  cbMetodoPago;
    private JButton            btnBuscar;
    private JButton            btnLimpiar;
    private JLabel             lblTotalVendido;
    private JButton            btnExportarExcel;

    public IFrmReporteVentas() {
        super("Historial de Ventas", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        buscarVentas();
        setSize(1000, 600);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    private void initComponents() {
        txtFechaInicio = new JDateChooser();
        txtFechaInicio.setPreferredSize(new Dimension(140, 36));
        txtFechaInicio.setDateFormatString("dd/MM/yyyy");

        txtFechaFin = new JDateChooser();
        txtFechaFin.setPreferredSize(new Dimension(140, 36));
        txtFechaFin.setDateFormatString("dd/MM/yyyy");

        cbMetodoPago = new JComboBox<>(new String[]{"Todos", "Efectivo"});
        cbMetodoPago.setPreferredSize(new Dimension(150, 36));
        cbMetodoPago.setFont(UIKit.BODY);

        btnBuscar = UIKit.primaryButton("🔍 Filtrar");
        btnLimpiar = UIKit.secondaryButton("✕ Limpiar");

        String[] columns = {"N°", "Fecha / Hora", "Cliente / Cajero", "Método", "Monto", "Acción"};
        modelVentas = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblVentas = UIKit.styledTable(modelVentas);
        
        // --- Mejoras pedidas por el usuario ---
        tblVentas.setAutoCreateRowSorter(true); // Permite ordenar haciendo clic en encabezados

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        for (int i = 0; i < tblVentas.getColumnCount(); i++) {
            tblVentas.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // --- Renderizador de la columna Acción para parecer enlace ---
        tblVentas.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                setForeground(UIKit.ACCENT);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                return c;
            }
        });
        
        lblTotalVendido = new JLabel("S/ 0.00", SwingConstants.RIGHT);
        lblTotalVendido.setFont(UIKit.H1);
        lblTotalVendido.setForeground(UIKit.TEXT_PRIMARY);

        btnExportarExcel = UIKit.primaryButton("⬇ Exportar PDF");
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        // Encabezado con patrón referencia (breadcrumb + título + acción)
        getContentPane().add(UIKit.screenHeader("Historial de Ventas",
            "Inicio / Ventas / Historial", btnExportarExcel), BorderLayout.NORTH);

        JPanel pnlFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlFiltros.setOpaque(false);
        pnlFiltros.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        JPanel grpDesde = new JPanel(new BorderLayout(0, 5)); grpDesde.setOpaque(false);
        grpDesde.add(UIKit.fieldLabel("Desde"), BorderLayout.NORTH);
        grpDesde.add(txtFechaInicio, BorderLayout.CENTER);
        
        JPanel grpHasta = new JPanel(new BorderLayout(0, 5)); grpHasta.setOpaque(false);
        grpHasta.add(UIKit.fieldLabel("Hasta"), BorderLayout.NORTH);
        grpHasta.add(txtFechaFin, BorderLayout.CENTER);
        
        JPanel grpMetodo = new JPanel(new BorderLayout(0, 5)); grpMetodo.setOpaque(false);
        grpMetodo.add(UIKit.fieldLabel("Método de pago"), BorderLayout.NORTH);
        grpMetodo.add(cbMetodoPago, BorderLayout.CENTER);
        
        JPanel grpBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        grpBotones.setOpaque(false);
        grpBotones.setBorder(new EmptyBorder(20, 0, 0, 0)); 
        grpBotones.add(btnBuscar);
        grpBotones.add(btnLimpiar);

        pnlFiltros.add(grpDesde);
        pnlFiltros.add(grpHasta);
        pnlFiltros.add(grpMetodo);
        pnlFiltros.add(grpBotones);

        JPanel pnlCentro = new JPanel(new BorderLayout(0, UIKit.SPACE_SM));
        pnlCentro.setOpaque(false);
        pnlCentro.add(pnlFiltros, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(tblVentas);
        scroll.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        scroll.getViewport().setBackground(UIKit.BG_CARD);
        pnlCentro.add(scroll, BorderLayout.CENTER);

        JPanel pnlInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        pnlInferior.setOpaque(false);
        JLabel t1 = new JLabel("TOTAL MOSTRADO:");
        t1.setFont(UIKit.BODY_BOLD);
        t1.setForeground(UIKit.TEXT_SECONDARY);
        pnlInferior.add(t1);
        pnlInferior.add(lblTotalVendido);

        getContentPane().add(pnlCentro, BorderLayout.CENTER);
        getContentPane().add(pnlInferior, BorderLayout.SOUTH);
    }

    private void attachEvents() {
        btnBuscar.addActionListener(e -> buscarVentas());
        btnLimpiar.addActionListener(e -> {
            txtFechaInicio.setDate(null);
            txtFechaFin.setDate(null);
            cbMetodoPago.setSelectedIndex(0);
            buscarVentas();
        });
        btnExportarExcel.addActionListener(e -> exportarPDF());
        
        this.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                buscarVentas();
            }
        });
        
        // --- Evento de Click en la tabla para "Detalle" ---
        tblVentas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tblVentas.rowAtPoint(e.getPoint());
                int col = tblVentas.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 5) { // Columna "Acción"
                    String idVentaStr = tblVentas.getValueAt(row, 0).toString().replace("#", "");
                    String fecha = tblVentas.getValueAt(row, 1).toString();
                    String cliente = tblVentas.getValueAt(row, 2).toString();
                    String metodo = tblVentas.getValueAt(row, 3).toString();
                    String monto = tblVentas.getValueAt(row, 4).toString();
                    
                    int idVenta = Integer.parseInt(idVentaStr);
                    mostrarDetalleVenta(idVenta, idVentaStr, fecha, cliente, metodo, monto);
                }
            }
        });
    }

    private void mostrarDetalleVenta(int idVenta, String idVentaStr, String fecha, String cliente, String metodo, String totalMonto) {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Venta #" + idVentaStr, true);
        dlg.setSize(500, 600);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());
        dlg.getContentPane().setBackground(Color.WHITE);

        JPanel pnlHeader = new JPanel(new GridLayout(3, 2, 10, 10));
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        pnlHeader.add(new JLabel("Fecha: " + fecha));
        pnlHeader.add(new JLabel("Cliente: " + cliente));
        pnlHeader.add(new JLabel("Método de Pago: " + metodo));
        pnlHeader.add(new JLabel("Monto Total: " + totalMonto));
        
        dlg.add(pnlHeader, BorderLayout.NORTH);

        String[] cols = {"Cant", "Producto", "P. Unit", "Subtotal"};
        DefaultTableModel modDetalle = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tblDetalle = UIKit.styledTable(modDetalle);
        tblDetalle.setShowGrid(true);
        tblDetalle.setGridColor(new Color(240, 240, 240));

        VentaDAO dao = new VentaDAO();
        List<Object[]> detalles = dao.obtenerDetallesVenta(idVenta);
        for (Object[] d : detalles) {
            int cant = (Integer) d[0];
            String prod = d[1].toString() + " - " + (d[2] != null ? d[2].toString() : "");
            BigDecimal pUnit = (BigDecimal) d[3];
            BigDecimal subtotal = (BigDecimal) d[4];
            
            modDetalle.addRow(new Object[]{
                cant, 
                prod, 
                "S/ " + pUnit.setScale(2, RoundingMode.HALF_UP).toPlainString(), 
                "S/ " + subtotal.setScale(2, RoundingMode.HALF_UP).toPlainString()
            });
        }

        JScrollPane scroll = new JScrollPane(tblDetalle);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        scroll.getViewport().setBackground(Color.WHITE);
        dlg.add(scroll, BorderLayout.CENTER);

        JPanel pnlBotones = new JPanel();
        pnlBotones.setBackground(Color.WHITE);
        JButton btnCerrar = UIKit.secondaryButton("Cerrar");
        btnCerrar.addActionListener(e -> dlg.dispose());
        pnlBotones.add(btnCerrar);
        dlg.add(pnlBotones, BorderLayout.SOUTH);

        dlg.setVisible(true);
    }

    private void buscarVentas() {
        VentaDAO dao = new VentaDAO();
        List<Venta> lista = dao.listarTodos();
        
        // --- Ordenar la lista para mostrar del mas reciente al mas antiguo ---
        Collections.reverse(lista);

        modelVentas.setRowCount(0);
        BigDecimal totalVendido = BigDecimal.ZERO;
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fIni = txtFechaInicio.getDate() != null ? sdf.format(txtFechaInicio.getDate()) : "";
        String fFin = txtFechaFin.getDate() != null ? sdf.format(txtFechaFin.getDate()) : "";

        for (Venta v : lista) {
            String fechaVenta = v.getFecha(); 
            String soloFecha = fechaVenta.length() >= 10 ? fechaVenta.substring(0, 10) : fechaVenta;
            
            boolean pasaFiltro = true;
            try {
                if (!fIni.isEmpty() && !fFin.isEmpty()) {
                    Date dIni = sdf.parse(fIni);
                    Date dFin = sdf.parse(fFin);
                    Date dVenta = sdf.parse(soloFecha);
                    if (dVenta.before(dIni) || dVenta.after(dFin)) {
                        pasaFiltro = false;
                    }
                }
            } catch(Exception e) {}
            
            if (!pasaFiltro) continue;

            String metodo = v.getMetodoPago() != null ? v.getMetodoPago() : "Efectivo";
            
            String metodoFiltro = cbMetodoPago.getSelectedItem().toString();
            if (!metodoFiltro.equals("Todos") && !metodoFiltro.equals(metodo)) continue;

            totalVendido = totalVendido.add(v.getTotal());
            
            String idStr = String.format("#%06d", v.getIdVenta());
            modelVentas.addRow(new Object[]{
                idStr,
                v.getFecha(),
                v.getCliente() != null && !v.getCliente().isEmpty() ? v.getCliente() : "Consumidor Final",
                metodo,
                "S/. " + v.getTotal().setScale(2, RoundingMode.HALF_UP).toPlainString(),
                "👁 Detalle"
            });
        }

        String totalStr = "S/. " + totalVendido.setScale(2, RoundingMode.HALF_UP).toPlainString();
        lblTotalVendido.setText(totalStr);
    }

    private void exportarPDF() {
        if (tblVentas.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay ventas para exportar.");
            return;
        }
        try {
            String fileName = "Historial_Ventas_" + System.currentTimeMillis() + ".pdf";
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(fileName));
            doc.open();

            com.itextpdf.text.Font titleFont  = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);

            Paragraph titulo = new Paragraph("HISTORIAL DE VENTAS", titleFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            doc.add(titulo);
            doc.add(new Paragraph(" "));

            PdfPTable tabla = new PdfPTable(5);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{1.5f, 3f, 3f, 2f, 2.5f});

            String[] headers = {"N°", "Fecha / Hora", "Cliente", "Método", "Monto"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setBackgroundColor(new BaseColor(30, 160, 100)); // Verde excel
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(6);
                com.itextpdf.text.Font whiteFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
                whiteFont.setColor(BaseColor.WHITE);
                cell.setPhrase(new Phrase(h, whiteFont));
                tabla.addCell(cell);
            }

            for (int i = 0; i < tblVentas.getRowCount(); i++) {
                tabla.addCell(String.valueOf(tblVentas.getValueAt(i, 0))); // ID
                tabla.addCell(String.valueOf(tblVentas.getValueAt(i, 1))); // Fecha
                tabla.addCell(String.valueOf(tblVentas.getValueAt(i, 2))); // Cliente
                tabla.addCell(String.valueOf(tblVentas.getValueAt(i, 3))); // Método
                tabla.addCell(String.valueOf(tblVentas.getValueAt(i, 4))); // Monto
            }
            doc.add(tabla);
            doc.add(new Paragraph(" "));

            com.itextpdf.text.Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Paragraph totalParagraph = new Paragraph("TOTAL MOSTRADO: " + lblTotalVendido.getText(), boldFont);
            totalParagraph.setAlignment(Element.ALIGN_RIGHT);
            doc.add(totalParagraph);

            doc.close();
            JOptionPane.showMessageDialog(this, "Reporte exportado exitosamente.");
            File f = new File(fileName);
            if (f.exists()) Desktop.getDesktop().open(f);

        } catch (Exception ex) {
            LoggerGlobal.error("IFrmReporteVentas.exportarPDF() falló", ex);
            JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage());
        }
    }
}
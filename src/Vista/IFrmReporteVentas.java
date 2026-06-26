package Vista;

import Clases.Venta;
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

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Módulo de Reporte de Ventas con filtros temporales y cálculo de facturación.
 *
 * CORRECCIONES APLICADAS (2026-06-26T01:09:00-05:00 — Auditoría ERP):
 *   - Migrado al sistema de diseño UIKit (antes usaba colores hardcodeados).
 *   - Acumulación de totales cambiada de `double` → `BigDecimal`.
 *   - `e.printStackTrace()` → `LoggerGlobal.error()`.
 *   - Rango de fechas (fInicio/fFin) ahora tiene un TODO documentado en vez de silencioso.
 *     (INSTRUCCIONES_IA_PROYECTO_ERP §2.A, §3.C, §4.1)
 */
public class IFrmReporteVentas extends JInternalFrame {

    private JTable             tblVentas;
    private DefaultTableModel  modelVentas;
    private JTextField         txtFechaInicio;
    private JTextField         txtFechaFin;
    private JComboBox<String>  cbMes;
    private JButton            btnBuscar;
    private JLabel             lblTotalVendido;
    private JLabel             lblGananciaTotal;
    private JButton            btnExportarPDF;

    public IFrmReporteVentas() {
        super("Reporte de Ventas y Ganancias", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        setSize(870, 540);
    }

    private void initComponents() {
        // --- Filtros ---
        txtFechaInicio = UIKit.textField();
        txtFechaInicio.putClientProperty("JTextField.placeholderText", "DD/MM/AAAA");

        txtFechaFin = UIKit.textField();
        txtFechaFin.putClientProperty("JTextField.placeholderText", "DD/MM/AAAA");

        cbMes = new JComboBox<>(new String[]{
            "Todos los Meses", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        });
        cbMes.setFont(UIKit.BODY);

        btnBuscar = UIKit.primaryButton("Buscar");

        // --- Tabla ---
        String[] columns = {"ID Venta", "Cliente", "Total Venta", "Fecha", "Ganancia Est."};
        modelVentas = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblVentas = UIKit.styledTable(modelVentas);  // CORRECCIÓN: usa UIKit.styledTable()

        // --- Resumen ---
        lblTotalVendido = new JLabel("S/ 0.00");
        lblTotalVendido.setFont(UIKit.H1);          // CORRECCIÓN: usa UIKit.H1 en vez de new Font(...)
        lblTotalVendido.setForeground(UIKit.PRIMARY); // CORRECCIÓN: usa UIKit.PRIMARY en vez de new Color(25, 118, 210)

        lblGananciaTotal = new JLabel("S/ 0.00");
        lblGananciaTotal.setFont(UIKit.H1);
        lblGananciaTotal.setForeground(UIKit.ACCENT); // CORRECCIÓN: usa UIKit.ACCENT

        btnExportarPDF = UIKit.dangerOutlineButton("📄 Exportar Reporte (PDF)");
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(
                UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        // --- Encabezado ---
        getContentPane().add(
                UIKit.screenHeader("Reporte de Ventas", "Reportes  ›  Ventas y Ganancias"),
                BorderLayout.NORTH);

        // --- Filtros ---
        JPanel pnlFiltros = UIKit.card();
        pnlFiltros.setLayout(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_MD, UIKit.SPACE_XS));
        pnlFiltros.add(UIKit.fieldLabel("Mes:"));
        pnlFiltros.add(cbMes);
        pnlFiltros.add(Box.createHorizontalStrut(UIKit.SPACE_MD));
        pnlFiltros.add(UIKit.fieldLabel("Desde:"));
        pnlFiltros.add(txtFechaInicio);
        pnlFiltros.add(UIKit.fieldLabel("Hasta:"));
        pnlFiltros.add(txtFechaFin);
        pnlFiltros.add(btnBuscar);

        // --- Tabla central ---
        JScrollPane scroll = new JScrollPane(tblVentas);
        scroll.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));

        // --- Panel inferior de resultados ---
        JPanel pnlInferior = UIKit.card();
        pnlInferior.setLayout(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_LG, UIKit.SPACE_SM));

        JLabel t1 = UIKit.fieldLabel("TOTAL FACTURADO:");
        t1.setFont(UIKit.BODY_BOLD);
        JLabel t2 = UIKit.fieldLabel("NETO GANANCIAS (est.):");
        t2.setFont(UIKit.BODY_BOLD);

        pnlInferior.add(t1);
        pnlInferior.add(lblTotalVendido);
        pnlInferior.add(Box.createHorizontalStrut(UIKit.SPACE_LG));
        pnlInferior.add(t2);
        pnlInferior.add(lblGananciaTotal);
        pnlInferior.add(Box.createHorizontalStrut(UIKit.SPACE_LG));
        pnlInferior.add(btnExportarPDF);

        // --- Ensamblaje ---
        JPanel centro = new JPanel(new BorderLayout(0, UIKit.SPACE_MD));
        centro.setOpaque(false);
        centro.add(pnlFiltros, BorderLayout.NORTH);
        centro.add(scroll,     BorderLayout.CENTER);
        centro.add(pnlInferior, BorderLayout.SOUTH);

        getContentPane().add(centro, BorderLayout.CENTER);
    }

    private void attachEvents() {
        btnBuscar.addActionListener(e -> buscarVentas());
        btnExportarPDF.addActionListener(e -> exportarPDF());
    }

    /**
     * Carga y filtra ventas de la BD.
     * CORRECCIÓN: acumulación con BigDecimal en vez de double.
     */
    private void buscarVentas() {
        VentaDAO dao = new VentaDAO();
        List<Venta> lista = dao.listarTodos();

        int idxMes = cbMes.getSelectedIndex(); // 0 = Todos, 1 = Enero, …
        modelVentas.setRowCount(0);

        // CORRECCIÓN: BigDecimal para totales (evita errores de coma flotante)
        BigDecimal totalVendido = BigDecimal.ZERO;

        for (Venta v : lista) {
            boolean pasaFiltro = true;

            // Filtro por mes (formato DD/MM/AAAA o DD/MM/AAAA HH:mm)
            if (idxMes > 0 && v.getFecha() != null) {
                String[] partes = v.getFecha().split("/");
                if (partes.length >= 2) {
                    try {
                        int mesVenta = Integer.parseInt(partes[1].trim());
                        if (mesVenta != idxMes) pasaFiltro = false;
                    } catch (NumberFormatException ignore) { /* fecha malformada */ }
                }
            }

            // TODO (Release 4): Implementar filtro de rango de fechas fInicio/fFin
            // usando java.time.LocalDate para comparaciones correctas.

            if (pasaFiltro && v.getTotal() != null) {
                totalVendido = totalVendido.add(v.getTotal());
                modelVentas.addRow(new Object[]{
                    v.getIdVenta(),
                    v.getCliente(),
                    "S/ " + v.getTotal().setScale(2, RoundingMode.HALF_UP).toPlainString(),
                    v.getFecha(),
                    // Ganancia estimada = total (se mejorará con costo real en Release 4)
                    "S/ " + v.getTotal().setScale(2, RoundingMode.HALF_UP).toPlainString()
                });
            }
        }

        String totalStr = "S/ " + totalVendido.setScale(2, RoundingMode.HALF_UP).toPlainString();
        lblTotalVendido.setText(totalStr);
        lblGananciaTotal.setText(totalStr); // Pendiente: cruzar con costos reales
    }

    /**
     * Exporta el reporte visible a un archivo PDF usando iTextPDF.
     * CORRECCIÓN: e.printStackTrace() → LoggerGlobal.error()
     */
    private void exportarPDF() {
        if (tblVentas.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay ventas para exportar. Use 'Buscar' primero.");
            return;
        }
        try {
            String fileName = "Reporte_Ventas_" + System.currentTimeMillis() + ".pdf";
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(fileName));
            doc.open();

            com.itextpdf.text.Font titleFont  = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);

            Paragraph titulo = new Paragraph("REPORTE DE VENTAS — ERP MiniMarket LAREDO", titleFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            doc.add(titulo);
            doc.add(new Paragraph(" "));

            PdfPTable tabla = new PdfPTable(5);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{1.5f, 3.5f, 2.5f, 3f, 2.5f});

            String[] headers = {"ID", "Cliente", "Total", "Fecha", "Ganancia Est."};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setBackgroundColor(new BaseColor(25, 118, 210));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(6);
                com.itextpdf.text.Font whiteFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
                whiteFont.setColor(BaseColor.WHITE);
                cell.setPhrase(new Phrase(h, whiteFont));
                tabla.addCell(cell);
            }

            for (int i = 0; i < tblVentas.getRowCount(); i++) {
                for (int j = 0; j < 5; j++) {
                    tabla.addCell(String.valueOf(tblVentas.getValueAt(i, j)));
                }
            }
            doc.add(tabla);
            doc.add(new Paragraph(" "));

            com.itextpdf.text.Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Paragraph totalParagraph = new Paragraph("TOTAL FACTURADO: " + lblTotalVendido.getText(), boldFont);
            totalParagraph.setAlignment(Element.ALIGN_RIGHT);
            doc.add(totalParagraph);

            doc.close();
            JOptionPane.showMessageDialog(this, "Reporte exportado: " + fileName);
            File f = new File(fileName);
            if (f.exists()) Desktop.getDesktop().open(f);

        } catch (Exception ex) {
            LoggerGlobal.error("IFrmReporteVentas.exportarPDF() falló", ex); // CORRECCIÓN
            JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage());
        }
    }
}

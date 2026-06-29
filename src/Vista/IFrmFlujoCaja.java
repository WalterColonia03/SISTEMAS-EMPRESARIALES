package Vista;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.util.Date;

import Clases.FlujoCaja;
import Modelo.FlujoCajaDAO;
import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class IFrmFlujoCaja extends JInternalFrame {

    private JDateChooser txtFechaInicio;
    private JDateChooser txtFechaFin;
    private JButton btnFiltrar;

    private JLabel lblIngresos;
    private JLabel lblEgresos;
    private JLabel lblSaldoNeto;
    
    private JLabel lblTipoCambio;
    private JTextField txtNuevoTipoCambio;
    private JButton btnActualizarTC;

    private JTable tblMovimientos;
    private DefaultTableModel modelMovimientos;

    private JButton btnNuevoIngreso;
    private JButton btnNuevoEgreso;
    private JButton btnExportar;

    public IFrmFlujoCaja() {
        super("Flujo de Caja - Resumen Financiero", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        setSize(960, 600);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    private void initComponents() {
        txtFechaInicio = new JDateChooser(); txtFechaInicio.setDateFormatString("dd/MM/yyyy");
        txtFechaInicio.setPreferredSize(new Dimension(140, 36));
        
        txtFechaFin = new JDateChooser(); txtFechaFin.setDateFormatString("dd/MM/yyyy");
        txtFechaFin.setPreferredSize(new Dimension(140, 36));
        
        btnFiltrar = UIKit.primaryButton("Filtrar");

        lblTipoCambio = new JLabel("3.72");
        lblTipoCambio.setFont(UIKit.H1);
        lblTipoCambio.setForeground(UIKit.TEXT_PRIMARY);

        txtNuevoTipoCambio = UIKit.textField();
        txtNuevoTipoCambio.setPreferredSize(new Dimension(100, 36));
        
        btnActualizarTC = UIKit.secondaryButton("Actualizar TC");

        String[] columns = {"Fecha", "Tipo", "Concepto", "Monto (S/)"};
        modelMovimientos = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblMovimientos = UIKit.styledTable(modelMovimientos);

        btnNuevoIngreso = UIKit.primaryButton("+ Nuevo Ingreso");
        btnNuevoEgreso = UIKit.dangerOutlineButton("- Nuevo Egreso");
        btnExportar = UIKit.secondaryButton("Exportar Reporte");
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(
                UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        // ===== Encabezado =====
        getContentPane().add(
                UIKit.screenHeader("Flujo de Caja", "Finanzas  >  Flujo de Caja"),
                BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(0, UIKit.SPACE_MD));
        cuerpo.setOpaque(false);

        // ÃƒÆ’ÂÂ¢ÃƒÂ¢Ã¢â€šÂ¬ÂÂÃƒÂ¢Ã¢â‚¬Å¡ÂÂ¬ÃƒÆ’ÂÂ¢ÃƒÂ¢Ã¢â€šÂ¬ÂÂÃƒÂ¢Ã¢â‚¬Å¡ÂÂ¬ Panel Superior: Tarjetas KPI y Tipo de Cambio ÃƒÆ’ÂÂ¢ÃƒÂ¢Ã¢â€šÂ¬ÂÂÃƒÂ¢Ã¢â‚¬Å¡ÂÂ¬ÃƒÆ’ÂÂ¢ÃƒÂ¢Ã¢â€šÂ¬ÂÂÃƒÂ¢Ã¢â‚¬Å¡ÂÂ¬
        JPanel pnlSuperior = new JPanel(new BorderLayout(UIKit.SPACE_LG, 0));
        pnlSuperior.setOpaque(false);

        // Tarjetas KPI (Izquierda)
        JPanel pnlCards = new JPanel(new GridLayout(1, 3, UIKit.SPACE_MD, 0));
        pnlCards.setOpaque(false);
        
        lblIngresos = new JLabel("S/ 0.00");
        lblIngresos.setFont(UIKit.H2);
        lblEgresos = new JLabel("S/ 0.00");
        lblEgresos.setFont(UIKit.H2);
        lblSaldoNeto = new JLabel("S/ 0.00");
        lblSaldoNeto.setFont(UIKit.H2);
        
        JPanel cardIngresos = UIKit.card();
        cardIngresos.setLayout(new BorderLayout());
        cardIngresos.add(new JLabel("Total Ingresos"), BorderLayout.NORTH);
        cardIngresos.add(lblIngresos, BorderLayout.CENTER);
        
        JPanel cardEgresos = UIKit.card();
        cardEgresos.setLayout(new BorderLayout());
        cardEgresos.add(new JLabel("Total Egresos"), BorderLayout.NORTH);
        cardEgresos.add(lblEgresos, BorderLayout.CENTER);
        
        JPanel cardSaldo = UIKit.card();
        cardSaldo.setLayout(new BorderLayout());
        cardSaldo.add(new JLabel("Saldo Neto"), BorderLayout.NORTH);
        cardSaldo.add(lblSaldoNeto, BorderLayout.CENTER);
        
        pnlCards.add(cardIngresos);
        pnlCards.add(cardEgresos);
        pnlCards.add(cardSaldo);
        
        pnlSuperior.add(pnlCards, BorderLayout.CENTER);

        // Tipo de Cambio (Derecha)
        JPanel pnlTC = UIKit.card();
        pnlTC.setPreferredSize(new Dimension(280, 0));
        pnlTC.setLayout(new GridBagLayout());
        
        GridBagConstraints gbcTC = new GridBagConstraints();
        gbcTC.fill = GridBagConstraints.HORIZONTAL;
        gbcTC.weightx = 1.0;
        
        gbcTC.gridx = 0; gbcTC.gridy = 0;
        gbcTC.gridwidth = 2;
        pnlTC.add(UIKit.sectionHeader("Tipo de Cambio (USD)", null), gbcTC);
        
        gbcTC.gridwidth = 1;
        gbcTC.gridy = 1;
        gbcTC.insets = new Insets(0, 0, UIKit.SPACE_XS, UIKit.SPACE_SM);
        pnlTC.add(UIKit.fieldLabel("TC Actual"), gbcTC);
        
        gbcTC.gridx = 1;
        gbcTC.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlTC.add(UIKit.fieldLabel("Nuevo TC"), gbcTC);
        
        gbcTC.gridy = 2;
        gbcTC.gridx = 0;
        gbcTC.insets = new Insets(0, 0, UIKit.SPACE_MD, UIKit.SPACE_SM);
        pnlTC.add(lblTipoCambio, gbcTC);
        
        gbcTC.gridx = 1;
        gbcTC.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlTC.add(txtNuevoTipoCambio, gbcTC);
        
        gbcTC.gridy = 3;
        gbcTC.gridx = 0;
        gbcTC.gridwidth = 2;
        gbcTC.insets = new Insets(0, 0, 0, 0);
        pnlTC.add(btnActualizarTC, gbcTC);

        pnlSuperior.add(pnlTC, BorderLayout.EAST);

        cuerpo.add(pnlSuperior, BorderLayout.NORTH);

        // ÃƒÆ’ÂÂ¢ÃƒÂ¢Ã¢â€šÂ¬ÂÂÃƒÂ¢Ã¢â‚¬Å¡ÂÂ¬ÃƒÆ’ÂÂ¢ÃƒÂ¢Ã¢â€šÂ¬ÂÂÃƒÂ¢Ã¢â‚¬Å¡ÂÂ¬ Panel Central: Tabla de Movimientos ÃƒÆ’ÂÂ¢ÃƒÂ¢Ã¢â€šÂ¬ÂÂÃƒÂ¢Ã¢â‚¬Å¡ÂÂ¬ÃƒÆ’ÂÂ¢ÃƒÂ¢Ã¢â€šÂ¬ÂÂÃƒÂ¢Ã¢â‚¬Å¡ÂÂ¬
        JPanel pnlCentral = UIKit.card();
        pnlCentral.setLayout(new BorderLayout(0, UIKit.SPACE_MD));
        
        // Cabecera de la tarjeta con filtros y botones
        JPanel pnlAcciones = new JPanel(new BorderLayout());
        pnlAcciones.setOpaque(false);
        
        JPanel pnlFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_SM, 0));
        pnlFiltros.setOpaque(false);
        pnlFiltros.add(UIKit.fieldLabel("Desde:"));
        pnlFiltros.add(txtFechaInicio);
        pnlFiltros.add(UIKit.fieldLabel("Hasta:"));
        pnlFiltros.add(txtFechaFin);
        pnlFiltros.add(btnFiltrar);
        
        JPanel pnlBotonesTabla = new JPanel(new FlowLayout(FlowLayout.RIGHT, UIKit.SPACE_SM, 0));
        pnlBotonesTabla.setOpaque(false);
        pnlBotonesTabla.add(btnNuevoEgreso);
        pnlBotonesTabla.add(btnNuevoIngreso);
        pnlBotonesTabla.add(btnExportar);
        
        pnlAcciones.add(pnlFiltros, BorderLayout.WEST);
        pnlAcciones.add(pnlBotonesTabla, BorderLayout.EAST);

        pnlCentral.add(UIKit.sectionHeader("Movimientos del PerÃƒÆ’Ã†â€™Ãƒâ€šÂÂ­odo", null), BorderLayout.NORTH);
        
        JPanel pnlInner = new JPanel(new BorderLayout(0, UIKit.SPACE_SM));
        pnlInner.setOpaque(false);
        pnlInner.add(pnlAcciones, BorderLayout.NORTH);
        
        JScrollPane scroll = new JScrollPane(tblMovimientos);
        scroll.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        pnlInner.add(scroll, BorderLayout.CENTER);
        
        pnlCentral.add(pnlInner, BorderLayout.CENTER);

        cuerpo.add(pnlCentral, BorderLayout.CENTER);

        getContentPane().add(cuerpo, BorderLayout.CENTER);
    }

    private void attachEvents() {
        cargarFlujo();
        
        btnFiltrar.addActionListener(e -> {
            cargarFlujo();
        });

        btnActualizarTC.addActionListener(e -> {
            String nuevo = txtNuevoTipoCambio.getText().trim();
            if (!nuevo.isEmpty()) {
                lblTipoCambio.setText(nuevo);
            }
        });

        btnNuevoIngreso.addActionListener(e -> {
            registrarMovimiento("INGRESO");
        });

        btnNuevoEgreso.addActionListener(e -> {
            registrarMovimiento("EGRESO");
        });

        btnExportar.addActionListener(e -> {
            exportarPDF();
        });
    }

    private void exportarPDF() {
        if (tblMovimientos.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay movimientos para exportar.");
            return;
        }
        try {
            String fileName = "Flujo_Caja_" + System.currentTimeMillis() + ".pdf";
            com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(doc, new java.io.FileOutputStream(fileName));
            doc.open();

            com.itextpdf.text.Font titleFont  = com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD, 18);
            com.itextpdf.text.Font headerFont = com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD, 11);

            com.itextpdf.text.Paragraph titulo = new com.itextpdf.text.Paragraph("REPORTE DE FLUJO DE CAJA", titleFont);
            titulo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            doc.add(titulo);
            doc.add(new com.itextpdf.text.Paragraph(" "));

            com.itextpdf.text.pdf.PdfPTable tabla = new com.itextpdf.text.pdf.PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{2f, 2f, 4f, 2f});

            String[] headers = {"Fecha", "Tipo", "Concepto", "Monto"};
            for (String h : headers) {
                com.itextpdf.text.pdf.PdfPCell cell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(h, headerFont));
                cell.setBackgroundColor(new com.itextpdf.text.BaseColor(30, 160, 100)); // Verde excel
                cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                cell.setPadding(6);
                com.itextpdf.text.Font whiteFont = com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD, 11);
                whiteFont.setColor(com.itextpdf.text.BaseColor.WHITE);
                cell.setPhrase(new com.itextpdf.text.Phrase(h, whiteFont));
                tabla.addCell(cell);
            }

            for (int i = 0; i < tblMovimientos.getRowCount(); i++) {
                tabla.addCell(String.valueOf(tblMovimientos.getValueAt(i, 0)));
                tabla.addCell(String.valueOf(tblMovimientos.getValueAt(i, 1)));
                tabla.addCell(String.valueOf(tblMovimientos.getValueAt(i, 2)));
                tabla.addCell(String.valueOf(tblMovimientos.getValueAt(i, 3)));
            }
            doc.add(tabla);
            doc.add(new com.itextpdf.text.Paragraph(" "));

            com.itextpdf.text.Font boldFont = com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD, 12);
            com.itextpdf.text.Paragraph resumen = new com.itextpdf.text.Paragraph(
                "Ingresos: " + lblIngresos.getText() + " | Egresos: " + lblEgresos.getText() + " | Saldo Neto: " + lblSaldoNeto.getText(),
                boldFont
            );
            resumen.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            doc.add(resumen);

            doc.close();
            JOptionPane.showMessageDialog(this, "Reporte exportado exitosamente.");
            java.io.File f = new java.io.File(fileName);
            if (f.exists()) java.awt.Desktop.getDesktop().open(f);

        } catch (Exception ex) {
            Utils.LoggerGlobal.error("IFrmFlujoCaja.exportarPDF() falló", ex);
            JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage());
        }
    }

    private void registrarMovimiento(String tipo) {
        JTextField txtConcepto = new JTextField();
        JTextField txtMonto = new JTextField();
        Object[] msg = {
            "Concepto:", txtConcepto,
            "Monto (S/):", txtMonto
        };
        int opt = JOptionPane.showConfirmDialog(this, msg, "Nuevo " + tipo, JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            try {
                FlujoCaja f = new FlujoCaja();
                f.setFecha(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                f.setTipo(tipo);
                f.setConcepto(txtConcepto.getText());
                // CORRECCIÃ“N: BigDecimal en vez de double para precisiÃƒÆ’Ã†â€™Ãƒâ€šÂÂ³n financiera
                f.setMonto(new java.math.BigDecimal(txtMonto.getText().replace(",", ".")));

                FlujoCajaDAO dao = new FlujoCajaDAO();
                dao.guardar(f);
                cargarFlujo();
            } catch (NumberFormatException nfe) {
                Utils.LoggerGlobal.error("IFrmFlujoCaja: monto invÃƒÆ’Ã†â€™Ãƒâ€šÂÂ¡lido", nfe);
                JOptionPane.showMessageDialog(this, "Error: ingrese un monto numÃƒÆ’Ã†â€™Ãƒâ€šÂÂ©rico vÃƒÆ’Ã†â€™Ãƒâ€šÂÂ¡lido (ej: 150.50)");
            } catch (Exception ex) {
                Utils.LoggerGlobal.error("IFrmFlujoCaja.registrarMovimiento() fallÃƒÆ’Ã†â€™Ãƒâ€šÂÂ³", ex);
                JOptionPane.showMessageDialog(this, "Error al registrar movimiento: " + ex.getMessage());
            }
        }
    }

    private void cargarFlujo() {
        FlujoCajaDAO dao = new FlujoCajaDAO();
        List<FlujoCaja> lista = dao.listarTodos();
        modelMovimientos.setRowCount(0);
        
        // CORRECCIÃ“N: acumuladores BigDecimal (2026-06-26 ÃƒÆ’ÂÂ¢ÃƒÂ¢Ã¢â‚¬Å¡ÂÂ¬ÃƒÂ¢Ã¢â€šÂ¬ÂÂ AuditorÃƒÆ’Ã†â€™Ãƒâ€šÂÂ­a ERP)
        java.math.BigDecimal totalIng = java.math.BigDecimal.ZERO;
        java.math.BigDecimal totalEgr = java.math.BigDecimal.ZERO;

        for (FlujoCaja f : lista) {
            java.math.BigDecimal monto = f.getMonto() != null ? f.getMonto() : java.math.BigDecimal.ZERO;
            modelMovimientos.addRow(new Object[]{
                f.getFecha(), f.getTipo(), f.getConcepto(),
                "S/ " + monto.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString()
            });
            if ("INGRESO".equals(f.getTipo())) totalIng = totalIng.add(monto);
            if ("EGRESO".equals(f.getTipo())) totalEgr = totalEgr.add(monto);
        }

        java.math.BigDecimal saldo = totalIng.subtract(totalEgr);
        lblIngresos.setText("S/ " + totalIng.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString());
        lblEgresos.setText("S/ " + totalEgr.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString());
        lblSaldoNeto.setText("S/ " + saldo.abs().setScale(2, java.math.RoundingMode.HALF_UP).toPlainString());
    }
}

package Vista;

import Clases.Bitacora;
import Modelo.BitacoraDAO;
import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vista de Bitácora de Auditoría — solo lectura (CA-4: sin botón Editar/Eliminar).
 *
 * Historia de Usuario: FR-020-v2 · CA-1 a CA-6
 *                      FR-041    · CA-1 a CA-2 (Exportar a PDF)
 *
 * Creado: 2026-06-25T23:50:00-05:00
 * Modificado: 2026-06-26T02:17:00-05:00 — getFechaHora() → getTimestamp(), +filtro usuario, +resultado
 * Modificado: 2026-06-27 — Se agrega exportación PDF (FR-041)
 */
public class IFrmBitacoraAuditoria extends JInternalFrame {

    private JTable              tblBitacora;
    private DefaultTableModel   modelBitacora;
    private JButton             btnActualizar;
    private JButton             btnBuscar;
    private JButton             btnExportarPDF;
    private JTextField          txtFiltroUsuario;
    private JComboBox<String>   cbFiltroResultado;

    public IFrmBitacoraAuditoria() {
        super("Bitácora de Auditoría", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        setSize(1060, 620);
    }

    private void initComponents() {
        // CA-3: columna Resultado visible para detectar anomalías
        String[] columns = {"ID", "Usuario", "Módulo", "Acción", "Resultado", "Detalle", "Timestamp"};
        modelBitacora = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblBitacora = UIKit.styledTable(modelBitacora);

        btnActualizar     = UIKit.primaryButton("↻ Actualizar");
        btnBuscar         = UIKit.secondaryButton("Buscar");
        btnExportarPDF    = UIKit.secondaryButton("Exportar PDF");
        txtFiltroUsuario  = UIKit.textField();                  // CORRECCIÓN: era styledTextField (no existe)
        txtFiltroUsuario.setToolTipText("Filtrar por usuario...");
        cbFiltroResultado = new JComboBox<>(new String[]{"Todos", "EXITO", "FALLO"});
        cbFiltroResultado.setFont(UIKit.BODY);                  // CORRECCIÓN: era FONT_BODY
        cbFiltroResultado.setBackground(UIKit.BG_CARD);         // CORRECCIÓN: era SURFACE
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(
                UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        getContentPane().add(
                UIKit.screenHeader("Bitácora de Auditoría", "Seguridad  >  Bitácora"),
                BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(0, UIKit.SPACE_MD));
        cuerpo.setOpaque(false);

        // Panel de filtros (CA-3: filtro por usuario y resultado)
        JPanel pnlFiltros = UIKit.card();
        pnlFiltros.setLayout(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_SM, UIKit.SPACE_SM));
        pnlFiltros.add(new JLabel("Usuario:"));
        pnlFiltros.add(txtFiltroUsuario);
        pnlFiltros.add(new JLabel("Resultado:"));
        pnlFiltros.add(cbFiltroResultado);
        pnlFiltros.add(btnBuscar);
        pnlFiltros.add(btnActualizar);
        pnlFiltros.add(btnExportarPDF);
        cuerpo.add(pnlFiltros, BorderLayout.NORTH);

        // Tabla — CA-4: sin botones de edición
        JPanel pnlTabla = UIKit.card();
        pnlTabla.setLayout(new BorderLayout());
        JScrollPane scroll = new JScrollPane(tblBitacora);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        pnlTabla.add(scroll, BorderLayout.CENTER);
        cuerpo.add(pnlTabla, BorderLayout.CENTER);

        getContentPane().add(cuerpo, BorderLayout.CENTER);
    }

    private void attachEvents() {
        btnActualizar.addActionListener(e -> cargarDatos(null, null));
        btnBuscar.addActionListener(e -> {
            String usuario = txtFiltroUsuario.getText().trim();
            String resultado = cbFiltroResultado.getSelectedIndex() == 0
                    ? null : cbFiltroResultado.getSelectedItem().toString();
            cargarDatos(usuario.isEmpty() ? null : usuario, resultado);
        });
        btnExportarPDF.addActionListener(e -> generarPDF());
        cargarDatos(null, null);
    }

    private void cargarDatos(String filtroUsuario, String filtroResultado) {
        BitacoraDAO dao = new BitacoraDAO();
        List<Bitacora> lista;

        if (filtroUsuario != null && !filtroUsuario.isEmpty()) {
            lista = dao.listarPorUsuario(filtroUsuario);
        } else {
            lista = dao.listarPorFiltros(null, filtroResultado);
        }

        modelBitacora.setRowCount(0);
        for (Bitacora b : lista) {
            modelBitacora.addRow(new Object[]{
                b.getIdBitacora(),
                b.getUsuario(),
                b.getModulo(),
                b.getAccion(),
                b.getResultado(),   // CORRECCIÓN: era b.getFechaHora() (método inexistente)
                b.getDetalle(),
                b.getTimestamp()    // CORRECCIÓN: era b.getFechaHora()
            });
        }
    }

    /**
     * Genera un reporte PDF de los registros visibles en la bitácora.
     * Capa: Vista — Implementa: FR-041
     */
    private void generarPDF() {
        if (modelBitacora.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay registros para exportar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombreArchivo = "Reporte_Bitacora_" + System.currentTimeMillis() + ".pdf";
        try {
            com.itextpdf.text.Document document = new com.itextpdf.text.Document(com.itextpdf.text.PageSize.A4.rotate());
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(nombreArchivo));
            document.open();

            document.add(new com.itextpdf.text.Paragraph("MINIMARKET LAREDO - REPORTE DE BITÁCORA"));
            document.add(new com.itextpdf.text.Paragraph("Fecha de emisión: " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date())));
            document.add(new com.itextpdf.text.Paragraph(" "));

            com.itextpdf.text.pdf.PdfPTable table = new com.itextpdf.text.pdf.PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1f, 2f, 2f, 3f, 2f, 3f, 3f});

            String[] cabeceras = {"ID", "Usuario", "Módulo", "Acción", "Resultado", "Detalle", "Timestamp"};
            for (String cabecera : cabeceras) {
                com.itextpdf.text.pdf.PdfPCell cell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(cabecera));
                cell.setBackgroundColor(new com.itextpdf.text.BaseColor(200, 200, 200));
                table.addCell(cell);
            }

            for (int i = 0; i < modelBitacora.getRowCount(); i++) {
                for (int j = 0; j < 7; j++) {
                    Object val = modelBitacora.getValueAt(i, j);
                    table.addCell(val == null ? "" : val.toString());
                }
            }

            document.add(table);
            document.close();
            JOptionPane.showMessageDialog(this, "Bitácora exportada exitosamente a PDF:\n" + nombreArchivo, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            Utils.BitacoraService.registrar(Clases.Sesion.getUsuario(), Utils.BitacoraService.MOD_SEGURIDAD, "EXPORTAR_BITACORA_PDF", Utils.BitacoraService.OK, nombreArchivo);
            
        } catch (Exception ex) {
            Utils.LoggerGlobal.logError(this.getClass().getName(), "generarPDF", "Error exportando bitácora", ex);
            JOptionPane.showMessageDialog(this, "Error al generar el PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

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
 *
 * Creado: 2026-06-25T23:50:00-05:00
 * Modificado: 2026-06-26T02:17:00-05:00 — getFechaHora() → getTimestamp(), +filtro usuario, +resultado
 */
public class IFrmBitacoraAuditoria extends JInternalFrame {

    private JTable              tblBitacora;
    private DefaultTableModel   modelBitacora;
    private JButton             btnActualizar;
    private JButton             btnBuscar;
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
                UIKit.screenHeader("Bitácora de Auditoría", "Seguridad  ›  Bitácora"),
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
}

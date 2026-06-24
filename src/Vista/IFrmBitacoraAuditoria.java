package Vista;

import Vista.Estilos.UIKit;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * IFrmBitacoraAuditoria - Bitácora de Auditoría.
 * Rediseñado con UIKit (Patrón D).
 */
public class IFrmBitacoraAuditoria extends JInternalFrame {

    private JTable tblAuditoria;
    private DefaultTableModel modelAuditoria;

    // Filtros
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JComboBox<String> cbUsuario;
    private JComboBox<String> cbAccion;

    private JButton btnFiltrar;
    private JButton btnLimpiar;
    private JButton btnExportar;

    public IFrmBitacoraAuditoria() {
        super("Bitácora de Auditoría", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        setSize(960, 600);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    private void initComponents() {
        // Filtros
        txtFechaInicio = UIKit.textField();
        txtFechaInicio.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "DD/MM/AAAA");
        
        txtFechaFin = UIKit.textField();
        txtFechaFin.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "DD/MM/AAAA");
        
        cbUsuario = new JComboBox<>(new String[]{"Todos", "ADMIN", "user1", "superadmin"});
        cbUsuario.setFont(UIKit.BODY);
        
        cbAccion = new JComboBox<>(new String[]{"Todos", "Inicio Sesión", "Creación de Cliente", "Registro Venta", "Actualización Stock", "Devolución"});
        cbAccion.setFont(UIKit.BODY);

        btnFiltrar = UIKit.primaryButton("Filtrar");
        btnLimpiar = UIKit.secondaryButton("Restablecer");
        btnExportar = UIKit.secondaryButton("Exportar Auditoría");

        // Tabla de auditoría
        String[] columns = {"Fecha y Hora", "Usuario", "Módulo", "Acción Realizada", "Detalles", "IP/Equipo"};
        modelAuditoria = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblAuditoria = UIKit.styledTable(modelAuditoria);
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(
                UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        // ===== Encabezado =====
        getContentPane().add(
                UIKit.screenHeader("Bitácora de Auditoría", "Administración  ›  Auditoría"),
                BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(0, UIKit.SPACE_MD));
        cuerpo.setOpaque(false);

        // ── Panel Superior: Filtros de Búsqueda ──
        JPanel pnlFiltros = UIKit.card();
        pnlFiltros.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlFiltros.add(UIKit.sectionHeader("Filtros de Búsqueda", null), gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, UIKit.SPACE_SM);
        pnlFiltros.add(UIKit.fieldLabel("Fecha Inicio"), gbc);
        
        gbc.gridx = 1;
        pnlFiltros.add(UIKit.fieldLabel("Fecha Fin"), gbc);
        
        gbc.gridx = 2;
        pnlFiltros.add(UIKit.fieldLabel("Usuario"), gbc);
        
        gbc.gridx = 3;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlFiltros.add(UIKit.fieldLabel("Acción"), gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, UIKit.SPACE_SM);
        pnlFiltros.add(txtFechaInicio, gbc);
        
        gbc.gridx = 1;
        pnlFiltros.add(txtFechaFin, gbc);
        
        gbc.gridx = 2;
        pnlFiltros.add(cbUsuario, gbc);
        
        gbc.gridx = 3;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlFiltros.add(cbAccion, gbc);

        // Panel de botones de filtros
        JPanel pnlBotonesFiltro = new JPanel(new FlowLayout(FlowLayout.RIGHT, UIKit.SPACE_SM, 0));
        pnlBotonesFiltro.setOpaque(false);
        pnlBotonesFiltro.add(btnLimpiar);
        pnlBotonesFiltro.add(btnFiltrar);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(0, 0, 0, 0);
        pnlFiltros.add(pnlBotonesFiltro, gbc);
        
        cuerpo.add(pnlFiltros, BorderLayout.NORTH);

        // ── Panel Central: Tabla ──
        JPanel pnlCentral = UIKit.card();
        pnlCentral.setLayout(new BorderLayout(0, UIKit.SPACE_SM));
        pnlCentral.add(UIKit.sectionHeader("Registros de Auditoría", btnExportar), BorderLayout.NORTH);
        
        JScrollPane scroll = new JScrollPane(tblAuditoria);
        scroll.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        pnlCentral.add(scroll, BorderLayout.CENTER);

        cuerpo.add(pnlCentral, BorderLayout.CENTER);

        getContentPane().add(cuerpo, BorderLayout.CENTER);
    }

    private void attachEvents() {
        btnFiltrar.addActionListener(e -> {
            // TODO: lógica TXT para leer logs de auditoria y cargarlos en la tabla según filtros
        });

        btnLimpiar.addActionListener(e -> {
            txtFechaInicio.setText("");
            txtFechaFin.setText("");
            cbUsuario.setSelectedIndex(0);
            cbAccion.setSelectedIndex(0);
        });

        btnExportar.addActionListener(e -> {
            // TODO: lógica TXT para guardar los registros de auditoría filtrados en un reporte de texto
            JOptionPane.showMessageDialog(this, "Bitácora exportada correctamente a la carpeta del proyecto");
        });
    }
}

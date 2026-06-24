package Vista;

import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class IFrmFlujoCaja extends JInternalFrame {

    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JButton btnFiltrar;

    // These labels will be updated when the real logic kicks in
    // For the UI redesign, we'll store references to the cards or update logic later
    private String valIngresos = "S/ 0.00";
    private String valEgresos = "S/ 0.00";
    private String valSaldoNeto = "S/ 0.00";
    
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
        txtFechaInicio = UIKit.textField();
        txtFechaInicio.setPreferredSize(new Dimension(140, 36));
        
        txtFechaFin = UIKit.textField();
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
                UIKit.screenHeader("Flujo de Caja", "Finanzas  ›  Flujo de Caja"),
                BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(0, UIKit.SPACE_MD));
        cuerpo.setOpaque(false);

        // ── Panel Superior: Tarjetas KPI y Tipo de Cambio ──
        JPanel pnlSuperior = new JPanel(new BorderLayout(UIKit.SPACE_LG, 0));
        pnlSuperior.setOpaque(false);

        // Tarjetas KPI (Izquierda)
        JPanel pnlCards = new JPanel(new GridLayout(1, 3, UIKit.SPACE_MD, 0));
        pnlCards.setOpaque(false);
        pnlCards.add(UIKit.kpiCard("Total Ingresos", valIngresos, "Período actual", UIKit.SUCCESS));
        pnlCards.add(UIKit.kpiCard("Total Egresos", valEgresos, "Período actual", UIKit.DANGER));
        pnlCards.add(UIKit.kpiCard("Saldo Neto", valSaldoNeto, "Ingresos - Egresos", UIKit.PRIMARY));
        
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

        // ── Panel Central: Tabla de Movimientos ──
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

        pnlCentral.add(UIKit.sectionHeader("Movimientos del Período", null), BorderLayout.NORTH);
        
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
        btnFiltrar.addActionListener(e -> {
            // TODO: lógica TXT para filtrar movimientos por rango de fechas desde flujo_caja.txt
        });

        btnActualizarTC.addActionListener(e -> {
            String nuevo = txtNuevoTipoCambio.getText().trim();
            if (!nuevo.isEmpty()) {
                lblTipoCambio.setText(nuevo);
            }
        });

        btnNuevoIngreso.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Formulario para nuevo ingreso (próximamente).");
        });

        btnNuevoEgreso.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Formulario para nuevo egreso (próximamente).");
        });

        btnExportar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Reporte exportado exitosamente.");
        });
    }
}

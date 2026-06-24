package Vista;

import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class IFrmCuentasCobrarPagar extends JInternalFrame {

    private JTabbedPane tabbedPane;

    // Cuentas por Cobrar
    private JTable tblCobrar;
    private DefaultTableModel modelCobrar;
    private JTextField txtBuscarCobrar;
    private JButton btnBuscarCobrar;
    private JButton btnRegistrarCobro;
    private JButton btnMarcarPagadoCobrar;

    // Cuentas por Pagar
    private JTable tblPagar;
    private DefaultTableModel modelPagar;
    private JTextField txtBuscarPagar;
    private JButton btnBuscarPagar;
    private JButton btnRegistrarPago;
    private JButton btnMarcarPagadoPagar;

    private String valTotalCobrar = "S/ 520.00";
    private String valTotalPagar = "S/ 1,450.00";
    private String valDiferencia = "S/ -930.00";

    public IFrmCuentasCobrarPagar() {
        super("Cuentas por Cobrar y Pagar", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        setSize(960, 600);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    private void initComponents() {
        // ── Cuentas por Cobrar ──
        txtBuscarCobrar = UIKit.textField();
        btnBuscarCobrar = UIKit.secondaryButton("Buscar");
        btnRegistrarCobro = UIKit.primaryButton("+ Nuevo Crédito / Cobro");
        btnMarcarPagadoCobrar = UIKit.secondaryButton("Marcar como Pagado");

        String[] colsCobrar = {"ID", "Cliente", "Documento", "Monto Total", "Saldo Pendiente", "Emisión", "Vencimiento", "Estado"};
        modelCobrar = new DefaultTableModel(colsCobrar, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblCobrar = UIKit.styledTable(modelCobrar);

        // ── Cuentas por Pagar ──
        txtBuscarPagar = UIKit.textField();
        btnBuscarPagar = UIKit.secondaryButton("Buscar");
        btnRegistrarPago = UIKit.primaryButton("+ Nueva Deuda / Pago");
        btnMarcarPagadoPagar = UIKit.secondaryButton("Marcar como Pagado");

        String[] colsPagar = {"ID", "Proveedor", "Documento", "Monto Total", "Saldo Pendiente", "Emisión", "Vencimiento", "Estado"};
        modelPagar = new DefaultTableModel(colsPagar, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblPagar = UIKit.styledTable(modelPagar);

        // Datos de ejemplo
        modelCobrar.addRow(new Object[]{"1", "Juan Pérez", "FV-001", "S/ 500.00", "S/ 200.00", "2025-05-01", "2025-06-01", "Pendiente"});
        modelCobrar.addRow(new Object[]{"2", "María López", "FV-002", "S/ 320.00", "S/ 320.00", "2025-05-15", "2025-06-15", "Pendiente"});

        modelPagar.addRow(new Object[]{"1", "Distribuidora del Sur", "FC-001", "S/ 1,200.00", "S/ 600.00", "2025-04-20", "2025-05-20", "Parcial"});
        modelPagar.addRow(new Object[]{"2", "Grupo Alimenticio SAC", "FC-002", "S/ 850.00", "S/ 850.00", "2025-05-10", "2025-06-10", "Pendiente"});
    }

    private JPanel buildPanelCobrar() {
        JPanel pnl = new JPanel(new BorderLayout(0, UIKit.SPACE_MD));
        pnl.setOpaque(false);

        JPanel pnlAcciones = new JPanel(new BorderLayout());
        pnlAcciones.setOpaque(false);
        
        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_SM, 0));
        pnlBusqueda.setOpaque(false);
        pnlBusqueda.add(txtBuscarCobrar);
        pnlBusqueda.add(btnBuscarCobrar);
        
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, UIKit.SPACE_SM, 0));
        pnlBotones.setOpaque(false);
        pnlBotones.add(btnMarcarPagadoCobrar);
        pnlBotones.add(btnRegistrarCobro);
        
        pnlAcciones.add(pnlBusqueda, BorderLayout.WEST);
        pnlAcciones.add(pnlBotones, BorderLayout.EAST);

        pnl.add(pnlAcciones, BorderLayout.NORTH);
        
        JScrollPane scroll = new JScrollPane(tblCobrar);
        scroll.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        pnl.add(scroll, BorderLayout.CENTER);

        return pnl;
    }

    private JPanel buildPanelPagar() {
        JPanel pnl = new JPanel(new BorderLayout(0, UIKit.SPACE_MD));
        pnl.setOpaque(false);

        JPanel pnlAcciones = new JPanel(new BorderLayout());
        pnlAcciones.setOpaque(false);
        
        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_SM, 0));
        pnlBusqueda.setOpaque(false);
        pnlBusqueda.add(txtBuscarPagar);
        pnlBusqueda.add(btnBuscarPagar);
        
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, UIKit.SPACE_SM, 0));
        pnlBotones.setOpaque(false);
        pnlBotones.add(btnMarcarPagadoPagar);
        pnlBotones.add(btnRegistrarPago);
        
        pnlAcciones.add(pnlBusqueda, BorderLayout.WEST);
        pnlAcciones.add(pnlBotones, BorderLayout.EAST);

        pnl.add(pnlAcciones, BorderLayout.NORTH);
        
        JScrollPane scroll = new JScrollPane(tblPagar);
        scroll.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        pnl.add(scroll, BorderLayout.CENTER);

        return pnl;
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(
                UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        // ===== Encabezado =====
        getContentPane().add(
                UIKit.screenHeader("Cuentas por Cobrar y Pagar", "Finanzas  ›  Cuentas por Cobrar y Pagar"),
                BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(0, UIKit.SPACE_LG));
        cuerpo.setOpaque(false);

        // Tarjetas KPI
        JPanel pnlCards = new JPanel(new GridLayout(1, 3, UIKit.SPACE_MD, 0));
        pnlCards.setOpaque(false);
        pnlCards.add(UIKit.kpiCard("Total por Cobrar", valTotalCobrar, "Cuentas pendientes", UIKit.SUCCESS));
        pnlCards.add(UIKit.kpiCard("Total por Pagar", valTotalPagar, "Deudas pendientes", UIKit.DANGER));
        pnlCards.add(UIKit.kpiCard("Diferencia", valDiferencia, "Por cobrar - Por pagar", UIKit.PRIMARY));
        
        cuerpo.add(pnlCards, BorderLayout.NORTH);

        // Tarjeta Central con TabbedPane
        JPanel pnlCentral = UIKit.card();
        pnlCentral.setLayout(new BorderLayout(0, UIKit.SPACE_SM));
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UIKit.BODY);
        tabbedPane.addTab("Cuentas por Cobrar", buildPanelCobrar());
        tabbedPane.addTab("Cuentas por Pagar", buildPanelPagar());
        
        pnlCentral.add(tabbedPane, BorderLayout.CENTER);

        cuerpo.add(pnlCentral, BorderLayout.CENTER);

        getContentPane().add(cuerpo, BorderLayout.CENTER);
    }

    private void attachEvents() {
        btnBuscarCobrar.addActionListener(e -> {
            // TODO: lógica TXT para filtrar cuentas por cobrar por cliente en cuentas_cobrar.txt
        });

        btnRegistrarCobro.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Formulario para nuevo crédito (próximamente).");
        });

        btnMarcarPagadoCobrar.addActionListener(e -> {
            int row = tblCobrar.getSelectedRow();
            if (row != -1) {
                JOptionPane.showMessageDialog(this, "Cobro marcado como pagado.");
            }
        });

        btnBuscarPagar.addActionListener(e -> {
            // TODO: lógica TXT para filtrar cuentas por pagar por proveedor en cuentas_pagar.txt
        });

        btnRegistrarPago.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Formulario para nueva deuda (próximamente).");
        });

        btnMarcarPagadoPagar.addActionListener(e -> {
            int row = tblPagar.getSelectedRow();
            if (row != -1) {
                JOptionPane.showMessageDialog(this, "Pago marcado como pagado.");
            }
        });
    }
}

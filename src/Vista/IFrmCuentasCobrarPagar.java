package Vista;

import Clases.CuentasCP;
import Modelo.CuentasCPDAO;
import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    private JLabel lblTotalCobrar;
    private JLabel lblTotalPagar;
    private JLabel lblDiferencia;

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

        // Datos cargados desde DAO en attachEvents()
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
                UIKit.screenHeader("Cuentas por Cobrar y Pagar", "Finanzas  >  Cuentas por Cobrar y Pagar"),
                BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(0, UIKit.SPACE_LG));
        cuerpo.setOpaque(false);

        // Tarjetas KPI
        JPanel pnlCards = new JPanel(new GridLayout(1, 3, UIKit.SPACE_MD, 0));
        pnlCards.setOpaque(false);
        
        lblTotalCobrar = new JLabel("S/ 0.00");
        lblTotalCobrar.setFont(UIKit.H2);
        lblTotalPagar = new JLabel("S/ 0.00");
        lblTotalPagar.setFont(UIKit.H2);
        lblDiferencia = new JLabel("S/ 0.00");
        lblDiferencia.setFont(UIKit.H2);
        
        JPanel cardCobrar = UIKit.card();
        cardCobrar.setLayout(new BorderLayout());
        cardCobrar.add(new JLabel("Total por Cobrar"), BorderLayout.NORTH);
        cardCobrar.add(lblTotalCobrar, BorderLayout.CENTER);
        
        JPanel cardPagar = UIKit.card();
        cardPagar.setLayout(new BorderLayout());
        cardPagar.add(new JLabel("Total por Pagar"), BorderLayout.NORTH);
        cardPagar.add(lblTotalPagar, BorderLayout.CENTER);
        
        JPanel cardDif = UIKit.card();
        cardDif.setLayout(new BorderLayout());
        cardDif.add(new JLabel("Diferencia (Cobrar - Pagar)"), BorderLayout.NORTH);
        cardDif.add(lblDiferencia, BorderLayout.CENTER);

        pnlCards.add(cardCobrar);
        pnlCards.add(cardPagar);
        pnlCards.add(cardDif);
        
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
        cargarDatos();

        btnBuscarCobrar.addActionListener(e -> {
            cargarDatos();
        });

        btnRegistrarCobro.addActionListener(e -> {
            registrarNuevaCuenta("COBRAR");
        });

        btnMarcarPagadoCobrar.addActionListener(e -> {
            marcarPagado(tblCobrar, modelCobrar);
        });

        btnBuscarPagar.addActionListener(e -> {
            cargarDatos();
        });

        btnRegistrarPago.addActionListener(e -> {
            registrarNuevaCuenta("PAGAR");
        });

        btnMarcarPagadoPagar.addActionListener(e -> {
            marcarPagado(tblPagar, modelPagar);
        });
    }
    
    private void registrarNuevaCuenta(String tipo) {
        JTextField txtNombre = new JTextField();
        JTextField txtDoc = new JTextField();
        JTextField txtMonto = new JTextField();
        Object[] msg = {
            "Cliente / Proveedor:", txtNombre,
            "Documento Asociado (Opcional):", txtDoc,
            "Monto (S/):", txtMonto
        };
        int opt = JOptionPane.showConfirmDialog(this, msg, "Registrar Cuenta por " + tipo, JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            try {
                CuentasCP c = new CuentasCP();
                c.setTipo(tipo);
                c.setClienteProveedor(txtNombre.getText());
                c.setDocumento(txtDoc.getText());
                // CORRECCIÓN: BigDecimal para montos (2026-06-26 — Auditoría ERP)
                java.math.BigDecimal monto = new java.math.BigDecimal(
                        txtMonto.getText().trim().replace(",", "."));
                c.setMontoOriginal(monto);
                c.setMontoPendiente(monto);
                String fechaHoy = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                c.setFechaEmision(fechaHoy);
                c.setFechaVencimiento(fechaHoy);
                c.setEstado("PENDIENTE");

                CuentasCPDAO dao = new CuentasCPDAO();
                dao.guardar(c);
                cargarDatos();
            } catch (NumberFormatException nfe) {
                Utils.LoggerGlobal.error("IFrmCuentasCobrarPagar: monto inválido", nfe);
                JOptionPane.showMessageDialog(this, "Error: ingrese un monto numérico válido (ej: 250.00)");
            } catch (Exception ex) {
                Utils.LoggerGlobal.error("IFrmCuentasCobrarPagar.registrar() falló", ex);
                JOptionPane.showMessageDialog(this, "Error al registrar. Verifique los datos.");
            }
        }
    }
    
    private void marcarPagado(JTable tabla, DefaultTableModel modelo) {
        int row = tabla.getSelectedRow();
        if (row != -1) {
            int idCuenta = Integer.parseInt(modelo.getValueAt(row, 0).toString());
            // CORRECCIÓN: pendiente como BigDecimal para el método abonar()
            java.math.BigDecimal pendiente = new java.math.BigDecimal(
                    modelo.getValueAt(row, 4).toString().replace("S/ ", "").replace(",", "."));
            CuentasCPDAO dao = new CuentasCPDAO();
            if (dao.abonar(idCuenta, pendiente)) {
                JOptionPane.showMessageDialog(this, "Cuenta marcada como pagada totalmente.");
                cargarDatos();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un registro de la tabla");
        }
    }

    private void cargarDatos() {
        CuentasCPDAO dao = new CuentasCPDAO();
        
        // Cobrar
        List<CuentasCP> listaCobrar = dao.listarPorTipo("COBRAR");
        modelCobrar.setRowCount(0);
        // CORRECCIÓN: acumuladores BigDecimal (2026-06-26 — Auditoría ERP)
        java.math.BigDecimal totalCobrar = java.math.BigDecimal.ZERO;
        String filtroC = txtBuscarCobrar.getText().trim().toLowerCase();

        for (CuentasCP c : listaCobrar) {
            if (filtroC.isEmpty() || c.getClienteProveedor().toLowerCase().contains(filtroC)) {
                java.math.BigDecimal orig = c.getMontoOriginal()   != null ? c.getMontoOriginal()   : java.math.BigDecimal.ZERO;
                java.math.BigDecimal pend = c.getMontoPendiente()  != null ? c.getMontoPendiente()  : java.math.BigDecimal.ZERO;
                modelCobrar.addRow(new Object[]{
                    c.getIdCuenta(), c.getClienteProveedor(), c.getDocumento(),
                    "S/ " + orig.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString(),
                    "S/ " + pend.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString(),
                    c.getFechaEmision(), c.getFechaVencimiento(), c.getEstado()
                });
                totalCobrar = totalCobrar.add(pend);
            }
        }

        // Pagar
        List<CuentasCP> listaPagar = dao.listarPorTipo("PAGAR");
        modelPagar.setRowCount(0);
        java.math.BigDecimal totalPagar = java.math.BigDecimal.ZERO;
        String filtroP = txtBuscarPagar.getText().trim().toLowerCase();

        for (CuentasCP c : listaPagar) {
            if (filtroP.isEmpty() || c.getClienteProveedor().toLowerCase().contains(filtroP)) {
                java.math.BigDecimal orig = c.getMontoOriginal()   != null ? c.getMontoOriginal()   : java.math.BigDecimal.ZERO;
                java.math.BigDecimal pend = c.getMontoPendiente()  != null ? c.getMontoPendiente()  : java.math.BigDecimal.ZERO;
                modelPagar.addRow(new Object[]{
                    c.getIdCuenta(), c.getClienteProveedor(), c.getDocumento(),
                    "S/ " + orig.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString(),
                    "S/ " + pend.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString(),
                    c.getFechaEmision(), c.getFechaVencimiento(), c.getEstado()
                });
                totalPagar = totalPagar.add(pend);
            }
        }

        java.math.BigDecimal diferencia = totalCobrar.subtract(totalPagar);
        lblTotalCobrar.setText("S/ " + totalCobrar.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString());
        lblTotalPagar.setText("S/ " + totalPagar.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString());
        lblDiferencia.setText("S/ " + diferencia.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString());
    }
}

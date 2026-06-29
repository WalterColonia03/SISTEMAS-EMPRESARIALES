package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import Clases.Sesion;
import Utils.LoggerGlobal;
import Vista.Estilos.UIKit;

public class FrmDashboard extends JFrame {

    private JDesktopPane desktopPane;
    private JInternalFrame bgDashboardFrame;
    private JPanel pnlTopBar;
    private JLabel lblBreadcrumb;
    
    private JButton btnPOS, btnVentas, btnDevoluciones, btnFidelizacion;
    private JButton btnClientes, btnProveedores;
    private JButton btnCategorÃ­as, btnProductos, btnKardex, btnAlertasInventario, btnRepInventario;
    private JButton btnCompras;
    private JButton btnFlujoCaja, btnLibroMayor, btnCuentasCP, btnRepVentas, btnReportesAvanzados;
    private JButton btnEmpleados, btnPlanilla, btnEvaluacion;
    private JButton btnUsuarios, btnAuditoria, btnConfig, btnPermisos;
    private JButton btnLogout;
    
    private JPanel grpInventario, grpCompras, grpPersonal, grpAdmin, grpFinanzas;

    private JButton btnSeleccionado = null;
    private Timer inactividadTimer;
    private Timer refreshTimer;
    private static final int TIEMPO_INACTIVIDAD = 30 * 60 * 1000;

    // Componentes del Dashboard a actualizar en tiempo real
    private JLabel lblVentasHoy, lblSubVentasHoy;
    private JLabel lblVentasSemana, lblSubVentasSemana;
    private JLabel lblAlertasStock, lblSubAlertasStock;
    private DefaultTableModel modelAlert;
    private DefaultTableModel modelSales;

    public FrmDashboard() {
        super("Minimarket LAREDO - Sistema ERP");
        initComponents();
        buildLayout();
        attachEvents();
        aplicarRol();
        configFrame();
        iniciarTimerInactividad();
        iniciarTimerRefresco();
    }

    private void iniciarTimerInactividad() {
        inactividadTimer = new Timer(TIEMPO_INACTIVIDAD, e -> {
            new Modelo.UsuarioDAO().cambiarEstadoSesion(Sesion.getUsuario(), 0);
            Utils.BitacoraService.registrar(Sesion.getUsuario(), Utils.BitacoraService.MOD_LOGIN, "CIERRE_INACTIVIDAD", Utils.BitacoraService.OK, "Sesion cerrada por inactividad");
            JOptionPane.showMessageDialog(this, "Sesión cerrada por inactividad (30 minutos).", "Seguridad", JOptionPane.WARNING_MESSAGE);
            new FrmLogin().setVisible(true);
            this.dispose();
        });
        inactividadTimer.setRepeats(false);
        inactividadTimer.start();

        long eventMask = AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK;
        Toolkit.getDefaultToolkit().addAWTEventListener(event -> inactividadTimer.restart(), eventMask);
    }
    
    private void iniciarTimerRefresco() {
        refreshTimer = new Timer(5000, e -> updateBgDashboard()); // Actualizar cada 5 segundos
        refreshTimer.start();
    }

    private void initComponents() {
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(UIKit.BG_APP);

        btnPOS = buildMenuButton("Punto de Venta (POS)");
        btnDevoluciones = buildMenuButton("Devoluciones");
        btnFidelizacion = buildMenuButton("Fidelización");
        btnClientes = buildMenuButton("Clientes");
        btnProveedores = buildMenuButton("Proveedores");
        btnCategorÃ­as = buildMenuButton("Categorías");
        btnProductos = buildMenuButton("Productos");
        btnKardex = buildMenuButton("Kardex");
        btnAlertasInventario = buildMenuButton("Alertas de Inventario");
        btnRepInventario = buildMenuButton("Reporte de Inventario");
        btnCompras = buildMenuButton("Registro de Compras");
        btnFlujoCaja = buildMenuButton("Flujo de Caja");
        btnLibroMayor = buildMenuButton("Libro Mayor");
        btnCuentasCP = buildMenuButton("Cuentas por Cobrar y Pagar");
        btnRepVentas = buildMenuButton("Historial de Ventas");
        btnReportesAvanzados = buildMenuButton("Reportes Avanzados (BI)");
        btnEmpleados = buildMenuButton("Ficha de Empleados");
        btnPlanilla = buildMenuButton("Planilla y Asistencia");
        btnEvaluacion = buildMenuButton("Evaluación Desempeño");
        btnUsuarios = buildMenuButton("Gestión Usuarios");
        btnPermisos = buildMenuButton("Gestión de Permisos");
        btnAuditoria = buildMenuButton("Bitácora Auditoría");
        btnConfig = buildMenuButton("Configuración ERP");
        btnLogout = buildMenuButton("Cerrar Sesión");
    }

    private void buildLayout() {
        setLayout(new BorderLayout());

        JPanel pnlSidebar = new JPanel();
        pnlSidebar.setLayout(new BoxLayout(pnlSidebar, BoxLayout.Y_AXIS));
        pnlSidebar.setBackground(UIKit.SIDEBAR_BG);
        pnlSidebar.setPreferredSize(new Dimension(240, 0));
        pnlSidebar.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Logo / marca del sistema
        JPanel pnlLogo = new JPanel(new BorderLayout());
        pnlLogo.setBackground(UIKit.SIDEBAR_BG);
        pnlLogo.setBorder(new EmptyBorder(20, 16, 16, 16));
        JLabel lblLogo = new JLabel("🛒  ERP LAREDO");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblLogo.setForeground(Color.WHITE);
        pnlLogo.add(lblLogo, BorderLayout.WEST);
        pnlSidebar.add(pnlLogo);
        pnlSidebar.add(UIKit.separator());

        JPanel pnlMenuContainer = new JPanel();
        pnlMenuContainer.setLayout(new BoxLayout(pnlMenuContainer, BoxLayout.Y_AXIS));
        pnlMenuContainer.setBackground(UIKit.PRIMARY);

        pnlMenuContainer.add(navGroup("Ventas", btnPOS, btnDevoluciones, btnFidelizacion));
        pnlMenuContainer.add(navGroup("Clientes y Proveedores", btnClientes, btnProveedores));
        
        grpInventario = navGroup("Inventario", btnCategorÃ­as, btnProductos, btnKardex, btnAlertasInventario, btnRepInventario);
        pnlMenuContainer.add(grpInventario);
        
        grpCompras = navGroup("Compras", btnCompras);
        pnlMenuContainer.add(grpCompras);
        
        grpFinanzas = navGroup("Finanzas y Reportes", btnFlujoCaja, btnLibroMayor, btnCuentasCP, btnRepVentas, btnReportesAvanzados);
        pnlMenuContainer.add(grpFinanzas);
        
        grpPersonal = navGroup("Personal", btnEmpleados, btnPlanilla, btnEvaluacion);
        pnlMenuContainer.add(grpPersonal);
        
        grpAdmin = navGroup("Administración", btnUsuarios, btnPermisos, btnAuditoria, btnConfig);
        pnlMenuContainer.add(grpAdmin);
        
        pnlMenuContainer.add(Box.createVerticalGlue());
        
        JScrollPane scrollMenu = new JScrollPane(pnlMenuContainer);
        scrollMenu.setBorder(null);
        scrollMenu.setOpaque(false);
        scrollMenu.getViewport().setOpaque(false);
        scrollMenu.getVerticalScrollBar().setUnitIncrement(16);
        pnlSidebar.add(scrollMenu);
        
        JPanel pnlLogout = new JPanel(new BorderLayout());
        pnlLogout.setOpaque(false);
        pnlLogout.setBorder(new EmptyBorder(10, 0, 0, 0));
        pnlLogout.add(btnLogout, BorderLayout.CENTER);
        pnlSidebar.add(pnlLogout);

        add(pnlSidebar, BorderLayout.WEST);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlTopBar = new JPanel(new BorderLayout());
        pnlTopBar.setBackground(Color.WHITE);
        pnlTopBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UIKit.BORDER),
            new EmptyBorder(10, 24, 10, 24)));
        
        lblBreadcrumb = new JLabel("Inicio / Dashboard");
        lblBreadcrumb.setFont(UIKit.BODY);
        lblBreadcrumb.setForeground(UIKit.TEXT_SECONDARY);
        pnlTopBar.add(lblBreadcrumb, BorderLayout.WEST);
        
        // Topbar derecho: badge usuario + rol (patrón referencia web)
        String nombreUsuario = Sesion.getUsuario() != null ? Sesion.getUsuario() : "Admin";
        String rolUsuario = Sesion.getRol() != null ? Sesion.getRol() : "Administrador";
        JPanel pnlUserInfo = UIKit.topbarUserBadge(nombreUsuario, rolUsuario);
        pnlTopBar.add(pnlUserInfo, BorderLayout.EAST);
        
        pnlCenter.add(pnlTopBar, BorderLayout.NORTH);
        pnlCenter.add(desktopPane, BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        initBgDashboard();
    }

    /** Grupo de navegación en el sidebar con etiqueta de sección y botones. */
    private JPanel navGroup(String titulo, JButton... botones) {
        JPanel grupo = new JPanel();
        grupo.setBackground(UIKit.SIDEBAR_BG);
        grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));
        grupo.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Etiqueta de sección en gris claro semitransparente
        JLabel lblGrupo = new JLabel(titulo.toUpperCase());
        lblGrupo.setFont(UIKit.CAPTION);
        lblGrupo.setForeground(UIKit.SIDEBAR_SECTION);
        lblGrupo.setBorder(new EmptyBorder(14, 16, 4, 16));
        grupo.add(lblGrupo);
        for (JButton b : botones) {
            grupo.add(b);
            grupo.add(Box.createVerticalStrut(1));
        }
        return grupo;
    }

    private void initBgDashboard() {
        bgDashboardFrame = new JInternalFrame("Resumen", false, false, false, false);
        bgDashboardFrame.setBorder(null);
        ((javax.swing.plaf.basic.BasicInternalFrameUI) bgDashboardFrame.getUI()).setNorthPane(null);

        JPanel pnlContent = new JPanel(new BorderLayout(15, 15));
        pnlContent.setBackground(UIKit.BG_APP);
        pnlContent.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel pnlCards = new JPanel(new GridLayout(1, 3, 16, 0));
        pnlCards.setOpaque(false);
        pnlCards.setPreferredSize(new Dimension(0, 110));

        // KPI cards con ícono — patrón referencia web (FR-001)
        JPanel cardHoy = UIKit.kpiCard("Total Ventas del Mes", "0", "0 Transacciones", UIKit.PRIMARY, "🛒");
        lblVentasHoy = (JLabel) cardHoy.getClientProperty("val");
        lblSubVentasHoy = (JLabel) cardHoy.getClientProperty("sub");
        
        JPanel cardSemana = UIKit.kpiCard("Ingresos del Mes", "S/ 0.00", "Semana en curso", UIKit.SUCCESS, "$");
        lblVentasSemana = (JLabel) cardSemana.getClientProperty("val");
        lblSubVentasSemana = (JLabel) cardSemana.getClientProperty("sub");
        
        JPanel cardAlerta = UIKit.kpiCard("Sin Stock", "0", "Productos agotados", UIKit.DANGER, "⚠");
        lblAlertasStock = (JLabel) cardAlerta.getClientProperty("val");
        lblSubAlertasStock = (JLabel) cardAlerta.getClientProperty("sub");

        pnlCards.add(cardHoy);
        pnlCards.add(cardSemana);
        pnlCards.add(cardAlerta);
        pnlContent.add(pnlCards, BorderLayout.NORTH);

        JPanel pnlTables = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlTables.setOpaque(false);

        JPanel pnlAlerts = UIKit.card();
        pnlAlerts.setLayout(new BorderLayout(0, UIKit.SPACE_SM));
        pnlAlerts.add(UIKit.sectionHeader("Alertas de Inventario Critico", null), BorderLayout.NORTH);
        String[] alertCols = {"Producto", "Stock Actual", "Minimo"};
        modelAlert = new DefaultTableModel(alertCols, 0) { public boolean isCellEditable(int r, int c) { return false; }};
        pnlAlerts.add(new JScrollPane(UIKit.styledTable(modelAlert)), BorderLayout.CENTER);
        pnlTables.add(pnlAlerts);

        JPanel pnlRecentSales = UIKit.card();
        pnlRecentSales.setLayout(new BorderLayout(0, UIKit.SPACE_SM));
        pnlRecentSales.add(UIKit.sectionHeader("Ventas Recientes (En Vivo)", null), BorderLayout.NORTH);
        String[] salesCols = {"#", "Cliente", "Total", "Fecha"};
        modelSales = new DefaultTableModel(salesCols, 0) { public boolean isCellEditable(int r, int c) { return false; }};
        pnlRecentSales.add(new JScrollPane(UIKit.styledTable(modelSales)), BorderLayout.CENTER);
        pnlTables.add(pnlRecentSales);

        pnlContent.add(pnlTables, BorderLayout.CENTER);

        bgDashboardFrame.add(pnlContent);
        desktopPane.add(bgDashboardFrame);
        bgDashboardFrame.setVisible(true);

        desktopPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                bgDashboardFrame.setBounds(0, 0, desktopPane.getWidth(), desktopPane.getHeight());
            }
        });
        
        updateBgDashboard(); // Carga inicial
    }
    
    private void updateBgDashboard() {
        if (!bgDashboardFrame.isVisible()) return; // Solo actualiza si esta viendose
        
        Modelo.VentaDAO ventaDAO = new Modelo.VentaDAO();
        Modelo.ProductoDAO productoDAO = new Modelo.ProductoDAO();

        Modelo.UsuarioDAO udao = new Modelo.UsuarioDAO();
        for (Clases.Usuario u : udao.listarTodos()) {
            if (u.getUsuario().equals(Sesion.getUsuario())) {
                if (u.getSesionActiva() == 0) {
                    if (refreshTimer != null) refreshTimer.stop();
                    JOptionPane.showMessageDialog(this, "Su sesión ha sido iniciada en otro dispositivo.\nPor seguridad, la aplicación se cerrará.", "Sesión Finalizada", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
                break;
            }
        }

        java.math.BigDecimal ventasHoy = ventaDAO.totalVentasHoy();
        java.math.BigDecimal ventasSemana = ventaDAO.totalVentasSemana();
        int txnHoy = ventaDAO.conteoVentasHoy();
        int alertasStock = productoDAO.contarProductosStockBajo();

        if(lblVentasHoy != null) lblVentasHoy.setText("S/ " + ventasHoy.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString());
        if(lblSubVentasHoy != null) lblSubVentasHoy.setText(txnHoy > 0 ? txnHoy + " Transacciones" : "Sin ventas hoy");
        if(lblVentasSemana != null) lblVentasSemana.setText("S/ " + ventasSemana.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString());
        if(lblAlertasStock != null) lblAlertasStock.setText(alertasStock > 0 ? alertasStock + " Productos" : "Sin alertas");

        java.util.List<Object[]> alertas = productoDAO.productosConStockBajo();
        modelAlert.setRowCount(0);
        if (alertas.isEmpty()) {
            modelAlert.addRow(new Object[]{"Sin alertas de stock", "-", "-"});
        } else {
            for (Object[] a : alertas) { modelAlert.addRow(new Object[]{a[1], a[2], a[3]}); }
        }

        java.util.List<Object[]> ultimasVentas = ventaDAO.ultimasVentas(15);
        modelSales.setRowCount(0);
        if (ultimasVentas.isEmpty()) {
            modelSales.addRow(new Object[]{"--", "Sin ventas registradas", "S/ 0.00", "-"});
        } else {
            for (Object[] u : ultimasVentas) {
                modelSales.addRow(new Object[]{ u[0], u[1], "S/ " + ((java.math.BigDecimal)u[2]).setScale(2, java.math.RoundingMode.HALF_UP).toPlainString(), u[3] });
            }
        }
    }

    /**
     * Construye botón de menú lateral con estilo dark-navy.
     * Implementa el patrón de sidebar de la referencia web.
     */
    private JButton buildMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(UIKit.BODY);
        btn.setForeground(UIKit.SIDEBAR_TEXT);
        btn.setBackground(UIKit.SIDEBAR_BG);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
        btn.setPreferredSize(new Dimension(240, 40));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));

        btn.addActionListener(e -> {
            // Restablecer botón anteriormente seleccionado
            if (btnSeleccionado != null) {
                btnSeleccionado.setBackground(UIKit.SIDEBAR_BG);
                btnSeleccionado.setForeground(UIKit.SIDEBAR_TEXT);
                btnSeleccionado.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
            }
            // Marcar nuevo botón activo con borde izquierdo accent
            btnSeleccionado = btn;
            btnSeleccionado.setBackground(UIKit.SIDEBAR_ACTIVE);
            btnSeleccionado.setForeground(Color.WHITE);
            btnSeleccionado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, Color.WHITE),
                BorderFactory.createEmptyBorder(0, 13, 0, 16)));
        });

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn != btnSeleccionado) btn.setBackground(UIKit.SIDEBAR_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn != btnSeleccionado) btn.setBackground(UIKit.SIDEBAR_BG);
            }
        });
        return btn;
    }

    private void attachEvents() {
        btnPOS.addActionListener(e -> openFrame(new IFrmPuntoVenta(), "Punto de Venta"));
        btnDevoluciones.addActionListener(e -> openFrame(new IFrmDevoluciones(), "Devoluciones"));
        btnFidelizacion.addActionListener(e -> openFrame(new IFrmFidelizacion(), "Fidelización"));
        btnClientes.addActionListener(e -> openFrame(new IFrmGestionClientes(), "Gestión de Clientes"));
        btnProveedores.addActionListener(e -> openFrame(new IFrmGestionProveedores(), "Proveedores"));
        btnCategorÃ­as.addActionListener(e -> openFrame(new IFrmGestionCategorias(), "Categorías"));
        btnProductos.addActionListener(e -> openFrame(new IFrmGestionProductos(), "Productos"));
        btnKardex.addActionListener(e -> openFrame(new IFrmKardex(), "Kardex"));
        btnAlertasInventario.addActionListener(e -> openFrame(new IFrmAlertasInventario(), "Alertas de Inventario"));
        btnRepInventario.addActionListener(e -> openFrame(new IFrmReporteInventario(), "Reporte de Inventario"));
        btnCompras.addActionListener(e -> openFrame(new IFrmRegistroCompras(), "Registro de Compras"));
        btnFlujoCaja.addActionListener(e -> openFrame(new IFrmFlujoCaja(), "Flujo de Caja"));
        btnLibroMayor.addActionListener(e -> openFrame(new IFrmLibroMayor(), "Libro Mayor"));
        btnCuentasCP.addActionListener(e -> openFrame(new IFrmCuentasCobrarPagar(), "Cuentas Cobrar / Pagar"));
        btnRepVentas.addActionListener(e -> openFrame(new IFrmReporteVentas(), "Historial de Ventas"));
        btnReportesAvanzados.addActionListener(e -> openFrame(new IFrmReportesAvanzados(), "Reportes Avanzados"));
        btnEmpleados.addActionListener(e -> openFrame(new IFrmFichaEmpleados(), "Empleados"));
        btnPlanilla.addActionListener(e -> openFrame(new IFrmPlanillaAsistencia(), "Planilla y Asistencia"));
        btnEvaluacion.addActionListener(e -> openFrame(new IFrmEvaluacionDesempeno(), "Evaluación Desempeño"));
        btnUsuarios.addActionListener(e -> openFrame(new IFrmGestionUsuarios(), "Usuarios"));
        btnPermisos.addActionListener(e -> openFrame(new IFrmGestionPermisos(), "Permisos"));
        btnAuditoria.addActionListener(e -> openFrame(new IFrmBitacoraAuditoria(), "Auditoría"));
        btnConfig.addActionListener(e -> openFrame(new IFrmConfiguracionERP(), "Configuración"));

        btnLogout.addActionListener(e -> {
            int op = JOptionPane.showConfirmDialog(this, "Â¿Cerrar sesión?", "Salir", JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) {
                new Modelo.UsuarioDAO().cambiarEstadoSesion(Sesion.getUsuario(), 0);
                Utils.BitacoraService.registrar(Sesion.getUsuario(), Utils.BitacoraService.MOD_LOGIN, "LOGOUT", Utils.BitacoraService.OK, "Cierre de sesión manual");
                new FrmLogin().setVisible(true);
                this.dispose();
            }
        });
    }

    private void openFrame(JInternalFrame iframe, String frameName) {
        lblBreadcrumb.setText("Inicio > " + frameName);
        for (JInternalFrame f : desktopPane.getAllFrames()) {
            if (f != bgDashboardFrame) {
                f.dispose();
            }
        }
        
        iframe.putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
        desktopPane.add(iframe);
        iframe.setVisible(true);
        try {
            iframe.setSelected(true);
            iframe.setMaximum(true);
        } catch (java.beans.PropertyVetoException ex) {}
    }

    private void aplicarRol() {
        String rol = Sesion.getRol();
        if (rol == null) return;

        Modelo.PermisoDAO dao = new Modelo.PermisoDAO();

        // Si es ADMINISTRADOR, por defecto PermisoDAO devuelve true para todo.
        boolean puedeVentas = dao.tienePermiso(rol, "VENTAS");
        boolean puedeInventario = dao.tienePermiso(rol, "INVENTARIO");
        boolean puedeCompras = dao.tienePermiso(rol, "COMPRAS");
        boolean puedeFinanzas = dao.tienePermiso(rol, "FINANZAS");
        boolean puedePersonal = dao.tienePermiso(rol, "PERSONAL");
        boolean puedeReportes = dao.tienePermiso(rol, "REPORTES");
        boolean puedeAdmin = dao.tienePermiso(rol, "ADMINISTRACION");

        btnPOS.setVisible(puedeVentas);
        btnDevoluciones.setVisible(puedeVentas);
        btnFidelizacion.setVisible(puedeVentas);
        btnClientes.setVisible(puedeVentas);

        grpInventario.setVisible(puedeInventario);

        grpCompras.setVisible(puedeCompras);
        btnProveedores.setVisible(puedeCompras);

        btnFlujoCaja.setVisible(puedeFinanzas);
        btnLibroMayor.setVisible(puedeFinanzas);
        btnCuentasCP.setVisible(puedeFinanzas);

        grpPersonal.setVisible(puedePersonal);

        btnRepVentas.setVisible(puedeReportes);
        btnReportesAvanzados.setVisible(puedeReportes);

        grpAdmin.setVisible(puedeAdmin);
    }

    private void configFrame() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 720));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (Sesion.getUsuario() != null) {
                    new Modelo.UsuarioDAO().cambiarEstadoSesion(Sesion.getUsuario(), 0);
                }
            }
        });
    }
}
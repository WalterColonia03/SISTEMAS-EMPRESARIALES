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

/**
 * FrmDashboard - Contenedor principal MDI (pantalla completa) con menú lateral.
 * Rediseñado con UIKit (Patrones F y navegación agrupada).
 */
public class FrmDashboard extends JFrame {

    private JDesktopPane desktopPane;
    private JInternalFrame bgDashboardFrame;
    private JPanel pnlTopBar;
    private JLabel lblBreadcrumb;
    
    // Botones del menú lateral agrupados
    // Ventas
    private JButton btnPOS, btnVentas, btnDevoluciones, btnFidelizacion;
    // Clientes y Proveedores
    private JButton btnClientes, btnProveedores;
    // Inventario
    private JButton btnCategorias, btnProductos, btnKardex, btnAlertasInventario, btnRepInventario;
    // Compras
    private JButton btnCompras;
    // Finanzas y Reportes
    private JButton btnFlujoCaja, btnLibroMayor, btnCuentasCP, btnRepVentas, btnReportesAvanzados;
    // Personal
    private JButton btnEmpleados, btnPlanilla;
    // Administración
    private JButton btnUsuarios, btnAuditoria, btnConfig;
    
    private JButton btnLogout;
    
    // Botón actualmente seleccionado en el sidebar
    private JButton btnSeleccionado = null;

    // Seguridad: Timer de Inactividad (FR-040)
    private Timer inactividadTimer;
    private static final int TIEMPO_INACTIVIDAD = 30 * 60 * 1000; // 30 minutos

    public FrmDashboard() {
        super("Minimarket LAREDO - Sistema ERP");
        initComponents();
        buildLayout();
        attachEvents();
        aplicarRol();
        configFrame();
        iniciarTimerInactividad();
    }

    private void iniciarTimerInactividad() {
        inactividadTimer = new Timer(TIEMPO_INACTIVIDAD, e -> {
            // FR-020-v2 CA-2: registrar cierre por inactividad
            Utils.BitacoraService.registrar(
                Sesion.getUsuario(),
                Utils.BitacoraService.MOD_LOGIN,
                "CIERRE_INACTIVIDAD",
                Utils.BitacoraService.OK,
                "Sesion cerrada por inactividad (30 min)"
            );
            JOptionPane.showMessageDialog(this, "Sesión cerrada por inactividad (30 minutos).", "Seguridad", JOptionPane.WARNING_MESSAGE);
            new FrmLogin().setVisible(true);
            this.dispose();
        });
        inactividadTimer.setRepeats(false);
        inactividadTimer.start();

        long eventMask = AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK;
        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            inactividadTimer.restart();
        }, eventMask);
    }

    private void initComponents() {
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(UIKit.BG_APP);

        // Instanciar botones del menú sin emojis
        btnPOS = buildMenuButton("Punto de Venta (POS)");
        btnVentas = buildMenuButton("Gestión de Ventas");
        btnDevoluciones = buildMenuButton("Devoluciones");
        btnFidelizacion = buildMenuButton("Fidelización");
        
        btnClientes = buildMenuButton("Clientes");
        btnProveedores = buildMenuButton("Proveedores");
        
        btnCategorias = buildMenuButton("Categorías");
        btnProductos = buildMenuButton("Productos");
        btnKardex = buildMenuButton("Kardex");
        btnAlertasInventario = buildMenuButton("Alertas de Inventario");
        btnRepInventario = buildMenuButton("Reporte de Inventario");
        
        btnCompras = buildMenuButton("Registro de Compras");
        
        btnFlujoCaja            = buildMenuButton("Flujo de Caja");
        btnLibroMayor           = buildMenuButton("Libro Mayor");
        btnCuentasCP            = buildMenuButton("Cuentas por Cobrar y Pagar");
        btnRepVentas            = buildMenuButton("Reporte de Ventas");
        btnReportesAvanzados    = buildMenuButton("Reportes Avanzados (BI)");
        
        btnEmpleados = buildMenuButton("Ficha de Empleados");
        btnPlanilla = buildMenuButton("Planilla y Asistencia");
        
        btnUsuarios = buildMenuButton("Gestión Usuarios");
        btnAuditoria = buildMenuButton("Bitácora Auditoría");
        btnConfig = buildMenuButton("Configuración ERP");
        
        btnLogout = buildMenuButton("Cerrar Sesión");
    }

    private void buildLayout() {
        setLayout(new BorderLayout());

        // ── Menú lateral (WEST) ──────────────────────────
        JPanel pnlSidebar = new JPanel();
        pnlSidebar.setLayout(new BoxLayout(pnlSidebar, BoxLayout.Y_AXIS));
        pnlSidebar.setBackground(UIKit.PRIMARY);
        pnlSidebar.setPreferredSize(new Dimension(260, 0));
        pnlSidebar.setBorder(new EmptyBorder(15, 0, 15, 0));

        // Logo / Cabecera Sidebar
        JLabel lblLogo = new JLabel("Minimarket LAREDO", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setBorder(new EmptyBorder(10, 0, 20, 0));
        pnlSidebar.add(lblLogo);

        // Contenedor scrolleable para el sidebar
        JPanel pnlMenuContainer = new JPanel();
        pnlMenuContainer.setLayout(new BoxLayout(pnlMenuContainer, BoxLayout.Y_AXIS));
        pnlMenuContainer.setBackground(UIKit.PRIMARY);

        pnlMenuContainer.add(navGroup("Ventas", btnPOS, btnVentas, btnDevoluciones, btnFidelizacion));
        pnlMenuContainer.add(navGroup("Clientes y Proveedores", btnClientes, btnProveedores));
        pnlMenuContainer.add(navGroup("Inventario", btnCategorias, btnProductos, btnKardex, btnAlertasInventario, btnRepInventario));
        pnlMenuContainer.add(navGroup("Compras", btnCompras));
        pnlMenuContainer.add(navGroup("Finanzas y Reportes",
            btnFlujoCaja, btnLibroMayor, btnCuentasCP,
            btnRepVentas, btnReportesAvanzados));
        pnlMenuContainer.add(navGroup("Personal", btnEmpleados, btnPlanilla));
        pnlMenuContainer.add(navGroup("Administración", btnUsuarios, btnAuditoria, btnConfig));
        pnlMenuContainer.add(Box.createVerticalGlue());
        
        JScrollPane scrollMenu = new JScrollPane(pnlMenuContainer);
        scrollMenu.setBorder(null);
        scrollMenu.setOpaque(false);
        scrollMenu.getViewport().setOpaque(false);
        scrollMenu.getVerticalScrollBar().setUnitIncrement(16);
        pnlSidebar.add(scrollMenu);
        
        // El boton logout queda fijo al fondo
        JPanel pnlLogout = new JPanel(new BorderLayout());
        pnlLogout.setOpaque(false);
        pnlLogout.setBorder(new EmptyBorder(10, 0, 0, 0));
        pnlLogout.add(btnLogout, BorderLayout.CENTER);
        pnlSidebar.add(pnlLogout);

        add(pnlSidebar, BorderLayout.WEST);

        // ── Top Bar y JDesktopPane (CENTER) ────────────────────────
        JPanel pnlCenter = new JPanel(new BorderLayout());
        
        pnlTopBar = new JPanel(new BorderLayout());
        pnlTopBar.setBackground(Color.WHITE);
        pnlTopBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UIKit.BORDER),
                new EmptyBorder(12, 24, 12, 24)
        ));
        
        lblBreadcrumb = new JLabel("Inicio › Panel de Control ERP");
        lblBreadcrumb.setFont(UIKit.BODY_BOLD);
        lblBreadcrumb.setForeground(UIKit.TEXT_PRIMARY);
        pnlTopBar.add(lblBreadcrumb, BorderLayout.WEST);
        
        JPanel pnlUserInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlUserInfo.setOpaque(false);
        
        JTextField txtSearchGlobal = UIKit.textField();
        txtSearchGlobal.setPreferredSize(new Dimension(200, 32));
        txtSearchGlobal.putClientProperty("JTextField.placeholderText", "Buscar módulo...");
        pnlUserInfo.add(txtSearchGlobal);
        
        String nombreUsuario = Sesion.getUsuario() != null ? Sesion.getUsuario() : "Admin";
        String rolUsuario = Sesion.getRol() != null ? Sesion.getRol() : "Administrador";
        JLabel lblUser = new JLabel(nombreUsuario + " (" + rolUsuario + ")");
        lblUser.setFont(UIKit.BODY);
        lblUser.setForeground(UIKit.TEXT_SECONDARY);
        pnlUserInfo.add(lblUser);
        
        pnlTopBar.add(pnlUserInfo, BorderLayout.EAST);
        
        pnlCenter.add(pnlTopBar, BorderLayout.NORTH);
        pnlCenter.add(desktopPane, BorderLayout.CENTER);
        
        add(pnlCenter, BorderLayout.CENTER);

        // Inicializar resumen dashboard de fondo
        initBgDashboard();
    }

    private JPanel navGroup(String titulo, JButton... botones) {
        JPanel grupo = new JPanel();
        grupo.setOpaque(false);
        grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));
        grupo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblGrupo = new JLabel(titulo.toUpperCase());
        lblGrupo.setFont(UIKit.CAPTION);
        lblGrupo.setForeground(new Color(255, 255, 255, 130)); 
        lblGrupo.setBorder(new EmptyBorder(UIKit.SPACE_MD, UIKit.SPACE_MD, UIKit.SPACE_XS, UIKit.SPACE_MD));
        grupo.add(lblGrupo);

        for (JButton b : botones) {
            grupo.add(b);
            grupo.add(Box.createVerticalStrut(2));
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

        // Fila de Tarjetas KPI
        JPanel pnlCards = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlCards.setOpaque(false);
        pnlCards.setPreferredSize(new Dimension(0, 100));

        pnlCards.add(UIKit.kpiCard("VENTAS HOY", "S/ 1,245.50", "12 Transacciones", UIKit.ACCENT));
        pnlCards.add(UIKit.kpiCard("VENTAS SEMANA", "S/ 8,720.00", "84 Transacciones", UIKit.SUCCESS));
        pnlCards.add(UIKit.kpiCard("ALERTAS DE STOCK", "4 Productos", "Requieren reposición", UIKit.WARNING));

        pnlContent.add(pnlCards, BorderLayout.NORTH);

        // Tablas inferiores (Alertas de Stock y Ventas Recientes)
        JPanel pnlTables = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlTables.setOpaque(false);

        // Panel de Alertas de Stock
        JPanel pnlAlerts = UIKit.card();
        pnlAlerts.setLayout(new BorderLayout(0, UIKit.SPACE_SM));
        pnlAlerts.add(UIKit.sectionHeader("Alertas de Inventario Crítico", null), BorderLayout.NORTH);

        String[] alertCols = {"Producto", "Stock Actual", "Mínimo"};
        Object[][] alertData = {
            {"Leche Gloria 1L", "5 unidades", "12 unidades"},
            {"Arroz Costeño 5kg", "2 unidades", "10 unidades"},
            {"Aceite Primor 1L", "3 unidades", "8 unidades"},
            {"Fideos Don Vittorio", "4 unidades", "15 unidades"}
        };
        JTable tblAlerts = UIKit.styledTable(new DefaultTableModel(alertData, alertCols));
        pnlAlerts.add(new JScrollPane(tblAlerts), BorderLayout.CENTER);
        pnlTables.add(pnlAlerts);

        // Panel de Ventas Recientes
        JPanel pnlRecentSales = UIKit.card();
        pnlRecentSales.setLayout(new BorderLayout(0, UIKit.SPACE_SM));
        pnlRecentSales.add(UIKit.sectionHeader("Ventas Recientes", null), BorderLayout.NORTH);

        String[] salesCols = {"Hora", "Cliente", "Total"};
        Object[][] salesData = {
            {"22:30", "Juan Perez", "S/ 45.00"},
            {"22:15", "Maria Gomez", "S/ 120.50"},
            {"21:50", "Carlos Lopez", "S/ 15.20"},
            {"21:30", "Ana Torres", "S/ 89.90"}
        };
        JTable tblSales = UIKit.styledTable(new DefaultTableModel(salesData, salesCols));
        pnlRecentSales.add(new JScrollPane(tblSales), BorderLayout.CENTER);
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
    }

    private JButton buildMenuButton(String text) {
        JButton btn = new JButton("  " + text);
        btn.setFont(UIKit.BODY);
        btn.setForeground(Color.WHITE);
        btn.setBackground(UIKit.PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Short.MAX_VALUE, 44));
        btn.setPreferredSize(new Dimension(260, 44));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));

        btn.addActionListener(e -> {
            if (btnSeleccionado != null) {
                btnSeleccionado.setBackground(UIKit.PRIMARY);
                btnSeleccionado.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
            }
            btnSeleccionado = btn;
            btnSeleccionado.setBackground(UIKit.PRIMARY_DARK);
            btnSeleccionado.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 3, 0, 0, UIKit.ACCENT),
                    BorderFactory.createEmptyBorder(0, 13, 0, 16)
            ));
        });

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { 
                if (btn != btnSeleccionado) btn.setBackground(UIKit.PRIMARY_DARK); 
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e)  { 
                if (btn != btnSeleccionado) btn.setBackground(UIKit.PRIMARY); 
            }
        });
        return btn;
    }

    private void attachEvents() {
        btnPOS.addActionListener(e -> openFrame(new IFrmPuntoVenta(), "Punto de Venta"));
        btnVentas.addActionListener(e -> openFrame(new GestionarVentas(), "Gestión de Ventas"));
        btnDevoluciones.addActionListener(e -> openFrame(new IFrmDevoluciones(), "Devoluciones"));
        btnFidelizacion.addActionListener(e -> openFrame(new IFrmFidelizacion(), "Fidelización"));
        
        btnClientes.addActionListener(e -> openFrame(new IFrmGestionClientes(), "Gestión de Clientes"));
        btnProveedores.addActionListener(e -> openFrame(new IFrmGestionProveedores(), "Proveedores"));
        
        btnCategorias.addActionListener(e -> openFrame(new IFrmGestionCategorias(), "Categorías"));
        btnProductos.addActionListener(e -> openFrame(new IFrmGestionProductos(), "Productos"));
        btnKardex.addActionListener(e -> openFrame(new IFrmKardex(), "Kardex"));
        btnAlertasInventario.addActionListener(e -> openFrame(new IFrmAlertasInventario(), "Alertas de Inventario"));
        btnRepInventario.addActionListener(e -> openFrame(new IFrmReporteInventario(), "Reporte de Inventario"));
        
        btnCompras.addActionListener(e -> openFrame(new IFrmRegistroCompras(), "Registro de Compras"));
        
        btnFlujoCaja.addActionListener(e -> openFrame(new IFrmFlujoCaja(), "Flujo de Caja"));
        btnLibroMayor.addActionListener(e -> openFrame(new IFrmLibroMayor(), "Libro Mayor"));
        btnCuentasCP.addActionListener(e -> openFrame(new IFrmCuentasCobrarPagar(), "Cuentas Cobrar / Pagar"));
        btnRepVentas.addActionListener(e -> openFrame(new IFrmReporteVentas(), "Reporte de Ventas"));
        // FR-006 CA-5: solo Gerente/Admin — la restricción de visibilidad está en aplicarRol()
        btnReportesAvanzados.addActionListener(e -> openFrame(new IFrmReportesAvanzados(), "Reportes Avanzados"));
        
        btnEmpleados.addActionListener(e -> openFrame(new IFrmFichaEmpleados(), "Empleados"));
        btnPlanilla.addActionListener(e -> openFrame(new IFrmPlanillaAsistencia(), "Planilla y Asistencia"));
        
        btnUsuarios.addActionListener(e -> openFrame(new IFrmGestionUsuarios(), "Usuarios"));
        btnAuditoria.addActionListener(e -> openFrame(new IFrmBitacoraAuditoria(), "Auditoría"));
        btnConfig.addActionListener(e -> openFrame(new IFrmConfiguracionERP(), "Configuración"));

        btnLogout.addActionListener(e -> {
            int op = JOptionPane.showConfirmDialog(this, "¿Cerrar sesión?", "Salir", JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) {
                // FR-020-v2 CA-1: registrar cierre de sesión manual
                Utils.BitacoraService.registrar(
                    Sesion.getUsuario(),
                    Utils.BitacoraService.MOD_LOGIN,
                    "LOGOUT",
                    Utils.BitacoraService.OK,
                    "Cierre de sesión manual"
                );
                new FrmLogin().setVisible(true);
                this.dispose();
            }
        });
    }

    private void openFrame(JInternalFrame iframe, String frameName) {
        lblBreadcrumb.setText("Inicio › " + frameName);
        for (JInternalFrame f : desktopPane.getAllFrames()) {
            if (f != bgDashboardFrame) {
                f.dispose();
            }
        }
        
        // Estandarizar la barra de título de la ventana interna
        iframe.putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
        
        desktopPane.add(iframe);
        iframe.setVisible(true);
        try {
            iframe.setSelected(true);
        } catch (java.beans.PropertyVetoException ex) {
            ex.printStackTrace();
        }
        Dimension desktopSize = desktopPane.getSize();
        Dimension jInternalFrameSize = iframe.getSize();
        iframe.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
    }

    private void aplicarRol() {
        String rol = Sesion.getRol();
        if (rol != null && rol.equalsIgnoreCase("Vendedor")) {
            btnUsuarios.setVisible(false);
            btnAuditoria.setVisible(false);
            btnRepVentas.setVisible(false);
            btnRepInventario.setVisible(false);
            btnConfig.setVisible(false);
            btnCompras.setVisible(false);
            btnEmpleados.setVisible(false);
            btnPlanilla.setVisible(false);
            btnFlujoCaja.setVisible(false);
            btnLibroMayor.setVisible(false);
            // FR-006 CA-5: Reportes Avanzados solo para Gerente/Administrador
            btnReportesAvanzados.setVisible(false);
        }
    }

    private void configFrame() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 720));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

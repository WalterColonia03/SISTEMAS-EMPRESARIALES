package Vista;

import Clases.Categoria;
import Clases.Kardex;
import Clases.Producto;
import Clases.Proveedor;
import Clases.Sesion;
import Modelo.CategoriaDAO;
import Modelo.KardexDAO;
import Modelo.ProductoDAO;
import Modelo.ProveedorDAO;
import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Módulo de Inventario / Kardex — ERP Minimarket LAREDO.
 * Capa: Vista — Implementa: FR-004, FR-052 (alertas), RNF-06 (bitácora).
 * Diseño: form de Registro de Entrada en la parte superior + historial de movimientos abajo.
 * Inspirado en el panel "Inventario" de la referencia web.
 */
public class IFrmKardex extends JInternalFrame {

    // ── Formulario de entrada ──
    private JComboBox<String> cbProductoEntrada;
    private JComboBox<String> cbProveedorEntrada;
    private JTextField txtCantidadEntrada;
    private JTextField txtVencimientoEntrada;
    private JButton btnRegistrarEntrada;
    private JButton btnEntradas;
    private JButton btnBajas;

    // ── Filtros del historial ──
    private JComboBox<String> cbCategoria;
    private JComboBox<String> cbProducto;
    private JTextField txtDesde;
    private JTextField txtHasta;
    private JButton btnFiltrar;

    // ── Tabla historial ──
    private JTable tblKardex;
    private DefaultTableModel modelKardex;

    // ── KPI cards ──
    private JLabel lblStockActual;
    private JLabel lblEntradas;
    private JLabel lblSalidas;

    private boolean mostrandoEntradas = true;

    public IFrmKardex() {
        super("Inventario — Kardex", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        cargarMovimientos();
        setSize(1100, 720);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    private void initComponents() {
        // Combos de la parte superior (registrar entrada)
        cbProductoEntrada  = new JComboBox<>(); cbProductoEntrada.setFont(UIKit.BODY); cbProductoEntrada.setPreferredSize(new Dimension(200,38));
        cbProveedorEntrada = new JComboBox<>(); cbProveedorEntrada.setFont(UIKit.BODY); cbProveedorEntrada.setPreferredSize(new Dimension(200,38));
        txtCantidadEntrada   = UIKit.textField(); UIKit.addIntegerValidator(txtCantidadEntrada,6); txtCantidadEntrada.setPreferredSize(new Dimension(100,38));
        txtVencimientoEntrada= UIKit.textField(); txtVencimientoEntrada.setPreferredSize(new Dimension(140,38));
        txtVencimientoEntrada.putClientProperty("JTextField.placeholderText","dd/mm/aaaa");

        btnRegistrarEntrada = UIKit.primaryButton("✔ Registrar Entrada");
        btnRegistrarEntrada.setBackground(UIKit.SUCCESS);
        btnEntradas = UIKit.primaryButton("Entradas");
        btnBajas    = UIKit.secondaryButton("Bajas");

        // Cargar productos y proveedores
        ProductoDAO pdao = new ProductoDAO();
        cbProductoEntrada.addItem("Seleccionar...");
        for (Producto p : pdao.listarTodos()) {
            cbProductoEntrada.addItem(p.getIdProducto() + " - " + p.getNombre());
        }
        ProveedorDAO pvdao = new ProveedorDAO();
        cbProveedorEntrada.addItem("Seleccionar...");
        for (Proveedor pv : pvdao.listarTodos()) {
            cbProveedorEntrada.addItem(pv.getIdProveedor() + " - " + pv.getRazonSocial());
        }

        // Filtros historial
        cbCategoria = new JComboBox<>(); cbCategoria.setFont(UIKit.BODY); cbCategoria.setPreferredSize(new Dimension(170,38));
        cbCategoria.addItem("Todas");
        for (Categoria c : new CategoriaDAO().listarTodos()) cbCategoria.addItem(c.getIdCategoria() + " - " + c.getDescripcion());

        cbProducto = new JComboBox<>(); cbProducto.setFont(UIKit.BODY); cbProducto.setPreferredSize(new Dimension(170,38));
        cbProducto.addItem("Todos");
        for (Producto p : pdao.listarTodos()) cbProducto.addItem(p.getIdProducto() + " - " + p.getNombre());

        txtDesde = UIKit.textField(); txtDesde.setPreferredSize(new Dimension(120,38)); txtDesde.putClientProperty("JTextField.placeholderText","dd/mm/aaaa");
        txtHasta = UIKit.textField(); txtHasta.setPreferredSize(new Dimension(120,38)); txtHasta.putClientProperty("JTextField.placeholderText","dd/mm/aaaa");
        btnFiltrar = UIKit.primaryButton("Aplicar");

        // Tabla historial de movimientos
        String[] cols = {"Producto","Proveedor","Cantidad","Vencimiento","Registrado por","Fecha"};
        modelKardex = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblKardex = UIKit.styledTable(modelKardex);
        tblKardex.getColumnModel().getColumn(2).setMaxWidth(100);

        // Badge de color para columna Cantidad (+/-)
        tblKardex.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                String val = v != null ? v.toString() : "";
                if (val.startsWith("+")) { comp.setForeground(UIKit.SUCCESS); }
                else if (val.startsWith("-")) { comp.setForeground(UIKit.DANGER); }
                else { comp.setForeground(UIKit.TEXT_PRIMARY); }
                setFont(UIKit.BODY_BOLD);
                setHorizontalAlignment(SwingConstants.CENTER);
                return comp;
            }
        });

        // KPIs
        lblStockActual = new JLabel("0"); lblStockActual.setFont(UIKit.KPI_VALUE); lblStockActual.setForeground(UIKit.PRIMARY);
        lblEntradas    = new JLabel("0"); lblEntradas.setFont(UIKit.KPI_VALUE); lblEntradas.setForeground(UIKit.SUCCESS);
        lblSalidas     = new JLabel("0"); lblSalidas.setFont(UIKit.KPI_VALUE); lblSalidas.setForeground(UIKit.DANGER);
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout(0, UIKit.SPACE_MD));
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        // Header
        JPanel pnlHeaderBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT,UIKit.SPACE_SM,0));
        pnlHeaderBtns.setOpaque(false);
        pnlHeaderBtns.add(btnEntradas); pnlHeaderBtns.add(btnBajas);
        getContentPane().add(UIKit.screenHeader("Inventario","Inicio / Inventario", pnlHeaderBtns), BorderLayout.NORTH);

        // ══ SECCIÓN REGISTRAR ENTRADA ══
        JPanel pnlEntrada = UIKit.card();
        pnlEntrada.setLayout(new BorderLayout(0, UIKit.SPACE_SM));

        JLabel lblTitEntry = new JLabel("Registrar Entrada");
        lblTitEntry.setFont(UIKit.H2); lblTitEntry.setForeground(UIKit.TEXT_PRIMARY);
        pnlEntrada.add(lblTitEntry, BorderLayout.NORTH);

        JPanel pnlForm = new JPanel(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_MD, 0));
        pnlForm.setOpaque(false);

        JPanel gProducto  = formGroup("Producto", cbProductoEntrada);
        JPanel gProveedor = formGroup("Proveedor", cbProveedorEntrada);
        JPanel gCantidad  = formGroup("Cantidad", txtCantidadEntrada);
        JPanel gVenc      = formGroup("Vencimiento", txtVencimientoEntrada);

        pnlForm.add(gProducto); pnlForm.add(gProveedor);
        pnlForm.add(gCantidad); pnlForm.add(gVenc);
        pnlForm.add(btnRegistrarEntrada);
        pnlEntrada.add(pnlForm, BorderLayout.CENTER);

        // ══ SECCIÓN HISTORIAL ══
        JPanel pnlHistorial = UIKit.card();
        pnlHistorial.setLayout(new BorderLayout(0, UIKit.SPACE_SM));

        JLabel lblTitHist = new JLabel("Historial de Entradas");
        lblTitHist.setFont(UIKit.H2); lblTitHist.setForeground(UIKit.TEXT_PRIMARY);
        pnlHistorial.add(lblTitHist, BorderLayout.NORTH);

        // Filtros del historial
        JPanel pnlFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_SM, 0));
        pnlFiltros.setOpaque(false);
        pnlFiltros.add(new JLabel("Filtrar:")); pnlFiltros.setFont(UIKit.BODY);
        pnlFiltros.add(formGroup("Desde", txtDesde));
        pnlFiltros.add(formGroup("Hasta", txtHasta));
        pnlFiltros.add(formGroup("Producto", cbProducto));
        pnlFiltros.add(btnFiltrar);
        pnlHistorial.add(pnlFiltros, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(tblKardex);
        scroll.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        pnlHistorial.add(scroll, BorderLayout.SOUTH);
        pnlHistorial.setLayout(new BorderLayout(0, UIKit.SPACE_SM));
        pnlHistorial.add(lblTitHist, BorderLayout.NORTH);
        pnlHistorial.add(pnlFiltros, BorderLayout.CENTER);
        pnlHistorial.add(scroll, BorderLayout.SOUTH);

        // ── KPIs resumen ──
        JPanel pnlKpis = new JPanel(new GridLayout(1,3, UIKit.SPACE_MD, 0));
        pnlKpis.setOpaque(false);
        pnlKpis.add(UIKit.kpiCard("Stock Total","0","Unidades en inventario", UIKit.PRIMARY,"📦"));
        pnlKpis.add(UIKit.kpiCard("Total Entradas","0","Este mes", UIKit.SUCCESS,"↑"));
        pnlKpis.add(UIKit.kpiCard("Total Salidas","0","Este mes", UIKit.DANGER,"↓"));

        JPanel centro = new JPanel(new BorderLayout(0, UIKit.SPACE_MD));
        centro.setOpaque(false);
        centro.add(pnlEntrada, BorderLayout.NORTH);
        centro.add(pnlHistorial, BorderLayout.CENTER);

        getContentPane().add(centro, BorderLayout.CENTER);
    }

    /** Crea un grupo label + componente apilados verticalmente. */
    private JPanel formGroup(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0,4));
        p.setOpaque(false);
        p.add(UIKit.fieldLabel(label), BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private void attachEvents() {
        btnFiltrar.addActionListener(e -> cargarMovimientos());

        // Registrar entrada
        btnRegistrarEntrada.addActionListener(e -> {
            if (cbProductoEntrada.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this,"Seleccione un producto.","Aviso",JOptionPane.INFORMATION_MESSAGE); return;
            }
            String cantStr = txtCantidadEntrada.getText().trim();
            if (cantStr.isEmpty() || Integer.parseInt(cantStr) <= 0) {
                JOptionPane.showMessageDialog(this,"Ingrese una cantidad válida.","Error",JOptionPane.ERROR_MESSAGE); return;
            }
            int idProd = Integer.parseInt(cbProductoEntrada.getSelectedItem().toString().split(" - ")[0]);
            int cant   = Integer.parseInt(cantStr);
            String venc= txtVencimientoEntrada.getText().trim();

            // Actualizar stock en ProductoDAO
            ProductoDAO pdao = new ProductoDAO();
            Producto prod = pdao.listarTodos().stream().filter(p -> p.getIdProducto()==idProd).findFirst().orElse(null);
            if (prod == null) return;
            prod.setCantidad(prod.getCantidad() + cant);
            pdao.actualizar(prod);

            // Registrar en Kardex
            KardexDAO kdao = new KardexDAO();
            Kardex k = new Kardex();
            k.setIdProducto(idProd); k.setTipoMovimiento("ENTRADA"); k.setCantidad(cant);
            k.setMotivo("Entrada registrada por " + Sesion.getUsuario());
            k.setFecha(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
            kdao.guardar(k);

            // Bitácora
            Utils.BitacoraService.registrar(Sesion.getUsuario(),
                Utils.BitacoraService.MOD_INVENTARIO,"ENTRADA_KARDEX",
                Utils.BitacoraService.OK,"Producto ID:"+idProd+" Cant:+"+cant);

            JOptionPane.showMessageDialog(this,"✅ Entrada registrada correctamente.");
            txtCantidadEntrada.setText(""); txtVencimientoEntrada.setText("");
            cargarMovimientos();
        });

        btnEntradas.addActionListener(e -> { mostrandoEntradas=true; cargarMovimientos(); });
        btnBajas.addActionListener(e -> { mostrandoEntradas=false; cargarMovimientos(); });
    }

    /** Carga movimientos del Kardex aplicando filtros activos. */
    private void cargarMovimientos() {
        modelKardex.setRowCount(0);
        KardexDAO kdao = new KardexDAO();
        ProductoDAO pdao = new ProductoDAO();
        ProveedorDAO pvdao = new ProveedorDAO();

        List<Object[]> movs = kdao.listarTodosDesc();
        int totEnt = 0, totSal = 0;

        for (Object[] row : movs) {
            // row: [fecha, producto, tipo, cantidad, documento, motivo]
            String tipo = row.length > 2 ? row[2].toString() : "";
            boolean esEntrada = "ENTRADA".equalsIgnoreCase(tipo);
            if (mostrandoEntradas && !esEntrada) continue;
            if (!mostrandoEntradas && esEntrada) continue;

            int cant = row.length > 3 ? (int)row[3] : 0;
            if (esEntrada) totEnt += cant; else totSal += cant;

            modelKardex.addRow(new Object[]{
                row.length>1 ? row[1] : "",   // Producto
                "-",                            // Proveedor (placeholder)
                (esEntrada ? "+" : "-") + cant, // Cantidad coloreada
                "-",                            // Vencimiento
                "Sistema",                      // Registrado por
                row[0]                          // Fecha
            });
        }
        lblEntradas.setText(String.valueOf(totEnt));
        lblSalidas.setText(String.valueOf(totSal));
        // Stock total
        int stock = pdao.listarTodos().stream().mapToInt(Producto::getCantidad).sum();
        lblStockActual.setText(String.valueOf(stock));
    }
}

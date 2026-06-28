package Vista;

import Clases.Kardex;
import Clases.Producto;
import Clases.Categoria;
import Modelo.KardexDAO;
import Modelo.ProductoDAO;
import Modelo.CategoriaDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Formulario interno para mostrar el Kardex (Historial de Entradas y Salidas).
 * Capa: Vista — Implementa: FR-004 (Visualizar Kardex) y Filtros por Categoría.
 */
public class IFrmKardex extends JInternalFrame {

    private JComboBox<String> cbCategoria;
    private JComboBox<String> cbProducto;
    private JTable tblKardex;
    private DefaultTableModel modelKardex;
    private JLabel lblStockActual;
    private JLabel lblEntradas;
    private JLabel lblSalidas;

    private JButton btnRefrescar;

    private static final Color COLOR_PRIMARY = new Color(25, 118, 210);
    private static final Color COLOR_ACCENT = new Color(46, 125, 50);

    public IFrmKardex() {
        super("Kardex - Historial de Entradas y Salidas", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        cargarMovimientos();
        setSize(850, 550);
    }

    private void initComponents() {
        cbCategoria = new JComboBox<>();
        cbCategoria.addItem("Todas las categorías");
        CategoriaDAO cdao = new CategoriaDAO();
        for (Categoria c : cdao.listarTodos()) {
            cbCategoria.addItem(c.getIdCategoria() + " - " + c.getDescripcion());
        }

        cbProducto = new JComboBox<>();
        actualizarComboProductos();
        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setBackground(COLOR_PRIMARY);
        btnRefrescar.setForeground(Color.WHITE);

        lblStockActual = new JLabel("0");
        lblStockActual.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblStockActual.setForeground(COLOR_PRIMARY);

        lblEntradas = new JLabel("0");
        lblEntradas.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblEntradas.setForeground(COLOR_ACCENT);

        lblSalidas = new JLabel("0");
        lblSalidas.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSalidas.setForeground(new Color(198, 40, 40));

        String[] columns = {"Fecha", "Tipo", "Cantidad", "Stock Despu\u00e9s", "Documento", "Observaci\u00f3n"};
        modelKardex = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblKardex = new JTable(modelKardex);
    }

    /**
     * Actualiza la lista de productos basada en la categoría seleccionada.
     * Capa: Vista — Implementa: Lógica de filtros dependientes.
     */
    private void actualizarComboProductos() {
        cbProducto.removeAllItems();
        cbProducto.addItem("Todos los productos");
        
        ProductoDAO pdao = new ProductoDAO();
        List<Producto> productos = pdao.listarTodos();
        
        int idCategoriaSel = -1;
        if (cbCategoria != null && cbCategoria.getSelectedIndex() > 0) {
            String catStr = cbCategoria.getSelectedItem().toString();
            idCategoriaSel = Integer.parseInt(catStr.split(" - ")[0]);
        }
        
        for (Producto p : productos) {
            if (idCategoriaSel == -1 || p.getIdCategoria() == idCategoriaSel) {
                cbProducto.addItem(p.getIdProducto() + " - " + p.getNombre());
            }
        }
    }

    private void buildLayout() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel pnlTop = new JPanel(new BorderLayout(10, 10));
        pnlTop.setBorder(BorderFactory.createTitledBorder("Seleccionar Producto"));

        JPanel pnlSelector = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSelector.add(new JLabel("Categoría:"));
        pnlSelector.add(cbCategoria);
        pnlSelector.add(new JLabel("Producto:"));
        pnlSelector.add(cbProducto);
        pnlSelector.add(btnRefrescar);
        pnlTop.add(pnlSelector, BorderLayout.WEST);

        JPanel pnlResumen = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
        pnlResumen.setBorder(new EmptyBorder(5, 0, 5, 0));

        JPanel pnlStock = new JPanel(new GridBagLayout());
        pnlStock.setBackground(new Color(232, 240, 254));
        pnlStock.setBorder(BorderFactory.createLineBorder(new Color(187, 222, 251)));
        pnlStock.setPreferredSize(new Dimension(150, 60));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        pnlStock.add(new JLabel("Stock Actual"), gbc);
        gbc.gridy = 1;
        pnlStock.add(lblStockActual, gbc);

        JPanel pnlEntradas = new JPanel(new GridBagLayout());
        pnlEntradas.setBackground(new Color(232, 245, 233));
        pnlEntradas.setBorder(BorderFactory.createLineBorder(new Color(200, 230, 201)));
        pnlEntradas.setPreferredSize(new Dimension(150, 60));
        gbc.gridy = 0;
        pnlEntradas.add(new JLabel("Entradas"), gbc);
        gbc.gridy = 1;
        pnlEntradas.add(lblEntradas, gbc);

        JPanel pnlSalidas = new JPanel(new GridBagLayout());
        pnlSalidas.setBackground(new Color(255, 235, 238));
        pnlSalidas.setBorder(BorderFactory.createLineBorder(new Color(255, 205, 210)));
        pnlSalidas.setPreferredSize(new Dimension(150, 60));
        gbc.gridy = 0;
        pnlSalidas.add(new JLabel("Salidas"), gbc);
        gbc.gridy = 1;
        pnlSalidas.add(lblSalidas, gbc);

        pnlResumen.add(pnlStock);
        pnlResumen.add(pnlEntradas);
        pnlResumen.add(pnlSalidas);

        pnlTop.add(pnlResumen, BorderLayout.CENTER);

        add(pnlTop, BorderLayout.NORTH);

        JPanel pnlTabla = new JPanel(new BorderLayout());
        pnlTabla.setBorder(BorderFactory.createTitledBorder("Movimientos del Producto"));
        pnlTabla.add(new JScrollPane(tblKardex), BorderLayout.CENTER);
        add(pnlTabla, BorderLayout.CENTER);
    }

    private void attachEvents() {
        btnRefrescar.addActionListener(e -> {
            cargarMovimientos();
        });

        cbCategoria.addActionListener(e -> {
            actualizarComboProductos();
            cargarMovimientos();
        });

        cbProducto.addActionListener(e -> {
            if (cbProducto.getItemCount() > 0) {
                cargarMovimientos();
            }
        });
    }

    /**
     * Carga los movimientos de Kardex de acuerdo a los filtros seleccionados.
     * Capa: Vista — Implementa: FR-004 y filtro dinámico.
     */
    private void cargarMovimientos() {
        if (cbProducto == null || cbProducto.getItemCount() == 0 || cbProducto.getSelectedItem() == null) return;

        ProductoDAO pdao = new ProductoDAO();
        KardexDAO dao = new KardexDAO();
        modelKardex.setRowCount(0);

        int idCategoriaSel = -1;
        if (cbCategoria.getSelectedIndex() > 0) {
            String catStr = cbCategoria.getSelectedItem().toString();
            idCategoriaSel = Integer.parseInt(catStr.split(" - ")[0]);
        }

        if (cbProducto.getSelectedIndex() <= 0) {
            // Mostrar todos los productos (filtrados por categoría si aplica)
            modelKardex.setColumnIdentifiers(new String[]{"Fecha", "Producto", "Tipo", "Cantidad", "Documento", "Observación"});
            
            List<Object[]> listaTodos;
            if (idCategoriaSel == -1) {
                listaTodos = dao.listarTodosDesc();
            } else {
                listaTodos = dao.listarPorCategoriaDesc(idCategoriaSel);
            }
            
            int totEntradas = 0;
            int totSalidas = 0;
            for (Object[] row : listaTodos) {
                if ("ENTRADA".equalsIgnoreCase((String)row[2])) {
                    totEntradas += (int)row[3];
                } else {
                    totSalidas += (int)row[3];
                }
                modelKardex.addRow(row);
            }
            
            int stockTotal = 0;
            for (Producto p : pdao.listarTodos()) {
                if (idCategoriaSel == -1 || p.getIdCategoria() == idCategoriaSel) {
                    stockTotal += p.getCantidad();
                }
            }
            
            lblStockActual.setText(String.valueOf(stockTotal));
            lblEntradas.setText(String.valueOf(totEntradas));
            lblSalidas.setText(String.valueOf(totSalidas));
            return;
        }
        
        // Mostrar un producto específico
        modelKardex.setColumnIdentifiers(new String[]{"Fecha", "Tipo", "Cantidad", "Stock Después", "Documento", "Observación"});
        String prodStr = cbProducto.getSelectedItem().toString();
        int idProducto = Integer.parseInt(prodStr.split(" - ")[0]);
        
        int stockActual = 0;
        for (Producto p : pdao.listarTodos()) {
            if (p.getIdProducto() == idProducto) {
                stockActual = p.getCantidad();
                break;
            }
        }
        
        List<Kardex> lista = dao.listarPorProducto(idProducto);
        
        int totEntradas = 0;
        int totSalidas = 0;
        int stockVirtual = 0;
        
        for (Kardex k : lista) {
            if ("ENTRADA".equalsIgnoreCase(k.getTipoMovimiento())) {
                totEntradas += k.getCantidad();
                stockVirtual += k.getCantidad();
            } else {
                totSalidas += k.getCantidad();
                stockVirtual -= k.getCantidad();
            }
            
            // Insertar en la posición 0 para invertir el orden (más reciente primero)
            modelKardex.insertRow(0, new Object[]{
                k.getFecha(),
                k.getTipoMovimiento(),
                k.getCantidad(),
                stockVirtual,
                "MOV-" + k.getIdMovimiento(),
                k.getMotivo()
            });
        }
        
        lblStockActual.setText(String.valueOf(stockActual));
        lblEntradas.setText(String.valueOf(totEntradas));
        lblSalidas.setText(String.valueOf(totSalidas));
    }
}

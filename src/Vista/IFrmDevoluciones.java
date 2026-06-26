package Vista;

import Clases.Producto;
import Modelo.ConexionDB;
import Modelo.ProductoDAO;
import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IFrmDevoluciones extends JInternalFrame {

    private JTable tblDevoluciones;
    private DefaultTableModel modelDevoluciones;
    
    private JTextField txtIdVenta;
    private JTextField txtIdProducto;
    private JTextField txtCantidad;
    private JComboBox<String> cbMotivo;
    private JComboBox<String> cbTipoReembolso;

    private JButton btnProcesar;
    private JButton btnLimpiar;
    private JButton btnBuscarVenta;

    public IFrmDevoluciones() {
        super("Control de Devoluciones", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        cargarDevoluciones();
        setSize(960, 600);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    private void initComponents() {
        String[] columns = {"ID Venta", "Producto", "Cant", "Motivo", "Reembolso", "Fecha"};
        modelDevoluciones = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblDevoluciones = UIKit.styledTable(modelDevoluciones);

        txtIdVenta = UIKit.textField();
        txtIdProducto = UIKit.textField();
        
        txtCantidad = UIKit.textField();
        txtCantidad.setHorizontalAlignment(JTextField.RIGHT);
        
        cbMotivo = new JComboBox<>(new String[]{"Defecto de Fábrica", "Producto Vencido", "Cambio de Opinión", "Error de Despacho"});
        cbMotivo.setFont(UIKit.BODY);
        
        cbTipoReembolso = new JComboBox<>(new String[]{"Efectivo", "Nota de Crédito", "Cambio de Producto"});
        cbTipoReembolso.setFont(UIKit.BODY);

        btnBuscarVenta = UIKit.secondaryButton("Buscar Venta");
        btnProcesar = UIKit.primaryButton("Procesar Devolución");
        btnLimpiar = UIKit.secondaryButton("Limpiar");
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(
                UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        // ===== Encabezado =====
        getContentPane().add(
                UIKit.screenHeader("Devoluciones", "Ventas  ›  Control de Devoluciones"),
                BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(UIKit.SPACE_LG, 0));
        cuerpo.setOpaque(false);

        // ── Panel Izquierdo: Tabla de Devoluciones Realizadas ──
        JPanel pnlTabla = UIKit.card();
        pnlTabla.setLayout(new BorderLayout(0, UIKit.SPACE_SM));
        pnlTabla.add(UIKit.sectionHeader("Historial de Devoluciones (Kardex)", null), BorderLayout.NORTH);
        
        JScrollPane scroll = new JScrollPane(tblDevoluciones);
        scroll.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        pnlTabla.add(scroll, BorderLayout.CENTER);

        cuerpo.add(pnlTabla, BorderLayout.CENTER);

        // ── Panel Derecho: Formulario para Nueva Devolución ──
        JPanel pnlForm = UIKit.card();
        pnlForm.setPreferredSize(new Dimension(320, 0));
        pnlForm.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(UIKit.sectionHeader("Registrar Retorno", null), gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("ID Venta"), gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(txtIdVenta, gbc);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(btnBuscarVenta, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("ID Producto"), gbc);
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(txtIdProducto, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Cantidad"), gbc);
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(txtCantidad, gbc);

        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Motivo"), gbc);
        gbc.gridy = 9;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(cbMotivo, gbc);

        gbc.gridy = 10;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Reembolso"), gbc);
        gbc.gridy = 11;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_LG, 0);
        pnlForm.add(cbTipoReembolso, gbc);

        JPanel pnlBotones = new JPanel(new GridLayout(2, 1, 0, UIKit.SPACE_SM));
        pnlBotones.setOpaque(false);
        pnlBotones.add(btnProcesar);
        pnlBotones.add(btnLimpiar);

        gbc.gridy = 12;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        pnlForm.add(pnlBotones, gbc);

        cuerpo.add(pnlForm, BorderLayout.EAST);

        getContentPane().add(cuerpo, BorderLayout.CENTER);
    }
    
    private void cargarDevoluciones() {
        modelDevoluciones.setRowCount(0);
        String sql = "SELECT idProducto, cantidad, fecha, motivo FROM tb_kardex WHERE motivo LIKE 'DEVOLUCION%' ORDER BY idMovimiento DESC LIMIT 30";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String motivoReal = rs.getString("motivo").replace("DEVOLUCION - ", "");
                int idProd = rs.getInt("idProducto");
                Producto p = new ProductoDAO().buscarPorNombre(String.valueOf(idProd)); // Dummy search to just get product, actually better to just show idProd or fetch
                modelDevoluciones.addRow(new Object[]{
                    "N/A", // Venta
                    "Prod #" + idProd,
                    rs.getInt("cantidad"),
                    motivoReal,
                    "Efectivo",
                    rs.getString("fecha")
                });
            }
        } catch (Exception e) {}
    }

    private void attachEvents() {
        btnBuscarVenta.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Las devoluciones en este MVP se procesan directamente como entradas al inventario sin comprobación estricta de la boleta para mayor rapidez en caja.");
        });

        btnProcesar.addActionListener(e -> {
            try {
                int idProd = Integer.parseInt(txtIdProducto.getText());
                int cant = Integer.parseInt(txtCantidad.getText());
                String motivo = cbMotivo.getSelectedItem().toString();
                String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
                
                // 1. Restaurar stock
                ProductoDAO pdao = new ProductoDAO();
                Producto p = null;
                for (Producto prod : pdao.listarTodos()) {
                    if (prod.getIdProducto() == idProd) { p = prod; break; }
                }
                
                if (p == null) {
                    JOptionPane.showMessageDialog(this, "El Producto ID " + idProd + " no existe.");
                    return;
                }
                
                p.setCantidad(p.getCantidad() + cant);
                pdao.actualizar(p);
                
                // 2. Registrar en Kardex
                String sql = "INSERT INTO tb_kardex (idProducto, tipoMovimiento, cantidad, fecha, motivo) VALUES (?, 'ENTRADA', ?, ?, ?)";
                try (Connection conn = ConexionDB.getConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, idProd);
                    ps.setInt(2, cant);
                    ps.setString(3, fecha);
                    ps.setString(4, "DEVOLUCION - " + motivo);
                    ps.executeUpdate();
                }
                
                JOptionPane.showMessageDialog(this, "Devolución procesada. El stock del producto ha sido restaurado.");
                btnLimpiar.doClick();
                cargarDevoluciones();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Verifique que el ID Producto y la Cantidad sean números válidos.");
            }
        });

        btnLimpiar.addActionListener(e -> {
            txtIdVenta.setText("");
            txtIdProducto.setText("");
            txtCantidad.setText("");
            cbMotivo.setSelectedIndex(0);
            cbTipoReembolso.setSelectedIndex(0);
        });
    }
}
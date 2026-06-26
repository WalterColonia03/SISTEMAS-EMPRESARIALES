package Vista;

import Clases.Cliente;
import Modelo.ClienteDAO;
import Modelo.ConexionDB;
import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * Creado/Modificado: 2026-06-25T23:53:00-05:00
 */
public class IFrmFidelizacion extends JInternalFrame {

    private JTable tblRanking;
    private DefaultTableModel modelRanking;
    
    private JTable tblHistorial;
    private DefaultTableModel modelHistorial;
    
    private JTextField txtBuscarDni;
    private JButton btnBuscarHistorial;

    public IFrmFidelizacion() {
        super("Programa de Fidelización CRM", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        setSize(960, 600);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    private void initComponents() {
        String[] colsRanking = {"Puesto", "Cliente", "DNI", "Puntos Acumulados", "Nivel VIP"};
        modelRanking = new DefaultTableModel(colsRanking, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblRanking = UIKit.styledTable(modelRanking);

        String[] colsHistorial = {"ID Venta", "Fecha", "Total", "Método"};
        modelHistorial = new DefaultTableModel(colsHistorial, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblHistorial = UIKit.styledTable(modelHistorial);

        txtBuscarDni = UIKit.textField();
        txtBuscarDni.putClientProperty("JTextField.placeholderText", "Ingrese DNI del cliente...");
        txtBuscarDni.setPreferredSize(new Dimension(200, 36));

        btnBuscarHistorial = UIKit.primaryButton("Buscar Historial");
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(
                UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        getContentPane().add(
                UIKit.screenHeader("CRM y Fidelización", "Ventas  ›  Fidelización"),
                BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UIKit.BODY_BOLD);

        // --- Pestaña 1: Ranking VIP ---
        JPanel pnlRanking = UIKit.card();
        pnlRanking.setLayout(new BorderLayout(0, UIKit.SPACE_MD));
        pnlRanking.setBorder(new EmptyBorder(UIKit.SPACE_MD, UIKit.SPACE_MD, UIKit.SPACE_MD, UIKit.SPACE_MD));
        pnlRanking.add(UIKit.sectionHeader("Top Clientes (Ranking VIP)", null), BorderLayout.NORTH);
        pnlRanking.add(new JScrollPane(tblRanking), BorderLayout.CENTER);
        tabs.addTab("Ranking de Clientes VIP", pnlRanking);

        // --- Pestaña 2: Historial por Cliente ---
        JPanel pnlHistorial = UIKit.card();
        pnlHistorial.setLayout(new BorderLayout(0, UIKit.SPACE_MD));
        pnlHistorial.setBorder(new EmptyBorder(UIKit.SPACE_MD, UIKit.SPACE_MD, UIKit.SPACE_MD, UIKit.SPACE_MD));
        
        JPanel pnlFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, UIKit.SPACE_SM, 0));
        pnlFiltro.setOpaque(false);
        pnlFiltro.add(UIKit.fieldLabel("DNI del Cliente:"));
        pnlFiltro.add(txtBuscarDni);
        pnlFiltro.add(btnBuscarHistorial);
        
        pnlHistorial.add(pnlFiltro, BorderLayout.NORTH);
        pnlHistorial.add(new JScrollPane(tblHistorial), BorderLayout.CENTER);
        tabs.addTab("Historial de Compras", pnlHistorial);

        getContentPane().add(tabs, BorderLayout.CENTER);
    }

    private void attachEvents() {
        cargarRanking();
        
        btnBuscarHistorial.addActionListener(e -> {
            String dni = txtBuscarDni.getText().trim();
            if (dni.isEmpty()) return;
            cargarHistorial(dni);
        });
    }

    private void cargarRanking() {
        ClienteDAO dao = new ClienteDAO();
        List<Cliente> clientes = dao.listarTodos();
        // Ordenar por puntos desc
        clientes.sort((c1, c2) -> Integer.compare(c2.getPuntos(), c1.getPuntos()));
        
        modelRanking.setRowCount(0);
        int puesto = 1;
        for (Cliente c : clientes) {
            if (c.getPuntos() > 0) { // Solo mostrar los que tienen puntos
                String nivel = c.getPuntos() >= 1000 ? "Oro" : (c.getPuntos() >= 500 ? "Plata" : "Bronce");
                modelRanking.addRow(new Object[]{
                    puesto++,
                    c.getNombre() + " " + (c.getApellido()!=null?c.getApellido():""),
                    c.getDni(),
                    c.getPuntos(),
                    nivel
                });
            }
        }
    }

    private void cargarHistorial(String dni) {
        // Asumiendo que tenemos una tb_venta con el cliente
        modelHistorial.setRowCount(0);
        // Para la demo o si VentaDAO no tiene buscarPorCliente, haremos un query directo
        String sql = "SELECT idVenta, fecha, total FROM tb_venta WHERE cliente LIKE ?";
        
        // Primero buscamos el nombre del cliente basado en su DNI
        ClienteDAO cdao = new ClienteDAO();
        String nombreCliente = "";
        for (Cliente c : cdao.listarTodos()) {
            if (c.getDni() != null && c.getDni().equals(dni)) {
                nombreCliente = c.getNombre();
                break;
            }
        }
        
        if (nombreCliente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado.");
            return;
        }

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             ps.setString(1, "%" + nombreCliente + "%");
             try (ResultSet rs = ps.executeQuery()) {
                 while (rs.next()) {
                     modelHistorial.addRow(new Object[]{
                         rs.getInt("idVenta"),
                         rs.getString("fecha"),
                         String.format("S/ %.2f", rs.getDouble("total")),
                         "Efectivo" // Mockup para método de pago temporal
                     });
                 }
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

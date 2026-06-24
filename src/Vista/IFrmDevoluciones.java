package Vista;

import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

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
        setSize(960, 600);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    private void initComponents() {
        String[] columns = {"ID Devolución", "ID Venta", "Producto", "Cant", "Motivo", "Reembolso"};
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
        pnlTabla.add(UIKit.sectionHeader("Historial de Devoluciones", null), BorderLayout.NORTH);
        
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

    private void attachEvents() {
        btnBuscarVenta.addActionListener(e -> {
            // TODO: lógica TXT para verificar la venta
        });

        btnProcesar.addActionListener(e -> {
            // TODO: lógica TXT para procesar la devolución
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

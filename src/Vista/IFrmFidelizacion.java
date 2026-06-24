package Vista;

import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class IFrmFidelizacion extends JInternalFrame {

    private JTable tblClientesPuntos;
    private DefaultTableModel modelClientesPuntos;
    
    // Configuración de Reglas de Puntos
    private JTextField txtSolesPorPunto;
    private JButton btnGuardarRegla;

    // Formulario de Canjes
    private JTextField txtIdCliente;
    private JTextField txtPuntosDisponibles;
    private JTextField txtPuntosACanjear;
    private JComboBox<String> cbPremios;
    private JButton btnCanjear;
    private JButton btnLimpiar;

    public IFrmFidelizacion() {
        super("Módulo de Fidelización", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        setSize(960, 600);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    private void initComponents() {
        // Tabla de puntos de clientes
        String[] columns = {"ID Cliente", "Nombre", "Apellido", "DNI", "Puntos"};
        modelClientesPuntos = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblClientesPuntos = UIKit.styledTable(modelClientesPuntos);

        // Reglas
        txtSolesPorPunto = UIKit.textField();
        txtSolesPorPunto.setText("10");
        txtSolesPorPunto.setHorizontalAlignment(JTextField.RIGHT);
        
        btnGuardarRegla = UIKit.secondaryButton("Actualizar Regla");

        // Formulario Canje
        txtIdCliente = UIKit.readOnlyField();
        
        txtPuntosDisponibles = UIKit.readOnlyField();
        txtPuntosDisponibles.setHorizontalAlignment(JTextField.RIGHT);
        
        txtPuntosACanjear = UIKit.textField();
        txtPuntosACanjear.setHorizontalAlignment(JTextField.RIGHT);
        
        cbPremios = new JComboBox<>(new String[]{
            "Vale de Descuento S/ 10 (100 Ptos)",
            "Vale de Descuento S/ 25 (200 Ptos)",
            "Bolsa Ecológica Laredo (50 Ptos)",
            "Taza de Regalo (80 Ptos)"
        });
        cbPremios.setFont(UIKit.BODY);

        btnCanjear = UIKit.primaryButton("Canjear Premio");
        btnLimpiar = UIKit.secondaryButton("Limpiar");
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(
                UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        // ===== Encabezado =====
        getContentPane().add(
                UIKit.screenHeader("Fidelización de Clientes", "Ventas  ›  Fidelización"),
                BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(UIKit.SPACE_LG, 0));
        cuerpo.setOpaque(false);

        // ── Panel Izquierdo: Tabla de Puntos de Clientes ──
        JPanel pnlTabla = UIKit.card();
        pnlTabla.setLayout(new BorderLayout(0, UIKit.SPACE_SM));
        pnlTabla.add(UIKit.sectionHeader("Clientes y Puntos Acumulados", null), BorderLayout.NORTH);
        
        JScrollPane scroll = new JScrollPane(tblClientesPuntos);
        scroll.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        pnlTabla.add(scroll, BorderLayout.CENTER);

        cuerpo.add(pnlTabla, BorderLayout.CENTER);

        // ── Panel Derecho: Configuración y Canjes ──
        JPanel pnlDerecho = new JPanel(new BorderLayout(0, UIKit.SPACE_LG));
        pnlDerecho.setPreferredSize(new Dimension(340, 0));
        pnlDerecho.setOpaque(false);

        // Subpanel 1: Reglas de Fidelización
        JPanel pnlReglas = UIKit.card();
        pnlReglas.setLayout(new GridBagLayout());
        GridBagConstraints gbcR = new GridBagConstraints();
        gbcR.fill = GridBagConstraints.HORIZONTAL;
        gbcR.weightx = 1.0;

        gbcR.gridx = 0; gbcR.gridy = 0;
        gbcR.gridwidth = 2;
        gbcR.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlReglas.add(UIKit.sectionHeader("Regla de Acumulación", null), gbcR);

        gbcR.gridy = 1;
        gbcR.gridwidth = 1;
        gbcR.insets = new Insets(0, 0, UIKit.SPACE_XS, UIKit.SPACE_SM);
        pnlReglas.add(UIKit.fieldLabel("Soles por Punto (S/)"), gbcR);
        
        gbcR.gridx = 1;
        gbcR.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlReglas.add(new JLabel(""), gbcR); // placeholder
        
        gbcR.gridy = 2;
        gbcR.gridx = 0;
        gbcR.insets = new Insets(0, 0, 0, UIKit.SPACE_SM);
        pnlReglas.add(txtSolesPorPunto, gbcR);
        
        gbcR.gridx = 1;
        gbcR.insets = new Insets(0, 0, 0, 0);
        pnlReglas.add(btnGuardarRegla, gbcR);

        // Subpanel 2: Canje de Premios
        JPanel pnlCanje = UIKit.card();
        pnlCanje.setLayout(new GridBagLayout());
        GridBagConstraints gbcC = new GridBagConstraints();
        gbcC.fill = GridBagConstraints.HORIZONTAL;
        gbcC.weightx = 1.0;

        gbcC.gridx = 0; gbcC.gridy = 0;
        gbcC.gridwidth = 2;
        gbcC.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlCanje.add(UIKit.sectionHeader("Canjear Puntos", null), gbcC);

        gbcC.gridy = 1;
        gbcC.gridwidth = 1;
        gbcC.insets = new Insets(0, 0, UIKit.SPACE_XS, UIKit.SPACE_SM);
        pnlCanje.add(UIKit.fieldLabel("ID Cliente"), gbcC);
        
        gbcC.gridx = 1;
        gbcC.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlCanje.add(UIKit.fieldLabel("Puntos Disponibles"), gbcC);
        
        gbcC.gridy = 2;
        gbcC.gridx = 0;
        gbcC.insets = new Insets(0, 0, UIKit.SPACE_MD, UIKit.SPACE_SM);
        pnlCanje.add(txtIdCliente, gbcC);
        
        gbcC.gridx = 1;
        gbcC.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlCanje.add(txtPuntosDisponibles, gbcC);

        gbcC.gridy = 3;
        gbcC.gridx = 0;
        gbcC.gridwidth = 2;
        gbcC.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlCanje.add(UIKit.fieldLabel("Premio a Canjear"), gbcC);
        
        gbcC.gridy = 4;
        gbcC.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlCanje.add(cbPremios, gbcC);

        gbcC.gridy = 5;
        gbcC.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlCanje.add(UIKit.fieldLabel("Puntos a Descontar"), gbcC);
        
        gbcC.gridy = 6;
        gbcC.insets = new Insets(0, 0, UIKit.SPACE_LG, 0);
        pnlCanje.add(txtPuntosACanjear, gbcC);

        JPanel pnlBotones = new JPanel(new GridLayout(2, 1, 0, UIKit.SPACE_SM));
        pnlBotones.setOpaque(false);
        pnlBotones.add(btnCanjear);
        pnlBotones.add(btnLimpiar);

        gbcC.gridy = 7;
        gbcC.weighty = 1.0;
        gbcC.anchor = GridBagConstraints.NORTH;
        gbcC.insets = new Insets(0, 0, 0, 0);
        pnlCanje.add(pnlBotones, gbcC);

        pnlDerecho.add(pnlReglas, BorderLayout.NORTH);
        pnlDerecho.add(pnlCanje, BorderLayout.CENTER);

        cuerpo.add(pnlDerecho, BorderLayout.EAST);

        getContentPane().add(cuerpo, BorderLayout.CENTER);
    }

    private void attachEvents() {
        btnGuardarRegla.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Regla de acumulación guardada: S/ " + txtSolesPorPunto.getText() + " = 1 punto.");
        });

        btnCanjear.addActionListener(e -> {
            // TODO: lógica TXT
        });

        btnLimpiar.addActionListener(e -> {
            txtIdCliente.setText("");
            txtPuntosDisponibles.setText("");
            txtPuntosACanjear.setText("");
            cbPremios.setSelectedIndex(0);
        });

        tblClientesPuntos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblClientesPuntos.getSelectedRow() != -1) {
                int row = tblClientesPuntos.getSelectedRow();
                txtIdCliente.setText(modelClientesPuntos.getValueAt(row, 0).toString());
                txtPuntosDisponibles.setText(modelClientesPuntos.getValueAt(row, 4).toString());
            }
        });
    }
}

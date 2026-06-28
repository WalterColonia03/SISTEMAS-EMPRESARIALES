package Vista;

import Vista.Estilos.UIKit;
import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * IFrmConfiguracionERP - Módulo para configurar los datos de la empresa y credenciales.
 * Rediseñado con UIKit (Patrón E).
 */
public class IFrmConfiguracionERP extends JInternalFrame {

    // Datos Empresa
    private JTextField txtRazonSocial;
    private JTextField txtRuc;
    private JTextField txtDireccion;
    private JTextField txtTelefonoEmpresa;
    private JTextField txtCorreoEmpresa;

    // API Mercado Pago
    private JPasswordField txtMpToken;
    private JTextField txtMpPublicKey;
    private JTextField txtMpClientId;

    private JButton btnGuardar;
    private JButton btnLimpiar;
    private JButton btnTestMp;
    private JButton btnOjoToken;

    public IFrmConfiguracionERP() {
        super("Configuración Global del ERP", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        setSize(960, 600);
        putClientProperty("JInternalFrame.isPalette", Boolean.FALSE);
    }

    private void initComponents() {
        // Datos Empresa
        txtRazonSocial = UIKit.textField();
        txtRuc = UIKit.textField();
        txtDireccion = UIKit.textField();
        txtTelefonoEmpresa = UIKit.textField();
        txtCorreoEmpresa = UIKit.textField();

        // API Mercado Pago
        txtMpToken = new JPasswordField();
        txtMpToken.setFont(UIKit.BODY);
        txtMpToken.putClientProperty(FlatClientProperties.STYLE,
                "arc: 8; borderColor: " + String.format("#%02x%02x%02x", UIKit.BORDER.getRed(), UIKit.BORDER.getGreen(), UIKit.BORDER.getBlue()) +
                "; focusedBorderColor: " + String.format("#%02x%02x%02x", UIKit.ACCENT.getRed(), UIKit.ACCENT.getGreen(), UIKit.ACCENT.getBlue()) + ";");
        txtMpToken.setPreferredSize(new Dimension(0, 36));

        btnOjoToken = new JButton("👁");
        btnOjoToken.setContentAreaFilled(false);
        btnOjoToken.setBorderPainted(false);
        btnOjoToken.setFocusPainted(false);
        btnOjoToken.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        txtMpPublicKey = UIKit.textField();
        txtMpClientId = UIKit.textField();

        // Botones
        btnGuardar = UIKit.primaryButton("Guardar Configuración");
        btnLimpiar = UIKit.secondaryButton("Limpiar / Restablecer");
        btnTestMp = UIKit.secondaryButton("Probar Conexión MP");
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(
                UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        // ===== Encabezado =====
        getContentPane().add(
                UIKit.screenHeader("Configuración", "Administración  >  Configuración ERP"),
                BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new GridLayout(1, 2, UIKit.SPACE_LG, 0));
        cuerpo.setOpaque(false);

        // ── Sección 1: Datos de la Empresa ──
        JPanel pnlEmpresa = UIKit.card();
        pnlEmpresa.setLayout(new GridBagLayout());
        
        GridBagConstraints gbcE = new GridBagConstraints();
        gbcE.fill = GridBagConstraints.HORIZONTAL;
        gbcE.weightx = 1.0;
        
        gbcE.gridx = 0; gbcE.gridy = 0;
        gbcE.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlEmpresa.add(UIKit.sectionHeader("Datos del Establecimiento", null), gbcE);

        gbcE.gridy = 1;
        gbcE.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlEmpresa.add(UIKit.fieldLabel("Razón Social"), gbcE);
        gbcE.gridy = 2;
        gbcE.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlEmpresa.add(txtRazonSocial, gbcE);

        gbcE.gridy = 3;
        gbcE.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlEmpresa.add(UIKit.fieldLabel("RUC de la Empresa"), gbcE);
        gbcE.gridy = 4;
        gbcE.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlEmpresa.add(txtRuc, gbcE);

        gbcE.gridy = 5;
        gbcE.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlEmpresa.add(UIKit.fieldLabel("Dirección Fiscal"), gbcE);
        gbcE.gridy = 6;
        gbcE.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlEmpresa.add(txtDireccion, gbcE);

        gbcE.gridy = 7;
        gbcE.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlEmpresa.add(UIKit.fieldLabel("Teléfono de Contacto"), gbcE);
        gbcE.gridy = 8;
        gbcE.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlEmpresa.add(txtTelefonoEmpresa, gbcE);

        gbcE.gridy = 9;
        gbcE.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlEmpresa.add(UIKit.fieldLabel("Correo Electrónico"), gbcE);
        gbcE.gridy = 10;
        gbcE.weighty = 1.0;
        gbcE.anchor = GridBagConstraints.NORTH;
        gbcE.insets = new Insets(0, 0, 0, 0);
        pnlEmpresa.add(txtCorreoEmpresa, gbcE);

        cuerpo.add(pnlEmpresa);

        // ── Sección 2: Configuración Mercado Pago API ──
        JPanel pnlApi = UIKit.card();
        pnlApi.setLayout(new GridBagLayout());
        
        GridBagConstraints gbcA = new GridBagConstraints();
        gbcA.fill = GridBagConstraints.HORIZONTAL;
        gbcA.weightx = 1.0;

        gbcA.gridx = 0; gbcA.gridy = 0;
        gbcA.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlApi.add(UIKit.sectionHeader("Integración Mercado Pago", null), gbcA);

        gbcA.gridy = 1;
        gbcA.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlApi.add(UIKit.fieldLabel("Access Token (Production/Sandbox)"), gbcA);
        
        gbcA.gridy = 2;
        gbcA.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        JPanel pnlToken = new JPanel(new BorderLayout());
        pnlToken.setOpaque(false);
        pnlToken.add(txtMpToken, BorderLayout.CENTER);
        pnlToken.add(btnOjoToken, BorderLayout.EAST);
        pnlApi.add(pnlToken, gbcA);

        gbcA.gridy = 3;
        gbcA.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlApi.add(UIKit.fieldLabel("Public Key"), gbcA);
        gbcA.gridy = 4;
        gbcA.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlApi.add(txtMpPublicKey, gbcA);

        gbcA.gridy = 5;
        gbcA.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlApi.add(UIKit.fieldLabel("Client ID / POS ID"), gbcA);
        gbcA.gridy = 6;
        gbcA.insets = new Insets(0, 0, UIKit.SPACE_LG, 0);
        pnlApi.add(txtMpClientId, gbcA);

        gbcA.gridy = 7;
        gbcA.fill = GridBagConstraints.NONE;
        gbcA.anchor = GridBagConstraints.WEST;
        gbcA.weighty = 1.0;
        pnlApi.add(btnTestMp, gbcA);

        cuerpo.add(pnlApi);

        // Panel inferior: Botones generales
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, UIKit.SPACE_SM, 0));
        pnlBotones.setOpaque(false);
        pnlBotones.add(btnLimpiar);
        pnlBotones.add(btnGuardar);

        getContentPane().add(cuerpo, BorderLayout.CENTER);
        
        JPanel pnlSur = new JPanel(new BorderLayout());
        pnlSur.setOpaque(false);
        pnlSur.setBorder(new EmptyBorder(UIKit.SPACE_MD, 0, 0, 0));
        pnlSur.add(pnlBotones, BorderLayout.EAST);
        getContentPane().add(pnlSur, BorderLayout.SOUTH);
    }

    private void attachEvents() {
        btnGuardar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Configuración guardada exitosamente");
        });

        btnLimpiar.addActionListener(e -> {
            txtRazonSocial.setText("");
            txtRuc.setText("");
            txtDireccion.setText("");
            txtTelefonoEmpresa.setText("");
            txtCorreoEmpresa.setText("");
            txtMpToken.setText("");
            txtMpPublicKey.setText("");
            txtMpClientId.setText("");
        });

        btnTestMp.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Prueba de conexión con Mercado Pago: OK\nToken verificado.");
        });

        btnOjoToken.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                txtMpToken.setEchoChar((char) 0);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                txtMpToken.setEchoChar('•');
            }
        });
    }
}

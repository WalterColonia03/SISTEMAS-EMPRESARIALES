package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import Clases.Usuario;
import ArchivosTXT.ArchivoUsuarioTXT;
import Vista.Estilos.UIKit;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatClientProperties;

/**
 * FrmLogin - Pantalla de inicio de sesión moderna y sin bordes (Patrón G).
 */
public class FrmLogin extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private JButton btnSalir;
    
    // Variables para arrastrar ventana undecorated
    private int mouseX, mouseY;

    public FrmLogin() {
        super("ERP Minimarket LAREDO");
        setUndecorated(true); // Sin bordes del SO
        initComponents();
        buildLayout();
        attachEvents();
        configFrame();
    }

    private void initComponents() {
        txtUsuario  = buildTextField("Ingrese su usuario");
        txtPassword = buildPasswordField("••••••••");
        btnIngresar = UIKit.primaryButton("Ingresar");
        
        btnSalir = new JButton("Salir del sistema");
        btnSalir.setFont(UIKit.BODY);
        btnSalir.setForeground(UIKit.TEXT_SECONDARY);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setBorderPainted(false);
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void buildLayout() {
        JPanel pnlRaiz = new JPanel(new BorderLayout());

        // ================= WEST (40%) =================
        JPanel pnlLeft = new JPanel(new GridBagLayout());
        pnlLeft.setBackground(UIKit.PRIMARY);
        pnlLeft.setPreferredSize(new Dimension(384, 600)); // 40% of 960

        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 0;
        gbcLeft.insets = new Insets(0, 0, 20, 0);
        gbcLeft.anchor = GridBagConstraints.CENTER;

        // Monograma Vectorial
        JPanel pnlLogo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(20, 20, 60, 60, 15, 15);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 36));
                g2.drawString("L", 40, 62);
                g2.dispose();
            }
        };
        pnlLogo.setPreferredSize(new Dimension(100, 100));
        pnlLogo.setOpaque(false);
        pnlLeft.add(pnlLogo, gbcLeft);

        gbcLeft.gridy = 1;
        gbcLeft.insets = new Insets(0, 0, 10, 0);
        JLabel lblBrand = new JLabel("Minimarket LAREDO");
        lblBrand.setFont(UIKit.H1);
        lblBrand.setForeground(Color.WHITE);
        pnlLeft.add(lblBrand, gbcLeft);

        gbcLeft.gridy = 2;
        JLabel lblSlogan = new JLabel("Sistema de Gestión Empresarial");
        lblSlogan.setFont(UIKit.SUBTITLE);
        lblSlogan.setForeground(new Color(255, 255, 255, 180));
        pnlLeft.add(lblSlogan, gbcLeft);

        pnlRaiz.add(pnlLeft, BorderLayout.WEST);

        // ================= EAST (60%) =================
        JPanel pnlRight = new JPanel(new GridBagLayout());
        pnlRight.setBackground(UIKit.BG_APP);

        JPanel pnlCard = UIKit.card();
        pnlCard.setPreferredSize(new Dimension(360, 420));
        pnlCard.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.anchor  = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;

        // Título
        gbc.gridy  = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        JLabel lblTitulo = new JLabel("Bienvenido", SwingConstants.CENTER);
        lblTitulo.setFont(UIKit.H2);
        lblTitulo.setForeground(UIKit.TEXT_PRIMARY);
        pnlCard.add(lblTitulo, gbc);

        // Etiqueta Usuario
        gbc.gridy  = 1;
        gbc.insets = new Insets(0, 0, 5, 0);
        pnlCard.add(UIKit.fieldLabel("Usuario"), gbc);

        // Campo Usuario
        gbc.gridy  = 2;
        gbc.insets = new Insets(0, 0, 16, 0);
        pnlCard.add(txtUsuario, gbc);

        // Etiqueta Contraseña
        gbc.gridy  = 3;
        gbc.insets = new Insets(0, 0, 5, 0);
        pnlCard.add(UIKit.fieldLabel("Contraseña"), gbc);

        // Campo Contraseña
        gbc.gridy  = 4;
        gbc.insets = new Insets(0, 0, 30, 0);
        pnlCard.add(txtPassword, gbc);

        // Botón Ingresar
        gbc.gridy  = 5;
        gbc.insets = new Insets(0, 0, 20, 0);
        pnlCard.add(btnIngresar, gbc);

        // Botón Salir
        gbc.gridy  = 6;
        gbc.insets = new Insets(0, 0, 20, 0);
        pnlCard.add(btnSalir, gbc);

        // Pie de versión
        gbc.gridy  = 7;
        gbc.insets = new Insets(10, 0, 0, 0);
        JLabel lblVersion = new JLabel("ERP v1.0.0 · Minimarket LAREDO © 2025", SwingConstants.CENTER);
        lblVersion.setFont(UIKit.CAPTION);
        lblVersion.setForeground(UIKit.TEXT_SECONDARY);
        pnlCard.add(lblVersion, gbc);

        pnlRight.add(pnlCard, new GridBagConstraints());
        pnlRaiz.add(pnlRight, BorderLayout.CENTER);

        setContentPane(pnlRaiz);
    }

    private void attachEvents() {
        // Permitir arrastrar la ventana undecorated
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                setLocation(x - mouseX, y - mouseY);
            }
        });

        btnIngresar.addActionListener(e -> {
            btnIngresar.setEnabled(false);
            btnIngresar.setText("Ingresando...");
            
            SwingUtilities.invokeLater(() -> {
                String user = txtUsuario.getText();
                String pass = new String(txtPassword.getPassword());

                ArchivoUsuarioTXT archivo = new ArchivoUsuarioTXT();
                List<Usuario> lista = archivo.leer();

                boolean encontrado = false;

                for (Usuario u : lista) {
                    if (u.getUsuario().equals(user) && u.getPassword().equals(pass)) {
                        encontrado = true;
                        Clases.Sesion.setRol(u.getRol());
                        Clases.Sesion.setUsuario(u.getUsuario());
                        JOptionPane.showMessageDialog(this, "Bienvenido " + u.getNombre() + " - " + u.getRol());
                        FrmDashboard dashboard = new FrmDashboard();
                        dashboard.setVisible(true);
                        this.dispose();
                        break;
                    }
                }

                if (!encontrado) {
                    JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
                    btnIngresar.setEnabled(true);
                    btnIngresar.setText("Ingresar");
                }
            });
        });

        btnSalir.addActionListener(e -> {
            System.exit(0);
        });

        txtUsuario.addActionListener(e  -> btnIngresar.doClick());
        txtPassword.addActionListener(e -> btnIngresar.doClick());
    }

    private void configFrame() {
        setSize(960, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JTextField buildTextField(String hint) {
        JTextField tf = UIKit.textField();
        tf.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, hint);
        return tf;
    }

    private JPasswordField buildPasswordField(String hint) {
        JPasswordField pf = new JPasswordField();
        pf.setFont(UIKit.BODY);
        pf.putClientProperty(FlatClientProperties.STYLE,
                "arc: 8; borderColor: " + String.format("#%02x%02x%02x", UIKit.BORDER.getRed(), UIKit.BORDER.getGreen(), UIKit.BORDER.getBlue()) +
                "; focusedBorderColor: " + String.format("#%02x%02x%02x", UIKit.ACCENT.getRed(), UIKit.ACCENT.getGreen(), UIKit.ACCENT.getBlue()) + ";");
        pf.setPreferredSize(new Dimension(0, 36));
        pf.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, hint);
        return pf;
    }

    public JTextField getTxtUsuario() { return txtUsuario; }
    public JPasswordField getTxtPassword() { return txtPassword; }
    public JButton getBtnIngresar() { return btnIngresar; }
    public JButton getBtnSalir() { return btnSalir; }

    public static void main(String[] args) {
        FlatLightLaf.setup();

        // Radios y overrides globales del sistema de diseño
        UIManager.put("Button.arc", 8);
        UIManager.put("Component.arc", 8);
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("CheckBox.arc", 4);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.width", 10);
        UIManager.put("TabbedPane.showTabSeparators", true);
        UIManager.put("Table.showHorizontalLines", false);
        UIManager.put("Table.showVerticalLines", false);
        UIManager.put("Table.rowHeight", 34);
        UIManager.put("defaultFont", UIKit.BODY);

        SwingUtilities.invokeLater(() -> new FrmLogin().setVisible(true));
    }
}

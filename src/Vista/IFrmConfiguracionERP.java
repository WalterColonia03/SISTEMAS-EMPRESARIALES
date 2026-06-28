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

    private JButton btnGuardar;
    private JButton btnLimpiar;

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

        // Botones
        btnGuardar = UIKit.primaryButton("Guardar Configuración");
        btnLimpiar = UIKit.secondaryButton("Limpiar / Restablecer");
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

        JPanel cuerpo = new JPanel(new GridLayout(1, 1, UIKit.SPACE_LG, 0));
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
        });
    }
}

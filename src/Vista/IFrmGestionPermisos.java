package Vista;

import Modelo.PermisoDAO;
import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Vista para gestionar permisos dinámicos por rol (FR-042)
 */
public class IFrmGestionPermisos extends JInternalFrame {

    private JComboBox<String> cbRol;
    private JCheckBox chkVentas;
    private JCheckBox chkInventario;
    private JCheckBox chkCompras;
    private JCheckBox chkFinanzas;
    private JCheckBox chkPersonal;
    private JCheckBox chkReportes;
    private JCheckBox chkAdministracion;
    
    private JButton btnGuardar;

    private PermisoDAO permisoDAO;

    public IFrmGestionPermisos() {
        super("Gestión de Permisos por Rol", true, true, true, true);
        permisoDAO = new PermisoDAO();
        initComponents();
        buildLayout();
        attachEvents();
        setSize(500, 400);
    }

    private void initComponents() {
        cbRol = new JComboBox<>(new String[]{"SELECCIONE...", "VENDEDOR", "CAJERO", "ALMACENERO", "CONTADOR", "FINANZAS", "GERENTE"});
        cbRol.setFont(UIKit.BODY);

        chkVentas = new JCheckBox("Módulo Ventas (POS, Devoluciones, Clientes)");
        chkInventario = new JCheckBox("Módulo Inventario (Productos, Kardex)");
        chkCompras = new JCheckBox("Módulo Compras y Proveedores");
        chkFinanzas = new JCheckBox("Módulo Finanzas (Flujo, Cuentas)");
        chkPersonal = new JCheckBox("Módulo Personal (RRHH, Planillas)");
        chkReportes = new JCheckBox("Módulo Reportes (BI, Estadísticas)");
        chkAdministracion = new JCheckBox("Módulo Administración (Usuarios, Bitácora)");

        btnGuardar = UIKit.primaryButton("Guardar Permisos");
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(UIKit.SPACE_MD, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        getContentPane().add(UIKit.screenHeader("Permisos por Rol", "Seguridad > Permisos (FR-042)"), BorderLayout.NORTH);

        JPanel pnlCentro = UIKit.card();
        pnlCentro.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        pnlCentro.add(UIKit.fieldLabel("Seleccionar Rol:"), gbc);
        gbc.gridx = 1; 
        pnlCentro.add(cbRol, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        pnlCentro.add(new JSeparator(), gbc);

        gbc.gridy = 2; pnlCentro.add(chkVentas, gbc);
        gbc.gridy = 3; pnlCentro.add(chkInventario, gbc);
        gbc.gridy = 4; pnlCentro.add(chkCompras, gbc);
        gbc.gridy = 5; pnlCentro.add(chkFinanzas, gbc);
        gbc.gridy = 6; pnlCentro.add(chkPersonal, gbc);
        gbc.gridy = 7; pnlCentro.add(chkReportes, gbc);
        gbc.gridy = 8; pnlCentro.add(chkAdministracion, gbc);

        gbc.gridy = 9; gbc.insets = new Insets(15, 5, 5, 5);
        pnlCentro.add(btnGuardar, gbc);

        getContentPane().add(pnlCentro, BorderLayout.CENTER);
        
        // El administrador no se puede editar aquí para evitar auto-bloqueos
        JLabel lblNota = new JLabel("Nota: El rol ADMINISTRADOR siempre tiene acceso total por diseño del sistema.");
        lblNota.setFont(UIKit.CAPTION);
        lblNota.setForeground(UIKit.TEXT_SECONDARY);
        getContentPane().add(lblNota, BorderLayout.SOUTH);
    }

    private void attachEvents() {
        cbRol.addActionListener(e -> {
            if (cbRol.getSelectedIndex() > 0) {
                String rol = cbRol.getSelectedItem().toString();
                String permisos = permisoDAO.getPermisos(rol);
                
                chkVentas.setSelected(permisos.contains("VENTAS"));
                chkInventario.setSelected(permisos.contains("INVENTARIO"));
                chkCompras.setSelected(permisos.contains("COMPRAS"));
                chkFinanzas.setSelected(permisos.contains("FINANZAS"));
                chkPersonal.setSelected(permisos.contains("PERSONAL"));
                chkReportes.setSelected(permisos.contains("REPORTES"));
                chkAdministracion.setSelected(permisos.contains("ADMINISTRACION"));
            } else {
                desmarcarTodos();
            }
        });

        btnGuardar.addActionListener(e -> {
            if (cbRol.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un rol", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String rol = cbRol.getSelectedItem().toString();
            List<String> modulos = new ArrayList<>();
            if (chkVentas.isSelected()) modulos.add("VENTAS");
            if (chkInventario.isSelected()) modulos.add("INVENTARIO");
            if (chkCompras.isSelected()) modulos.add("COMPRAS");
            if (chkFinanzas.isSelected()) modulos.add("FINANZAS");
            if (chkPersonal.isSelected()) modulos.add("PERSONAL");
            if (chkReportes.isSelected()) modulos.add("REPORTES");
            if (chkAdministracion.isSelected()) modulos.add("ADMINISTRACION");
            
            String modulosStr = String.join(",", modulos);
            permisoDAO.setPermisos(rol, modulosStr);
            
            Utils.BitacoraService.registrar(Clases.Sesion.getUsuario(), Utils.BitacoraService.MOD_SEGURIDAD, "ACTUALIZAR_PERMISOS", Utils.BitacoraService.OK, "Rol: " + rol);
            
            JOptionPane.showMessageDialog(this, "Permisos actualizados correctamente.\nLos cambios tendrán efecto inmediato para las nuevas sesiones o al recargar el dashboard.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void desmarcarTodos() {
        chkVentas.setSelected(false);
        chkInventario.setSelected(false);
        chkCompras.setSelected(false);
        chkFinanzas.setSelected(false);
        chkPersonal.setSelected(false);
        chkReportes.setSelected(false);
        chkAdministracion.setSelected(false);
    }
}

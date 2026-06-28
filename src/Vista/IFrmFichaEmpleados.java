package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class IFrmFichaEmpleados extends JInternalFrame {

    private JTable tblEmpleados;
    private DefaultTableModel modelEmpleados;
    private JTextField txtBuscar;

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtDni;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtTurno;
    private JComboBox<String> cbCargo;
    private JTextField txtSalario;
    private JComboBox<String> cbEstado;

    private JButton btnBuscar;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    private static final Color COLOR_PRIMARY = new Color(25, 118, 210);
    private static final Color COLOR_ACCENT = new Color(46, 125, 50);

    public IFrmFichaEmpleados() {
        super("Ficha de Empleados", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        setSize(950, 600);
        cargarEmpleados();
    }

    private void cargarEmpleados() {
        modelEmpleados.setRowCount(0);
        Modelo.EmpleadoDAO dao = new Modelo.EmpleadoDAO();
        for (Clases.Empleado e : dao.listarTodos()) {
            modelEmpleados.addRow(new Object[]{
                e.getCodigo(), e.getNombres(), e.getApellidos(), e.getDni(),
                e.getCargo(), e.getSueldoBase(), e.getTelefono(), e.isActivo() ? "Activo" : "Inactivo"
            });
        }
    }

    private void initComponents() {
        txtBuscar = new JTextField(15);
        btnBuscar = new JButton("Buscar");

        String[] columns = {"ID", "Nombre", "Apellido", "DNI", "Cargo", "Salario", "Tel\u00e9fono", "Estado"};
        modelEmpleados = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblEmpleados = new JTable(modelEmpleados);

        txtId = new JTextField();
        txtId.setEditable(false);
        txtNombre = new JTextField();
        txtApellido = new JTextField();
        txtDni = new JTextField();
        txtTelefono = new JTextField();
        txtCorreo = new JTextField();
        txtTurno = new JTextField();
        cbCargo = new JComboBox<>(new String[]{"Vendedor", "Cajero", "Supervisor", "Administrador", "Almacenero", "Contador", "Gerente"});
        txtSalario = new JTextField();
        cbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});

        btnGuardar = new JButton("Guardar / Actualizar");
        btnGuardar.setBackground(COLOR_ACCENT);
        btnGuardar.setForeground(Color.WHITE);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(198, 40, 40));
        btnEliminar.setForeground(Color.WHITE);

        btnLimpiar = new JButton("Limpiar / Nuevo");
        btnLimpiar.setBackground(new Color(96, 125, 139));
        btnLimpiar.setForeground(Color.WHITE);
    }

    private void buildLayout() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel pnlTabla = new JPanel(new BorderLayout(10, 10));
        pnlTabla.setBorder(BorderFactory.createTitledBorder("Listado del Personal"));

        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBusqueda.add(new JLabel("Buscar:"));
        pnlBusqueda.add(txtBuscar);
        pnlBusqueda.add(btnBuscar);
        pnlTabla.add(pnlBusqueda, BorderLayout.NORTH);
        pnlTabla.add(new JScrollPane(tblEmpleados), BorderLayout.CENTER);

        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createTitledBorder("Datos del Empleado"));
        pnlForm.setPreferredSize(new Dimension(380, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        pnlForm.add(new JLabel("ID Empleado:"), gbc);
        gbc.gridx = 1;
        pnlForm.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        pnlForm.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        pnlForm.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        pnlForm.add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 1;
        pnlForm.add(txtApellido, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        pnlForm.add(new JLabel("DNI:"), gbc);
        gbc.gridx = 1;
        pnlForm.add(txtDni, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        pnlForm.add(new JLabel("Tel\u00e9fono:"), gbc);
        gbc.gridx = 1;
        pnlForm.add(txtTelefono, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        pnlForm.add(new JLabel("Correo:"), gbc);
        gbc.gridx = 1;
        pnlForm.add(txtCorreo, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        pnlForm.add(new JLabel("Turno:"), gbc);
        gbc.gridx = 1;
        pnlForm.add(txtTurno, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        pnlForm.add(new JLabel("Cargo:"), gbc);
        gbc.gridx = 1;
        pnlForm.add(cbCargo, gbc);

        gbc.gridx = 0; gbc.gridy = 8;
        pnlForm.add(new JLabel("Salario (S/):"), gbc);
        gbc.gridx = 1;
        pnlForm.add(txtSalario, gbc);

        gbc.gridx = 0; gbc.gridy = 9;
        pnlForm.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        pnlForm.add(cbEstado, gbc);

        JPanel pnlBotones = new JPanel(new GridLayout(1, 3, 8, 8));
        pnlBotones.setBorder(new EmptyBorder(10, 0, 0, 0));
        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnEliminar);
        pnlBotones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 10;
        gbc.gridwidth = 2;
        pnlForm.add(pnlBotones, gbc);

        add(pnlTabla, BorderLayout.CENTER);
        add(pnlForm, BorderLayout.EAST);
    }

    private void attachEvents() {
        btnBuscar.addActionListener(e -> {
            String buscar = txtBuscar.getText().toLowerCase();
            modelEmpleados.setRowCount(0);
            Modelo.EmpleadoDAO dao = new Modelo.EmpleadoDAO();
            for (Clases.Empleado emp : dao.listarTodos()) {
                if (emp.getNombres().toLowerCase().contains(buscar) || 
                    emp.getApellidos().toLowerCase().contains(buscar) || 
                    emp.getDni().contains(buscar)) {
                    modelEmpleados.addRow(new Object[]{
                        emp.getCodigo(), emp.getNombres(), emp.getApellidos(), emp.getDni(),
                        emp.getCargo(), emp.getSueldoBase(), emp.getTelefono(), emp.isActivo() ? "Activo" : "Inactivo"
                    });
                }
            }
        });

        btnGuardar.addActionListener(e -> {
            Modelo.EmpleadoDAO dao = new Modelo.EmpleadoDAO();
            boolean isNuevo = txtId.getText().isEmpty();
            String codigo = isNuevo ? dao.generarCodigo() : txtId.getText();
            
            try {
                Clases.Empleado emp = new Clases.Empleado(
                    codigo,
                    txtDni.getText(),
                    txtNombre.getText(),
                    txtApellido.getText(),
                    cbCargo.getSelectedItem().toString(),
                    new java.util.Date(), // Fecha ingreso (hoy por defecto)
                    new java.math.BigDecimal(txtSalario.getText()),
                    txtTurno.getText(),
                    txtTelefono.getText(),
                    txtCorreo.getText(),
                    cbEstado.getSelectedIndex() == 0
                );
                
                boolean exito = isNuevo ? dao.guardar(emp) : dao.actualizar(emp);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Empleado guardado correctamente.");
                    cargarEmpleados();
                    btnLimpiar.doClick();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al guardar el empleado.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Por favor verifique que los datos sean correctos. (Ej. Salario numérico)");
            }
        });

        btnEliminar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "La eliminación física de empleados no está permitida. Cambie su estado a Inactivo.");
        });

        btnLimpiar.addActionListener(e -> {
            txtId.setText("");
            txtNombre.setText("");
            txtApellido.setText("");
            txtDni.setText("");
            txtTelefono.setText("");
            txtCorreo.setText("");
            txtTurno.setText("");
            cbCargo.setSelectedIndex(0);
            txtSalario.setText("");
            cbEstado.setSelectedIndex(0);
            tblEmpleados.clearSelection();
        });

        tblEmpleados.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblEmpleados.getSelectedRow() != -1) {
                int row = tblEmpleados.getSelectedRow();
                String codigo = modelEmpleados.getValueAt(row, 0).toString();
                Modelo.EmpleadoDAO dao = new Modelo.EmpleadoDAO();
                for (Clases.Empleado emp : dao.listarTodos()) {
                    if (emp.getCodigo().equals(codigo)) {
                        txtId.setText(emp.getCodigo());
                        txtNombre.setText(emp.getNombres());
                        txtApellido.setText(emp.getApellidos());
                        txtDni.setText(emp.getDni());
                        txtTelefono.setText(emp.getTelefono());
                        txtCorreo.setText(emp.getEmail());
                        txtTurno.setText(emp.getTurno());
                        txtSalario.setText(emp.getSueldoBase().toString());
                        cbCargo.setSelectedItem(emp.getCargo());
                        cbEstado.setSelectedIndex(emp.isActivo() ? 0 : 1);
                        break;
                    }
                }
            }
        });
    }
}

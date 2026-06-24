package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class IFrmPlanillaAsistencia extends JInternalFrame {

    private JComboBox<String> cbEmpleado;
    private JTextField txtMes;
    private JTextField txtAnio;

    private JTable tblAsistencia;
    private DefaultTableModel modelAsistencia;

    private JTextField txtDiasTrabajados;
    private JTextField txtFaltas;
    private JTextField txtVacacionesAcumuladas;
    private JTextField txtDiasVacaciones;
    private JTextField txtSalarioBase;
    private JTextField txtBonificacion;
    private JTextField txtDescuento;
    private JTextField txtPagoFinal;

    private JButton btnCalcular;
    private JButton btnRegistrarVacaciones;
    private JButton btnGenerarPlanilla;

    private static final Color COLOR_PRIMARY = new Color(25, 118, 210);
    private static final Color COLOR_ACCENT = new Color(46, 125, 50);

    public IFrmPlanillaAsistencia() {
        super("Planilla y Control de Asistencia", true, true, true, true);
        initComponents();
        buildLayout();
        attachEvents();
        setSize(1000, 620);
    }

    private void initComponents() {
        cbEmpleado = new JComboBox<>(new String[]{"Seleccione empleado", "Carlos L\u00f3pez", "Mar\u00eda Torres", "Jos\u00e9 Ram\u00edrez"});
        txtMes = new JTextField(8);
        txtAnio = new JTextField(6);

        String[] columns = {"D\u00eda", "Fecha", "Entrada", "Salida", "Horas", "Estado"};
        modelAsistencia = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblAsistencia = new JTable(modelAsistencia);

        txtDiasTrabajados = new JTextField();
        txtDiasTrabajados.setEditable(false);
        txtFaltas = new JTextField();
        txtFaltas.setEditable(false);
        txtVacacionesAcumuladas = new JTextField();
        txtVacacionesAcumuladas.setEditable(false);
        txtDiasVacaciones = new JTextField();
        txtSalarioBase = new JTextField();
        txtBonificacion = new JTextField();
        txtDescuento = new JTextField();
        txtPagoFinal = new JTextField();
        txtPagoFinal.setEditable(false);
        txtPagoFinal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtPagoFinal.setForeground(COLOR_ACCENT);

        btnCalcular = new JButton("Calcular Pago");
        btnCalcular.setBackground(COLOR_PRIMARY);
        btnCalcular.setForeground(Color.WHITE);

        btnRegistrarVacaciones = new JButton("Registrar Vacaciones");
        btnRegistrarVacaciones.setBackground(new Color(255, 143, 0));
        btnRegistrarVacaciones.setForeground(Color.WHITE);

        btnGenerarPlanilla = new JButton("Generar Planilla del Mes");
        btnGenerarPlanilla.setBackground(COLOR_ACCENT);
        btnGenerarPlanilla.setForeground(Color.WHITE);
        btnGenerarPlanilla.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    private void buildLayout() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel pnlSelector = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlSelector.setBorder(BorderFactory.createTitledBorder("Seleccionar Empleado y Per\u00edodo"));
        pnlSelector.add(new JLabel("Empleado:"));
        pnlSelector.add(cbEmpleado);
        pnlSelector.add(new JLabel("Mes:"));
        pnlSelector.add(txtMes);
        pnlSelector.add(new JLabel("A\u00f1o:"));
        pnlSelector.add(txtAnio);

        JPanel pnlCentral = new JPanel(new BorderLayout(10, 10));

        JPanel pnlTabla = new JPanel(new BorderLayout(5, 5));
        pnlTabla.setBorder(BorderFactory.createTitledBorder("Registro de Asistencia"));
        pnlTabla.add(new JScrollPane(tblAsistencia), BorderLayout.CENTER);
        pnlCentral.add(pnlTabla, BorderLayout.CENTER);

        JPanel pnlDerecho = new JPanel(new GridBagLayout());
        pnlDerecho.setBorder(BorderFactory.createTitledBorder("C\u00e1lculo de Pago y Vacaciones"));
        pnlDerecho.setPreferredSize(new Dimension(340, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        pnlDerecho.add(new JLabel("D\u00edas Trabajados:"), gbc);
        gbc.gridx = 1;
        pnlDerecho.add(txtDiasTrabajados, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        pnlDerecho.add(new JLabel("Faltas / Inasistencias:"), gbc);
        gbc.gridx = 1;
        pnlDerecho.add(txtFaltas, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        pnlDerecho.add(new JLabel("Vacaciones Acumuladas (d\u00edas):"), gbc);
        gbc.gridx = 1;
        pnlDerecho.add(txtVacacionesAcumuladas, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        pnlDerecho.add(new JLabel("D\u00edas a Tomar Vacaciones:"), gbc);
        gbc.gridx = 1;
        pnlDerecho.add(txtDiasVacaciones, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        JSeparator sep = new JSeparator();
        sep.setPreferredSize(new Dimension(0, 10));
        pnlDerecho.add(sep, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        pnlDerecho.add(new JLabel("Salario Base:"), gbc);
        gbc.gridx = 1;
        pnlDerecho.add(txtSalarioBase, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        pnlDerecho.add(new JLabel("Bonificaci\u00f3n:"), gbc);
        gbc.gridx = 1;
        pnlDerecho.add(txtBonificacion, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        pnlDerecho.add(new JLabel("Descuentos:"), gbc);
        gbc.gridx = 1;
        pnlDerecho.add(txtDescuento, gbc);

        gbc.gridx = 0; gbc.gridy = 8;
        pnlDerecho.add(new JLabel("Pago Final:"), gbc);
        gbc.gridx = 1;
        pnlDerecho.add(txtPagoFinal, gbc);

        JPanel pnlBotones = new JPanel(new GridLayout(3, 1, 5, 5));
        pnlBotones.setBorder(new EmptyBorder(10, 0, 0, 0));
        pnlBotones.add(btnCalcular);
        pnlBotones.add(btnRegistrarVacaciones);
        pnlBotones.add(btnGenerarPlanilla);

        gbc.gridx = 0; gbc.gridy = 9;
        gbc.gridwidth = 2;
        pnlDerecho.add(pnlBotones, gbc);

        pnlCentral.add(pnlDerecho, BorderLayout.EAST);

        add(pnlSelector, BorderLayout.NORTH);
        add(pnlCentral, BorderLayout.CENTER);
    }

    private void attachEvents() {
        cbEmpleado.addActionListener(e -> {
            // TODO: l\u00f3gica TXT para cargar asistencia del empleado seleccionado del mes/a\u00f1o
        });

        btnCalcular.addActionListener(e -> {
            // TODO: l\u00f3gica TXT para calcular pago en base a asistencia, salario base, bonificaciones y descuentos
            // F\u00f3rmula: pago = salarioBase + bonificacion - descuento
        });

        btnRegistrarVacaciones.addActionListener(e -> {
            // TODO: l\u00f3gica TXT para registrar vacaciones y actualizar d\u00edas acumulados en empleados.txt
        });

        btnGenerarPlanilla.addActionListener(e -> {
            // TODO: l\u00f3gica TXT para generar planilla completa del mes (todos los empleados) y guardar en planilla.txt o PDF
            JOptionPane.showMessageDialog(this, "Planilla del mes generada exitosamente.");
        });
    }
}

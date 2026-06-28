package Vista;

import Clases.Empleado;
import Clases.Evaluacion;
import Modelo.EmpleadoDAO;
import Modelo.EvaluacionDAO;
import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Vista para Registrar Evaluación de Desempeño.
 * Implementa: FR-038
 */
public class IFrmEvaluacionDesempeno extends JInternalFrame {

    private JComboBox<String> cbEmpleado;
    private JComboBox<String> cbPuntaje;
    private JTextArea txtComentarios;
    private JButton btnRegistrar;
    
    private JTable tblHistorial;
    private DefaultTableModel modelHistorial;

    private EvaluacionDAO evaluacionDAO;
    private EmpleadoDAO empleadoDAO;

    public IFrmEvaluacionDesempeno() {
        super("Evaluación de Desempeño", true, true, true, true);
        evaluacionDAO = new EvaluacionDAO();
        empleadoDAO = new EmpleadoDAO();
        
        initComponents();
        buildLayout();
        attachEvents();
        setSize(800, 500);
        cargarEmpleados();
    }

    private void initComponents() {
        cbEmpleado = new JComboBox<>();
        cbEmpleado.addItem("Seleccione un empleado");
        cbEmpleado.setFont(UIKit.BODY);

        cbPuntaje = new JComboBox<>(new String[]{
            "1 - Muy Deficiente", 
            "2 - Deficiente", 
            "3 - Aceptable", 
            "4 - Bueno", 
            "5 - Excelente"
        });
        cbPuntaje.setFont(UIKit.BODY);

        txtComentarios = new JTextArea(4, 20);
        txtComentarios.setFont(UIKit.BODY);
        txtComentarios.setLineWrap(true);
        txtComentarios.setWrapStyleWord(true);

        btnRegistrar = UIKit.primaryButton("Registrar Evaluación");

        String[] cols = {"Fecha", "Puntaje", "Comentarios"};
        modelHistorial = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblHistorial = UIKit.styledTable(modelHistorial);
    }

    private void buildLayout() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(
                UIKit.SPACE_MD, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        getContentPane().add(UIKit.screenHeader("Evaluación de Desempeño", "Personal > Evaluación (FR-038)"), BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(UIKit.SPACE_LG, 0));
        cuerpo.setOpaque(false);

        // Panel izquierdo: Historial
        JPanel pnlHistorial = UIKit.card();
        pnlHistorial.setLayout(new BorderLayout(0, UIKit.SPACE_SM));
        pnlHistorial.add(UIKit.sectionHeader("Historial del Empleado", null), BorderLayout.NORTH);
        
        JScrollPane scroll = new JScrollPane(tblHistorial);
        scroll.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        pnlHistorial.add(scroll, BorderLayout.CENTER);
        cuerpo.add(pnlHistorial, BorderLayout.CENTER);

        // Panel derecho: Formulario
        JPanel pnlForm = UIKit.card();
        pnlForm.setPreferredSize(new Dimension(300, 0));
        pnlForm.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0; 
        
        gbc.gridy = 0; gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(UIKit.sectionHeader("Nueva Evaluación", null), gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Empleado"), gbc);
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(cbEmpleado, gbc);

        gbc.gridy = 3; gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Puntaje (1-5)"), gbc);
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        pnlForm.add(cbPuntaje, gbc);

        gbc.gridy = 5; gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        pnlForm.add(UIKit.fieldLabel("Comentarios"), gbc);
        gbc.gridy = 6; gbc.insets = new Insets(0, 0, UIKit.SPACE_LG, 0);
        
        JScrollPane scrollComentarios = new JScrollPane(txtComentarios);
        pnlForm.add(scrollComentarios, gbc);

        gbc.gridy = 7; gbc.weighty = 1.0; gbc.anchor = GridBagConstraints.NORTH;
        pnlForm.add(btnRegistrar, gbc);

        cuerpo.add(pnlForm, BorderLayout.EAST);
        getContentPane().add(cuerpo, BorderLayout.CENTER);
    }

    private void cargarEmpleados() {
        for (Empleado e : empleadoDAO.listarTodos()) {
            if (e.isActivo()) {
                cbEmpleado.addItem(e.getCodigo() + " - " + e.getNombres() + " " + e.getApellidos());
            }
        }
    }

    private void attachEvents() {
        cbEmpleado.addActionListener(e -> {
            if (cbEmpleado.getSelectedIndex() > 0) {
                String idEmpleado = cbEmpleado.getSelectedItem().toString().split(" - ")[0];
                cargarHistorial(idEmpleado);
            } else {
                modelHistorial.setRowCount(0);
            }
        });

        btnRegistrar.addActionListener(e -> {
            if (cbEmpleado.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un empleado", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (txtComentarios.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese comentarios para la evaluación", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String idEmpleado = cbEmpleado.getSelectedItem().toString().split(" - ")[0];
            int puntaje = cbPuntaje.getSelectedIndex() + 1;
            String comentarios = txtComentarios.getText().trim();
            String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
            
            Evaluacion eval = new Evaluacion(0, idEmpleado, puntaje, comentarios, fecha);
            if (evaluacionDAO.registrar(eval)) {
                JOptionPane.showMessageDialog(this, "Evaluación registrada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                txtComentarios.setText("");
                cbPuntaje.setSelectedIndex(0);
                cargarHistorial(idEmpleado);
                
                Utils.BitacoraService.registrar(Clases.Sesion.getUsuario(), Utils.BitacoraService.MOD_RRHH, "REGISTRO_EVALUACION", Utils.BitacoraService.OK, "Puntaje: " + puntaje);
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar la evaluación", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void cargarHistorial(String idEmpleado) {
        modelHistorial.setRowCount(0);
        List<Evaluacion> lista = evaluacionDAO.listarPorEmpleado(idEmpleado);
        for (Evaluacion e : lista) {
            modelHistorial.addRow(new Object[]{
                e.getFecha(),
                e.getPuntaje() + "/5",
                e.getComentarios()
            });
        }
    }
}

package edu.umg.programacion2.proyecto.ui.vista;

import edu.umg.programacion2.proyecto.dao.CitaDAO;
import edu.umg.programacion2.proyecto.modelo.Cita;
import edu.umg.programacion2.proyecto.modelo.EstadoCita;
import edu.umg.programacion2.proyecto.ui.util.EstiloUtil;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class FormularioCitaFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtPaciente;
    private JTextField txtMotivo;
    private JComboBox<Integer> comboDia;
    private JComboBox<String> comboMes;
    private JComboBox<Integer> comboAnio;
    private JComboBox<String> comboHora;
    private JComboBox<Integer> comboSala;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private CitaDAO citaDAO = new CitaDAO();

    public FormularioCitaFrame() {
        setTitle("Agendar Nueva Cita Médica");
        setSize(520, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        
        // Fondo general
        getContentPane().setBackground(EstiloUtil.COLOR_FONDO);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        // Tarjeta contenedor usando EstiloUtil
        JPanel panelTarjeta = EstiloUtil.crearPanelTarjeta();
        panelTarjeta.setLayout(new GridLayout(6, 2, 12, 14));

        Font fuenteBase = new Font("Segoe UI", Font.PLAIN, 13);

        // Paciente
        JLabel lblPaciente = new JLabel("Nombre del Paciente:");
        lblPaciente.setFont(fuenteBase);
        lblPaciente.setForeground(EstiloUtil.COLOR_TEXTO);
        txtPaciente = new JTextField();
        txtPaciente.setFont(fuenteBase);
        panelTarjeta.add(lblPaciente);
        panelTarjeta.add(txtPaciente);

        // Fecha
        JLabel lblFecha = new JLabel("Fecha de la Cita:");
        lblFecha.setFont(fuenteBase);
        lblFecha.setForeground(EstiloUtil.COLOR_TEXTO);
        
        JPanel panelFecha = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        panelFecha.setOpaque(false);

        Integer[] dias = new Integer[31];
        for (int i = 0; i < 31; i++) dias[i] = i + 1;
        comboDia = new JComboBox<>(dias);
        comboDia.setFont(fuenteBase);

        comboMes = new JComboBox<>(EstiloUtil.MESES);
        comboMes.setFont(fuenteBase);

        Integer[] anios = {2026, 2027};
        comboAnio = new JComboBox<>(anios);
        comboAnio.setFont(fuenteBase);

        LocalDate hoy = LocalDate.now();
        comboDia.setSelectedItem(hoy.getDayOfMonth());
        comboMes.setSelectedIndex(hoy.getMonthValue() - 1);
        comboAnio.setSelectedItem(hoy.getYear());

        panelFecha.add(comboDia);
        panelFecha.add(comboMes);
        panelFecha.add(comboAnio);
        panelTarjeta.add(lblFecha);
        panelTarjeta.add(panelFecha);

        // Hora
        JLabel lblHora = new JLabel("Hora Inicio (Bloque 1h):");
        lblHora.setFont(fuenteBase);
        lblHora.setForeground(EstiloUtil.COLOR_TEXTO);
        String[] horas = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00"};
        comboHora = new JComboBox<>(horas);
        comboHora.setFont(fuenteBase);
        panelTarjeta.add(lblHora);
        panelTarjeta.add(comboHora);

        // Sala
        JLabel lblSala = new JLabel("Sala de Consulta:");
        lblSala.setFont(fuenteBase);
        lblSala.setForeground(EstiloUtil.COLOR_TEXTO);
        Integer[] salas = {1, 2, 3, 4};
        comboSala = new JComboBox<>(salas);
        comboSala.setFont(fuenteBase);
        panelTarjeta.add(lblSala);
        panelTarjeta.add(comboSala);

        // Motivo
        JLabel lblMotivo = new JLabel("Motivo de Consulta:");
        lblMotivo.setFont(fuenteBase);
        lblMotivo.setForeground(EstiloUtil.COLOR_TEXTO);
        txtMotivo = new JTextField();
        txtMotivo.setFont(fuenteBase);
        panelTarjeta.add(lblMotivo);
        panelTarjeta.add(txtMotivo);

        // Botones estilizados con EstiloUtil
        btnGuardar = EstiloUtil.crearBotonRedondeado("Guardar Cita", EstiloUtil.COLOR_EXITO, Color.WHITE);
        btnCancelar = EstiloUtil.crearBotonRedondeado("Cancelar", EstiloUtil.COLOR_NEUTRO, Color.WHITE);

        btnGuardar.addActionListener(e -> guardarCita());
        btnCancelar.addActionListener(e -> {
            new BienvenidaFrame().setVisible(true);
            dispose();
        });

        panelTarjeta.add(btnGuardar);
        panelTarjeta.add(btnCancelar);

        // Padding alrededor de la tarjeta
        JPanel contenedorConMargen = new JPanel(new BorderLayout());
        contenedorConMargen.setOpaque(false);
        contenedorConMargen.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contenedorConMargen.add(panelTarjeta, BorderLayout.CENTER);

        add(contenedorConMargen, BorderLayout.CENTER);
    }

    private void guardarCita() {
        try {
            String paciente = txtPaciente.getText().trim();
            String motivo = txtMotivo.getText().trim();

            if (paciente.isEmpty() || motivo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor llene todos los campos obligatorios.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int dia = (Integer) comboDia.getSelectedItem();
            int mes = comboMes.getSelectedIndex() + 1;
            int anio = (Integer) comboAnio.getSelectedItem();

            LocalDate fecha = LocalDate.of(anio, mes, dia);
            LocalTime horaInicio = LocalTime.parse((String) comboHora.getSelectedItem());
            LocalTime horaFin = horaInicio.plusHours(1);
            int sala = (Integer) comboSala.getSelectedItem();

            if (citaDAO.existeCitaEnHorario(fecha, horaInicio, horaFin, sala, 0)) {
                LocalTime horaSugerida = citaDAO.buscarSiguienteHoraDisponible(fecha, sala, 0);

                if (horaSugerida != null) {
                    int opcion = JOptionPane.showConfirmDialog(
                            this,
                            "La Sala " + sala + " ya está ocupada el " + fecha + " a las " + horaInicio + ".\n\n" +
                                    "¿Desea agendarla en el siguiente bloque disponible (" + horaSugerida + " - " + horaSugerida.plusHours(1) + ")?",
                            "Conflicto de Horario",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

                    if (opcion == JOptionPane.YES_OPTION) {
                        horaInicio = horaSugerida;
                        horaFin = horaInicio.plusHours(1);
                        comboHora.setSelectedItem(String.format("%02d:00", horaInicio.getHour()));
                    } else {
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "La Sala " + sala + " no tiene horarios disponibles para el " + fecha + ".", "Sin Disponibilidad", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Cita nuevaCita = new Cita(paciente, fecha, horaInicio, horaFin, motivo, EstadoCita.PENDIENTE, sala);

            if (citaDAO.insertar(nuevaCita)) {
                JOptionPane.showMessageDialog(this, "Cita agendada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                new BienvenidaFrame().setVisible(true);
                dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agendar cita: " + ex.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
        }
    }
}
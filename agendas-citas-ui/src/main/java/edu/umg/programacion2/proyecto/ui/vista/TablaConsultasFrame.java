package edu.umg.programacion2.proyecto.ui.vista;

import edu.umg.programacion2.proyecto.dao.CitaDAO;
import edu.umg.programacion2.proyecto.modelo.Cita;
import edu.umg.programacion2.proyecto.modelo.EstadoCita;
import edu.umg.programacion2.proyecto.ui.util.EstiloUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TablaConsultasFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTable tablaCitas;
    private DefaultTableModel modeloTabla;
    private CitaDAO citaDAO = new CitaDAO();
    private List<Cita> listaCitasActuales;
    private Timer timerAutoRefresco;

    public TablaConsultasFrame() {
        setTitle("Agenda y Estado de Consultas Médicas");
        setSize(980, 540);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        getContentPane().setBackground(EstiloUtil.COLOR_FONDO);

     // Arreglo con la nueva columna integrada
        String[] columnas = {"ID", "Sala", "Paciente", "Fecha", "Horario", "Motivo", "Estado", "1ra Visita"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCitas = new JTable(modeloTabla);
        tablaCitas.setRowHeight(30);
        tablaCitas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaCitas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaCitas.getTableHeader().setBackground(EstiloUtil.COLOR_TARJETA);
        tablaCitas.getTableHeader().setForeground(EstiloUtil.COLOR_TEXTO);
        
        tablaCitas.setDefaultRenderer(Object.class, new ResaltadoTablaRenderer());

        JScrollPane scrollPane = new JScrollPane(tablaCitas);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        scrollPane.getViewport().setBackground(EstiloUtil.COLOR_FONDO);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones inferior
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        panelBotones.setOpaque(false);

        JButton btnModificar = EstiloUtil.crearBotonRedondeado("Modificar Cita", EstiloUtil.COLOR_PRIMARIO, Color.WHITE);
        JButton btnCambiarEstado = EstiloUtil.crearBotonRedondeado("Cambiar Estado", EstiloUtil.COLOR_EXITO, Color.WHITE);
        // Botón para consultar el conteo por categoría (Map)
        JButton btnResumenMotivos = EstiloUtil.crearBotonRedondeado("Resumen por Motivo", EstiloUtil.COLOR_PRIMARIO, Color.WHITE);
        JButton btnRegresar = EstiloUtil.crearBotonRedondeado("Regresar al Menú", EstiloUtil.COLOR_NEUTRO, Color.WHITE);
       

        btnModificar.addActionListener(e -> abrirEdicionCita());
        btnCambiarEstado.addActionListener(e -> cambiarEstadoCita());
        btnResumenMotivos.addActionListener(e -> mostrarResumenMotivos()); // Listener del nuevo botón
        btnRegresar.addActionListener(e -> {
        	if (timerAutoRefresco != null) {
                timerAutoRefresco.stop(); // Detener el temporizador al regresar al menú
            }
            new BienvenidaFrame().setVisible(true);
            dispose();
        });

        panelBotones.add(btnModificar);
        panelBotones.add(btnCambiarEstado);
        panelBotones.add(btnResumenMotivos); // Agregado aquí
        panelBotones.add(btnRegresar);

        add(panelBotones, BorderLayout.SOUTH);
        
        timerAutoRefresco = new Timer(30000, e -> cargarDatos());
        timerAutoRefresco.start();

        // Detener el temporizador si el usuario cierra la ventana desde la 'X'
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (timerAutoRefresco != null) {
                    timerAutoRefresco.stop();
                }
            }
        });

        cargarDatos();
    }

    private void cargarDatos() {
        try {
            modeloTabla.setRowCount(0);
            listaCitasActuales = citaDAO.listarTodas();

            for (Cita c : listaCitasActuales) {
                String rangoHorario = String.format("%s - %s", c.getHoraInicio(), c.getHoraFin());
                String primeraVisitaStr = c.isEsPrimeraVisita() ? "Sí" : "No";
                
                modeloTabla.addRow(new Object[]{
                        c.getId(),
                        "Sala " + c.getSala(),
                        c.getNombrePaciente(),
                        c.getFechaCita(),
                        rangoHorario,
                        c.getMotivo(),
                        c.getEstado(),
                        primeraVisitaStr
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la lista de citas: " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirEdicionCita() {
        int fila = tablaCitas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita de la tabla para modificar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (listaCitasActuales == null || fila >= listaCitasActuales.size()) {
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la cita.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cita citaSeleccionada = listaCitasActuales.get(fila);

        String[] opcionesHoras = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00"};
        JComboBox<String> comboHora = new JComboBox<>(opcionesHoras);
        JComboBox<Integer> comboSala = new JComboBox<>(new Integer[]{1, 2, 3, 4});

        comboHora.setSelectedItem(String.format("%02d:00", citaSeleccionada.getHoraInicio().getHour()));
        comboSala.setSelectedItem(citaSeleccionada.getSala());

        JPanel panelForm = new JPanel(new GridLayout(2, 2, 10, 10));
        panelForm.add(new JLabel("Nueva Hora Inicio:"));
        panelForm.add(comboHora);
        panelForm.add(new JLabel("Nueva Sala:"));
        panelForm.add(comboSala);

        int result = JOptionPane.showConfirmDialog(
                this, 
                panelForm, 
                "Modificar Cita #" + citaSeleccionada.getId() + " (" + citaSeleccionada.getNombrePaciente() + ")", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalTime nuevaHora = LocalTime.parse((String) comboHora.getSelectedItem());
                LocalTime nuevaHoraFin = nuevaHora.plusHours(1);
                int nuevaSala = (Integer) comboSala.getSelectedItem();
                LocalDate fecha = citaSeleccionada.getFechaCita();

                if (citaDAO.existeCitaEnHorario(fecha, nuevaHora, nuevaHoraFin, nuevaSala, citaSeleccionada.getId())) {
                    LocalTime sugerida = citaDAO.buscarSiguienteHoraDisponible(fecha, nuevaSala, citaSeleccionada.getId());
                    String msgSugerencia = (sugerida != null) 
                            ? "\n\nHora disponible sugerida en Sala " + nuevaSala + ": " + sugerida + " a " + sugerida.plusHours(1) 
                            : "\n\nNo hay horarios libres en esta sala para esa fecha.";
                    
                    JOptionPane.showMessageDialog(this, "La Sala " + nuevaSala + " ya está ocupada a las " + nuevaHora + "." + msgSugerencia, "Conflicto de Horario", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Cita citaEditada = new Cita(
                        citaSeleccionada.getId(),
                        citaSeleccionada.getNombrePaciente(),
                        fecha,
                        nuevaHora,
                        nuevaHoraFin,
                        citaSeleccionada.getMotivo(),
                        citaSeleccionada.getEstado(),
                        nuevaSala,
                        citaSeleccionada.isEsPrimeraVisita()
                );

                if (citaDAO.actualizarCitaCompleta(citaEditada)) {
                    JOptionPane.showMessageDialog(this, "Cita modificada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarDatos();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar la cita: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cambiarEstadoCita() {
        int fila = tablaCitas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita de la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cita citaSeleccionada = listaCitasActuales.get(fila);
        EstadoCita nuevoEstado = (EstadoCita) JOptionPane.showInputDialog(
                this, 
                "Seleccione el nuevo estado para el paciente " + citaSeleccionada.getNombrePaciente() + ":", 
                "Cambiar Estado de Cita",
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                EstadoCita.values(), 
                citaSeleccionada.getEstado()
        );

        if (nuevoEstado != null) {
            try {
                if (citaDAO.actualizarEstado(citaSeleccionada.getId(), nuevoEstado)) {
                    cargarDatos();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar el estado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class ResaltadoTablaRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            String estadoStr = table.getValueAt(row, 6).toString();

            if (!isSelected) {
                switch (estadoStr) {
                    case "EN_CONSULTA":
                        c.setBackground(new Color(224, 231, 255)); // Azul suave Material
                        c.setForeground(EstiloUtil.COLOR_TEXTO);
                        break;
                    case "EXPIRADA":
                        c.setBackground(new Color(254, 226, 226)); // Rojo suave Material
                        c.setForeground(EstiloUtil.COLOR_TEXTO);
                        break;
                    case "COMPLETADA":
                        c.setBackground(new Color(209, 250, 229)); // Verde esmeralda suave
                        c.setForeground(EstiloUtil.COLOR_TEXTO);
                        break;
                    case "CANCELADA":
                        c.setBackground(new Color(241, 245, 249)); // Gris pizarra claro
                        c.setForeground(EstiloUtil.COLOR_SECUNDARIO_TEXTO);
                        break;
                    default:
                        c.setBackground(Color.WHITE);
                        c.setForeground(EstiloUtil.COLOR_TEXTO);
                        break;
                }
            }
            return c;
        }
    }
    
    private void mostrarResumenMotivos() {
        try {
            java.util.Map<String, Integer> conteoMap = citaDAO.obtenerConteoPorMotivo();

            if (conteoMap.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay citas registradas para agrupar.", "Resumen Vacío", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder("=== CANTIDAD DE CITAS POR MOTIVO ===\n\n");
            for (java.util.Map.Entry<String, Integer> entry : conteoMap.entrySet()) {
                sb.append("• ").append(entry.getKey()).append(": ").append(entry.getValue()).append(" cita(s)\n");
            }

            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Consolas", Font.PLAIN, 13));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(380, 220));

            JOptionPane.showMessageDialog(this, scrollPane, "Resumen Estadístico por Categoría", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al calcular el resumen: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
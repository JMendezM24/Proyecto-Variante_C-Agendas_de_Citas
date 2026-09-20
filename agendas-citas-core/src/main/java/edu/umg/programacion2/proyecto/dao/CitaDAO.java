package edu.umg.programacion2.proyecto.dao;

import edu.umg.programacion2.proyecto.modelo.Cita;
import edu.umg.programacion2.proyecto.modelo.EstadoCita;
import edu.umg.programacion2.proyecto.util.ConexionBD;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class CitaDAO {

	public boolean insertar(Cita cita) throws SQLException {
        String sql = "INSERT INTO citas (nombre_paciente, fecha_cita, hora_cita, hora_fin, sala, motivo, estado, es_primera_visita) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cita.getNombrePaciente());
            ps.setDate(2, Date.valueOf(cita.getFechaCita()));
            ps.setTime(3, Time.valueOf(cita.getHoraInicio()));
            ps.setTime(4, Time.valueOf(cita.getHoraFin()));
            ps.setInt(5, cita.getSala());
            ps.setString(6, cita.getMotivo());
            ps.setString(7, cita.getEstado().name());
            ps.setBoolean(8, cita.isEsPrimeraVisita());
            
            return ps.executeUpdate() > 0;
        }
    }
    
	// Marca como EXPIRADAS o COMPLETADAS las citas pasadas
    public void actualizarCitasExpiradas() {
        // 1. PENDIENTE -> EXPIRADA si la hora fin ya pasó
        String sqlExpiradas = "UPDATE citas SET estado = 'EXPIRADA' " +
                             "WHERE estado = 'PENDIENTE' AND (fecha_cita < CURRENT_DATE() " +
                             "OR (fecha_cita = CURRENT_DATE() AND hora_fin <= CURRENT_TIME()))";

        // 2. EN_CONSULTA -> COMPLETADA si el horario ya terminó
        String sqlCompletadas = "UPDATE citas SET estado = 'COMPLETADA' " +
                               "WHERE estado = 'EN_CONSULTA' AND (fecha_cita < CURRENT_DATE() " +
                               "OR (fecha_cita = CURRENT_DATE() AND hora_fin <= CURRENT_TIME()))";

        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sqlExpiradas);
            stmt.executeUpdate(sqlCompletadas);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Cita> listarTodas() throws SQLException {
        actualizarCitasExpiradas();

        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT * FROM citas ORDER BY sala ASC, fecha_cita ASC, hora_cita ASC";

        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Time hFin = rs.getTime("hora_fin");
                LocalTime horaInicio = rs.getTime("hora_cita").toLocalTime();
                LocalTime horaFinLocal = (hFin != null) ? hFin.toLocalTime() : horaInicio.plusHours(1);

                Cita c = new Cita(
                        rs.getInt("id"),
                        rs.getString("nombre_paciente"),
                        rs.getDate("fecha_cita").toLocalDate(),
                        horaInicio,
                        horaFinLocal,
                        rs.getString("motivo"),
                        EstadoCita.valueOf(rs.getString("estado")),
                        rs.getInt("sala"),
                        rs.getBoolean("es_primera_visita")
                );
                lista.add(c);
            }
        }
        return lista;
    }
    
 // Verifica choque de horario (permite ignorar el ID actual si se está editando)
    public boolean existeCitaEnHorario(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, int sala, int idExcluir) throws SQLException {
        String sql = "SELECT COUNT(*) FROM citas WHERE fecha_cita = ? AND sala = ? AND id != ? AND estado NOT IN ('CANCELADA', 'EXPIRADA') " +
                     "AND ((hora_cita < ? AND hora_fin > ?) OR (hora_cita >= ? AND hora_cita < ?))";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(fecha));
            stmt.setInt(2, sala);
            stmt.setInt(3, idExcluir);
            stmt.setTime(4, Time.valueOf(horaFin));
            stmt.setTime(5, Time.valueOf(horaInicio));
            stmt.setTime(6, Time.valueOf(horaInicio));
            stmt.setTime(7, Time.valueOf(horaFin));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    
 // Busca el primer bloque de 1 hora libre dentro de la jornada (08:00 a 17:00)
    public LocalTime buscarSiguienteHoraDisponible(LocalDate fecha, int sala, int idExcluir) {
        try {
            for (int h = 8; h <= 17; h++) {
                LocalTime inicio = LocalTime.of(h, 0);
                LocalTime fin = inicio.plusHours(1);
                if (!existeCitaEnHorario(fecha, inicio, fin, sala, idExcluir)) {
                    return inicio;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean actualizarCitaCompleta(Cita cita) throws SQLException {
        String sql = "UPDATE citas SET fecha_cita = ?, hora_cita = ?, hora_fin = ?, sala = ?, motivo = ?, estado = ?, es_primera_visita = ? WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(cita.getFechaCita()));
            stmt.setTime(2, Time.valueOf(cita.getHoraInicio()));
            stmt.setTime(3, Time.valueOf(cita.getHoraFin()));
            stmt.setInt(4, cita.getSala());
            stmt.setString(5, cita.getMotivo());
            stmt.setString(6, cita.getEstado().name());
            stmt.setBoolean(7, cita.isEsPrimeraVisita());
            stmt.setInt(8, cita.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean actualizarEstado(int id, EstadoCita nuevoEstado) throws SQLException {
        String sql = "UPDATE citas SET estado = ? WHERE id = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado.name());
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        }
    }
    
 // Calcula la frecuencia de citas por motivo usando Map<String, Integer> recorriendo listarTodas()
    public Map<String, Integer> obtenerConteoPorMotivo() throws SQLException {
        List<Cita> citas = listarTodas();
        Map<String, Integer> conteoMap = new HashMap<>();

        for (Cita c : citas) {
            String motivo = c.getMotivo();
            conteoMap.put(motivo, conteoMap.getOrDefault(motivo, 0) + 1);
        }

        return conteoMap;
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM citas WHERE id = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }
}
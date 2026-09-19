package edu.umg.programacion2.proyecto.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/agenda_citas_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    
    // Obtiene la clave de la variable DB_PASSWORD.
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection obtenerConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC de MySQL no encontrado en el classpath.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
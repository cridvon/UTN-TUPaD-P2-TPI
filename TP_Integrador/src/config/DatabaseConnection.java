/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

/**
 *
 * @author Mario Campana
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase de utilidad para obtener conexiones JDBC a MySQL.
 *
 * Lee primero las propiedades del sistema:
 *   -Ddb.url
 *   -Ddb.user
 *   -Ddb.password
 *
 * Si no están definidas, usa valores por defecto:
 *   url: jdbc:mysql://localhost:3306/tpip2
 *   user: root
 *   password: (vacío)
 */
public final class DatabaseConnection {

    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/tpip2?useSSL=false&serverTimezone=UTC";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "Holacomova01-";

    static {
        try {
            // Driver moderno de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");  //Revisar que esté en la librería
        } catch (ClassNotFoundException e) {
            System.err.println("No se pudo cargar el driver de MySQL: " + e.getMessage());
        }
    }

    private DatabaseConnection() {
        // Evita instanciación
    }

    public static Connection getConnection() throws SQLException {
        String url = System.getProperty("db.url", DEFAULT_URL);
        String user = System.getProperty("db.user", DEFAULT_USER);
        String password = System.getProperty("db.password", DEFAULT_PASSWORD);

        return DriverManager.getConnection(url, user, password);
    }
}



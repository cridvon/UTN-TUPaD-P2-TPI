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
import java.sql.SQLException;

/**
 * Maneja una transacción sobre una única Connection.
 * Se usa con try-with-resources:
 *
 * try (TransactionManager tx = new TransactionManager()) {
 *     Connection conn = tx.getConnection();
 *     // ... usar conn en DAOs ...
 *     tx.commit();
 * }
 * // Si no se llama a commit(), hace rollback en close()
 */
public class TransactionManager implements AutoCloseable {

    private final Connection connection;
    private boolean completed;

    public TransactionManager() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
        this.connection.setAutoCommit(false);
        this.completed = false;
    }

    public Connection getConnection() {
        return connection;
    }

    public void commit() throws SQLException {
        connection.commit();
        completed = true;
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.err.println("Error al hacer rollback: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            if (!completed) {
                rollback();
            }
            connection.setAutoCommit(true);
            connection.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar TransactionManager: " + e.getMessage());
        }
    }
}


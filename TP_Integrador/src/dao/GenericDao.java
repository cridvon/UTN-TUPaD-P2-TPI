/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

/**
 *
 * @author Mario Campana
 */

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Interface genérica para DAOs.
 * Todos los métodos trabajan con una Connection externa
 * para permitir transacciones coordinadas.
 */
public interface GenericDao<T> {

    T crear(T entity, Connection connection) throws SQLException;

    Optional<T> leerPorId(long id, Connection connection) throws SQLException;

    List<T> leerTodos(Connection connection) throws SQLException;

    void actualizar(T entity, Connection connection) throws SQLException;

    void eliminar(long id, Connection connection) throws SQLException;
}


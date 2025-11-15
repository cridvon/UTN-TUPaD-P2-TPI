/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Mario Campana
 */

import entities.DomicilioFiscal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC de GenericDao para DomicilioFiscal.
 *
 * Trabaja siempre con una Connection externa, que debe ser
 * administrada por la capa de servicio (para permitir transacciones).
 */
public class DomicilioFiscalDao implements GenericDao<DomicilioFiscal> {

    @Override
    public DomicilioFiscal crear(DomicilioFiscal entity, Connection connection) throws SQLException {
        String sql = "INSERT INTO Domicilios (eliminado, calle, numero) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBoolean(1, entity.isEliminado());
            ps.setString(2, entity.getCalle());
            if (entity.getNumero() != null) {
                ps.setInt(3, entity.getNumero());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long idGenerado = rs.getLong(1);
                    entity.setId(idGenerado);
                }
            }
        }

        return entity;
    }

    @Override
    public Optional<DomicilioFiscal> leerPorId(long id, Connection connection) throws SQLException {
        String sql = "SELECT id, eliminado, calle, numero " +
                     "FROM Domicilios " +
                     "WHERE id = ? AND eliminado = FALSE";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    @Override
    public List<DomicilioFiscal> leerTodos(Connection connection) throws SQLException {
        String sql = "SELECT id, eliminado, calle, numero " +
                     "FROM Domicilios " +
                     "WHERE eliminado = FALSE " +
                     "ORDER BY id";

        List<DomicilioFiscal> lista = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        }

        return lista;
    }

    @Override
    public void actualizar(DomicilioFiscal entity, Connection connection) throws SQLException {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("No se puede actualizar un domicilio sin id.");
        }

        String sql = "UPDATE Domicilios " +
                     "SET eliminado = ?, calle = ?, numero = ? " +
                     "WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBoolean(1, entity.isEliminado());
            ps.setString(2, entity.getCalle());
            if (entity.getNumero() != null) {
                ps.setInt(3, entity.getNumero());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setLong(4, entity.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(long id, Connection connection) throws SQLException {
        // Baja lógica: marcamos eliminado = TRUE
        String sql = "UPDATE Domicilios SET eliminado = TRUE WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    // --------- Métodos auxiliares ---------

    private DomicilioFiscal mapRow(ResultSet rs) throws SQLException {
        DomicilioFiscal d = new DomicilioFiscal();
        d.setId(rs.getLong("id"));
        d.setEliminado(rs.getBoolean("eliminado"));
        d.setCalle(rs.getString("calle"));
        int numero = rs.getInt("numero");
        if (rs.wasNull()) {
            d.setNumero(null);
        } else {
            d.setNumero(numero);
        }
        return d;
    }
}


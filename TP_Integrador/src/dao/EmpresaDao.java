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
import entities.Empresa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC de GenericDao para Empresa.
 *
 * Usa LEFT JOIN con Domicilios para poblar el DomicilioFiscal.
 */
public class EmpresaDao implements GenericDao<Empresa> {

    @Override
    public Empresa crear(Empresa entity, Connection connection) throws SQLException {
        String sql = "INSERT INTO Empresas " +
                     "(eliminado, razonSocial, cuit, actividadPrincipal, email, domicilioFiscal_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBoolean(1, entity.isEliminado());
            ps.setString(2, entity.getRazonSocial());
            ps.setString(3, entity.getCuit());
            ps.setString(4, entity.getActividadPrincipal());
            ps.setString(5, entity.getEmail());

            if (entity.getDomicilioFiscal() != null && entity.getDomicilioFiscal().getId() != null) {
                ps.setLong(6, entity.getDomicilioFiscal().getId());
            } else {
                ps.setNull(6, Types.BIGINT);
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
    public Optional<Empresa> leerPorId(long id, Connection connection) throws SQLException {
        String sql = "SELECT " +
                     "  e.id AS e_id, e.eliminado AS e_eliminado, " +
                     "  e.razonSocial, e.cuit, e.actividadPrincipal, e.email, e.domicilioFiscal_id, " +
                     "  d.id AS d_id, d.eliminado AS d_eliminado, d.calle, d.numero " +
                     "FROM Empresas e " +
                     "LEFT JOIN Domicilios d ON e.domicilioFiscal_id = d.id AND d.eliminado = FALSE " +
                     "WHERE e.id = ? AND e.eliminado = FALSE";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Empresa empresa = mapRow(rs);
                    return Optional.of(empresa);
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    @Override
    public List<Empresa> leerTodos(Connection connection) throws SQLException {
        String sql = "SELECT " +
                     "  e.id AS e_id, e.eliminado AS e_eliminado, " +
                     "  e.razonSocial, e.cuit, e.actividadPrincipal, e.email, e.domicilioFiscal_id, " +
                     "  d.id AS d_id, d.eliminado AS d_eliminado, d.calle, d.numero " +
                     "FROM Empresas e " +
                     "LEFT JOIN Domicilios d ON e.domicilioFiscal_id = d.id AND d.eliminado = FALSE " +
                     "WHERE e.eliminado = FALSE " +
                     "ORDER BY e.id";

        List<Empresa> lista = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        }

        return lista;
    }

    @Override
    public void actualizar(Empresa entity, Connection connection) throws SQLException {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("No se puede actualizar una empresa sin id.");
        }

        String sql = "UPDATE Empresas " +
                     "SET eliminado = ?, razonSocial = ?, cuit = ?, " +
                     "    actividadPrincipal = ?, email = ?, domicilioFiscal_id = ? " +
                     "WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBoolean(1, entity.isEliminado());
            ps.setString(2, entity.getRazonSocial());
            ps.setString(3, entity.getCuit());
            ps.setString(4, entity.getActividadPrincipal());
            ps.setString(5, entity.getEmail());

            if (entity.getDomicilioFiscal() != null && entity.getDomicilioFiscal().getId() != null) {
                ps.setLong(6, entity.getDomicilioFiscal().getId());
            } else {
                ps.setNull(6, Types.BIGINT);
            }

            ps.setLong(7, entity.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(long id, Connection connection) throws SQLException {
        // Baja lógica: marcamos eliminado = TRUE
        String sql = "UPDATE Empresas SET eliminado = TRUE WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    // --------- Métodos auxiliares ---------

    private Empresa mapRow(ResultSet rs) throws SQLException {
        Empresa e = new Empresa();
        e.setId(rs.getLong("e_id"));
        e.setEliminado(rs.getBoolean("e_eliminado"));
        e.setRazonSocial(rs.getString("razonSocial"));
        e.setCuit(rs.getString("cuit"));
        e.setActividadPrincipal(rs.getString("actividadPrincipal"));
        e.setEmail(rs.getString("email"));

        Long domicilioId = rs.getLong("d_id");
        if (!rs.wasNull()) {
            DomicilioFiscal d = new DomicilioFiscal();
            d.setId(domicilioId);
            d.setEliminado(rs.getBoolean("d_eliminado"));
            d.setCalle(rs.getString("calle"));
            int numero = rs.getInt("numero");
            if (rs.wasNull()) {
                d.setNumero(null);
            } else {
                d.setNumero(numero);
            }
            e.setDomicilioFiscal(d);
        } else {
            e.setDomicilioFiscal(null);
        }

        return e;
    }
}


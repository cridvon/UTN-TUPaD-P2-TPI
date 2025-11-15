/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author Mario Campana
 */

import config.TransactionManager;
import dao.DomicilioFiscalDao;
import entities.DomicilioFiscal;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de DomicilioFiscalService.
 */
public class DomicilioFiscalServiceImpl implements DomicilioFiscalService {

    private final DomicilioFiscalDao domicilioDao;

    public DomicilioFiscalServiceImpl() {
        this.domicilioDao = new DomicilioFiscalDao();
    }

    @Override
    public DomicilioFiscal insertar(DomicilioFiscal entity) {
        validar(entity);

        try (TransactionManager tx = new TransactionManager()) {
            Connection conn = tx.getConnection();
            DomicilioFiscal creado = domicilioDao.crear(entity, conn);
            tx.commit();
            return creado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar domicilio fiscal", e);
        }
    }

    @Override
    public DomicilioFiscal actualizar(DomicilioFiscal entity) {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("El domicilio a actualizar debe tener id.");
        }
        validar(entity);

        try (TransactionManager tx = new TransactionManager()) {
            Connection conn = tx.getConnection();
            domicilioDao.actualizar(entity, conn);
            tx.commit();
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar domicilio fiscal", e);
        }
    }

    @Override
    public void eliminar(long id) {
        try (TransactionManager tx = new TransactionManager()) {
            Connection conn = tx.getConnection();
            domicilioDao.eliminar(id, conn);
            tx.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar domicilio fiscal", e);
        }
    }

    @Override
    public Optional<DomicilioFiscal> getById(long id) {
        try (TransactionManager tx = new TransactionManager()) {
            Connection conn = tx.getConnection();
            return domicilioDao.leerPorId(id, conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener domicilio fiscal por id", e);
        }
    }

    @Override
    public List<DomicilioFiscal> getAll() {
        try (TransactionManager tx = new TransactionManager()) {
            Connection conn = tx.getConnection();
            return domicilioDao.leerTodos(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar domicilios fiscales", e);
        }
    }

    // --------- Validaciones básicas ---------

    private void validar(DomicilioFiscal d) {
        if (d == null) {
            throw new IllegalArgumentException("El domicilio no puede ser null.");
        }
        if (d.getCalle() == null || d.getCalle().trim().isEmpty()) {
            throw new IllegalArgumentException("La calle es obligatoria.");
        }
        // numero puede ser null, así que no forzamos nada acá.
    }
}


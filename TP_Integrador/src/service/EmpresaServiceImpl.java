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
import dao.EmpresaDao;
import entities.DomicilioFiscal;
import entities.Empresa;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de EmpresaService.
 *
 * Orquesta DAOs de Empresa y DomicilioFiscal,
 * maneja transacciones y validaciones básicas.
 */
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaDao empresaDao;
    private final DomicilioFiscalDao domicilioDao;

    public EmpresaServiceImpl() {
        this.empresaDao = new EmpresaDao();
        this.domicilioDao = new DomicilioFiscalDao();
    }

    // ---- Métodos de GenericService<Empresa> ----

    @Override
    public Empresa insertar(Empresa entity) {
        validar(entity);

        try (TransactionManager tx = new TransactionManager()) {
            Connection conn = tx.getConnection();

            // En este método asumimos que, si hay domicilioFiscal, ya tiene id válido
            Empresa creada = empresaDao.crear(entity, conn);

            tx.commit();
            return creada;
        } catch (SQLException e) {
            manejarExcepcionSqlEmpresa("insertar empresa", e);
            return null; // nunca llega acá, pero el compilador lo pide
        }
    }

    @Override
    public Empresa actualizar(Empresa entity) {
        if (entity.getId() == null) {
            throw new IllegalArgumentException("La empresa a actualizar debe tener id.");
        }
        validar(entity);

        try (TransactionManager tx = new TransactionManager()) {
            Connection conn = tx.getConnection();
            empresaDao.actualizar(entity, conn);
            tx.commit();
            return entity;
        } catch (SQLException e) {
            manejarExcepcionSqlEmpresa("actualizar empresa", e);
            return null;
        }
    }

    @Override
    public void eliminar(long id) {
        try (TransactionManager tx = new TransactionManager()) {
            Connection conn = tx.getConnection();
            empresaDao.eliminar(id, conn);
            tx.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar empresa", e);
        }
    }

    @Override
    public Optional<Empresa> getById(long id) {
        try (TransactionManager tx = new TransactionManager()) {
            Connection conn = tx.getConnection();
            return empresaDao.leerPorId(id, conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener empresa por id", e);
        }
    }

    @Override
    public List<Empresa> getAll() {
        try (TransactionManager tx = new TransactionManager()) {
            Connection conn = tx.getConnection();
            return empresaDao.leerTodos(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar empresas", e);
        }
    }

    // ---- Métodos específicos de EmpresaService ----

    @Override
    public Empresa crearEmpresaConDomicilio(Empresa empresa, DomicilioFiscal domicilioFiscal) {
        validar(empresa);
        // domicilioFiscal puede ser null (empresa sin domicilio)

        try (TransactionManager tx = new TransactionManager()) {
            Connection conn = tx.getConnection();

            // 1) Si vino domicilio, lo creamos primero
            if (domicilioFiscal != null) {
                validarDomicilio(domicilioFiscal);
                domicilioFiscal = domicilioDao.crear(domicilioFiscal, conn);
                empresa.setDomicilioFiscal(domicilioFiscal);
            }

            // 2) Creamos la empresa asociada (o sin domicilio si era null)
            Empresa creada = empresaDao.crear(empresa, conn);

            tx.commit();
            return creada;
        } catch (SQLException e) {
            manejarExcepcionSqlEmpresa("crear empresa con domicilio", e);
            return null;
        }
    }

    @Override
    public Optional<Empresa> buscarPorCuit(String cuit) {
        if (cuit == null || cuit.trim().isEmpty()) {
            throw new IllegalArgumentException("El CUIT no puede ser vacío.");
        }

        try (TransactionManager tx = new TransactionManager()) {
            Connection conn = tx.getConnection();
            // Reutilizamos leerTodos + filtro simple.
            // (Si quisieras hacerlo más eficiente, podrías agregar un método específico en EmpresaDao.)
            List<Empresa> todas = empresaDao.leerTodos(conn);
            return todas.stream()
                    .filter(e -> cuit.equals(e.getCuit()))
                    .findFirst();
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar empresa por CUIT", e);
        }
    }

    @Override
    public List<Empresa> buscarPorRazonSocial(String patron) {
        if (patron == null || patron.trim().isEmpty()) {
            throw new IllegalArgumentException("El patrón de razón social no puede ser vacío.");
        }

        try (TransactionManager tx = new TransactionManager()) {
            Connection conn = tx.getConnection();
            List<Empresa> todas = empresaDao.leerTodos(conn);
            List<Empresa> filtradas = new ArrayList<>();

            String patronLower = patron.toLowerCase();

            for (Empresa e : todas) {
                if (e.getRazonSocial() != null &&
                        e.getRazonSocial().toLowerCase().contains(patronLower)) {
                    filtradas.add(e);
                }
            }

            return filtradas;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar empresas por razón social", e);
        }
    }

    // --------- Validaciones ---------

    private void validar(Empresa e) {
        if (e == null) {
            throw new IllegalArgumentException("La empresa no puede ser null.");
        }
        if (e.getRazonSocial() == null || e.getRazonSocial().trim().isEmpty()) {
            throw new IllegalArgumentException("La razón social es obligatoria.");
        }
        if (e.getCuit() == null || e.getCuit().trim().isEmpty()) {
            throw new IllegalArgumentException("El CUIT es obligatorio.");
        }
        // Podrías agregar más validaciones de formato de CUIT si querés.
    }

    private void validarDomicilio(DomicilioFiscal d) {
        if (d == null) {
            throw new IllegalArgumentException("El domicilio no puede ser null.");
        }
        if (d.getCalle() == null || d.getCalle().trim().isEmpty()) {
            throw new IllegalArgumentException("La calle es obligatoria.");
        }
    }

    // --------- Manejo de errores SQL ---------

    private void manejarExcepcionSqlEmpresa(String operacion, SQLException e) {
        // 23000 suele ser violación de constraint (ej: UNIQUE CUIT)
        if ("23000".equals(e.getSQLState())) {
            throw new RuntimeException(
                    "Error al " + operacion + ": posible violación de restricción (CUIT duplicado u otra constraint).",
                    e
            );
        }
        throw new RuntimeException("Error al " + operacion, e);
    }
}


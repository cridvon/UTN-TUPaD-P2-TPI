/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

/**
 *
 * @author Mario Campana
 */

import entities.Empresa;
import entities.DomicilioFiscal;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar Empresas.
 * Extiende las operaciones CRUD genéricas y agrega algunas específicas.
 */
public interface EmpresaService extends GenericService<Empresa> {

    /**
     * Crea una empresa y, opcionalmente, su domicilio fiscal en una sola transacción.
     * Si domicilioFiscal es null, crea solo la empresa.
     */
    Empresa crearEmpresaConDomicilio(Empresa empresa, DomicilioFiscal domicilioFiscal);

    /**
     * Busca una empresa por CUIT (sin incluir eliminadas).
     */
    Optional<Empresa> buscarPorCuit(String cuit);

    /**
     * Busca empresas por patrón de razón social (ej: "%SA%").
     */
    List<Empresa> buscarPorRazonSocial(String patron);
}


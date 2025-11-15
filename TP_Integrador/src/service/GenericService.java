/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

/**
 *
 * @author Mario Campana
 */

import java.util.List;
import java.util.Optional;

/**
 * Interface genérica para la capa de servicio.
 * Define operaciones típicas de CRUD con reglas de negocio.
 */
public interface GenericService<T> {

    T insertar(T entity);

    T actualizar(T entity);

    void eliminar(long id);

    Optional<T> getById(long id);

    List<T> getAll();
}


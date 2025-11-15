/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

/**
 *
 * @author Mario Campana
 */

/**
 * Clase base para todas las entidades persistentes.
 * Contiene id y eliminado (baja lógica).
 */

public abstract class Base {

    protected Long id;
    protected boolean eliminado;

    public Base() {
    }

    public Base(Long id, boolean eliminado) {
        this.id = id;
        this.eliminado = eliminado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    @Override
    public String toString() {
        return "Base{" +
                "id=" + id +
                ", eliminado=" + eliminado +
                '}';
    }
}


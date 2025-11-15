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
 * Entidad B: DomicilioFiscal
 *
 * Tabla sugerida en BD: Domicilios
 * Campos:
 *   - id (PK, BIGINT, AUTO_INCREMENT)
 *   - eliminado (BOOLEAN)
 *   - calle (VARCHAR(100), NOT NULL)
 *   - numero (INT)
 */
public class DomicilioFiscal extends Base {

    private String calle;
    private Integer numero;

    public DomicilioFiscal() {
        super();
    }

    public DomicilioFiscal(Long id, boolean eliminado, String calle, Integer numero) {
        super(id, eliminado);
        this.calle = calle;
        this.numero = numero;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "DomicilioFiscal{" +
                "id=" + id +
                ", eliminado=" + eliminado +
                ", calle='" + calle + '\'' +
                ", numero=" + numero +
                '}';
    }
}


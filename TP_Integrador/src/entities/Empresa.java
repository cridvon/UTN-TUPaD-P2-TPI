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
 * Entidad A: Empresa
 *
 * Tabla sugerida en BD: Empresas
 * Campos:
 *   - id (PK, BIGINT, AUTO_INCREMENT)
 *   - eliminado (BOOLEAN)
 *   - razonSocial (VARCHAR(120), NOT NULL)
 *   - cuit (VARCHAR(13), NOT NULL, UNIQUE)
 *   - actividadPrincipal (VARCHAR(80))
 *   - email (VARCHAR(120))
 *   - domicilioFiscal_id (BIGINT, UNIQUE, FK a Domicilios.id, puede ser NULL)
 *
 * Relación 1→1 unidireccional:
 *   Empresa contiene un atributo DomicilioFiscal.
 */
public class Empresa extends Base {

    private String razonSocial;
    private String cuit;
    private String actividadPrincipal;
    private String email;

    // Relación 1→1 (A → B)
    private DomicilioFiscal domicilioFiscal;

    public Empresa() {
        super();
    }

    public Empresa(Long id,
                   boolean eliminado,
                   String razonSocial,
                   String cuit,
                   String actividadPrincipal,
                   String email,
                   DomicilioFiscal domicilioFiscal) {
        super(id, eliminado);
        this.razonSocial = razonSocial;
        this.cuit = cuit;
        this.actividadPrincipal = actividadPrincipal;
        this.email = email;
        this.domicilioFiscal = domicilioFiscal;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getActividadPrincipal() {
        return actividadPrincipal;
    }

    public void setActividadPrincipal(String actividadPrincipal) {
        this.actividadPrincipal = actividadPrincipal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DomicilioFiscal getDomicilioFiscal() {
        return domicilioFiscal;
    }

    public void setDomicilioFiscal(DomicilioFiscal domicilioFiscal) {
        this.domicilioFiscal = domicilioFiscal;
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "id=" + id +
                ", eliminado=" + eliminado +
                ", razonSocial='" + razonSocial + '\'' +
                ", cuit='" + cuit + '\'' +
                ", actividadPrincipal='" + actividadPrincipal + '\'' +
                ", email='" + email + '\'' +
                ", domicilioFiscal=" + (domicilioFiscal != null ? domicilioFiscal : "SIN DOMICILIO") +
                '}';
    }
}

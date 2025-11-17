# 🏛️ Sistema de Gestión de Empresas y Domicilios Fiscales

## Trabajo Práctico Integrador - Programación 2

---

## 1. 💼 Descripción del Dominio y Arquitectura

### A. Descripción del Dominio Elegido
El sistema está diseñado para gestionar dos entidades principales: **Empresa** y **DomicilioFiscal**.

* **Empresa**: Representa una persona jurídica con atributos como Razón Social, CUIT (único), Actividad Principal y Email.
* **Domicilio Fiscal**: Representa la dirección física registrada, con atributos como Calle y Número.

La relación clave es **1:1 unidireccional**: Cada `Empresa` puede tener *cero o un* `DomicilioFiscal`. Esta relación es manejada en la base de datos a través de una clave foránea (FK) con restricción `UNIQUE` en la tabla `Empresas`.

### B. Arquitectura de Capas

El proyecto está estructurado en una arquitectura de **cuatro capas** para garantizar la separación de responsabilidades:

1.  **Modelo (entities):** Contiene las clases `Base`, `Empresa` y `DomicilioFiscal`.
2.  **Acceso a Datos (dao):** Contiene la interfaz `GenericDao<T>` y sus implementaciones (`EmpresaDao`, `DomicilioFiscalDao`). Se encarga de la persistencia y mapeo directo con la base de datos, trabajando exclusivamente con una `Connection` externa.
3.  **Lógica de Negocio (service):** Contiene la interfaz `GenericService<T>` y sus implementaciones (`EmpresaServiceImpl`, `DomicilioFiscalServiceImpl`). **Orquesta las transacciones** (usando `TransactionManager`), aplica las reglas de negocio (ej. validaciones) y coordina el uso de los DAOs.
4.  **Presentación (main/config):** Contiene `AppMenu` (la interfaz de consola) y `Main` (el punto de entrada). También incluye la lógica de infraestructura (`DatabaseConnection`, `TransactionManager`).

---

## 2. ⚙️ Requisitos y Configuración de la Base de Datos

### A. Requisitos de Software

| Componente | Versión Requerida |
| :--- | :--- |
| **Java** | 17+ |
| **MySQL** | 8.x |
| **Driver JDBC** | `mysql-connector-java` (gestionado por Gradle) |

### B. Creación y Configuración de la Base de Datos

**1. Archivo SQL:**
El archivo `.sql` provisto debe ejecutarse en el motor de MySQL para crear el esquema.

**2. Pasos para la creación:**

1.  **Iniciar MySQL Server:** Asegúrate de que el servicio de MySQL esté corriendo.
2.  **Crear Base de Datos:** Abre un cliente MySQL (Workbench, línea de comandos, etc.) y ejecuta el comando para crear el esquema, por ejemplo:
    ```sql
    CREATE DATABASE tpip2;
    ```
3.  **Ejecutar Script:** Selecciona la base de datos y ejecuta el contenido completo del archivo `.sql` provisto (o el contenido a continuación) para crear las tablas `Domicilios` y `Empresas` con sus relaciones y restricciones.

```sql
-- Estructura de la base de datos 'tpip2' (ejemplo)

-- Crear tabla Domicilios (entidad B)
CREATE TABLE Domicilios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    calle VARCHAR(100) NOT NULL,
    numero INT
);

-- Crear tabla Empresas (entidad A)
CREATE TABLE Empresas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    razonSocial VARCHAR(120) NOT NULL,
    cuit VARCHAR(13) NOT NULL UNIQUE, -- Restricción UNIQUE para CUIT
    actividadPrincipal VARCHAR(80),
    email VARCHAR(120),
    domicilioFiscal_id BIGINT UNIQUE, -- UNIQUE para la relación 1:1

    -- Relación 1:1 con Domicilios
    CONSTRAINT fk_domicilio
        FOREIGN KEY (domicilioFiscal_id)
        REFERENCES Domicilios(id)
);

## 3. ▶️ Compilación y Ejecución

---

### A. Compilación

Utilice **Gradle** desde la terminal en la carpeta raíz del proyecto:

```bash
./gradlew clean build

## 4. 📺 Enlace al Video Demostrativo

[Insertar Enlace al Video Demostrativo Aquí]
*(Ejemplo: `https://youtube.com/link-a-la-demostracion`)*
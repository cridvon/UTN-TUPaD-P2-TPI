# Sistema de Gestión de Empresas y Domicilios Fiscales

## Trabajo Práctico Integrador - Programación 2

### Descripción del Proyecto

Este Trabajo Práctico Integrador tiene como objetivo demostrar la aplicación práctica de los conceptos fundamentales de Programación Orientada a Objetos y Persistencia de Datos aprendidos durante el cursado de Programación 2. El proyecto consiste en desarrollar un sistema completo de gestión de empresas y domicilios fiscales que permita realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre estas entidades, implementando una arquitectura robusta y profesional.

### Enlace al video

https://drive.google.com/file/d/1iVQu0hwD831VUpfBHG_ovGSuK-kwDMLn/view?usp=drive_link

### Objetivos Académicos

El desarrollo de este sistema permite aplicar y consolidar los siguientes conceptos clave de la materia:

**1. Arquitectura en Capas (Layered Architecture)**

- Implementación de separación de responsabilidades en 4 capas diferenciadas
- Capa de Presentación (Main/UI): Interacción con el usuario mediante consola
- Capa de Lógica de Negocio (Service): Validaciones y reglas de negocio
- Capa de Acceso a Datos (DAO): Operaciones de persistencia
- Capa de Modelo (Models): Representación de entidades del dominio

**2. Programación Orientada a Objetos**

- Aplicación de principios SOLID (Single Responsibility, Dependency Injection)
- Uso de herencia mediante clase abstracta Base
- Implementación de interfaces genéricas (GenericDAO, GenericService)
- Encapsulamiento con atributos privados y métodos de acceso
- Sobrescritura de métodos (equals, hashCode, toString)

**3. Persistencia de Datos con JDBC**

- Conexión a base de datos MySQL mediante JDBC
- Implementación del patrón DAO (Data Access Object)
- Uso de PreparedStatements para prevenir SQL Injection
- Gestión de transacciones con commit y rollback
- Manejo de claves autogeneradas (AUTO_INCREMENT)
- Consultas con LEFT JOIN para relaciones entre entidades

**4. Manejo de Recursos y Excepciones**

- Uso del patrón try-with-resources para gestión automática de recursos JDBC
- Implementación de AutoCloseable en TransactionManager
- Manejo apropiado de excepciones con propagación controlada
- Validación multi-nivel: base de datos y aplicación

**5. Patrones de Diseño**

- Factory Pattern (DatabaseConnection)
- Service Layer Pattern (separación lógica de negocio)
- DAO Pattern (abstracción del acceso a datos)
- Soft Delete Pattern (eliminación lógica de registros)
- Dependency Injection manual

**6. Validación de Integridad de Datos**

- Validación de unicidad (Cuit único por empresa)
- Validación de campos obligatorios en múltiples niveles
- Validación de integridad referencial (Foreign Keys)
- Implementación de eliminación segura para prevenir referencias huérfanas

### Funcionalidades Implementadas

El sistema permite gestionar dos entidades principales con las siguientes operaciones:

## Características Principales

- **Gestión de empresas**: Crear, listar, actualizar y eliminar empresas con validación de cuit único
- **Gestión de Domicilios Fiscales**: Administrar domicilios fiscales de forma independiente o asociados a empresas
- **Búsqueda Inteligente**: Filtrar empresas por cuit.
- **Soft Delete**: Eliminación lógica que preserva la integridad de datos
- **Seguridad**: Protección contra SQL injection mediante PreparedStatements
- **Validación Multi-capa**: Validaciones en capa de servicio y base de datos
- **Transacciones**: Soporte para operaciones atómicas con rollback automático

## Requisitos del Sistema

| Componente        | Versión Requerida       |
| ----------------- | ----------------------- |
| Java JDK          | 17 o superior           |
| MySQL             | 8.0 o superior          |
| Gradle            | 8.12 (incluido wrapper) |
| Sistema Operativo | Windows, Linux o macOS  |

## Instalación

### 1. Configurar Base de Datos

Ejecutar el siguiente script SQL en MySQL:

````sql
CREATE DATABASE IF NOT EXISTS tpip2;
USE tpip2;

CREATE TABLE Domicilios (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    eliminado BOOLEAN DEFAULT FALSE,
    calle VARCHAR(100) NOT NULL,
    numero INT
 );

 CREATE TABLE Empresas (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    eliminado BOOLEAN DEFAULT FALSE,
    razonSocial VARCHAR(120) NOT NULL,
    cuit VARCHAR(13) NOT NULL UNIQUE,
    actividadPrincipal VARCHAR(80),
    email VARCHAR(120),
    domicilioFiscal_id BIGINT UNIQUE,  -- Relación 1 a 1 con DomicilioFiscal

    CONSTRAINT fk_empresa_domicilio
        FOREIGN KEY (domicilioFiscal_id)
        REFERENCES Domicilios(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
 );
 ```


### 2. Configurar Conexión (Opcional)

Por defecto conecta a:

- **Host**: localhost:3306
- **Base de datos**: dbtpip2
- **Usuario**: root
- **Contraseña**: (vacía)


## Ejecución

### Ejecutar desde IDE


-- 1. Abrir proyecto en Apache Netbeans

-- 2. Localizar JAR de MySQL dentro del proyecto y configurar la dirección
-- ..\UTN-TUPaD-P2-TPI\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0.jar

-- 3. Ejecutar clase `Main.Main`


## Uso del Sistema

### Menú Principal

```
========= MENU EMPRESAS =========
1. Crear empresa (con o sin domicilio)
2. Listar empresas
3. Buscar empresa por CUIT
4. Actualizar empresa
5. Eliminar empresa
========= MENU DOMICILIOS =========
6. Crear domicilio fiscal
7. Listar domicilios fiscales
8. Actualizar domicilio fiscal
9. Eliminar domicilio fiscal
0. Salir
```

### Operaciones Disponibles

#### 1. Crear Empresa

- Captura razón social, cuit, actividad pricipal y email
- Permite agregar domicilio fiscal opcionalmente
- Valida CUIT único (no permite duplicados)

**Ejemplo:**

```
Razón social: Vaca Loca
Cuit: 2038900981
Actividad pricipal: Carniceria
Email: vacaloca@gmail.com
¿Desea agregar un domicilio fiscal? (s/n): s
Calle: Savio
Numero: 123
```

#### 2. Listar Empresas

- Muestra un listado de las empresas existentes con sus correspondientes datos.
```

**Resultado:**

```
== Listado de empresas ==
Empresa{id=1, eliminado=false, razonSocial='Vaca Loca', cuit='2038900981', actividadPrincipal='Carniceria', email='vacaloca@gmail.com', domicilioFiscal=DomicilioFiscal{id=1, eliminado=false, calle='Savio', numero=123}}
```

#### 3. Actualizar Empresa

- Permite modificar Razón social, Actividad principal, Cuit, Email
- Permite mantener o quitar el domicilio fiscal
- Presionar Enter sin escribir mantiene el valor actual
- Requiere ID

**Ejemplo:**

```
Empresa actual:
Empresa{id=1, eliminado=false, razonSocial='Vaca Loca', cuit='2038900981', actividadPrincipal='Carniceria', email='vacaloca@gmail.com', domicilioFiscal=DomicilioFiscal{id=1, eliminado=false, calle='Savio', numero=123}}
Deje vacío el campo para mantener el valor actual.
Nueva razón social (Vaca Loca):
Nuevo CUIT (2038900981):
Nueva actividad (Carniceria): Frigorífico
Nuevo email (vacaloca@gmail.com):
Domicilio actual: domicilioFiscal=DomicilioFiscal{id=1, eliminado=false, calle='Savio', numero=123}
Opciones de domicilio:
1. Mantener como está
2. Quitar domicilio (dejar empresa sin domicilio) / 2. Asignar domicilio
Seleccione opción (1-2, Enter para 1):
Empresa actualizada correctamente:
Empresa{id=1, eliminado=false, razonSocial='Vaca Loca', cuit='2038900981', actividadPrincipal='Frigorífico', email='vacaloca@gmail.com', domicilioFiscal=DomicilioFiscal{id=1, eliminado=false, calle='Savio', numero=123}}


```

#### 4. Eliminar Empresa

- Eliminación lógica (marca como eliminada, no borra físicamente)
- Requiere ID de la empresa

#### 5. Crear Domicilio fiscal

- Crea domicilio independiente sin asociarlo a empresa
- Puede asociarse posteriormente

#### 6. Listar Domicilios fisacles

- Muestra todos los domicilios fiscales activos con ID, calle y número

#### 7. Actualizar Domicilio fiscal por ID

- Actualiza calle y/o número de cualquier domicilio fiscal
- Requiere ID del domicilio fiscal

#### 8. Eliminar Domicilio fiscal por ID

- ✅ **Eliminación segura**: Primero actualiza la referencia en empresa, luego elimina
- Previene referencias huérfanas


## Arquitectura

### Estructura en Capas

```
┌─────────────────────────────────────┐
│     Main / UI Layer                 │
│  (Interacción con usuario)          │
│  AppMenu, Main                      │
└───────────┬─────────────────────────┘
            │
┌───────────▼─────────────────────────┐
│     Service Layer                   │
│  (Lógica de negocio y validación)   │
│  EmpresaServiceImpl                 │
│  DomicilioServiceImpl               │
│  GenericService                     │
└───────────┬─────────────────────────┘
            │
┌───────────▼─────────────────────────┐
│     DAO Layer                       │
│  (Acceso a datos)                   │
│  EmpresaDAO, DomicilioFiscalDAO,    │
│  GenericDAO                         │
└───────────┬─────────────────────────┘
            │
┌───────────▼─────────────────────────┐
│     Models Layer                    │
│  (Entidades de dominio)             │
│  Empresa, DomicilioFiscal, Base     │
└─────────────────────────────────────┘
```

### Componentes Principales

**Config**

- `DatabaseConnection.java`: Gestión de conexiones JDBC con validación en inicialización estática.
- `TransactionManager.java`: Manejo de transacciones con AutoCloseable.

**Entities**

- `Base.java`: Clase abstracta con campos id y eliminado.
- `Empresa.java`: Entidad Empresa (nombre, apellido, dni, domicilio).
- `Domicilio.java`: Entidad Domicilio (calle, numero).

**Dao**

- `GenericDAO<T>`: Interface genérica con operaciones CRUD.
- `EmpresaDAO`: Implementación con queries LEFT JOIN para incluir domicilio.
- `DomicilioDAO`: Implementación para domicilios.

**Service**

- `GenericService<T>`: Interface genérica para servicios
- `EmpresaService`: Interface para la gestión de `Empresa`, incluyendo operaciones de negocio como la **coordinación de creación con domicilio**.
- `DomicilioFiscalService`: Interface para la gestión de `DomicilioFiscal`, extendiendo al servicio genérico.
- `EmpresaServiceImpl`: Implementación de validaciones de empresa y coordinación con domicilios.
- `DomicilioFiscalServiceImpl`: Implementación de validaciones de domicilio.

**Main**

- `Main.java`: Punto de entrada.
- `AppMenu.java`: Implementación de operaciones CRUD con captura de entrada.

## Modelo de Datos

```
┌────────────────────┐          ┌──────────────────┐
│     Empresa        │          │   Domicilio      │
├────────────────────┤          ├──────────────────┤
│ id (PK)            │          │ id (PK)          │
│ razon_social       │          │ calle            │
│ actividad          │          │ numero           │
│ cuit (UNIQUE)      │          │ eliminado        │
│ domicilio_id (FK)  │──────┐   └──────────────────┘
│ email              │      │
│ eliminado          │      │
└────────────────────┘      │
                            │
                            └──▶ Relación 0..1
```

**Reglas:**

- Una empresa puede tener 0 o 1 domicilio
- Cuit es único (constraint en base de datos y validación en aplicación)
- Eliminación lógica: campo `eliminado = TRUE`
- Foreign key `domicilio_id` puede ser NULL

## Patrones y Buenas Prácticas

### Seguridad

- **100% PreparedStatements**: Prevención de SQL injection
- **Validación multi-capa**: Service layer valida antes de persistir
- **Cuit único**: Constraint en BD + validación en `EmpresaServiceImpl.validateCuitUnique()`

### Gestión de Recursos

- **Try-with-resources**: Todas las conexiones, statements y resultsets
- **AutoCloseable**: TransactionManager cierra y hace rollback automático
- **Scanner cerrado**: En `AppMenu.run()` al finalizar

### Validaciones

- **Input trimming**: Todos los inputs usan `.trim()` inmediatamente
- **Campos obligatorios**: Validación de null y empty en service layer
- **IDs positivos**: Validación `id > 0` en todas las operaciones --Esto no lo se
- **Verificación de rowsAffected**: En UPDATE y DELETE

### Soft Delete

- DELETE ejecuta: `UPDATE tabla SET eliminado = TRUE WHERE id = ?`
- SELECT filtra: `WHERE eliminado = FALSE`
- No hay eliminación física de datos

## Reglas de Negocio Principales

1. **Cuit único**: No se permiten empresas con Cuit duplicado
2. **Campos obligatorios**: razon_social y Cuit son requeridos para empresa
3. **Validación antes de persistir**: Service layer valida antes de llamar a DAO
4. **Preservación de valores**: En actualización, campos vacíos mantienen valor original
5. **Búsqueda flexible**: LIKE con % permite coincidencias parciales
6. **Transacciones**: Operaciones complejas soportan rollback

## Solución de Problemas

### Error: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"

**Causa**: JAR de MySQL no está en classpath

**Solución**: Incluir mysql-connector-j-8.4.0.jar en el comando java -cp

### Error: "Communications link failure"

**Causa**: MySQL no está ejecutándose

### Error: "Access denied for user 'root'@'localhost'"

**Causa**: Credenciales incorrectas

**Solución**: Verificar usuario/contraseña en DatabaseConnection.java o usar -Ddb.user y -Ddb.password

### Error: "Unknown database 'dbtpi3'"

**Causa**: Base de datos no creada

**Solución**: Ejecutar script de creación de base de datos (ver sección Instalación)

### Error: "Table 'empresas' doesn't exist"

**Causa**: Tablas no creadas

**Solución**: Ejecutar script de creación de tablas (ver sección Instalación)


## Documentación Adicional

- **HISTORIAS_DE_USUARIO.md**: Especificaciones funcionales completas
  - Historias de usuario detalladas
  - Reglas de negocio numeradas
  - Criterios de aceptación en formato Gherkin
  - Diagramas de flujo

## Tecnologías Utilizadas

- **Lenguaje**: Java 24.0.2
- **Base de Datos**: MySQL 10.x
- **JDBC Driver**: mysql-connector-j 9.5.0


## Estructura de Directorios

```
UTN-TUPaD-P2-TPI
│
├── TP_Integrador/src/
│   ├── config/          # Configuración de BD y transacciones
│   ├── dao/             # Capa de acceso a datos
│   ├── entities/        # Entidades de dominio
│   ├── main/            # UI y punto de entrada
│   └── Service/         # Lógica de negocio
│
├── mysql-connector-j-9.5.0/ # Contiene el .jar para realizar la conexion con mysql
│
├── Database/            # Contiene las consultas necesarias para la creacion y llenado inicial de la base de datos.
│
├── README.md            # Este archivo
└── HISTORIAS_DE_USUARIO.md  # Especificaciones funcionales
└── Diagrama_UML.pdf     # Diagrama UML del proyecto
└── Informe.pdf          # Informe del proyecto
```



## Contexto Académico

**Materia**: Programación 2
**Tipo de Evaluación**: Trabajo Práctico Integrador (TPI)
**Modalidad**: Desarrollo de sistema CRUD con persistencia en base de datos
**Objetivo**: Aplicar conceptos de POO, JDBC, arquitectura en capas y patrones de diseño

Este proyecto representa la integración de todos los conceptos vistos durante el cuatrimestre, demostrando capacidad para:

- Diseñar sistemas con arquitectura profesional
- Implementar persistencia de datos con JDBC
- Aplicar patrones de diseño apropiados
- Manejar recursos y excepciones correctamente
- Validar integridad de datos en múltiples niveles
- Documentar código de forma profesional

---
**Proyecto Educativo** - Trabajo Práctico Integrador de Programación 2
**Alumnos**: Campana, Mario - Chiavón, Cristian - Chiavón, Facundo - Kohn, Shai.
````

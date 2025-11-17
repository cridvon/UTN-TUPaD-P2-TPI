# Historias de Usuario - Sistema de Empresas y Domicilios Fiscales

Especificaciones funcionales completas del sistema CRUD de empresas y domicilos fiscales.

---

# Tabla de Contenidos
1. [Épica 1 – Gestión de Empresas](#épica-1--gestión-de-empresas)  
2. [Épica 2 – Gestión de Domicilios Fiscales](#épica-2--gestión-de-domicilios-fiscales)  
3. [Épica 3 – Relación 1→0..1 Empresa–DomicilioFiscal](#épica-3--relación-10-1-empresadomiciliofiscal)  
4. [Épica 4 – Transacciones y Atomicidad](#épica-4--transacciones-y-atomicidad)  
5. [Reglas de Negocio](#reglas-de-negocio)  
6. [Diagrama UML y Modelo de Datos](#diagrama-uml-y-modelo-de-datos)  

---

# Épica 1 – Gestión de Empresas
La gestión de empresas constituye el eje principal del sistema. Incluye operaciones CRUD completas y funcionalidades de búsqueda basadas en CUIT.

---

## HU-001 – Crear Empresa
**Como** usuario administrativo  
**Quiero** registrar una nueva empresa en el sistema  
**Para** mantener un registro actualizado y consistente de entidades fiscales

### Criterios de Aceptación (Gherkin)
```gherkin
Escenario: Alta de empresa sin domicilio fiscal
  Dado que el usuario selecciona "Crear empresa"
  Y completa razón social, CUIT, actividad y email válidos
  Y selecciona "No" cuando se le pregunta si desea agregar domicilio fiscal
  Entonces el sistema crea la empresa con domicilioFiscal_id = NULL
  Y muestra el ID asignado

Escenario: Alta de empresa con CUIT duplicado
  Dado que en la base existe una empresa con CUIT "20389009817"
  Cuando el usuario intenta registrar una nueva empresa con ese mismo CUIT
  Entonces sistema detecta SQLException con estado 23000
  Y se muestra "Error: el CUIT ingresado ya existe"
  Y no se inserta ningún registro

Escenario: Falta de razón social
  Cuando el usuario deja vacía la razón social
  Entonces el sistema lanza IllegalArgumentException
  Y muestra "La razón social es obligatoria"
```

### Referencias Técnicas
| Capa | Elemento | Descripción |
|------|----------|-------------|
| Presentación | `AppMenu.crearEmpresa()` | Orquestra la captura de datos y delega en el service |
| Servicio | `EmpresaServiceImpl.insertar()` | Valida y gestiona errores de BD |
| Persistencia | `EmpresaDao.crear()` | Ejecución SQL con RETURN_GENERATED_KEYS |
| BD | UNIQUE(cuit) | Garantiza unicidad a nivel físico |

---

## HU-002 – Listar Empresas
**Como** usuario del sistema  
**Quiero** visualizar todas las empresas activas  
**Para** obtener una vista general del ecosistema empresarial registrado

### Criterios de Aceptación
```gherkin
Escenario: Listado de empresas
  Dado que existen empresas con eliminado = FALSE
  Cuando el usuario selecciona "Listar empresas"
  Entonces se muestran todas, junto con su domicilio si existe
```

### Referencias Técnicas
- `EmpresaDao.leerTodos()` usa LEFT JOIN con DomicilioFiscal  
- El filtrado por `eliminado = FALSE` proviene de la clase Base  

---

## HU-003 – Buscar Empresa por CUIT
**Como** usuario  
**Quiero** buscar rápidamente una empresa por su CUIT  
**Para** acceder a su información sin revisar el listado completo

### Criterios de Aceptación
```gherkin
Escenario: CUIT encontrado
  Cuando el usuario ingresa un CUIT válido registrado
  Entonces se muestran los datos completos de la empresa

Escenario: CUIT inexistente
  Cuando el usuario ingresa un CUIT no registrado
  Entonces se muestra "No se encontró empresa con ese CUIT"
```

---

## HU-004 – Actualizar Empresa
**Como** usuario administrativo  
**Quiero** actualizar parcialmente la información de una empresa  
**Para** mantener datos consistentes sin reescribir toda la entidad

### Criterios de Aceptación
```gherkin
Escenario: Actualización parcial
  Dado que el usuario deja campos vacíos
  Cuando confirma la actualización
  Entonces los campos vacíos mantienen su valor previo

Escenario: Email inválido
  Cuando el usuario ingresa un email sin formato correcto
  Entonces se muestra "El email ingresado no es válido"
```

---

## HU-005 – Eliminar Empresa (Baja Lógica)
**Como** usuario  
**Quiero** eliminar una empresa  
**Para** evitar que aparezca en listados sin borrar información histórica

### Criterios de Aceptación
```gherkin
Escenario: Baja lógica exitosa
  Cuando el usuario elimina una empresa existente
  Entonces eliminado = TRUE
  Y la empresa deja de aparecer en listados
```

---

# Épica 2 – Gestión de Domicilios Fiscales

## HU-006 – Crear Domicilio Fiscal
**Como** usuario del sistema  
**Quiero** registrar un domicilio fiscal  
**Para** asociarlo posteriormente a una empresa

### Criterios de Aceptación
```gherkin
Escenario: Alta de domicilio válido
  Dado que el usuario ingresa calle válida
  Y número opcional
  Entonces el sistema crea el domicilio fiscal

Escenario: Calle vacía
  Cuando el usuario deja vacío el campo calle
  Entonces se lanza IllegalArgumentException
```

---

## HU-007 – Listar Domicilios Fiscales
```gherkin
Escenario: Listado de domicilios
  Dado que existen domicilios activos
  Cuando el usuario lista los domicilios
  Entonces se muestran todos los que tienen eliminado = FALSE
```

---

## HU-008 – Actualizar Domicilio Fiscal
```gherkin
Escenario: Actualización parcial
  Dado que un campo se deja vacío
  Entonces conserva su valor actual
```

---

## HU-009 – Eliminar Domicilio Fiscal
```gherkin
Escenario: Baja lógica segura
  Dado que un domicilio está asociado a una empresa
  Cuando se elimina el domicilio
  Entonces primero se debe desasociar la empresa
  Y luego marcar eliminado = TRUE
```

---

# Épica 3 – Relación 1→0..1 Empresa–DomicilioFiscal

## HU-010 – Asociar un Domicilio a una Empresa
```gherkin
Escenario: Asociación válida
  Dado que la empresa no tiene domicilio
  Cuando se selecciona un domicilio válido
  Entonces se asigna correctamente

Escenario: Intento de asignar un segundo domicilio
  Dado que una empresa ya tiene domicilioFiscal_id != NULL
  Cuando el usuario intenta asociar otro domicilio
  Entonces el sistema muestra "La empresa ya tiene un domicilio Fiscal asignado"
```

---

# Épica 4 – Transacciones y Atomicidad

## HU-011 – Crear Empresa + Domicilio en una sola operación
**Como** usuario  
**Quiero** crear empresa y domicilio juntos  
**Para** asegurar consistencia entre tablas

### Criterios de Aceptación
```gherkin
Escenario: Operación atómica exitosa
  Dado que los datos de empresa y domicilio son válidos
  Cuando se ejecuta crearEmpresaConDomicilio
  Entonces se crea primero el domicilio
  Y luego la empresa
  Y se ejecuta commit()

Escenario: Error por CUIT duplicado
  Dado que el domicilio ya fue insertado durante la transacción
  Cuando empresaDao.crear() falla por UNIQUE(CUIT)
  Entonces rollback() revierte ambos inserts
  Y no quedan datos inconsistentes
```

### Referencias Técnicas Importantes
| Componente | Función |
|-----------|---------|
| `TransactionManager` | Control de commit/rollback automático |
| `EmpresaServiceImpl.crearEmpresaConDomicilio()` | Orquestación transaccional |
| `try-with-resources` | Garantiza rollback si no se llama commit() |

---

# Reglas de Negocio

| Código | Regla |
|--------|--------|
| RN-001 | Toda empresa debe tener razón social y CUIT |
| RN-002 | CUIT debe ser único |
| RN-003 | Calle es obligatoria en domicilio fiscal |
| RN-004 | Eliminación es lógica (eliminado = TRUE) |
| RN-005 | Relación 1→0..1 Empresa→DomicilioFiscal |
| RN-006 | No puede asignarse un domicilio eliminado |
| RN-007 | Campos vacíos en actualización conservan valor |
| RN-008 | Operación Empresa + Domicilio debe ser atómica |
| RN-009 | Uso obligatorio de transacciones JDBC |
| RN-010 | BD protege integridad con FK UNIQUE |

---

# Modelo de Datos
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

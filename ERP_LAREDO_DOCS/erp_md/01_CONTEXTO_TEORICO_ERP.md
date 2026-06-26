# 📚 CONTEXTO TEÓRICO ERP — Aplicado al Minimarket LAREDO
**Sesiones 9, 10 y 11 · Ms. Ing. Elías Santa Cruz · UPAO Sistemas Empresariales**

---

## 1. ¿Qué es un ERP y por qué LAREDO lo necesita? (Sesión 9)

> *"Los ERP han dejado de ser una moda y han pasado a ser una herramienta de productividad."*  
> — Ms. Ing. Elías Santa Cruz, Sesión 9

El sistema actual del Minimarket LAREDO es un **TPS** (Transaction Processing System) que registra ventas en archivos `.txt`. El objetivo del curso es evolucionarlo a un **ERP** con las siguientes características (Laudon & Laudon, 2018):

| Característica ERP | Definición (Sesión 9) | Cómo aplica en LAREDO |
|--------------------|-----------------------|----------------------|
| **Integración** | Los departamentos se relacionan entre sí, comparten datos en tiempo real | Que al registrar una venta, el stock baje automáticamente Y la caja lo registre Y la bitácora lo anote |
| **Modularidad** | Funcionalidad dividida en módulos instalables según necesidad | Los 7 módulos (Dashboard, Ventas, Inventario, Compras, Finanzas, RRHH, Seguridad) |
| **Adaptabilidad** | Parametrización de procesos según las salidas requeridas | Stock mínimo configurable por producto, roles configurables, tipos de descuento parametrizables |
| **Escalabilidad** | Crece junto con la empresa | Migración de `.txt` a H2, luego a MySQL/PostgreSQL sin cambiar la lógica de negocio |
| **Seguridad** | Control de accesos y permisos | RBAC: Cajero / Gerente / Administrador con pantallas diferenciadas |

---

## 2. El Corazón del ERP: Base de Datos Única (Sesión 9)

El concepto central de la Sesión 9 es la **base de datos única compartida**:

```
SISTEMA ACTUAL (TPS - Problemático)          SISTEMA ERP (Objetivo)
─────────────────────────────────           ────────────────────────────────
ventas.txt      ← datos aislados            ┌─────────────────────────────┐
productos.txt   ← sin relación real         │    BASE DE DATOS ÚNICA H2    │
clientes.txt    ← duplicación posible       │                             │
usuarios.txt    ← sin auditoría             │  VENTAS ←→ PRODUCTOS        │
                                            │     ↕           ↕           │
Sin trazabilidad                            │  CLIENTES ←→ KARDEX         │
Sin atomicidad                              │     ↕           ↕           │
Sin concurrencia real                       │  COMPRAS ←→ PROVEEDORES     │
                                            │     ↕           ↕           │
                                            │  BITACORA ←→ USUARIOS       │
                                            └─────────────────────────────┘

Beneficios: Dato único · Trazabilidad · Atomicidad · Concurrencia
```

---

## 3. Estructura de Módulos ERP para LAREDO (Sesión 9)

Basado en la **Estructura por Bloques** de la Sesión 9:

### 3.1 Finanzas / Contabilidad
```
Funciones requeridas:
├── Flujo de caja diario (entradas vs salidas)
├── Cuentas por cobrar (ventas a crédito)
├── Cuentas por pagar (deudas con proveedores)
├── Libro diario de transacciones
├── Conciliación de métodos de pago
└── Reportes financieros por período (FR-019, FR-025..FR-029)
```

### 3.2 Logística / Inventarios
```
Funciones requeridas:
├── Consulta de stock en tiempo real (FR-003)
├── Kardex de movimientos entrada/salida/merma (FR-004, FR-044)
├── Alertas de stock mínimo configurable (FR-002, FR-043)
├── Alertas de fechas de vencimiento (FR-007)
├── Productos sin movimiento (FR-005)
└── Actualización automática al comprar (FR-009)
```

### 3.3 Logística / Compras
```
Funciones requeridas:
├── Registro de proveedores con validación RUC (FR-011)
├── Edición y baja de proveedores (FR-012)
├── Listado y búsqueda de proveedores (FR-013)
├── Registro de compras con detalle de productos (FR-008)
└── Historial de compras por proveedor y fecha (FR-010)
```

### 3.4 Ventas / Comercial
```
Funciones requeridas:
├── POS con búsqueda de productos (FR-022)
├── Múltiples métodos de pago (FR-015, FR-046)
├── Descuentos manuales y por tipo (FR-023, FR-045)
├── Registro de devoluciones (FR-014, FR-047)
├── Historial de compras por cliente (FR-016)
├── Programa de fidelización con puntos (FR-030, FR-031)
└── Ranking de clientes VIP (FR-032)
```

### 3.5 RR.HH.
```
Funciones requeridas:
├── Ficha del empleado completa (FR-035)
├── Control de asistencia por DNI (FR-024)
├── Gestión de vacaciones (FR-036)
├── Evaluación de desempeño (FR-038)
└── Cálculo de planilla mensual (FR-037)
```

---

## 4. Arquitectura del ERP LAREDO (Sesión 9 — 3 Capas)

La Sesión 9 define la arquitectura Cliente-Servidor de tres capas de SAP. LAREDO implementa una variante en escritorio:

```
┌──────────────────────────────────────────────────────────────┐
│                   CAPA DE PRESENTACIÓN                       │
│  Java Swing + FlatLaf + UIKit propio                        │
│  IFrm*, FrmDashboard, FrmLogin                              │
│  Responsabilidad: UI/UX, validación visual, navegación      │
├──────────────────────────────────────────────────────────────┤
│                   CAPA DE NEGOCIO                            │
│  Controladores Java (patrón MVC)                            │
│  VentaController, ProductoController, etc.                  │
│  Responsabilidad: reglas de negocio, validación, cálculos   │
├──────────────────────────────────────────────────────────────┤
│                   CAPA DE DATOS                              │
│  Fase 1: archivos .txt (actual)                             │
│  Fase 2: H2 Database (objetivo) con JDBC                    │
│  DAOs: ProductoDAO, VentaDAO, ProveedorDAO, etc.            │
│  Responsabilidad: persistencia, consultas, transacciones    │
└──────────────────────────────────────────────────────────────┘
```

**Beneficio de esta arquitectura** (Sesión 9): facilita el mantenimiento — si mañana cambiamos H2 por MySQL, solo cambia la capa de datos, no la lógica ni la UI.

---

## 5. Optimización del ERP LAREDO (Sesión 10)

La Sesión 10 define 6 fases de optimización post-implementación. Aplicadas al proyecto:

### Fase 1 — Evaluación (ya realizada: auditoría)
- ✅ Auditoría de código realizada: identificados 20 IFrm con `// TODO`
- ✅ KPIs medidos: 88% UI, 12% lógica, 30% persistencia
- ✅ Defectos documentados: 6 bugs heredados del TPS

### Fase 2 — Capacitación y Gestión del Cambio
- El equipo debe dominar FlatLaf, JFreeChart y JDBC antes de IT-02
- Crear documentación de "super-usuarios" por módulo
- Reforzar conocimiento de patrón MVC antes de implementar controladores

### Fase 3 — Personalización Estratégica
```java
// Parametrización recomendada (no hardcodear):
// MALO (actual):
if (descuento > 20) { ... }

// BUENO (parametrizable):
double LIMITE_DESCUENTO_CAJERO = ConfiguracionSistema.get("descuento.max.cajero");
if (descuento > LIMITE_DESCUENTO_CAJERO) { ... }
```
- Tabla `CONFIGURACION` en BD con parámetros del sistema
- Stock mínimo editable por producto (no hardcodeado)
- Colores de alertas configurables
- Porcentaje de puntos por venta configurable

### Fase 4 — Mantenimiento y Actualizaciones
- Migrar de `.txt` a H2 (ver `08_MODELO_DATOS_BD.md`)
- Versionado con Git: una branch por módulo ERP
- Plan de respaldo diario de la BD

### Fase 5 — Optimización de Procesos
- Automatizar el Kardex al registrar ventas y compras
- Generar bitácora automáticamente en cada operación CRUD
- Acumular puntos de fidelización automáticamente al procesar venta

### Fase 6 — Soporte Continuo
- Definir SLA académico: el docente revisa cada iteración en la presentación
- Documentar lecciones aprendidas (conecta con KM, Sesión 11)

---

## 6. Integración TPS → KM en LAREDO (Sesión 11)

La Sesión 11 explica que el TPS genera datos valiosos que la Gestión del Conocimiento (KM) convierte en decisiones estratégicas. Para LAREDO:

```
DATOS (TPS)              INFORMACIÓN              CONOCIMIENTO (KM)
──────────────────────────────────────────────────────────────────
ventas.txt            → Reporte de ventas     → "Los lunes vendo
"2024-01-15,P001,3"      del período             30% menos → pedir
                                                 menos stock lunes"

productos.txt         → Alerta stock bajo     → "Producto X se
"P001,...,stock=2"       en Dashboard           agota cada viernes
                                                 → pedir jueves"

clientes.txt          → Ranking VIP clientes  → "Cliente DNI 42XX
"DNI,compras=45"         por volumen            es top 5 → ofrecerle
                                                 descuento fidelidad"
```

**Herramientas del puente TPS → KM** (Sesión 11):
- **Dashboard KPI**: convierte ventas.txt en información visual en tiempo real
- **Módulo de Reportes**: genera conocimiento por período exportable
- **Bitácora de Auditoría**: base para análisis de patrones de uso
- **Base de Conocimiento** (futuro): documentar decisiones basadas en datos del sistema

---

## 7. Alineación con Bibliografía del Curso

| Concepto aplicado | Fuente |
|-------------------|--------|
| Módulos ERP (Finanzas, Logística, RRHH, Ventas) | Laudon & Laudon (2018), Cap. ERP |
| Arquitectura 3 capas | Sesión 9 — Elías Santa Cruz |
| Parametrización vs Personalización | Sesión 10 — Elías Santa Cruz |
| Datos → Información → Conocimiento | Sesión 11 — Nonaka & Takeuchi (1995) |
| Optimización post-implementación | Sesión 10 — Chopra & Meindl (2019) |
| Base de datos única compartida | Turban, Pollard & Wood (2018) |

---

*Referencia: Sesiones 9, 10 y 11 — Ms. Ing. Elías Santa Cruz · UPAO 2026*

# MANUAL DE ESTÁNDARES DE INGENIERÍA Y DIRECTRICES PARA ASISTENTES DE IA
## Proyecto: ERP LAREDO — Sistema de Gestión para Minimarket
**Versión 2.0 · Última actualización: 27 de junio de 2026**
**Stack real del proyecto: Java + Swing (NetBeans / Ant) · Persistencia en migración de `.txt` (TPS legacy) → H2 Database embebida**

---

## 📑 Índice

0. [Qué cambió en esta versión (v2.0)](#0-qué-cambió-en-esta-versión-v20)
1. [Propósito y alcance de este documento](#1-propósito-y-alcance-de-este-documento)
2. [Cómo usar las fuentes de este repositorio](#2-cómo-usar-las-fuentes-de-este-repositorio)
3. [Stack tecnológico y estado actual del proyecto](#3-stack-tecnológico-y-estado-actual-del-proyecto)
4. [Arquitectura por capas de ERP LAREDO](#4-arquitectura-por-capas-de-erp-laredo)
5. [Fundamentos bibliográficos aplicados a LAREDO](#5-fundamentos-bibliográficos-aplicados-a-laredo)
6. [Patrones de diseño obligatorios y dónde usarlos](#6-patrones-de-diseño-obligatorios-y-dónde-usarlos-en-laredo)
7. [REGLA OBLIGATORIA — Comentarios en todo el código generado](#7-regla-obligatoria--comentarios-en-todo-el-código-generado)
8. [Seguridad, RNF y cumplimiento](#8-seguridad-rnf-y-cumplimiento)
9. [UI/UX obligatorio](#9-uiux-obligatorio)
10. [Validaciones obligatorias](#10-validaciones-obligatorias)
11. [Persistencia y modelo de datos](#11-persistencia-y-modelo-de-datos)
12. [Test-Driven Prompting adaptado a LAREDO](#12-test-driven-prompting-adaptado-a-laredo)
13. [Trazabilidad HU ↔ Código (XP)](#13-trazabilidad-hu--código-xp)
14. [Gestión del conocimiento aplicada al código (KM)](#14-gestión-del-conocimiento-aplicada-al-código-km)
15. [Flujo de trabajo obligatorio de la IA ante cualquier tarea](#15-flujo-de-trabajo-obligatorio-de-la-ia-ante-cualquier-tarea)
16. [Checklist final de autocrítica antes de entregar código](#16-checklist-final-de-autocrítica-antes-de-entregar-código)
17. [Mantenimiento de este documento](#17-mantenimiento-de-este-documento)
18. [Referencias bibliográficas completas](#18-referencias-bibliográficas-completas)

---

## 0. Qué cambió en esta versión (v2.0)

La v1.0 de este manual daba directrices genéricas para "un ERP de supermercados". Esta versión las reemplaza por directrices **atadas al proyecto real**: ERP LAREDO, un sistema de escritorio en Java Swing para un minimarket, hoy en migración de archivos `.txt` heredados del TPS hacia una base de datos H2.

Cambios principales:

- Se eliminan los ejemplos genéricos ("supermercado", "MermaStock" abstracto) y se reemplazan por las **clases, tablas, HU y RNF reales** definidos en `03`–`10`.
- Se agrega una **arquitectura por capas concreta** (sección 4), mapeada a las carpetas que el proyecto ya usa (`Vista/`, `Servicio/`, `DAO/`, `Modelo/`, `Clases/`).
- Se agrega la **sección 7**, con la regla explícita y obligatoria de comentar todo el código generado — incluyendo ejemplos correctos e incorrectos con clases reales del proyecto.
- Se agrega la **sección 6**, una tabla cerrada de patrones GoF con la funcionalidad exacta de LAREDO donde corresponde aplicarlos (nada de "usar patrones porque sí").
- Se consolidan en un solo lugar los RNF-01..11, los Spikes MT-001..004 del Addendum, y las reglas de UI/UX y validaciones, todas con referencia cruzada a su documento fuente.
- Se añade la sección 2, que establece la jerarquía de fuentes: los **libros dan la técnica**, los **documentos `03`–`10` dan el dominio**. Ante cualquier conflicto, manda el documento del proyecto.

---

## 1. Propósito y alcance de este documento

Este archivo es una directriz operativa obligatoria para cualquier sistema de Inteligencia Artificial (LLM, agente autónomo, copiloto o asistente de código — incluyendo herramientas tipo Claude Code, Cursor o GitHub Copilot) que interactúe, genere, refactorice o audite código en este repositorio.

Cualquier IA que trabaje en este proyecto debe subordinar sus decisiones de diseño, generación de código y estructuración de software a los principios descritos aquí, aplicados **específicamente al dominio de ERP LAREDO**: un minimarket de una sola sede, con caja única o pocas cajas, baja-a-media concurrencia (objetivo: 5 usuarios simultáneos — RNF-09), consistencia crítica de inventario y dinero, y un ciclo de vida académico con iteraciones quincenales bajo XP (ver `04_PLAN_RELEASE_XP.md`).

Al procesar cualquier solicitud sobre este repositorio, la IA debe actuar bajo el rol de un **Ingeniero de Software Principal**: pragmático, riguroso, que prioriza la salud a largo plazo del sistema sobre el parche rápido, y que **nunca inventa una regla de negocio** que no esté respaldada por los documentos `03`–`10`.

---

## 2. Cómo usar las fuentes de este repositorio

Este proyecto tiene dos tipos de fuentes, y no deben confundirse:

| Tipo de fuente | Qué aporta | Ejemplos |
|---|---|---|
| **Libros de ingeniería de software** (sección 18) | La **técnica**: cómo nombrar, cómo estructurar, qué patrón usar, cómo escribir un requisito verificable | Clean Code, DDD, Refactoring, GoF, ISO 29148 |
| **Documentos del proyecto `03`–`10`** | El **dominio**: qué hace LAREDO, qué entidades existen, qué validación tiene cada campo, qué tabla SQL existe, qué prioridad tiene cada HU | `05_HU_COMPLETAS_GHERKIN.md`, `08_MODELO_DATOS_BD.md` |

**Regla de oro:** cuando este manual cite un libro (p. ej. *Domain-Driven Design* de Evans), la aplicación de ese principio nunca debe ser genérica ni inventada — debe interpretarse usando las entidades, HU, RNF, validaciones y modelo de datos **reales** ya definidos en `03`–`10`. Los libros son la fuente de las técnicas; los documentos del proyecto son la fuente de verdad del negocio. Si un libro sugiere algo que contradice lo ya definido en estos documentos (p. ej. una entidad que no existe, una regla de redondeo distinta), **gana el documento del proyecto**, y la IA debe señalar la discrepancia en vez de improvisar — exactamente como se hizo en `04b_ADDENDUM_R3_FASE3_IT11_IT13.md` con el análisis crítico del plan de Gemini (Incoherencias I-1 a I-5).

**Mapa rápido — cuándo consultar cada documento antes de programar:**

| Documento | Consúltalo cuando vayas a... |
|---|---|
| `03_DISEÑO_UI_UX_PROFESIONAL.md` | Crear o tocar cualquier pantalla Swing (`IFrm*`, `Frm*`) |
| `05_HU_COMPLETAS_GHERKIN.md` | Implementar o testear una funcionalidad con ID `FR-XXX` o `1000XXX*` |
| `06_RNF_CALIDAD.md` | Tomar decisiones de seguridad, rendimiento, atomicidad o disponibilidad |
| `07_VALIDACIONES_FORMULARIOS.md` | Escribir la validación de cualquier campo de formulario |
| `08_MODELO_DATOS_BD.md` | Tocar persistencia, DAO, SQL, o cualquier entidad de dominio |
| `04_PLAN_RELEASE_XP.md` | Necesites saber prioridad MoSCoW, iteración o el Definition of Done |
| `04b_ADDENDUM_R3_FASE3_IT11_IT13.md` | Trabajar en Reportes Avanzados (IT-11) o Seguridad/Bitácora (IT-13) |
| `09_INTEGRACION_TPS_KM.md` | Trabajar en el Dashboard, reportes BI o el módulo de conocimiento (FR-049/050) |
| `10_OPTIMIZACION_PERSONALIZACION.md` | Tengas dudas de configuración vs. personalización, branching o checklist de iteración |

---

## 3. Stack tecnológico y estado actual del proyecto

```
Lenguaje:        Java (Swing) — build con NetBeans / Ant (build.xml)
Persistencia:    EN MIGRACIÓN — .txt (TPS heredado) → H2 Database embebida
                 URL: jdbc:h2:./data/laredo;AUTO_SERVER=TRUE
                 Selección de implementación vía DAOFactory (ver sección 11)
Reportes:        iTextPDF (PDF) — ÚNICA librería de exportación declarada hoy
Gráficos:        JFreeChart (Dashboard, sección 9_INTEGRACION_TPS_KM)
Contraseñas:     SHA-256 (mínimo aceptable, RNF-03) — BCrypt recomendado a futuro
Librerías:       SOLO las declaradas en /librerias/: h2-2.2.224.jar, iTextPDF, jfreechart-1.5.x.jar
                 Apache POI (.xlsx) NO está declarado aún — ver sección 8, "Sin dependencias fantasma"
```

**Estado de Go-Live (auditoría — `10_OPTIMIZACION_PERSONALIZACION.md`):**

| Módulo | Estado | Lectura para la IA |
|---|---|---|
| TPS (Ventas/Productos/Clientes) | Parcial, 6 defectos documentados | Corregir antes de construir encima — son `1000001*`…`1000015*` |
| Dashboard KPI | Existe como menú, no como dashboard real | Implementar lógica de `DashboardService`, no solo la vista |
| Compras/Proveedores | Módulo nuevo | Construir desde cero siguiendo DAO dual |
| Inventario avanzado | Sin kardex | Implementar `MovimientoInventario` (Kardex) |
| Finanzas / RRHH | 0% implementado | No asumir que existe nada; revisar `08_MODELO_DATOS_BD.md` |
| Seguridad avanzada | Login básico, sin bloqueo ni bitácora | RNF-02 a RNF-06 son prioridad Must |

**Lectura clave para cualquier IA:** la cobertura de **interfaces visuales** ya es alta (88%), pero la **lógica de negocio real** está en 12% y la **persistencia real** en 30%. Esto significa que, en la mayoría de tareas, **ya existe una pantalla Swing** — el trabajo de la IA no es "rehacer la UI", sino conectarla a una capa de `Servicio/` y `DAO/` real, con las validaciones y comentarios que exige este manual.

---

## 4. Arquitectura por capas de ERP LAREDO

LAREDO es una aplicación de escritorio, no un sistema web. La "Arquitectura Limpia" se aplica de forma **pragmática** (Head First Design Patterns: evitar sobre-ingeniería), mapeada a las carpetas que el proyecto ya usa o debe usar:

```
Vista/        → Formularios Swing (IFrm*, Frm*). SOLO construcción de UI y
                delegación de eventos a Servicio/Controlador. CERO lógica de
                negocio, CERO SQL, CERO lectura de archivos aquí.

Controlador/  → Mediadores Vista ↔ Servicio para flujos con estado propio
                (ej. LoginController con intentos fallidos y bloqueo, RNF-04).
                Opcional: en formularios simples, Vista puede llamar directo
                a Servicio sin Controlador intermedio.

Servicio/     → Lógica de negocio y casos de uso (DashboardService,
                BitacoraService, VentaService, ReporteProductosDAO-lógica...).
                AQUÍ viven las reglas: cálculo de descuentos (FR-023/045),
                puntos de fidelización (FR-030/031), atomicidad de venta
                (RNF-10), modo degradado (RNF-07).

Modelo/       → Entidades de dominio puras: Producto, Venta, Cliente,
                Proveedor, Compra, MovimientoInventario, Empleado, Bitacora...
                (ver 08_MODELO_DATOS_BD.md §5). CERO SQL, CERO Swing aquí.

DAO/          → Interfaces I*DAO + implementaciones *DAOtxt / *DAOh2 +
                DAOFactory (ver 08_MODELO_DATOS_BD.md §6).

Reportes/     → Generadores de PDF (iTextPDF) y, cuando se declare Apache
                POI, de Excel.

Clases/       → Utilidades transversales: UITokens, UIFonts, UISpacing,
                UIButton, UITextField, UITable, ToastNotification,
                ValidatorUtil, HashUtil, LoggerGlobal, ConfiguracionSistema,
                ConexionDB, SessionManager.

data/         → Archivos .txt heredados + BD H2 (laredo.mv.db) + backups/
logs/         → Salida de LoggerGlobal (Spike MT-003)
librerias/    → JARs declarados — única fuente de verdad de dependencias permitidas
```

**Regla de dependencia (obligatoria):**

```
Vista  →  Controlador/Servicio  →  DAO  →  Modelo
```

Nunca al revés. `Modelo/` no conoce a nadie (ni a Swing ni a SQL). `DAO/` no conoce a `Vista/`. `Vista/` nunca abre una conexión JDBC ni lee un archivo `.txt` directamente — siempre pasa por `Servicio/` o `DAO/`. Esta es la aplicación pragmática de *Clean Architecture* (Martin) y del Patrón DAO ya definido en `08_MODELO_DATOS_BD.md §6`: la base de datos (`.txt` o H2) es un detalle externo intercambiable, jamás el centro del diseño.

---

## 5. Fundamentos bibliográficos aplicados a LAREDO

### A. Fundamentos, Mentalidad y Código Limpio

| Libro | Autores | Aplicación concreta en ERP LAREDO |
|---|---|---|
| **The Pragmatic Programmer** | Hunt, Thomas | Ortogonalidad/DRY: la lógica de descuentos (`FR-023`, `FR-045`) y de puntos de fidelización (`FR-030`, `FR-031`) vive en `Servicio/`, separada de `DAO/` e independiente del Kardex (`FR-004`). Un cambio en `ConfiguracionSistema` (`puntos.por.sol`) nunca debe obligar a tocar `ProductoDAOh2`. |
| **Clean Code** | Robert C. Martin | Nombres de dominio ya establecidos en el proyecto: `MovimientoInventario` (no `Mov`), `stockMinimo`, `puntosAcumulados`, `Bitacora` (no `Log`). Funciones ≤20 líneas, una sola responsabilidad: `DashboardService.getVentasHoy()` no debe también escribir en la bitácora — eso es responsabilidad de otra llamada explícita. |
| **The Software Craftsman** | Sandro Mancuso | Ante un pedido de "parche rápido" (ej. *"solo agrega el campo sin validar"*), la IA debe proponer la versión completa con validación (`07_VALIDACIONES_FORMULARIOS.md`) y advertir el riesgo de omitirla, citando el RNF aplicable. |

### B. Arquitectura y Modelado de Negocio Complejo

| Libro | Autores | Aplicación concreta en ERP LAREDO |
|---|---|---|
| **Domain-Driven Design** | Eric Evans | Bounded Contexts ya definidos en LAREDO: *POS/Ventas*, *Inventario/Kardex*, *Compras/Proveedores*, *Finanzas*, *RRHH*, *Seguridad/Bitácora*, *Reportes/KM*. Un `Producto` en POS solo necesita id, precio y código de barras; en Inventario necesita `stock`, `stockMinimo`, `fechaVencimiento` (`08_MODELO_DATOS_BD.md §3.3`). No mezclar responsabilidades entre contextos. |
| **Clean Architecture** | Robert C. Martin | La regla de dependencia de la sección 4. H2 o `.txt` son detalles de `DAO/`; la lógica de puntos o descuentos no debe saber si está leyendo de un archivo o de una tabla. |
| **Designing Data-Intensive Applications** | Martin Kleppmann | RNF-10 (atomicidad) es la aplicación directa de este libro: ninguna venta puede reducir stock sin guardar la venta (ver patrón de rollback manual en `06_RNF_CALIDAD.md §RNF-10`). RNF-09 exige `synchronized`/`FileLock` mientras se usa `.txt`, y transacciones JDBC (`commit`/`rollback`) en H2. |

### C. Patrones de Diseño y Evolución del Código

| Libro | Autores | Aplicación concreta en ERP LAREDO |
|---|---|---|
| **Design Patterns (GoF)** | Gamma, Helm, Johnson, Vlissides | Ver tabla cerrada de la sección 6, con la funcionalidad exacta de LAREDO donde corresponde cada patrón. |
| **Head First Design Patterns** | Freeman, Robson | Preferir composición sobre herencia — ejemplo real: `UIButton.primary()/secondary()/danger()/icon()` en vez de 4 subclases de `JButton` (`03_DISEÑO_UI_UX_PROFESIONAL.md §5.1`). |
| **Refactoring** | Martin Fowler | Cada vez que se toque `Producto.java` o `GestionarProductos.java` (clases heredadas del TPS con defectos conocidos: `1000001*`…`1000015*`), identificar code smells (listas largas de parámetros, clase Dios) **antes** de añadir funcionalidad nueva encima. |

### D. Sistemas Distribuidos y Operaciones Modernas

| Libro | Autores | Aplicación concreta en ERP LAREDO |
|---|---|---|
| **Building Microservices** | Sam Newman | No aplica todavía — LAREDO es un monolito de escritorio mono-sesión (ver `09_INTEGRACION_TPS_KM.md §3`, fila "Intranet: no aplica"). El desacoplamiento DAO/Servicio ya deja la puerta abierta si en el futuro se separa en servicios. |
| **System Design Interview** | Alex Xu | RNF-09 (5 usuarios concurrentes) es la versión a escala de LAREDO de "diseñar sin estado": los DAO no deben mantener estado entre llamadas. |
| **Site Reliability Engineering** | Murphy et al. | El Spike `MT-003` (`LoggerGlobal`) y el RNF-07 (modo degradado ante caída de RENIEC/SUNAT/Mercado Pago) son la aplicación directa de *Circuit Breaker* + observabilidad a la escala de un minimarket. |

---

## 6. Patrones de diseño obligatorios y dónde usarlos en LAREDO

Esta tabla es **cerrada**: la IA no debe introducir un patrón GoF que no esté aquí sin justificar por qué el caso de uso lo requiere. Aplicar un patrón sin un caso real detrás es sobre-ingeniería (Head First Design Patterns).

| Patrón | Dónde se usa en LAREDO | HU / clase relacionada |
|---|---|---|
| **Strategy** | Métodos de pago (Efectivo/Tarjeta/Digital/Mixto) y tipo de descuento (% o S/.) | `FR-015`, `FR-046`, `FR-045` → `EstrategiaPago`, `EstrategiaDescuento` |
| **Factory Method** | Selección de implementación DAO (`.txt` vs. H2) y generación de comprobantes fiscales | `DAOFactory` (`08_MODELO_DATOS_BD.md §6`); `ComprobanteFactory` para Boleta/Factura/Nota de Crédito (`FR-034`) |
| **Singleton** | Conexión única a BD, sesión activa, configuración cacheada | `ConexionDB`, `SessionManager`, `ConfiguracionSistema` (ya definidos en `08` y `10`) |
| **Template Method** | Estructura común de todos los reportes PDF (encabezado + filtros + tabla + pie + firma) | `FR-006`, `FR-017`, `FR-018`, `FR-019-v2`, `FR-041` → clase abstracta `ReportePDFBase` |
| **Observer** | Refresco de KPIs del Dashboard tras cada venta/compra y alertas de stock crítico/vencimiento, sin que `VentaService` conozca a `FrmDashboard` | `FR-001`, `FR-002`, `FR-007` |
| **Builder** | Construcción de objetos compuestos con muchos campos opcionales: `Venta` + lista de `DetalleVenta`; `Compra` + lista de `DetalleCompra` | `FR-008`, evita constructores con 8+ parámetros |
| **Repository / DAO** | Toda persistencia del sistema, sin excepción | Interfaces `I*DAO` (`08_MODELO_DATOS_BD.md §6`) |
| **State** | Estados con transición controlada: `Venta.estado` (COMPLETADA\|ANULADA), `CuentasCobrar/Pagar.estado` (PENDIENTE\|PAGADO\|VENCIDO), bloqueo de cuenta | `RNF-04`, modelo de `08_MODELO_DATOS_BD.md §3.5/3.12` |
| **Decorator** | Apilado de hasta 3 `ToastNotification` simultáneos sin crear una subclase por tipo de aviso | `03_DISEÑO_UI_UX_PROFESIONAL.md §5.4` |
| **Chain of Responsibility** | `FormValidator` ya encadena validaciones (`.obligatorio().validar()...`) — extender ese mismo patrón, no inventar otro paralelo | `07_VALIDACIONES_FORMULARIOS.md §12` |

---

## 7. REGLA OBLIGATORIA — Comentarios en todo el código generado

Esta regla es **innegociable** y aplica a cualquier línea de código que la IA escriba, modifique o refactorice en este repositorio, sea Java, SQL embebido, scripts de migración o archivos de configuración.

### 7.1 Qué debe llevar comentario

1. **Toda clase** generada o modificada inicia con un bloque Javadoc que explica:
   - Qué representa la clase en el dominio de LAREDO (no la implementación, el concepto de negocio).
   - A qué capa pertenece (`Vista` / `Controlador` / `Servicio` / `DAO` / `Modelo`).
   - Qué HU(s) o RNF implementa (trazabilidad — ver sección 13).
2. **Todo método público** lleva Javadoc con `@param`, `@return` (si aplica) y una frase que explique el **porqué de negocio**, no solo el qué técnico.
3. **Todo bloque de lógica no trivial** (validaciones encadenadas, cálculo de descuentos o puntos, transacciones atómicas, condicionales de negocio, queries SQL) lleva un comentario en línea que explique la **regla de negocio** que se está aplicando, citando la HU/RNF cuando exista.
4. **Todo `CREATE TABLE`, `ALTER TABLE` o `INSERT` de configuración relevante** lleva un comentario `--` explicando su propósito (mismo estándar ya usado en `08_MODELO_DATOS_BD.md §3.12`, tabla `CONFIGURACION_SISTEMA`).

### 7.2 Reglas de estilo del comentario

- Se escriben **en español**, usando el lenguaje ubicuo ya definido en el proyecto (`stockMinimo`, `montoReembolso`, `idProducto`, `LoteInventario`, `MermaStock`). Prohibido comentar en inglés o con jerga genérica (`data`, `info`, `stuff`, `helper`).
- Prohibido el comentario vacío o redundante que solo repite el nombre del método (`// guarda el producto` sobre `guardarProducto()`). El comentario debe aportar el **por qué**, no repetir el **qué** obvio.
- Nunca eliminar un comentario de trazabilidad existente (p. ej. `// FR-001`) sin reemplazarlo por uno igual o más específico.
- Si una clase o método ya existente **no tiene** estos comentarios, la IA debe agregarlos antes de considerar la tarea terminada — incluso si el código ya funciona y no se pidió explícitamente "agrega comentarios".

### 7.3 Ejemplo — Incorrecto vs. correcto

```java
// ❌ INCORRECTO — comentario vacío, sin trazabilidad, sin explicar la regla de negocio
public double getVentasHoy() {
    // query
    String sql = "SELECT COALESCE(SUM(total),0) FROM VENTAS WHERE fecha=CURRENT_DATE";
    ...
}
```

```java
// ✅ CORRECTO
/**
 * Calcula el total de ventas con estado COMPLETADA registradas en la fecha actual.
 * Capa: Servicio (DashboardService) — Implementa: FR-001 (KPI "Ventas Hoy").
 *
 * @return total en soles (S/.) de ventas del día; BigDecimal.ZERO si no hay ventas
 */
public BigDecimal getVentasHoy() {
    // Se filtra estado = 'COMPLETADA' porque una venta ANULADA no debe
    // inflar el KPI que ve el Gerente en el Dashboard (FR-001, CA-1).
    String sql = "SELECT COALESCE(SUM(total), 0) FROM VENTAS " +
                 "WHERE fecha = CURRENT_DATE AND estado = 'COMPLETADA'";
    ...
}
```

Nótese además el uso de `BigDecimal` en vez de `double` — obligatorio para cualquier campo monetario (ver sección 8, Spike `MT-001`).

---

## 8. Seguridad, RNF y cumplimiento

Tabla de referencia rápida — toda IA debe verificar estos puntos antes de entregar código que toque dinero, usuarios, sesiones o datos sensibles. El detalle completo de cada RNF está en `06_RNF_CALIDAD.md`; el de los Spikes técnicos en `04b_ADDENDUM_R3_FASE3_IT11_IT13.md §2`.

| ID | Regla | Acción obligatoria de la IA |
|---|---|---|
| RNF-01 | Tiempo de respuesta < 2s en CRUD estándar | No introducir lecturas completas de archivo o `SELECT *` innecesarios en operaciones frecuentes |
| RNF-02 | RBAC con 3 roles (Cajero/Gerente/Administrador) | Todo módulo nuevo se registra en `PermisosConfig` y se valida con `SessionManager.puedeAcceder()` antes de mostrarse o ejecutarse |
| RNF-03 | Contraseñas nunca en texto plano | Usar `HashUtil.sha256()` (mínimo) en cualquier flujo que cree o migre contraseñas |
| RNF-04 | Bloqueo de cuenta tras 3 intentos fallidos, 15 min | Reutilizar el patrón ya definido en `LoginController`, no reescribirlo desde cero |
| RNF-05 | Cierre de sesión a los 30 min de inactividad | Usar `InactivityTimer`, resetear en eventos de mouse/teclado |
| RNF-06 | Bitácora inmutable | La tabla `BITACORA` solo admite `INSERT`/`SELECT` a nivel de permisos de BD — nunca generar código con `UPDATE`/`DELETE` sobre esta tabla, ni un botón "Editar" en su UI |
| RNF-07 | Modo degradado ante falla de servicios externos (RENIEC, SUNAT, Mercado Pago) | Nunca bloquear la operación principal; siempre ofrecer alternativa manual y mensaje claro |
| RNF-08 | Exportación PDF y `.xlsx` | PDF con iTextPDF siempre disponible; `.xlsx` **solo** cuando Apache POI esté declarado en `build.xml` (ver "Sin dependencias fantasma" abajo) |
| RNF-09 | 5 usuarios concurrentes | `synchronized`/`FileLock` mientras se use `.txt`; transacciones JDBC nativas en H2 |
| RNF-10 | Atomicidad transaccional | Toda operación que toque ≥2 entidades (venta, compra, canje de puntos, devolución) debe poder revertirse completa si un paso falla |
| RNF-11 | Mensajes de error amigables, en español, sin trazas técnicas | Nunca propagar `NullPointerException`, `SQLException` o rutas de archivo al usuario final |
| MT-001 | Dinero siempre en `BigDecimal` | Prohibido `double`/`float` en cualquier campo o variable monetaria, en Java o en el esquema SQL (`DECIMAL(10,2)`) |
| MT-002 | Hash SHA-256 de contraseñas | Ver RNF-03 |
| MT-003 | `LoggerGlobal` en vez de `System.out`/`printStackTrace()` | Todo `catch` en `DAO/` o `Servicio/` registra contexto (método, id/dni/ruc implicado) vía `LoggerGlobal` |
| MT-004 | Esquema BD con `DECIMAL(10,2)` | Cualquier columna monetaria nueva sigue este tipo, nunca `FLOAT`/`DOUBLE` |

**Sin dependencias fantasma (obligatorio):** la IA nunca debe importar ni asumir una librería que no esté en `/librerias/` o declarada en `build.xml`. Hoy eso significa: H2, iTextPDF y JFreeChart sí; Apache POI **no** — cualquier funcionalidad de exportación `.xlsx` se implementa con un mensaje "Disponible en Release 4 (requiere Apache POI)" hasta que se agregue la dependencia, exactamente como resolvió la Incoherencia I-4 del Addendum.

**Sin PII real en prompts ni en datos de prueba:** usar siempre datos ficticios (DNI/RUC de ejemplo, nombres inventados) al generar mocks o pruebas — nunca datos reales de clientes del minimarket.

**SQL siempre parametrizado:** todo acceso a H2 vía `PreparedStatement`, nunca concatenación de strings con datos del usuario (inyección SQL, OWASP A03:2021).

---

## 9. UI/UX obligatorio

Toda pantalla nueva o modificada debe cumplir `03_DISEÑO_UI_UX_PROFESIONAL.md`. Resumen de lo no negociable:

- **Cero colores hardcodeados** — solo `UITokens`. Cero fuentes sueltas — solo `UIFonts`. Espaciado siempre en múltiplos de 8 (`UISpacing`).
- **Sidebar fijo + `CardLayout`** en el área de contenido — nunca `JInternalFrame`/MDI.
- **`ToastNotification`** para éxito/información/advertencia — `JOptionPane` solo para confirmaciones destructivas o errores críticos bloqueantes.
- **Formularios:** label arriba del campo, error abajo, un solo botón primario por pantalla (a la derecha), campos obligatorios marcados con `*`, validación visual en tiempo real (no solo al guardar).
- **Tablas:** filas alternas, hover, columna de acciones con `UIButton.icon()`, alto de fila ≥40px.
- **RBAC visual:** la misma pantalla debe ocultar/deshabilitar lo que el rol activo no puede usar (no solo bloquearlo a nivel de lógica).

Antes de marcar cualquier `IFrm` como terminado, repasar el checklist de `03_DISEÑO_UI_UX_PROFESIONAL.md §11`.

---

## 10. Validaciones obligatorias

Toda validación de campo sigue `07_VALIDACIONES_FORMULARIOS.md`:

- Reutilizar **siempre** `ValidatorUtil` (`esEnteroPositivo`, `esDNI`, `esRUC`, `esEmail`, `esTelefono`...) antes de escribir una expresión regular nueva — evita la duplicación que advierte la sección de "Mitigación de la Deuda Técnica Inducida por IA" (DRY).
- Encadenar validaciones con el patrón ya definido `FormValidator.obligatorio(...).validar(...)` (ver sección 6, Chain of Responsibility) — no crear un mecanismo paralelo.
- Respetar el evento correcto por campo: `BLUR` para la mayoría, `CHANGE` para búsquedas en tiempo real (ej. POS), `SUBMIT` para validaciones cruzadas (ej. suma de pago mixto = total).
- Los mensajes de error son los ya redactados en `07_VALIDACIONES_FORMULARIOS.md` cuando existen — no inventar una redacción distinta para el mismo caso.

---

## 11. Persistencia y modelo de datos

Toda persistencia sigue `08_MODELO_DATOS_BD.md`:

- **Patrón DAO dual obligatorio:** interfaz `I*DAO` + implementación `*DAOtxt` (legacy) + implementación `*DAOh2`, seleccionadas por `DAOFactory` según `ConfiguracionSistema.get("db.use_h2")`. Nunca acceder a `.txt` o a H2 directamente desde `Servicio/` o `Vista/`.
- **Dinero:** `DECIMAL(10,2)` en SQL, `BigDecimal` en Java — nunca `FLOAT`/`DOUBLE` (`MT-001`, `MT-004`).
- **Integridad referencial:** toda relación nueva entre tablas usa `FOREIGN KEY`, replicando el ERD de `08_MODELO_DATOS_BD.md §4`.
- **Bitácora inmutable a nivel de BD**, no solo de UI: el usuario de aplicación tiene permisos `INSERT`/`SELECT` únicamente sobre `BITACORA` (Incoherencia I-5 del Addendum).
- **No inventar tablas ni columnas:** si una HU requiere un campo que no existe en el esquema actual, se propone como una adenda nueva (igual formato que `04b_ADDENDUM_R3_FASE3_IT11_IT13.md`), nunca se agrega en silencio.
- **Migración por fases (F0–F5):** respetar el orden ya definido en `08_MODELO_DATOS_BD.md §7` — no migrar una tabla de una fase posterior sin que la anterior esté lista, para no romper el TPS mientras sigue operando con `.txt`.

---

## 12. Test-Driven Prompting adaptado a LAREDO

1. Al implementar cualquier HU (`FR-XXX` o `1000XXX*`), la IA **debe generar primero** el archivo de pruebas JUnit, extrayendo los casos **directamente** de los criterios Gherkin (`Given/When/Then`, columnas `CA-1`, `CA-2`...) ya redactados en `05_HU_COMPLETAS_GHERKIN.md`. No se inventan casos distintos a los ya escritos por el equipo; sí se pueden **añadir** casos borde adicionales coherentes con el dominio (stock exactamente en 0, descuento de 100%, RUC con dígitos corruptos, fecha límite de vencimiento) — nunca casos que contradigan un CA existente.
2. Ejemplo: para `FR-011` (registrar proveedor), los tests cubren `CA-1` a `CA-5` tal como están redactados, más cualquier borde adicional justificado (RUC de 10 dígitos, RUC con letras).
3. **Validación sintáctica obligatoria:** si hay acceso a las herramientas de build (Ant/NetBeans) en el entorno, compilar y correr los tests antes de marcar la tarea como hecha — esto corresponde a los puntos 1 y 2 del Definition of Done (`04_PLAN_RELEASE_XP.md §2`).

---

## 13. Trazabilidad HU ↔ Código (XP)

- Toda clase o método nuevo debe poder rastrearse a un identificador: `FR-XXX` (HU nueva), `1000XXX*` (defecto heredado del TPS) o `MT-XXX` (Spike técnico).
- Si la IA implementa algo que no corresponde a ninguna HU existente (p. ej. un refactor de seguridad o de observabilidad), debe **proponerlo como un nuevo Spike** `MT-XXX`, siguiendo el mismo formato que `MT-001`–`MT-004` en `04b_ADDENDUM_R3_FASE3_IT11_IT13.md §2`: rol, quiero, para, tabla de criterios de aceptación con estado. Esto sigue el principio de Beck (XP): *"todo trabajo visible debe poder rastrearse"*.
- Al cerrar cualquier tarea, la IA debe contrastar el resultado contra el Definition of Done de 8 puntos (`04_PLAN_RELEASE_XP.md §2`) y reportar explícitamente en su respuesta qué puntos quedaron cubiertos y cuáles pendientes (p. ej. "falta que el tester firme el caso de prueba" no lo puede marcar la IA por sí misma).

---

## 14. Gestión del conocimiento aplicada al código (KM)

Inspirado en la Sesión 11 (`09_INTEGRACION_TPS_KM.md`): los comentarios de trazabilidad y los bloques Javadoc de la sección 7 **no son burocracia** — son la base de conocimiento del proyecto, equivalente a la fila "Wikis / Base documental" de la tabla de herramientas KM. Un comentario bien escrito hoy es lo que evita que el equipo "pierda" la razón de una regla de negocio en la próxima iteración, con el mismo espíritu que `FR-049` (Observaciones de Temporada).

Si la IA detecta, mientras programa, una decisión de diseño que no está documentada en ningún `.md` del proyecto (p. ej. una regla de redondeo no escrita, un límite no definido para un campo), debe **señalarlo explícitamente** en su respuesta para que el Gerente/docente la registre formalmente — nunca asumirla en silencio ni documentarla solo dentro del código.

---

## 15. Flujo de trabajo obligatorio de la IA ante cualquier tarea

1. Identificar la(s) HU involucradas — buscar el ID en `05_HU_COMPLETAS_GHERKIN.md` y/o `04b_ADDENDUM_R3_FASE3_IT11_IT13.md`.
2. Leer los RNF aplicables en `06_RNF_CALIDAD.md`.
3. Leer las validaciones del formulario en `07_VALIDACIONES_FORMULARIOS.md` (si la tarea toca un campo de entrada).
4. Leer el modelo de datos en `08_MODELO_DATOS_BD.md` (si la tarea toca persistencia).
5. Verificar que cualquier librería que se vaya a usar esté declarada en `/librerias/` (sección 8 — sin dependencias fantasma).
6. Generar primero las pruebas (sección 12).
7. Implementar el código con **todos** los comentarios obligatorios (sección 7).
8. Aplicar el patrón de diseño correspondiente solo si está justificado por la tabla de la sección 6.
9. Verificar `BigDecimal` para dinero, SQL parametrizado, RBAC y bitácora si aplica (sección 8).
10. Verificar el checklist de UI/UX si se tocó una pantalla (sección 9).
11. Hacer la autocrítica final (sección 16) antes de entregar.
12. Reportar en la respuesta qué HU se cubrió y qué puntos del Definition of Done quedaron pendientes (sección 13).

---

## 16. Checklist final de autocrítica antes de entregar código

**Diseño y código limpio:**
- [ ] ¿Las funciones tienen una sola responsabilidad y ≤20 líneas?
- [ ] ¿Los nombres usan el lenguaje ubicuo del proyecto (sin `data`, `info`, `temp`)?
- [ ] ¿Se aplicó algún patrón GoF que no esté justificado en la tabla de la sección 6? (si sí, quitarlo)
- [ ] ¿Se respetó la regla de dependencia `Vista → Servicio → DAO → Modelo`?

**Comentarios (sección 7):**
- [ ] ¿Toda clase/método nuevo tiene Javadoc con trazabilidad a una HU/RNF?
- [ ] ¿Todo bloque de lógica de negocio no trivial tiene su comentario explicando el porqué?
- [ ] ¿Los comentarios están en español y usan el vocabulario del dominio?

**Seguridad y RNF (sección 8):**
- [ ] ¿El dinero usa `BigDecimal`/`DECIMAL(10,2)`, nunca `double`/`float`?
- [ ] ¿El SQL está parametrizado?
- [ ] ¿Se respetó RBAC (`SessionManager.puedeAcceder`)?
- [ ] ¿Las operaciones críticas escriben en bitácora?
- [ ] ¿No se introdujo ninguna librería fuera de `/librerias/`?

**UI/UX y validaciones (secciones 9 y 10):**
- [ ] ¿Cero colores/fuentes hardcodeadas?
- [ ] ¿Se reutilizó `ValidatorUtil`/`FormValidator` en vez de regex nuevas sueltas?
- [ ] ¿El feedback de éxito usa `ToastNotification`, no `JOptionPane`?

**Proceso XP (sección 13, `04_PLAN_RELEASE_XP.md`):**
- [ ] ¿El código compila sin errores ni warnings?
- [ ] ¿Existen pruebas que cubren los criterios Gherkin de la HU?
- [ ] ¿Quedó algún `System.out.println` o `// TODO` sin resolver? (si sí, no está terminado)
- [ ] ¿Se reportó al usuario qué puntos del Definition de Done siguen pendientes de validación humana (tester, demo, aprobación del cliente)?

---

## 17. Mantenimiento de este documento

Este manual se actualiza (nueva versión, no sobrescritura silenciosa) cuando:

- Se inicia un nuevo Release o se cierra una iteración con cambios de prioridad relevantes.
- Se introduce un patrón de diseño no contemplado en la sección 6.
- Se crea un nuevo Addendum (como `04b`) que corrige o ramplía una HU existente.
- Se declara una nueva librería en `/librerias/` (debe reflejarse en la sección 8, "sin dependencias fantasma").
- Cambia el estado de migración `.txt` → H2 (sección 11).

Cada actualización debe añadirse como una nueva sección "Qué cambió" (como la sección 0 de esta versión), conservando el historial — nunca borrando silenciosamente las decisiones anteriores, en línea con el principio de Gestión del Conocimiento de la sección 14.

---

## 18. Referencias bibliográficas completas

**Ingeniería de software, arquitectura y patrones:**

- Hunt, A. & Thomas, D. — *The Pragmatic Programmer*
- Martin, R. C. — *Clean Code: A Handbook of Agile Software Craftsmanship*
- Martin, R. C. — *Clean Architecture*
- Mancuso, S. — *The Software Craftsman*
- Evans, E. — *Domain-Driven Design: Tackling Complexity in the Heart of Software*
- Kleppmann, M. — *Designing Data-Intensive Applications*
- Gamma, E., Helm, R., Johnson, R., Vlissides, J. (GoF) — *Design Patterns: Elements of Reusable Object-Oriented Software*
- Freeman, E. & Robson, E. — *Head First Design Patterns*
- Fowler, M. — *Refactoring: Improving the Design of Existing Code*
- Newman, S. — *Building Microservices*
- Xu, A. — *System Design Interview (Vol. 1 y 2)*
- Murphy, N. R. et al. — *Site Reliability Engineering*

**Ingeniería de requisitos y análisis de sistemas (planificación, HU, RNF):**

- Dick, J., Hull, E. & Jackson, K. (2017) — *Requirements Engineering*
- Koelsch, G. (2016) — *Requirements Writing for Systems Engineering*
- ISO/IEC/IEEE 29148:2018 — *Ingeniería de Requisitos*
- Kendall, K. & Kendall, J. (2011) — *Análisis y Diseño de Sistemas*, 8.ª ed.
- Laplante, P. (2018) — *Requirements Engineering for Software and Systems*
- Sommerville, I. (2016) — *Software Engineering*, 10.ª ed.
- SWEBOK v4 (2024)
- Wiegers, K. & Beatty, J. (2013) — *Requisitos de Software*
- Beck, K. (1999) — *Extreme Programming Explained*
- Stellman, A. & Greene, J. (2021) — gestión de proyectos ágiles
- Nonaka, I. & Takeuchi, H. (1995) — *The Knowledge-Creating Company*
- Laudon, K. & Laudon, J. (2018/2020) — Sistemas de Información Gerencial
- Chopra, S. & Meindl, P. (2019) — *Supply Chain Management*

---

*Este documento es ley en el repositorio. Las reglas de negocio reales de ERP LAREDO viven en los documentos `03`–`10`; este manual define el CÓMO escribir el código que las implementa — con arquitectura, patrones, seguridad y trazabilidad — y exige que cada pieza de ese código quede comentada y documentada para quien continúe el proyecto.*

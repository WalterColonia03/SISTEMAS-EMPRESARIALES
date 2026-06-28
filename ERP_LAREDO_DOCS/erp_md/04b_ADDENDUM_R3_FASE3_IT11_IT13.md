# 📋 ADDENDUM AL PLAN DE RELEASE XP — R3 Fase 3
## Reportes Avanzados e Inteligencia de Negocio · Bitácora de Auditoría
**Documento:** Adenda IT-11 / IT-13 · Generado: 2026-06-26T02:06:00-05:00  
**Referencia:** ISO/IEC/IEEE 29148:2018 · Wiegers & Beatty (2013) · Sommerville (2016) · SWEBOK v4 (2024)

---

> [!IMPORTANT]
> Este documento registra:
> 1. **HU nuevas** derivadas de funcionalidades técnicas implementadas durante la auditoría que no estaban en el backlog original (INSTRUCCIONES_IA §3.C — evitar deuda técnica inducida por IA).
> 2. **Análisis crítico** del plan de Gemini versus las INSTRUCCIONES_IA_PROYECTO_ERP.
> 3. **Plan de implementación** de IT-11 (Reportes Avanzados) e IT-13 (Seguridad).

---

## SECCIÓN 1 — Análisis Crítico del Plan de Gemini

### 1.1 Coherencias encontradas ✅

| Aspecto | Evaluación |
|---|---|
| Priorización MoSCoW de FR-006, FR-017, FR-018, FR-019 como **Must** en IT-11 | ✅ Coherente — son condición de aceptación del entregable académico |
| Separación de CRM (IT-09/10) vs Reportes (IT-11) | ✅ Correcto — siguiendo Bounded Contexts de Evans (DDD) |
| FR-020 (Bitácora) como Must en IT-13 | ✅ Alineado con RNF-06 y OWASP A09:2021 |
| Propuesta de "Ranking de productos más vendidos" | ✅ Mapeado a FR-006 existente en el backlog |

### 1.2 Incoherencias y mejoras identificadas ⚠️

| # | Incoherencia/Mejora | Fuente bibliográfica | Acción |
|---|---|---|---|
| **I-1** | El plan de Gemini propone "ganancias restando costos" pero en el dominio actual **no existe la entidad `CostoPorCompra`** cruzada con `DetalleVenta`. Asumir `precioCompra` sin relación explícita es una alucinación funcional. | Wiegers & Beatty (2013) Cap. 8 — "Los requisitos deben ser verificables y trazables a datos existentes" | Se documenta como **FR-019-v2** con fuente de datos explícita: `tb_detalle_compra.precioUnitario` cruzado con `tb_detalle_venta` por `idProducto` |
| **I-2** | La Fase 3 de Gemini no especifica **quién puede ver cada reporte** (control de acceso por rol). Sommerville (2016, Cap. 4) exige que las HU incluyan el actor con su restricción. | Sommerville (2016) §4.5 — "User stories should identify the user role" | HU actualizadas para incluir restricciones de rol (Gerente vs Administrador) |
| **I-3** | El plan no contemplaba las **HU técnicas de refactorización** implementadas durante la auditoría (PasswordUtils, LoggerGlobal, BigDecimal). XP exige que toda pieza de trabajo sea rastreable a una HU o Spike. | Beck (1999) XP — "Everything visible must be tracked" | Se agregan como **Spikes de Madurez Técnica** (MT-001 a MT-004) |
| **I-4** | FR-017 propone "exportar .xlsx" pero el proyecto usa **iTextPDF** como única dependencia de exportación en `librerias/`. No hay Apache POI declarado. | INSTRUCCIONES_IA §3.C — "Prohibición de Dependencias Fantasma" | Exportación limitada a PDF. El .xlsx se mueve a **Could** en R4 con nota de añadir Apache POI a build.xml |
| **I-5** | La Bitácora (FR-020) debe ser **inmutable desde BD** (no solo desde UI como decía el plan). Un admin con acceso directo a MySQL podría manipularla. | ISO/IEC/IEEE 29148:2018 §5.2.8 — "Security requirements shall specify resistance to unauthorized modification" | Se agrega criterio CA-5 en FR-020: tabla `tb_bitacora` con `INSERT` only — sin `UPDATE/DELETE` grants |

---

## SECCIÓN 2 — Spikes de Madurez Técnica (nuevas HU técnicas)

> Según Wiegers & Beatty (2013, p. 144): *"Technical user stories represent work that delivers business value indirectly by improving the system's quality attributes."*  
> Según SWEBOK v4 (2024) §3.2: *"Quality requirements are as important as functional requirements for system dependability."*

### `MT-001` — Migración BigDecimal (Seguridad Financiera)
- **Como** Sistema ERP LAREDO
- **Quiero** que todos los cálculos monetarios usen `BigDecimal` en vez de `double`
- **Para** garantizar exactitud contable y evitar multas por descuadres (INSTRUCCIONES_IA §4.1)

| # | Criterio de Aceptación | Estado |
|---|---|---|
| CA-1 | Ninguna clase en `Clases/` declara campo monetario como `double` o `float` | ✅ HECHO |
| CA-2 | Ningún DAO usa `setDouble()`/`getDouble()` para columnas monetarias | ✅ HECHO |
| CA-3 | El schema SQL usa `DECIMAL(10,2)` en todas las columnas monetarias | ✅ HECHO |
| CA-4 | Las vistas acumulan totales con `BigDecimal.add()`, no con `+=` sobre double | ✅ HECHO |

> **SP:** 8 · **IT:** Transversal (Auditoría) · **Clase afectada:** 15+ clases en Clases/, Modelo/, Vista/

---

### `MT-002` — Hashing de Contraseñas SHA-256 (OWASP A02:2021)
- **Como** Sistema ERP LAREDO
- **Quiero** que ninguna contraseña de usuario se almacene en texto plano
- **Para** cumplir con OWASP A02:2021 — Cryptographic Failures

| # | Criterio de Aceptación | Estado |
|---|---|---|
| CA-1 | `PasswordUtils.hashPassword(pwd)` retorna hash SHA-256 de 64 caracteres hex | ✅ HECHO |
| CA-2 | `FrmLogin` usa `PasswordUtils.verifyPassword()` en vez de comparación directa | ✅ HECHO |
| CA-3 | `GestionarUsuarios` llama `hashPassword()` antes de `u.setPassword()` | ✅ HECHO |
| CA-4 | `MigracionDB.migrarUsuarios()` detecta contraseñas en texto plano (< 64 chars) y las hashea | ✅ HECHO |

> **SP:** 5 · **IT:** Transversal (Auditoría) · **Clase afectada:** `PasswordUtils`, `FrmLogin`, `GestionarUsuarios`, `MigracionDB`

---

### `MT-003` — Observabilidad con LoggerGlobal (SRE)
- **Como** Administrador técnico del sistema
- **Quiero** que todos los errores del sistema queden registrados con contexto en un log estructurado
- **Para** diagnosticar fallos en producción sin depender de `System.out` o `printStackTrace()`

| # | Criterio de Aceptación | Estado |
|---|---|---|
| CA-1 | Ningún bloque `catch` en Modelo/ usa `e.printStackTrace()` | ✅ HECHO |
| CA-2 | Cada mensaje de log incluye el nombre del método y el dato que causó el fallo (ej. id, ruc, dni) | ✅ HECHO |
| CA-3 | El log se escribe en archivo físico en la carpeta `logs/` | ✅ HECHO |
| CA-4 | Las vistas que realizan operaciones de BD usan LoggerGlobal en sus catch | ✅ HECHO |

> **SP:** 3 · **IT:** Transversal · **Clase afectada:** Todos los DAOs y 5 vistas

---

### `MT-004` — Schema BD con DECIMAL(10,2) (Consistencia de Datos)
- **Como** DBA del proyecto ERP LAREDO
- **Quiero** que el esquema MySQL use `DECIMAL(10,2)` para columnas monetarias
- **Para** garantizar que la precisión financiera en Java se refleje también en la BD

| # | Criterio de Aceptación | Estado |
|---|---|---|
| CA-1 | `tb_compra.total`, `tb_detalle_compra.precioUnitario` son `DECIMAL(10,2)` | ✅ HECHO |
| CA-2 | `tb_detalle_venta.precioUnitario`, `tb_flujo_caja.monto` son `DECIMAL(10,2)` | ✅ HECHO |
| CA-3 | `tb_libro_mayor.montoDebe/montoHaber`, `tb_cuentas_cp.montoOriginal/montoPendiente` son `DECIMAL(10,2)` | ✅ HECHO |
| CA-4 | Existe `InitDB5_MigrarDecimal.java` que ejecuta `ALTER TABLE` para BDs existentes | ✅ HECHO |

> **SP:** 2 · **IT:** Transversal · **Script:** `InitDB5_MigrarDecimal.java`

---

## SECCIÓN 3 — HU de Release 3, Fase 3 (a implementar)

> Formato según: Wiegers & Beatty (2013) §6 · ISO/IEC/IEEE 29148:2018 §5.2 · Dick, Hull & Jackson (2017) Cap. 4

### `FR-006` — Ranking de Productos Más Vendidos
- **Como** Gerente del Minimarket LAREDO
- **Quiero** ver un ranking de los 10 productos más vendidos en un período
- **Para** tomar decisiones de reposición, promociones y negociación con proveedores

> *Stakeholder: Gerente General · Fuente: Entrevista a usuario §3 · Trazabilidad: RNF-08*

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | Existen ventas con detalles en el período jun 2026 | El Gerente abre Reportes → Productos Más Vendidos | El sistema muestra tabla ordenada por cantidad_vendida DESC, máximo 10 filas |
| CA-2 | El Gerente filtra por "Mes: Julio" | Presiona Buscar | La tabla se actualiza con datos del mes seleccionado |
| CA-3 | El Gerente presiona "Exportar PDF" | — | Se genera PDF con el ranking, con fecha de generación y período seleccionado |
| CA-4 | No hay ventas en el período | — | Muestra "Sin ventas registradas para el período seleccionado" |
| CA-5 | **[Restricción de rol]** Un Cajero accede al módulo | — | El módulo no aparece en su menú (solo Gerente y Administrador) |

> **SP:** 3 · **MoSCoW:** Must · **IT:** IT-11 · **Fuente de datos:** `tb_detalle_venta JOIN tb_producto GROUP BY idProducto ORDER BY SUM(cantidad) DESC`

---

### `FR-017` — Reporte de Productos con Filtros (Actualizado)
- **Como** Gerente del Minimarket LAREDO
- **Quiero** ver el reporte completo de movimiento de productos con filtros
- **Para** analizar el desempeño de cada producto en el inventario

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | El Gerente abre el módulo de Reportes | Selecciona "Reporte de Productos" | Ve tabla con: Producto, Categoría, Stock Actual, Precio, Total Vendido (unidades) |
| CA-2 | El Gerente filtra por categoría "Lácteos" | Presiona Filtrar | Solo muestra productos de esa categoría |
| CA-3 | El Gerente presiona "Exportar PDF" | — | Se genera PDF usando iTextPDF (librería ya declarada en build.xml) |
| CA-4 | **[Restricción deuda técnica I-4]** El Gerente presiona "Exportar .xlsx" | — | El botón muestra "Disponible en Release 4 (requiere Apache POI)" |

> **SP:** 3 · **MoSCoW:** Must · **IT:** IT-11 · **Nota:** Exportación .xlsx movida a R4 (ver Incoherencia I-4)

---

### `FR-018` — Reporte de Stock Bajo
- **Como** Gerente del Minimarket LAREDO
- **Quiero** ver un reporte de todos los productos con stock por debajo del mínimo
- **Para** generar órdenes de compra antes de que se agoten

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | Existen productos con `cantidad < stockMinimo` | El Gerente abre Reportes → Stock Bajo | Ve tabla: Producto, Stock Actual, Stock Mínimo, Diferencia, Proveedor sugerido |
| CA-2 | El sistema detecta 0 productos con stock bajo | — | Muestra panel verde: "✅ Todos los productos tienen stock suficiente" |
| CA-3 | El Gerente presiona "Exportar PDF" | — | Se genera el reporte en PDF con fecha y firma de generación |
| CA-4 | **[Restricción de rol]** Cajero accede | — | Módulo no visible en su menú |

> **SP:** 2 · **MoSCoW:** Must · **IT:** IT-11 · **Fuente:** `tb_producto WHERE cantidad < stockMinimo` (campo `stockMinimo` ya en tabla)

---

### `FR-019-v2` — Reporte de Ganancias Reales por Período
- **Como** Gerente del Minimarket LAREDO
- **Quiero** ver las ganancias reales (ingresos − costos de compra)
- **Para** evaluar la rentabilidad con datos verificables, no estimados

> *Corrección sobre plan Gemini (Incoherencia I-1): la ganancia se calcula cruzando `tb_detalle_venta.precioUnitario × cantidad` (ingreso) contra `tb_detalle_compra.precioUnitario × cantidad` (costo) para el mismo `idProducto` en el período.*

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | Existen ventas y compras registradas en jun 2026 con el mismo producto | El Gerente selecciona 01/06 – 30/06 | El reporte muestra: Ingreso Bruto, Costo Real, Ganancia Bruta, % Margen |
| CA-2 | Un producto fue vendido pero no tiene compra registrada en el período | — | La columna "Costo Real" muestra "S/ 0.00 (sin compra registrada)" con nota ⚠ |
| CA-3 | El Gerente presiona "Exportar PDF" | — | PDF generado con iTextPDF incluye totales y firma de período |
| CA-4 | No hay datos en el período | — | "Sin movimientos para el período seleccionado" |

> **SP:** 5 · **MoSCoW:** Must · **IT:** IT-11 · **Query:** `JOIN tb_detalle_venta dv JOIN tb_detalle_compra dc ON dv.idProducto = dc.idProducto`

---

### `FR-020-v2` — Bitácora de Auditoría de Seguridad (Actualizada)
- **Como** Gerente del Minimarket LAREDO
- **Quiero** ver un registro inmutable de todas las acciones realizadas por usuarios del sistema
- **Para** cumplir con auditorías de seguridad y detectar comportamientos anómalos

> *Corrección sobre plan Gemini (Incoherencia I-5): la inmutabilidad no puede ser solo a nivel UI. El esquema SQL debe garantizarla mediante permisos de BD o triggers.*

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | El usuario "jperez" (Cajero) registra una venta | Operación ejecutada | Se crea registro en `tb_bitacora`: usuario=jperez, modulo=POS, accion=REGISTRO_VENTA, resultado=EXITO, timestamp=ahora |
| CA-2 | Un intento de login falla | — | Registro en bitácora: usuario=XXX, modulo=LOGIN, accion=LOGIN_INTENTO, resultado=FALLO |
| CA-3 | El Gerente abre la Bitácora | Filtra por usuario "jperez" | Solo muestra registros de ese usuario, ordenados por fecha DESC |
| CA-4 | El Gerente intenta editar un registro desde la UI | — | No existe botón Editar ni Eliminar en la interfaz |
| CA-5 | **[Nuevo — Incoherencia I-5]** El admin ejecuta `DELETE FROM tb_bitacora` en MySQL | — | El script `InitDB_Bitacora.java` crea la tabla con `ENGINE=MyISAM` sin soporte de FK delete-cascade; se documenta en manual de BD que el usuario de app tiene solo `INSERT/SELECT` en esta tabla |
| CA-6 | El Gerente presiona "Exportar Bitácora PDF" | — | Se genera PDF con los registros visibles en pantalla |

> **SP:** 5 · **MoSCoW:** Must · **IT:** IT-13 · **Modelo nuevo:** `Bitacora.java`, `BitacoraDAO.java`

---

---

### `FR-051` — Regulación de Venta con DNI por S/ 700 (Regla SUNAT)
- **Como** Cajero del Minimarket LAREDO
- **Quiero** que el sistema valide si la compra llega o supera los S/ 700.00
- **Para** exigir el DNI del cliente y cumplir con las normativas legales de SUNAT/Indecopi

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | El total de la venta es >= S/ 700 y el cliente es "Consumidor Final" | El Cajero presiona "Realizar Venta" | El sistema bloquea la venta y muestra: "Por ley de SUNAT, compras a partir de S/ 700.00 exigen DNI o RUC" |
| CA-2 | El total de la venta es >= S/ 700 y el cliente tiene un DNI ingresado | El Cajero presiona "Realizar Venta" | El sistema permite continuar con la venta |
| CA-3 | El total de la venta es < S/ 700 y el cliente es "Consumidor Final" | El Cajero presiona "Realizar Venta" | El sistema permite continuar con la venta |

> **SP:** 1 · **MoSCoW:** Must · **IT:** IT-11 · **Regla Legal:** Res. de Superintendencia N° 239-2018/SUNAT

---

### `FR-052` — Monitor de Alertas: Sin Rotación y Próximos a Vencer
- **Como** Encargado de Almacén del Minimarket LAREDO
- **Quiero** ver un monitor que me alerte de productos sin rotación en los últimos 30 días y productos próximos a vencer
- **Para** tomar acciones preventivas (ofertas, devoluciones a proveedor) y no perder dinero

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | Existen productos sin ventas ni compras en los últimos 30 días | El usuario abre Alertas de Inventario -> Sin Rotación | El sistema muestra los productos inactivos y el número de días desde su última venta |
| CA-2 | Existen productos con fechaVencimiento <= 30 días | El usuario abre Alertas de Inventario -> Próximos a Vencer | El sistema lista los productos y pinta en rojo los que vencen en < 10 días |

> **SP:** 3 · **MoSCoW:** Must · **IT:** IT-11

## SECCIÓN 4 — Plan de Implementación IT-11 + IT-13

### Orden de implementación (priorizado por dependencias)

```
1. BitacoraService.java     [Util transversal — debe estar disponible para todos]
2. Bitacora.java            [Modelo de dominio]
3. BitacoraDAO.java         [Persistencia — INSERT only]
4. InitDB_Bitacora.java     [Schema SQL tb_bitacora]
5. ReporteProductosDAO.java [Query ranking, stock bajo, ganancias — lógica de BD]
6. IFrmRptProductosMasVendidos.java  [Vista FR-006, FR-017]
7. IFrmRptStockBajo.java             [Vista FR-018]
8. IFrmRptGanancias.java             [Vista FR-019-v2]
9. IFrmBitacora.java                 [Vista FR-020-v2]
10. Integrar BitacoraService en FrmLogin y IFrmPuntoVenta
```

### Estimación de Story Points restantes IT-11 + IT-13

| HU | SP | Estado |
|---|---|---|
| FR-006 + FR-017 (Ranking + Reporte Productos) | 3+3 | ⬜ Pendiente |
| FR-018 (Stock Bajo) | 2 | ⬜ Pendiente |
| FR-019-v2 (Ganancias Reales) | 5 | ⬜ Pendiente |
| FR-020-v2 (Bitácora) | 5 | ⬜ Pendiente |
| MT-001 a MT-004 (Madurez Técnica) | 18 | ✅ Completado |
| **Total IT-11+IT-13** | **36** | En progreso |

---

*Referencia bibliográfica: Wiegers & Beatty (2013) · ISO/IEC/IEEE 29148:2018 §5.2 · Sommerville (2016) Cap. 4 · SWEBOK v4 (2024) §3 · Dick, Hull & Jackson (2017) Cap. 4 · Laplante (2018) Cap. 7*

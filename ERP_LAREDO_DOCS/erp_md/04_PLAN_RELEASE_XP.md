# 🗓️ PLAN DE RELEASE XP — ERP LAREDO v2.0
**Actualizado con Sesiones 9, 10 y 11 · XP Beck (1999) · Ciclo IV 2026**

---

## 1. Fundamento Metodológico (XP)

> *"El cliente decide qué se construye primero; el programador decide cuánto tiempo llevará."*  
> — Kent Beck, Extreme Programming Explained (1999)

### Roles del equipo

| Rol XP | Responsabilidad | Artefactos |
|--------|----------------|-----------|
| **Cliente** (Product Owner) | Prioriza el backlog, define y aprueba criterios de aceptación, representa al minimarket | User Stories, Acceptance Tests, prioridades MoSCoW |
| **Programador** | Implementa HU, aplica TDD, refactoring continuo (YAGNI + DRY) | Código Java, Pruebas unitarias JUnit, Tarjetas CRC |
| **Tester** | Ejecuta pruebas de aceptación Gherkin, registra defectos | Informe de pruebas, Defect Log, capturas de pantalla |
| **Tracker** | Mide velocidad del equipo, actualiza burndown, alerta desvíos | Burndown chart, Velocity chart |
| **Coach** (Docente) | Guía XP, revisa arquitectura, valida prácticas | Feedback iteración, revisión de código |

---

## 2. Definition of Done (DoD) — Criterio de "HECHO"

Una HU se considera **TERMINADA** solo cuando cumple los 8 puntos:

- [ ] Código compilado sin errores ni warnings en NetBeans
- [ ] Todos los criterios Gherkin Given/When/Then pasan satisfactoriamente
- [ ] Los datos persisten correctamente (`.txt` o BD H2 según la fase)
- [ ] Todos los campos del formulario tienen validaciones completas
- [ ] El formulario cumple el checklist UI/UX de `03_DISEÑO_UI_UX_PROFESIONAL.md`
- [ ] Sin `System.out.println`, `// TODO` ni comentarios de código sin resolver
- [ ] El tester ejecutó el caso de prueba y firmó el resultado
- [ ] El cliente (docente) revisó y aprobó la pantalla en la demo de iteración

---

## 3. Parámetros del Equipo

| Parámetro | Valor |
|-----------|-------|
| Duración de iteración | 2 semanas |
| Velocidad estimada | 8 Story Points por iteración |
| Total de releases | 4 releases |
| Total de iteraciones | 16 iteraciones |
| Total de HU | 48 (FR-001..FR-048 + correcciones TPS) |
| Criterio de prioridad | MoSCoW (Must / Should / Could / Won't) |

---

## 4. Backlog Completo Priorizado — 48 HU

> **Leyenda SP:** Story Points estimados por el equipo  
> **Leyenda MoSCoW:** M=Must · S=Should · C=Could  
> **`*`** = Defecto heredado del TPS (máxima prioridad)

### RELEASE 1 — "Sistema Funcional Base" (IT-01 a IT-04)
*Objetivo: corregir el TPS, construir el Dashboard real, implementar el módulo de Proveedores y Compras*

| ID | Historia de Usuario | Módulo | SP | MoSCoW | IT |
|----|---------------------|--------|----|--------|----|
| `1000001*` | Registrar ventas — corregir validación de cantidad | Ventas TPS | 1 | M | IT-01 |
| `1000015*` | Corregir numeración/IDs duplicados de productos | Productos TPS | 1 | M | IT-01 |
| `1000003*` | Agregar campo ID Producto en reporte de ventas PDF | Reportes TPS | 1 | M | IT-01 |
| `1000005*` | Reemplazar filtro de mes fijo por selector de rango de fechas | Reportes TPS | 2 | M | IT-01 |
| `1000012*` | Mostrar movimientos del cliente en reporte | Clientes TPS | 2 | M | IT-01 |
| `1000014*` | Corregir campo descripción en reporte de productos | Reportes TPS | 1 | M | IT-01 |
| `FR-001` | Dashboard: ingresos del día/semana/mes con KPI cards reales | Dashboard | 5 | M | IT-02 |
| `FR-002` | Dashboard: alertas de stock mínimo con badge y lista de productos | Dashboard | 3 | M | IT-02 |
| `FR-011` | Registrar proveedor con validación de RUC (11 dígitos, sin duplicados) | Compras | 3 | M | IT-03 |
| `FR-012` | Editar y dar de baja proveedores | Compras | 2 | M | IT-03 |
| `FR-013` | Listar y buscar proveedores por nombre y RUC | Compras | 2 | M | IT-03 |
| `FR-008` | Registrar compra a proveedor con detalle de productos | Compras | 5 | M | IT-04 |
| `FR-009` | Actualizar stock automáticamente al registrar compra | Inventario | 3 | M | IT-04 |
| `FR-010` | Historial de compras filtrado por proveedor y rango de fechas | Compras | 3 | M | IT-04 |

**Total R1:** 34 SP en 4 iteraciones (8-9 SP/iter)

---

### RELEASE 2 — "Inventario y Ventas Completos" (IT-05 a IT-08)
*Objetivo: Kardex completo, POS mejorado, devoluciones, múltiples métodos de pago*

| ID | Historia de Usuario | Módulo | SP | MoSCoW | IT |
|----|---------------------|--------|----|--------|----|
| `FR-003` | Consultar stock con filtros por categoría; "Sin stock" cuando = 0 | Inventario | 2 | M | IT-05 |
| `FR-004` | Registrar movimiento de inventario (Kardex: entrada/salida/merma) | Inventario | 5 | M | IT-05 |
| `FR-043` | Configurar stock mínimo por producto desde el formulario de edición | Inventario | 2 | M | IT-05 |
| `FR-007` | Alertas de productos próximos a vencer (≤ 7 días de fechaVencimiento) | Inventario | 3 | S | IT-06 |
| `FR-005` | Reporte de productos sin movimiento en un período dado | Inventario | 3 | S | IT-06 |
| `FR-044` | Registrar mermas de inventario con motivo (Daño/Vencimiento/Robo) | Inventario | 2 | S | IT-06 |
| `FR-014` | Registrar devoluciones: requiere venta previa, actualiza stock | Ventas | 5 | M | IT-07 |
| `FR-015` | Registrar método de pago por venta (Efectivo / Tarjeta / Digital) | Ventas | 3 | M | IT-07 |
| `FR-023` | Aplicar descuento manual con límite por rol del usuario | Ventas | 2 | M | IT-07 |
| `FR-045` | Tipo de descuento: porcentaje (%) o monto fijo (S/.) | Ventas | 2 | S | IT-07 |
| `FR-022` | Búsqueda de productos en POS por código y nombre en tiempo real | Ventas | 2 | M | IT-08 |
| `FR-046` | Pago mixto: efectivo + digital en una misma venta | Ventas | 3 | C | IT-08 |

**Total R2:** 34 SP en 4 iteraciones

---

### RELEASE 3 — "CRM, Reportes Avanzados y Seguridad" (IT-09 a IT-13)
*Objetivo: programa de fidelización, reportes exportables, bitácora y bloqueo de cuentas*

| ID | Historia de Usuario | Módulo | SP | MoSCoW | IT |
|----|---------------------|--------|----|--------|----|
| `FR-016` | Historial de compras por cliente con filtro de fecha | CRM | 3 | M | IT-09 |
| `FR-021` | Productos más comprados por cliente (top 5 por cliente) | CRM | 3 | S | IT-09 |
| `FR-030` | Acumular puntos automáticamente: 1 punto = S/ 1.00 en cada venta | CRM | 3 | S | IT-09 |
| `FR-031` | Canjear puntos por descuento: 100 puntos = S/ 1.00 | CRM | 3 | S | IT-10 |
| `FR-032` | Ranking de clientes VIP por volumen total de compras | CRM | 2 | C | IT-10 |
| `FR-006` | Ranking de productos más vendidos, filtrado por período | Reportes | 3 | M | IT-10 |
| `FR-017` | Reporte de productos más vendidos con filtros y exportación PDF/.xlsx | Reportes | 3 | M | IT-11 |
| `FR-018` | Reporte de productos con stock bajo (actual vs mínimo) | Reportes | 2 | M | IT-11 |
| `FR-019` | Reporte de ganancias por período con cálculo de margen | Reportes | 3 | M | IT-11 |
| `FR-025` | Consultar flujo de caja del día (entradas vs salidas) | Finanzas | 3 | S | IT-12 |
| `FR-026` | Registrar cuentas por cobrar (ventas a crédito) | Finanzas | 3 | S | IT-12 |
| `FR-027` | Registrar cuentas por pagar (deudas con proveedores) | Finanzas | 3 | S | IT-12 |
| `FR-020` | Bitácora de auditoría: usuario/módulo/acción/fecha, no editable | Seguridad | 5 | M | IT-13 |
| `FR-039` | Bloquear cuenta tras 3 intentos fallidos por 15 minutos | Seguridad | 3 | M | IT-13 |
| `FR-040` | Cierre de sesión automático tras 30 min de inactividad | Seguridad | 2 | M | IT-13 |

**Total R3:** 43 SP en 5 iteraciones

---

### RELEASE 4 — "RRHH, Integraciones y KM" (IT-14 a IT-16)
*Objetivo: módulo RRHH completo, integraciones externas, base de conocimiento*

| ID | Historia de Usuario | Módulo | SP | MoSCoW | IT |
|----|---------------------|--------|----|--------|----|
| `FR-035` | Registrar ficha completa del empleado con DNI único | RRHH | 3 | S | IT-14 |
| `FR-024` | Control de asistencia: marca entrada/salida por DNI | RRHH | 3 | S | IT-14 |
| `FR-036` | Gestionar solicitudes de vacaciones con validación de superposición | RRHH | 3 | C | IT-14 |
| `FR-037` | Calcular planilla mensual con descuentos ONP/AFP y exportar | RRHH | 5 | C | IT-15 |
| `FR-038` | Registrar evaluación de desempeño (escala 1-5) | RRHH | 2 | C | IT-15 |
| `FR-033` | Validar DNI con API RENIEC, modo degradado si falla | Integración | 5 | C | IT-15 |
| `FR-048` | Obtener tipo de cambio SUNAT con actualización automática | Integración | 3 | C | IT-15 |
| `FR-041` | Exportar bitácora a CSV y PDF con filtros | Seguridad | 2 | S | IT-16 |
| `FR-042` | Gestionar permisos por módulo y rol con efecto inmediato | Seguridad | 3 | S | IT-16 |
| `FR-047` | Historial de devoluciones por período exportable | Ventas | 2 | C | IT-16 |
| `FR-034` | Emitir boleta electrónica vía OSE/SUNAT con código QR | Integración | 8 | C | IT-16 |

**Total R4:** 39 SP en 3 iteraciones

---

## 5. Resumen de Releases

| Release | Foco | Iteraciones | SP | Entrega |
|---------|------|-------------|----|---------|
| **R1** | Base funcional + Dashboard + Compras | IT-01 a IT-04 | 34 | Semana 8 |
| **R2** | Inventario completo + POS mejorado | IT-05 a IT-08 | 34 | Semana 16 |
| **R3** | CRM + Reportes + Seguridad | IT-09 a IT-13 | 43 | Semana 26 |
| **R4** | RRHH + Integraciones + KM | IT-14 a IT-16 | 39 | Semana 32 |
| **TOTAL** | | **16 iteraciones** | **150 SP** | |

---

## 6. Plan de Iteración Detallado — IT-01 (Primera semana)

> Cada iteración debe documentarse con esta estructura antes de comenzar:

### Iteración IT-01 — "Reparar el TPS"

| Campo | Valor |
|-------|-------|
| Semanas | 1 y 2 |
| Objetivo | Corregir los 6 defectos heredados del TPS y dejar la base lista para ERP |
| Velocidad comprometida | 8 SP |
| Criterio de éxito | Los 6 HU del TPS pasan sus criterios de aceptación sin defectos |

**Tareas técnicas:**

| # | Tarea | HU relacionada | Responsable | Estado |
|---|-------|---------------|-------------|--------|
| 1 | Corregir `attachEvents()` en `NewVenta.java`: validar cantidad > 0, entero, no supera stock | 1000001 | Programador | ⬜ |
| 2 | Agregar columna `idProducto` al reporte de ventas en `ReporteVentas.java` | 1000003 | Programador | ⬜ |
| 3 | Reemplazar JComboBox mes por dos JDateChooser en `RegistroVentasMensual.java` | 1000005 | Programador | ⬜ |
| 4 | Agregar sección de movimientos en `ReporteClientes.java` cruzando ventas.txt | 1000012 | Programador | ⬜ |
| 5 | Verificar índice del split en `Producto.java` para campo descripcion | 1000014 | Programador | ⬜ |
| 6 | Implementar generador de ID secuencial único en `GestionarProductos.java` | 1000015 | Programador | ⬜ |
| 7 | Instalar y configurar `UITokens.java`, `UIFonts.java`, `UISpacing.java` | UI/UX base | Programador | ⬜ |
| 8 | Aplicar estilo correcto al `FrmLogin.java` según `03_DISEÑO_UI_UX_PROFESIONAL.md` | UI/UX | Programador | ⬜ |

---

## 7. Burndown Chart Conceptual — R1

```
SP  │
34  │ ████████████████████████████████████
    │ Velocidad ideal (línea diagonal)
    │  ↘
28  │    ↘
    │      ↘
21  │        ↘
    │          ↘
14  │            ↘
    │              ↘
7   │                ↘
    │                  ↘
0   │────────────────────↘───────────────
    IT-01  IT-02  IT-03  IT-04

Si la línea real está POR ENCIMA de la ideal → el equipo va retrasado
Si la línea real está POR DEBAJO de la ideal → el equipo va adelantado
```

---

## 8. Riesgos del Proyecto (Sesión 10 — Gestión de Riesgos)

| Riesgo | Probabilidad | Impacto | Mitigación |
|--------|-------------|---------|-----------|
| Almacenamiento en `.txt` no soporta concurrencia | Alta | Alto | Migrar a H2 en IT-03 (paralelo al desarrollo de módulos) |
| `attachEvents()` sin implementar en 20+ IFrm | Alta | Alto | Priorizar IT-01 y IT-02 antes de cualquier otro módulo |
| Integraciones RENIEC/SUNAT fallan en producción | Media | Medio | Implementar modo degradado (RNF-07) desde el inicio |
| FrmDashboard rediseñado rompe navegación existente | Media | Alto | Migrar a CardLayout antes de IT-02, probar navegación completa |
| Equipo no conoce JDBC | Alta | Alto | Taller JDBC + H2 en semana 1 de IT-03 |

---

*Referencia: Beck (1999) · Stellman & Greene (2021) · Sommerville (2016, Cap. 3)*

# 📝 HISTORIAS DE USUARIO COMPLETAS — ERP LAREDO
**48 HU con criterios de aceptación Gherkin · Formato ISO 29148 / Wiegers & Beatty (2013)**

---

## Convenciones

```
ID:        FR-XXX (nueva) | 1000XXX* (herencia TPS con defecto)
Formato:   Como [ROL], quiero [ACCIÓN], para [BENEFICIO]
Criterios: Given [PRECONDICIÓN] / When [ACCIÓN] / Then [RESULTADO]
SP:        Story Points (1=trivial, 2=simple, 3=medio, 5=complejo, 8=muy complejo)
```

---

## 🔴 PRIORIDAD MÁXIMA — Defectos TPS (IT-01)

### `1000001*` — Registrar ventas (corregir cantidad)
- **Como** Cajero del Minimarket LAREDO
- **Quiero** que el campo cantidad valide datos antes de registrar la venta
- **Para** evitar errores de cálculo en el total cobrado al cliente

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | El Cajero ingresa "0" en el campo cantidad | Presiona Agregar al carrito | El sistema muestra ⚠ "La cantidad debe ser mayor a 0" y no agrega el producto |
| CA-2 | El Cajero ingresa "-5" o letras en cantidad | Presiona Agregar | El sistema muestra ⚠ "Ingrese un número entero positivo" |
| CA-3 | El Cajero ingresa una cantidad mayor al stock disponible | Presiona Agregar | El sistema muestra ⚠ "Stock insuficiente: hay X unidades disponibles" |
| CA-4 | El Cajero ingresa "3" para un producto con 5 unidades | Presiona Agregar | El producto se agrega al carrito, el subtotal se calcula correctamente: precio × 3 |

> **SP:** 1 · **MoSCoW:** Must · **Clase afectada:** `NewVenta.java`

---

### `1000003*` — Reporte de ventas (agregar ID producto)
- **Como** Administrador del Minimarket LAREDO
- **Quiero** que el reporte de ventas incluya el código del producto
- **Para** poder rastrear cada transacción por producto específico

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | Existen ventas registradas con productos en el sistema | El Admin genera el reporte PDF de ventas | El PDF incluye la columna "Cód. Producto" en cada fila del detalle |
| CA-2 | El Admin abre el reporte generado | Lo revisa en el visor | El código de producto es legible y corresponde con el producto del catálogo |

> **SP:** 1 · **Clase afectada:** `ReporteVentas.java`

---

### `1000005*` — Rango de fechas en ventas
- **Como** Administrador del Minimarket LAREDO
- **Quiero** poder filtrar el registro de ventas por un rango de fechas personalizado
- **Para** analizar períodos específicos sin estar limitado al mes fijo

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | El Admin accede al módulo de ventas | Selecciona fecha inicio "01/06/2026" y fecha fin "15/06/2026" | El sistema muestra solo las ventas dentro de ese rango |
| CA-2 | El Admin ingresa una fecha fin anterior a la fecha inicio | Presiona Filtrar | El sistema muestra ⚠ "La fecha fin debe ser posterior a la fecha inicio" |
| CA-3 | No hay ventas en el rango seleccionado | Presiona Filtrar | El sistema muestra "Sin ventas para el período seleccionado" |

> **SP:** 2 · **Clase afectada:** `RegistroVentasMensual.java`

---

### `1000012*` — Movimientos del cliente en reporte
- **Como** Administrador
- **Quiero** ver el historial de compras de cada cliente en el reporte
- **Para** conocer el comportamiento de compra de cada cliente

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | El cliente con DNI 12345678 tiene 5 compras registradas | El Admin genera el reporte de ese cliente | El PDF muestra una sección "Historial de Compras" con fecha, total y productos de cada venta |
| CA-2 | El cliente no tiene compras | El Admin genera su reporte | La sección muestra "Este cliente no tiene compras registradas" |

> **SP:** 2 · **Clase afectada:** `ReporteClientes.java`

---

### `1000014*` — Descripción correcta en reporte productos
> **SP:** 1 · Verificar índice del `split()` en `Producto.java` al leer productos.txt

### `1000015*` — Numeración de productos sin errores
> **SP:** 1 · Implementar generador de ID secuencial leyendo el último ID del archivo antes de crear uno nuevo

---

## 📊 DASHBOARD (R1 — IT-02)

### `FR-001` — Dashboard: ingresos por período
- **Como** Gerente del Minimarket LAREDO
- **Quiero** ver los ingresos totales del día, semana y mes en el panel principal
- **Para** tomar decisiones operativas rápidas sin abrir reportes

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | Existen ventas registradas hoy | El Gerente abre el Dashboard | Aparece una KPI Card "Ventas Hoy" con el total en S/. calculado sumando ventas del día |
| CA-2 | No hay ventas registradas hoy | El Gerente abre el Dashboard | La card muestra "S/ 0.00" con el mensaje "Sin ventas hoy" |
| CA-3 | El Gerente abre el Dashboard un lunes (inicio de semana) | Ve la card de semana | Muestra las ventas de la semana actual (lunes a domingo) |
| CA-4 | Hay ventas de distintos métodos de pago | El Gerente ve la card del mes | El total incluye todos los métodos: efectivo + digital |

> **SP:** 5 · **MoSCoW:** Must · **Pantalla:** `FrmDashboard.java` (refactorizar)

---

### `FR-002` — Dashboard: alertas de stock crítico
- **Como** Gerente del Minimarket LAREDO
- **Quiero** ver en el Dashboard qué productos están con stock crítico
- **Para** hacer pedidos de reposición a tiempo

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | El producto "Arroz Gloria" tiene stock=3 y stockMinimo=5 | El Gerente abre el Dashboard | Aparece en la sección de alertas con 🔴 "Arroz Gloria — 3 uds. (mín. 5)" |
| CA-2 | Todos los productos tienen stock normal | El Gerente abre el Dashboard | La sección muestra "✅ Sin alertas de stock" |
| CA-3 | Se registra una venta que lleva un producto a stock crítico | El Dashboard está abierto | La alerta aparece al siguiente refresco automático (5 min) o al navegar al Dashboard |

> **SP:** 3 · **MoSCoW:** Must

---

## 🛒 COMPRAS Y PROVEEDORES (R1 — IT-03, IT-04)

### `FR-011` — Registrar proveedor
- **Como** Gerente
- **Quiero** registrar nuevos proveedores con sus datos completos
- **Para** poder asociarlos a las compras de reposición

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | El formulario está vacío | El Gerente ingresa RUC "20601234567", razón social y demás datos, y guarda | El proveedor queda en proveedores.txt con código P001; aparece en el listado |
| CA-2 | El Gerente ingresa un RUC que no empieza en 10 o 20 | Presiona Guardar | ⚠ "RUC inválido. Debe empezar con 10 (persona natural) o 20 (empresa)" |
| CA-3 | El RUC ya existe en el sistema | El Gerente intenta guardar | ⚠ "Ya existe un proveedor con RUC 20601234567" |
| CA-4 | El Gerente deja el campo Razón Social vacío | Presiona Guardar | ⚠ "La razón social es obligatoria" |
| CA-5 | El Gerente ingresa un email inválido | Presiona Guardar | ⚠ "Ingrese un correo electrónico válido" |

> **SP:** 3 · **Modelo nuevo:** `Proveedor.java` · **Archivo:** `proveedores.txt`

---

### `FR-008` — Registrar compra a proveedor
- **Como** Gerente
- **Quiero** registrar el ingreso de mercadería de un proveedor
- **Para** mantener el inventario actualizado y tener historial de compras

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | Existe proveedor P001 y producto P005 | El Gerente registra compra: P001, factura F001-123, 50 unidades de P005 | Stock de P005 aumenta en 50; la compra queda en compras.txt; el kardex registra Entrada |
| CA-2 | El Gerente intenta registrar el mismo N° de factura | Presiona Guardar | ⚠ "Esta factura ya fue registrada. Verifique el número." |
| CA-3 | La compra tiene 3 productos distintos | Se confirma la compra | Los 3 productos actualizan su stock de forma atómica (RNF-10) |
| CA-4 | El sistema falla durante el proceso de actualización de stock | — | Ningún cambio persiste (atomicidad garantizada) |

> **SP:** 5 · **MoSCoW:** Must · **Modelos:** `Compra.java`, `DetalleCompra.java`

---

## 📦 INVENTARIO (R2 — IT-05, IT-06)

### `FR-004` — Kardex de movimientos
- **Como** Gerente
- **Quiero** ver el historial de entradas y salidas de cada producto
- **Para** auditar los movimientos de inventario y detectar inconsistencias

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | El producto P001 tuvo 3 entradas y 5 salidas | El Gerente accede al Kardex de P001 | Se muestra tabla cronológica con: fecha, tipo (Entrada/Salida/Merma), cantidad, stock anterior, stock resultante |
| CA-2 | No hay movimientos para el producto | El Gerente lo consulta | "Sin movimientos registrados para este producto" |
| CA-3 | El Gerente filtra por rango de fechas | — | Solo muestra movimientos del período seleccionado |

> **SP:** 5 · **Modelo nuevo:** `MovimientoInventario.java` · **Archivo:** `kardex.txt`

---

### `FR-007` — Alertas de vencimiento
- **Como** Gerente
- **Quiero** que el sistema me avise cuando hay productos próximos a vencer
- **Para** retirarlos antes de que sean consumidos por clientes o generen merma

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | El producto "Leche Gloria Lote 4" vence el 20/06/2026 y hoy es 15/06/2026 | El Gerente abre el módulo de alertas | Aparece en la lista con 🟡 "Leche Gloria Lote 4 — vence en 5 días (20/06/2026)" |
| CA-2 | Un producto ya venció | — | Aparece con 🔴 "VENCIDO — retirar del estante inmediatamente" |
| CA-3 | El Gerente registra la retirada del producto | — | El producto desaparece de las alertas y genera un registro de merma automático |

> **SP:** 3 · **Campo nuevo:** `fechaVencimiento` en `Producto.java`

---

## 💰 VENTAS Y POS (R2 — IT-07, IT-08)

### `FR-014` — Devoluciones
- **Como** Cajero
- **Quiero** registrar la devolución de un producto vendido
- **Para** actualizar el inventario y registrar el reembolso al cliente

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | Existe la venta V001 con producto P001 cantidad 2 | El Cajero ingresa N° de venta V001, selecciona P001 y motivo "Producto dañado" | La devolución se registra, el stock de P001 aumenta en 2, se muestra el monto a reembolsar |
| CA-2 | El Cajero ingresa un N° de venta que no existe | Presiona Registrar | ⚠ "La venta ingresada no existe en el sistema" |
| CA-3 | El Cajero intenta devolver más unidades de las compradas | — | ⚠ "No puede devolver más unidades de las que se vendieron (máx: 2)" |

> **SP:** 5 · **Modelo nuevo:** `Devolucion.java`

---

### `FR-023` — Descuento manual en venta
- **Como** Cajero
- **Quiero** aplicar un descuento al total de la venta
- **Para** reflejar promociones o precios negociados con el cliente

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | Hay una venta con total S/ 50.00; el usuario tiene rol Cajero | El Cajero ingresa 10% de descuento | El sistema calcula S/ 5.00 de descuento; total = S/ 45.00; visible en pantalla y en comprobante |
| CA-2 | El Cajero intenta aplicar 25% de descuento (límite = 20% para Cajero) | Presiona Aplicar | ⚠ "Descuento máximo para Cajero: 20%. Solicite aprobación del Gerente" |
| CA-3 | El Gerente aplica el mismo 25% | — | El sistema lo acepta y registra que fue el Gerente quien aprobó |
| CA-4 | El Cajero ingresa "-5" o "abc" como descuento | Presiona Aplicar | ⚠ "El descuento debe ser un número entre 0 y 20" |

> **SP:** 2 · **MoSCoW:** Must

---

## 👥 CRM Y FIDELIZACIÓN (R3 — IT-09, IT-10)

### `FR-030` — Acumular puntos de fidelización
- **Como** Cajero
- **Quiero** que el sistema acumule puntos automáticamente al cliente en cada venta
- **Para** incentivar la fidelidad al minimarket

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | El cliente DNI 12345678 tiene 50 puntos y realiza una compra de S/ 35.00 | Se confirma la venta | El cliente acumula 35 puntos más; total = 85 puntos; el comprobante muestra "Puntos ganados: 35 | Saldo: 85 pts" |
| CA-2 | La venta se hace sin asociar cliente | Se confirma | No se acumulan puntos; el sistema no genera error |
| CA-3 | La venta es de S/ 4.50 (menos de S/ 5.00) | Se confirma | No se acumulan puntos; comprobante muestra "Compra mínima para puntos: S/ 5.00" |

> **SP:** 3 · **Campo nuevo:** `puntosAcumulados` en `Cliente.java`

---

### `FR-031` — Canjear puntos
- **Como** Cajero
- **Quiero** aplicar los puntos del cliente como descuento en su compra
- **Para** que el cliente vea el beneficio de su fidelidad

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | El cliente tiene 200 puntos y el total de venta es S/ 15.00 | El Cajero activa "Canjear puntos" y el cliente decide usar 100 | El sistema descuenta S/ 1.00 del total (S/ 14.00); reduce 100 puntos del saldo del cliente |
| CA-2 | El cliente tiene 50 puntos y quiere canjear 150 | El Cajero intenta procesar | ⚠ "Saldo insuficiente. El cliente tiene 50 puntos disponibles" |
| CA-3 | El descuento por puntos + descuento manual supera el total | — | ⚠ "El descuento total no puede superar el valor de la venta" |

> **SP:** 3 · **MoSCoW:** Should

---

## 📊 REPORTES AVANZADOS (R3 — IT-11)

### `FR-019` — Reporte de ganancias por período
- **Como** Gerente
- **Quiero** ver un reporte de las ganancias del negocio en un período dado
- **Para** evaluar la rentabilidad y tomar decisiones de precios

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | Hay ventas registradas en junio 2026 | El Gerente selecciona rango 01/06 – 30/06 y genera el reporte | El reporte muestra: total de ingresos, total de costos (precio de compra × cantidad), ganancia bruta, porcentaje de margen |
| CA-2 | No hay ventas en el período | — | "Sin ventas registradas para el período seleccionado" |
| CA-3 | El Gerente presiona "Exportar .xlsx" | — | Se genera un archivo Excel con las mismas columnas del reporte, abre correctamente en Excel 2016+ |
| CA-4 | El Gerente presiona "Exportar PDF" | — | Se genera el PDF con el mismo formato, abre en lector de PDF |

> **SP:** 3 · **RNF:** RNF-08 (exportación)

---

## 🔒 SEGURIDAD (R3 — IT-13)

### `FR-020` — Bitácora de auditoría
- **Como** Gerente
- **Quiero** ver un registro de todas las acciones realizadas por usuarios del sistema
- **Para** auditar operaciones y detectar anomalías

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | El usuario "jperez" registra una venta | Ocurre la operación | Se crea automáticamente un registro en bitacora.txt con: usuario=jperez, módulo=Ventas, acción=RegistroVenta, resultado=Éxito, fecha/hora actual |
| CA-2 | El Gerente accede a la bitácora y filtra por usuario "jperez" | — | Solo muestra las acciones de ese usuario |
| CA-3 | El Gerente intenta editar un registro de la bitácora | — | El botón Editar no existe; es solo lectura. La bitácora es inmutable desde la UI |
| CA-4 | Un login falla | — | Se registra en bitácora con resultado=Fallo y el nombre de usuario intentado |

> **SP:** 5 · **Modelo nuevo:** `Bitacora.java` · **RNF:** RNF-06

---

### `FR-039` — Bloqueo de cuenta
- **Como** Sistema ERP LAREDO (automático)
- **Quiero** bloquear una cuenta tras 3 intentos fallidos consecutivos
- **Para** proteger el sistema contra accesos no autorizados

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | El usuario "cajero1" intenta login con contraseña incorrecta | 3.er intento | La cuenta se bloquea 15 minutos; aparece ⚠ "Cuenta bloqueada por 3 intentos fallidos. Intente en 15 min" |
| CA-2 | El 4.° intento ocurre antes de 15 minutos | — | ⚠ "Cuenta bloqueada. Tiempo restante: X minutos" |
| CA-3 | El Administrador accede a Gestión de Usuarios | Desbloquea manualmente | La cuenta queda disponible de inmediato |

> **SP:** 3 · **RNF:** RNF-04

---

## 👤 RRHH (R4 — IT-14, IT-15)

### `FR-035` — Registrar ficha de empleado
- **Como** Gerente
- **Quiero** registrar los datos completos de cada empleado
- **Para** tener control del personal del minimarket

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | El Gerente ingresa todos los datos válidos | Presiona Guardar | El empleado queda en empleados.txt con código EMP001; se muestra en el listado |
| CA-2 | El Gerente ingresa DNI de 7 dígitos | Presiona Guardar | ⚠ "El DNI debe tener exactamente 8 dígitos" |
| CA-3 | El DNI ya existe en el sistema | Presiona Guardar | ⚠ "Ya existe un empleado con DNI XXXXXXXX" |
| CA-4 | El sueldo ingresado es menor a S/ 1,025.00 | Presiona Guardar | ⚠ "El sueldo base no puede ser menor al mínimo legal (S/ 1,025.00)" |

> **SP:** 3 · **Modelo nuevo:** `Empleado.java`

---

### `FR-024` — Control de asistencia
- **Como** Gerente
- **Quiero** registrar la entrada y salida de cada empleado por DNI
- **Para** controlar la puntualidad y calcular las horas trabajadas

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | El empleado con DNI 12345678 existe y no tiene marca del día | Gerente ingresa el DNI y selecciona "Entrada" | Se registra la hora de entrada actual en asistencia.txt; ✅ "Entrada registrada — 08:03 a.m." |
| CA-2 | El mismo empleado tiene una entrada sin salida | Gerente ingresa su DNI y selecciona "Salida" | Se registra la hora de salida; muestra horas trabajadas |
| CA-3 | El empleado intenta marcar dos entradas el mismo día | — | ⚠ "Ya existe una marca de entrada para hoy sin salida correspondiente" |
| CA-4 | Se ingresa un DNI que no existe en el sistema | — | ⚠ "Empleado no encontrado en el sistema" |

> **SP:** 3 · **Modelo nuevo:** `Asistencia.java`

---

## 🔌 INTEGRACIONES (R4 — IT-15, IT-16)

### `FR-033` — Validar DNI con API RENIEC
| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | El Cajero ingresa DNI "12345678" con conexión disponible | Presiona Tab o el ícono de validar | Los campos nombre y apellido se autocompletan con datos de RENIEC; ✅ "DNI verificado" |
| CA-2 | El servicio RENIEC no está disponible | Misma acción | ⚠ "Servicio RENIEC no disponible. Ingrese los datos manualmente." El formulario sigue editable (RNF-07) |
| CA-3 | El DNI ingresado no existe en RENIEC | — | ⚠ "DNI no encontrado en el registro nacional" |

> **SP:** 5 · **RNF:** RNF-07 (modo degradado)

---

### `FR-048` — Tipo de cambio SUNAT
| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | El tipo de cambio almacenado tiene más de 24 horas | El sistema inicia o el Gerente abre el Dashboard | El sistema consulta la API SUNAT, actualiza el valor y muestra "Tipo de cambio: S/ 3.72 (actualizado 15/06/2026)" |
| CA-2 | La API SUNAT no responde | — | Usa el último valor guardado y muestra "S/ 3.70 (al 14/06/2026 — no actualizado)" |

> **SP:** 3 · **RNF:** RNF-07

---

## 📋 Resumen de HU por Módulo

| Módulo | HU Must | HU Should | HU Could | Total |
|--------|---------|-----------|----------|-------|
| Correcciones TPS | 6 | 0 | 0 | 6 |
| Dashboard | 2 | 0 | 0 | 2 |
| Compras / Proveedores | 5 | 1 | 0 | 6 |
| Inventario | 3 | 3 | 0 | 6 |
| Ventas / POS | 5 | 2 | 1 | 8 |
| CRM / Fidelización | 1 | 4 | 1 | 6 |
| Reportes | 4 | 0 | 0 | 4 |
| Finanzas | 0 | 3 | 0 | 3 |
| Seguridad | 4 | 1 | 0 | 5 |
| RRHH | 1 | 2 | 2 | 5 |
| Integraciones | 0 | 0 | 3 | 3 |
| **TOTAL** | **31** | **16** | **7** | **54** |

---

*Referencia: Wiegers & Beatty (2013) · ISO/IEC/IEEE 29148:2018 · Beck (1999)*

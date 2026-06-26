# 📝 HISTORIAS DE USUARIO COMPLETAS Y TRAZABLES — ERP LAREDO
**Planificación Alineada al Código Realizado - 6 Módulos Básicos**

Esta planificación refleja exactamente el código fuente desarrollado para el ERP Minimarket LAREDO. Sigue estrictamente la estructura solicitada por el docente, basada en los autores vistos en clase (Wiegers & Beatty, ISO 29148) y las directrices del archivo `INSTRUCCIONES_IA_PROYECTO_ERP.md`.

---

## 1. MÓDULO: DASHBOARD (PANEL DE CONTROL)

| Identificador (ID) | Rol | Característica / Funcionalidad | Razón / Resultado | Número (#) de Escenario | Criterio de Aceptación (Título) | Contexto | Evento | Resultado / Comportamiento esperado |
|:---|:---|:---|:---|:---|:---|:---|:---|:---|
| **FR-001** | Gerente | Quiero ver los ingresos totales del día y semana calculados en tiempo real. | Para tomar decisiones operativas rápidas sin abrir reportes. | 1 | KPI de Ventas Hoy | Dado que existen ventas registradas en la base de datos para la fecha actual. | Cuando el Gerente accede al Dashboard y se llama a `VentaDAO.totalVentasHoy()`. | El sistema muestra una KPI Card "VENTAS HOY" con el monto total exacto calculado por la base de datos (Ej: S/ 1,245.50). |
| | | | | 2 | KPI de Ventas Semana | Dado que existen ventas en la semana en curso. | Cuando el sistema invoca `VentaDAO.totalVentasSemana()`. | El Dashboard muestra la KPI Card "VENTAS SEMANA" con los ingresos acumulados. |
| | | | | 3 | Sin ventas registradas | Dado que no hay ventas en el día. | Cuando el Dashboard se inicializa. | La tarjeta "VENTAS HOY" muestra "S/ 0.00" y el mensaje "Sin ventas hoy". |
| **FR-002** | Gerente | Quiero ver alertas de productos con stock crítico (bajo el mínimo). | Para hacer pedidos de reposición a los proveedores a tiempo. | 1 | Alertas de Stock Crítico | Dado que existen productos cuyo `cantidad` es menor que su `stockMinimo` en la BD. | Cuando el Dashboard invoca `ProductoDAO.productosConStockBajo()`. | El sistema lista los productos en rojo en la tabla "Alertas de Inventario Crítico" con su faltante. |
| | | | | 2 | Sin alertas activas | Dado que ningún producto tiene stock por debajo de su mínimo. | Cuando el Dashboard se actualiza. | La tabla muestra "✅ Sin alertas de stock" y no se resaltan indicadores. |

## 2. MÓDULO: VENTAS Y PUNTO DE VENTA (POS)

| Identificador (ID) | Rol | Característica / Funcionalidad | Razón / Resultado | Número (#) de Escenario | Criterio de Aceptación (Título) | Contexto | Evento | Resultado / Comportamiento esperado |
|:---|:---|:---|:---|:---|:---|:---|:---|:---|
| **FR-014** | Vendedor | Quiero registrar devoluciones de productos de una venta previa. | Para actualizar el inventario y mantener las finanzas precisas. | 1 | Devolución parcial de producto | Dado que la venta V001 tiene 2 unidades del producto P001. | Cuando el Cajero selecciona P001 e ingresa la devolución de 1 unidad. | El sistema incrementa el stock de P001 en 1, reduce el total de V001 y registra en la Bitácora y Kardex. |
| **FR-015** | Vendedor | Quiero procesar ventas con múltiples métodos de pago (Efectivo, Yape, Plin, Tarjeta). | Para dar facilidades de pago al cliente final. | 1 | Pago con Yape / Plin | Dado que el cliente escoge pagar por aplicativo móvil. | Cuando el Vendedor selecciona "Yape" en el ComboBox de `IFrmPuntoVenta`. | El sistema guarda la venta etiquetando el método de pago y genera el ticket 80mm correspondiente. |
| **FR-022** | Vendedor | Quiero buscar productos por código de barras o por nombre en el POS. | Para añadir productos al carrito rápidamente sin memorizar códigos. | 1 | Búsqueda dinámica por nombre | Dado que el Vendedor no conoce el ID del producto. | Cuando escribe "Arroz" en el campo de búsqueda de producto. | El sistema autocompleta la lista con todos los productos que contienen "Arroz" usando `ProductoDAO.buscarPorTermino()`. |
| **FR-023** | Gerente / Vendedor | Quiero aplicar descuentos manuales a nivel de toda la boleta. | Para ofrecer rebajas especiales o cerrar ventas por volumen. | 1 | Descuento del 10% | Dado que el total del carrito es S/ 100.00. | Cuando el Vendedor aplica un 10% en la casilla de descuentos de `IFrmPuntoVenta`. | El sistema calcula S/ 10.00 de descuento, actualiza el "Total a Pagar" a S/ 90.00 y lo refleja en el ticket. |
| **FR-030** | Vendedor | Quiero que el sistema acumule puntos automáticamente en cada venta. | Para fidelizar a los clientes recurrentes. | 1 | Acumulación por compra | Dado que el Cliente con DNI 12345678 realiza una compra de S/ 45.00. | Cuando se aprueba la venta y se guarda el registro en la BD. | El sistema llama a `ClienteDAO.actualizarPuntosPorDni()` sumando 45 puntos al cliente. |
| **FR-031** | Vendedor | Quiero canjear los puntos de un cliente como descuento en su compra actual. | Para recompensar al cliente por su fidelidad. | 1 | Canje de Puntos Exitoso | Dado que el cliente tiene 100 puntos (equivalente a S/ 1.00 de descuento). | Cuando el cajero marca "Canjear Puntos" en el POS. | El sistema resta S/ 1.00 al total de la venta, descuenta los 100 puntos en BD y lo registra en el ticket. |

## 3. MÓDULO: GESTIÓN DE INVENTARIO Y KARDEX

| Identificador (ID) | Rol | Característica / Funcionalidad | Razón / Resultado | Número (#) de Escenario | Criterio de Aceptación (Título) | Contexto | Evento | Resultado / Comportamiento esperado |
|:---|:---|:---|:---|:---|:---|:---|:---|:---|
| **FR-003** | Gerente | Quiero consultar el stock de productos filtrando por categoría. | Para realizar inspecciones de góndolas o planificación de piso. | 1 | Filtro por categoría | Dado que existen productos en la categoría "Lácteos". | Cuando el usuario filtra por esa categoría en `IFrmGestionProductos`. | La tabla de productos se actualiza para mostrar únicamente lácteos. |
| **FR-004** | Gerente | Quiero ver el historial inmutable de movimientos en el Kardex. | Para auditar entradas, salidas y detectar posibles mermas o robos. | 1 | Registro de Salida por Venta | Dado que se completa una venta de 3 botellas de aceite. | Cuando `VentaDAO.registrarVentaCompleta()` ejecuta la transacción. | El sistema inserta un movimiento tipo "SALIDA" (motivo "VENTA") en la tabla `tb_kardex` atado a la fecha y hora exactas. |
| **FR-043** | Gerente | Quiero definir y modificar el "Stock Mínimo" de cada producto individualmente. | Para ajustar las alertas de reposición a la rotación real de cada ítem. | 1 | Stock mínimo bajo alcanzado | Dado que el producto "Pan Bimbo" tiene stock 4 y mínimo 5. | Cuando se abre el Dashboard o Reporte de Stock Bajo. | Aparece la alerta porque la condición `cantidad < stockMinimo` se cumple. |

## 4. MÓDULO: COMPRAS Y PROVEEDORES

| Identificador (ID) | Rol | Característica / Funcionalidad | Razón / Resultado | Número (#) de Escenario | Criterio de Aceptación (Título) | Contexto | Evento | Resultado / Comportamiento esperado |
|:---|:---|:---|:---|:---|:---|:---|:---|:---|
| **FR-008** | Gerente | Quiero registrar la recepción de compras a mis proveedores con facturas. | Para tener control de pagos y documentar entradas oficiales. | 1 | Registro de Compra Completo | Dado que el proveedor trae mercadería amparada en una factura. | Cuando el usuario guarda la compra en `IFrmRegistroCompras`. | El sistema guarda la cabecera (compra) y el detalle de cada producto adquirido en BD. |
| **FR-009** | Sistema | Quiero que al guardar una compra, el stock se incremente automáticamente. | Para evitar procesos manuales de doble registro y errores humanos. | 1 | Incremento atómico de inventario | Dado que la compra incluye 50 unidades del producto X. | Cuando `CompraDAO` confirma el registro mediante una transacción SQL. | El stock del producto X sube en 50 unidades y se registra una "ENTRADA" en el Kardex de manera automática y transaccional. |
| **FR-011** | Gerente | Quiero registrar nuevos proveedores y validar su número de RUC. | Para mantener una cartera de abastecedores estandarizada. | 1 | Registro de Proveedor Válido | Dado que el RUC ingresado tiene 11 dígitos y empieza con 10 o 20. | Cuando se hace clic en "Guardar" en `IFrmGestionProveedores`. | El sistema guarda el proveedor. Si es duplicado, el sistema avisa con "Ya existe un proveedor con este RUC". |

## 5. MÓDULO: CRM Y GESTIÓN DE CLIENTES

| Identificador (ID) | Rol | Característica / Funcionalidad | Razón / Resultado | Número (#) de Escenario | Criterio de Aceptación (Título) | Contexto | Evento | Resultado / Comportamiento esperado |
|:---|:---|:---|:---|:---|:---|:---|:---|:---|
| **FR-016** | Gerente | Quiero ver el historial de compras completo por cada cliente. | Para analizar tendencias de compra o resolver disputas. | 1 | Historial de Cliente Existente | Dado que el cliente con DNI 12345678 ha comprado 3 veces. | Cuando en `IFrmGestionClientes` se pulsa "Historial Compras". | Se llama a `VentaDAO.ventasPorCliente()` y aparece una tabla con las 3 ventas y sus montos. |
| **FR-021** | Gerente | Quiero conocer los 5 productos más consumidos por un cliente específico. | Para poder armar promociones personalizadas a futuro. | 1 | Top 5 Productos por Cliente | Dado que el cliente es frecuente en cervezas y snacks. | Cuando se pulsa "Top Productos" en el perfil del cliente. | El sistema suma el detalle de ventas (`ClienteDAO.topProductosPorCliente()`) y lista los más comprados. |
| **FR-032** | Gerente | Quiero tener un Ranking VIP de los mejores clientes del Minimarket. | Para otorgar beneficios a quienes más ingresos le generan al negocio. | 1 | Visualización del Ranking VIP | Dado que hay múltiples clientes con distinto volumen de gasto histórico. | Cuando se pulsa "Ranking VIP" en `IFrmGestionClientes`. | El sistema usa un query SQL con `SUM(total) GROUP BY cliente` (`ClienteDAO.rankingVip()`) y lista el Top 10 ordenado por gasto. |

## 6. MÓDULO: REPORTES AVANZADOS, FINANZAS Y SEGURIDAD

| Identificador (ID) | Rol | Característica / Funcionalidad | Razón / Resultado | Número (#) de Escenario | Criterio de Aceptación (Título) | Contexto | Evento | Resultado / Comportamiento esperado |
|:---|:---|:---|:---|:---|:---|:---|:---|:---|
| **FR-019** | Gerente | Quiero reportes de ganancias reales (Ventas brutas - Costo de productos). | Para evaluar la rentabilidad del negocio y el margen bruto. | 1 | Margen Real Calculado | Dado que se vendieron productos que tienen un costo de adquisición previo. | Cuando se revisa el Tab de Finanzas en `IFrmReportesAvanzados`. | La rentabilidad no es un % estimado, sino el resultado del cruce estricto entre `tb_detalle_venta` y `tb_detalle_compra`. |
| **FR-020** | Admin | Quiero una Bitácora de Auditoría inmutable que registre cada evento del sistema. | Para tener trazabilidad total (quién hizo qué y cuándo). | 1 | Registro de auditoría | Dado que un usuario registra un cliente, una venta, o se loguea. | Cuando se llama a `BitacoraService.registrar()`. | El evento se guarda en la tabla `tb_bitacora` (Módulo, Acción, Resultado, Usuario) de solo lectura. No existe botón "Editar". |
| **FR-025** | Gerente | Quiero un Flujo de Caja que me liste entradas vs salidas del día. | Para cuadrar la caja al final del turno sin perder dinero. | 1 | Arqueo Diario | Dado que hubo S/ 500 en ventas y se registró un pago de S/ 100 de luz. | Cuando se abre `IFrmFlujoCaja`. | El sistema suma las entradas (500) resta salidas (100) y da el balance total (400) para el cuadre físico. |
| **FR-039** | Sistema | Quiero bloquear el acceso a un usuario tras varios intentos de contraseña fallidos. | Para prevenir ataques de fuerza bruta. | 1 | Bloqueo por Fuerza Bruta | Dado que un usuario ingresa mal su clave 3 veces consecutivas. | Cuando falla el 3er intento en `FrmLogin`. | El sistema deniega el acceso por un temporizador (15 min) e inscribe un evento de FALLO_SEGURIDAD en la bitácora. |

---

## CONVENCIÓN DE ESTÁNDARES APLICADOS EN EL DESARROLLO

De acuerdo con `INSTRUCCIONES_IA_PROYECTO_ERP.md`:

1. **Patrón de Arquitectura (MVC con DAOs)**
   - Todo el acceso a datos se hizo a través de Patrón DAO (`ClienteDAO`, `ProductoDAO`, `VentaDAO`).
   - Las vistas de Swing (`IFrm*`) nunca hacen `SELECT` o `UPDATE` directamente.
2. **Precisión Financiera (Clean Code)**
   - Todos los montos monetarios (precios, totales, subtotales) han sido modelados usando `java.math.BigDecimal` y no tipos flotantes primitivos (`double`, `float`) para evitar errores de redondeo de IEEE 754.
3. **Seguridad y Trazabilidad**
   - Transacciones críticas (ej. Registrar Venta: Cabecera + Detalle + Stock + Kardex) están envueltas en `conn.setAutoCommit(false)` para garantizar atomicidad (ACID).
   - Uso obligatorio de `PreparedStatement` para prevenir inyecciones SQL en todo método.
4. **Experiencia de Usuario (UI/UX)**
   - Se ha diseñado una capa centralizada de estilos llamada `UIKit.java` que actúa como Design System de toda la interfaz del ERP (colores modernos, tipografías consistentes, layout espaciado con componentes unificados).

### INTEGRANTES DEL EQUIPO

- Gutierrez Rodriguez Sebástian
- Venegas Aguirre José David
- Alberto Valderrama Carlos Alberto
- Alfaro Villanueva Cristhian
- Colonia Infantes Walter

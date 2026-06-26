# Prueba de Aceptación ERP LAREDO
**Trazabilidad Directa con Código Implementado - 6 Módulos Básicos**

| Identificador (ID) de la Historia | Rol | Característica / Funcionalidad | Razón / Resultado | Número (#) de Escenario | Criterio de Aceptación (Título) | Resultado / Comportamiento esperado | Estado |
|:---|:---|:---|:---|:---|:---|:---|:---|
| **FR-001** | Gerente | Ver ingresos diarios y semanales. | Monitorear el desempeño en tiempo real. | 1 | Cálculo automático de ventas | El sistema muestra los montos exactos en el Dashboard, consultando `VentaDAO`. | ✅ Completado |
| | | | | 2 | Sin ventas | Muestra "S/ 0.00" y mensaje indicativo. | ✅ Completado |
| **FR-002** | Gerente | Alertas de stock crítico. | Reposición oportuna. | 1 | Producto bajo mínimo | Aparece en la tabla roja de alertas del Dashboard automáticamente. | ✅ Completado |
| **FR-014** | Vendedor | Devoluciones de productos. | Corregir ventas y actualizar inventario. | 1 | Registrar devolución | El sistema devuelve la cantidad al stock (`ProductoDAO`) y actualiza el kardex. | ✅ Completado |
| **FR-022** | Vendedor | Búsqueda por código o nombre en POS. | Rapidez en la atención. | 1 | Búsqueda parcial | El sistema autocompleta los productos que coinciden con el texto ingresado. | ✅ Completado |
| **FR-023** | Vendedor | Descuentos manuales. | Otorgar rebajas a clientes VIP. | 1 | Aplicar porcentaje | El sistema recalcula el "Total a Pagar" restando el descuento ingresado. | ✅ Completado |
| **FR-030** | Vendedor | Acumular puntos de fidelidad. | Premiar compras recurrentes. | 1 | Acumulación automática | Se suman puntos equivalentes al monto gastado al DNI del cliente (`ClienteDAO`). | ✅ Completado |
| **FR-031** | Vendedor | Canjear puntos. | Incentivar fidelidad. | 1 | Descuento por canje | El sistema descuenta el valor de los puntos y lo refleja en la BD. | ✅ Completado |
| **FR-003** | Gerente | Consultar stock por categorías. | Revisión de piso. | 1 | Filtrado dinámico | La tabla del inventario solo muestra ítems de la categoría elegida. | ✅ Completado |
| **FR-004** | Gerente | Kardex de movimientos. | Control estricto. | 1 | Registro inmutable | Cada compra o venta genera un registro automático de Entrada o Salida. | ✅ Completado |
| **FR-008** | Gerente | Registro de compras a proveedores. | Abastecimiento. | 1 | Factura guardada | El sistema almacena la cabecera y el detalle de los productos adquiridos. | ✅ Completado |
| **FR-009** | Sistema | Actualizar stock automático tras compra. | Evitar doble registro. | 1 | Incremento atómico | El stock sube automáticamente al confirmar la compra usando transacciones SQL. | ✅ Completado |
| **FR-011** | Gerente | Registrar proveedores. | Estandarización. | 1 | Validación de RUC | Impide registrar duplicados y exige que el RUC sea de 11 dígitos. | ✅ Completado |
| **FR-016** | Gerente | Historial de compras de clientes. | Análisis CRM. | 1 | Lista de transacciones | Muestra todas las boletas pasadas vinculadas a un DNI. | ✅ Completado |
| **FR-021** | Gerente | Top 5 productos por cliente. | Promociones directas. | 1 | Ranking de preferencias | Lista los productos más comprados agrupando por unidades adquiridas. | ✅ Completado |
| **FR-032** | Gerente | Ranking VIP de clientes. | Beneficios. | 1 | Top clientes por gasto | Ordena a todos los clientes según su volumen total de compras históricas. | ✅ Completado |
| **FR-019** | Gerente | Reporte de Ganancias Reales. | Análisis financiero. | 1 | Margen bruto | Calcula ventas netas menos costos de compra exactos por producto. | ✅ Completado |
| **FR-020** | Admin | Bitácora de Auditoría. | Trazabilidad. | 1 | Inmutabilidad | Todo login, CRUD o falla genera un log que no puede ser borrado de `tb_bitacora`. | ✅ Completado |
| **FR-025** | Gerente | Flujo de Caja. | Cuadre diario. | 1 | Entradas vs Salidas | Muestra el dinero total en caja tras sumar ventas y restar gastos operativos. | ✅ Completado |
| **FR-039** | Sistema | Bloqueo por intentos fallidos. | Seguridad. | 1 | 3 intentos | La cuenta se bloquea por 15 minutos en el formulario de Login. | ✅ Completado |

---

### FIRMAS DE ACEPTACIÓN - EQUIPO DE DESARROLLO (QA)

- [x] Gutierrez Rodriguez Sebástian
- [x] Venegas Aguirre José David
- [x] Alberto Valderrama Carlos Alberto
- [x] Alfaro Villanueva Cristhian
- [x] Colonia Infantes Walter

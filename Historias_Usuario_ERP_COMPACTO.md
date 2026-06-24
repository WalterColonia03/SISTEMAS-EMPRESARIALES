# Historias de Usuario

| | Desarrollo ágil: Historias de usuario y criterios de aceptación | Unnamed: 2 | Unnamed: 3 | Unnamed: 4 | Unnamed: 5 | Unnamed: 6 | Unnamed: 7 | Unnamed: 8 | Unnamed: 9 |
|:------------|:------------------------------------------------------------------|:-----------------------------------|:----------------------------------------------------------------------|:-----------------------------------------------------------------------------|:------------------------|:-------------------------------------------|:---------------------------------------------------------------|:---------------------------------------------------|:-----------------------------------------------------------------------------|
| | | Enunciado de la Historia | | | | Criterios de Aceptación | | | |
| | Identificador (ID) de la Historia | Rol | Característica / Funcionalidad | Razón / Resultado | Número (#) de Escenario | Criterio de Aceptación (Título) | Contexto | Evento | Resultado / Comportamiento esperado |
| | FR-001 | Como Gerente | Quiero visualizar los ingresos diarios, semanales, mensuales | Para monitorear el desempeño | 1 | Visualizar ingresos diarios | Dado que existen ventas registradas | Cuando se acceda al Dashboard | El sistema muestra el total de ingresos diarios |
| | | | | | 2 | Visualizar ingresos semanales | Dado que existen ventas registradas en la semana | Cuando el administrador selecciona vista semanal | El sistema muestra los ingresos acumulados de la semana |
| | | | | | 3 | Visualizar ingresos mensuales | Dado que existen ventas registradas en el mes | Cuando el administrador selecciona vista mensual | El sistema muestra los ingresos acumulados del mes |
| | | | | | 4 | Sin ventas registradas | Dado que no existen ventas en el período seleccionado | El sistema muestra los ingresos acumulados del mes | El sistema muestra S/0.00 |
| | FR-002 | Como Gerente | Quiero visualizar alertas de productos próximos a agotarse | Para tomar decisiones de reposición oportunamente. | 1 | Mostrar productos críticos | Dado que existen productos con stock mínimo | Cuando se carga el Dashboard | El sistema muestra alertas visuales |
| | | | | | 2 | Resaltar productos agotados | Dado que un producto tiene stock 0 | Cuando se visualiza el Dashboard | El producto aparece resaltado |
| | | | | | 3 | Actualizar alertas automáticamente | Dado que se registra una venta | Cuando el stock disminuye | El Dashboard actualiza las alertas |
| | | | | | 4 | Sin productos críticos | Dado que todos los productos tienen stock suficiente | Cuando se abre el Dashboard | El sistema no muestra alertas |
| | FR-003 | Como Gerente | Quiero consultar el stock disponible de los productos | Para conocer la disponibilidad actual. | 1 | Consultar stock | Dado que existen productos registrados | Cuando el usuario consulta un producto | El sistema muestra el stock actual |
| | | | | | 2 | Actualización tras venta | Dado que se realizó una venta | Cuando se consulta el producto | El stock refleja la reducción |
| | | | | | 3 | Actualización tras compra | Dado que se registró una compra | Cuando se consulta el producto | El stock refleja el incremento |
| | | | | | 4 | Producto sin stock | Dado que el producto tiene stock 0 | Cuando se consulta | El sistema muestra "Sin stock" |
| | FR-004 | Como Gerente | Quiero registrar movimientos de entrada y salida | Para mantener control del inventario. | 1 | Registrar entrada | Dado que llega mercadería | Cuando se registra una compra | El sistema almacena el movimiento |
| | | | | | 2 | Registrar salida | Dado que se realiza una venta | Cuando se confirma la venta | El sistema registra la salida |
| | | | | | 3 | Consultar historial | Dado que existen movimientos registrados | Cuando se solicita el historial | El sistema muestra los registros |
| | | | | | 4 | Movimiento inválido | Dado que faltan datos obligatorios | Cuando se intenta registrar | El sistema muestra un mensaje de error |
| | FR-005 | Como Gerente | Quiero identificar productos sin movimientos | Para evaluar productos de baja actividad. | 1 | Consultar productos sin movimiento | Dado que existen productos sin actividad | Cuando se genera el reporte | El sistema muestra el listado |
| | | | | | 2 | Filtrar por fecha | Dado que existe un rango seleccionado | Cuando se consulta | El sistema filtra correctamente |
| | | | | | 3 | Exportar reporte | Dado que existe información | Cuando se exporta | El sistema genera el reporte |
| | | | | | 4 | Sin resultados | Dado que todos tuvieron movimientos | Cuando se consulta | El sistema informa que no existen registros |
| | FR-006 | Como Gerente | Quiero identificar productos más vendidos | Para conocer la demanda del negocio. | 1 | Generar ranking | Dado que existen ventas registradas | Cuando se consulta | El sistema genera el ranking |
| | | | | | 2 | Ordenar resultados | Dado que existen varios productos | Cuando se visualiza el reporte | El sistema los ordena por ventas |
| | | | | | 3 | Filtrar por período | Dado que existe un rango de fechas | Cuando se consulta | El sistema muestra el resultado filtrado |
| | | | | | 4 | Sin ventas | Dado que no existen ventas | Cuando se consulta | El sistema muestra un mensaje informativo |
| | FR-007 | Como gerente | Quiero recibir alertas de productos próximos a vencer | Con la finalidad de evitar pérdidas económicas por vencimiento de productos. | 1 | Visualizar productos próximos a vencer | En caso que existan productos con fecha de vencimiento cercana | Cuando ingreso al módulo de inventario | El sistema muestra una alerta de productos próximos a vencer. |
| | | | | | 2 | Filtrar productos por fecha de vencimiento | En caso que existan varios productos registrados | Cuando selecciono un rango de fechas | El sistema muestra únicamente los productos dentro del rango seleccionado |
| | | | | | 3 | Consultar detalle del producto | En caso que exista una alerta generada | Cuando selecciono un producto de la lista | El sistema muestra el detalle completo del producto. |
| | | | | | 4 | Actualizar estado de alerta | En caso que el producto haya sido retirado o vendido | Cuando se actualiza el inventario | El sistema actualiza automáticamente la alerta correspondiente. |
| | FR-008 | Como gerente | Quiero registrar compras realizadas a proveedores | Con la finalidad de mantener abastecido el inventario. | 1 | Registrar compra | En caso que exista un proveedor registrado | Cuando ingreso una nueva compra | El sistema registra la compra correctamente. |
| | | | | | 2 | Asociar proveedor | En caso que se registre una compra | Cuando selecciono un proveedor | El sistema asocia la compra al proveedor elegido. |
| | | | | | 3 | Validar datos obligatorios | En caso que falten datos de compra | Cuando intento guardar el registro | El sistema muestra un mensaje de validación. |
| | | | | | 4 | Confirmar registro | En caso que los datos sean válidos | Cuando guardo la compra | El sistema confirma el registro exitosamente. |
| | FR-009 | Como gerente | Quiero actualizar automáticamente el stock al registrar compras | Con la finalidad de mantener actualizado el inventario. | 1 | Incrementar stock | En caso que se registre una compra | Cuando se confirma la compra | El sistema incrementa automáticamente el stock. |
| | | | | | 2 | Consultar stock actualizado | En caso que la compra haya sido registrada | Cuando consulto el producto | El sistema muestra el stock actualizado. |
| | | | | | 3 | Registrar historial | En caso que se modifique el stock | Cuando finaliza la compra | El sistema registra el movimiento correspondiente. |
| | | | | | 4 | Actualización múltiple | En caso que la compra incluya varios productos | Cuando se guarda la compra | El sistema actualiza el stock de todos los productos. |
| | FR-010 | Como gerente | Quiero consultar el historial de compras realizadas a proveedores | Con la finalidad de controlar el abastecimiento del negocio. | 1 | Visualizar historial | En caso que existan compras registradas | Cuando accedo al historial | El sistema muestra todas las compras registradas. |
| | | | | | 2 | Buscar por proveedor | En caso que existan varios proveedores | Cuando selecciono un proveedor | El sistema filtra las compras asociadas. |
| | | | | | 3 | Buscar por fecha | En caso que existan registros históricos | Cuando selecciono un rango de fechas | El sistema muestra las compras correspondientes. |
| | | | | | 4 | Consultar detalle | En caso que exista una compra registrada | Cuando selecciono una compra | El sistema muestra el detalle completo. |
| | FR-011 | Como gerente | Quiero registrar proveedores | Con la finalidad de gestionar el abastecimiento del minimarket. | 1 | Registrar proveedor | En caso que el proveedor sea nuevo | Cuando ingreso sus datos | El sistema registra al proveedor. |
| | | | | | 2 | Validar RUC | En caso que se registre un proveedor | Cuando ingreso el RUC | El sistema valida el formato. |
| | | | | | 3 | Evitar duplicados | En caso que el proveedor ya exista | Cuando intento registrarlo nuevamente | El sistema muestra un mensaje de advertencia. |
| | | | | | 4 | Confirmar registro | En caso que los datos sean correctos | Cuando guardo el proveedor | El sistema confirma el registro. |
| | FR-012 | Como gerente | Quiero actualizar información de proveedores | Con la finalidad de mantener información actualizada. | 1 | Modificar datos | En caso que exista un proveedor registrado | Cuando edito su información | El sistema guarda los cambios. |
| | | | | | 2 | Validar información | En caso que existan datos inválidos | Cuando intento actualizar | El sistema muestra una advertencia. |
| | | | | | 3 | Consultar cambios | En caso que se haya actualizado información | Cuando consulto el proveedor | El sistema muestra los datos actualizados. |
| | | | | | 4 | Confirmar actualización | En caso que la modificación sea válida | Cuando guardo cambios | El sistema confirma la actualización. |
| | FR-013 | Como gerente | Quiero consultar la lista de proveedores registrados | Con la finalidad de administrar mis proveedores. | 1 | Mostrar proveedores | En caso que existan proveedores registrados | Cuando ingreso al módulo | El sistema muestra la lista completa. |
| | | | | | 2 | Buscar proveedor | En caso que existan varios proveedores | Cuando ingreso un criterio de búsqueda | El sistema filtra los resultados. |
| | | | | | 3 | Ordenar proveedores | En caso que existan múltiples registros | Cuando selecciono un criterio de ordenamiento | El sistema ordena la información. |
| | | | | | 4 | Consultar detalle | En caso que exista un proveedor seleccionado | Cuando abro su registro | El sistema muestra la información completa. |
| | FR-014 | Como Vendedor | Quiero registrar devoluciones de productos | Con la finalidad de corregir ventas y actualizar el inventario | 1 | Registrar devolución | En caso que exista una venta previa | Cuando registro una devolución | : El sistema almacena la devolución. |
| | | | | | 2 | Actualizar stock | En caso que la devolución sea válida | Cuando se confirma la devolución | El sistema incrementa el stock. |
| | | | | | 3 | Consultar historial | En caso que existan devoluciones registradas | Cuando consulto el historial | El sistema muestra las devoluciones. |
| | | | | | 4 | Validar venta existente | En caso que no exista la venta | Cuando intento registrar la devolución | El sistema rechaza la operación. |
| | FR-015 | Como Vendedor | Quiero registrar diferentes métodos de pago | Con la finalidad de brindar mayor flexibilidad en las ventas | 1 | Seleccionar método de pago | En caso que se realice una venta | Cuando selecciono el método de pago | El sistema registra el método elegido |
| | | | | | 2 | Registrar pago en efectivo | En caso que el cliente pague en efectivo | Cuando finalizo la venta | El sistema almacena la información |
| | | | | | 3 | Registrar pago digital | En caso que el cliente utilice transferencia o tarjeta | Cuando confirmo la venta | El sistema registra el método correspondiente |
| | | | | | 4 | Consultar método registrado | En caso que exista una venta registrada | Cuando consulto la venta | El sistema muestra el método de pago utilizado |
| | FR-016 | Como gerente | Quiero consultar el historial de compras de cada cliente | Con la finalidad de conocer sus hábitos de compra. | 1 | Visualizar historial completo | En caso que existan compras registradas del cliente | Cuando accedo al historial del cliente | El sistema muestra todas las compras realizadas. |
| | | | | | 2 | Buscar compras por fecha | En caso que existan registros históricos | Cuando selecciono un rango de fechas | El sistema muestra las compras correspondientes al período. |
| | | | | | 3 | Consultar detalle de compra | En caso que exista una compra registrada | Cuando selecciono una compra del historial | El sistema muestra el detalle completo de la compra. |
| | | | | | 4 | Filtrar compras por cliente | En caso que existan varios clientes registrados | Cuando selecciono un cliente específico | El sistema filtra y muestra únicamente las compras de ese cliente. |
| | FR-017 | Como gerente | Quiero generar reportes de productos más vendidos | Con la finalidad de identificar los productos con mayor demanda. | 1 | Generar reporte general | En caso que existan ventas registradas | Cuando accedo al módulo de reportes | El sistema genera el reporte de productos más vendidos |
| | | | | | 2 | Filtrar por fechas | En caso que se quiera analizar un período específico | Cuando selecciono un rango de fechas | El sistema muestra el reporte filtrado por el período indicado. |
| | | | | | 3 | Ordenar por cantidad vendida | En caso que existan múltiples productos en el reporte | Cuando selecciono el criterio de ordenamiento | El sistema ordena los productos de mayor a menor cantidad vendida. |
| | | | | | 4 | Exportar reporte | En caso que el reporte haya sido generado | Cuando selecciono la opción de exportar | El sistema exporta el reporte en el formato seleccionado. |
| | FR-018 | Como gerente | Quiero generar reportes de productos con stock bajo | Con la finalidad de planificar reposiciones. | 1 | Generar reporte | En caso que existan productos con stock por debajo del mínimo | Cuando accedo al módulo de reportes | El sistema genera el reporte de productos con stock bajo. |
| | | | | | 2 | Filtrar por categoría | En caso que existan productos de distintas categorías | Cuando selecciono una categoría | El sistema muestra únicamente los productos de esa categoría con stock bajo. |
| | | | | | 3 | Visualizar stock mínimo | En caso que el reporte haya sido generado | Cuando consulto un producto del reporte | El sistema muestra el stock actual y el stock mínimo configurado. |
| | | | | | 4 | Exportar reporte | En caso que el reporte haya sido generado | Cuando selecciono la opción de exportar | El sistema exporta el reporte en el formato seleccionado. |
| | FR-019 | Como gerente | Quiero generar reportes de ganancias por período | Con la finalidad de evaluar el desempeño del negocio. | 1 | Consultar ganancias diarias | En caso que existan ventas registradas en el día | Cuando accedo al reporte diario | El sistema muestra las ganancias del día actual. |
| | | | | | 2 | Consultar ganancias mensuales | En caso que existan ventas registradas en el mes | Cuando selecciono el reporte mensual | El sistema muestra las ganancias del mes seleccionado. |
| | | | | | 3 | Filtrar por rango de fechas | En caso que se requiera un período personalizado | Cuando ingreso un rango de fechas | El sistema muestra las ganancias correspondientes al período. |
| | | | | | 4 | Exportar reporte | En caso que el reporte haya sido generado | Cuando selecciono la opción de exportar | El sistema exporta el reporte en el formato seleccionado. |
| | FR-020 | Como gerente | Quiero registrar una bitácora de acciones realizadas por los usuarios | Con la finalidad de auditar el uso del sistema. | 1 | Registrar inicio de sesión | En caso que un usuario acceda al sistema | Cuando inicia sesión | El sistema registra fecha, hora y usuario. |
| | | | | | 2 | Registrar modificación | En caso que un usuario modifique información | Cuando guarda cambios | El sistema registra la acción realizada. |
| | | | | | 3 | Consultar bitácora | En caso que existan registros almacenados | Cuando accedo al módulo de auditoría | El sistema muestra el historial de acciones. |
| | | | | | 4 | Filtrar registros | En caso que existan múltiples acciones registradas | Cuando selecciono filtros de búsqueda | El sistema muestra únicamente los registros solicitados. |
| | FR-021 | Como vendedor | Quiero visualizar los productos mas comprados por un cliente | Para conocer sus preferencias de compra | 1 | Consultar compras de cliente | Dado que existen compras registradas de clientes | Cuando selecciono Cliente | El sistema muestra historial de compras |
| | | | | | 2 | Mostrar productos mas comprados | Dado que el cliente tiene compras registradas | Cuando consulto su historial | El sistema muestra los productos comprados |
| | | | | | 3 | Filtrar por periodo | Dado que exisren compras en diferentes fechas | Cuando selecciono un rango de fechas | El sistema muestra unicamente las compras del periodo |
| | | | | | 4 | Cliente sin compras | Dado que el cliente no tiene compras registradas | Cuando consulto su historial | El sistema envia un mensaje de "Sin compras registradas" |
| | FR-022 | como vendedor | Quiero buscar productos por código y nombre | Para consultar rápidamente su existencia en inventario | 1 | Buscar producto por nombre | Dado que existen productos registrados | Cuando ingreso el nombre del producto | El sistema muestra el producto correspondiente |
| | | | | | 2 | Buscar producto por código | Dado que existen los productos registrados | Cuando ingreso el codigo del producto | El sistema muestra el producto del código |
| | | | | | 3 | Consultar su stock encontrado | Dado que el producto existe | Cuando selecciono un producto de la lista | El sistema muestra el stock disponible |
| | | | | | 4 | Producto no encontrado | Dado que no existe un producto con los datos ingresados | Cuando realizo la búsqueda | El sistema envia un mensaje de "Sin productos coincidentes" |
| | FR-023 | Como vendedor | Quiero aplicar un descuento manual al total de la venta | Para otorgar rebajas especiales a clientes autorizados | 1 | Aplicar descuento | Dado que se ingresa un porcentaje válido | Cuando se presiona aplicar | El sistema reduce el monto total de la venta |
| | | | | | 2 | Descuento excesivo | Dado que se digita un monto mayor al límite | Cuando se intente aplicar | El sistema bloquea la acción y muestra un error |
| | | | | | 3 | Formato inválido | Dado que se ingresan letras en el descuento | Cuando se presiona aplicar | El sistema solicita ingresar un valor numérico |
| | | | | | 4 | Quitar descuento | Dado que se decida remover la rebaja | Cuando se limpie el descuento | El sistema restablece el valor original de la venta |
| | FR-024 | Como Gerente | Quiero registrar la asistencia de los empleados | Para supervisar las horas laboradas por el personal | 1 | Marcar ingreso | Dado que el empleado inicia su jornada | Cuando se ingresa el DNI | El sistema guarda la hora de entrada |
| | | | | | 2 | Marcar salida | Dado que el empleado finaliza su turno | Cuando se confirma el código | El sistema actualiza el registro de salida |
| | | | | | 3 | DNI no registrado | Dado que el documento no existe | Cuando se intenta guardar la asistencia | El sistema bloquea la acción y muestra un error |
| | | | | | 4 | Marca duplicada | Dado que la entrada ya fue registrada hoy | Cuando se intenta volver a guardar | El sistema impide el registro y envía un mensaje |
| | | INTEGRANTES: | | | | | | | |
| | | Alfaro Villanueva Cristhian Alfaro | | | | | | | |
| | | Alvaro Valderrama Carlos Alberto | | | | | | | |
| | | Venegas Aguirre José David | | | | | | | |
| | | Guitierrez Rodriguez Sebastián | | | | | | | |
| | | Colonia Infantes Walter | | | | | | | |

# Instructivo

| Unnamed: 0 | Historias de usuario y criterios de aceptación: Instructivo | Unnamed: 2 |
|:-------------|:--------------------------------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| | Columna | Instrucciones |
| | Identificador (ID) de la historia | Código que identifica unívocamente a la historia en el Proyecto que se esté desarrollando. El formato debe ser elegido por el equipo. |
| | Rol | Es el rol que está desempeñando el usuario cuando utiliza la funcionalidad que se está describiendo. Debe ser lo más especifico posible, describiendo el rol o actor que se está desempeñando. El enunciado puede escribirse como se sigue: Yo como un [Rol], Desempeñando el rol de [Rol], Como un [Rol], entre otros. Por ejemplo: |
| | | |
| | | Yo como cliente registrado. |
| | | Desempeñando el rol de cliente registrado. |
| | | Como un cliente registrado. |
| | Característica / Funcionalidad | Representa la función que el rol quiere o necesita hacer en el sistema que se está desarrollando. Puede diferenciarse entre acciones obligatorias u opcionales, utilizando la palabra puede o necesita para describir la acción. Por ejemplo: |
| | | |
| | | Necesito realizar búsquedas de productos por categorías. |
| | | Puedo seleccionar una categoría para ver el número de productos que tiene asociado. |
| | Razón / Resultado | Lo que el rol necesita lograr al ejecutar la acción. Este es el resultado de ejecutar la acción desde el punto de vista del rol. Este punto puede ser opcional, pues la historia puede documentarse sólo con la definición del rol y la acción (sin definir la consecuencia). |
| | Número (#) de Escenario | Número (ejemplo 1, 2, 3 ó 4), que identifica al escenario asociado a la historia. |
| | Criterio de Aceptación (Título) | Describe el contexto del escenario que define un comportamiento. Por ejemplo, si se toma el ejemplo de búsquedas de productos por categoría, un posible ejemplo pudiera ser: Categoría sin productos asociados. |
| | Contexto | Proporciona mayor descripción sobre las condiciones que desencadenan el escenario. |
| | Evento | Representa la acción que el usuario ejecuta, en el contexto definido para el escenario. |
| | Resultado / Comportamiento esperado | Dado el contexto y la acción ejecutada por el usuario, la consecuencia es el comportamiento del sistema en esa situación. |

# Ejemplo

| Unnamed: 0 | Historias de usuario y criterios de aceptación: Ejemplo | Unnamed: 2 | Unnamed: 3 | Unnamed: 4 | Unnamed: 5 | Unnamed: 6 | Unnamed: 7 | Unnamed: 8 | Unnamed: 9 |
|:-------------|:----------------------------------------------------------|:-------------------------|:--------------------------------------------------------------------------------------|:--------------------------------------------------------------------|:------------------------|:------------------------------------|:---------------------------------------------------------------|:-------------------------------------------------------------|:----------------------------------------------------------------------------------------------------------------------|
| | | | | | | | | | |
| | | | | | | | | | |
| | | Enunciado de la Historia | | | | Criterios de Aceptación | | | |
| | Identificador (ID) de la Historia | Rol | Característica / Funcionalidad | Razón / Resultado | Número (#) de Escenario | Criterio de Aceptación (Título) | Contexto | Evento | Resultado / Comportamiento esperado |
| | XX-XXXX-XXXX | Como un Cliente. | Necesito ver un listado de categorías de productos y poder seleccionar una categoría. | Con la finalidad de realizar busquedas de productos por categorías. | 1 | Categoría con al menos un producto. | En caso que una categoría tenga al menos un producto asociado. | Cuando se despliegue el listado de categorías a seleccionar. | A continuación del nombre de la categoría, se mostrará entre paréntesis el número de productos asociados. |
| | | | | | 2 | Categoría sin productos. | En caso que una categoría no tenga productos asociados. | Cuando se despliegue el listado de categorías a seleccionar. | A continuación del nombre de la categoría, se mostrará entre paréntesis el siguiente texto "Sin Productos asociados". |
| | | | | | 3 | Ordenamiento de las categorías | | Cuando se despliegue el listado de categorías a seleccionar. | El sistema mostrará las categorías en orden alfabetico. |


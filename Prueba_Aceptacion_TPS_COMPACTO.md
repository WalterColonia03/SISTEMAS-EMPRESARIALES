# Prueba de Aceptación

| Desarrollo ágil: Prueba de Aceptación por Historias de usuario | Unnamed: 1 | Unnamed: 2 | Unnamed: 3 | Unnamed: 4 | Unnamed: 5 | Unnamed: 6 | Unnamed: 7 |
|:-----------------------------------------------------------------|:---------------------------------------------|:------------------------------------------------------------|:-------------------------------------------------------------------------|:------------------------|:------------------------------------|:----------------------------------------------------------------------------------|:--------------------------------------------|
| | | | | | | | |
| | | | | | | | |
| | Prueba de Aceptación por Historia de Usuario | | | Criterios de Aceptación | | | |
| Identificador (ID) de la Historia | Rol | Característica / Funcionalidad | Razón / Resultado | Número (#) de Escenario | Criterio de Aceptación (Título) | Resultado / Comportamiento esperado | |
| 1000001 | Como vendedor | Registrar ventas de cada producto | Control ordenado de las transacciones | 1 | Registrar ventas | El sistema registra la venta y genera el monto a pagar | CORREGIR CANTIDAD |
| | | | | 2 | Calculo automatico | El sistema calcula automaticamente el total | |
| | | | | 3 | Emision de comprobante | El sistema procesa el pago y genera el recibo o factura correspondiente | |
| | | | | 4 | Actualizacion de stock | El sistema descuenta automaticamente los productos vendidos del inventario | |
| | | | | | | | |
| 1000002 | Como Administrador | Registrar nuevos productos | Tener el inventario actualizado | 1 | Registrar producto | El sistema lo guarda | |
| | | | | 2 | Error de registro del producto | El sistema te muestra error | |
| | | | | 3 | Producto existente | El sistema te muestra como producto duplicado | |
| | | | | 4 | Registro completado | El sistema confirma el registro del producto | |
| | | | | | | | |
| 1000003 | Como Administrador | Ver reportes de ventas | Analizar el rendimiento del negocio | 1 | Visualizar reporte de ventas | El sistema muestra la lista de ventas | FALTA CAMPO ID DEL PRODUCTO |
| | | | | 2 | Exportar reporte a PDF | El sistema genera un archivo PDF con los datos | |
| | | | | 3 | Abrir archivo PDF | El sistema se abre el documento con la informacion de ventas | |
| | | | | 4 | Reporte sin datos | El sistema muestra mensaje informativo | |
| | | | | | | | |
| 1000004 | Como Administrador | Eliminar productos | Mantener el inventario limpio Y actualizado | 1 | Eliminación exitosa | El sistema lo borra del archivo | |
| | | | | 2 | Confirmación de seguridad | El sistema muestra mensaje de confirmación | |
| | | | | 3 | Notificacion de eliminacion exitosa | El sistema muestra un mensaje emergente confirmando que el producto fue eliminado | |
| | | | | 4 | Cancelación de acción | El sistema mantiene el producto intacto | |
| | | | | | | | |
| 1000005 | Como Administrador | Necestio ver el resgitro de ventas | Consultar y exportar ventas mesuales | 1 | Buscar ventas por mes | El sistema muestra las ventas registradas según el mes seleccionado | FALTA RANGO ENTRE FECHAS |
| | | | | 2 | No existen ventas registradas | El sistema muestra un mensaje indicando que no hay ventas disponibles | |
| | | | | 3 | Exportar reportes de ventas | El sistema genera automáticamente el reporte en PDF | |
| | | | | 4 | Exportación completa | El sistema confirma la exportación y abre el archivo PDF generad | |
| | | | | | | | |
| 1000006 | Como vendedor | Necesito registrar cliente | Con la finalidad de llevar control de compradores | 1 | Registrar cliente | El sistema guarda la información | |
| | | | | 2 | Datos incompletos | El sistema muestra error | |
| | | | | 3 | Cliente existente | El sistema alertar duplicado | |
| | | | | 4 | Registro exitoso | El sistema confirme el registro | |
| | | | | | | | |
| 1000007 | Como Administrador | Necesito agregar categoria de productos | Con la finalidad de tener una mejor organizacion del inventario | 1 | Ingrese una nueva categoria | El sistema registra la categoria correctamente | |
| | | | | 2 | Datos Incompletos | El sistema muestra un mensaje | |
| | | | | 3 | Categoria existente | El sistema rechaza el registro | |
| | | | | 4 | Registro existoso | El sistema confirma la operacion | |
| | | | | | | | |
| 1000008 | Como Administrador | Necesito administrar categorias | Con la finalidad de matener actulizada la clasificacion de los productos | 1 | Actualizar nombre de categoria | Eln sistema actuliza la informacion correctamente | |
| | | | | 2 | Validacion de descripcion vacia | El sistema muestra un mensaje de error solicitando ingresar una descripción | |
| | | | | 3 | Eliminar categoria | El sistema elimina la categoria | |
| | | | | 4 | Confirmacion de eliminacion | El sistema elimina la categoria correctamente | |
| | | | | | | | |
| 1000009 | Como Administrador | Necesito registrar nuevos usuarios en el sistema | Para controlar quien tiene acceso al sistema | 1 | Registro correcto | El sistema registra al usuario correctamente | |
| | | | | 2 | Validacion de datos | El sisrema muestra un mensaje | |
| | | | | 3 | Usuario duplicado | El sistema muestra que ya existe ese usuario | |
| | | | | 4 | Asignacion de rol | el sistema asigna el rol correctamente | |
| | | | | | | | |
| 1000010 | Como Administrador | Necesito gestionar los usuarios en el sistema | Para administrar permisos y tambien eliminar usuarios | 1 | Editar datos de usuario | El sistema actualiza la informacion | |
| | | | | 2 | Cambiar roles | El sistema actiliza los permisos | |
| | | | | 3 | Validacion de acceso | El sistema oculta ese menu | |
| | | | | 4 | Confirmacion de acciones | El sistema ejecuta la solicitud | |
| | | | | | | | |
| 1000011 | Como Vendedor | Necesito administrar los clientes registrados en el sistema | Para administrar datos del cliente | 1 | Seleccionar clientes | El sistema permite seleccionar un cliente para editar o eliminar | |
| | | | | 2 | Editar datos del cliente | El sistema actuliaza la informacion | |
| | | | | 3 | Eliminar cliente | El sistema elimina al cliente | |
| | | | | 4 | Visualizar lista de clientes | El sistema muestra la tabla de los clientes | |
| | | | | | | | |
| 1000012 | Como Administrador | ver reportes de clientes | analizar su comportamiento y tomar decisiones | 1 | Visualizar reportes de clientes | El sistema muestra la lista de clientes | VER MOVIMIENTOS DEL CLIENTE |
| | | | | 2 | Exportar reporte a PDF | El sistema genera un archivo PDF con los datos | |
| | | | | 3 | Abrir archivo PDF | El sistema abre el documento con la informacion de clientes | |
| | | | | 4 | Reporte sin datos | El sistema muestra mensaje informativo | |
| | | | | | | | |
| 1000013 | Como Administrador | Ver reportes de categorias | Visualizar las categorias registradas | 1 | Visualizar reporte de categorias | El sistema muestra la lista de categorias | |
| | | | | 2 | Exportar reporte a PDF | El sistema genera un archivo PDF con las categorias | |
| | | | | 3 | Abrir archivo PDF | El sistema abre el documento con la informacion de categorias | |
| | | | | 4 | Reporte sin datos | El sistema muestra un mensaje informativo | |
| | | | | | | | |
| 1000014 | Como Administrador | Ver reportes de productos | Corregir errores en las transacciones | 1 | Visualizar reporte de productos | El sistema muestra la lista de productos | CORREGIR DESCRIPCION |
| | | | | 2 | Exportar reporte a PDF | El sistema genera un archivo PDF con los datos | |
| | | | | 3 | Abrir archivo en PDF | El sistema abre el documeto con la informacion de productos | |
| | | | | 4 | Reporte sin datos | El sistema muestra un mensaje informativo | |
| | | | | | | | |
| 1000015 | Como Administrador | Necesito administrar productos | Mantener actualizado y corregido el inventario | 1 | Editar producto | El sistema actualiza la informacion del producto | CORREGIR ERRORES EN NUMERACION DE PRODUCTOS |
| | | | | 2 | Eliminar producto | El sistema elimina el producto del sistema | |
| | | | | 3 | Visualizar productos | El sistema muestra la lista de productos | |
| | | | | 4 | Actulizar Stock de producto | El sistema actualiza el stock del producto | |


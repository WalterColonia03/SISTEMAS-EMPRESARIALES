# Historias de Usuario

| Unnamed: 0 | Desarrollo ágil: Historias de usuario y criterios de aceptación | Unnamed: 2 | Unnamed: 3 | Unnamed: 4 | Unnamed: 5 | Unnamed: 6 | Unnamed: 7 | Unnamed: 8 | Unnamed: 9 | Unnamed: 10 |
|:-------------|:------------------------------------------------------------------|:-------------------------|:----------------------------------------------------------|:-------------------------------------------------------------------------|:------------------------|:---------------------------------|:--------------------------------------------------------|:----------------------------------------------------------|:--------------------------------------------------------------|:--------------|
| | | | | | | | | | | |
| | | | | | | | | | | |
| | | Enunciado de la Historia | | | | Criterios de Aceptación | | | | |
| | Identificador (ID) de la Historia | Rol | Característica / Funcionalidad | Razón / Resultado | Número (#) de Escenario | Criterio de Aceptación (Título) | Contexto | Evento | Resultado / Comportamiento esperado | |
| | 1000001 | Como vendedor | Registrar ventas de cada producto | Control ordenado de las transacciones | 1 | Registrar ventas | En caso que el sistema este disponible | Cuando el cliente seleccione producto y confirme la venta | El sistema registra la venta y genera el monto a pagar | |
| | | | | | 2 | Calculo automatico | En caso que haya producto en la lista | Cuando se agreguen prodcutos | El sistema calcula automaticamente el total | |
| | | | | | 3 | usuario repetido | En caso que la venta se complete | Cuando el usuario finalice la operación | El sistema genera un comprobante | |
| | | | | | 4 | Asignar rol | En caso que el producto no exista | Cuando se intente registrar | El sistema muestra un mensaje de error | C |
| | | | | | | | | | | |
| | 1000002 | Como Administrador | Registrar nuevos productos | Tener el inventario actualizado | 1 | Registrar producto | En caso que el gerente ingrese datos | Cuando registre producto | El sistema lo guarda | |
| | | | | | 2 | Error de registro del producto | En caso de que falten datos | Cuando el nuevo producto se intente guardar | El sistema te muestra error | |
| | | | | | 3 | Mismo producto | En caso el producto ya exista | Cuando se vuelva a registrar | El sistema te muestra como producto duplicado | |
| | | | | | 4 | Registro completado | En caso que todo este correcto | Cuando todos los datos esten completos | El sistema confirma el registro del producto | |
| | | | | | | | | | | |
| | 1000003 | Como Administrador | Ver reportes de ventas | Analizar el rendimiento del negocio | 1 | Visualizar reporte de ventas | En caso que existan ventas registradas | Cuando el administrador acceda el módulo de reportes | El sistema muestra la lista de ventas | |
| | | | | | 2 | Exportar reporte a PDF | En caso que existan datos de ventas | Cuando el administrador seleccione exportar | El sistema genera un archivo PDF con los datos | |
| | | | | | 3 | Abrir archivo PDF | En caso que el archivo haya sido generado | Cuando el administrador acceda el archivo | El sistema se abre el documento con la informacion de ventas | |
| | | | | | 4 | Reporte sin datos | En caso de que no haya ventas registradas | Cuando se intente generar el reporte | El sistema muestra mensaje informativo | |
| | | | | | | | | | | |
| | 1000004 | Como Administrador | Eliminar productos | Mantener el inventario limpio Y actualizado | 1 | Eliminación exitosa | En caso que el producto exista | Cuando se seleccione eliminar | El sistema lo borra de la base de datos | |
| | | | | | 2 | Confirmación de seguridad | En caso que se intente borrar | Cuando se haga clic | El sistema muestra mensaje de confirmación | |
| | | | | | 3 | Notificacion de eliminacion | En caso que el producto se elimine | Cuando se elimine el producto seleccionado | El sistema muestra un mensaje | |
| | | | | | 4 | Cancelación de acción | En caso que se decida no borrar | Cuando se elija "No" | El sistema mantiene el producto intacto | |
| | | | | | | | | | | |
| | 1000005 | Como vendedor | Iniciar sesion en el sistema | Acceder a las funciones del sistemas | 1 | Acceso correcto | En caso que los datos sean correctos | Cuando el usuario inicie sesión | El sistema permite el acceso | |
| | | | | | 2 | Datos incorrectos | En caso que las credenciales sean invalidas | Cuando se intente ingresar | El sistema muestre error | |
| | | | | | 3 | Usuario inexistente | En caso que el usuario no este registrado | Cuando intente ingresar | El sistema rechaza acceso | |
| | | | | | 4 | Cierre de sesion | En caso que el usauario cierre sesión | Cuando cierre sesión | El sistema bloquea acceso | |
| | | | | | | | | | | |
| | 1000006 | Como Vendedor | Realizar una nueva venta | Con la finalidad de completar ventas correctamente | 1 | Registrar venta | En caso que el cliente realice la compra | Cuando el vededor registre los productos y el pago | El sistema registra la venta correctamente | |
| | | | | | 2 | Generar boleta en PDF | En caso que la venta haya sido registrada | Cuando se complete el registro de la venta | El sistema genera automaticamente la boleta PDF | |
| | | | | | 3 | Eliminar producto seleccionado | En caso que exista un producto agregado a la venta | Cuando el vendedor seleccione eliminar el producto | El sistema elimina el producto de la lista de venta | |
| | | | | | 4 | Calcular total de venta | En caso que existan productos agregados | Cuando el vendedor finalice el registro de productos | El sistema muestra el monto total de la venta | |
| | | | | | | | | | | |
| | 1000007 | Como vendedor | Necesito registrar cliente | Con la finalidad de llevar control de compradores | 1 | Registrar cliente | En caso que se ingresen datos | Cuando se registre | El sistema guarda la información | |
| | | | | | 2 | Datos incompletos | En caso que falten datos | Cuando se intenten registrar | El sistema muestra error | |
| | | | | | 3 | Cliente existente | En caso que el cliente exista | Cuando se intente registar el mismo DNI | El sistema alertar duplicado | |
| | | | | | 4 | Registro exitoso | En caso que los datos sean correctos | Cuando se complete | El sistema confirme el registro | |
| | | | | | | | | | | |
| | 1000008 | Como Administrador | Necesito agregar categoria de productos | Con la finalidad de tener una mejor organizacion del inventario | 1 | Ingrese una nueva categoria | En caso se ingrese una nueva categoria | cuando complete los datos requeridos | El sistema registra la categoria correctamente | |
| | | | | | 2 | Datos Incompletos | En caso que falten datos obligatorios | Cuando se intente guardar | El sistema muestra un mensaje | |
| | | | | | 3 | Categoria existente | En caso la categoria ya exista | Cuando se intente guardar el duplicado | El sistema rechaza el registro | |
| | | | | | 4 | Registro existoso | En caso correcto | Cuando se guade la categoria | El sistema confirma la opercion | |
| | | | | | | | | | | |
| | 1000009 | Como Administrador | Necesito gestionar categorias | Con la finalidad de matener actulizada la clasificacion de los productos | 1 | Actualizar nombre de categoria | En caso que la categroia | Cuando el gerente modifique el nombre | Eln sistema actuliza la informacion correctamente | |
| | | | | | 2 | Validación de nombre inválido | En caso que el nuevo nombre esta vacio | Cuando se intente guardar | El sistema muestra un mesaje de error | |
| | | | | | 3 | Eliminar categoria | En caso que la categoria exista | Cuando se confirme la eliminacion | El sistema elimina la categoria | |
| | | | | | 4 | Confirmacion de eliminacion | En caso que el usuario seleccione eliminar | Cuando el sistemaa solicite confirmacion | El sistema elimina la categoria correctamente | |
| | | | | | | | | | | |
| | 1000010 | como Administrador | Necesito registrar nuevos usuarios en el sistema | Para controlar quien tiene acceso al sistema | 1 | Registro correcto | En caso que se quiera acceder al modulo de usuarios | Cuando ingrese los datos del nuevo usuario | El sistema registra al usuario correctamente | |
| | | | | | 2 | Validacion de datos | En caso que falten datos obligatorios | Cuando se intente registrar | El sisrema muestra un mensaje | |
| | | | | | 3 | Usuario duplicado | En caso que el usuario ya exista | Cuando se intente registar con los mismos datos | El sistema muestra que ya existe ese usuario | |
| | | | | | 4 | Asignacion de rol | En caso que se cree un usuario | Cuando se seleccione el tipo de ususario | el sistema asigna el rol correctamente | |
| | | | | | | | | | | |
| | 1000011 | como Administrador | Necesito gestionar los usuarios en el sistema | Para administrar permisos y tambien eliminar usuarios | 1 | Editar datos de usuario | En caso que el usuario exista | Cuando se modifique sus datos | El sistema actualiza la informacion | |
| | | | | | 2 | Cambiar roles | En caso que se gestione un usuario | Cuando se seleccion su rol | El sistema actiliza los permisos | |
| | | | | | 3 | Validacion de acceso | En caso que otro usuario intente gestionar los usuarios | Cuando inicie sesion se validara el rol | El sistema oculta ese menu | |
| | | | | | 4 | Confirmacion de acciones | En caso de eliminacion o cambios importantes | Cuando se confirme la accion | El sistema ejecuta la solicitud | |
| | | | | | | | | | | |
| | 1000012 | como Vendedor | Necesito gestionar los clientes registrados en el sistema | Para administrar datos o anular la venta | 1 | Editar datos del cliente | En caso que el cliente exista | Cuando se modifiquen sus datos | El sistema actuliaza la informacion | |
| | | | | | 2 | Eliminar cliente | En caso que el cliente exista | Cuando se elimine el cliente | El sistema elimina al cliente | |
| | | | | | 3 | Visualizar lista de clientes | En caso que existan clientes resgitrados | Cuando se acceda al modulo de clientes | El sistema muestra la tabla de los clientes | |
| | | | | | 4 | Validar existencia de cliente | El cliente no esta registrado | cuando se intente buscar o editar | EL sistema muestra un mensaje de error | |
| | | | | | | | | | | |
| | 10000013 | Como Administrador | ver reportes de clientes | analizar su comportamiento y tomar decisiones | 1 | Visualizar reportes de clientes | En caso que existan clientes registrados | Cuando el administrador acceda al modulo de reportes | El sistema muestra la lista de clientes | |
| | | | | | 2 | Exportar reporte a PDF | En caso que existan datos de clientes | Cuando el administrador seleccion la opcion de exportar | El sistema genera un archivo PDF con los datos | |
| | | | | | 3 | Abrir archivo PDF | En caso que el archivo haya sido generado | Cuando el gerente acceda al archivo | El sistema abre el documento con la informacion de clientes | |
| | | | | | 4 | Reporte sin datos | No existen clientes registrados | Cuando se intente generar el reporte | El sistema muestra mensaje informativo | |
| | | | | | | | | | | |
| | 1000014 | como administrador | Ver reportes de categorias | Visualizar las categorias registradas | 1 | Visualizar reporte de categorias | En caso de que existan categorias registradas | Cuando el administrador acceda al modulo de reportes | El sistema muestra la lista de categorias | |
| | | | | | 2 | Exportar reporte a PDF | En caso se quiera documentar las categorias registradas | Cuando el administrador seleccione exportar | El sistema genera un archivo PDF con las categorias | |
| | | | | | 3 | Abrir archivo PDF | En caso que el archivo haya sido generado | Cuando el administrador accede al archivo | El sistema abre el documento con la informacion de categorias | |
| | | | | | 4 | Reporte sin datos | En caso de que no existan categorias registradas | Cuando se intente generar el reporte | El sistema muestra un mensaje informativo | |
| | | | | | | | | | | |
| | 1000015 | como administrador | Ver reportes de productos | Corregir errores en las transacciones | 1 | Visualizar reporte de productos | En caso de que existan productos registrados | Cuando el administrador acceda al modulo | El sistema muestra la lista de productos | |
| | | | | | 2 | Exportar reporte a PDF | En caso de datos de productos | Cuando el administrador seleccione exportar | El sistema genera un archivo PDF con los datos | |
| | | | | | 3 | Abrir archivo en Excel | En caso que el archivo haya sido generado | Cuando el administrador acceda al archivo | El sistema abre el documeto con la informacion de productos | |
| | | | | | 4 | Reporte sin datos | En caso que no exista productos registrados | Cuando se intente generar el reporte | El sistema muestra un mensaje informativo | |
| | | | | | | | | | | |
| | 1000016 | Como Administrador | Necesito gestionar productos | Mantener actualizado el inventario | 1 | Editar producto | En caso que el producto exista | Cuando el administrador modifique los datos | El sistema actualiza la informacion del producto | |
| | | | | | 2 | Eliminar producto | En caso que el producto exista | Cuando se seleccione eliminar | El sistema elimina el producto del sistema | |
| | | | | | 3 | Visualizar productos | En caso que existan productos | Cuando se acceda al modulo se productos | El sistema muestra la lista de productos | |
| | | | | | 4 | Actulizar Stock de producto | En caso que el producto exista | Cuando se modifique la cantidad disponible | El sistema actualiza el stock del producto | |
| | | | | | | | | | | |
| | 1000017 | | | | 1 | | | | | |
| | | | | | 2 | | | | | |
| | | | | | 3 | | | | | |
| | | | | | 4 | | | | | |
| | | | | | | | | | | |
| | 1000018 | | | | 1 | | | | | |
| | | | | | 2 | | | | | |
| | | | | | 3 | | | | | |
| | | | | | 4 | | | | | |
| | | | | | | | | | | |
| | 1000019 | | | | 1 | | | | | |
| | | | | | 2 | | | | | |
| | | | | | 3 | | | | | |
| | | | | | 4 | | | | | |
| | | | | | | | | | | |
| | 1000020 | | | | 1 | | | | | |
| | | | | | 2 | | | | | |
| | | | | | 3 | | | | | |
| | | | | | 4 | | | | | |
| | | | | | | | | | | |
| | | | | | | | | | | |
| | | | | | | | | | | |
| | | | | | | | | | | |
| | | | | | | | | | | |
| | | | | | | | | | | |
| | | | | | | | | | | |
| | | | | | | | | | | |
| | | | | | | | | | | |
| | | | | | | | | | | |
| | | | | | | | | | | |
| | | | | | | | | | | |
| | | | | | | | | | | |
| | | | | | | | | | | |
| | | | | | | | | | | |
| | | | | | | | | | | |
| | | | | | | | | | | |
| | | | | | | | | | | |
| | | | Integrantes: | | | | | | | |
| | | | Gutierrez Rodriguez Sebástian | | | | | | | |
| | | | Venegas Aguirre José David | | | | | | | |
| | | | Alberto Valderrama Carlos Alberto | | | | | | | |
| | | | Alfaro Villanueva Cristhian | | | | | | | |
| | | | Colonia Infantas Walter | | | | | | | |

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


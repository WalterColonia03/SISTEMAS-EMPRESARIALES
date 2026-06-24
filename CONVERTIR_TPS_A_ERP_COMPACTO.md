UNIVERSIDAD PRIVADA ANTENOR ORREGO

FACULTAD DE INGENIERÍA

ESCUELA PROFESIONAL DE INGENIERÍA DE SISTEMAS E INTELIGENCIA ARTIFICIAL

PROYECTO DE LABORATORIO

SISTEMAS EMPRESARIALES

Docente: Santa Cruz Damian Elias Enrique

Integrantes:

Alfaro Villanueva Cristhian Alejandro

Alvarado Valderrama Carlos Alberto

Venegas Aguirre José David

Gutierrez Rodriguez Sebástian

Colonia Infantas Walter

Ciclo: IV

2026-20

Resumen Ejecutivo

•Objetivo: Propósito principal de la implementación del ERP.

Implementar un sistema ERP (Enterprise Resource Planning) en el minimarket LAREDO con la finalidad de integrar y optimizar todos los procesos del negocio en una única plataforma centralizada: ventas, inventario, compras, finanzas, recursos humanos y gestión de clientes. El sistema busca automatizar las operaciones críticas del minimarket, eliminar el registro manual de transacciones, garantizar la integridad y disponibilidad de la información en tiempo real, y dotar al personal y a la gerencia de herramientas que permitan una toma de decisiones más ágil, precisa y basada en datos actualizados.

•Impacto esperado:

Reducción de costos operativos: La automatización del registro de ventas, control de inventario, generación de comprobantes electrónicos, cálculo de planilla y gestión de compras eliminará las tareas manuales repetitivas, reduciendo los errores y el tiempo invertido en operaciones administrativas.

Calidad y veracidad de la información: La validación automática de identidad por DNI y RUC garantiza que los datos de clientes y proveedores registrados en el sistema sean correctos y estén actualizados desde el momento de su ingreso, sin depender del llenado manual.

Centralización e integridad de la información: Todos los datos del negocio ventas, productos, clientes, proveedores, empleados y finanzas— estarán almacenados en un único repositorio centralizado, eliminando la duplicidad de información y garantizando la consistencia entre módulos.

Control preciso del inventario: El sistema actualizará el stock en tiempo real con cada venta, compra o ajuste, emitirá alertas automáticas al alcanzar el nivel mínimo de existencias y proporcionará el kardex completo de movimientos por producto.

Mejora en la experiencia del cliente: La incorporación de pagos digitales vía Mercado Pago, la emisión de boletas electrónicas válidas y el programa de fidelización con acumulación y canje de puntos agilizará el proceso de cobro, ampliará las opciones de pago y recompensará la lealtad de los clientes frecuentes.

Cumplimiento tributario: La generación de boletas electrónicas a través de un OSE autorizado sitúa al minimarket en el camino hacia el cumplimiento del proceso de masificación de la facturación electrónica impulsado por SUNAT.

Visibilidad financiera: El módulo de Finanzas permitirá conocer el flujo de caja, las cuentas por cobrar y pagar, el tipo de cambio oficial del día y los resultados del período, mejorando la capacidad de análisis gerencial.

Gestión eficiente del personal: El módulo de RRHH centralizará la información de los empleados, automatizará el cálculo de la planilla y facilitará el control de asistencia, vacaciones y evaluación de desempeño.

Toma de decisiones basada en datos: El Dashboard Gerencial presentará los indicadores clave del negocio en tiempo real, permitiendo a la gerencia identificar tendencias, detectar problemas operativos y tomar decisiones estratégicas con información actualizada.

2. Alcance del Proyecto

• Módulos incluidos

El ERP implementará los siguientes módulos para cubrir los procesos integrales del minimarket LAREDO:

Dashboard Gerencial: Pantalla principal con indicadores clave del negocio en tiempo real: ventas del día, stock crítico, KPIs financieros, tipo de cambio oficial y resumen del programa de fidelización.

Finanzas y Contabilidad:Libro mayor con asientos automáticos, cuentas por cobrar y pagar, flujo de caja, tipo de cambio SUNAT, conciliación de métodos de pago e informes financieros exportables.

Ventas, Gestión de Clientes y Fidelización (CRM): Ciclo comercial completo con validación de identidad por DNI, generación de boletas electrónicas vía OSE autorizado, pagos digitales con Mercado Pago, gestión de prospectos, cotizaciones, pedidos, historial de clientes y programa de fidelización con puntos..

Gestión de Inventario y Almacén: Control de stock en tiempo real, kardex automático de movimientos, alertas de reposición y gestión de lotes con fecha de vencimiento.

Compras y Aprovisionamiento: Gestión de proveedores con validación de RUC, órdenes de compra con flujo de aprobación, recepción de mercadería y control de cuentas por pagar.

Recursos Humanos (RRHH): Ficha de empleados, control de asistencia, gestión de vacaciones, evaluación de desempeño y cálculo automatizado de planilla con generación de boletas de pago..

Seguridad y Administración de Usuarios: perfiles de usuario con permisos por rol, control de acceso, historial de auditoría y protección de credenciales..

Módulos excluidos

Para mantener un alcance controlado y asegurar una implementación exitosa en esta primera fase, no se incluirán los siguientes módulos:

Facturación electrónica en producción registrada ante SUNAT:El sistema genera boletas electrónicas a través de un OSE autorizado en su entorno de validación, cubriendo el ciclo completo de emisión, firma y obtención del PDF. La habilitación en el entorno productivo oficial de SUNAT requiere el registro formal de la empresa como emisor electrónico, lo cual corresponde a una etapa posterior a este proyecto..

Integración con otras pasarelas de pago: Se utiliza exclusivamente Mercado Pago como pasarela de pago digital. No se integran otras pasarelas adicionales. El cobro automático sin intervención del cajero no se implementa..

Gestión multi-sucursal:El sistema opera para un único establecimiento. La sincronización de datos entre múltiples locales es una funcionalidad futura.

Análisis predictivo e Inteligencia Artificial: Pronósticos de demanda, recomendaciones automáticas y algoritmos de aprendizaje automático exceden el alcance del proyecto.

Comercio electrónico y aplicación móvil: El sistema es de escritorio. No incluye tienda en línea ni interfaz para dispositivos móviles.

Business Intelligence avanzado: Cubos de análisis multidimensional e integración con plataformas de inteligencia de negocios externas quedan para versiones futuras. Los reportes y gráficos incluidos en el sistema cubren las necesidades de análisis operativo del minimarket.

Integración con sistemas logísticos externos: No se contempla conexión con proveedores de transporte, couriers ni plataformas de distribución.

3. Mapa de Procesos Actuales (As-Is)

•Área / Departamento: (Ej. Almacén). Ventas y Atención al cliente

•Proceso actual: Descripción breve de cómo operan hoy.

El proceso de venta inicia cuando el cliente ingresa al minimarket y selecciona los productos que desea adquirir. Si no encuentra algún producto, solicita ayuda al personal de atención. Una vez que ha reunido los productos deseados, se dirige a la caja, donde el cajero registra los productos seleccionados y el sistema calcula el monto total a pagar. Posteriormente, el cliente realiza el pago y se verifica si este ha sido aprobado. En caso de que el pago sea rechazado, se solicita otro método de pago. Si el pago es aprobado, se entrega la boleta o factura correspondiente, luego se entregan los productos al cliente y finalmente este se retira del establecimiento.

Puntos de dolor: Cuellos de botella, tareas manuales o errores frecuentes

El cliente puede tener dificultades para encontrar algunos productos y requerir asistencia del personal.

El registro manual de los productos puede generar errores y retrasos durante el proceso de venta.

El cálculo del monto a pagar puede presentar errores cuando no existe automatización.

La validación del pago puede ocasionar demoras cuando el método utilizado es rechazado.

No existe una actualización automática del inventario después de cada venta.

La generación de reportes de ventas requiere tiempo adicional y trabajo manual.

La información de ventas y productos no se encuentra centralizada, dificultando la toma de decisiones.

Existe riesgo de inconsistencias en el control del stock y en el seguimiento de los productos más vendidos.

4. REQUERIMIENTOS FUNCIONALES Y TÉCNICOS (AQUI PEGARA LOS RF)

5. REQUERIMIENTOS NO FUNCIONALES

● Rendimiento:

Tiempos de respuesta: Las operaciones estándar de consulta, registro y actualización deben completarse en menos de 2 segundos para el 95% de las transacciones. El Dashboard carga en máximo 4 segundos, la boleta electrónica se emite en máximo 5 segundos desde que el cajero confirma la venta, y las consultas de DNI/RUC se resuelven en máximo 3 segundos. Si un servicio externo no responde, el sistema habilita el ingreso manual sin interrumpir el flujo.

Reportes: Los reportes de productos más vendidos, stock bajo y ganancias por período (RF-017, RF-018, RF-019) para períodos de hasta 12 meses se generan en máximo 5 segundos, sin bloquear la operación del personal en otros módulos.

Concurrencia: Con hasta 5 usuarios operando simultáneamente en distintos módulos, el sistema mantiene su rendimiento sin errores ni pérdida de datos.

● Seguridad:

Accesos y roles: El sistema implementa control de acceso por roles (RBAC). El Cajero solo puede registrar ventas, aplicar devoluciones (RF-014) y consultar historial de clientes (RF-016). Solo el Administrador puede acceder a la configuración del sistema y administración de usuarios. Las restricciones se aplican automáticamente al iniciar sesión.

Protección de credenciales: Las contraseñas se almacenan cifradas de forma irreversible. Tras tres intentos fallidos consecutivos, la cuenta queda bloqueada y solo el Administrador puede reactivarla. La sesión se cierra automáticamente tras 30 minutos de inactividad.

Bitácora de auditoría: Toda acción registrada en el sistema (RF-020) queda almacenada con usuario, módulo, tipo de acción y fecha/hora exacta. El registro no puede ser eliminado por ningún usuario.

● Integración con servicios externos:

DNI, RUC y tipo de cambio: El sistema consulta automáticamente los datos de clientes por DNI y proveedores por RUC (RF-011, RF-016), y obtiene el tipo de cambio SUNAT al iniciar. Ante indisponibilidad de cualquier servicio, muestra el último valor registrado y habilita el ingreso manual sin bloquear el proceso.

Boletas electrónicas y pagos digitales: Cada venta genera una boleta validada por un OSE autorizado. Los pagos se procesan vía Mercado Pago (RF-015). Si alguno de estos servicios falla, el sistema registra la venta normalmente, emite un documento provisional y permite continuar con pago en efectivo sin perder los datos.

Exportación de reportes: Los reportes de ventas, stock bajo y ganancias (RF-017, RF-018, RF-019) pueden exportarse en formato Excel (.xlsx) con columnas y totales correctamente organizados.

● Disponibilidad:

Modo degradado: Ante la caída de cualquier servicio externo, el sistema continúa operativo en todos los módulos locales. Las ventas en efectivo, consultas de stock (RF-003), movimientos de inventario (RF-004) y registro de compras (RF-008) funcionan siempre desde la base de datos local.

Integridad transaccional: Las operaciones que afectan múltiples módulos a la vez — como una venta que actualiza el stock (RF-009), registra el método de pago (RF-015) y genera la boleta — se ejecutan de forma atómica. Si alguna parte falla, todos los cambios se revierten automáticamente.

Copias de seguridad y uptime: El sistema permite generar respaldos completos en una ubicación definida por el Administrador y restaurarlos sin pérdida de información. Debe permanecer disponible al menos el 95% del tiempo dentro del horario operativo del minimarket.

● Usabilidad:

Capacitación mínima: Un usuario sin experiencia previa debe poder registrar una venta completa — incluyendo consulta de cliente por DNI, selección del método de pago y recepción de la boleta electrónica — tras una capacitación máxima de 30 minutos.

Mensajes claros: El sistema muestra mensajes en lenguaje cotidiano ante errores de ingreso o fallas de servicios externos, indicando qué ocurrió y cómo continuar. No se exponen mensajes técnicos al usuario final en ningún momento.

6. Suposiciones y Restricciones

Restricciones:

Tecnología de desarrollo: El sistema se desarrolla en Java con interfaz de escritorio, manteniendo la estructura de múltiples ventanas del proyecto base. No se cambia el lenguaje de programación ni se migra a arquitectura web durante el desarrollo del proyecto.

Motor de base de datos: Se utiliza una solución de base de datos embebida que no requiere la instalación de un servidor externo adicional, facilitando el despliegue en los equipos del minimarket. La migración a un servidor externo se contempla como mejora futura.

Presupuesto: El proyecto es de naturaleza académica y no dispone de presupuesto para adquisición de licencias de software. Todas las herramientas, librerías y servicios externos utilizados cuentan con planes gratuitos suficientes para el alcance del proyecto.

Modalidad de entrega: El proyecto se desarrolla y entrega como un único sprint. Los requisitos Must son de implementación obligatoria. Los Should se incluyen si el tiempo lo permite. Los Could quedan documentados para versiones futuras.

Conectividad: El sistema requiere conexión a internet para operar con los servicios externos integrados: validación de DNI/RUC, tipo de cambio SUNAT, boletas electrónicas vía OSE y pagos con Mercado Pago. En ausencia de conexión, el sistema opera en modo degradado permitiendo las operaciones que no dependen de servicios externos.

Pagos digitales: El soporte para pagos digitales se implementa exclusivamente mediante Mercado Pago. No se integran otras pasarelas adicionales ni se implementa el cobro automático sin intervención del cajero.

Facturación electrónica: El sistema genera boletas electrónicas a través de un OSE autorizado en su entorno de validación. La habilitación en el entorno productivo oficial de SUNAT, que requiere el registro formal de la empresa como emisor electrónico, corresponde a una etapa posterior al proyecto académico.

Programa de fidelización: La tasa de acumulación de puntos y las categorías de fidelización son configurables únicamente por el Administrador. Los cajeros pueden consultar puntos y aplicar canjes, pero no modificar la configuración del programa.

Migración de datos: Las ventas anteriores a la puesta en marcha migrarán con los datos disponibles (cliente, total y fecha) sin detalle de productos ni puntos de fidelización. Desde el inicio de operaciones del ERP, todas las ventas incluirán detalle completo.

● Suposiciones:

Capacidad del personal: El personal que utilizará el sistema tiene conocimientos básicos de computación: manejo de teclado, mouse y navegación en Windows. No se requiere formación técnica especializada.

Equipamiento disponible: El minimarket cuenta con equipos con Windows 10 u 11 y recursos suficientes para ejecutar la aplicación de escritorio sin problemas de rendimiento.

Conectividad: El minimarket dispone de conexión a internet estable durante su horario de atención para operar con los servicios externos integrados.

Cuenta de Mercado Pago: El equipo de desarrollo dispone de una cuenta de desarrollador activa en Mercado Pago con acceso a las credenciales necesarias para la integración de pagos.

Servicios de validación: Existe disponibilidad de un servicio de consulta de DNI y RUC con plan gratuito suficiente para cubrir el volumen de consultas del minimarket durante el período de evaluación académica.

OSE para facturación electrónica: Existe disponibilidad de un Operador de Servicios Electrónicos autorizado con entorno de validación gratuito para el desarrollo y evaluación del proyecto.

Operación en un solo local: El minimarket LAREDO opera en un único establecimiento. El sistema no requiere sincronización entre múltiples sucursales.

Moneda y régimen tributario: Todas las transacciones se realizan en Soles peruanos (S/) con IGV del 18% incluido en el desglose de cada comprobante. El tipo de cambio del dólar se consulta únicamente como referencia financiera.

Participación en capacitación: El personal dispondrá del tiempo necesario para recibir la capacitación básica antes de la puesta en operación del sistema.

7. Riesgos y Mitigaciones

● Riesgo 1: Pérdida o corrupción de datos durante la migración al nuevo sistema.

Los datos existentes del minimarket pueden verse afectados al trasladarse al nuevo repositorio centralizado, especialmente considerando que no contienen detalle de productos por venta ni puntos de fidelización.
Mitigación: Realizar una copia de seguridad completa del sistema actual antes de iniciar la migración. Probar el procedimiento en un ambiente separado y verificar la integridad de cada registro migrado antes de activar el nuevo sistema. Las ventas históricas migrarán únicamente con los datos disponibles (cliente, total y fecha), dejando constancia de esta limitación en el manual del sistema.

● Riesgo 2: Resistencia al cambio por parte del personal del minimarket.

Los trabajadores, acostumbrados a registrar ventas y compras manualmente, pueden mostrar dificultad para adoptar el nuevo sistema y sus restricciones de seguridad.
Mitigación: Mantener una interfaz familiar para reducir la curva de aprendizaje y elaborar una guía de usuario por módulo con capturas de pantalla. Realizar sesiones de capacitación práctica antes de la puesta en marcha, incluyendo el uso del programa de fidelización, los pagos con Mercado Pago y la emisión de boletas electrónicas.

● Riesgo 3: Inconsistencia de datos por operaciones incompletas ante fallos del sistema.

Una falla durante una operación que involucra múltiples registros simultáneos — venta, actualización de stock, acumulación de puntos y boleta electrónica — puede dejar los datos en un estado parcialmente actualizado.
Mitigación: Implementar manejo de transacciones atómicas en todas las operaciones que afecten múltiples entidades, de modo que ante cualquier error todos los cambios de esa operación sean revertidos automáticamente, manteniendo la consistencia de los datos.

● Riesgo 4: Indisponibilidad de servicios externos durante la operación.

Los servicios de validación de DNI/RUC, el OSE para boletas electrónicas, el tipo de cambio SUNAT o Mercado Pago pueden estar temporalmente fuera de línea, afectando el flujo normal de atención al cliente.
Mitigación: El sistema detecta la indisponibilidad de cada servicio de forma independiente y opera en modo degradado, permitiendo el ingreso manual de datos, emitiendo documentos provisionales y habilitando el pago en efectivo como alternativa. Las ventas nunca quedan bloqueadas por la falla de un servicio externo.

● Riesgo 5: Conflictos de concurrencia en la base de datos con múltiples usuarios simultáneos.

Al acceder varios usuarios al sistema al mismo tiempo desde distintos equipos, pueden producirse conflictos en el acceso a los datos, especialmente en el módulo de inventario y ventas.
Mitigación: Configurar la base de datos en modo servidor local que soporte múltiples conexiones simultáneas. Documentar el procedimiento de configuración como parte del manual de instalación del sistema.

● Riesgo 6: Tiempo insuficiente para completar todos los módulos e integraciones externas.

El alcance ampliado con cuatro integraciones externas (DNI/RUC, OSE, Mercado Pago y tipo de cambio) incrementa la complejidad del proyecto dentro de un sprint único.
Mitigación: Priorizar la implementación en el siguiente orden: (1) módulos base Must, (2) integración con Mercado Pago, (3) integración con OSE para boletas electrónicas, (4) validación de DNI/RUC, (5) tipo de cambio SUNAT, (6) programa de fidelización y demás funcionalidades Should.

● Riesgo 7: Almacenamiento inseguro de credenciales de usuario.

Si las contraseñas se almacenan en texto legible, cualquier persona con acceso directo a la base de datos podría comprometer las cuentas de todos los usuarios del sistema.
Mitigación: Implementar el cifrado de contraseñas desde el inicio del desarrollo. La migración de usuarios existentes aplica el cifrado a todas las credenciales. Ninguna contraseña queda almacenada de forma legible en ningún momento.

● Riesgo 8: Discrepancias entre el estado de pagos en Mercado Pago y lo registrado en el sistema.

Un pago registrado como aprobado podría ser revertido posteriormente por la plataforma, generando diferencias en la conciliación de caja al cierre del día

Mitigación: El sistema verifica el estado del pago en Mercado Pago antes de confirmar la venta como cobrada. El reporte de conciliación de caja permite identificar transacciones con estado inconsistente. Se recomienda revisar el panel de Mercado Pago al cierre de cada jornada .
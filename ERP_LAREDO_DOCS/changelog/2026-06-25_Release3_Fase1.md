# Registro de Cambios y Planificación - ERP LAREDO
**Fecha y Hora:** 2026-06-25T23:42:00-05:00

## 🎯 Objetivo de la Sesión
Iniciar el **Release 3 (Fase 1: Seguridad)**. Se deben implementar los siguientes requerimientos:
1. **Bitácora de Auditoría (FR-020):** Registro inmutable en BD de qué usuario hace qué acción.
2. **Bloqueo de Cuenta (FR-039):** Bloquear acceso al sistema por 15 minutos tras 3 intentos fallidos de login.
3. **Logger Global del Sistema:** Registrar todos los errores (`Exceptions`) y eventos clave en archivos de texto locales para facilitar la depuración (Debugging rápido).

## 📝 Plan de Acción Técnico
- Crear la clase utilitaria `LoggerGlobal.java` que escribirá en la carpeta `logs/`.
- Crear la tabla `tb_bitacora` (ID, usuario, modulo, accion, fecha).
- Crear las clases `Bitacora.java` y `BitacoraDAO.java`.
- Modificar `FrmLogin.java` para contar los intentos fallidos. En caso de error de login, se actualizará un campo en la BD o en memoria. Se usará la BD para que sea persistente.
- Crear la interfaz `IFrmBitacoraAuditoria.java` para ver los logs visualmente en el menú.

## 🐛 Registro de Bugs Encontrados y Soluciones (Logs)
*(Este apartado se irá llenando conforme se presenten fallos y se logre solucionarlos durante la programación)*

# ⚙️ REQUISITOS NO FUNCIONALES — ERP LAREDO
**ISO/IEC/IEEE 29148:2018 · SWEBOK v4 · ISO 25010 · Sesión 9 (Características ERP)**

---

## Marco de referencia

La Sesión 9 define cinco características obligatorias de un ERP:
**Integración · Modularidad · Adaptabilidad · Escalabilidad · Seguridad**

Cada RNF de este documento está mapeado a una o más de estas características.

---

## RNF-01 — Rendimiento (Tiempo de Respuesta)

| Campo | Valor |
|-------|-------|
| **Categoría** | Rendimiento / Eficiencia |
| **Característica ERP** | Integración (dato único en tiempo real) |
| **Prioridad** | Must |

**Especificación:**
El sistema debe responder a cualquier operación CRUD estándar en menos de **2 segundos** bajo carga normal (1–3 usuarios simultáneos). Las operaciones de generación de reportes PDF o carga masiva de datos pueden tardar hasta **5 segundos** mostrando un indicador de carga visible.

**Métrica de verificación:**

| Operación | Tiempo máximo aceptable |
|-----------|------------------------|
| Login exitoso | < 1 segundo |
| Registrar una venta | < 2 segundos |
| Buscar producto por nombre | < 1 segundo |
| Cargar listado de 100 productos | < 2 segundos |
| Generar reporte PDF | < 5 segundos |
| Consultar kardex de un producto | < 2 segundos |

**Cómo verificarlo:** Cronometrar con stopwatch en 10 pruebas consecutivas. El promedio debe ser < 2 s y ninguna prueba puede superar el máximo definido.

---

## RNF-02 — Seguridad / RBAC (Control de Acceso por Roles)

| Campo | Valor |
|-------|-------|
| **Categoría** | Seguridad |
| **Característica ERP** | Seguridad |
| **Prioridad** | Must |

**Especificación:**
El sistema debe implementar **Role-Based Access Control (RBAC)** con 3 roles diferenciados. El menú principal y los IFrm accesibles deben construirse dinámicamente según el rol del usuario autenticado.

**Matriz de permisos por rol:**

| Módulo / Acción | Cajero | Gerente | Administrador |
|----------------|--------|---------|---------------|
| Registrar venta | ✅ | ✅ | ✅ |
| Aplicar descuento > 20% | ❌ | ✅ | ✅ |
| Ver historial de ventas | Sólo las propias | ✅ Todas | ✅ Todas |
| Gestión de productos (CRUD) | Ver | ✅ | ✅ |
| Gestión de proveedores | ❌ | ✅ | ✅ |
| Registro de compras | ❌ | ✅ | ✅ |
| Módulo Finanzas | ❌ | ✅ | ✅ |
| Módulo RRHH | ❌ | ✅ | ✅ |
| Gestión de usuarios del sistema | ❌ | ❌ | ✅ |
| Configuración del sistema | ❌ | ❌ | ✅ |
| Ver bitácora de auditoría | ❌ | ✅ | ✅ |
| Exportar reportes | ❌ | ✅ | ✅ |

**Implementación Java:**
```java
// SessionManager.java — singleton con usuario activo
public class SessionManager {
    private static Usuario usuarioActivo;

    public static boolean puedeAcceder(String modulo) {
        return PermisosConfig.tienePermiso(usuarioActivo.getRol(), modulo);
    }
}

// En SidebarPanel: construir menú según rol
if (SessionManager.puedeAcceder("RRHH")) {
    sidebar.addItem("RRHH", IconSet.PEOPLE);
}
```

**Cómo verificarlo:** Login con cada rol → verificar que el menú solo muestra módulos permitidos. Intentar navegar por código a un módulo restringido.

---

## RNF-03 — Seguridad / Cifrado de Contraseñas

| Campo | Valor |
|-------|-------|
| **Categoría** | Seguridad |
| **Característica ERP** | Seguridad |
| **Prioridad** | Must |

**Especificación:**
Las contraseñas **nunca se almacenan en texto plano** en `usuarios.txt`. Se usa hash **SHA-256** como mínimo, o **BCrypt** (recomendado).

**Implementación Java:**
```java
// HashUtil.java
import java.security.MessageDigest;

public class HashUtil {
    // SHA-256 (mínimo aceptable)
    public static String sha256(String password) {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    // Verificación en login
    public static boolean verificar(String passwordIngresado, String hashGuardado) {
        return sha256(passwordIngresado).equals(hashGuardado);
    }
}

// En usuarios.txt: nunca "password123", siempre "ef92b778bafe..."
```

**Cómo verificarlo:** Abrir `usuarios.txt` directamente → las contraseñas deben ser cadenas de 64 caracteres hexadecimales, nunca texto legible.

---

## RNF-04 — Seguridad / Bloqueo por Intentos Fallidos

| Campo | Valor |
|-------|-------|
| **Categoría** | Seguridad |
| **Característica ERP** | Seguridad |
| **Prioridad** | Must |
| **HU relacionada** | FR-039 |

**Especificación:**
El sistema debe bloquear automáticamente una cuenta de usuario por **15 minutos** tras **3 intentos consecutivos** de login con contraseña incorrecta. El Administrador puede desbloquearlo manualmente.

**Implementación:**
```java
// En LoginController.java
private Map<String, Integer> intentosFallidos = new HashMap<>();
private Map<String, LocalDateTime> bloqueoHasta = new HashMap<>();

public LoginResult intentarLogin(String usuario, String password) {
    if (estaBloqueado(usuario)) {
        long minutosRestantes = calcularMinutosRestantes(usuario);
        return LoginResult.bloqueado(minutosRestantes);
    }

    if (credencialesCorrectas(usuario, password)) {
        intentosFallidos.remove(usuario);
        return LoginResult.exitoso(buscarUsuario(usuario));
    } else {
        int intentos = intentosFallidos.getOrDefault(usuario, 0) + 1;
        intentosFallidos.put(usuario, intentos);
        if (intentos >= 3) {
            bloqueoHasta.put(usuario, LocalDateTime.now().plusMinutes(15));
            bitacora.registrar(usuario, "LOGIN", "3 intentos fallidos - cuenta bloqueada", "FALLO");
            return LoginResult.bloqueado(15);
        }
        return LoginResult.credencialesInvalidas(3 - intentos);
    }
}
```

**Cómo verificarlo:** Intentar login 3 veces con contraseña incorrecta → el 3.er intento debe bloquear la cuenta y mostrar el mensaje con tiempo de espera.

---

## RNF-05 — Seguridad / Cierre de Sesión Automático

| Campo | Valor |
|-------|-------|
| **Categoría** | Seguridad |
| **Prioridad** | Must |
| **HU relacionada** | FR-040 |

**Especificación:**
El sistema debe cerrar la sesión automáticamente tras **30 minutos de inactividad**, mostrando una advertencia emergente con 60 segundos de anticipación.

**Implementación:**
```java
// InactivityTimer.java — usando javax.swing.Timer
public class InactivityTimer {
    private Timer timer;
    private final int INACTIVIDAD_MS = 29 * 60 * 1000; // 29 minutos
    private final int AVISO_MS       = 60 * 1000;       // 60 segundos

    public void resetear() {
        if (timer != null) timer.restart();
    }

    public void iniciar() {
        timer = new Timer(INACTIVIDAD_MS, e -> mostrarAviso());
        timer.setRepeats(false);
        timer.start();
    }

    private void mostrarAviso() {
        // Diálogo de cuenta regresiva (no JOptionPane estándar)
        // → Si el usuario no responde en 60s: cerrar sesión
        new SesionExpiracionDialog(AVISO_MS, () -> cerrarSesion());
    }
}

// Resetear el timer en cualquier evento del usuario (MouseListener, KeyListener)
```

---

## RNF-06 — Auditabilidad / Bitácora Inalterable

| Campo | Valor |
|-------|-------|
| **Categoría** | Auditabilidad |
| **Característica ERP** | Trazabilidad (Sesión 9: "permite auditar cualquier operación") |
| **Prioridad** | Must |
| **HU relacionada** | FR-020, FR-041 |

**Especificación:**
El sistema debe registrar automáticamente en `bitacora.txt` (o tabla BITACORA en BD) **todas las operaciones** de creación, modificación y eliminación. El registro es **inmutable** desde la interfaz: ningún usuario puede editar ni eliminar entradas de la bitácora.

**Campos obligatorios de cada registro:**

| Campo | Descripción | Ejemplo |
|-------|-------------|---------|
| `id` | Secuencial único | BIT-001 |
| `usuario` | Login del usuario activo | jperez |
| `rol` | Rol del usuario | CAJERO |
| `modulo` | Módulo donde ocurrió | Ventas |
| `accion` | Operación realizada | RegistroVenta |
| `descripcion` | Detalle de la acción | Venta V-045 por S/ 28.50 |
| `resultado` | Éxito / Fallo | Éxito |
| `ipEquipo` | IP local del equipo | 192.168.1.10 |
| `fechaHora` | Timestamp completo | 2026-06-15 10:32:15 |

**Operaciones que SIEMPRE generan registro:**
- ✅ Login exitoso y fallido
- ✅ Registrar, editar o eliminar cualquier entidad
- ✅ Generar cualquier reporte
- ✅ Cambio de contraseña
- ✅ Bloqueo y desbloqueo de cuentas
- ✅ Registro y anulación de ventas y compras
- ✅ Descuentos aplicados (quién y cuánto)
- ✅ Cierre de sesión (manual o automático)

---

## RNF-07 — Disponibilidad / Modo Degradado

| Campo | Valor |
|-------|-------|
| **Categoría** | Disponibilidad |
| **Característica ERP** | Adaptabilidad (Sesión 9) |
| **Prioridad** | Must |
| **HU relacionadas** | FR-033, FR-048 |

**Especificación:**
Ante la falla de **cualquier servicio externo** (API RENIEC, OSE/SUNAT, Mercado Pago, tipo de cambio), el sistema debe:
1. **No bloquear** la operación principal
2. **Mostrar un mensaje claro** explicando qué servicio falló
3. **Ofrecer una alternativa manual** para continuar

**Tabla de modos degradados:**

| Servicio externo | Falla | Comportamiento degradado |
|-----------------|-------|------------------------|
| API RENIEC (DNI) | No responde / timeout 5s | "RENIEC no disponible. Ingrese nombre manualmente." Los campos quedan editables |
| API SUNAT tipo de cambio | No responde | Usa último valor almacenado. Muestra "(al DD/MM/AAAA — no actualizado)" |
| OSE para boleta electrónica | Error | Genera comprobante provisional imprimible. "Boleta electrónica pendiente de envío" |
| Mercado Pago | Error de conexión | "Pago digital no disponible. Registrar pago en efectivo o reintentar." |

---

## RNF-08 — Usabilidad / Exportación de Reportes

| Campo | Valor |
|-------|-------|
| **Categoría** | Usabilidad |
| **Característica ERP** | Adaptabilidad |
| **Prioridad** | Must |

**Especificación:**
Todos los reportes del sistema deben exportarse en **dos formatos**: PDF y **.xlsx** (Excel). El archivo generado debe abrirse correctamente en Microsoft Excel 2016+ y en Adobe Reader / cualquier lector PDF.

**Reportes que deben exportar en ambos formatos:**

| Reporte | HU | PDF | .xlsx |
|---------|-----|-----|-------|
| Ventas por período | FR-017, FR-019 | ✅ | ✅ |
| Stock bajo | FR-018 | ✅ | ✅ |
| Productos más vendidos | FR-006, FR-017 | ✅ | ✅ |
| Historial de compras | FR-010 | ✅ | ✅ |
| Clientes VIP / ranking | FR-032 | ✅ | ✅ |
| Bitácora de auditoría | FR-041 | ✅ | ✅ (.csv) |
| Planilla mensual | FR-037 | ✅ | ✅ |
| Boleta de venta | (POS) | ✅ | ❌ (no aplica) |

**Librería Java recomendada:** Apache POI para `.xlsx` · iText / JasperReports para PDF

---

## RNF-09 — Concurrencia / Usuarios Simultáneos

| Campo | Valor |
|-------|-------|
| **Categoría** | Confiabilidad |
| **Característica ERP** | Escalabilidad (Sesión 9) |
| **Prioridad** | Should (en fase `.txt`) / Must (en fase H2) |

**Especificación:**
El sistema debe soportar hasta **5 usuarios simultáneos** sin que se corrompan los archivos de datos ni se pierdan transacciones.

**Problema actual con `.txt`:**
```
Sin sincronización, dos Cajeros pueden escribir ventas.txt al mismo tiempo
y una venta puede sobreescribir a la otra → pérdida de datos.

Solución temporal (mientras se usa .txt):
- synchronized en métodos de escritura
- FileLock de Java NIO
- O migrar a H2 que maneja concurrencia nativa
```

**Solución definitiva:** migrar a H2 (ver `08_MODELO_DATOS_BD.md`)

---

## RNF-10 — Integridad Transaccional (Atomicidad)

| Campo | Valor |
|-------|-------|
| **Categoría** | Integridad |
| **Característica ERP** | Integración (Sesión 9: "flujo continuo, dato único") |
| **Prioridad** | Must |

**Especificación:**
Las operaciones que modifican **múltiples entidades a la vez** deben ser **atómicas**: si cualquier paso falla, ningún cambio debe persistir.

**Operaciones atómicas requeridas:**

| Operación | Pasos involucrados | Qué pasa si falla en el paso 2 |
|-----------|-------------------|-------------------------------|
| Registrar venta | 1. Guardar venta / 2. Reducir stock / 3. Generar comprobante / 4. Registrar bitácora | El stock NO se reduce, la venta NO se guarda |
| Registrar compra | 1. Guardar compra / 2. Aumentar stock / 3. Actualizar kardex | Si falla el paso 2, la compra NO se guarda |
| Canjear puntos | 1. Aplicar descuento / 2. Reducir puntos / 3. Registrar en historial | Si falla el paso 2, el descuento NO se aplica |
| Devolver producto | 1. Registrar devolución / 2. Aumentar stock / 3. Reembolso | Si falla el paso 2, la devolución NO se registra |

**Implementación con `.txt` (temporal):**
```java
// Patrón: leer todo → modificar en memoria → escribir todo solo si no hay errores
public boolean registrarVentaAtomico(Venta venta) {
    try {
        // 1. Validar todo antes de escribir nada
        validarStock(venta.getDetalles());

        // 2. Solo si todo pasa: escribir en orden
        ventaDAO.guardar(venta);
        productoDAO.actualizarStock(venta.getDetalles());
        kardexDAO.registrarSalidas(venta);
        bitacoraDAO.registrar("Ventas", "RegistroVenta", venta.getId());

        return true;
    } catch (Exception e) {
        // En caso de error parcial: intentar rollback manual
        rollbackVenta(venta);
        return false;
    }
}
```

**Implementación con H2 (definitiva):**
```java
// Con JDBC y transacciones SQL:
conn.setAutoCommit(false);
try {
    ventaDAO.guardar(conn, venta);
    productoDAO.actualizarStock(conn, venta.getDetalles());
    conn.commit(); // ← todo o nada
} catch (SQLException e) {
    conn.rollback(); // ← si algo falla, deshace todo
}
```

---

## RNF-11 — Usabilidad / Mensajes de Error Amigables

| Campo | Valor |
|-------|-------|
| **Categoría** | Usabilidad |
| **Característica ERP** | Adaptabilidad (Sesión 10: capacitación y gestión del cambio) |
| **Prioridad** | Must |

**Especificación:**
Ningún mensaje de error técnico debe mostrarse al usuario. Los errores deben ser descriptivos, en español, indicar qué salió mal y qué debe hacer el usuario.

**Ejemplos:**

| ❌ Mensaje técnico (prohibido) | ✅ Mensaje amigable (requerido) |
|-------------------------------|-------------------------------|
| `NullPointerException at line 45` | "Error al procesar la venta. Verifique que todos los campos estén completos." |
| `File not found: productos.txt` | "No se puede acceder a los datos de productos. Contacte al Administrador." |
| `SQLException: unique constraint` | "Ya existe un producto con ese código. Use un código diferente." |
| `Connection refused: localhost:8080` | "El servicio externo no está disponible. Puede continuar ingresando los datos manualmente." |
| `ArrayIndexOutOfBoundsException` | "Error al leer los datos. El archivo puede estar corrupto. Contacte al Administrador." |

---

## Resumen de RNF

| ID | Nombre | Categoría | Must/Should | Verificable |
|----|--------|-----------|-------------|------------|
| RNF-01 | Tiempo de respuesta < 2s | Rendimiento | Must | ✅ Cronómetro |
| RNF-02 | RBAC por roles | Seguridad | Must | ✅ Prueba de roles |
| RNF-03 | Contraseñas cifradas SHA-256 | Seguridad | Must | ✅ Leer archivo .txt |
| RNF-04 | Bloqueo tras 3 intentos | Seguridad | Must | ✅ Prueba funcional |
| RNF-05 | Cierre sesión 30 min | Seguridad | Must | ✅ Prueba temporización |
| RNF-06 | Bitácora inalterable | Auditoría | Must | ✅ Intentar editar |
| RNF-07 | Modo degradado | Disponibilidad | Must | ✅ Desconectar red |
| RNF-08 | Exportación PDF y .xlsx | Usabilidad | Must | ✅ Abrir en Excel |
| RNF-09 | 5 usuarios concurrentes | Confiabilidad | Should | ✅ Prueba paralela |
| RNF-10 | Atomicidad transaccional | Integridad | Must | ✅ Inyectar error |
| RNF-11 | Mensajes de error amigables | Usabilidad | Must | ✅ Revisión visual |

---

*Referencia: ISO/IEC/IEEE 29148:2018 · ISO/IEC 25010 · SWEBOK v4 (2024) · Sesión 9 (características ERP)*

# Changelog: Refactorización Integral Post-Auditoría ERP
**Fecha:** 2026-06-26T01:31:00-05:00  
**Sesión:** Correcciones derivadas de la Auditoría Técnica Independiente  
**Alcance:** Toda la pila (Clases, Modelo, Vista, BD, Utils)

---

## FASE A — Seguridad (OWASP)

### A1. Credenciales de BD removidas del código fuente
- **Archivo creado:** `config.properties` (excluido del VCS via `.gitignore`)
- **Archivo creado:** `src/Utils/ConfigManager.java` — lector centralizado de propiedades
- **Archivo modificado:** `src/Modelo/ConexionDB.java` — ahora usa `ConfigManager.get("db.*")`
- **Archivo modificado:** `.gitignore` — agregados `config.properties`, `logs/`, `*.log`

### A2. Token JWT de API externa removido del código fuente
- **Archivo modificado:** `src/Modelo/ApisPeruService.java`
  - Token ahora en `config.properties` via `ConfigManager.get("api.token")`
  - Refactorizado: HTTP GET centralizado en `ejecutarGet()` con timeout de 5s (Circuit Breaker básico)
  - `e.printStackTrace()` → `LoggerGlobal.error()`

### A3. Contraseñas en texto plano — OWASP A02:2021
- **Archivo creado:** `src/Utils/PasswordUtils.java` — hash SHA-256 + verificación
- **Archivo modificado:** `src/Vista/FrmLogin.java`
  - `u.getPassword().equals(pass)` → `PasswordUtils.verifyPassword(pass, u.getPassword())`
- **Archivo modificado:** `src/Vista/GestionarUsuarios.java`
  - `u.setPassword(password)` → `u.setPassword(PasswordUtils.hashPassword(password))`
- **Archivo modificado:** `src/Modelo/MigracionDB.java`
  - `migrarUsuarios()` ahora hashea contraseñas de texto plano provenientes del TXT

### A4. ConexionDB — Thread-Safety
- **Archivo modificado:** `src/Modelo/ConexionDB.java`
  - Método `getConexion()` es ahora `synchronized`
  - `cerrar()` también es `synchronized`
  - `e.printStackTrace()` → `LoggerGlobal.error()`

---

## FASE B — Integridad Financiera (double → BigDecimal)

### B1. Modelos de Dominio (Clases/)
| Archivo | Campo(s) corregido(s) |
|---|---|
| `Producto.java` | `precio: double` → `BigDecimal` |
| `Venta.java` | `total: double` → `BigDecimal` |
| `DetalleVenta.java` | `precioUnitario: double` → `BigDecimal` |
| `Compra.java` | `total: double` → `BigDecimal` |
| `DetalleCompra.java` | `precioUnitario: double` → `BigDecimal` |
| `FlujoCaja.java` | `monto: double` → `BigDecimal` |
| `LibroMayor.java` | `montoDebe, montoHaber: double` → `BigDecimal` |
| `CuentasCP.java` | `montoOriginal, montoPendiente: double` → `BigDecimal` |

### B2. Capa de Persistencia (Modelo/)
| DAO | Corrección |
|---|---|
| `ProductoDAO` | `setDouble/getDouble(precio)` → `setBigDecimal/getBigDecimal` |
| `VentaDAO` | `setDouble/getDouble(total)` → BigDecimal; import duplicado eliminado |
| `CompraDAO` | `setDouble` → `setBigDecimal`; mejorado manejo de error en rollback |
| `FlujoCajaDAO` | `setDouble/getDouble(monto)` → BigDecimal |
| `LibroMayorDAO` | `setDouble/getDouble(montoDebe/montoHaber)` → BigDecimal |
| `CuentasCPDAO` | `setDouble` × 5 → BigDecimal; `abonar()` acepta BigDecimal |
| `MigracionDB` | `setDouble(precio)`, `setDouble(total)` → `setBigDecimal` |

### B3. Capa de Vista (Vista/)
| Vista | Corrección |
|---|---|
| `IFrmPuntoVenta` | `recalcularTotal()` usa `BigDecimal.ZERO + add()`; registrar venta usa `new BigDecimal(totalStr)` |
| `IFrmRegistroCompras` | `recalcularTotales()` usa BigDecimal; IGV calculado con `new BigDecimal("0.18")` |
| `IFrmReporteVentas` | Acumulador `totalVendido` es `BigDecimal.ZERO` + `.add()` |

### B4. Esquema de Base de Datos
| Script | Corrección |
|---|---|
| `InitDB.java` | `tb_compra.total DOUBLE` → `DECIMAL(10,2)`; `tb_detalle_compra.precioUnitario` → `DECIMAL(10,2)` |
| `InitDB2.java` | `tb_flujo_caja.monto`, `tb_libro_mayor.montoDebe/montoHaber`, `tb_cuentas_cp.montoOriginal/montoPendiente` → `DECIMAL(10,2)` |
| `InitDB3.java` | `tb_detalle_venta.precioUnitario DOUBLE` → `DECIMAL(10,2)` |
| **`InitDB5_MigrarDecimal.java`** | **NUEVO** — Script ALTER TABLE para BDs existentes. Seguro de ejecutar múltiples veces. |

---

## FASE C — Observabilidad (println/printStackTrace → LoggerGlobal)

Reemplazado `e.printStackTrace()` con `LoggerGlobal.error("contexto específico", ex)` en:
- `ProductoDAO`, `VentaDAO`, `CompraDAO`, `FlujoCajaDAO`, `LibroMayorDAO`
- `CuentasCPDAO`, `UsuarioDAO`, `ClienteDAO`, `KardexDAO`
- `ProveedorDAO`, `CategoriaDAO`, `ConexionDB`, `ApisPeruService`
- `IFrmPuntoVenta` (btnRegistrar catch)
- `IFrmRegistroCompras` (btnRegistrarCompra catch)
- `IFrmReporteVentas` (exportarPDF catch)

**Total:** ~30 ocurrencias eliminadas. Todos los logs ahora incluyen contexto (id, ruc, dni, etc.)

---

## FASE D — Calidad y Consistencia

### D1. Bug Visual Corregido
- **Archivo:** `IFrmPuntoVenta.java` líneas 141-144
- `lblPuntos` y `btnCanjearPuntos` se añadían 2 veces al panel. **Duplicados eliminados.**

### D2. Import duplicado eliminado
- **Archivo:** `IFrmRegistroCompras.java` — `import java.awt.*` duplicado removido
- **Archivo:** `VentaDAO.java` — `import java.sql.SQLException` duplicado removido

### D3. IFrmReporteVentas — Refactorización UIKit
- Todos los colores hardcodeados (`new Color(25, 118, 210)`) → `UIKit.PRIMARY`
- Fuentes manuales → `UIKit.H1`, `UIKit.BODY_BOLD`
- `new JTable(model)` → `UIKit.styledTable(model)`
- Botones → `UIKit.primaryButton()`, `UIKit.dangerOutlineButton()`
- Nombre del PDF exportado ahora incluye timestamp para evitar sobreescrituras

### D4. SELECT * → Columnas Explícitas
- 12 DAOs actualizados: columnas de SELECT ahora son explícitas en todos

### D5. COALESCE para generarId()
- `SELECT MAX(id) + 1` → `SELECT COALESCE(MAX(id), 0) + 1` en `CategoriaDAO`, `ProductoDAO`, `VentaDAO`, `ClienteDAO`, `UsuarioDAO`, `ProveedorDAO`
- Previene `NullPointerException` cuando la tabla está vacía

---

## Archivos Nuevos Creados

| Archivo | Propósito |
|---|---|
| `config.properties` | Almacén seguro de credenciales (excluido de Git) |
| `src/Utils/ConfigManager.java` | Lector centralizado de config.properties |
| `src/Utils/PasswordUtils.java` | Hashing y verificación SHA-256 |
| `src/InitDB5_MigrarDecimal.java` | ALTER TABLE para BDs existentes con columnas DOUBLE |

---

## Estado Final

- ✅ **40 puntos de contacto** con `double` para dinero → `BigDecimal`
- ✅ **~30 ocurrencias** de `printStackTrace()` → `LoggerGlobal.error()`
- ✅ **3 vulnerabilidades OWASP** críticas corregidas
- ✅ **1 bug visual** en POS corregido
- ✅ **12 DAOs** con SELECT explícito
- ✅ **Sistema de diseño UIKit** aplicado en IFrmReporteVentas
- ✅ **Esquema BD** actualizado con DECIMAL(10,2) en 8 columnas monetarias

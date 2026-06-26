# ✅ VALIDACIONES POR FORMULARIO — ERP LAREDO
**Reglas campo a campo · Basado en Kendall & Kendall (2011, Cap. 13) · SWEBOK v4**

---

## Convenciones

```
[M] = Obligatorio (Mandatory)
[O] = Opcional
Evento de validación:
  - BLUR:    al salir del campo (recomendado para la mayoría)
  - CHANGE:  en tiempo real mientras se escribe
  - SUBMIT:  solo al presionar Guardar (validaciones cruzadas)
```

**Implementación base:**
```java
// ValidatorUtil.java — métodos reutilizables
public class ValidatorUtil {
    public static boolean esEnteroPositivo(String s) { ... }
    public static boolean esDecimalPositivo(String s) { ... }
    public static boolean esDNI(String s) { return s.matches("\\d{8}"); }
    public static boolean esRUC(String s) { return s.matches("[12]0\\d{9}"); }
    public static boolean esEmail(String s) { return s.matches("^[\\w.+-]+@[\\w-]+\\.[a-z]{2,}$"); }
    public static boolean esTelefono(String s) { return s.matches("(\\+51)?[0-9]{7,9}"); }
    public static boolean esFechaFutura(LocalDate d) { return d.isAfter(LocalDate.now()); }
    public static boolean enRango(double v, double min, double max) { return v >= min && v <= max; }
}
```

---

## 1. FrmLogin — Pantalla de Inicio de Sesión

| Campo | Tipo | Req | Evento | Regla de validación | Mensaje de error |
|-------|------|-----|--------|--------------------|--------------------|
| Usuario | String | [M] | BLUR | 3–20 caracteres alfanuméricos, sin espacios | "El usuario debe tener entre 3 y 20 caracteres sin espacios." |
| Contraseña | String | [M] | BLUR | Mínimo 6 caracteres, no vacía | "La contraseña debe tener al menos 6 caracteres." |
| (Login) | — | — | SUBMIT | Comparar hash SHA-256; incrementar contador de intentos fallidos | "Usuario o contraseña incorrectos." *(mensaje genérico, no revelar cuál falló)* |
| (Bloqueo) | — | — | SUBMIT | Si intentos ≥ 3 → mostrar tiempo restante | "Cuenta bloqueada. Intente en X minutos." |

**Comportamiento adicional:**
- Presionar `Enter` en el campo contraseña equivale a hacer clic en "Iniciar sesión"
- El botón "Iniciar sesión" se desactiva durante la validación (evitar doble clic)
- El campo contraseña siempre muestra `••••••` (nunca texto plano)
- Ícono 👁 para alternar visibilidad de contraseña

---

## 2. IFrmPuntoVenta — Punto de Venta / POS

### 2a. Búsqueda y adición de productos al carrito

| Campo | Tipo | Req | Evento | Regla | Mensaje |
|-------|------|-----|--------|-------|---------|
| Código / Nombre producto | String | [M] | CHANGE | Mínimo 2 caracteres para iniciar búsqueda; busca en tiempo real en productos.txt | — (muestra resultados como autocompletado) |
| Cantidad | Entero | [M] | BLUR | Entero ≥ 1; no puede superar el stock disponible del producto; no acepta letras ni decimales | "Cantidad inválida." / "Stock insuficiente: hay X unidades disponibles." |
| (Agregar) | — | — | SUBMIT | Producto debe existir; carrito no puede estar vacío al procesar | "Seleccione un producto válido antes de agregar." |

### 2b. Descuento

| Campo | Tipo | Req | Evento | Regla | Mensaje |
|-------|------|-----|--------|-------|---------|
| Tipo de descuento | Enum | [O] | — | Seleccionar entre "%" o "S/." | — |
| Valor de descuento | Decimal | [O] | BLUR | Si "%": entre 0.01 y 20.00 (Cajero) o 0.01 y 100.00 (Gerente). Si "S/.": entre 0.01 y el subtotal actual. Máx 2 decimales | "Descuento fuera del rango permitido para su rol." / "El descuento no puede superar el subtotal." |

### 2c. Pago y cierre

| Campo | Tipo | Req | Evento | Regla | Mensaje |
|-------|------|-----|--------|-------|---------|
| DNI cliente | String | [O] | BLUR | Si se ingresa: exactamente 8 dígitos numéricos | "El DNI debe tener exactamente 8 dígitos numéricos." |
| Método de pago | Enum | [M] | — | Obligatorio seleccionar: Efectivo / Tarjeta / Digital / Mixto | "Debe seleccionar un método de pago." |
| Monto recibido (efectivo) | Decimal | [M si Efectivo] | BLUR | Debe ser ≥ total de la venta. Máx 2 decimales. > 0 | "El monto recibido debe ser mayor o igual al total (S/ X.XX)." |
| Monto digital (si Mixto) | Decimal | [M si Mixto] | BLUR | La suma Efectivo + Digital debe igualar exactamente el total de la venta | "La suma de los pagos (S/ X.XX) no coincide con el total (S/ Y.YY)." |
| Carrito | Lista | [M] | SUBMIT | Debe tener al menos 1 producto antes de procesar | "El carrito está vacío. Agregue al menos un producto." |

---

## 3. IFrmGestionProductos — Gestión de Productos

| Campo | Tipo | Req | Evento | Regla | Mensaje |
|-------|------|-----|--------|-------|---------|
| Código | String | [M] | BLUR | 3–10 caracteres alfanuméricos. Sin espacios. Único en el sistema | "El código debe tener entre 3 y 10 caracteres." / "Ya existe un producto con ese código." |
| Nombre | String | [M] | BLUR | 3–100 caracteres. No puede ser solo espacios | "El nombre debe tener entre 3 y 100 caracteres." |
| Categoría | Relación | [M] | — | Debe seleccionar una categoría del combo (cargado desde categorias.txt) | "Debe seleccionar una categoría." |
| Precio venta | Decimal | [M] | BLUR | Mayor a 0.00. Máx 2 decimales. No puede superar 99,999.99 | "El precio debe ser mayor a 0 con máximo 2 decimales." |
| Precio costo | Decimal | [O] | BLUR | ≥ 0.00. Debe ser ≤ precio de venta (advertencia, no error) | *(Advertencia)* "El precio de costo es mayor al precio de venta. Verifique." |
| Stock inicial | Entero | [M] | BLUR | ≥ 0. Entero. No puede ser negativo | "El stock no puede ser negativo." |
| Stock mínimo | Entero | [M] | BLUR | ≥ 1. Entero. Debe ser menor al stock inicial (advertencia) | "El stock mínimo debe ser al menos 1." |
| Fecha vencimiento | Fecha | [O] | BLUR | Si se ingresa: no puede ser fecha pasada (más de 1 día antes) | "La fecha de vencimiento no puede ser anterior a hoy." |
| Descripción | String | [O] | BLUR | Máx 500 caracteres | "La descripción no puede superar los 500 caracteres." |
| (Guardar) | — | — | SUBMIT | Todos los campos [M] válidos; código único confirmado | Mostrar todos los errores pendientes en un resumen |

**Validaciones cruzadas:**
- Si `stockMinimo ≥ stockActual` al guardar → advertencia (no bloquea): "El stock mínimo es mayor al stock actual. El sistema generará una alerta desde el inicio."
- Si `precioVenta < precioCosto` → advertencia: "Está vendiendo por debajo del costo. ¿Desea continuar?"

---

## 4. IFrmGestionClientes — Gestión de Clientes

| Campo | Tipo | Req | Evento | Regla | Mensaje |
|-------|------|-----|--------|-------|---------|
| DNI | String | [M] | BLUR | Exactamente 8 dígitos numéricos. Único en el sistema | "El DNI debe tener exactamente 8 dígitos numéricos." / "Ya existe un cliente con ese DNI." |
| Nombres | String | [M] | BLUR | 2–50 caracteres. Solo letras, espacios y tildes. No puede ser solo espacios | "Los nombres son obligatorios (2–50 caracteres)." |
| Apellidos | String | [M] | BLUR | 2–50 caracteres. Solo letras, espacios y tildes | "Los apellidos son obligatorios (2–50 caracteres)." |
| Teléfono | String | [O] | BLUR | Si se ingresa: 7–9 dígitos numéricos (puede incluir prefijo +51) | "El teléfono debe tener entre 7 y 9 dígitos." |
| Email | String | [O] | BLUR | Si se ingresa: formato válido (contiene @ y dominio con punto) | "Ingrese un correo electrónico válido." |
| Dirección | String | [O] | BLUR | Si se ingresa: 5–200 caracteres | "La dirección debe tener entre 5 y 200 caracteres." |

---

## 5. IFrmProveedores — Gestión de Proveedores

| Campo | Tipo | Req | Evento | Regla | Mensaje |
|-------|------|-----|--------|-------|---------|
| RUC | String | [M] | BLUR | 11 dígitos. Empieza con 10 (persona natural) o 20 (empresa). Único | "RUC inválido. Debe empezar con 10 o 20 y tener 11 dígitos." / "Este RUC ya está registrado." |
| Razón social | String | [M] | BLUR | 3–100 caracteres. No puede ser solo espacios | "La razón social es obligatoria (3–100 caracteres)." |
| Teléfono | String | [M] | BLUR | 7–9 dígitos numéricos (puede incluir +51) | "El teléfono debe tener entre 7 y 9 dígitos." |
| Email | String | [M] | BLUR | Formato válido de correo electrónico | "Ingrese un correo electrónico válido." |
| Dirección | String | [M] | BLUR | 5–200 caracteres | "La dirección es obligatoria (5–200 caracteres)." |
| Categoría de producto | Enum | [M] | — | Debe seleccionar al menos una categoría del combo | "Seleccione la categoría de productos que provee." |
| Condición de pago | Enum | [O] | — | Contado / 15 días / 30 días / 60 días | — |
| Estado | Boolean | — | — | Activo por defecto al crear. Solo el Administrador puede dar de baja | — |

---

## 6. IFrmRegistroCompras — Registro de Compras

| Campo | Tipo | Req | Evento | Regla | Mensaje |
|-------|------|-----|--------|-------|---------|
| Proveedor | Relación | [M] | — | Debe existir en proveedores.txt y estar activo | "Debe seleccionar un proveedor activo." |
| N.° de Factura | String | [M] | BLUR | Formato: F001-XXXXXXXX (alfanumérico, 4–20 caracteres). Único en compras.txt | "Formato de factura inválido." / "Esta factura ya fue registrada." |
| Fecha de compra | Fecha | [M] | BLUR | No puede ser fecha futura. No puede ser anterior a 30 días desde hoy | "La fecha de compra no puede ser futura ni mayor a 30 días." |
| Condición de pago | Enum | [M] | — | Contado / Crédito 15/30/60 días | "Seleccione la condición de pago." |
| Producto (en detalle) | Relación | [M] | — | Debe existir en productos.txt. No puede repetirse en el mismo detalle | "Producto no encontrado." / "Este producto ya fue agregado a la compra." |
| Cantidad comprada | Entero | [M] | BLUR | Entero ≥ 1; máx 9,999 | "La cantidad debe ser un entero positivo mayor a 0." |
| Precio unitario compra | Decimal | [M] | BLUR | > 0.00. Máx 2 decimales. No puede superar 999,999.99 | "El precio debe ser un valor positivo con máximo 2 decimales." |
| (Total) | Decimal | — | AUTO | Calculado automáticamente: suma de (cantidad × precio) de cada ítem | — (no editable) |
| (Guardar) | — | — | SUBMIT | Al menos 1 ítem en el detalle; proveedor seleccionado; factura única | Mostrar resumen de errores |

---

## 7. IFrmDevoluciones — Registro de Devoluciones

| Campo | Tipo | Req | Evento | Regla | Mensaje |
|-------|------|-----|--------|-------|---------|
| N.° de Venta | String | [M] | BLUR | Debe existir en ventas.txt; la venta no puede haber sido devuelta completamente antes | "Venta no encontrada." / "Esta venta ya fue revertida completamente." |
| Producto a devolver | Relación | [M] | — | Solo puede seleccionar productos que estaban en esa venta | "El producto seleccionado no corresponde a esa venta." |
| Cantidad a devolver | Entero | [M] | BLUR | ≥ 1; no puede superar la cantidad original vendida de ese producto | "No puede devolver más unidades de las compradas (máx: X)." |
| Motivo | Enum | [M] | — | Producto dañado / Vencido / Error en pedido / Otro | "Seleccione el motivo de la devolución." |
| Observaciones | String | [O si motivo = Otro] | BLUR | Si motivo = "Otro": obligatorio. 5–200 caracteres | "Ingrese una observación cuando el motivo es 'Otro'." |

---

## 8. IFrmAsistenciaEmpleados — Control de Asistencia

| Campo | Tipo | Req | Evento | Regla | Mensaje |
|-------|------|-----|--------|-------|---------|
| DNI Empleado | String | [M] | BLUR | 8 dígitos numéricos. Debe existir en empleados.txt y estar activo | "Empleado no encontrado." / "El empleado está dado de baja." |
| Tipo de marca | Enum | [M] | — | Entrada / Salida. No se puede marcar dos Entradas seguidas sin Salida | "Ya existe una marca de Entrada sin Salida correspondiente." |
| Fecha | Fecha | [M] | — | No puede ser fecha futura. No puede ser anterior a 3 días | "No se registran asistencias futuras ni con más de 3 días de antigüedad." |
| Hora | Hora | [O] | BLUR | Si se ingresa manualmente: formato HH:mm; Entrada: 05:00–14:00; Salida: posterior a Entrada del mismo día | "Hora fuera del rango permitido." / "La hora de salida debe ser posterior a la entrada." |
| Observación | String | [O] | BLUR | Máx 200 caracteres | "La observación no puede superar los 200 caracteres." |

---

## 9. IFrmEmpleados — Ficha de Empleado

| Campo | Tipo | Req | Evento | Regla | Mensaje |
|-------|------|-----|--------|-------|---------|
| DNI | String | [M] | BLUR | 8 dígitos numéricos. Único en empleados.txt | "El DNI debe tener exactamente 8 dígitos." / "Ya existe un empleado con ese DNI." |
| Nombres | String | [M] | BLUR | 2–50 caracteres. Solo letras, espacios y tildes | "Los nombres son obligatorios (2–50 caracteres)." |
| Apellidos | String | [M] | BLUR | 2–50 caracteres | "Los apellidos son obligatorios (2–50 caracteres)." |
| Cargo | Enum | [M] | — | Cajero / Reponedor / Supervisor / Gerente | "Debe seleccionar un cargo." |
| Fecha de ingreso | Fecha | [M] | BLUR | No puede ser fecha futura. No puede ser anterior al 01/01/2000 | "La fecha de ingreso no puede ser futura ni anterior al año 2000." |
| Sueldo base | Decimal | [M] | BLUR | ≥ 1,025.00 (sueldo mínimo legal Perú 2024). Máx 2 decimales | "El sueldo base no puede ser menor al mínimo legal (S/ 1,025.00)." |
| Turno | Enum | [M] | — | Mañana (6am-2pm) / Tarde (2pm-10pm) / Noche (10pm-6am) | "Debe seleccionar un turno." |
| Teléfono | String | [O] | BLUR | 7–9 dígitos numéricos | "El teléfono debe tener entre 7 y 9 dígitos." |
| Email | String | [O] | BLUR | Formato válido | "Ingrese un correo electrónico válido." |

---

## 10. IFrmBitacora — Consulta de Auditoría

> Este formulario es de **solo lectura**. No tiene campos de entrada de datos.
> Solo tiene filtros para consultar:

| Filtro | Tipo | Req | Regla |
|--------|------|-----|-------|
| Fecha desde | Fecha | [O] | No puede ser posterior a "Fecha hasta" |
| Fecha hasta | Fecha | [O] | No puede ser anterior a "Fecha desde" |
| Usuario | String | [O] | Autocompletado desde usuarios registrados |
| Módulo | Enum | [O] | Lista desplegable con todos los módulos |
| Resultado | Enum | [O] | Éxito / Fallo / Todos |
| (Exportar) | — | — | Siempre exporta todos los registros del filtro actual |

---

## 11. IFrmFinanzasCuentas — Cuentas por Cobrar / Pagar

| Campo | Tipo | Req | Evento | Regla | Mensaje |
|-------|------|-----|--------|-------|---------|
| Tipo | Enum | [M] | — | Por Cobrar / Por Pagar | — |
| Cliente / Proveedor | Relación | [M] | — | Debe existir en el sistema | "Seleccione un cliente o proveedor válido." |
| Monto | Decimal | [M] | BLUR | > 0.00. Máx 2 decimales | "El monto debe ser mayor a 0." |
| Fecha de emisión | Fecha | [M] | — | Por defecto: fecha actual. No editable manualmente | — |
| Fecha de vencimiento | Fecha | [M] | BLUR | Debe ser posterior a la fecha de emisión | "La fecha de vencimiento debe ser posterior a la fecha de emisión." |
| Descripción | String | [M] | BLUR | 5–200 caracteres | "La descripción es obligatoria (5–200 caracteres)." |

---

## 12. Patrón de Implementación Recomendado

```java
// FormValidator.java — validador centralizado para reutilizar
public class FormValidator {
    private List<String> errores = new ArrayList<>();
    private boolean valido = true;

    // Uso:
    public FormValidator validar(String campo, String valor, String regex, String mensaje) {
        if (!valor.matches(regex)) {
            errores.add(mensaje);
            valido = false;
        }
        return this; // permite encadenamiento
    }

    public FormValidator obligatorio(String campo, String valor, String mensaje) {
        if (valor == null || valor.trim().isEmpty()) {
            errores.add(mensaje);
            valido = false;
        }
        return this;
    }

    // Ejemplo de uso en IFrmProveedores:
    public boolean validarProveedor(Proveedor p) {
        return new FormValidator()
            .obligatorio("RUC", p.getRuc(), "El RUC es obligatorio.")
            .validar("RUC", p.getRuc(), "[12]0\\d{9}", "RUC inválido.")
            .obligatorio("Razón Social", p.getRazonSocial(), "La razón social es obligatoria.")
            .isValido();
    }
}
```

---

*Referencia: Kendall & Kendall (2011, Cap. 13) · Wiegers & Beatty (2013) · SWEBOK v4*

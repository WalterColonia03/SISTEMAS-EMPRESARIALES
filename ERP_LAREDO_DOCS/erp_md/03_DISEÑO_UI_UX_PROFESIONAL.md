# 🎨 SISTEMA DE DISEÑO UI/UX PROFESIONAL — ERP LAREDO
**Guía completa de interfaz: de "novato" a ERP de nivel empresarial**

---

## ⚠️ DIAGNÓSTICO: Por qué la UI actual se siente novata

```
PROBLEMAS ACTUALES                    SOLUCIÓN PROFESIONAL
──────────────────────────────────    ──────────────────────────────────
❌ FrmDashboard = 20 botones          ✅ Dashboard con KPIs y gráficos reales
❌ MDI: ventanas flotantes desorden   ✅ Sidebar fijo + área de contenido
❌ Sin jerarquía visual clara         ✅ Sistema tipográfico consistente
❌ Formularios sin feedback visual    ✅ Validación en tiempo real con color
❌ JOptionPane genérico               ✅ Toast notifications profesionales
❌ Sin estados de carga               ✅ Loading indicators por operación
❌ Tablas planas sin interacción      ✅ Tablas con hover, filtro y acciones
❌ Sin consistencia de espaciado      ✅ Grid 8px aplicado en todos los componentes
❌ Colores sin sistema                ✅ Design tokens centralizados en UITokens.java
❌ Iconos inconsistentes              ✅ FlatLaf Icons + biblioteca unificada
```

---

## 1. SISTEMA DE COLORES (Design Tokens)

### 1.1 Paleta Principal

```java
// UITokens.java — ÚNICO archivo de colores en todo el proyecto
// NUNCA hardcodear new Color(r,g,b) fuera de esta clase

public class UITokens {

    // ── PRIMARIOS ──────────────────────────────────────────
    public static final Color PRIMARY         = new Color(0x1565C0); // Azul ERP
    public static final Color PRIMARY_HOVER   = new Color(0x1976D2);
    public static final Color PRIMARY_LIGHT   = new Color(0xE3F2FD); // Fondo tenue
    public static final Color PRIMARY_DARK    = new Color(0x0D47A1);

    // ── SEMÁNTICOS ─────────────────────────────────────────
    public static final Color SUCCESS         = new Color(0x2E7D32); // Verde éxito
    public static final Color SUCCESS_BG      = new Color(0xE8F5E9);
    public static final Color WARNING         = new Color(0xE65100); // Naranja alerta
    public static final Color WARNING_BG      = new Color(0xFFF3E0);
    public static final Color DANGER          = new Color(0xC62828); // Rojo error
    public static final Color DANGER_BG       = new Color(0xFCEBEB);
    public static final Color INFO            = new Color(0x01579B); // Azul info
    public static final Color INFO_BG         = new Color(0xE1F5FE);

    // ── NEUTROS ────────────────────────────────────────────
    public static final Color BG_PAGE         = new Color(0xF4F6F9); // Fondo página
    public static final Color BG_CARD         = new Color(0xFFFFFF); // Fondo tarjeta
    public static final Color BG_SIDEBAR      = new Color(0x1E2A3A); // Sidebar oscuro
    public static final Color BG_SIDEBAR_ITEM = new Color(0x273547);
    public static final Color BG_SIDEBAR_ACT  = new Color(0x1565C0); // Item activo

    // ── TEXTO ──────────────────────────────────────────────
    public static final Color TEXT_PRIMARY    = new Color(0x1A1A2E);
    public static final Color TEXT_SECONDARY  = new Color(0x546E7A);
    public static final Color TEXT_DISABLED   = new Color(0xB0BEC5);
    public static final Color TEXT_ON_DARK    = new Color(0xFFFFFF);
    public static final Color TEXT_ON_PRIMARY = new Color(0xFFFFFF);

    // ── BORDES Y SEPARADORES ───────────────────────────────
    public static final Color BORDER          = new Color(0xE0E0E0);
    public static final Color BORDER_FOCUS    = new Color(0x1565C0);
    public static final Color BORDER_ERROR    = new Color(0xC62828);
    public static final Color DIVIDER         = new Color(0xF0F0F0);

    // ── TABLAS ─────────────────────────────────────────────
    public static final Color TABLE_HEADER    = new Color(0xF5F7FA);
    public static final Color TABLE_ROW_ALT   = new Color(0xFAFBFC);
    public static final Color TABLE_HOVER     = new Color(0xE3F2FD);
    public static final Color TABLE_SELECTED  = new Color(0xBBDEFB);
}
```

### 1.2 Cuándo usar cada color

| Contexto | Color | Ejemplo |
|----------|-------|---------|
| Botón principal de la pantalla | `PRIMARY` | "Guardar", "Procesar Venta" |
| Operación exitosa | `SUCCESS` | "Venta registrada correctamente" |
| Alerta sin bloqueo | `WARNING` | "Stock bajo: 3 unidades" |
| Error o acción destructiva | `DANGER` | "Eliminar proveedor", errores de validación |
| Texto de etiquetas | `TEXT_SECONDARY` | Labels de campos |
| Fondo de toda la ventana | `BG_PAGE` | JFrame principal |
| Fondo de formularios y tarjetas | `BG_CARD` | JPanel con borde redondeado |

---

## 2. TIPOGRAFÍA

```java
// UIFonts.java — Sistema tipográfico completo

public class UIFonts {
    // Fuente base (disponible en todos los OS)
    private static final String FAMILY = "Segoe UI";  // Win
    // Fallback: "SF Pro Text" (Mac), "Inter" (Linux/custom)

    // ── ESCALAS ────────────────────────────────────────────
    public static final Font DISPLAY    = new Font(FAMILY, Font.BOLD,   22); // Títulos de página
    public static final Font H1         = new Font(FAMILY, Font.BOLD,   18); // Encabezados sección
    public static final Font H2         = new Font(FAMILY, Font.BOLD,   15); // Sub-secciones
    public static final Font BODY_LG    = new Font(FAMILY, Font.PLAIN,  14); // Texto cuerpo grande
    public static final Font BODY       = new Font(FAMILY, Font.PLAIN,  13); // Texto cuerpo estándar
    public static final Font BODY_BOLD  = new Font(FAMILY, Font.BOLD,   13); // Énfasis en cuerpo
    public static final Font CAPTION    = new Font(FAMILY, Font.PLAIN,  11); // Notas, ayuda
    public static final Font LABEL      = new Font(FAMILY, Font.BOLD,   12); // Labels de formulario
    public static final Font KPI_VALUE  = new Font(FAMILY, Font.BOLD,   32); // Números del dashboard
    public static final Font KPI_LABEL  = new Font(FAMILY, Font.PLAIN,  12); // Etiqueta del KPI
    public static final Font NAV_ITEM   = new Font(FAMILY, Font.PLAIN,  13); // Items del sidebar
    public static final Font NAV_ITEM_A = new Font(FAMILY, Font.BOLD,   13); // Item activo sidebar

    // ── TAMAÑO DE CAMPOS DE FORMULARIO ─────────────────────
    public static final Font INPUT      = new Font(FAMILY, Font.PLAIN,  13);
    public static final Font BUTTON     = new Font(FAMILY, Font.BOLD,   13);
    public static final Font TABLE_H    = new Font(FAMILY, Font.BOLD,   12); // Header de tabla
    public static final Font TABLE_ROW  = new Font(FAMILY, Font.PLAIN,  13); // Fila de tabla
}
```

**Regla de oro:** nunca uses más de 3 tamaños distintos en una misma pantalla.

---

## 3. ESPACIADO — Grid de 8px

```
Todos los márgenes y paddings deben ser múltiplos de 8:

ESCALA:    4px   8px   16px   24px   32px   48px   64px
USO:       hairline  xs    sm     md     lg     xl     2xl

Ejemplos correctos:
├── Padding interno de un JPanel:      16px (sm) todos los lados
├── Espacio entre campos en formulario: 8px (xs)
├── Margen entre secciones:            24px (md)
├── Padding del sidebar:               16px (sm)
├── Alto de un botón estándar:         40px (fijo)
├── Alto de un input estándar:         36px (fijo)
└── Padding horizontal de botón:       16px mínimo

// UISpacing.java
public class UISpacing {
    public static final int XS  = 4;
    public static final int SM  = 8;
    public static final int MD  = 16;
    public static final int LG  = 24;
    public static final int XL  = 32;
    public static final int XXL = 48;

    // Tamaños fijos de componentes
    public static final int INPUT_HEIGHT  = 36;
    public static final int BUTTON_HEIGHT = 40;
    public static final int SIDEBAR_WIDTH = 220;
    public static final int TOPBAR_HEIGHT = 64;
    public static final int CARD_RADIUS   = 8;
}
```

---

## 4. LAYOUT GENERAL — Reemplazar MDI por Sidebar Navigation

### ❌ PATRÓN ACTUAL (MDI — evitar):
```
┌────────────────────────────────────────────────────┐
│  [Ventana 1 flotante]   [Ventana 2 flotante]       │
│       ┌──────────────┐                            │
│       │ Ventana 3    │                            │
│       └──────────────┘                            │
└────────────────────────────────────────────────────┘
→ Problema: caótico, ventanas se solapan, sin jerarquía
```

### ✅ PATRÓN OBJETIVO (Sidebar + Content Area):
```
┌──────────────────────────────────────────────────────────────┐
│  TOPBAR: Logo LAREDO | [Módulo activo] | [Usuario] [Salir]   │  64px
├────────────┬─────────────────────────────────────────────────┤
│            │                                                 │
│  SIDEBAR   │             CONTENT AREA                       │
│  220px     │             (JPanel dinámico)                  │
│            │                                                 │
│  🏠 Inicio │  ┌─ Título de la sección ──────────────────┐   │
│  📊 Ventas │  │                                         │   │
│  📦 Invent.│  │  [Aquí se carga el IFrm correspondiente]│   │
│  🛒 Compras│  │                                         │   │
│  👥 Clientes│  └─────────────────────────────────────────┘   │
│  💰 Finanzas│                                                │
│  👤 RRHH   │                                                 │
│  🔒 Admin  │                                                 │
│            │                                                 │
└────────────┴─────────────────────────────────────────────────┘
```

### Implementación en Java Swing:
```java
// MainFrame.java — estructura base
public class MainFrame extends JFrame {
    private SidebarPanel sidebar;       // JPanel izquierdo fijo
    private TopBarPanel topBar;         // JPanel superior fijo
    private JPanel contentArea;         // CardLayout para cargar IFrm

    public void navigateTo(String moduleName) {
        // 1. Destacar item activo en sidebar
        sidebar.setActiveItem(moduleName);
        // 2. Cargar el panel correspondiente
        CardLayout cl = (CardLayout) contentArea.getLayout();
        cl.show(contentArea, moduleName);
        // 3. Actualizar topBar con el módulo actual
        topBar.setCurrentModule(moduleName);
    }
}
```

---

## 5. COMPONENTES ESTÁNDAR

### 5.1 Botones — 4 variantes, nunca mezclar

```java
// UIButton.java — fábrica de botones

// PRIMARIO — acción principal de la pantalla (solo 1 por pantalla)
// Fondo: PRIMARY, texto blanco, hover: PRIMARY_HOVER
// Uso: "Guardar", "Registrar Venta", "Procesar Compra"
JButton btnGuardar = UIButton.primary("Guardar");

// SECUNDARIO — acción alternativa
// Fondo: blanco, borde PRIMARY, texto PRIMARY
// Uso: "Cancelar", "Limpiar", "Exportar"
JButton btnCancelar = UIButton.secondary("Cancelar");

// PELIGRO — acciones destructivas, siempre con confirmación
// Fondo: DANGER, texto blanco
// Uso: "Eliminar", "Dar de baja"
JButton btnEliminar = UIButton.danger("Eliminar");

// GHOST / ICONO — acciones secundarias compactas en tablas
// Fondo: transparente, solo ícono + tooltip
// Uso: botones de edición/detalle por fila en tabla
JButton btnEditar = UIButton.icon(IconSet.EDIT, "Editar proveedor");

// REGLA: todos los botones tienen 40px de alto, radio 6px, fuente BUTTON
```

### 5.2 Campos de formulario — validación visual

```java
// UITextField.java — campo con validación visual
public class UITextField extends JPanel {
    private JLabel lblLabel;      // Label arriba del campo
    private JTextField txtInput;  // El campo real
    private JLabel lblError;      // Mensaje de error debajo
    private JLabel lblHint;       // Texto de ayuda opcional

    // Estados visuales:
    // NORMAL:  borde BORDER (gris claro)
    // FOCUS:   borde BORDER_FOCUS (azul)
    // ERROR:   borde BORDER_ERROR (rojo) + lblError visible
    // SUCCESS: borde SUCCESS (verde) + ícono check

    public void setError(String message) {
        txtInput.setBorder(BorderFactory.createLineBorder(UITokens.BORDER_ERROR, 1, true));
        lblError.setText("⚠ " + message);
        lblError.setVisible(true);
    }

    public void clearError() {
        txtInput.setBorder(BorderFactory.createLineBorder(UITokens.BORDER, 1, true));
        lblError.setVisible(false);
    }
}
```

**Diseño de campo correcto:**
```
┌─────────────────────────────────┐
│  Nombre del producto *           │  ← Label (LABEL font, TEXT_SECONDARY)
│ ┌───────────────────────────┐   │
│ │ Ingrese el nombre...       │   │  ← Input (36px alto, BORDER radius 6px)
│ └───────────────────────────┘   │
│  ⚠ El nombre es obligatorio     │  ← Error (CAPTION font, DANGER color)
└─────────────────────────────────┘
```

### 5.3 Tablas — profesionales con interacción

```java
// UITable.java — tabla estándar ERP

public class UITable extends JTable {

    @Override
    public Component prepareRenderer(TableCellRenderer r, int row, int col) {
        Component c = super.prepareRenderer(r, row, col);

        // 1. Filas alternas
        if (!isRowSelected(row)) {
            c.setBackground(row % 2 == 0
                ? UITokens.BG_CARD
                : UITokens.TABLE_ROW_ALT);
        }
        // 2. Hover effect (listener en mouseMotion)
        if (row == hoveredRow) {
            c.setBackground(UITokens.TABLE_HOVER);
        }
        // 3. Seleccionado
        if (isRowSelected(row)) {
            c.setBackground(UITokens.TABLE_SELECTED);
        }
        return c;
    }
}

// Estructura de columna de acciones (última columna):
// [Editar 🖊]  [Ver 👁]  [Eliminar 🗑]
// Usando UIButton.icon() — NO JButton plano
```

### 5.4 Toast Notifications — reemplazar JOptionPane

```java
// ToastNotification.java — notificaciones no bloqueantes

// USO:
ToastNotification.success("Venta registrada correctamente");
ToastNotification.error("El producto no tiene stock suficiente");
ToastNotification.warning("Stock crítico: Arroz Gloria → 2 unidades");
ToastNotification.info("Tipo de cambio actualizado: S/ 3.72");

// Aparece: esquina inferior derecha, 3 segundos, animación fade
// NO bloquea la interacción del usuario
// Máximo 3 toasts apilados simultáneamente
// El usuario puede cerrarlos manualmente con ×

// Cuándo usar JOptionPane (solo casos excepcionales):
// - Confirmación de eliminación (requiere respuesta explícita)
// - Error crítico que impide continuar
// - Nunca para feedback de éxito o información
```

### 5.5 Estados de carga — Loading indicators

```java
// Operación rápida (< 1s): sin indicador
// Operación media (1-3s): spinner en el botón
btnGuardar.setEnabled(false);
btnGuardar.setText("Guardando...");
// → al terminar: restaurar

// Operación lenta (> 3s): overlay en el panel
LoadingOverlay.show(contentArea, "Generando reporte PDF...");
// → al terminar:
LoadingOverlay.hide(contentArea);
```

---

## 6. DISEÑO DEL DASHBOARD KPI (FR-001, FR-002)

### Estructura visual del Dashboard real:

```
┌─────────────────────────────────────────────────────────────────┐
│  📊 Dashboard — Minimarket LAREDO           Lunes 15 Jun 2026   │
├──────────┬──────────┬──────────┬──────────────────────────────┤
│  KPI CARD │  KPI CARD │  KPI CARD │           KPI CARD          │
│           │           │           │                             │
│  VENTAS   │  VENTAS   │  VENTAS   │    ALERTAS STOCK            │
│   HOY     │ SEMANA    │   MES     │    ─────────────            │
│  S/245.50 │ S/1,823   │ S/7,420   │    🔴 Arroz × 3 uds.       │
│  ↑ 12%    │  ↑ 5%     │  ↓ 3%    │    🔴 Azúcar × 1 ud.       │
│  vs ayer  │  vs s.ant │  vs s.ant │    🟡 Fideos × 5 uds.      │
├──────────┴──────────┴──────────┴──────────────────────────────┤
│  GRÁFICO DE VENTAS (7 días)     │  TOP 5 PRODUCTOS HOY         │
│                                 │                              │
│  S/ │▄   ▄ ▄  ▄  ▄            │  1. Arroz Gloria   S/89.00   │
│     │ ▄▄   ▄▄  ▄▄  ▄          │  2. Aceite Primor  S/62.50   │
│     └─────────────────────     │  3. Gaseosa        S/45.00   │
│      L  M  X  J  V  S  D      │  4. Pan de molde   S/38.00   │
│                                 │  5. Leche Gloria   S/34.00   │
├────────────────────────────────┴──────────────────────────────┤
│  ACCESO RÁPIDO:  [+ Nueva Venta]  [+ Registrar Compra]  [📋 Reportes] │
└─────────────────────────────────────────────────────────────────┘
```

### KPI Card — código base:
```java
// KPICard.java
public class KPICard extends JPanel {
    public KPICard(String title, String value, String trend, TrendType type) {
        setBackground(UITokens.BG_CARD);
        setBorder(new RoundedBorder(UITokens.BORDER, UISpacing.CARD_RADIUS));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UIFonts.KPI_LABEL);
        lblTitle.setForeground(UITokens.TEXT_SECONDARY);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(UIFonts.KPI_VALUE);
        lblValue.setForeground(UITokens.TEXT_PRIMARY);

        JLabel lblTrend = new JLabel(trend);
        lblTrend.setFont(UIFonts.CAPTION);
        lblTrend.setForeground(type == TrendType.UP
            ? UITokens.SUCCESS : UITokens.DANGER);

        // Layout: title arriba, value en el centro grande, trend abajo
    }
}
```

---

## 7. DISEÑO DE FORMULARIOS — Patrón estándar

### Antes vs Después:

```
❌ FORMULARIO ACTUAL (novato):           ✅ FORMULARIO ERP (profesional):
─────────────────────────────            ─────────────────────────────────
JLabel: "Nombre:"  JTextField            ┌─ Registrar Producto ────────────┐
JLabel: "Precio:"  JTextField            │  ← Volver al listado            │
JLabel: "Stock:"   JTextField            ├─────────────────────────────────┤
                                         │  Información básica              │
JButton: "Guardar" JButton: "Cancelar"   │                                 │
                                         │  Código *          Categoría *   │
Problemas:                               │  [__________]      [▼ Abarrotes] │
- Labels en línea con campos             │                                  │
- Sin jerarquía visual                   │  Nombre del producto *           │
- Sin secciones                          │  [________________________________]│
- Sin campos obligatorios marcados       │                                  │
- Sin validación visual                  │  Precio venta (S/.) * Stock ini.*│
- Botones sin estilo                     │  [__________]      [___________] │
                                         │                                  │
                                         │  Stock mínimo       Vencimiento  │
                                         │  [__________]      [__/__/____]  │
                                         │                                  │
                                         │  Descripción                     │
                                         │  [________________________________]│
                                         │  [________________________________]│
                                         ├─────────────────────────────────┤
                                         │  [Cancelar]          [Guardar ✓]│
                                         └─────────────────────────────────┘
```

### Reglas de formularios profesionales:
1. **Título claro** con breadcrumb (dónde estoy)
2. **Campos agrupados** por sección lógica con subtítulos
3. **Label ARRIBA del campo**, nunca a la izquierda
4. **Campos obligatorios** marcados con `*` (leyenda al pie)
5. **Validación en tiempo real** al salir del campo (no solo al guardar)
6. **Botón primario a la derecha**, secundario a la izquierda
7. **Un solo botón primario** por formulario
8. **Placeholder** como guía, nunca como etiqueta
9. **Anchura de campo proporcional** al dato esperado (DNI ≠ Descripción)
10. **Estado disabled** visual claro para campos no editables

---

## 8. DISEÑO DE LISTADOS / TABLAS

### Patrón estándar de pantalla de gestión:
```
┌─ Gestión de Productos ──────────────────────────────────────────┐
│                                                    [+ Nuevo]     │
├──────────────────────────────────────────────────────────────────┤
│  Filtros: [Buscar por nombre/código...]  [Categoría ▼]  [🔍]     │
├──────────┬──────────────┬──────────┬──────────┬────────────────┤
│  Código  │  Nombre      │  Precio  │  Stock   │  Acciones      │
├──────────┼──────────────┼──────────┼──────────┼────────────────┤
│  P001    │  Arroz Gloria│  S/ 2.50 │  🔴 3    │  [✏] [👁] [🗑]│
│  P002    │  Aceite      │  S/ 8.90 │  🟢 24   │  [✏] [👁] [🗑]│
│  P003    │  Azúcar      │  S/ 2.10 │  🔴 1    │  [✏] [👁] [🗑]│
├──────────┴──────────────┴──────────┴──────────┴────────────────┤
│  Mostrando 3 de 47 productos          [← 1 2 3 ... 10 →]       │
└──────────────────────────────────────────────────────────────────┘

Stock 🔴 = stock ≤ stockMinimo  |  🟢 = stock normal  |  🟡 = stock medio
```

---

## 9. SIDEBAR — Navegación Principal

```
┌────────────────────┐
│  🏪 LAREDO ERP     │  ← Logo + nombre (20px fuente)
├────────────────────┤
│  ─── OPERACIONES   │  ← Separador de sección (caption, gris)
│                    │
│  🏠  Inicio        │  ← Item normal
│  💰  Ventas     ←  │  ← Item ACTIVO (fondo PRIMARY, texto blanco)
│  📦  Inventario    │
│  🛒  Compras       │
├────────────────────┤
│  ─── GESTIÓN       │
│  👥  Clientes      │
│  🚚  Proveedores   │
│  👤  RRHH          │
│  💹  Finanzas      │
├────────────────────┤
│  ─── SISTEMA       │
│  📊  Reportes      │
│  🔒  Seguridad     │
│  ⚙️   Configuración │
├────────────────────┤
│  🟢  Juan Pérez    │  ← Usuario activo
│  Gerente           │  ← Rol
│  [Cerrar sesión]   │  ← Botón ghost pequeño
└────────────────────┘

Reglas del sidebar:
- Ancho fijo: 220px
- Fondo oscuro: BG_SIDEBAR (#1E2A3A)
- Item activo: BG_SIDEBAR_ACT + fuente BOLD
- Hover: BG_SIDEBAR_ITEM (ligeramente más claro)
- Iconos: 18px, alineados con el texto
- Separadores de sección: texto en mayúsculas, CAPTION font, TEXT_SECONDARY
```

---

## 10. PANTALLA DE LOGIN — Primer contacto con el usuario

```
┌─────────────────────────────────────────────────────────────────┐
│                                                                 │
│                  ┌───────────────────────────┐                 │
│                  │                           │                 │
│                  │  🏪 Minimarket LAREDO      │                 │
│                  │  Sistema ERP v2.0          │                 │
│                  │                           │                 │
│                  │  Usuario                  │                 │
│                  │  [_____________________]  │                 │
│                  │                           │                 │
│                  │  Contraseña               │                 │
│                  │  [___________________🔒]  │                 │
│                  │                           │                 │
│                  │  [    Iniciar sesión    ]  │                 │
│                  │                           │                 │
│                  │  ¿Olvidaste tu contraseña?│                 │
│                  │                           │                 │
│                  │  ─────────────────────── │                 │
│                  │  UPAO · Sistemas Emp. 2026│                 │
│                  └───────────────────────────┘                 │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘

Fondo: degradado BG_PAGE → PRIMARY_LIGHT
Card central: BG_CARD, sombra suave, radio 12px
Ancho card: 380px, centrado horizontal y vertical
```

---

## 11. CHECKLIST DE REVISIÓN UI/UX POR PANTALLA

Antes de marcar cualquier IFrm como "HECHO", verificar:

- [ ] ¿Usa solo colores de `UITokens`? (cero hardcoded)
- [ ] ¿Usa solo fuentes de `UIFonts`?
- [ ] ¿Spacing sigue la escala de 8px?
- [ ] ¿Campos tienen label ARRIBA y mensaje de error abajo?
- [ ] ¿Hay un solo botón primario por pantalla?
- [ ] ¿El botón primario está a la derecha?
- [ ] ¿Operaciones destructivas piden confirmación?
- [ ] ¿El feedback de éxito usa Toast, no JOptionPane?
- [ ] ¿La tabla tiene filas alternas + hover + columna de acciones?
- [ ] ¿Hay estado de carga para operaciones > 1 segundo?
- [ ] ¿El formulario tiene título claro + secciones agrupadas?
- [ ] ¿Campos obligatorios marcados con `*`?
- [ ] ¿El sidebar refleja el módulo activo?
- [ ] ¿La pantalla funciona igual con el rol Cajero y con Gerente (RBAC visual)?

---

## 12. ANTI-PATRONES — Cosas que NUNCA hacer

```java
// ❌ NUNCA: colores hardcodeados
label.setForeground(new Color(255, 0, 0));
// ✅ SIEMPRE:
label.setForeground(UITokens.DANGER);

// ❌ NUNCA: JOptionPane para éxito
JOptionPane.showMessageDialog(this, "Guardado correctamente");
// ✅ SIEMPRE:
ToastNotification.success("Producto guardado correctamente");

// ❌ NUNCA: bordes de ventana dentro de ventana (MDI caótico)
JInternalFrame iframe = new JInternalFrame("Productos");
desktop.add(iframe);
// ✅ SIEMPRE: CardLayout en el content area
cl.show(contentArea, "productos");

// ❌ NUNCA: label como placeholder
lblNombre.setText("Nombre del producto:");
txtNombre.setText("Ingrese nombre...");
// ✅ SIEMPRE: UITextField con placeholder configurado
UITextField fldNombre = new UITextField("Nombre del producto", "Ingrese nombre...", true);

// ❌ NUNCA: botones sin jerarquía visual
JButton btn1 = new JButton("Guardar");
JButton btn2 = new JButton("Cancelar");
// ✅ SIEMPRE: jerarquía clara
JButton btnGuardar  = UIButton.primary("Guardar");
JButton btnCancelar = UIButton.secondary("Cancelar");

// ❌ NUNCA: tabla con altura de fila por defecto (muy comprimida)
// ✅ SIEMPRE:
table.setRowHeight(40);
```

---

*Referencia: Material Design · SAP Fiori Design Guidelines · Fluent UI · Sesión 10 (Optimización ERP)*

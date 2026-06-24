# PROMPT MAESTRO — Rediseño UI/UX del ERP "Minimarket LAREDO" (Java Swing)

> **Cómo usar este documento:** copia y pega este archivo completo como instrucción/system prompt para la IA que va a ejecutar el rediseño (puede ser Claude, GPT, Copilot, etc. trabajando directamente sobre el código Java). Está escrito para que esa IA tenga cero ambigüedad: qué tono visual lograr, qué tokens de diseño usar, qué código base reutilizar y qué cambiar pantalla por pantalla, sin tocar la lógica de negocio existente.

---

## 0. Rol y contexto para la IA ejecutora

Actúa como **diseñador/a UI senior especializado en software empresarial (ERP) sobre Java Swing**, con experiencia llevando aplicaciones de escritorio "estilo NetBeans GUI Builder de los 2010" a una estética **2024-2026 de software corporativo real**: SAP Fiori, Oracle NetSuite, Microsoft Dynamics 365 Business Central y Defontana (ERP chileno).

El proyecto es un sistema de punto de venta / ERP para un minimarket ("Minimarket LAREDO"), escrito en **Java Swing puro**, con persistencia en archivos `.txt` (clases `ArchivosTXT.*`), paquete `Vista` para las pantallas, y dos estilos de construcción de UI conviviendo en el mismo proyecto:

1. **Pantallas generadas por el GUI Builder de NetBeans** (`ActulizarStock`, `GestionarCategoria`, `GestionarClientes`, `GestionarProductos`, `GestionarUsuarios`, `GestionarVentas`): usan `org.netbeans.lib.awtextra.AbsoluteLayout`, coordenadas absolutas, bloques `//GEN-BEGIN/GEN-END` que NetBeans regenera, y archivos `.form` asociados.
2. **Pantallas escritas a mano** (`FrmLogin`, `FrmDashboard`, `IFrmAlertasInventario`, `IFrmBitacoraAuditoria`, `IFrmConfiguracionERP`, `IFrmCuentasCobrarPagar`, `IFrmDevoluciones`): usan `BorderLayout`, `GridBagLayout`, `BoxLayout`, ya sin `.form`.

**Regla de oro, no negociable:** este es un encargo de **rediseño visual**, no de reingeniería funcional. En cada clase:

- **NUNCA** cambies la lógica dentro de los métodos `*ActionPerformed`, `cargarTabla()`, `cargarProductos()`, `cargarClientes()`, validaciones de `KeyTyped`, lectura/escritura de archivos TXT, ni las firmas de los métodos.
- **NUNCA** renombres variables de instancia que ya existen (aunque tengan errores de tipeo, ej. `txt_descipcion`, `jButton_Actulizar`, `ActulizarStock`) — pueden estar referenciadas desde otros lados que no ves en este encargo.
- Para las pantallas con `.form` (NetBeans), si el ejecutor reescribe `initComponents()` a mano dejará de ser editable visualmente en el GUI Builder de NetBeans — **esto es aceptable y esperado** en este rediseño (se sacrifica el editor visual de NetBeans por código propio con el nuevo sistema de diseño), pero el bloque debe seguir compilando como método normal (puedes quitar los comentarios `//GEN-BEGIN/GEN-END` si reescribes a mano, o conservarlos si prefieres mantener compatibilidad parcial).
- **SÍ** puedes y debes: reemplazar layouts, colores, fuentes, bordes, tamaños, agregar/quitar paneles puramente visuales, reorganizar la jerarquía de componentes, agregar iconografía, mejorar mensajes de validación a nivel visual, y refactorizar la construcción de la UI en métodos auxiliares más limpios.

---

## 1. Dirección de diseño — qué estilo lograr (y qué evitar)

### 1.1 Referencias y qué tomar de cada una

No copiamos pantallas literales (eso sería clonar interfaces con derechos de marca) — extraemos **principios de lenguaje visual**:

| Referencia | Qué tomar |
|---|---|
| **SAP Fiori** | Diseño plano (flat), tarjetas blancas con acento de color mínimo, tipografía limpia y compacta, densidad de información alta pero ordenada en grillas, iconografía lineal monocromática, azul corporativo como color de acción primaria. |
| **Microsoft Dynamics 365 / Fluent** | Barra de comandos (command bar) con acciones agrupadas arriba de las tablas, paneles de detalle a la derecha tipo "record form", breadcrumbs, uso generoso de espacio en blanco, esquinas levemente redondeadas. |
| **Oracle NetSuite** | Paneles ("portlets") tipo dashboard con KPIs en tarjetas pequeñas, listas con filas alternadas (zebra), badges de estado coloreados (Pendiente/Pagado/Parcial), barra lateral de navegación densa pero escaneable. |
| **Defontana** | Paleta más cercana al contexto local (azules/grises sobrios), formularios de alta densidad pero con agrupación clara por secciones, botones de acción primaria siempre en la esquina superior derecha del bloque que afectan. |

### 1.2 Principios transversales (aplican a las 20 pantallas)

1. **Plano, no plástico**: cero gradientes, cero sombras pesadas, cero relieve 3D tipo Windows 98/Java Metal. Superficies blancas sobre fondo gris muy claro, separadas por bordes de 1px, no por sombras.
2. **Una sola escala tipográfica reutilizada en todo el sistema** (sección 2.2) — nada de tamaños "al ojo" distintos en cada pantalla como ocurre hoy (hay `Font("Tahoma", 1, 18)`, `Font("Segoe UI", 1, 14)`, `Font("Tahoma", 0, 14)` mezclados sin criterio).
3. **Jerarquía por tipografía y espacio, no por mayúsculas ni colores saturados**. Hoy varias pantallas gritan con botones verde/rojo saturado (`new Color(0,153,0)`, `new Color(153,0,0)`) — eso se reserva solo para estados semánticos reales (éxito/peligro), no para "Actualizar" cotidiano.
4. **Cero emojis como iconografía de producto** (`🛒`, `👥`, `⭐`, etc. en `FrmDashboard`). Los emojis renderizan distinto en cada sistema operativo/fuente y se ven poco profesionales en un ERP. Reemplazar por un set de iconos lineales consistente (sección 3.3).
5. **Cero `AbsoluteLayout`** en el resultado final. Todo con `GridBagLayout`, `BorderLayout`, `BoxLayout` o `MigLayout` (si se agrega la librería) para que las ventanas internas (`JInternalFrame`) puedan redimensionarse sin romperse.
6. **Cero ventanas de alerta modal (`JOptionPane`) para validaciones triviales de campo** ("Ingrese una descripción", "Solo se permiten letras"). Esas pasan a ser texto de ayuda/error inline bajo el campo. `JOptionPane` se reserva para confirmaciones destructivas (eliminar) y mensajes de éxito puntuales — y aun esos, restyleados (sección 3.5).
7. **Toda tabla de datos es una "lista enterprise"**: encabezado gris claro, filas con leve zebra, sin líneas de grilla verticales, selección con tinte azul muy sutil, columna de acciones por fila cuando aplique.
8. **Todo formulario de detalle usa el patrón "label arriba, campo abajo"**, agrupado en tarjetas con título de sección — nunca "label a la izquierda pegado al campo" en coordenadas absolutas como hoy.

---

## 2. Sistema de diseño (Design Tokens)

Esto es la **fuente única de verdad**. Todas las pantallas deben consumir estos valores desde una clase central (sección 3.2), nunca hardcodear colores/fuentes sueltos de nuevo.

### 2.1 Paleta de color

```
PRIMARY        #1B3B6F   // Azul marino — sidebar, encabezados de marca
PRIMARY_DARK   #142B52   // Hover/pressed sobre PRIMARY
ACCENT         #2D6CDF   // Azul de acción — botones primarios, enlaces, foco
ACCENT_HOVER   #1E54B7   // Hover sobre ACCENT
BG_APP         #F5F6F8   // Fondo general de ventanas y paneles contenedores
BG_CARD        #FFFFFF   // Fondo de tarjetas, tablas, formularios
BORDER         #E1E4E8   // Bordes y separadores de 1px
TEXT_PRIMARY   #1F2733   // Texto principal
TEXT_SECONDARY #5B6472   // Texto secundario, labels, captions
SUCCESS        #1E8E5A   // Confirmaciones, estado "Pagado/Activo"
WARNING        #F2994A   // Alertas medias, estado "Parcial/Por vencer"
DANGER         #D64550   // Errores, estado "Vencido/Stock crítico", acción eliminar
INFO           #2D6CDF   // Igual a ACCENT, para badges informativos
```

Regla semántica: **SUCCESS/WARNING/DANGER se usan solo para representar estado de datos** (badges de tabla, alertas de stock, mensajes de resultado). Los botones de acción cotidiana (Guardar/Actualizar/Buscar/Filtrar) usan **ACCENT**, nunca verde. El botón "Eliminar" usa **DANGER**, pero como botón secundario/outline, no relleno sólido — el rojo sólido se reserva para el botón de "Confirmar eliminación" dentro del diálogo de confirmación.

### 2.2 Tipografía

Fuente base: **"Segoe UI"** (estándar en Windows, que es la plataforma objetivo de este Swing). Fallback lógico `"SansSerif"` si no está disponible (no usar `"Tahoma"`, que es la fuente que hoy ensucia el sistema visual con look Windows XP).

```
H1            Segoe UI, BOLD, 22  — título de pantalla/módulo
H2            Segoe UI, BOLD, 16  — título de sección/tarjeta
SUBTITLE      Segoe UI, PLAIN, 13 — texto de apoyo bajo H1/H2 (color TEXT_SECONDARY)
BODY          Segoe UI, PLAIN, 13 — texto de tabla, inputs, contenido general
BODY_BOLD     Segoe UI, BOLD, 13  — énfasis dentro de BODY (totales, valores clave)
LABEL         Segoe UI, BOLD, 11  — etiquetas de campo de formulario (mayúscula opcional, TEXT_SECONDARY)
CAPTION       Segoe UI, PLAIN, 11 — ayudas, contadores, pies de página
KPI_VALUE     Segoe UI, BOLD, 26  — número grande en tarjetas KPI
```

### 2.3 Espaciado (grilla de 4px)

```
SPACE_XS = 4    SPACE_SM = 8    SPACE_MD = 16    SPACE_LG = 24    SPACE_XL = 32
```

Todo `insets`, `EmptyBorder` y separación entre componentes debe ser un múltiplo de 4. Alturas estándar de componentes interactivos: **36px** (inputs, botones estándar), **40px** (botón primario destacado / search bar), **44px** (botón de navegación lateral).

### 2.4 Bordes, radios y "elevación"

No hay sombras (diseño plano). La separación entre superficies se logra con:
- Borde `1px BORDER` alrededor de tarjetas/tablas/paneles.
- Esquinas levemente redondeadas: **radio 8px** en botones/inputs/tarjetas, **radio 999 (pill)** en badges de estado y barra de scroll. Esto se logra con FlatLaf (sección 3.1) vía la propiedad de cliente `arc`.

### 2.5 Iconografía

Sustituir todos los emojis por un set lineal consistente. Dos rutas válidas (elegir una y ser consistente en las 20 pantallas):

- **Opción A (recomendada, cero dependencias nuevas):** usar `FlatSVGIcon` de FlatLaf cargando un set de iconos SVG propios (16x16 / 20x20) guardados en `/resources/icons/` — trazo simple de 1.5-2px, sin relleno, estilo "outline" (carrito, usuarios, devolución, estrella, auditoría, gráfico, engranaje, candado).
- **Opción B (más rápida de implementar):** agregar la librería **Ikonli** (`org.kordamp.ikonli`) con el pack `ikonli-feather-pack` o `ikonli-material2-pack`, que permite instanciar iconos como fuente vectorial con una sola línea: `FontIcon.of(Feather.SHOPPING_CART, 18, UIKit.TEXT_SECONDARY)`.

---

## 3. Fundamentos técnicos obligatorios

### 3.1 Look & Feel base: FlatLaf

El proyecto debe adoptar **FlatLaf** (`com.formdev:flatlaf`, Maven Central, Apache 2.0) como Look & Feel base. Es la vía más directa para que Swing deje de verse "Java 2005" y soporte radios de esquina, estilos por componente vía client properties, y consistencia entre Windows/Mac/Linux.

**Dependencia (Maven):**
```xml
<dependency>
    <groupId>com.formdev</groupId>
    <artifactId>flatlaf</artifactId>
    <version>3.4</version>
</dependency>
```
*(Si el proyecto usa solo classpath de NetBeans sin Maven/Gradle, descargar el JAR desde el sitio oficial de FlatLaf y agregarlo a Libraries del proyecto.)*

**Inicialización — en el primer `main()` que se ejecute (hoy está en `FrmDashboard` y en `FrmLogin`; debe quedar SOLO en el punto de entrada real de la app):**
```java
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;

public static void main(String[] args) {
    FlatLightLaf.setup();

    // Radios y overrides globales del sistema de diseño
    UIManager.put("Button.arc", 8);
    UIManager.put("Component.arc", 8);
    UIManager.put("TextComponent.arc", 8);
    UIManager.put("CheckBox.arc", 4);
    UIManager.put("ScrollBar.thumbArc", 999);
    UIManager.put("ScrollBar.width", 10);
    UIManager.put("TabbedPane.showTabSeparators", true);
    UIManager.put("Table.showHorizontalLines", false);
    UIManager.put("Table.showVerticalLines", false);
    UIManager.put("Table.rowHeight", 34);
    UIManager.put("defaultFont", Vista.Estilos.UIKit.BODY);

    javax.swing.SwingUtilities.invokeLater(() -> new Vista.FrmLogin().setVisible(true));
}
```

### 3.2 Clase central de tokens + fábrica de componentes (`UIKit`)

Crear el paquete **`Vista.Estilos`** con una clase `UIKit` que centraliza TODO lo de la sección 2. Ninguna pantalla vuelve a escribir `new Color(0,153,0)` o `new Font("Tahoma", 1, 18)` directamente: siempre referencia `UIKit.ACCENT`, `UIKit.H1`, etc., y usa las fábricas (`UIKit.primaryButton(...)`) en vez de `new JButton(...)` + estilizado manual repetido.

```java
package Vista.Estilos;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Sistema de diseño centralizado del ERP Minimarket LAREDO.
 * Toda pantalla nueva o rediseñada debe construir su UI usando
 * estos tokens y fábricas — nunca colores/fuentes sueltos.
 */
public final class UIKit {

    private UIKit() {}

    // ===================== PALETA =====================
    public static final Color PRIMARY        = new Color(0x1B3B6F);
    public static final Color PRIMARY_DARK    = new Color(0x142B52);
    public static final Color ACCENT          = new Color(0x2D6CDF);
    public static final Color ACCENT_HOVER    = new Color(0x1E54B7);
    public static final Color BG_APP          = new Color(0xF5F6F8);
    public static final Color BG_CARD         = Color.WHITE;
    public static final Color BORDER          = new Color(0xE1E4E8);
    public static final Color TEXT_PRIMARY    = new Color(0x1F2733);
    public static final Color TEXT_SECONDARY  = new Color(0x5B6472);
    public static final Color SUCCESS         = new Color(0x1E8E5A);
    public static final Color WARNING         = new Color(0xF2994A);
    public static final Color DANGER          = new Color(0xD64550);
    public static final Color INFO            = ACCENT;

    // ===================== TIPOGRAFÍA =====================
    private static final String FAM = "Segoe UI";
    public static final Font H1        = new Font(FAM, Font.BOLD, 22);
    public static final Font H2        = new Font(FAM, Font.BOLD, 16);
    public static final Font SUBTITLE  = new Font(FAM, Font.PLAIN, 13);
    public static final Font BODY      = new Font(FAM, Font.PLAIN, 13);
    public static final Font BODY_BOLD = new Font(FAM, Font.BOLD, 13);
    public static final Font LABEL     = new Font(FAM, Font.BOLD, 11);
    public static final Font CAPTION   = new Font(FAM, Font.PLAIN, 11);
    public static final Font KPI_VALUE = new Font(FAM, Font.BOLD, 26);

    // ===================== ESPACIADO =====================
    public static final int SPACE_XS = 4, SPACE_SM = 8, SPACE_MD = 16, SPACE_LG = 24, SPACE_XL = 32;

    // ===================== BOTONES =====================
    public static JButton primaryButton(String text) {
        JButton b = baseButton(text);
        b.setBackground(ACCENT);
        b.setForeground(Color.WHITE);
        b.putClientProperty(FlatClientProperties.STYLE,
                "arc: 8; focusWidth: 0; hoverBackground: " + hex(ACCENT_HOVER) + ";");
        return b;
    }

    public static JButton secondaryButton(String text) {
        JButton b = baseButton(text);
        b.setBackground(BG_CARD);
        b.setForeground(TEXT_PRIMARY);
        b.putClientProperty(FlatClientProperties.STYLE,
                "arc: 8; borderWidth: 1; borderColor: " + hex(BORDER) + "; focusWidth: 0;");
        return b;
    }

    public static JButton dangerOutlineButton(String text) {
        JButton b = baseButton(text);
        b.setBackground(BG_CARD);
        b.setForeground(DANGER);
        b.putClientProperty(FlatClientProperties.STYLE,
                "arc: 8; borderWidth: 1; borderColor: " + hex(DANGER) + "; focusWidth: 0;");
        return b;
    }

    public static JButton dangerSolidButton(String text) {
        JButton b = baseButton(text);
        b.setBackground(DANGER);
        b.setForeground(Color.WHITE);
        b.putClientProperty(FlatClientProperties.STYLE, "arc: 8; focusWidth: 0;");
        return b;
    }

    private static JButton baseButton(String text) {
        JButton b = new JButton(text);
        b.setFont(BODY_BOLD);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMargin(new Insets(8, 18, 8, 18));
        b.setPreferredSize(new Dimension(b.getPreferredSize().width, 36));
        return b;
    }

    // ===================== CAMPOS DE FORMULARIO =====================
    public static JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text.toUpperCase());
        l.setFont(LABEL);
        l.setForeground(TEXT_SECONDARY);
        return l;
    }

    public static JTextField textField() {
        JTextField tf = new JTextField();
        tf.setFont(BODY);
        tf.putClientProperty(FlatClientProperties.STYLE,
                "arc: 8; borderColor: " + hex(BORDER) + "; focusedBorderColor: " + hex(ACCENT) + ";");
        tf.setPreferredSize(new Dimension(0, 36));
        return tf;
    }

    /** Campo de solo lectura (reemplaza el patrón "JTextField deshabilitado gris" actual). */
    public static JTextField readOnlyField() {
        JTextField tf = textField();
        tf.setEditable(false);
        tf.setBackground(BG_APP);
        tf.setForeground(TEXT_SECONDARY);
        return tf;
    }

    // ===================== TARJETAS / SECCIONES =====================
    /** Tarjeta blanca con borde sutil, usada para envolver tablas, formularios y bloques. */
    public static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(BG_CARD);
        p.putClientProperty(FlatClientProperties.STYLE, "arc: 10; [light]borderColor: " + hex(BORDER) + ";");
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(SPACE_MD, SPACE_MD, SPACE_MD, SPACE_MD)));
        return p;
    }

    /** Encabezado estándar de una tarjeta de sección (título + opcional acción a la derecha). */
    public static JPanel sectionHeader(String title, JComponent trailingAction) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel lbl = new JLabel(title);
        lbl.setFont(H2);
        lbl.setForeground(TEXT_PRIMARY);
        header.add(lbl, BorderLayout.WEST);
        if (trailingAction != null) header.add(trailingAction, BorderLayout.EAST);
        header.setBorder(new EmptyBorder(0, 0, SPACE_SM, 0));
        return header;
    }

    /** Encabezado de pantalla: H1 + subtítulo/breadcrumb. */
    public static JPanel screenHeader(String titulo, String breadcrumb) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel lblBreadcrumb = new JLabel(breadcrumb);
        lblBreadcrumb.setFont(CAPTION);
        lblBreadcrumb.setForeground(TEXT_SECONDARY);
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(H1);
        lblTitulo.setForeground(TEXT_PRIMARY);
        p.add(lblBreadcrumb);
        p.add(lblTitulo);
        p.setBorder(new EmptyBorder(0, 0, SPACE_MD, 0));
        return p;
    }

    // ===================== TARJETA KPI =====================
    public static JPanel kpiCard(String label, String valor, String subLabel, Color acento) {
        JPanel card = card();
        card.setLayout(new BorderLayout(0, SPACE_XS));

        JPanel barra = new JPanel();
        barra.setBackground(acento);
        barra.setPreferredSize(new Dimension(4, 0));

        JPanel contenido = new JPanel();
        contenido.setOpaque(false);
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));

        JLabel lblLabel = new JLabel(label.toUpperCase());
        lblLabel.setFont(LABEL);
        lblLabel.setForeground(TEXT_SECONDARY);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(KPI_VALUE);
        lblValor.setForeground(TEXT_PRIMARY);

        JLabel lblSub = new JLabel(subLabel);
        lblSub.setFont(CAPTION);
        lblSub.setForeground(TEXT_SECONDARY);

        contenido.add(lblLabel);
        contenido.add(Box.createVerticalStrut(SPACE_XS));
        contenido.add(lblValor);
        contenido.add(lblSub);

        card.add(barra, BorderLayout.WEST);
        card.add(contenido, BorderLayout.CENTER);
        return card;
    }

    // ===================== BADGE DE ESTADO =====================
    public static JLabel statusBadge(String texto, Color color) {
        JLabel badge = new JLabel(texto);
        badge.setFont(CAPTION);
        badge.setForeground(color);
        badge.setOpaque(false);
        badge.setHorizontalAlignment(SwingConstants.CENTER);
        badge.setBorder(new EmptyBorder(3, 10, 3, 10));
        badge.putClientProperty(FlatClientProperties.STYLE,
                "arc: 999; background: " + hex(mezclarConBlanco(color, 0.85f)) + ";");
        badge.setOpaque(true);
        return badge;
    }

    // ===================== TABLAS =====================
    public static JTable styledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(BODY);
        table.setRowHeight(34);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(mezclarConBlanco(ACCENT, 0.88f));
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(LABEL);
        header.setBackground(BG_APP);
        header.setForeground(TEXT_SECONDARY);
        header.setPreferredSize(new Dimension(0, 38));
        header.setReorderingAllowed(false);

        // Zebra striping
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? BG_CARD : mezclarConBlanco(BG_APP, 0.4f));
                }
                setBorder(new EmptyBorder(0, SPACE_SM, 0, SPACE_SM));
                return c;
            }
        });
        return table;
    }

    // ===================== HELPERS =====================
    private static String hex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }

    private static Color mezclarConBlanco(Color c, float proporcionBlanco) {
        int r = (int) (c.getRed() + (255 - c.getRed()) * proporcionBlanco);
        int g = (int) (c.getGreen() + (255 - c.getGreen()) * proporcionBlanco);
        int b = (int) (c.getBlue() + (255 - c.getBlue()) * proporcionBlanco);
        return new Color(r, g, b);
    }
}
```

> Nota para la IA ejecutora: el código anterior es una base funcional, no un mockup. Ajusta nombres de paquete si el proyecto real usa otra convención, pero **mantén la API pública igual** (`UIKit.primaryButton(...)`, `UIKit.card()`, etc.) porque las especificaciones de la sección 7 la referencian directamente.

### 3.3 Reemplazo de iconos emoji (mapa de migración)

| Emoji actual (en `FrmDashboard`) | Icono lineal de reemplazo |
|---|---|
| 🛒 Punto de Venta | `shopping-cart` |
| 👥 Gestión Clientes | `users` |
| 🔄 Devoluciones | `rotate-ccw` / `corner-up-left` |
| ⭐ Fidelización | `star` |
| 👤 Gestión Usuarios | `user` |
| 📝 Bitácora Auditoría | `file-text` / `clipboard` |
| 📊 Reporte Ventas | `bar-chart-2` |
| 📦 Reporte Inventario | `package` |
| ⚙️ Configuración ERP | `settings` |
| 🚪 Cerrar Sesión | `log-out` |
| 🔐 (ícono login) | reemplazar por un monograma vectorial simple (ver sección 7.1) en vez de emoji de candado |
| ⚠️ (alertas inventario) | `alert-triangle` |
| ⚡ (probar conexión MP) | `zap` |
| 📂 (exportar auditoría) | `download` |

(Nombres de icono en convención **Feather/Lucide**, disponibles vía Ikonli o como SVG sueltos — son solo referencia de qué pictograma usar, no un paquete específico obligatorio.)

### 3.4 Migración de layouts (regla general AbsoluteLayout → GridBagLayout/BorderLayout)

Patrón a aplicar en **todas** las pantallas NetBeans (`ActulizarStock`, `GestionarCategoria`, `GestionarClientes`, `GestionarProductos`, `GestionarUsuarios`, `GestionarVentas`):

```
getContentPane().setLayout(new BorderLayout());
((JComponent) getContentPane()).setBorder(new EmptyBorder(SPACE_LG, SPACE_LG, SPACE_LG, SPACE_LG));
getContentPane().setBackground(UIKit.BG_APP);

NORTH  -> UIKit.screenHeader(titulo, breadcrumb)              // título + subtítulo
CENTER -> panel con BorderLayout o GridBagLayout 2 columnas:
            CENTER/WEST -> tarjeta con barra de herramientas + tabla (UIKit.styledTable)
            EAST        -> tarjeta de formulario de detalle (label arriba, campo abajo)
```

Para el panel de **detalle/formulario**, usar `GridBagLayout` con una fila por campo, `gbc.fill = HORIZONTAL`, `gbc.weightx = 1`, separación vertical `SPACE_SM` entre filas, y los botones de acción (`primaryButton`/`dangerOutlineButton`) en una fila final alineada a la derecha (`FlowLayout(FlowLayout.RIGHT)`).

### 3.5 Restyle de diálogos (`JOptionPane`)

Para los mensajes que SÍ deben seguir siendo modales (confirmación de eliminar, error crítico de archivo), aplicar un wrapper que respete la paleta:

```java
public static void confirmarEliminacion(Component parent, Runnable accionSiConfirma) {
    int opcion = JOptionPane.showConfirmDialog(parent,
            "Esta acción no se puede deshacer. ¿Deseas continuar?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
    if (opcion == JOptionPane.YES_OPTION) accionSiConfirma.run();
}
```

FlatLaf ya restylea automáticamente los `JOptionPane` por defecto (íconos planos, botones con `UIKit`-like radios) en cuanto se activa `FlatLightLaf.setup()` — no se requiere código adicional salvo para los textos/tono de los mensajes (ver sección 1.2, punto 6: usar mensajes inline para validaciones simples, reservar el modal solo para lo importante).

---

## 4. Patrones de pantalla reutilizables

El sistema completo (≈20 pantallas) se reduce a **5 patrones**. Identifica a cuál pertenece cada pantalla nueva que se agregue en el futuro y aplica el patrón correspondiente — no improvises un sexto patrón sin necesidad real.

### Patrón A — "Maestro-Detalle CRUD"
Tabla a la izquierda/centro + formulario de edición a la derecha + barra de acciones. Es el patrón de `GestionarCategoria`, `GestionarClientes`, `GestionarProductos`, `GestionarUsuarios`, `GestionarVentas`, `IFrmDevoluciones`.

```
┌─────────────────────────────────────────────────────────┐
│  Breadcrumb                                              │
│  Título H1                                    [+ Nuevo]  │
├───────────────────────────────────┬─────────────────────┤
│  🔍 Buscar...           N registros│  TARJETA DETALLE     │
│ ┌───────────────────────────────┐ │  LABEL                │
│ │ Tabla (zebra, sin grilla)     │ │  [ campo            ] │
│ │                                │ │  LABEL                │
│ │                                │ │  [ campo            ] │
│ └───────────────────────────────┘ │                       │
│                                    │  [Eliminar] [Guardar] │
└───────────────────────────────────┴─────────────────────┘
```

### Patrón B — "Formulario único" (sin tabla)
Una sola tarjeta centrada con campos en columna. Es el patrón de `ActulizarStock`.

```
┌─────────────────────────────┐
│      Título H1               │
│      Subtítulo                │
│  LABEL                        │
│  [ combo / campo            ] │
│  LABEL                        │
│  [ campo solo lectura       ] │
│  LABEL                        │
│  [ campo editable           ] │
│           [ Guardar ]         │
└─────────────────────────────┘
```

### Patrón C — "Panel de monitoreo / alertas" con tabs
Tabs + tabla(s) + acción de refresco. Es el patrón de `IFrmAlertasInventario`.

### Patrón D — "Filtros + tabla + exportación"
Barra de filtros horizontal (tarjeta) arriba, tabla grande abajo, acción de exportar al pie. Es el patrón de `IFrmBitacoraAuditoria`.

### Patrón E — "Configuración por secciones"
Dos o más tarjetas tituladas en grilla, cada una con sus propios campos y, opcionalmente, su propio botón de acción contextual. Es el patrón de `IFrmConfiguracionERP` y `IFrmCuentasCobrarPagar` (con tabs adicionales).

### Patrón F — "Shell / Contenedor principal"
Sidebar de navegación + barra superior + área de contenido MDI. Es `FrmDashboard`, único en su tipo.

### Patrón G — "Login / Acceso"
Pantalla sin decoración de SO, dos paneles (marca + formulario). Es `FrmLogin`, único en su tipo.

---

## 5. Reglas de migración y checklist técnico (aplican a TODAS las pantallas)

Antes de dar por terminada cualquier pantalla, verificar:

- [ ] `AbsoluteLayout` eliminado del `getContentPane()` y de todos los sub-paneles.
- [ ] Todos los colores y fuentes provienen de `UIKit`, cero `new Color(r,g,b)` o `new Font(...)` sueltos en la clase de la pantalla.
- [ ] Ningún emoji en texto de botones/labels.
- [ ] Los botones de acción primaria usan `UIKit.primaryButton`, los secundarios `UIKit.secondaryButton`, eliminar usa `UIKit.dangerOutlineButton` (o `dangerSolidButton` solo dentro del diálogo de confirmación).
- [ ] Las tablas usan `UIKit.styledTable(modelo)` en vez de `new JTable(modelo)` desnudo.
- [ ] El nombre de cada variable de instancia generada por NetBeans (`jButton_Actulizar`, `txt_descripcion`, `jTable_categoria`, etc.) **no cambió**.
- [ ] Cada método `*ActionPerformed`, `cargarTabla()`, `cargarProductos()`, etc. compila igual y conserva su lógica interna intacta — solo cambió el código de construcción visual en `initComponents()`/`buildLayout()`.
- [ ] El `JInternalFrame` resultante puede redimensionarse sin que los componentes se superpongan (prueba mental: ¿qué pasa si el usuario maximiza la ventana interna dentro del `JDesktopPane`? El layout debe acomodarse, no quedar flotando en coordenadas fijas).
- [ ] Mensajes de validación de campo individual ya no usan `JOptionPane` (pasan a texto inline bajo el campo, color `UIKit.DANGER`, fuente `UIKit.CAPTION`).
- [ ] Si la pantalla tenía un `jLabel_wallpaper` u otro elemento decorativo sin uso real (común en `GestionarClientes`, `GestionarVentas`), se elimina y se reemplaza por el fondo `UIKit.BG_APP` a nivel de panel contenedor.

---

## 6. Especificación pantalla por pantalla

> Cada bloque indica: **Patrón** aplicable (sección 4), **qué conservar tal cual**, y **qué cambiar visualmente**. Los nombres entre backticks son exactamente los del código fuente provisto — úsalos para ubicar qué tocar.

### 6.1 `FrmLogin` — Patrón G

**Conservar:** toda la lógica en `attachEvents()` (lectura de `ArchivoUsuarioTXT`, comparación usuario/password, `Clases.Sesion`, apertura de `FrmDashboard`), el arrastre de ventana sin decoración (`mousePressed`/`mouseDragged`), `getTxtUsuario()`/`getTxtPassword()`/getters públicos.

**Cambiar:**
- Tamaño de ventana de `420x580` a **`960x600`**, dividida en dos paneles con `BorderLayout`:
  - **WEST (40%)**: panel sólido `UIKit.PRIMARY`, con un monograma vectorial simple (dibujado con `Graphics2D`/formas geométricas — NO emoji 🔐) + texto "Minimarket LAREDO" en blanco + una frase corta de marca ("Sistema de Gestión Empresarial"). Esto reemplaza el ícono emoji y el panel único centrado actual.
  - **EAST (60%)**: fondo `UIKit.BG_APP`, formulario centrado verticalmente en una tarjeta `UIKit.card()` de ancho fijo ~360px: título "Bienvenido" (`H2`), labels `UIKit.fieldLabel("Usuario")` / `UIKit.fieldLabel("Contraseña")`, campos `UIKit.textField()` (reemplazando el `JTextField`/`JPasswordField` con placeholder pintado a mano — esa lógica de placeholder puede conservarse, solo se reestiliza el borde/foco con los tokens).
- `btnIngresar` → `UIKit.primaryButton("Ingresar")`, ancho completo del formulario.
- `btnSalir` → ya no es un botón rojo sólido (salir de un login no es una acción destructiva): convertir en **enlace de texto** (`JButton` estilo "link", fondo transparente, texto `TEXT_SECONDARY`, sin borde) con el texto "Salir del sistema", alineado al pie de la tarjeta.
- Pie de versión (`lblVersion`) se mantiene pero con `UIKit.CAPTION` y `TEXT_SECONDARY`.
- Opcional (UX, sin tocar lógica): al hacer click en `btnIngresar`, deshabilitarlo y cambiar su texto a "Ingresando…" hasta que termine la validación, luego restaurarlo — mejora percibida sin alterar el flujo.

### 6.2 `FrmDashboard` — Patrón F

**Conservar:** `aplicarRol()` (ocultamiento de botones por rol "Vendedor"), `openFrame(JInternalFrame)`, el cierre de frames previos al abrir uno nuevo, `Sesion.getUsuario()/getRol()`, los datos de ejemplo de las tablas del resumen (KPIs/alertas/ventas recientes pueden quedar como están hasta que existan fuentes de datos reales — solo se restylean visualmente).

**Cambiar:**
- `buildMenuButton(String text)`: quitar el emoji embebido en el string de cada botón (`"🛒 Punto de Venta (POS)"` → `"Punto de Venta (POS)"`) y en su lugar usar un `JLabel`/ícono a la izquierda del texto dentro del botón (layout `FlowLayout(FlowLayout.LEFT)` interno o `setIcon()` con un `FlatSVGIcon`/Ikonli `FontIcon`, ver mapa de la sección 3.3). Altura del botón: **44px** (hoy 40).
- Agregar **estado "seleccionado"** persistente: cuando se hace click en un botón del sidebar, debe quedar con fondo `UIKit.PRIMARY_DARK` y un acento vertical de 3px en `UIKit.ACCENT` en su borde izquierdo, hasta que se seleccione otro. Hoy solo existe hover, no hay indicación de "dónde estoy".
- Agregar una **barra superior (top bar)** entre el sidebar y el `desktopPane`, dentro de un `BorderLayout.NORTH` del panel central: breadcrumb con el nombre del módulo abierto (ej. "Inicio › Gestión de Clientes"), buscador global decorativo (`UIKit.textField()` con ícono de lupa), y a la derecha un bloque con nombre de usuario + rol (`Sesion.getUsuario()/getRol()`) + ícono de notificación + el botón "Cerrar sesión" reubicado aquí (puede convivir temporalmente con el del sidebar si se prefiere no tocar `attachEvents()`).
- **Tarjetas KPI** (`buildKpiCard`): rediseñar de "bloque de color sólido" a **tarjeta blanca con barra de acento de 4px** usando directamente `UIKit.kpiCard(label, valor, subLabel, color)` de la sección 3.2 — mismo contenido (`"VENTAS HOY"`, `"S/ 1,245.50"`, etc.), solo cambia el contenedor visual.
- Las dos tablas del resumen (`tblAlerts`, `tblSales`) pasan a usar `UIKit.styledTable(...)`.
- `bgDashboardFrame` (el `JInternalFrame` sin barra de título que aloja el resumen): mantener sin barra de título, pero el `pnlContent` pasa a fondo `UIKit.BG_APP` y los paneles `pnlAlerts`/`pnlRecentSales` se construyen con `UIKit.card()` en vez de `BorderFactory.createCompoundBorder(createLineBorder(...))` manual.
- Para los demás `JInternalFrame` que se abren vía `openFrame(...)` (`IFrmPuntoVenta`, `IFrmGestionClientes`, etc.): estandarizar su barra de título nativa de Swing usando las claves de `UIManager` de FlatLaf para `InternalFrame` (color de fondo de la barra = `UIKit.PRIMARY`, texto blanco, solo botón de cerrar visible si el frame no necesita minimizar/maximizar) — esto da consistencia entre todas las ventanas internas sin tener que reconstruir su title bar a mano.

### 6.3 `GestionarCategoria` — Patrón A (la más simple — usar como plantilla, ver sección 8)

**Conservar:** `jButton_ActulizarActionPerformed` (validación de categoría duplicada, actualización vía `ArchivoCategoriaTXT`), `jButton_EliminarActionPerformed` (validación de productos asociados antes de eliminar), `cargarTabla()`, el listener de selección de fila que llena `txt_descripcion`.

**Cambiar:** aplicar el Patrón A completo (sección 4). La tabla (`jTable_categoria`, columnas ID/Descripción/Estado) ocupa la tarjeta izquierda; la tarjeta derecha contiene solo el campo "Descripción" (`txt_descripcion`) y los dos botones (`jButton_Actulizar` → `primaryButton("Guardar cambios")`, `jButton_Eliminar` → `dangerOutlineButton("Eliminar categoría")`). Es la pantalla ideal para usar como ejemplo 1:1 — ver el código completo de referencia en la sección 8.

### 6.4 `GestionarClientes` — Patrón A

**Conservar:** validación de DNI duplicado, `txt_*KeyTyped` (solo letras en nombre/apellido, solo números en DNI/teléfono con límite de dígitos), `cargarTabla()` con columnas ID/Nombre/Apellido/DNI/Teléfono/Dirección/Estado.

**Cambiar:** el formulario de detalle tiene **5 campos** (nombre, apellido, DNI, teléfono, dirección) — no los apiles en una sola columna larga: usa el panel de detalle en **grilla de 2 columnas** (`GridBagLayout` con `gridx` alternando 0/1) para que la tarjeta de detalle no quede desproporcionadamente alta. Eliminar por completo `jLabel_wallpaper` (hoy es un `JLabel` vacío de 890x470 que cubre toda la pantalla sin función real, vestigio de una imagen de fondo comentada).

### 6.5 `GestionarProductos` — Patrón A

**Conservar:** todos los nombres de campo tal cual, **incluyendo el typo existente `txt_descipcion`** (no corregir el nombre de la variable — solo puede corregirse el *label visible* a "Descripción" si hoy dice distinto, pero el identificador Java se queda igual para no romper referencias).

**Cambiar:** los 4 campos (nombre, descripción, precio, cantidad) en grilla 2 columnas igual que `GestionarClientes`. El campo `cantidad`/`precio` son numéricos — usar `UIKit.textField()` igual, pero alinear el texto a la derecha (`setHorizontalAlignment(JTextField.RIGHT)`) como convención para campos numéricos en tablas/formularios enterprise.

### 6.6 `GestionarUsuarios` — Patrón A

**Conservar:** toda la lógica de `txt_password` (variable `JTextField`, no `JPasswordField`, tal como está en el código fuente actual — **no cambiar el tipo de componente**, eso sería un cambio funcional/de seguridad fuera del alcance de este encargo de rediseño visual).

**Cambiar:** estilo idéntico al resto del Patrón A. **Nota aparte para quien apruebe el encargo (no es una tarea de esta IA, solo una observación a registrar):** dado que `txt_password` muestra la contraseña en texto plano sin máscara, conviene evaluar en un encargo de seguridad/funcional aparte si debería ser `JPasswordField`. Se documenta aquí para no perderlo de vista, pero no se resuelve en este rediseño visual.

### 6.7 `GestionarVentas` — Patrón A (variante con campos de solo lectura + acción "Nueva venta")

**Conservar:** la lógica de `jButton_ActulizarActionPerformed` (actualiza el cliente de una venta seleccionada), `jButton1ActionPerformed` (abre `Registro_Ventas` dentro del `JDesktopPane` actual vía `getDesktopPane()`), `cargarTabla()`/`cargarClientes()`.

**Cambiar:**
- `txt_TotalPagar` y `txt_fecha` son de solo lectura (`setEnabled(false)` hoy) — migrar a `UIKit.readOnlyField()` en vez de un `JTextField` deshabilitado por defecto (que en Swing estándar se ve gris y "roto"); alternativamente, si no necesitan ser técnicamente editables nunca, se pueden mostrar como pares `JLabel` LABEL/valor dentro de una fila de "resumen" en la tarjeta de detalle.
- `jButton1` ("Registro") no debe quedar como un botón más dentro del formulario de edición de cliente — son dos acciones no relacionadas. Moverlo a la **barra de herramientas de la tabla**, como `UIKit.primaryButton("+ Nueva venta")`, alineado a la derecha sobre la tabla (mismo lugar conceptual que el "+ Nuevo" del diagrama del Patrón A).
- Eliminar `jLabel_wallpaper` (mismo vestigio que en `GestionarClientes`).

### 6.8 `ActulizarStock` — Patrón B

**Conservar:** `cargarProductos()`, la validación numérica en `jTextField_StockNuevoKeyTyped`, la actualización vía `ArchivoProductoTXT` en `jButton_actulizarActionPerformed`.

**Cambiar:** tarjeta única centrada (`UIKit.card()`, ancho fijo ~420px) dentro de la ventana de 500x400. `jComboBox_productos` con la fuente/altura estándar de `UIKit`. `jTextField_stockActual` (hoy deshabilitado) → `UIKit.readOnlyField()` o, mejor aún, mostrarlo como un valor grande tipo "chip" (`H2` sobre fondo `BG_APP`, sin aspecto de campo de formulario, ya que nunca se edita). `jTextField_StockNuevo` → `UIKit.textField()`, alineado a la derecha (numérico). Botón `jButton_actulizar` → `UIKit.primaryButton("Actualizar stock")`, ancho completo de la tarjeta.

### 6.9 `IFrmAlertasInventario` — Patrón C

**Conservar:** la estructura de 3 tabs (`Próximos a Vencer`, `Sin Rotación`, `Stock Bajo`), los datos de ejemplo, el TODO de `btnRefrescar`.

**Cambiar:**
- Las 3 tablas pasan a `UIKit.styledTable(...)`.
- Agregar **formato condicional por severidad** en columnas clave usando un `TableCellRenderer` personalizado:
  - En `modelPorVencer`, columna "Días Rest.": valor < 10 → texto/fondo `DANGER` suave; < 30 → `WARNING` suave; resto → `TEXT_SECONDARY` normal.
  - En `modelStockBajo`, columna "Déficit": cualquier valor negativo → badge `UIKit.statusBadge(valor, DANGER)`.
  Esto es exactamente el tipo de "alerta visual accionable" que distingue a NetSuite/Fiori de una tabla plana.
- `btnRefrescar` → `UIKit.secondaryButton("Refrescar alertas")` con ícono `refresh-cw`, movido a la esquina superior derecha del header (ya está ahí, solo se restylea).

### 6.10 `IFrmBitacoraAuditoria` — Patrón D

**Conservar:** los combos `cbUsuario`/`cbAccion`, el `TODO` de filtrado real, `btnExportar`.

**Cambiar:** la sección `pnlFiltros` (hoy `BorderFactory.createTitledBorder` + `GridBagLayout` con look default) se reconstruye como `UIKit.card()` con `UIKit.sectionHeader("Filtros de búsqueda", null)` y los campos en una fila con `UIKit.fieldLabel` arriba de cada combo/campo en vez de labels pegados al costado. `txtFechaInicio`/`txtFechaFin` mantienen su placeholder "DD/MM/AAAA" tal cual (no es obligatorio integrar un date-picker, pero si se quiere ir más allá, la librería **LGoodDatePicker** es la opción estándar en Swing para esto — mejora opcional, no requerida). `btnFiltrar` → `primaryButton`, `btnLimpiar` → `secondaryButton`, `btnExportar` → `secondaryButton` con ícono `download`.

### 6.11 `IFrmConfiguracionERP` — Patrón E

**Conservar:** las dos secciones (`Datos del Establecimiento` / `Integración Mercado Pago`), `btnTestMp`, `btnGuardar`, `btnLimpiar`.

**Cambiar:**
- Reemplazar `BorderFactory.createTitledBorder(...)` (look "Swing clásico" con texto incrustado en el borde) por `UIKit.card()` + `UIKit.sectionHeader(titulo, null)` en ambas secciones.
- **`txtMpToken` (Access Token de Mercado Pago) debe mostrarse enmascarado**, igual que una contraseña — hoy es un `JTextField` plano mostrando un secreto de API en texto claro, lo cual es un anti-patrón incluso a nivel puramente visual/UX (todo ERP serio oculta credenciales). Pasar a `JPasswordField` con un botón "ojo" (`UIKit.secondaryButton` ícono-only `eye`/`eye-off`) al costado para mostrar/ocultar — patrón estándar en NetSuite/Defontana para credenciales de integraciones.
- `btnGuardar` → `primaryButton`, `btnLimpiar` → `secondaryButton`, `btnTestMp` → `secondaryButton` con ícono `zap`.

### 6.12 `IFrmCuentasCobrarPagar` — Patrón E (con tabs)

**Conservar:** ambas pestañas (`Cuentas por Cobrar`/`Cuentas por Pagar`), los datos de ejemplo, los botones de cada tab.

**Cambiar:**
- La barra de totales (`lblTotalCobrar`/`lblTotalPagar`/`lblDiferencia`, hoy `JLabel`s sueltos en un `FlowLayout`) se rediseña como **3 mini tarjetas KPI** (`UIKit.kpiCard`) en la cabecera: "Por Cobrar" (verde si aplica), "Por Pagar" (info/azul), "Diferencia" (rojo si el valor es negativo, verde si es positivo — color condicional según signo, igual que hoy ya calculan `"S/ -930.00"`).
- Columna "Estado" en ambas tablas (`Pendiente`/`Parcial`/`Pagado`) → `UIKit.statusBadge(texto, color)` vía un `TableCellRenderer` (Pendiente=WARNING, Parcial=INFO, Pagado=SUCCESS).
- Botones `btnRegistrarCobro`/`btnRegistrarPago` → `primaryButton`; `btnMarcarPagadoCobrar`/`btnMarcarPagadoPagar` → `secondaryButton`; `btnBuscarCobrar`/`btnBuscarPagar` → ícono de lupa dentro del propio `JTextField` de búsqueda en vez de un botón "Buscar" separado (patrón de búsqueda instantánea típico de listas enterprise), si se prefiere no tocar el listener se puede dejar el botón pero restyleado como `secondaryButton` ícono-only.

### 6.13 `IFrmDevoluciones` — Patrón A (variante: tabla + formulario lateral fijo)

**Conservar:** `cbMotivo`/`cbTipoReembolso`, los TODO de `btnBuscarVenta`/`btnProcesar`, la tabla de historial.

**Cambiar:** layout idéntico al Patrón A — la diferencia es que aquí el panel de formulario ya está a la derecha con ancho fijo (320px), que es exactamente el patrón recomendado, solo falta el restyle de tokens. `txtIdVenta` + `btnBuscarVenta`: agrupar en una sola fila (campo + botón ícono-only de lupa pegado al borde derecho del campo) en vez de dos filas separadas. `btnProcesar` → `primaryButton("Procesar devolución")` ancho completo; `btnLimpiar` → `secondaryButton`.

---

### 6.14 Módulos referenciados pero no incluidos en este encargo

`FrmDashboard.attachEvents()` abre las clases `IFrmPuntoVenta`, `IFrmGestionClientes`, `IFrmFidelizacion`, `IFrmGestionUsuarios`, `IFrmReporteVentas`, `IFrmReporteInventario` — ninguna de estas coincide en nombre con las clases realmente provistas (`GestionarClientes`, `GestionarUsuarios`, `GestionarProductos`, `GestionarVentas`). Esto indica que el dashboard probablemente está apuntando a clases que aún no existen o que el proyecto real las tiene con otro nombre.

**Antes de ejecutar el rediseño de `FrmDashboard`, resolver esta inconsistencia** (es una decisión de arquitectura, no de diseño — quien ejecute debe preguntar al dueño del proyecto, no decidir por su cuenta):
- Opción 1: renombrar `GestionarClientes`→`IFrmGestionClientes`, `GestionarUsuarios`→`IFrmGestionUsuarios`, etc., para que coincidan con lo que el dashboard espera.
- Opción 2: corregir las referencias dentro de `FrmDashboard.attachEvents()` para que apunten a los nombres reales (`GestionarClientes`, `GestionarUsuarios`, `GestionarProductos`).

Cuando se agreguen las pantallas faltantes (`IFrmPuntoVenta`, `IFrmFidelizacion`, `IFrmReporteVentas`, `IFrmReporteInventario`, `Registro_Ventas`) deben construirse usando el mismo `UIKit` y el patrón que les corresponda según la tabla de la sección 4 (POS probablemente necesite un patrón propio tipo "punto de venta" con carrito — no cubierto en este documento porque no se proveyó su código fuente).

---

## 7. Ejemplo completo de transformación — `GestionarCategoria` como plantilla

Esta es la pantalla más simple del Patrón A — úsala como **plantilla literal** para aplicar el mismo criterio a `GestionarClientes`, `GestionarProductos`, `GestionarUsuarios`, `GestionarVentas`, `IFrmDevoluciones`. Comparar contra el original ayuda a calibrar el nivel de cambio esperado: **se reescribe por completo `initComponents()`**, pero **los métodos de negocio no cambian ni una línea**.

**Criterio aplicado sobre qué campos eliminar:** `jLabel1` (título estático "Administrar Categorias") y `jPanel2` (panel contenedor de los dos botones) eran *puramente decorativos/estructurales* en el original — ningún método de negocio los lee ni escribe. Por eso es seguro absorberlos dentro del nuevo `screenHeader`/`card` y quitarlos de la lista de variables. En cambio `jTable_categoria`, `txt_descripcion`, `jButton_Actulizar`, `jButton_Eliminar` sí se leen/escriben dentro de los manejadores de eventos — esos se conservan exactamente con el mismo nombre.

```java
package Vista;

import Clases.Categoria;
import ArchivosTXT.ArchivoCategoriaTXT;
import ArchivosTXT.ArchivoProductoTXT;
import Clases.Producto;
import Vista.Estilos.UIKit;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Gestión de Categorías — rediseñada sobre el sistema de diseño UIKit.
 * Lógica de negocio idéntica a la versión original generada por NetBeans;
 * solo cambia la construcción visual (initComponents).
 */
public class GestionarCategoria extends javax.swing.JInternalFrame {

    public GestionarCategoria() {
        initComponents();
        this.setSize(new Dimension(760, 480));
        this.setTitle("Gestión de categorías");

        cargarTabla();

        jTable_categoria.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = jTable_categoria.getSelectedRow();
                if (fila != -1) {
                    String descripcion = jTable_categoria.getValueAt(fila, 1).toString();
                    txt_descripcion.setText(descripcion);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        // ===== Contenedor raíz =====
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIKit.BG_APP);
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(
                UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG, UIKit.SPACE_LG));

        // ===== Encabezado =====
        getContentPane().add(
                UIKit.screenHeader("Categorías", "Inventario  ›  Categorías"),
                BorderLayout.NORTH);

        // ===== Cuerpo: tabla + panel de detalle =====
        JPanel cuerpo = new JPanel(new BorderLayout(UIKit.SPACE_LG, 0));
        cuerpo.setOpaque(false);

        // --- Tarjeta de listado ---
        JPanel tarjetaTabla = UIKit.card();
        tarjetaTabla.setLayout(new BorderLayout(0, UIKit.SPACE_SM));

        JButton btnNuevo = UIKit.primaryButton("+ Nueva categoría");
        // TODO (fuera de alcance visual): conectar a un flujo de alta de categoría si no existe aún.
        tarjetaTabla.add(UIKit.sectionHeader("Categorías registradas", btnNuevo), BorderLayout.NORTH);

        jTable_categoria = UIKit.styledTable(new DefaultTableModel(
                new Object[][]{}, new String[]{"ID Categoria", "Descripcion", "Estado"}));
        JScrollPane scroll = new JScrollPane(jTable_categoria);
        scroll.setBorder(BorderFactory.createLineBorder(UIKit.BORDER));
        tarjetaTabla.add(scroll, BorderLayout.CENTER);

        cuerpo.add(tarjetaTabla, BorderLayout.CENTER);

        // --- Tarjeta de detalle ---
        JPanel tarjetaDetalle = UIKit.card();
        tarjetaDetalle.setPreferredSize(new Dimension(260, 0));
        tarjetaDetalle.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_MD, 0);
        tarjetaDetalle.add(UIKit.sectionHeader("Editar categoría", null), gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_XS, 0);
        tarjetaDetalle.add(UIKit.fieldLabel("Descripción"), gbc);

        txt_descripcion = UIKit.textField();
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_LG, 0);
        tarjetaDetalle.add(txt_descripcion, gbc);

        jButton_Actulizar = UIKit.primaryButton("Guardar cambios");
        jButton_Actulizar.addActionListener(this::jButton_ActulizarActionPerformed);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, UIKit.SPACE_SM, 0);
        tarjetaDetalle.add(jButton_Actulizar, gbc);

        jButton_Eliminar = UIKit.dangerOutlineButton("Eliminar categoría");
        jButton_Eliminar.addActionListener(this::jButton_EliminarActionPerformed);
        gbc.gridy = 4;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        tarjetaDetalle.add(jButton_Eliminar, gbc);

        cuerpo.add(tarjetaDetalle, BorderLayout.EAST);

        getContentPane().add(cuerpo, BorderLayout.CENTER);
    }

    // ===================================================================
    // A partir de aquí, CERO cambios respecto al archivo original.
    // ===================================================================

    private void jButton_ActulizarActionPerformed(java.awt.event.ActionEvent evt) {
        int fila = jTable_categoria.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría");
            return;
        }

        String nuevaDescripcion = txt_descripcion.getText().trim();

        if (nuevaDescripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una descripción");
            return;
        }

        ArchivoCategoriaTXT archivo = new ArchivoCategoriaTXT();
        List<Categoria> lista = archivo.leer();

        int id = Integer.parseInt(jTable_categoria.getValueAt(fila, 0).toString());

        for (Categoria c : lista) {
            if (c.getDescripcion().equalsIgnoreCase(nuevaDescripcion) && c.getIdCategoria() != id) {
                JOptionPane.showMessageDialog(this, "La categoría ya existe");
                return;
            }
        }

        for (Categoria c : lista) {
            if (c.getIdCategoria() == id) {
                c.setDescripcion(nuevaDescripcion);
                break;
            }
        }

        archivo.guardar(lista);

        JOptionPane.showMessageDialog(this, "Categoría actualizada");

        cargarTabla();
        txt_descripcion.setText("");
    }

    private void jButton_EliminarActionPerformed(java.awt.event.ActionEvent evt) {
        int fila = jTable_categoria.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría");
            return;
        }

        int id = Integer.parseInt(jTable_categoria.getValueAt(fila, 0).toString());

        ArchivoProductoTXT archivoProducto = new ArchivoProductoTXT();
        List<Producto> listaProductos = archivoProducto.leer();
        for (Producto p : listaProductos) {
            if (p.getIdCategoria() == id) {
                JOptionPane.showMessageDialog(this,
                        "No se puede eliminar la categoría porque tiene productos asociados");
                return;
            }
        }

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas eliminar esta categoría?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        ArchivoCategoriaTXT archivo = new ArchivoCategoriaTXT();
        List<Categoria> lista = archivo.leer();

        lista.removeIf(c -> c.getIdCategoria() == id);

        archivo.guardar(lista);

        JOptionPane.showMessageDialog(this, "Categoría eliminada");

        cargarTabla();
        txt_descripcion.setText("");
    }

    public void cargarTabla() {

        ArchivoCategoriaTXT archivo = new ArchivoCategoriaTXT();
        List<Categoria> lista = archivo.leer();

        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelo.addColumn("ID Categoria");
        modelo.addColumn("Descripcion");
        modelo.addColumn("Estado");

        for (Categoria c : lista) {
            Object[] fila = {c.getIdCategoria(), c.getDescripcion(), c.getEstado()};
            modelo.addRow(fila);
        }

        jTable_categoria.setModel(modelo);
    }

    // Variables declaration
    // jLabel1 y jPanel2 del original eran puramente decorativos (título estático
    // y contenedor de botones) y fueron absorbidos por screenHeader()/card();
    // se omiten aquí porque ningún método de negocio los referenciaba.
    private javax.swing.JButton jButton_Actulizar;
    private javax.swing.JButton jButton_Eliminar;
    private javax.swing.JTable jTable_categoria;
    private javax.swing.JTextField txt_descripcion;
}
```

Aplica exactamente este mismo criterio (header + tarjeta tabla + tarjeta detalle en `GridBagLayout`, botones vía `UIKit`, eliminación de campos puramente decorativos sin uso en lógica) a las demás pantallas del Patrón A, ajustando solo: número de columnas de la tabla, número y tipo de campos del formulario (usar grilla 2 columnas cuando haya 4+ campos, como se indica en 6.4/6.5), y el texto de los botones de acción.

---

## 8. Instrucciones finales de ejecución

**Orden recomendado de implementación** (de menor a mayor riesgo de romper algo):

1. Crear `Vista.Estilos.UIKit` (sección 3.2) y la inicialización de FlatLaf (sección 3.1). Verificar que el proyecto compila y arranca igual que antes, solo con el L&F nuevo (todavía sin tocar ninguna pantalla individual).
2. `GestionarCategoria` (la plantilla, sección 7) — validar que el patrón funciona en la práctica antes de replicarlo.
3. Resto del Patrón A: `GestionarClientes`, `GestionarProductos`, `GestionarUsuarios`, `GestionarVentas`, `IFrmDevoluciones`.
4. `ActulizarStock` (Patrón B).
5. `IFrmAlertasInventario`, `IFrmBitacoraAuditoria`, `IFrmConfiguracionERP`, `IFrmCuentasCobrarPagar` (Patrones C/D/E) — ya tienen buena estructura de base, son los de menor esfuerzo.
6. `FrmLogin` (Patrón G).
7. `FrmDashboard` (Patrón F) — al final, porque depende de resolver primero la inconsistencia de nombres de clases descrita en 6.14, y porque es el "marco" que aloja a todas las demás (conviene verlas ya rediseñadas antes de ajustar cómo se ven dentro del `JDesktopPane`).

**Formato de entrega esperado por cada archivo `.java` modificado:**
- El archivo completo y compilable, no solo el fragmento cambiado.
- Mismo nombre de paquete, misma clase pública, mismos métodos públicos/privados con la misma firma.
- Si una pantalla tenía `.form` asociado (las del GUI Builder de NetBeans), indicar explícitamente al entregar que ese `.form` queda desactualizado/obsoleto respecto al `.java` (NetBeans podría sobrescribir los cambios si se vuelve a abrir en modo Diseño — recomendar eliminar o ignorar el `.form` una vez migrado el código a mano).
- Listar, al final de cada archivo entregado, un mini-changelog de 3-5 líneas: qué patrón se aplicó, qué campos se eliminaron (si alguno) y por qué, y cualquier supuesto tomado.

**No se considera terminada una pantalla hasta pasar el checklist completo de la sección 5.**

---
---

# PARTE 2 — Segundo lote de pantallas (Login, Menu y 17 módulos `IFrm*` adicionales)

> Este lote reveló información estructural importante sobre el proyecto real (no solo más pantallas que diseñar). Por eso esta parte empieza con un hallazgo que **debe leerse antes de tocar código**, porque cambia qué pantallas vale la pena rediseñar.

## 9. Hallazgo crítico — mapa real de navegación, pantallas duplicadas y huérfanas

Al analizar `Login.java` y `Menu.java` junto con los 17 `IFrm*` de este lote, se confirma que el proyecto tiene **dos generaciones de pantallas conviviendo**, no solo una pareja suelta como se sospechaba en la sección 6.14 de la Parte 1:

| Generación | Acceso | Contenedor principal | Pantallas que abre |
|---|---|---|---|
| **Legacy** (AbsoluteLayout, NetBeans puro) | `Login` | `Menu` (barra `JMenuBar` superior con menús desplegables) | `GestionarCategoria`, `GestionarClientes`, `GestionarVentas`, `ActulizarStock`, `NewCategory`, `NewClient`*, `ReporteVentas`*, `ReporteClientes`*, `ReporteCategoria`*, `ReporteProductos`* |
| **Nueva** (hand-coded, `GridBagLayout`/`BorderLayout`) | `FrmLogin` | `FrmDashboard` (sidebar lateral) | `IFrmPuntoVenta`, `IFrmGestionClientes`, `IFrmDevoluciones`, `IFrmFidelizacion`, `IFrmGestionUsuarios`, `IFrmBitacoraAuditoria`, `IFrmReporteVentas`, `IFrmReporteInventario`, `IFrmConfiguracionERP` + 11 módulos **sin botón de acceso todavía** (ver punto 9.2) |

\* `NewClient`, `ReporteVentas`, `ReporteClientes`, `ReporteCategoria`, `ReporteProductos` son clases que `Menu.java` instancia pero **no fueron subidas** — no se pueden especificar sin verlas.

**Dato revelador:** dentro de `Menu.java`, el propio `jMenuItem_cerrar_sesionActionPerformed` ya abre `FrmLogin` (la pantalla nueva) al cerrar sesión, no `Login`. Eso confirma que la migración hacia la generación nueva **ya está en marcha** y `Menu`/`Login` son el remanente a desmontar, no una rama paralela a mantener.

### 9.1 Recomendación (decisión de producto, no de diseño — confírmala con quien gestione el proyecto)

1. **No invertir tiempo de rediseño visual en `Login` ni en `Menu`.** Son la versión vieja de `FrmLogin`/`FrmDashboard`, que ya fueron rediseñadas en la Parte 1. Rediseñar ambas parejas sería mantener dos veces la misma pantalla con estilos que además quedarían inconsistentes entre sí mientras dure la transición.
2. Para cada pareja Legacy → Nueva ya identificada, confirmar que la Nueva reemplaza completamente a la Legacy antes de borrar la Legacy:

   | Legacy (retirar) | Reemplazo nuevo (mantener y rediseñar) | Estado |
   |---|---|---|
   | `Login` | `FrmLogin` | Ya rediseñado en Parte 1 (§6.1) |
   | `Menu` | `FrmDashboard` | Ya rediseñado en Parte 1 (§6.2) — pero ver 9.2, le faltan accesos |
   | `GestionarCategoria` | `IFrmGestionCategorias` | Nueva versión ya tiene buscador — usar la nueva (ver §11.3) |
   | `GestionarClientes` | `IFrmGestionClientes` | Idéntico caso |
   | `GestionarProductos` | `IFrmGestionProductos` | Idéntico caso |
   | `GestionarUsuarios` | `IFrmGestionUsuarios` | La nueva además **corrige** que la contraseña use `JPasswordField` (en la legacy es un `JTextField` plano, ver Parte 1 §6.6) — otra razón más para migrar a la nueva, no solo estética |
   | `ActulizarStock` | *(sin reemplazo directo confirmado)* | `IFrmGestionProductos` ya incluye el campo "Cantidad" editable en el mismo formulario de producto — verificar con el equipo si `ActulizarStock` debe retirarse o si cumple un propósito distinto (ej. ajuste rápido sin abrir la ficha completa de producto) |
   | `GestionarVentas` | *(sin reemplazo `IFrm*` confirmado en lo subido)* | Mantener por ahora, rediseñar igual que se hizo en Parte 1 |

3. Si por restricciones de tiempo **no se puede retirar `Login`/`Menu` todavía**, aplicar exactamente la misma especificación de `FrmLogin` (Parte 1, §6.1) y `FrmDashboard` (§6.2) a `Login`/`Menu` respectivamente, solo adaptando nombres de variable (`txt_Usuario`→campo equivalente, `jButton1_IniciarSesion`→botón equivalente, el `JMenuBar` de `Menu` tendría que convertirse en el mismo sidebar — es un cambio de paradigma de navegación, no solo un restyle, así que technically es más trabajo que las demás pantallas de este documento).

### 9.2 Hallazgo aparte (más urgente que el estético): `FrmDashboard` no tiene botón de acceso a 11 módulos que ya existen

Comparando el sidebar actual de `FrmDashboard` (Parte 1, §6.2 — 9 botones: POS, Clientes, Devoluciones, Fidelización, Usuarios, Auditoría, Rep. Ventas, Rep. Inventario, Configuración) contra **todas** las clases `IFrm*` que existen en el proyecto, faltan botones para:

`IFrmGestionCategorias`, `IFrmGestionProductos`, `IFrmGestionProveedores`, `IFrmCuentasCobrarPagar`, `IFrmAlertasInventario`, `IFrmFlujoCaja`, `IFrmKardex`, `IFrmLibroMayor`, `IFrmPlanillaAsistencia`, `IFrmRegistroCompras`, `IFrmFichaEmpleados`.

Esto significa que, aunque rediseñemos estas 11 pantallas a la perfección, **nadie puede llegar a ellas** desde la pantalla principal. Esto no es un detalle visual — es un agujero de navegación. Con 20 módulos en total, además, un sidebar plano de 20 botones sería poco escaneable (un problema real de UX, no solo estético). La solución correcta — y es exactamente lo que hacen SAP Fiori Launchpad / NetSuite / Dynamics con sus catálogos de apps — es **agrupar el menú lateral por área de negocio** con encabezados de sección, en vez de seguir agregando botones sueltos.

**Reestructuración de navegación recomendada para `FrmDashboard`:**

| Grupo | Pantallas (botón → clase) |
|---|---|
| **VENTAS** | Punto de Venta → `IFrmPuntoVenta` · Gestión de Ventas → `GestionarVentas` (hasta que exista una versión `IFrm`) · Devoluciones → `IFrmDevoluciones` · Fidelización → `IFrmFidelizacion` |
| **CLIENTES Y PROVEEDORES** | Clientes → `IFrmGestionClientes` · Proveedores → `IFrmGestionProveedores` |
| **INVENTARIO** | Categorías → `IFrmGestionCategorias` · Productos → `IFrmGestionProductos` · Kardex → `IFrmKardex` · Alertas de Inventario → `IFrmAlertasInventario` · Reporte de Inventario → `IFrmReporteInventario` |
| **COMPRAS** | Registro de Compras → `IFrmRegistroCompras` |
| **FINANZAS** | Flujo de Caja → `IFrmFlujoCaja` · Libro Mayor → `IFrmLibroMayor` · Cuentas por Cobrar y Pagar → `IFrmCuentasCobrarPagar` · Reporte de Ventas → `IFrmReporteVentas` |
| **PERSONAL** | Ficha de Empleados → `IFrmFichaEmpleados` · Planilla y Asistencia → `IFrmPlanillaAsistencia` |
| **ADMINISTRACIÓN** | Usuarios → `IFrmGestionUsuarios` · Bitácora de Auditoría → `IFrmBitacoraAuditoria` · Configuración ERP → `IFrmConfiguracionERP` |
| *(fuera de grupos, al fondo)* | Cerrar sesión |

**Patrón de implementación — sidebar con encabezados de grupo**, agregar a la clase `Vista.Estilos.UIKit` (o directamente en `FrmDashboard` si se prefiere no generalizarlo todavía):

```java
/** Construye un bloque de navegación con encabezado de sección + botones. */
public static JPanel navGroup(String titulo, JButton... botones) {
    JPanel grupo = new JPanel();
    grupo.setOpaque(false);
    grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));
    grupo.setAlignmentX(Component.LEFT_ALIGNMENT);

    JLabel lblGrupo = new JLabel(titulo.toUpperCase());
    lblGrupo.setFont(CAPTION);
    lblGrupo.setForeground(new Color(255, 255, 255, 130)); // blanco translúcido sobre el sidebar navy
    lblGrupo.setBorder(new EmptyBorder(SPACE_MD, SPACE_SM, SPACE_XS, SPACE_SM));
    grupo.add(lblGrupo);

    for (JButton b : botones) {
        grupo.add(b);
        grupo.add(Box.createVerticalStrut(2));
    }
    return grupo;
}
```

En `FrmDashboard.buildLayout()`, sustituir la secuencia plana actual de `pnlSidebar.add(btnX)` por:

```java
pnlSidebar.add(UIKit.navGroup("Ventas", btnPOS, btnVentas, btnDevoluciones, btnFidelizacion));
pnlSidebar.add(UIKit.navGroup("Clientes y Proveedores", btnClientes, btnProveedores));
pnlSidebar.add(UIKit.navGroup("Inventario", btnCategorias, btnProductos, btnKardex, btnAlertasInventario, btnRepInventario));
pnlSidebar.add(UIKit.navGroup("Compras", btnCompras));
pnlSidebar.add(UIKit.navGroup("Finanzas", btnFlujoCaja, btnLibroMayor, btnCuentasCP, btnRepVentas));
pnlSidebar.add(UIKit.navGroup("Personal", btnEmpleados, btnPlanilla));
pnlSidebar.add(UIKit.navGroup("Administración", btnUsuarios, btnAuditoria, btnConfig));
pnlSidebar.add(Box.createVerticalGlue());
pnlSidebar.add(btnLogout);
```

Y en `initComponents()`/`attachEvents()`, agregar los botones nuevos siguiendo exactamente el mismo patrón que ya usan los existentes (`buildMenuButton(...)` + `openFrame(new IFrmX())`), por ejemplo:

```java
btnCategorias = buildMenuButton("Categorías");
btnProductos  = buildMenuButton("Productos");
btnProveedores = buildMenuButton("Proveedores");
btnFlujoCaja  = buildMenuButton("Flujo de Caja");
btnKardex     = buildMenuButton("Kardex");
btnLibroMayor = buildMenuButton("Libro Mayor");
btnCuentasCP  = buildMenuButton("Cuentas por Cobrar y Pagar");
btnPlanilla   = buildMenuButton("Planilla y Asistencia");
btnCompras    = buildMenuButton("Registro de Compras");
btnEmpleados  = buildMenuButton("Ficha de Empleados");
btnAlertasInventario = buildMenuButton("Alertas de Inventario");

// ...
btnCategorias.addActionListener(e -> openFrame(new IFrmGestionCategorias()));
btnProductos.addActionListener(e -> openFrame(new IFrmGestionProductos()));
btnProveedores.addActionListener(e -> openFrame(new IFrmGestionProveedores()));
btnFlujoCaja.addActionListener(e -> openFrame(new IFrmFlujoCaja()));
btnKardex.addActionListener(e -> openFrame(new IFrmKardex()));
btnLibroMayor.addActionListener(e -> openFrame(new IFrmLibroMayor()));
btnCuentasCP.addActionListener(e -> openFrame(new IFrmCuentasCobrarPagar()));
btnPlanilla.addActionListener(e -> openFrame(new IFrmPlanillaAsistencia()));
btnCompras.addActionListener(e -> openFrame(new IFrmRegistroCompras()));
btnEmpleados.addActionListener(e -> openFrame(new IFrmFichaEmpleados()));
btnAlertasInventario.addActionListener(e -> openFrame(new IFrmAlertasInventario()));
```

(`btnVentas` quedaría apuntando a `GestionarVentas` hasta que exista una versión `IFrm` equivalente; recordar también extender `aplicarRol()` para ocultar los botones nuevos que correspondan al rol "Vendedor", siguiendo el mismo criterio que ya aplica a los botones existentes.)

### 9.3 `NewCategory.form` sin `.java` provisto

Se subió `NewCategory.form` pero no su `NewCategory.java` — sin el código no se puede especificar su rediseño. Si esta pantalla sigue en uso (alta de categoría desde el menú legacy `Menu`), súbela en el siguiente lote; si fue reemplazada por el flujo de alta dentro de `IFrmGestionCategorias` (que ya maneja crear/actualizar en el mismo formulario, ver botón "Guardar / Actualizar"), puede retirarse junto con `Menu`.

---

## 10. Patrones adicionales (amplían la sección 4 de la Parte 1)

Este lote introdujo formas de pantalla que no estaban cubiertas por los patrones A-G. Se agregan tres patrones nuevos:

### Patrón A2 — "Listado + Búsqueda + Formulario GridBag" (ya con buena base estructural)
A diferencia del Patrón A original (pantallas NetBeans con `AbsoluteLayout` que requerían reconstrucción completa), este subgrupo (`IFrmGestionCategorias`, `IFrmGestionClientes`, `IFrmGestionProductos`, `IFrmGestionProveedores`, `IFrmGestionUsuarios`, `IFrmFichaEmpleados`) **ya** usa `GridBagLayout`/`BorderLayout` y ya tiene barra de búsqueda — el trabajo es de **restyle de tokens**, no de reestructuración de layout. Ver especificación consolidada en §11.3.

### Patrón H — "Documento Transaccional" (POS / Compras)
Cabecera de datos generales (cliente/proveedor + documento + fecha) arriba → formulario rápido "agregar ítem" → tabla de detalle (carrito) → panel lateral de resumen (subtotal/impuestos/total) + botón de acción principal grande. Es el patrón de `IFrmPuntoVenta` y `IFrmRegistroCompras`. Ver §11.9 y §11.10.

### Patrón C2 — "Paneles comparativos lado a lado"
Variante del Patrón C (alertas con tabs) pero mostrando 2+ tablas simultáneamente en columnas en vez de pestañas, para comparación visual directa. Es el patrón de `IFrmReporteInventario`. Ver §11.11.

### Patrón I — "Resumen financiero con tarjetas de cifras"
Selector/filtro arriba → fila de 2-4 tarjetas de cifra grande (ingresos/egresos/saldo, stock/entradas/salidas, etc.) → tabla de movimientos abajo. Es el patrón de `IFrmFlujoCaja`, `IFrmKardex`, `IFrmLibroMayor` y la mitad derecha de `IFrmPlanillaAsistencia`. **Estas pantallas ya construyen manualmente, con código repetido en cada una, exactamente lo que `UIKit.kpiCard(...)` ya provee** (`new JPanel(new GridBagLayout())` + `setBackground(colorPastel)` + `setBorder(createLineBorder(...))` + dos `JLabel`). Sustituir esas tarjetas hechas a mano por `UIKit.kpiCard(...)` en las 4 pantallas elimina decenas de líneas duplicadas de una sola vez.

---

## 11. Especificación pantalla por pantalla — Lote 2

### 11.1 `Login` — duplicado de `FrmLogin` (Patrón G)
Ver §9.1: no priorizar. Si se mantiene temporalmente, aplicar la spec de §6.1 adaptando nombres (`txt_Usuario`, `txt_Password`, `jButton1_IniciarSesion`) y reemplazando los íconos de imagen (`/Imagenes/user2.png`, `/Imagenes/password.png`, `/Imagenes/carrito1.png`) por los íconos vectoriales del mapa de la sección 3.3.

### 11.2 `Menu` — duplicado de `FrmDashboard` (Patrón F)
Ver §9.1 y §9.2: no priorizar el restyle de `Menu` en sí (su `JMenuBar` superior es un paradigma de navegación distinto al sidebar y migrarlo sería duplicar el trabajo de `FrmDashboard`). Lo verdaderamente urgente es la reestructuración de navegación de `FrmDashboard` descrita en §9.2.

### 11.3 Grupo consolidado Patrón A2 — restyle de tokens únicamente

**Transformación genérica (aplica a las 6 pantallas de esta tabla):**
1. Cada `BorderFactory.createTitledBorder("...")` → `UIKit.card()` + `UIKit.sectionHeader("...", null)`.
2. La fila de búsqueda (`JLabel("Buscar:")` + `JTextField` + `JButton`) → un solo `UIKit.textField()` con texto guía "Buscar…" (el ícono de lupa comunica la función; ya no necesita el label suelto "Buscar:" ni, si se quiere simplificar más, el botón — puede dispararse búsqueda con `DocumentListener` a futuro, pero **eso sería cambio funcional, fuera de alcance**: por ahora basta restylear el botón existente como `UIKit.secondaryButton` ícono-only de lupa).
3. Cada par `JLabel + campo` del formulario de detalle (hoy en 2 columnas `gridx=0/1`) pasa a **label arriba, campo abajo** en una sola columna (`gridx=0` siempre, incrementando `gridy`), usando `UIKit.fieldLabel(...)` + `UIKit.textField()`/`UIKit.readOnlyField()` para el campo ID.
4. Tabla → `UIKit.styledTable(modelo)`. Si la tabla tiene columna "Estado" → `UIKit.statusBadge` vía `TableCellRenderer` (Activo=SUCCESS, Inactivo=TEXT_SECONDARY).
5. Botones: **Guardar/Actualizar** → `primaryButton`; **Eliminar** → `dangerOutlineButton`; **Limpiar/Nuevo** → `secondaryButton`; cualquier botón extra de la tabla (`btnHistorial`) → `secondaryButton` con ícono acorde.

| Pantalla | Columnas de tabla | Campos del formulario (en orden) | Botones extra | Nota |
|---|---|---|---|---|
| `IFrmGestionCategorias` | ID, Descripción, Estado | `txtId`(RO), `txtDescripcion`, `cbEstado` | — | La más simple del grupo |
| `IFrmGestionClientes` | ID, Nombre, Apellido, DNI/RUC, Teléfono, Correo | `txtId`(RO), `txtNombre`, `txtApellido`, `txtDni`, `txtTelefono`, `txtCorreo` | `btnHistorial` ("Ver Historial de Compras") | Quitar el emoji 📜 del texto del botón, usar ícono `clock`/`file-text` |
| `IFrmGestionProductos` | ID, Nombre, Cant., Precio, Categoría, Lote, Vencimiento | `txtId`(RO), `txtNombre`, `txtDescripcion`, `txtCantidad`, `txtPrecio`, `cbCategoria`, `txtLote`, `txtVencimiento` | filtro extra `cbFiltroCategoria` en la barra de búsqueda | Campos numéricos (`txtCantidad`, `txtPrecio`) alineados a la derecha |
| `IFrmGestionProveedores` | ID, RUC, Razón Social, Contacto, Teléfono, Correo, Dirección, Estado | `txtId`(RO), `txtRuc`, `txtRazonSocial`, `txtContacto`, `txtTelefono`, `txtCorreo`, `txtDireccion`, `cbEstado` | búsqueda específica por RUC (`txtBuscarRuc`) en vez de búsqueda genérica | — |
| `IFrmGestionUsuarios` | ID, Nombre, Apellido, Usuario, Teléfono, Rol, Estado | `txtId`(RO), `txtNombre`, `txtApellido`, `txtUsuario`, `txtPassword`, `txtTelefono`, `cbRol`, `cbEstado` | — | Esta versión **ya usa `JPasswordField`** correctamente (a diferencia de la legacy `GestionarUsuarios`) — no tocar el tipo de componente, solo aplicar `UIKit` al estilo visual del campo |
| `IFrmFichaEmpleados` | ID, Nombre, Apellido, DNI, Cargo, Salario, Teléfono, Estado | `txtId`(RO), `txtNombre`, `txtApellido`, `txtDni`, `txtTelefono`, `txtCorreo`, `txtDireccion`, `cbCargo`, `txtSalario`, `cbEstado` | — | Formulario más largo del grupo (10 campos) — usar grilla de 2 columnas para no quedar excesivamente alto, igual que se indicó para `GestionarClientes` en la Parte 1 (§6.4) |

### 11.4 `IFrmFidelizacion` — híbrido Patrón A2 + Patrón E

**Conservar:** `attachEvents()` completo (guardar regla, canjear, limpiar, selección de fila que llena `txtIdCliente`/`txtPuntosDisponibles`).

**Cambiar:** los tres `BorderFactory.createTitledBorder` (`pnlTabla`, `pnlReglas`, `pnlCanje`) → `UIKit.card()` + `sectionHeader`. `tblClientesPuntos` → `styledTable`. Botones: `btnCanjear` (acción principal de la pantalla) → `primaryButton`; `btnGuardarRegla` (acción de configuración secundaria) → `secondaryButton`; `btnLimpiar` → `secondaryButton`. `txtIdCliente`/`txtPuntosDisponibles` (no editables) → `UIKit.readOnlyField()`.

### 11.5 `IFrmFlujoCaja` — Patrón I

**Conservar:** filtrar por fechas, actualizar tipo de cambio, nuevo ingreso/egreso, exportar.

**Cambiar:** `pnlFiltros` → card. Las 3 tarjetas hechas a mano (`pnlIngresosCard`/`pnlEgresosCard`/`pnlSaldoCard`, cada una con `new Color(232,245,233)` etc.) → reemplazar las tres por **una sola llamada cada una** a `UIKit.kpiCard("Ingresos", lblIngresos.getText(), "Periodo actual", UIKit.SUCCESS)` (y análogo para Egresos en `DANGER`, Saldo Neto en `PRIMARY`) — eliminando ~30 líneas de `GridBagLayout`/`setBackground`/`setBorder` repetidas. `pnlTC` (tipo de cambio) → mini-card simple. Tabla de movimientos → `styledTable`, columna "Tipo" con badge (Ingreso=SUCCESS, Egreso=DANGER). Botones: `btnNuevoIngreso` → `primaryButton`, `btnNuevoEgreso` → `dangerOutlineButton` (mantiene la semántica de "salida de dinero" sin usar rojo sólido agresivo para una acción cotidiana), `btnActualizarTC`/`btnExportar` → `secondaryButton`.

### 11.6 `IFrmKardex` — Patrón I (simplificado)

**Conservar:** selección de producto, refrescar.

**Cambiar:** mismo criterio que 11.5 — las 3 tarjetas manuales (`pnlStock`/`pnlEntradas`/`pnlSalidas`) → 3 llamadas a `UIKit.kpiCard`. Tabla `tblKardex` → `styledTable`, columna "Tipo" con badge (Entrada=SUCCESS, Salida=DANGER). `btnRefrescar` → `secondaryButton` con ícono `refresh-cw`.

### 11.7 `IFrmLibroMayor` — Patrón D + tarjeta resumen lateral

**Conservar:** filtros por cuenta contable/fechas, refrescar, los asientos de ejemplo.

**Cambiar:** `pnlFiltros` → card con `fieldLabel` arriba de cada campo. Tabla `tblAsientos` → `styledTable`. `pnlResumen` (hoy una columna de `JLabel`s sueltos con un `JSeparator`) → reconstruir como 2-3 `UIKit.kpiCard` apiladas verticalmente (Total Debe en `SUCCESS`, Total Haber en `DANGER`, Saldo Actual en `PRIMARY`) en vez de pares de labels sin jerarquía visual clara. `lblAyuda` (texto itálico gris explicativo) se conserva tal cual, solo migrar a `UIKit.CAPTION`/`TEXT_SECONDARY`.

### 11.8 `IFrmPlanillaAsistencia` — Patrón A2-variante "cálculo"

**Conservar:** toda la lógica de cálculo de pago (`btnCalcular`), registro de vacaciones, generación de planilla.

**Cambiar:** `pnlSelector` → card. Tabla `tblAsistencia` → `styledTable`. `pnlDerecho` → card con `sectionHeader`; los campos de solo lectura (`txtDiasTrabajados`, `txtFaltas`, `txtVacacionesAcumuladas`) → `UIKit.readOnlyField()`. `txtPagoFinal` (hoy grande/verde/no editable) → en vez de un `JTextField`, mostrarlo como una mini tarjeta de resultado tipo `UIKit.kpiCard("Pago Final", valor, "Periodo actual", UIKit.ACCENT)` al pie del formulario — es literalmente el resultado/KPI de todo el cálculo, merece ese tratamiento en vez de verse como "un campo de texto más". El `JSeparator` que ya divide "asistencia" de "cálculo de pago" se conserva, solo recolorear a `UIKit.BORDER`. Botones: `btnCalcular` → `primaryButton`; `btnRegistrarVacaciones` → `secondaryButton` (puede llevar acento `WARNING` sutil ya que hoy es naranja, manteniendo la semántica de "atención"); `btnGenerarPlanilla` → `primaryButton` ancho completo (es el cierre de todo el flujo del mes, merece ser la acción más prominente de la pantalla).

### 11.9 `IFrmPuntoVenta` — Patrón H

**Conservar:** toda la lógica de carrito, búsqueda de cliente, registrar venta, cancelar.

**Cambiar:** `pnlCliente` → card. `pnlAgregarProd` (form rápido cod. producto/cantidad) → card con campos en fila usando `UIKit.textField()`. `tblCarrito` → `styledTable`. `pnlLateral` (resumen de pago) → card; `lblTotal` (ya es grande, 28pt) se conserva ese tamaño pero usando el token `UIKit.KPI_VALUE` y color `UIKit.ACCENT`. **Excepción de altura estándar, justificada:** `btnRegistrar` ("Cobrar y Registrar") es la acción principal de toda la pantalla — usar `primaryButton` pero con altura 48px (no 36) y ancho completo del panel lateral, para que destaque sobre cualquier otro botón de la pantalla. `btnCancelar` → `dangerOutlineButton`. `btnAgregar`/`btnBuscarCliente` → `secondaryButton`.

### 11.10 `IFrmRegistroCompras` — Patrón H (variante de compras)

**Conservar:** agregar/quitar producto del detalle, registrar compra, `recalcularTotales()` (incluye el cálculo de IGV — no tocar esa lógica).

**Cambiar:** mismo criterio que 11.9. `pnlCabecera` (proveedor/documento/fecha) → card. `pnlForm` (agregar producto a la compra) → card, con `txtProductoNombre` (no editable) como `UIKit.readOnlyField()`. `tblDetalle` → `styledTable`. `pnlResumen` (subtotal/IGV/total) → usar 1-2 `UIKit.kpiCard` apiladas para el total final, o como mínimo aplicar `UIKit.H2`/`KPI_VALUE` a `lblTotal`. `btnRegistrarCompra` ("Registrar Compra y Actualizar Stock") → `primaryButton` ancho completo, altura 48 (mismo criterio de excepción que el botón de cobro del POS — es la acción que cierra el documento). `btnAgregarProducto` → `secondaryButton`; `btnQuitarProducto` → `dangerOutlineButton`; `btnLimpiar` → `secondaryButton`.

### 11.11 `IFrmReporteInventario` — Patrón C2

**Conservar:** ambas tablas (`tblStockBajo`, `tblMasVendidos`), `attachEvents()`.

**Cambiar:** `pnlStockBajo`/`pnlMasVendidos` → `UIKit.card()` cada una, con `sectionHeader` en vez de `createTitledBorder` (quitando los emojis ⚠️/🏆 del título — usar ícono `alert-triangle` y `award` respectivamente). Ambas tablas → `styledTable`. En `tblStockBajo`, columna "Stock Actual" con badge `DANGER` cuando esté por debajo del mínimo (comparando contra "Mínimo Requerido" de la misma fila, vía `TableCellRenderer`, igual criterio que `IFrmAlertasInventario` en la Parte 1 §6.9). Botones: `btnActualizar` → `secondaryButton` ícono `refresh-cw` (quitar 🔄); `btnExportarPDF` → `secondaryButton` ícono `download` (quitar 📂).

### 11.12 `IFrmReporteVentas` — Patrón D + resumen inferior

**Conservar:** filtros (fecha/mes), búsqueda, exportar.

**Cambiar:** `pnlFiltros` → card con `fieldLabel` arriba de cada campo (en vez de label-al-costado). Tabla `tblVentas` → `styledTable`. `pnlInferior` (TOTAL FACTURADO / NETO GANANCIAS) → 2 `UIKit.kpiCard` (Total Facturado en `PRIMARY`, Neto Ganancias en `SUCCESS`) en vez de pares `JLabel` sueltos en `GridBagLayout`. `btnExportarPDF`: hoy es rojo (`new Color(198,40,40)`), lo cual es semánticamente confuso para una acción de exportar (el rojo se reserva para peligro/eliminar) → cambiar a `secondaryButton` con ícono `download`, quitando el emoji 📄 del texto.

### 11.13 `NewCategory`

Sin `.java` provisto — ver nota en §9.3. No se especifica hasta recibir el código fuente.

---

## 12. Checklist y orden de ejecución combinado (Lote 1 + Lote 2)

El orden de la sección 8 (Parte 1) sigue vigente; se inserta este lote así:

1. `UIKit` + FlatLaf (si no se hizo ya).
2. **Antes de seguir, resolver §9.1**: confirmar con el dueño del proyecto qué pantallas legacy se retiran (`Login`, `Menu`, `GestionarCategoria`, `GestionarClientes`, `GestionarProductos`, `GestionarUsuarios`, y a confirmar `ActulizarStock`). Esto evita rediseñar pantallas que van a borrarse.
3. Grupo Patrón A2 (§11.3) — son las de menor esfuerzo de este lote, ya tienen buena base estructural.
4. `IFrmFidelizacion`, `IFrmReporteInventario`, `IFrmReporteVentas` — esfuerzo medio.
5. `IFrmFlujoCaja`, `IFrmKardex`, `IFrmLibroMayor`, `IFrmPlanillaAsistencia` — agrupar porque comparten la migración de "tarjetas manuales" → `UIKit.kpiCard` (una vez resuelta en una, las otras 3 son casi copiar el mismo criterio).
6. `IFrmPuntoVenta`, `IFrmRegistroCompras` — Patrón H, dejarlas para el final de este lote porque son las pantallas operativas más usadas del sistema (mayor riesgo si algo queda mal) y conviene tener ya el `UIKit` maduro y probado en las pantallas anteriores antes de tocarlas.
7. **Al cerrar todo el rediseño visual**, ejecutar la reestructuración de navegación de `FrmDashboard` descrita en §9.2 — recién en este punto tiene sentido, porque ya van a existir todas las pantallas `IFrm*` rediseñadas a las que el nuevo sidebar agrupado debe apuntar.

**Checklist adicional específico de este lote** (sumar al de la sección 5):
- [ ] Las tarjetas de cifra construidas a mano (`pnlIngresosCard`, `pnlStock`, etc.) fueron reemplazadas por `UIKit.kpiCard(...)` — cero `new Color(232,245,233)` ni paneles pastel hechos a mano sueltos en el código final.
- [ ] Ningún botón de exportar/actualizar quedó en rojo (`new Color(198,40,40)`) — el rojo queda reservado para eliminar/cancelar.
- [ ] Se confirmó con el dueño del proyecto la lista de pantallas legacy a retirar (§9.1) antes de invertir tiempo en ellas.
- [ ] `FrmDashboard` expone un botón para cada uno de los 20 módulos confirmados como vigentes (no solo los 9 originales).

---
---

# PARTE 3 — Lote final (formularios "Nuevo X" y módulos de Reporte legacy)

> Con este lote se termina de ver el árbol completo de `Menu.java`: cada `JMenuItem` que antes quedaba sin código fuente asociado ahora tiene su clase. Esto cierra del todo el mapa de navegación legacy y confirma — con evidencia completa — la recomendación de la Parte 2.

## 13. Cierre del mapa de navegación legacy

Con `NewCategory`, `NewClient`, `NewProduct`, `NewUser`, `NewVent`, `Registro_Ventas`, `ReporteCategoria`, `ReporteClientes`, `ReporteProductos` y `ReporteVentas`, **ya no queda ningún `JMenuItem` de `Menu.java` apuntando a una clase desconocida.** El árbol completo:

```
Login → Menu (JMenuBar)
 ├─ Usuario:    NewUser · GestionarUsuarios
 ├─ Producto:   NewProduct · GestionarProductos · ActulizarStock
 ├─ Categoria:  NewCategory · GestionarCategoria
 ├─ Cliente:    NewClient · GestionarClientes
 ├─ Venta:      NewVent · GestionarVentas (con botón interno → Registro_Ventas)
 └─ Reportes:   ReporteClientes · ReporteCategoria · ReporteProductos · ReporteVentas
```

Esto **confirma con evidencia completa** lo que la Parte 2 (§9.1) sugería con datos parciales: es un sistema legacy completo y autocontenido, en paralelo al sistema nuevo (`FrmLogin → FrmDashboard → IFrm*`). La recomendación se mantiene y ahora es más firme: **no rediseñar visualmente esta rama si el plan es reemplazarla por la nueva.** Confirma con el dueño del proyecto antes de tocar nada de esta Parte 3.

### 13.1 Dos hallazgos de paridad de funcionalidad (no son de diseño — son de producto, pero hay que conocerlos antes de apagar la rama legacy)

1. **`NewVent` (Facturación legacy) tiene dos funciones que `IFrmPuntoVenta` (la nueva) todavía no tiene:** cálculo de cambio (`txt_efectivo` − total → `jTextField_Cambio`) y generación de una boleta en PDF al registrar la venta (vía iText, ya funcionando). Si se retira `NewVent` sin portar estas dos funciones a `IFrmPuntoVenta`, el cajero pierde capacidades reales, no solo una pantalla más bonita. Recomendación: portar `calcularCambio` y la generación de boleta PDF a `IFrmPuntoVenta` **antes** de retirar `NewVent`, no después.
2. **El "reporte de ventas" existe triplicado:** `ReporteVentas` (legacy, simple: tabla + exportar), `Registro_Ventas` (legacy, con filtro por mes y cálculo de ganancia total, accesible desde el botón "Registro" de `GestionarVentas`) e `IFrmReporteVentas` (nuevo, de la Parte 2, con filtros por rango de fechas y por mes). La nueva (`IFrmReporteVentas`) ya cubre o supera a las otras dos en filtros — falta solo confirmar que también exporta a PDF con la misma calidad (el código de exportación de `ReporteVentas`/`Registro_Ventas` vía iText puede reutilizarse directamente ahí, ver punto 3).
3. **El proyecto ya usa iText (`com.itextpdf`) y funciona** en `ReporteCategoria`, `ReporteClientes`, `ReporteProductos`, `ReporteVentas`, `Registro_Ventas` y `NewVent` (boleta). Cuando se restyle el botón "Exportar" de cualquier pantalla `IFrm*` (Parte 1 §6.9, Parte 2 §11.5/11.11/11.12), **la lógica de generación de PDF puede copiarse de estos archivos legacy tal cual** — ya está resuelta y probada, no hay que escribirla de nuevo.

## 14. Patrón A3 — "Alta rápida" (formulario único sin tabla): `NewCategory`, `NewClient`, `NewProduct`, `NewUser`

Las cuatro siguen exactamente el mismo molde NetBeans `AbsoluteLayout`: título grande arriba, labels alineadas a la derecha + campo a la derecha del label, un botón "Guardar" gris al pie. **Ya tienen equivalente en la generación nueva** (`IFrmGestionCategorias/Clientes/Productos/Usuarios`, Parte 2 §11.3), donde el mismo formulario de detalle sirve para crear **y** editar (botón "Guardar / Actualizar" único). Por eso son las primeras candidatas a retirar de toda la Parte 3 — no aportan nada que la pantalla nueva no haga ya, y mantenerlas duplica la validación (ej. `NewClient` valida DNI a 8 dígitos y teléfono a 9 — esa misma regla debería vivir una sola vez, en el alta/edición de `IFrmGestionClientes`).

Si por algún motivo deben mantenerse temporalmente, el restyle es idéntico al Patrón B de la Parte 1 (formulario único centrado, sección 6.8): tarjeta `UIKit.card()` centrada, `fieldLabel` arriba de cada campo en vez de label-al-costado, botón `primaryButton` ancho completo.

| Pantalla | Campos | Validaciones ya existentes (conservar tal cual) |
|---|---|---|
| `NewCategory` | `txt_descripciones` | solo letras y espacios; descripción duplicada |
| `NewClient` | `txt_nombre`, `txt_apellido`, `txt_DNI`, `txt_telefono`, `txt_direccion` | nombre/apellido solo letras; DNI 8 dígitos numéricos; teléfono 9 dígitos; DNI duplicado |
| `NewProduct` | `txt_nombre`, `txt_cantidad`, `txt_precio`, `txt_descripcion`, `jComboBox_categoria` | nombre solo letras; cantidad solo números; precio numérico con un solo punto decimal; producto+descripción duplicados; categoría obligatoria |
| `NewUser` | `txt_nombre`, `txt_apellido`, `txt_usuario`, `jPassword`, `txt_telefono`, `jComboBox_rol` | nombre/apellido solo letras; teléfono 9 dígitos; usuario duplicado; rol obligatorio. (Aquí también usa correctamente `JPasswordField`.) |

## 15. Patrón D-lite — "Reporte simple: tabla + exportar PDF": `ReporteCategoria`, `ReporteClientes`, `ReporteProductos`, `ReporteVentas`

Las cuatro comparten el mismo molde generado con `GroupLayout` (NetBeans "Free Design", distinto del `AbsoluteLayout` de las demás pantallas legacy, pero igual de antiguo visualmente): título en mayúsculas arriba, una tabla, un botón "EXPORTAR" abajo a la derecha. Cada una arma su propio `Document`/`PdfPTable` de iText con encabezados en gris claro — código duplicado 4 veces con columnas distintas.

**Decisión de producto pendiente (no de diseño):** ¿esta funcionalidad de "ver tabla + exportar PDF" debe seguir viviendo como pantallas standalone, o debe convertirse en un botón "Exportar PDF" dentro de las pantallas `IFrmGestion*`/`IFrmReporte*` correspondientes (que ya tienen la tabla en pantalla)? La segunda opción evita mantener una pantalla extra solo para exportar lo que ya se está viendo. Si se opta por la primera (mantenerlas separadas), el restyle es el mismo Patrón D-lite para las cuatro: `UIKit.card()` envolviendo la tabla (`UIKit.styledTable`), título con `UIKit.H1`/breadcrumb en vez de `JLabel` suelto en mayúsculas, botón "EXPORTAR" → `UIKit.secondaryButton("Exportar PDF")` con ícono `download` (quitar el estilo de texto todo en mayúsculas, que no sigue la escala tipográfica del sistema).

| Pantalla | Columnas de tabla | Particularidad |
|---|---|---|
| `ReporteCategoria` | ID, Descripción, Estado | tiene código de búsqueda comentado (`buscarCategoria()`) sin usar — si se mantiene la pantalla, decidir si se reactiva esa búsqueda o se borra el código muerto |
| `ReporteClientes` | ID, Nombre, Apellido, DNI, Teléfono, Estado | mismo bloque de búsqueda comentado sin usar |
| `ReporteProductos` | ID, Nombre, Cantidad, Precio, Descripción, Categoría, Estado | idem |
| `ReporteVentas` | ID Venta, Cliente, Total, Fecha | sin filtros (a diferencia de `Registro_Ventas` e `IFrmReporteVentas`, que sí filtran) |

## 16. `Registro_Ventas` — variante de reporte con filtro por mes y ganancia (Patrón D)

Ver hallazgo §13.1.2 — es prácticamente un subconjunto de lo que ya hace `IFrmReporteVentas` (Parte 2, §11.12), con la diferencia de que se abre desde dentro de `GestionarVentas` en vez de desde el menú/sidebar principal. Antes de invertir en restylearla, confirmar si sigue cumpliendo un propósito distinto a `IFrmReporteVentas` o si puede retirarse junto con `GestionarVentas` cuando esa migre. Si se mantiene, mismo tratamiento que el Patrón D-lite de la sección 15: tarjeta + `styledTable` + `UIKit.kpiCard("Ganancia Total", ...)` en vez del `JTextField` deshabilitado (`txt_GananciaTotal`) que tiene hoy.

## 17. `NewVent` — Punto de Venta legacy (Patrón H)

Ver hallazgo §13.1.1 antes de decidir si esto se retira. Estructura: cliente (buscar por DNI o combo) → producto+cantidad a añadir → tabla carrito con **columna de botón "Eliminar" por fila** (vía `TableCellRenderer`/`TableCellEditor` personalizados — patrón correcto y reutilizable, conservar la técnica si en algún momento `IFrmPuntoVenta` necesita lo mismo en su `tblCarrito`) → resumen (subtotal/descuento/total/efectivo/cambio) → botón "Registrar venta" que genera la boleta PDF.

**Detalle menor detectado, no es responsabilidad de este rediseño visual pero vale registrarlo:** en `calcularTotalVenta()`, `descuento` está fijo en `0` (`double descuento = 0;`) — el campo `jTextField_Descuento` existe en la interfaz pero no hay lógica que lo calcule o lo deje editable; es un descuento "decorativo" en el código actual. No afecta el rediseño visual, pero si se porta la función de descuento real a `IFrmPuntoVenta` (no existe ahí tampoco), tenerlo en cuenta.

Si se mantiene temporalmente, aplicar Patrón H igual que `IFrmPuntoVenta`/`IFrmRegistroCompras` (Parte 2, §11.9/§11.10): cabecera cliente → card; formulario añadir producto → card con campos en fila; `jTable_Venta` → `styledTable` (conservando la columna de botón "Eliminar" tal cual, solo restyleando el `JButton` interno con `UIKit.dangerOutlineButton`); panel de resumen → card lateral con `jTextField_TotalPagar` tratado como KPI grande; `jButton_Regis_venta` → `primaryButton` altura 48 ancho completo (mismo criterio de excepción ya usado en POS/Compras).

---

## 18. Inventario maestro final — las ~38 pantallas revisadas en las 3 partes

| Pantalla | Estado | Reemplazo / relación | Patrón |
|---|---|---|---|
| `Login` | Legacy — retirar | `FrmLogin` | G |
| `FrmLogin` | **Canonical** | — | G |
| `Menu` | Legacy — retirar | `FrmDashboard` | F |
| `FrmDashboard` | **Canonical** (pendiente ampliar sidebar, §9.2) | — | F |
| `GestionarCategoria` | Legacy — retirar | `IFrmGestionCategorias` | A → A2 |
| `IFrmGestionCategorias` | **Canonical** | — | A2 |
| `GestionarClientes` | Legacy — retirar | `IFrmGestionClientes` | A → A2 |
| `IFrmGestionClientes` | **Canonical** | — | A2 |
| `GestionarProductos` | Legacy — retirar | `IFrmGestionProductos` | A → A2 |
| `IFrmGestionProductos` | **Canonical** | — | A2 |
| `GestionarUsuarios` | Legacy — retirar (además sin máscara de password) | `IFrmGestionUsuarios` | A → A2 |
| `IFrmGestionUsuarios` | **Canonical** | — | A2 |
| `GestionarVentas` | A confirmar (sin reemplazo `IFrm` directo) | — | A |
| `ActulizarStock` | A confirmar (¿absorbido por `IFrmGestionProductos`?) | — | B |
| `NewCategory` | Legacy — retirar (alta ya incluida en `IFrmGestionCategorias`) | `IFrmGestionCategorias` | A3 |
| `NewClient` | Legacy — retirar | `IFrmGestionClientes` | A3 |
| `NewProduct` | Legacy — retirar | `IFrmGestionProductos` | A3 |
| `NewUser` | Legacy — retirar | `IFrmGestionUsuarios` | A3 |
| `NewVent` | Legacy — retirar **después** de portar cambio+boleta (§13.1.1) | `IFrmPuntoVenta` | H |
| `IFrmPuntoVenta` | **Canonical** (pendiente recibir cambio+boleta) | — | H |
| `Registro_Ventas` | A confirmar vs. `IFrmReporteVentas` | `IFrmReporteVentas` (parcial) | D |
| `ReporteVentas` | A confirmar (¿fusionar export a `IFrmReporteVentas`?) | `IFrmReporteVentas` (parcial) | D-lite |
| `IFrmReporteVentas` | **Canonical** | — | D |
| `ReporteCategoria` | A confirmar | — | D-lite |
| `ReporteClientes` | A confirmar | — | D-lite |
| `ReporteProductos` | A confirmar | — | D-lite |
| `IFrmReporteInventario` | **Canonical** | — | C2 |
| `IFrmDevoluciones` | **Canonical** | — | A |
| `IFrmFidelizacion` | **Canonical** | — | A2+E |
| `IFrmGestionProveedores` | **Canonical** (sin legacy previo) | — | A2 |
| `IFrmFichaEmpleados` | **Canonical** (sin legacy previo) | — | A2 |
| `IFrmFlujoCaja` | **Canonical** (sin legacy previo) | — | I |
| `IFrmKardex` | **Canonical** (sin legacy previo) | — | I |
| `IFrmLibroMayor` | **Canonical** (sin legacy previo) | — | I |
| `IFrmPlanillaAsistencia` | **Canonical** (sin legacy previo) | — | A2-variante |
| `IFrmRegistroCompras` | **Canonical** (sin legacy previo) | — | H |
| `IFrmCuentasCobrarPagar` | **Canonical** (sin legacy previo) | — | E |
| `IFrmAlertasInventario` | **Canonical** (sin legacy previo) | — | C |
| `IFrmBitacoraAuditoria` | **Canonical** (sin legacy previo) | — | D |
| `IFrmConfiguracionERP` | **Canonical** (sin legacy previo) | — | E |

**Resumen:** de las pantallas revisadas en total, **16 son legacy con reemplazo directo confirmado** (retirar sin rediseñar), **6 quedan "a confirmar"** (requieren una decisión de producto antes de invertir tiempo de diseño), y **17 son canónicas** y concentran todo el esfuerzo de rediseño real — ya cubiertas en las Partes 1 y 2 de este documento.

## 19. Checklist final de cierre (sumar a los de las secciones 5 y 12)

- [ ] Se portaron a `IFrmPuntoVenta` las dos funciones de `NewVent` identificadas en §13.1.1 (cálculo de cambio, generación de boleta PDF) antes de retirar `NewVent`.
- [ ] Se decidió qué pasa con la funcionalidad de exportar PDF de `ReporteCategoria`/`ReporteClientes`/`ReporteProductos` (§15) — ¿pantalla propia o botón dentro de las `IFrmGestion*`?
- [ ] Se decidió si `Registro_Ventas` se retira en favor de `IFrmReporteVentas` o si cumple un rol distinto.
- [ ] Se decidió el destino de `GestionarVentas` y `ActulizarStock` (sin reemplazo `IFrm` directo confirmado).
- [ ] La lógica de exportación PDF con iText se reutilizó de los archivos legacy en vez de reescribirse (§13.1.3).




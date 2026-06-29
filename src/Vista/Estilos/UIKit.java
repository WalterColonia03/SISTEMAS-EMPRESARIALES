package Vista.Estilos;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Sistema de diseño centralizado del ERP Minimarket LAREDO.
 * Versión 3.0 — Paleta actualizada a estilo moderno indigo/dark-navy.
 * Capa: Clases (Utilidades transversales)
 * Implementa: RNF-01 (tiempo respuesta), sección 9 del manual (UI/UX obligatorio).
 *
 * TODOS los componentes Swing del sistema deben construirse usando esta clase.
 * PROHIBIDO usar colores, fuentes o estilos hardcodeados fuera de UIKit.
 */
public final class UIKit {

    private UIKit() {}

    // ============================================================
    // PALETA — Estilo moderno indigo/dark-navy (referencia web)
    // ============================================================

    /** Color de fondo del sidebar y cabeceras principales */
    public static final Color SIDEBAR_BG      = new Color(0x1a1f36);
    /** Color del ítem activo en el sidebar */
    public static final Color SIDEBAR_ACTIVE  = new Color(0x4F5BD5);
    /** Color hover del sidebar */
    public static final Color SIDEBAR_HOVER   = new Color(0x252b4a);
    /** Texto del sidebar */
    public static final Color SIDEBAR_TEXT    = Color.WHITE;
    /** Texto de sección/grupo del sidebar (opaco) */
    public static final Color SIDEBAR_SECTION = new Color(255, 255, 255, 110);

    /** Color primario de acción (botones, enlaces, acento) — indigo */
    public static final Color PRIMARY         = new Color(0x4F5BD5);
    /** Hover del primario */
    public static final Color PRIMARY_DARK    = new Color(0x3D49C4);
    /** Azul de acento secundario */
    public static final Color ACCENT          = new Color(0x4F5BD5);
    public static final Color ACCENT_HOVER    = new Color(0x3D49C4);

    /** Fondo general de la aplicación (muy claro, azul-blanco) */
    public static final Color BG_APP          = new Color(0xF5F7FF);
    /** Fondo de tarjetas y formularios */
    public static final Color BG_CARD         = Color.WHITE;

    /** Color del encabezado de tabla — indigo sólido como referencia */
    public static final Color TABLE_HEADER_BG  = new Color(0x5B6ADA);
    public static final Color TABLE_HEADER_FG  = Color.WHITE;

    /** Borde general */
    public static final Color BORDER          = new Color(0xE5E7EB);

    /** Texto principal */
    public static final Color TEXT_PRIMARY    = new Color(0x1A1F36);
    /** Texto secundario / subtítulos */
    public static final Color TEXT_SECONDARY  = new Color(0x8892B0);

    /** Verde éxito */
    public static final Color SUCCESS         = new Color(0x16A34A);
    /** Verde claro para badge "Activo" */
    public static final Color SUCCESS_BG      = new Color(0xDCFCE7);
    /** Ámbar advertencia */
    public static final Color WARNING         = new Color(0xD97706);
    /** Ámbar claro para badge "Advertencia" */
    public static final Color WARNING_BG      = new Color(0xFEF3C7);
    /** Rojo peligro */
    public static final Color DANGER          = new Color(0xDC2626);
    /** Rojo claro para badge "Inactivo"/"Sin stock" */
    public static final Color DANGER_BG       = new Color(0xFEE2E2);
    /** Información */
    public static final Color INFO            = new Color(0x2563EB);
    public static final Color INFO_BG         = new Color(0xDBEAFE);

    // Colores de roles — cada rol tiene su propio color único (FR-002 RBAC)
    public static final Color ROLE_ADMIN_COLOR   = new Color(0x4F5BD5); // indigo
    public static final Color ROLE_ADMIN_BG      = new Color(0xE0E7FF);
    public static final Color ROLE_GERENTE_COLOR = new Color(0xD97706); // amber
    public static final Color ROLE_GERENTE_BG    = new Color(0xFEF3C7);
    public static final Color ROLE_VENDEDOR_COLOR= new Color(0x16A34A); // green
    public static final Color ROLE_VENDEDOR_BG   = new Color(0xDCFCE7);
    public static final Color ROLE_ALMACEN_COLOR = new Color(0x7C3AED); // purple
    public static final Color ROLE_ALMACEN_BG    = new Color(0xEDE9FE);
    public static final Color ROLE_OTHER_COLOR   = new Color(0x0284C7); // sky
    public static final Color ROLE_OTHER_BG      = new Color(0xE0F2FE);

    // ============================================================
    // TIPOGRAFÍA — Segoe UI (sistema Windows), fallback Sans-Serif
    // ============================================================
    private static final String FAM = "Segoe UI";
    public static final Font H1        = new Font(FAM, Font.BOLD, 22);
    public static final Font H2        = new Font(FAM, Font.BOLD, 16);
    public static final Font SUBTITLE  = new Font(FAM, Font.PLAIN, 14);
    public static final Font BODY      = new Font(FAM, Font.PLAIN, 13);
    public static final Font BODY_BOLD = new Font(FAM, Font.BOLD, 13);
    public static final Font LABEL     = new Font(FAM, Font.BOLD, 11);
    public static final Font CAPTION   = new Font(FAM, Font.PLAIN, 11);
    public static final Font KPI_VALUE = new Font(FAM, Font.BOLD, 28);
    public static final Font KPI_LABEL = new Font(FAM, Font.PLAIN, 11);

    // ============================================================
    // ESPACIADO — múltiplos de 8 (obligatorio según manual §9)
    // ============================================================
    public static final int SPACE_XS = 4, SPACE_SM = 8, SPACE_MD = 16,
                             SPACE_LG = 24, SPACE_XL = 32;

    // ============================================================
    // BOTONES
    // ============================================================

    /** Botón primario indigo — acción principal de la pantalla. */
    public static JButton primaryButton(String text) {
        JButton b = baseButton(text);
        b.setBackground(PRIMARY);
        b.setForeground(Color.WHITE);
        b.putClientProperty(FlatClientProperties.STYLE,
                "arc: 8; focusWidth: 0; hoverBackground: " + hex(PRIMARY_DARK) + ";");
        return b;
    }

    /** Botón secundario (borde) — acciones secundarias. */
    public static JButton secondaryButton(String text) {
        JButton b = baseButton(text);
        b.setBackground(BG_CARD);
        b.setForeground(TEXT_PRIMARY);
        b.putClientProperty(FlatClientProperties.STYLE,
                "arc: 8; borderWidth: 1; borderColor: " + hex(BORDER) + "; focusWidth: 0;");
        return b;
    }

    /** Botón peligro con borde rojo — acciones destructivas no críticas. */
    public static JButton dangerOutlineButton(String text) {
        JButton b = baseButton(text);
        b.setBackground(BG_CARD);
        b.setForeground(DANGER);
        b.putClientProperty(FlatClientProperties.STYLE,
                "arc: 8; borderWidth: 1; borderColor: " + hex(DANGER) + "; focusWidth: 0;");
        return b;
    }

    /** Botón peligro sólido rojo — acciones críticas (eliminación definitiva). */
    public static JButton dangerSolidButton(String text) {
        JButton b = baseButton(text);
        b.setBackground(DANGER);
        b.setForeground(Color.WHITE);
        b.putClientProperty(FlatClientProperties.STYLE, "arc: 8; focusWidth: 0;");
        return b;
    }

    /** Botón de icono compacto para columnas de acciones en tablas. */
    public static JButton iconButton(String icon, Color fgColor) {
        JButton b = new JButton(icon);
        b.setFont(new Font(FAM, Font.PLAIN, 14));
        b.setForeground(fgColor);
        b.setBackground(BG_CARD);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMargin(new Insets(2, 6, 2, 6));
        b.setPreferredSize(new Dimension(32, 32));
        b.putClientProperty(FlatClientProperties.STYLE, "arc: 6; focusWidth: 0;");
        return b;
    }

    /** Botón "+ Nueva X" — estilo pill con icono. */
    public static JButton newButton(String label) {
        JButton b = baseButton("+ " + label);
        b.setBackground(PRIMARY);
        b.setForeground(Color.WHITE);
        b.putClientProperty(FlatClientProperties.STYLE,
                "arc: 20; focusWidth: 0; hoverBackground: " + hex(PRIMARY_DARK) + ";");
        b.setPreferredSize(new Dimension(b.getPreferredSize().width + 10, 38));
        return b;
    }

    private static JButton baseButton(String text) {
        JButton b = new JButton(text);
        b.setFont(BODY_BOLD);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMargin(new Insets(6, 16, 6, 16));
        b.setPreferredSize(new Dimension(b.getPreferredSize().width, 38));
        return b;
    }

    // ============================================================
    // CAMPOS DE FORMULARIO
    // ============================================================

    /** Etiqueta de campo en MAYÚSCULAS pequeñas sobre el input. */
    public static JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text.toUpperCase());
        l.setFont(LABEL);
        l.setForeground(TEXT_SECONDARY);
        return l;
    }

    /** Campo de texto estándar con border indigo al hacer focus. */
    public static JTextField textField() {
        JTextField tf = new JTextField();
        tf.setFont(BODY);
        tf.putClientProperty(FlatClientProperties.STYLE,
                "arc: 8; borderColor: " + hex(BORDER) +
                "; focusedBorderColor: " + hex(PRIMARY) + ";");
        tf.setPreferredSize(new Dimension(0, 38));
        return tf;
    }

    /** Campo de solo lectura — fondo suave, sin edición. */
    public static JTextField readOnlyField() {
        JTextField f = textField();
        f.setEditable(false);
        f.setBackground(new Color(0xF9FAFB));
        f.setForeground(TEXT_SECONDARY);
        return f;
    }

    /** ComboBox estilizado para filtros de tabla (mismo alto que textField). */
    public static JComboBox<String> filterCombo(String[] opciones) {
        JComboBox<String> cb = new JComboBox<>(opciones);
        cb.setFont(BODY);
        cb.setPreferredSize(new Dimension(160, 38));
        cb.putClientProperty(FlatClientProperties.STYLE,
                "arc: 8; borderColor: " + hex(BORDER) + ";");
        return cb;
    }

    // ============================================================
    // VALIDACIONES EN CAMPO (KeyListeners)
    // ============================================================
    public static void addNumericValidator(JTextField field, int maxLength) {
        field.addKeyListener(new KeyAdapter() {
            @Override public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar()) && e.getKeyChar() != '.' &&
                    e.getKeyChar() != KeyEvent.VK_BACK_SPACE) { e.consume(); }
                else if (field.getText().length() >= maxLength) { e.consume(); }
            }
        });
    }

    public static void addIntegerValidator(JTextField field, int maxLength) {
        field.addKeyListener(new KeyAdapter() {
            @Override public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar()) &&
                    e.getKeyChar() != KeyEvent.VK_BACK_SPACE) { e.consume(); }
                else if (field.getText().length() >= maxLength) { e.consume(); }
            }
        });
    }

    public static void addTextValidator(JTextField field, int maxLength) {
        field.addKeyListener(new KeyAdapter() {
            @Override public void keyTyped(KeyEvent e) {
                if (field.getText().length() >= maxLength) { e.consume(); }
            }
        });
    }

    // ============================================================
    // TARJETAS / CONTENEDORES
    // ============================================================

    /** Tarjeta blanca con sombra sutil y borde redondeado. */
    public static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(SPACE_MD, SPACE_MD, SPACE_MD, SPACE_MD)));
        return p;
    }

    /** Encabezado de sección: título H2 + acción opcional a la derecha. */
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

    /**
     * Encabezado de pantalla: breadcrumb + título H1 + acción primaria opcional (ej. "+ Nuevo").
     * Implementa el patrón de cabecera visto en la referencia web.
     */
    public static JPanel screenHeader(String titulo, String breadcrumb, JComponent actionButton) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(0, 0, SPACE_MD, 0));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel lblBread = new JLabel(breadcrumb);
        lblBread.setFont(CAPTION);
        lblBread.setForeground(TEXT_SECONDARY);

        JLabel lblTitle = new JLabel(titulo);
        lblTitle.setFont(H1);
        lblTitle.setForeground(TEXT_PRIMARY);

        left.add(lblBread);
        left.add(Box.createVerticalStrut(2));
        left.add(lblTitle);

        p.add(left, BorderLayout.WEST);
        if (actionButton != null) p.add(actionButton, BorderLayout.EAST);
        return p;
    }

    /** Sobrecarga sin botón de acción (compatibilidad con módulos existentes). */
    public static JPanel screenHeader(String titulo, String breadcrumb) {
        return screenHeader(titulo, breadcrumb, null);
    }

    // ============================================================
    // TARJETA KPI (Dashboard)
    // ============================================================

    /**
     * Tarjeta KPI para el Dashboard con ícono a la derecha.
     * Retorna panel con propiedades "val" y "sub" para actualizaciones en tiempo real.
     * Implementa: FR-001 (KPIs del Dashboard).
     */
    public static JPanel kpiCard(String label, String valor, String subLabel,
                                  Color acento, String icono) {
        JPanel card = new JPanel(new BorderLayout(SPACE_MD, 0));
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(SPACE_MD, SPACE_LG, SPACE_MD, SPACE_LG)));

        // Borde izquierdo de color
        JPanel barraIzq = new JPanel();
        barraIzq.setBackground(acento);
        barraIzq.setPreferredSize(new Dimension(4, 0));

        JPanel contenido = new JPanel();
        contenido.setOpaque(false);
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(KPI_LABEL);
        lblLabel.setForeground(TEXT_SECONDARY);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(KPI_VALUE);
        lblValor.setForeground(TEXT_PRIMARY);

        JLabel lblSub = new JLabel(subLabel);
        lblSub.setFont(CAPTION);
        lblSub.setForeground(TEXT_SECONDARY);

        contenido.add(lblLabel);
        contenido.add(Box.createVerticalStrut(4));
        contenido.add(lblValor);
        contenido.add(Box.createVerticalStrut(2));
        contenido.add(lblSub);

        // Ícono a la derecha con fondo de color suave
        JLabel lblIcono = new JLabel(icono, SwingConstants.CENTER);
        lblIcono.setFont(new Font(FAM, Font.PLAIN, 22));
        lblIcono.setForeground(acento);
        JPanel iconPanel = new JPanel(new GridBagLayout());
        iconPanel.setBackground(mezclarConBlanco(acento, 0.85f));
        iconPanel.setPreferredSize(new Dimension(48, 48));
        iconPanel.setBorder(BorderFactory.createEmptyBorder());
        try {
            ((JPanel)iconPanel).setBorder(BorderFactory.createLineBorder(mezclarConBlanco(acento, 0.70f), 1, true));
        } catch (Exception ignored) {}
        iconPanel.add(lblIcono);

        // Guardar referencias para actualización en tiempo real
        card.putClientProperty("val", lblValor);
        card.putClientProperty("sub", lblSub);

        JPanel wrapper = new JPanel(new BorderLayout(SPACE_MD, 0));
        wrapper.setOpaque(false);
        wrapper.add(barraIzq, BorderLayout.WEST);
        wrapper.add(contenido, BorderLayout.CENTER);
        wrapper.add(iconPanel, BorderLayout.EAST);
        card.add(wrapper);

        return card;
    }

    // ============================================================
    // BADGES
    // ============================================================

    /**
     * Badge de estado: "Activo" (verde), "Inactivo" (rojo), "Pendiente" (ámbar).
     * Implementa: RNF-02 — identificación visual del estado en tablas.
     */
    public static JLabel statusBadge(String texto) {
        Color bg, fg;
        switch (texto.toUpperCase()) {
            case "ACTIVO":
                bg = SUCCESS_BG; fg = SUCCESS; break;
            case "INACTIVO":
                bg = DANGER_BG; fg = DANGER; break;
            case "PENDIENTE":
                bg = WARNING_BG; fg = WARNING; break;
            case "SIN STOCK":
                bg = DANGER_BG; fg = DANGER; break;
            case "COMPLETADA":
                bg = SUCCESS_BG; fg = SUCCESS; break;
            case "ANULADA":
                bg = DANGER_BG; fg = DANGER; break;
            default:
                bg = INFO_BG; fg = INFO; break;
        }
        return buildBadge(texto, bg, fg);
    }

    /**
     * Badge de rol con color único por tipo (FR-002 RBAC — identificación visual de roles).
     * Cada rol tiene su paleta definida en los tokens de color.
     */
    public static JLabel rolBadge(String rol) {
        Color bg, fg;
        if (rol == null) rol = "—";
        switch (rol.toUpperCase()) {
            case "ADMINISTRADOR":
            case "ADMIN":
                bg = ROLE_ADMIN_BG; fg = ROLE_ADMIN_COLOR; break;
            case "GERENTE":
                bg = ROLE_GERENTE_BG; fg = ROLE_GERENTE_COLOR; break;
            case "VENDEDOR":
            case "CAJERO":
                bg = ROLE_VENDEDOR_BG; fg = ROLE_VENDEDOR_COLOR; break;
            case "ALMACENERO":
            case "SUPERVISOR":
                bg = ROLE_ALMACEN_BG; fg = ROLE_ALMACEN_COLOR; break;
            default:
                bg = ROLE_OTHER_BG; fg = ROLE_OTHER_COLOR; break;
        }
        return buildBadge(rol, bg, fg);
    }

    /** Constructor interno de badge: label con fondo de color y bordes redondeados. */
    private static JLabel buildBadge(String texto, Color bg, Color fg) {
        JLabel badge = new JLabel(texto, SwingConstants.CENTER);
        badge.setFont(new Font(FAM, Font.BOLD, 11));
        badge.setForeground(fg);
        badge.setOpaque(true);
        badge.setBackground(bg);
        badge.setBorder(new EmptyBorder(3, 10, 3, 10));
        badge.putClientProperty(FlatClientProperties.STYLE,
                "arc: 12; background: " + hex(bg) + ";");
        return badge;
    }

    /**
     * Panel con nombre de usuario + badge de rol — para el topbar del Dashboard.
     * Implementa el patrón "Admin Principal | Administrador" de la referencia.
     */
    public static JPanel topbarUserBadge(String nombre, String rol) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        p.setOpaque(false);
        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(BODY_BOLD);
        lblNombre.setForeground(TEXT_PRIMARY);
        p.add(lblNombre);
        p.add(rolBadge(rol));
        return p;
    }

    // ============================================================
    // TABLAS
    // ============================================================

    /**
     * Tabla estilizada con header indigo, filas alternas y hover.
     * Implementa: sección 9 del manual (tablas con filas alternas ≥40px, hover, columnas de acciones).
     */
    public static JTable styledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(BODY);
        table.setRowHeight(42);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(mezclarConBlanco(PRIMARY, 0.88f));
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(false);

        // Header indigo sólido como en la referencia
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font(FAM, Font.BOLD, 12));
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(TABLE_HEADER_FG);
        header.setPreferredSize(new Dimension(0, 42));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);
        header.putClientProperty(FlatClientProperties.STYLE,
                "background: " + hex(TABLE_HEADER_BG) + "; foreground: #ffffff;");
        ((javax.swing.table.DefaultTableCellRenderer) header.getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.LEFT);

        // Renderizador con zebra striping
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        t, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? BG_CARD : new Color(0xF8F9FF));
                }
                setBorder(new EmptyBorder(0, SPACE_MD, 0, SPACE_MD));
                setForeground(isSelected ? TEXT_PRIMARY : TEXT_PRIMARY);
                return c;
            }
        });

        return table;
    }

    // ============================================================
    // SEPARADOR
    // ============================================================

    /** Separador horizontal con color del borde del sistema. */
    public static JSeparator separator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER);
        sep.setBackground(BORDER);
        return sep;
    }

    // ============================================================
    // PANEL DE BÚSQUEDA / FILTROS
    // ============================================================

    /**
     * Panel de búsqueda estandarizado: campo de búsqueda con ícono de lupa.
     * Implementa el patrón de búsqueda de la referencia web.
     */
    public static JTextField searchField(String placeholder) {
        JTextField tf = textField();
        tf.putClientProperty("JTextField.placeholderText", "🔍  " + placeholder);
        tf.setPreferredSize(new Dimension(240, 38));
        return tf;
    }

    // ============================================================
    // HELPERS PRIVADOS
    // ============================================================

    public static String hex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }

    public static Color mezclarConBlanco(Color c, float proporcionBlanco) {
        int r = (int) (c.getRed()   + (255 - c.getRed())   * proporcionBlanco);
        int g = (int) (c.getGreen() + (255 - c.getGreen()) * proporcionBlanco);
        int b = (int) (c.getBlue()  + (255 - c.getBlue())  * proporcionBlanco);
        return new Color(
            Math.min(255, Math.max(0, r)),
            Math.min(255, Math.max(0, g)),
            Math.min(255, Math.max(0, b))
        );
    }

    // ============================================================
    // SOBRECARGAS DE COMPATIBILIDAD — mantienen compilación de código existente
    // ============================================================

    /**
     * Sobrecarga de kpiCard sin ícono explícito.
     * Compatibilidad con FrmDashboard que llama kpiCard(label,valor,sub,Color).
     */
    public static JPanel kpiCard(String label, String valor, String subLabel, Color acento) {
        return kpiCard(label, valor, subLabel, acento, "●");
    }

    /**
     * Sobrecarga de statusBadge con color ignorado (el color ahora se determina por el texto).
     * Compatibilidad con IFrmAlertasInventario y otros módulos existentes.
     */
    public static JLabel statusBadge(String texto, Color colorIgnorado) {
        return statusBadge(texto);
    }
}

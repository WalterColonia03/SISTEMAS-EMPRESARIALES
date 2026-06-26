# 🧠 INTEGRACIÓN TPS → KM — ERP LAREDO
**Sesión 11 · Gestión del Conocimiento · Ms. Ing. Elías Santa Cruz**  
*Nonaka & Takeuchi (1995) · Laudon & Laudon (2020)*

---

## 1. Fundamento Teórico: De Datos a Conocimiento (Sesión 11)

La Sesión 11 establece la cadena de valor de la información que debemos aplicar en LAREDO:

```
DATOS            INFORMACIÓN           CONOCIMIENTO          DECISIÓN
──────────────────────────────────────────────────────────────────────────
Hechos brutos    Datos procesados      Información + contexto  Acción
sin contexto     con significado       + experiencia           estratégica

ventas.txt   →   Reporte de ventas  →  "Las galletas Oreo    →  Pedir Oreo
"2026-06-15,       del 1–15 Jun         se venden 3×más           los lunes
 P042, 5"          por producto         los viernes"              por semana

productos.txt →  Alerta stock bajo  →  "El stock se agota   →  Configurar
 stock=2          en Dashboard         cada jueves, hoy es      stock mín = 10
                                        lunes → actuar ahora"    y reordenar

clientes.txt  →  Top 5 clientes     →  "María López compra  →  Ofrecerle
 compras=45       por volumen           cada 2 días, es VIP      descuento 5%
                                        y nunca recibió           exclusivo"
                                        beneficio"
```

---

## 2. El TPS como Fuente de Conocimiento

El TPS del Minimarket LAREDO genera datos operativos en 5 archivos que, al ser analizados, se convierten en conocimiento estratégico:

| Archivo TPS | Datos que genera | Conocimiento que produce |
|-------------|-----------------|--------------------------|
| `ventas.txt` | Cada transacción: producto, cantidad, precio, fecha, hora | Patrones de demanda por día/hora, productos estrella, temporadas |
| `productos.txt` | Código, nombre, stock, precio | Rotación de inventario, productos sin movimiento, rentabilidad |
| `clientes.txt` | DNI, compras, frecuencia | Segmentación de clientes, fidelización, comportamiento de compra |
| `categorias.txt` | Agrupación de productos | Qué categorías generan más margen |
| `usuarios.txt` | Login, accesos | Patrones de uso del sistema, productividad por cajero |

---

## 3. Herramientas KM que el ERP LAREDO debe incorporar (Sesión 11)

La Sesión 11 menciona herramientas de KM. Estas son sus equivalentes en el ERP:

| Herramienta KM (Sesión 11) | Equivalente en ERP LAREDO | HU relacionada |
|----------------------------|--------------------------|----------------|
| **Dashboard / KPIs** | FrmDashboard real con ingresos, alertas y tendencias | FR-001, FR-002 |
| **Data Warehouse / ETL** | Tablas de reportes agregados en H2 (vistas materializadas) | FR-017, FR-018, FR-019 |
| **Business Intelligence (BI)** | Gráficos JFreeChart en el Dashboard y en reportes | FR-006, FR-017 |
| **Lessons Learned** | Módulo de "Observaciones de Temporada" en Reportes | FR-049 (nuevo) |
| **Wikis / Base documental** | Módulo de "Notas del Gerente" con historial | FR-050 (nuevo) |
| **Directorios** | Listado de proveedores con historial de desempeño | FR-013 |
| **After Action Review** | Comparativo de período anterior vs actual en reportes | FR-017, FR-019 |
| **Comunidades de Práctica** | Manual de procesos del minimarket (externo al sistema) | Documentación |
| **Intranet** | No aplica para LAREDO (sistema desktop monousuario) | — |

---

## 4. Nuevas HU de KM — FR-049 y FR-050

Basado en la Sesión 11, se proponen dos HU adicionales de Gestión del Conocimiento:

### FR-049 — Registrar observaciones de temporada (Lessons Learned)

- **Como** Gerente del Minimarket LAREDO
- **Quiero** registrar notas sobre lo que aprendí en cada período de ventas
- **Para** que el conocimiento del negocio no se pierda y pueda guiar decisiones futuras

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | El Gerente cierra el mes de junio | Accede a Reportes → Notas de Temporada | Puede ingresar una observación libre de texto: "En junio las gaseosas aumentaron 40% por el calor. Pedir doble stock en verano." |
| CA-2 | El Gerente abre el módulo al año siguiente | Filtra por "junio" de años anteriores | Ve las notas de junio 2026 y puede tomarlas como referencia para su compra |
| CA-3 | La nota tiene más de 1000 caracteres | Presiona Guardar | ⚠ "Las notas no pueden superar los 1000 caracteres." |

> **SP:** 2 · **MoSCoW:** Could · **Archivo nuevo:** `observaciones.txt` o tabla `OBSERVACIONES`

---

### FR-050 — Comparativo vs período anterior (After Action Review)

- **Como** Gerente del Minimarket LAREDO
- **Quiero** ver automáticamente cómo le fue este mes comparado con el mes anterior
- **Para** evaluar si las decisiones tomadas mejoraron o empeoraron el negocio

| # | Given | When | Then |
|---|-------|------|------|
| CA-1 | Hay ventas registradas en mayo y junio 2026 | El Gerente genera el reporte de junio | El reporte incluye una columna "vs mayo 2026" con la variación (+/-) en ventas totales, unidades vendidas y número de clientes |
| CA-2 | No hay datos del período anterior | — | Muestra "Sin datos comparativos para el período anterior" sin error |
| CA-3 | El Gerente exporta el comparativo | — | El .xlsx incluye las dos columnas lado a lado con formato condicional (verde si mejora, rojo si empeora) |

> **SP:** 3 · **MoSCoW:** Could

---

## 5. Arquitectura del Flujo TPS → KM en LAREDO

```
┌──────────────────────────────────────────────────────────────────────┐
│                    CAPA TPS (Transaccional)                          │
│                                                                      │
│  POS (IFrmPuntoVenta) → ventas.txt / tabla VENTAS                  │
│  Compras (IFrmRegistroCompras) → compras.txt / tabla COMPRAS        │
│  Clientes → clientes.txt / tabla CLIENTES                           │
└──────────────────────┬───────────────────────────────────────────────┘
                       │ ETL Automático
                       ▼ (al guardar cada operación)
┌──────────────────────────────────────────────────────────────────────┐
│                    CAPA ANALÍTICA (KM)                               │
│                                                                      │
│  Dashboard KPI:                                                      │
│  ├── Ventas hoy/semana/mes (SUM de ventas.fecha = hoy)              │
│  ├── Top 5 productos (GROUP BY producto ORDER BY SUM qty)           │
│  ├── Stock crítico (WHERE stock ≤ stockMinimo)                      │
│  └── Vencimientos próximos (WHERE fechaVencimiento ≤ hoy + 7)      │
│                                                                      │
│  Reportes BI:                                                        │
│  ├── Gráfico de ventas 7 días (JFreeChart BarChart)                 │
│  ├── Ranking de productos (JFreeChart PieChart)                     │
│  ├── Comparativo vs período anterior (After Action Review)          │
│  └── Exportación .xlsx para análisis externo                        │
└──────────────────────┬───────────────────────────────────────────────┘
                       │ Conocimiento aplicado
                       ▼
┌──────────────────────────────────────────────────────────────────────┐
│                    CAPA DE DECISIÓN (Estratégica)                    │
│                                                                      │
│  Gerente toma decisiones basadas en datos:                          │
│  ├── "Pedir más Oreo antes del viernes" (dato: pico de viernes)     │
│  ├── "Cliente María López → descuento VIP" (dato: top cliente)      │
│  ├── "Retirar Leche Lote 4" (dato: alerta de vencimiento)           │
│  └── "Reducir pedido de bebidas en invierno" (lección registrada)   │
│                                                                      │
│  Base de Conocimiento:                                              │
│  ├── Observaciones de temporada (FR-049)                            │
│  └── Comparativos históricos (FR-050)                               │
└──────────────────────────────────────────────────────────────────────┘
```

---

## 6. Implementación del Dashboard como herramienta KM

El Dashboard es el puente principal entre TPS y KM. Cada KPI requiere una consulta específica:

```java
// DashboardService.java — lógica de negocio del Dashboard
public class DashboardService {

    // KPI-1: Ventas del día (FR-001)
    public double getVentasHoy() {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM VENTAS " +
                     "WHERE fecha = CURRENT_DATE AND estado = 'COMPLETADA'";
        // Si usa .txt: filtrar ventas.txt por fecha = LocalDate.now()
    }

    // KPI-2: Ventas de la semana
    public double getVentasSemana() {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM VENTAS " +
                     "WHERE WEEK(fecha) = WEEK(CURRENT_DATE) " +
                     "AND YEAR(fecha) = YEAR(CURRENT_DATE)";
    }

    // KPI-3: Top 5 productos del día (KM: qué está vendiendo bien HOY)
    public List<ItemRanking> getTop5ProductosHoy() {
        String sql = "SELECT p.nombre, SUM(dv.cantidad) AS unidades, " +
                     "SUM(dv.subtotal) AS ingresos " +
                     "FROM DETALLE_VENTA dv " +
                     "JOIN PRODUCTOS p ON dv.idProducto = p.id " +
                     "JOIN VENTAS v ON dv.idVenta = v.id " +
                     "WHERE v.fecha = CURRENT_DATE " +
                     "GROUP BY p.id, p.nombre " +
                     "ORDER BY ingresos DESC " +
                     "LIMIT 5";
    }

    // KPI-4: Productos con stock crítico (FR-002)
    public List<Producto> getProductosStockCritico() {
        String sql = "SELECT * FROM PRODUCTOS " +
                     "WHERE stock <= stockMinimo AND activo = TRUE " +
                     "ORDER BY (stock - stockMinimo) ASC";
    }

    // KPI-5: Comparativo vs ayer (KM: ¿estamos mejor o peor que ayer?)
    public double getVariacionVsAyer() {
        double hoy  = getVentasHoy();
        double ayer = getVentasPorFecha(LocalDate.now().minusDays(1));
        return ayer > 0 ? ((hoy - ayer) / ayer) * 100 : 0;
    }
}
```

---

## 7. Implementación de Gráficos con JFreeChart

```java
// GraficoVentas7Dias.java — gráfico de barras para el Dashboard
import org.jfreeChart.JFreeChart;
import org.jfreeChart.chart.ChartFactory;
import org.jfreeChart.data.category.DefaultCategoryDataset;

public class GraficoVentas7Dias {

    public ChartPanel crear() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Obtener datos de los últimos 7 días
        for (int i = 6; i >= 0; i--) {
            LocalDate fecha = LocalDate.now().minusDays(i);
            double ventas = dashboardService.getVentasPorFecha(fecha);
            String etiqueta = fecha.getDayOfWeek()
                                   .getDisplayName(TextStyle.SHORT, new Locale("es"));
            dataset.addValue(ventas, "Ventas S/.", etiqueta);
        }

        JFreeChart chart = ChartFactory.createBarChart(
            null,           // sin título (ya hay uno en el panel)
            null,           // eje X: sin label
            "S/.",          // eje Y
            dataset
        );

        // Personalizar con colores del sistema
        chart.setBackgroundPaint(UITokens.BG_CARD);
        chart.getPlot().setBackgroundPaint(UITokens.BG_CARD);

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(450, 200));
        return panel;
    }
}
```

**Librería requerida:** `jfreechart-1.5.x.jar` — agregar a la carpeta `/librerias/` del proyecto.

---

## 8. Desafíos de la Integración TPS → KM (Sesión 11)

| Desafío (Sesión 11) | Manifestación en LAREDO | Solución propuesta |
|--------------------|------------------------|-------------------|
| **Silos de información** | Los 5 `.txt` no se cruzan entre sí | Base de datos H2 con Foreign Keys que relacionan ventas ↔ productos ↔ clientes |
| **Seguridad de datos** | `usuarios.txt` con contraseñas en texto plano | Cifrado SHA-256 (RNF-03) + migración a BD |
| **Resistencia cultural** | El equipo está acostumbrado al sistema de `.txt` | Migración gradual por fases (8_MODELO_DATOS_BD.md) + mantener `.txt` como respaldo |
| **Interoperabilidad** | El TPS no "habla" con el Dashboard | Patrón DAO con interfaces — los datos vienen de la misma fuente ya sea `.txt` o H2 |

---

## 9. Beneficios de la Integración para LAREDO (Sesión 11)

| Beneficio (Sesión 11) | Cómo se logra en LAREDO |
|-----------------------|------------------------|
| **Decisiones más informadas** | Dashboard con KPIs en tiempo real; reportes exportables para análisis |
| **Mejora continua y aprendizaje** | FR-049 (observaciones) + FR-050 (comparativo) + Bitácora para patrones |
| **Optimización de procesos** | Alertas automáticas de stock → menos quiebres de inventario |
| **Mayor colaboración** | Datos centralizados en H2 accesibles para todos los módulos y usuarios |

---

## 10. Referencia cruzada con otras sesiones

| Sesión | Concepto | Cómo conecta con KM |
|--------|----------|---------------------|
| **Sesión 9** | Base de datos única | Elimina silos → facilita el análisis KM |
| **Sesión 10** | Optimización post-implementación | Auditorías → detectar qué conocimiento no se está aprovechando |
| **Sesión 11** | TPS → KM directo | El TPS es la fuente; el Dashboard y los reportes son el puente |

---

*Referencia: Nonaka & Takeuchi (1995) · Laudon & Laudon (2020) · Sesión 11 — Ms. Ing. Elías Santa Cruz*

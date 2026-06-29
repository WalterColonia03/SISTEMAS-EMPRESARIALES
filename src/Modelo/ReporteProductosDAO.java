package Modelo;

import Utils.LoggerGlobal;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO especializado en reportes de Inteligencia de Negocio.
 *
 * Historias de Usuario implementadas:
 * - FR-006 Ranking de productos más vendidos (IT-11, CA-1 a CA-5)
 * - FR-017 Reporte de productos con filtros (IT-11)
 * - FR-018 Reporte de stock bajo (IT-11, CA-1 a CA-4)
 * - FR-019-v2 Reporte de ganancias reales por período (IT-11, CA-1 a CA-4)
 *
 * Principio: Separación de responsabilidades (Clean Code §10).
 * Este DAO no modifica datos — solo lee y agrega.
 *
 * Creado: 2026-06-26T02:06:00-05:00
 */
public class ReporteProductosDAO {

    // ─────────────────────────────────────────────────────────────────────────
    // FR-006 — Ranking de productos más vendidos
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Retorna el ranking de los N productos más vendidos por cantidad de unidades.
     *
     * @param limite Máximo de productos a retornar (normalmente 10)
     * @param mesNum Número de mes 1-12, o 0 para todos
     * @param anio   Año (ej. 2026), o 0 para todos
     * @return Lista de Object[] con columnas:
     *         [0] idProducto, [1] nombre, [2] cantidadVendida, [3] totalIngreso
     */
    public List<Object[]> rankingProductosMasVendidos(int limite, int mesNum, int anio) {
        List<Object[]> resultado = new ArrayList<>();

        // El filtro de mes/año se aplica sobre la columna fecha de tb_venta
        // (DD/MM/YYYY)
        // Usamos SUBSTRING para extraer mes y año sin función nativa de fecha
        // (ya que fecha está almacenada como VARCHAR — plan de migración en R4)
        StringBuilder sql = new StringBuilder(
                "SELECT dv.idProducto, p.nombre, " +
                        "       SUM(dv.cantidad) AS cantidadVendida, " +
                        "       SUM(dv.cantidad * dv.precioUnitario) AS totalIngreso " +
                        "FROM tb_detalle_venta dv " +
                        "INNER JOIN tb_producto p ON dv.idProducto = p.idProducto " +
                        "INNER JOIN tb_venta v ON dv.idVenta = v.idVenta ");

        List<Object> params = new ArrayList<>();
        buildFechaWhere(sql, params, mesNum, anio, "v.fecha");

        sql.append(" GROUP BY dv.idProducto, p.nombre ")
                .append(" ORDER BY cantidadVendida DESC ")
                .append(" LIMIT ?");
        params.add(limite);

        try (Connection conn = ConexionDB.getConexion();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            setParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(new Object[] {
                            rs.getInt("idProducto"),
                            rs.getString("nombre"),
                            rs.getInt("cantidadVendida"),
                            rs.getBigDecimal("totalIngreso") != null
                                    ? rs.getBigDecimal("totalIngreso").setScale(2, RoundingMode.HALF_UP)
                                    : BigDecimal.ZERO
                    });
                }
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("ReporteProductosDAO.rankingProductosMasVendidos() falló", ex);
        }
        return resultado;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // FR-018 — Reporte de stock bajo
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Retorna todos los productos cuyo stock actual está por debajo del mínimo.
     *
     * @return Lista de Object[] con:
     *         [0] idProducto, [1] nombre, [2] stockActual, [3] stockMinimo, [4]
     *         diferencia
     */
    public List<Object[]> productosConStockBajo() {
        List<Object[]> resultado = new ArrayList<>();
        // stockMinimo usa la columna ya existente en tb_producto
        // Si no existe aún, el query retornará vacío — se maneja en la vista
        String sql = "SELECT idProducto, nombre, cantidad AS stockActual, " +
                "       COALESCE(stockMinimo, 5) AS stockMinimo, " +
                "       (COALESCE(stockMinimo, 5) - cantidad) AS diferencia " +
                "FROM tb_producto " +
                "WHERE cantidad < COALESCE(stockMinimo, 5) AND estado = 1 " +
                "ORDER BY diferencia DESC";
        try (Connection conn = ConexionDB.getConexion();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                resultado.add(new Object[] {
                        rs.getInt("idProducto"),
                        rs.getString("nombre"),
                        rs.getInt("stockActual"),
                        rs.getInt("stockMinimo"),
                        rs.getInt("diferencia")
                });
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("ReporteProductosDAO.productosConStockBajo() falló", ex);
        }
        return resultado;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // FR-052 — Monitor de Alertas: Sin Rotación
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Retorna los productos que no han tenido ventas en los últimos 30 días.
     * Implementación: FR-052.
     * @return Lista de Object[] con: [0] nombre, [1] stockActual, [2] ingresos(mock), [3] salidas(mock), [4] diasInactivo
     */
    public List<Object[]> productosSinRotacion() {
        List<Object[]> resultado = new ArrayList<>();
        String sql = "SELECT p.nombre, p.cantidad, " +
                     "MAX(v.fecha) as ultimaVenta " +
                     "FROM tb_producto p " +
                     "LEFT JOIN tb_detalle_venta dv ON p.idProducto = dv.idProducto " +
                     "LEFT JOIN tb_venta v ON dv.idVenta = v.idVenta " +
                     "WHERE p.estado = 1 " +
                     "GROUP BY p.idProducto, p.nombre, p.cantidad";
                     
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            java.util.Date hoy = new java.util.Date();
            long unDiaMillis = 1000 * 60 * 60 * 24;
            
            while (rs.next()) {
                String uVentaStr = rs.getString("ultimaVenta");
                long diasInactivo = 31; // Default to > 30 if never sold
                
                if (uVentaStr != null && !uVentaStr.isEmpty()) {
                    try {
                        // Tratar de parsear solo la fecha si viene con hora
                        String soloFecha = uVentaStr.split(" ")[0]; 
                        java.util.Date uVenta = sdf.parse(soloFecha);
                        diasInactivo = (hoy.getTime() - uVenta.getTime()) / unDiaMillis;
                    } catch (Exception e) {}
                }
                
                if (diasInactivo > 30) {
                    String diasTexto = uVentaStr == null ? "Nunca vendido" : diasInactivo + " días";
                    resultado.add(new Object[] {
                        rs.getString("nombre"),
                        rs.getInt("cantidad"),
                        0, // Ingresos 30d (pendiente módulo compras)
                        0, // Salidas 30d 
                        diasTexto
                    });
                }
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("ReporteProductosDAO.productosSinRotacion() falló", ex);
        }
        return resultado;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // FR-019-v2 — Ganancias reales por período
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Calcula ganancias reales cruzando ingresos (ventas) con costos (compras)
     * por producto en el período especificado.
     *
     * Corrección de Incoherencia I-1: la ganancia se calcula desde datos reales
     * de la BD, no asumida. Productos sin compra registrada se muestran con
     * costo = 0 y una advertencia.
     *
     * @return Lista de Object[] con:
     *         [0] nombre, [1] cantidadVendida, [2] ingresoTotal,
     *         [3] costoReal, [4] gananciabruta, [5] margenPct, [6] tieneAdvertencia
     */
    public List<Object[]> gananciasPorProducto(int mesNum, int anio) {
        List<Object[]> resultado = new ArrayList<>();

        // Query de ingresos (ventas)
        StringBuilder sqlIngresos = new StringBuilder(
                "SELECT dv.idProducto, p.nombre, " +
                        "       SUM(dv.cantidad) AS cantVendida, " +
                        "       SUM(dv.cantidad * dv.precioUnitario) AS ingresoTotal " +
                        "FROM tb_detalle_venta dv " +
                        "INNER JOIN tb_producto p ON dv.idProducto = p.idProducto " +
                        "INNER JOIN tb_venta v ON dv.idVenta = v.idVenta ");
        List<Object> paramsIng = new ArrayList<>();
        buildFechaWhere(sqlIngresos, paramsIng, mesNum, anio, "v.fecha");
        sqlIngresos.append(" GROUP BY dv.idProducto, p.nombre");

        // Query de costos (última compra del período para el producto)
        StringBuilder sqlCostos = new StringBuilder(
                "SELECT dc.idProducto, " +
                        "       SUM(dc.cantidad * dc.precioUnitario) AS costoTotal " +
                        "FROM tb_detalle_compra dc " +
                        "INNER JOIN tb_compra c ON dc.idCompra = c.idCompra ");
        List<Object> paramsCos = new ArrayList<>();
        buildFechaWhere(sqlCostos, paramsCos, mesNum, anio, "c.fecha");
        sqlCostos.append(" GROUP BY dc.idProducto");

        // Mapa de costos por idProducto
        Map<Integer, BigDecimal> costos = new LinkedHashMap<>();
        try (Connection conn = ConexionDB.getConexion();
                PreparedStatement ps = conn.prepareStatement(sqlCostos.toString())) {

            setParams(ps, paramsCos);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BigDecimal costo = rs.getBigDecimal("costoTotal");
                    costos.put(rs.getInt("idProducto"),
                            costo != null ? costo.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                }
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("ReporteProductosDAO.gananciasPorProducto() — costos falló", ex);
            return resultado;
        }

        // Cruzar con ingresos
        try (Connection conn = ConexionDB.getConexion();
                PreparedStatement ps = conn.prepareStatement(sqlIngresos.toString())) {

            setParams(ps, paramsIng);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idProd = rs.getInt("idProducto");
                    BigDecimal ingreso = rs.getBigDecimal("ingresoTotal");
                    if (ingreso == null)
                        ingreso = BigDecimal.ZERO;
                    ingreso = ingreso.setScale(2, RoundingMode.HALF_UP);

                    BigDecimal costo = costos.getOrDefault(idProd, BigDecimal.ZERO);
                    boolean sinCosto = !costos.containsKey(idProd);

                    BigDecimal ganancia = ingreso.subtract(costo).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal margen = BigDecimal.ZERO;
                    if (ingreso.compareTo(BigDecimal.ZERO) > 0) {
                        margen = ganancia.divide(ingreso, 4, RoundingMode.HALF_UP)
                                .multiply(new BigDecimal("100"))
                                .setScale(2, RoundingMode.HALF_UP);
                    }

                    resultado.add(new Object[] {
                            rs.getString("nombre"),
                            rs.getInt("cantVendida"),
                            ingreso,
                            costo,
                            ganancia,
                            margen,
                            sinCosto // advertencia I-1: sin compra registrada
                    });
                }
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("ReporteProductosDAO.gananciasPorProducto() — ingresos falló", ex);
        }
        return resultado;
    }

    /**
     * Suma total de ganancias para mostrar en el panel de resumen.
     */
    public BigDecimal[] totalesGanancias(List<Object[]> filas) {
        BigDecimal totalIngreso = BigDecimal.ZERO;
        BigDecimal totalCosto = BigDecimal.ZERO;
        BigDecimal totalGanancia = BigDecimal.ZERO;
        for (Object[] f : filas) {
            totalIngreso = totalIngreso.add((BigDecimal) f[2]);
            totalCosto = totalCosto.add((BigDecimal) f[3]);
            totalGanancia = totalGanancia.add((BigDecimal) f[4]);
        }
        return new BigDecimal[] { totalIngreso, totalCosto, totalGanancia };
    }

    // ─────────────────────────────────────────────────────────────────────────
    // FR-052 — Monitor de Alertas: Próximos a Vencer
    // ─────────────────────────────────────────────────────────────────────────

    public List<Object[]> productosProximosAVencer() {
        List<Object[]> resultado = new ArrayList<>();
        String sql = "SELECT nombre, lote, fechaVencimiento, cantidad FROM tb_producto WHERE estado = 1 AND fechaVencimiento IS NOT NULL AND fechaVencimiento != '-' AND fechaVencimiento != ''";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            java.util.Date hoy = new java.util.Date();
            long unDiaMillis = 1000 * 60 * 60 * 24;
            
            while (rs.next()) {
                String fVencStr = rs.getString("fechaVencimiento");
                try {
                    java.util.Date fVenc = sdf.parse(fVencStr);
                    long diasRestantes = (fVenc.getTime() - hoy.getTime()) / unDiaMillis;
                    
                    if (diasRestantes <= 30) {
                        String diasTexto = diasRestantes < 0 ? "VENCIDO" : diasRestantes + " días";
                        resultado.add(new Object[] {
                            rs.getString("nombre"),
                            rs.getString("lote") != null ? rs.getString("lote") : "N/A",
                            fVencStr,
                            diasTexto,
                            rs.getInt("cantidad")
                        });
                    }
                } catch (Exception e) {}
            }
        } catch (SQLException ex) {
            LoggerGlobal.error("ReporteProductosDAO.productosProximosAVencer() falló", ex);
        }
        return resultado;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers privados
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Agrega condición de filtro por mes/año para columnas fecha en formato
     * DD/MM/YYYY.
     * Evita duplicar la lógica de SUBSTRING en cada query.
     */
    private void buildFechaWhere(StringBuilder sql, List<Object> params,
            int mesNum, int anio, String colFecha) {
        boolean hasFiltro = (mesNum > 0 || anio > 0);
        sql.append(hasFiltro ? "WHERE " : "");

        List<String> conds = new ArrayList<>();
        // Mes: posición 4-5 en "DD/MM/YYYY"
        if (mesNum > 0) {
            conds.add("SUBSTRING(" + colFecha + ", 4, 2) = ?");
            params.add(String.format("%02d", mesNum));
        }
        // Año: posición 7-10 en "DD/MM/YYYY"
        if (anio > 0) {
            conds.add("SUBSTRING(" + colFecha + ", 7, 4) = ?");
            params.add(String.valueOf(anio));
        }
        sql.append(String.join(" AND ", conds));
        if (!conds.isEmpty())
            sql.append(" ");
    }

    /** Aplica la lista de parámetros al PreparedStatement en orden. */
    private void setParams(PreparedStatement ps, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }
    }
}

-- ============================================================
-- SCRIPT: Limpieza completa de datos - ERP Minimarket LAREDO
-- Fecha: 2026-06-28
-- Propósito: Dejar la BD lista para carga de datos reales.
--            Conserva la estructura (CREATE TABLE) pero borra
--            todos los datos de negocio.
--            Re-inserta el usuario administrador por defecto.
-- ============================================================

USE bd_erp;

-- Desactivar restricciones FK temporalmente
SET FOREIGN_KEY_CHECKS = 0;

-- ── Tablas de detalle / hijas (primero) ──
TRUNCATE TABLE tb_bitacora;
TRUNCATE TABLE tb_detalle_venta;
TRUNCATE TABLE tb_detalle_compra;
TRUNCATE TABLE tb_kardex;
TRUNCATE TABLE tb_flujo_caja;
TRUNCATE TABLE tb_libro_mayor;
TRUNCATE TABLE tb_cuentas_cp;
TRUNCATE TABLE tb_evaluaciones;

-- ── Tablas de cabecera ──
TRUNCATE TABLE tb_venta;
TRUNCATE TABLE tb_compra;
TRUNCATE TABLE tb_cliente;

-- ── Maestros ──
TRUNCATE TABLE tb_producto;
TRUNCATE TABLE tb_categoria;
TRUNCATE TABLE tb_proveedor;
TRUNCATE TABLE tb_usuario;

-- Reactivar restricciones FK
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- USUARIO ADMINISTRADOR POR DEFECTO
-- usuario: admin | contraseña: admin123 (hash SHA-256)
-- Hash de "admin123" en SHA-256:
-- 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9
-- ============================================================
INSERT INTO tb_usuario
    (idUsuario, nombre, apellido, usuario, password, telefono, rol, estado, sesion_activa)
VALUES
    (1, 'Admin', 'Principal', 'admin',
     '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9',
     '999000001', 'Administrador', 1, 0);

-- ============================================================
-- CATEGORÍAS BASE del Minimarket LAREDO
-- (productos empaquetados, enlatados, embotellados para uso
--  doméstico: sin electrodomésticos ni artículos de limpieza
--  como escobas/recogedores)
-- ============================================================
INSERT INTO tb_categoria (idCategoria, descripcion, estado) VALUES
    (1,  'Aceites y Grasas',       1),
    (2,  'Bebidas Gaseosas',       1),
    (3,  'Bebidas no Alcohólicas', 1),
    (4,  'Licores y Cervezas',     1),
    (5,  'Conservas y Enlatados',  1),
    (6,  'Lácteos',                1),
    (7,  'Snacks y Galletas',      1),
    (8,  'Cereales y Granos',      1),
    (9,  'Condimentos y Salsas',   1),
    (10, 'Agua y Hidratantes',     1),
    (11, 'Jugos y Néctares',       1),
    (12, 'Confitería',             1);

-- ============================================================
-- FIN DEL SCRIPT
-- Los productos, proveedores, clientes, ventas y kardex
-- se cargarán desde el archivo de datos del negocio.
-- ============================================================
SELECT 'BD limpiada exitosamente. Solo admin y categorías base insertadas.' AS resultado;

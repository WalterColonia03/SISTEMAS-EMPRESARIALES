# 🗄️ MODELO DE DATOS Y MIGRACIÓN — ERP LAREDO
**De archivos .txt a H2 Database · Arquitectura 3 Capas (Sesión 9)**

---

## 1. Por qué migrar (Sesión 9 — Base de Datos Única)

> *"El corazón del ERP es una base de datos única que comparte información en tiempo real,  
> evita la duplicación de registros y permite auditar cualquier operación."*  
> — Ms. Ing. Elías Santa Cruz, Sesión 9

| Problema con `.txt` | Solución con H2 |
|---------------------|----------------|
| Sin concurrencia real (RNF-09) | JDBC maneja múltiples conexiones nativo |
| Sin atomicidad (RNF-10) | Transacciones SQL con `commit/rollback` |
| Sin integridad referencial | Foreign Keys entre tablas |
| Contraseñas en texto plano difíciles de migrar | Campo `passwordHash` tipado |
| Consultas = leer TODO el archivo | SQL con `WHERE`, `JOIN`, `ORDER BY` |
| Bitácora editable (viola RNF-06) | Permisos de tabla de solo inserción |
| Escalabilidad imposible | H2 → MySQL sin cambiar lógica (DAO Pattern) |

---

## 2. Tecnología elegida: H2 Database (Embedded)

```xml
<!-- pom.xml (si migramos a Maven) o agregar H2.jar a /librerias/ -->
<!-- h2-2.2.224.jar — ya existe en /librerias/ del proyecto -->

Modo: Embedded (archivo local)
URL:  jdbc:h2:./data/laredo;AUTO_SERVER=TRUE
      → AUTO_SERVER=TRUE permite múltiples conexiones simultáneas
Driver: org.h2.Driver
```

```java
// ConexionDB.java — Singleton de conexión
public class ConexionDB {
    private static final String URL    = "jdbc:h2:./data/laredo;AUTO_SERVER=TRUE";
    private static final String USER   = "sa";
    private static final String PASS   = "";
    private static Connection   conn   = null;

    public static Connection getConexion() throws SQLException {
        if (conn == null || conn.isClosed()) {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection(URL, USER, PASS);
        }
        return conn;
    }

    public static void cerrar() {
        try { if (conn != null) conn.close(); }
        catch (SQLException e) { e.printStackTrace(); }
    }
}
```

---

## 3. Esquema Completo de Base de Datos

### 3.1 USUARIOS

```sql
CREATE TABLE USUARIOS (
    id              VARCHAR(10)  PRIMARY KEY,   -- USR001, USR002...
    username        VARCHAR(20)  NOT NULL UNIQUE,
    passwordHash    VARCHAR(64)  NOT NULL,       -- SHA-256: 64 hex chars
    nombre          VARCHAR(50)  NOT NULL,
    apellidos       VARCHAR(50)  NOT NULL,
    rol             VARCHAR(20)  NOT NULL,       -- CAJERO | GERENTE | ADMIN
    activo          BOOLEAN      DEFAULT TRUE,
    intentosFallidos INT         DEFAULT 0,
    bloqueadoHasta  TIMESTAMP    NULL,
    ultimoAcceso    TIMESTAMP    NULL,
    fechaCreacion   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- Índices
CREATE INDEX idx_usuarios_rol ON USUARIOS(rol);
CREATE INDEX idx_usuarios_activo ON USUARIOS(activo);
```

### 3.2 CATEGORIAS

```sql
CREATE TABLE CATEGORIAS (
    id          VARCHAR(10)  PRIMARY KEY,   -- CAT001, CAT002...
    nombre      VARCHAR(50)  NOT NULL UNIQUE,
    descripcion VARCHAR(200) NULL,
    activo      BOOLEAN      DEFAULT TRUE
);
```

### 3.3 PRODUCTOS

```sql
CREATE TABLE PRODUCTOS (
    id               VARCHAR(10)   PRIMARY KEY,   -- P001, P002...
    codigo           VARCHAR(10)   NOT NULL UNIQUE,
    nombre           VARCHAR(100)  NOT NULL,
    descripcion      VARCHAR(500)  NULL,
    precioVenta      DECIMAL(10,2) NOT NULL CHECK (precioVenta > 0),
    precioCosto      DECIMAL(10,2) NULL     CHECK (precioCosto >= 0),
    stock            INT           NOT NULL DEFAULT 0 CHECK (stock >= 0),
    stockMinimo      INT           NOT NULL DEFAULT 1 CHECK (stockMinimo >= 0),
    fechaVencimiento DATE          NULL,
    idCategoria      VARCHAR(10)   NOT NULL,
    activo           BOOLEAN       DEFAULT TRUE,
    fechaCreacion    TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idCategoria) REFERENCES CATEGORIAS(id)
);

CREATE INDEX idx_productos_categoria ON PRODUCTOS(idCategoria);
CREATE INDEX idx_productos_stock     ON PRODUCTOS(stock);
CREATE INDEX idx_productos_vencim    ON PRODUCTOS(fechaVencimiento);
```

### 3.4 CLIENTES

```sql
CREATE TABLE CLIENTES (
    id                VARCHAR(10)   PRIMARY KEY,   -- CLI001...
    dni               CHAR(8)       NOT NULL UNIQUE,
    nombres           VARCHAR(50)   NOT NULL,
    apellidos         VARCHAR(50)   NOT NULL,
    telefono          VARCHAR(12)   NULL,
    email             VARCHAR(100)  NULL,
    direccion         VARCHAR(200)  NULL,
    puntosAcumulados  INT           DEFAULT 0 CHECK (puntosAcumulados >= 0),
    activo            BOOLEAN       DEFAULT TRUE,
    fechaRegistro     TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_clientes_dni ON CLIENTES(dni);
```

### 3.5 VENTAS

```sql
CREATE TABLE VENTAS (
    id              VARCHAR(10)   PRIMARY KEY,   -- V001, V-2026-0001...
    fecha           DATE          NOT NULL,
    hora            TIME          NOT NULL,
    idCliente       VARCHAR(10)   NULL,
    idUsuario       VARCHAR(10)   NOT NULL,
    subtotal        DECIMAL(10,2) NOT NULL,
    descuento       DECIMAL(10,2) DEFAULT 0.00,
    total           DECIMAL(10,2) NOT NULL CHECK (total >= 0),
    metodoPago      VARCHAR(20)   NOT NULL,  -- EFECTIVO|TARJETA|DIGITAL|MIXTO
    montoEfectivo   DECIMAL(10,2) DEFAULT 0.00,
    montoDigital    DECIMAL(10,2) DEFAULT 0.00,
    puntosOtorgados INT           DEFAULT 0,
    estado          VARCHAR(20)   DEFAULT 'COMPLETADA',  -- COMPLETADA|ANULADA
    FOREIGN KEY (idCliente)  REFERENCES CLIENTES(id),
    FOREIGN KEY (idUsuario)  REFERENCES USUARIOS(id)
);

CREATE TABLE DETALLE_VENTA (
    id              VARCHAR(10)   PRIMARY KEY,
    idVenta         VARCHAR(10)   NOT NULL,
    idProducto      VARCHAR(10)   NOT NULL,
    cantidad        INT           NOT NULL CHECK (cantidad > 0),
    precioUnitario  DECIMAL(10,2) NOT NULL,
    descuentoLinea  DECIMAL(10,2) DEFAULT 0.00,
    subtotal        DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (idVenta)     REFERENCES VENTAS(id),
    FOREIGN KEY (idProducto)  REFERENCES PRODUCTOS(id)
);

CREATE INDEX idx_ventas_fecha     ON VENTAS(fecha);
CREATE INDEX idx_ventas_cliente   ON VENTAS(idCliente);
CREATE INDEX idx_ventas_usuario   ON VENTAS(idUsuario);
```

### 3.6 PROVEEDORES

```sql
CREATE TABLE PROVEEDORES (
    id                 VARCHAR(10)  PRIMARY KEY,   -- PROV001...
    ruc                CHAR(11)     NOT NULL UNIQUE,
    razonSocial        VARCHAR(100) NOT NULL,
    telefono           VARCHAR(12)  NOT NULL,
    email              VARCHAR(100) NOT NULL,
    direccion          VARCHAR(200) NOT NULL,
    categoriaProducto  VARCHAR(50)  NOT NULL,
    condicionPago      VARCHAR(20)  DEFAULT 'CONTADO',
    activo             BOOLEAN      DEFAULT TRUE,
    fechaRegistro      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_proveedores_ruc ON PROVEEDORES(ruc);
```

### 3.7 COMPRAS

```sql
CREATE TABLE COMPRAS (
    id              VARCHAR(10)   PRIMARY KEY,   -- C001, C-2026-0001...
    idProveedor     VARCHAR(10)   NOT NULL,
    nroFactura      VARCHAR(20)   NOT NULL UNIQUE,
    fechaCompra     DATE          NOT NULL,
    total           DECIMAL(10,2) NOT NULL CHECK (total > 0),
    estado          VARCHAR(20)   DEFAULT 'RECIBIDO',  -- RECIBIDO|PARCIAL|PENDIENTE
    idUsuario       VARCHAR(10)   NOT NULL,
    FOREIGN KEY (idProveedor) REFERENCES PROVEEDORES(id),
    FOREIGN KEY (idUsuario)   REFERENCES USUARIOS(id)
);

CREATE TABLE DETALLE_COMPRA (
    id              VARCHAR(10)   PRIMARY KEY,
    idCompra        VARCHAR(10)   NOT NULL,
    idProducto      VARCHAR(10)   NOT NULL,
    cantidad        INT           NOT NULL CHECK (cantidad > 0),
    precioUnitario  DECIMAL(10,2) NOT NULL CHECK (precioUnitario > 0),
    subtotal        DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (idCompra)    REFERENCES COMPRAS(id),
    FOREIGN KEY (idProducto)  REFERENCES PRODUCTOS(id)
);

CREATE INDEX idx_compras_proveedor ON COMPRAS(idProveedor);
CREATE INDEX idx_compras_fecha     ON COMPRAS(fechaCompra);
```

### 3.8 KARDEX (Movimientos de Inventario)

```sql
CREATE TABLE KARDEX (
    id               VARCHAR(10)  PRIMARY KEY,   -- KDX001...
    idProducto       VARCHAR(10)  NOT NULL,
    tipo             VARCHAR(20)  NOT NULL,       -- ENTRADA|SALIDA|MERMA|DEVOLUCION
    cantidad         INT          NOT NULL CHECK (cantidad > 0),
    stockAnterior    INT          NOT NULL,
    stockResultante  INT          NOT NULL,
    idReferencia     VARCHAR(10)  NULL,           -- ID de venta, compra o devolución
    tipoReferencia   VARCHAR(20)  NULL,           -- VENTA|COMPRA|DEVOLUCION|MANUAL
    motivo           VARCHAR(100) NULL,
    idUsuario        VARCHAR(10)  NOT NULL,
    fechaHora        TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idProducto) REFERENCES PRODUCTOS(id),
    FOREIGN KEY (idUsuario)  REFERENCES USUARIOS(id)
);

CREATE INDEX idx_kardex_producto  ON KARDEX(idProducto);
CREATE INDEX idx_kardex_fecha     ON KARDEX(fechaHora);
CREATE INDEX idx_kardex_tipo      ON KARDEX(tipo);
```

### 3.9 DEVOLUCIONES

```sql
CREATE TABLE DEVOLUCIONES (
    id              VARCHAR(10)   PRIMARY KEY,   -- DEV001...
    idVenta         VARCHAR(10)   NOT NULL,
    idProducto      VARCHAR(10)   NOT NULL,
    cantidad        INT           NOT NULL CHECK (cantidad > 0),
    motivo          VARCHAR(50)   NOT NULL,
    observacion     VARCHAR(200)  NULL,
    montoReembolso  DECIMAL(10,2) NOT NULL,
    idUsuario       VARCHAR(10)   NOT NULL,
    fecha           TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idVenta)     REFERENCES VENTAS(id),
    FOREIGN KEY (idProducto)  REFERENCES PRODUCTOS(id),
    FOREIGN KEY (idUsuario)   REFERENCES USUARIOS(id)
);
```

### 3.10 EMPLEADOS Y ASISTENCIA

```sql
CREATE TABLE EMPLEADOS (
    codigo          VARCHAR(10)   PRIMARY KEY,   -- EMP001...
    dni             CHAR(8)       NOT NULL UNIQUE,
    nombres         VARCHAR(50)   NOT NULL,
    apellidos       VARCHAR(50)   NOT NULL,
    cargo           VARCHAR(30)   NOT NULL,
    fechaIngreso    DATE          NOT NULL,
    sueldoBase      DECIMAL(10,2) NOT NULL CHECK (sueldoBase >= 1025.00),
    turno           VARCHAR(20)   NOT NULL,
    telefono        VARCHAR(12)   NULL,
    email           VARCHAR(100)  NULL,
    activo          BOOLEAN       DEFAULT TRUE
);

CREATE TABLE ASISTENCIA (
    id              VARCHAR(10)  PRIMARY KEY,   -- ASI001...
    idEmpleado      VARCHAR(10)  NOT NULL,
    fecha           DATE         NOT NULL,
    horaEntrada     TIME         NULL,
    horaSalida      TIME         NULL,
    horasTrabajadas DECIMAL(5,2) NULL,          -- calculado al registrar salida
    observacion     VARCHAR(200) NULL,
    FOREIGN KEY (idEmpleado) REFERENCES EMPLEADOS(codigo),
    UNIQUE (idEmpleado, fecha)                   -- Solo 1 registro de asistencia por día por empleado
);
```

### 3.11 BITÁCORA DE AUDITORÍA

```sql
CREATE TABLE BITACORA (
    id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
    idUsuario   VARCHAR(10)  NULL,               -- NULL si el usuario no existe aún (login fallido)
    username    VARCHAR(20)  NOT NULL,
    rol         VARCHAR(20)  NULL,
    modulo      VARCHAR(30)  NOT NULL,
    accion      VARCHAR(50)  NOT NULL,
    descripcion VARCHAR(300) NULL,
    resultado   VARCHAR(10)  NOT NULL,            -- EXITO|FALLO
    ipEquipo    VARCHAR(20)  NULL,
    fechaHora   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);
-- NOTA: No crear UPDATE ni DELETE en esta tabla. Solo INSERT.
-- La bitácora es inmutable por diseño.

CREATE INDEX idx_bitacora_usuario  ON BITACORA(idUsuario);
CREATE INDEX idx_bitacora_fecha    ON BITACORA(fechaHora);
CREATE INDEX idx_bitacora_modulo   ON BITACORA(modulo);
```

### 3.12 FINANZAS

```sql
CREATE TABLE CUENTAS_COBRAR (
    id              VARCHAR(10)   PRIMARY KEY,
    idCliente       VARCHAR(10)   NOT NULL,
    idVenta         VARCHAR(10)   NULL,
    monto           DECIMAL(10,2) NOT NULL CHECK (monto > 0),
    fechaEmision    DATE          NOT NULL,
    fechaVencimiento DATE         NOT NULL,
    estado          VARCHAR(20)   DEFAULT 'PENDIENTE',  -- PENDIENTE|PAGADO|VENCIDO
    fechaPago       DATE          NULL,
    FOREIGN KEY (idCliente) REFERENCES CLIENTES(id),
    FOREIGN KEY (idVenta)   REFERENCES VENTAS(id)
);

CREATE TABLE CUENTAS_PAGAR (
    id              VARCHAR(10)   PRIMARY KEY,
    idProveedor     VARCHAR(10)   NOT NULL,
    idCompra        VARCHAR(10)   NULL,
    monto           DECIMAL(10,2) NOT NULL CHECK (monto > 0),
    fechaEmision    DATE          NOT NULL,
    fechaVencimiento DATE         NOT NULL,
    estado          VARCHAR(20)   DEFAULT 'PENDIENTE',
    fechaPago       DATE          NULL,
    FOREIGN KEY (idProveedor) REFERENCES PROVEEDORES(id),
    FOREIGN KEY (idCompra)    REFERENCES COMPRAS(id)
);

CREATE TABLE CONFIGURACION_SISTEMA (
    clave       VARCHAR(50)  PRIMARY KEY,
    valor       VARCHAR(200) NOT NULL,
    descripcion VARCHAR(300) NULL,
    modificado  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- Valores iniciales de configuración (parametrización — Sesión 10)
INSERT INTO CONFIGURACION_SISTEMA VALUES
    ('descuento.max.cajero',   '20',    'Descuento máximo (%) que puede aplicar el cajero'),
    ('puntos.por.sol',         '1',     'Puntos otorgados por cada S/ 1.00 de venta'),
    ('puntos.canje.por.sol',   '100',   'Puntos necesarios para obtener S/ 1.00 de descuento'),
    ('compra.minima.puntos',   '5.00',  'Monto mínimo de compra para acumular puntos'),
    ('stock.alerta.dias',      '7',     'Días de anticipación para alerta de vencimiento'),
    ('sesion.timeout.minutos', '30',    'Minutos de inactividad para cierre automático de sesión'),
    ('tipo.cambio.usd',        '3.72',  'Tipo de cambio USD → PEN (actualizar con API SUNAT)'),
    ('tipo.cambio.fecha',      '2026-06-01', 'Fecha de última actualización del tipo de cambio');
```

---

## 4. Diagrama de Relaciones (ERD simplificado)

```
USUARIOS ──────────────────────────────────────────────────────────┐
   │ idUsuario                                                      │
   ├──→ VENTAS ──────────────────────────────────────────┐         │
   │       │ idVenta                                      │         │
   │       ├──→ DETALLE_VENTA ──→ PRODUCTOS ──→ CATEGORIAS│         │
   │       ├──→ DEVOLUCIONES ──→ PRODUCTOS                │         │
   │       └──→ CUENTAS_COBRAR ──→ CLIENTES              │         │
   │                                                      │         │
   ├──→ COMPRAS ─────────────────────────────────┐       │         │
   │       │ idCompra                             │       │         │
   │       ├──→ DETALLE_COMPRA ──→ PRODUCTOS      │       │         │
   │       └──→ CUENTAS_PAGAR ──→ PROVEEDORES     │       │         │
   │                                              │       │         │
   ├──→ KARDEX ──→ PRODUCTOS                      │       │         │
   └──→ BITACORA                                  │       │         │
                                                  └───────┘         │
EMPLEADOS ──→ ASISTENCIA                                            │
                                                                    │
CONFIGURACION_SISTEMA (sin relaciones)                              │
```

---

## 5. Clases Java del Dominio (Modelo)

### Clases EXISTENTES (del TPS):
- ✅ `Usuario.java` — Completar con campos `intentosFallidos`, `bloqueadoHasta`
- ✅ `Producto.java` — Agregar `stockMinimo`, `fechaVencimiento`, `precioCosto`
- ✅ `Cliente.java` — Agregar `puntosAcumulados`
- ✅ `Categoria.java`
- ✅ `Venta.java` — Agregar `metodoPago`, `montoEfectivo`, `montoDigital`, `puntosOtorgados`

### Clases FALTANTES (crear en R1–R4):

```java
// Proveedor.java — R1 IT-03
public class Proveedor {
    private String id, ruc, razonSocial, telefono, email, direccion;
    private String categoriaProducto, condicionPago;
    private boolean activo;
    // Getters, setters, constructor, toString(), fromCSV()
}

// Compra.java — R1 IT-04
public class Compra {
    private String id, idProveedor, nroFactura, idUsuario;
    private LocalDate fechaCompra;
    private List<DetalleCompra> detalle;
    private double total;
    private String estado;
}

// DetalleCompra.java — R1 IT-04
public class DetalleCompra {
    private String id, idCompra, idProducto;
    private int cantidad;
    private double precioUnitario, subtotal;
}

// MovimientoInventario.java (Kardex) — R2 IT-05
public class MovimientoInventario {
    private String id, idProducto, tipo, idReferencia, tipoReferencia;
    private int cantidad, stockAnterior, stockResultante;
    private String motivo, idUsuario;
    private LocalDateTime fechaHora;
}

// Devolucion.java — R2 IT-07
public class Devolucion {
    private String id, idVenta, idProducto, motivo, observacion, idUsuario;
    private int cantidad;
    private double montoReembolso;
    private LocalDateTime fecha;
}

// Empleado.java — R4 IT-14
public class Empleado {
    private String codigo, dni, nombres, apellidos, cargo, turno;
    private LocalDate fechaIngreso;
    private double sueldoBase;
    private boolean activo;
}

// Asistencia.java — R4 IT-14
public class Asistencia {
    private String id, idEmpleado, observacion;
    private LocalDate fecha;
    private LocalTime horaEntrada, horaSalida;
    private double horasTrabajadas;
}

// Bitacora.java — R3 IT-13
public class Bitacora {
    private long id;
    private String idUsuario, username, rol, modulo, accion;
    private String descripcion, resultado, ipEquipo;
    private LocalDateTime fechaHora;
}
```

---

## 6. Patrón DAO — Separación de Persistencia

```java
// Patrón: una interfaz DAO + implementación TXT + implementación H2
// Esto permite migrar sin cambiar la lógica de negocio ni la UI

// IProductoDAO.java (interfaz)
public interface IProductoDAO {
    Producto buscarPorCodigo(String codigo);
    List<Producto> listarTodos();
    List<Producto> listarConStockCritico();
    boolean guardar(Producto p);
    boolean actualizar(Producto p);
    boolean eliminar(String id);
}

// ProductoDAOtxt.java (implementación actual)
public class ProductoDAOtxt implements IProductoDAO {
    private static final String ARCHIVO = "./data/productos.txt";
    // Lee y escribe en productos.txt
}

// ProductoDAOh2.java (implementación objetivo)
public class ProductoDAOh2 implements IProductoDAO {
    // Usa JDBC con ConexionDB.getConexion()
    // SELECT, INSERT, UPDATE según el método
}

// DAOFactory.java — switch entre implementaciones
public class DAOFactory {
    private static final boolean USE_H2 = ConfiguracionSistema.get("db.use_h2").equals("true");

    public static IProductoDAO getProductoDAO() {
        return USE_H2 ? new ProductoDAOh2() : new ProductoDAOtxt();
    }
}
```

---

## 7. Plan de Migración por Fases

| Fase | Qué se hace | Iteración | Riesgo |
|------|-------------|-----------|--------|
| **F0** | Crear `ConexionDB.java` y verificar que H2 funciona. Crear script SQL de `CREATE TABLE`. Probar con H2 Console. | IT-01 (paralelo) | Bajo |
| **F1** | Crear tablas USUARIOS, CATEGORIAS, PRODUCTOS, CLIENTES. Script de migración que lee `.txt` e inserta en H2. El TPS sigue usando `.txt` sin romperse. | IT-01 | Medio |
| **F2** | Crear tablas VENTAS y DETALLE_VENTA. Migrar ventas históricas (normalizar detalle embebido en ventas.txt). Los reportes existentes se adaptan a SQL. | IT-02 | Alto |
| **F3** | Crear PROVEEDORES, COMPRAS, DETALLE_COMPRA. Implementar IFrmProveedores y IFrmRegistroCompras con JDBC desde el inicio. | IT-03/04 | Bajo (módulo nuevo) |
| **F4** | Crear KARDEX, BITACORA, DEVOLUCIONES. Integrar bitácora automática en todos los DAO. | IT-05/13 | Medio |
| **F5** | Crear EMPLEADOS, ASISTENCIA, CUENTAS_COBRAR, CUENTAS_PAGAR, CONFIGURACION_SISTEMA. | IT-14/15 | Bajo |

---

## 8. Script de Migración desde .txt

```java
// MigracionTXT_H2.java — ejecutar UNA SOLA VEZ
public class MigracionTXT_H2 {

    public static void migrarUsuarios() throws Exception {
        List<String> lineas = Files.readAllLines(Paths.get("./data/usuarios.txt"));
        String sql = "INSERT INTO USUARIOS (id, username, passwordHash, nombre, apellidos, rol) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            for (String linea : lineas) {
                String[] p = linea.split("\\|");
                ps.setString(1, p[0]);  // id
                ps.setString(2, p[1]);  // username
                // Hashear contraseña al migrar:
                ps.setString(3, HashUtil.sha256(p[2]));
                ps.setString(4, p[3]);  // nombre
                ps.setString(5, p[4]);  // apellidos
                ps.setString(6, p[5]);  // rol
                ps.executeUpdate();
            }
        }
        System.out.println("✅ Usuarios migrados: " + lineas.size());
    }

    // Similar para migrarProductos(), migrarClientes(), migrarVentas()...
}
```

---

*Referencia: Kendall & Kendall (2011, Cap. 15) · Sesión 9 (Arquitectura 3 capas) · Pressman (2005, Cap. 8)*

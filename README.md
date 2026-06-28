# ERP Minimarket LAREDO

Sistema Integrado de Gestión (ERP) para puntos de venta y administración comercial, desarrollado en Java bajo arquitectura MVC, metodología XP y diseño estandarizado (UIKit).

## Características Principales
*   **Punto de Venta (POS):** Facturación con múltiples medios de pago, soporte para lector de código de barras (plug & play) y validación de reglas de negocio (ej. SUNAT S/ 700).
*   **Inventario (Kardex):** Trazabilidad de entradas, salidas y devoluciones.
*   **Seguridad OWASP:** Hash criptográfico de contraseñas, RBAC dinámico (control de permisos), prevención de sesiones duplicadas y bitácora de auditoría inmutable.
*   **Gestión Financiera:** Procesamiento riguroso de montos utilizando `BigDecimal` para evitar pérdida de precisión en transacciones.
*   **Módulo RRHH:** Control de asistencia y evaluación de desempeño del personal.

## Prerrequisitos
*   **Java Development Kit (JDK):** Versión 8 o superior (recomendado JDK 11+).
*   **Base de Datos:** Servidor MySQL 8+.
*   **Ant:** Para compilación del proyecto (`build.xml`).

## Instrucciones de Instalación (Setup)

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/WalterColonia03/SISTEMAS-EMPRESARIALES.git
   cd SISTEMAS-EMPRESARIALES
   ```

2. **Configurar el entorno:**
   * Copie el archivo de ejemplo a su archivo definitivo:
     ```bash
     cp config.properties.example config.properties
     ```
   * Edite `config.properties` e ingrese los datos de su base de datos local y su token de `apisperu.com`.

3. **Preparar la Base de Datos:**
   * Abra su cliente MySQL y ejecute los scripts ubicados en la clase principal (o configure el usuario `laredo_app` como sugiere el `config.properties`).
   * El sistema creará automáticamente sus tablas en el primer inicio.

4. **Compilar y Ejecutar:**
   Si está en Windows, simplemente ejecute:
   ```bash
   run.bat
   ```
   También puede abrir el proyecto en NetBeans y presionar *Run*.

## Estructura del Repositorio
*   `/src`: Código fuente (MVC, DAOs, Utilidades).
*   `/ERP_LAREDO_DOCS`: Documentación formal del proyecto, backlog XP, requerimientos y diseños de arquitectura de BD.
*   `build.xml`: Script de compilación (Apache Ant).

---
*Desarrollado como proyecto académico para el curso de Sistemas Empresariales.*

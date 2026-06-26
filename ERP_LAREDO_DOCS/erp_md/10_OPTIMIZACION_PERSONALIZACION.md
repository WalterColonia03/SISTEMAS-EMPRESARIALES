# 🔧 OPTIMIZACIÓN Y PERSONALIZACIÓN DEL ERP — LAREDO
**Sesión 10 · Ms. Ing. Elías Santa Cruz · Chopra & Meindl (2019) · Laudon & Laudon (2018)**

---

## 1. Optimización Continua (Sesión 10: Post Go-Live)

> *"La optimización de un ERP no termina con la implementación inicial,  
> sino que es un proceso continuo que busca maximizar el valor a lo largo del tiempo."*  
> — Sesión 10

### Estado actual del "Go-Live" del ERP LAREDO

| Módulo | Go-Live | Observación |
|--------|---------|-------------|
| TPS (Ventas, Productos, Clientes) | ✅ Parcial | Funciona pero con 6 defectos documentados |
| Dashboard KPI | ❌ Pendiente | Existe como menú, no como dashboard real |
| Compras / Proveedores | ❌ Pendiente | Módulo completamente nuevo |
| Inventario avanzado | ❌ Pendiente | Solo stock básico sin kardex |
| Finanzas | ❌ Pendiente | 0% implementado |
| RRHH | ❌ Pendiente | 0% implementado |
| Seguridad avanzada | ❌ Parcial | Login básico sin bloqueo ni bitácora |

---

## 2. Fase 1 — Evaluación y Auditoría (ya realizada)

La auditoría del proyecto identificó los siguientes KPIs de estado:

```
Indicadores de calidad del proyecto (Sesión 10 — KPIs de auditoría):

KPI-1: Cobertura de interfaces         → 88%  🟢 Bien
KPI-2: Lógica de negocio implementada  → 12%  🔴 Crítico
KPI-3: Persistencia real               → 30%  🔴 Crítico
KPI-4: HU con criterios de aceptación  → 50%  🟡 Regular
KPI-5: Defectos heredados TPS          →  6   🔴 Resolver primero
KPI-6: RNF verificables definidos      → 11   🟢 Bien
KPI-7: Cumplimiento UI/UX estándar     →  0%  🔴 Redeseñar todo
```

**Acción inmediata:** IT-01 resuelve los KPI-5 y comienza KPI-2.

---

## 3. Fase 2 — Capacitación y Gestión del Cambio

Antes de comenzar IT-02, el equipo debe dominar:

| Conocimiento | Por qué es crítico | Recurso de aprendizaje |
|---|---|---|
| **JDBC con H2** | Sin esto no se puede implementar ninguna lógica de BD | Tutorial en docs.h2database.com |
| **Patrón MVC en Swing** | Separar la lógica de los formularios (los TODO en `attachEvents()`) | Kendall & Kendall Cap. 18 |
| **JFreeChart** | Requerido para el Dashboard real (FR-001) | jfreechart.org ejemplos |
| **Apache POI** | Requerido para exportación .xlsx (RNF-08) | poi.apache.org |
| **SHA-256 en Java** | Para cifrado de contraseñas (RNF-03) | `MessageDigest` en Java SE |
| **UITokens.java** | Todos deben usar los mismos colores y fuentes | `03_DISEÑO_UI_UX_PROFESIONAL.md` |
| **Gherkin / BDD** | Para escribir y ejecutar criterios de aceptación | `05_HU_COMPLETAS_GHERKIN.md` |

---

## 4. Fase 3 — Personalización Estratégica (Sesión 10)

### 4.1 Diferencia entre Configuración y Personalización

| Tipo | Definición (Sesión 10) | Ejemplo en LAREDO | Costo |
|------|----------------------|------------------|-------|
| **Configuración** | Ajustar parámetros predefinidos sin cambiar código | Cambiar el % máximo de descuento del Cajero de 20% a 15% | Bajo |
| **Personalización** | Cambiar el código subyacente o agregar funcionalidades nuevas | Agregar un módulo de Delivery que no estaba planificado | Alto |

### 4.2 Qué debe ser configurable en LAREDO (no hardcodeado)

Todo parámetro que podría cambiar debe estar en la tabla `CONFIGURACION_SISTEMA`:

```java
// ConfiguracionSistema.java — acceso centralizado a parámetros
public class ConfiguracionSistema {
    private static Map<String, String> cache = new HashMap<>();

    public static String get(String clave) {
        if (cache.isEmpty()) cargarDesdeDB();
        return cache.getOrDefault(clave, "");
    }

    public static double getDouble(String clave, double defaultVal) {
        try { return Double.parseDouble(get(clave)); }
        catch (Exception e) { return defaultVal; }
    }
}

// ─── VALORES CONFIGURABLES (no hardcodear NUNCA) ────────────────
// descuento.max.cajero = 20        (% máximo que puede aplicar el Cajero)
// puntos.por.sol = 1               (puntos acumulados por S/ 1.00 de compra)
// puntos.canje.por.sol = 100       (puntos necesarios para S/ 1.00 de descuento)
// compra.minima.puntos = 5.00      (monto mínimo de compra para acumular puntos)
// stock.alerta.dias = 7            (días de anticipación para alerta de vencimiento)
// sesion.timeout.minutos = 30      (inactividad antes de cierre automático de sesión)

// EJEMPLO DE USO:
double limiteDescuento = ConfiguracionSistema.getDouble("descuento.max.cajero", 20.0);
if (descuento > limiteDescuento) {
    UIFeedback.error("Descuento máximo para su rol: " + limiteDescuento + "%");
}
```

### 4.3 IFrmConfiguracion — Pantalla de Parámetros del Sistema

```
┌─ Configuración del Sistema ────────────────────────────────────────┐
│                                               Solo Administrador   │
├──────────────────────────────────────────────────────────────────┤
│  Ventas y Descuentos                                              │
│  Descuento máximo Cajero (%):  [20      ]                        │
│  Descuento máximo Gerente (%): [100     ]                        │
│                                                                   │
│  Programa de Fidelización                                         │
│  Puntos por S/ 1.00 de compra: [1       ]                        │
│  Puntos para canjear S/ 1.00:  [100     ]                        │
│  Compra mínima para puntos:    [5.00    ]                        │
│                                                                   │
│  Inventario y Alertas                                             │
│  Días de anticipo vencimiento: [7       ]                        │
│                                                                   │
│  Seguridad de Sesión                                              │
│  Minutos de inactividad:       [30      ]                        │
│                                                                   │
├──────────────────────────────────────────────────────────────────┤
│  [Cancelar]                                      [Guardar cambios]│
└──────────────────────────────────────────────────────────────────┘
```

---

## 5. Fase 4 — Mantenimiento y Actualizaciones (Sesión 10)

### 5.1 Gestión del repositorio Git

```
Estrategia de branching para el proyecto académico:

main                ← código estable, lo que está funcionando
├── release/R1      ← Release 1 en desarrollo
│   ├── feature/IT-01-corregir-tps
│   ├── feature/IT-02-dashboard
│   ├── feature/IT-03-proveedores
│   └── feature/IT-04-compras
├── release/R2      ← después de cerrar R1
│   ├── feature/IT-05-kardex
│   └── ...
└── hotfix/bug-xxxx ← correcciones urgentes que van directo a main

Regla: nunca hacer commit directamente en main.
Merge a main solo cuando la iteración pasa todas las pruebas de aceptación.
```

### 5.2 Respaldo de datos

```java
// BackupService.java — respaldo automático diario
public class BackupService {
    public static void respaldarBD() {
        // H2 tiene comando de backup nativo:
        String sql = "BACKUP TO './backup/laredo_" +
                     LocalDate.now().toString() + ".zip'";
        // Programar con ScheduledExecutorService cada día a medianoche
    }
}
```

### 5.3 Plan de Recuperación de Desastres (DRP)

| Escenario | Tiempo de recuperación | Procedimiento |
|-----------|----------------------|---------------|
| Corrupción de `laredo.mv.db` | < 10 minutos | Restaurar desde `./backup/laredo_YYYY-MM-DD.zip` |
| Pérdida de archivos `.txt` | < 5 minutos | Restaurar desde copia en OneDrive/USB |
| Fallo del equipo principal | < 30 minutos | Instalar en otro equipo + restaurar backup |

---

## 6. Fase 5 — Optimización de Procesos de Negocio (Sesión 10)

### 6.1 Automatizaciones que debe implementar el ERP

| Proceso manual actual | Proceso automatizado en ERP |
|----------------------|---------------------------|
| El cajero actualiza el stock manualmente después de vender | Stock se reduce automáticamente al confirmar la venta (FR-009 + Kardex) |
| El gerente revisa los archivos .txt para saber qué hay en stock | Dashboard muestra stock crítico en tiempo real (FR-002) |
| El gerente busca en el historial de ventas para identificar clientes frecuentes | Módulo CRM con ranking automático de clientes VIP (FR-032) |
| Los puntos de fidelización se calculan a mano | Se acumulan automáticamente al cerrar cada venta (FR-030) |
| El gerente recuerda cuándo pedir mercadería mirando los estantes | Alertas automáticas de stock mínimo y vencimiento (FR-002, FR-007) |

### 6.2 Estandarización de procesos (Sesión 10 — Reingeniería de Procesos)

```
PROCESO ACTUAL: Registrar una venta
────────────────────────────────────────
1. Cajero busca producto manualmente en una lista
2. Cajero calcula el total a mano o con calculadora
3. Cajero escribe la venta en ventas.txt
4. Cajero recuerda reducir el stock en productos.txt (¡a veces se olvida!)
5. No hay registro de quién hizo la venta
6. No hay comprobante formal

PROCESO OPTIMIZADO CON ERP:
────────────────────────────────────────
1. Cajero busca producto por código o nombre (autocomplete en tiempo real)
2. Sistema calcula subtotal automáticamente al ingresar cantidad
3. Cajero confirma → sistema guarda venta + reduce stock + registra kardex
       + genera bitácora + actualiza puntos del cliente
       (TODO en una transacción atómica — RNF-10)
4. Sistema genera comprobante (Boleta) automáticamente
5. Dashboard actualiza los KPIs del día en tiempo real

Reducción de pasos: de 6 pasos manuales a 2 pasos para el Cajero
```

---

## 7. Ventajas y Desventajas de la Personalización en LAREDO (Sesión 10)

### Personalizaciones planificadas y su justificación:

| Personalización | Tipo | Justificación (Sesión 10) | Riesgo |
|-----------------|------|--------------------------|--------|
| Módulo de Fidelización con puntos | Código nuevo | Diferenciador competitivo del negocio; no viene en TPS estándar | Medio — requiere campo nuevo en clientes |
| Validación RUC peruano (10xx/20xx) | Configuración | Regla específica de legislación peruana (no es estándar global) | Bajo |
| Integración API RENIEC | Código nuevo | Específico de Perú; no existe en ERP genérico | Alto — dependencia externa |
| Cálculo planilla con ONP/AFP peruano | Código nuevo | Legislación laboral específica; no está en ERP genérico | Alto — requiere actualización anual |
| Dashboard con gráficos JFreeChart | Código nuevo | Característica de presentación no incluida en Swing básico | Bajo |

### Desventajas a mitigar (Sesión 10):

| Desventaja (Sesión 10) | Cómo se mitiga en LAREDO |
|------------------------|--------------------------|
| **Alto costo de personalización** | Usar librerías open source (JFreeChart, Apache POI, H2) — costo $0 |
| **Complejidad en actualizaciones** | Separar código personalizado en clases aisladas (ej: `PlanillaPeruService.java`) para no afectar módulos base |
| **Dependencia del desarrollador** | Documentar todo con JavaDoc + este conjunto de .md para que cualquier integrante pueda continuar |
| **Mayor riesgo de errores** | Pruebas unitarias JUnit para cada clase personalizada; TDD para la lógica de negocio |

---

## 8. Checklist de Optimización por Iteración

Antes de cerrar cada iteración, verificar:

### UI/UX (Sesión 10 — Experiencia de usuario):
- [ ] Todas las pantallas nuevas usan `UITokens`, `UIFonts`, `UISpacing` (sin colores hardcodeados)
- [ ] Ningún `JOptionPane.showMessageDialog()` para feedback de éxito (usar `ToastNotification`)
- [ ] Todos los formularios tienen label ARRIBA del campo y mensaje de error ABAJO
- [ ] El sidebar refleja correctamente el módulo activo
- [ ] Botón primario a la derecha, secundario a la izquierda

### Código (Sesión 10 — Calidad técnica):
- [ ] Sin `System.out.println()` en código de producción (solo en pruebas)
- [ ] Sin `// TODO` sin resolver en el código entregado
- [ ] Los DAO usan la interfaz (no la implementación directa) — preparado para migrar a H2
- [ ] Las contraseñas se guardan como hash SHA-256, nunca en texto plano
- [ ] Cada operación crítica escribe en la bitácora

### Proceso (Sesión 10 — Metodología XP):
- [ ] El tester ejecutó todos los criterios Gherkin y los firmó
- [ ] El cliente (docente) aprobó la pantalla
- [ ] Se hizo commit en la branch correcta (no en main directamente)
- [ ] El backlog está actualizado con el estado real de cada HU
- [ ] Se actualizó el burndown chart de la iteración

---

## 9. SLA Académico — Acuerdo con el Docente

| Criterio | Mínimo aceptable | Excelente |
|----------|-----------------|-----------|
| Cobertura de HU Must | 80% de los Must del Release | 100% de los Must del Release |
| Defectos en demostración | ≤ 2 defectos de severidad media | 0 defectos en flujos principales |
| Calidad de UI | Sin hardcoding de colores; formularios con validación visual | Cumple 100% del checklist `03_DISEÑO.md` |
| Documentación Gherkin | Al menos 3 criterios por HU Must | 5+ criterios por HU con casos negativos |
| Persistencia | Datos se guardan y recuperan correctamente | Operaciones atómicas + bitácora automática |

---

*Referencia: Sesión 10 — Ms. Ing. Elías Santa Cruz · Chopra & Meindl (2019) · Laudon & Laudon (2018)*

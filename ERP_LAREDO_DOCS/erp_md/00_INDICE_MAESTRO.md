# 📋 ÍNDICE MAESTRO — ERP Minimarket "LAREDO"
**Universidad Privada Antenor Orrego · Sistemas Empresariales · Ciclo IV 2026**  
**Metodología:** Extreme Programming (XP) · **Instructor:** Ms. Ing. Elías Santa Cruz  

---

## 📁 Estructura del Proyecto de Documentación

```
ERP_LAREDO_DOCS/
├── 00_INDICE_MAESTRO.md           ← Este archivo
├── 01_CONTEXTO_TEORICO_ERP.md     ← Sesiones 9, 10 y 11 aplicadas al proyecto
├── 02_ARQUITECTURA_SISTEMA.md     ← Arquitectura 3 capas + módulos ERP
├── 03_DISEÑO_UI_UX_PROFESIONAL.md ← Sistema de diseño completo (crítico)
├── 04_PLAN_RELEASE_XP.md          ← Backlog + iteraciones + velocidad
├── 05_HU_COMPLETAS_GHERKIN.md     ← 48 HU con criterios Given/When/Then
├── 06_RNF_CALIDAD.md              ← Requisitos no funcionales ISO 29148
├── 07_VALIDACIONES_FORMULARIOS.md ← Reglas campo por campo por pantalla
├── 08_MODELO_DATOS_BD.md          ← Schema H2 + migración desde .txt
└── 09_INTEGRACION_TPS_KM.md       ← Integración TPS → KM (Sesión 11)
```

---

## 🎯 Estado Actual del Proyecto (Diagnóstico)

| Capa | % Avance | Evidencia | Acción requerida |
|------|----------|-----------|-----------------|
| **Vista / IFrm** | 88% | ~20 formularios con FlatLaf y UIKit | Rediseñar UX/UI (ver `03_DISEÑO`) |
| **Lógica de negocio** | 12% | Todos los IFrm nuevos tienen `// TODO` | Implementar `attachEvents()` |
| **Persistencia** | 30% | Solo 5 archivos `.txt` del TPS | Migrar a H2 Database |
| **Modelo de clases** | 42% | Faltan 7 clases Java | Crear clases del dominio |

---

## 🗺️ Mapa de Módulos ERP (Sesión 9 — Elías Santa Cruz)

Según la estructura definida en clase, un ERP tiene estos módulos:

```
┌─────────────────────────────────────────────────────┐
│                ERP MINIMARKET LAREDO                │
├──────────┬──────────┬──────────┬──────────┬─────────┤
│Dashboard │Inventario│  Ventas  │  Compras │Finanzas │
│  KPI     │  Almacén │  CRM     │Proveedores│Contab. │
├──────────┼──────────┼──────────┼──────────┼─────────┤
│  RRHH    │Reportes  │Seguridad │   KM     │Config.  │
│ Planilla │Analytics │Bitácora  │ Decisiones│Sistema│
└──────────┴──────────┴──────────┴──────────┴─────────┘
```

---

## 🚦 Semáforo de Cobertura por Módulo

| Módulo | HU existentes | HU faltantes | Estado |
|--------|-------------|--------------|--------|
| Dashboard Gerencial | FR-001, FR-002 | KPI tipo cambio, fidelización | 🟡 Parcial |
| Inventario y Almacén | FR-003..FR-009 | Stock mín configurable, mermas | 🟢 Buena base |
| Compras y Proveedores | FR-008..FR-013 | — | 🟢 Buena base |
| Ventas / CRM | FR-014..FR-016, FR-021..FR-023 | Fidelización puntos, DNI RENIEC | 🔴 Débil |
| Finanzas y Contabilidad | FR-001 (parcial), FR-019 | Flujo caja, cuentas, libro diario | 🔴 Débil |
| Recursos Humanos | FR-024 (solo asistencia) | Ficha, vacaciones, planilla, eval. | 🔴 Débil |
| Seguridad y Admin | FR-020 (bitácora) | Bloqueo, sesión auto, permisos | 🟡 Parcial |
| Reportes / Analytics | FR-006, FR-017..FR-019 | Exportación .xlsx, KM analytics | 🟢 Buena base |
| KM (Sesión 11) | — | ETL, base conocimiento, lecciones | 🔴 Nuevo |

---

## ⚡ Defectos Heredados del TPS (Máxima Prioridad)

> Estos 6 bugs deben resolverse en IT-01 antes de cualquier nuevo módulo.

| # | HU | Bug | Clase Java |
|---|----|-----|-----------|
| 1 | 1000001 | Campo cantidad acepta valores inválidos | `NewVenta.java` |
| 2 | 1000003 | Falta ID del producto en reporte PDF | `ReporteVentas.java` |
| 3 | 1000005 | Solo filtra por mes, no por rango de fechas | `RegistroVentasMensual.java` |
| 4 | 1000012 | No muestra movimientos del cliente | `ReporteClientes.java` |
| 5 | 1000014 | Descripción incorrecta en reporte productos | `ReporteProductos.java` |
| 6 | 1000015 | IDs duplicados o con saltos en productos | `GestionarProductos.java` |

---

## 📚 Marco Teórico del Curso (Referencia cruzada)

| Sesión | Tema | Aplicación en LAREDO |
|--------|------|---------------------|
| **Sesión 9** | ERP: conceptos, características, arquitecturas | Base conceptual de los 7 módulos |
| **Sesión 10** | Optimización y personalización del ERP | Refactoring, parametrización, UX |
| **Sesión 11** | Integración TPS + KM | Dashboard analítico, base de conocimiento |

---

*Actualizado: Junio 2026 · Versión 2.0 — incorpora sesiones 9, 10 y 11*

# MANUAL DE ESTÁNDARES DE INGENIERÍA Y DIRECTRICES PARA ASISTENTES DE IA
## Proyecto: Sistema ERP para Supermercados (Enterprise Resource Planning)
**Última actualización: 26 de junio de 2026**

---

## 1. PROPÓSITO DE ESTE DOCUMENTO
Este archivo es una directriz operativa obligatoria para cualquier Sistema de Inteligencia Artificial (LLM, Agente Autónomo, Copilot o Asistente de Código) que interactúe, genere, refactorice o audite código en este repositorio. 

Cualquier IA que lea este archivo debe subordinar sus decisiones de diseño, generación de código y estructuración de software a los principios descritos aquí, adaptándolos específicamente al dominio de un **ERP de Supermercados** (Alta concurrencia, consistencia crítica de datos, lógica de negocio compleja, inventario en tiempo real y facturación).

---

## 2. FUNDAMENTOS BIBLIOGRÁFICOS Y APLICACIÓN EN EL ERP
La IA debe aplicar estrictamente los conceptos de las siguientes obras cumbre de la industria. A continuación se detalla cómo se traduce cada libro en el contexto de nuestro ERP para supermercados:

### A. Fundamentos, Mentalidad y Código Limpio

| Título del Libro | Autores | Aplicación Concreta en el ERP de Supermercados |
| :--- | :--- | :--- |
| **The Pragmatic Programmer** | Andrew Hunt, David Thomas | **Ortogonalidad y DRY (Don't Repeat Yourself):** Las reglas de cálculo de impuestos (IVA, retenciones) y descuentos masivos deben ser ortogonales. Un cambio en la lógica de promociones no debe afectar el módulo de inventario físico. |
| **Clean Code: A Handbook of Agile Software Craftsmanship** | Robert C. Martin ("Uncle Bob") | **Nombres de Dominio Claros:** Prohibido usar variables genéricas como `data` o `info`. Usar `LoteInventario`, `CodigoBarrasEAN`, `MermaStock`. Las funciones no deben superar las 20 líneas y deben hacer una sola cosa (Principio de Responsabilidad Única). |
| **The Software Craftsman** | Sandro Mancuso | **Profesionalismo Técnico:** La IA debe rechazar generar código "parche". Si se solicita una funcionalidad, debe proponer la solución que mantenga la salud a largo plazo del ERP, promoviendo la refactorización si es necesario. |

### B. Arquitectura y Modelado de Negocio Complejo

| Título del Libro | Autores | Aplicación Concreta en el ERP de Supermercados |
| :--- | :--- | :--- |
| **Domain-Driven Design: Tackling Complexity in the Heart of Software** | Eric Evans | **Lenguaje Ubicuo y Bounded Contexts:** El ERP se divide en Contextos Acotados estrictos: *Ventas en Cajas (POS)*, *Gestión de Inventario (Stock)*, *Compras/Proveedores*, y *Contabilidad*. La IA no debe mezclar entidades. Un `Producto` en el POS solo tiene ID, precio y código de barras; en Inventario tiene dimensiones, temperatura de conservación y lote. |
| **Clean Architecture** | Robert C. Martin | **Independencia de Frameworks:** La lógica del negocio (reglas de facturación, cálculo de mermas) debe residir en el núcleo (Entidades y Casos de Uso). La base de datos (PostgreSQL/MongoDB) o el framework de API (Spring Boot, NestJS, FastAPI) son detalles externos intercambiables. |
| **Designing Data-Intensive Applications** | Martin Kleppmann | **Consistencia y Concurrency:** Un supermercado opera con alta concurrencia. La IA debe diseñar pensando en aislamiento de transacciones (ACID), estrategias de bloqueo (Optimista/Pesimista) para evitar stock negativo, y el uso correcto de cachés (Redis) para lectura de precios. |

### C. Patrones de Diseño y Evolución del Código

| Título del Libro | Autores | Aplicación Concreta en el ERP de Supermercados |
| :--- | :--- | :--- |
| **Design Patterns: Elements of Reusable Object-Oriented Software** | Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides (GoF) | **Patrones Aplicados:** Uso de *Strategy* para diferentes métodos de pago (Efectivo, Tarjeta, Vales, QR) o reglas de descuento. Uso de *Factory* para la generación de diferentes formatos de comprobantes fiscales (Facturas, Boletas, Notas de Crédito). |
| **Head First Design Patterns** | Eric Freeman, Elisabeth Robson | **Comprensibilidad:** Los patrones implementados por la IA deben seguir estructuras claras, evitando la sobre-ingeniería. Preferir composición sobre herencia. |
| **Refactoring: Improving the Design of Existing Code** | Martin Fowler | **Refactorización Segura:** Cada vez que la IA modifique un módulo de compras o proveedores, debe identificar "olores de código" (Code Smells) como clases Dios o largas listas de parámetros, y aplicar refactorizaciones paso a paso. |

### D. Sistemas Distribuidos y Operaciones Modernas

| Título del Libro | Autores | Aplicación Concreta en el ERP de Supermercados |
| :--- | :--- | :--- |
| **Building Microservices** | Sam Newman | **Desacoplamiento:** Si el ERP migra a microservicios, la comunicación entre la pasarela de pagos y el inventario debe ser preferiblemente asíncrona guiada por eventos (Kafka/RabbitMQ) para asegurar que si el módulo contable cae, las cajas del supermercado sigan facturando. |
| **System Design Interview (Vol. 1 y 2)** | Alex Xu | **Escalabilidad Horizontal:** Diseñar componentes sin estado (stateless) para soportar picos de tráfico como los "Black Friday" o días de pago en el supermercado. |
| **Site Reliability Engineering** | Niall Richard Murphy, et al. | **Monitoreo y Tolerancia a Fallos:** Todo endpoint crítico del ERP debe incluir métricas y logs estructurados. Implementar patrones de *Circuit Breaker* (Disyuntor) cuando se dependa de servicios externos (ej. la API del gobierno para validación fiscal). |

---

## 3. ESTADO DE LART EN PROGRAMACIÓN ASISTIDA POR IA (EDICIÓN 2026)
A la fecha actual (junio de 2026), las capacidades de los modelos de lenguaje han evolucionado sustancialmente (Contextos masivos de más de 2M de tokens, ejecución de código nativa, protocolos MCP y agentes multi-paso). Como IA que trabaja en este proyecto, debes adherirte a las siguientes **nuevas prácticas de ingeniería asistida por IA**:

### A. Gestión Óptima del Contexto (Model Context Protocol - MCP)
1. **Minimización de Basura de Contexto:** No generes explicaciones conversacionales largas ("¡Claro! Estaré encantado de ayudarte con..."). Ve directo al grano técnico.
2. **Uso de Archivos de Definición de Interfaces:** Antes de escribir una implementación completa, lee las interfaces y los contratos de datos del sistema. Respeta escrupulosamente los tipos estáticos.

### B. El Enfoque "Test-Driven Prompting" (TDP)
La IA suele sufrir de sutiles alucinaciones en lógica lógica compleja (ej. cálculo de descuentos encadenados).
1. **Primero los Tests (TDD):** Cuando se te pida crear un nuevo Caso de Uso, **debes generar primero el archivo de pruebas unitarias** con todos los casos borde del supermercado (ej. producto con 100% de descuento, stock exactamente en cero, códigos de barra corruptos).
2. **Validación Sintáctica Obligatoria:** Si tienes acceso a herramientas de ejecución o linters en el entorno, debes verificar que el código compile y pase los linters antes de dar la tarea por finalizada.

### C. Mitigación de la Deuda Técnica Inducida por IA
1. **Prohibición de Dependencias Fantasma:** No inventes librerías ni utilices paquetes externos que no estén explícitamente declarados en el archivo de dependencias del proyecto (`package.json`, `pom.xml`, `requirements.txt`, `go.mod`).
2. **Evitar la Duplicación por Aislamiento:** Como la IA tiende a escribir funciones locales para resolver problemas rápidos, debes buscar en el espacio de nombres del proyecto si ya existen utilidades para el formateo de monedas, manejo de zonas horarias (crucial para fechas de caducidad en supermercados) o validaciones de cadenas.

### D. Seguridad y Privacidad de Datos en el ERP (Compliance 2026)
1. **Cero Datos Sensibles (PII) en Prompts:** Queda estrictamente prohibido usar datos reales de clientes del supermercado o tarjetas de crédito en entornos de desarrollo o prompts. Utiliza generadores de datos ficticios (Mocks).
2. **Auditoría de Inyecciones y OWASP:** Todo código SQL, NoSQL o de consulta generado por la IA debe estar parametrizado para evitar inyecciones. La IA debe verificar activamente las vulnerabilidades de seguridad del código que auto-genera.

---

## 4. INSTRUCCIONES DIRECTAS PARA LA IA EN EL FLUJO DE TRABAJO
Si eres un agente de IA leyendo esto para empezar a programar, ejecuta estas directivas en orden:

1. **Fase de Análisis de Dominio:** Entiende que estás trabajando en un ambiente donde un error de redondeo puede causar descuadres contables masivos o multas gubernamentales. Usa tipos de datos precisos para dinero (evita `float` o `double`; usa `BigDecimal`, `Decimal` o enteros en centavos según el lenguaje).
2. **Fase de Generación:** Sigue la estructura de carpetas establecida (ej. Arquitectura Hexagonal: `domain/`, `application/`, `infrastructure/`). No mezcles lógica de persistencia de base de datos dentro de los modelos de dominio.
3. **Fase de Autocrítica (Ciclo de Reflexión):** Antes de entregar tu respuesta con el código, ejecuta internamente una verificación: *¿Este código viola algún principio de Clean Code? ¿Cumple con los patrones de diseño del Gang of Four adecuados para este caso? ¿Previene condiciones de carrera en el stock?*

---
*Este documento es ley en el repositorio. Al procesar solicitudes subsecuentes, actúa bajo el rol de un Ingeniero de Software Principal altamente experimentado, pragmático y riguroso.*

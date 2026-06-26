# Registro de Cambios y Planificación - ERP LAREDO
**Fecha y Hora:** 2026-06-25T23:48:00-05:00

## 🎯 Objetivo de la Sesión
Continuar con el **Release 3 (Fase 2: CRM y Fidelización)**. Se deben implementar los siguientes requerimientos:
1. **Acumular puntos (FR-030):** En cada venta, el cliente recibe 1 punto por cada S/ 1.00 gastado.
2. **Canjear puntos (FR-031):** El cliente puede canjear 100 puntos por S/ 1.00 de descuento en el Punto de Venta (POS).
3. **Ranking y CRM (FR-016 y FR-032):** Interfaz para ver el historial de compras de los clientes y un ranking de clientes VIP según sus compras y puntos acumulados.

## 📝 Plan de Acción Técnico
- Modificar el modelo `Cliente.java` para incluir el atributo `puntos`.
- Actualizar `ClienteDAO.java` para poder consultar y actualizar los puntos.
- Modificar `IFrmPuntoVenta.java` para mostrar los puntos disponibles del cliente en pantalla, incluir un botón para "Aplicar Descuento por Puntos", y sumar puntos tras concretar la venta en `VentaDAO`.
- Desarrollar la vista `IFrmFidelizacion.java` que mostrará dos pestañas (Historial de Compras por Cliente y Ranking VIP).

## 🐛 Registro de Bugs Encontrados y Soluciones (Logs)
*(Pendiente)*

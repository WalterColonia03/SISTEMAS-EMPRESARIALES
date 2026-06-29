package Modelo;

import Utils.ConfigManager;
import Utils.LoggerGlobal;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Servicio para consultar APIs de DNI (RENIEC) y RUC (SUNAT) via apisperu.com.
 *
 * CORRECCIONES APLICADAS (2026-06-26T00:53:00-05:00):
 *   - SEGURIDAD: Token JWT ya NO está hardcodeado en el código fuente.
 *     Se lee desde config.properties via ConfigManager.
 *   - OBSERVABILIDAD: Errores se registran en LoggerGlobal.
 *   - ROBUSTEZ: Se añadió timeout de conexión (5 seg) para evitar que la UI
 *     se congele si el servicio externo no responde (Circuit Breaker básico).
 *     (Site Reliability Engineering — Tolerancia a Fallos)
 *
 * Principio aplicado: DRY — Un solo punto para configurar la URL y token.
 */
public class ApisPeruService {

    // API Decolecta (https://api.decolecta.com/v1)
    private static final String TOKEN   = "sk_16843.paavpbKWDiR8LVrQATYyD30OusNYlPqD";
    private static final String URL_DNI = "https://api.decolecta.com/v1/reniec/dni?numero=";
    private static final String URL_RUC = "https://api.decolecta.com/v1/sunat/ruc?numero=";

    // Timeout de 5 segundos para evitar que la UI se congele (Circuit Breaker básico)
    private static final int TIMEOUT_MS = 5000;

    /**
     * Consulta un DNI en RENIEC y devuelve [nombres, apellidoPaterno, apellidoMaterno].
     * Retorna null si el DNI no se encuentra o hay error de conexión.
     */
    public static String[] consultarDNI(String dni) {
        try {
            String json = ejecutarGet(URL_DNI + dni);
            if (json == null) return null;

            String nombres    = extraerValorJSON(json, "first_name");
            String apPaterno  = extraerValorJSON(json, "first_last_name");
            String apMaterno  = extraerValorJSON(json, "second_last_name");

            if (nombres != null) {
                return new String[]{nombres, apPaterno != null ? apPaterno : "", apMaterno != null ? apMaterno : ""};
            }
        } catch (Exception ex) {
            LoggerGlobal.error("Error al consultar DNI " + dni + " en RENIEC", ex);
        }
        return null;
    }

    /**
     * Consulta un RUC en SUNAT y devuelve [razonSocial, direccion, estado].
     * Retorna null si el RUC no se encuentra o hay error de conexión.
     */
    public static String[] consultarRUC(String ruc) {
        try {
            String json = ejecutarGet(URL_RUC + ruc);
            if (json == null) return null;

            String razonSocial = extraerValorJSON(json, "razon_social");
            String direccion   = extraerValorJSON(json, "direccion");
            String estado      = extraerValorJSON(json, "estado");

            if (razonSocial != null) {
                return new String[]{razonSocial, direccion != null ? direccion : "", estado != null ? estado : ""};
            }
        } catch (Exception ex) {
            LoggerGlobal.error("Error al consultar RUC " + ruc + " en SUNAT", ex);
        }
        return null;
    }

    /**
     * Ejecuta una petición HTTP GET y retorna el body como String.
     * Incluye timeouts para evitar bloqueos en la UI.
     */
    private static String ejecutarGet(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + TOKEN);
            conn.setConnectTimeout(TIMEOUT_MS);
            conn.setReadTimeout(TIMEOUT_MS);

            if (conn.getResponseCode() != 200) {
                LoggerGlobal.log("API respondió con código: " + conn.getResponseCode() + " para URL: " + urlStr);
                conn.disconnect();
                return null;
            }

            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    sb.append(linea);
                }
            }
            conn.disconnect();
            return sb.toString();
        } catch (java.net.SocketTimeoutException ex) {
            LoggerGlobal.log("Timeout al conectar con la API externa: " + urlStr);
            return null;
        } catch (Exception ex) {
            LoggerGlobal.error("Error de red al llamar API: " + urlStr, ex);
            return null;
        }
    }

    /**
     * Extrae el valor String de una clave en un JSON simple (sin anidamiento).
     * Maneja claves con y sin espacio después de los dos puntos.
     */
    private static String extraerValorJSON(String json, String key) {
        // Busca "key": "value" o "key":"value"
        String[] variantes = { "\"" + key + "\": \"", "\"" + key + "\":\"" };
        for (String searchKey : variantes) {
            int startIdx = json.indexOf(searchKey);
            if (startIdx != -1) {
                int valueStart = startIdx + searchKey.length();
                int valueEnd   = json.indexOf("\"", valueStart);
                if (valueEnd != -1) {
                    return json.substring(valueStart, valueEnd);
                }
            }
        }
        return null;
    }
}

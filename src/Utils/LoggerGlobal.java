package Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utilitario centralizado para registrar errores y eventos del sistema.
 * Niveles soportados: INFO, ERROR.
 *
 * Creado: 2026-06-25T23:43:00-05:00
 * Modificado: 2026-06-26T02:06:00-05:00 — agregado info() como alias explícito
 */
public class LoggerGlobal {

    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = LOG_DIR + "/sistema_error.log";

    static {
        File dir = new File(LOG_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /** Registra un evento informativo (nivel INFO). */
    public static void log(String mensaje) {
        escribir("INFO", mensaje, null);
    }

    /** Alias explícito de log() con nombre más descriptivo (nivel INFO). */
    public static void info(String mensaje) {
        escribir("INFO", mensaje, null);
    }

    /** Registra un error con stack trace (nivel ERROR). */
    public static void error(String mensaje, Exception ex) {
        escribir("ERROR", mensaje, ex);
    }

    private static void escribir(String nivel, String mensaje, Exception ex) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {

            String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            pw.println("[" + fechaHora + "] [" + nivel + "] " + mensaje);
            if (ex != null) {
                ex.printStackTrace(pw);
            }
            pw.println("--------------------------------------------------");
        } catch (Exception e) {
            System.err.println("Error fatal al escribir en el logger global: " + e.getMessage());
        }
    }
}

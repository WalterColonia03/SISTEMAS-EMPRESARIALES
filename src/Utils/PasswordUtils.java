package Utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilidad para el hashing seguro de contraseñas con SHA-256.
 *
 * SEGURIDAD (OWASP A02:2021 — Cryptographic Failures):
 * Las contraseñas NUNCA deben almacenarse ni compararse en texto plano.
 * Esta clase resuelve esa vulnerabilidad identificada en la auditoría
 * del 2026-06-26.
 *
 * Creado: 2026-06-26T00:53:00-05:00
 */
public class PasswordUtils {

    private PasswordUtils() {
        // Clase utilitaria — no instanciable
    }

    /**
     * Genera el hash SHA-256 de una contraseña en texto plano.
     * @param plainPassword Contraseña en texto plano.
     * @return Hash en formato hexadecimal (64 caracteres).
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 siempre está disponible en Java SE; esto nunca debe ocurrir
            LoggerGlobal.error("Error fatal: SHA-256 no disponible en la JVM", e);
            throw new RuntimeException("Algoritmo de hash no disponible", e);
        }
    }

    /**
     * Compara una contraseña en texto plano con su hash almacenado.
     * @param plainPassword Contraseña ingresada por el usuario.
     * @param storedHash    Hash almacenado en base de datos.
     * @return true si coinciden, false en caso contrario.
     */
    public static boolean verifyPassword(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null) return false;
        String inputHash = hashPassword(plainPassword);
        return inputHash.equals(storedHash);
    }
}

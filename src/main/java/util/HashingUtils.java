package util;

import org.mindrot.jbcrypt.BCrypt;

public class HashingUtils {

    /**
     * MENGUBAH Password Biasa -> BCrypt Hash
     * Outputnya akan berawalan "$2a$10$..."
     */
    public static String hashPassword(String plainPassword) {
        // 12 adalah tingkat kekuatan salt (work factor). Semakin tinggi semakin aman tapi lambat.
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    /**
     * MENGECEK Password (Login)
     * Otomatis mendeteksi apakah password di database BCrypt atau Plain Text
     */
    public static boolean checkPassword(String plainPassword, String storedHash) {
        if (storedHash == null || plainPassword == null) {
            return false;
        }

        try {
            // 1. Cek apakah formatnya BCrypt (Diawali $2a$)
            if (storedHash.startsWith("$2a$")) {
                return BCrypt.checkpw(plainPassword, storedHash);
            }

            // 2. FALLBACK: Jika password di database MASIH Plain Text (belum di-hash)
            // Ini agar kamu tidak terkunci jika mereset password manual di database
            else {
                return plainPassword.equals(storedHash);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
package ica12;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * author julianvazquez
 */
public class ICA12 {

    private static final String ALGORITHM = "AES";
    private static final String CIPHER = "AES/CBC/PKCS5PADDING";

    public static String encrypt(byte[] key, byte[] initVector, String value) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(initVector);
        SecretKeySpec skeySpec = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(value.getBytes("UTF-8"));
        String encoded = Base64.getEncoder().encodeToString(encrypted);
        return encoded;
    }

    public static String decrypt(byte[] key, byte[] initVector, String encrypted) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(initVector);
        SecretKeySpec skeySpec = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
        return new String(original);
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }

    public static void main(String[] args) {
        try {
            // Note: Generates a new Key and initVector each time the program runs.  In a real
            //   implementation you would need to store the key and initVector as secrets
            //   to later decrypt.
            //
            SecureRandom sr = new SecureRandom();
            byte[] key = new byte[16];
            sr.nextBytes(key); // 128 bit key
            byte[] initVector = new byte[16];
            sr.nextBytes(initVector); // 16 bytes IV
            System.out.println("Random key=" + bytesToHex(key));
            System.out.println("initVector=" + bytesToHex(initVector));

            String payload = "This is the plaintext from Erik and Milton's article.";
            System.out.println("Original text=" + payload);

            String encrypted = encrypt(key, initVector, payload);
            System.out.println("Encrypted text=" + encrypted);

            String decrypted = decrypt(key, initVector, encrypted);
            System.out.println("Decrypted text=" + decrypted);

            String result = decrypted.equals(payload) ? "It works!" : "Somethings not right.";
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

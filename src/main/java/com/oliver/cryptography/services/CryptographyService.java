package com.oliver.cryptography.services;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class CryptographyService {

    private static String ALGORITHM = "AES";
    private static String CHARSET = "UTF-8";
    private static String SECRET_KEY = "Secret_KEY1234";

    public static String generate(String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(str.getBytes());
        byte[] digest = md.digest();
        String result = new BigInteger(1, digest).toString(16).toUpperCase();
        return result;
    }

    public static String decrypt(String textEncrypted) {
        try {
            Key key = new SecretKeySpec(SECRET_KEY.getBytes(CHARSET), ALGORITHM);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] decodedBytes = Base64.getDecoder().decode(textEncrypted);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);

            return new String(decryptedBytes, CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting text: " + e.getMessage(), e);
        }
    }
}
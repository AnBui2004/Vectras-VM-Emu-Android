package com.anbui.elephant.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignatureUtil {
    public String getSha256(byte[] cert) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(cert);

            StringBuilder hex = new StringBuilder();

            for (byte b : digest) {
                hex.append(String.format("%02X", b));
            }

            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}

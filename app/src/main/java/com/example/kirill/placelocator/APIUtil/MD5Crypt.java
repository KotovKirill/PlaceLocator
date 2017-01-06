package com.example.kirill.placelocator.APIUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Crypt {
    public static final String md5(final String s) {
        try {
            // создание MD5 хеш
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // создание Hex строки
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}

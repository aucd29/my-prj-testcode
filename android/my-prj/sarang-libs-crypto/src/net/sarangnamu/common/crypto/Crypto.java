/*
 * Crypto.java
 * Copyright 2014 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.common.crypto;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import net.sarangnamu.common.DLog;

public class Crypto {
    private static final String TAG = "Crypto";

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // SHA
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public static String toSHA256(final String data) {
        return getDigest("SHA-256", data);
    }

    public static String toSHA512(final String data) {
        return getDigest("SHA-512", data);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // MD5
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public static String toMD5(final String data) {
        return getDigest("MD5", data);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // CRYPTO
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public static String encryptBlowfish(final String data, final String key) {
        return encrypt("Blowfish", data, key);
    }

    public static String decryptBlowfish(final String data, final String key) {
        return decrypt("Blowfish", data, key);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // PRIVATE
    //
    ////////////////////////////////////////////////////////////////////////////////////

    private static String getDigest(final String type, final String data) {
        try {
            return MessageDigest.getInstance(type).digest(data.getBytes("UTF-8")).toString();
        } catch (NullPointerException e) {
            DLog.e(TAG, "getDigest", e);
        } catch (Exception e) {
            DLog.e(TAG, "getDigest", e);
        }

        return null;
    }

    private static String encrypt(final String type, final String data, final String key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), type);
            Cipher cipher = Cipher.getInstance(type);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            return bytesToHex(cipher.doFinal(data.getBytes()));
        } catch (NullPointerException e) {
            DLog.e(TAG, "encrypt", e);
        } catch (Exception e) {
            DLog.e(TAG, "encrypt", e);
        }

        return null;
    }

    private static String decrypt(final String type, final String data, final String key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), type);
            Cipher cipher = Cipher.getInstance(type);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decrypted = cipher.doFinal(hexToBytes(data));

            return new String(decrypted);
        } catch (NullPointerException e) {
            DLog.e(TAG, "decrypt", e);
        } catch (Exception e) {
            DLog.e(TAG, "decrypt", e);
        }

        return null;
    }

    public static byte[] hexToBytes(String values) {
        if (values == null || values.length() == 0) {
            return null;
        }

        byte[] value = new byte[values.length() / 2];
        for (int i = 0; i < value.length; ++i) {
            value[i] = (byte) Integer.parseInt(values.substring(2 * i, 2 * i + 2), 16);
        }

        return value;
    }

    public static String bytesToHex(byte[] values) {
        if (values == null || values.length == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer(values.length * 2);
        String hexNumber;
        for (byte value : values) {
            hexNumber = "0" + Integer.toHexString(0xff & value);
            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }

        return sb.toString();
    }
}

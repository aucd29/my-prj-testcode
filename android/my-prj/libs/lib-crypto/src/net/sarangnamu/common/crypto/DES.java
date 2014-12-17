/*
 * DES.java
 * Copyright 2014 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.common.crypto;

import net.sarangnamu.common.DLog;

/**
 * <pre>
 * {@code
 * String key = "key!!!@@##";
 *
 * String cipherText = DES.encrypt("hello world", key);
 * String plainText  = DES.decrypt(cipherText, key);
 *
 * String cipherText = DES.encrypt(DES.ECB.PKCS5, "hello world", key);
 * String plainText  = DES.decrypt(DES.ECB.PKCS5, cipherText, key);
 * }
 * </pre>
 *
 * @see http://www.herongyang.com/Cryptography/DES-Mode-ECB-Electronic-CodeBook.html
 *
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class DES extends CipherBase {
    private static final String TAG = "DES";
    private static final String TYPE = "DES";

    public static String encrypt(final String data, final String key) {
        if (!checkKeyLength(key)) {
            return null;
        }

        return doEncrypt(TYPE, data, key);
    }

    public static String decrypt(final String data, final String key) {
        if (!checkKeyLength(key)) {
            return null;
        }

        return doDecrypt(TYPE, data, key);
    }

    public static String encrypt(final String option, final String data, final String key) {
        if (!checkKeyLength(key)) {
            return null;
        }

        return doEncrypt(option, null, TYPE, data, key);
    }

    public static String decrypt(final String option, final String data, final String key) {
        if (!checkKeyLength(key)) {
            return null;
        }

        return doDecrypt(option, null, TYPE, data, key);
    }

    public static String encrypt(final String option, final String iv, final String data, final String key) {
        if (!checkKeyLength(key)) {
            return null;
        }

        return doEncrypt(option, iv, TYPE, data, key);
    }

    public static String decrypt(final String option, final String iv, final String data, final String key) {
        if (!checkKeyLength(key)) {
            return null;
        }

        return doDecrypt(option, iv, TYPE, data, key);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // PRIVATE
    //
    ////////////////////////////////////////////////////////////////////////////////////

    private static boolean checkKeyLength(final String key) {
        if (key.length() > 8) {
            DLog.e(TAG, "ERROR::checkKeyLength::DES key too long - should be 8 bytes");
            return false;
        }

        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // INTERFACE
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public static interface ECB {
        public static final String PKCS5 = "DES/ECB/PKCS5Padding";
        public static final String NOPAD = "DES/ECB/NoPadding";
    }

    public static interface CBC {
        public static final String PKCS5 = "DES/CBC/PKCS5Padding";
        public static final String NOPAD = "DES/CBC/NoPadding";
    }
}

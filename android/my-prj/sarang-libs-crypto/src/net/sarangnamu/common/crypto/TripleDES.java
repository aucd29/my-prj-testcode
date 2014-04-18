/*
 * TripleDES.java
 * Copyright 2014 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.common.crypto;

import net.sarangnamu.common.DLog;

public class TripleDES extends CipherBase {
    private static final String TAG = "TripleDES";

    public static String encrypt(final String option, final String iv, final String data, final String key) {
        if (!checkKeyLength(key)) {
            return null;
        }

        return doEncrypt(option, iv, "DESede", data, key);
    }

    public static String decrypt(final String option, final String iv, final String data, final String key) {
        if (!checkKeyLength(key)) {
            return null;
        }

        return doDecrypt(option, iv, "DESede", data, key);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // PRIVATE
    //
    ////////////////////////////////////////////////////////////////////////////////////

    private static boolean checkKeyLength(final String key) {
        if (key.length() > 8) {
            DLog.e(TAG, "ERROR::encrypt::DES key too long - should be 8 bytes");
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
        public static final String PKCS5 = "DESede/ECB/PKCS5Padding";
        public static final String NOPAD = "DESede/ECB/NoPadding";
    }

    public static interface CBC {
        public static final String PKCS5 = "DESede/CBC/PKCS5Padding";
        public static final String NOPAD = "DESede/CBC/NoPadding";
    }
}

/*
 * Blowfish.java
 * Copyright 2014 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.common.crypto;

public class Blowfish extends CipherBase {
    private static final String TYPE = "Blowfish";

    public static String encrypt(final String data, final String key) {
        return doEncrypt(TYPE, data, key);
    }

    public static String decrypt(final String data, final String key) {
        return doDecrypt(TYPE, data, key);
    }
}

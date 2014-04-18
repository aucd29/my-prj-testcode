/*
 * Blowfish.java
 * Copyright 2014 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.common.crypto;

public class Blowfish extends CipherBase {
    public static String encrypt(final String data, final String key) {
        return doEncrypt("Blowfish", data, key);
    }

    public static String decrypt(final String data, final String key) {
        return doDecrypt("Blowfish", data, key);
    }
}

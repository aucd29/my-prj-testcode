/*
 * Hex.java
 * Copyright 2014 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.common.crypto;

public class Hex {
    public static byte[] toBytes(String values) {
        if (values == null || values.length() == 0) {
            return null;
        }

        byte[] value = new byte[values.length() / 2];
        for (int i = 0; i < value.length; ++i) {
            value[i] = (byte) Integer.parseInt(values.substring(2 * i, 2 * i + 2), 16);
        }

        return value;
    }
}

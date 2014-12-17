/*
 * Bytes.java
 * Copyright 2014 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.common.crypto;

public class Bytes {
    public static String toHex(byte[] values) {
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

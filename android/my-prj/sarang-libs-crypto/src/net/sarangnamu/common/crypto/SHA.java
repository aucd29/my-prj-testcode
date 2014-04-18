/*
 * SHA.java
 * Copyright 2014 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.common.crypto;

/**
 * <pre>
 * {@code
 * String hash256 = SHA.hash256("hello world");
 *
 * String hash512 = SHA.hash512("hello world");
 * }
 * </pre>
 *
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class SHA extends DigestBase {
    public static String hash256(final String data) {
        return getDigest("SHA-256", data);
    }

    public static String hash512(final String data) {
        return getDigest("SHA-512", data);
    }
}

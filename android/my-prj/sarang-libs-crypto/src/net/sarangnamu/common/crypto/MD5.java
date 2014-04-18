/*
 * MD5.java
 * Copyright 2014 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.common.crypto;

/**
 * <pre>
 * {@code
 * String md5 = MD5.hash("hello world");
 * }
 * </pre>
 *
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class MD5 extends DigestBase {
    public static String hash(final String data) {
        return getDigest("MD5", data);
    }
}

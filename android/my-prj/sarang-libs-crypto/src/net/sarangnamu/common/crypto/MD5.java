/*
 * MD5.java
 * Copyright 2014 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.common.crypto;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
    private static final String TYPE = "MD5";

    public static String hash(final String data) {
        return getDigest(TYPE, data);
    }

    /**
     * @see http://www.mkyong.com/java/java-md5-hashing-example/
     */
    public static String sum(final String path) throws IOException, NoSuchAlgorithmException {
        InputStream is = new FileInputStream(path);
        MessageDigest digest = MessageDigest.getInstance(TYPE);
        byte[] buffer = new byte[8192];
        int read = 0;

        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }

            byte[] md5sum = digest.digest();
            StringBuffer sb = new StringBuffer();

            for (int i=0; i<md5sum.length; ++i) {
                sb.append(Integer.toString((md5sum[i] & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        } finally {
            is.close();
        }
    }
}

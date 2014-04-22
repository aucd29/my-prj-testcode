/*
 * LibsTest.java
 * Copyright 2014 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.common.crypto.tests;

import net.sarangnamu.common.DLog;
import net.sarangnamu.common.crypto.Blowfish;
import net.sarangnamu.common.crypto.DES;
import net.sarangnamu.common.crypto.MD5;
import net.sarangnamu.common.crypto.SHA;
import net.sarangnamu.common.crypto.TripleDES;
import android.test.AndroidTestCase;

public class LibsTest extends AndroidTestCase {
    private static final String TAG = "LibsTest";
    private static final String plainText = "hello world";
    private static final String iv = "12345678"; // initial vector

    public void testMD5() {
        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "MD5 " + MD5.hash(plainText));
        DLog.d(TAG, "===================================================================");
    }

    public void testSHA() {
        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "SHA256 " + SHA.hash256(plainText));
        DLog.d(TAG, "-------------------------------------------------------------------");
        DLog.d(TAG, "SHA512 " + SHA.hash512(plainText));
        DLog.d(TAG, "===================================================================");
    }

    public void testBlowfish() {
        final String key = "test key !@#$%";

        String encrypt = Blowfish.encrypt(plainText, key);
        String decrypt = Blowfish.decrypt(encrypt, key);

        assertNotNull(encrypt);
        assertNotNull(decrypt);

        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "BLOWFISH ENCRYPT " + encrypt);
        DLog.d(TAG, "BLOWFISH DECRYPT " + decrypt);
        DLog.d(TAG, "===================================================================");
    }

    public void testDES() {
        String key = "test$%!%";

        String cipherText = DES.encrypt(plainText, key);
        String res        = DES.decrypt(cipherText, key);

        assertNotNull(cipherText);
        assertNotNull(res);

        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "DES ENCRYPT " + cipherText);
        DLog.d(TAG, "DES DECRYPT " + res);
        DLog.d(TAG, "===================================================================");

        cipherText = DES.encrypt(DES.ECB.PKCS5, plainText, key);
        res        = DES.decrypt(DES.ECB.PKCS5, cipherText, key);

        assertNotNull(cipherText);
        assertNotNull(res);

        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, DES.ECB.PKCS5 + " ENCRYPT " + cipherText);
        DLog.d(TAG, DES.ECB.PKCS5 + " DECRYPT " + res);
        DLog.d(TAG, "===================================================================");

        cipherText = DES.encrypt(DES.CBC.PKCS5, iv, plainText, key);
        res        = DES.decrypt(DES.CBC.PKCS5, iv, cipherText, key);

        assertNotNull(cipherText);
        assertNotNull(res);

        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, DES.CBC.PKCS5 + " ENCRYPT " + cipherText);
        DLog.d(TAG, DES.CBC.PKCS5 + " DECRYPT " + res);
        DLog.d(TAG, "===================================================================");
    }

    public void testTripleDES() {
        String key = "test$%!%";

        String cipherText = TripleDES.encrypt(TripleDES.ECB.PKCS5, iv, plainText, key);
        String res        = TripleDES.decrypt(TripleDES.ECB.PKCS5, iv, cipherText, key);

        assertNull(cipherText);
        assertNull(res);

        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, TripleDES.ECB.PKCS5 + " ENCRYPT " + cipherText);
        DLog.d(TAG, TripleDES.ECB.PKCS5 + " DECRYPT " + res);
        DLog.d(TAG, "===================================================================");
    }
}

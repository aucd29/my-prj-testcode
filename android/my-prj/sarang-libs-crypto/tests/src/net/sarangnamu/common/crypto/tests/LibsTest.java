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

    public void testMD5() {
        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "MD5 " + MD5.hash("hello world"));
        DLog.d(TAG, "===================================================================");
    }

    public void testSHA() {
        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "SHA256 " + SHA.hash256("hello world"));
        DLog.d(TAG, "-------------------------------------------------------------------");
        DLog.d(TAG, "SHA512 " + SHA.hash512("hello world"));
        DLog.d(TAG, "===================================================================");
    }

    public void testBlowfish() {
        final String key = "test key !@#$%";

        String encrypt = Blowfish.encrypt("hello world", key);
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

        String cipherText = DES.encrypt("hello world", key);
        String plainText  = DES.decrypt(cipherText, key);

        assertNotNull(cipherText);
        assertNotNull(plainText);

        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "DES ENCRYPT " + cipherText);
        DLog.d(TAG, "DES DECRYPT " + plainText);
        DLog.d(TAG, "===================================================================");

        String cipherText2 = DES.encrypt(DES.ECB.PKCS5, "hello world", key);
        String plainText2  = DES.decrypt(DES.ECB.PKCS5, cipherText2, key);

        assertNotNull(cipherText2);
        assertNotNull(plainText2);

        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, DES.ECB.PKCS5 + " ENCRYPT " + cipherText2);
        DLog.d(TAG, DES.ECB.PKCS5 + " DECRYPT " + plainText2);
        DLog.d(TAG, "===================================================================");

        cipherText2 = DES.encrypt(DES.CBC.PKCS5, "hello world", key);
        plainText2  = DES.decrypt(DES.CBC.PKCS5, cipherText2, key);

        assertNotNull("des cbc enc", cipherText2);
        assertNotNull("des cbc dec", plainText2);

        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, DES.CBC.PKCS5 + " ENCRYPT " + cipherText2);
        DLog.d(TAG, DES.CBC.PKCS5 + " DECRYPT " + plainText2);
        DLog.d(TAG, "===================================================================");
    }

    public void testTripleDES() {
        String key = "test$%!%";
        String iv = "12345678";

        String cipherText = TripleDES.encrypt(TripleDES.ECB.PKCS5, iv, "hello world", key);
        String plainText  = TripleDES.decrypt(TripleDES.ECB.PKCS5, iv, cipherText, key);

        assertNull(cipherText);
        assertNull(plainText);

        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, TripleDES.ECB.PKCS5 + " ENCRYPT " + cipherText);
        DLog.d(TAG, TripleDES.ECB.PKCS5 + " DECRYPT " + plainText);
        DLog.d(TAG, "===================================================================");
    }
}

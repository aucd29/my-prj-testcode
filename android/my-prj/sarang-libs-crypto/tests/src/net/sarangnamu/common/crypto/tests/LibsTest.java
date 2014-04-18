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
import android.test.AndroidTestCase;

public class LibsTest extends AndroidTestCase {
    private static final String TAG = "LibsTest";

//    public static Test suite() {
//        return new TestSuiteBuilder(LibsTest.class)
//        .includeAllPackagesUnderHere()
//        .build();
//    }

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

        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "BLOWFISH ENCRYPT " + encrypt);
        DLog.d(TAG, "BLOWFISH DECRYPT " + decrypt);
        DLog.d(TAG, "===================================================================");
    }

    public void testDES() {
        String key = "test$%!%";

        String cipherText = DES.encrypt("hello world", key);
        String plainText  = DES.decrypt(cipherText, key);

        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "DES ENCRYPT " + cipherText);
        DLog.d(TAG, "DES DECRYPT " + plainText);
        DLog.d(TAG, "===================================================================");

        String cipherText2 = DES.encrypt(DES.ECB.PKCS5, "hello world", key);
        String plainText2  = DES.decrypt(DES.ECB.PKCS5, cipherText2, key);

        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, DES.ECB.PKCS5 + " ENCRYPT " + cipherText2);
        DLog.d(TAG, DES.ECB.PKCS5 + " DECRYPT " + plainText2);
        DLog.d(TAG, "===================================================================");
    }
}

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

    public void test1() {
        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "START TEST");
        DLog.d(TAG, "===================================================================");
    }
}

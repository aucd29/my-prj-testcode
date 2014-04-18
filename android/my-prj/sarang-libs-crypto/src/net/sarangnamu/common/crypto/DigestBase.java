/*
 * DigestBase.java
 * Copyright 2014 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.common.crypto;

import java.security.MessageDigest;

import net.sarangnamu.common.DLog;

public abstract class DigestBase {
    private static final String TAG = "DigestBase";

    protected static String getDigest(final String type, final String data) {
        try {
            return MessageDigest.getInstance(type).digest(data.getBytes("UTF-8")).toString();
        } catch (NullPointerException e) {
            DLog.e(TAG, "getDigest", e);
        } catch (Exception e) {
            DLog.e(TAG, "getDigest", e);
        }

        return null;
    }
}

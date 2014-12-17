/*
 * LibsTest.java
 * Copyright 2014 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.common.tests;

import java.io.File;

import net.sarangnamu.common.BkCfg;
import net.sarangnamu.common.BkFile;
import net.sarangnamu.common.DLog;
import net.sarangnamu.common.XPathParser;
import android.test.AndroidTestCase;

public class LibsTest extends AndroidTestCase {
    private static final String TAG = "CommonLibsTest";

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // TEST BkCfg
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public void testSdPath() {
        String sdpath = BkCfg.sdPath();
        assertNotNull(sdpath);
    }

    public void testExternalFilePath() {
        String extPath = BkCfg.externalFilePath(getContext());

        DLog.d(TAG, "extPath " + extPath);
        assertNotNull(extPath);
    }

    public void testPreference() {
        final String TEST = "TEST";
        final String VALUE = "VALUE";
        BkCfg.set(getContext(), TEST, VALUE);
        String preferecne = BkCfg.get(getContext(), TEST, null);

        assertEquals(preferecne, VALUE);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // TEST BkFile
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public void testMkdirExternal() {
        String extPath = BkCfg.externalFilePath(getContext());
        String testPath = "test-path";

        File fp = new File(extPath, testPath);
        DLog.d(TAG, "path " + fp.getAbsolutePath());
        try {
            BkFile.mkdirs(fp);
            DLog.d(TAG, "res : " + fp.exists());
            assertTrue(fp.exists());
        } catch (Exception e) {
            assertFalse(true);
            e.printStackTrace();
        }
    }

    public void testMkdirSdcard() {
        String sdPath = BkCfg.sdPath();
        String testPath = "test-path";

        File fp = new File(sdPath, testPath);
        DLog.d(TAG, "path " + fp.getAbsolutePath());
        try {
            BkFile.mkdirs(fp);
            DLog.d(TAG, "res : " + fp.exists());
            assertTrue(fp.exists());
        } catch (Exception e) {
            assertFalse(true);
            e.printStackTrace();
        }
    }

    public void testCopyAssetFile() {

    }

    public void testLogMsg() {
        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "debug");
        DLog.d(TAG, "===================================================================");
        DLog.e(TAG, "testLogMsg");
        DLog.d(TAG, "===================================================================");
    }

    class XPathTest extends XPathParser {
        @Override
        protected void parsing() throws Exception {
        }
    }

    public void testXpath() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\">" +
                "<root><name attr=\"attr\">contenxt</name></root>";
    }
}

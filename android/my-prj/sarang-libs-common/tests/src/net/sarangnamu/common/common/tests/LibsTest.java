/*
 * LibsTest.java
 * Copyright 2014 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.common.common.tests;

import java.io.File;
import java.io.FileReader;

import net.sarangnamu.common.BkCfg;
import net.sarangnamu.common.BkFile;
import net.sarangnamu.common.DLog;
import android.test.AndroidTestCase;

public class LibsTest extends AndroidTestCase {
    private static final String TAG = "CommonLibsTest";

    public void testSdPath() {
        String sdpath = BkCfg.sdPath();
        assertNotNull(sdpath);
    }

    public void testPreference() {
        final String TEST = "TEST";
        final String VALUE = "VALUE";
        BkCfg.set(getContext(), TEST, VALUE);
        String preferecne = BkCfg.get(getContext(), TEST, null);

        assertEquals(preferecne, VALUE);
    }

    public void testMakeDirAndFileCopy() {
        String sdpath = "/sdcard"; //BkCfg.sdPath();
        sdpath += "/test";

        try {
            if (!BkFile.mkdirs(sdpath)) {
                DLog.e(TAG, "===================================================================");
                DLog.e(TAG, "testMakeDirAndFileCopy NO CREATED DIRECTORY");
                DLog.e(TAG, "===================================================================");
                return;
            }

//            String filepath = sdpath + "/file";
//            FileWriter fw = new FileWriter(new File(filepath), false);
//            fw.append("[]");
//            fw.append("HELLO WORLD!");
//            fw.flush();
//            fw.close();
            /*checkFileReadValue(filepath);

            // TEST
            File ff3 = new File(filepath);
            BkFile.copyFile(ff3, filepath + "2");
            checkFileReadValue(filepath + "2");

            // TEST
            String copydir = BkCfg.sdPath() + "/TEST/DIR2";
            BkFile.copyDirectory(new File(sdpath), new File(copydir));

            fp.delete();
            assertTrue("REMOVED DIR", !fp.exists());*/
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public void checkFileReadValue(final String filepath) throws Exception {
        File ff = new File(filepath);
        FileReader fr = new FileReader(ff);
        char buff[] = new char[128];
        int r = fr.read(buff, 0, 128);
        fr.close();

        assertTrue("MAKED FILE", ff.exists());
        assertTrue("WRITED FILE", r > 0);
        assertEquals("COMPARE STRING", new String(buff), "[]HELLO WORLD!");
    }
}

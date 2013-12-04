/*
 * BkFile.java
 * Copyright 2013 Burke.Choi All rights reserved.
 *             http://www.sarangnamu.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sarangnamu.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * {@code
 * <pre>

 * </pre>}
 * 
 * @author <a href="mailto:aucd29@gmail.com.com">Burke Choi</a>
 */
public class BkFile {
    private static final String TAG = "BkFile";

    public static void mkdirs(File fp) throws Exception {
        if (!fp.exists()) {
            if (!fp.mkdirs()) {
                throw new Exception("Unable to create folder " + fp.getAbsolutePath());
            }
        }
    }

    public static void mkdirs(final String path) throws Exception {
        File fp = new File(path);
        if (!fp.exists()) {
            if (!fp.mkdirs()) {
                throw new Exception("Unable to create folder " + fp.getAbsolutePath());
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // COPY
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public static void copyFile(File fpSrc, String destPathName) throws Exception {
        copyFile(fpSrc, destPathName, null);
    }

    public static void copyFile(File fpSrc, String destPathName, FileCopyListener l) throws Exception {
        String fileName = BkString.getFileName(fpSrc.getAbsolutePath());

        File fpDestDir = new File(destPathName);
        if (!fpDestDir.exists()) {
            boolean res = fpDestDir.mkdirs();
            DLog.d(TAG, "make dir " + res);
        }

        File fpDest = new File(destPathName, fileName);
        InputStream in = new FileInputStream(fpSrc);
        OutputStream out = new FileOutputStream(fpDest);

        // Copy the bits from instream to outstream
        byte[] buf = new byte[4096];
        int len;
        while ((len = in.read(buf)) > 0) {
            if (l.isCancelled()) {
                break;
            }

            out.write(buf, 0, len);
        }

        out.flush();
        in.close();
        out.close();

        if (l != null) {
            if (l.isCancelled()) {
                l.onCancelled();

                return ;
            }

            l.copyFile(fpDest.getAbsolutePath());
            Thread.sleep(1);
        }
    }

    public static void copyDirectory(File srcPath, File destPath) throws Exception {
        copyDirectory(srcPath, destPath, null);
    }

    public static void copyDirectory(File srcPath, File destPath, FileCopyListener l) throws Exception {
        if (srcPath.isDirectory()) {
            if (!destPath.exists() && !destPath.mkdirs()) {
                throw new IOException("Cannot create dir " + destPath.getAbsolutePath());
            }

            String[] children = srcPath.list();
            for (String element : children) {
                if (l.isCancelled()) {
                    l.onCancelled();
                    break;
                }

                copyDirectory(new File(srcPath, element), new File(destPath, element), l);
            }
        } else {
            // make sure the directory we plan to store the recording in exists
            File directory = destPath.getParentFile();
            if (directory != null && !directory.exists() && !directory.mkdirs()) {
                throw new IOException("Cannot create dir " + directory.getAbsolutePath());
            }

            InputStream in = new FileInputStream(srcPath);
            OutputStream out = new FileOutputStream(destPath);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) > 0) {
                if (l.isCancelled()) {
                    break;
                }

                out.write(buf, 0, len);
                Thread.sleep(1);
            }

            out.flush();
            in.close();
            out.close();

            if (l != null) {
                if (l.isCancelled()) {
                    l.onCancelled();

                    return ;
                }

                l.copyFile(srcPath.getAbsolutePath());
            }
        }
    }

    public interface FileCopyListener {
        public void copyFile(String name);
        public boolean isCancelled();
        public void onCancelled();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // MOVE
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public static boolean moveTo(final String srcPath, final String destPath) {
        try {
            File src  = new File(srcPath);
            File dest = new File(destPath);

            mkdirs(dest);
            src.renameTo(dest);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // DELETE
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public static void deleteAll(final File fp) {
        try {
            if (!fp.exists()) {
                return ;
            }

            File[] files = fp.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteAll(file);
                } else {
                    file.delete();
                }
            }

            fp.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteFile(final String path) {
        File fp = new File(path);
        if (!fp.exists()) {
            return ;
        }

        fp.delete();
    }
}

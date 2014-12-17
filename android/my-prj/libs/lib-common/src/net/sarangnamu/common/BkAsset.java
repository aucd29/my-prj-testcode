/*
 * BkAsset.java
 * Copyright 2014 Burke.Choi All rights reserved.
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.sarangnamu.common.BkFile.FileCopyListener;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

public class BkAsset {
    private static final String TAG = "BkAsset";

    public static void copyDirectory(final Context context, final String packageName, String srcPath, String destPath) throws Exception {
        copyDirectory(context, packageName, srcPath, destPath, null);
    }

    public static void copyDirectory(final Context context, final String packageName, String srcPath, String destPath, FileCopyListener l) throws Exception {
        Resources rs = context.createPackageContext(packageName, 0).getResources();

        copyAssetAll(rs.getAssets(), srcPath, destPath, l);
    }

    private static boolean copyAssetAll(AssetManager am, String srcPath, String destPath, FileCopyListener l) {
        String assets[] = null;

        try {
            assets = am.list(srcPath);
            if (assets.length == 0) {
                // copy file
                copyAssetFile(am, srcPath, destPath, l);
            } else {
                // create directory
                if (destPath == null) {
                    destPath = "";
                }

                if (srcPath == null) {
                    srcPath = "";
                }

                File dir = new File(destPath);
                if (!dir.exists() && !dir.mkdirs()) {
                    DLog.i(TAG, "could not create dir " + destPath);
                    return false;
                }

                for (int i = 0; i < assets.length; ++i) {
                    String p, dp;
                    if (destPath.equals("")) {
                        dp = "";
                    } else {
                        dp = destPath + "/";
                    }

                    if (srcPath.length() == 0) {
                        p = "";
                    } else {
                        p = srcPath + "/";
                    }

                    boolean res = copyAssetAll(am, p + assets[i], dp + assets[i], l);
                    if (!res) {
                        throw new Exception();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static void copyAssetFile(AssetManager am, String srcPath, String destPath, FileCopyListener l) throws Exception {
        InputStream in     = null;
        OutputStream out   = null;
        String newFileName = destPath;

        in  = am.open(srcPath);
        out = new FileOutputStream(newFileName);

        byte[] buffer = new byte[4096];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
            Thread.sleep(1);
        }

        if (l != null) {
            l.onFinish(srcPath);
        }

        in.close();
        in = null;
        out.flush();
        out.close();
        out = null;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // FILE COUNT
    //
    ////////////////////////////////////////////////////////////////////////////////////

    private static int assetFileCount = 0;       ///< asset 내 파일들의 개수 값
    private static int fileCount = 0;       ///< 파일들의 개수 값

    /**
     * thread 에서 사용할 file count return 을 위한 리스너
     * @author <a href="Burke.Choi@obigo.com">krinbuch</a>
     *
     */
    public interface FileCountListener {
        public void onResult(int count);
    }

    /**
     * asset 폴더 내에 파일 개수를 구한다.
     *
     * @param packageName 원하는 asset 의 패키지 명
     * @param path asset 내의 경로
     * @return
     */
    public static int getAssetFileCount(final Context context, final String packageName, final String path) {
        assetFileCount = 0;

        try {
            Resources rs = context.createPackageContext(packageName, 0).getResources();
            AssetManager am = rs.getAssets();
            getAssetFileCountImpl(am, path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return assetFileCount;
    }

    public static void getAssetFileCountThread(final Context context, final String packageName, final String path, final FileCountListener l) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int res = getAssetFileCount(context, packageName, path);
                if (l == null) {
                    DLog.d(TAG, "IAssetFileCount is null");
                    return ;
                }

                l.onResult(res);
            }
        }).start();
    }

    private static void getAssetFileCountImpl(AssetManager am, String path) throws IOException {
        String assets[] = am.list(path);
        for (String asset : assets) {
            try {
                InputStream is = am.open(path + "/" + asset);
                is.close();
                ++assetFileCount;
            } catch (Exception e) {
                // exception 이 발생 하면 이는 directory 임을 의미 한다.
                getAssetFileCountImpl(am, path + "/" + asset);
            }
        }
    }

    public static void getFileCountThread(final String path, final FileCountListener l) {
        fileCount = 0;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getFileCount(path);
                    if (l == null) {
                        DLog.d(TAG, "IAssetFileCount is null");
                        return ;
                    }

                    l.onResult(fileCount);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static int getFileCount(String path) {
        try {
            fileCount = 0;
            getFileCountImpl(path);

            return fileCount;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    private static void getFileCountImpl(String path) throws IOException {
        String files[] = new File(path).list();
        for (String file : files) {
            if (new File(path + "/" + file).isDirectory()) {
                getFileCountImpl(path + "/" + file);
            } else {
                fileCount++;
            }
        }
    }
}

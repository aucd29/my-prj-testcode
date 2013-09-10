package com.example.fileupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

/**
 * 파일 처리 관련 클래스
 * 
 */
public class FileExt {
    private static final String TAG = "FileExt";

    /**
     * 원하는 경로에 모든 파일을 삭제 한다.
     * 
     * @param fp
     */
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

    /**
     * 원하는 경로에 모든 파일을 삭제 한다.
     * 
     * @param path
     */
    public static void deleteAll(final String path) {
        File fp = new File(path);
        deleteAll(fp);
    }

    /**
     * 원하는 파일을 읽어 byte 로 반환 한다.
     * 
     * @param is
     * @return
     */
    public static byte[] readFile(InputStream is) {
        try {
            byte [] buffer = new byte[is.available()];
            while (is.read(buffer) != -1) {
                ;
            }
            is.close();

            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 존재하는 디렉토리이면 생성하지 않고 존재하는 디렉토리 일경우 생성한다.
     * 이때 디렉토리를 생성하지 못하면 예외를 발생 시킨다.
     * 
     * @param fp
     * @throws Exception
     */
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
    // FILE COPY
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public interface IFileCopy {
        public void copyFile(String name);
        public boolean isCancelled();
        public void onCancelled();
    }

    public static void copyDirectory(File srcPath, File destPath) throws Exception {
        copyDirectory(srcPath, destPath, null);
    }

    public static void copyDirectory(File srcPath, File destPath, IFileCopy l) throws Exception {
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
            }
            out.flush();
            in.close();
            out.close();

            if (l != null) {
                if (l.isCancelled()) {
                    l.onCancelled();
                }

                l.copyFile(srcPath.getAbsolutePath());
                Thread.sleep(1);
            }
        }
    }

    /**
     * 원하는 한개의 파일을 이동 한다.
     * 
     * @param srcPath
     * @param destPath
     * @return
     */
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
    // ASSET FILE COPY
    //
    ////////////////////////////////////////////////////////////////////////////////////

    /**
     * asset 에 있는 데이터를 특정 디렉토리로 복사할 경우 사용한다.
     * 
     * @param context
     * @param packageName
     * @param srcPath
     * @param destPath
     * @throws Exception
     */
    public static void copyAssetDirectory(final Context context, final String packageName, String srcPath, String destPath) throws Exception {
        copyAssetDirectory(context, packageName, srcPath, destPath, null);
    }

    public static void copyAssetDirectory(final Context context, final String packageName, String srcPath, String destPath, IFileCopy l) throws Exception {
        Resources rs = context.createPackageContext(packageName, 0).getResources();

        copyAssetAll(rs.getAssets(), srcPath, destPath, l);
    }

    private static boolean copyAssetAll(AssetManager am, String srcPath, String destPath, IFileCopy l) {
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
                    Log.i("tag", "could not create dir " + destPath);
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

    private static void copyAssetFile(AssetManager am, String srcPath, String destPath, IFileCopy l) throws Exception {
        InputStream in     = null;
        OutputStream out   = null;
        String newFileName = destPath;

        in  = am.open(srcPath);
        out = new FileOutputStream(newFileName);

        byte[] buffer = new byte[4096];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }

        if (l != null) {
            l.copyFile(srcPath);
            Thread.sleep(1);
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
    public interface IFileCount {
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


    /**
     * asset 폴더 내에 파일 개수 thread 를 통해서 구한뒤 결과를 리스너로 전달 한다.
     * 
     * @param packageName 원하는 asset 의 패키지 명
     * @param path asset 내의 경로
     * @return
     */
    public static void getAssetFileCountThread(final Context context, final String packageName, final String path, final IFileCount l) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int res = getAssetFileCount(context, packageName, path);
                if (l == null) {
                    Log.d(TAG, "IAssetFileCount is null");
                    return ;
                }

                l.onResult(res);
            }
        }).start();
    }

    /**
     * asset 폴더 list 정보를 기준으로 open 하여 파일 count 한다.
     * 이 메소드는 재귀 구조이다.
     * 
     * @param am
     * @param path
     * @throws IOException
     */
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

    /**
     * 특정 디렉토리에 파일의 개수를 thread 를 통해 얻는다.
     * 
     * @param path
     * @param l
     */
    public static void getFileCountThread(final String path, final IFileCount l) {
        fileCount = 0;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getFileCount(path);
                    if (l == null) {
                        Log.d(TAG, "IAssetFileCount is null");
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

    /**
     * 특정 디렉토리에 파일의 개수를 얻는 다.
     * @param path
     * @throws IOException
     */
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

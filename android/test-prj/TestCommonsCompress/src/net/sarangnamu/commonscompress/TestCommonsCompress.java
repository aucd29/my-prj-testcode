package net.sarangnamu.commonscompress;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ExtraFieldUtils;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.utils.Charsets;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class TestCommonsCompress extends Activity {
    private static final String TAG = "TestCommonsCompress";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try {
            //unzip("/sdcard/a/bandi/", new FileInputStream("/sdcard/a/SensorTestSample.zip"));
            //unzip("/sdcard/a/bbang/", new FileInputStream("/sdcard/a/SensorTestSample(2).zip"));

            // Test Factory
            unzip("/sdcard/a/bbang2-1/", new BufferedInputStream(new FileInputStream("/sdcard/a/ppDownloads.zip")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // COMMONS COMPRESS
    //
    ////////////////////////////////////////////////////////////////////////////////////

    private static final int BUFFER_SIZE = 2048;
    private byte buffer[] = new byte[BUFFER_SIZE];
    private final String[] charsetsToBeTested = {"UTF-8", "EUC-KR", "ISO-8859-7"};

    public void unzip2(String location, InputStream in) throws Exception {
        ArchiveEntry ze = null;
        ArchiveInputStream input = new ArchiveStreamFactory().createArchiveInputStream(in);
        String fullPath;

        int i=0;
        while ((ze = input.getNextEntry()) != null) {
            fullPath = location + ze.getName();
            Log.d(TAG, (i++) + " "  + (ze.isDirectory() ? "dir" : "file") + " : " + fullPath + " " + ze.getName());
        }
    }

    public void countAllFiles(InputStream in) throws IOException {
        ZipArchiveEntry ze = null;
        ZipArchiveInputStream zin = new ZipArchiveInputStream(in);
        long count = 0;
        while ((ze = zin.getNextZipEntry()) != null) {
            count += ze.getSize();
        }
        zin.close();
        Log.d(TAG, "===================================================================");
        Log.d(TAG, "total " + count / 1024 / 1024 + " MB");
        Log.d(TAG, "===================================================================");
    }

    public void unzip(String location, InputStream in) throws Exception {
        makeDirs(location);
        ZipArchiveEntry ze = null;
        ZipArchiveInputStream zin = new ZipArchiveInputStream(in);
        String fullPath;

        countAllFiles(in);

        Log.d(TAG, "===================================================================");
        Log.d(TAG, "size : " + (zin.getBytesRead() / 1024 / 1024));
        Log.d(TAG, "===================================================================");

        int i=0;
        Charsets cs = new Charsets();
        while ((ze = zin.getNextZipEntry()) != null) {
            byte[] rawName = ze.getRawName();

            //Log.d(TAG, "charset " + cs.toCharset(ze.getName()));

            fullPath = location + new String(rawName, detectCharset(rawName));
            //Log.d(TAG, (i++) + " "  + (ze.isDirectory() ? "dir" : "file") + " : " + fullPath + " " + ze.getName());

            Log.d(TAG, "size :" + ze.getSize());

            byte[] centeral = ze.getCentralDirectoryExtra();
            ExtraFieldUtils utils = new ExtraFieldUtils();
            ZipExtraField[] fields = utils.parse(centeral);
            for (ZipExtraField field : fields) {
                Log.d(TAG, "getCentralDirectoryLength " + field.getCentralDirectoryLength());
            }

            if (ze.isDirectory()) {
                makeDirs(fullPath);
            } else {
                // 빵집은 반디집에 비교해 zip 알고리즘이 좀 다른 듯 하다.
                // 반디집은 디렉토리 후에 파일이 리스트가 나타나는데 반해 빵집은
                // 파일 내용이 이후에 디렉토리가 나타난다.
                // 그렇기 때문에 아래와 같이 먼저 링크가 디렉토리인지 아닌지
                // 구분을 해줘서 데이터를 출력해야 한다.
                //
                // 또 다른 큰 차이점으로 반디집은 디렉토리 구분을 마지막에 / 으로
                // 하는데 반해 빵집은 별도의 구분자가 없어서 isDirectory 가 적용
                // 되지 않는다.
                File fp = new File(fullPath);
                if (!fp.isDirectory()) {
                    try {
                        extractFile(fullPath, zin);
                    } catch (FileNotFoundException e) {
                        // 파일순서가 항상 디렉토리 다음에 파일이 들어오는게 아니라서
                        // exception 날 경우가 생긴다.

                        String path = fullPath.substring(0, fullPath.lastIndexOf("/"));
                        makeDirs(path);
                        extractFile(fullPath, zin);
                    }
                }
            }
        }

        Log.d(TAG, "size : " + (zin.getBytesRead() / 1024 / 1024));

        zin.close();
    }

    public void extractFile(final String fullPath, ZipArchiveInputStream zin) throws IOException  {
        FileOutputStream fo = new FileOutputStream(fullPath);
        for (int c = zin.read(buffer); c != -1; c = zin.read(buffer)) {
            fo.write(buffer, 0, c);
        }
        fo.flush();
        fo.close();
    }

    public void makeDirs(final String path) {
        File fp = new File(path);
        if (!fp.exists()) {
            fp.mkdirs();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // CHARSET DETECTOR
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public class CharsetDetector {
        public Charset detectCharset(byte[] data, String[] charsets) {
            Charset charset = null;
            for (String charsetName : charsets) {
                charset = detectCharset(data, Charset.forName(charsetName));
                if (charset != null) {
                    break;
                }
            }
            return charset;
        }

        private Charset detectCharset(byte[] data, Charset charset) {
            try {
                CharsetDecoder decoder = charset.newDecoder();
                decoder.reset();

                boolean identified = identify(data, decoder);
                if (identified) {
                    return charset;
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }
        private boolean identify(byte[] bytes, CharsetDecoder decoder) {
            try {
                decoder.decode(ByteBuffer.wrap(bytes));
            } catch (CharacterCodingException e) {
                return false;
            }
            return true;
        }
    }

    public Charset detectCharset(byte[] data) {
        CharsetDetector cd = new CharsetDetector();

        return cd.detectCharset(data, charsetsToBeTested);
    }
}
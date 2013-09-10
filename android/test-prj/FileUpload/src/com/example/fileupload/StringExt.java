package com.example.fileupload;

import android.content.Context;
import android.util.Log;

/**
 * 문자열 처리 관련 클래스
 * 
 */
public class StringExt {
    private static final String TAG = "StringExt";

    /**
     * 주어진 문자열에서 파일명에서 file extension 을 제외한 이름만 가져온다. 예로
     * file.txt 가 존재 한다면 file 를 가져온다.
     * 
     * @param filename
     * @return
     */
    public static String getFileName(String filename) {
        int lastIndex = filename.indexOf(".");
        if (lastIndex == -1) {
            Log.d(TAG, "getFileName : Do not found '.'");

            return "";
        }

        return filename.substring(0, lastIndex);
    }

    /**
     * 문자열에서 파일명을 반환 한다. 이때 file extension 도 포함 한다.
     * 
     * @param filename
     * @return
     */
    public static String getLastFileName(String filename) {
        int lastIndex = filename.lastIndexOf("/");
        if (lastIndex != -1) {
            return filename.substring(lastIndex + 1, filename.length());
        }

        return filename;
    }

    /**
     * URI 문자열에 . 과 마지막 / 를 기준으로 문자열을 자른뒤 이를 반환 한다.
     * 
     * @param uri
     * @return
     */
    public static String getUriToName(final String uri) {
        int dotPos   = uri.lastIndexOf(".");
        int slashPos = uri.lastIndexOf("/");

        if (dotPos == -1 || slashPos == -1) {
            return "";
        }

        return uri.substring(slashPos + 1, dotPos);
    }

    /**
     * 문자열 끝에 슬러시가 없는지 검사해서 없으면 추가한 뒤 반환 한다.
     * 
     * @param str
     * @return
     */
    public static String endsWithSlash(String str) {
        if (str == null) {
            return str;
        }

        if (!str.endsWith("/")) {
            str += "/";
        }

        return str;
    }

    /**
     * 외부 jar 파일에서 main app 의 xml string 에 id를 얻는다.
     * @param context
     * @param id
     * @return
     */
    public static int getResString(final Context context, final String id) {
        return context.getResources().getIdentifier(context.getPackageName() + ":string/" + id, null, null);
    }
}

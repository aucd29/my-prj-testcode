/*
 * BkString.java
 * Copyright 2013 Burke Choi All rights reserved.
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

/**
 * {@code
 * <pre>

 * </pre>}
 * 
 * @author <a href="mailto:aucd29@gmail.com.com">Burke Choi</a>
 */
public class BkString {
    private static final String TAG = "BkString";

    public static String getFileName(String filename) {
        int lastIndex = filename.lastIndexOf("/");
        if (lastIndex != -1) {
            return filename.substring(lastIndex + 1, filename.length());
        }

        return filename;
    }

    public static String getFilePath(String file) {
        // input data : /sdcard/name/name.ext

        if (file == null) {
            DLog.e(TAG, "getFilePath file == null");
            return "";
        }

        int pos = file.lastIndexOf("/");
        if (pos < 0) {
            DLog.e(TAG, "getFilePath not found /");
            return "";
        }

        String path = file.substring(0, pos);

        DLog.d(TAG, "TEST CODE " + path);

        return path;
    }

    public static String getRemoveFileExtension(String filename) {
        int lastIndex = filename.indexOf(".");
        if (lastIndex == -1) {
            DLog.d(TAG, "getFileName : Do not found '.'");

            return "";
        }

        return filename.substring(0, lastIndex);
    }

    public static String setLastSlash(String str) {
        if (str == null) {
            return str;
        }

        if (!str.endsWith("/")) {
            str += "/";
        }

        return str;
    }
}

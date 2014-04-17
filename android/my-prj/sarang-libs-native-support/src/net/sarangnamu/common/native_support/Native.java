/*
 * Native.java
 * Copyright 2014 Burke Choi All rights reserved.
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
package net.sarangnamu.common.native_support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * <pre>
 * {@code
 * - default command
 * Native.ls("/sdcard/apks");
 * Native.exec("/sdcard/your-native-bin");
 *
 * - with environment commadn
 * StringBuilder sb = new StringBuilder();
   sb.append("LD_LIBRARY_PATH=");
   sb.append("yourAppPath");
   sb.append(":$LD_LIBRARY_PATH");
 * Native.exec("/sdcard/your-native-bin", new String[] {sb.toString()});
 * }
 * </pre>
 *
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class Native {
    public static StringBuffer ls(String path) throws RuntimeException, IOException, InterruptedException, NullPointerException {
        return exec("ls " + path);
    }

    public static StringBuffer exec(final String command) throws RuntimeException, IOException, InterruptedException, NullPointerException {
        return exec(command, null);
    }

    public static StringBuffer exec(String command, String[] envp) throws RuntimeException, IOException, InterruptedException, NullPointerException {
        if (command == null) {
            throw new NullPointerException("command is null");
        }

        Process process;
        if (envp == null) {
            process = Runtime.getRuntime().exec(command);
        } else {
            process = Runtime.getRuntime().exec(command, envp);
        }

        StringBuffer res = new StringBuffer();
        res.append(readStream(process.getInputStream()));
        res.append(readStream(process.getErrorStream()));

        process.waitFor();

        return res;
    }

    public static StringBuffer execNoWait(String command, String[] envp) throws RuntimeException, IOException, InterruptedException, NullPointerException {
        if (command == null) {
            throw new NullPointerException("command is null");
        }

        Process process;
        if (envp == null) {
            process = Runtime.getRuntime().exec(command);
        } else {
            process = Runtime.getRuntime().exec(command, envp);
        }

        StringBuffer res = new StringBuffer();
        res.append(readStream(process.getInputStream()));
        res.append(readStream(process.getErrorStream()));

        return res;
    }

    public static StringBuffer readStream(InputStream stream) throws IOException {
        int read = 0;
        char[] buffer = new char[1024];
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuffer output = new StringBuffer();

        while ((read = reader.read(buffer)) > 0) {
            output.append(buffer, 0, read);
        }

        reader.close();

        return output;
    }
}

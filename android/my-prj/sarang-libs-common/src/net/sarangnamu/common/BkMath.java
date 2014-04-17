/*
 * BkMath.java
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
 * <pre>
 * {@code
   BkMath.toFileSizeString(size);
 * }
 * </pre>
 *
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class BkMath {
    /**
     * Surprisingly for me but loop based algorithm is about 10% faster.
     *
     * @see http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc
     */
    public static String toFileSizeString(long size) {
        int u = 0;

        for (;size > 1024 * 1024; size >>= 10) {
            u++;
        }

        if (size > 1024) {
            u++;
        }

        return String.format("%.1f %cB", size/1024f, " KMGTPE".charAt(u));
    }
}

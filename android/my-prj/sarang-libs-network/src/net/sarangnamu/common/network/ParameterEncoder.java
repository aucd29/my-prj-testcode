/*
 * ParameterEncoder.java
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
package net.sarangnamu.common.network;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * {@code
 * <pre>

 * </pre>}
 * 
 * @author <a href="mailto:aucd29@gmail.com.com">Burke Choi</a>
 */
public class ParameterEncoder {
    public String encode = "UTF-8";
    private ArrayList<String> fields = new ArrayList<String>();
    private ArrayList<String> values = new ArrayList<String>();

    public ParameterEncoder() {

    }

    public ParameterEncoder(final String encode) {
        this.encode = encode;
    }

    public void add(final String field, final String value) {
        fields.add(field);
        values.add(value);
    }

    public String getParameter() {
        StringBuilder parameters = new StringBuilder();

        try {
            int size = fields.size();
            for (int i=0; i<size; ++i) {
                parameters.append(URLEncoder.encode(fields.get(i), encode));
                parameters.append("=");
                parameters.append(URLEncoder.encode(values.get(i), encode));
                parameters.append("&");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int count = parameters.length();
        if (count > 0) {
            --count;
        }

        String res = parameters.substring(0, count);
        fields.clear();
        values.clear();

        return res;
    }
}

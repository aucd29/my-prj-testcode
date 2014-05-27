/*
 * BkResponseHandler.java
 * Copyright 2014 Burke Choi All right reserverd.
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

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

public class BkResponseHandler implements ResponseHandler {
    private String encoding; // EUC-KR
    private byte data[] = new byte[2048];

    public BkResponseHandler(final String encoding) {
        this.encoding = encoding;
    }

    public void setEncoding(final String encoding) {
        this.encoding = encoding;
    }

    @Override
    public Object handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        InputStream input = response.getEntity().getContent();

        int count;
        StringBuilder sb = new StringBuilder();

        while ((count = input.read(data)) != -1) {
            sb.append(new String(data, encoding));
        }

        return sb.toString();
    }
}

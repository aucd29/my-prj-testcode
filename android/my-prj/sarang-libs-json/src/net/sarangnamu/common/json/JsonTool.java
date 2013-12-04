/*
 * JsonTool.java
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
package net.sarangnamu.common.json;

import java.io.IOException;

import net.sarangnamu.common.DLog;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
// mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);

/**
 * {@code
 * <pre>

 * </pre>}
 * 
 * @author <a href="mailto:aucd29@gmail.com.com">Burke Choi</a>
 */
public class JsonTool {
    private static final String TAG = "JsonTool";

    public static String toJson(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            //mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            mapper.setSerializationInclusion(Include.NON_DEFAULT);

            String json = mapper.writeValueAsString(obj);
            return json;
        } catch (JsonProcessingException e) {
            DLog.e(TAG, "obj2Json", e);
        }

        return "";
    }

    public static <T> Object toObj(String json, Class<? extends T> objectType) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            T modDevices = mapper.readValue(json, objectType);
            return modDevices;
        } catch (JsonParseException e) {
            DLog.e(TAG, "Json2Obj JsonParseException", e);
        } catch (JsonMappingException e) {
            DLog.e(TAG, "Json2Obj JsonMappingException", e);
        } catch (IOException e) {
            DLog.e(TAG, "Json2Obj IOException", e);
        }

        return null;
    }

    public static <T> Object toObj(String json, TypeReference<T> typeRef) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            T modDevices = mapper.readValue(json, typeRef);
            return modDevices;
        } catch (JsonParseException e) {
            DLog.e(TAG, "Json2Obj JsonParseException", e);
        } catch (JsonMappingException e) {
            DLog.e(TAG, "Json2Obj JsonMappingException", e);
        } catch (IOException e) {
            DLog.e(TAG, "Json2Obj IOException", e);
        }

        return null;
    }
}

/*
 * Copyright 2013 OBIGO Inc. All rights reserved. http://www.obigo.com
 */
package com.example.test_gson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * 
 * @author <a href="mailto:Kurt.Na@obigo.com">krinkuna</a>
 * 
 */
public class UtilForJson {
    private static final String TAG = "UtilForJson";
    /**
     * object to JSON(String)
     * @param obj
     * @return
     */
    public static String obj2Json(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            if (!(obj instanceof DevicesJs)) {
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
                mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
            }

            String json = mapper.writeValueAsString(obj);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * JSON(String) to object
     * @param str
     * @param objectType
     * @return
     */
    public static <T> Object Json2Obj(String str, Class<? extends T> objectType) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            if (!objectType.equals(DevicesJs.class)) {
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
                mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
            }

            T readObj = mapper.readValue(str, objectType);
            return readObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

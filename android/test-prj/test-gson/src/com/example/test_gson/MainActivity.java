package com.example.test_gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public String toJSONString(ChoresDto obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
            //TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};
            String json = mapper.writeValueAsString(obj);
            Log.d(TAG, json);

            return json;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void toJSONObject(String str) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ChoresDto p1 = mapper.readValue(str, ChoresDto.class);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected byte[] jdkSerialize(Object o) throws IOException
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream(1000);
        ObjectOutputStream obOut = new ObjectOutputStream(bytes);
        obOut.writeObject(o);
        obOut.close();
        return bytes.toByteArray();
    }

    @SuppressWarnings("unchecked")
    protected <T> T jdkDeserialize(byte[] raw) throws IOException
    {
        ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(raw));
        try {
            return (T) objIn.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            objIn.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}

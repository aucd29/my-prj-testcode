package net.sarangnamu.simple_framework_test.xml;

import java.io.StringReader;
import java.io.StringWriter;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class XMLMarshaller {
    private static final String XML_HEADER = "<?xml version='1.0' encoding='UTF-8' ?>\r\n";

    public String objToXml(Object obj) {
        Serializer serializer = new Persister();
        StringWriter sw = new StringWriter();

        try {
            serializer.write(obj, sw);
            StringBuilder sb = new StringBuilder(XML_HEADER);
            sb.append(sw.toString());

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;
    }

    public <T> Object xmlToObj(String xml, Class<? extends T> objType) {
        try {
            Serializer serializer = new Persister();
            StringReader reader = new StringReader(xml);
            return serializer.read(objType, reader);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

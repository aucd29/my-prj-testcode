package net.sarangnamu.simple_framework_test.xml;

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder(XML_HEADER);
        sb.append(sw.toString());

        return sb.toString();
    }

    public <T> Object xmlToObj(String xml, Class<? extends T> objType) {
        try {
            Serializer serializer = new Persister();
            return serializer.read(objType, xml);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

package net.sarangnamu.testxpath;

import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;


public abstract class XPathParser {
    protected XPath xpath = null;
    protected Document document = null;
    protected DocumentBuilder builder = null;

    public XPathParser() {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            builder = builderFactory.newDocumentBuilder();
            xpath = XPathFactory.newInstance().newXPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadXml(final String path) {
        File fp = new File(path);
        if (!fp.exists()) {
            return ;
        }

        try {
            document = builder.parse(fp);
            parsing();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadXml(InputStream is) {
        try {
            if (is == null) {
                throw new Exception("InputStream == null");
            }


            document = builder.parse(is);

            //            String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><groupby id=\"1\" version=\"1.0\" name=\"Groupby test\"><items count=\"3\"><appby path=\"test1\" /><appby path=\"test2\" /><appby path=\"test3\" /></items></groupby>";
            //            document = builder.parse(data);
            parsing();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void parsing() throws Exception;
}

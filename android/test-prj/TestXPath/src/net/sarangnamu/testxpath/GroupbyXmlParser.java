package net.sarangnamu.testxpath;

import java.util.ArrayList;

import javax.xml.xpath.XPathConstants;

public class GroupbyXmlParser extends XPathParser {
    private static final String TAG = "GroupbyXmlParser";

    private String id = null;
    private String name = null;
    private String version = null;
    private ArrayList<String> pathes = new ArrayList<String>();

    @Override
    protected void parsing() throws Exception {
        /*
         * GROPBY XML SAMPLE CODE
         * 
            <?xml version="1.0" encoding="UTF-8"?>
            <groupby id="1" version="1.1" name="Groupby test">
            <items count="3">
                <appby path="test1" />
                <appby path="test2" />
                <appby path="test3" />
            </items>
            </groupby>
         */
        String expression = "/groupby/@version";
        version = xpath.evaluate(expression, document, XPathConstants.STRING).toString();

        expression = "/groupby/@id";
        id = xpath.evaluate(expression, document, XPathConstants.STRING).toString();

        expression = "/groupby/@name";
        name = xpath.evaluate(expression, document, XPathConstants.STRING).toString();

        int count = 0;
        expression = "count(//appby)";
        count = Integer.parseInt(xpath.evaluate(expression, document, XPathConstants.STRING).toString());

        for (int i=1; i<=count; ++i) {
            expression = "//items/appby[" + i + "]/@path";
            pathes.add(xpath.evaluate(expression, document, XPathConstants.STRING).toString());
        }
    }

    public int getCount() {
        if (pathes == null) {
            return -1;
        }

        return pathes.size();
    }

    public String getPath(int pos) {
        try {
            return pathes.get(pos);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getID() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }
}
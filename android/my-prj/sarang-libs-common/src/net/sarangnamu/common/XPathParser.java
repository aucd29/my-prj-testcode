/*
 * XPathParser.java
 * Copyright 2013 OBIGO All rights reserved.
 *             http://www.obigo.com
 */
package net.sarangnamu.common;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

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
            document  = builder.parse(is);
            parsing();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadXmlString(String xml) {
        try {
            document  = builder.parse(new InputSource(new StringReader(xml)));
            parsing();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ABSTRACT
    //
    ////////////////////////////////////////////////////////////////////////////////////

    protected abstract void parsing() throws Exception;
}

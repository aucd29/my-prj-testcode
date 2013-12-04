/*
 * XPathParser.java
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

/**
 * {@code
 * <pre>
    class TestParser extends XPathParser {
        protected void parsing() throws Exception {
            // TODO
        }
    }
 * </pre>}
 * 
 * @author <a href="mailto:aucd29@gmail.com.com">Burke Choi</a>
 */
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

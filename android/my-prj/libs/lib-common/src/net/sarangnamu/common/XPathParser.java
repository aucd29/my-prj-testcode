/*
 * XPathParser.java
 * Copyright 2013 Burke Choi All rights reserved.
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
 * <pre>
 * {@code
   class TestParser extends XPathParser {
       protected void parsing() throws Exception {
            String expr;
            int count;

            expr = "count(//xsyncData)";
            count = Integer.parseInt(xpath.evaluate(expr, document, XPathConstants.STRING).toString());

            expr = "//rgist/text()";
            emsNum = xpath.evaluate(expr, document, XPathConstants.STRING).toString();

            if (emsNum == null || emsNum.length() == 0) {
                expr = "//message/text()";
                errMsg = xpath.evaluate(expr, document, XPathConstants.STRING).toString();
                errMsg += " - [";

                expr = "//error_code/text()";
                errMsg += xpath.evaluate(expr, document, XPathConstants.STRING).toString();
                errMsg += "]";

                emsNum = tmpNum;
                emsData.add(new EmsData());
            } else {
                for (int i=2; i<=count; ++i) {
                    emsData.add(new EmsData(i));
                }
            }
       }
   }
 * }
 * </pre>
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public abstract class XPathParser {
    protected XPath mXPath = null;
    protected Document mDocument = null;
    protected DocumentBuilder mBuilder = null;

    public XPathParser() {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            mBuilder = builderFactory.newDocumentBuilder();
            mXPath = XPathFactory.newInstance().newXPath();
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
            mDocument = mBuilder.parse(fp);
            parsing();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadXml(InputStream is) {
        try {
            mDocument  = mBuilder.parse(is);
            parsing();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadXmlString(String xml) {
        try {
            mDocument  = mBuilder.parse(new InputSource(new StringReader(xml)));
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

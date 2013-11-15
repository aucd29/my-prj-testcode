/**
 * Ems.java
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
package net.sarangnamu.ems_tracking.api.xml;

import javax.xml.xpath.XPathConstants;

import net.sarangnamu.common.DLog;
import net.sarangnamu.common.XPathParser;

public class Ems extends XPathParser {
    private static final String TAG = "Ems";

    public String emsNum;
    public String date;
    public String status;
    public String office;
    public String detail;

    public Ems(String ems) {
        super();

        loadXmlString(ems);
    }

    @Override
    protected void parsing() throws Exception {
        /*
         *  <?xml version='1.0' encoding="utf-8"?>
            <xsync>
            <xsyncData>
                <rgist><![CDATA[RB839962647CN]]></rgist>
            </xsyncData>
            <xsyncData>
                <processDe><![CDATA[2013-10-24 18:11]]></processDe>
                <processSttus><![CDATA[접수]]></processSttus>
                <nowLc><![CDATA[100614]]></nowLc>
                <detailDc><![CDATA[]]></detailDc>
            </xsyncData>
            </xsync>
         */
        String expr;

        expr = "//rgist/text()";
        emsNum = xpath.evaluate(expr, document, XPathConstants.STRING).toString();

        expr = "//processDe/text()";
        date = xpath.evaluate(expr, document, XPathConstants.STRING).toString();

        expr = "//processSttus/text()";
        status = xpath.evaluate(expr, document, XPathConstants.STRING).toString();

        expr = "//nowLc/text()";
        office = xpath.evaluate(expr, document, XPathConstants.STRING).toString();

        expr = "//detailDc/text()";
        detail = xpath.evaluate(expr, document, XPathConstants.STRING).toString();
    }

    public void trace() {
        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "EMS DATA INFO");
        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "emsNum " + emsNum);
        DLog.d(TAG, "date " + date);
        DLog.d(TAG, "status " + status);
        DLog.d(TAG, "office " + office);
        DLog.d(TAG, "detail " + detail);
        DLog.d(TAG, "===================================================================");
    }
}

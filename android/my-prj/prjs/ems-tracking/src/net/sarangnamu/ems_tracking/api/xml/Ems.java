/*
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

import java.util.ArrayList;

import javax.xml.xpath.XPathConstants;

import net.sarangnamu.common.DLog;
import net.sarangnamu.common.XPathParser;

public class Ems extends XPathParser {
    private static final String TAG = "Ems";

    public String mEmsNum, mTmpNum;
    public ArrayList<EmsData> mEmsData = new ArrayList<EmsData>();
    public String mErrMsg;

    public Ems(String ems, String emsNum) {
        super();

        mTmpNum = emsNum.toUpperCase();
        loadXmlString(ems);
    }

    @Override
    protected void parsing() throws Exception {
        if (mEmsData == null) {
            mEmsData = new ArrayList<EmsData>();
        }

        /*
         *  <?xml version='1.0' encoding="utf-8"?>
            <xsync>
            <xsyncData>
                <rgist><![CDATA[RB832426012CN]]></rgist>
            </xsyncData>
            <xsyncData>
                <processDe><![CDATA[2013-10-06 16:05]]></processDe>
                    <processSttus><![CDATA[접수]]></processSttus>
                    <nowLc><![CDATA[528032]]></nowLc>
                    <detailDc><![CDATA[]]></detailDc>
                </xsyncData>
            <xsyncData>
                <processDe><![CDATA[2013-10-09 11:02]]></processDe>
                    <processSttus><![CDATA[발송준비]]></processSttus>
                    <nowLc><![CDATA[CNCANA]]></nowLc>
                    <detailDc><![CDATA[]]></detailDc>
                </xsyncData>
            <xsyncData>
                <processDe><![CDATA[2013-10-11 15:48]]></processDe>
                <processSttus><![CDATA[교환국 도착]]></processSttus>
                <nowLc><![CDATA[국제우편물류센터]]></nowLc>
                <detailDc><![CDATA[]]></detailDc>
            </xsyncData>
            <xsyncData>
                <processDe><![CDATA[2013-10-11 22:10]]></processDe>
                <processSttus><![CDATA[발송]]></processSttus>
                <nowLc><![CDATA[국제우편물류센터]]></nowLc>
                <detailDc><![CDATA[]]></detailDc>
            </xsyncData>
            <xsyncData>
                <processDe><![CDATA[2013-10-11 23:41]]></processDe>
                <processSttus><![CDATA[도착]]></processSttus>
                <nowLc><![CDATA[동서울우편집중국]]></nowLc>
                <detailDc><![CDATA[]]></detailDc>
            </xsyncData>
            <xsyncData>
                <processDe><![CDATA[2013-10-12 03:31]]></processDe>
                <processSttus><![CDATA[발송]]></processSttus>
                <nowLc><![CDATA[동서울우편집중국]]></nowLc>
                <detailDc><![CDATA[]]></detailDc>
            </xsyncData>
            <xsyncData>
                <processDe><![CDATA[2013-10-12 04:27]]></processDe>
                <processSttus><![CDATA[도착]]></processSttus>
                <nowLc><![CDATA[서울강남]]></nowLc>
                <detailDc><![CDATA[]]></detailDc>
            </xsyncData>
            <xsyncData>
                <processDe><![CDATA[2013-10-14 09:28]]></processDe>
                <processSttus><![CDATA[배달준비]]></processSttus>
                <nowLc><![CDATA[서울강남]]></nowLc>
                <detailDc><![CDATA[]]></detailDc>
            </xsyncData>
            <xsyncData>
                <processDe><![CDATA[2013-10-15 16:05]]></processDe>
                <processSttus><![CDATA[배달완료]]></processSttus>
                <nowLc><![CDATA[서울강남]]></nowLc>
                <detailDc><![CDATA[]]></detailDc>
            </xsyncData>
            </xsync>

            // error
            <?xml version='1.0' encoding="utf-8"?>
            <xsync>
            <xsyncData>
            <error_code><![CDATA[ERR-001]]></error_code>
            <message><![CDATA[조회결과가 없습니다.]]></message>
            </xsyncData>
            </xsync>
         */


        String expr;
        int count;

        expr = "count(//xsyncData)";
        count = Integer.parseInt(xpath.evaluate(expr, document, XPathConstants.STRING).toString());

        expr = "//rgist/text()";
        mEmsNum = xpath.evaluate(expr, document, XPathConstants.STRING).toString();

        if (mEmsNum == null || mEmsNum.length() == 0) {
            expr = "//message/text()";
            mErrMsg = xpath.evaluate(expr, document, XPathConstants.STRING).toString();
            mErrMsg += " - [";

            expr = "//error_code/text()";
            mErrMsg += xpath.evaluate(expr, document, XPathConstants.STRING).toString();
            mErrMsg += "]";

            mEmsNum = mTmpNum;
            mEmsData.add(new EmsData());
        } else {
            for (int i=2; i<=count; ++i) {
                mEmsData.add(new EmsData(i));
            }
        }
    }

    public int getDataCount() {
        if (mEmsData == null) {
            return 0;
        }

        return mEmsData.size();
    }

    public EmsData getEmsData(int pos) {
        if (mEmsData == null) {
            return null;
        }

        return mEmsData.get(pos);
    }

    public EmsData getLastEmsData() {
        if (mEmsData == null) {
            return null;
        }

        int pos = mEmsData.size();
        if (pos == 0) {
            return null;
        }

        return mEmsData.get(pos - 1);
    }

    public void trace() {
        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "EMS DATA INFO");
        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "emsNum " + mEmsNum);

        for (EmsData data : mEmsData) {
            data.trace();
        }

        DLog.d(TAG, "===================================================================");
    }

    public class EmsData {
        public String date;
        public String status;
        public String office;
        public String detail;

        public EmsData() {
            date   = "";
            status = "미등록";
            office = "-";
            detail = "-";
        }

        public EmsData(int pos) throws Exception {
            String expr, prefix = "//xsyncData[" + pos + "]";

            expr = prefix + "/processDe/text()";
            date = xpath.evaluate(expr, document, XPathConstants.STRING).toString();

            expr = prefix + "/processSttus/text()";
            status = xpath.evaluate(expr, document, XPathConstants.STRING).toString();

            expr = prefix + "/nowLc/text()";
            office = xpath.evaluate(expr, document, XPathConstants.STRING).toString();

            expr = prefix + "/detailDc/text()";
            detail = xpath.evaluate(expr, document, XPathConstants.STRING).toString();
        }

        public void trace() {
            DLog.d(TAG, "date " + date);
            DLog.d(TAG, "status " + status);
            DLog.d(TAG, "office " + office);
            DLog.d(TAG, "detail " + detail);
        }
    }
}

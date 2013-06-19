/*
 * Copyright (c) 2003-2011, cheol-dong choi, twitter @aucd29
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.sarangnamu.baedal.config;

import net.sarangnamu.utils.NMEALoader;

import org.json.JSONObject;

import android.content.Context;

public class ConfigBaedal {
	
	/*main 
	 * 
	 * {
	    "s_version": "5.2.6",
	    "s_alert_url": "",
	    "s_notice_url": "http://www.smartbaedal.com/user_bbs/gnuboard4/bbs/board.php?bo_table=android_notice&page=0&wr_id=17",
	    "a_search_keyword": [
	        {
	            "keyword": "멕시카나"
	        },
	        {
	            "keyword": "멕시카나"
	        },
	        {
	            "keyword": "맥도날드"
	        },
	        {
	            "keyword": "돈치킨"
	        },
	        {
	            "keyword": "스쿨푸드"
	        },
	        {
	            "keyword": "수도시락"
	        },
	        {
	            "keyword": "9292"
	        },
	        {
	            "keyword": "둘둘치킨"
	        },
	        {
	            "keyword": "네네치킨"
	        },
	        {
	            "keyword": "파닭에파무쳐"
	        }
	    ],
	    "s_event": "[발표] 충전케이블 이벤트 당첨자",
	    "i_alert_code": "0",
	    "s_market": "http://market.android.com/details?id=com.sampleapp",
	    "s_alert_text": "",
	    "a_banner_list": [
	        {
	            "s_banner_url": "www.smartbaedal.com/app_event/2011_08_mexicana.php?sb_lat=&sb_lng=",
	            "s_banner_img": "http://img.woowabros.co.kr/event/2011_08_mexicana/main_420x120.png"
	        },
	        {
	            "s_banner_url": "www.smartbaedal.com/app_event/2011_09_pdpm.html?sb_lat=&sb_lng=",
	            "s_banner_img": "http://img.woowabros.co.kr/event/2011_08_pdpm/event420.png"
	        },
	        {
	            "s_banner_url": "www.smartbaedal.com/app_event/2011_08_cable_android.html?sb_lat=&sb_lng=",
	            "s_banner_img": "http://img.woowabros.co.kr/event/2011_08_cable/event420.png"
	        },
	        {
	            "s_banner_url": "www.smartbaedal.com/app_event/2011_06_partner.html?sb_lat=&sb_lng=",
	            "s_banner_img": "http://img.woowabros.co.kr/event/2011_06_partner/event420.png"
	        },
	        {
	            "s_banner_url": "www.smartbaedal.com/app_event/2011_03_tree.html?sb_lat=&sb_lng=",
	            "s_banner_img": "http://img.woowabros.co.kr/event/2011_03_tree/tree_event420.png"
	        }
	    ],
	    "s_notice": "중앙일보에 배달의민족이 소개됐습니다 ^^",
	    "s_event_url": "http://www.smartbaedal.com/user_bbs/gnuboard4/bbs/board.php?bo_table=android_event&page=0&wr_id=52"
	}
	 */
	
	
	
	/* market list
	 * 
	 * {
	    "i_page": "1",
	    "i_pageTotal": 2,
	    "a_list": [
	        {
	            "i_key": "66027",
	            "s_title": "비에치씨치킨",
	            "s_phoneNumber": "0319053392",
	            "s_phoneNumberV": "05058376307",
	            "s_phoneNumberF": "",
	            "s_address": "경기 고양시 일산동구 장항동 776-1",
	            "f_latitude": "37.6619811",
	            "f_longitude": "126.7694194",
	            "i_reviewCount": "4",
	            "f_gradeAvg": "4.5",
	            "i_menuBool": "2",
	            "i_propery": "2",
	            "i_coupon": "0",
	            "i_deliveryB": "12",
	            "i_deliveryE": "0",
	            "i_order": "7",
	            "s_logoPath": "http://img.woowabros.co.kr/food_img/fran/01/franchise_thumb_112.jpg",
	            "s_delivery": "주엽동,정발산동,마두동,장항동",
	            "i_card": "1",
	            "s_closeDay": "",
	            "i_delivery_yn": "1",
	            "f_distance": "0.65"
	        },
	        {
	            "i_key": "44204",
	            "s_title": "교촌치킨강선점",
	            "s_phoneNumber": "0319119980",
	            "s_phoneNumberV": "05058376451",
	            "s_phoneNumberF": "",
	            "s_address": "경기 고양시 일산동구 장항2동 717-3",
	            "f_latitude": "37.6551165",
	            "f_longitude": "126.768748",
	            "i_reviewCount": "1",
	            "f_gradeAvg": "4",
	            "i_menuBool": "3",
	            "i_propery": "2",
	            "i_coupon": "0",
	            "i_deliveryB": "0",
	            "i_deliveryE": "0",
	            "i_order": "1",
	            "s_logoPath": "",
	            "s_delivery": "",
	            "i_card": "3",
	            "s_closeDay": "",
	            "i_delivery_yn": "1",
	            "f_distance": "0.29"
	        }	        
	    ]
	}
	 * 
	 */

	public static NMEALoader nmea;
	public static JSONObject mainContent, marketList;

	public final static String MAIN_URI = "http://apple.sellertool.co.kr/android/v3/main.php";
	public final static String MARKET_URI = "http://www.woowabros.co.kr/user/user.php?compress=N";
	
	public final static String MY_TWITTER = "https://mobile.twitter.com/#!/aucd29";
	public final static String GOOGLE_SEARCH_ENGINE = "http://www.google.co.kr/m/search?q=";
	
	public final static String REGISTRATION = "http://www.smartbaedal.com/app_reg/baedal_reg_android.html";
	public final static String REGISTRATION_CAFE = "http://www.smartbaedal.com/app_reg/baedal_cafe.html";
	public final static String MAIL_URI = "http://www.smartbaedal.com/app_reg/baedal_email.html";
	public final static String POST_URI = "http://www.smartbaedal.com/app_reg/baedal_post.html";
	
	public final static String NOTICE_URI = "http://www.smartbaedal.com/user_bbs/gnuboard4/bbs/board.php?bo_table=android_notice";
	public final static String EVENT_URI = "http://www.smartbaedal.com/user_bbs/gnuboard4/bbs/board.php?bo_table=android_event";
	public final static String TWITTER_URI = "http://mobile.twitter.com/baedal2";

	public final static String BANNER_LIST   	= "a_banner_list";
	public final static String BANNER_IMG    	= "s_banner_img";
	public final static String BANNER_URI    	= "s_banner_url";
	public final static String S_NOTICE  	 	= "s_notice";
	public final static String S_NOTICE_URI  	= "s_notice_url";
	public final static String S_EVENT  	 	= "s_event";
	public final static String S_EVENT_URI   	= "s_event_url";
	public final static String SEARCH_KEYWORD 	= "a_search_keyword";
	public final static String KEYWORD			= "keyword";
	
	public final static String MARKETLIST_ALL		 			= "a_list";
	public final static String MARKETLIST_LOGOPATH 	 		= "s_logoPath";	
	public final static String MARKETLIST_TITLE	 			= "s_title";
	public final static String MARKETLIST_DISTANCE 	 		= "f_distance";
	public final static String MARKETLIST_ADDR		 		= "s_address";
	public final static String MARKETLIST_GRADEAVG 			= "f_gradeAvg";
	public final static String MARKETLIST_REVIEWER_COUNT 		= "i_reviewCount";
	public final static String MARKETLIST_MENU		 		= "i_menuBool";
	public final static String MARKETLIST_DELIVERY_YN 	 	= "i_delivery_yn";
	public final static String MARKETLIST_CARD 	 			= "i_card";
	public final static String MARKETLIST_COUPON 	 			= "i_coupon";		
	public final static String MARKETLIST_FREE_PHONENUM 		= "s_phoneNumberF";
	public final static String MARKETLIST_DELIVERY_BEGINTIME	= "i_deliveryB";
	public final static String MARKETLIST_DELIVERY_ENDTIME 	= "i_deliveryE";
	public final static String MARKETLIST_DELIVERY_AREA 		= "s_delivery";
	public final static String MARKETLIST_PHONENUM 			= "s_phoneNumber";	
//	public final static String MARKETLIST_PROPERTY = "i_propery";	
	public final static String MARKETLIST_ORDER 	 			= "i_order";			
//	public final static String MARKETLIST_CLOSEDAY	 = "s_closeDay";	
	
	public final static String USER_AGENT		= "and1_5.2";

    public static void instanceNMEA(Context context) {
    	nmea = new NMEALoader(context);
    }
}

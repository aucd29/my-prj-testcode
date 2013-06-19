/*
 *	Copyright 2011 cheol-dong, choi, twitter @aucd29
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package net.sarangnamu.android;


import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import net.sarangnamu.android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;


public class ScheduleList extends Activity {

	/*
	 * Olleh square data URL
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		_listView = (ListView)findViewById(R.id.listView);

		try {
			String response = ResponseData.getInstance().listData(false);

			/*
			    <ul class="list_data">
        		<li>
					<div class="data_img">
						<a href="./view/249">
                    		<img src="/files/concert/1589.jpg" width="120" height="130"/>
                		</a>
				 	</div>
            		<div class="data_title">
                		<a href="./view/249" class="font_title">
                    	허니서클
                		</a>
            		</div>
            		<div class="data_date font_date">
						공연일시 : 2011/03/04 19:30 ~ 21:00
            		</div>

            		<div class="data_content font_content">
                		허니서클은 스탠더드, 퓨전재즈, 컨템포러리 음악과 그루브 있는 사운드 연주로 유명한 팀입니다. 서울재즈 아카데미 강사진...
            		</div>

            		<div class="data_btn">
                		<a href="./view/249"><img src="/views/concert/images/list_data_btn.gif" alt="" width="121" height="20" /></a>
            		</div>
        		</li>
			 */


			int ps = response.indexOf("<ul class=\"list_data\">");
			if (ps == -1) throw new Exception();

			int pe = response.indexOf("</ul>", ps);
			if (pe == -1) throw new Exception();

			response = response.substring(ps, pe);
			ps = pe = 0;
			Log.d("@response", Integer.toString(response.length()));


			// set listview adapter
			final ImageListAdapter adapter = new ImageListAdapter(this, R.layout.listitem);

			// 리스트 상에 썸네일에 해당하는 아이디를 설정한다.
			adapter.setImageViewId(R.id.imageView);

			// 리스트 상에 문자열에 해당하는 아이디를 설정한다.
			// 이때 이 순서대로 리스트상에 데이터가 입력되어야 한다.
			// 이는 리스트에 문자열은 배열 형태로 구성되어 있기 때문이고
			// 이는 ImageListViewType 를 별도로 수정하지 않아도 데이터를
			// 추가/삭제 하여 사용할 수 있기 때문에 이렇게 구현 하였다.
			//
			adapter.addLisViewId(R.id.groupName);
			adapter.addLisViewId(R.id.date);
			adapter.addLisViewId(R.id.contents);

			// parse the XML as a W3C Document
			// html 이지만 구조는 어차피 같으니 xpath를 이용해 쉽게 파싱하자.
			// 주의점은 xpath는 dom 형태이므로 sax 와 비교하면 메모리를 더 소요
			// 하게 되므로 이를 기억하고 작업하도록 한다. (요즘 단말들이 워낙
			// 빵빵하긴 하지만... 서도)
			//
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			XPath xpath = XPathFactory.newInstance().newXPath();

			// XPATH 에 대한 문법은 다음 사이트를 참고 하면 된다.
			// http://sarangnamu.net/basic/basic_view.php?no=4726
			//
			String concert, expression, image, xmlData[] = new String[4];
			while(true) {
				ps = response.indexOf("<li>", pe);
				if (ps == -1) break;

				pe = response.indexOf("</li>", ps);
				if (pe == -1) break;
				pe += 5; // </li> length

				concert = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + response.substring(ps, pe).trim();

				// html parsing
				Document document = builder.parse(new ByteArrayInputStream(concert.getBytes()));

				expression = "//div [@class='data_img']/a/img/@src";
				image = ResponseData.getInstance().getBaseURL() + ((String) xpath.evaluate(expression, document, XPathConstants.STRING)).trim();
				Log.d("imgURL", image);


				expression = "//div [@class='data_title']/a/text()";
				xmlData[0] = ((String) xpath.evaluate(expression, document, XPathConstants.STRING)).trim();

				expression = "//div [@class='data_date font_date']/text()";
				xmlData[1] = ((String) xpath.evaluate(expression, document, XPathConstants.STRING)).trim();
				int pos = xmlData[1].indexOf(":");
				if (pos != -1) {
					xmlData[1] = xmlData[1].substring(pos + 1);
				}

				expression = "//div [@class='data_content font_content']/text()";
				xmlData[2] = ((String) xpath.evaluate(expression, document, XPathConstants.STRING)).trim();

				expression = "//div [@class='data_btn']/a/@href";
				xmlData[3] = ResponseData.getInstance().getBaseURL() + "/concert" + ((String) xpath.evaluate(expression, document, XPathConstants.STRING)).trim().substring(1);
				pos = xmlData[3].indexOf("?");
				if (pos != -1) {
					xmlData[3] = xmlData[3].substring(0, pos);
				}

				// 도메인에 해당하는 이미지 정보를 불러들이는 데 이는 초기에 이미 SplashScreen
				// 클래스에서 데이터를 읽어 메모리에 보관해 둔 데이터를 반환하게 된다.
				//
				adapter.add(new ImageListViewType(DrawableManager.getInstance().fetchDrawable(image), xmlData));
			}

			_listView.setAdapter(adapter);
			_listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

					// 이미 보유하고 있는 데이터는 인자를 전달한다.
					//
					ImageListViewType  item = (ImageListViewType)adapter.getItem(arg2);
					Intent detail = new Intent("net.sarangnamu.android.DetailView");

					detail.putExtra("teamName", item.getText(0));
					detail.putExtra("date", item.getText(1));
					detail.putExtra("uri", item.getText(3));

					startActivityForResult(detail, 0);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/*
	 * Attributes
	 */
	private ListView _listView;	///< main list view
}

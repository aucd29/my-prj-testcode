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
import java.io.InputStream;
import java.net.URL;

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
import android.text.Html;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

public class DetailView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);

		_teamName = (TextView)findViewById(R.id.teamName);
		_date = (TextView)findViewById(R.id.date);
		_playList = (TextView)findViewById(R.id.playList);
		_description = (TextView)findViewById(R.id.description);

		// get data
		Intent myIntent = getIntent();
		_teamName.setText(myIntent.getStringExtra("teamName"));
		_date.setText(myIntent.getStringExtra("date"));

		try {

			// 혹시나 xml 로 데이터를 요청 받을 수 있을까 하는 생각에
			// 조금 기다려봤으나 facebook 으로 통한 연락이 더 이상
			// 진척 되지 않아 html 을 파싱하는 형태를 유지해야 겠다.
			//
			String uri = myIntent.getStringExtra("uri");
			String content = ResponseData.getInstance().getURLData(uri);

			// [start] <a name="concert_view_content01"></a>
			// [end]   <a name="concert_view_content03"></a>

			String playList, description, token;
			token = "<div class=\"smartOutput\">";

			int posf, pose;

			posf = content.indexOf(token);
			if (posf == -1) throw new Exception();

			pose = content.indexOf("</div>", posf + 1);
			if (pose == -1) throw new Exception();
			posf += token.length();

			description = content.substring(posf, pose).trim();

			// playlist
			posf = content.indexOf(token, pose + 1);
			if (pose == -1) throw new Exception();

			pose = content.indexOf("</div>", posf + 1);
			if (pose == -1) throw new Exception();
			posf += token.length();

			playList = content.substring(posf, pose).trim();

			// html 형태에 데이터를 모두 제거해 plain text 로
			// 만든다.
			description = description.replaceAll("<(.|\n)*?>","");
			_description.setText(description);

			// 위와 동일 하나 개행이 되는 부분이나 <, > 와 같은
			// 부분도 올바른 표현을 위해 수정했다.
			// 결론적으로 xml로 받으면 이런 짓을 할 필요가 없는데.
			// 애석하다
			playList = playList.replace("</P>", "\n");
			playList = playList.replace("<BR>", "\n");
			playList = playList.replace("<BR />", "\n");
			playList = playList.replace("&lt;", "<");
			playList = playList.replace("&gt;", ">");
			playList = playList.replace("&nbsp;", "");
			playList = playList.replaceAll("<(.|\n)*?>","");
			_playList.setText(playList);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/*
	 * Attributes
	 */

	private TextView _teamName;

	private TextView _date;

	private TextView _playList;

	private TextView _description;
}

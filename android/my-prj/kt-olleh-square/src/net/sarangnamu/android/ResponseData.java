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

public class ResponseData {

	private final String BASE_URL = "http://ollehsquare.kt.com";

	private final String LIST_URL = "/concert/list";

	static {
		_instance = new ResponseData();
	}


	/**
	 * constructor
	 */
	private ResponseData() {
		_util = new Util();
	}


	/**
	 * �ڽ��� ��ü�� ��ȯ
	 *
	 * @return ResponseData
	 */
	public static ResponseData getInstance() {
		return _instance;
	}


	/**
	 * web ���� ���� ���� ����Ʈ �����͸� ��ȯ
	 *
	 * @param reset http �����͸� �� ��û������ ���� �Ѵ�.
	 * @return ����Ʈ�� �ش��ϴ� ���ڿ� ������
	 */
	public String listData(boolean reset) {

		if (reset == false && _listData.length() > 0) {
			return _listData;
		}

		try {
			_listData = _util.openURL(BASE_URL + LIST_URL, "GET", null);

			if (_listData.length() == 0) {
				throw new Exception("no data");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return _listData;
	}


	/**
	 * ���ϴ� url�� ���ڿ� �����͸� ��ȯ �Ѵ�.
	 *
	 * @param url ���ϴ� �����Ϳ� URL
	 * @return ���� ���ڿ� ������
	 */
	public String getURLData(String url) {
		try {
			return _util.openURL(url, "GET", null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}


	/**
	 * �����͸� ������ �⺻ domain �� �����´�.
	 *
	 * @return domain ���ڿ�
	 */
	public String getBaseURL() {
		return BASE_URL;
	}


	/*
	 * Attributes
	 */
	private static ResponseData _instance;

	private String _listData;

	private Util _util;
}
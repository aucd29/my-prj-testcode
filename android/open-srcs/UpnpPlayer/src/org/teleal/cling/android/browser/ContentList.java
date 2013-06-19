/*
 * Copyright (C) 2011 Aquilegia, South Korea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.teleal.cling.android.browser;

import java.util.ArrayList;

/**
 * @author Aquilegia
 */
public class ContentList {
	public static final int IMAGEITEM=0;
	public static final int AUDIOITEM=1;
	public static final int VIDEOITEM=2;

	private ArrayList<String> titlelist;
	private ArrayList<String> urilist;
	private int itemType;
	int childItems = 0;

	/**
	 * @param container
	 * @param item
	 */
	public ContentList(int type) {
		super();
		titlelist = new ArrayList<String>();
		urilist = new ArrayList<String>();
		itemType = type;
	}
	
	public ArrayList<String> getTitleList() {
		return titlelist;
	}
	
	public ArrayList<String> getUriList() {
		return urilist;
	}
	
	public void addContent(ContentNode childNode) {
		if (!childNode.isContainer()) {
			String className = childNode.getItem().getClazz().getValue();
			if (itemType == VIDEOITEM && className.contains("videoItem")) {
				titlelist.add(childNode.getItem().getTitle());
				urilist.add(childNode.getItem().getFirstResource().getValue());
				this.childItems++;
			}
			else if (itemType == AUDIOITEM && className.contains("audioItem")) {
				titlelist.add(childNode.getItem().getTitle());
				urilist.add(childNode.getItem().getFirstResource().getValue());
				this.childItems++;
			}
			else if (itemType == IMAGEITEM && className.contains("imageItem")) {
				titlelist.add(childNode.getItem().getTitle());
				urilist.add(childNode.getItem().getFirstResource().getValue());
				this.childItems++;
			}
		}
	}
	
	public void addContentList(ArrayList<ContentNode> childNodes) {
		for (ContentNode childNode : childNodes)
			addContent(childNode);
	}
	
	public int getItemIndex(ContentNode node) {
		int index = 0;
		for (String title : titlelist) {
			String titleName = node.getItem().getTitle();
			if (title.equals(titleName))
				return index;
			index++;
		}
		return -1;		
	}
	
}
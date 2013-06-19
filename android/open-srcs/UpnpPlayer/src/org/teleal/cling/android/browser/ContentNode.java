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

import org.teleal.cling.support.model.container.Container;
import org.teleal.cling.support.model.item.Item;

/**
 * @author Aquilegia
 */
public class ContentNode {
	private boolean isStorageFolder;
	
	Container container;
	Item item;
	ContentNode parentNode;
	ArrayList<ContentNode> childNodes;
	int childContainers = 0;
	int childItems = 0;

	/**
	 * @param container
	 * @param item
	 */
	public ContentNode(ContentNode parent, Container container) {
		super();
		this.parentNode = parent;
		this.container = container;
		this.isStorageFolder = true;
	}
	
	public ContentNode(ContentNode parent, Item item) {
		super();
		this.parentNode = parent;
		this.item = item;
		this.isStorageFolder = false;
	}

	public boolean isContainer() {
		return isStorageFolder;
	}
	
	public Container getContainer() {
		return container;
	}
	
	public Item getItem() {
		return item;
	}
	
	public void setChildNodes(ArrayList<ContentNode> childNodes) {
		this.childNodes = childNodes;
	}
	
	public void addChildNode(ContentNode childNode) {
		if (this.childNodes == null)
			this.childNodes = new ArrayList<ContentNode>();
		this.childNodes.add(childNode);
		if (childNode.isContainer())
			this.childContainers++;
		else
			this.childItems++;
	}
	
	public ArrayList<ContentNode> getChildNodes() {
		return this.childNodes;
	}
	
	public int getChildContainers() {
		return this.childContainers;
	}
	
	public int getChildItems() {
		return this.childItems;
	}
	public ContentNode getParentNode() {
		return parentNode;
	}

}
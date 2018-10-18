package jp.faraopro.play.view;

import java.util.ArrayList;

import jp.faraopro.play.mclient.MCBusinessItem;

public class GuidesItem extends CustomListItem {
	private ArrayList<GuidesItem> children;
	private GuidesItem parent;

	public GuidesItem() {
		children = new ArrayList<GuidesItem>();
	}

	public GuidesItem(CustomListItem item) {
		super(item);
		children = new ArrayList<GuidesItem>();
	}

	public GuidesItem(MCBusinessItem item) {
		super(item);
		children = new ArrayList<GuidesItem>();
	}

	public void setChildren(ArrayList<GuidesItem> items) {
		if (items != null && items.size() > 0) {
			for (GuidesItem gi : items) {
				children.add(gi);
			}
		}
	}

	public void removeChildren() {
		if (children != null)
			children.clear();
		children = new ArrayList<GuidesItem>();
	}

	public GuidesItem getChild(int index) {
		GuidesItem item = null;

		if (children != null && children.size() > index)
			item = children.get(index);

		return item;
	}

	public ArrayList<GuidesItem> getChildren() {
		return children;
	}

	public ArrayList<CustomListItem> getListItem() {
		ArrayList<CustomListItem> list = new ArrayList<CustomListItem>();
		if (children != null && children.size() > 0) {
			for (GuidesItem gi : children) {
				list.add(gi);
			}
		}
		return list;
	}

	public void setParentItem(GuidesItem parent) {
		this.parent = parent;
	}

	public GuidesItem getParentItem() {
		return this.parent;
	}

	public boolean hasChild() {
		boolean has = false;

		if (children != null && children.size() > 0)
			has = true;

		return has;
	}

	public boolean isRoot() {
		boolean root = false;

		if (this.getId() == 0)
			root = true;

		return root;
	}
}

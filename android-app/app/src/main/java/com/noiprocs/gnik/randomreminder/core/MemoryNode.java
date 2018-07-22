package com.noiprocs.gnik.randomreminder.core;

import java.util.List;
import java.util.LinkedList;

public abstract class MemoryNode {
	protected int childCount;

	private List<MemoryNode> parentList = new LinkedList<>();

	public int getChildCount() {
		return childCount;
	}

	public void updateChildCount() {
		++childCount;
		for (MemoryNode parent: parentList) {
			if (parent != null) parent.updateChildCount();
		}
	}

	public void addParent(MemoryNode node) {
		parentList.add(node);
	}

	public abstract String getChild(int index) throws MemoryAiderException;
}

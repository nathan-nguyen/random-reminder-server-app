package com.noiprocs.gnik.randomreminder.core;

import java.util.LinkedList;
import java.util.List;

public class MemoryTag extends MemoryNode {
	private String tag;
	private List<MemoryNode> childrenList = new LinkedList<>();

	public MemoryTag(String tag){
		this.tag = tag;
	}

	public void addChild(MemoryNode child) {
		childrenList.add(child);
	}

	@Override
	public String getChild(int index) throws MemoryAiderException {
		if (childCount == 0) throw new MemoryAiderException("parent: " + tag + " doesn't have any child");
		int total = 0;
		for (MemoryNode child: childrenList) {
			total += child.childCount;
			if (total > index) return child.getChild(index - (total - child.childCount));
		}
		throw new MemoryAiderException("parent: " + tag + " : Index exceeds total childs");
	}
}


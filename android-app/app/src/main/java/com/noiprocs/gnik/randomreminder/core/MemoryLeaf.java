package com.noiprocs.gnik.randomreminder.core;

public class MemoryLeaf extends MemoryNode {
	private String content;

	MemoryLeaf(String content) {
		this.content = content;
	}

	@Override
	public String getChild(int index) throws MemoryAiderException {
		if (index != 0) throw new MemoryAiderException("Index is not equal to 0: " + index);
		return content;
	}
}

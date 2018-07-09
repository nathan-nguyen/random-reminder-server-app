public class MemoryLeaf extends MemoryNode {
	private String content;

	public MemoryLeaf(String content) {
		this.content = content;
	}

	@Override
	public String getChild(int index) throws MemoryAiderException {
		if (index != 0) throw new MemoryAiderException("Index is not equal to 0: " + index);
		return content;
	}
}

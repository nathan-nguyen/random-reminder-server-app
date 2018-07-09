import java.io.FileInputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;
import java.util.Properties;
import java.util.Scanner;

public class MemoryAider {
	private static final Logger LOGGER = Logger.getLogger(MemoryAider.class.getName());

	private MemoryTag rootNode = new MemoryTag("root");
	private Map<String, MemoryTag> tagList = new HashMap<>();
	private Properties properties = new Properties();

	public static void main(String[] args) throws Exception {
		LogManager.getLogManager().reset();

		MemoryAider memoryAider = new MemoryAider();
		memoryAider.initializeProperties();
		memoryAider.loadData();

		System.out.println(memoryAider.getRandomLeaf());
	}

	private void initializeProperties() throws Exception {
		FileReader fileReader = null;
		fileReader = new FileReader(Constant.PROPERTIES_FILE);
		properties.load(fileReader);
		fileReader.close();
	}

	// TODO: Quality Checking - Duplicate - Loop for each line
	private void loadData() throws Exception {
		tagList.put("root", rootNode);

		String fileName = properties.getProperty(Constant.DATA_FILE_KEY);
		LOGGER.info("Data File Format: " + fileName);

		Scanner scanner = new Scanner(new FileInputStream(fileName));

		// Read and construct the tag graph
		while (scanner.hasNextLine()) {
			String s = scanner.nextLine().replaceAll(Constant.SPACE_STRING, Constant.EMPTY_STRING);
			if (s.length() == 0) break;

			if (s.contains(Constant.COMMENT_SIGN)) continue;

			String[] result = MemoryAiderUtil.firstSplit(s, Constant.COLON_CHARACTER);
			String parent = result[0];
			LOGGER.info("parent: " + parent);

			MemoryTag parentNode = tagList.get(parent);
			if (parentNode == null) throw new MemoryAiderException("Parent does not exist: " + parent);
			String[] childrenList = result[1].split(Constant.COMMA_CHARACTER);

			for (String child: childrenList) {
				LOGGER.info("child: " + child);
				MemoryTag childNode = tagList.getOrDefault(child, new MemoryTag(child));
				parentNode.addChild(childNode);
				childNode.addParent(parentNode);
				tagList.put(child, childNode);
			}
		}

		// Read and create leaf node
		while (scanner.hasNextLine()) {
			String s = scanner.nextLine();
			if (s.contains(Constant.COMMENT_SIGN) || s.length() == 0) continue;

			String[] result = MemoryAiderUtil.firstSplit(s, Constant.DELIMETER);
			String parent = result[0];
			LOGGER.info("parent: " + parent);
		
			MemoryTag parentNode = tagList.get(parent);
			if (parentNode == null) throw new MemoryAiderException("Parent does not exist: " + parent);

			MemoryLeaf childNode = new MemoryLeaf(result[1]);
			parentNode.addChild(childNode);
			childNode.addParent(parentNode);
			childNode.updateChildCount();
		}
	}

	private String getRandomLeaf() throws MemoryAiderException {
		return rootNode.getChild(MemoryAiderUtil.randomRange(rootNode.getChildCount()));
	}
}

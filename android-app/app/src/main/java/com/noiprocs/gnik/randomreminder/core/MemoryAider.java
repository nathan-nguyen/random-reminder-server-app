package com.noiprocs.gnik.randomreminder.core;

import android.util.Log;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public abstract class MemoryAider {
	private final String TAG = MemoryAider.class.getCanonicalName();

	private MemoryTag rootNode = new MemoryTag("root");
	private Map<String, MemoryTag> tagList = new HashMap<>();
	private Properties properties = new Properties();

	public void initializeProperties(InputStream inputStream) throws Exception {
		properties.load(inputStream);
	}

	// TODO: Quality Checking - Duplicate - Loop for each line
	public void loadData(InputStream inputStream) throws Exception {
		tagList.put("root", rootNode);

		Scanner scanner = new Scanner(inputStream);

		// Read and construct the tag graph
		while (scanner.hasNextLine()) {
			String s = scanner.nextLine().replaceAll(Constant.SPACE_STRING, Constant.EMPTY_STRING);
			if (s.length() == 0) break;

			if (s.contains(Constant.COMMENT_SIGN)) continue;

			String[] result = MemoryAiderUtil.firstSplit(s, Constant.COLON_CHARACTER);
			String parent = result[0];
			Log.i(TAG,"parent: " + parent);

			String[] childrenList = result[1].split(Constant.COMMA_CHARACTER);

			for (String child: childrenList) {
				addParentChild(parent, child);
			}
		}

		// Read and create leaf node
		while (scanner.hasNextLine()) {
			String s = scanner.nextLine();
			if (s.contains(Constant.COMMENT_SIGN) || s.length() == 0) continue;

			String[] result = MemoryAiderUtil.firstSplit(s, Constant.DELIMETER);
			String parent = result[0];
			Log.i(TAG,"parent: " + parent);
		
			addLeaf(parent, result[1]);
		}
	}

	protected void addParentChild(String parent, String child) {
        MemoryTag parentNode = tagList.get(parent);
        if (parentNode == null) {
            Log.i(TAG, "Parent does not exist: " + parent);
            parentNode = new MemoryTag(parent);
        }

        Log.i(TAG,"child: " + child);
        MemoryTag childNode = tagList.get(new MemoryTag(child));

        if (childNode == null) {
            childNode = new MemoryTag(child);
        }

        parentNode.addChild(childNode);
        childNode.addParent(parentNode);
        tagList.put(child, childNode);
    }

    protected void addLeaf(String parent, String content) throws MemoryAiderException {
        MemoryTag parentNode = tagList.get(parent);
        if (parentNode == null) throw new MemoryAiderException("Parent does not exist: " + parent);

        MemoryLeaf childNode = new MemoryLeaf(content);
        parentNode.addChild(childNode);
        childNode.addParent(parentNode);
        childNode.updateChildCount();
    }

	public String getRandomLeaf() throws MemoryAiderException {
	    if (rootNode.childCount == 0) return new String("No note");
		return rootNode.getChild(MemoryAiderUtil.randomRange(rootNode.getChildCount()));
	}
}

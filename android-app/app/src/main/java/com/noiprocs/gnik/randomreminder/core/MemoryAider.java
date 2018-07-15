package com.noiprocs.gnik.randomreminder.core;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;
import java.util.Properties;
import java.util.Scanner;

public class MemoryAider {
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

			MemoryTag parentNode = tagList.get(parent);
			if (parentNode == null) throw new MemoryAiderException("Parent does not exist: " + parent);
			String[] childrenList = result[1].split(Constant.COMMA_CHARACTER);

			for (String child: childrenList) {
				Log.i(TAG,"child: " + child);
				MemoryTag childNode = tagList.get(new MemoryTag(child));
				if (childNode == null) {
					childNode = new MemoryTag(child);
				}
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
			Log.i(TAG,"parent: " + parent);
		
			MemoryTag parentNode = tagList.get(parent);
			if (parentNode == null) throw new MemoryAiderException("Parent does not exist: " + parent);

			MemoryLeaf childNode = new MemoryLeaf(result[1]);
			parentNode.addChild(childNode);
			childNode.addParent(parentNode);
			childNode.updateChildCount();
		}
	}

	public String getRandomLeaf() throws MemoryAiderException {
		return rootNode.getChild(MemoryAiderUtil.randomRange(rootNode.getChildCount()));
	}
}

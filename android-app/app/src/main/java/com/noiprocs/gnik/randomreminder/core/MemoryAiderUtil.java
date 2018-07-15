package com.noiprocs.gnik.randomreminder.core;

import java.util.Random;

public class MemoryAiderUtil {
	public static Random random = new Random();

	public static String[] firstSplit(String s, String delimeter) {
		int delimeterIndex = s.indexOf(delimeter);
		return new String[]{ s.substring(0, delimeterIndex), s.substring(delimeterIndex + delimeter.length()) };
	}

	// [0, end)
	public static int randomRange(int end) {
		return random.nextInt(end);
	}
}

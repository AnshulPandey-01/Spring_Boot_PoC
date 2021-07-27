package com.anshul.boot_poc.helper;

import java.util.Random;

public class RandomString {
	
	private static final String characters = "abcdefghijklmnopqrstuvwxyz";
	
	private static final String numbers = "0123456789";
	
	private static Random rand;
	
	private static char[] letters;
	
	static {
		rand = new Random();
		letters = (RandomString.characters + RandomString.numbers + RandomString.characters.toUpperCase()).toCharArray();
	}
	
	public static String getAlphaNumericString(int len) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i<len; i++) {
			sb.append(letters[rand.nextInt(62)]);
		}
		return sb.toString();
	}
}

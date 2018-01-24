package com.oracle.coherence.info.helper;

public class Localization {
	public static String getLocalText(String string){
		return string;
	}

	public static String getLocalText(String string, String[] strings) {
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<strings.length; i++){
			builder.append(strings[i]);
		}
		return builder.toString();
	}
}

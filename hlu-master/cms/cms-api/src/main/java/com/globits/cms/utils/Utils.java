package com.globits.cms.utils;

public class Utils {
	public static boolean isBlank(String str) {
		if(str != null) {
			if(str.trim().length() > 0) {
				return false;
			}else {
				return true;
			}
		}
		return true;
	}
}

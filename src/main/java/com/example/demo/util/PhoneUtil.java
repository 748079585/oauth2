package com.example.demo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lei
 * @date 2019/08/16
 */
public class PhoneUtil {

	private static String REGEX = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
	private static Pattern P = Pattern.compile(REGEX);
	private static int phoneLenth = 11;

	/**
	 * 校验手机号
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean checkPhone(String phone) {
		if (phone == null || phone.length() != phoneLenth) {
			return Boolean.FALSE;
		}

		Matcher m = P.matcher(phone);
		return m.matches();
	}
}

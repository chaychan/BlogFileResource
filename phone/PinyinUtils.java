package com.chaychan.blogfileresource.phone;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PinyinUtils {

	public static String getPinyin(String str) {
		// 设置拼音结果的格式
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE);// 设置为大写形式
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不用加入声调

		StringBuilder sb = new StringBuilder();

		char[] charArray = str.toCharArray();

		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];

			if (Character.isWhitespace(c)) {// 如果是空格则跳过
				continue;
			}

			if (isHanZi(c)) {// 如果是汉字
				String s = "";
				try {
					// toHanyuPinyinStringArray 返回一个字符串数组是因为该汉字可能是多音字，此处只取第一个结果
					s = PinyinHelper.toHanyuPinyinStringArray(c, format)[0];
					sb.append(s);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
					sb.append(s);
				}

			} else {
				// 不是汉字
				if (i == 0) {
					if (isEnglish(c)) {// 第一个属于字母，则返回该字母
						return String.valueOf(c).toUpperCase(Locale.ENGLISH);
					}
					return "#"; // 不是的话返回#号
				}
			}
		}
		return sb.toString();
	}

	public static boolean isHanZi(char c) {
		Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]+");
		Matcher matcher = pattern.matcher(String.valueOf(c));
		return matcher.matches();
	}
	
	 public static boolean isEnglish(char c) {
		  return String.valueOf(c).matches("^[a-zA-Z]*");
     }

}

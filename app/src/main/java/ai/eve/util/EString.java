package ai.eve.util;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

/**
 * <p>
 * 字符串工具类
 * </p>
 * @author Eve-wyong
 * @version 2015-09-10 上午11:03:06
 * @Copyright 2016 EVE. All rights reserved.
 */
public class EString {

    /**
     * 判断字符串是否为空或长度为0
     * @param str
     * @return
     */
    public static boolean IsEmpty(String str){
        return TextUtils.isEmpty(str);
    }

    /**
     * 设置字符串首字母大写
     * @param str
     * @return
     */
    public static String ToCapFirstLetter(String str) {
        if (IsEmpty(str)) {
            return str;
        }
        char c = str.charAt(0);
        if (!Character.isLetter(c) || Character.isUpperCase(c)) {
            return str;
        } else {
            return new StringBuilder(str.length()).append(Character.toUpperCase(c)).append(str.substring(1)).toString();
        }
    }

    /**
     * 将字符串中小写字母改为大写字母
     * @param str
     * @return
     */
    public static String ToUpperCase(String str) {
        if (IsEmpty(str)) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isLetter(c) && Character.isLowerCase(c)) {
                stringBuilder.append(Character.toUpperCase(c));
            } else {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 将字符串中大写字母改为小写字母
     * @param str
     * @return
     */
    public static String ToLowerCase(String str) {
        if (IsEmpty(str)) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isLetter(c) && Character.isUpperCase(c)) {
                stringBuilder.append(Character.toLowerCase(c));
            } else {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 去掉字符串左边空格
     * @param str
     * @return
     */
    public static String Ltrim(String str) {
        if (IsEmpty(str)) {
            return str;
        }
        int start = 0;
        int end = str.length() - 1;
        while (start <= end && str.charAt(start) <= ' ') {
            start++;
        }
        if (start <= end) {
            return str.substring(start);
        } else {
            return "";
        }
    }

    /**
     * 去掉字符串右边空格
     * @param str
     * @return
     */
    public static String Rtrim(String str) {
        if (IsEmpty(str)) {
            return str;
        }
        int start = 0;
        int end = str.length() - 1;
        while (start <= end && str.charAt(end) <= ' ') {
            end--;
        }
        if (start <= end) {
            return str.substring(start, end + 1);
        } else {
            return "";
        }
    }

    /**
     * 对字符串设置颜色  [start, end)半开半闭区间
     * @param str
     * @param start
     * @param end
     * @param color
     * @return
     */
    public static SpannableStringBuilder SetColorFormat(String str, int start, int end, int color) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
        if (IsEmpty(str) || start < 0 || end < 0 || start >= end || start >= str.length() || end > str.length()) {
            return spannableStringBuilder;
        }
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(color);
        spannableStringBuilder.setSpan(foregroundColorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    /**
     * 对字符串设置颜色  [ start, end)半开半闭区间
     * @param str
     * @param start
     * @param end
     * @param characterStyle
     */
    public static SpannableStringBuilder SetColorFormat(String str, int start, int end, CharacterStyle characterStyle) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
        if (IsEmpty(str) || start < 0 || end < 0 || start >= end || start >= str.length() || end > str.length()) {
            return spannableStringBuilder;
        }
        spannableStringBuilder.setSpan(characterStyle, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

}
package com.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author by songjiuhua
 * createdAt  16/1/31
 */
public class ReplaceMsgTmplateUtils {

    /**
     * 返回模版替换后的内容
     * @param template  模版
     * @param context   map key:需要替换的内容     value:替换后的内容
     * @param startMark     开始标记
     * @param endMark   结束标记
     * @return
     */
    public static String returnTemplate(String template, Map<String, Object> context, String startMark, String endMark) {
        StringBuilder replacedContent = new StringBuilder();
        StringBuilder returnedTmp = new StringBuilder();
        boolean isStart = false;
        for (int i = 0; i < template.length(); i++) {
            if (!isStart) {
                int j = 0;
                for (; j < startMark.length(); j++) {
                    //当前字符是否等于开始标志的第一个字符;当前字符的下一个字符 是否 等于 开始标志的下一个字符;直到遍历完开始标志
                    if (template.charAt(i + j) != startMark.charAt(j)) {
                        break;
                    }
                }
                if (j == startMark.length()) {
                    isStart = true;
                    i += j - 1;
                } else {
                    //如果当前字符还在开始标记之前,则加到replacedContent中
                    returnedTmp.append(template.charAt(i));
                }
            } else {
                int j = 0;
                for (; j < endMark.length(); j++) {
                    if (template.charAt(i + j) != endMark.charAt(j)) {
                        break;
                    }
                }
                if (j == endMark.length()) {
                    isStart = false;
                    i += j - 1;
                    //将开始标记和结束标记之间的内容替换掉,然后初始化returnedTmp(用于存放需要替换的内容)
                    returnedTmp.append(context.get(replacedContent.toString()));
                    replacedContent.setLength(0);
                } else {
                    //如果当前字符在开始标记和结束标记之间,则放在returnedTmp中
                    replacedContent.append(template.charAt(i));
                }
            }
        }
        return returnedTmp.toString();
    }


    public static void main(String aregs[]){
        String template = "大家好,【{A}-{B}-{C}-{D}】，别玩游戏{E}了，【{F}】。";
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("A","章三");
        context.put("B","你妈妈");
        context.put("C","叫你");
        context.put("D","回家");
        context.put("E","地下城");
        context.put("F","赶快回家");
        String msg = returnTemplate(template, context, "{", "}");
        System.out.println(msg);
    }
}

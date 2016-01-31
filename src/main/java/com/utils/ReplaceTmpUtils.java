package com.utils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author by songjiuhua
 * createdAt  16/1/8
 */
public class ReplaceTmpUtils {

    public static void main(String[] args){
        String template = "$$经纪人姓名$$，您预约拍摄的【$$房源区域$$-$$楼盘名$$-$$栋座名$$-$$室号$$】，房源编号$$房源编号$$由【摄影师：$$摄影师姓名$$；电话：$$摄影师电话$$；电话：；】前往拍摄，请您尽快联系摄影师，拍摄时间只能在预约之日起4日之内。如遇异常，请在腾讯通上联系80000反馈。";
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("经纪人姓名", "张三");
        context.put("房源区域", "浦东");
        context.put("楼盘名", "高荣小区");
        context.put("栋座名", 07);
        context.put("室号", "101");
        context.put("房源编号", "102029042");
        context.put("摄影师姓名", "里斯");
        context.put("摄影师电话", "18800001111");
        //开始标志和结束标志可以一样,也可以不一样
        String msg = returnTemplate(template, context, "$$", "$$");
        System.out.println(msg);
    }

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

}

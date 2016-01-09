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



    /**
     * 返回替换后得到的内容
     * @param template  短信模版
     * @param map   key:需要替换的值     value:该值的内容
     * @return
     */
    public static String returnTemplate(String template, Map<String, Object> map, String startMark, String endMark){
        Set<String> set = getReplacedParams(template, startMark, endMark);
        for(String name : set){
            String value = (String) (map.get(name) == null ? "" : map.get(name));
            String regex = "\\Q" + startMark + name + endMark +"\\E";
            template = template.replaceAll(regex, value);
        }
        return template;
    }

    /**
     * 获取需要替换的内容,存放在set中
     * @param template  短信模版
     * @return
     */
    private static Set<String> getReplacedParams(String template, String startMark, String endMark){
        Set<String> set = new HashSet<String>();
        if(startMark.equals(endMark)){
            set = startMarkEqualsEndMark(template, startMark, endMark);
        }else{
            set = startMarkNotEqualsEndMark(template, startMark, endMark);
        }
        return set;
    }

    /**
     * 开始标志位等于结束标志位时,返回需要替换的内容
     * @param template
     * @param startMark
     * @param endMark
     * @return
     */
    private static Set<String> startMarkEqualsEndMark(String template, String startMark, String endMark){
        Set<String> set = new HashSet<String>();
        int start = 0, end = 0;
        while (end < template.length()) {
            start = template.indexOf(startMark, end) + startMark.length();
            if (start == -1) break;
            end = template.indexOf(endMark, start);
            if (end == -1) break;
            set.add(template.substring(start, end));
            end = end + endMark.length();
        }
        return set;
    }

    /**
     * 开始标志位等于结束标志位时,返回需要替换的内容
     * @param template
     * @param startMark
     * @param endMark
     * @return
     */
    private static Set<String> startMarkNotEqualsEndMark(String template, String startMark, String endMark){
        Set<String> set = new HashSet<String>();
        int start = 0, end = 0;
        while (template.indexOf(startMark) != -1){
            start = template.indexOf(startMark) + startMark.length();
            end = template.indexOf(endMark);
            set.add(template.substring(start, end));
            template = template.substring(end + endMark.length());
        }
        return set;
    }

    public static void main(String[] args) {
        String template = "{{经纪人姓名}}，您预约拍摄的【{{房源区域}}-{{楼盘名}}-{{栋座名}}-{{室号}}】，房源编号{{房源编号}}由【摄影师：{{摄影师姓名}}；电话：{{摄影师电话}}】前往拍摄，请您尽快联系摄影师，拍摄时间只能在预约之日起4日之内。如遇异常，请在腾讯通上联系80000反馈。";
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("经纪人姓名", "张三");
        context.put("房源区域", "浦东");
        context.put("楼盘名", "高荣小区");
        context.put("栋座名", "07");
        context.put("室号", "101");
        context.put("房源编号", "102029042");
        context.put("摄影师姓名", "里斯");
        context.put("摄影师电话", "18800001111");
        String tem = returnTemplate(template, context, "{{", "}}");
        System.out.println(tem);
    }

}

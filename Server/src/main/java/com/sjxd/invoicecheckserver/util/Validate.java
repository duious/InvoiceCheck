package com.sjxd.invoicecheckserver.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangyl
 * 验证工具类
 * <p>
 * 2017年09月26日
 */
public class Validate {
    public static boolean isNull(String... params) {
        for (String param : params) {
            if (null == param || "".equals(param.trim()) || "null".equalsIgnoreCase(param.trim())
                    || "NaN".equalsIgnoreCase(param.trim())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNum(Integer... params) {
        Pattern pattern = Pattern.compile("[1-9][0-9]*");
        for (Integer param : params) {
            Matcher m = pattern.matcher(String.valueOf(param));
            if (!m.matches()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNull(Collection<?>... params) {
        for (Collection<?> param : params) {
            if (null == param || param.size() == 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNum(String... params) {
        Pattern pattern = Pattern.compile("[1-9][0-9]*");
        Pattern pattern0 = Pattern.compile("[0-9]*");
        for (String param : params) {
            Matcher m;
            if (String.valueOf(param).startsWith("0")) {
                m = pattern0.matcher(String.valueOf(param));
            } else {
                m = pattern.matcher(String.valueOf(param));
            }
            if (!m.matches())
                return false;
        }
        return true;
    }

    public static boolean isUUID(String... params) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9]{32}$");
        for (String param : params) {
            Matcher m = pattern.matcher(String.valueOf(param));
            if (!m.matches()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 纯小写字母
     *
     * @param params 参数
     * @return true验证通过
     */
    public static boolean isabc(String... params) {
        Pattern pattern = Pattern.compile("^[a-z]*");
        for (String param : params) {
            Matcher m = pattern.matcher(String.valueOf(param));
            if (!m.matches()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isMobile(String... params) {
//        Pattern pattern = Pattern.compile("(13\\d|14[57]|15[^4,\\D]|17[35678]|18\\d)\\d{8}|170[059]\\d{7}");
        Pattern pattern = Pattern.compile("^(13\\d|14[01456789]|15\\d|16[56]|17[012345678]|18\\d|19[89])\\d{8}$");
        for (String param : params) {
            Matcher m = pattern.matcher(String.valueOf(param));
            if (!m.matches()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmail(String... params) {
        Pattern pattern = Pattern.compile("\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}");
        for (String param : params) {
            Matcher m = pattern.matcher(String.valueOf(param));
            if (!m.matches()) {
                return false;
            }
        }
        return true;
    }

    /**
     * xxxx-xx-xx xx:xx:xx
     *
     * @param params 参数
     * @return true验证通过
     */
    public static boolean isDate(String... params) {
        Pattern pattern = Pattern.compile(
                "((((1[6-9]|[2-9]\\d)\\d{2})-(1[02]|0?[13578])-([12]\\d|3[01]|0?[1-9]))|(((1[6-9]|[2-9]\\d)\\d{2})-" +
                        "(1[012]|0?[13456789])-([12]\\d|30|0?[1-9]))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-" +
                        "(1\\d|2[0-8]|0?[1-9]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|(" +
                        "(16|[2468][048]|[3579][26])00))-0?2-29-)) ([01]?\\d|2[0-3]):[0-5]?\\d:[0-5]?\\d");
        for (String param : params) {
            Matcher m = pattern.matcher(String.valueOf(param));
            if (!m.matches())
                return false;
        }
        return true;
    }

    public static boolean isHtml(String... params) {
        Pattern pattern = Pattern.compile("<(.*)(.*)>.*</\\1>|<(.*) />");
        for (String param : params) {
            Matcher m = pattern.matcher(String.valueOf(param));
            if (!m.matches()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isIDCard(String... params) {
        for (String param : params) {
            if (!isIDCardValidate(param).equals("true")) {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证信用卡号
     */
    private static boolean isCreditCard(String sCard) {
        String reCard = "^(4\\d{12}(?:\\d{3})?)$";
        Pattern com = Pattern.compile(reCard);
        Matcher mat = com.matcher(sCard);
        if (mat.find()) {
            // 判断是否合法
            boolean luhn = isLuhn(sCard);
            if (luhn) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 验证信用卡有效期
     *
     * @param date MM/yy格式日期
     */
    public static boolean isCreditCardDate(String date) {
        if (isNull(date) || date.length() != 5) {
            return false;
        }
        try {
            if (Integer.parseInt(date.substring(3)) > Integer.parseInt(
                    String.valueOf(Util.TimeFormat(Util.getTime(), 8)).substring(2, 4))) {
                return true;
            } else if (Integer.parseInt(date.substring(3)) == Integer.parseInt(
                    String.valueOf(Util.TimeFormat(Util.getTime(), 8)).substring(2, 4))) {
                if (Integer.parseInt(date.substring(0, 2)) > Integer.parseInt(
                        String.valueOf(Util.TimeFormat(Util.getTime(), 8)).substring(4, 6))) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * luhn算法
     */
    private static boolean isLuhn(String strNum) {
        int oddSum = 0;
        int evenSum = 0;
        boolean isOdd = true;
        for (int i = strNum.length() - 1; i >= 0; i--) {
            char cNum = strNum.charAt(i);
            int num = Integer.parseInt(cNum + "");
            if (isOdd) {
                oddSum += num;
            } else {
                num = num * 2;
                if (num > 9) {
                    num = num % 10 + 1;
                }
                evenSum = evenSum + num;
            }
            isOdd = !isOdd;
        }
        return ((evenSum + oddSum) % 10 == 0);
    }

    private static String isIDCardValidate(String IDStr) {
        String tipInfo = "true";// 记录错误信息
        String Ai = "";
        // 判断号码的长度 15位或18位
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            tipInfo = "身份证号码长度应该为15位或18位。";
            return tipInfo;
        }

        // 18位身份证前17位位数字，如果是15位的身份证则所有号码都为数字
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (!isNum(Ai)) {
            tipInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return tipInfo;
        }

        // 判断出生年月是否有效
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 日期
        if (!isDateValidate(strYear + "-" + strMonth + "-" + strDay)) {
            tipInfo = "身份证出生日期无效。";
            return tipInfo;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                tipInfo = "身份证生日不在有效范围。";
                return tipInfo;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            tipInfo = "身份证月份无效";
            return tipInfo;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            tipInfo = "身份证日期无效";
            return tipInfo;
        }
        // 判断地区码是否有效
        Map<String, Object> areacode = GetAreaCode();
        // 如果身份证前两位的地区码不在Hashtable，则地区码有误
        if (areacode.get(Ai.substring(0, 2)) == null) {
            tipInfo = "身份证地区编码错误。";
            return tipInfo;
        }
        if (!isVarifyCode(Ai, IDStr)) {
            tipInfo = "身份证校验码无效，不是合法的身份证号码";
            return tipInfo;
        }
        return tipInfo;
    }

    /**
     * 将所有地址编码保存在一个Hashtable中
     *
     * @return Hashtable 对象
     */
    private static Map<String, Object> GetAreaCode() {
        Map<String, Object> hashtable = new HashMap<String, Object>();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * @param Ai    前17位数字
     * @param IDStr 判断第18位校验码是否正确<br>
     *              第18位校验码的计算方式：<br>
     *              1. 对前17位数字本体码加权求和 <br>
     *              公式为：S = Sum(Ai * Wi), i = 0, ... , 16<br>
     *              其中Ai表示第i个位置上的身份证号码数字值，Wi表示第i位置上的加权因子，其各位对应的值依次为： 7 9 10 5 8 4
     *              2 1 6 3 7 9 10 5 8 4 2<br>
     *              2. 用11对计算结果取模 <br>
     *              Y = mod(S, 11)<br>
     *              3. 根据模的值得到对应的校验码<br>
     *              对应关系为：<br>
     *              Y值： 0 1 2 3 4 5 6 7 8 9 10 <br>
     *              校验码： 1 0 X 9 8 7 6 5 4 3 2
     * @return true 验证通过
     */
    private static boolean isVarifyCode(String Ai, String IDStr) {
        String[] VarifyCode = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum = sum + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue = sum % 11;
        String strVerifyCode = VarifyCode[modValue];
        Ai = Ai + strVerifyCode;
        if (IDStr.length() == 18) {
            boolean a = Ai.equalsIgnoreCase(IDStr);
            boolean b = Ai.equals(IDStr);
            if (!a && !b) {
                return false;
            }
        }
        return true;
    }

    /**
     * 功能：判断字符串出生日期是否符合正则表达式：包括年月日，闰年、平年和每月31天、30天和闰月的28天或者29天
     *
     * @param strDate 待验证日期
     * @return true验证通过
     */
    private static boolean isDateValidate(String strDate) {
        Pattern pattern;
        pattern = Pattern.compile(
                "^((\\d{2}(([02468][048])|([13579][26]))[\\-/\\s]?((((0?[13578])|(1[02]))[\\-/\\s]?((0?[1-9])|" +
                        "([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|" +
                        "(0?2[\\-/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))" +
                        "[\\-/\\s]?((((0?[13578])|(1[02]))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|" +
                        "(11))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-/\\s]?((0?[1-9])|(1[0-9])|(2[0-8])))))" +
                        ")?$");
        Matcher m = pattern.matcher(strDate);
        return m.matches();
    }

//    public static void main(String[] args) {
    // System.out.println(isUUID("12345678123456781234567812345678"));
//         System.out.println(isMobile("17311111111"));
    // System.out.println(isDate("2017-01-11 20:30:30"));
    // Date date = new Date();
    // System.out.println(date.getTime());
    // System.out.println(isMobile("18201571705"));
    // System.out.println(isabc("asdasdasZXCdasd"));
    // System.out.println(isIDCard("150404199206"));
//        System.out.println(isCreditCard("3568891143637767127"));
//    }

}

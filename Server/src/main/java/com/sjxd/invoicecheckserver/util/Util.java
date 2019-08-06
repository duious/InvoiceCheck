package com.sjxd.invoicecheckserver.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import net.sf.json.JSONArray;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 工具
 *
 * @author zhangyl
 */
public final class Util {

    /**
     * 静态文件基础路径
     */
    public final static String fileBasePath = "";
    /**
     * 临时文件存储路径
     */
    public final static String filePath = "file/upload/";

    /**
     * 获取mysql分页起始页
     *
     * @param pageNum  传入页码
     * @param pageSize 每页条数
     * @return mysql limit 第一个参数
     */
    public static String getPageNum(String pageNum, String pageSize) {
        return String.valueOf((Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageSize));
    }

    /**
     * @return UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getVersionNO() {
        return Util.TimeFormat(getTime(), 15).substring(0, 9);
    }

    /**
     * @return 时间戳
     */
    public static String getTime() {
        Date date = new Date();
        return String.valueOf(date.getTime());
    }

    /**
     * @param time 时间戳
     * @param type 类型<br>
     *             1:yyyy-MM-dd<br>
     *             2:yyyy-MM-dd HH:mm<br>
     *             3:yyyy-MM-dd HH:mm:ss<br>
     *             4:yyyy年MM月dd日<br>
     *             5:yyyy年MM月dd日 HH时mm分<br>
     *             6:yyyy年MM月dd日 HH时mm分ss秒<br>
     *             7:MM月dd日 E<br>
     *             8:yyyyMMdd<br>
     *             9:MM月dd日 HH时mm分ss秒<br>
     *             10:MM月dd日 HH时mm分<br>
     *             11:MM月dd日 HH时<br>
     *             12:MM-dd HH<br>
     *             13:MM-dd HH:mm<br>
     *             14:MM-dd HH:mm:ss<br>
     *             15:yyyyMMddHHmmss<br>
     * @return String
     */
    public static String TimeFormat(String time, int type) {
        if (Validate.isNull(time) || !Validate.isNum(time)) {
            return "";
        }
        SimpleDateFormat formatter = null;
        if (1 == type) {
            formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        }
        if (2 == type) {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        }
        if (3 == type) {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        }
        if (4 == type) {
            formatter = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        }
        if (5 == type) {
            formatter = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分", Locale.CHINA);
        }
        if (6 == type) {
            formatter = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒", Locale.CHINA);
        }
        if (7 == type) {
            formatter = new SimpleDateFormat("MM月dd日 E", Locale.CHINA);
        }
        if (8 == type) {
            formatter = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        }
        if (9 == type) {
            formatter = new SimpleDateFormat("MM月dd日 HH时mm分ss秒", Locale.CHINA);
        }
        if (10 == type) {
            formatter = new SimpleDateFormat("MM月dd日 HH时mm分", Locale.CHINA);
        }
        if (11 == type) {
            formatter = new SimpleDateFormat("MM月dd日 HH时", Locale.CHINA);
        }
        if (12 == type) {
            formatter = new SimpleDateFormat("MM-dd HH", Locale.CHINA);
        }
        if (13 == type) {
            formatter = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
        }
        if (14 == type) {
            formatter = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA);
        }
        if (15 == type) {
            formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        }
        Date date = new Date();
        date.setTime(Long.parseLong(time));
        if (formatter != null) {
            return formatter.format(date);
        } else {
            return "";
        }
    }

    /**
     * @param time 待格式化时间
     * @param type 类型<br>
     *             1:yyyy-MM-dd<br>
     *             2:yyyy-MM-dd HH:mm<br>
     *             3:yyyy-MM-dd HH:mm:ss<br>
     *             4:yyyy年MM月dd日<br>
     *             5:yyyy年MM月dd日 HH时mm分<br>
     *             6:yyyy年MM月dd日 HH时mm分ss秒<br>
     *             7:yyyyMMdd<br>
     * @return 时间戳
     */
    public static String FormatTime(String time, int type) {
        if (Validate.isNull(time)) {
            return "";
        }
        SimpleDateFormat formatter = null;
        if (1 == type) {
            formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        }
        if (2 == type) {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        }
        if (3 == type) {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        }
        if (4 == type) {
            formatter = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        }
        if (5 == type) {
            formatter = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分", Locale.CHINA);
        }
        if (6 == type) {
            formatter = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒", Locale.CHINA);
        }
        if (7 == type) {
            formatter = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        }
        Date date;
        String str = "";
        try {
            if (formatter != null) {
                date = formatter.parse(time);
                str = String.valueOf(date.getTime());
            }
        } catch (Exception ignored) {
        }
        return str;
    }


    /**
     * 对字符串md5加密
     *
     * @param str 待加密字符串
     * @return MD5 hash值
     */
    public static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md;
            md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            String md5 = new BigInteger(1, md.digest()).toString(16);
            if (md5.length() == 31) {
                return "0" + md5;
            } else {
                return md5;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return 6位纯数字
     */
    public static String getCode() {
        BigDecimal b = new BigDecimal((Math.random() * 9 + 1) * 100000);
        return (int) b.setScale(0, RoundingMode.FLOOR).doubleValue() + "";
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     *
     * @param request 请求实体
     * @return iprealaddress
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (!Validate.isNull(ip) && ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }


    /**
     * String 转码 UTF-8
     *
     * @param str    待转码字符串
     * @param encode 编码格式
     * @return string
     */
    public static String encode(String str, String encode) {
        try {
            str = URLEncoder.encode(str, encode == null ? "UTF-8" : encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * 读取请求的中文件
     *
     * @param request 请求实体
     * @return Vector<File>
     */
    public static Vector<File> getRequestFile(HttpServletRequest request) {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        Vector<File> vector = new Vector<>();
        // 判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            List<MultipartFile> attachs = multiRequest.getFiles("file");
            for (MultipartFile f : attachs) {
                if (f != null) {
                    try {
                        // 取得当前上传文件的文件名称
                        String myFileName = (new String(f.getOriginalFilename().getBytes(), StandardCharsets.UTF_8));
                        // 如果名称不为"",说明该文件存在，否则说明该文件不存在
                        if (!Validate.isNull(myFileName.trim())) {
                            // 定义上传路径
                            File localFile = new File(fileBasePath + filePath, Util.getTime() + "-" +
                                    Util.getUUID().substring(22) +
                                    myFileName.substring(myFileName.lastIndexOf(".")).toLowerCase());
                            f.transferTo(localFile);
                            vector.addElement(localFile);
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return vector;
    }

    /**
     * 读取请求的中文件
     *
     * @param request 请求实体
     * @return Vector<File>
     */
    public static Vector<Map<String, Object>> getReqFileList(HttpServletRequest request) {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        Vector<Map<String, Object>> vector = new Vector<>();
        // 判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            List<MultipartFile> attachs = multiRequest.getFiles("file");
            for (MultipartFile f : attachs) {
                if (f != null) {
                    try {
                        // 取得当前上传文件的文件名称
                        String myFileName = new String(f.getOriginalFilename().getBytes(multiRequest.getCharacterEncoding()), StandardCharsets.UTF_8);
                        // 如果名称不为"",说明该文件存在，否则说明该文件不存在
                        if (!Validate.isNull(myFileName.trim())) {
                            Map<String, Object> map = new HashMap<>(2);
                            // 定义上传路径
                            File localFile = new File(fileBasePath + filePath, Util.getTime() + "-" +
                                    Util.getUUID().substring(22) +
                                    myFileName.substring(myFileName.lastIndexOf(".")).toLowerCase());
                            f.transferTo(localFile);
                            map.put("fileName", myFileName);
                            map.put("file", localFile);
                            vector.addElement(map);
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return vector;
    }

    /**
     * 输出文件流<br>
     * 2048bps
     *
     * @param file     文件
     * @param response 返回实体
     * @return true成功
     */
    public static boolean outPutFile(File file, HttpServletResponse response, HttpServletRequest request)
            throws Exception {
        final int a = 2048;
        String filename = file.getName();
        String userAgent = request.getHeader("User-Agent");
        // 针对IE或者以IE为内核的浏览器：
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            filename = URLEncoder.encode(filename, "UTF-8");
        } else {
            // 非IE浏览器的处理：
            filename = new String(filename.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
        }
        // 清空response
        response.reset();
        response.addHeader("Content-Length", "" + file.length());
        response.addHeader("Content-Disposition", "attachment;filename=" + filename);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/x-msdownload");
        FileInputStream finput = new FileInputStream(file);
        OutputStream output = response.getOutputStream();
        BufferedInputStream buffin = new BufferedInputStream(finput);
        BufferedOutputStream buffout = new BufferedOutputStream(output);
        byte[] buffer = new byte[a];
        int count;
        while ((count = buffin.read(buffer, 0, buffer.length)) > 0) {
            buffout.write(buffer, 0, count);
        }
        buffin.close();
        buffout.close();
        finput.close();
        output.close();
        file.delete();
        return true;
    }

    /**
     * 输出文件流<br>
     * 2048bps
     *
     * @param path     路径
     * @param response 返回实体
     * @return true成功
     */
    public static boolean outPutFile(String path, HttpServletResponse response) throws Exception {
        final int a = 2048;
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + path.substring(path.lastIndexOf("\\") + 1));
        File file = new File(path);
        FileInputStream finput = new FileInputStream(file);
        OutputStream output = response.getOutputStream();
        BufferedInputStream buffin = new BufferedInputStream(finput);
        BufferedOutputStream buffout = new BufferedOutputStream(output);
        byte[] buffer = new byte[a];
        int count;
        while ((count = buffin.read(buffer, 0, buffer.length)) > 0) {
            buffout.write(buffer, 0, count);
        }
        buffin.close();
        buffout.close();
        finput.close();
        output.close();
        file.delete();
        return true;
    }

    /**
     * obj to json
     *
     * @param arg object
     * @return json
     */
    public static String convJSONObject(Object arg) {
        if (arg == null) {
            return "{}";
        } else {
            return JSONObject.toJSONString(arg, SerializerFeature.DisableCircularReferenceDetect,
                    SerializerFeature.WriteNullStringAsEmpty);
        }
    }

    /**
     * objArray to json
     *
     * @param arg objArray
     * @return json
     */
    public static String convJSONArray(Object arg) {
        if (arg == null) {
            return "[]";
        } else {
            return JSONArray.fromObject(arg).toString();
        }
    }

    /**
     * String to jsonMap
     *
     * @param arg json
     * @return map
     */
    public static Map<String, Object> convJSONToMap(String arg) {
        Map<String, Object> map = new LinkedHashMap<>();
        net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(arg);
        @SuppressWarnings("rawtypes")
        Iterator keys = json.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = json.get(key).toString();
            if (value.startsWith("{") && value.endsWith("}")) {
                map.put(key, convJSONToMap(value));
            } else {
                map.put(key, value);
            }
        }
        return map;
    }
}
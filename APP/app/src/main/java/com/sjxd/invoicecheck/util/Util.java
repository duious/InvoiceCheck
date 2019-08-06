package com.sjxd.invoicecheck.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;
import org.json.JSONObject;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * 工具
 *
 * @author zhangyl
 */
@SuppressWarnings("all")
public final class Util {
    private static boolean isConnected = false;

    //  public static List<String> MyWebBackForwardList = new ArrayList<String>();

    public synchronized boolean getIsConnected() {
        return isConnected;
    }

    public synchronized void setIsConnected(boolean isConnected) {
        Util.isConnected = isConnected;
    }

    /**
     * @return UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
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
        Date date = new Date();
        date.setTime(Long.parseLong(time));
        if (formatter != null) {
            return formatter.format(date);
        } else {
            return null;
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
     * 判断MainActivity是否活动
     *
     * @param context      一个context
     * @param activityName 要判断Activity
     * @return boolean
     */
    public static boolean isMainActivityAlive(Context context, String activityName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            // 注意这里的 topActivity 包含 packageName和className，可以打印出来看看
            if (info.topActivity.toString().equals(activityName) || info.baseActivity.toString().equals(activityName)) {
                // Log.i(TAG,info.topActivity.getPackageName() + " info.baseActivity.getPackageName()="+info
                // .baseActivity.getPackageName());
                return true;
            }
        }
        return false;
    }

    /**
     * 通过Uri找到File
     */
    public static File uriToFile(Activity context, Uri uri) {
        File file;
        String[] project = {MediaStore.Images.Media.DATA};
        Cursor actualImageCursor = context.getContentResolver().query(uri, project, null,
                null, null);
        if (actualImageCursor != null) {
            int actual_image_column_index = actualImageCursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualImageCursor.moveToFirst();
            String img_path = actualImageCursor
                    .getString(actual_image_column_index);
            file = new File(img_path);
        } else {
            file = new File(uri.getPath());
        }
        if (actualImageCursor != null) {
            actualImageCursor.close();
        }
        return file;
    }

    /**
     * 屏幕分辨率
     * 480x960
     */
    public static String getDeviceMsg(Context context) {
        Map<String, String> map = new HashMap<>();
        // 设备厂商
        map.put("brand", Build.BRAND);

        // 设备名称
        map.put("model", Build.MODEL);

        // sdk版本号
        map.put("SDKVersion", String.valueOf(Build.VERSION.SDK_INT));

        // 系统版本
        map.put("SDKVersion", Build.VERSION.RELEASE);

        //屏幕分辨率
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        map.put("pointX", String.valueOf(point.x));
        map.put("pointY", String.valueOf(point.y));

        //设备唯一id
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        map.put("deviceId", telephonyManager.getDeviceId());

        map.put("sim", telephonyManager.getSimSerialNumber());

        String imsi = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
        map.put("imsi", imsi);

        //结果返回值
        String netType = "nono_connect";
        //获取手机所有连接管理对象
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取NetworkInfo对象
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //NetworkInfo对象为空 则代表没有网络
        if (networkInfo == null) {
            return netType;
        }
        //否则 NetworkInfo对象不为空 则获取该networkInfo的类型
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            //WIFI
            netType = "wifi";
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            //4G
            if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                    && !telephonyManager.isNetworkRoaming()) {
                netType = "4G";
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0 && !telephonyManager.isNetworkRoaming()) {
                netType = "3G";
                //2G 移动和联通的2G为GPRS或EGDE，电信的2G为CDMA
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                    || nSubType == TelephonyManager.NETWORK_TYPE_CDMA && !telephonyManager.isNetworkRoaming()) {
                netType = "2G";
            } else {
                netType = "2G";
            }
        }
        map.put("netType", netType);

        JSONObject jsonObject = new JSONObject(map);
        return jsonObject.toString();
    }

}
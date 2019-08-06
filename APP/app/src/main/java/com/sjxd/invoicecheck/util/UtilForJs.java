package com.sjxd.invoicecheck.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
import com.sjxd.invoicecheck.MainActivity;
import com.sjxd.invoicecheck.util.zxing.CaptureActivity;

/**
 * @author zhangyl
 * <p>
 * 17-10-26
 */
@SuppressLint("Registered")
public class UtilForJs extends Activity {
    private static UtilCache utilCache;
    private MainActivity mainActivity;

    private UtilForJs() {
    }

    public UtilForJs(ContextWrapper contextWrapper) {
        utilCache = new UtilCache(contextWrapper);
    }

    public UtilForJs(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        utilCache = new UtilCache(mainActivity);
    }
    public static void setUtilCache(ContextWrapper contextWrapper){
        UtilForJs.utilCache = new UtilCache(contextWrapper);
    }

    @JavascriptInterface
    public void scanQRCode() {
        Toast.makeText(this.mainActivity.getApplicationContext(), "scan", Toast.LENGTH_LONG).show();
        // 启动扫描二维码的activity
        Intent startScan = new Intent(this.mainActivity.getApplicationContext(), CaptureActivity.class);
        // startActivity(startScan);需要返回值，所以暂时不用此方法
        this.mainActivity.startActivityForResult(startScan, 1);
    }

    @JavascriptInterface
    public String getValue(String str) {
        return utilCache.getValue(str);
    }

    @JavascriptInterface
    public Boolean putValue(String str, String val) {
        return utilCache.putValue(str, val);
    }

    @JavascriptInterface
    public boolean delValue(String str) {
        return utilCache.delValue(str);
    }

}

package com.sjxd.invoicecheck;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.sjxd.invoicecheck.listener.PermissionListener;
import com.sjxd.invoicecheck.util.UtilForJs;
import com.sjxd.invoicecheck.util.UtilPermission;
import com.sjxd.invoicecheck.util.UtilPhoto;

import java.io.File;
import java.util.List;

import static com.sjxd.invoicecheck.util.UtilPhoto.*;

/**
 * @author zhangyl
 */
@SuppressWarnings("all")
public class MainActivity extends AppCompatActivity {

    private WebView webView;
    public static Boolean recognition = false;
    private final static int PHOTO_REQUEST = 100;
    private Uri imageUri;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 权限检查
        permissionCheck(new String[]{Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.VIBRATE,
                Manifest.permission.REORDER_TASKS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_WIFI_STATE,});
        //（这个对宿主没什么影响，建议声明）
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        //防止字体被从新设置
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        displayMetrics.scaledDensity = displayMetrics.density;

        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.base_view);
        WebSettings webSettings = webView.getSettings();

        webView.requestFocusFromTouch(); //支持获取手势焦点，输入用户名、密码或其他
        webSettings.setSavePassword(false);//是否自动保存密码
        webSettings.setSaveFormData(false);//表格数据
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//缓存模式
        webSettings.setDomStorageEnabled(true);
        webSettings.setTextZoom(100);//设置webview内部字体的缩放比例
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
        webSettings.setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true
        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        webSettings.setAllowFileAccessFromFileURLs(false);
        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
        webSettings.setAllowUniversalAccessFromFileURLs(false);
        webSettings.supportMultipleWindows();  //多窗口
        webSettings.setSupportMultipleWindows(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS); //支持内容重新布局
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setSupportZoom(false);  //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。
        //若上面是false，则该WebView不可缩放，这个不管设置什么都不能缩放。
        webSettings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setJavaScriptEnabled(true);  //支持js
        webSettings.setPluginState(WebSettings.PluginState.ON);  //支持插件
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);//提高渲染等级
        // webSettings.setBlockNetworkLoads(false);//默认false，设置为true，网页上面报net::ERR_CACHE_MISS错误 log Cannot call
        /**
         *  Webview在安卓5.0之前默认允许其加载混合网络协议内容
         *  在安卓5.0之后，默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    webView.getSettings()
                            .setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                }
            }
        });

        webView.loadUrl(this.getString(R.string.host));
        UtilForJs.setUtilCache(MainActivity.this);
        webView.addJavascriptInterface(new UtilForJs(MainActivity.this), "UtilForJS");

        webView.setWebChromeClient(new WebChromeClient() {
            // For Android 5.0+
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                takePhoto();
                filePathCallback.onReceiveValue(null);
                filePathCallback = null;
                return true;
            }

            @Override
            public void onPermissionRequest(PermissionRequest request) {
                request.grant(request.getResources());
            }
        });

    }

    /**
     * 拍照
     */
    private void takePhoto() {
        File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/" + SystemClock.currentThreadTimeMillis() + ".jpg");
        imageUri = FileProvider.getUriForFile(MainActivity.this, MainActivity.this.getApplicationContext().getPackageName() + ".provider", fileUri);//通过FileProvider创建一个content类型的Uri
        UtilPhoto.takePicture(MainActivity.this, imageUri, PHOTO_REQUEST);
    }

    /**
     * 结果接收
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PHOTO_REQUEST) {
                Bitmap compressBitmap = compressImage(getBitmapFromUri(imageUri, MainActivity.this));
                String str = bitmapToBase64(compressBitmap);
                webView.loadUrl("javascript:recognitionCallBack('" + str + "')");
            } else {
                // 二维码扫码
                Bundle bundle = data.getExtras();
                if (recognition) {
                    Toast.makeText(MainActivity.this, bundle.getString("result"), Toast.LENGTH_LONG).show();
                    webView.loadUrl("javascript:recognitionCallBack('" + bundle.getString("result") + "')");
                } else {
                    webView.loadUrl("javascript:scanQRCallBack('" + bundle.getString("result") + "')");
                }
                // Toast.makeText(MainActivity.this, "scan finsh", Toast.LENGTH_LONG).show();
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (webView.canGoBack()) {
                    webView.goBack();
                    return false;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.setWebViewClient(null);
            webView.setWebChromeClient(null);
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            webView.destroy();
            webView = null;
        }
    }

    private void permissionCheck(String[] permisionString) {
        //权限检查
        //创建PermissionUtil对象，参数为继承自V4包的 FragmentActivity
        UtilPermission permissionUtil = new UtilPermission(MainActivity.this);
        //调用requestPermissions
        permissionUtil.requestPermissions(permisionString, new PermissionListener() {
            @Override
            public void onGranted() {
                //所有权限都已经授权
                //Toast.makeText(BaseActivity.this, "所有权限都已授权", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                //Toast第一个被拒绝的权限
                //Toast.makeText(BaseActivity.this, "拒绝了权限" + deniedPermission.get(0), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onShouldShowRationale(List<String> deniedPermission) {
                //Toast第一个勾选不在提示的权限
                //Toast.makeText(BaseActivity.this, "这个权限" + deniedPermission.get(0) + "勾选了不在提示，要像用户解释为什么需要这权限",
                // Toast.LENGTH_LONG).show();
            }
        });
    }

}

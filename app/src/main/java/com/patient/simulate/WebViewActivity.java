package com.patient.simulate;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;

import com.patient.android.activity.BaseActivity;
import com.patient.simulate.event.EventAction;
import com.patient.simulate.event.util.DataCleanManager;
import com.patient.simulate.net.OkHttpUtils;

import java.lang.reflect.Method;

/**
 * 加载web页面的activity
 *
 * @author zhangshuai
 */
public class WebViewActivity extends BaseActivity {

    public static final String KEY_URL = "url";

    private LinearLayout mWebViewContainer;
    private WebChromeClient mWebChromeClient;
    private WebViewClient mWebViewClient;
    private DownloadListener mDownloadListener;
    private ProgressBar mProgressBar;

    private WebView mWebView;
    private String mUrl;
    private boolean interception = false;
    private Runnable reloadRunnable;
    private Runnable runnable;

    @Override
    protected void init(Bundle bundle) {
        setContentView(R.layout.act_webview);

        mUrl = getIntent().getStringExtra(KEY_URL);

        initView();
        /*
        mDefaultHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MotionEvent downEvent = MotionEvent.obtain(
                        SystemClock.uptimeMillis(),
                        SystemClock.uptimeMillis(),
                        MotionEvent.ACTION_DOWN,
                        100,
                        100,
                        0);
                mWebView.dispatchTouchEvent(downEvent);
                MotionEvent upEvent = MotionEvent.obtain(
                        SystemClock.uptimeMillis(),
                        SystemClock.uptimeMillis(),
                        MotionEvent.ACTION_UP,
                        100,
                        100,
                        0);
                mWebView.dispatchTouchEvent(upEvent);
            }
        }, 5000);
        */

        final EventAction homeEventAction = new EventAction();
        homeEventAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                EventHelper.getInstance().homePage(homeEventAction);
            }
        });

        final EventAction loginEventAction = new EventAction();
        loginEventAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                EventHelper.getInstance().loginPage(loginEventAction);
            }
        });
        homeEventAction.setNext(loginEventAction);

        final EventAction regEventAction = new EventAction();
        regEventAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                EventHelper.getInstance().regPage(regEventAction);
            }
        });
        loginEventAction.setNext(regEventAction);

        final EventAction logonSearchEventAction = new EventAction();
        logonSearchEventAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                EventHelper.getInstance().logonSearch(logonSearchEventAction);
            }
        });
        regEventAction.setNext(logonSearchEventAction);
        /*
        final EventAction rollEventAction = new EventAction();
        rollEventAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                EventHelper.getInstance().rollUpAndDown(rollEventAction);
            }
        });
        logonSearchEventAction.setNext(rollEventAction);

        final EventAction addToCartEventAction = new EventAction();
        addToCartEventAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                EventHelper.getInstance().addToCart(addToCartEventAction);
            }
        });
        rollEventAction.setNext(addToCartEventAction);
        */
        homeEventAction.run();
    }

    private void initView() {
        mWebViewContainer = getViewById(R.id.container);
        mProgressBar = getViewById(R.id.progressBar);
        createNewWebView();

        openUrl(mUrl);
    }

    private void createNewWebView() {
        if (mWebView != null) {
            mWebViewContainer.removeView(mWebView);
            clearWebView(mWebView);
        }
        mWebView = new WebView(this);
        mWebView.clearCache(true);
        mWebView.clearFormData();

        mWebViewContainer.addView(mWebView, 0);
        initWebViewParams();
        initWebViewSettings(mWebView);
    }

    private void initWebViewParams() {
        mWebChromeClient = new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    setProgressBarGone();
                }
            }

        };

        mWebViewClient = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                setProgressBarGone();
                // https://www.amazon.com/gp/aw/d/B0169EXN2M/ref=mp_s_a_1_4?ie=UTF8&qid=1480315073&sr=8-4&pi=AC_SX236_SY340_FMwebp_QL65&keywords=hose+holder
                if (url.startsWith("https://www.amazon.com/gp/aw/s/ref=is_") || url.startsWith("https://www.amazon.com/gp/aw/d")) {
                    view.loadUrl("javascript:window.java_obj.getSource(" +
                            "document.getElementsByTagName('html')[0].innerHTML);");
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("shouldOverrideUrlLoading: " + url);
//                if (null != url && url.startsWith("tel:")) {
//                    Intent intent = new Intent();
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.setAction(Intent.ACTION_DIAL);
//                    intent.setData(Uri.parse(url));
//                    try {
//                        startActivity(intent);
//                        return true;
//                    } catch (ActivityNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//                return false;
                if (url.startsWith("data:")) {
                    url = "https://www.amazon.com" + url.substring(5);
                }

                if (url.startsWith("https://www.amazon.com/gp/aw/s/ref=is_")) {
                    if (!interception) {
                        return false;
                    }

                    System.out.println(url);
                    OkHttpUtils.getAsync(url, new OkHttpUtils.ICallback() {
                        @Override
                        public void onSuccess(String result) {
                            System.out.println("onSuccess");
                            dealSearchResult(result);
                        }

                        @Override
                        public void onFailure(int errorCode, String errorDetail) {
                            System.out.println("onFailure");
                        }
                    });

                    return true;
                }

                return false;
            }
        };
        mDownloadListener = new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {

            }

        };
    }

    private void initWebViewSettings(WebView webView) {
        webView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.requestFocusFromTouch();

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        webSettings.setPluginsEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        try {
            Class<?> classPluginState = Class
                    .forName("android.webkit.WebSettings$PluginState");
            Method method = WebSettings.class.getDeclaredMethod(
                    "setPluginState", classPluginState);
            if (null != method) {
                method.invoke(webSettings,
                        classPluginState.getEnumConstants()[0]);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAppCacheEnabled(false);

        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");

        webView.setWebChromeClient(mWebChromeClient);
        webView.setWebViewClient(mWebViewClient);
        webView.setDownloadListener(mDownloadListener);
    }

    private void clearWebView(WebView webView) {
        if (webView != null) {
            mWebView.setWebChromeClient(null);
            mWebView.clearFormData();
            webView.destroy();
            webView = null;
        }
    }

    private void openUrl(String url) {
        mWebView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearWebView(mWebView);
        mDefaultHandler.removeCallbacksAndMessages(null);
        DataCleanManager.clearAllCache(getApplicationContext());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setProgressBarGone() {
        mDefaultHandler.post(new Runnable() {
            public void run() {
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void getSource(String html) {
            Log.d("html=", html);
            final StringBuilder sb = new StringBuilder();
            sb.append("<html>").append(html).append("</html>");
            dealSearchResult(sb.toString());
//            mWebView.loadUrl("javascript:document.getElementsByTagName('html')[0].innerHTML='haha'");
//            mWebView.loadUrl("javascript:window.java_obj.changedSource(" +
//                    "document.getElementsByTagName('html')[0].innerHTML='haha');");
//            mWebView.loadUrl("javascript:window.java_obj.changedSource("+"'haha');");

//            mWebView.loadUrl("javascript:window.java_obj.changedSource("+"'haha');");
        }

        @JavascriptInterface
        public void changedSource(String html) {
            Log.d("html=", html);
        }

        @JavascriptInterface
        public void addToCartReady() {
            System.out.println("addToCartReady");
        }
    }

    private void dealSearchResult(final String result) {
        final String target = "</body>";
        final String replace = "<script type=\"text/javascript\">\n" +
                "    function clickByValue(val) {\n" +
//                                    "        alert(val);\n" +
                "        var arr = document.getElementsByTagName('a');\n" +
//                                    "        alert('length' + arr.length);\n" +
                "        if (arr.length > 0) {\n" +
                "            for (var i = 0; i < arr.length; ++i) {\n" +
//                                    "                alert(arr[i].innerHTML);\n" +
                "                if (arr[i].innerHTML == val) {\n" +
                "var href = arr[i].href;\n" +
//                "                    alert(href);\n" +
                "                    if (href.length > 5) {\n" +
                "                        href = 'https://www.amazon.com' + href.substring(5);\n" +
                "                    }\n" +
//                "                    alert(href);\n" +
                "                    window.location.href = href;" +
                "                    break;\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                /*
                "function clickByDataAsinId(dataAsinId) {\n" +
                "        alert(dataAsinId);\n" +
                "        var arr = document.getElementsByTagName('a');\n" +
                "        alert('length' + arr.length);\n" +
                "        if (arr.length > 0) {\n" +
                "            for (var i = 0; i < arr.length; ++i) {\n" +
                "                alert('index' + i);"+
                "                alert(arr[i].data('asin'));\n" +
                "                if (arr[i].data('asin') == dataAsinId) {\n" +
                "                    alert('index' + i);\n" +
                "                    var href = arr[i].href;\n" +
                "                    alert(href);\n" +
                "                    if (href.length > 5) {\n" +
                "                        href = 'https://www.amazon.com' + href.substring(5);\n" +
                "                    }\n" +
                "                    alert(href);\n" +
                "                    window.location.href = href;\n" +
                "                    break;\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }"+
                */
                /*
                "function clickByDataAsinId(dataAsinId) {\n" +
                "        alert(dataAsinId);\n" +
                "        var arr = document.getElementsByTagName('a');\n" +
//                "        alert('length' + arr.length);\n" +
                "        if (arr.length > 0) {\n" +
                "            for (var i = 0; i < arr.length; ++i) {\n" +
                "                alert('index' + i);\n" +
                "                var href = arr[i].href;\n" +
                "                if (href) {\n" +
//                "                    alert(href);\n" +
                "                    if (href.indexOf(dataAsinId) > 0) {\n" +
                "                        if (href.length > 5 && href.indexOf('http') != 0) {\n" +
                "                            href = 'https://www.amazon.com' + href.substring(5);\n" +
                "                        }\n" +
                "                        alert(href);\n" +
                "                        window.location.href = href;\n" +
                "                        break;\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }" +
                */
                "function clickByDataAsinId(dataAsinId) {\n" +
                /*
                "var btn = document.getElementById('add-to-cart-button');\n" +
//                "alert('find');\n"+
                "        if (btn) {\n" +
//                "            alert('todo click');\n" +
                "            btn.click();\n" +
                "            alert('click');\n" +
                "\n" +
                "            return;\n" +
                "        }\n" +
                */
                "        var formTmp = document.getElementById('addToCart');\n" +
                "        if (formTmp) {\n" +
                "            alert('todo submit');\n" +
                "            var actionTmp = formTmp.getAttribute('action');\n" +
                "            alert(actionTmp);\n" +
                "            if (actionTmp.indexOf('http') != 0) {actionTmp = 'https://www.amazon.com' + actionTmp;}\n" +
                "            formTmp.setAttribute('action', actionTmp); " +
                "            formTmp.submit();\n" +
//                "            alert('submit');\n" +
                "            window.java_obj.addToCartReady();\n" +
                "\n" +
                "            return;\n" +
                "        }\n" +
//                "        alert(dataAsinId);\n" +
                "var findIt = false;\n" +
                "        var arr = document.getElementsByClassName('a-spacing-none a-link-normal sx-table-product sx-detail-page-link aw-search-results');\n" +
//                "        alert('length' + arr.length);\n" +
                "        if (arr.length > 0) {\n" +
                "            for (var i = 0; i < arr.length; ++i) {\n" +
//                "                alert('index' + i);\n" +
                "                var item = arr[i];\n" +
                "                var tmp = item.getAttribute('data-asin');\n" +
                "                if (tmp) {\n" +
//                "                alert(tmp);\n" +
                "                    if (tmp == dataAsinId) {\n" +
                "                        var href = item.getAttribute('href');\n" +
//                "                        alert(href);\n" +
                "                        if (href) {\n" +
                "                           if (href.indexOf('http') != 0) {\n" +
                "                               if (href.indexOf('data:') == 0) {\n" +
                "                                   href = 'https://www.amazon.com' + href.substring(5);\n" +
                "                               } else {\n" +
                "                                   href = 'https://www.amazon.com' + href;\n" +
                "                               }\n" +
                "                        }" +
                "findIt = true;\n" +
                "                            window.location.href = href;\n" +
                "                            break;\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "if(!findIt){clickByValue('Next →');}\n" +
                "    }\n" +

                "</script>\n" +
                "</body>";
        final String dealResult = result.replace(target, replace);

//                            System.out.println(result);
        mDefaultHandler.removeCallbacks(reloadRunnable);
        reloadRunnable = new Runnable() {
            @Override
            public void run() {
                mWebView.loadData(dealResult, "text/html; charset=UTF-8", null);    // 解决乱码问题
                mDefaultHandler.removeCallbacks(runnable);
                runnable = new Runnable() {
                    @Override
                    public void run() {
//                        final String dataAsinId = "B00MBVD9I6"; // 第四页的数据
//                        final String dataAsinId = "B0169EXN2M"; // 第一页的数据
                        final String dataAsinId = "B01H6OWJGA";
                        mWebView.loadUrl("javascript:clickByDataAsinId('" + dataAsinId + "')");


//                        mWebView.loadUrl("javascript:addToCart()");
                        /*
                        final String value = "Next →";
//                        final String value = "Next";
                        mWebView.loadUrl("javascript:clickByValue('" + value + "')");
                        */
                    }
                };
                mDefaultHandler.postDelayed(runnable, 15000);
            }
        };
        mDefaultHandler.post(reloadRunnable);
    }
}

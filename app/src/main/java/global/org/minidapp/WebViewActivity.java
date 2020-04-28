package global.org.minidapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {

    WebView mWebview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview);

        // set ref
        mWebview = findViewById(R.id.webview);
        // Load MINIDAPP WebView
        startWebView("http://127.0.0.1:21000");

    }

    // Load as WebView
    private void startWebView(String url) {

//        var webSettings = webView.Settings;
//        webSettings.JavaScriptEnabled = true;
//        webSettings.DomStorageEnabled = true;
//        webSettings.LoadWithOverviewMode = true;
//        webSettings.UseWideViewPort = true;
//        webSettings.BuiltInZoomControls = true;
//        webSettings.DisplayZoomControls = false;
//        webSettings.SetSupportZoom(true);
//        webSettings.DefaultTextEncodingName = "utf-8";
        mWebview.getSettings().setDomStorageEnabled(true);
        mWebview.getSettings().setDefaultTextEncodingName("utf-8");
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.getSettings().setUseWideViewPort(true);
        mWebview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebview.setScrollbarFadingEnabled(false);
        mWebview.getSettings().setSupportZoom(true);
        mWebview.loadUrl(url);

    }

}

package com.bimahelpline.starhealth;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bimahelpline.starhealth.custom.ConnectionDetector;

public class News extends AppCompatActivity implements ConnectionDetector.ConnectivityReceiverListener  {

    private ProgressBar mProgressBar;
    private WebView mWebView;
    private FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        mWebView = (WebView)findViewById(R.id.main_webview);
        mFrameLayout = (FrameLayout)findViewById(R.id.framelayout1);
        checkConnection();
        mWebView.setWebViewClient(new myWebClient());
        mWebView.getSettings().setJavaScriptEnabled(true);

    }

    private void mloadUrl(){
        mWebView.loadUrl("http://www.bimahelpline.com");
    }

    @Override
    public void onBackPressed() {
        if (mWebView != null) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                super.onBackPressed();
            }
        }
    }

    private void checkConnection() {
        boolean isConnected = ConnectionDetector.isConnected();
        showSnack(isConnected);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
            mWebView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            mFrameLayout.setVisibility(View.GONE);
            mloadUrl();
        } else {
            message = "Sorry ! Not Connected to Internet";
            color = Color.RED;
            mWebView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            mFrameLayout.setVisibility(View.VISIBLE);
        }
        RelativeLayout activity_news = (RelativeLayout) findViewById(R.id.activity_news);
        Snackbar snackbar = Snackbar.make(activity_news, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApp.getInstance().setConnectivityListener(this);
    }

    private class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            mProgressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            mProgressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;
        }
    }
}

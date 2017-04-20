package com.bimahelpline.starhealth;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bimahelpline.starhealth.custom.ConnectionDetector;

public class Hospital extends AppCompatActivity implements ConnectionDetector.ConnectivityReceiverListener {

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        mWebView = (WebView) findViewById(R.id.main_webview);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mFrameLayout = (FrameLayout) findViewById(R.id.framelayout1);
        mWebView.setWebViewClient(new Hospital.myWebClient());
        checkConnection();
        mWebView.getSettings().setJavaScriptEnabled(true);

    }

    private void mloadUrl() {
        mWebView.loadUrl("http://www.starhealth.in/network-hospitals");
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
        RelativeLayout activity_hospital = (RelativeLayout) findViewById(R.id.activity_hospital);
        Snackbar snackbar = Snackbar.make(activity_hospital, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
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

    @Override
    protected void onResume() {
        super.onResume();
        MyApp.getInstance().setConnectivityListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

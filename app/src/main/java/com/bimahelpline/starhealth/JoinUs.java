package com.bimahelpline.starhealth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
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

public class JoinUs extends AppCompatActivity implements ConnectionDetector.ConnectivityReceiverListener {

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private TextView mShareButton;
    private FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_us);

        mWebView = (WebView) findViewById(R.id.main_webview);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mFrameLayout = (FrameLayout)findViewById(R.id.framelayout1);
        mShareButton = (TextView)findViewById(R.id.shareButton);
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setData(Uri.parse("market://details?id=com.bimahelpline.starhealth"));
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        '\n'+ "Hi Friends," +
                                "\n"+
                                '\n'+"I found this insurance premium calculator app very useful, i think you may like it too."+
                                '\n'+
                                '\n'+"Using this App you can :"+
                                '\n'+"1. Generate the quote for health insurance"+
                                "\n"+"2. Find network hospital." +
                                '\n'+"3. View our plans." +
                                "\n" + "You can download this wonderful app for free from GooglePlay Store." +
                                '\n'+
                                '\n'+"powered by http://www.bimahelpline.com - "+
                                "https://play.google.com/store/apps/details?id=com.bimahelpline.starhealth");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share with ..."));
            }
        });
        checkConnection();
        mWebView.setWebViewClient(new myWebClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
    }

    private void mloadUrl(){
        mWebView.loadUrl("https://docs.google.com/document/d/1JdYZ5KN6DzCZbPovx5Tu3boTa2dbC2qZEz7fiCE5PQE/edit?usp=sharing");
    }

    private class myWebClient extends WebViewClient
    {
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
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if((keyCode == KeyEvent.KEYCODE_BACK)&& mWebView.canGoBack()){
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
            mShareButton.setVisibility(View.VISIBLE);
            mloadUrl();
        } else {
            message = "Sorry ! Not Connected to Internet";
            color = Color.RED;
            mWebView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            mFrameLayout.setVisibility(View.VISIBLE);
            mShareButton.setVisibility(View.GONE);
        }
        RelativeLayout activity_joinus = (RelativeLayout) findViewById(R.id.activity_join_us);
        Snackbar snackbar = Snackbar.make(activity_joinus, message, Snackbar.LENGTH_LONG);
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
}

package com.harrisonmcguire.recyclerview;

/**
 * Created by Harrison on 5/24/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class WebViewActivity extends FragmentActivity {

    //set up webview variables
    private WebView mWebview ;
    private static final String urlTag = "url";

    //Launch webview as intent
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // getting intent data
        Intent intent = getIntent();

        // Get JSON values from previous intent
        String postUrl = intent.getStringExtra(urlTag);

        mWebview  = new WebView(this);
        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

        final Activity activity = this;

        //function keeps the browser inside the app so the user doesn't
        //have to switch between applications
        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
        });

        //load selected url
        mWebview.loadUrl(postUrl);
        setContentView(mWebview);

    }
}
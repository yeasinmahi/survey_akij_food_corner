package com.devfolder.yeasin.survey;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    //start internet connection dialogue

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CreateWebViewClient();
        MobileAds.initialize(this, getString(R.string.app_id));
        AdView adView = findViewById(R.id.adView);
        AddLoader.loadBannerAd(adView);
    }



    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != cm) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            return (info != null && info.isConnected());
        }
        return false;
    }

    public void CreateWebViewClient() {
        WebViewClient mWebClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (NetworkUtility.isConnected(MainActivity.this)) {
                    view.loadUrl(url);
                    if (true) {
                        InterstitialAd interstitial = new InterstitialAd(MainActivity.this);
                        interstitial.setAdUnitId(getString(R.string.interstitial_id));
                        AddLoader.loadInterstialAd(interstitial);
                    }
                } else {
                    buildDialog(MainActivity.this).show();
                }
                return true;
            }

            @Override
            public void onLoadResource(WebView view, String url) {

            }
        };
        //start checking internet connection
        if (!NetworkUtility.isConnected(this)) {
            buildDialog(MainActivity.this).show();
        } else {
            Toast.makeText(MainActivity.this, "Welcome To Food Survey !!", Toast.LENGTH_SHORT).show();
            // setContentView(R.layout.activity_main);
            webView = (WebView) findViewById(R.id.webView);


            webView.setWebViewClient(mWebClient);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient(){
                @Override
                public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                    AlertDialog dialog = new AlertDialog.Builder(view.getContext()).
                            setTitle("Feedback").
                            setMessage(message).
                            setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //do nothing
                                }
                            }).create();
                    dialog.show();
                    result.confirm();
                    return true;
                }
            });
            webView.loadUrl("https://survey.akij.net/Feedback-fc.html");
        }
        //end checking internet connection
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or WiFi to access this app.");

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                CreateWebViewClient();
            }

        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                CreateWebViewClient();
            }
        });
        return builder;
    }
    //end internet connection dialogue
}
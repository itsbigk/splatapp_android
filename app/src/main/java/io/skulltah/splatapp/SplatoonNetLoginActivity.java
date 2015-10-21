package io.skulltah.splatapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class SplatoonNetLoginActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splatoon_net_login);

        webView = (WebView) findViewById(R.id.splatoonnet_webview);

        setupWebview();

        AlertDialog alertDialog = new AlertDialog.Builder(SplatoonNetLoginActivity.this).create();
        alertDialog.setTitle("Please log in");
        alertDialog.setMessage("You will now be asked to login. \nPlease use your Nintendo ID.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void setupWebview(){
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new SplatoonNetJavaScriptInterface(this), "HtmlViewer");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                findViewById(R.id.loading_view).setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(webView, url);

                findViewById(R.id.loading_view).setVisibility(View.GONE);

                webView.loadUrl("javascript:window.HtmlViewer.showHTML" +
                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Snackbar.make(findViewById(R.id.login_layout), "Loading...", Snackbar.LENGTH_LONG);
            }
        });

        CookieManager.getInstance().setAcceptCookie(true);

        webView.loadUrl("https://splatoon.nintendo.net/sign_in");
    }

    public class SplatoonNetJavaScriptInterface {

        private Context ctx;

        SplatoonNetJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @android.webkit.JavascriptInterface
        public void showHTML(String html) {
            SplatoonNet.Nnid nnid = new SplatoonNet.Nnid(html);
            if(nnid.miiName!=null && !nnid.miiName.equals("")){
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                prefs.edit().putString("mii_name", nnid.miiName).apply();
                prefs.edit().putString("mii_icon", nnid.iconUrl).apply();

                AlertDialog alertDialog = new AlertDialog.Builder(SplatoonNetLoginActivity.this).create();
                alertDialog.setTitle("Welcome, " + nnid.miiName);
                alertDialog.setMessage("Enjoy the app!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Done",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                finish();
                            }
                        });
                alertDialog.show();
            }
        }
    }
}

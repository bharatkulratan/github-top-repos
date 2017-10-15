package info.techienotes.toprepos;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import info.techienotes.toprepos.utils.Constants;
import info.techienotes.toprepos.utils.TypefaceSpan;

/**
 * Created by bharatkulratan
 */

public class BrowserActivity extends AppCompatActivity{

    private boolean loadingFinished = true;
    private boolean redirect = false;

    private String urlToOpen;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_browser);

        Tracker gaTracker = ((GithubApp) getApplication()).getDefaultTracker();
        gaTracker.setScreenName("BrowserActivity");
        gaTracker.send(new HitBuilders.ScreenViewBuilder().build());

        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey(Constants.URL)) {
            urlToOpen = bundle.getString(Constants.URL);
        }

        String title = getResources().getString(R.string.default_title_browser);

        if (bundle.containsKey(Constants.TITLE)){
            title = bundle.getString(Constants.TITLE);
        }

        setUpActionBar(title);

        findViewById(R.id.loader).setVisibility(View.GONE);
        findViewById(R.id.web_view).setVisibility(View.VISIBLE);

        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setBuiltInZoomControls(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                loadingFinished = false;
                findViewById(R.id.loader).setVisibility(View.VISIBLE);
                findViewById(R.id.web_view).setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!loadingFinished) {
                    redirect = true;
                }
                loadingFinished = false;
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!redirect) {
                    loadingFinished = true;
                }

                if (loadingFinished && !redirect) {
                    findViewById(R.id.loader).setVisibility(View.GONE);
                    findViewById(R.id.web_view).setVisibility(View.VISIBLE);

                } else {
                    redirect = false;
                }
            }

            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {}
        });

        webView.loadUrl(urlToOpen);
    }

    private void setUpActionBar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SpannableString s = new SpannableString(title);
        s.setSpan(new TypefaceSpan(this, "OpenSans-Bold.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(s);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

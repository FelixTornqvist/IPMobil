package com.example.a412weblinker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Main Activity for task 4.1.2, a simple list with links that open in a WebView below.
 */
public class MainActivity extends AppCompatActivity {
    private String[] websites = new String[]{
            "https://www.google.com",
            "https://www.youtube.com",
            "https://www.dsv.su.se",
            "https://www.su.se",
            "https://www.aftonbladet.se"
    };
    ListView websitesListV;
    ArrayAdapter websitesArrAdapter;
    WebView webView;

    /**
     * Initializes a ListView with the links, with a listener attatched as well.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        websitesListV = findViewById(R.id.list_main_websites);
        websitesArrAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, websites);
        websitesListV.setAdapter(websitesArrAdapter);

        websitesListV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openWebSite(websites[i]);
            }
        });

        setupWebView();
        openWebSite(websites[0]);
    }

    /**
     * Sets up the WebView with correct settings
     */
    private void setupWebView() {
        webView = findViewById(R.id.web_main);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
    }

    /**
     * Opens specified website in the WebView
     *
     * @param link link to website to open
     */
    private void openWebSite(String link) {
        Toast.makeText(this, "opening " + link, Toast.LENGTH_SHORT).show();
        webView.loadUrl(link);
    }
}

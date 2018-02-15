package com.example.gesllprovnumberchecker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NumberSearchActivity extends AppCompatActivity {
    public static final String EXTRA_SEARCH_FOR_NUMBER = "search_for_number";

    private WebView webSearch;
    private TextView callerTW;
    private Button copyNoBTN;
    private String numToCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webSearch = findViewById(R.id.web_main_number_search);
        callerTW = findViewById(R.id.text_main_caller);
        copyNoBTN = findViewById(R.id.button_main_copy_number);

        setupWebView();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_SEARCH_FOR_NUMBER)) {
            setupForNumber(intent.getStringExtra(EXTRA_SEARCH_FOR_NUMBER));
        } else {
            Toast.makeText(this, "Error: no intent with number provided", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /**
     * Sets everything up so that they relate to a phone number.
     * @param num Phone number to relate the view to.
     */
    private void setupForNumber(String num) {
        callerTW.setText(getString(R.string.main_caller_number, num));
        webSearch.loadUrl("https://www.hitta.se/vem-ringde/" + num);
        numToCopy = num;
        copyNoBTN.setEnabled(true);
    }

    /**
     * Sets up the WebView with correct settings
     */
    private void setupWebView() {
        webSearch.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webSearch.getSettings();

        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
    }

    /**
     * Activates when the copy button have been pressed, copies the phone number that called to the clipboard.
     * @param v not used, required to register listener in layout xml file.
     */
    public void onCopyBtnListener(View v) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simple text", numToCopy);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, R.string.main_number_copied, Toast.LENGTH_SHORT).show();
    }
}

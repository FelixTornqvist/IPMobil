package com.example.uppgift711copypaster;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText toCopyET;
    private TextView pastedTW;
    private ClipboardManager clipboard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toCopyET = findViewById(R.id.edit_main_tocopy);
        pastedTW = findViewById(R.id.text_main_pasted_text);

        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    }

    /**
     * Paste from clipboard when the app starts/resumes.
     */
    @Override
    public void onResume() {
        super.onResume();
        pasteBtnListener(null);
    }

    /**
     * Listener for the "copy" button. Copies the text in toCopyET to the clipboard.
     *
     * @param v not used, required to register listener in layout file.
     */
    public void copyBtnListener(View v) {
        String toCopyStr = toCopyET.getText().toString();
        ClipData clip = ClipData.newPlainText("simple text", toCopyStr);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "copied!", Toast.LENGTH_SHORT).show();
    }


    /**
     * Listener for the "paste" button. Pastes the content in clipboard to pastedTW.
     *
     * @param v not used, required to register listener in layout file.
     */
    public void pasteBtnListener(View v) {
        if (clipboard.hasPrimaryClip()) {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            CharSequence pasted = item.coerceToText(this);

            if (pasted != null && pasted.length() > 0)
                pastedTW.setText(pasted);
            else
                Toast.makeText(this, "clipboard contents empty or unsupported", Toast.LENGTH_LONG).show();
        }
    }
}

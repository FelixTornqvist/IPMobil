package com.example.uppgift711copypaster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText toCopyET;
    TextView pastedTW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toCopyET = findViewById(R.id.edit_main_tocopy);
        pastedTW = findViewById(R.id.text_main_pasted_text);
    }

    /**
     * Listener for the "copy" button. Copies the text in toCopyET to the clipboard.
     * @param v not used, required to register listener in layout file.
     */
    public void copyBtnListener(View v) {

    }


    /**
     * Listener for the "paste" button. Pastes the content in clipboard to pastedTW.
     * @param v not used, required to register listener in layout file.
     */
    public void pasteBtnListener(View v) {

    }
}

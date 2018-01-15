package com.example.uppgift32audiostore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RecordActivity extends AppCompatActivity {
    Button recordB, stopB;
    TextView filenameTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        recordB = findViewById(R.id.action_record);
        stopB = findViewById(R.id.action_stop);
        filenameTxt = findViewById(R.id.text_filename);

        stopB.setEnabled(false);

        setRecBtnListener();
        setStopBtnListener();
    }

    private void setStopBtnListener() {
        stopB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setRecBtnListener() {
        recordB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
    }
}

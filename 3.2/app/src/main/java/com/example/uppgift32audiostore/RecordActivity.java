package com.example.uppgift32audiostore;

import android.content.Intent;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class RecordActivity extends AppCompatActivity {
    public static final String EXTRA_SOUND_FILE = "rec_file";
    private File outputFile;
    MediaRecorder myAudioRecorder;

    private Button recordB, stopB;
    private TextView filenameTxt, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        recordB = findViewById(R.id.button_recordAct_record);
        stopB = findViewById(R.id.button_recordAct_stop);
        filenameTxt = findViewById(R.id.text_filename);
        title = findViewById(R.id.text_title_recording);

        stopB.setEnabled(false);

        Intent intent = getIntent();
        if (intent != null) {
            outputFile = (File) intent.getSerializableExtra(EXTRA_SOUND_FILE);
            filenameTxt.setText(outputFile.getName());
        }

        if (outputFile == null)
            throw new IllegalStateException("intent with EXTRA_SOUND_FILE path required");

        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile.getPath());
    }

    public void startRecordingListener(View v) {
        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        } catch (IllegalStateException ise) {
            Toast.makeText(this, "failed to record", Toast.LENGTH_LONG).show();
        } catch (IOException ioe) {
            Toast.makeText(this, "failed to record", Toast.LENGTH_LONG).show();
        }

        stopB.setEnabled(true);
        recordB.setEnabled(false);
        title.setText(R.string.recording_title);
    }

    public void stopRecordingListener(View v) {
        stopRecording();
        Toast.makeText(this, "saved recording", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void stopRecording() {
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;

        stopB.setEnabled(false);
        recordB.setEnabled(true);
        title.setText(R.string.record_title);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (myAudioRecorder != null) {
            stopRecording();
        }
    }
}

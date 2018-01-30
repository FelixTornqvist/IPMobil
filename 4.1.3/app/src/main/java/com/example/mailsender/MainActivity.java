package com.example.mailsender;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int RESULT_PICK_FILE = 1;
    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 2;
    private static final String FILES_AUTHORITY = "com.example.mailsender.fileprovider";

    EditText recipientET, subjectET, messageET;
    TextView chosenFileTW;
    ImageButton deleteTmpFileBTN;

    File tmpFile;

    /**
     * Sets the attachment to the one that was last used (it is stored in getCacheDir())
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recipientET = findViewById(R.id.edit_main_recipient_addr);
        subjectET = findViewById(R.id.edit_main_subject);
        messageET = findViewById(R.id.edit_main_message);

        chosenFileTW = findViewById(R.id.text_main_attached_file);
        deleteTmpFileBTN = findViewById(R.id.button_main_delete_attached_file);

        File tmpDir = getCacheDir();
        File[] fileList = tmpDir.listFiles();
        if (fileList.length > 0) {
            tmpFile = fileList[0];
            chosenFileTW.setText(tmpFile.getName());
            deleteTmpFileBTN.setVisibility(ImageButton.VISIBLE);
        }
    }

    /**
     * Listener for the attach file button, opens an file picker activity.
     *
     * @param v unused, but required to be able to register this method in layout file.
     */
    public void choseFileBtnListener(View v) {
        if (!requestPermissionIfNone(Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSION_REQUEST_READ_EXTERNAL_STORAGE)) {
            openFileChooserAct();
        }
    }

    /**
     * Opens activity for picking a file
     */
    private void openFileChooserAct() {
        deleteTmpFile();

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, RESULT_PICK_FILE);
    }

    /**
     * Listener for the send mail button, opens a mail activity pre-filled with recipient,
     * subject and message.
     *
     * @param v unused, but required to be able to register this method in layout file.
     */
    public void sendBtnListener(View v) {
        String recipient = recipientET.getText().toString();
        String subject = subjectET.getText().toString();
        String message = messageET.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setData(Uri.parse("mailto:" + recipient));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> resInfoList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        if (tmpFile != null) {
            Uri fileUri = FileProvider.getUriForFile(this, FILES_AUTHORITY, tmpFile);

            intent.putExtra(Intent.EXTRA_STREAM, fileUri);

            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }

        if (resInfoList.size() > 0) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No email client found!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Receives the result from the file-chooser activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_PICK_FILE:
                Uri chosenFile = data.getData();
                String fileName = getFileName(chosenFile);
                chosenFileTW.setText(fileName);
                deleteTmpFileBTN.setVisibility(ImageButton.VISIBLE);

                File outDir = getCacheDir();
                try {
                    tmpFile = new File(outDir, fileName);

                    InputStream is = getContentResolver().openInputStream(chosenFile);
                    byte[] buffer = new byte[is.available()];
                    is.read(buffer);
                    is.close();

                    OutputStream outStream = new FileOutputStream(tmpFile);
                    outStream.write(buffer);
                    outStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    /**
     * Gets filename for specified file Uri
     * @param uri Uri to get filename for (assumes that it's a content:// uri)
     * @return String with filename
     */
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        return result;
    }


    /**
     * Listener for the button that deletes the attached file.
     * @param v unused, but required to be able to register this method in layout file.
     */
    public void deleteTmpFile(View v) {
        deleteTmpFile();
    }

    /**
     * Deletes all files in the getCacheDir() directory and clears attachment filename text.
     */
    private void deleteTmpFile() {
        for (File file : getCacheDir().listFiles())
            file.delete();
        tmpFile = null;
        chosenFileTW.setText("");
        deleteTmpFileBTN.setVisibility(ImageButton.INVISIBLE);
    }

    /**
     * Makes sure that a permission is granted by the user.
     *
     * @param permission  permission to grant.
     * @param requestCode this int code is received in onRequestPermissionsResult() if the permission had to be requested.
     * @return true if the permission have been requested, false if permission already granted.
     */
    private boolean requestPermissionIfNone(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            return true;
        }
        return false;
    }

    /**
     * Deals with what should happen after the user have answered the permission question.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_READ_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    openFileChooserAct();
                } else {
                    Toast.makeText(this, "Storage permission required to open a file", Toast.LENGTH_LONG).show();
                }
                break;


        }
    }
}

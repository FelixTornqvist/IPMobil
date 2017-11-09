package com.example.uppgift12;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        String[] listItems = {"Hello", "world!", "this", "is ", "a", "test", "press the plus-button to activate dialog"};
        ListView todoList = (ListView) findViewById(R.id.todo_list);
        todoList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems));
    }

    /**
     * Called when the floating action button is pressed
     */
    public void fabClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        builder.setMessage("To be continued...");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

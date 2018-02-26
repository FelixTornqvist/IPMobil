package com.example.uppgift411YoutubeLinker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Main Activity for task 4.1.1, an app with links to YouTube videos.
 */
public class MainActivity extends AppCompatActivity {
    String[] ytLinks = new String[]{
            "https://www.youtube.com/watch?v=II3AXSpwsMk",
            "https://www.youtube.com/watch?v=m8KzmlIEsHs",
            "https://www.youtube.com/watch?v=xe-f4gokRBs",
            "https://www.youtube.com/watch?v=VS8wlS9hF8E",
            "https://www.youtube.com/watch?v=rsXQInxxzBU",
            "https://www.youtube.com/watch?v=r6Rp-uo6HmI",
            "https://www.youtube.com/watch?v=iYx10BWoL58"
    };
    ListView ytListView;
    ArrayAdapter ytLinkAdapter;

    /**
     * Initializes a ListView with the links, with a listener attached as well.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ytLinkAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ytLinks);
        ytListView = findViewById(R.id.list_main_linkList);

        ytListView.setAdapter(ytLinkAdapter);
        ytListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openYTLink(ytLinks[i]);
            }
        });
    }

    /**
     * Opens specified link in YouTube app or gives the user an error if the youtube app was unavailable.
     * @param link YouTube link to open
     */
    private void openYTLink(String link) {
        Uri uri = Uri.parse(link);
        uri = Uri.parse("vnd.youtube:" + uri.getQueryParameter("v"));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        PackageManager packageManager = getPackageManager();
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No application for Youtube was found :'(", Toast.LENGTH_LONG).show();
        }
    }
}

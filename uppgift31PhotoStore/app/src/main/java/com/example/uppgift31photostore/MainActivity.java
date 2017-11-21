package com.example.uppgift31photostore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements PhotoRecyclerViewAdapter.ItemClickListener {
    RecyclerView photosRecycler;
    String[] tmp = {"hello", "world", "!", "the", "story", "of", "the", "ape", "that", "were", "to", "be", "a", "adult", "banana", "which", "were", "to", "be", "eaten", "by", "a", "fully", "grown", "female", "lion", "tiger", "hybrid", "car", "tesla", "is", "better", "then", "hybrids", "as", "they", "only", "run", "on", "electric", "power"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        PhotoRecyclerViewAdapter photosGridAdapter = new PhotoRecyclerViewAdapter(this, tmp);
        photosGridAdapter.setClickListener(this);

        photosRecycler = findViewById(R.id.photos_grid);
        int numberOfColumns = 2;
        photosRecycler.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        photosRecycler.setAdapter(photosGridAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, tmp[position], Toast.LENGTH_SHORT).show();
    }
}

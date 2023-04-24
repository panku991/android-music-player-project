package com.example.melody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class Queue extends AppCompatActivity {

    RecyclerView recyclerView_queue;
    ArrayList<Music_Model> arrmusic;
    ImageView go_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);


        recyclerView_queue=findViewById(R.id.recyclerView_queue);
        go_back=findViewById(R.id.go_back);

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Queue.this.finish();
            }
        });

        arrmusic=MainActivity.musicFiles;

        Queue_adapter music_adapter = new Queue_adapter(this,arrmusic);
        recyclerView_queue.setAdapter(music_adapter);
        recyclerView_queue.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));


    }


}
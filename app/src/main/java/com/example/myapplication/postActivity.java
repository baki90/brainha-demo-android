package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

public class postActivity extends AppCompatActivity {

    List<comment> list;
    ListView listView;
    commentAdapter listAdapter;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        listView = (ListView)findViewById(R.id.quest_listview);
        imageView = (ImageView) findViewById(R.id.imageView6);
        imageView.setImageResource(R.drawable.user);

        list = new LinkedList<>();
        list.add(new comment(12171835, "이렇게 하세요!"));
        list.add(new comment(12171835, "이건 어떤가요?"));
        listAdapter = new commentAdapter(this, list);

        listView.setAdapter(listAdapter);




    }
}
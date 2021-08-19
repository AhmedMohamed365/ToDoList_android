package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

public class work extends AppCompatActivity {
    RecyclerView recyclerView ;
    RecyclerView.Adapter adapter ;
    RecyclerView.LayoutManager layoutManager ;
    int done =R.drawable.ic_baseline_done_24;
    int edit =R.drawable.ic_baseline_edit_24;
    int delete =R.drawable.ic_baseline_delete_24;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_work);
        ArrayList<taskshow> tasks= new ArrayList<>();
        tasks.add(new taskshow("FARH","22/8/2021",done,edit,delete));
        tasks.add(new taskshow("FARH","22/8/2021",done,edit,delete));
        tasks.add(new taskshow("FARH","22/8/2021",done,edit,delete));
        Button ADD = findViewById(R.id.ADD) ;
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ViewHandler(tasks);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        ADD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(work.this, AddActivity.class);
                intent.putExtra("type","work");
                startActivity(intent);
            }
        });
    }

}
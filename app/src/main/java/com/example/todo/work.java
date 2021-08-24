package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class work extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    int done = R.drawable.ic_baseline_done_24;
    int edit = R.drawable.ic_baseline_edit_24;
    int delete = R.drawable.ic_baseline_delete_24;
Button addBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        recyclerView = findViewById(R.id.recyclerView);
        addBt = findViewById(R.id.addBt);
        recyclerView.setHasFixedSize(true);
        ArrayList<taskshow> tasks = new ArrayList<>();
        MyDatabaseHelper DB = new MyDatabaseHelper(this);
        DB.addTask("AHMED", "WORK", 25, "work", "","");
        DB.addTask("AHMED", "WORK", 25, "work", "","");
        DB.addTask("AHMED", "WORK", 25, "work", "","");
        DB.addTask("AHMED", "WORK", 25, "work", "","");
        DB.addTask("AHMED", "WORK", 25, "work", "","");
        DB.addTask("AHMED", "WORK", 25, "work", "","");
        DB.addTask("AHMED", "WORK", 25, "work", "","");
        DB.addTask("AHMED", "WORK", 25, "work", "","");
        DB.addTask("AHMED", "WORK", 25, "work", "","");
        DB.addTask("AHMED", "WORK", 25, "work", "","");
        ArrayList<String> theList = new ArrayList<>();
        Cursor data = DB.getListContents();
        if (data.getCount() == 0) {
            Toast.makeText(this, "There are no contents in this list!", Toast.LENGTH_LONG).show();
        }
        else
        {
            while (data.moveToNext()) {
                if (data.getString(3).equals("work")) {
                    tasks.add(new taskshow(data.getString(1),data.getString(2) ,done,edit,delete));
                    layoutManager = new LinearLayoutManager(this);
                    adapter = new ViewHandler(tasks);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                }
            }
        }

        //handle add bt
        addBt.setOnClickListener(view ->
                {
                    Intent intent  = new Intent(getBaseContext(), AddActivity.class );
                    startActivity(intent);
                }
                                );
    }
}
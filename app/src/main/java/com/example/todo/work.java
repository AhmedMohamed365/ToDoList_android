package com.example.todo;

import static com.example.todo.MainActivity.whichActivity;
import  com.example.todo.MainActivity.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class work extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showtasks);
        TextView workLabel  = findViewById(R.id.label);
        workLabel.setText(whichActivity);
        loadData();
    }
    public void loadData()
    {
        RecyclerView recyclerView;
        RecyclerView.Adapter adapter;
        int done = R.drawable.ic_baseline_done_24;
        int edit = R.drawable.ic_baseline_edit_24;
        int delete = R.drawable.ic_baseline_delete_24;
        RecyclerView.LayoutManager layoutManager;
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        ArrayList<taskshow> tasks = new ArrayList<>();
        MyDatabaseHelper DB = new MyDatabaseHelper(this);
        Cursor data = DB.getListContents();
        if (data.getCount() == 0) {
            Toast.makeText(this, "There are no contents in this list!", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                if (data.getString(3).equals(whichActivity.toLowerCase())) {
                    tasks.add(new taskshow(data.getString(1),data.getString(2) ,done,edit,delete));
                    layoutManager = new LinearLayoutManager(this);
                    adapter = new ViewHandler(tasks);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                }
            }
        }
    }
}
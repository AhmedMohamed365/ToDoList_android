package com.example.todo;
import static com.example.todo.MainActivity.whichActivity;
import  com.example.todo.MainActivity.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class work extends AppCompatActivity {


    MyDatabaseHelper myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showtasks);
        myDB = new MyDatabaseHelper(this);
        TextView workLabel  = findViewById(R.id.label);

        Button addTaskBt = findViewById(R.id.addBtn);
        addTaskBt.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), AddActivity.class);
            startActivity(intent);
        });

        workLabel.setText(whichActivity);
        loadData();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("deleteOrder"));


    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String taskName = intent.getStringExtra("taskName");

            Toast.makeText(work.this,taskName +" " ,Toast.LENGTH_SHORT).show();

            if(taskName != null)
            {
                Cursor data = myDB.getListContents();

                if(data.getCount() > 0)
                {
                    myDB.deleteTask(taskName);
                    loadData();
                }
            }
        }
    };

    //this function will update data when coming back from add Task activity
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public  void loadData()
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

        Cursor data = myDB.getListContents();
        if (data.getCount() == 0) {
            Toast.makeText(this, "There are no contents in this list!", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                if (data.getString(3).equals(whichActivity)) {
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
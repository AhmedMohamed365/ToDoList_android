package com.example.todo;
import static com.example.todo.MainActivity.shwoDone;
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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

public class work extends AppCompatActivity {


    MyDatabaseHelper myDB;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    LinkedList<taskshow> tasks ;
    String selected_taskName,selected_taskDescription;
    Switch showDone;
    //holder for selected card postion
    int CardPosition;
    int done,edit,delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showtasks);
        done = R.drawable.ic_baseline_done_24;
         edit = R.drawable.ic_baseline_edit_24;
         delete = R.drawable.ic_baseline_delete_24;
        myDB = new MyDatabaseHelper(this);
        TextView workLabel  = findViewById(R.id.label);

        Button addTaskBt = findViewById(R.id.addBtn);
        addTaskBt.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), AddActivity.class);
            startActivity(intent);
        });
        showDone = (Switch) findViewById(R.id.showDone);
        showDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    shwoDone = "show";
                } else {
                    shwoDone = "dont";
                }
            }
        });
        workLabel.setText(whichActivity);
        loadData();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("cardOrder"));

        LocalBroadcastManager.getInstance(this).registerReceiver(changesReciver,
                new IntentFilter("changedInfo"));


    }
    public BroadcastReceiver changesReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //recivie changges after editing in addActivity
            String taskName = intent.getStringExtra("taskName");
            String taskDescription = intent.getStringExtra("taskDescription");
            boolean edited = intent.getBooleanExtra("edited",false);
            if(!taskName.equals("") )
            {
                if( edited)
                {
                    // ahmed edit mkn el deadline hena :)
                    // 7t variable 3la 7sb el function
                    tasks.set( CardPosition, new taskshow(taskName,taskDescription,"" ,done,edit,delete));
                    adapter.notifyItemChanged(CardPosition);
                }

                else
                {
                    // we hena kman :)
                    tasks.add(  new taskshow(taskName,taskDescription,"" ,done,edit,delete));
                    adapter.notifyDataSetChanged();
                }


            }

        }
    };
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent


             selected_taskName = intent.getStringExtra("taskName");
             selected_taskDescription = intent.getStringExtra("date");
            int order = intent.getIntExtra("order",-1);
             CardPosition = intent.getIntExtra("CardPosition",0);
            Toast.makeText(work.this,selected_taskName +" " ,Toast.LENGTH_SHORT).show();

            if(selected_taskName != null)
            {
                Cursor data = myDB.getListContents();

                if(data.getCount() > 0)
                {
                    if(order == 2)
                    {
                        //Delete code
                        myDB.deleteTask(selected_taskName);
                        //update the recycle view after deletion
                        tasks.remove(CardPosition);
                        recyclerView.removeViewAt(CardPosition);
                        adapter.notifyItemRemoved(CardPosition);
                        adapter.notifyItemRangeChanged(CardPosition, tasks.size());
                    }

                    else if(order == 0)
                    {
                        // Done code
                        myDB.updateTask(selected_taskName,selected_taskName,"",1,"WORK","ordinary","Done");
                        adapter.notifyDataSetChanged();
                     //   tasks.re

                    }
                    else if (order == 1)
                    {
                        //Edit  code
                        Intent transferIntent  = new Intent(getBaseContext(), AddActivity.class );
                      //  whichActivity  = "WEEKEND";

                        //we need to transfer the task we want to edit now and change add Bt to save changes
                      //  transferIntent.putExtra("taskName",taskName);
                        //update list contents

                        /*Old method for updating after editing
                        *
                                *   while (data.moveToNext()) {
                                    if (data.getString(3).equals(whichActivity) && data.getString(1).equals(selected_taskName)) {
                                        transferIntent.putExtra("taskName",selected_taskName);
                                        transferIntent.putExtra("description",data.getString(2));
                                        transferIntent.putExtra("date",data.getString(4));
                                    }
                                }
                        *
                        *
                        *
                        *
                        * */
                     //   data = myDB.getListContents();



                        transferIntent.putExtra("taskName",selected_taskName);
                        transferIntent.putExtra("description",selected_taskDescription);
                        /* date will be sent later when we add it well in the design*********/

                        //transferIntent.putExtra("date",data.getString(4));

                        startActivity(transferIntent);
                    }

                    //There is better way than that
                   // loadData();
                }
            }
        }
    };
    public  void loadData()
    {


        RecyclerView.LayoutManager layoutManager;
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        tasks = new LinkedList<>();

        Cursor data = myDB.getListContents();
        if (data.getCount() == 0) {
            Toast.makeText(this, "There are no contents in this list!", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                if (data.getString(3).equals(whichActivity)) {
                    tasks.add(new taskshow(data.getString(1),data.getString(2),data.getString(4) ,done,edit,delete));
                    layoutManager = new LinearLayoutManager(this);
                    adapter = new ViewHandler(tasks);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                }
            }
        }
    }
}
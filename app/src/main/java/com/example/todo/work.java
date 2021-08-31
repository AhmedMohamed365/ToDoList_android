package com.example.todo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

import static com.example.todo.MainActivity.shwoDone;
import static com.example.todo.MainActivity.whichActivity;

public class work extends AppCompatActivity {


    MyDatabaseHelper myDB;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    LinkedList<taskshow> tasks ;
    String selected_taskName,selected_taskDescription , selected_date;
    Switch showDone;

    //holder for selected card postion
    int CardPosition;
    int done,edit,delete;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showtasks);
        done = R.drawable.ic_baseline_done_24;
         edit = R.drawable.ic_baseline_edit_24;
         delete = R.drawable.ic_baseline_delete_24;
        myDB = new MyDatabaseHelper(this);
        TextView workLabel  = findViewById(R.id.label);


        RecyclerView.LayoutManager layoutManager;
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        tasks = new LinkedList<>();
        //Recycle
        layoutManager = new LinearLayoutManager(this);
        adapter = new ViewHandler(tasks);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        Button addTaskBt = findViewById(R.id.addBtn);
        addTaskBt.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), AddActivity.class);
            startActivity(intent);
        });
        showDone = (Switch) findViewById(R.id.showDone);
        showDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    shwoDone = true;
                    addTaskBt.setEnabled(false);
                } else {
                    shwoDone = false;
                    addTaskBt.setEnabled(true);
                }

                tasks.clear();
               // adapter.notifyDataSetChanged();
                loadData();
            }
        });
        workLabel.setText(whichActivity.toUpperCase());
        if(whichActivity.equals("work"))
            workLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.w, 0, 0, 0);
        else if(whichActivity.equals("family"))
            workLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fam, 0, 0, 0);
        else if(whichActivity.equals("gym"))
            workLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fitness, 0, 0, 0);
        else if(whichActivity.equals("studying"))
            workLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.studying, 0, 0, 0);
        else if(whichActivity.equals("shopping"))
            workLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sh, 0, 0, 0);
        else if(whichActivity.equals("weekend"))
            workLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.we, 0, 0, 0);

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
            String taskDate = intent.getStringExtra("taskDate");
            boolean edited = intent.getBooleanExtra("edited",false);
            if(!taskName.equals("") )
            {
                if( edited)
                {
                   // Edit existing Task
                    tasks.set( CardPosition, new taskshow(taskName,taskDescription,taskDate ,done,edit,delete,false));
                    adapter.notifyItemChanged(CardPosition);
                }

                else
                {
                        //Add Task
                    tasks.add(  new taskshow(taskName,taskDescription,taskDate ,done,edit,delete,false));
                    if(tasks.size() !=0)
                    adapter.notifyItemInserted(tasks.size() - 1);
//Animation section


           // animationDrawable.setOneShot(true);
                    adapter.notifyDataSetChanged();
                    //
                   // loadData();

                }


            }

        }
    };
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent


             selected_taskName = intent.getStringExtra("taskName");
             Log.println(Log.ERROR,"selectedTask",selected_taskName);
             selected_taskDescription = intent.getStringExtra("data");
            selected_date = intent.getStringExtra("date");
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
                        //loadData();
                        //update the recycle view after deletion

                        try {
                            tasks.remove(CardPosition);
                            recyclerView.removeViewAt(CardPosition);
                            adapter.notifyItemRemoved(CardPosition);
                            adapter.notifyItemRangeChanged(CardPosition, tasks.size());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    else if(order == 0)
                    {
                        // Done code
                        myDB.updateTask(selected_taskName,selected_taskName,selected_taskDescription,selected_date,whichActivity,"ordinary","Done");
                       // tasks.set(CardPosition,new taskshow(selected_taskName,selected_taskDescription,selected_date,done,edit,delete,true));
                        //adapter.notifyItemChanged(CardPosition);
                       // adapter.notifyItemChanged(CardPosition);

                       // recyclerView.findViewHolderForAdapterPosition(CardPosition).itemView.setBackgroundColor(Color.GREEN);
                       // loadData();
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
                        transferIntent.putExtra("date",selected_date);


                        /* date will be sent later when we add it well in the design*********/

                        //transferIntent.putExtra("date",data.getString(4));

                        startActivity(transferIntent);
                        //loadData();
                    }

                    //There is better way than that
                   // loadData();
                }
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        tasks.clear();
        loadData();
    }

    public  void loadData()
    {



        //variable to set Done Cards to green
        View card;
        Cursor data = myDB.getListContents();
        int i = 0;
        if (data.getCount() == 0) {

        } else {
            while (data.moveToNext()) {
                if (data.getString(3).equals(whichActivity)) {

                    if(shwoDone && data.getString(6).equals("Done"))
                    {
                        tasks.add(new taskshow(data.getString(1),data.getString(2),data.getString(4) ,done,edit,delete,true));

                        i++;
                    }
                    else if (shwoDone == false &&  data.getString(6).equals("going"))
                    {
                        tasks.add(new taskshow(data.getString(1),data.getString(2),data.getString(4) ,done,edit,delete,false));
                        i++;
                    }


// Implement a way to color Done cards in the begining

//                    layoutManager = new LinearLayoutManager(this);
//                    adapter = new ViewHandler(tasks);
//                    recyclerView.setLayoutManager(layoutManager);
//                    recyclerView.setAdapter(adapter);




                }

            }

            adapter.notifyDataSetChanged();


//            for(int i = 0; i<tasks.size();i++)
//            {
//                if(tasks.get(i).getStatus())
//                {
//                    Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(i)).itemView.setBackgroundColor(Color.GREEN);
//                }
//            }
        }
    }


}
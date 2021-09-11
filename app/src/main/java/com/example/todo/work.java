package com.example.todo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.LinkedList;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import static com.example.todo.MainActivity.shwoDone;
import static com.example.todo.MainActivity.whichActivity;

public class work extends AppCompatActivity {


    MyDatabaseHelper myDB;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    LinkedList<taskshow> tasks;
    String selected_taskName, selected_taskDescription, selected_date;
    Switch showDone;
    private int shortAnimationDuration;
    //holder for selected card postion
    int CardPosition;
    int done, edit, delete;

    TextView workLabel;
    final int channelId = 123;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showtasks);

        onNewIntent(getIntent());
        done = R.drawable.ic_baseline_done_24;
        edit = R.drawable.ic_baseline_edit_24;
        delete = R.drawable.ic_baseline_delete_24;
        myDB = new MyDatabaseHelper(this);
        workLabel = findViewById(R.id.label);


        //to make Notification works well :)
//
//        if(().getStringExtra("type")!= null)
//        {
//            whichActivity = getIntent().getStringExtra("type");
//        }
        RecyclerView.LayoutManager layoutManager;
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        tasks = new LinkedList<>();
        //Recycle
        layoutManager = new LinearLayoutManager(this);
        adapter = new ViewHandler(tasks);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

//NOTIFICATION SECTION

        //  whichActivity  = "work";
        // Create an explicit intent for an Activity in your app
        Intent notificationIntent = new Intent(this, work.class);
        notificationIntent.putExtra("type", whichActivity);
        notificationIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK|FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        pendingIntent.getActivity(this, 0, notificationIntent,   PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, whichActivity + "Channel")
                .setSmallIcon(R.drawable.f)
                .setContentTitle("My notification")
                .setContentText("tasks you have to finish for +" + whichActivity)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("tasks you have to finish for +" + whichActivity))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        createNotificationChannel();


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);


// notificationId is a unique int for each notification that you must define
        notificationManager.notify(channelId, builder.build());


        //Aniamtion section
        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);


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
                    //  shortAnimationDuration =
                }

                tasks.clear();
                // adapter.notifyDataSetChanged();
                loadData();
            }
        });
        workLabel.setText(whichActivity.toUpperCase());
        if (whichActivity.equals("work"))
            workLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.w, 0, 0, 0);
        else if (whichActivity.equals("family"))
            workLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fam, 0, 0, 0);
        else if (whichActivity.equals("gym"))
            workLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fitness, 0, 0, 0);
        else if (whichActivity.equals("studying"))
            workLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.studying, 0, 0, 0);
        else if (whichActivity.equals("shopping"))
            workLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sh, 0, 0, 0);
        else if (whichActivity.equals("weekend"))
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
            boolean edited = intent.getBooleanExtra("edited", false);
            if (!taskName.equals("")) {
                if (edited) {
                    // Edit existing Task
                    tasks.set(CardPosition, new taskshow(taskName, taskDescription, taskDate, done, edit, delete, false));
                    adapter.notifyItemChanged(CardPosition);
                } else {
                    //Add Task
                    tasks.add(new taskshow(taskName, taskDescription, taskDate, done, edit, delete, false));
                    if (tasks.size() != 0)
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
            Log.println(Log.ERROR, "selectedTask", selected_taskName);
            selected_taskDescription = intent.getStringExtra("data");
            selected_date = intent.getStringExtra("date");
            int order = intent.getIntExtra("order", -1);
            CardPosition = intent.getIntExtra("CardPosition", 0);


            Toast.makeText(work.this, selected_taskName + " ", Toast.LENGTH_SHORT).show();

            if (selected_taskName != null) {
                Cursor data = myDB.getListContents();

                if (data.getCount() > 0) {
                    if (order == 2) {
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

                    } else if (order == 0) {
                        // Done code
                        myDB.updateTask(selected_taskName, selected_taskName, selected_taskDescription, selected_date, whichActivity, "ordinary", "Done");
                        // tasks.set(CardPosition,new taskshow(selected_taskName,selected_taskDescription,selected_date,done,edit,delete,true));
                        //adapter.notifyItemChanged(CardPosition);
                        // adapter.notifyItemChanged(CardPosition);

                        // recyclerView.findViewHolderForAdapterPosition(CardPosition).itemView.setBackgroundColor(Color.GREEN);
                        // loadData();
                        //   tasks.re
                    } else if (order == 1) {

                        //Edit  code
                        Intent transferIntent = new Intent(getBaseContext(), AddActivity.class);
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


                        transferIntent.putExtra("taskName", selected_taskName);
                        transferIntent.putExtra("description", selected_taskDescription);
                        transferIntent.putExtra("date", selected_date);


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

    public void loadData() {


        //variable to set Done Cards to green
        View card;
        Cursor data = myDB.getListContents();
        int i = 0;
        if (data.getCount() == 0) {

        } else {
            while (data.moveToNext()) {
                if (data.getString(3).equals(whichActivity)) {

                    if (shwoDone && data.getString(6).equals("Done")) {
                        tasks.add(new taskshow(data.getString(1), data.getString(2), getLeftTime(data.getString(4)), done, edit, delete, true));

                        i++;
                    } else if (shwoDone == false && data.getString(6).equals("going")) {
                        tasks.add(new taskshow(data.getString(1), data.getString(2), getLeftTime(data.getString(4)), done, edit, delete, false));
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


    public String getLeftTime(String date) {
        SQLiteDatabase db = myDB.getReadableDatabase();
        // Cursor data =   db.rawQuery("select JULIANDAY('?') - JULIANDAY('now') ",new String[] {date});

        int days = 0;

        //query to calculate the left time from Now to the time where task should be ended.
        Cursor data = db.rawQuery("select  JULIANDAY(?) -JULIANDAY(datetime(datetime('now'), 'localtime') )  ", new String[]{date});
        data.moveToNext();


        double doubleAsString = data.getDouble(0);

        if (doubleAsString < 0) {
            return "overDue";
        }
        days = (int) doubleAsString;
        double hours = (doubleAsString - days) * 24;

        int minutes = (int) ((hours - (int) hours) * 60);


        //Format to one place only
        NumberFormat formatter = new DecimalFormat("#0.0");
        hours = Double.parseDouble(formatter.format(hours));


        String leftTime = "";
        if (days >= 1)
            leftTime += days + "days ";

        if (hours >= 1)
            leftTime += (int) hours + "hours ";

        if (minutes != 0)
            leftTime += minutes + "minutes";

        return leftTime;

    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "first";
            String description = "to allarm user";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(whichActivity + "Channel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(intent.getExtras() != null)
        {
            String type = intent.getStringExtra("type");
           // Toast.makeText(this,type + "is not empty",Toast.LENGTH_SHORT).show();

            whichActivity  = intent.getStringExtra("type");
           // workLabel.setText(whichActivity);
        }





    }

}





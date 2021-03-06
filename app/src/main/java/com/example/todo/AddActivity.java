package com.example.todo;

import static com.example.todo.MainActivity.tempWhich;
import static com.example.todo.MainActivity.whichActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    EditText title_input, data_input;
    TextView   dateField;
    Button add_button;
    String priorityChoice = "ordinary";

    String pickedTime;
   /*
    To Be used later :)
    enum priority  {ordinary , important , urgent};



    */


    int  hour , minute;
    String taskName,description,date;

    Button []priorityBts;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

         taskName=getIntent().getStringExtra("taskName");
         description = getIntent().getStringExtra("description");
         date = getIntent().getStringExtra("date");

        title_input= findViewById(R.id.taskName);
        data_input = findViewById(R.id.taskData);
        dateField= (TextView) findViewById(R.id.DatePicker);
        add_button = findViewById(R.id.addingTaskBtn);
        add_button.setEnabled(false);
        if(taskName != null || description != null)
        {
            title_input.setText(taskName);
            data_input.setText(description);
            dateField.setText(date);
            add_button.setText("Save changes");
        }


        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "YYYY-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                dateField.setText(sdf.format(myCalendar.getTime()));
            }

        };

        dateField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

            //Ensure somthing is in task_Name
        title_input.setOnFocusChangeListener((view, b) -> {
            if(title_input.getText().toString().equals(""))
            {
                add_button.setEnabled(false);

            }
            else
            {
                add_button.setEnabled(true);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
//finish();

    }

    public void handleAddTaskBt(View view)
    {


        if(title_input.getText().toString().equals(""))
        {
            Toast.makeText(this,"You can't leave Task Name empty !",Toast.LENGTH_SHORT).show();
            add_button.setEnabled(false);
            return;
        }
        MyDatabaseHelper myDB = new MyDatabaseHelper(this);
        SQLiteDatabase db = myDB.getReadableDatabase();
        Cursor data = db.rawQuery("select * from mytask where task_title = ?" ,new String[]{title_input.getText().toString()});


        Intent changedInfo = new Intent("changedInfo");

        changedInfo.putExtra("taskName",title_input.getText().toString());
        changedInfo.putExtra("taskDescription",data_input.getText().toString());
        changedInfo.putExtra("taskDate",dateField.getText().toString());


        if(data.getCount() >= 1)
        {
            Toast.makeText(this,"You can't put existing taskName !",Toast.LENGTH_LONG).show();

        }

        //Edit Task doesn't work when trying to change task description only
        else
        {
            //edit Code
            if(add_button.getText().toString().equals("Save changes"))
            {
                myDB.updateTask(taskName,title_input.getText().toString(),data_input.getText().toString(),dateField.getText().toString().trim()+pickedTime
                        ,whichActivity,priorityChoice,"going");
                changedInfo.putExtra("edited", true);
            }
            //Add code
            else {

                changedInfo.putExtra("edited", false);
                myDB.addTask(title_input.getText().toString().trim(), data_input.getText().toString().trim(),
                        dateField.getText().toString().trim() + pickedTime, whichActivity, priorityChoice, "going");

            }
        }




        LocalBroadcastManager.getInstance(this).sendBroadcast(changedInfo);

//        Intent intent = new Intent(getBaseContext(),work.class);
//        startActivity(intent);
//        finish();

//        Intent intent = new Intent(getBaseContext(),MainActivity.class);
//        startActivity(intent);
//        Intent intent2 = new Intent(getBaseContext(),work.class);
//        startActivity(intent2);
    }


   public void getPriority(View view)
    {


       Button selectedBt = (Button) view;

        priorityChoice =  selectedBt.getText().toString();

        Toast.makeText(this,"You Chosed bt "+priorityChoice,Toast.LENGTH_SHORT).show();
    }


    public void pickTime(View view)
    {


        TextView timeText = (TextView) view;



        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {


                hour = selectedHour;
                minute = selectedMinute;
                pickedTime =' '+ String.format(Locale.getDefault(), "%02d:%02d:00",hour, minute);
                timeText.setText(pickedTime);
            }


        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, false);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}
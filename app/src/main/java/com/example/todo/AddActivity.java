package com.example.todo;

import static com.example.todo.MainActivity.whichActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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
    Button []priorityBts;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        String cateogry=getIntent().getStringExtra("type");
        title_input= findViewById(R.id.taskName);
        data_input = findViewById(R.id.taskData);
        dateField= (TextView) findViewById(R.id.DatePicker);
        add_button = findViewById(R.id.addingTaskBtn);
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
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                dateField.setText(sdf.format(myCalendar.getTime()));
            }

        };

        dateField.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(AddActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });





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
        MyDatabaseHelper myDB= new MyDatabaseHelper(this);
        Cursor data = myDB.getListContents();
        if (data.getCount() == 0) {
            Toast.makeText(this, "There are no contents in this list!", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                if (data.getString(3).equals("work") ) {
                    tasks.add(new taskshow(data.getString(1),data.getString(2) ,done,edit,delete));
                    layoutManager = new LinearLayoutManager(this);
                    adapter = new ViewHandler(tasks);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                }
            }
        }




    }
    public void handleAddTaskBt(View view)
    {
        //Will give error not all fields return a value
        MyDatabaseHelper myDB = new MyDatabaseHelper(this);
        //myDB.deleteTask("AHMED");
        myDB.addTask(title_input.getText().toString().trim(), data_input.getText().toString().trim(),
               dateField.getText().toString().trim(), "WORK","","going");

//load the updated data
        loadData();
    }
   public void getPriority(View view)
    {


       Button selectedBt = (Button) view;

        priorityChoice =  selectedBt.getText().toString();

        Toast.makeText(this,"You Chosed bt "+priorityChoice,Toast.LENGTH_SHORT).show();
    }
}
package com.example.todo;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    EditText title_input, data_input, days_input;
         TextView   dateField;
    Button add_button;
    String priorityChoice = "ordinary";
    Button []priorityBts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        String cateogry=getIntent().getStringExtra("type");


        final Calendar myCalendar = Calendar.getInstance();

        dateField= (TextView) findViewById(R.id.DatePicker);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

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

        dateField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }



    public void handleAddTaskBt()
    {
        //Will give error not all fields return a value
        MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
        myDB.addTask(title_input.getText().toString().trim(),
                data_input.getText().toString().trim(),
                Integer.valueOf(days_input.getText().toString().trim()),"work",priorityChoice,"Done");
    }
   public void getPriority(View view)
    {


       Button selectedBt = (Button) view;

        priorityChoice =  selectedBt.getText().toString();

        Toast.makeText(this,"You Chosed bt "+priorityChoice,Toast.LENGTH_SHORT).show();
    }
}
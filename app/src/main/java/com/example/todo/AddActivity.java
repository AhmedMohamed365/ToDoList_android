package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    EditText title_input, data_input, days_input;
    Button add_button;
String priorityChoice = "ordinary";
    Button []priorityBts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        String cateogry=getIntent().getStringExtra("type");
        title_input = findViewById(R.id.title_input);
        data_input = findViewById(R.id.data_input);
        days_input = findViewById(R.id.days_input);
        add_button = findViewById(R.id.add_button);
        //prioritySection
        priorityBts = new Button[]{findViewById(R.id.urgentBt), findViewById(R.id.ordinaryBt),findViewById(R.id.ImportantBt)};
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                myDB.addTask(title_input.getText().toString().trim(),
                        data_input.getText().toString().trim(),
                        Integer.valueOf(days_input.getText().toString().trim()),cateogry,priorityChoice);
            }
        });


    }

   public void getPriority(View view)
    {


       Button selectedBt = (Button) view;

//       title_input.setClickable(true);
        title_input.setFocusableInTouchMode(true);
        title_input.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(title_input, InputMethodManager.SHOW_IMPLICIT);



        //title_input.

//        priorityChoice =  selectedBt.getText().toString();
//
//        Toast.makeText(this,"You Chosed bt "+priorityChoice,Toast.LENGTH_SHORT).show();
    }


}
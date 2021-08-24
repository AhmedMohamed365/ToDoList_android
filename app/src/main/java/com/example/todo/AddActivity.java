package com.example.todo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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



    }

   public void getPriority(View view)
    {


       Button selectedBt = (Button) view;

        priorityChoice =  selectedBt.getText().toString();

        Toast.makeText(this,"You Chosed bt "+priorityChoice,Toast.LENGTH_SHORT).show();
    }
}
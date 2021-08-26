package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private FloatingActionButton fab4;
    private FloatingActionButton fab5;
    private FloatingActionButton fab6;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;
    static String whichActivity = "main";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Calendar calendar = Calendar.getInstance();
        String currentdate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView textView = findViewById(R.id.textView2);
        textView.setText(currentdate);

        button1 =findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getBaseContext(), work.class );
                whichActivity  = "work";
                startActivity(intent);
            }
        });

        button2 =findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getBaseContext(), work.class );
                whichActivity  = "family";
                startActivity(intent);
            }
        });
        button3 =findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getBaseContext(), work.class );
                whichActivity  = "weekend";
                startActivity(intent);
            }
        });
        button4 =findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getBaseContext(), work.class );
                whichActivity  = "shopping";
                startActivity(intent);
            }
        });
        button5 =findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getBaseContext(), work.class );
                whichActivity  = "studying";
                startActivity(intent);
            }
        });
        button6 =findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getBaseContext(), work.class );
                whichActivity  = "gym";
                startActivity(intent);
            }
        });
    }
}

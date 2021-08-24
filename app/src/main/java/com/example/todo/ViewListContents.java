package com.example.todo;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitch on 2016-05-13.
 */
public class ViewListContents extends AppCompatActivity {

    MyDatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewlistcontents_layout);

        ListView listView = (ListView)findViewById(R.id.listView);
        myDB = new MyDatabaseHelper(this);
        String cateogry=getIntent().getStringExtra("type");
        //populate an ArrayList<String> from the database and then view it
        ArrayList<String> theList = new ArrayList<>();
        //Testing deleteing by name of task



        // use the same code only change which function gets called deleteTask or updateTask

//        Cursor data = myDB.getListContents();
//        if(data.getCount() > 0)
//        {
//            data.moveToNext();
//            myDB.deleteTask(data.getString(1));
//            //myDB.updateTask(data.getString(1),"It's New Title","This is example for update",1,cateogry,"Urgent");
//            //get the updated list after deletion
//
//            data = myDB.getListContents();
//        }







        Cursor data = myDB.getListContents();
        if(data.getCount() == 0){
            Toast.makeText(this, "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }

        else{
            while(data.moveToNext()) {
                if (data.getString(3).equals(cateogry)) {




                    theList.add(data.getString(1));
                    theList.add(data.getString(2));
                    theList.add(data.getString(4));
                    theList.add(data.getString(5));
                    theList.add("\n");
                    ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, theList);
                    listView.setAdapter(listAdapter);
                }
            }
            }
        }


    }

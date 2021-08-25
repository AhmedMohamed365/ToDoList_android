package com.example.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "to_do_list";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "mytask";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "task_title";
    private static final String COLUMN_data = "task_data";
    private static final String COLUMN_days = "task_days";
    private static final String COLUMN_type = "task_type";
    private static final String COLUMN_priority = "task_priority";
    private static final String COLUMN_status = "task_status";
    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_data + " TEXT, "+
                COLUMN_type + " TEXT, "+
                COLUMN_days + " INTEGER,"+
        COLUMN_priority +" TEXT,"+
        COLUMN_status+" TEXT );" ;
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

//        if ( i1 > i){
//            db.execSQL("DROP TABLE mytask");
//        }
        onCreate(db);
    }




//Basic operations insert - update - retrieve - delete
    public void addTask(String title, String data, String date, String type, String priority,String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues row = new ContentValues();

        row.put(COLUMN_TITLE, title);
        row.put(COLUMN_data, data);
        row.put(COLUMN_days,date);
        row.put(COLUMN_type, type);
        row.put(COLUMN_priority,priority);
        row.put(COLUMN_status,status);
        long result = db.insert(TABLE_NAME,null, row);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();


        }
    }

    public void updateTask(String title,String newTitle, String data, int days,String type,String priority,String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newRowContent = new ContentValues();

        newRowContent.put(COLUMN_TITLE, newTitle);
        newRowContent.put(COLUMN_data, data);
        newRowContent.put(COLUMN_days, days);
        newRowContent.put(COLUMN_type, type);
        newRowContent.put(COLUMN_priority,priority);
        newRowContent.put(COLUMN_status,status);

        Cursor cursor = db.rawQuery("select * from mytask where task_title =?" ,new String[]{title});

        if(cursor.getCount() >0)
        {
            long result = db.update(TABLE_NAME,newRowContent, "task_title = ?" , new String[]{title});
            if(result == -1){
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "updated Successfully!", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(context, "Can't find the requested task to edit!", Toast.LENGTH_SHORT).show();
        }

    }


    public void deleteTask(String title){
        SQLiteDatabase db = this.getWritableDatabase();


        Cursor cursor = db.rawQuery("select * from mytask where task_title =?" ,new String[]{title});

        if(cursor.getCount() >0)
        {
            long result = db.delete(TABLE_NAME, "task_title = ?" , new String[]{title});
            if(result == -1){
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(context, "Can't find the requested task to delete!", Toast.LENGTH_SHORT).show();
        }

    }

    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

   public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

}

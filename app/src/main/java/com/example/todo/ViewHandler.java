package com.example.todo;
import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewHandler extends RecyclerView.Adapter<ViewHandler.EXAMPLEVIEWHOLDER> {
    private  ArrayList <taskshow> tasks ;
    public static class EXAMPLEVIEWHOLDER extends RecyclerView.ViewHolder
    {
        public ImageView edit,done,delete ;
        public TextView name,date;

        public EXAMPLEVIEWHOLDER(@NonNull View itemView) {
            super(itemView);

            delete = itemView.findViewById(R.id.deleteImage);
            done = itemView.findViewById(R.id.doneImage);
            edit = itemView.findViewById(R.id.editImage);
            name = itemView.findViewById(R.id.nameTxt);
            date = itemView.findViewById(R.id.dateTxt);
            delete.setOnClickListener(view -> {

                //delete the requested task
                Intent intent = new Intent("deleteOrder");
                intent.putExtra("taskName",name.getText().toString());
               // LocalBroadcastManager.getInstance().sendBroadcast(intent);
            });
        }
    }
    public ViewHandler(ArrayList<taskshow> examplist)
    {
        tasks = examplist ;
    }
    @NonNull
    @Override
    public EXAMPLEVIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewlistcontents_layout,parent,false);
        EXAMPLEVIEWHOLDER evh = new EXAMPLEVIEWHOLDER(v);
        return evh ;
    }

    @Override
    public void onBindViewHolder(@NonNull EXAMPLEVIEWHOLDER holder, int position) {
        taskshow currentItem = tasks.get(position);
        holder.edit.setImageResource(currentItem.getEditPic());
        holder.delete.setImageResource(currentItem.getDeletePic());
        holder.done.setImageResource(currentItem.getDonePic());
        holder.name.setText(currentItem.getName());
        holder.date.setText(currentItem.getDate());

    }
    @Override
    public int getItemCount() {
        return tasks.size();
    }
}


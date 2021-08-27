package com.example.todo;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
public class ViewHandler extends RecyclerView.Adapter<ViewHandler.EXAMPLEVIEWHOLDER> {
    private LinkedList<taskshow> tasks ;
    public static class EXAMPLEVIEWHOLDER extends RecyclerView.ViewHolder
    {
        ImageView edit,done,delete ;
        TextView name,data,deadline;
        RelativeLayout relativeLayout;
        int order = -1;
        public EXAMPLEVIEWHOLDER(@NonNull View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.deleteImage);
            done = itemView.findViewById(R.id.doneImage);
            edit = itemView.findViewById(R.id.editImage);
            name = itemView.findViewById(R.id.nameTxt);
            data = itemView.findViewById(R.id.dataTxt);
            deadline = itemView.findViewById(R.id.deadLine);
            relativeLayout = itemView.findViewById(R.id.relative);
            edit.setOnClickListener(view -> {
                order = 1;
                Intent intent = new Intent("cardOrder");
                intent.putExtra("taskName",name.getText().toString());
                intent.putExtra("order",order);
                intent.putExtra("data",data.getText().toString());
                LocalBroadcastManager.getInstance(delete.getContext()).sendBroadcast(intent);

            });
            done.setOnClickListener(view -> {

                itemView.setBackgroundColor(Color.GREEN);
                order = 0;
                Intent intent = new Intent("cardOrder");
                intent.putExtra("taskName",name.getText().toString());
                intent.putExtra("order",order);
                LocalBroadcastManager.getInstance(delete.getContext()).sendBroadcast(intent);



            });
            delete.setOnClickListener(view -> {
                order = 2;
                //delete the requested task
                Intent intent = new Intent("cardOrder");
                intent.putExtra("taskName",name.getText().toString());
                intent.putExtra("order",order);
                LocalBroadcastManager.getInstance(delete.getContext()).sendBroadcast(intent);
            });
        }
    }
    public ViewHandler(LinkedList<taskshow> examplist)
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
        holder.data.setText(currentItem.getData());
        holder.deadline.setText(currentItem.getDeadline());

    }
    @Override
    public int getItemCount() {
        return tasks.size();
    }
}


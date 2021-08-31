package com.example.todo;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

import static com.example.todo.MainActivity.shwoDone;

public class ViewHandler extends RecyclerView.Adapter<ViewHandler.EXAMPLEVIEWHOLDER> {
    private LinkedList<taskshow> tasks ;
    private boolean status = false;
    int offset = 50;

    public static class EXAMPLEVIEWHOLDER extends RecyclerView.ViewHolder
    {
        ImageView edit,done,delete ;
        TextView name,data,deadline;
         public RelativeLayout relativeLayout;
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

//            if(shwoDone)
//            {
//                edit.setEnabled(false);
//                done.setEnabled(false);
//                Log.d("done","true");
//
//            }


            edit.setOnClickListener(view -> {
                order = 1;
                Intent intent = new Intent("cardOrder");
                intent.putExtra("taskName",name.getText().toString());
                intent.putExtra("order",order);
                intent.putExtra("data",data.getText().toString());
                intent.putExtra("date",deadline.getText().toString());
                intent.putExtra("CardPosition",getAdapterPosition());
                LocalBroadcastManager.getInstance(delete.getContext()).sendBroadcast(intent);

            });
            done.setOnClickListener(view -> {

                edit.setEnabled(false);
                done.setEnabled(false);
                itemView.setBackgroundColor(Color.GREEN);
                order = 0;
                Intent intent = new Intent("cardOrder");
                intent.putExtra("taskName",name.getText().toString());
                intent.putExtra("data",data.getText().toString());
                intent.putExtra("date",deadline.getText().toString());
                intent.putExtra("order",order);
                intent.putExtra("CardPosition",getAdapterPosition());
                LocalBroadcastManager.getInstance(delete.getContext()).sendBroadcast(intent);
            });
            delete.setOnClickListener(view -> {
                order = 2;
                //delete the requested task
                Intent intent = new Intent("cardOrder");
                intent.putExtra("taskName",name.getText().toString());
                intent.putExtra("CardPosition",getAdapterPosition());
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

//       if(shwoDone)
//       {
//           v.setBackgroundColor(Color.GREEN);
//       }
//       else
//       {
//           v.setBackgroundColor(Color.RED);
//       }


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
        status = currentItem.getStatus();
        if(status)
        {

            holder.itemView.setBackgroundColor(Color.GREEN);
            holder.done.setEnabled(false);
            holder.edit.setEnabled(false);

        }
        else
        {
            //under exoeriments
            Rect bounds  = holder.relativeLayout.getBackground().getBounds();
//
           holder.relativeLayout.getBackground().setBounds(new Rect( bounds.left+10,bounds.top+10,bounds.right-offset,bounds.bottom+10) );
           offset+= 50;


            AnimationDrawable animationDrawable = (AnimationDrawable) holder.relativeLayout.getBackground();
            animationDrawable.setEnterFadeDuration(2000);
            animationDrawable.setExitFadeDuration(3000);
           // animationDrawable.setOneShot(true);
            animationDrawable.setBounds(new Rect( bounds.left+10,bounds.top+10,bounds.right-offset,bounds.bottom+10));

            animationDrawable.start();


           // AnimationDrawable animation = (AnimationDrawable)  holder.relativeLayout.getBackground();

//            animationDrawable.setEnterFadeDuration(2000);
//            animationDrawable.setExitFadeDuration(3000);
//            animationDrawable.start();
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);

            holder.done.setEnabled(true);
            holder.edit.setEnabled(true);
        }


    }





    @Override
    public int getItemCount() {
        return tasks.size();
    }
}


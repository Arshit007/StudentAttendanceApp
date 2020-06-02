package com.example.arshit.studentattendanceapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.arshit.studentattendanceapp.MessageActivity;
import com.example.arshit.studentattendanceapp.Model.Teacher;
import com.example.arshit.studentattendanceapp.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class TeacherChatAdapter extends RecyclerView.Adapter<TeacherChatAdapter.UserViewHolder> {

    private Context context;
    private  ArrayList<Teacher> userdata;
    private String imageURL;
    DatabaseReference database;
    String    image;

    public TeacherChatAdapter(Context context, ArrayList<Teacher> userdata) {
        this.context = context;
        this.userdata = userdata;
    }



    @NonNull
    @Override
    public TeacherChatAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.userdisplay, parent, false);
        return new UserViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, final int position) {


        final Teacher user = userdata.get(position);
        holder.uname.setText(user.getName());
        holder.ustatus.setText(user.getDeptField());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid",user.getTeacherId());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() { return userdata.size(); }


    public class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView uname,ustatus;
        public     ImageView profileImg;
        public UserViewHolder(View itemView) {
            super(itemView);

            profileImg = itemView.findViewById(R.id.userdiaplayimg1);
            ustatus = itemView.findViewById(R.id.ustatus);
            uname = itemView.findViewById(R.id.uname);

        }


    }
}

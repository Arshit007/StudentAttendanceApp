package com.example.arshit.studentattendanceapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.arshit.studentattendanceapp.Model.Chat;
import com.example.arshit.studentattendanceapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context context;

    private  ArrayList<Chat> chatsdata;
  FirebaseUser firebaseUser;
private  String imageurl;


    public MessageAdapter(Context context, ArrayList<Chat> chatsdata) {
        this.context = context;
        this.chatsdata = chatsdata;

    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new MessageViewHolder(mView);
        }
        else {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new MessageViewHolder(mView);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        Chat chat = chatsdata.get(position);


//        holder.show_msg.setText(chat.getMessage());

        String message_type = chat.getType();

        if (message_type.equals("text")) {

            holder.show_msg.setText(chat.getMessage());

            holder.messageImage.setVisibility(View.INVISIBLE);


        } else {

            holder.show_msg.setVisibility(View.INVISIBLE);

            Picasso.get().load(chat.getMessage()).placeholder(R.drawable.profile_avatar_small).into(holder.messageImage);
//        }


        }
    }

    @Override
    public int getItemCount() { return chatsdata.size(); }


    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView show_msg,time_display;
        public ImageView profileimg,messageImage;

        public MessageViewHolder(View itemView) {
            super(itemView);
time_display = itemView.findViewById(R.id.time_display);
            show_msg = itemView.findViewById(R.id.chat_msg);
//            show_msg = itemView.findViewById(R.id.group_msg);
//            profileimg = itemView.findViewById(R.id.user_diaplay_img);
            messageImage = itemView.findViewById(R.id.message_image_layout);
        }

    }

    @Override
    public int getItemViewType(int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (chatsdata.get(position).getSender().equals(firebaseUser.getUid())){

            return MSG_TYPE_RIGHT;
        }

        else {
            return MSG_TYPE_LEFT;
        }

    }
}

package com.example.arshit.studentattendanceapp;



import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arshit.studentattendanceapp.Adapter.MessageAdapter;
import com.example.arshit.studentattendanceapp.Model.Chat;
import com.example.arshit.studentattendanceapp.Model.Teacher;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessageActivity extends AppCompatActivity {

    CircleImageView profileimg;
    private TextView username;
    private DatabaseReference dbrfer;
    private FirebaseUser firebaseUser;
    private EditText sendmsg;
    ImageButton sendBtn,camera_send_btn;
    ImageView toolbar_img;
    RecyclerView rv_chat;
    private String mCurrentUserId;
    Intent intent;
    String chat_user_ref;
    String push_id;
    DatabaseReference databaseReference;
    private StorageReference imgStorageReference;
    FirebaseRecyclerAdapter adapter;
    String userid;
    String current_user_ref;
    ArrayList<Chat> mchat;
    private ProgressDialog mProgressDialog;

    MessageAdapter messageAdapter;

    private static int Gallery_Pick =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        toolbar();
        intialise();

        sendButton();


        dbrfer.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Teacher user = dataSnapshot.getValue(Teacher.class);
                username.setText(user.getName());

                toolbar_img.setImageResource(R.drawable.profile_avatar_small);
//
//                        if (user.getProfilePic().equals("default")){
//
//           profileimg.setImageResource(R.drawable.profile_avatar_small);
//        }
//        else{
//
//            Glide.with(MessageActivity.this).load(user.getProfilePic()).into(profileimg);
//        }

                readmessage(firebaseUser.getUid(),userid);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    private void sendMessage(String sender,String receiver,String message)
    {

        DatabaseReference dbrfer = FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("type", "text");
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("time",ServerValue.TIMESTAMP);

        dbrfer.child("Chats").push().setValue(hashMap);


    }

    private void ImageMessage(String sender,String receiver,String message)
    {

        DatabaseReference dbrfer = FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("time",ServerValue.TIMESTAMP);

        dbrfer.child("Chats").push().setValue(hashMap);


    }



    private void readmessage(final String myid, final String userid)

    {

        mchat = new ArrayList<>();

        DatabaseReference dbrfer = FirebaseDatabase.getInstance().getReference("Chats");

        dbrfer.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    if(chat.getReceiver().equals(myid) &&  chat.getSender().equals(userid)||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){

                        mchat.add(chat);
                    }

                    messageAdapter =new MessageAdapter(MessageActivity.this,mchat);
                    rv_chat.setAdapter(messageAdapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == Gallery_Pick && resultCode == RESULT_OK) {

            Uri imageURI = data.getData();



            Uri imageUri = data.getData();



            current_user_ref = "messages/" + mCurrentUserId + "/" + userid;
            chat_user_ref = "messages/" + userid + "/" + mCurrentUserId;

//            DatabaseReference user_message_push = mRootRef.child("Chats")
//                    .child(mCurrentUserId).child(userid).push();
//
//          push_id = user_message_push.getKey();
//

            CropImage.activity(imageURI).setAspectRatio(1, 1).start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {


                mProgressDialog = new ProgressDialog(MessageActivity.this);
                mProgressDialog.setTitle("Saving Changes");
                mProgressDialog.setMessage("Wait untill changes are saved");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();


                Uri resultUri = result.getUri();

//                final String current_user_id = firebaseUser.getUid();

//            StorageReference filepath = imgStorageReference.child("Message_images").child( push_id + ".jpg");

                StorageReference filepath = imgStorageReference.child("Message_images").child(mCurrentUserId + ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {


                            imgStorageReference.child("Message_images").child(mCurrentUserId + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String downloadUrl = uri.toString();



                                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                                    Map messageMap = new HashMap();

                                    messageMap.put("sender",firebaseUser.getUid());
                                    messageMap.put("message", downloadUrl);
                                    messageMap.put("type", "image");
                                    messageMap.put("time", ServerValue.TIMESTAMP);
                                    messageMap.put("receiver", userid);

                                    mRootRef.child("Chats").push().setValue(messageMap);

                                    mProgressDialog.dismiss();

//                                    Map messageUserMap = new HashMap();
//                                    messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
//                                    messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

//                                    mChatMessageView.setText("");




//                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");

//                                   mRootRef.setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//
//                                            if (task.isSuccessful()) {
//
//
//                                            }
//
//
//                                        }
//                                    });

                                }

                            });

                        }

//                           Toast.makeText(SettingsActivity.this, "Working", Toast.LENGTH_SHORT).show();
//                     }
                        else {

                            Toast.makeText(MessageActivity.this, "Not Working", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }

                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }

        }
    }


    private void sendButton(){

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg = sendmsg.getText().toString();
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                if (!msg.equals("")){
                    sendMessage(firebaseUser.getUid(),userid,msg);
                }

                else {
                    Toast.makeText(MessageActivity.this, "NO empty text allowed", Toast.LENGTH_SHORT).show();
                }
                sendmsg.setText("");
            }
        });



    }

    private void intialise(){


        camera_send_btn = (ImageButton) findViewById(R.id.camera_btn);
        username = findViewById(R.id.toolbar_title);
        toolbar_img =  findViewById(R.id.toolbar_img);



        camera_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,Gallery_Pick);

            }
        });


        sendBtn =  (ImageButton) findViewById(R.id.sendbtn);
        sendmsg =  findViewById(R.id.sendmsg);

        intent= getIntent();





        userid =  intent.getStringExtra("userid");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mCurrentUserId = firebaseUser.getUid();

        dbrfer = FirebaseDatabase.getInstance().getReference("Teachers").child(userid);
        //        profileimg = findViewById(R.id.profilePic)
        //
        rv_chat = findViewById(R.id.msg_rv);

        imgStorageReference = FirebaseStorage.getInstance().getReference();
        rv_chat.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        rv_chat.setLayoutManager(linearLayoutManager);



    }


    private void toolbar(){


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.user_toolbar);
//        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}


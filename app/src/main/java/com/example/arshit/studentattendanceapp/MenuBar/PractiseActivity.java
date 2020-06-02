package com.example.arshit.studentattendanceapp.MenuBar;

import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.arshit.studentattendanceapp.R;


import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.media.MediaPlayer;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PractiseActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    private Button startbtn, stopbtn, playbtn, stopplay;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private static final String LOG_TAG = "AudioRecording";
    private static String mFileName = null;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    final static int PICK_PDF_CODE = 2342;
    MediaRecorder recorder;
    StorageReference videoRef, imgStorageReference;
    ListView audio_list;
    List<String> audioList;
    DatabaseReference mDatabaseReference;

                        String[] uploads;

//    private static final String MEDIA_PATH = new String(
//            Environment.getExternalStorageDirectory() + "/AudioRecorder/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practise);

        mPlayer = new MediaPlayer();

        audio_list = findViewById(R.id.audio_list);

        audioList = new ArrayList<String>();

        imgStorageReference =    FirebaseStorage.getInstance().getReference();

                startbtn = (Button)findViewById(R.id.btnRecord);


                startbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        chooseImage();

                    }
                });

                playbtn = (Button)findViewById(R.id.btnPlay);

    playbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

//            play();

            showPdf();
        }
    });
    }

    private void showPdf() {



        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Pdf");

        //retrieving upload data from firebase database
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                audioList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    uploads = new String[audioList.size()];
                    for (final DataSnapshot dataSnapshot1 : postSnapshot.getChildren()) {

                        Toast.makeText(PractiseActivity.this, "" + dataSnapshot1.child("url").getValue().toString(), Toast.LENGTH_SHORT).show();
//                        Upload upload = dataSnapshot1.getValue(Upload.class);
//                        audioList.add(upload);

                        audioList.add(dataSnapshot1.child("Name").getValue().toString());


//
//                    for (int i = 0; i < uploads.length; i++) {
//                        uploads[i] = audioList.get(i).getName();
//                    }
                        audio_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                //getting the upload

                                String upload = audioList.get(i);

                                //Opening the upload file in browser using the upload url
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(dataSnapshot1.child("url").getValue().toString()));
                                startActivity(intent);
                            }
                        });
                    }                }


                //displaying it to list
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,audioList);
                audio_list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



//    private void play() {
//
//mPlayer = new MediaPlayer();
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("Audios");
//
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
//                for (final DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
//
//
//
//                   audioList.add(dataSnapshot2.child("Audio").getValue().toString());
//                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PractiseActivity.this, android.R.layout.simple_list_item_1,audioList);
//                    audio_list.setAdapter(arrayAdapter);
//
////                    try {
//////                        mPlayer.setDataSource(dataSnapshot2.child("Audio").getValue().toString());
////
////
////
////                    }                    catch (IOException e) {
////                        e.printStackTrace();
////                    }
//
//                    audio_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                            mPlayer = new MediaPlayer();
////                            playMySong(songPath);
//
////                            if (adapterView.getItemAtPosition(i).toString().equals(dataSnapshot2.child("Audio").getValue().toString())) {
//                                try {
//
//                                    if (mPlayer != null) {
//                                        if (mPlayer.isPlaying()) {
//                                            mPlayer.stop();
//                                            mPlayer.reset();
//                                            mPlayer.release();
//                                            mPlayer = null;
//                                        }
//
//                                        else{
//                                        mPlayer.setDataSource(dataSnapshot2.child("Audio").getValue().toString());
//                                        mPlayer.prepare();
//                                        mPlayer.start();
//                                        }
//
//                                    }
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
////                            else {
////
////                                mPlayer.stop();
////                                mPlayer.reset();
////                                mPlayer.release();
////
////
////                            }
////                        }
//
//                    });
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//    }

    private void chooseImage() {
//        Intent intent_upload = new Intent();
//        intent_upload.setType("audio/*");
//        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent_upload,1);

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){

//            if(resultCode == RESULT_OK){


                final Uri uri1 = data.getData();

                final String uniqueID = UUID.randomUUID().toString();



                                StorageReference filepath = imgStorageReference.child("Pdf").child(uniqueID+".3gp");

                filepath.putFile(uri1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                        if (task.isSuccessful()) {
                            imgStorageReference.child("Pdf").child(uniqueID+".3gp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String d = uri.toString();

                               DatabaseReference     databaseReference = FirebaseDatabase.getInstance().getReference("Pdf");
                                    databaseReference.child(uniqueID).push().child("url").setValue(d);
                                }
                            });


                                  }
                        }
                });

            }
        }
//      }


//                StorageReference filepath = imgStorageReference.child("Audios").child(uniqueID+".3gp");
//
//                filepath.putFile(uri1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//
//
//                        if (task.isSuccessful()) {
//                            imgStorageReference.child("Audios").child(uniqueID+".3gp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//
//                                    String d = uri.toString();
//
//                               DatabaseReference     databaseReference = FirebaseDatabase.getInstance().getReference("Audios");
//                                    databaseReference.child(uniqueID).push().child("Audio").setValue(d);
//                                }
//                            });
//
//
//                                  }
//                        }
//                });
//
//            }
//        }
//      }


}
//

//        stopbtn = (Button)findViewById(R.id.btnStop);
//        playbtn = (Button)findViewById(R.id.btnPlay);
//        stopplay = (Button)findViewById(R.id.btnStopPlay);
//imgStorageReference =    FirebaseStorage.getInstance().getReference();
//databaseReference = FirebaseDatabase.getInstance().getReference();
//
//        stopbtn.setEnabled(false);
//        playbtn.setEnabled(false);
//        stopplay.setEnabled(false);
//
//        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
//        mFileName += "/AudioRecording.3gp";
//
//        startbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(CheckPermissions()) {
//                    stopbtn.setEnabled(true);
//                    startbtn.setEnabled(false);
//                    playbtn.setEnabled(false);
//                    stopplay.setEnabled(false);
//                    mRecorder = new MediaRecorder();
//                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//
//                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//
//                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//
//
//                    mRecorder.setOutputFile(mFileName);
//
//                    try {
//                        mRecorder.prepare();
//                    } catch (IOException e) {
//                        Log.e(LOG_TAG, "prepare() failed");
//                    }
//                    mRecorder.start();
//                    Toast.makeText(getApplicationContext(), "Recording Started", Toast.LENGTH_LONG).show();
//                }
//                else
//                {
//                    RequestPermissions();
//                }
//            }
//        });
//
//
//        stopbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stopbtn.setEnabled(false);
//                startbtn.setEnabled(true);
//                playbtn.setEnabled(true);
//                stopplay.setEnabled(true);
//                mRecorder.stop();
//                mRecorder.release();
//                mRecorder = null;
//                Toast.makeText(getApplicationContext(), "Recording Stopped", Toast.LENGTH_LONG).show();
//            }
//        });
//
//
//        playbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stopbtn.setEnabled(false);
//                startbtn.setEnabled(true);
//                playbtn.setEnabled(false);
//
//                stopplay.setEnabled(true);
//uploadData(Uri.parse(mFileName));
//                databaseReference.child("Audio").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
//                            try {
//
//                                mPlayer = new MediaPlayer();
////
////                                Toast.makeText(PractiseActivity.this, "Audio Start", Toast.LENGTH_SHORT).show();
////
////                                mPlayer.setDataSource(String.valueOf(dataSnapshot1.child("Audio").getValue().toString()));
////
////                                Toast.makeText(PractiseActivity.this, "Audio End Start", Toast.LENGTH_SHORT).show();
//
////
//
////                               Toast.makeText(PractiseActivity.this, "Audio Start", Toast.LENGTH_SHORT).show();
////
////                                mPlayer.setDataSource(String.valueOf(dataSnapshot1.child("Audio2").getValue().toString()));
////
////                                Toast.makeText(PractiseActivity.this, "Audio End Start", Toast.LENGTH_SHORT).show();
////
//
//                                Toast.makeText(PractiseActivity.this, "start", Toast.LENGTH_SHORT).show();
//
//                                mPlayer.setDataSource(String.valueOf(dataSnapshot1.child("Audio1").getValue().toString()));
//
//                                Toast.makeText(PractiseActivity.this, "end", Toast.LENGTH_SHORT).show();
//
//
//                                mPlayer.prepare();
//                                mPlayer.start();
//
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
////                    uploadData(Uri.parse(mFileName));
//
//                Toast.makeText(getApplicationContext(), "Recording Started Playing", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        stopplay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPlayer.release();
//                mPlayer = null;
//                stopbtn.setEnabled(false);
//                startbtn.setEnabled(true);
//                playbtn.setEnabled(true);
//                stopplay.setEnabled(false);
//                Toast.makeText(getApplicationContext(),"Playing Audio Stopped", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//



//    private void uploadData(Uri videoUri) {
//        final String uniqueID = UUID.randomUUID().toString();
//
//            Uri file = Uri.fromFile(new File(String.valueOf(videoUri)));
//            StorageReference riversRef = imgStorageReference.child("Audio/"+uniqueID +file.getLastPathSegment());
//
//
//        UploadTask uploadTask1 = riversRef.putFile(file);
//        Task<Uri> downloadUrl = riversRef.getDownloadUrl();
//
//        databaseReference.child("Audio").child(uniqueID).child("Audio").setValue(riversRef.getDownloadUrl().toString());
////
//        databaseReference.child("Audio").child(uniqueID).child("Audio1").setValue(file.toString());
//
//        databaseReference.child("Audio").child(uniqueID).child("Audio2").setValue(uniqueID +file.getLastPathSegment());
//
////        databaseReference.child("Audio").child(uniqueID).child("Audio4").setValue(downloadUrl.toString());
////
////        databaseReference.child("Audio").child(uniqueID).child("Audio5").setValue(riversRef.getFile(file));
//
////        uploadTask1.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
////            @Override
////            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////
////            }
////        });
//        //
//
//
//
////        Toast.makeText(this, "Download url \n ="+downloadUrl.toString(), Toast.LENGTH_SHORT).show();
//
//
//        imgStorageReference.child("Audio/"+uniqueID +file.getLastPathSegment()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//
//                // Got the download URL for 'users/me/profile.png'
//
//                String downloadUrl = uri.toString();
//
//
//                databaseReference.child(uniqueID).child("Audio4").setValue(downloadUrl);
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });
//
//
//
//
//
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_AUDIO_PERMISSION_CODE:
//                if (grantResults.length> 0) {
//                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    boolean permissionToStore = grantResults[1] ==  PackageManager.PERMISSION_GRANTED;
//                    if (permissionToRecord && permissionToStore) {
//                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_LONG).show();
//                    }
//                }
//                break;
//        }
//    }
//    public boolean CheckPermissions() {
//        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
//        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
//        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
//    }
//    private void RequestPermissions() {
//        ActivityCompat.requestPermissions(PractiseActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
//    }

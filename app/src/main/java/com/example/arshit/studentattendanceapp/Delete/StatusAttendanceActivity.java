package com.example.arshit.studentattendanceapp.Delete;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.arshit.studentattendanceapp.Model.AttendanceModel;
import com.example.arshit.studentattendanceapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusAttendanceActivity extends AppCompatActivity {

    RecyclerView uRecyclerView;
    FirebaseRecyclerAdapter adapter;

    private DatabaseReference Rootref,Root;
    String selected_user_id;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    String currentUserId,currentName,CCode;

    private String Subjectvalue, ValueName, UserName, UserSubject;

    private ProgressDialog mProgressDialog;
    private StorageReference imgStorageReference;
    int sum = 0;

    int total = 0;
    String CName;
    String CourseName;
    TextView presentTxt,totalTxt,percentatageTxt;
    private FloatingActionButton add_teacher_float;
    View view;
    float percentage;
    int present,absent;
    SharedPreferences myprefs;
    TextView toolbar_title;
    Intent intent;
    HashMap<String,String> hashMap;
    String PSum,Total;
    RelativeLayout InnerRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_attendance);

        intialise();
        showAttendance();
//        totalAttendance();
        toolbar();

    }


    private void toolbar() {


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.user_toolbar);

        toolbar_title =toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(CourseName);
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

    private  void intialise(){

        myprefs = getApplicationContext().getSharedPreferences("user",Context.MODE_PRIVATE);
        currentUserId = myprefs.getString("session_id", null);
        currentName = myprefs.getString("Uname",null);
        intent= getIntent();
        InnerRelativeLayout = findViewById(R.id.InnerRelativeLayout);

        CourseName = intent.getStringExtra("CourseSelected");

        Rootref = FirebaseDatabase.getInstance().getReference();
        Root = FirebaseDatabase.getInstance().getReference("AttendanceTotal");

        totalTxt = findViewById(R.id.total_value);
        presentTxt = findViewById(R.id.present_value);
        percentatageTxt = findViewById(R.id.percentage_value);


        uRecyclerView = findViewById(R.id.list_all_courses1);
        uRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, true));

    }



    private void showAttendance() {

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Attendance").child(CourseName);
        database.keepSynced(true);

        FirebaseRecyclerOptions<AttendanceModel> options = new FirebaseRecyclerOptions.Builder<AttendanceModel>()
                .setQuery(database,AttendanceModel.class).build();

        adapter = new FirebaseRecyclerAdapter<AttendanceModel, StatusAttendanceActivity.UserViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull final StatusAttendanceActivity.UserViewHolder holder, final int position, @NonNull final AttendanceModel attendanceModel) {

                final String selected_user_id = getRef(position).getKey();


                database.child(selected_user_id).orderByKey().equalTo(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        String CName = dataSnapshot.getKey().toString();
                        holder.uName.setText(CName);

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            CCode = snapshot.child(currentName).getValue().toString();
                            if (CCode.equals("Absent")){
                                sum = sum+1;
                            }

                            if (CCode.equals("Present")){

                                total = total+1;
                            }

                            holder.uStatus.setText(CCode);

                        }


                        presentTxt.setText(String.valueOf(total));
                        totalTxt.setText(String.valueOf(Integer.parseInt(String.valueOf(sum))+ (Integer.parseInt(String.valueOf(total)))));
                            percentatageTxt.setText(String.valueOf(Float.parseFloat(presentTxt.getText().toString())/Float.parseFloat(totalTxt.getText().toString())*100)+"%");


                        holder.uSubjectValue.setText("Date");
                        holder.uNameValue.setText("Status");


                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });




            }




            @NonNull
            @Override
            public StatusAttendanceActivity.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_courses, parent, false);

                return new StatusAttendanceActivity.UserViewHolder(view);

            }} ;

        uRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }



    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView profileImg;

        TextView uName,uCode,uStatus,uNameValue,uSubjectValue;
        View  mView;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;


            uName = mView.findViewById(R.id.category_name);
            uStatus = mView.findViewById(R.id.course_code);
            uNameValue = mView.findViewById(R.id.course_code_text);
            uSubjectValue = mView.findViewById(R.id.course_name);

        }



    }

}

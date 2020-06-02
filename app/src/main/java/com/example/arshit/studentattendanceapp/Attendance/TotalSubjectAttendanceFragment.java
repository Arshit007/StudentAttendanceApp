package com.example.arshit.studentattendanceapp.Attendance;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arshit.studentattendanceapp.Model.CourseModel;
import com.example.arshit.studentattendanceapp.R;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.arshit.studentattendanceapp.Model.CourseModel;
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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TotalSubjectAttendanceFragment extends Fragment {

    RecyclerView uRecyclerView;
    View view;

    FirebaseRecyclerAdapter adapter;
    String currentUserId;

    SharedPreferences myprefs;
    String DeptName,DeptField,DeptSpec,SemesterItem,session_id,SessionId;
    Spinner spinner,semesterSpinner;
    List<String> sem;


    FirebaseUser currentFirebaseUser;
    ListView listView;
    DatabaseReference rootRef,databaseReference;

    public TotalSubjectAttendanceFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_total_attendance, container, false);
intialise();
        return view;

    }

    private void intialise() {


        SharedPreferences myprefs= getContext().getSharedPreferences("user",Context.MODE_PRIVATE);
        currentUserId = myprefs.getString("session_id", null);


        uRecyclerView = view.findViewById(R.id.rv_show_total_attendance);
        uRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
showInfo();
//show();//


    }

    private void showInfo() {


        semesterSpinner = view.findViewById(R.id.all_attendace_sem_spinner);

        rootRef = FirebaseDatabase.getInstance().getReference("StudentInfo");
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        session_id= currentFirebaseUser.getUid();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {

                    String id =  dataSnapshot1.child("UserId").getValue().toString();

                    if (id.equals(session_id))
                    {

                        DeptName = dataSnapshot1.child("DeptName").getValue().toString();
                        DeptField = dataSnapshot1.child("DeptField").getValue().toString();
                        DeptSpec = dataSnapshot1.child("DeptSpec").getValue().toString();
                        SessionId = dataSnapshot1.child("Id").getValue().toString();

                        selectSem(DeptName,DeptField,DeptSpec,SessionId);
//                        showCourses(DeptName,DeptField,DeptSpec,SessionId, SemesterItem);
                    }

                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void selectSem(final String deptName, final String deptField, final String deptSpec, String sessionId) {


        sem = new ArrayList<String>();


        databaseReference = FirebaseDatabase.getInstance().getReference("IndividualAttendance").child(deptName).child(deptField).child(deptSpec).child(sessionId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                sem.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String semName = dataSnapshot1.getKey();
                    Toast.makeText(getContext(), ""+semName, Toast.LENGTH_SHORT).show();
                    sem.add(semName);

                }



                ArrayAdapter<String> SemAdpater = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,sem);
                SemAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                semesterSpinner.setAdapter(SemAdpater);





            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                SemesterItem = adapterView.getItemAtPosition(i).toString();

                showCourses(deptName,deptField,deptSpec,SemesterItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void showCourses(String deptName, String deptField, String deptSpec, String semesterItem) {

        final DatabaseReference db = FirebaseDatabase.getInstance().getReference("IndividualAttendance").child(deptName).child(deptField).child(deptSpec).child(SessionId).child(semesterItem);


        FirebaseRecyclerOptions<CourseModel> options = new FirebaseRecyclerOptions.Builder<CourseModel>()
                .setQuery(db,CourseModel.class).build();


        adapter = new FirebaseRecyclerAdapter<CourseModel,TotalSubjectAttendanceFragment.UserViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull final TotalSubjectAttendanceFragment.UserViewHolder holder, final int position, @NonNull final CourseModel courseModel) {

                final String selected_user_id = getRef(position).getKey();
                db.child(selected_user_id).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int Psum = 0, Absum = 0;

                        holder.uNameValue.setText(dataSnapshot.getKey());
                        holder.uName.setText("Course Name :");


//                                    Toast.makeText(getContext(), ""+dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


//                            Toast.makeText(getContext(), "" + snapshot.child("Status").getValue().toString(), Toast.LENGTH_SHORT).show();

                            if (snapshot.child("Status").getValue().toString().equals("Present")) {
                                Psum = Psum + 1;
                            }

                            if (snapshot.child("Status").getValue().toString().equals("Absent")) {
                                Absum = Absum + 1;

                            }

                        }
                        holder.uPresent.setText("Class Attended");
                        holder.uPercentage.setText("Perecentage =");
                        holder.uTotal.setText("Total Classes =");

                        holder.uPresentValue.setText(String.valueOf(Psum));

                        if(position % 2 == 0){
                            holder.relativeLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.pink50));
                        }

                        else {
                            holder.relativeLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.teal));

                        }
                        holder.uTotalValue.setText(String.valueOf(Integer.parseInt(String.valueOf(Psum)) + (Integer.parseInt(String.valueOf(Absum)))));
                        holder.uPercentageValue.setText(String.valueOf(Float.parseFloat(holder.uPresentValue.getText().toString()) / Float.parseFloat(holder.uTotalValue.getText().toString()) * 100) + "%");



                    }






                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });



            }




            @NonNull
            @Override
            public TotalSubjectAttendanceFragment.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {


                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_all_attendance,parent, false);

                return new TotalSubjectAttendanceFragment.UserViewHolder(view);

            }

        } ;


        uRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }



    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView profileImg;
        ListView listView;
        TextView uName, uStatus, Uid, uNameValue, uSubjectValue,uTotal,uPresent,uPresentValue,uTotalValue,uPercentage,uPercentageValue;
        View mView;
        LinearLayout displayLinearLayout;
        RelativeLayout relativeLayout;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            displayLinearLayout = mView.findViewById(R.id.displayLinearLayout);
            relativeLayout = mView.findViewById(R.id.relativeLayout);
            uName = mView.findViewById(R.id.course_code_text);
            uNameValue = mView.findViewById(R.id.course_code);
            uTotal = mView.findViewById(R.id.total_attend);
            uTotalValue = mView.findViewById(R.id.total_attend_value);
            uPresent = mView.findViewById(R.id.class_attend);
            uPresentValue = mView.findViewById(R.id.class_attend_value);
            uPercentage = mView.findViewById(R.id.percentage);
            uPercentageValue = mView.findViewById(R.id.percentage_value);


        }



    }
}
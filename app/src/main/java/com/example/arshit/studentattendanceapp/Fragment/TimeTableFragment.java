package com.example.arshit.studentattendanceapp.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.arshit.studentattendanceapp.CourseRegister;
import com.example.arshit.studentattendanceapp.Model.CourseModel;
import com.example.arshit.studentattendanceapp.R;
import com.example.arshit.studentattendanceapp.TimeTable.MondayFragment;
import com.example.arshit.studentattendanceapp.TimeTable.TimeTableMainActivity;
import com.example.arshit.studentattendanceapp.TimeTable.TuesdayFragment;
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
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class TimeTableFragment extends Fragment  {

    View view;

    List<String> sem;

    String DeptName,DeptField,DeptSpec,SemesterItem,session_id,SessionId;
    FirebaseUser currentFirebaseUser;
    TabLayout tabLayout;
     MondayFragment mondayFragment;
    TuesdayFragment tuesdayFragment;
    ViewPager vpPager;
    Spinner semesterSpinner;
    DatabaseReference rootRef,databaseReference;
    Button btnSubmit;
    Locale myLocale;

    public TimeTableFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_time_table, container, false);
        intialise();
        return view;


    }



    private void intialise() {

        semesterSpinner = view.findViewById(R.id.time_table_spinner);

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        session_id= currentFirebaseUser.getUid();
        sem = new ArrayList<String>();

        btnSubmit = (Button)view.findViewById(R.id.btn_see_time_table);

        rootRef = FirebaseDatabase.getInstance().getReference("StudentInfo");

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


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), TimeTableMainActivity.class);
                intent.putExtra("SemesterItem",SemesterItem);
                startActivity(intent);

            }
        });

           }



    private void selectSem(String deptName, String deptField, String deptSpec, String sessionId) {




        databaseReference = FirebaseDatabase.getInstance().getReference().child(deptName).child(deptField).child(deptSpec);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                sem.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String semName = dataSnapshot1.getKey();
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

             }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }




}





package com.example.arshit.studentattendanceapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.arshit.studentattendanceapp.CourseRegister;
import com.example.arshit.studentattendanceapp.FragmentManagement.FragmentHandler;
import com.example.arshit.studentattendanceapp.Model.Category;
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
import java.util.Objects;
import java.util.UUID;


public class HomeFragment extends Fragment {

    SharedPreferences myprefs;
    String DeptName,DeptField,DeptSpec,SemesterItem,session_id,SessionId;
    Spinner spinner,semesterSpinner;
    List<String> sem;


    FirebaseUser currentFirebaseUser;
ListView listView;
RecyclerView         uRecyclerView;
    DatabaseReference rootRef,databaseReference;

    String id,sessionId;
    View view;

    FloatingActionButton floatingActionButton;


    public HomeFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

//        toolbar();

        intialise();



        return view;

    }


    private void intialise() {

        uRecyclerView = view.findViewById(R.id.rv_show_course_detail);
        uRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        session_id= currentFirebaseUser.getUid();


        semesterSpinner = view.findViewById(R.id.course_sem_spinner);

        sessionId = UUID.randomUUID().toString();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", sessionId);

        rootRef = FirebaseDatabase.getInstance().getReference("StudentInfo");

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {

                     id =  dataSnapshot1.child("UserId").getValue().toString();

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



        floatingActionButton = view.findViewById(R.id.addRegisterCourse);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), CourseRegister.class);
//                intent.putExtra("session_id",currentFirebaseUser.getUid());
                startActivity(intent);



            }
        });

    }

    private void selectSem(final String deptName, final String deptField, final String deptSpec, String sessionId) {


        sem = new ArrayList<String>();


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

                showCourses(deptName,deptField,deptSpec,SemesterItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void showCourses(String deptName, String deptField, String deptSpec,String semesterItem) {

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("StudentCoursesOpted").child(deptName).child(deptField).child(deptSpec).child(semesterItem).child(SessionId);

        database.keepSynced(true);

        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(database, Category.class).build();

        FirebaseRecyclerAdapter adapter1 = new FirebaseRecyclerAdapter<Category,HomeFragment.UserViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull final HomeFragment.UserViewHolder holder,final int position, @NonNull final Category category) {


                String selected_user_id1 = getRef(position).getKey();

                database.child(selected_user_id1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(position % 2 == 0){
                            holder.constraintLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue));
                        }

                        else {
                            holder.constraintLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.yellow));

                        }

                        holder.uSubjectName.setText(String.valueOf(dataSnapshot.getKey()));
                        holder.uTeacherName.setText(String.valueOf(dataSnapshot.child("CourseCode").getValue().toString()));
                        holder.uRoomNumber.setText(String.valueOf(dataSnapshot.child("CourseCredit").getValue().toString()));


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });




            }

            @NonNull
            @Override
            public HomeFragment.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_course, parent, false);
                return new HomeFragment.UserViewHolder(view);

            }

        } ;


        uRecyclerView.setAdapter(adapter1);
        adapter1.startListening();




    }




    public static class UserViewHolder extends RecyclerView.ViewHolder  {

        TextView uSubjectName,uTeacherName,uTimeSlot,uRoomNumber;
        View  mView;
        ConstraintLayout constraintLayout;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;


            uSubjectName = mView.findViewById(R.id.subjectexams);
            uTeacherName = mView.findViewById(R.id.teacherexams);
//            uTimeSlot = mView.findViewById(R.id.timeexams);
            uRoomNumber = mView.findViewById(R.id.roomexams);

constraintLayout = mView.findViewById(R.id.ConstraintLayout);

        }



    }



}

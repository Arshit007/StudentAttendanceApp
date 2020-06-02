package com.example.arshit.studentattendanceapp.Assignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.arshit.studentattendanceapp.Fragment.HomeFragment;
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


public class ShowAssignmentFragment extends Fragment {

    String DeptName,DeptField,DeptSpec,SemesterItem,session_id,SessionId,CourseItem;
    Spinner spinner,semesterSpinner,courseSpinner;
    List<String> sem,courseList;


    FirebaseUser currentFirebaseUser;
    RecyclerView  uRecyclerView;
    DatabaseReference rootRef,databaseReference;

    String id;
    View view;
    DatabaseReference database;




    public ShowAssignmentFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_show_assignment, container, false);


intialise();
        return view;

    }

    private void intialise() {

        uRecyclerView = view.findViewById(R.id.see_assignment_rv);
        uRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        session_id= currentFirebaseUser.getUid();

        database = FirebaseDatabase.getInstance().getReference();

        courseSpinner = view.findViewById(R.id.CourseAsignspinner);

        semesterSpinner = view.findViewById(R.id.SemesterspinnerAssign);

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

    }






    private void selectSem(final String deptName, final String deptField, final String deptSpec, final String session_id) {


        database = FirebaseDatabase.getInstance().getReference().child("IndividualAssignment").child(deptName).child(deptField).child(deptSpec).child(session_id);


sem = new ArrayList<>();

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                sem.clear();

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
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

    private void showCourses(final String deptName, final String deptField, final String deptSpec, String semesterItem) {


        database = FirebaseDatabase.getInstance().getReference().child("IndividualAssignment").child(deptName).child(deptField).child(deptSpec).child(SessionId).child(semesterItem);


         courseList = new ArrayList<>();

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                courseList.clear();

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    String semName = dataSnapshot1.getKey();
                    courseList.add(semName);

                }
                ArrayAdapter<String> CourseAdpater = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,courseList);
                CourseAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                courseSpinner.setAdapter(CourseAdpater);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                CourseItem = adapterView.getItemAtPosition(i).toString();

                showAssignment(deptName,deptField,deptSpec,SessionId,SemesterItem,CourseItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    private void showAssignment(String deptName, String deptField, String deptSpec, String sessionId, String semesterItem, String courseItem) {

        final DatabaseReference database12 = FirebaseDatabase.getInstance().getReference().child("IndividualAssignment").child(deptName).child(deptField).child(deptSpec).child(sessionId).child(semesterItem).child(courseItem);
        database12.keepSynced(true);

        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(database12, Category.class).build();

        FirebaseRecyclerAdapter adapter1 = new FirebaseRecyclerAdapter<Category,ShowAssignmentFragment.UserViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull final ShowAssignmentFragment.UserViewHolder holder, final int position, @NonNull final Category category) {

                String selected_user_id1 = getRef(position).getKey();

                database12.child(selected_user_id1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


//                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {



                            holder.uRoomNumber.setText(dataSnapshot.child("Status").getValue().toString());
                            holder.uTxtId.setText(dataSnapshot.child("PdfUrl").getValue().toString());
                            holder.uTxtId.setVisibility(View.GONE);

                            if (dataSnapshot.child("Status").getValue().toString().equals("Submitted")){

                                holder.uTeacherNameValue.setText("Date Submitted");
                                holder.uTeacherName.setText(String.valueOf(dataSnapshot.child("DateOfSubmissionStudent").getValue().toString()));
                                holder.uDownTxt.setVisibility(View.GONE);
                                if (String.valueOf(dataSnapshot.child("Grade").getValue().toString()).equals("")) {
                                    holder.uGrade.setVisibility(View.GONE);
                                }
                                else {

                                    holder.uGrade.setVisibility(View.VISIBLE);
                                    holder.uGrade.setText("GRADE : " + String.valueOf(dataSnapshot.child("Grade").getValue().toString()));

                                }
                            }

                            else {
                                holder.uDownTxt.setVisibility(View.VISIBLE);

                                holder.uTeacherNameValue.setText("Submission Date :");
                                holder.uTeacherName.setText(String.valueOf(dataSnapshot.child("SubmitDate").getValue().toString()));

                            }

                            holder.uSubjectName.setText("Assignment No : "+String.valueOf(dataSnapshot.child("AssignNo").getValue().toString()));

                            holder.uSubjectNameValue.setText("Status : ");


                            if(position % 2 == 0){
                            holder.constraintLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue));
                        }

                        else {
                            holder.constraintLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.yellow));

                        }
//
// }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });



                holder.uDownTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(String.valueOf(holder.uTxtId.getText().toString())));
                        startActivity(intent);
                    }
                });


            }

            @NonNull
            @Override
            public ShowAssignmentFragment.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_course, parent, false);
                return new ShowAssignmentFragment.UserViewHolder(view);

            }

        } ;


        uRecyclerView.setAdapter(adapter1);
        adapter1.startListening();


    }



    public static class UserViewHolder extends RecyclerView.ViewHolder  {

        TextView uSubjectNameValue,uTeacherNameValue,uSubjectName,uTeacherName,uGrade,uRoomNumber,uTxtId,uDownTxt;
        View  mView;
        ConstraintLayout constraintLayout;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

   uGrade = mView.findViewById(R.id.grade);
            uSubjectName = mView.findViewById(R.id.subjectexams);
            uTeacherName = mView.findViewById(R.id.teacherexams);

            uTxtId = mView.findViewById(R.id.txt_pdf_url);
            uDownTxt = mView.findViewById(R.id.txt_pdf_url1);

            uRoomNumber = mView.findViewById(R.id.roomexams);


            uTeacherNameValue = mView.findViewById(R.id.teacherName);
            uSubjectNameValue = mView.findViewById(R.id.roomValue);
            constraintLayout = mView.findViewById(R.id.ConstraintLayout);

        }



    }


}
package com.example.arshit.studentattendanceapp.TimeTable;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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


public class ThursdayFragment extends Fragment {

    RecyclerView uRecyclerView;

    private DatabaseReference rootRef,database1,databaseReference;

    String SemesterItem;

    String DeptName,DeptField,DeptSpec,session_id,SessionId;
    FirebaseUser currentFirebaseUser;
    View view;
    RelativeLayout relativeLayout;



    public ThursdayFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_thursday, container, false);


    intialise();



        return view;
}

    private void intialise() {

        SemesterItem = getArguments().getString("Semester");


        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        session_id= currentFirebaseUser.getUid();



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

                        showCourses(DeptName,DeptField,DeptSpec,SessionId, SemesterItem);
                    }

                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        relativeLayout = view.findViewById(R.id.txt_Thurs);
        relativeLayout.setVisibility(View.VISIBLE);

        uRecyclerView = view.findViewById(R.id.rv_thurs);
        uRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }

    private void showCourses(String deptName, String deptField, String deptSpec, String sessionId, String semesterItem) {

        database1 = FirebaseDatabase.getInstance().getReference("IndividualTimeTable").child(deptName).child(deptField).child(deptSpec).child(semesterItem).child("Thursday");
        databaseReference = FirebaseDatabase.getInstance().getReference("StudentCoursesOpted").child(deptName).child(deptField).child(deptSpec).child(semesterItem).child(sessionId);

        database1.keepSynced(true);

        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(database1, Category.class).build();

        FirebaseRecyclerAdapter       adapter1 = new FirebaseRecyclerAdapter<Category,ThursdayFragment.UserViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull final ThursdayFragment.UserViewHolder holder, final int position, @NonNull final Category category) {


                String selected_user_id1 = getRef(position).getKey();

                database1.child(selected_user_id1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        relativeLayout.setVisibility(View.INVISIBLE);
                        if(position % 2 == 0){
                            holder.constraintLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue));
                        }

                        else {
                            holder.constraintLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.pink));

                        }



                        for(final DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                            databaseReference.child(dataSnapshot1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot dataSnapshot11:dataSnapshot.getChildren()) {
                                        holder.uSubjectName.setText(String.valueOf(dataSnapshot1.child("CourseName").getValue().toString()));
                                        holder.uTeacherName.setText(String.valueOf(dataSnapshot1.child("TeacherName").getValue().toString()));
                                        holder.uTimeSlot.setText(String.valueOf(dataSnapshot1.child("Time").getValue().toString()));
                                        holder.uRoomNumber.setText(String.valueOf(dataSnapshot1.child("Room").getValue().toString()));
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });




            }

            @NonNull
            @Override
            public ThursdayFragment.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_table, parent, false);
                return new ThursdayFragment.UserViewHolder(view);

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


            uSubjectName = mView.findViewById(R.id.subjectexams1);
            uTeacherName = mView.findViewById(R.id.teacherexams1);
            uTimeSlot = mView.findViewById(R.id.timeexams1);
            uRoomNumber = mView.findViewById(R.id.roomexams1);

            constraintLayout = mView.findViewById(R.id.ConstraintLayout);


        }



    }




}
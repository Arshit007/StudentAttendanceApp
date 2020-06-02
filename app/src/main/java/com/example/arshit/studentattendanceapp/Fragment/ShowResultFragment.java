package com.example.arshit.studentattendanceapp.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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


public class ShowResultFragment extends Fragment {

    private DatabaseReference database,RootRef;
 SharedPreferences myprefs;

 TextView Cgpa,SGPA,cgpaCredit,sgpaCredit;
    List<String>  semList;
    int  Ci = 0,CixGi = 0;
    String DeptName,DeptField,DeptSpec,SessionId,CourseItem;

    String grade,Ccredit;
    RecyclerView uRecyclerView;

    DatabaseReference Rootref;
    FirebaseRecyclerAdapter adapter;
    Spinner semSpinner;
    FirebaseUser currentFirebaseUser;

    String currentUserId,semItem,session_id;

     View view;

    public ShowResultFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_result, container, false);



        intialise();

        return view;

    }


    private void intialise() {


         SGPA = view.findViewById(R.id.txt_all_Sem_sgpa_value);
        Cgpa = view.findViewById(R.id.txt_Current_Sem_sgpa_value);


cgpaCredit = view.findViewById(R.id.txt_all_Sem_credit_value);
        sgpaCredit = view.findViewById(R.id.txt_Current_Sem_credit_value);


         semSpinner = view.findViewById(R.id.rst_sem_spin);

        uRecyclerView = view.findViewById(R.id.Show_result_Recycler);
        uRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));


        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        session_id= currentFirebaseUser.getUid();

semList = new ArrayList<>();

        Rootref = FirebaseDatabase.getInstance().getReference("StudentInfo");

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
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

                        showResult(DeptName,DeptField,DeptSpec,SessionId);
//                        showCourses(DeptName,DeptField,DeptSpec,SessionId, SemesterItem);
                    }

                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        showResult(DeptName, DeptField, DeptSpec, SessionId);


    }

    private void showResult(final String deptName, final String deptField, final String deptSpec, final String sessionId) {


        RootRef = FirebaseDatabase.getInstance().getReference().child("ResultOverall")
                                                               .child(deptName).child(deptField).child(deptSpec)
                                                              .child(sessionId);
semList.clear();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                for (final DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {


                    semList.add(dataSnapshot1.getKey());
//                    String result = dataSnapshot.child("Semester1").getValue().toString();

                    ArrayAdapter<String> SemAdpater = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, semList);
                    SemAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    semSpinner.setAdapter(SemAdpater);
                    semSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            semItem = adapterView.getItemAtPosition(i).toString();

                            Cgpa.setText(dataSnapshot.child(semItem).child("SGCPA").getValue().toString());
                            SGPA.setText(dataSnapshot.child(semItem).child("CGPA").getValue().toString());
                            cgpaCredit.setText(dataSnapshot.child(semItem).child("TotalCredit").getValue().toString());
                            sgpaCredit.setText(dataSnapshot.child(semItem).child("Credit").getValue().toString());

                            showOverallResult(deptName, deptField, deptSpec, semItem, sessionId);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                }            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    private void showOverallResult(String deptName, String deptField, String deptSpec, String semItem, String sessionId) {
        database = FirebaseDatabase.getInstance().getReference().child("Result").
                 child(deptName).child(deptField).child(deptSpec)
                .child(sessionId).child(semItem);



        FirebaseRecyclerOptions<CourseModel> options = new FirebaseRecyclerOptions.Builder<CourseModel>()
                .setQuery(database,CourseModel.class).build();


        adapter = new FirebaseRecyclerAdapter<CourseModel, ShowResultFragment.UserViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull final ShowResultFragment.UserViewHolder holder, int position, @NonNull final CourseModel courseModel) {


                final String selected_user_id = getRef(position).getKey();
                database.child(selected_user_id).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        grade = dataSnapshot.child("Grade").getValue().toString();
                        Ccredit = dataSnapshot.child("CourseCredit").getValue().toString();

                        Ci = Ci + Integer.parseInt(String.valueOf(Ccredit));



                        holder.uCourseTotalCredit.setText(Ccredit);
                        holder.uCourseName.setText(dataSnapshot.getKey());
                        holder.uGrade.setText(grade);


                        if (grade.equals("O(90-100)")){

                            holder.uCreditEarned.setText(Ccredit);
                            CixGi  =  CixGi + Integer.parseInt(String.valueOf(Ccredit))*10;
                        }
                        else if (grade.equals("A+(80-89)")){

                            holder.uCreditEarned.setText(Ccredit);
                            CixGi  =  CixGi + Integer.parseInt(String.valueOf(Ccredit))*9;
                        }
                        else if (grade.equals("A(70-79)")){
                            holder.uCreditEarned.setText(Ccredit);

                            CixGi  =  CixGi + Integer.parseInt(String.valueOf(Ccredit))*8;
                        }
                        else if (grade.equals("B+(60-69)")){

                            holder.uCreditEarned.setText(Ccredit);
                            CixGi  =  CixGi + Integer.parseInt(String.valueOf(Ccredit))*7;
                        }
                        else if (grade.equals("B(50-59)")){

                            holder.uCreditEarned.setText(Ccredit);
                            CixGi  =  CixGi + Integer.parseInt(String.valueOf(Ccredit))*6;
                        }
                        else if (grade.equals("C(40-49)")){

                            holder.uCreditEarned.setText(Ccredit);
                            CixGi  =  CixGi + Integer.parseInt(String.valueOf(Ccredit))*5;
                        }
                        else if (grade.equals("P(30-39)")){

                            holder.uCreditEarned.setText(Ccredit);
                            CixGi  =  CixGi + Integer.parseInt(String.valueOf(Ccredit))*4;
                        }

                        else if (grade.equals("F(29 and below)")){
                            holder.uCreditEarned.setText("0");

                            CixGi  =  CixGi + Integer.parseInt(String.valueOf(Ccredit))*0;
                        }
                        else
//                        if (grade.equals("Ab(Absent)"))
                        {

                            holder.uCreditEarned.setText("0");
                            CixGi  =  CixGi + Integer.parseInt(String.valueOf(Ccredit))*0;
                        }
//
////                        Toast.makeText(getContext(), "CI*GI \n "+CixGi, Toast.LENGTH_SHORT).show();
////
//
//                      float sum =   Float.parseFloat(String.valueOf(Float.parseFloat(String.valueOf(CixGi))/Float.parseFloat(String.valueOf(Ci))));
//
//                        DecimalFormat df = new DecimalFormat();
//                        df.setMaximumFractionDigits(2);

//                        Toast.makeText(getContext(), "SGPA"+df.format(sum), Toast.LENGTH_SHORT).show();


                    }




                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });



            }




            @NonNull
            @Override
            public ShowResultFragment.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {


                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_result,parent, false);

                return new ShowResultFragment.UserViewHolder(view);

            }

        } ;


        uRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView profileImg;
        ListView listView;
    TextView uCourseName,uCourseTotalCredit,uCreditEarned,uGrade;
        View mView;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;


            uCourseName = mView.findViewById(R.id.course_name_text);
            uCourseTotalCredit = mView.findViewById(R.id.course_total_credit);
            uCreditEarned = mView.findViewById(R.id.course_total_credit_earned);
            uGrade = mView.findViewById(R.id.course_total_grade);


        }




    }


}

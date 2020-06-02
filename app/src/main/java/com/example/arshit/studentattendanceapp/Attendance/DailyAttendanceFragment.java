package com.example.arshit.studentattendanceapp.Attendance;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class DailyAttendanceFragment extends Fragment {

    RecyclerView uRecyclerView;
    FirebaseRecyclerAdapter adapter;

    private DatabaseReference Rootref,databaseReference;
Spinner SemesterSpinner,courseSpinner;
String SemesterItem;
FirebaseUser currentFirebaseUser;
String session_id;
    List<String> sem,courseList;
    int sum = 0;

    int total = 0;

    TextView presentTxt,totalTxt,percentatageTxt;
    String DeptName,DeptField,DeptSpec,SessionId,CourseItem;
    View view;

    public DailyAttendanceFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_daily_attendance, container, false);
intialise();
        return view;

    }

    private void intialise() {

        totalTxt = view.findViewById(R.id.total_value1);
        presentTxt = view.findViewById(R.id.present_value1);
        percentatageTxt = view.findViewById(R.id.percentage_value1);

        SemesterSpinner = view.findViewById(R.id.attendance_sem_spinner);
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        uRecyclerView = view.findViewById(R.id.rv_show_attendance_detail);
        uRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        courseSpinner = view.findViewById(R.id.course_select_spinner);
        session_id= currentFirebaseUser.getUid();

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

    private void selectSem(final String deptName, final String deptField, final String deptSpec, final String sessionId) {


        sem = new ArrayList<String>();
        sem.clear();


        databaseReference = FirebaseDatabase.getInstance().getReference("IndividualAttendance").child(deptName).child(deptField).child(deptSpec).child(sessionId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String semName = dataSnapshot1.getKey();
                    sem.add(semName);

                }


                ArrayAdapter<String> SemAdpater = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, sem);
                SemAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                SemesterSpinner.setAdapter(SemAdpater);


            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        SemesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                SemesterItem = adapterView.getItemAtPosition(i).toString();


                selectCourse(deptName,deptField,deptSpec,SemesterItem,sessionId);
//                showCourses(deptName,deptField,deptSpec,SemesterItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void selectCourse(final String deptName, final String deptField, final String deptSpec, String semesterItem, final String sessionId) {


        courseList = new ArrayList<String>();
        courseList.clear();

     DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference("IndividualAttendance").child(deptName).child(deptField).child(deptSpec).child(sessionId).child(semesterItem);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String semName = dataSnapshot1.getKey();
                    courseList.add(semName);

                }



                ArrayAdapter<String> courseAdpater = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,courseList);
                courseAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                courseSpinner.setAdapter(courseAdpater);





            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                CourseItem = adapterView.getItemAtPosition(i).toString();


                showDetail(deptName,deptField,deptSpec,SemesterItem, sessionId,CourseItem);
//                showCourses(deptName,deptField,deptSpec,SemesterItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void showDetail(String deptName, String deptField, String deptSpec, String semesterItem, String sessionId, String courseItem) {
        databaseReference = FirebaseDatabase.getInstance().getReference("IndividualAttendance").child(deptName).child(deptField).child(deptSpec).child(sessionId).child(semesterItem).child(courseItem);

        databaseReference.keepSynced(true);

        FirebaseRecyclerOptions<AttendanceModel> options = new FirebaseRecyclerOptions.Builder<AttendanceModel>()
                .setQuery(databaseReference,AttendanceModel.class).build();

        adapter = new FirebaseRecyclerAdapter<AttendanceModel, DailyAttendanceFragment.UserViewHolder>(options) {




            @Override
            protected void onBindViewHolder(@NonNull final DailyAttendanceFragment.UserViewHolder holder, final int position, @NonNull final AttendanceModel attendanceModel) {

                final String selected_user_id = getRef(position).getKey();


                databaseReference.child(selected_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        holder.uName.setText(dataSnapshot.child("Status").getValue().toString());

                            if (dataSnapshot.child("Status").getValue().toString().equals("Absent")){
                                sum = sum+1;
                            }

                            if (dataSnapshot.child("Status").getValue().toString().equals("Present")){

                                total = total+1;
                            }


                        if(position % 2 == 0){
                            holder.relativeLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.organge));
                        }

                        else {
                            holder.relativeLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple));

                        }


//                        presentTxt.setText(String.valueOf(total));
//                        totalTxt.setText(String.valueOf(Integer.parseInt(String.valueOf(sum))+ (Integer.parseInt(String.valueOf(total)))));
//                        percentatageTxt.setText(String.valueOf(Float.parseFloat(presentTxt.getText().toString())/Float.parseFloat(totalTxt.getText().toString())*100)+"%");

                        holder.uNameValue.setText(dataSnapshot.child("Date").getValue().toString());
                        holder.uName.setText("Date :");

                        holder.uPresent.setText("Time Slot");
                        holder.uPercentage.setText("Status");
                        holder.uTotal.setText("Day : ");

                        holder.uPresentValue.setText(dataSnapshot.child("TimeSlot").getValue().toString());
                        holder.uTotalValue.setText(dataSnapshot.child("Day").getValue().toString());
                        holder.uPercentageValue.setText(dataSnapshot.child("Status").getValue().toString());



                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });




            }




            @NonNull
            @Override
            public DailyAttendanceFragment.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_all_attendance, parent, false);

                return new DailyAttendanceFragment.UserViewHolder(view);

            }} ;

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
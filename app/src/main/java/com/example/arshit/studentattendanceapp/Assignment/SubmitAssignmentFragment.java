package com.example.arshit.studentattendanceapp.Assignment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arshit.studentattendanceapp.CourseRegister;
import com.example.arshit.studentattendanceapp.Model.Category;
import com.example.arshit.studentattendanceapp.R;
import com.example.arshit.studentattendanceapp.SideNavigation.HomeMainActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class SubmitAssignmentFragment extends Fragment {

    String DeptName,DeptField,DeptSpec,SemesterItem,session_id,SessionId,CourseItem,TeacherId,DAteOfSubmission;
    Spinner assignNoSpinner,semesterSpinner,courseSpinner;
    List<String> sem,courseList,assignNoList;
    TextView txtSem,txtCourse,txtSelectAssign,txtAssignNo;
    Button btnUpload,btnSelect;


    FirebaseUser currentFirebaseUser;
    RecyclerView  uRecyclerView;
    Uri uri1;
    DatabaseReference rootRef,databaseReference;
    final static int PICK_PDF_CODE = 2342;

    String id,AssignNoItem;
    View view;
    DatabaseReference database;
    StorageReference imgStorageReference;

    private ProgressDialog progressBar;
    HashMap<String,String> hashMap;


    public SubmitAssignmentFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_submit_assigmnent, container, false);

        intialise();

        return view;

    }


    private void intialise() {


        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        session_id= currentFirebaseUser.getUid();

        database = FirebaseDatabase.getInstance().getReference();

        imgStorageReference =    FirebaseStorage.getInstance().getReference();

        courseSpinner = view.findViewById(R.id.CourseAsignspinner1);
        semesterSpinner = view.findViewById(R.id.SemesterspinnerAssign1);
        assignNoSpinner = view.findViewById(R.id.spinner_assignment_no);


        txtSelectAssign = view.findViewById(R.id.txt_file_select);
        txtSem = view.findViewById(R.id.txtSemesterAssign1);
        txtCourse = view.findViewById(R.id.txt_asign1);
        txtAssignNo = view.findViewById(R.id.txt_assignment_no);




        btnSelect = view.findViewById(R.id.btnSelectPdf);
        btnUpload = view.findViewById(R.id.btnUploadPdf);




        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPdf();
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitData();
            }
        });


        assignNoList = new ArrayList<>();
        sem = new ArrayList<>();
        courseList = new ArrayList<>();


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

    private void submitData() {

        if (uri1 != null) {
            uploadUri(uri1);
        } else {
            Toast.makeText(getContext(), "Please Select File to be uploaded", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private  void   uploadUri(Uri uri1){

        final String uniqueID = UUID.randomUUID().toString();
        progressBar = new ProgressDialog(getContext());
        progressBar.setTitle("Saving Changes");
        progressBar.setMessage("Wait untill changes are saved");
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();



        StorageReference filepath = imgStorageReference.child("Pdf").child(uniqueID+".3gp");

        filepath.putFile(uri1)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                        if (task.isSuccessful()) {
                            imgStorageReference.child("Pdf").child(uniqueID+".3gp").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @SuppressLint("ResourceType")
                                @Override
                                public void onSuccess(Uri uri) {

                                    String d = uri.toString();

                                    long date = System.currentTimeMillis();

                                    SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy ");
                                    String dateString = sdf.format(date);

                                    hashMap = new HashMap<>();
                                    hashMap.put("AssignNo",AssignNoItem);
                                    hashMap.put("DateOfSubmissionStudent",dateString);
                                    hashMap.put("SubmitDate",DAteOfSubmission);
                                    hashMap.put("Status","Submitted");
                                    hashMap.put("SpecName", DeptSpec);
                                    hashMap.put("SemName",SemesterItem);
                                    hashMap.put("CourseName",CourseItem);
                                    hashMap.put("Grade","");
                                    hashMap.put("DeptName",DeptName);
                                    hashMap.put("UserUid",session_id);
                                    hashMap.put("EnrollmentNo",SessionId);
                                    hashMap.put("TeacherId",TeacherId);
                                    hashMap.put("DeptField",DeptField);
                                    hashMap.put("PdfUrl",d);


                                    DatabaseReference RooRef = FirebaseDatabase.getInstance().getReference();

                                    DatabaseReference RooRef1 = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference RooRef2 = FirebaseDatabase.getInstance().getReference();


                                    RooRef2.child("SubmitIndividualAssignment")
                                            .child(DeptName).
                                            child(DeptField)
                                            .child(TeacherId)
                                            .child(DeptSpec)
                                            .child(SemesterItem)
                                            .child(CourseItem)
                                            .child(AssignNoItem+SessionId).setValue(hashMap);


                                    RooRef1.child("IndividualAssignment")
                                            .child(DeptName).
                                            child(DeptField)
                                            .child(DeptSpec)

                                            .child(SessionId)
                                            .child(SemesterItem)
                                            .child(CourseItem)
                                            .child(AssignNoItem).setValue(hashMap);

                                    RooRef.child("TeacherManageAssignment")
                                            .child(DeptName).child(DeptField)
                                            .child(TeacherId)
                                            .child(DeptSpec)
                                            .child(SessionId)
                                            .child(SemesterItem)
                                            .child(CourseItem)
                                            .child(AssignNoItem).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){

                                                progressBar.dismiss();

                                                Intent intent = new Intent(getContext(),HomeMainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                progressBar.dismiss();

                                            }
                                        }
                                    });



                                }
                            });


                        }
                    }
                });

    }


    private void selectPdf() {

        Intent intent = new Intent();
        intent.setType("application/pdf");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {


            uri1 = data.getData();

            txtSelectAssign.setText("File Select is \n"+data.getData());

            txtSem.setVisibility(View.VISIBLE);
            semesterSpinner.setVisibility(View.VISIBLE);

            txtCourse.setVisibility(View.VISIBLE);
            courseSpinner.setVisibility(View.VISIBLE);

            txtAssignNo.setVisibility(View.VISIBLE);
            assignNoSpinner.setVisibility(View.VISIBLE);


            btnUpload.setVisibility(View.VISIBLE);



        }
    }

    private void showAssignment(final String deptName, final String deptField, final String deptSpec, String sessionId, String semesterItem, String courseItem) {

        final DatabaseReference assignmentRef = FirebaseDatabase.getInstance().getReference().child("IndividualAssignment").child(deptName).child(deptField).child(deptSpec).child(sessionId).child(semesterItem).child(courseItem);

        assignmentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                assignNoList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    String semName = dataSnapshot1.getKey();
                    assignNoList.add(semName);

                    TeacherId = dataSnapshot1.child("TeacherId").getValue().toString();
                    DAteOfSubmission =  dataSnapshot1.child("SubmitDate").getValue().toString();
                }
                ArrayAdapter<String> CourseAdpater = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,assignNoList);
                CourseAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                assignNoSpinner.setAdapter(CourseAdpater);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        assignNoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                AssignNoItem = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }






    }





package com.example.arshit.studentattendanceapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arshit.studentattendanceapp.Model.Category;
import com.example.arshit.studentattendanceapp.SideNavigation.HomeMainActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CourseRegister extends AppCompatActivity {

    private String Cname;

    FirebaseRecyclerAdapter adapter;
    private DatabaseReference database,Ref,DbRef,DbRef1,CRef;
    HashMap<String, String> hashMap1;
    TextView toolbar_title;
    RecyclerView recyclerViewCheckBox;
    EditText course_name,course_code;
    List<String> sem;
    String DeptName,DeptField,DeptSpec,SemesterItem,session_id,SessionId,CCredit,CCode;
    SharedPreferences myprefs;
    Spinner semesterSpinner;
    Button btnAddCourse;
    RecyclerView recycler_category;
    DatabaseReference databaseReference,rootRef;
    Intent intent;
FirebaseUser currentFirebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_register);

        intialise();
        toolbar();
    }

    private void toolbar() {


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.user_toolbar);

        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Course Registration");

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

    private void intialise() {

        semesterSpinner = findViewById(R.id.Semesterspinner);
        recycler_category = findViewById(R.id.add_register_courses_rv);

         GridLayoutManager glm = new GridLayoutManager(getApplicationContext(), 1);
//    intent = getIntent();
//
    recycler_category.setLayoutManager(glm);
        hashMap1 = new HashMap<>();




        btnAddCourse = findViewById(R.id.btn_add_courses);

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        session_id= currentFirebaseUser.getUid();

        Ref = FirebaseDatabase.getInstance().getReference();

        rootRef = FirebaseDatabase.getInstance().getReference("StudentInfo");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {

                    String id =  dataSnapshot1.child("UserId").getValue().toString();

                    if (id.equals(session_id))
                    {
//                        Toast.makeText(CourseRegister.this, ""+dataSnapshot1.getKey(), Toast.LENGTH_SHORT).show();

                         DeptName = dataSnapshot1.child("DeptName").getValue().toString();
                       DeptField = dataSnapshot1.child("DeptField").getValue().toString();
                  DeptSpec = dataSnapshot1.child("DeptSpec").getValue().toString();
                    SessionId = dataSnapshot1.child("Id").getValue().toString();


                     selectSemester(DeptName,DeptField,DeptSpec);
                        addCourses(DeptName,DeptField,DeptSpec,SessionId);
                    }

                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });









    }

    private void selectSemester(final String deptName, final String deptField, final String deptSpec) {



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



                ArrayAdapter<String> SemAdpater = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,sem);
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

                showCourses(SemesterItem,deptName,deptField,deptSpec);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void addStudent(final String id) {


        DbRef = FirebaseDatabase.getInstance().getReference("CoursesRegister").child(id).child(SemesterItem);
        DbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (final DataSnapshot snapshot:dataSnapshot.getChildren()){

                    DbRef1 = FirebaseDatabase.getInstance().getReference().child("Courses");
                    DbRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                                Cname =      dataSnapshot1.child("CourseName").getValue().toString();


                                if (Cname.equals(snapshot.getValue()))
                                {
                                    CCredit = dataSnapshot1.child("CourseCredit").getValue().toString();

                                    CCode = dataSnapshot1.child("CourseCode").getValue().toString();

                                    HashMap<String,String> hashMap = new HashMap<>();

                                    hashMap.put("CourseCode",CCode);
                                    hashMap.put("CourseCredit",CCredit);
                                    CRef = FirebaseDatabase.getInstance().getReference("CoursesOpted").child(DeptName).child(DeptField).child(DeptSpec).child(SemesterItem);
                                 DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("StudentCoursesOpted").child(DeptName).child(DeptField).child(DeptSpec).child(SemesterItem);


                                 databaseReference.child(id).child(Cname).setValue(hashMap);
                                    CRef.child(Cname).child(id).child("Id").setValue(id);


                                }

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void showCourses(String semesterItem, String deptName, String deptField, String deptSpec) {


        database = FirebaseDatabase.getInstance().getReference().child(deptName).child(deptField).child(deptSpec).child(semesterItem);
        database.keepSynced(true);

        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(database, Category.class).build();

        adapter = new FirebaseRecyclerAdapter<Category, CourseRegister.UserViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull final CourseRegister.UserViewHolder holder, int position, @NonNull final Category category) {

                final String selected_user_id = getRef(position).getKey();

                database.child(selected_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        String CCode = dataSnapshot.child("CourseCode").getValue().toString();
                        String  CName = dataSnapshot.child("CourseName").getValue().toString();

                        holder.uCode.setText(CCode);
                        holder.uName.setText(CName);



        holder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, boolean isChecked) {


          if (compoundButton.isChecked()) {


              hashMap1.put(holder.uCode.getText().toString(), holder.uName.getText().toString());



          }


                else
                {

                    hashMap1.remove(holder.uCode.getText().toString());




                }


    }
});


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public CourseRegister.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_checkbox, parent, false);

                return new CourseRegister.UserViewHolder(view);

            }

        } ;



        recycler_category.setAdapter(adapter);
        adapter.startListening();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImg;
        private TextView uName, uCode;
        CheckBox cbSelect;
        View mView;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            uName = mView.findViewById(R.id.name_course);
            uCode = mView.findViewById(R.id.name_code);

            cbSelect = mView.findViewById(R.id.cbSelect);


        }

    }



    private  void addCourses(String deptName, String deptField, String deptSpec, final String sessionId){

        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Ref.child("CoursesRegister").child(sessionId).child(SemesterItem).setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){

                            Toast.makeText(CourseRegister.this, "Courses Register", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(CourseRegister.this, HomeMainActivity.class);
//                            intent.putExtra("session_id",session_id);
                            addStudent(sessionId);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(intent);

                        }
                    }
                });





            }
        });
    }





}

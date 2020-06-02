package com.example.arshit.studentattendanceapp.Login;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.arshit.studentattendanceapp.R;
import com.example.arshit.studentattendanceapp.SideNavigation.HomeMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignIn extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    DatabaseReference db,db1;
    String uid,uEmail,uName;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    private TextView sign_up_text;
    private CheckBox show_hide_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);



        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

//        if (auth.getCurrentUser() != null) {
//            startActivity(new Intent(SignIn.this,AddStudent.class));
//            finish();
//        }

//         set the view now
        setContentView(R.layout.activity_sign_in);


        db = FirebaseDatabase.getInstance().getReference().child("StudentInfo");
        db1 = FirebaseDatabase.getInstance().getReference().child("StudentInfo");

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        sign_up_text =  findViewById(R.id.sign_up_text);

        btnLogin = (Button) findViewById(R.id.sign_in_button);

        show_hide_password = findViewById(R.id.show_hide_password);


        auth = FirebaseAuth.getInstance();

        sign_up_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(SignIn.this, SignUp.class));
            }
        });

        show_hide_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b){

                    inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }

                else{

                    inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());


                }


            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);




                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressBar.setVisibility(View.GONE);
//                                Toast.makeText(SignIn.this, "working", Toast.LENGTH_SHORT).show();

                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(SignIn.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {


                                    Intent intent = new Intent(SignIn.this, HomeMainActivity.class);
                                    startActivity(intent);
                                    finish();
//                                    db.addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
//                                            {
//
//                                                db1.child(snapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//
////
//
//                               uEmail = dataSnapshot.child("Email").getValue().toString();
//
//                                                if (uEmail.equals(email)){
////
//
//      uid = dataSnapshot.child("Id").getValue().toString();
//      uName = dataSnapshot.child("Name").getValue().toString();
//String  DeptField = dataSnapshot.child("DeptField").getValue().toString();
//String  DeptName = dataSnapshot.child("DeptName").getValue().toString();
//String  DeptSpec = dataSnapshot.child("DeptSpec").getValue().toString();
//String AdmissionYear = dataSnapshot.child("AdmissionYear").getValue().toString();
//
//
//      Intent intent = new Intent(SignIn.this, HomeMainActivity.class);
////      intent.putExtra("Uname",uName);
////      intent.putExtra("UserId",uid);
////      intent.putExtra("DeptName",DeptName);
////      intent.putExtra("DeptField",DeptField);
////      intent.putExtra("DeptSpec",DeptSpec);
////      intent.putExtra("AdmissionYear",AdmissionYear);
//
//                                                    startActivity(intent);
//      finish();
//
//
//                                                }
//
////}
//
//                                                    }
////                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                    }
//                                                });
//
//
//
//                                            }
//
//
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                        }
//                                    });

//                                    Toast.makeText(SignIn.this, "Successful", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }
}
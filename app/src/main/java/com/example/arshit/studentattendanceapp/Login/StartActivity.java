package com.example.arshit.studentattendanceapp.Login;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import com.example.arshit.studentattendanceapp.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

     FirebaseAuth auth;
     FirebaseUser firebaseUser;
    private Button btnRegister,btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(StartActivity.this,SignIn.class));
            finish();
        }



        btnLogin = findViewById(R.id.Login);
        btnRegister = findViewById(R.id.register);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(StartActivity.this,SignIn.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                startActivity(new Intent(StartActivity.this,SignUp.class));

            }
        });


    }
}


package com.example.arshit.studentattendanceapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.arshit.studentattendanceapp.Fees.FeesActivity;
import com.example.arshit.studentattendanceapp.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class PayFeesFragment extends Fragment {


    FirebaseUser currentFirebaseUser;
    String semItem,session_id,id,DeptName,DeptField,DeptSpec,SessionId,Year;
    DatabaseReference  rootRef,databaseReference;
    Spinner semSpinner;
    List<String> semList;
    TextView txtAmt;
    Button btnSubmit;



    View view;

    public PayFeesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_pay_fees, container, false);

        intialise();
        return view;


    }

    private void intialise() {


        semList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        semSpinner = view.findViewById(R.id.sem_price_spinner);

        txtAmt = view.findViewById(R.id.txt_sem_fees);


        btnSubmit = view.findViewById(R.id.btn_fees);

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        session_id = currentFirebaseUser.getUid().toString();

//        session_id = UUID.randomUUID().toString();
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
                        Year = dataSnapshot1.child("AdmissionYear").getValue().toString();

                        selectSem(DeptName,DeptField,DeptSpec,SessionId,Year);
                    }

                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void selectSem(final String deptName, final String deptField, final String deptSpec, final String sessionId, final String year) {



        databaseReference.child("FeesManagement")
                .child(deptName).child(deptField)
                .child(deptSpec).child(year).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

semList.clear();

for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){


    String Value = dataSnapshot1.getKey().toString();
    semList.add(Value);

    ArrayAdapter<String> DeptNameAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, semList);
    DeptNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    semSpinner.setAdapter(DeptNameAdapter);

}


semSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        semItem = adapterView.getItemAtPosition(i).toString();

        databaseReference.child("FeesManagement")
                .child(deptName).child(deptField)
                .child(deptSpec).child(year).child(semItem).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    txtAmt.setText(dataSnapshot.child("Price").getValue().toString());


                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(getContext(), FeesActivity.class);
                            intent.putExtra("Amt",txtAmt.getText().toString());
                            startActivity(intent);


                        }
                    });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
});


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}
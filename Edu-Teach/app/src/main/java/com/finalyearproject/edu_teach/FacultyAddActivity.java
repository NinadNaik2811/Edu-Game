package com.finalyearproject.edu_teach;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FacultyAddActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        setFont();
    }

    private void setFont() {
        ChalkTextView welcome = (ChalkTextView) findViewById(R.id.welcomeMessage);
        welcome.setText(R.string.faculty);
        welcome.setFont(getApplicationContext());
        ChalkEditText email= (ChalkEditText) findViewById(R.id.loginEmailId);
        email.setFont(getApplicationContext());
        ChalkTextView signup = (ChalkTextView) findViewById(R.id.signupUser);
        signup.setFont(getApplicationContext());
    }

    public void signupUser(View view) {
        EditText name = (EditText) findViewById(R.id.loginName);
        EditText email = (EditText) findViewById(R.id.loginEmailId);
        checkInFirebase(email.getText().toString());
    }



    private void checkInFirebase(final String email) {
        final DatabaseReference teacher = databaseReference.child("Faculty");
        teacher.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean flag=false;
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    if(snap.child("email").getValue().toString().equals(email)){
                        teacher.child(snap.getKey()).child("approved").setValue(true);
                        flag=true;
                        finish();
                    }
                }
                if(!flag){
                    Toast toast=Toast.makeText(getApplicationContext(),"No such mail found",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}

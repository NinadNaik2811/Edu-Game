package com.finalyearproject.edu_teach;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ChalkTextView welcome = (ChalkTextView) findViewById(R.id.welcomeMessage);
        welcome.setFont(getApplicationContext());
        ChalkTextView takeASec = (ChalkTextView) findViewById(R.id.takeASec);
        takeASec.setFont(getApplicationContext());

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Head");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String headEmail = dataSnapshot.child("email").getValue().toString();
                    Log.d("Blah",firebaseUser.getEmail());
                    if (headEmail.equals(firebaseUser.getEmail())) {
                        startActivity(new Intent(MainActivity.this, HeadMainActivity.class));
                    } else {
                        startActivity(new Intent(MainActivity.this, FacultyMainActivity.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}

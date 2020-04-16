package com.finalyearproject.edu_teach;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FacultyMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ChalkTextView welcome = (ChalkTextView) findViewById(R.id.welcomeMessage);
        welcome.setFont(getApplicationContext());
        ChalkTextView takeASec = (ChalkTextView) findViewById(R.id.takeASec);
        takeASec.setFont(getApplicationContext());

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser teacher = firebaseAuth.getCurrentUser();
        DatabaseReference teacherReference = FirebaseDatabase.getInstance().getReference("Faculty").child(teacher.getUid());
        teacherReference.child("approved").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().toString().equals("false")) {
                    setContentView(R.layout.activity_faculty_unapproved_main);
                    setFontUnapproved();
                } else {
                    setContentView(R.layout.activity_faculty_main);
                    setFontApproved();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void setFontUnapproved() {
        ChalkTextView mainMenu = (ChalkTextView) findViewById(R.id.mainMenu);
        mainMenu.setFont(getApplicationContext());
        ChalkTextView approval = (ChalkTextView) findViewById(R.id.approval);
        approval.setFont(getApplicationContext());
        ChalkTextView signout = (ChalkTextView) findViewById(R.id.signout);
        signout.setFont(getApplicationContext());
    }

    private void setFontApproved() {
        ChalkTextView mainMenu = (ChalkTextView) findViewById(R.id.mainMenu);
        mainMenu.setFont(getApplicationContext());
        ChalkTextView material = (ChalkTextView) findViewById(R.id.material);
        material.setFont(getApplicationContext());
        ChalkTextView assignment = (ChalkTextView) findViewById(R.id.assignment);
        assignment.setFont(getApplicationContext());
        ChalkTextView exam = (ChalkTextView) findViewById(R.id.exam);
        exam.setFont(getApplicationContext());
        ChalkTextView signout = (ChalkTextView) findViewById(R.id.signout);
        signout.setFont(getApplicationContext());
    }

    public void signOutUser(View view) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void menuSelected(View view) {
        ChalkTextView selectedItem = (ChalkTextView) view;
        Intent intent;
        switch (selectedItem.getText().toString()) {

            case "Study Material":
                intent = new Intent(this, ClassListActivity.class);
                intent.putExtra("currentHeader", "Study Material");
                startActivity(intent);
                break;
            case "Assignment":
                intent = new Intent(this, ClassListActivity.class);
                intent.putExtra("currentHeader", "Assignment");
                startActivity(intent);
                break;
            case "Exam Marks":
                intent = new Intent(this, ClassListActivity.class);
                intent.putExtra("currentHeader", "Exam Marks");
                startActivity(intent);

                break;
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

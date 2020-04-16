package com.finalyearproject.edu_teach;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class EvaluateActivity extends AppCompatActivity {

    ListView listOfStudents;
    ChalkTextView heading;
    ChalkTextView nameHeader;
    ChalkTextView mksHeader;
    ChalkTextView confirmHeader;

    String currentClass;
    String currentSubject;
    String assignmentOrExamName;
    ChalkArrayAdapter arrayAdapter;
    LinkedList<String> studentName;
    LinkedList<String> studentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        currentClass=getIntent().getStringExtra("currentClass");
        currentSubject=getIntent().getStringExtra("currentSubject");
        assignmentOrExamName=getIntent().getStringExtra("assignmentOrExamName");
        listOfStudents= (ListView) findViewById(R.id.listOfStudents);
        studentName=new LinkedList<String>();
        studentId=new LinkedList<String>();
        setFont();
        retrieveDataFromFirebase();
    }

    private void setFont() {
        heading= (ChalkTextView) findViewById(R.id.heading);
        heading.setFont(getApplicationContext());
        nameHeader= (ChalkTextView) findViewById(R.id.nameHeader);
        nameHeader.setFont(getApplicationContext());
        mksHeader= (ChalkTextView) findViewById(R.id.marksHeader);
        mksHeader.setFont(getApplicationContext());
        confirmHeader= (ChalkTextView) findViewById(R.id.confirmHeader);
        confirmHeader.setFont(getApplicationContext());
    }

    private void retrieveDataFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Classes");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    if(snap.child("name").getValue().toString().equals(currentClass)){
                        for(DataSnapshot student:snap.child("Students").getChildren()){
                            studentId.add(student.getKey());
                            studentName.add(student.child("name").getValue().toString());
                        }
                        break;
                    }
                }
                if((assignmentOrExamName.contains("UT"))||(assignmentOrExamName.contains("Exam"))){
                    if(assignmentOrExamName.contains("UT"))
                        arrayAdapter=new ChalkArrayAdapter(getApplicationContext(),R.layout.evaluate_list_item,studentName,studentId,"UT",currentClass,currentSubject,assignmentOrExamName);
                    else
                        arrayAdapter=new ChalkArrayAdapter(getApplicationContext(),R.layout.evaluate_list_item,studentName,studentId,"EndSem",currentClass,currentSubject,assignmentOrExamName);
                }else{
                    arrayAdapter=new ChalkArrayAdapter(getApplicationContext(),R.layout.evaluate_list_item,studentName,studentId,"assignment",currentClass,currentSubject,assignmentOrExamName);
                }
                listOfStudents.setAdapter(arrayAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

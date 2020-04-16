package com.finalyearproject.edu_teach;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClassAssignActivity extends AppCompatActivity {

    String uid;
    long preAssignedCount;
    boolean classRefFlag=false;
    boolean subjectRefFlag=false;
    ChalkArrayAdapter arrayAdapter;
    //PreAssignedData
    String[] preAssignedClass;
    String[] preAssignedSubject;
    ListView classList;
    RelativeLayout placeholder;
    ChalkTextView placeholderText;
    ChalkTextView classHeader;
    ChalkTextView subjectHeader;
    //NewData
    String[] newClass;
    String[] newSubject;
    ChalkSpinner classes;
    ChalkSpinner subject;
    ChalkTextView moreClasses;
    ChalkTextView allow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_assign);

        prepareVariablesForPreAssigned();
        retrieveFirebaseForPreAssigned();

        prepareVariablesForNew();
        retrieveFirebaseForNew();
    }

    private void prepareVariablesForPreAssigned() {
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        classList = (ListView) findViewById(R.id.classList);
        placeholder = (RelativeLayout) findViewById(R.id.placeholder);
        placeholderText = (ChalkTextView) findViewById(R.id.placeholderText);
        placeholderText.setFont(getApplicationContext());
        classHeader = (ChalkTextView) findViewById(R.id.classHeader);
        classHeader.setFont(getApplicationContext());
        subjectHeader = (ChalkTextView) findViewById(R.id.subjectHeader);
        subjectHeader.setFont(getApplicationContext());

    }

    private void retrieveFirebaseForPreAssigned() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Faculty").child(uid).child("classes");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                preAssignedCount=dataSnapshot.getChildrenCount();
                if (dataSnapshot.getChildrenCount() == 0) {
                    placeholderText.setText("No classes assigned yet!");
                } else {
                    placeholder.setVisibility(View.GONE);
                    classList.setVisibility(View.VISIBLE);
                    preAssignedClass = new String[(int) (dataSnapshot.getChildrenCount())];
                    preAssignedSubject = new String[(int) (dataSnapshot.getChildrenCount())];
                    int i = 0;
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        preAssignedClass[i] =snap.child("name").getValue().toString();
                        preAssignedSubject[i++] = snap.child("subject").getValue().toString();
                    }
                    arrayAdapter = new ChalkArrayAdapter(ClassAssignActivity.this, R.layout.list_setter_class_assign, preAssignedClass, preAssignedSubject);
                    classList.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void prepareVariablesForNew() {
        classes = (ChalkSpinner) findViewById(R.id.classes);
        subject = (ChalkSpinner) findViewById(R.id.subjects);
        moreClasses= (ChalkTextView) findViewById(R.id.moreClassesHeader);
        moreClasses.setFont(getApplicationContext());
        allow= (ChalkTextView) findViewById(R.id.allow);
        allow.setFont(getApplicationContext());
    }

    private void retrieveFirebaseForNew() {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Classes");
        classes.prepare(getApplicationContext(),databaseReference,"Select a Class",R.layout.default_list_setter,android.R.layout.simple_spinner_dropdown_item,classes);
        databaseReference=FirebaseDatabase.getInstance().getReference("Subjects");
        subject.prepare(getApplicationContext(),databaseReference,"Select a Subject",R.layout.default_list_setter,android.R.layout.simple_spinner_dropdown_item,subject);
    }

    public void approveTeacherClass(View view){
        final String currentClass=classes.getSelectedItem().toString();
        final String currentSubject=subject.getSelectedItem().toString();
        if((currentClass.contains("Select"))||(currentSubject.contains("Select"))){
            Toast toast=Toast.makeText(this,"Please Select both Class and Subject ",Toast.LENGTH_SHORT);
            toast.show();
        }else{
            final DatabaseReference assigned=FirebaseDatabase.getInstance().getReference("Assign");
            assigned.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean alreadyAssigned=false;
                    for(DataSnapshot snap:dataSnapshot.getChildren()){
                        if((snap.child("name").getValue().toString().equals(currentClass))&&(snap.child("subject").getValue().toString().equals(currentSubject))){
                            alreadyAssigned=true;
                            break;
                        }
                    }
                    if(!alreadyAssigned){
                        DatabaseReference facultyClasses=FirebaseDatabase.getInstance().getReference("Faculty").child(uid).child("classes");
                        facultyClasses.child(String.valueOf(preAssignedCount)).setValue(new TeachingClasses(currentClass,currentSubject));
                        assigned.child(String.valueOf(dataSnapshot.getChildrenCount())).setValue(new TeachingClasses(currentClass,currentSubject));
                    }else{
                        Toast toast=Toast.makeText(getApplicationContext(),"Already assigned",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}


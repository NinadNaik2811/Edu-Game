package com.finalyearproject.edu_teach;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClassListActivity extends AppCompatActivity {

    String uid;
    long assignedCount;
    String[] assignedClass;
    String[] assignedSubject;
    ListView classList;
    RelativeLayout placeholder;
    ChalkTextView placeholderText;
    ChalkTextView classHeader;
    ChalkTextView subjectHeader;
    ChalkArrayAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);

        prepareVariablesForPreAssigned();
        retrieveFirebaseForPreAssigned();
        classList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent previousIntent=getIntent();
                Intent intent;
                switch(previousIntent.getStringExtra("currentHeader")){
                    case "Study Material":
                        intent=new Intent(ClassListActivity.this,StudyMaterialAndAssignmentActivity.class);
                        intent.putExtra("currentHeader","Study Material");
                        intent.putExtra("currentClass",assignedClass[position]);
                        intent.putExtra("currentSubject",assignedSubject[position]);
                        startActivity(intent);
                        break;
                    case "Assignment":
                        intent=new Intent(ClassListActivity.this,UploadOrEvaluateActivity.class);
                        intent.putExtra("currentHeader","Assignment");
                        intent.putExtra("currentClass",assignedClass[position]);
                        intent.putExtra("currentSubject",assignedSubject[position]);
                        startActivity(intent);
                        break;
                    case "Exam Marks":
                        intent=new Intent(ClassListActivity.this,ListMarkableAndPreviousActivity.class);
                        intent.putExtra("currentHeader","Exam Marks");
                        intent.putExtra("currentClass",assignedClass[position]);
                        intent.putExtra("currentSubject",assignedSubject[position]);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void prepareVariablesForPreAssigned() {
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
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
                assignedCount=dataSnapshot.getChildrenCount();
                if (dataSnapshot.getChildrenCount() == 0) {
                    placeholderText.setText("No classes assigned yet!");
                } else {
                    placeholder.setVisibility(View.GONE);
                    classList.setVisibility(View.VISIBLE);
                    assignedClass = new String[(int) (dataSnapshot.getChildrenCount())];
                    assignedSubject = new String[(int) (dataSnapshot.getChildrenCount())];
                    int i = 0;
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        int j=i+1;
                        assignedClass[i] =snap.child("name").getValue().toString();
                        assignedSubject[i++] = snap.child("subject").getValue().toString();
                    }
                    arrayAdapter = new ChalkArrayAdapter(ClassListActivity.this, R.layout.list_setter_class_assign, assignedClass, assignedSubject);
                    classList.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }
}
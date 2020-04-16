package com.finalyearproject.edu_teach;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class ListMarkableAndPreviousActivity extends AppCompatActivity {

    LinkedList<String> items;
    String objective;
    String currentHeader;
    String currentClass;
    String currentSubject;
    ChalkArrayAdapter arrayAdapter;
    ChalkTextView header;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_and_header);
        items=new LinkedList<String>();
        objective=getIntent().getStringExtra("objective");
        currentHeader = getIntent().getStringExtra("currentHeader");
        currentClass = getIntent().getStringExtra("currentClass");
        currentSubject = getIntent().getStringExtra("currentSubject");
        listView = (ListView) findViewById(R.id.list);
        header = (ChalkTextView) findViewById(R.id.header);
        if(getIntent().getBooleanExtra("isPrevious",false))
            header.setText("Previous");
        else
            header.setText(currentHeader);
        header.setFont(getApplicationContext());
        if(!header.getText().toString().equals("Exam Marks"))
            retrieveFirebase();
        else
            makeExamMarksList();

        listViewHandler();
    }

    private void listViewHandler() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if(header.getText().toString().equals("Assignment")){
                    intent=new Intent(ListMarkableAndPreviousActivity.this,EvaluateActivity.class);
                    intent.putExtra("currentClass",currentClass);
                    intent.putExtra("currentSubject",currentSubject);
                    intent.putExtra("assignmentOrExamName",items.get(position));
                    startActivity(intent);
                }else if(header.getText().toString().equals("Exam Marks")){
                    intent=new Intent(ListMarkableAndPreviousActivity.this,EvaluateActivity.class);
                    intent.putExtra("currentClass",currentClass);
                    intent.putExtra("currentSubject",currentSubject);
                    intent.putExtra("assignmentOrExamName",items.get(position));
                    startActivity(intent);
                }
            }
        });
    }

    private void retrieveFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Classes");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=0;
                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    if(snap.child("name").getValue().toString().equals(currentClass)){
                        for(DataSnapshot aom:snap.child(objective).child(currentSubject).getChildren()){
                            items.add(aom.child("title").getValue().toString());
                            i++;
                        }
                        break;
                    }
                }
                arrayAdapter=new ChalkArrayAdapter(getApplicationContext(),R.layout.default_list_setter,items);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void makeExamMarksList() {
        items.add("UT1");
        items.add("UT2");
        items.add("Mid Term Exam");

        items.add("UT3");
        items.add("UT4");
        items.add("Final Exam");
        arrayAdapter=new ChalkArrayAdapter(getApplicationContext(),R.layout.default_list_setter,items);
        listView.setAdapter(arrayAdapter);
    }


}

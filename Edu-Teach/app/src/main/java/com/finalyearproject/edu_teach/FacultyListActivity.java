package com.finalyearproject.edu_teach;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class FacultyListActivity extends AppCompatActivity {

    ListView listView;
    LinkedList<String> faculty = new LinkedList<String>();
    String[] uid;
    ChalkArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_and_header);
        ChalkTextView facultyHeader = (ChalkTextView) findViewById(R.id.header);
        facultyHeader.setText("Faculty");
        facultyHeader.setFont(getApplicationContext());
        retrieveFirebase();
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FacultyListActivity.this, ClassAssignActivity.class);
                intent.putExtra("uid", uid[position]);
                startActivity(intent);
            }
        });
    }

    private void retrieveFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Faculty");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uid = new String[(int) dataSnapshot.getChildrenCount()];
                int i = 0;
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    if (snap.child("approved").getValue().toString().equals("true")) {
                        faculty.add(snap.child("name").getValue().toString());
                        uid[i++] = snap.getKey();
                    }

                }
                arrayAdapter = new ChalkArrayAdapter(FacultyListActivity.this, R.layout.default_list_setter, faculty);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

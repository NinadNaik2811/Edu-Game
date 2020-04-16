package com.finalyearproject.edu_teach;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


public class StudyMaterialAndAssignmentActivity extends AppCompatActivity {

    String objective, databaseObjective;
    String currentClass;
    String currentSubject;
    DatePicker datePicker;
    ChalkTextView header;
    ChalkEditText title;
    ChalkEditText link;
    ChalkEditText description;
    ChalkTextView timeText;
    ChalkTextView submit;
    ChalkTextView info;
    SubmissionTime submissionTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        objective = getIntent().getStringExtra("currentHeader");
        if (objective.equals("Assignment")) {
            databaseObjective = "assignment";
        } else if (objective.equals("Study Material")) {
            databaseObjective = "material";
        }
        currentClass = getIntent().getStringExtra("currentClass");
        currentSubject = getIntent().getStringExtra("currentSubject");
        setFont();
        datePicker = (DatePicker) findViewById(R.id.time);
        DatePicker.OnDateChangedListener listener = new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                datePicker.setVisibility(View.INVISIBLE);
                ++monthOfYear;
                submissionTime = new SubmissionTime(year, monthOfYear, dayOfMonth);
                timeText.setText(dayOfMonth + "-" + monthOfYear + "-" + year);
            }
        };
        Calendar calendar = Calendar.getInstance();
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), listener);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);
    }


    private void setFont() {
        header = (ChalkTextView) findViewById(R.id.header);
        header.setText(objective);
        header.setFont(getApplicationContext());
        title = (ChalkEditText) findViewById(R.id.title);
        title.setFont(getApplicationContext());
        link = (ChalkEditText) findViewById(R.id.link);
        link.setFont(getApplicationContext());
        description = (ChalkEditText) findViewById(R.id.description);
        description.setFont(getApplicationContext());
        timeText = (ChalkTextView) findViewById(R.id.timeText);
        timeText.setFont(getApplicationContext());
        ChalkTextView previousAssignments = (ChalkTextView) findViewById(R.id.previousAssignments);
        previousAssignments.setFont(getApplicationContext());
        if (!objective.equals("Assignment")) {
            timeText.setVisibility(View.INVISIBLE);
            previousAssignments.setText("Previous Material");
        } else
            previousAssignments.setText("Previous Assignments");
        submit = (ChalkTextView) findViewById(R.id.submit);
        submit.setFont(getApplicationContext());
        info = (ChalkTextView) findViewById(R.id.info);
        info.setText("Class: " + currentClass + "   Subject: " + currentSubject);
        info.setFont(getApplicationContext());
    }

    public void submitObjective(View view) {
        final String assignmentTitle = title.getText().toString();
        final String assignmentLink = link.getText().toString();
        final String assignmentDescription = description.getText().toString();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Classes");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!anyInfoNotProvided(assignmentTitle, assignmentLink)) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        if (snap.child("name").getValue().toString().equals(currentClass)) {
                            String currentAssignmentOrMaterial = String.valueOf(snap.child(databaseObjective).child(currentSubject).getChildrenCount());
                            databaseReference.child(snap.getKey()).child(databaseObjective).child(currentSubject).child(currentAssignmentOrMaterial).setValue(new AssignmentOrMaterial(assignmentTitle, assignmentLink, assignmentDescription, submissionTime));
                            finish();
                        }
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please provide all information", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean anyInfoNotProvided(String assignmentTitle, String assignmentLink) {
        if ((assignmentTitle.equals("")) || (assignmentLink.equals(""))) {
            return true;
        } else {
            return false;
        }
    }

    public void openCalendar(View view) {
        DatePicker calendar = (DatePicker) findViewById(R.id.time);
        calendar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onBackPressed() {
        if (datePicker.getVisibility() == View.VISIBLE)
            datePicker.setVisibility(View.INVISIBLE);
        else
            super.onBackPressed();
    }

    public void listOfAssignments(View view) {
        Intent intent=new Intent(this,ListMarkableAndPreviousActivity.class);

        intent.putExtra("currentClass",currentClass);
        intent.putExtra("currentSubject",currentSubject);
        intent.putExtra("isPrevious",true);
        intent.putExtra("objective",databaseObjective);
        startActivity(intent);
    }
}

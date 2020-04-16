package com.finalyearproject.edu_teach;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

/**
 * Created by ninad on 12/24/2016.
 */

public class ChalkArrayAdapter extends ArrayAdapter {

    String flag;
    String whatIsEvaluated;
    String currentClass;
    int classChild;
    String currentSubject;
    String assignmentOrExamName;
    LinkedList<String> people = new LinkedList<String>();
    LinkedList<String> peopleId = new LinkedList<String>();
    LinkedList<String> peopleIdGoneOver = new LinkedList<String>();
    String[] classes;
    String[] subjects;
    Context context;

    public ChalkArrayAdapter(Context context, int resource, LinkedList<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.flag = "default";
        people = objects;
    }

    public ChalkArrayAdapter(Context context, int resource, LinkedList<String> objects1, LinkedList<String> objects2, String whatIsEvaluated, String currentClass, String currentSubject, String assignmentOrExamName) {
        super(context, resource, objects1);
        this.context = context;
        this.flag = "evaluate";
        people = objects1;
        this.whatIsEvaluated = whatIsEvaluated;
        this.currentClass = currentClass;
        getClassChild();
        this.currentSubject = currentSubject;
        this.assignmentOrExamName = assignmentOrExamName;
        peopleId = objects2;
    }

    public ChalkArrayAdapter(Context context, int resource, String[] objects1, String[] objects2) {
        super(context, resource, objects1);
        this.context = context;
        this.flag = "classAssign";
        classes = objects1;
        subjects = objects2;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (flag) {
            case "default":
                return defaultList(position, convertView, parent);
            case "classAssign":
                return classAssignList(position, convertView, parent);
            case "evaluate":
                return evaluateList(position, convertView, parent);
        }
        return null;
    }

    private View classAssignList(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_setter_class_assign, null);
        }
        ChalkTextView itemCount = (ChalkTextView) v.findViewById(R.id.classItemCount);
        itemCount.setText(String.valueOf(position + 1) + ": ");
        itemCount.setFont(context);
        ChalkTextView name = (ChalkTextView) v.findViewById(R.id.classItem);
        name.setText(classes[position]);
        name.setFont(context);
        ChalkTextView subject = (ChalkTextView) v.findViewById(R.id.subjectItem);
        subject.setText(subjects[position]);
        subject.setFont(context);
        return v;
    }

    private View defaultList(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.default_list_setter, null);
        }
        ChalkTextView name = (ChalkTextView) v.findViewById(R.id.listItem);
        name.setText(people.get(position));
        name.setFont(context);
        return v;
    }

    private View evaluateList(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.evaluate_list_item, null);
        }
        ChalkTextView name = (ChalkTextView) v.findViewById(R.id.studentName);
        name.setText(people.get(position));
        name.setFont(context);
        final ChalkEditText marks = (ChalkEditText) v.findViewById(R.id.studentMarks);
        marks.setFont(context);
        ChalkTextView confirm = (ChalkTextView) v.findViewById(R.id.studentConfirm);
        confirm.setFont(context);
        checkFirebase(position, marks, confirm);
        return v;
    }

    private void translateMarksToExperience(int position, final ChalkEditText marks, final String whatIsEvaluated) {
        final DatabaseReference forGame = FirebaseDatabase.getInstance().getReference("Students/" + peopleId.get(position) + "/Characters");
        forGame.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String character = getCharacter();
                int currentMarks = Integer.parseInt(marks.getText().toString());
                double level = Math.ceil(Double.parseDouble(dataSnapshot.child(character).child("level").getValue().toString()));
                double has = Math.ceil(Double.parseDouble(dataSnapshot.child(character).child("has").getValue().toString()));
                double required = Math.ceil(Double.parseDouble(dataSnapshot.child(character).child("required").getValue().toString()));
                double experience;
                if (whatIsEvaluated.equals("assignment")) {
                    experience = 435 * (currentMarks / 10.0);
                } else if (whatIsEvaluated.equals(("UT"))) {
                    experience = 3914 * (currentMarks / 20.0);
                } else {
                    experience = 7827 * (currentMarks / 100.0);
                }
                double totalHas;
                totalHas = has + experience;
                while (totalHas > required) {
                    level++;
                    totalHas = totalHas-(required - has);
                    required = Math.ceil(1.15 * required-(required*0.1));
                    Log.d("Blah",level+" "+totalHas+" "+required+" "+has);
                    has = 0;
                }
                has=totalHas;
                forGame.child(character).child("level").setValue(level);
                forGame.child(character).child("has").setValue(has);
                forGame.child(character).child("required").setValue(required);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkFirebase(final int position, final ChalkEditText marks, final ChalkTextView confirm) {
        final DatabaseReference getMarks = FirebaseDatabase.getInstance().getReference("Classes/" + classChild + "/" + whatIsEvaluated + "/" + currentSubject);
        getMarks.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (whatIsEvaluated.equals("assignment")) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        checkFirebaseAssignment(snap, position, marks, confirm);
                    }
                } else if (whatIsEvaluated.equals("UT")) {
                    checkFirebaseExam(dataSnapshot, position, marks, confirm, 0);
                } else {
                    checkFirebaseExam(dataSnapshot, position, marks, confirm, 1);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkFirebaseExam(DataSnapshot snap, final int position, final ChalkEditText marks, final ChalkTextView confirm, final int flag) {
        boolean isListenerSet=false;
        String getChild;
        if (flag == 0) {
            getChild = getUTChild();
        } else {
            getChild = getEndSemChild();
        }
        final String utChild = getChild;
        DataSnapshot student = snap.child(utChild).child(peopleId.get(position));
        boolean allow=false;
        for(DataSnapshot students:snap.child(utChild).getChildren()){
            int i=0;
            while((!allow)&&(i<peopleIdGoneOver.size())){
                if(students.getKey().equals(peopleIdGoneOver.get(i))){
                    allow=true;
                    i=0;
                }else{
                    i++;
                }
            }
            if ((students.getKey().equals(peopleId.get(position)))&&(allow)) {
                marks.setEnabled(false);
                marks.setText(students.child("marks").getValue().toString());
                confirm.setOnClickListener(null);
                isListenerSet=true;
            }
            peopleIdGoneOver.add(peopleId.get(position));
        }
        if(!isListenerSet) {
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag == 0)
                        saveMarksExam(marks, utChild, position, 20);
                    else
                        saveMarksExam(marks, utChild, position, 100);
                    if (!marks.isEnabled())
                        confirm.setOnClickListener(null);
                }
            });
        }
    }

    private void checkFirebaseAssignment(DataSnapshot snap, final int position, final ChalkEditText marks, final ChalkTextView confirm) {
        boolean isListenerSet = false;
        if (snap.child("title").getValue().equals(assignmentOrExamName)) {
            boolean allow=false;
            for (DataSnapshot students : snap.child("Students").getChildren()) {
                int i=0;
                while((!allow)&&(i<peopleIdGoneOver.size())){

                    if(students.getKey().equals(peopleIdGoneOver.get(i))){
                        allow=true;
                        i=0;
                    }else{
                        i++;
                    }
                }
                if ((students.getKey().equals(peopleId.get(position)))&&(allow)) {
                    marks.setEnabled(false);
                    marks.setText(students.child("marks").getValue().toString());
                    confirm.setOnClickListener(null);
                    isListenerSet = true;
                }
                peopleIdGoneOver.add(peopleId.get(position));
            }
            if (!isListenerSet) {
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveMarksAssignment(position, marks);
                        if(!marks.isEnabled())
                        confirm.setOnClickListener(null);
                    }
                });
            }
        }
    }

    private String getEndSemChild() {
        switch (assignmentOrExamName) {
            case "Mid Term Exam":
                return "0";
            case "Final Exam":
                return "1";
        }
        return "-1";
    }

    private String getUTChild() {
        switch (assignmentOrExamName) {
            case "UT1":
                return "0";
            case "UT2":
                return "1";
            case "UT3":
                return "2";
            case "UT4":
                return "3";
        }
        return "-1";
    }

    private String getCharacter() {
        switch (currentSubject) {
            case "Social Studies":
                return "1";
            case "Mathematics":
                return "2";
            case "Science":
                return "3";
            case "Languages":
                return "4";
        }
        return "0";
    }

    private void getClassChild() {
        switch (currentClass) {
            case "First":
                classChild = 0;
                break;
            case "Second":
                classChild = 1;
                break;
            case "Third":
                classChild = 2;
                break;
            case "Fourth":
                classChild = 3;
                break;
        }
    }

    private void saveMarksAssignment(final int position, final ChalkEditText marks) {
        final DatabaseReference forMarks = FirebaseDatabase.getInstance().getReference("Classes/" + classChild + "/" + whatIsEvaluated + "/" + currentSubject);
        forMarks.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    if (snap.child("title").getValue().equals(assignmentOrExamName)) {
                        int integerMarks = Integer.parseInt(marks.getText().toString());
                        if (integerMarks <= 10) {
                            forMarks.child(snap.getKey()).child("Students").child(peopleId.get(position)).child("marks").setValue(marks.getText().toString());
                            forMarks.child(snap.getKey()).child("Students").child(peopleId.get(position)).child("title").setValue(assignmentOrExamName);
                            Toast.makeText(context, "Marks updated Successfully", Toast.LENGTH_SHORT).show();
                            marks.setEnabled(false);
                            translateMarksToExperience(position, marks, whatIsEvaluated);

                        } else {
                            Toast.makeText(context, "Please enter marks >0 and <=10", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveMarksExam(final ChalkEditText marks, String utChild, int position, int maxMarks) {
        final DatabaseReference forMarks = FirebaseDatabase.getInstance().getReference("Classes/" + classChild + "/" + whatIsEvaluated + "/" + currentSubject);
        int integerMarks = Integer.parseInt(marks.getText().toString());
        if (integerMarks <= maxMarks) {
            forMarks.child(utChild).child(peopleId.get(position)).child("marks").setValue(marks.getText().toString());
            Toast.makeText(context, "Marks updated Successfully", Toast.LENGTH_SHORT).show();
            translateMarksToExperience(position, marks, whatIsEvaluated);
            marks.setEnabled(false);
        } else {
            Toast.makeText(context, "Please enter marks >0 and <=" + maxMarks, Toast.LENGTH_SHORT).show();
        }
    }

}

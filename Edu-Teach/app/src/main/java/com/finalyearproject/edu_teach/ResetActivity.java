package com.finalyearproject.edu_teach;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResetActivity extends AppCompatActivity {

    ChalkEditText resetText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        resetSetFont();
    }

    private void resetSetFont() {
        ChalkTextView resetInstruction = (ChalkTextView) findViewById(R.id.resetInstruction);
        resetInstruction.setVisibility(View.VISIBLE);
        resetInstruction.setFont(getApplicationContext());
        resetText = (ChalkEditText) findViewById(R.id.resetText);
        resetText.setFont(getApplicationContext());
        ChalkTextView resetButton = (ChalkTextView) findViewById(R.id.resetButton);
        resetButton.setFont(getApplicationContext());
    }

    public void resetDatabase(View view) {
        if (resetText.getText().toString().toLowerCase().equals("reset")) {

            final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DataSnapshot classes=dataSnapshot.child("Classes");
                    for (DataSnapshot snap : classes.getChildren()) {
                        databaseReference.child("Classes").child(snap.getKey()).child("assignment").removeValue();
                        databaseReference.child("Classes").child(snap.getKey()).child("material").removeValue();
                        databaseReference.child("Classes").child(snap.getKey()).child("UT").removeValue();
                        databaseReference.child("Classes").child(snap.getKey()).child("EndSem").removeValue();
                        Object obj=snap.child("Students").getValue();
                        if(obj!=null){
                            Log.d("Blah",obj.toString());
                            switch(snap.getKey()){
                                case "0":
                                    databaseReference.child("Classes").child("1").child("Students").setValue(obj);
                                    databaseReference.child("Classes").child("0").child("Students").removeValue();

                                    break;
                                case "1":
                                    databaseReference.child("Classes").child("2").child("Students").setValue(obj);
                                    databaseReference.child("Classes").child("1").child("Students").removeValue();
                                    break;
                                case "2":
                                    databaseReference.child("Classes").child("3").child("Students").setValue(obj);
                                    databaseReference.child("Classes").child("2").child("Students").removeValue();
                                    break;
                                case "3":
                                    databaseReference.child("Classes").child("4").child("Students").setValue(obj);
                                    databaseReference.child("Classes").child("3").child("Students").removeValue();
                                    break;
                                case "4":
                                    databaseReference.child("Classes").child("4").child("Students").removeValue();
                                    break;
                            }
                        }
                    }


                    DataSnapshot faculty=dataSnapshot.child("Faculty");
                    for (DataSnapshot snap : faculty.getChildren()) {
                        databaseReference.child("Faculty").child(snap.getKey()).child("classes").removeValue();
                    }
                    DataSnapshot students=dataSnapshot.child("Students");
                    for (DataSnapshot snap : students.getChildren()) {
                        databaseReference.child("Students").child(snap.getKey()).child("Characters").child("1/has").setValue(0);
                        databaseReference.child("Students").child(snap.getKey()).child("Characters").child("2/has").setValue(0);
                        databaseReference.child("Students").child(snap.getKey()).child("Characters").child("3/has").setValue(0);
                        databaseReference.child("Students").child(snap.getKey()).child("Characters").child("4/has").setValue(0);

                        databaseReference.child("Students").child(snap.getKey()).child("Characters").child("1/level").setValue(1);
                        databaseReference.child("Students").child(snap.getKey()).child("Characters").child("2/level").setValue(1);
                        databaseReference.child("Students").child(snap.getKey()).child("Characters").child("3/level").setValue(1);
                        databaseReference.child("Students").child(snap.getKey()).child("Characters").child("4/level").setValue(1);

                        databaseReference.child("Students").child(snap.getKey()).child("Characters").child("1/required").setValue(20);
                        databaseReference.child("Students").child(snap.getKey()).child("Characters").child("2/required").setValue(20);
                        databaseReference.child("Students").child(snap.getKey()).child("Characters").child("3/required").setValue(20);
                        databaseReference.child("Students").child(snap.getKey()).child("Characters").child("4/required").setValue(20);
                        switch(snap.child("class").getValue().toString()){
                            case "1":
                                databaseReference.child("Students").child(snap.getKey()).child("class").setValue("2");
                                break;
                            case "2":
                                databaseReference.child("Students").child(snap.getKey()).child("class").setValue("3");
                                break;
                            case "3":
                                databaseReference.child("Students").child(snap.getKey()).child("class").setValue("4");
                                break;
                            case "4":
                                databaseReference.child("Students").child(snap.getKey()).child("class").setValue("5");
                                break;
                            case "5":
                                databaseReference.child("Students").child(snap.getKey()).removeValue();
                                break;

                        }
                    }
                    DatabaseReference assigned = FirebaseDatabase.getInstance().getReference("Assign");
                    assigned.removeValue();
                    finish();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            Toast toast=Toast.makeText(getApplicationContext(),"Please type \"reset\" in the text field",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}

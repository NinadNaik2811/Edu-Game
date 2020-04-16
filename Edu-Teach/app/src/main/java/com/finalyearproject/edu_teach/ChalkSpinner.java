package com.finalyearproject.edu_teach;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

/**
 * Created by ninad on 12/25/2016.
 */

public class ChalkSpinner extends Spinner {


    public ChalkSpinner(Context context) {
        super(context);
    }

    public ChalkSpinner(Context context, int mode) {
        super(context, mode);
    }

    public ChalkSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChalkSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChalkSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    public ChalkSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int mode) {
        super(context, attrs, defStyleAttr, defStyleRes, mode);
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        super.setAdapter(adapter);
    }


    public void prepare(final Context context, DatabaseReference databaseReference, final String header, final int adapterInflaterResource, final int dropDownViewResource,final ChalkSpinner spinner){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 1;
                LinkedList<String> data = new LinkedList<String>();
                data.add(header);
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    data.add(snap.child("name").getValue().toString());
                    i++;
                }
                ChalkArrayAdapter arrayAdapter=new ChalkArrayAdapter(context,adapterInflaterResource,data);
                arrayAdapter.setDropDownViewResource(dropDownViewResource);
                spinner.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

package com.finalyearproject.edu_teach;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class UploadOrEvaluateActivity extends AppCompatActivity {

    ChalkTextView welcome;
    ChalkTextView upload;
    ChalkTextView or;
    ChalkTextView eval;
    String currentHeader,currentClass,currentSubject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_or_evaluate);
        setFont();
        currentHeader=getIntent().getStringExtra("currentHeader");
        currentClass=getIntent().getStringExtra("currentClass");
        currentSubject=getIntent().getStringExtra("currentSubject");
        welcome.setText(currentHeader);
    }

    private void setFont() {
        welcome = (ChalkTextView) findViewById(R.id.welcomeMessage);
        welcome.setFont(this);
        upload = (ChalkTextView) findViewById(R.id.upload);
        upload.setFont(this);
        or = (ChalkTextView) findViewById(R.id.or);
        or.setFont(this);
        eval = (ChalkTextView) findViewById(R.id.eval);
        eval.setFont(this);
    }

    public void menuSelected(View v){
        String text=((ChalkTextView)v).getText().toString();
        Intent intent;
        if(text.equals("Upload")){
            intent=new Intent(UploadOrEvaluateActivity.this,StudyMaterialAndAssignmentActivity.class);
            intent.putExtra("currentHeader","Assignment");
            intent.putExtra("currentClass",currentClass);
            intent.putExtra("currentSubject",currentSubject);
            intent.putExtra("objective","assignment");
            startActivity(intent);
        }else if (text.equals("Evaluate")){
            intent=new Intent(UploadOrEvaluateActivity.this,ListMarkableAndPreviousActivity.class);
            intent.putExtra("currentHeader","Assignment");
            intent.putExtra("currentClass",currentClass);
            intent.putExtra("currentSubject",currentSubject);
            intent.putExtra("objective","assignment");
            startActivity(intent);

        }
    }
}

package com.finalyearproject.edu_teach;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.firebase.auth.FirebaseAuth;

public class HeadMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_main);

        setFont();
    }

    private void setFont() {
        ChalkTextView mainMenu = (ChalkTextView) findViewById(R.id.mainMenu);
        mainMenu.setFont(getApplicationContext());
        ChalkTextView faculty = (ChalkTextView) findViewById(R.id.faculty);
        faculty.setFont(getApplicationContext());
        ChalkTextView classes= (ChalkTextView) findViewById(R.id.classes);
        classes.setFont(getApplicationContext());
        ChalkTextView reset= (ChalkTextView) findViewById(R.id.reset);
        reset.setFont(getApplicationContext());
        ChalkTextView signout = (ChalkTextView) findViewById(R.id.signout);
        signout.setFont(getApplicationContext());
    }

    public void signOutUser(View view) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void menuSelected(View view) {
        ChalkTextView currentSelected = (ChalkTextView) view;
        switch (currentSelected.getText().toString()) {
             case "Faculty":
                startActivity(new Intent(this,FacultyAddActivity.class));
                break;
            case "Classes":
                startActivity(new Intent(this,FacultyListActivity.class));
                break;
            case "Reset":
                startActivity(new Intent(this,ResetActivity.class));
                break;
        }
    }


    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

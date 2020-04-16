package com.finalyearproject.edu_teach;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseReference=FirebaseDatabase.getInstance().getReference();
        setFont();
    }

    private void setFont(){
        Typeface font=Typeface.createFromAsset(getAssets(),"fonts/chalk.ttf");
        ChalkTextView welcome=(ChalkTextView)findViewById(R.id.welcomeMessage);
        welcome.setFont(getApplicationContext());
        ChalkEditText name= (ChalkEditText)findViewById(R.id.loginName);
        name.setFont(getApplicationContext());
        ChalkEditText email= (ChalkEditText)findViewById(R.id.loginEmailId);
        email.setFont(getApplicationContext());
        ChalkEditText password=(ChalkEditText)findViewById(R.id.loginPassword);
        password.setFont(getApplicationContext());
        ChalkTextView loginSignup=(ChalkTextView) findViewById(R.id.loginOrSignupUser);
        loginSignup.setFont(getApplicationContext());
        ChalkTextView error=(ChalkTextView)findViewById(R.id.instWelcome);
        error.setFont(getApplicationContext());
        Switch  optionSwitch=(Switch)findViewById(R.id.optionSwitch);
        optionSwitch.setTypeface(font);
    }

    public void onLayoutChangeClick(View view){
        TextView loginSignup=(TextView) findViewById(R.id.loginOrSignupUser);
        if(loginSignup.getText().toString().equals("Sign up")){
            loginSignup.setText("Login");
            ((ChalkEditText)findViewById(R.id.loginName)).setVisibility(View.GONE);
            ((Switch)findViewById(R.id.optionSwitch)).setText(getResources().getString(R.string.signup));
        }else{
            loginSignup.setText("Sign up");
            ((ChalkEditText)findViewById(R.id.loginName)).setVisibility(View.VISIBLE);
            ((Switch)findViewById(R.id.optionSwitch)).setText(getResources().getString(R.string.login));
        }
    }

    public void signupOrLoginUser(View view){
        TextView signupOrLoginButton=(TextView) view;
        EditText name=(EditText)findViewById(R.id.loginName);
        EditText email=(EditText)findViewById(R.id.loginEmailId);
        EditText password=(EditText)findViewById(R.id.loginPassword);
        TextView error=(TextView)findViewById(R.id.instWelcome);
        if(signupOrLoginButton.getText().toString().equals("Sign up")){
            signupUser(name.getText().toString(),email.getText().toString(),password.getText().toString(),error);
        }else{
            loginUser(email.getText().toString(),password.getText().toString(),error);
        }
    }

    private void signupUser(final String name,final String email,final String password,final TextView error){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        try{
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        storeInFirebase(name,email,task.getResult().getUser().getUid());
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else{
                        if(task.getException().toString().contains("already in use"))
                            error.setText("Already registered user");
                        error.setVisibility(View.VISIBLE);
                    }
                }
            });
        }catch(IllegalArgumentException e){
            error.setText("Please enter email and password");
            error.setVisibility(View.VISIBLE);
        }
    }

    private void storeInFirebase(final String name,final String email,final String uid){
        final DatabaseReference teacher=databaseReference.child("Faculty");
        teacher.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                teacher.child(uid).setValue(new Faculty(name,email));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loginUser(final String email,final String password,final TextView error){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        final DatabaseReference headMaster=databaseReference.child("Head");
        try{
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        headMaster.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String headEmail=dataSnapshot.child("email").getValue().toString();
                                if(!email.equals(headEmail)){
                                    startActivity(new Intent(LoginActivity.this,FacultyMainActivity.class));
                                } else{
                                    startActivity(new Intent(LoginActivity.this,HeadMainActivity.class));
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else{
                        if(task.getException().toString().contains("no user record"))
                            error.setText("No user with such an email address exists");
                        error.setVisibility(View.VISIBLE);
                    }
                }
            });
        }catch (IllegalArgumentException e){
            error.setText("Please enter email and password");
            error.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

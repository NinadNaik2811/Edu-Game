using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;
using Firebase;
using Firebase.Auth;
using Firebase.Database;
using Firebase.Unity.Editor;

public class SignupUser : MonoBehaviour
{
    string userName;
    string email;
    string password;
    string std;
    public FirebaseUser user;
    public DatabaseReference databaseReference,databaseRefForClass;

    void Start()
    {
        FirebaseApp.DefaultInstance.SetEditorDatabaseUrl("https://edu-game-b7fd3.firebaseio.com/");
        databaseReference = FirebaseDatabase.DefaultInstance.RootReference;
        databaseReference = databaseReference.Child("Students");
        databaseRefForClass = FirebaseDatabase.DefaultInstance.RootReference;
        databaseRefForClass = databaseRefForClass.Child("Classes");
    }

    public void firebaseSignupGetName(InputField name)
    {
        this.userName = name.text;
    }

    public void firebaseSignupStd(InputField std)
    {
        this.std = std.text;
    }

    public void firebaseSignupGetEmail(InputField email)
    {
        this.email = email.text;
    }

    public void firebaseSignupGetPassword(InputField password)
    {
        this.password = password.text;
    }

    public void firebaseSignup()
    {
        int classes=Convert.ToInt32(std);
        classes = classes - 1;
        databaseRefForClass = databaseRefForClass.Child(classes.ToString());
        databaseRefForClass = databaseRefForClass.Child("Students");
        FirebaseAuth auth = FirebaseAuth.DefaultInstance;
        auth.CreateUserWithEmailAndPasswordAsync(email, password).ContinueWith(task =>
        {
            if (task.IsCompleted)
            {
                databaseReference.Child(task.Result.UserId).Child("email").SetValueAsync(email);
                databaseReference.Child(task.Result.UserId).Child("name").SetValueAsync(userName);
                databaseReference.Child(task.Result.UserId).Child("class").SetValueAsync(std);
                databaseReference = databaseReference.Child(task.Result.UserId).Child("Characters");

                databaseReference.Child("1").Child("level").SetValueAsync(1);
                databaseReference.Child("1").Child("has").SetValueAsync(0);
                databaseReference.Child("1").Child("required").SetValueAsync(20);

                databaseReference.Child("2").Child("level").SetValueAsync(1);
                databaseReference.Child("2").Child("has").SetValueAsync(0);
                databaseReference.Child("2").Child("required").SetValueAsync(20);

                databaseReference.Child("3").Child("level").SetValueAsync(1);
                databaseReference.Child("3").Child("has").SetValueAsync(0);
                databaseReference.Child("3").Child("required").SetValueAsync(20);

                databaseReference.Child("4").Child("level").SetValueAsync(1);
                databaseReference.Child("4").Child("has").SetValueAsync(0);
                databaseReference.Child("4").Child("required").SetValueAsync(20);

                databaseRefForClass.Child(task.Result.UserId).Child("name").SetValueAsync(userName);
                databaseRefForClass.Child(task.Result.UserId).Child("email").SetValueAsync(email);
 
                SceneManager.LoadScene("MainScreen");
            }
        });
    }

    public void backArrowPressed()
    {
        SceneManager.LoadScene("MainScreen");
    }

    public void goToLogin()
    {
        SceneManager.LoadScene("Login");
    }


}

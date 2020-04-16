using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;
using Firebase.Auth;

public class LoginUser : MonoBehaviour
{
    string email;
    string password;
    public static FirebaseUser user;

    public void firebaseLoginGetEmail(InputField email)
    {
        this.email = email.text.ToString();
    }

    public void firebaseLoginGetPassword(InputField password)
    {
        this.password = password.text.ToString();
    }

    public void firebaseLogin()
    {

        FirebaseAuth auth=FirebaseAuth.DefaultInstance;
        auth.SignInWithEmailAndPasswordAsync(email, password).ContinueWith(task =>
        {
            print(email + " " + password);
            if (task.IsCompleted)
            {
                user = task.Result;
                print("Login Sucessful " + user.DisplayName);
                SceneManager.LoadScene("MainScreen");
            }else
            {
                SceneManager.LoadScene("Signup");
            }
 
        });
    }

    public void backArrowPressed()
    {
        SceneManager.LoadScene("MainScreen");
    }

    public void goToSignup()
    {
        SceneManager.LoadScene("Signup");
    }

}

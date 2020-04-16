using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using Firebase.Auth;

public class UserLoginCheck : MonoBehaviour {

    Text text;
    FirebaseUser user;
	// Use this for initialization
	void Start () {
        text=GetComponent<Text>();
        user = FirebaseAuth.DefaultInstance.CurrentUser;
        if (user != null)
        {
            text.text = user.Email;
            print(user.Email);
        }else
        {
            Text startText = GameObject.Find("Canvas/Start").GetComponent<Text>();
            startText.GetComponentInChildren<Button>().interactable=false;
        }
	}
	
	// Update is called once per frame
	void Update () {
		
	}
}

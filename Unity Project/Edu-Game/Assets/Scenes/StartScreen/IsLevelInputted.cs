using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class IsLevelInputted : MonoBehaviour {
    Text play;
    // Use this for initialization
    void Start () {
        play= GameObject.Find("Canvas/Play").GetComponent<Text>();
        play.GetComponentInChildren<Button>().interactable = false;
    }

    // Update is called once per frame
    void Update () {
        InputField level=GameObject.Find("Canvas/Level").GetComponent<InputField>();
        if (!level.text.ToString().Equals("")){
            PlayerStats.difficultyLevel = Convert.ToInt32(level.text);
            if((PlayerStats.difficultyLevel<=100)&& (PlayerStats.difficultyLevel > 0))
            {
                play.GetComponentInChildren<Button>().interactable = true;
            }else
            {
                play.GetComponentInChildren<Button>().interactable = false;
            }
        }
    }
}

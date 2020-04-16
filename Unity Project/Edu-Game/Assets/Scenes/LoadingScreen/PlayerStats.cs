using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;
using Firebase;
using Firebase.Auth;
using Firebase.Database;
using Firebase.Unity.Editor;


public class PlayerStats : MonoBehaviour {

    float[] baseStats1Good = new float[] { 15, 5, 4, 0, 2, 0.8f };
    float[] baseStats2Good = new float[] { 15, 0, 2, 5, 4, 0.7f };
    float[] baseStats3Good = new float[] { 25, 3, 6, 0, 3, 0.4f };
    float[] baseStats4Good = new float[] { 20, 0, 3, 3, 6, 0.6f };

    public static float[] currentStats1Good = new float[5];
    public static float[] currentStats2Good = new float[5];
    public static float[] currentStats3Good = new float[5];
    public static float[] currentStats4Good = new float[5]; 

    float[] baseStats1Bad = new float[] { 12, 4, 3, 0, 1, 0.8f };
    float[] baseStats2Bad = new float[] { 12, 0, 1, 4, 3, 0.7f };
    float[] baseStats3Bad = new float[] { 20, 2, 5, 0, 2, 0.4f };
    float[] baseStats4Bad = new float[] { 16, 0, 2, 2, 5, 0.6f };

    public static float[] currentStats1Bad = new float[5];
    public static float[] currentStats2Bad = new float[5];
    public static float[] currentStats3Bad = new float[5];
    public static float[] currentStats4Bad = new float[5];

    public static int[] currentLevel = new int[4];
    public static int difficultyLevel = 1;
    public DatabaseReference databaseReference;
    public FirebaseUser user;

    void Start()
    {
        user = FirebaseAuth.DefaultInstance.CurrentUser;
        FirebaseApp.DefaultInstance.SetEditorDatabaseUrl("https://edu-game-b7fd3.firebaseio.com/");
        databaseReference = FirebaseDatabase.DefaultInstance.GetReference("Students");
        databaseReference = databaseReference.Child(user.UserId);
        databaseReference = databaseReference.Child("Characters");
        getCharacterLevel();
    }

    void getCharacterLevel()
    {
        GameObject.Find("Canvas/Text").GetComponent<Text>().text = "Getting things Ready....";
        databaseReference.GetValueAsync().ContinueWith(task =>{
            GameObject.Find("Canvas/Text").GetComponent<Text>().text = "Sorting out some Kinks....";
            if (task.IsCompleted)
            {
                DataSnapshot snap = task.Result;
                currentLevel[0] = Convert.ToInt32(snap.Child("1").Child("level").GetValue(true));
                currentLevel[1] = Convert.ToInt32(snap.Child("2").Child("level").GetValue(true));
                currentLevel[2] = Convert.ToInt32(snap.Child("3").Child("level").GetValue(true));
                currentLevel[3] = Convert.ToInt32(snap.Child("4").Child("level").GetValue(true));
                GameObject.Find("Canvas/Text").GetComponent<Text>().text = "Loading data...";


                for (int i = 0; i < 5; i++)
                {
                    try
                    {
                        for(int j = 0; j < currentLevel[0]; j++)
                        {
                            if (j == 0)
                            {
                                currentStats1Good[i] = baseStats1Good[i];
                            }else
                            {
                                currentStats1Good[i] = (float)Math.Ceiling(1.15 * currentStats1Good[i] * 0.9);
                            }
                        }
                        for (int j = 0; j < currentLevel[1]; j++)
                        {
                            if (j == 0)
                            {
                                currentStats2Good[i] = baseStats2Good[i];
                            }
                            else
                            {
                                currentStats2Good[i] = (float)Math.Ceiling(1.15 * currentStats2Good[i] * 0.9);
                            }
                        }
                        for (int j = 0; j < currentLevel[2]; j++)
                        {
                            if (j == 0)
                            {
                                currentStats3Good[i] = baseStats3Good[i];
                            }
                            else
                            {
                                currentStats3Good[i] = (float)Math.Ceiling(1.15 * currentStats3Good[i] * 0.9);
                            }
                        }
                        for (int j = 0; j < currentLevel[3]; j++)
                        {
                            if (j == 0)
                            {
                                currentStats4Good[i] = baseStats4Good[i];
                            }
                            else
                            {
                                currentStats4Good[i] = (float)Math.Ceiling(1.15 * currentStats4Good[i] * 0.9);
                            }
                        }
                        //Baddies
                        for (int j = 0; j < difficultyLevel; j++)
                        {
                            if (j == 0)
                            {
                                currentStats1Bad[i] = baseStats1Bad[i];
                                currentStats2Bad[i] = baseStats2Bad[i];
                                currentStats3Bad[i] = baseStats3Bad[i];
                                currentStats4Bad[i] = baseStats4Bad[i];
                            }
                            else
                            {
                                if (j < 30)
                                {
                                    currentStats1Bad[i] = (float)Math.Ceiling(1.15 * currentStats1Bad[i] * 0.9);
                                    currentStats2Bad[i] = (float)Math.Ceiling(1.15 * currentStats2Bad[i] * 0.9);
                                    currentStats3Bad[i] = (float)Math.Ceiling(1.15 * currentStats3Bad[i] * 0.9);
                                    currentStats4Bad[i] = (float)Math.Ceiling(1.15 * currentStats4Bad[i] * 0.9);

                                }else if(j < 80)
                                {
                                    currentStats1Bad[i] = (float)Math.Ceiling(1.16 * currentStats1Bad[i] * 0.9);
                                    currentStats2Bad[i] = (float)Math.Ceiling(1.16 * currentStats2Bad[i] * 0.9);
                                    currentStats3Bad[i] = (float)Math.Ceiling(1.16 * currentStats3Bad[i] * 0.9);
                                    currentStats4Bad[i] = (float)Math.Ceiling(1.16 * currentStats4Bad[i] * 0.9);
                                }
                                else
                                {
                                    currentStats1Bad[i] = (float)Math.Ceiling(1.13 * currentStats1Bad[i] * 0.9);
                                    currentStats2Bad[i] = (float)Math.Ceiling(1.13 * currentStats2Bad[i] * 0.9);
                                    currentStats3Bad[i] = (float)Math.Ceiling(1.13 * currentStats3Bad[i] * 0.9);
                                    currentStats4Bad[i] = (float)Math.Ceiling(1.13 * currentStats4Bad[i] * 0.9);
                                }
                            }
                        }

                    }
                    catch (Exception e)
                    {
                        GameObject.Find("Canvas/Text").GetComponent<Text>().text = e.Message;
                    }
                    GameObject.Find("Canvas/Text").GetComponent<Text>().text = "Just Give it a min....";
                }
                SceneManager.LoadScene("Play");
            }
        });
    }

}

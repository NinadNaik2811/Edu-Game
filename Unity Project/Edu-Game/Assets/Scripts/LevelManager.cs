using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class LevelManager : MonoBehaviour
{

    public void loadLevel(string name)
    {
        SceneManager.LoadScene(name);
    }

    void Update()
    {
        if (!SceneManager.GetActiveScene().name.Equals("Play"))
        {
            if (Input.GetKeyDown(KeyCode.Escape))
            {
                Application.Quit();
            }
        }
    }
}

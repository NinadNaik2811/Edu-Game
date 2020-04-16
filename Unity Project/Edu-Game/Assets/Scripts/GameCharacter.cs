using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class GameCharacter {

    public float boundingMinX;
    public float boundingMinY;
    public float boundingMaxX;
    public float boundingMaxY;
    public Vector3 position;
    public bool allowChar=false;
    public bool hasMoved=false;
    public bool attackYtranslated = false;
    public bool isKilled=false;
    public float[] stats;
    public Text healthText;

    public GameCharacter(float boundingMinX, float boundingMinY, float boundingMaxX, float boundingMaxY, Vector3 position, Text healthText)
    {
        this.boundingMinX = boundingMinX;
        this.boundingMinY = boundingMinY;
        this.boundingMaxX = boundingMaxX;
        this.boundingMaxY = boundingMaxY;
        this.position = position;
        this.healthText = healthText;
    }



}

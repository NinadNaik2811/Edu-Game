using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class GameplayScript : MonoBehaviour {

//For Finding which character is clicked and gettimg details
    private GameCharacter character1Attr, character2Attr, character3Attr, character4Attr;
    private GameCharacter character1BadAttr, character2BadAttr, character3BadAttr, character4BadAttr;
    private GameCharacter currentGoodBeingAttacked,currentBadBeingAttacked;
    private GameObject arrow, arrowBad;
    int count = 0;
    //Animation and gameObject getter
    private GameObject character1, character2, character3, character4;
    private GameObject character1Bad, character2Bad, character3Bad, character4Bad;

    GameObject current;
    Button confirmButton;

    //Variables for gameplay
    private int currentGoodCharacter = 1;
    private int currentBadCharacter = 1, runOverAllBadCharacters = 1;
    bool attackedByBadDecided = false;
    Text showDamageText;

    bool attackDone = false, notAttacking=true, isBulletInstantiated= false, isNotesInstantiated=false;
    bool currentTurn = true;

    // Use this for initialization
    void Start()
    {
        Input.multiTouchEnabled = false;
        arrow = GameObject.Find("Arrow");
        confirmButton = GameObject.Find("Canvas/Confirm").GetComponent<Button>();
        arrowBad = GameObject.Find("ArrowBad");
        findAllGameObjectInfo();
        prepareHealthVariables();
        GameObject.Find("Canvas/Turn").GetComponent<Text>().text = "Your Turn";
        showDamageText=GameObject.Find("Canvas/Damage").GetComponent<Text>();
    }

    private void prepareHealthVariables()
    {
        character1Attr.healthText.text = character1Attr.stats[0].ToString();
        character2Attr.healthText.text = character2Attr.stats[0].ToString();
        character3Attr.healthText.text = character3Attr.stats[0].ToString();
        character4Attr.healthText.text = character4Attr.stats[0].ToString();

        character1BadAttr.healthText.text = PlayerStats.currentStats1Bad[0].ToString();
        character2BadAttr.healthText.text = PlayerStats.currentStats2Bad[0].ToString();
        character3BadAttr.healthText.text = PlayerStats.currentStats3Bad[0].ToString();
        character4BadAttr.healthText.text = PlayerStats.currentStats4Bad[0].ToString();
    }

    private void findAllGameObjectInfo()
    {
        findAllGameObjectInfoGood();
        findAllGameObjectInfoBad();
    }

    private void findAllGameObjectInfoGood()
    {
        character1 = GameObject.Find("Character1");
        Renderer char1 = character1.GetComponentInChildren<Renderer>();
        Text c1 = GameObject.Find("Canvas/C1Health").GetComponent<Text>();
        character1Attr = new GameCharacter(char1.bounds.min.x, char1.bounds.min.y, char1.bounds.max.x, char1.bounds.max.y, character1.transform.position,c1);
        character1Attr.stats = PlayerStats.currentStats1Good;

        character2 = GameObject.Find("Character2");
        Renderer char2 = character2.GetComponentInChildren<Renderer>();
        Text c2 = GameObject.Find("Canvas/C2Health").GetComponent<Text>();
        character2Attr = new GameCharacter(char2.bounds.min.x, char2.bounds.min.y, char2.bounds.max.x, char2.bounds.max.y, character2.transform.position,c2);
        character2Attr.stats = PlayerStats.currentStats2Good;


        character3 = GameObject.Find("Character3");
        Renderer char3 = character3.GetComponentInChildren<Renderer>();
        Text c3 = GameObject.Find("Canvas/C3Health").GetComponent<Text>();
        character3Attr = new GameCharacter(char3.bounds.min.x, char3.bounds.min.y, char3.bounds.max.x, char3.bounds.max.y, character3.transform.position,c3);
        character3Attr.stats = PlayerStats.currentStats3Good;

        character4 = GameObject.Find("Character4");
        Renderer char4 = character4.GetComponentInChildren<Renderer>();
        Text c4 = GameObject.Find("Canvas/C4Health").GetComponent<Text>();
        character4Attr = new GameCharacter(char4.bounds.min.x, char4.bounds.min.y, char4.bounds.max.x, char4.bounds.max.y, character4.transform.position,c4);
        character4Attr.stats = PlayerStats.currentStats4Good;
    }

    private void findAllGameObjectInfoBad()
    {
        character1Bad = GameObject.Find("Character1Bad");
        Renderer char1Bad = character1Bad.GetComponentInChildren<Renderer>();
        Text c1Bad = GameObject.Find("Canvas/C1BadHealth").GetComponent<Text>();
        character1BadAttr = new GameCharacter(char1Bad.bounds.min.x, char1Bad.bounds.min.y, char1Bad.bounds.max.x, char1Bad.bounds.max.y, character1Bad.transform.position,c1Bad);
        character1BadAttr.stats = PlayerStats.currentStats1Bad;

        character2Bad = GameObject.Find("Character2Bad");
        Renderer char2Bad = character2Bad.GetComponentInChildren<Renderer>();
        Text c2Bad = GameObject.Find("Canvas/C2BadHealth").GetComponent<Text>();
        character2BadAttr = new GameCharacter(char2Bad.bounds.min.x, char2Bad.bounds.min.y, char2Bad.bounds.max.x, char2Bad.bounds.max.y, character2Bad.transform.position,c2Bad);
        character2BadAttr.stats = PlayerStats.currentStats2Bad;

        character3Bad = GameObject.Find("Character3Bad");
        Renderer char3Bad = character3Bad.GetComponentInChildren<Renderer>();
        Text c3Bad = GameObject.Find("Canvas/C3BadHealth").GetComponent<Text>();
        character3BadAttr = new GameCharacter(char3Bad.bounds.min.x, char3Bad.bounds.min.y, char3Bad.bounds.max.x, char3Bad.bounds.max.y, character3Bad.transform.position,c3Bad);
        character3BadAttr.stats = PlayerStats.currentStats3Bad;

        character4Bad = GameObject.Find("Character4Bad");
        Renderer char4Bad = character4Bad.GetComponentInChildren<Renderer>();
        Text c4Bad = GameObject.Find("Canvas/C4BadHealth").GetComponent<Text>();
        character4BadAttr = new GameCharacter(char4Bad.bounds.min.x, char4Bad.bounds.min.y, char4Bad.bounds.max.x, char4Bad.bounds.max.y, character4Bad.transform.position,c4Bad);
        character4BadAttr.stats = PlayerStats.currentStats4Bad;
        currentBadBeingAttacked = character1BadAttr;


    }

    // Update is called once per frame
    void Update()
    {
        if((character1Bad == null) && (character2Bad == null) && (character3Bad == null) && (character4Bad == null))
        {
            GameObject.Find("Canvas/Turn").GetComponent<Text>().text = "You Win!!";
            confirmButton.interactable = false;
            currentTurn = false;
        }
        else if ((character1 == null) && (character2 == null) && (character3 == null) && (character4 == null))
        {
            GameObject.Find("Canvas/Turn").GetComponent<Text>().text = "You Lose!!";
            confirmButton.interactable = false;
            currentTurn = false;
        }else
        {
            isCurrentTurnOver();
        }
        if (currentTurn)
        {
            if ((Input.touchCount == 1) && (notAttacking))
            {
                Touch touch = Input.touches[0];
                Vector3 worldPos = Camera.main.ScreenToWorldPoint(touch.position);
                Vector2 screenPos = new Vector2(worldPos.x, worldPos.y);
                findCharacter(screenPos.x, screenPos.y);
            }
            if(character1!=null)
                character1AttackCycle();
            else
                character1Attr.hasMoved = true;

            if(character2!=null)
                character2AttackCycle();
            else
                character2Attr.hasMoved = true;

            if(character3!=null)
                character3AttackCycle();
            else
                character3Attr.hasMoved = true;

            if (character4 != null)
                character4AttackCycle();
            else
                character4Attr.hasMoved = true;
        }
        else
        {
            charactersBadAttackCycle();
        }
    }

    private void isCurrentTurnOver()
    {
        if ((character1Attr.hasMoved) && (character2Attr.hasMoved) && (character3Attr.hasMoved) && (character4Attr.hasMoved))
        {
            currentTurn = false;
            character1BadAttr.hasMoved = false; character2BadAttr.hasMoved = false; character3BadAttr.hasMoved = false; character4BadAttr.hasMoved = false;
            character1Attr.hasMoved = false; character2Attr.hasMoved = false; character3Attr.hasMoved = false; character4Attr.hasMoved = false;
            GameObject.Find("Canvas/Turn").GetComponent<Text>().text = "Enemy Turn";
        }
        else if ((character1BadAttr.hasMoved) && (character2BadAttr.hasMoved) && (character3BadAttr.hasMoved) && (character4BadAttr.hasMoved))
        {
            currentTurn = true;
            character1BadAttr.hasMoved = false; character2BadAttr.hasMoved = false; character3BadAttr.hasMoved = false; character4BadAttr.hasMoved = false;
            character1Attr.hasMoved = false; character2Attr.hasMoved = false; character3Attr.hasMoved = false; character4Attr.hasMoved = false;
            GameObject.Find("Canvas/Turn").GetComponent<Text>().text = "Your Turn";
            runOverAllBadCharacters = 1;
            confirmButton.interactable = true;
        }
    }

    private void character1AttackCycle()
    {
        if ((character1Attr.allowChar)&&(!character1Attr.hasMoved))
        {
            if (!character1Attr.attackYtranslated)
            {
                current.transform.Translate(new Vector3(0, arrowBad.transform.position.y-arrow.transform.position.y, 0));
                character1Attr.attackYtranslated = true;
            }

            notAttacking = false;
            if ((current.transform.position.x < currentBadBeingAttacked.position.x - 1.5)&&(!attackDone))
            {
                current.GetComponent<Animator>().SetBool("isRunning", true);
                current.transform.Translate(Vector3.right * 5 * Time.deltaTime);
                if(current.transform.position.x+1 > currentBadBeingAttacked.position.x - 0.5)
                {
                    current.GetComponent<Animator>().SetBool("isAttacking", true);
                    current.GetComponent<Animator>().SetBool("isRunning", false);
                }
            }
            else if (attackDone)
            {
                if (current.transform.localScale != new Vector3(-3f, 3f, 3f))
                {
                    current.transform.localScale = new Vector3(-3f, 3f, 3f);
                }
                current.GetComponent<Animator>().SetBool("isRunning", true);
                current.GetComponent<Animator>().SetBool("isAttacking", false);
                if (current.GetComponent<Animator>().GetCurrentAnimatorStateInfo(0).IsName("C1Running"))
                {
                    if (current.transform.position.x > arrow.transform.position.x)
                    {
                        current.transform.Translate(Vector3.left * 5 * Time.deltaTime);
                    }
                    else if (current.transform.position.x < arrow.transform.position.x)
                    {
                        current.GetComponent<Animator>().SetBool("isRunning", false);
                        current.transform.position = Vector3.MoveTowards(transform.position, new Vector3(character1Attr.position.x,character1Attr.position.y,0f), 100000f);
                        current.transform.localScale = new Vector3(3f, 3f, 3f);
                        currentBadBeingAttacked=mathToReduceHealthOnAttack(character1Attr, currentBadBeingAttacked);
                        if (currentBadBeingAttacked.isKilled)
                        {
                            destroyKilledCharacter(currentBadCharacter,0);
                        }
                        attackDone = false;
                        character1Attr.attackYtranslated = false;
                        character1Attr.allowChar = false;
                        notAttacking = true;
                        character1Attr.hasMoved = true;
                        confirmButton.interactable = false;
                    }
                }

            }
            if (current.GetComponent<Animator>().GetCurrentAnimatorStateInfo(0).IsName("C1Attacking"))
            {
                if (current.GetComponent<Animator>().GetCurrentAnimatorStateInfo(0).normalizedTime > 1 && !current.GetComponent<Animator>().IsInTransition(0))
                    attackDone = true;
            }

        }
    }

    private void character2AttackCycle()
    {
        if ((character2Attr.allowChar) && (!character2Attr.hasMoved))
        {
            notAttacking = false;
            if (!character2Attr.attackYtranslated)
            {
                current.transform.Translate(new Vector3(0, arrowBad.transform.position.y-0.5f, 0));
                character2Attr.attackYtranslated = true;
            }
            if ((current.transform.position.x <= currentBadBeingAttacked.position.x) && (!attackDone)){
                current.GetComponent<Animator>().SetBool("isAttacking", true);
                current.transform.Translate(Vector3.right * 5 * Time.deltaTime);
                if(current.transform.position.x+1 > currentBadBeingAttacked.position.x+1)
                {
                    current.transform.Translate(Vector3.right * 5 * Time.deltaTime);
                    attackDone = true;
                }
            }
            else if (attackDone)
            {
                current.GetComponent<Animator>().SetBool("isAttacking", false);
                currentBadBeingAttacked=mathToReduceHealthOnAttack(character2Attr, currentBadBeingAttacked);
                if (currentBadBeingAttacked.isKilled)
                {
                    destroyKilledCharacter(currentBadCharacter, 0);
                }
                current.GetComponent<Renderer>().sortingOrder = 0;
                current.transform.position = Vector3.MoveTowards(transform.position, character2.transform.position, 100000f);
                current.GetComponent<Renderer>().sortingOrder = 5;
                attackDone = false;
                character2Attr.attackYtranslated = false;
                character2Attr.allowChar = false;
                notAttacking = true;
                character2Attr.hasMoved = true;
                confirmButton.interactable = false;


            }
        }
    }

    private void character3AttackCycle()
    {
        if ((character3Attr.allowChar) && (!character3Attr.hasMoved))
        {
            notAttacking = false;
            character3.GetComponent<Animator>().SetBool("isAttacking",true);
            if (character3.GetComponent<Animator>().GetCurrentAnimatorStateInfo(0).IsName("C3Attacking"))
            {
                if (character3.GetComponent<Animator>().GetCurrentAnimatorStateInfo(0).normalizedTime > 1 && !character3.GetComponent<Animator>().IsInTransition(0))
                {
                    current.GetComponent<Renderer>().sortingOrder = 5;
                    isBulletInstantiated = true;
                }
            }
            if (isBulletInstantiated)
            {
                character3.GetComponent<Animator>().SetBool("isAttacking", false);
                current.GetComponent<Renderer>().sortingOrder = 5;
                if (current.transform.position.x < currentBadBeingAttacked.position.x - 1.15)
                {
                    current.transform.Translate(Vector3.right * 15 * Time.deltaTime);
                }
                else
                {
                    current.GetComponent<Renderer>().sortingOrder = 0;
                    character3Attr.allowChar = false;
                    notAttacking = true;
                    isBulletInstantiated = false;
                    current.transform.position=Vector3.MoveTowards(transform.position,character3.transform.position,100000f);
                    currentBadBeingAttacked=mathToReduceHealthOnAttack(character3Attr, currentBadBeingAttacked);
                    if (currentBadBeingAttacked.isKilled)
                    {
                        destroyKilledCharacter(currentBadCharacter, 0);
                    }

                    character3Attr.hasMoved = true;
                    confirmButton.interactable = false;

                }
            }
        }
    }

    private void character4AttackCycle()
    {
        if ((character4Attr.allowChar) && (!character4Attr.hasMoved))
        {
            notAttacking = false;
            character4.GetComponent<Animator>().SetBool("isAttacking", true);
            if (character4.GetComponent<Animator>().GetCurrentAnimatorStateInfo(0).IsName("C4Attacking"))
            {
                if (character4.GetComponent<Animator>().GetCurrentAnimatorStateInfo(0).normalizedTime > 1 && !character4.GetComponent<Animator>().IsInTransition(0))
                {
                    current.GetComponent<Renderer>().sortingOrder = 5;
                    isNotesInstantiated = true;
                }
            }
            if (isNotesInstantiated)
            {
                character4.GetComponent<Animator>().SetBool("isAttacking", false);
                current.GetComponent<Renderer>().sortingOrder = 5;
                if (current.transform.position.x < currentBadBeingAttacked.position.x - 1.15)
                {
                    current.transform.Translate(Vector3.right * 5 * Time.deltaTime);
                }
                else
                {
                    current.GetComponent<Renderer>().sortingOrder = 0;
                    character4Attr.allowChar = false;
                    currentBadBeingAttacked=mathToReduceHealthOnAttack(character4Attr, currentBadBeingAttacked);
                    if (currentBadBeingAttacked.isKilled)
                    {
                        destroyKilledCharacter(currentBadCharacter, 0);
                    }

                    notAttacking = true;
                    isNotesInstantiated = false;
                    current.transform.position = Vector3.MoveTowards(transform.position, character4.transform.position, 100000f);
                    character4Attr.hasMoved = true;
                    confirmButton.interactable = false;

                }

            }

        }
    }

    private GameCharacter mathToReduceHealthOnAttack(GameCharacter currentAttr,GameCharacter currentEnemyAttr)
    {
        float damage;
        int defType;
        double def;
        int atkType;
        System.Random r = new System.Random();
        int toHit = r.Next(1, 101);
        if (currentAttr.stats[1] != 0)
        {
            atkType = 1;
            defType = 2;
        }else
        {
            atkType = 3;
            defType = 4;
        }
        if (toHit < 10)
        {
            def = Math.Ceiling(currentEnemyAttr.stats[defType] * 0.1);
        }
        else if (toHit < 90)
        {
            def = Math.Ceiling(currentEnemyAttr.stats[defType] * 0.4);
        }
        else
        {
            def = Math.Ceiling(currentEnemyAttr.stats[defType] * 0.8);
        }
        if (def * 0.3 > currentAttr.stats[atkType])
        {
            damage = 1;
        }
        else if(def< currentAttr.stats[atkType])
            damage = (float)(currentAttr.stats[atkType] - def);
        else
            damage = (float)Math.Ceiling(currentAttr.stats[atkType] - def * 0.2);
        StartCoroutine(showDamageFunction(damage));
        if(currentEnemyAttr.stats[0] - damage <= 0)
        {
            currentEnemyAttr.stats[0] = 0;
            currentEnemyAttr.isKilled = true;
        }
        else
        {
            currentEnemyAttr.stats[0] -= damage;
        }
        currentEnemyAttr.healthText.text = currentEnemyAttr.stats[0].ToString();
        return currentEnemyAttr;
    }

    private void destroyKilledCharacter(int killedCharacter,int flagGoodOrBad)
    {
        switch (killedCharacter)
        {
            case 1:
                if (flagGoodOrBad == 0)
                    GameObject.Destroy(character1Bad);
                else
                    GameObject.Destroy(character1);
                break;
            case 2:
                if (flagGoodOrBad == 0)
                    GameObject.Destroy(character2Bad);
                else
                    GameObject.Destroy(character2);
                break;
            case 3:
                if (flagGoodOrBad == 0)
                    GameObject.Destroy(character3Bad);
                else
                    GameObject.Destroy(character3);
                break;
            case 4:
                if (flagGoodOrBad == 0)
                    GameObject.Destroy(character4Bad);
                else
                    GameObject.Destroy(character4);
                break;

        }
        switchPositionOfArrow(flagGoodOrBad);
    }

    private void switchPositionOfArrow(int flagGoodOrBad)
    {
        if (flagGoodOrBad == 0)
        {
            if (!character1BadAttr.isKilled)
            {
                arrowBad.transform.position = character1Bad.GetComponent<Transform>().position;
                arrowBad.transform.Translate(0f, 1.7f, 0f);
                currentBadCharacter = 1;
                currentBadBeingAttacked = character1BadAttr;
            }
            else if (!character2BadAttr.isKilled)
            {
                arrowBad.transform.position = character2Bad.GetComponent<Transform>().position;
                arrowBad.transform.Translate(0f, 1.7f, 0f);
                currentBadCharacter = 2;
                currentBadBeingAttacked = character2BadAttr;
            }
            else if (!character3BadAttr.isKilled)
            {
                arrowBad.transform.position = character3Bad.GetComponent<Transform>().position;
                arrowBad.transform.Translate(0f, 1.7f, 0f);
                currentBadCharacter = 3;
                currentBadBeingAttacked = character3BadAttr;
            }
            else if (!character4BadAttr.isKilled)
            {
                arrowBad.transform.position = character4Bad.GetComponent<Transform>().position;
                arrowBad.transform.Translate(0f, 1.7f, 0f);
                currentBadCharacter = 4;
                currentBadBeingAttacked = character4BadAttr;
            }
        }else
        {
            if (!character1Attr.isKilled)
            {
                arrow.transform.position = character1.GetComponent<Transform>().position;
                arrow.transform.Translate(0f, 1.7f, 0f);
                currentGoodCharacter = 1;
                currentGoodBeingAttacked = character1Attr;
            }
            else if (!character2Attr.isKilled)
            {
                arrow.transform.position = character2.GetComponent<Transform>().position;
                arrow.transform.Translate(0f, 1.7f, 0f);
                currentGoodCharacter = 2;
                currentGoodBeingAttacked = character2Attr;
            }
            else if (!character3Attr.isKilled)
            {
                arrow.transform.position = character3.GetComponent<Transform>().position;
                arrow.transform.Translate(0f, 1.7f, 0f);
                currentGoodCharacter = 3;
                currentGoodBeingAttacked = character3Attr;
            }
            else if (!character4Attr.isKilled)
            {
                arrow.transform.position = character4.GetComponent<Transform>().position;
                arrow.transform.Translate(0f, 1.7f, 0f);
                currentGoodCharacter = 4;
                currentGoodBeingAttacked = character4Attr;
            }

        }
    }

    IEnumerator showDamageFunction(float damage)
    {
        showDamageText.text = damage.ToString();
        yield return new WaitForSeconds(0.5f);
        showDamageText.text = " ";
    }

    //BADDIES Transition From Here

    private void charactersBadAttackCycle()
    {
        System.Random r = new System.Random();
        int toHit = r.Next(1, 5);
        if (!attackedByBadDecided)
        {
            switch (toHit)
            {
                case 1:
                    if (!character1Attr.isKilled)
                    {
                        currentGoodBeingAttacked = character1Attr;
                        attackedByBadDecided = true;
                        arrow.transform.position = character1.GetComponent<Transform>().position;
                        arrow.transform.Translate(0f, 1.7f, 0f);
                        currentGoodCharacter = 1;
                    }
                    break;
                case 2:
                    if (!character2Attr.isKilled)
                    {
                        currentGoodBeingAttacked = character2Attr;
                        attackedByBadDecided = true;
                        arrow.transform.position = character2.GetComponent<Transform>().position;
                        arrow.transform.Translate(0f, 1.7f, 0f);
                        currentGoodCharacter = 2;
                    }
                    break;
                case 3:
                    if (!character3Attr.isKilled)
                    {
                        currentGoodBeingAttacked = character3Attr;
                        attackedByBadDecided = true;
                        arrow.transform.position = character3.GetComponent<Transform>().position;
                        arrow.transform.Translate(0f, 1.7f, 0f);
                        currentGoodCharacter = 3;

                    }
                    break;
                case 4:
                    if (!character4Attr.isKilled)
                    {
                        currentGoodBeingAttacked = character4Attr;
                        attackedByBadDecided = true;
                        arrow.transform.position = character4.GetComponent<Transform>().position;
                        arrow.transform.Translate(0f, 1.7f, 0f);
                        currentGoodCharacter = 4;
                    }
                    break;
            }
        }
        switch (runOverAllBadCharacters)
        {
            case 1:
                current = character1Bad;
                character1BadAttr.allowChar = true;

                if (character1Bad != null)
                {
                    arrowBad.transform.position = character1Bad.GetComponent<Transform>().position;
                    arrowBad.transform.Translate(0f, 1.7f, 0f);
                    currentBadCharacter = 1;
                    currentBadBeingAttacked = character1BadAttr;
                    character1BadAttackCycle();
                }
                else
                {
                    character1BadAttr.hasMoved = true;
                    runOverAllBadCharacters++;
                }
                break;
            case 2:
                current = GameObject.Find("Character2Bad/BadOrbs");
                character2BadAttr.allowChar = true;
                if (character2Bad != null)
                {
                    arrowBad.transform.position = character2Bad.GetComponent<Transform>().position;
                    arrowBad.transform.Translate(0f, 1.7f, 0f);
                    currentBadCharacter = 2;
                    currentBadBeingAttacked = character2BadAttr;
                    character2BadAttackCycle();
                }
                else
                {
                    character2BadAttr.hasMoved = true;
                    runOverAllBadCharacters++;
                }
                break;
            case 3:
                current = GameObject.Find("Character3Bad/BadBullets");
                character3BadAttr.allowChar = true;
                if (character3Bad != null)
                {
                    arrowBad.transform.position = character3Bad.GetComponent<Transform>().position;
                    arrowBad.transform.Translate(0f, 1.7f, 0f);
                    currentBadCharacter = 3;
                    currentBadBeingAttacked = character3BadAttr;
                    character3BadAttackCycle();
                }
                else
                {
                    character3BadAttr.hasMoved = true;
                    runOverAllBadCharacters++;
                }
                break;
            case 4:
                current = GameObject.Find("Character4Bad/BadNotes");
                character4BadAttr.allowChar = true;
                if (character4Bad != null)
                {
                    arrowBad.transform.position = character4Bad.GetComponent<Transform>().position;
                    arrowBad.transform.Translate(0f, 1.7f, 0f);
                    currentBadCharacter = 4;
                    currentBadBeingAttacked = character4BadAttr;
                    character4BadAttackCycle();
                }
                else
                {
                    character4BadAttr.hasMoved = true;
                    runOverAllBadCharacters++;
                }
                break;
        }
    }

    private void character1BadAttackCycle()
    {
        if ((character1BadAttr.allowChar) && (!character1BadAttr.hasMoved))
        {
            if (!character1BadAttr.attackYtranslated)
            {
                character1BadAttr.attackYtranslated = true;
            }

            notAttacking = false;
            if ((current.transform.position.x > currentGoodBeingAttacked.position.x+1) && (!attackDone))
            {
                current.GetComponent<Animator>().SetBool("isRunning", true);
                current.transform.Translate(Vector3.left * 5 * Time.deltaTime);
                if (current.transform.position.x - 1 < currentGoodBeingAttacked.position.x + 1.5)
                {
                    current.GetComponent<Animator>().SetBool("isAttacking", true);
                    current.GetComponent<Animator>().SetBool("isRunning", false);
                }
            }
            else if (attackDone)
            {
                if (current.transform.localScale != new Vector3(3f, 3f, 3f))
                {
                    current.transform.localScale = new Vector3(3f, 3f, 3f);
                }
                current.GetComponent<Animator>().SetBool("isRunning", true);
                current.GetComponent<Animator>().SetBool("isAttacking", false);
                if (current.GetComponent<Animator>().GetCurrentAnimatorStateInfo(0).IsName("C1BadRunning"))
                {
                    if (current.transform.position.x < character1BadAttr.position.x)
                    {
                        current.transform.Translate(Vector3.right * 5 * Time.deltaTime);
                    }
                    else if (current.transform.position.x > character1BadAttr.position.x)
                    {
                        current.GetComponent<Animator>().SetBool("isRunning", false);
                        current.transform.localScale = new Vector3(-3f, 3f, 3f);
                        attackDone = false;
                        currentGoodBeingAttacked = mathToReduceHealthOnAttack(character1BadAttr, currentGoodBeingAttacked);
                        if (currentGoodBeingAttacked.isKilled)
                        {
                            destroyKilledCharacter(currentGoodCharacter, 1);
                            handleGoodDeath(currentGoodCharacter);
                        }
                        character1BadAttr.attackYtranslated = false;
                        character1BadAttr.allowChar = false;
                        notAttacking = true;
                        character1BadAttr.hasMoved = true;
                        confirmButton.interactable = false;
                        runOverAllBadCharacters++;
                        attackedByBadDecided = false;
                    }
                }

            }
            if (current.GetComponent<Animator>().GetCurrentAnimatorStateInfo(0).IsName("C1BadAttacking"))
            {
                if (current.GetComponent<Animator>().GetCurrentAnimatorStateInfo(0).normalizedTime > 1 && !current.GetComponent<Animator>().IsInTransition(0))
                    attackDone = true;
            }

        }

    }

    private void character2BadAttackCycle()
    {
        if ((character2BadAttr.allowChar) && (!character2BadAttr.hasMoved))
        {
            notAttacking = false;
            if (!character2BadAttr.attackYtranslated)
            {
                current.transform.Translate(new Vector3(0, arrowBad.transform.position.y - 0.5f, 0));
                character2BadAttr.attackYtranslated = true;
            }
            if ((current.transform.position.x >= currentGoodBeingAttacked.position.x) && (!attackDone))
            {
                current.GetComponent<Animator>().SetBool("isAttacking", true);
                current.transform.Translate(Vector3.right * 5 * Time.deltaTime);
                if (current.transform.position.x + 1 < currentGoodBeingAttacked.position.x + 1)
                {
                    current.transform.Translate(Vector3.right * 5 * Time.deltaTime);
                    attackDone = true;
                }
            }
            else if (attackDone)
            {
                current.GetComponent<Animator>().SetBool("isAttacking", false);
                current.GetComponent<Renderer>().sortingOrder = 0;
                current.transform.position = Vector3.MoveTowards(transform.position, character2Bad.transform.position, 100000f);
                current.GetComponent<Renderer>().sortingOrder = 5;
                currentGoodBeingAttacked = mathToReduceHealthOnAttack(character2BadAttr, currentGoodBeingAttacked);
                if (currentGoodBeingAttacked.isKilled)
                {
                    destroyKilledCharacter(currentGoodCharacter, 1);
                    handleGoodDeath(currentGoodCharacter);
                }
                attackDone = false;
                character2BadAttr.attackYtranslated = false;
                character2BadAttr.allowChar = false;
                notAttacking = true;
                character2BadAttr.hasMoved = true;
                confirmButton.interactable = false;
                runOverAllBadCharacters++;
                attackedByBadDecided = false;


            }
        }

    }

    private void character3BadAttackCycle()
    {
        if ((character3BadAttr.allowChar) && (!character3BadAttr.hasMoved))
        {
            notAttacking = false;
            character3Bad.GetComponent<Animator>().SetBool("isAttacking", true);
            if (character3Bad.GetComponent<Animator>().GetCurrentAnimatorStateInfo(0).IsName("C3BadAttacking"))
            {
                if (character3Bad.GetComponent<Animator>().GetCurrentAnimatorStateInfo(0).normalizedTime > 1 && !character3Bad.GetComponent<Animator>().IsInTransition(0))
                {
                    current.GetComponent<Renderer>().sortingOrder = 5;
                    isBulletInstantiated = true;
                }
            }
            if (isBulletInstantiated)
            {
                character3Bad.GetComponent<Animator>().SetBool("isAttacking", false);
                current.GetComponent<Renderer>().sortingOrder = 5;
                if (current.transform.position.x > currentGoodBeingAttacked.position.x + 1.15)
                {
                    current.transform.Translate(Vector3.right * 15 * Time.deltaTime);
                }
                else
                {
                    current.GetComponent<Renderer>().sortingOrder = 0;
                    character3BadAttr.allowChar = false;
                    notAttacking = true;
                    isBulletInstantiated = false;
                    currentGoodBeingAttacked = mathToReduceHealthOnAttack(character3BadAttr, currentGoodBeingAttacked);
                    if (currentGoodBeingAttacked.isKilled)
                    {
                        destroyKilledCharacter(currentGoodCharacter, 1);
                        handleGoodDeath(currentGoodCharacter);
                    }
                    current.transform.position = Vector3.MoveTowards(transform.position, character3Bad.transform.position, 100000f);
                    character3BadAttr.hasMoved = true;
                    confirmButton.interactable = false;
                    runOverAllBadCharacters++;
                    attackedByBadDecided = false;

                }
            }
        }

    }

    private void character4BadAttackCycle()
    {
        if ((character4BadAttr.allowChar) && (!character4BadAttr.hasMoved))
        {
            notAttacking = false;
            character4Bad.GetComponent<Animator>().SetBool("isAttacking", true);
            if (character4Bad.GetComponent<Animator>().GetCurrentAnimatorStateInfo(0).IsName("C4BadAttacking"))
            {
                if (character4Bad.GetComponent<Animator>().GetCurrentAnimatorStateInfo(0).normalizedTime > 1 && !character4Bad.GetComponent<Animator>().IsInTransition(0))
                {
                    current.GetComponent<Renderer>().sortingOrder = 5;
                    isNotesInstantiated = true;
                }
            }
            if (isNotesInstantiated)
            {
                character4Bad.GetComponent<Animator>().SetBool("isAttacking", false);
                current.GetComponent<Renderer>().sortingOrder = 5;
                if (current.transform.position.x > currentGoodBeingAttacked.position.x + 1.15)
                {
                    current.transform.Translate(Vector3.right * 5 * Time.deltaTime);
                }
                else
                {
                    current.GetComponent<Renderer>().sortingOrder = 0;
                    character4BadAttr.allowChar = false;
                    notAttacking = true;
                    isNotesInstantiated = false;
                    currentGoodBeingAttacked = mathToReduceHealthOnAttack(character4BadAttr, currentGoodBeingAttacked);
                    if (currentGoodBeingAttacked.isKilled)
                    {
                        destroyKilledCharacter(currentGoodCharacter, 1);
                        handleGoodDeath(currentGoodCharacter);
                    }
                    current.transform.position = Vector3.MoveTowards(transform.position, character4Bad.transform.position, 100000f);
                    character4BadAttr.hasMoved = true;
                    confirmButton.interactable = false;
                    runOverAllBadCharacters++;
                    attackedByBadDecided = false;

                }

            }

        }

    }

    private void handleGoodDeath(int deadGood)
    {
        switch (deadGood)
        {
            case 1:
                    character1Attr.hasMoved = true;
                    switchPositionOfArrow(1);

                break;
            case 2:
                    character2Attr.hasMoved = true;
                    switchPositionOfArrow(1);

                break;
            case 3:
                    character3Attr.hasMoved = true;
                    switchPositionOfArrow(1);

                break;
            case 4:
                    character4Attr.hasMoved = true;
                    switchPositionOfArrow(1);

                break;
        }
    }

    private void findCharacter(float screenPosX,float screenPosY)
    {
        if (screenPosX < 0)
        {
            findGoodCharacter(screenPosX, screenPosY);
        }else
        {
            findBadCharacter(screenPosX,screenPosY);
        }
 
    }

    private void findGoodCharacter(float screenPosX,float screenPosY)
    {
        confirmButton.interactable = true;
        if (((character1Attr.boundingMinX <= screenPosX) && (character1Attr.boundingMinY <= screenPosY)) && ((character1Attr.boundingMaxX >= screenPosX) && (character1Attr.boundingMaxY >= screenPosY)))
        {
            arrow.transform.position = character1.GetComponent<Transform>().position;
            arrow.transform.Translate(0f, 1.7f, 0f);
            currentGoodCharacter = 1;
            if (character1Attr.hasMoved)
                confirmButton.interactable = false;
        }
        else if (((character2Attr.boundingMinX <= screenPosX) && (character2Attr.boundingMinY <= screenPosY)) && ((character2Attr.boundingMaxX >= screenPosX) && (character2Attr.boundingMaxY >= screenPosY)))
        {
            arrow.transform.position = character2.GetComponent<Transform>().position;
            arrow.transform.Translate(0f, 1.7f, 0f);
            currentGoodCharacter = 2;
            if (character2Attr.hasMoved)
                confirmButton.interactable = false;
        }
        else if (((character3Attr.boundingMinX <= screenPosX) && (character3Attr.boundingMinY <= screenPosY)) && ((character3Attr.boundingMaxX >= screenPosX) && (character3Attr.boundingMaxY >= screenPosY)))
        {
            arrow.transform.position = character3.GetComponent<Transform>().position;
            arrow.transform.Translate(0f, 1.7f, 0f);
            currentGoodCharacter = 3;
            if (character3Attr.hasMoved)
                confirmButton.interactable = false;
        }
        else if (((character4Attr.boundingMinX <= screenPosX) && (character4Attr.boundingMinY <= screenPosY)) && ((character4Attr.boundingMaxX >= screenPosX) && (character4Attr.boundingMaxY >= screenPosY)))
        {
            arrow.transform.position = character4.GetComponent<Transform>().position;
            arrow.transform.Translate(0f, 1.7f, 0f);
            currentGoodCharacter = 4;
            if (character4Attr.hasMoved)
                confirmButton.interactable = false;
        }

    }

    private void findBadCharacter(float screenPosX, float screenPosY)
    {
        if (((character1BadAttr.boundingMinX <= screenPosX) && (character1BadAttr.boundingMinY <= screenPosY)) && ((character1BadAttr.boundingMaxX >= screenPosX) && (character1BadAttr.boundingMaxY >= screenPosY)))
        {
            arrowBad.transform.position = character1Bad.GetComponent<Transform>().position;
            arrowBad.transform.Translate(0f, 1.7f, 0f);
            currentBadCharacter = 1;
            currentBadBeingAttacked = character1BadAttr;
        }
        else if (((character2BadAttr.boundingMinX <= screenPosX) && (character2BadAttr.boundingMinY <= screenPosY)) && ((character2BadAttr.boundingMaxX >= screenPosX) && (character2BadAttr.boundingMaxY >= screenPosY)))
        {
            arrowBad.transform.position = character2Bad.GetComponent<Transform>().position;
            arrowBad.transform.Translate(0f, 1.7f, 0f);
            currentBadCharacter = 2;
            currentBadBeingAttacked = character2BadAttr;
        }
        else if (((character3BadAttr.boundingMinX <= screenPosX) && (character3BadAttr.boundingMinY <= screenPosY)) && ((character3BadAttr.boundingMaxX >= screenPosX) && (character3BadAttr.boundingMaxY >= screenPosY)))
        {
            arrowBad.transform.position = character3Bad.GetComponent<Transform>().position;
            arrowBad.transform.Translate(0f, 1.7f, 0f);
            currentBadCharacter = 3;
            currentBadBeingAttacked = character3BadAttr;
        }
        else if (((character4BadAttr.boundingMinX <= screenPosX) && (character4BadAttr.boundingMinY <= screenPosY)) && ((character4BadAttr.boundingMaxX >= screenPosX) && (character4BadAttr.boundingMaxY >= screenPosY)))
        {
            arrowBad.transform.position = character4Bad.GetComponent<Transform>().position;
            arrowBad.transform.Translate(0f,1.7f,0f);
            currentBadCharacter = 4;
            currentBadBeingAttacked = character4BadAttr;
        }

    }

    public void changeAnimation()
    {
        confirmButton.interactable = false;
        switch (currentGoodCharacter)
        {
            case 1:
                current = character1;
                character1Attr.allowChar = true;
                break;
            case 2:
                current = GameObject.Find("Character2/Orbs");
                character2Attr.allowChar = true;
                break;
            case 3:
                current = GameObject.Find("Character3/Bullet");
                character3Attr.allowChar = true;
                break;
            case 4:
                current = GameObject.Find("Character4/Notes");
                character4Attr.allowChar = true;
                break;
        }


    }
}

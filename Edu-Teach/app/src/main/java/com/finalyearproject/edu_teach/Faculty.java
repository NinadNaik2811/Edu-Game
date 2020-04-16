package com.finalyearproject.edu_teach;

/**
 * Created by ninad on 12/13/2016.
 */

public class Faculty {
    String name;
    String email;
    Boolean approved;
    TeachingClasses[] classes;
    public Faculty(String name, String email){
        this.name=name;
        this.email=email;
        this.approved=false;
        classes=null;
    }
}

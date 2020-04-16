package com.finalyearproject.edu_teach;

/**
 * Created by ninad on 12/26/2016.
 */

public class AssignmentOrMaterial {
    public String title;
    public String description;
    public String link;
    public SubmissionTime dateOfSubmission;

    public AssignmentOrMaterial(String title, String link, String description, SubmissionTime dateOfSubmission){
        this.title=title;
        this.link=link;
        this.description=description;
        this.dateOfSubmission=dateOfSubmission;
    }
}

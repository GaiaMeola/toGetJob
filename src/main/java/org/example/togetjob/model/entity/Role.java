package org.example.togetjob.model.entity;

public enum Role {

    RECRUITER("Recruiter"),
    STUDENT("Student");

    private final String id;

    Role(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

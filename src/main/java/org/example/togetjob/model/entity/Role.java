package org.example.togetjob.model.entity;

public enum Role {

    RECRUITER("recruiter"),
    STUDENT("student");

    private final String id;

    Role(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

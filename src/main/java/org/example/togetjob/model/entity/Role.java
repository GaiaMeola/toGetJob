package org.example.togetjob.model.entity;

public enum Role {

    RECRUITER("Recruiter"),
    STUDENT("Student");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}

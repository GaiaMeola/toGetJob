package org.example.togetjob.model.entity;

public enum Role {

    RECRUITER("recruiter"),
    STUDENT("student");

    private final String id;

    private Role(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public static Role fromId(String id){
        for(Role role: Role.values()){
            if(role.getId().equalsIgnoreCase(id)){
                return role;
            }
        }
        throw new IllegalArgumentException("Role not valid for id: " + id);
    }

}

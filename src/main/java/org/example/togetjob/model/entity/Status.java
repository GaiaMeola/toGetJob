package org.example.togetjob.model.entity;

public enum Status {

    PENDING("pending"),
    ACCEPTED("accepted"),
    REJECTED("rejected");

    private final String id;

    Status(String id) {
        this.id = id;
    }

}

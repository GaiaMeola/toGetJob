package org.example.togetjob.model.entity;

public enum Status {

    PENDING("pending"),
    ACCEPTED("accepted"),
    REJECTED("rejected");

    private final String statusName;

    Status(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}

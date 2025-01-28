package it.model.entity;

public enum Status {

    PENDING("pending"),
    ACCEPTED("accepted"),
    REJECTED("rejected");

    private final String id;

    private Status(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static Status fromId(String id) {
        for (Status status : Status.values()) {
            if (status.getId().equalsIgnoreCase(id)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Stato non valido per l'id: " + id);
    }
}

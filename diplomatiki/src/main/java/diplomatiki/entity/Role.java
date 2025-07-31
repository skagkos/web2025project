package diplomatiki.entity;

public enum Role {
    STUDENT,
    PROFESSOR,
    SECRETARY;

    public String authority() {
        return "ROLE_" + this.name();
    }
}
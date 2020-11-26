package net.mjduncan.gradebook.enums;

public enum AssessmentForm {

    SINGLE("Single"),
    SET("Set");

    private final String name;

    AssessmentForm(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

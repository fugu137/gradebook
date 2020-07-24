package gradebook.enums;

public enum AssessmentForm {

    SINGLE("Single"),
    SET("Set");

    private String name;

    private AssessmentForm(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

package gradebook.model;

public enum Answer {

    YES("Yes"),
    NO("No");

    private String name;

    private Answer(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

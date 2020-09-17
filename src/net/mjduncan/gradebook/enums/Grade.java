package net.mjduncan.gradebook.enums;

public enum Grade {

    ANY("Any"),
    HD("HD"),
    D("D"),
    CR("CR"),
    P("P"),
    F("F");

    private String name;

    private Grade(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

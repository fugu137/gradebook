package gradebook.model;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Statistics {

    private ObservableList<Student> hdStudents;
    private ObservableList<Student> dStudents;
    private ObservableList<Student> crStudents;
    private ObservableList<Student> pStudents;
    private ObservableList<Student> fStudents;

    public Statistics() {
        this.hdStudents = FXCollections.observableArrayList();
        this.dStudents = FXCollections.observableArrayList();
        this.crStudents = FXCollections.observableArrayList();
        this.pStudents = FXCollections.observableArrayList();
        this.fStudents = FXCollections.observableArrayList();
    }

    public void updateStatistics(Student student, ObjectProperty<?> gradeProperty) {
        remove(student);

        double grade = Double.parseDouble(String.valueOf(gradeProperty.getValue()));

        if (grade < 50) {
            fStudents.add(student);

        } else if (grade < 65) {
            pStudents.add(student);

        } else if (grade < 75) {
            crStudents.add(student);

        } else if (grade < 85) {
            dStudents.add(student);

        } else if (grade <= 100) {
            hdStudents.add(student);

        } else {
            System.out.println("Unable to update statistics. Invalid grade.");
        }
    }

    private void remove(Student student) {
        hdStudents.remove(student);
        dStudents.remove(student);
        crStudents.remove(student);
        pStudents.remove(student);
        fStudents.remove(student);
    }

    public int numberOfHDs() {
        return hdStudents.size();
    }

    public int numberOfDs() {
        return dStudents.size();
    }

    public int numberOfCRs() {
        return crStudents.size();
    }

    public int numberOfPs() {
        return pStudents.size();
    }

    public int numberOfFs() {
        return fStudents.size();
    }

    public int numberOfStudents() {
        return hdStudents.size() + dStudents.size() + crStudents.size() + pStudents.size() + fStudents.size();
    }

    public ObservableList<Student> getHDStudents() {
        return hdStudents;
    }

    public ObservableList<Student> getDStudents() {
        return dStudents;
    }

    public ObservableList<Student> getCRStudents() {
        return crStudents;
    }

    public ObservableList<Student> getPStudents() {
        return pStudents;
    }

    public ObservableList<Student> getFStudents() {
        return fStudents;
    }
}

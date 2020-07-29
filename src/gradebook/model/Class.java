package gradebook.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Class {

    private StringProperty name;
    private ObservableList<Student> students;
    private ObservableList<Assessment> assessments;

    public Class(String name) {
        this.name = new SimpleStringProperty(name);
        this.students = FXCollections.observableArrayList();
        this.assessments = FXCollections.observableArrayList();
    }

    public String getName() {
        return name.getValue();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(Student student) {
        students.remove(student);
    }

    public ObservableList<Student> getStudents() {
        return students;
    }

    public void addAssessment(Assessment assessment) {
        assessments.add(assessment);

        for (Student s: students) {
            if (assessment instanceof StdAssessment) {
                s.addStdAssessmentData((StdAssessment) assessment);
            }
            if (assessment instanceof AssessmentSet) {
                s.addAssessmentSetData((AssessmentSet) assessment);
            }
        }
    }

    public ObservableList<Assessment> getAssessments() {
        return assessments;
    }

    @Override
    public String toString() {
        return name.getValue();
    }
}

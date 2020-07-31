package gradebook.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.LinkedHashMap;

public class Course implements StudentGroup {

    private StringProperty name;
    private ObservableList<Student> students;
    private ObservableList<Assessment> assessments;
    private ObservableMap<Assessment, Statistics> assessmentStatistics;
    private Statistics totalGradeStatistics;

    public Course(String name) {
        this.name = new SimpleStringProperty(name);
        this.students = FXCollections.observableArrayList();
        this.assessments = FXCollections.observableArrayList();
        this.assessmentStatistics = FXCollections.observableMap(new LinkedHashMap<>());
        this.totalGradeStatistics = new Statistics();
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void addAllAt(int index, ObservableList<Student> students) {
        this.students.addAll(index, students);
    }

    public void removeStudent(Student student) {
        students.remove(student);
    }

    public ObservableList<Student> getStudents() {
        return students;
    }

    public ObservableList<Assessment> getAssessments() {
        return assessments;
    }

    public Statistics getStatistics(Assessment assessment) {
        return assessmentStatistics.get(assessment);
    }

    public Statistics getTotalStatistics() {
        return totalGradeStatistics;
    }

    public int getNumberAttempted(Assessment assessment) {
        return assessmentStatistics.get(assessment).numberOfStudents();
    }

    public int getNumberOfStudents() {
        return students.size();
    }

    public void updateAssessmentStatistics(Assessment assessment, Student student, ObjectProperty<?> gradeProperty) {
        assessmentStatistics.get(assessment).updateStatistics(student, gradeProperty);
    }

    public void updateTotalGradeStatistics(Student student, ObjectProperty<Double> gradeProperty) {
        totalGradeStatistics.updateStatistics(student, gradeProperty);
    }

    public void addAssessment(Assessment assessment) {
        assessments.add(assessment);
        assessmentStatistics.put(assessment, new Statistics());
    }

    @Override
    public String toString() {
        return "All";
    }
}

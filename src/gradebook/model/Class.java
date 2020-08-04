package gradebook.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.LinkedHashMap;

public class Class implements StudentGroup {

    private StringProperty name;
    private ObservableList<Student> students;
    private ObservableList<Assessment> assessments;
    private ObservableMap<Assessment, AssessmentStatistics> assessmentStatistics;
    private Statistics totalGradeStatistics;

    public Class(String name) {
        this.name = new SimpleStringProperty(name);
        this.students = FXCollections.observableArrayList();
        this.assessments = FXCollections.observableArrayList();
        this.assessmentStatistics = FXCollections.observableMap(new LinkedHashMap<>());
        this.totalGradeStatistics = new Statistics(students);
    }

    public String getName() {
        return name.getValue();
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

    public void addAssessment(Assessment assessment) {
        assessments.add(assessment);
        assessmentStatistics.put(assessment, new AssessmentStatistics(students, assessment));

        for (Student s: students) {
            if (assessment instanceof StdAssessment) {
                s.addStdAssessmentData((StdAssessment) assessment);
            }
            if (assessment instanceof AssessmentSet) {
                s.addAssessmentSetData((AssessmentSet) assessment);
            }
        }
    }

    public void removeAssessment(Assessment assessment) {
        assessments.remove(assessment);
        assessmentStatistics.remove(assessment);

        for (Student s: students) {
            s.removeAssessmentData(assessment);
        }
    }

    public ObservableList<Assessment> getAssessments() {
        return assessments;
    }

    public AssessmentStatistics getStatistics(Assessment assessment) {
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
        assessmentStatistics.get(assessment).update(student, gradeProperty);
    }

    public void updateTotalGradeStatistics(Student student, ObjectProperty<Double> gradeProperty) {
        totalGradeStatistics.update(student, gradeProperty);
    }

    public ObjectProperty<Double> totalGradeMedianProperty() {
        return totalGradeStatistics.medianProperty();
    }

    public ObjectProperty<Double> totalGradeAverageProperty() {
        return totalGradeStatistics.averageProperty();
    }

    public ObjectProperty<Double> totalGradeHighestProperty() {
        return totalGradeStatistics.highestGradeProperty();
    }

    public ObjectProperty<Double> totalGradeLowestProperty() {
        return totalGradeStatistics.lowestGradeProperty();
    }

    public ObjectProperty<Double> assessmentMedianProperty(Assessment assessment) {
        return assessmentStatistics.get(assessment).medianProperty();
    }

    public ObjectProperty<Double> assessmentAverageProperty(Assessment assessment) {
        return assessmentStatistics.get(assessment).averageProperty();
    }

    public ObjectProperty<Double> assessmentHighestProperty(Assessment assessment) {
        return assessmentStatistics.get(assessment).highestGradeProperty();
    }

    public ObjectProperty<Double> assessmentLowestProperty(Assessment assessment) {
        return assessmentStatistics.get(assessment).lowestGradeProperty();
    }


    @Override
    public String toString() {
        return name.getValue();
    }
}

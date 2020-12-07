package net.mjduncan.gradebook.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.chart.BarChart;

import java.util.LinkedHashMap;
import java.util.List;

public class ClassGroup implements StudentGroup {

    private final StringProperty name;
    private final ObservableList<Student> students;
    private final ObservableList<Assessment> assessments;
    private final ObservableMap<Assessment, AssessmentStatistics> assessmentStatistics;
    private Statistics totalGradeStatistics;

    public ClassGroup(String name) {
        this.name = new SimpleStringProperty(name);
        this.students = FXCollections.observableArrayList();
        this.assessments = FXCollections.observableArrayList();
        this.assessmentStatistics = FXCollections.observableMap(new LinkedHashMap<>());
        this.totalGradeStatistics = new Statistics(students);
    }

    public void clear() {
        students.clear();
        assessments.clear();
        assessmentStatistics.clear();
        totalGradeStatistics = new Statistics(students);
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String newName) {
        name.set(newName);
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

        for (Student s : students) {
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

        for (Student s : students) {
            s.removeAssessmentData(assessment);
        }
    }

    public void addSubAssessment(AssessmentSet assessmentSet, StdAssessment subAssessment) {
        for (Student s : students) {
            s.addSubAssessmentData(assessmentSet, subAssessment);
        }
    }

    public void removeSubAssessments(AssessmentSet assessmentSet, List<StdAssessment> toRemove) {
        for (Student s : students) {
            s.removeSubAssessmentData(assessmentSet, toRemove);
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

    public Integer getNumberAttempted() {
        return totalGradeStatistics.getNumberOfStudentsWithGrades();
    }

    public Integer getNumberAttempted(Assessment assessment) {
        return assessmentStatistics.get(assessment).getNumberOfStudentsWithGrades();
    }

    public ObjectProperty<Integer> numberAttemptedProperty() {
        return totalGradeStatistics.numberOfStudentsWithGradesProperty();
    }

    public ObjectProperty<Integer> numberAttemptedProperty(Assessment assessment) {
        return assessmentStatistics.get(assessment).numberOfStudentsWithGradesProperty();
    }

    public int getNumberOfStudents() {
        return students.size();
    }

    public void updateAssessmentStatistics(Assessment assessment, Student student, ObjectProperty<?> gradeProperty) {
        AssessmentStatistics stats = assessmentStatistics.get(assessment);
        stats.update(student, gradeProperty);
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

    public void fillBarChartWithOverallGrades(BarChart<String, Number> barChart) {
        totalGradeStatistics.fillBarChart(barChart, this);
        barChart.setBarGap(0.0);
        barChart.setCategoryGap(35);
    }

    public void fillBarChartWithAssessmentGrades(BarChart<String, Number> barChart, Assessment assessment) {
        assessmentStatistics.get(assessment).fillBarChart(barChart, this);
        barChart.setBarGap(0.0);
        barChart.setCategoryGap(35);
    }

    @Override
    public String toString() {
        return name.getValue();
    }
}

package net.mjduncan.gradebook.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class StdAssessmentData implements AssessmentData {

    private final StdAssessment stdAssessment;
    private final ObjectProperty<Integer> grade;

    public StdAssessmentData(StdAssessment stdAssessment) {
        this(stdAssessment, null);
    }

    public StdAssessmentData(StdAssessment stdAssessment, Integer grade) {
        this.stdAssessment = stdAssessment;
        this.grade = new SimpleObjectProperty<>(grade);
    }

    public StdAssessment getStdAssessment() {
        return stdAssessment;
    }

    @Override
    public ObjectProperty<Integer> gradeProperty() {
        return grade;
    }

    @Override
    public Integer getGrade() {
        return grade.getValue();
    }

    public void setGrade(Integer grade) {
        this.grade.set(grade);
    }

    @Override
    public boolean setIncompleteToZero() {
        if (grade.getValue() == null) {
            grade.set(0);
            return true;
        }
        return false;
    }

    @Override
    public StdAssessment getAssessment() {
        return stdAssessment;
    }

    @Override
    public String toString() {
        return "Assessment: " + stdAssessment.getName() + ", " +
                "Grade: " + getGrade();
    }



}

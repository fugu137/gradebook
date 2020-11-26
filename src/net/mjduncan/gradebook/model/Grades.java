package net.mjduncan.gradebook.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import net.mjduncan.gradebook.tools.NumberRounder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Grades {

    private final ObservableMap<Assessment, AssessmentData> grades;
    private final ObjectProperty<Double> totalGrade;

    public Grades() {
        this.grades = FXCollections.observableMap(new LinkedHashMap<>());
        this.totalGrade = new SimpleObjectProperty<>(null);
    }

    public void updateTotalGrade() {
        Double total = null;
        Double totalWeighting = null;

        for (AssessmentData a : grades.values()) {
            if (a.gradeProperty() != null && a.gradeProperty().getValue() != null) {

                if (total == null) {
                    total = 0.0;
                }
                if (totalWeighting == null) {
                    totalWeighting = 0.0;
                }

                Double assessmentWeighting = a.getAssessment().getWeighting();
                double grade;

                if (a.getGrade() instanceof Integer) {
                    grade = (double) (int) a.getGrade();
                } else {
                    grade = (double) a.getGrade();
                }

                total = total + NumberRounder.round(grade * assessmentWeighting, 1);
                totalWeighting = totalWeighting + assessmentWeighting;
            }
        }

        if (total == null) {
            totalGrade.set(null);
        } else {
            totalGrade.set(NumberRounder.round(total / totalWeighting, 1));
        }
    }

    public void setIncompleteGradesToZero() {
        for (Assessment a : grades.keySet()) {
            AssessmentData data = grades.get(a);
            data.setIncompleteToZero();
        }
    }

    public void add(Assessment assessment, AssessmentData assessmentData) {
        grades.putIfAbsent(assessment, assessmentData);
    }

    public void remove(Assessment assessment) {
        grades.remove(assessment);
    }

    public AssessmentData get(Assessment assessment) {
        return grades.get(assessment);
    }

    public List<Assessment> assessmentList() {
        return new ArrayList<>(grades.keySet());
    }

    public List<AssessmentData> assessmentDataList() {
        return new ArrayList<>(grades.values());
    }

    public ObjectProperty<Double> totalGradeProperty() {
        return totalGrade;
    }

    public Double getTotalGrade() {
        return totalGrade.getValue();
    }
}

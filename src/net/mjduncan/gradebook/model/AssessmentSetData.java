package net.mjduncan.gradebook.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.mjduncan.gradebook.tools.NumberRounder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AssessmentSetData implements AssessmentData {

    private final AssessmentSet assessmentSet;
    private final ObservableList<StdAssessmentData> stdAssessmentDataList;
    private final ObjectProperty<Double> assessmentSetTotalGrade;

    public AssessmentSetData(AssessmentSet assessmentSet) {
        this.assessmentSet = assessmentSet;
        this.stdAssessmentDataList = FXCollections.observableArrayList();

        for (StdAssessment a : assessmentSet.getStdAssessments()) {
            stdAssessmentDataList.add(new StdAssessmentData(a));
        }
        assessmentSetTotalGrade = new SimpleObjectProperty<>(null);
    }

    public void updateAssessmentSetTotalGrade() {
        Double total = null;
        Integer number = null;

        List<Integer> assessmentGrades = getGradeList();

        if (assessmentGrades.size() > 0) {
            total = 0.0;
            number = assessmentGrades.size();

            if (assessmentSet.getBestOf() < number) {
                number = assessmentSet.getBestOf();
            }

            for (int i = 0; i < number; i++) {
                total = total + assessmentGrades.get(i);
            }
        }

        if (total == null) {
            assessmentSetTotalGrade.set(null);
        } else {
            assessmentSetTotalGrade.set(NumberRounder.round(total / number, 1));
        }
    }

    private List<Integer> getGradeList() {
        List<Integer> gradeList = new ArrayList<>();

        for (StdAssessmentData s : stdAssessmentDataList) {
            if (s.gradeProperty() != null && s.gradeProperty().getValue() != null) {
                gradeList.add(s.getGrade());
            }
        }
        gradeList.sort(Collections.reverseOrder());

        return gradeList;
    }

    public ObjectProperty<Integer> assessmentGradeProperty(StdAssessment stdAssessment) {
        ObjectProperty<Integer> grade = null;

        for (StdAssessmentData d : stdAssessmentDataList) {
            if (d.getStdAssessment() == stdAssessment) {
                grade = d.gradeProperty();
                break;
            }
        }
        return grade;
    }

    public ObservableList<StdAssessmentData> getStdAssessmentDataList() {
        return stdAssessmentDataList;
    }

    @Override
    public Double getGrade() {
        return assessmentSetTotalGrade.getValue();
    }

    @Override
    public boolean setIncompleteToZero() {
        boolean wasChanged = false;

        for (StdAssessmentData data : stdAssessmentDataList) {
            if (!wasChanged) {
                wasChanged = data.setIncompleteToZero();

            } else {
                data.setIncompleteToZero();
            }
        }
        return wasChanged;
    }

    public void setSubAssessmentGrades(Integer[] grades) {
        for (int i = 0; i < grades.length; i++) {
            StdAssessmentData data = stdAssessmentDataList.get(i);
            data.setGrade(grades[i]);
        }
    }

    public Integer[] getSubAssessmentGrades() {
        int size = stdAssessmentDataList.size();
        Integer[] grades = new Integer[size];

        for (int i = 0; i < size; i++) {
            grades[i] = stdAssessmentDataList.get(i).getGrade();
        }
        return grades;
    }

    public void addSubAssessmentData(StdAssessmentData stdData) {
        stdAssessmentDataList.add(stdData);
    }

    public void removeSubAssessmentData(List<StdAssessment> subAssessments) {
        for (StdAssessment stdAssessment : subAssessments) {
            stdAssessmentDataList.removeIf(data -> stdAssessment == data.getStdAssessment());
        }
        updateAssessmentSetTotalGrade();
    }

    @Override
    public ObjectProperty<Double> gradeProperty() {
        return assessmentSetTotalGrade;
    }

    @Override
    public AssessmentSet getAssessment() {
        return assessmentSet;
    }

    public StdAssessmentData getSubAssessmentData(StdAssessment subAssessment) {
        for (StdAssessmentData d : stdAssessmentDataList) {
            if (d.getStdAssessment() == subAssessment) {
                return d;
            }
        }
        return null;
    }

}

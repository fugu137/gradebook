package gradebook.model;

import gradebook.tools.NumberRounder;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AssessmentSetData implements AssessmentData {

    private AssessmentSet assessmentSet;
    private ObservableList<StdAssessmentData> stdAssessmentData;
    private ObjectProperty<Double> totalGrade;

    public AssessmentSetData(AssessmentSet assessmentSet) {
        this.assessmentSet = assessmentSet;
        this.stdAssessmentData = FXCollections.observableArrayList();

        for (StdAssessment a : assessmentSet.getStdAssessments()) {
            stdAssessmentData.add(new StdAssessmentData(a));
        }
        totalGrade = new SimpleObjectProperty<>(null);
    }

    private void updateTotalGrade() {
        Double total = null;
        Double totalWeighting = assessmentSet.getWeighting();

        for (StdAssessmentData s : stdAssessmentData) {
            if (s.gradeProperty().getValue() != null && s.gradeProperty() != null) {

                if (total == null) {
                    total = 0.0;
                }
                total = total + NumberRounder.round(s.getGrade() * totalWeighting, 1);
            }
        }
        if (total == null) {
            totalGrade.set(null);
        } else {
            totalGrade.set(total);
        }
    }

    public AssessmentSet getAssessmentSet() {
        return assessmentSet;
    }

    public ObjectProperty<Double> totalGradeProperty() {
        return totalGrade;
    }

    public ObjectProperty<Integer> assessmentGradeProperty(StdAssessment stdAssessment) {
        ObjectProperty<Integer> grade = null;

        for (StdAssessmentData d : stdAssessmentData) {
            if (d.getStdAssessment() == stdAssessment) {
                grade = d.gradeProperty();
                break;
            }
        }
        return grade;
    }

    public ObservableList<StdAssessmentData> getStdAssessmentData() {
        return stdAssessmentData;
    }

    public Double getGrade() {
        return totalGrade.getValue();
    }

    public ObjectProperty<Double> gradeProperty() {
        return totalGrade;
    }

    @Override
    public AssessmentSet getAssessment() {
        return assessmentSet;
    }


}

package gradebook.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class StdAssessmentData implements AssessmentData {

    private StdAssessment stdAssessment;
    private ObjectProperty<Integer> grade;

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

    public ObjectProperty<Integer> gradeProperty() {
        return grade;
    }

    public Integer getGrade() {
        return grade.getValue();
    }

    public void setGrade(Integer grade) {
        this.grade.set(grade);
    }

    public StdAssessment getAssessment() {
        return stdAssessment;
    }

}

package gradebook.model;

import javafx.beans.property.ObjectProperty;

public interface AssessmentData {

    Number getGrade();
    void setGradeToZero();
    ObjectProperty<?> gradeProperty();
    Assessment getAssessment();

}

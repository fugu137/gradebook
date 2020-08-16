package gradebook.model;

import javafx.beans.property.ObjectProperty;

public interface AssessmentData {

    Number getGrade();
    void setIncompleteToZero();
    ObjectProperty<?> gradeProperty();
    Assessment getAssessment();

}

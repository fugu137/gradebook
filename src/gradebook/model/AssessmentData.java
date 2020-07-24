package gradebook.model;

import javafx.beans.property.ObjectProperty;

public interface AssessmentData {

    public Number getGrade();

    public ObjectProperty<?> gradeProperty();

    public Assessment getAssessment();
}

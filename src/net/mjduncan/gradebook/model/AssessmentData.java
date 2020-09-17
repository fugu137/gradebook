package net.mjduncan.gradebook.model;

import javafx.beans.property.ObjectProperty;

public interface AssessmentData {

    Number getGrade();
    boolean setIncompleteToZero();
    ObjectProperty<?> gradeProperty();
    Assessment getAssessment();

}

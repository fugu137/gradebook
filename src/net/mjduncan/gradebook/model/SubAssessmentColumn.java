package net.mjduncan.gradebook.model;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SubAssessmentColumn<Student, Number> extends AssessmentColumn<Student, Number> implements Comparable<SubAssessmentColumn<Student, ?>> {

    AssessmentSet parent;

    public SubAssessmentColumn(String name) {
        super(name);
    }

    public SubAssessmentColumn(String name, Assessment assessment, AssessmentSet parent) {
        super(name, assessment);
        this.parent = parent;
    }

    public AssessmentSet getParent() {
        return parent;
    }

    @Override
    public VBox assessmentInfoBox() {
        VBox box = parent.infoBox();
        Label title = new Label("Assessment Set");
        title.getStyleClass().add("title-text");
        box.getChildren().add(0, title);
        return box;
    }

    @Override
    public int compareTo(SubAssessmentColumn<Student, ?> c) {

        if (this.getParent().getName().compareTo(c.getParent().getName()) < 0) {
            return -1;

        } else if (this.getParent().getName().compareTo(c.getParent().getName()) > 0) {
            return 1;

        } else {

            if (this.getAssessment().getName().compareTo(c.getAssessment().getName()) < 0 ) {
                return -1;

            } else if (this.getAssessment().getName().compareTo(c.getAssessment().getName()) > 0 ) {
                return 1;

            } else {
                return 0;
            }
        }
    }
}

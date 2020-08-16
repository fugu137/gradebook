package gradebook.model;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SubAssessmentColumn<Student, Number> extends AssessmentColumn<Student, Number> {

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
}

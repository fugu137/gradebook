package gradebook.model;

import javafx.scene.control.TableColumn;

public class AssessmentColumn<Student, Number> extends TableColumn<Student, Number> {

    private Assessment assessment;

    public AssessmentColumn(String name) {
        this(name, null);
    }

    public AssessmentColumn(String name, Assessment assessment) {
        super(name);
        this.assessment = assessment;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    @Override
    public String toString() {
        return getText();
    }
}



package gradebook.model;

import javafx.scene.control.TableColumn;

public class AssessmentColumn<Student, Integer> extends TableColumn<Student, Integer> {

    private Assessment assessment;

    public AssessmentColumn(String name) {
        this(name, null);
    }

    public AssessmentColumn(String name, Assessment assessment) {
        super(name);
        this.assessment = assessment;
    }

}



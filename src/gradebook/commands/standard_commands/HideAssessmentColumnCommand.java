package gradebook.commands.standard_commands;

import gradebook.model.AssessmentColumn;
import gradebook.model.Student;
import javafx.collections.ObservableList;

import java.util.LinkedHashMap;
import java.util.Map;

public class HideAssessmentColumnCommand implements StandardCommand {

    ObservableList<AssessmentColumn<Student, ?>> checkBoxColumns;
    Map<AssessmentColumn<Student, ?>, Boolean> visibilityMap;

    public HideAssessmentColumnCommand(ObservableList<AssessmentColumn<Student, ?>> checkBoxColumns) {
        this.checkBoxColumns = checkBoxColumns;
        this.visibilityMap = new LinkedHashMap<>();

        checkBoxColumns.forEach(c -> visibilityMap.put(c, c.isVisible()));
    }

    @Override
    public void execute() {
        checkBoxColumns.forEach(c -> c.setVisible(false));
    }

    @Override
    public void undo() {
        checkBoxColumns.forEach(c -> c.setVisible(visibilityMap.get(c)));
    }

    @Override
    public void redo() {
        execute();
    }
}

package net.mjduncan.gradebook.commands.standard_commands.view_commands;

import javafx.collections.ObservableList;
import net.mjduncan.gradebook.commands.standard_commands.StandardCommand;
import net.mjduncan.gradebook.model.AssessmentColumn;
import net.mjduncan.gradebook.model.Student;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShowAssessmentColumnCommand implements StandardCommand {

    ObservableList<AssessmentColumn<Student, ?>> checkBoxColumns;
    Map<AssessmentColumn<Student, ?>, Boolean> visibilityMap;

    public ShowAssessmentColumnCommand(ObservableList<AssessmentColumn<Student, ?>> checkBoxColumns) {
        this.checkBoxColumns = checkBoxColumns;
        this.visibilityMap = new LinkedHashMap<>();

        checkBoxColumns.forEach(c -> visibilityMap.put(c, c.isVisible()));
    }

    @Override
    public void execute() {
        checkBoxColumns.forEach(c -> c.setVisible(true));
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

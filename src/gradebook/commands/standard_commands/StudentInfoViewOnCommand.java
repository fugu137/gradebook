package gradebook.commands.standard_commands;

import gradebook.model.Student;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

import java.util.HashMap;
import java.util.Map;

public class StudentInfoViewOnCommand implements StandardCommand {

    private ObservableList<TableColumn<Student, ?>> infoColumns;
    private Map<TableColumn<Student, ?>, Boolean> visibilityMap;

    public StudentInfoViewOnCommand(ObservableList<TableColumn<Student, ?>> infoColumns) {
        this.infoColumns = infoColumns;
        this.visibilityMap = new HashMap<>();

        infoColumns.forEach(c -> visibilityMap.put(c, c.isVisible()));
    }

    @Override
    public void execute() {
        infoColumns.forEach(c -> c.setVisible(true));
    }

    @Override
    public void undo() {
        infoColumns.forEach(c -> {
            boolean wasVisible = visibilityMap.get(c);
            c.setVisible(wasVisible);
        });
    }

    @Override
    public void redo() {
        execute();
    }
}

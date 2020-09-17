package net.mjduncan.gradebook.commands.standard_commands.view_commands;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import net.mjduncan.gradebook.commands.standard_commands.StandardCommand;
import net.mjduncan.gradebook.model.Student;

import java.util.HashMap;
import java.util.Map;

public class AnonymiseOnCommand implements StandardCommand {

    private ObservableList<TableColumn<Student, ?>> infoColumns;
    private Map<TableColumn<Student, ?>, Boolean> visibilityMap;

    public AnonymiseOnCommand(ObservableList<TableColumn<Student, ?>> infoColumns) {
        this.infoColumns = infoColumns;
        this.visibilityMap = new HashMap<>();

        infoColumns.forEach(c -> visibilityMap.put(c, c.isVisible()));
    }

    @Override
    public void execute() {
        infoColumns.forEach(c -> c.setVisible(false));
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

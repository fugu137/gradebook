package net.mjduncan.gradebook.commands.standard_commands.view_commands;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import net.mjduncan.gradebook.commands.standard_commands.StandardCommand;
import net.mjduncan.gradebook.model.Student;

public class AnonymiseOffCommand implements StandardCommand {

    private final ObservableList<TableColumn<Student, ?>> infoColumns;

    public AnonymiseOffCommand(ObservableList<TableColumn<Student, ?>> infoColumns) {
        this.infoColumns = infoColumns;
    }

    @Override
    public void execute() {
        infoColumns.forEach(c -> c.setVisible(true));
    }

    @Override
    public void undo() {
        infoColumns.forEach(c -> c.setVisible(false));
    }

    @Override
    public void redo() {
        execute();
    }
}

package gradebook.commands.standard_commands.view_commands;

import gradebook.commands.standard_commands.StandardCommand;
import gradebook.model.Student;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

public class AnonymiseOffCommand implements StandardCommand {

    private ObservableList<TableColumn<Student, ?>> infoColumns;

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

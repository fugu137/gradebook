package gradebook.commands.standard_commands.view_commands;

import gradebook.commands.standard_commands.StandardCommand;
import gradebook.model.Student;
import javafx.scene.control.TableColumn;

public class StudentInfoViewOffCommand implements StandardCommand {

    private TableColumn<Student, ?> emailColumn;
    private TableColumn<Student, ?> sidColumn;
    private TableColumn<Student, ?> degreeColumn;


    public StudentInfoViewOffCommand(TableColumn<Student, ?> emailColumn, TableColumn<Student, ?> sidColumn, TableColumn<Student, ?> degreeColumn) {
        this.emailColumn = emailColumn;
        this.sidColumn = sidColumn;
        this.degreeColumn = degreeColumn;
    }

    @Override
    public void execute() {
        emailColumn.setVisible(false);
        sidColumn.setVisible(false);
        degreeColumn.setVisible(false);
    }

    @Override
    public void undo() {
        emailColumn.setVisible(true);
        sidColumn.setVisible(true);
        degreeColumn.setVisible(true);
    }

    @Override
    public void redo() {
        execute();
    }
}

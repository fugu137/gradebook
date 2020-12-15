package net.mjduncan.gradebook.commands.standard_commands.view_commands;

import javafx.scene.control.TableColumn;
import net.mjduncan.gradebook.commands.standard_commands.StandardCommand;
import net.mjduncan.gradebook.model.Student;

public class StudentInfoViewOffCommand implements StandardCommand {

    private final TableColumn<Student, ?> emailColumn;
    private final TableColumn<Student, ?> sidColumn;
    private final TableColumn<Student, ?> degreeColumn;
    private final TableColumn<Student, ?> genderColumn;


    public StudentInfoViewOffCommand(TableColumn<Student, ?> emailColumn, TableColumn<Student, ?> sidColumn, TableColumn<Student, ?> degreeColumn, TableColumn<Student, ?> genderColumn) {
        this.emailColumn = emailColumn;
        this.sidColumn = sidColumn;
        this.degreeColumn = degreeColumn;
        this.genderColumn = genderColumn;
    }

    @Override
    public void execute() {
        emailColumn.setVisible(false);
        sidColumn.setVisible(false);
        degreeColumn.setVisible(false);
        genderColumn.setVisible(false);
    }

    @Override
    public void undo() {
        emailColumn.setVisible(true);
        sidColumn.setVisible(true);
        degreeColumn.setVisible(true);
        genderColumn.setVisible(true);
    }

    @Override
    public void redo() {
        execute();
    }
}

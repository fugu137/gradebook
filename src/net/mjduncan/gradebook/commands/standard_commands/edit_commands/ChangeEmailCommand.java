package net.mjduncan.gradebook.commands.standard_commands.edit_commands;

import net.mjduncan.gradebook.commands.standard_commands.StandardCommand;
import net.mjduncan.gradebook.model.Student;

public class ChangeEmailCommand implements StandardCommand {

    private final Student student;
    private final String oldEmail;
    private final String newEmail;

    public ChangeEmailCommand(Student student, String oldEmail, String newEmail) {
        this.student = student;
        this.oldEmail = oldEmail;
        this.newEmail = newEmail;
    }

    @Override
    public void execute() {
        student.setEmail(newEmail);
    }

    @Override
    public void undo() {
        student.setEmail(oldEmail);
    }

    @Override
    public void redo() {
        execute();
    }
}

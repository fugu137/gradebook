package net.mjduncan.gradebook.commands.standard_commands.edit_commands;

import net.mjduncan.gradebook.commands.standard_commands.StandardCommand;
import net.mjduncan.gradebook.enums.Gender;
import net.mjduncan.gradebook.model.Student;

public class ChangeGenderCommand implements StandardCommand {

    private final Student student;
    private final Gender oldGender;
    private final Gender newGender;

    public ChangeGenderCommand(Student student, Gender oldGender, Gender newGender) {
        this.student = student;
        this.oldGender = oldGender;
        this.newGender = newGender;
    }

    @Override
    public void execute() {
        student.setGender(newGender);
    }

    @Override
    public void undo() {
        student.setGender(oldGender);
    }

    @Override
    public void redo() {
        execute();
    }
}

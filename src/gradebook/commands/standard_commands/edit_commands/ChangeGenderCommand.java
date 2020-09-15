package gradebook.commands.standard_commands.edit_commands;

import gradebook.commands.standard_commands.StandardCommand;
import gradebook.enums.Gender;
import gradebook.model.Student;

public class ChangeGenderCommand implements StandardCommand {

    private Student student;
    private Gender oldGender;
    private Gender newGender;

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

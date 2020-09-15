package gradebook.commands.standard_commands.edit_commands;

import gradebook.commands.standard_commands.StandardCommand;
import gradebook.model.ClassGroup;
import gradebook.model.Student;

public class ChangeClassGroupCommand implements StandardCommand {

    private Student student;
    private ClassGroup oldClass;
    private ClassGroup newClass;

    public ChangeClassGroupCommand(Student student, ClassGroup oldClass, ClassGroup newClass) {
        this.student = student;
        this.oldClass = oldClass;
        this.newClass = newClass;
    }

    @Override
    public void execute() {
        student.setClassGroup(newClass);
    }

    @Override
    public void undo() {
        student.setClassGroup(oldClass);
    }

    @Override
    public void redo() {
        execute();
    }
}

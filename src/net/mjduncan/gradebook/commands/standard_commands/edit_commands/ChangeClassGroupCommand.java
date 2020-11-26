package net.mjduncan.gradebook.commands.standard_commands.edit_commands;

import net.mjduncan.gradebook.commands.standard_commands.StandardCommand;
import net.mjduncan.gradebook.model.ClassGroup;
import net.mjduncan.gradebook.model.Student;

public class ChangeClassGroupCommand implements StandardCommand {

    private final Student student;
    private final ClassGroup oldClass;
    private final ClassGroup newClass;

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

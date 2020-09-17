package net.mjduncan.gradebook.commands.standard_commands.edit_commands;

import net.mjduncan.gradebook.commands.standard_commands.StandardCommand;
import net.mjduncan.gradebook.model.Student;

public abstract class ChangeNameCommand implements StandardCommand {

    private Student student;
    private String oldName;
    private String newName;

    public ChangeNameCommand(Student student, String oldName, String newName) {
        this.student = student;
        this.oldName = oldName;
        this.newName = newName;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    @Override
    public void execute() {

    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }
}

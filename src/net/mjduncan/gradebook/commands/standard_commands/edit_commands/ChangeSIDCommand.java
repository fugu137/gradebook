package net.mjduncan.gradebook.commands.standard_commands.edit_commands;

import net.mjduncan.gradebook.commands.standard_commands.StandardCommand;
import net.mjduncan.gradebook.model.Student;

public class ChangeSIDCommand implements StandardCommand {

    private final Student student;
    private final Integer oldSID;
    private final Integer newSID;

    public ChangeSIDCommand(Student student, Integer oldSID, Integer newSID) {
        this.student = student;
        this.oldSID = oldSID;
        this.newSID = newSID;
    }

    @Override
    public void execute() {
        student.setSid(newSID);
    }

    @Override
    public void undo() {
        student.setSid(oldSID);
    }

    @Override
    public void redo() {
        execute();
    }
}

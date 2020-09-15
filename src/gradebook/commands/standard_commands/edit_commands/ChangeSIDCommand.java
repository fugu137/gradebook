package gradebook.commands.standard_commands.edit_commands;

import gradebook.commands.standard_commands.StandardCommand;
import gradebook.model.Student;

public class ChangeSIDCommand implements StandardCommand {

    private Student student;
    private Integer oldSID;
    private Integer newSID;

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

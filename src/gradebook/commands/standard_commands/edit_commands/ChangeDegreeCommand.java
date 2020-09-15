package gradebook.commands.standard_commands.edit_commands;

import gradebook.commands.standard_commands.StandardCommand;
import gradebook.model.Student;

public class ChangeDegreeCommand implements StandardCommand {

    private Student student;
    private String oldDegree;
    private String newDegree;

    public ChangeDegreeCommand(Student student, String oldDegree, String newDegree) {
        this.student = student;
        this.oldDegree = oldDegree;
        this.newDegree = newDegree;
    }

    @Override
    public void execute() {
        student.setDegree(newDegree);
    }

    @Override
    public void undo() {
        student.setDegree(oldDegree);
    }

    @Override
    public void redo() {
        execute();
    }
}

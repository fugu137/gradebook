package gradebook.commands;

import gradebook.model.Student;

public class GivenNamesChangeCommand extends NameChangeCommand {

    public GivenNamesChangeCommand(Student student, String oldName, String newName) {
        super(student, oldName, newName);
    }

    @Override
    public void execute() {
        getStudent().setGivenNames(getNewName());
    }

    @Override
    public void undo() {
        getStudent().setGivenNames(getOldName());
    }

    @Override
    public void redo() {
        execute();
    }
}

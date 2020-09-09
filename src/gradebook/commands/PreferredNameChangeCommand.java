package gradebook.commands;

import gradebook.model.Student;

public class PreferredNameChangeCommand extends NameChangeCommand {

    public PreferredNameChangeCommand(Student student, String oldName, String newName) {
        super(student, oldName, newName);
    }

    @Override
    public void execute() {
        getStudent().setPreferredName(getNewName());
    }

    @Override
    public void undo() {
        getStudent().setPreferredName(getOldName());
    }

    @Override
    public void redo() {
        execute();
    }
}

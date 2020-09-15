package gradebook.commands.standard_commands.edit_commands;

import gradebook.model.Student;

public class ChangePreferredNameCommand extends ChangeNameCommand {

    public ChangePreferredNameCommand(Student student, String oldName, String newName) {
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

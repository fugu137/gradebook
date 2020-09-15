package gradebook.commands.standard_commands.edit_commands;

import gradebook.model.Student;

public class ChangeSurnameCommand extends ChangeNameCommand {

    public ChangeSurnameCommand(Student student, String oldName, String newName) {
        super(student, oldName, newName);
    }

    @Override
    public void execute() {
        getStudent().setSurname(getNewName());
    }

    @Override
    public void undo() {
        getStudent().setSurname(getOldName());
    }

    @Override
    public void redo() {
       execute();
    }
}

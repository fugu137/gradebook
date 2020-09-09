package gradebook.commands;

import gradebook.model.Student;

public class SurnameChangeCommand extends NameChangeCommand {

    public SurnameChangeCommand(Student student, String oldName, String newName) {
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

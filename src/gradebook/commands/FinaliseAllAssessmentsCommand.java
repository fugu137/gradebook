package gradebook.commands;

import gradebook.model.AssessmentSetData;
import gradebook.model.StdAssessmentData;
import gradebook.model.Student;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class FinaliseAllAssessmentsCommand implements UserCommand {

    private ObservableList<Student> students;
    private List<UserCommand> commands;

    public FinaliseAllAssessmentsCommand(ObservableList<Student> students) {
        this.students = students;
        this.commands = new ArrayList<>();
    }

    @Override
    public void execute() {
        for (Student s : students) {
            s.getAssessmentDataList().forEach(d -> {
                if (d instanceof StdAssessmentData) {
                    UserCommand command = new FinaliseStdAssessmentDataCommand((StdAssessmentData) d);
                    command.execute();
                    commands.add(command);
                }
                if (d instanceof AssessmentSetData) {
                    UserCommand command = new FinaliseAssessmentSetDataCommand((AssessmentSetData) d);
                    command.execute();
                    commands.add(command);
                }
            });
        }
    }

    @Override
    public void undo() {
        for (UserCommand c : commands) {
            c.undo();
        }
    }

    @Override
    public void redo() {
        commands.forEach(UserCommand::execute);
    }
}

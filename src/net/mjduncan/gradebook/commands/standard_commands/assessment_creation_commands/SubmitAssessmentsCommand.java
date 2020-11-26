package net.mjduncan.gradebook.commands.standard_commands.assessment_creation_commands;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import net.mjduncan.gradebook.AssessmentCreationController;
import net.mjduncan.gradebook.MainController;
import net.mjduncan.gradebook.commands.standard_commands.StandardCommand;
import net.mjduncan.gradebook.model.Assessment;
import net.mjduncan.gradebook.model.AssessmentCreationBar;

public class SubmitAssessmentsCommand implements StandardCommand {

    private MainController mainController;
    private AssessmentCreationController assessmentCreationController;
    private ObservableList<AssessmentCreationBar> assessmentCreationBars;
    private ObservableList<StandardCommand> commands;
    private Button addAssessmentsButton;
    private Button modifyAssessmentsButton;

    public SubmitAssessmentsCommand(MainController mainController, AssessmentCreationController assessmentCreationController, ObservableList<AssessmentCreationBar> assessmentCreationBars) {
        this.mainController = mainController;
        this.assessmentCreationController = assessmentCreationController;
        this.assessmentCreationBars = assessmentCreationBars;
        this.commands = FXCollections.observableArrayList();
        this.addAssessmentsButton = mainController.getAddAssessmentsButton();
        this.modifyAssessmentsButton = mainController.getModifyAssessmentsButton();
    }

    @Override
    public void execute() {
        ObservableList<Assessment> assessments = FXCollections.observableArrayList();

        if (assessmentCreationController.getAssessmentsToRemove().size() > 0) {
            RemoveAssessmentsSubCommand removeCommand = new RemoveAssessmentsSubCommand(mainController, assessmentCreationController.getAssessmentsToRemove());
            removeCommand.execute();
            commands.add(removeCommand);
        }

        for (AssessmentCreationBar bar : assessmentCreationBars) {

            if (bar.isActive()) {

                if (bar.getAssessment() == null) {
//                    System.out.println("Adding assessment!");
                    AddBarAssessmentSubCommand command = new AddBarAssessmentSubCommand(bar);
                    command.execute();
                    commands.add(command);

                    assessments.add(bar.getAssessment());

                } else {
//                    System.out.println("Modifying assessment!");
                    ModifyAssessmentSubCommand command = new ModifyAssessmentSubCommand(mainController, bar);
                    command.execute();
                    commands.add(command);
                }
            }
        }

        if (assessments.size() > 0) {
            SetupAllAssessmentsSubCommand setupCommand = new SetupAllAssessmentsSubCommand(mainController, assessments, addAssessmentsButton, modifyAssessmentsButton);
            setupCommand.execute();
            commands.add(setupCommand);
        }
    }

    @Override
    public void undo() {

        for (StandardCommand command : commands) {
            command.undo();
        }
    }

    @Override
    public void redo() {

        for (StandardCommand command : commands) {
            command.redo();
        }
    }

}

package gradebook.commands.standard_commands.assessment_creation_commands;

import gradebook.MainController;
import gradebook.commands.standard_commands.StandardCommand;
import gradebook.model.Assessment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;

public class SetupAllAssessmentsSubCommand implements StandardCommand {

    private MainController mainController;
    private ObservableList<Assessment> assessments;
    private Button addAssessmentsButton;
    private Button modifyAssessmentsButton;
    private boolean addAssessmentsButtonWasOn;

    private ObservableList<StandardCommand> commands;

    public SetupAllAssessmentsSubCommand(MainController mainController, ObservableList<Assessment> assessments, Button addAssessmentsButton, Button modifyAssessmentsButton) {
        this.mainController = mainController;
        this.assessments = assessments;
        this.addAssessmentsButton = addAssessmentsButton;
        addAssessmentsButtonWasOn = !addAssessmentsButton.isDisabled();

        this.modifyAssessmentsButton = modifyAssessmentsButton;

        this.commands = FXCollections.observableArrayList();
    }

    @Override
    public void execute() {

        for (Assessment assessment : assessments) {
            AddAssessmentSubCommand command = new AddAssessmentSubCommand(mainController, assessment);
            command.execute();
            commands.add(command);
        }

        mainController.createTotalColumn();

        addAssessmentsButton.setDisable(true);
        modifyAssessmentsButton.setDisable(false);
        mainController.setStatusText("Assessments successfully created...", 4);
    }

    @Override
    public void undo() {
        for (StandardCommand command : commands) {
            command.undo();
        }

        if (addAssessmentsButtonWasOn) {
            mainController.removeTotalColumn();

            addAssessmentsButton.setDisable(false);
            modifyAssessmentsButton.setDisable(true);
        }
    }

    @Override
    public void redo() {
        for (StandardCommand command : commands) {
            command.redo();
        }

        mainController.createTotalColumn();

        addAssessmentsButton.setDisable(true);
        modifyAssessmentsButton.setDisable(false);
        mainController.setStatusText("Assessments successfully created...", 4);
    }


}

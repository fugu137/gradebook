package net.mjduncan.gradebook.commands.standard_commands.assessment_creation_commands;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.mjduncan.gradebook.MainController;
import net.mjduncan.gradebook.commands.standard_commands.StandardCommand;
import net.mjduncan.gradebook.enums.AssessmentType;
import net.mjduncan.gradebook.model.AssessmentSet;
import net.mjduncan.gradebook.model.StdAssessment;
import net.mjduncan.gradebook.tools.CourseManager;

public class AddSubAssessmentsSubCommand implements StandardCommand {

    private MainController mainController;
    private CourseManager courseManager;
    private AssessmentSet assessmentSet;
    private int newQuantity;
    private ObservableList<StdAssessment> subAssessments;

    AddSubAssessmentsSubCommand(MainController mainController, AssessmentSet assessmentSet, int newQuantity) {
        this.mainController = mainController;
        this.courseManager = mainController.getCourseManager();
        this.assessmentSet = assessmentSet;
        this.newQuantity = newQuantity;
        this.subAssessments = FXCollections.observableArrayList();
    }

    @Override
    public void execute() {
        int oldQuantity = assessmentSet.getQuantity();
        int number = newQuantity - oldQuantity;

        for (int i = 0; i < number; i++) {
            String name = assessmentSet.getName() + " " + (assessmentSet.getQuantity() + 1);
            System.out.println("Name of quiz: " + name);
            AssessmentType type = assessmentSet.getType();
            StdAssessment subAssessment = StdAssessment.newSubAssessment(name, type);

            courseManager.assignNewSubAssessment(assessmentSet, subAssessment);
            mainController.getBlankStudent().addSubAssessmentData(assessmentSet, subAssessment);
            mainController.createSubAssessmentColumn(assessmentSet, subAssessment);
            subAssessments.add(subAssessment);
        }
    }

    @Override
    public void undo() {
        courseManager.unassignSubAssessments(assessmentSet, subAssessments);
        mainController.getBlankStudent().removeSubAssessmentData(assessmentSet, subAssessments);

        subAssessments.forEach(s -> {
            mainController.removeStdAssessmentColumn(s);
        });

    }

    @Override
    public void redo() {
        subAssessments.forEach(s -> {
            courseManager.assignNewSubAssessment(assessmentSet, s);
            mainController.getBlankStudent().addSubAssessmentData(assessmentSet, s);
            mainController.createSubAssessmentColumn(assessmentSet, s);
        });
    }


}

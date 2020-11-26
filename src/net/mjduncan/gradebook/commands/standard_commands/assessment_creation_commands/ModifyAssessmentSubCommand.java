package net.mjduncan.gradebook.commands.standard_commands.assessment_creation_commands;

import net.mjduncan.gradebook.MainController;
import net.mjduncan.gradebook.commands.standard_commands.StandardCommand;
import net.mjduncan.gradebook.enums.AssessmentType;
import net.mjduncan.gradebook.model.Assessment;
import net.mjduncan.gradebook.model.AssessmentCreationBar;
import net.mjduncan.gradebook.model.AssessmentSet;
import net.mjduncan.gradebook.model.StdAssessment;

public class ModifyAssessmentSubCommand implements StandardCommand {

    private final MainController mainController;
    private final AssessmentCreationBar bar;
    private StandardCommand changeQuantityCommand;
    private Assessment assessmentCopyForUndo;
    private Assessment assessmentCopyForRedo;


    ModifyAssessmentSubCommand(MainController mainController, AssessmentCreationBar bar) {
        this.mainController = mainController;
        this.bar = bar;
    }

    @Override
    public void execute() {
        assessmentCopyForUndo = copyAssessment(bar.getAssessment());

        String name = bar.getName();
        AssessmentType type = bar.getType();
        Double weighting = 1.0 * bar.getWeighting() / 100;

        if (bar.getAssessment() instanceof AssessmentSet) {
            AssessmentSet assessmentSet = (AssessmentSet) bar.getAssessment();

            Integer bestOf = bar.getBestOf();

            changeAssessmentValues(assessmentSet, type, name, weighting, bestOf);

            Integer newQuantity = bar.getQuantity();
            Integer oldQuantity = assessmentSet.getQuantity();

            if (newQuantity > oldQuantity) {
                changeQuantityCommand = new AddSubAssessmentsSubCommand(mainController, assessmentSet, newQuantity);
                changeQuantityCommand.execute();

            } else if (newQuantity < oldQuantity) {
                changeQuantityCommand = new RemoveSubAssessmentsSubCommand(mainController, assessmentSet, newQuantity);
                changeQuantityCommand.execute();
            }

        } else {
            changeAssessmentValues(bar.getAssessment(), type, name, weighting, null);
        }
    }

    @Override
    public void undo() {
        if (changeQuantityCommand != null) {
            assessmentCopyForRedo = copyAssessment(bar.getAssessment());
            revertTo(assessmentCopyForUndo);

            changeQuantityCommand.undo();
        }
    }

    @Override
    public void redo() {
        if (changeQuantityCommand != null) {
            changeQuantityCommand.redo();
            revertTo(assessmentCopyForRedo);
        }
    }

    private Assessment copyAssessment(Assessment assessmentToCopy) {
        Assessment assessmentCopy = null;

        AssessmentType type = assessmentToCopy.getType();
        String name = assessmentToCopy.getName();
        Double weighting = assessmentToCopy.getWeighting();

        if (assessmentToCopy instanceof StdAssessment) {
            assessmentCopy = new StdAssessment(name, type, weighting);
        }

        if (assessmentToCopy instanceof AssessmentSet) {
            Integer quantity = ((AssessmentSet) assessmentToCopy).getQuantity();
            Integer bestOf = ((AssessmentSet) assessmentToCopy).getBestOf();
            assessmentCopy = new AssessmentSet(name, type, weighting, quantity, bestOf);
        }

        return assessmentCopy;
    }

    private void revertTo(Assessment assessment) {
        AssessmentType type = assessment.getType();
        String name = assessment.getName();
        Double weighting = assessment.getWeighting();
        Integer bestOf = null;

        if (assessment instanceof AssessmentSet) {
            AssessmentSet assessmentSet = (AssessmentSet) assessment;
            bestOf = assessmentSet.getBestOf();
        }

        changeAssessmentValues(bar.getAssessment(), type, name, weighting, bestOf);
        syncBarFields(type, name, weighting, bestOf);
    }

    private void changeAssessmentValues(Assessment assessment, AssessmentType type, String name, Double weighting, Integer bestOf) {
        assessment.setType(type);
        assessment.setName(name);

        assessment.setWeighting(weighting);

        if (bestOf != null && assessment instanceof AssessmentSet) {
            AssessmentSet assessmentSet = (AssessmentSet) assessment;
            assessmentSet.setBestOf(bestOf);
        }
    }

    private void syncBarFields(AssessmentType type, String name, Double weighting, Integer bestOf) {
        bar.setType(type);
        bar.setName(name);
        bar.setWeighting(weighting);
        bar.setBestOf(bestOf);
    }
}

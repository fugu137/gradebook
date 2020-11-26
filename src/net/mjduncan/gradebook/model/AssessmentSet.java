package net.mjduncan.gradebook.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import net.mjduncan.gradebook.enums.AssessmentType;
import net.mjduncan.gradebook.tools.NumberRounder;

public class AssessmentSet implements Assessment {

    private final StringProperty name;
    private ObservableList<StdAssessment> stdAssessments;
    private AssessmentType type;
    private final DoubleProperty weighting;
    private final IntegerProperty quantity;
    private final IntegerProperty bestOf;

    public AssessmentSet(String name, AssessmentType type, Double weighting, Integer quantity, Integer bestOf) {
        this.name = new SimpleStringProperty(name);
        this.type = type;
        this.weighting = new SimpleDoubleProperty(weighting);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.bestOf = new SimpleIntegerProperty(bestOf);

        createAssessments();
    }

    private void createAssessments() {
        this.stdAssessments = FXCollections.observableArrayList();

        for (int i = 0; i < quantity.getValue(); i++) {
            stdAssessments.add(StdAssessment.newSubAssessment(name.getValue() + " " + (i + 1), type));
        }
    }

    @Override
    public String getName() {
        return name.getValue();
    }

    public StringProperty nameProperty() {
        return name;
    }

    @Override
    public void setName(String name) {
        String oldName = getName();
        this.name.set(name);

        stdAssessments.forEach(std -> {
            String stdName = std.getName();
            std.setName(stdName.replace(oldName, name));
        });
    }

    public ObservableList<StdAssessment> getStdAssessments() {
        return stdAssessments;
    }

    @Override
    public Double getWeighting() {
        return weighting.getValue();
    }

    public DoubleProperty weightingProperty() {
        return weighting;
    }

    public void setWeighting(Double weighting) {
        this.weighting.set(weighting);
    }

    public Integer getQuantity() {
        return quantity.getValue();
    }

//    public void setQuantity(int quantity) {
//        this.quantity.set(quantity);
//    }
    public void addSubAssessment(StdAssessment subAssessment) {
        stdAssessments.add(subAssessment);
        this.quantity.set(getQuantity() + 1);
    }

    public void removeSubAssessment(StdAssessment subAssessment) {
        stdAssessments.remove(subAssessment);
        this.quantity.set(getQuantity() - 1);
    }

//    public void setQuantity(Integer quantity) {
//        Integer oldQuantity = this.quantity.getValue();
//
//        if (quantity < oldQuantity) {
//            for (int i = 0; i < oldQuantity - quantity; i++) {
//                System.out.println(stdAssessments.size() - 1);
//                stdAssessments.remove(stdAssessments.size() - 1);
//            }
//
//        } else {
//            for (int i = oldQuantity + 1; i <= quantity; i++) {
//                stdAssessments.add(new StdAssessment(name.getValue() + " " + (i + 1), type, null));
//            }
//        }


//        int difference = quantity - oldQuantity;
//
//        this.quantity.set(quantity);
//
//        if (quantity > oldQuantity) {
//            for (int i = oldQuantity + 1; i <= quantity; i++) {
//                stdAssessments.add(new StdAssessment(name.getValue() + " " + (i + 1), type, null));
//            }
//        }
//
//        if (quantity < oldQuantity) {
//            for (int i = oldQuantity; i > quantity; i--) {
//                stdAssessments.remove(i);
//            }
//        }
//    }

//    public void addSubAssessment(StdAssessment assessment) {
//        stdAssessments.add(assessment);
//        quantity.set(quantity.getValue() + 1);
//    }

    public Integer getBestOf() {
        return bestOf.getValue();
    }

    public IntegerProperty bestOfProperty() {
        return bestOf;
    }

    public void setBestOf(Integer bestOf) {
        this.bestOf.set(bestOf);
    }

    @Override
    public AssessmentType getType() {
        return type;
    }

    @Override
    public void setType(AssessmentType type) {
        this.type = type;
    }

    public void renameStdAssessments(String[] names) {
        for (int i = 0; i < names.length; i++) {
            stdAssessments.get(i).setName(names[i]);
        }
    }

    @Override
    public String columnName() {
        return name.getValue() + " Total";
    }

    @Override
    public VBox infoBox() {
        VBox box = new VBox();
        GridPane grid = new GridPane();
        grid.setVgap(4);
        box.setPadding(new Insets(16, 16, 20, 16));

        grid.addRow(0, new Label("Type: "), new Label(getType().toString()));
        grid.addRow(1, new Label("Weighting:   "), new Label(NumberRounder.roundToInt(getWeighting() * 100) + "%"));
        grid.addRow(2, new Label("Quantity: "), new Label(getQuantity().toString()));
        grid.addRow(3, new Label("Best of: "), new Label(getBestOf().toString()));

        box.getChildren().add(grid);
        return box;
    }
}

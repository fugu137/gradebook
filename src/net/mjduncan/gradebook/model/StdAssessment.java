package net.mjduncan.gradebook.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import net.mjduncan.gradebook.enums.AssessmentType;
import net.mjduncan.gradebook.tools.NumberRounder;

public class StdAssessment implements Assessment {

    private StringProperty name;
    private AssessmentType type;
    private DoubleProperty weighting;

    public StdAssessment(String name, AssessmentType type, Double weighting) {
        this.name = new SimpleStringProperty(name);
        this.type = type;

        if (weighting != null) {
            this.weighting = new SimpleDoubleProperty(weighting);
        } else {
            this.weighting = null;
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
        this.name.set(name);
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

    @Override
    public AssessmentType getType() {
        return type;
    }

    @Override
    public String columnName() {
        return name.getValue();
    }

    @Override
    public void setType(AssessmentType type) {
        this.type = type;
    }

    @Override
    public VBox infoBox() {
        VBox box = new VBox();
        GridPane grid = new GridPane();
        grid.setVgap(4);
        box.setPadding(new Insets(16, 16, 20, 16));

        if (weighting != null) {
            grid.addRow(0, new Label("Type: "), new Label(getType().toString()));
            grid.addRow(1, new Label("Weighting:   "), new Label(NumberRounder.roundToInt(getWeighting() * 100) + "%"));

            box.getChildren().add(grid);
            return box;

        } else {
            return null;
        }
    }

}

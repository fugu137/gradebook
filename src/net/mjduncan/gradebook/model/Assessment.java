package net.mjduncan.gradebook.model;

import javafx.scene.layout.VBox;
import net.mjduncan.gradebook.enums.AssessmentType;

public interface Assessment {

    void setWeighting(Double weighting);
    Double getWeighting();
    AssessmentType getType();
    void setType(AssessmentType type);
    String getName();
    void setName(String newName);
    String columnName();
    VBox infoBox();

}

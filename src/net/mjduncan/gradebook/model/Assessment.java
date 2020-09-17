package net.mjduncan.gradebook.model;

import javafx.scene.layout.VBox;
import net.mjduncan.gradebook.enums.AssessmentType;

public interface Assessment {

    public void setWeighting(Double weighting);
    public Double getWeighting();
    public AssessmentType getType();
    public void setType(AssessmentType type);
    public String getName();
    public void setName(String newName);
    public String columnName();
    public VBox infoBox();

}

package gradebook.model;

import gradebook.enums.AssessmentType;
import javafx.scene.layout.VBox;

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

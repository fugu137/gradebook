package gradebook.model;

import gradebook.enums.AssessmentType;

public interface Assessment {

    public Double getWeighting();
    public AssessmentType getType();
    public void setType(AssessmentType type);
    public String getName();
    public void setName(String newName);
    public String columnName();

}

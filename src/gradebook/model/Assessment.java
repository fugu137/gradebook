package gradebook.model;

import gradebook.enums.AssessmentType;

public interface Assessment {

    public Double getWeighting();
    public AssessmentType getType();
    public String getName();
}

package gradebook.model;

import gradebook.enums.Gender;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Collection;
import java.util.Objects;


public class Student {

    private StringProperty surname;
    private StringProperty givenNames;
    private StringProperty preferredName;
    private CourseCohort courseCohort;
    private ObjectProperty<Class> classGroup;
    private Gender gender;
    private ObjectProperty<Integer> sid;
    private StringProperty degree;
    private StringProperty email;

    private Grades grades;


    public Student() {
        this(null, null, null, null, null, null, null, null);
    }

    public Student(String surname, String givenNames, String preferredName, Class classGroup, Gender gender, Integer sid, String degree, String email) {
        this.surname = new SimpleStringProperty(surname);
        this.givenNames = new SimpleStringProperty(givenNames);
        this.preferredName = new SimpleStringProperty(preferredName);
        this.classGroup = new SimpleObjectProperty<>(classGroup);
        this.gender = gender;
        this.sid = new SimpleObjectProperty<>(sid);
        this.degree = new SimpleStringProperty(degree);
        this.email = new SimpleStringProperty(email);
        this.grades = new Grades();

        addTotalGradeListener();
    }

    private void addTotalGradeListener() {
        Class studentClass = classGroup.getValue();

        grades.totalGradeProperty().addListener(obs -> {
            if (studentClass != null) {
                studentClass.updateTotalGradeStatistics(this, grades.totalGradeProperty());
            }
            if (courseCohort != null) {
                courseCohort.updateTotalGradeStatistics(this, grades.totalGradeProperty());
            }
        });
    }

    public void setCourseCohort(CourseCohort courseCohort) {
        this.courseCohort = courseCohort;
    }

    public String getSurname() {
        return surname.get();
    }

    public StringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getGivenNames() {
        return givenNames.get();
    }

    public StringProperty givenNamesProperty() {
        return givenNames;
    }

    public void setGivenNames(String givenNames) {
        this.givenNames.set(givenNames);
    }

    public String getPreferredName() {
        return preferredName.get();
    }

    public StringProperty preferredNameProperty() {
        return preferredName;
    }

    public void setPreferredName(String preferredName) {
        this.preferredName.set(preferredName);
    }

    public Class getClassGroup() {
        return classGroup.getValue();
    }

    public ObjectProperty<Class> classGroupProperty() {
        return classGroup;
    }

    public void setClassGroup(Class classGroup) {
        this.classGroup.set(classGroup);
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getSid() {
        return sid.get();
    }

    public ObjectProperty<Integer> sidProperty() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid.set(sid);
    }

    public String getDegree() {
        return degree.get();
    }

    public StringProperty degreeProperty() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree.set(degree);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }


    //Main Methods//
    public void addStdAssessmentData(StdAssessment stdAssessment) {
        Class studentClass = classGroup.getValue();

        StdAssessmentData data = new StdAssessmentData(stdAssessment);
        grades.add(stdAssessment, data);

        data.gradeProperty().addListener(obs -> {
            grades.updateTotalGrade();

            if (data.gradeProperty() != null && data.gradeProperty().getValue() != null) {
                if (studentClass != null) {
                    studentClass.updateAssessmentStatistics(stdAssessment, this, data.gradeProperty());
                }
                if (courseCohort != null) {
                    courseCohort.updateAssessmentStatistics(stdAssessment, this, data.gradeProperty());
                }
            }
        });
    }

    public void addAssessmentSetData(AssessmentSet assessmentSet) {
        Class studentClass = classGroup.getValue();

        AssessmentSetData data = new AssessmentSetData(assessmentSet);
        grades.add(assessmentSet, data);

        data.gradeProperty().addListener(obs -> {
            grades.updateTotalGrade();

            if (data.gradeProperty() != null && data.gradeProperty().getValue() != null) {
                if (studentClass != null) {
                    studentClass.updateAssessmentStatistics(assessmentSet, this, data.gradeProperty());
                }
                if (courseCohort != null) {
                    courseCohort.updateAssessmentStatistics(assessmentSet, this, data.gradeProperty());
                }
            }
        });

        for (StdAssessmentData d : data.getStdAssessmentDataList()) {
            d.gradeProperty().addListener(obs -> data.updateAssessmentSetTotalGrade());
        }
    }

    public void removeAssessmentData(Assessment assessment) {
        grades.remove(assessment);
    }

    public ObjectProperty<Integer> stdAssessmentGradeProperty(StdAssessment stdAssessment) {
        StdAssessmentData data = (StdAssessmentData) grades.get(stdAssessment);

        if (data != null) {
            return data.gradeProperty();

        } else {
            System.out.println("Assessment not found! [Student: " + this.getSurname() + ", Assessment: " + stdAssessment.getName() + "]");
            return null;
        }
    }

    public ObjectProperty<Double> assessmentSetTotalGradeProperty(AssessmentSet assessmentSet) {
        AssessmentSetData data = (AssessmentSetData) grades.get(assessmentSet);

        if (data != null) {
            return data.gradeProperty();

        } else {
            System.out.println("AssessmentSet not found! [Student: " + this.getSurname() + ", Assessment: " + assessmentSet.getName() + "]");
            return null;
        }
    }

    public ObjectProperty<Integer> assessmentSetGradeProperty(AssessmentSet assessmentSet, StdAssessment
            stdAssessment) {
        AssessmentSetData data = (AssessmentSetData) grades.get(assessmentSet);

        if (data != null) {
            return data.assessmentGradeProperty(stdAssessment);

        } else {
            System.out.println("AssessmentSet not found! [Student: " + this.getSurname() + ", Assessment: " + assessmentSet.getName() + "]");
            return null;
        }
    }

    public ObjectProperty<Double> totalGradeProperty() {
        return grades.totalGradeProperty();
    }

    public Double getTotalGrade() {
        return grades.getTotalGrade();
    }

    public Collection<AssessmentData> getAssessmentDataList() {
        return grades.assessmentDataList();
    }

    public AssessmentData getAssessmentData(Assessment assessment) {
        return grades.get(assessment);
    }

    public void setStdAssessmentGrade(StdAssessment stdAssessment, Integer grade) {
        StdAssessmentData stdAssessmentData = (StdAssessmentData) grades.get(stdAssessment);
        stdAssessmentData.setGrade(grade);
    }


    //Overridden Methods//
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        Student student = (Student) o;
        return Objects.equals(getSurname(), student.getSurname()) &&
                Objects.equals(getGivenNames(), student.getGivenNames()) &&
                Objects.equals(getPreferredName(), student.getPreferredName()) &&
                Objects.equals(getClassGroup(), student.getClassGroup()) &&
                getGender() == student.getGender() &&
                Objects.equals(getSid(), student.getSid()) &&
                Objects.equals(getDegree(), student.getDegree()) &&
                Objects.equals(getEmail(), student.getEmail()) &&
                Objects.equals(grades, student.grades);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, givenNames, preferredName, classGroup, gender, sid, degree, email, grades);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getGivenNames() + " " + getSurname() + ", class: " + getClassGroup() + ", preferred name: " + getPreferredName() +
                ", gender: " + getGender() + ", sid: " + getSid() + ", degree: " + getDegree() + ", email: " + getEmail() + " ");

        sb.append(" [Total Grade: ").append(getTotalGrade()).append(" ");
        for (AssessmentData d : grades.assessmentDataList()) {
            if (d instanceof StdAssessmentData) {
                StdAssessmentData stdData = (StdAssessmentData) d;
                sb.append(stdData.getStdAssessment().getName()).append(", ").append(stdData.getGrade()).append("; ");
            }
            if (d instanceof AssessmentSetData) {
                AssessmentSetData setData = (AssessmentSetData) d;
                sb.append(setData.getAssessment().getName()).append(": ");

                for (StdAssessmentData s : setData.getStdAssessmentDataList()) {
                    sb.append(s.getStdAssessment().getName()).append(", ").append(s.getGrade()).append("; ");
                }
            }
        }
        sb.append("]");
        return sb.toString();
    }
}

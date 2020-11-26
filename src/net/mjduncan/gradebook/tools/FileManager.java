package net.mjduncan.gradebook.tools;

import javafx.collections.ObservableList;
import net.mjduncan.gradebook.MainController;
import net.mjduncan.gradebook.enums.AssessmentType;
import net.mjduncan.gradebook.enums.Gender;
import net.mjduncan.gradebook.model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class FileManager {

    private File file;

    public void saveAs(File fileFromFileChooser, MainController mainController) {
        this.file = fileFromFileChooser;
        save(mainController);
    }

    public void save(MainController mainController) {
        CourseManager courseManager = mainController.getCourseManager();
        String courseName = courseManager.getCourseName();
        ObservableList<ClassGroup> classes = courseManager.getClasses();

        if (file == null) {
            System.out.println("No file found!");
        }

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(file, false));

            for (ClassGroup c : classes) {
                ObservableList<Student> students = c.getStudents();

                for (Student s : students) {
                    String studentInfo = courseName + ", " + s.getClassGroup() + " | " + s.getSid() + ", " + s.getSurname() + ", " +
                            s.getGivenNames() + ", " + s.getPreferredName() + ", " + s.getGender() + ", " + s.getDegree() + ", " +
                            s.getEmail() + " | ";

                    Collection<AssessmentData> assessmentData = s.getAssessmentDataList();

                    List<StdAssessmentData> stdAssessmentData = assessmentData.stream().filter(a -> a instanceof StdAssessmentData).map(a -> (StdAssessmentData) a).collect(Collectors.toList());
                    List<AssessmentSetData> assessmentSetData = assessmentData.stream().filter(a -> a instanceof AssessmentSetData).map(a -> (AssessmentSetData) a).collect(Collectors.toList());

                    StringBuilder stdAssessmentInfoSB = new StringBuilder();
                    String spacer = "";

                    for (StdAssessmentData data : stdAssessmentData) {
                        AssessmentType type = data.getStdAssessment().getType();
                        String name = data.getStdAssessment().getName();
                        String weighting = String.valueOf(data.getStdAssessment().getWeighting());
                        String grade = String.valueOf(data.getGrade());

                        stdAssessmentInfoSB.append(spacer).append(type).append(", ").append(name).append(", ").append(weighting)
                                .append(", ").append(grade);
                        spacer = "; ";
                    }

                    StringBuilder assessmentSetInfoSB = new StringBuilder();
                    spacer = "";

                    for (AssessmentSetData data : assessmentSetData) {
                        AssessmentType type = data.getAssessment().getType();
                        String name = data.getAssessment().getName();
                        String weighting = String.valueOf(data.getAssessment().getWeighting());
                        String quantity = String.valueOf(data.getAssessment().getQuantity());
                        String bestOf = String.valueOf(data.getAssessment().getBestOf());
                        String grade = String.valueOf(data.getGrade());

                        assessmentSetInfoSB.append(spacer).append(type).append(", ").append(name).append(", ").append(weighting).append(", ")
                                .append(quantity).append(", ").append(bestOf).append(", ").append(grade).append(": ");

                        String comma = "";

                        for (StdAssessmentData stdData : data.getStdAssessmentDataList()) {
                            String stdAssessmentName = stdData.getAssessment().getName();
                            String stdAssessmentGrade = String.valueOf(stdData.getGrade());

                            assessmentSetInfoSB.append(comma).append(stdAssessmentName).append(", ").append(stdAssessmentGrade);
                            comma = ", ";
                        }
                        spacer = "; ";
                    }

                    String stdAssessmentInfo = stdAssessmentInfoSB.toString() + " | ";
                    String assessmentSetInfo = assessmentSetInfoSB.toString() + " | ";

                    writer.println(studentInfo + stdAssessmentInfo + assessmentSetInfo);
                }
            }

            writer.close();
            System.out.println("Save successful!");

            mainController.setStatusText("Save successful!", 4);

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();

            mainController.setStatusText("Gradebook failed to save!", 4);
            //TODO: detailed error message?
        }
    }

    public void load(File fileFromFileChooser, MainController mainController) {

        this.file = fileFromFileChooser;

        CourseManager courseManager = mainController.getCourseManager();
        Map<String, ClassGroup> classMap = new HashMap<>();
        classMap.put("None", courseManager.getUnassigned());

        mainController.reset();

        try {
            Scanner reader = new Scanner(fileFromFileChooser);

            Map<String, StdAssessment> stdAssessmentMap = new LinkedHashMap<>();
            Map<String, AssessmentSet> assessmentSetMap = new LinkedHashMap<>();
            int lineNumber = 1;

            while (reader.hasNextLine()) {
                String line = reader.nextLine();

                if (!line.isEmpty()) {
                    String[] sections = line.split("\\|");

                    String[] courseAndClass = sections[0].trim().split(", ");
                    String[] studentInfo = sections[1].trim().split(", ");
                    String[] stdAssessments = sections[2].trim().split("; ");
                    String[] assessmentSets = sections[3].trim().split("; ");

                    String courseName = courseAndClass[0];
                    courseManager.setCourseName(courseName);

                    if (lineNumber == 1) {
                        if (!(stdAssessments[0].isBlank() && assessmentSets[0].isBlank())) {
                            addAssessments(mainController, stdAssessmentMap, assessmentSetMap, stdAssessments, assessmentSets);
                        } else {
                            mainController.disableModifyAssessmentsButton();
                        }
                    }

                    Student student = makeStudent(classMap, courseAndClass, studentInfo);
                    courseManager.newStudent(student);

                    addGrades(stdAssessmentMap, assessmentSetMap, student, stdAssessments, assessmentSets);
                    lineNumber++;
                }
            }
            if (stdAssessmentMap.size() > 0 || assessmentSetMap.size() > 0) {
                mainController.createTotalColumn();
            }

            mainController.addAllStudentsToTable(courseManager.getAllStudents());

        } catch (Exception e) {
            System.out.println("Error reading from file! " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addAssessments(MainController mainController, Map<String, StdAssessment> stdAssessmentMap, Map<String, AssessmentSet> assessmentSetMap, String[] stdAssessments, String[] assessmentSets) throws IOException {
        for (String s : stdAssessments) {
            String[] assessmentInfo = s.trim().split(", ");
            StdAssessment stdAssessment;

            if (!assessmentInfo[0].isBlank()) {
                AssessmentType type = AssessmentType.valueOf(assessmentInfo[0]);
                String name = assessmentInfo[1];
                Double weighting = Double.parseDouble(assessmentInfo[2]);

                stdAssessment = new StdAssessment(name, type, weighting);
                stdAssessmentMap.put(name, stdAssessment);

                mainController.getAssessmentCreationController().addAssessment(stdAssessment);
            }
        }

        for (String s : assessmentSets) {
            String[] assessmentSetInfo = s.trim().split(", ");
            AssessmentSet set;

            if (!assessmentSetInfo[0].isBlank()) {

                AssessmentType type = AssessmentType.valueOf(assessmentSetInfo[0]);
                String name = assessmentSetInfo[1];
                Double weighting = Double.parseDouble(assessmentSetInfo[2]);
                Integer quantity = Integer.parseInt(assessmentSetInfo[3]);
                Integer bestOf = Integer.parseInt(assessmentSetInfo[4]);

                set = new AssessmentSet(name, type, weighting, quantity, bestOf);
                assessmentSetMap.put(name, set);

                mainController.getAssessmentCreationController().addAssessment(set);


                String[] parts = s.trim().split(": ");
                String[] subAssessmentInfo = parts[1].trim().split(", ");
                String[] subAssessmentNames = new String[subAssessmentInfo.length / 2];

                int index = 0;

                for (int i = 0; i < subAssessmentNames.length; i++) {
                    subAssessmentNames[i] = subAssessmentInfo[index];
                    index = index + 2;
                }

                set.renameStdAssessments(subAssessmentNames);
            }
        }

        mainController.getAssessmentCreationController().submitAssessmentsWithoutClick();
    }

    private void addGrades(Map<String, StdAssessment> stdAssessmentMap, Map<String, AssessmentSet> assessmentSetMap, Student student, String[] stdAssessments, String[] assessmentSets) {

        for (String s : stdAssessments) {
            String[] assessmentInfo = s.trim().split(", ");
            StdAssessment stdAssessment;

            if (!assessmentInfo[0].isBlank()) {
                String name = assessmentInfo[1];
                Integer grade = null;

                if (!assessmentInfo[3].equals("null")) {
                    grade = Integer.parseInt(assessmentInfo[3]);
                }
                stdAssessment = stdAssessmentMap.get(name);

                if (stdAssessment != null) {
                    student.setStdAssessmentGrade(stdAssessment, grade);

                } else {
                    System.out.println("No StdAssessment found for student " + student.getSurname());
                }
            }
        }

        for (String s : assessmentSets) {
            String[] assessmentSetInfo = s.trim().split(", ");
            AssessmentSet set;

            if (!assessmentSetInfo[0].isBlank()) {
                String name = assessmentSetInfo[1];

                    set = assessmentSetMap.get(name);

                if (set != null) {
                    String[] parts = s.trim().split(": ");
                    String[] subAssessmentInfo = parts[1].trim().split(", ");
                    String[] subAssessmentNames = new String[subAssessmentInfo.length / 2];
                    Integer[] subAssessmentGrades = new Integer[subAssessmentInfo.length / 2];

                    int index = 0;

                    for (int i = 0; i < subAssessmentNames.length; i++) {
                        subAssessmentNames[i] = subAssessmentInfo[index];

                        if (subAssessmentInfo[index + 1].equals("null")) {
                            subAssessmentGrades[i] = null;
                        } else {
                            subAssessmentGrades[i] = Integer.parseInt(subAssessmentInfo[index + 1]);
                        }
                        index = index + 2;
                    }

                    AssessmentSetData setData = (AssessmentSetData) student.getAssessmentData(set);
                    setData.setSubAssessmentGrades(subAssessmentGrades);
                }
            }
        }
    }

    private Student makeStudent(Map<String, ClassGroup> classMap, String[] courseAndClass, String[] studentInfo) {
        ClassGroup classGroup;

        if (!classMap.containsKey(courseAndClass[1])) {
            classGroup = new ClassGroup(courseAndClass[1]);
            classMap.put(courseAndClass[1], classGroup);

        } else {
            classGroup = classMap.get(courseAndClass[1]);
        }

        Integer sid = studentInfo[0].equals("null") ? null : Integer.parseInt(studentInfo[0]);
        String surname = studentInfo[1].equals("null") ? null : studentInfo[1];
        String givenNames = studentInfo[2].equals("null") ? null : studentInfo[2];
        String preferredName = studentInfo[3].equals("null") ? null : studentInfo[3];
        Gender gender = studentInfo[4].equals("null") ? null : Gender.valueOf(studentInfo[4]);
        String degree = studentInfo[5].equals("null") ? null : studentInfo[5];
        String email = studentInfo[6].equals("null") ? null : studentInfo[6];

        return new Student(surname, givenNames, preferredName, classGroup, gender, sid, degree, email);
    }

}


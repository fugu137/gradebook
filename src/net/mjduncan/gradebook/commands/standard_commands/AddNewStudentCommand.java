package net.mjduncan.gradebook.commands.standard_commands;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import net.mjduncan.gradebook.MainController;
import net.mjduncan.gradebook.commands.standard_commands.view_commands.FilterByClassAndGradeCommand;
import net.mjduncan.gradebook.enums.Grade;
import net.mjduncan.gradebook.model.Student;
import net.mjduncan.gradebook.model.StudentGroup;
import net.mjduncan.gradebook.tools.CourseManager;

public class AddNewStudentCommand implements StandardCommand {

    private final MainController mainController;
    private final CourseManager courseManager;
    private final ObservableList<Student> tableItems;
    private final Student student;
    private final boolean addBlankStudent;

    public AddNewStudentCommand(MainController mainController, Student student, boolean addBlankStudent) {
        this.mainController = mainController;
        this.courseManager = mainController.getCourseManager();
        this.tableItems = mainController.getTable().getItems();
        this.student = student;
        this.addBlankStudent = addBlankStudent;
    }

    @Override
    public void execute() {
        courseManager.newStudent(student);
        if (addBlankStudent) {
            mainController.newBlankStudent();
        }

        student.totalGradeProperty().addListener(obs -> {
            ComboBox<StudentGroup> classListBox = mainController.getClassListBox();
            ComboBox<Grade> gradeListBox = mainController.getGradeListBox();
            new FilterByClassAndGradeCommand(mainController, classListBox, gradeListBox).execute();
        });
    }

    @Override
    public void undo() {
        courseManager.removeStudent(student);
        tableItems.remove(student);
    }

    @Override
    public void redo() {
        courseManager.newStudent(student);
        tableItems.add(tableItems.size() - 1, student);
    }
}

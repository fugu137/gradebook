package net.mjduncan.gradebook.commands.standard_commands.edit_commands;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import net.mjduncan.gradebook.MainController;
import net.mjduncan.gradebook.commands.standard_commands.StandardCommand;
import net.mjduncan.gradebook.model.Student;
import net.mjduncan.gradebook.tools.StudentCloner;

public class CopyCommand implements StandardCommand {

    private final MainController mainController;
    private final TableView<Student> table;
    private ObservableList<Student> clipBoardCopy;

    public CopyCommand(MainController mainController) {
        this.mainController = mainController;
        this.table = mainController.getTable();
        this.clipBoardCopy = FXCollections.observableArrayList();

        clipBoardCopy = mainController.getClipBoardStudents();
    }

    @Override
    public void execute() {
        mainController.getClipBoardStudents().clear();
        ObservableList<Student> toCopy = table.getSelectionModel().getSelectedItems();
        ObservableList<Student> copied = StudentCloner.run(toCopy);

        mainController.getClipBoardStudents().addAll(copied);
    }

    @Override
    public void undo() {
        mainController.getClipBoardStudents().clear();
        mainController.getClipBoardStudents().addAll(clipBoardCopy);
    }

    @Override
    public void redo() {
        execute();
    }
}

package gradebook.tools;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileChooserWindow {


    public static List<File> displayImportWindow(Window window, String title, boolean setMultiple) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Comma Separated Values", "*.csv"));

        List<File> selectedFiles = new ArrayList<>();

        fileChooser.setTitle(title);

        if (setMultiple) {
            List<File> files = fileChooser.showOpenMultipleDialog(window);

            if (files != null) {
                selectedFiles.addAll(files);
            }

        } else {
            File file = fileChooser.showOpenDialog(window);

            if (file != null) {
                selectedFiles.add(file);
            }
        }
        return selectedFiles;
    }

    public static File displayLoadWindow(Window window, String title) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Gradebook Files", "*.gbk"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setTitle(title);

        return fileChooser.showOpenDialog(window);
    }


    public static File displaySaveWindow(Window window, String title) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Gradebook Files", "*.gbk"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setTitle(title);

        return fileChooser.showSaveDialog(window);
    }
}

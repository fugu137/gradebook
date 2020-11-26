package net.mjduncan.gradebook.tools;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.mjduncan.gradebook.enums.Gender;
import net.mjduncan.gradebook.model.ClassGroup;
import net.mjduncan.gradebook.model.Student;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class StudentImporter {

    private static Scanner reader;


    public static ObservableList<Student> importStudents(File file)  {
        ObservableList<Student> studentList = FXCollections.observableArrayList();

        ClassGroup classGroup = null;

        try {
            reader = new Scanner(file);

            reader.nextLine();
            while (reader.hasNextLine()) {
                String line = reader.nextLine();

                if (!line.isBlank()) {
                    List<String> details = getSubStrings(line);

                    String className = getClassName(details.get(0));
                    Integer sid = Integer.parseInt(details.get(1));
                    String surname = details.get(2);
                    String givenNames = capitalizeEveryWord(details.get(3));
                    String preferredName = capitalizeEveryWord(details.get(4));
                    Gender gender = Gender.valueOf(details.get(5));
                    String degree = details.get(6);
                    String email = details.get(7);

                    if (classGroup == null) {
                        classGroup = new ClassGroup(className);
                    }

                    studentList.add(new Student(surname, givenNames, preferredName, classGroup, gender, sid, degree, email));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentList;
    }

    private static List<String> getSubStrings(String line) {
        List<String> details = new ArrayList<>();

        String[] bigParts = line.trim().split(",\"|\",");

        for (String bigString: bigParts) {
            bigString = bigString.trim();
            String[] smallParts = bigString.split(",");

            for (String smallString: smallParts) {
                smallString = smallString.trim();
                details.add(smallString);
            }
        }
        return details;
    }

    private static String getClassName(String classDetails) {
        int index = classDetails.lastIndexOf('.') + 1;
        return classDetails.substring(index);
    }

    private static String capitalizeEveryWord(String string) {
        String[] words = string.split(" ");
        List<String> formattedWords = new ArrayList<>();

        for (String s: words) {
            String lowerCaseWord = s.toLowerCase().trim();
            String formattedWord = capitalizeWord(lowerCaseWord);

            int i = formattedWord.indexOf("-");
            formattedWord = formattedWord.substring(0, i + 1) + capitalizeWord(formattedWord.substring(i + 1));
            formattedWords.add(formattedWord);
        }

        StringBuilder sb = new StringBuilder();
        for (String s: formattedWords) {
            sb.append(s).append(" ");
        }
        return sb.toString().trim();
    }

    private static String capitalizeWord(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

}


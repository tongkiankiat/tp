package mindexpander.storage;

import mindexpander.data.QuestionBank;
import mindexpander.data.question.Question;
import mindexpander.data.question.QuestionType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Handles saving of {@code QuestionBank} to a text file to ensure data persistence.
 * <p>
 * The file format follows:
 * <pre>
 * FITB|QuestionText|Answer
 * </pre>
 * <p>
 * Currently, only Fill in the Blanks (FITB) questions are supported.
 *
 * @author Jensen Kuok
 * @version 1.0
 * @since 2025-03-06
 */
public class StorageFile {
    private final File file;

    public StorageFile() {
        this.file = new File("./data/MindExpander.txt");
    }

    public StorageFile(String filePath) {
        this.file = new File(filePath);
    }

    /**
     * Saves the given {@code QuestionBank} to the storage file.
     * <p>
     * Ensures the storage directory exists before writing data.
     *
     * @param questionBank The {@code QuestionBank} instance to be saved.
     */
    public void save(QuestionBank questionBank) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < questionBank.getQuestionCount(); i++) {
                Question q = questionBank.getQuestion(i);
                writer.write(formatQuestionForSaving(q));
                writer.newLine();
            }
            System.out.println("Questions saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private String formatQuestionForSaving(Question q) {
        if (q.getType() == QuestionType.FITB) {
            return "FITB|" + q.getQuestion() + "|" + q.getAnswer();
        }
        return "";
    }
}
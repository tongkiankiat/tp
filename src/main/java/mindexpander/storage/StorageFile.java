package mindexpander.storage;

import mindexpander.data.QuestionBank;
import mindexpander.data.question.FillInTheBlanks;
import mindexpander.data.question.Question;
import mindexpander.data.question.QuestionType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading of {@code QuestionBank} to ensure data persistence.
 * <p>
 * The file format follows:
 * <pre>
 * FITB|QuestionText|Answer
 * </pre>
 * <p>
 * Currently, only Fill in the Blanks (FITB) questions are supported.
 *
 * @author Jensen Kuok
 * @version 0.1
 * @since 2025-03-14
 */
public class StorageFile {
    private final File file;

    public StorageFile() {
        this.file = new File("./data/MindExpander.txt");
        //clearFileOnStartup(); // Uncomment to clear MindExpander.txt when StorageFile is initialised
    }

    /**
     * Saves the given {@code QuestionBank} to the storage file.
     * <p>
     * Ensures the storage directory exists before writing data.
     *
     * @param questionBank The {@code QuestionBank} instance to be saved.
     */
    public void save(QuestionBank questionBank) {
        boolean directoryCreated = file.getParentFile().mkdirs();
        if (!directoryCreated) {
            System.out.println("Warning: Failed to create directory for storage file.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < questionBank.getQuestionCount(); i++) {
                Question q = questionBank.getQuestion(i);
                writer.write(formatQuestionForSaving(q));
                writer.newLine();
            }
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

    /**
     * Loads questions from the storage file into a new {@code QuestionBank} instance.
     * <p>
     * If the storage file does not exist, an empty {@code QuestionBank} is returned.
     * Otherwise, it reads the file line by line, parses each question, and adds it
     * to the {@code QuestionBank}.
     * </p>
     *
     * @return A {@code QuestionBank} containing all questions loaded from the storage file.
     *         If the file is missing or empty, an empty {@code QuestionBank} is returned.
     */
    public QuestionBank load() {
        List<Question> questions = new ArrayList<>();

        if (!file.exists()) {
            return new QuestionBank(questions);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Question parsedQuestion = parseQuestionFromFile(line);
                if (parsedQuestion != null) {
                    questions.add(parsedQuestion);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }

        return new QuestionBank(questions);
    }

    /**
     * Parses a line from the storage file and returns a {@code Question}.
     */
    private Question parseQuestionFromFile(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0];
        String questionText = parts[1];
        String answer = parts[2];

        if ("FITB".equals(type)) {
            return new FillInTheBlanks(questionText, answer);
        }
        return null; // Ignore unsupported types for now
    }

    //uncomment to clear MindExpander.txt
    //    private void clearFileOnStartup() {
    //        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
    //            writer.write(""); // Uncomment this line to enable clearing
    //        } catch (IOException e) {
    //            System.out.println("Error clearing data file: " + e.getMessage());
    //        }
    //    }
}

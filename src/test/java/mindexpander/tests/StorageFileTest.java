package mindexpander.tests;

import mindexpander.common.Messages;
import mindexpander.data.QuestionBank;
import mindexpander.data.question.FillInTheBlanks;
import mindexpander.data.question.MultipleChoice;
import mindexpander.data.question.Question;
import mindexpander.storage.StorageFile;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * JUnit test class for {@code StorageFile}.
 * Validates correct save/load behavior and file interactions.
 *
 * @author Jensen
 * @version 1.0
 * @since 2025-03-14
 */
class StorageFileTest {
    private StorageFile storageFile;
    private final String filePath = "./data/MindExpander.txt";
    private final File file = new File(filePath);

    @BeforeEach
    void setUp() {
        storageFile = new StorageFile();
        deleteFileIfExists(); // Ensure clean state before each test
    }

    @AfterEach
    void tearDown() {
        deleteFileIfExists(); // Clean up after test
    }

    private void deleteFileIfExists() {
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testSaveCreatesFile() {
        QuestionBank qb = new QuestionBank();
        qb.addQuestion(new FillInTheBlanks("What is 1+1?", "2"));
        qb.addQuestion(new MultipleChoice("Largest planet?", "Jupiter",
                Arrays.asList("Jupiter", "Earth", "Mars", "Venus")));

        storageFile.save(qb);
        assertTrue(file.exists(), "File should be created after saving.");
    }

    @Test
    void testSaveAndLoadSingleQuestion() {
        QuestionBank qb = new QuestionBank();
        qb.addQuestion(new FillInTheBlanks("Capital of France is ___", "Paris"));
        qb.addQuestion(new MultipleChoice("5 + 3 = ?", "8",
                Arrays.asList("8", "5", "6", "7")));

        storageFile.save(qb);
        QuestionBank loadedQb = storageFile.load();

        assertEquals(2, loadedQb.getQuestionCount(), "Loaded question count should be 2.");
        Question loadedQ = loadedQb.getQuestion(0);
        assertEquals("Capital of France is ___", loadedQ.getQuestion());
        assertEquals("Paris", loadedQ.getAnswer());
        Question loadedMCQ = loadedQb.getQuestion(1);
        assertEquals("5 + 3 = ?", loadedMCQ.getQuestion());
        assertEquals("8", loadedMCQ.getAnswer());
    }

    @Test
    void testSaveAndLoadMultipleQuestions() {
        QuestionBank qb = new QuestionBank();
        qb.addQuestion(new FillInTheBlanks("What is 2+2?", "4"));
        qb.addQuestion(new FillInTheBlanks("The sky is ___", "blue"));
        qb.addQuestion(new FillInTheBlanks("Water freezes at ___ degrees Celsius", "0"));
        qb.addQuestion(new MultipleChoice("Which is a fruit?", "Apple",
                Arrays.asList("Apple", "Chair", "Stone", "Bottle")));

        storageFile.save(qb);
        QuestionBank loadedQb = storageFile.load();

        assertEquals(4, loadedQb.getQuestionCount(), "Loaded question count should be 4.");
        assertEquals("4", loadedQb.getQuestion(0).getAnswer());
        assertEquals("blue", loadedQb.getQuestion(1).getAnswer());
        assertEquals("0", loadedQb.getQuestion(2).getAnswer());
        assertEquals("Apple", loadedQb.getQuestion(3).getAnswer());
        assertEquals("Which is a fruit?", loadedQb.getQuestion(3).getQuestion());
    }

    @Test
    void testLoadFromNonExistentFile() {
        // Ensure file does not exist
        deleteFileIfExists();

        QuestionBank loadedQb = storageFile.load();
        assertEquals(0, loadedQb.getQuestionCount(),
                "Loading from non-existent file should return empty QuestionBank.");
    }

    @Test
    void testLoadWithInvalidDataLine() throws IOException {
        // Create file with one valid and one invalid line
        file.getParentFile().mkdirs(); // Ensure directory exists
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("FITB" + Messages.STORAGE_DELIMITER + "The capital of Japan is ___"
                    + Messages.STORAGE_DELIMITER + "Tokyo\n");
            writer.write("INVALID DATA LINE WITHOUT DELIMITER\n");
        }

        QuestionBank loadedQb = storageFile.load();
        assertEquals(1, loadedQb.getQuestionCount(), "Only valid lines should be loaded.");
        assertEquals("Tokyo", loadedQb.getQuestion(0).getAnswer());
    }
}

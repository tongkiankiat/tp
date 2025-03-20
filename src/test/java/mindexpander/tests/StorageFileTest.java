package mindexpander.tests;

import mindexpander.data.QuestionBank;
import mindexpander.data.question.FillInTheBlanks;
import mindexpander.data.question.Question;
import mindexpander.storage.StorageFile;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

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

        storageFile.save(qb);
        assertTrue(file.exists(), "File should be created after saving.");
    }

    @Test
    void testSaveAndLoadSingleQuestion() {
        QuestionBank qb = new QuestionBank();
        qb.addQuestion(new FillInTheBlanks("Capital of France is ___", "Paris"));

        storageFile.save(qb);
        QuestionBank loadedQb = storageFile.load();

        assertEquals(1, loadedQb.getQuestionCount(), "Loaded question count should be 1.");
        Question loadedQ = loadedQb.getQuestion(0);
        assertEquals("Capital of France is ___", loadedQ.getQuestion());
        assertEquals("Paris", loadedQ.getAnswer());
    }
}

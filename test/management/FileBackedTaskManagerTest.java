package management;
import org.junit.jupiter.api.*;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    final String autosaveFilePath = "tmp";

    @BeforeEach
    void setup() {
        try {
            Files.createFile(Path.of(autosaveFilePath));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @AfterEach
    void teardown() {
        try {
            Files.delete(Path.of(autosaveFilePath));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void saveOneTaskInStorage() {
        // Given
        FileBackedTaskManager ts = new FileBackedTaskManager(autosaveFilePath);

        // When
        Task task = new Task("name", "description");
        ts.addTask(task);
        int taskId = task.getId();

        // Then
        ts = new FileBackedTaskManager(autosaveFilePath);
        assertNotNull(ts.getTaskById(taskId));
    }

    @Test
    void saveOneEpicInStorage() {
        // Given
        FileBackedTaskManager ts = new FileBackedTaskManager(autosaveFilePath);

        // When
        Epic epic = new Epic("name", "description");
        ts.addEpic(epic);
        int epicId = epic.getId();

        // Then
        ts = new FileBackedTaskManager(autosaveFilePath);
        assertNotNull(ts.getEpicById(epicId));
    }

    @Test
    void saveOneSubtaskInStorage() {
        // Given
        FileBackedTaskManager ts = new FileBackedTaskManager(autosaveFilePath);

        // When
        Subtask subtask = new Subtask("name", "description");
        ts.addSubtask(subtask);
        int subtaskId = subtask.getId();

        // Then
        ts = new FileBackedTaskManager(autosaveFilePath);
        assertNotNull(ts.getSubtaskById(subtaskId));
    }

    @Test
    void saveClearTaskInStorage() {
        // Given
        FileBackedTaskManager ts = new FileBackedTaskManager(autosaveFilePath);

        // When
        Task task = new Task("name", "description");
        ts.addTask(task);
        ts.clearTasks();

        // Then
        ts = new FileBackedTaskManager(autosaveFilePath);
        assertEquals(0, ts.getTasks().size());
    }

    @Test
    void saveClearSubtaskInStorage() {
        // Given
        FileBackedTaskManager ts = new FileBackedTaskManager(autosaveFilePath);

        // When
        Subtask subtask = new Subtask("name", "description");
        ts.addSubtask(subtask);
        ts.clearSubtasks();

        // Then
        ts = new FileBackedTaskManager(autosaveFilePath);
        assertEquals(0, ts.getSubtasks().size());
    }

    @Test
    void saveClearEpicInStorage() {
        // Given
        FileBackedTaskManager ts = new FileBackedTaskManager(autosaveFilePath);

        // When
        Epic epic = new Epic("name", "description");
        ts.addEpic(epic);
        ts.clearEpics();

        // Then
        ts = new FileBackedTaskManager(autosaveFilePath);
        assertEquals(0, ts.getEpics().size());
    }

    @Test
    void saveAllTypesTaskInStorage() {
        // Given
        FileBackedTaskManager ts = new FileBackedTaskManager(autosaveFilePath);

        // When
        Task task = new Task("name", "description");
        ts.addTask(task);

        Epic epic = new Epic("name", "description");
        ts.addEpic(epic);

        Subtask subtask = new Subtask("name", "description", epic.getId());
        ts.addSubtask(subtask);

        // Then
        ts = new FileBackedTaskManager(autosaveFilePath);
        // check collection is not empty
        assertEquals(1, ts.getTasks().size());
        assertEquals(1, ts.getSubtasks().size());
        assertEquals(1, ts.getEpics().size());
        // check collection store the same tasks
        assertNotNull(ts.getTaskById(task.getId()));
        assertNotNull(ts.getSubtaskById(subtask.getId()));
        assertNotNull(ts.getEpicById(epic.getId()));
    }

    @Test
    void saveUpdateTaskInStorage() {
        // Given
        FileBackedTaskManager ts = new FileBackedTaskManager(autosaveFilePath);

        // When
        Task task = new Task("name", "description");
        ts.addTask(task);
        task.setStatus(TaskStatus.IN_PROGRESS);
        ts.updateTask(task);

        // Then
        ts = new FileBackedTaskManager(autosaveFilePath);
        assertEquals(TaskStatus.IN_PROGRESS, ts.getTaskById(task.getId()).getStatus());
    }
}
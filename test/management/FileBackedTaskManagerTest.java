package management;
import org.junit.jupiter.api.*;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

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

    /**
     * Тест на сохранение и восстановление задачи из файлового хранилища
     */
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

    /**
     * Тест на сохранение и восстановление эпика из файлового хранилища
     */
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

    /**
     * Тест на сохранение и восстановление подзадачи из файлового хранилища
     */
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

    /**
     * Тест на корректное сохранение состояния после очистки списка задач
     */
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

    /**
     * Тест на корректное сохранение состояния после очистки списка подзадач
     */
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

    /**
     * Тест на корректное сохранение состояния после очистки списка эпиков
     */
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

    /**
     * Тест на совместное сохранение и восстановление всех типов задач
     */
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

    /**
     * Тест на сохранение обновленного состояния задачи в хранилище
     */
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

    /**
     * Тест корректного преобразования задачи с временными параметрами в CSV-формат
     */
    @Test
    void toCsvWhenTaskWithTimeThenCorrectFormat() {
        // Given
        Task task = new Task(1, "Task", "Description", TaskStatus.NEW);
        task.setStartTime(LocalDateTime.of(2023, 1, 1, 10, 0));
        task.setDuration(Duration.ofMinutes(30));

        // When
        String csv = task.toCsv();

        // Then
        String expected = "1,TASK,Task,NEW,Description,,2023-01-01T10:00,30\n";
        assertEquals(expected, csv);
    }

    /**
     * Тест корректного создания объекта задачи из CSV-строки
     */
    @Test
    void fromCsvWhenValidTaskThenCorrectObject() {
        // Given
        String csv = "1,TASK,Task Name,NEW,Task Description,,2023-01-01T10:00,30";

        // When
        Task task = Task.fromCsv(csv);

        // Then
        assertNotNull(task);
        assertEquals(1, task.getId());
        assertEquals("Task Name", task.getName());
        assertEquals(TaskStatus.NEW, task.getStatus());
        assertEquals(LocalDateTime.of(2023, 1, 1, 10, 0), task.getStartTime());
        assertEquals(Duration.ofMinutes(30), task.getDuration());
    }

    /**
     * Тест корректного создания подзадачи из CSV-строки с указанием идентификатора эпика
     */
    @Test
    void fromCsvWhenSubtaskThenCorrectEpicId() {
        // Given
        String csv = "2,SUBTASK,Subtask Name,DONE,Subtask Description,1,2023-01-01T11:00,45";

        // When
        Task task = Task.fromCsv(csv);

        // Then
        assertInstanceOf(Subtask.class, task);
        Subtask subtask = (Subtask) task;
        assertEquals(2, subtask.getId());
        assertEquals(1, subtask.getEpicId());
        assertEquals(LocalDateTime.of(2023, 1, 1, 11, 0), subtask.getStartTime());
        assertEquals(Duration.ofMinutes(45), subtask.getDuration());
    }

    /**
     * Тест обработки CSV-строки с отсутствующими временными параметрами
     */
    @Test
    void fromCsvWhenMissingTimeFieldsThenHandleGracefully() {
        // Given
        String csv = "3,TASK,Task Without Time,NEW,Description,,,";

        // When
        Task task = Task.fromCsv(csv);

        // Then
        assertNotNull(task);
        assertEquals(3, task.getId());
        assertEquals("Task Without Time", task.getName());
        assertNull(task.getStartTime());
        assertNull(task.getDuration());
    }

    /**
     * Тест обработки некорректного CSV-формата при создании задачи
     */
    @Test
    void fromCsvWhenInvalidFormatThenThrowException() {
        // Given
        String invalidCsv = "invalid,format";

        // When-Then
        assertThrows(IllegalArgumentException.class, () -> Task.fromCsv(invalidCsv));
    }
}
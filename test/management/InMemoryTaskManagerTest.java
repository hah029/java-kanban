package management;

import org.junit.jupiter.api.Test;
import task.*;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    final TaskManager tm = Managers.getDefault();

    /**
     * Тест на идентичность объекта Task при добавлении
     * в список задач через TaskManager и его чтении
     */
    @Test
    void taskAddedAndGetBackIsTheSame() {
        // Given

        // When
        Task task = new Task("task_name", "task_description", TaskStatus.NEW);
        tm.addTask(task);

        // Then
        assertEquals(task, tm.getTaskById(task.getId()), "Task 1 and Task 2 is not the same!");
    }

    /**
     * Тест на идентичность объекта Subtask при добавлении
     * в список задач через TaskManager и его чтении
     */
    @Test
    void subtaskAddedAndGetBackIsTheSame() {
        // Given

        // When
        Epic epic = new Epic("epic_name", "epic_description");
        tm.addEpic(epic);
        Subtask subtask = new Subtask("subtask_name", "subtask_description", TaskStatus.NEW, epic.getId());
        tm.addSubtask(subtask);

        // Then
        assertEquals(subtask, tm.getSubtaskById(subtask.getId()), "Subtask 1 and Subtask 2 is not the same!");
    }

    /**
     * Тест на идентичность объекта Epic при добавлении
     * в список задач через TaskManager и его чтении
     */
    @Test
    void epicAddedAndGetBackIsTheSame() {
        // Given

        // When
        Epic epic = new Epic("epic_name", "epic_description");
        tm.addEpic(epic);

        // Then
        assertEquals(epic, tm.getEpicById(epic.getId()), "Epic 1 and Epic 2 is not the same!");
    }

    /**
     * Тест на невозможность указания у Subtask'а в качестве Epic'а самого себя
     */
    @Test
    void subtaskCantBeEpicForItself() {
        // Given

        // When
        Subtask subtask = new Subtask("subtask_name", "subtask_description", TaskStatus.NEW);
        subtask.setEpicId(subtask.getId());

        // Then
        assertEquals(-1, subtask.getEpicId());
    }

    /**
     * Тест на удаление задачи из менеджера
     */
    @Test
    void taskCorrectRemove() {
        // Given

        // When
        Task task = new Task("name", "description");
        tm.addTask(task);
        tm.removeTaskById(task.getId());

        // Then
        assertEquals(0, tm.getTasks().size());
    }

    /**
     * Тест на удаление задачи из менеджера
     */
    @Test
    void subtaskAndEpicCorrectRemove() {
        // Given

        // When
        Subtask subtask = new Subtask("name", "description");
        Epic epic = new Epic("name", "description");
        subtask.setEpicId(epic.getId());
        tm.addEpic(epic);
        tm.addSubtask(subtask);
        tm.removeSubtaskById(subtask.getId());

        // Then
        assertEquals(0, tm.getSubtasks().size());
        assertEquals(0, tm.getTasks().size());
    }

    /**
     * Тест на удаление задачи из истории в случае удалении ее из менеджера
     */
    @Test
    void taskCorrectRemoveFromHistoryWhenRemoveFromTaskManager() {
        // Given

        // When
        Task task1 = new Task("name1", "description1");
        Task task2 = new Task("name2", "description2");

        tm.addTask(task1);
        tm.addTask(task2);

        tm.getTaskById(task1.getId());
        tm.getTaskById(task2.getId());
        int originalSize = tm.getHistoryList().size();

        tm.removeTaskById(task1.getId());
        int targetSize = tm.getHistoryList().size();

        // Then
        assertEquals(originalSize - 1, targetSize);
    }
}
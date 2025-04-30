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
        Task task = new Task("task_name", "task_description", TaskStatus.NEW);
        tm.addTask(task);
        assertEquals(task, tm.getTaskById(task.getId()), "Task 1 and Task 2 is not the same!");
    }

    /**
     * Тест на идентичность объекта Subtask при добавлении
     * в список задач через TaskManager и его чтении
     */
    @Test
    void subtaskAddedAndGetBackIsTheSame() {
        Epic epic = new Epic("epic_name", "epic_description");
        tm.addEpic(epic);

        Subtask subtask = new Subtask("subtask_name", "subtask_description", TaskStatus.NEW, epic.getId());
        tm.addSubtask(subtask);
        assertEquals(subtask, tm.getSubtaskById(subtask.getId()), "Subtask 1 and Subtask 2 is not the same!");
    }

    /**
     * Тест на идентичность объекта Epic при добавлении
     * в список задач через TaskManager и его чтении
     */
    @Test
    void epicAddedAndGetBackIsTheSame() {
        Epic epic = new Epic("epic_name", "epic_description");
        tm.addEpic(epic);
        assertEquals(epic, tm.getEpicById(epic.getId()), "Epic 1 and Epic 2 is not the same!");
    }

    /**
     * Тест на невозможность указания у Subtask'а в качестве Epic'а самого себя
     */
    @Test
    void subtaskCantBeEpicForItself() {
        Subtask subtask = new Subtask("subtask_name", "subtask_description", TaskStatus.NEW);
        subtask.setEpicId(subtask.getId());
        assertEquals(-1, subtask.getEpicId());
    }
}
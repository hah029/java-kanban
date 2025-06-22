package management;

import org.junit.jupiter.api.Test;
import task.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    /**
     * Тест корректный расчет временных полей Epic при добавлении в него Subtask
     */
    @Test
    void calculateEpicTimeWhenSubtasksWithTimeThenCorrectEpicTime() {
        // Given
        Epic epic = new Epic(1, "Epic", "Description", TaskStatus.NEW);

        Subtask subtask1 = new Subtask(2, "Subtask 1", "Description", TaskStatus.DONE, 1);
        subtask1.setStartTime(LocalDateTime.of(2023, 1, 1, 10, 0));
        subtask1.setDuration(Duration.ofMinutes(30));

        Subtask subtask2 = new Subtask(3, "Subtask 2", "Description", TaskStatus.IN_PROGRESS, 1);
        subtask2.setStartTime(LocalDateTime.of(2023, 1, 1, 9, 0));
        subtask2.setDuration(Duration.ofMinutes(60));

        // When
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        // Then
        assertEquals(LocalDateTime.of(2023, 1, 1, 9, 0), epic.getStartTime());
        assertEquals(LocalDateTime.of(2023, 1, 1, 10, 30), epic.getEndTime());
        assertEquals(Duration.ofMinutes(90), epic.getDuration());
    }

    /**
     * Тест корректный расчет временных полей Epic в случае отсутствия в нем Subtask
     */
    @Test
    void calculateEpicTimeWhenNoSubtasksThenNullTime() {
        // Given
        Epic epic = new Epic(1, "Epic", "Description", TaskStatus.NEW);

        // When-Then
        assertNull(epic.getStartTime());
        assertNull(epic.getEndTime());
        assertEquals(Duration.ZERO, epic.getDuration());
    }

    /**
     * Тест на получение задач в порядке приоритета (по времени начала)
     * при наличии задач с временными интервалами
     */
    @Test
    void getPrioritizedTasksWhenTasksWithTimeThenOrderedByStartTime() {
        // Given
        TaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("Task 1", "Description");
        task1.setStartTime(LocalDateTime.of(2023, 1, 1, 12, 0));
        task1.setDuration(Duration.ofMinutes(30)); // 12:00-12:30

        Task task2 = new Task("Task 2", "Description");
        task2.setStartTime(LocalDateTime.of(2023, 1, 1, 10, 0));
        task2.setDuration(Duration.ofMinutes(60)); // 10:00-11:00

        Task task3 = new Task("Task 3", "Description");
        task3.setStartTime(LocalDateTime.of(2023, 1, 1, 11, 30)); // 11:30-12:30
        task3.setDuration(Duration.ofMinutes(60));

        // When
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        List<Task> prioritized = new ArrayList<>(manager.getPrioritizedTasks());

        // Then
        assertEquals(3, prioritized.size());
        assertEquals("Task 2", prioritized.get(0).getName()); // 10:00
        assertEquals("Task 3", prioritized.get(1).getName()); // 11:30
        assertEquals("Task 1", prioritized.get(2).getName()); // 12:00
    }

    /**
     * Тест на то, что задачи без времени не включаются в список приоритетных задач
     */
    @Test
    void getPrioritizedTasksWhenTasksWithoutTimeThenNotIncluded() {
        // Given
        TaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("Task 1", "Description");
        task1.setStartTime(LocalDateTime.of(2023, 1, 1, 12, 0));
        task1.setDuration(Duration.ofMinutes(30)); // Добавляем duration

        Task task2 = new Task("Task 2", "Description"); // Без времени

        // When
        manager.addTask(task1);
        manager.addTask(task2);

        // Then
        Set<Task> prioritizedTasks = manager.getPrioritizedTasks();
        assertEquals(1, prioritizedTasks.size());

        // Получаем первый элемент через итератор
        Task firstTask = prioritizedTasks.iterator().next();
        assertEquals("Task 1", firstTask.getName());
    }

    /**
     * Тест на успешное добавление задач, временные интервалы которых не пересекаются
     */
    @Test
    void addTaskWhenTasksDontOverlapThenSuccess() {
        // Given
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("Task 1", "Description");
        task1.setStartTime(LocalDateTime.of(2023, 1, 1, 10, 0));
        task1.setDuration(Duration.ofMinutes(30)); // 10:00-10:30

        Task task2 = new Task("Task 2", "Description");
        task2.setStartTime(LocalDateTime.of(2023, 1, 1, 11, 0)); // 11:00-12:00
        task2.setDuration(Duration.ofMinutes(60));

        // When
        manager.addTask(task1);
        manager.addTask(task2); // Не должно быть исключения

        // Then
        assertEquals(2, manager.getTasks().size());
    }

    /**
     * Тест на установление статуса Epic в NEW, когда все его Subtask имеют статус NEW
     */
    @Test
    void updateStatusWhenAllSubtasksNewThenEpicNew() {
        // Given
        Epic epic = new Epic(1, "Epic", "Description", TaskStatus.NEW);
        Subtask subtask1 = new Subtask(2, "Sub 1", "Desc", TaskStatus.NEW, 1);
        Subtask subtask2 = new Subtask(3, "Sub 2", "Desc", TaskStatus.NEW, 1);

        // When
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        // Then
        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    /**
     * Тест на установление статуса Epic в IN_PROGRESS,
     * когда его Subtask имеют разные статусы (NEW и DONE)
     */
    @Test
    void updateStatusWhenSubtasksMixedThenEpicInProgress() {
        // Given
        Epic epic = new Epic(1, "Epic", "Description", TaskStatus.NEW);
        Subtask subtask1 = new Subtask(2, "Sub 1", "Desc", TaskStatus.NEW, 1);
        Subtask subtask2 = new Subtask(3, "Sub 2", "Desc", TaskStatus.DONE, 1);

        // When
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        // Then
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

    /**
     * Тест на установление статуса Epic в DONE, когда все его Subtask имеют статус DONE
     */
    @Test
    void updateStatusWhenAllSubtasksDoneThenEpicDone() {
        // Given
        Epic epic = new Epic(1, "Epic", "Description", TaskStatus.NEW);
        Subtask subtask1 = new Subtask(2, "Sub 1", "Desc", TaskStatus.DONE, 1);
        Subtask subtask2 = new Subtask(3, "Sub 2", "Desc", TaskStatus.DONE, 1);

        // When
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        // Then
        assertEquals(TaskStatus.DONE, epic.getStatus());
    }

    /**
     * Тест на установление статуса Epic в IN_PROGRESS,
     * когда все его Subtask имеют статус IN_PROGRESS
     */
    @Test
    void updateStatusWhenSubtasksInProgressThenEpicInProgress() {
        // Given
        Epic epic = new Epic(1, "Epic", "Description", TaskStatus.NEW);
        Subtask subtask1 = new Subtask(2, "Sub 1", "Desc", TaskStatus.IN_PROGRESS, 1);
        Subtask subtask2 = new Subtask(3, "Sub 2", "Desc", TaskStatus.IN_PROGRESS, 1);

        // When
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        // Then
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }
}
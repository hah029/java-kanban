package management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;
import java.util.List;

import task.Task;

class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager manager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        manager = new InMemoryHistoryManager();
        task1 = new Task("Task 1", "Description");
        task2 = new Task("Task 2", "Description");
        task3 = new Task("Task 3", "Description");
    }

    /**
     * Тест на добавление одной задачи в историю просмотра
     */
    @Test
    void testAddSingleTask() {
        // Given
        int initialSize = manager.getHistory().size();

        // When
        manager.add(task1);

        // Then
        Assertions.assertEquals(initialSize + 1, manager.getHistory().size());
        Assertions.assertTrue(manager.getHistory().contains(task1));
    }

    /**
     * Тест на добавление трех задач в историю просмотра
     */
    @Test
    void testAddTasks() {
        // Given
        int initialSize = manager.getHistory().size();

        // When
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);

        // Then
        Assertions.assertEquals(initialSize + 3, manager.getHistory().size());
        Assertions.assertTrue(manager.getHistory().contains(task1));
        Assertions.assertTrue(manager.getHistory().contains(task2));
        Assertions.assertTrue(manager.getHistory().contains(task3));

    }

    /**
     * Тест на проверку пустой истории в начале работы (без добавления задач)
     */
    @Test
    void testGetHistoryIsEmpty() {
        // Given
        // When
        ArrayList<Task> history = manager.getHistory();

        // Then
        Assertions.assertTrue(history.isEmpty());
    }

    /**
     * Тест на проверку пустой истории в начале работы (без добавления задач)
     */
    @Test
    void testGetOnlyUniquesTasksFromHistoryWithOneTask() {
        // Given

        // When
        manager.add(task1);
        manager.add(task1);
        ArrayList<Task> history = manager.getHistory();

        // Then
        Assertions.assertEquals(1, history.size());
    }

    /**
     * Тест на проверку пустой истории в начале работы (без добавления задач)
     */
    @Test
    void testGetInversedUniqueListOfTaskInHistoryWhenTryRepeatAddSameTask() {
        // Given
        List<Task> inversedTasks = List.of(task2, task1);

        // When
        manager.add(task1);
        manager.add(task2);
        manager.add(task1);
        List<Task> history = manager.getHistory();

        // Then
        Assertions.assertEquals(inversedTasks, history);
    }

}

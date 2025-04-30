package management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;

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

    @Test
    void testCapacityAndReplaceOldTask() {
        // Given
        manager.add(task1);
        for (int i = 0; i < InMemoryHistoryManager.HISTORY_CAPACITY - 1; i++) {
            manager.add(new Task("Task " + i, "description"));
        }

        // When
        manager.add(task2);

        // Then
        Assertions.assertEquals(InMemoryHistoryManager.HISTORY_CAPACITY, manager.getHistory().size());
        Assertions.assertFalse(manager.getHistory().isEmpty());
        Assertions.assertFalse(manager.getHistory().contains(task1));
        Assertions.assertTrue(manager.getHistory().contains(task2));
    }

    @Test
    void testGetHistoryIsEmpty() {
        // Given
        // When
        ArrayList<Task> history = manager.getHistory();

        // Then
        Assertions.assertTrue(history.isEmpty());
    }

}

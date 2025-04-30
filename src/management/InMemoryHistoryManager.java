package management;

import task.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    public static final int HISTORY_CAPACITY = 10;
    private final ArrayList<Task> history = new ArrayList<>();

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }

    @Override
    public void add(Task task) {
        if (history.size() == HISTORY_CAPACITY) {
            history.removeFirst();
        }
        history.add(task);
    }
}

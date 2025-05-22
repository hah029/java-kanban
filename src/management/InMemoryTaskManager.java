package management;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;


public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> taskCollection = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskCollection = new HashMap<>();
    private final HashMap<Integer, Epic> epicCollection = new HashMap<>();
    private final HistoryManager history = Managers.getDefaultHistory();

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(taskCollection.values());
    }
    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtaskCollection.values());
    }
    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epicCollection.values());
    }

    @Override
    public void clearTasks() {
        for (Task t : taskCollection.values()) {
            history.remove(t.getId());
        }
        taskCollection.clear();
    }
    @Override
    public void clearSubtasks() {
        for (Task t : subtaskCollection.values()) {
            history.remove(t.getId());
        }

        subtaskCollection.clear();
        for (Epic e : epicCollection.values()) {
            e.clearSubtaskList();
        }
    }
    @Override
    public void clearEpics() {
        for (Task t : epicCollection.values()) {
            history.remove(t.getId());
        }

        epicCollection.clear();
        clearSubtasks();
    }

    @Override
    public Task getTaskById(int id) {
        if (!taskCollection.containsKey(id)) {
            System.out.println("Задача c id=" + id + " не найдена.");
            return null;
        }
        Task task = taskCollection.get(id);
        history.add(task);
        return task;
    }
    @Override
    public Subtask getSubtaskById(int id) {
        if (!subtaskCollection.containsKey(id)) {
            System.out.println("Подзадача c id=" + id + " не найдена.");
            return null;
        }
        Subtask task = subtaskCollection.get(id);
        history.add(task);
        return task;
    }
    @Override
    public Epic getEpicById(int id) {
        if (!epicCollection.containsKey(id)) {
            System.out.println("Эпик c id=" + id + " не найден.");
            return null;
        }
        Epic task = epicCollection.get(id);
        history.add(task);
        return task;
    }

    @Override
    public int addTask(Task task) {
        taskCollection.put(task.getId(), task);
        return task.getId();
    }
    @Override
    public int addSubtask(Subtask subtask) {
        subtaskCollection.put(subtask.getId(), subtask);
        Epic epic = getEpicById(subtask.getEpicId());
        epic.addSubtask(subtask);
        return subtask.getId();
    }
    @Override
    public int addEpic(Epic epic) {
        epicCollection.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public void updateTask(Task task) {
        if (taskCollection.isEmpty() || !taskCollection.containsKey(task.getId())) {
            System.out.println("Задача c id=" + task.getId() + " не найдена.");
            return;
        }
        taskCollection.put(task.getId(), task);
    }
    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtaskCollection.isEmpty() || !subtaskCollection.containsKey(subtask.getId())) {
            System.out.println("Подзадача c id=" + subtask.getId() + " не найдена.");
            return;
        }
        subtaskCollection.put(subtask.getId(), subtask);

        Epic epic = getEpicById(subtask.getEpicId());
        epic.updateStatus();

    }
    @Override
    public void updateEpic(Epic epic) {
        if (epicCollection.isEmpty() || !epicCollection.containsKey(epic.getId())) {
            System.out.println("Подзадача c id=" + epic.getId() + " не найдена.");
            return;
        }
        epicCollection.put(epic.getId(), epic);
    }

    @Override
    public void removeTaskById(int id) {
        if (!taskCollection.containsKey(id)) {
            System.out.println("Задача c id=" + id + " не найдена.");
            return;
        }
        taskCollection.remove(id);
        history.remove(id);
    }
    @Override
    public void removeSubtaskById(int id) {
        if (!subtaskCollection.containsKey(id)) {
            System.out.println("Подзадача c id=" + id + " не найдена.");
            return;
        }
        Subtask task = getSubtaskById(id);
        Epic epic = getEpicById(task.getEpicId());
        epic.removeSubtaskById(id);
        subtaskCollection.remove(id);
        history.remove(id);
    }
    @Override
    public void removeEpicById(int id) {
        if (!epicCollection.containsKey(id)) {
            System.out.println("Эпик c id=" + id + " не найден.");
            return;
        }
        epicCollection.remove(id);
        history.remove(id);

        ArrayList<Integer> subtaskIdsToRemove = new ArrayList<>();
        for (Subtask st : subtaskCollection.values()) {
            if (st.getEpicId() == id) subtaskIdsToRemove.add(st.getId());
        }
        for (int stId : subtaskIdsToRemove) {
            subtaskCollection.remove(stId);
            history.remove(stId);
        }
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int id) {
        if (!epicCollection.containsKey(id)) {
            System.out.println("Эпик c id=" + id + " не найден.");
            return null;
        }
        return epicCollection.get(id).getSubtaskList();
    }

    public ArrayList<Task> getHistoryList() {
        return history.getHistory();
    }

}

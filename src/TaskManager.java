import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> taskCollection;
    private final HashMap<Integer, Subtask> subtaskCollection;
    private final HashMap<Integer, Epic> epicCollection;

    public TaskManager() {
        taskCollection = new HashMap<>();
        subtaskCollection = new HashMap<>();
        epicCollection = new HashMap<>();
    }

    public HashMap<Integer, Task> getTasks() {
        return taskCollection;
    }
    public HashMap<Integer, Subtask> getSubtasks() {
        return subtaskCollection;
    }
    public HashMap<Integer, Epic> getEpics() {
        return epicCollection;
    }

    public void clearTasks() {
        taskCollection.clear();
    }
    public void clearSubtasks() {
        subtaskCollection.clear();
    }
    public void clearEpics() {
        epicCollection.clear();
    }

    public Task getTaskById(int id) {
        if (!taskCollection.containsKey(id)) {
            System.out.println("Задача c id=" + id + " не найдена.");
            return null;
        }
        return taskCollection.get(id);
    }
    public Subtask getSubtaskById(int id) {
        if (!subtaskCollection.containsKey(id)) {
            System.out.println("Подзадача c id=" + id + " не найдена.");
            return null;
        }
        return subtaskCollection.get(id);
    }
    public Epic getEpicById(int id) {
        if (!epicCollection.containsKey(id)) {
            System.out.println("Эпик c id=" + id + " не найден.");
            return null;
        }
        return epicCollection.get(id);
    }

    public int addTask(Task task) {
        taskCollection.put(task.getId(), task);
        return task.getId();
    }
    public int addSubtask(Subtask subtask) {
        subtaskCollection.put(subtask.getId(), subtask);
        Epic epic = getEpicById(subtask.getEpicId());
        epic.addSubtask(subtask);
        return subtask.getId();
    }
    public int addEpic(Epic epic) {
        epicCollection.put(epic.getId(), epic);
        return epic.getId();
    }

    public void updateTask(Task task) {
        if (taskCollection.isEmpty() || !taskCollection.containsKey(task.getId())) {
            System.out.println("Задача c id=" + task.getId() + " не найдена.");
            return;
        }
        taskCollection.put(task.getId(), task);
    }
    public void updateSubtask(Subtask subtask) {
        if (subtaskCollection.isEmpty() || !subtaskCollection.containsKey(subtask.getId())) {
            System.out.println("Подзадача c id=" + subtask.getId() + " не найдена.");
            return;
        }
        subtaskCollection.put(subtask.getId(), subtask);

        Epic epic = getEpicById(subtask.getEpicId());
        epic.updateStatus();

    }
    public void updateEpic(Epic epic) {
        if (epicCollection.isEmpty() || !epicCollection.containsKey(epic.getId())) {
            System.out.println("Подзадача c id=" + epic.getId() + " не найдена.");
            return;
        }
        epicCollection.put(epic.getId(), epic);
    }

    public void removeTaskById(int id) {
        if (!taskCollection.containsKey(id)) {
            System.out.println("Задача c id=" + id + " не найдена.");
            return;
        }
        taskCollection.remove(id);
    }
    public void removeSubtaskById(int id) {
        if (!subtaskCollection.containsKey(id)) {
            System.out.println("Подзадача c id=" + id + " не найдена.");
            return;
        }
        Subtask task = getSubtaskById(id);
        Epic epic = getEpicById(task.getEpicId());
        epic.removeSubtaskById(id);
        subtaskCollection.remove(id);
    }
    public void removeEpicById(int id) {
        if (!epicCollection.containsKey(id)) {
            System.out.println("Эпик c id=" + id + " не найден.");
            return;
        }
        epicCollection.remove(id);

        ArrayList<Integer> subtaskIdsToRemove = new ArrayList<>();
        for (Subtask st : subtaskCollection.values()) {
            if (st.getEpicId() == id) subtaskIdsToRemove.add(st.getId());
        }
        for (int stId : subtaskIdsToRemove) {
            subtaskCollection.remove(stId);
        }
    }

    public HashMap<Integer, Subtask> getEpicSubtasks(int id) {
        if (!epicCollection.containsKey(id)) {
            System.out.println("Эпик c id=" + id + " не найден.");
            return null;
        }
        return epicCollection.get(id).getSubtaskList();
    }
}

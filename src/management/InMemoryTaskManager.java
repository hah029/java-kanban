package management;

import task.Epic;
import task.Subtask;
import task.Task;

import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> taskCollection = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskCollection = new HashMap<>();
    private final HashMap<Integer, Epic> epicCollection = new HashMap<>();
    private final HistoryManager history = Managers.getDefaultHistory();

    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));

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
        validateTaskOverlap(task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
        taskCollection.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int addSubtask(Subtask subtask) {
        validateTaskOverlap(subtask);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
        subtaskCollection.put(subtask.getId(), subtask);
        int epicId = subtask.getEpicId();
        if (epicId != -1) {
            Epic epic = getEpicById(epicId);
            if (epic != null) {
                epic.addSubtask(subtask);
            }
        }
        return subtask.getId();
    }

    @Override
    public int addEpic(Epic epic) {
        epicCollection.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public void updateTask(Task task) {
        Task oldTask = taskCollection.get(task.getId());
        if (oldTask != null) {
            prioritizedTasks.remove(oldTask);
        }
        validateTaskOverlap(task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
        taskCollection.put(task.getId(), task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask oldSubtask = subtaskCollection.get(subtask.getId());
        if (oldSubtask != null) {
            prioritizedTasks.remove(oldSubtask);
        }
        validateTaskOverlap(subtask);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
        subtaskCollection.put(subtask.getId(), subtask);
        Epic epic = getEpicById(subtask.getEpicId());
        if (epic != null) {
            epic.updateStatus();
        }
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
        Task task = taskCollection.remove(id);
        if (task != null) {
            prioritizedTasks.remove(task);
            history.remove(id);
        }
    }

    @Override
    public void removeSubtaskById(int id) {
        if (!subtaskCollection.containsKey(id)) {
            System.out.println("Подзадача c id=" + id + " не найдена.");
            return;
        }
        Subtask subtask = subtaskCollection.remove(id);
        if (subtask != null) {
            prioritizedTasks.remove(subtask);
            history.remove(id);
            Epic epic = getEpicById(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskById(id);
            }
        }
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
            Subtask subtask = subtaskCollection.remove(stId);
            prioritizedTasks.remove(subtask);
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

    private boolean isOverlapping(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null) {
            return false;
        }

        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();

        return !(end1.isBefore(start2) || end1.equals(start2)) &&
                !(end2.isBefore(start1) || end2.equals(start1));
    }

    private void validateTaskOverlap(Task task) {
        if (task.getStartTime() == null) {
            return;
        }
        boolean hasOverlap = prioritizedTasks.stream()
                .filter(t -> t.getStartTime() != null)
                .anyMatch(t -> isOverlapping(task, t));
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return new TreeSet<>(prioritizedTasks);
    }
}

package task;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {

    private final HashMap<Integer, Subtask> subtaskList = new HashMap<>();
    private final TaskTypes type;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        this.type = TaskTypes.EPIC;
    }

    public Epic(Integer id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
        this.type = TaskTypes.EPIC;
        setStartCounter(id);
    }

    @Override
    public TaskTypes getType() {
        return type;
    }

    public void addSubtask(Subtask subtask) {
        subtaskList.put(subtask.getId(), subtask);
        subtask.setEpicId(this.getId());
        updateStatus();
    }

    public void removeSubtaskById(int id) {
        if (!subtaskList.containsKey(id)) {
            return;
        }
        subtaskList.remove(id);
        updateStatus();
    }

    public void clearSubtaskList() {
        if (subtaskList.isEmpty()) {
            return;
        }
        subtaskList.clear();
        updateStatus();
    }

    @Override
    public void setStatus(TaskStatus status) {
        System.out.println("Ошибка. Невозможно напрямую изменить статус эпика.");
    }

    public ArrayList<Subtask> getSubtaskList() {
        return new ArrayList<>(subtaskList.values());
    }

    public void updateStatus() {
        if (subtaskList.isEmpty()) {
            this.status = TaskStatus.DONE;
            return;
        }
        int newCount = 0;
        int doneCount = 0;
        for (Subtask st : subtaskList.values()) {
            if (st.getStatus() == TaskStatus.NEW) {
                newCount++;
            } else if (st.getStatus() == TaskStatus.DONE) {
                doneCount++;
            }
        }
        if (newCount == 0 && doneCount == subtaskList.size()) {
            this.status = TaskStatus.DONE;
        } else if (doneCount == 0 && newCount == subtaskList.size()) {
            this.status = TaskStatus.NEW;
        } else {
            this.status = TaskStatus.IN_PROGRESS;
        }
    }

    @Override
    public String toString() {
        return "task.Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtaskList.count=" + subtaskList.size() +
                '}';
    }
}

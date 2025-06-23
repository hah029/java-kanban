package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

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

    @Override
    public LocalDateTime getEndTime() {
        return this.subtaskList.values()
                .stream()
                .map(Task::getEndTime)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder()).orElse(null);
    }

    @Override
    public LocalDateTime getStartTime() {
        return this.subtaskList.values()
                .stream()
                .map(Task::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo).orElse(null);
    }

    @Override
    public Duration getDuration() {
        LocalDateTime startTime = this.getStartTime();
        LocalDateTime endTime = this.getEndTime();

        if (startTime != null && endTime != null) {
            return Duration.between(startTime, endTime);
        }
        return null;
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        System.out.println("Ошибка. Невозможно напрямую изменить старт эпика.");
    }

    @Override
    public void setDuration(Duration duration) {
        System.out.println("Ошибка. Невозможно напрямую изменить длительность эпика.");
    }

    @Override
    public String toCsv() {
        String id = String.valueOf(this.getId());
        String type = this.getType().toString();
        String name = this.getName();
        String status = this.getStatus().toString();
        String description = this.getDescription();
        String epic = "";
        String startTime = "";
        String duration = "";

        return String.join(",", id, type, name, status, description, epic, startTime, duration) + "\n";
    }
}

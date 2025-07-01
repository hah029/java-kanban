package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private final int id;
    private String name;
    private String description;
    protected TaskStatus status;
    private final TaskTypes type;

    private Duration duration;
    private LocalDateTime startTime;

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    private static int taskCounter = 0;

    public String getName() {
        return name;
    }

    public TaskTypes getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Task(Integer id, String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = TaskTypes.TASK;
        this.startTime = startTime;
        this.duration = duration;

        setStartCounter(id);
    }

    public Task(Integer id, String name, String description, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = TaskTypes.TASK;

        setStartCounter(id);
    }

    public Task(String name, String description, TaskStatus status) {
        this.id = getNextId();
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = TaskTypes.TASK;
    }

    public Task(String name, String description) {
        this.id = getNextId();
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.type = TaskTypes.TASK;
    }

    public static int getNextId() {
        return ++taskCounter;
    }

    static void setStartCounter(int newId) {
        if (newId > taskCounter) {
            taskCounter = newId;
        }
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getEndTime() {
        return startTime != null ? startTime.plus(duration) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }

    @Override
    public String toString() {
        return "task.Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", status=" + status +
                '}';
    }

    public String toCsv() {
        String id = String.valueOf(this.getId());
        String type = this.getType().toString();
        String name = this.getName();
        String status = this.getStatus().toString();
        String description = this.getDescription();
        String epic = "";
        String startTime = this.getStartTime() != null ? this.getStartTime().toString() : "";
        String duration = this.getDuration() != null ? String.valueOf(this.getDuration().toMinutes()) : "";

        return String.join(",", id, type, name, status, description, epic, startTime, duration) + "\n";
    }

    public static Task fromCsv(String value) throws IllegalArgumentException {
        String[] attrs = value.split(",", -1);
        if (attrs.length >= 6) { // Минимум 6 атрибутов
            Integer id = Integer.parseInt(attrs[0]);
            TaskTypes type = TaskTypes.valueOf(attrs[1]);
            String name = attrs[2];
            TaskStatus status = TaskStatus.valueOf(attrs[3]);
            String description = attrs[4];

            LocalDateTime startTime = null;
            Duration duration = null;

            if (type == TaskTypes.SUBTASK) {
                int epicId = Integer.parseInt(attrs[5]);

                if (attrs.length >= 8) {
                    startTime = !attrs[6].isEmpty() ? LocalDateTime.parse(attrs[6]) : null;
                    duration = !attrs[7].isEmpty() ? Duration.ofMinutes(Long.parseLong(attrs[7])) : null;
                }

                Subtask subtask = new Subtask(id, name, description, status, epicId);
                subtask.setStartTime(startTime);
                subtask.setDuration(duration);
                return subtask;
            } else {
                if (attrs.length >= 7) {
                    startTime = !attrs[6].isEmpty() ? LocalDateTime.parse(attrs[6]) : null;
                    duration = !attrs[7].isEmpty() ? Duration.ofMinutes(Long.parseLong(attrs[7])) : null;
                }

                if (type == TaskTypes.TASK) {
                    Task task = new Task(id, name, description, status);
                    task.setStartTime(startTime);
                    task.setDuration(duration);
                    return task;
                } else if (type == TaskTypes.EPIC) {
                    return new Epic(id, name, description, status);
                }
            }
        }

        throw new IllegalArgumentException("Invalid CSV format: " + value);
    }


}

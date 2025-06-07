package task;

import java.util.Objects;

public class Task {
    private final int id;
    private String name;
    private String description;
    protected TaskStatus status;
    private final TaskTypes type;

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

        return String.join(",", id, type, name, status, description, epic) + "\n";
    }

    public static Task fromCsv(String value, int attrsRequired) throws IllegalArgumentException {
        String[] attrs = value.split(",", -1);
        if (attrs.length == attrsRequired) {
            Integer id = Integer.parseInt(attrs[0]);
            TaskTypes type = TaskTypes.valueOf(attrs[1]);
            String name = attrs[2];
            TaskStatus status = TaskStatus.valueOf(attrs[3]);
            String description = attrs[4];

            return switch (type) {
                case TaskTypes.TASK -> new Task(id, name, description, status);
                case TaskTypes.SUBTASK -> {
                    int epic = Integer.parseInt(attrs[5]);
                    yield new Subtask(id, name, description, status, epic);
                }
                case TaskTypes.EPIC -> new Epic(id, name, description, status);
            };
        }

        throw new IllegalArgumentException("Count of attributes is incorrect.");
    }

}

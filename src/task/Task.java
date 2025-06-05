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

//    @Override
//    public String toString() {
//        return String.join(
//                ",",
//                String.valueOf(id),
//                type.toString(),
//                name,
//                status.toString(),
//                description,
//                ""
//        );
//    }

//    public void fromString(String str) {
//        String[] attrs = str.split(",");
//    }


}

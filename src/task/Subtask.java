package task;

public class Subtask extends Task {

    private int epicId;
    private final TaskTypes type;

    public Subtask(String name, String description, TaskStatus status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
        this.type = TaskTypes.SUBTASK;
    }

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        this.type = TaskTypes.SUBTASK;
    }

    public Subtask(String name, String description) {
        super(name, description);
        this.epicId = -1;
        this.type = TaskTypes.SUBTASK;
    }

    public Subtask(String name, String description, TaskStatus status) {
        super(name, description, status);
        this.epicId = -1;
        this.type = TaskTypes.SUBTASK;
    }

    public Subtask(Integer id, String name, String description, TaskStatus status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
        this.type = TaskTypes.SUBTASK;
        setStartCounter(id);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        if (epicId == this.getId()) return;
        this.epicId = epicId;
    }

    @Override
    public TaskTypes getType() {
        return type;
    }

    @Override
    public String toString() {
        return "task.Subtask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", epicId=" + epicId +
                '}';
    }

    @Override
    public String toCsv() {
        String id = String.valueOf(this.getId());
        String type = this.getType().toString();
        String name = this.getName();
        String status = this.getStatus().toString();
        String description = this.getDescription();
        String epic = String.valueOf(this.getEpicId());
        String startTime = this.getStartTime() != null ? this.getStartTime().toString() : "";
        String duration = this.getDuration() != null ? String.valueOf(this.getDuration().toMinutes()) : "";

        return String.join(",", id, type, name, status, description, epic, startTime, duration) + "\n";
    }
}

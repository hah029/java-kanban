package task;

public class Subtask extends Task {

    private int epicId;

    public Subtask(String name, String description, TaskStatus status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description) {
        super(name, description);
        this.epicId = -1;
    }

    public Subtask(String name, String description, TaskStatus status) {
        super(name, description, status);
        this.epicId = -1;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        if (epicId == this.getId()) return;
        this.epicId = epicId;
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
}

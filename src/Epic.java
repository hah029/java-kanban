import java.util.HashMap;

public class Epic extends Task {

    private final HashMap<Integer, Subtask> subtaskList;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        this.subtaskList = new HashMap<>();
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

    public HashMap<Integer, Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void updateStatus() {
        if (subtaskList.isEmpty()) {
            setStatus(TaskStatus.NEW);
        }
        int newCount = 0;
        int doneCount = 0;
        for (Subtask st : subtaskList.values()) {
            if (st.getStatus() == TaskStatus.NEW) newCount++;
            if (st.getStatus() == TaskStatus.DONE) doneCount++;
        }
        if (newCount == 0 && doneCount == subtaskList.size()) {
            setStatus(TaskStatus.DONE);
        } else if (doneCount == 0 && newCount == subtaskList.size()) {
            setStatus(TaskStatus.NEW);
        } else {
            setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtaskList.count=" + subtaskList.size() +
                '}';
    }
}

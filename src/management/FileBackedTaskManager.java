package management;

import task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static final String fileHeader = "id,type,name,status,description,epic";
    private static final int fileAttrsCount = 6;
    private final String autosaveFilePath;

    public FileBackedTaskManager(String path) {
        super();
        this.autosaveFilePath = path;
        loadFromFile(path);
    }

    private void save() throws ManagerSaveException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.autosaveFilePath, StandardCharsets.UTF_8))) {

            writer.write(fileHeader + "\n");

            ArrayList<Task> tasks = getTasks();
            for (Task task : tasks) {
                writer.write(task.toCsv());
            }

            ArrayList<Epic> epics = getEpics();
            for (Epic task : epics) {
                writer.write(task.toCsv());
            }

            ArrayList<Subtask> subtasks = getSubtasks();
            for (Subtask task : subtasks) {
                writer.write(task.toCsv());
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public void loadFromFile(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {

            while (reader.ready()) {
                String line = reader.readLine();
                if (line.equals(fileHeader)) continue;

                Task task = Task.fromCsv(line, fileAttrsCount);

                switch (task.getType()) {
                    case TaskTypes.TASK:
                        addTask(task);
                        break;
                    case TaskTypes.SUBTASK:
                        addSubtask((Subtask) task);
                        break;
                    case TaskTypes.EPIC:
                        addEpic((Epic) task);
                        break;
                }

            }
        } catch (IOException | IllegalArgumentException e) {
            throw new ManagerLoadException(e.getMessage());
        }
    }

    @Override
    public int addTask(Task task) {
        int id = super.addTask(task);
        save();
        return id;
    }

    @Override
    public int addSubtask(Subtask subtask) {
        int id = super.addSubtask(subtask);
        save();
        return id;
    }

    @Override
    public int addEpic(Epic epic) {
        int id = super.addEpic(epic);
        save();
        return id;
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }
}

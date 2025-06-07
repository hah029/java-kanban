package management;

import task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static final String fileHeader = "id,type,name,status,description,epic";
    private static final int fileAttrsCount = 6;
    private final String autosaveFilePath;

    public FileBackedTaskManager(String path) {
        super();
        this.autosaveFilePath = path;

        try {
            loadFromFile(path);
        } catch (ManagerLoadException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

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

    public void loadFromFile(String path) throws ManagerLoadException {
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
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return id;
    }

    @Override
    public int addSubtask(Subtask subtask) {
        int id = super.addSubtask(subtask);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return id;
    }

    @Override
    public int addEpic(Epic epic) {
        int id = super.addEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return id;
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}

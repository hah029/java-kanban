package management;

import task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final String autosaveFilePath;
    private final String fileHeader = "id,type,name,status,description,epic\n";

    public FileBackedTaskManager(String path) {
        super();
        this.autosaveFilePath = path;

        try {
            loadFromFile(path);
        } catch (ManagerLoadException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

    }

    private String toString(Task task) {

        String id = String.valueOf(task.getId());
        String type = task.getType().toString();
        String name = task.getName();
        String status = task.getStatus().toString();
        String description = task.getDescription();

        String epic;
        if (task instanceof Subtask st) {
            epic = String.valueOf(st.getEpicId());
        } else {
            epic = "";
        }

        return String.join(",", id, type, name, status, description, epic) + "\n";
    }


    private Task fromString(String value) {
        String[] attrs = value.split(",");
        if (attrs.length != 0) {

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
        return null;
    }

    private void save() throws ManagerSaveException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.autosaveFilePath, StandardCharsets.UTF_8))) {

            writer.write(fileHeader);

            ArrayList<Task> tasks = getTasks();
            for (Task task : tasks) {
                writer.write(toString(task));
            }

            ArrayList<Epic> epics = getEpics();
            for (Epic task : epics) {
                writer.write(toString(task));
            }

            ArrayList<Subtask> subtasks = getSubtasks();
            for (Subtask task : subtasks) {
                writer.write(toString(task));
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public void loadFromFile(String path) throws ManagerLoadException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path, StandardCharsets.UTF_8))) {

            while (reader.ready()) {
                String line = reader.readLine();
                if (line.equals(fileHeader.replace("\n", ""))) continue;

                Task task = fromString(line);

                if (task != null) {
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

            }
        } catch (IOException e) {
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

public class Main {

    public static void main(String[] args) {
        System.out.println("Старт программы");

        management.TaskManager tm = new management.TaskManager();

        // ====== Testing begins
        // ------ create objects
        System.out.println("Создаем объекты для теста...");
        int t1Id = tm.addTask(new task.Task("задача 1", "описание", task.TaskStatus.NEW));
        int t2Id = tm.addTask(new task.Task("задача 2", "описание", task.TaskStatus.NEW));

        int e1Id = tm.addEpic(new task.Epic("эпик 1", "описание"));
        int e1s1Id = tm.addSubtask(new task.Subtask("подзадача 1", "описание", task.TaskStatus.NEW, e1Id));
        int e1s2Id = tm.addSubtask(new task.Subtask("подзадача 2", "описание", task.TaskStatus.NEW, e1Id));

        int e2Id = tm.addEpic(new task.Epic("эпик 2", "описание"));
        int e2s1Id = tm.addSubtask(new task.Subtask("подзадача 3", "описание", task.TaskStatus.NEW, e2Id));

        // ------ print object lists
        System.out.println("Печатаем созданные задачи:");
        System.out.println(tm.getTasks());
        System.out.println("Печатаем созданные подзадачи:");
        System.out.println(tm.getSubtasks());
        System.out.println("Печатаем созданные эпики:");
        System.out.println(tm.getEpics());

        System.out.println("\n");

        // ------ update object status
        System.out.println("Обновляем статус задач на IN_PROGRESS...");
        task.Task t1 = tm.getTaskById(t1Id);
        t1.setStatus(task.TaskStatus.IN_PROGRESS);
        tm.updateTask(t1);

        task.Task t2 = tm.getTaskById(t2Id);
        t2.setStatus(task.TaskStatus.IN_PROGRESS);
        tm.updateTask(t2);

        task.Subtask s1 = tm.getSubtaskById(e1s1Id);
        s1.setStatus(task.TaskStatus.IN_PROGRESS);
        tm.updateSubtask(s1);

        task.Subtask s2 = tm.getSubtaskById(e1s2Id);
        s2.setStatus(task.TaskStatus.IN_PROGRESS);
        tm.updateSubtask(s2);

        task.Subtask s3 = tm.getSubtaskById(e2s1Id);
        s3.setStatus(task.TaskStatus.IN_PROGRESS);
        tm.updateSubtask(s3);

        System.out.println("Печатаем обновленные задачи:");
        System.out.println(tm.getTasks());
        System.out.println("Печатаем обновленные подзадачи:");
        System.out.println(tm.getSubtasks());
        System.out.println("Печатаем обновленные эпики:");
        System.out.println(tm.getEpics());

        System.out.println("\n");

        // ------ remove object status
        tm.removeTaskById(t1Id);
        tm.removeEpicById(e1Id);
        tm.removeSubtaskById(e2s1Id);

        System.out.println("Печатаем задачи после удаления:");
        System.out.println(tm.getTasks());
        System.out.println("Печатаем подзадачи после удаления:");
        System.out.println(tm.getSubtasks());
        System.out.println("Печатаем эпики после удаления:");
        System.out.println(tm.getEpics());

        System.out.println("Конец программы");
    }
}

public class Main {

    public static void main(String[] args) {
        System.out.println("Старт программы");

        TaskManager tm = new TaskManager();

        // ====== Testing begins
        // ------ create objects
        System.out.println("Создаем объекты для теста...");
        int t1Id = tm.addTask(new Task("задача 1", "описание", TaskStatus.NEW));
        int t2Id = tm.addTask(new Task("задача 2", "описание", TaskStatus.NEW));

        int e1Id = tm.addEpic(new Epic("эпик 1", "описание"));
        int e1s1Id = tm.addSubtask(new Subtask("подзадача 1", "описание", TaskStatus.NEW, e1Id));
        int e1s2Id = tm.addSubtask(new Subtask("подзадача 2", "описание", TaskStatus.NEW, e1Id));

        int e2Id = tm.addEpic(new Epic("эпик 2", "описание"));
        int e2s1Id = tm.addSubtask(new Subtask("подзадача 3", "описание", TaskStatus.NEW, e2Id));

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
        Task t1 = tm.getTaskById(t1Id);
        t1.setStatus(TaskStatus.IN_PROGRESS);
        tm.updateTask(t1);

        Task t2 = tm.getTaskById(t2Id);
        t2.setStatus(TaskStatus.IN_PROGRESS);
        tm.updateTask(t2);

        Subtask s1 = tm.getSubtaskById(e1s1Id);
        s1.setStatus(TaskStatus.IN_PROGRESS);
        tm.updateSubtask(s1);

        Subtask s2 = tm.getSubtaskById(e1s2Id);
        s2.setStatus(TaskStatus.IN_PROGRESS);
        tm.updateSubtask(s2);

        Subtask s3 = tm.getSubtaskById(e2s1Id);
        s3.setStatus(TaskStatus.IN_PROGRESS);
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

        System.out.println("Печатаем задачи после удаления:");
        System.out.println(tm.getTasks());
        System.out.println("Печатаем подзадачи после удаления:");
        System.out.println(tm.getSubtasks());
        System.out.println("Печатаем эпики после удаления:");
        System.out.println(tm.getEpics());

        System.out.println("Конец программы");
    }
}

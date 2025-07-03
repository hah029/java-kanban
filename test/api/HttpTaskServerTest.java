package api;

import com.google.gson.*;
import management.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {

    InMemoryTaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);

    public HttpTaskServerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.clearTasks();
        manager.clearSubtasks();
        manager.clearEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        // создаём задачу
        String taskJson = "{\"name\":\"Test 1\",\"description\":\"Testing task 1\",\"duration\":5,\"startTime\":\"2025-07-03T21:30:42.293216\"}";;

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 1", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        // создаём подзадачу
        String subtaskJson = "{\"name\":\"Test 1\",\"description\":\"Testing subtask 1\",\"duration\":5,\"startTime\":\"2025-07-03T21:30:42.293216\"}";

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Subtask> subtasksFromManager = manager.getSubtasks();

        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 1", subtasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        // создаём эпик
        String epicJson = "{\"name\":\"Test 1\",\"description\":\"Testing epic 1\"}";

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Epic> epicsFromManager = manager.getEpics();

        assertNotNull(epicsFromManager, "Задачи не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 1", epicsFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Test", "Test");
        task.setDuration(Duration.ofMinutes(5));
        task.setStartTime(LocalDateTime.now());

        manager.addTask(task);
        String taskJson = String.format("{\"id\": %d, \"name\":\"New Test\",\"description\":\"New Test\"}", task.getId());;

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("New Test", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateSubtask() throws IOException, InterruptedException {
        // создаём задачу
        Subtask subtask = new Subtask("Test", "Test");
        subtask.setDuration(Duration.ofMinutes(5));
        subtask.setStartTime(LocalDateTime.now());

        manager.addSubtask(subtask);
        String subtaskJson = String.format("{\"id\": %d, \"name\":\"New Test\",\"description\":\"New Test\"}", subtask.getId());;

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Subtask> tasksFromManager = manager.getSubtasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("New Test", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateEpic() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic("Test", "Test");

        manager.addEpic(epic);
        String epicJson = String.format("{\"id\": %d, \"name\":\"New Test\",\"description\":\"New Test\"}", epic.getId());;

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Epic> tasksFromManager = manager.getEpics();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("New Test", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTasks() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Test", "Test");
        task.setDuration(Duration.ofMinutes(5));
        task.setStartTime(LocalDateTime.now());

        manager.addTask(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());

        assertTrue(jsonElement.isJsonArray(), "Ответ от сервера не соответствует ожидаемому.");

        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertEquals(1, jsonArray.size(), "Некорректное количество задач");
        String name = jsonArray.get(0).getAsJsonObject().get("name").getAsString();
        assertEquals("Test", name, "Некорректное имя задачи");

    }

    @Test
    public void testGetSubtasks() throws IOException, InterruptedException {
        // создаём задачу
        Subtask subtask = new Subtask("Test", "Test");
        subtask.setDuration(Duration.ofMinutes(5));
        subtask.setStartTime(LocalDateTime.now());
        manager.addSubtask(subtask);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());

        assertTrue(jsonElement.isJsonArray(), "Ответ от сервера не соответствует ожидаемому.");

        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertEquals(1, jsonArray.size(), "Некорректное количество задач");
        String name = jsonArray.get(0).getAsJsonObject().get("name").getAsString();
        assertEquals("Test", name, "Некорректное имя задачи");
    }

    @Test
    public void testGetEpics() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic("Test", "Test");
        manager.addEpic(epic);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());

        assertTrue(jsonElement.isJsonArray(), "Ответ от сервера не соответствует ожидаемому.");

        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertEquals(1, jsonArray.size(), "Некорректное количество задач");
        String name = jsonArray.get(0).getAsJsonObject().get("name").getAsString();
        assertEquals("Test", name, "Некорректное имя задачи");
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Test", "Test");
        task.setDuration(Duration.ofMinutes(5));
        task.setStartTime(LocalDateTime.now());
        manager.addTask(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(String.format("http://localhost:8080/tasks/%d", task.getId()));
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());

        assertTrue(jsonElement.isJsonObject(), "Ответ от сервера не соответствует ожидаемому.");

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String name = jsonObject.get("name").getAsString();
        assertEquals("Test", name, "Некорректное имя задачи");

    }

    @Test
    public void testGetTaskByIdNotFound() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Test", "Test");
        task.setDuration(Duration.ofMinutes(5));
        task.setStartTime(LocalDateTime.now());
        manager.addTask(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(String.format("http://localhost:8080/tasks/%d", task.getId() + 999));
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(404, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonNull(), "Ответ от сервера не соответствует ожидаемому.");
    }

    @Test
    public void testGetSubtaskById() throws IOException, InterruptedException {
        // создаём задачу
        Subtask subtask = new Subtask("Test", "Test");
        subtask.setDuration(Duration.ofMinutes(5));
        subtask.setStartTime(LocalDateTime.now());
        manager.addSubtask(subtask);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(String.format("http://localhost:8080/subtasks/%d", subtask.getId()));
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());

        assertTrue(jsonElement.isJsonObject(), "Ответ от сервера не соответствует ожидаемому.");

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String name = jsonObject.get("name").getAsString();
        assertEquals("Test", name, "Некорректное имя задачи");
    }

    @Test
    public void testGetSubtaskByIdNotFound() throws IOException, InterruptedException {
        // создаём задачу
        Subtask subtask = new Subtask("Test", "Test");
        subtask.setDuration(Duration.ofMinutes(5));
        subtask.setStartTime(LocalDateTime.now());
        manager.addSubtask(subtask);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(String.format("http://localhost:8080/subtasks/%d", subtask.getId() + 999));
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(404, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());

        assertTrue(jsonElement.isJsonNull(), "Ответ от сервера не соответствует ожидаемому.");
    }

    @Test
    public void testGetEpicById() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic("Test", "Test");
        manager.addEpic(epic);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(String.format("http://localhost:8080/epics/%d", epic.getId()));
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());

        assertTrue(jsonElement.isJsonObject(), "Ответ от сервера не соответствует ожидаемому.");

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String name = jsonObject.get("name").getAsString();
        assertEquals("Test", name, "Некорректное имя задачи");
    }

    @Test
    public void testGetEpicByIdNotFound() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic("Test", "Test");
        manager.addEpic(epic);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(String.format("http://localhost:8080/epics/%d", epic.getId() + 999));
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(404, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonNull(), "Ответ от сервера не соответствует ожидаемому.");
    }

    @Test
    public void testGetEpicSubtasksById() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic("Epic Test", "Test");
        Subtask subtask = new Subtask("Subtask Test", "Test", epic.getId());
        manager.addEpic(epic);
        manager.addSubtask(subtask);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(String.format("http://localhost:8080/epics/%d/subtasks", epic.getId()));
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());

        assertTrue(jsonElement.isJsonArray(), "Ответ от сервера не соответствует ожидаемому.");

        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertEquals(1, jsonArray.size(), "Некорректное количество задач");
        String name = jsonArray.get(0).getAsJsonObject().get("name").getAsString();
        assertEquals("Subtask Test", name, "Некорректное имя задачи");
    }

    @Test
    public void testGetEpicSubtasksByIdNotFound() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic("Epic Test", "Test");
        Subtask subtask = new Subtask("Subtask Test", "Test", epic.getId());
        manager.addEpic(epic);
        manager.addSubtask(subtask);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(String.format("http://localhost:8080/epics/%d/subtasks", epic.getId() + 999));
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(404, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonNull(), "Ответ от сервера не соответствует ожидаемому.");
    }

    @Test
    public void testDeleteTaskById() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Test", "Test");
        task.setDuration(Duration.ofMinutes(5));
        task.setStartTime(LocalDateTime.now());
        manager.addTask(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(String.format("http://localhost:8080/tasks/%d", task.getId()));
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        assertNull(manager.getTaskById(task.getId()), "Задача не удалилась");
    }

    @Test
    public void testDeleteSubtaskById() throws IOException, InterruptedException {
        // создаём задачу
        Subtask subtask = new Subtask("Test", "Test");
        subtask.setDuration(Duration.ofMinutes(5));
        subtask.setStartTime(LocalDateTime.now());
        manager.addSubtask(subtask);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(String.format("http://localhost:8080/subtasks/%d", subtask.getId()));
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        assertNull(manager.getSubtaskById(subtask.getId()), "Задача не удалилась");
    }

    @Test
    public void testDeleteEpicById() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic("Test", "Test");
        manager.addEpic(epic);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(String.format("http://localhost:8080/epics/%d", epic.getId()));
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        assertNull(manager.getEpicById(epic.getId()), "Задача не удалилась");
    }

    @Test
    public void testGetHistory() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Test", "Test");
        task.setDuration(Duration.ofMinutes(5));
        task.setStartTime(LocalDateTime.now());

        manager.addTask(task);
        manager.getTaskById(task.getId());
        List<Task> history = manager.getHistoryList();

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());

        assertTrue(jsonElement.isJsonArray(), "Ответ от сервера не соответствует ожидаемому.");

        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertEquals(history.size(), jsonArray.size(), "Некорректное количество задач");
        String name = jsonArray.get(0).getAsJsonObject().get("name").getAsString();
        assertEquals(history.getFirst().getName(), name, "Некорректное имя задачи");
    }

    @Test
    public void testGetPrioritized() throws IOException, InterruptedException {
        // создаём задачу
        Task task1 = new Task("Test 1", "Test 1");
        task1.setDuration(Duration.ofMinutes(5));
        task1.setStartTime(LocalDateTime.now().plusHours(1));

        Task task2 = new Task("Test 2", "Test 2");
        task2.setDuration(Duration.ofMinutes(5));
        task1.setStartTime(LocalDateTime.now().minusHours(1));

        manager.addTask(task1);
        manager.addTask(task2);

        TreeSet<Task> prioritizedTasks = manager.getPrioritizedTasks();

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());

        assertTrue(jsonElement.isJsonArray(), "Ответ от сервера не соответствует ожидаемому.");

        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertEquals(prioritizedTasks.size(), jsonArray.size(), "Некорректное количество задач");
        String name = jsonArray.get(0).getAsJsonObject().get("name").getAsString();
        assertEquals(prioritizedTasks.getFirst().getName(), name, "Некорректное имя задачи");
    }
}
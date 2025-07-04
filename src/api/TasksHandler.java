package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import management.TaskManager;
import management.TaskTimeConflictException;
import task.Task;
import java.io.IOException;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public TasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] parts = path.split("/");

            if ("GET".equals(method) && parts.length == 2) {
                handleGetTasks(exchange);
            } else if ("GET".equals(method) && parts.length == 3) {
                handleGetTaskById(exchange, parts[2]);
            } else if ("POST".equals(method) && parts.length == 2) {
                handlePostTask(exchange);
            } else if ("DELETE".equals(method) && parts.length == 3) {
                handleDeleteTaskById(exchange, parts[2]);
            } else {
                sendBadRequest(exchange);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        String response = gson.toJson(manager.getTasks());
        sendText(exchange, response);
    }

    private void handleGetTaskById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            Task task = manager.getTaskById(id);
            if (task == null) {
                sendNotFound(exchange);
            } else {
                sendText(exchange, gson.toJson(task));
            }
        } catch (NumberFormatException e) {
            sendBadRequest(exchange);
        }
    }

    private void handlePostTask(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        Task task = gson.fromJson(body, Task.class);
        try {
            if (task.getId() == 0) {
                manager.addTask(task);
            } else {
                manager.updateTask(task);
            }
            sendCreated(exchange);
        } catch (TaskTimeConflictException e) {
            sendHasInteractions(exchange);
        }
    }

    private void handleDeleteTaskById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            manager.removeTaskById(id);
            sendOk(exchange);
        } catch (NumberFormatException e) {
            sendBadRequest(exchange);
        }
    }
}
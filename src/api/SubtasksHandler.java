package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import management.TaskManager;
import management.TaskTimeConflictException;
import task.Subtask;

import java.io.IOException;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public SubtasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] parts = path.split("/");

            if ("GET".equals(method) && parts.length == 2) {
                handleGetSubtasks(exchange);
            } else if ("GET".equals(method) && parts.length == 3) {
                handleGetSubtaskById(exchange, parts[2]);
            } else if ("POST".equals(method) && parts.length == 2) {
                handlePostSubtask(exchange);
            } else if ("DELETE".equals(method) && parts.length == 3) {
                handleDeleteSubtaskById(exchange, parts[2]);
            } else {
                sendBadRequest(exchange);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        String response = gson.toJson(manager.getSubtasks());
        sendText(exchange, response);
    }

    private void handleGetSubtaskById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            Subtask subtask = manager.getSubtaskById(id);
            if (subtask == null) {
                sendNotFound(exchange);
            } else {
                sendText(exchange, gson.toJson(subtask));
            }
        } catch (NumberFormatException e) {
            sendBadRequest(exchange);
        }
    }

    private void handlePostSubtask(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        Subtask subtask = gson.fromJson(body, Subtask.class);
        try {
            if (subtask.getId() == 0) {
                manager.addSubtask(subtask);
            } else {
                manager.updateSubtask(subtask);
            }
            sendCreated(exchange);
        } catch (TaskTimeConflictException e) {
            sendHasInteractions(exchange);
        }
    }

    private void handleDeleteSubtaskById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            manager.removeSubtaskById(id);
            sendOk(exchange);
        } catch (NumberFormatException e) {
            sendBadRequest(exchange);
        }
    }
}
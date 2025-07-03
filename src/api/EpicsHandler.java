package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import management.TaskManager;
import management.TaskTimeConflictException;
import task.Epic;

import java.io.IOException;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public EpicsHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] parts = path.split("/");

            if ("GET".equals(method) && parts.length == 2) {
                handleGetEpics(exchange);
            } else if ("GET".equals(method) && parts.length == 3) {
                handleGetEpicById(exchange, parts[2]);
            } else if ("GET".equals(method) && parts.length == 4) {
                handleGetSubtasksOfEpicById(exchange, parts[2]);
            } else if ("POST".equals(method) && parts.length == 2) {
                handlePostEpic(exchange);
            } else if ("DELETE".equals(method) && parts.length == 3) {
                handleDeleteEpicById(exchange, parts[2]);
            } else {
                sendBadRequest(exchange);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        String response = gson.toJson(manager.getEpics());
        sendText(exchange, response);
    }

    private void handleGetEpicById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            Epic epic = manager.getEpicById(id);
            if (epic == null) {
                sendNotFound(exchange);
            } else {
                sendText(exchange, gson.toJson(epic));
            }
        } catch (NumberFormatException e) {
            sendBadRequest(exchange);
        }
    }

    private void handleGetSubtasksOfEpicById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            Epic epic = manager.getEpicById(id);
            if (epic == null) {
                sendNotFound(exchange);
            } else {
                sendText(exchange, gson.toJson(epic.getSubtaskList()));
            }
        } catch (NumberFormatException e) {
            sendBadRequest(exchange);
        }
    }

    private void handlePostEpic(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        Epic epic = gson.fromJson(body, Epic.class);
        try {
            if (epic.getId() == 0) {
                manager.addEpic(epic);
            } else {
                manager.updateEpic(epic);
            }
            sendCreated(exchange);
        } catch (TaskTimeConflictException e) {
            sendHasInteractions(exchange);
        }
    }

    private void handleDeleteEpicById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            manager.removeEpicById(id);
            sendOk(exchange);
        } catch (NumberFormatException e) {
            sendBadRequest(exchange);
        }
    }
}
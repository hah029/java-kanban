import api.HttpTaskServer;
import management.Managers;
import management.TaskManager;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefault();
        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();
    }
}
import application.services.RequestHandler;
import application.usecases.BasicWebServer;
import infrastructure.request_handlers.FileSystemRequestHandler;

import java.net.Socket;
import java.net.ServerSocket;
import java.util.concurrent.Executors;


public class Main {
    public static void main(String[] args) {
        try {
            BasicWebServer basicWebServer = new BasicWebServer(Executors.newCachedThreadPool(), FileSystemRequestHandler.class);
            basicWebServer.run();
        } catch (Exception e){

        }
    }
}

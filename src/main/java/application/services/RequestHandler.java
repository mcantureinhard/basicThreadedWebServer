package application.services;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public abstract class RequestHandler implements Runnable {
    protected final Socket socket;

    public RequestHandler(Socket socket) {
        this.socket = socket;
    }
}

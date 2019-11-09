package application.services;

import java.net.Socket;

public abstract class RequestHandler implements Runnable {
    protected final Socket socket;

    public RequestHandler(Socket socket) {
        this.socket = socket;
    }
}

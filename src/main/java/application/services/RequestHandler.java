package application.services;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public abstract class RequestHandler implements Runnable {
    protected final Socket socket;
    protected final BlockingQueue<Socket> socketBlockingQueue;

    public RequestHandler(Socket socket, BlockingQueue<Socket> socketBlockingQueue) {
        this.socket = socket;
        this.socketBlockingQueue = socketBlockingQueue;
    }
}

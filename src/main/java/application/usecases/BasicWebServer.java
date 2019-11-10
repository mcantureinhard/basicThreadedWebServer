package application.usecases;

import application.ApplicationConfiguration;
import application.services.LoggingService;
import application.services.RequestHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class BasicWebServer<T extends RequestHandler> {
    private final Class<T> requestHandlerClass;
    private int port;
    private int maxQueueSize;
    private ThreadedQueueConsumer threadedQueueConsumer;
    private ExecutorService executorService;
    private BlockingQueue<Socket> socketBlockingQueue;
    private ServerSocket serverSocket;
    private Thread threadedQueueConsumerThread;
    private int timeout = 5000;
    private boolean verbose = false;

    private void init() throws Exception {
        if(ApplicationConfiguration.getInstance().get("timeout") != null){
            timeout = Integer.parseInt(ApplicationConfiguration.getInstance().get("timeout"));
        }
        if(ApplicationConfiguration.getInstance().get("verbose") != null){
            verbose = Boolean.parseBoolean(ApplicationConfiguration.getInstance().get("verbose"));
        }
        serverSocket = new ServerSocket(port);
        socketBlockingQueue = new ArrayBlockingQueue<>(maxQueueSize);
        threadedQueueConsumer = new ThreadedQueueConsumer(executorService, socketBlockingQueue, requestHandlerClass);
        threadedQueueConsumerThread = new Thread(threadedQueueConsumer);
        threadedQueueConsumerThread.start();
    }

    public BasicWebServer(int port, ExecutorService executorService, Class<T> requestHandlerClass, int maxQueueSize) throws Exception {
        this.port = port;
        this.executorService = executorService;
        this.requestHandlerClass = requestHandlerClass;
        this.maxQueueSize = maxQueueSize;
        init();
    }

    public BasicWebServer(ExecutorService executorService, Class<T> requestHandlerClass) throws Exception {
        this.executorService = executorService;
        this.requestHandlerClass = requestHandlerClass;
        this.port = 8080;
        this.maxQueueSize = 2048;
        init();
    }

    public void run() throws Exception {
        if(verbose)
            System.out.println("Started Basic Web Server");
        while (true){
            Socket socket = serverSocket.accept();
            if(verbose)
                System.out.println("Accepted socket");
            socketBlockingQueue.add(socket);
        }
    }

    private class ThreadedQueueConsumer<T extends RequestHandler> implements Runnable {
        private ExecutorService executorService;
        private BlockingQueue<Socket> socketBlockingQueue;
        private final Class<T> requestHandlerClass;

        public ThreadedQueueConsumer(ExecutorService executorService, BlockingQueue<Socket> socketBlockingQueue, Class<T> requestHandlerClass) {
            this.executorService = executorService;
            this.socketBlockingQueue = socketBlockingQueue;
            this.requestHandlerClass = requestHandlerClass;
        }

        @Override
        public void run() {
            while(true){
                try {
                    Socket socket = socketBlockingQueue.take();
                    socket.setSoTimeout(timeout);
                    if(verbose)
                        System.out.println("Got socket from queue");
                    RequestHandler handler = requestHandlerClass.getDeclaredConstructor(new Class[]{Socket.class, BlockingQueue.class, Boolean.class}).newInstance(socket, socketBlockingQueue, verbose);
                    executorService.execute(handler);
                } catch (Exception e){
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    String error = sw.toString();
                    LoggingService.getInstance().getLogger().logExceptionMessage(error);
                }
            }
        }
    }
}

package infrastructure.request_handlers;

import application.services.RequestHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class FileSystemRequestHandler extends RequestHandler {
    public FileSystemRequestHandler(Socket socket) {
        super(socket);
    }

    public void init(){
        System.out.println("Init");
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String str = in.readLine();
            System.out.println(str);
        } catch (Exception e){
            System.out.println(e);
        }
    }
}

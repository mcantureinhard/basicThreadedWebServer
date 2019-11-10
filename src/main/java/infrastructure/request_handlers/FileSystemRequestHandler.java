package infrastructure.request_handlers;

import application.services.ApplicationConfiguration;
import application.services.LoggingService;
import application.services.RequestHandler;
import domain.models.SimpleHttpRequest;
import domain.models.SimpleHttpResponse;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

public class FileSystemRequestHandler extends RequestHandler {
    //TODO Use configuration
    private final String rootDir;
    public FileSystemRequestHandler(Socket socket, BlockingQueue<Socket> socketBlockingQueue) throws Exception{
        super(socket, socketBlockingQueue);
        rootDir = ApplicationConfiguration.getInstance().get("fsroot");
    }

    public FileSystemRequestHandler(Socket socket, BlockingQueue<Socket> socketBlockingQueue, Boolean verbose) throws Exception {
        super(socket, socketBlockingQueue, verbose);
        rootDir = ApplicationConfiguration.getInstance().get("fsroot");
    }

    @Override
    public void run() {
        //TODO implement keep alive, minor change to RequestHandler needed, pass queue so we can return socket to queue rather than close
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;
        BufferedOutputStream bufferedOutputStream = null;
        SimpleHttpResponse response = null;
        SimpleHttpRequest request = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream());
            bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
            request = SimpleHttpRequest.fromBufferedReader(bufferedReader);
            if(verbose)
                System.out.println("Handling: " + request.getMethod() + " " + request.getPath() + "?" + request.getQuery());
            switch (request.getMethod()){
                case GET:
                    response = get(request);
                    break;
                default:
                    response = notImplemented();
            }
        } catch (SocketTimeoutException e){
            try {
                if(verbose)
                    System.out.println("Closing socket due to timeout");
                socket.close();
            } catch (Exception innerException){
            }
        } catch (Exception e) {
            response = errorResponse();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String error = sw.toString();
            LoggingService.getInstance().getLogger().logExceptionMessage(error);
        } finally {
            try {
                sendResponse(response, printWriter, bufferedOutputStream);
                if(request == null || !request.getKeepAlive()) {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (printWriter != null) {
                        printWriter.close();
                    }
                    if (bufferedOutputStream != null) {
                        bufferedOutputStream.close();
                    }
                    socket.close();
                } else {
                    socketBlockingQueue.add(socket);
                }
            } catch (Exception e){
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                String error = sw.toString();
                LoggingService.getInstance().getLogger().logExceptionMessage(error);
            }
        }
    }

    private SimpleHttpResponse get(SimpleHttpRequest request) throws Exception {
        String fileName = rootDir + request.getPath();
        File file = new File(fileName);
        if(!file.exists()){
            return notFoundResponse();
        }
        int fileSize = (int) file.length();
        SimpleHttpResponse.ContentType contentType = getContentType(fileName);

        SimpleHttpResponse response = new SimpleHttpResponse(
                SimpleHttpResponse.ResponseCode.OK,
                contentType,
                fileName,
                fileSize
        );
        return response;
    }

    private SimpleHttpResponse notImplemented() throws Exception {
        SimpleHttpResponse response = new SimpleHttpResponse(
                SimpleHttpResponse.ResponseCode.NOTIMPLEMENTED,
                SimpleHttpResponse.ContentType.TEXTHTML,
                null,
                0
        );
        return response;
    }

    private void sendResponse(SimpleHttpResponse response, PrintWriter printWriter, BufferedOutputStream bufferedOutputStream) throws Exception{
        if(response != null) {
            printWriter.println("HTTP/1.1 " + response.getResponseCode().code + " " + response.getResponseCode().message);
            printWriter.println("Server: " + response.getServer());
            printWriter.println("Date: " + new Date());
            printWriter.println("Content-type: " + response.getContentType().toString());
            printWriter.println("Content-length: " + response.getFileSize());
            printWriter.println();
            printWriter.flush();
            if (response.getFile() != null) {
                writeFileToOutputStream(response.getFile(), bufferedOutputStream);
            }
        }
    }

    private void writeFileToOutputStream(String file, BufferedOutputStream bufferedOutputStream) throws Exception{
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        byte[] buffer = new byte[32 * 1024];
        int len = 0;
        while ((len = bufferedInputStream.read(buffer)) != -1){
            bufferedOutputStream.write(buffer, 0, len);
        }
        bufferedOutputStream.flush();
    }

    private SimpleHttpResponse.ContentType getContentType(String fileRequested) {
        if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
            return SimpleHttpResponse.ContentType.TEXTHTML;
        else
            return SimpleHttpResponse.ContentType.TEXTPLAIN;
    }

    private SimpleHttpResponse errorResponse(){
        SimpleHttpResponse response = new SimpleHttpResponse(
                SimpleHttpResponse.ResponseCode.ERROR,
                SimpleHttpResponse.ContentType.TEXTPLAIN,
                null,
                0
        );
        return response;
    }

    private SimpleHttpResponse notFoundResponse(){
        SimpleHttpResponse response = new SimpleHttpResponse(
                SimpleHttpResponse.ResponseCode.NOTFOUND,
                SimpleHttpResponse.ContentType.TEXTPLAIN,
                null,
                0
        );
        return response;
    }
}

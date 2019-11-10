import application.services.ApplicationConfiguration;
import application.services.LoggingService;
import application.usecases.BasicWebServer;
import infrastructure.application_configuration.FileApplicationConfiguration;
import infrastructure.logging.PrintLogger;
import infrastructure.request_handlers.FileSystemRequestHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.Executors;


public class Main {
    public static void main(String[] args) {
        try {
            LoggingService.getInstance().init(new PrintLogger());
            ApplicationConfiguration.getInstance().init(new FileApplicationConfiguration());
            BasicWebServer basicWebServer = new BasicWebServer(Executors.newCachedThreadPool(), FileSystemRequestHandler.class);
            basicWebServer.run();
        } catch (Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String error = sw.toString();
            System.out.println(error);
        }
    }
}

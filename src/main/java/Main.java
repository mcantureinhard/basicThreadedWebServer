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
            //Use PrintLogger
            LoggingService.getInstance().init(new PrintLogger());
            //Read configuration from file
            ApplicationConfiguration.getInstance().init(new FileApplicationConfiguration());
            BasicWebServer basicWebServer = new BasicWebServer(Executors.newCachedThreadPool(), FileSystemRequestHandler.class);
            basicWebServer.run();
        } catch (Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String error = sw.toString();
            //TODO if we are here due to our LoggingService, we might want to log somehow else
            LoggingService.getInstance().getLogger().logExceptionMessage(error);
        }
    }
}

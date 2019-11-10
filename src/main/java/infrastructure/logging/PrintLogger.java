package infrastructure.logging;

import application.interfaces.LoggingInterface;

public class PrintLogger implements LoggingInterface {
    @Override
    public void logExceptionMessage(String message) {
        System.out.println(message);
    }
}

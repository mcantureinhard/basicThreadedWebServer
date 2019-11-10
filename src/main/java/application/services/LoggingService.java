package application.services;

import application.interfaces.LoggingInterface;

public class LoggingService {
    private static LoggingService instance;
    private LoggingInterface loggingInterface;
    private LoggingService(){}

    public void init(LoggingInterface loggingInterface){
        if(this.loggingInterface == null){
            this.loggingInterface = loggingInterface;
        }
    }

    public static synchronized LoggingService getInstance(){
        if(instance == null){
            instance = new LoggingService();
        }
        return instance;
    }

    public LoggingInterface getLogger(){
        if(this.loggingInterface == null){
            return null;
        }
        return this.loggingInterface;
    }
}

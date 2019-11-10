package application;

import application.interfaces.ApplicationConfigurationInterface;
import application.services.LoggingService;

public class ApplicationConfiguration {
    private static ApplicationConfiguration instance;
    private ApplicationConfigurationInterface applicationConfigurationInterface;
    private ApplicationConfiguration(){}

    public void init(ApplicationConfigurationInterface applicationConfigurationInterface){
        if(this.applicationConfigurationInterface == null){
            this.applicationConfigurationInterface = applicationConfigurationInterface;
        }
    }

    public static synchronized ApplicationConfiguration getInstance(){
        if(instance == null){
            instance = new ApplicationConfiguration();
        }
        return instance;
    }

    public String get(String key) throws Exception {
        if(this.applicationConfigurationInterface == null){
            throw new Exception("Missing application configuration interface");      }
        return this.applicationConfigurationInterface.get(key);
    }
}

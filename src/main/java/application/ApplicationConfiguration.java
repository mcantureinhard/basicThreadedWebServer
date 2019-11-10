package application;

import application.services.ApplicationConfigurationInterface;

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

    public String get(String key){
        if(this.applicationConfigurationInterface == null){
            System.out.println("Missing application configuration interface");
            return null;
        }
        return this.applicationConfigurationInterface.get(key);
    }
}

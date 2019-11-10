package infrastructure.application_configuration;

import application.services.ApplicationConfigurationInterface;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

public class FileApplicationConfiguration implements ApplicationConfigurationInterface {
    HashMap<String,String> keyValueMap;
    public FileApplicationConfiguration() throws Exception{
        keyValueMap = new HashMap<>();
        InputStream inputStream = FileApplicationConfiguration.class.getResourceAsStream("/config.properties");
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        while((line = bufferedReader.readLine()) != null){
            //Basic comment handling
            //TODO use library for this
            if(line.indexOf("//") == 0)
                continue;
            StringTokenizer tokenizer = new StringTokenizer(line, "=");
            keyValueMap.put(tokenizer.nextToken(), tokenizer.nextToken());
        }
    }
    @Override
    public String get(String key) {
        return keyValueMap.getOrDefault(key, null);
    }
}

package domain.models;

import application.services.ApplicationConfiguration;

import java.io.BufferedReader;
import java.util.StringTokenizer;

public class SimpleHttpRequest {
    public enum Method{
        GET, POST, DELETE, PUT
    }

    private final Method method;
    private final String path;
    private final String query;
    private final Boolean keepAlive;
    private final String httpVersion;

    public static SimpleHttpRequest fromBufferedReader(BufferedReader reader) throws Exception{
        String line = reader.readLine();
        if("true".equals(ApplicationConfiguration.getInstance().get("verbose")))
            System.out.println("Method and path:" + line);
        if(line == null)
            return null;
        StringTokenizer stringTokenizer = new StringTokenizer(line);
        Method method = Method.valueOf(stringTokenizer.nextToken().toUpperCase());
        StringTokenizer pathQuery = new StringTokenizer(stringTokenizer.nextToken(), "?");
        String path = pathQuery.nextToken();
        String query = null;
        if(pathQuery.hasMoreTokens()){
            query = pathQuery.nextToken();
        }
        String httpVersion = stringTokenizer.nextToken();
        Boolean keepAlive = false;
        if(!"HTTP/1.1".equals(httpVersion)) {
            keepAlive = true;
        }
        while (!(line = reader.readLine()).equals("")){
            if("true".equals(ApplicationConfiguration.getInstance().get("verbose")))
                System.out.println(line);
            StringTokenizer tokenizer = new StringTokenizer(line, ":");
            // Probably I also need to check for HTTP 1.1
            if(tokenizer.nextToken().equals("Connection")){
                String value = tokenizer.nextToken().trim();
                if("keep-alive".equals(value)){
                    keepAlive = true;
                } else if("close".equals(value)){
                    keepAlive = false;
                }
            }
        }
        return new SimpleHttpRequest(
                method,
                path,
                query,
                httpVersion,
                keepAlive
        );
    }

    public SimpleHttpRequest(Method method, String path, String query, String httpVersion) {
        this.method = method;
        this.path = path;
        this.query = query;
        this.httpVersion = httpVersion;
        this.keepAlive = false;
    }

    public SimpleHttpRequest(Method method, String path, String query, String httpVersion, Boolean keepAlive) {
        this.method = method;
        this.path = path;
        this.query = query;
        this.httpVersion = httpVersion;
        this.keepAlive = keepAlive;
    }

    public Method getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getQuery() {
        return query;
    }

    public Boolean getKeepAlive() {
        return keepAlive;
    }
}

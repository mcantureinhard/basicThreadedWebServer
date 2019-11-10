package domain.models;

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

    public static SimpleHttpRequest fromBufferedReader(BufferedReader reader) throws Exception{
        String line = reader.readLine();
        StringTokenizer stringTokenizer = new StringTokenizer(line);
        Method method = Method.valueOf(stringTokenizer.nextToken().toUpperCase());
        StringTokenizer pathQuery = new StringTokenizer(stringTokenizer.nextToken(), "?");
        String path = pathQuery.nextToken();
        String query = null;
        if(pathQuery.hasMoreTokens()){
            query = pathQuery.nextToken();
        }
        while (!(line = reader.readLine()).equals("")){
            System.out.println(line);
        }
        return new SimpleHttpRequest(
                method,
                path,
                query,
                false
        );
    }

    public SimpleHttpRequest(Method method, String path, String query) {
        this.method = method;
        this.path = path;
        this.query = query;
        this.keepAlive = false;
    }

    public SimpleHttpRequest(Method method, String path, String query, Boolean keepAlive) {
        this.method = method;
        this.path = path;
        this.query = query;
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

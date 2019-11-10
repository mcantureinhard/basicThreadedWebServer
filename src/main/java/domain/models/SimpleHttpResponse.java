package domain.models;

import java.io.BufferedInputStream;
import java.time.LocalDateTime;
import java.lang.Enum;

public class SimpleHttpResponse {
    public enum ResponseCode {
        OK("OK", 200),
        NOTFOUND ("Not Found", 404),
        ERROR("Internal Server Error", 500),
        NOTIMPLEMENTED("Not Implemented", 501);
        public final String message;
        public final int code;
        private ResponseCode(String message, int code){
            this.message = message;
            this.code = code;
        }
    }

    public enum ContentType {
        TEXTHTML {
            public String toString(){
                return "text/html";
            }
        },
        TEXTPLAIN {
            public String toString(){ return "text/plain"; }
        }
    }
    private final ResponseCode responseCode;
    private final ContentType contentType;
    private final String server;
    private final String file;
    private final int fileSize;

    public SimpleHttpResponse(ResponseCode responseCode, ContentType contentType, String file, int fileSize) {
        this.server = "Basic Web Server";
        this.responseCode = responseCode;
        this.contentType = contentType;
        this.file = file;
        this.fileSize = fileSize;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public String getServer() {
        return server;
    }

    public String getFile() {
        return file;
    }

    public int getFileSize() {
        return fileSize;
    }
}

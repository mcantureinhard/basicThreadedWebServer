package domain.models;

import java.time.LocalDateTime;

public class SimpleHttpResponse {
    enum ContentType {
        TEXTHTML {
            public String toString(){
                return "text/html";
            }
        }
    }
    private final Integer resposeCode;
    private final ContentType contentType;
    private final LocalDateTime date;
    private final byte[] data;

    public SimpleHttpResponse(Integer resposeCode, ContentType contentType, byte[] data) {
        this.resposeCode = resposeCode;
        this.contentType = contentType;
        this.date = LocalDateTime.now();
        this.data = data;
    }


}

package ir.shirazservice.expert.webservice.savetoken;

import com.squareup.moshi.Json;

public class SaveTokenKeyResponse {

    @Json(name = "code")
    private int code;
    @Json(name = "message")
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

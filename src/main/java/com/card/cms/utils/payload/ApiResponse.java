package com.card.cms.utils.payload;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse implements Serializable {
    boolean success;
    String message;

    public ApiResponse() {
        this.success = true;
    }

    public ApiResponse(boolean success) {
        this.success = success;
    }

    public  ApiResponse (boolean success, String message){
        this.success = success;
        this.message = message;
    }
}
